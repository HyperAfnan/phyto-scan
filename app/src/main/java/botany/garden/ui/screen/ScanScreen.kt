package botany.garden.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import botany.garden.data.model.Plant
import botany.garden.data.repository.PlantRepository
import botany.garden.ui.theme.Paper
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.Executors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ScanScreen(onPlantFound: (Plant) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val repository = remember { PlantRepository(context) }
    val executor = remember { Executors.newSingleThreadExecutor() }
    val scope = rememberCoroutineScope()
    val busy = remember { AtomicBoolean(false) }
    val lastScan = remember { AtomicLong(0L) }
    val found = remember { AtomicBoolean(false) }
    var hasPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }
    var status by remember { mutableStateOf("Point at a printed plant name") }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        hasPermission = it
    }

    DisposableEffect(Unit) {
        onDispose { executor.shutdown() }
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    Box(Modifier.fillMaxSize().background(Paper)) {
        if (!hasPermission) {
            Column(
                modifier = Modifier.align(Alignment.Center).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text("Camera access is needed to scan a plant name")
                Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) { Text("Allow camera") }
            }
        } else {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { viewContext ->
                    val previewView = PreviewView(viewContext)
                    val providerFuture = ProcessCameraProvider.getInstance(viewContext)
                    providerFuture.addListener({
                        val provider = providerFuture.get()
                        val preview = androidx.camera.core.Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val analysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                        analysis.setAnalyzer(executor) { image ->
                            val now = System.currentTimeMillis()
                            if (found.get() || now - lastScan.get() < 500L || !busy.compareAndSet(false, true)) {
                                image.close()
                                return@setAnalyzer
                            }
                            lastScan.set(now)
                            val bitmap = try {
                                image.toBitmap().rotate(image.imageInfo.rotationDegrees)
                            } finally {
                                image.close()
                            }
                            scope.launch {
                                try {
                                    val match = withContext(Dispatchers.Default) {
                                        recognize(context, repository, bitmap)
                                    }
                                    if (match != null && found.compareAndSet(false, true)) {
                                        status = "Plant found"
                                        onPlantFound(match)
                                    }
                                } finally {
                                    if (!bitmap.isRecycled) bitmap.recycle()
                                    busy.set(false)
                                }
                            }
                        }
                        provider.unbindAll()
                        provider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, analysis)
                    }, ContextCompat.getMainExecutor(viewContext))
                    previewView
                },
            )
            Column(
                modifier = Modifier.align(Alignment.BottomCenter).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(status, color = Color.White)
            }
        }
    }
}

private fun recognize(context: android.content.Context, repository: PlantRepository, bitmap: Bitmap): Plant? {
    val root = File(context.filesDir, "tesseract")
    val data = File(root, "tessdata/eng.traineddata")
    if (!data.exists()) {
        data.parentFile?.mkdirs()
        context.assets.open("tessdata/eng.traineddata").use { input -> data.outputStream().use(input::copyTo) }
    }
    val api = TessBaseAPI()
    return try {
        check(api.init(root.path, "eng")) { "Tesseract initialization failed" }
        api.setPageSegMode(11)
        val variants = preprocess(bitmap)
        try {
            variants.asSequence()
                .map { variant ->
                    api.setImage(variant)
                    val text = api.getUTF8Text() ?: ""
                    Log.d("ScanScreen", "Recognized text: ${text.trim()}")
                    text
                }
                .mapNotNull(repository::findBestMatch)
                .firstOrNull()
        } finally {
            variants.forEach { it.recycle() }
        }
    } finally {
        api.clear()
        api.recycle()
    }
}

private fun Bitmap.rotate(degrees: Int): Bitmap {
    if (degrees == 0) return this
    return Bitmap.createBitmap(this, 0, 0, width, height, Matrix().apply { postRotate(degrees.toFloat()) }, true)
}

private fun preprocess(source: Bitmap): List<Bitmap> {
    val left = source.width / 10
    val top = source.height / 10
    val cropped = Bitmap.createBitmap(source, left, top, source.width - left * 2, source.height - top * 2)
    val grayscale = Bitmap.createBitmap(cropped.width, cropped.height, Bitmap.Config.ARGB_8888)
    val grayscalePaint = Paint().apply {
        colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
    }
    Canvas(grayscale).drawBitmap(cropped, 0f, 0f, grayscalePaint)

    val highContrast = Bitmap.createBitmap(cropped.width, cropped.height, Bitmap.Config.ARGB_8888)
    val contrastPaint = Paint().apply {
        colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
            setSaturation(0f)
            setScale(2f, 2f, 2f, 1f)
        })
    }
    Canvas(highContrast).drawBitmap(cropped, 0f, 0f, contrastPaint)
    cropped.recycle()
    return listOf(grayscale, highContrast)
}
