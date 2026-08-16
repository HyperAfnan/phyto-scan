package botany.garden.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import botany.garden.data.model.Plant
import botany.garden.data.qr.QrScanner
import botany.garden.data.repository.PlantRepository
import botany.garden.ui.theme.Ink
import botany.garden.ui.theme.Paper
import botany.garden.ui.theme.Paper92Alpha
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.Executors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ScanScreen(
    repository: PlantRepository,
    onPlantFound: (Plant) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val qrScanner = remember { QrScanner() }
    val executor = remember { Executors.newSingleThreadExecutor() }
    val scope = rememberCoroutineScope()
    val busy = remember { AtomicBoolean(false) }
    val lastScan = remember { AtomicLong(0L) }
    val found = remember { AtomicBoolean(false) }
    var camera by remember { mutableStateOf<Camera?>(null) }
    var isTorchOn by remember { mutableStateOf(false) }
    var hasPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }
    var status by remember { mutableStateOf("Point camera at a plant QR code") }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        hasPermission = it
    }

    DisposableEffect(Unit) {
        onDispose {
            executor.shutdown()
        }
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
                Text("Camera access is needed to scan a plant QR code")
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
                        analysis.setAnalyzer(executor) { imageProxy ->
                            val now = System.currentTimeMillis()
                            if (found.get() || now - lastScan.get() < 250L || !busy.compareAndSet(false, true)) {
                                imageProxy.close()
                                return@setAnalyzer
                            }
                            lastScan.set(now)
                            val scannedText = try {
                                qrScanner.scan(imageProxy)
                            } finally {
                                imageProxy.close()
                            }

                            if (!scannedText.isNullOrBlank()) {
                                scope.launch {
                                    try {
                                        val match = withContext(Dispatchers.Default) {
                                            repository.findPlantByQrCode(scannedText)
                                        }
                                        if (match != null && found.compareAndSet(false, true)) {
                                            status = "Plant found: ${match.commonNames.firstOrNull() ?: match.botanicalName}"
                                            onPlantFound(match)
                                        }
                                    } finally {
                                        busy.set(false)
                                    }
                                }
                            } else {
                                busy.set(false)
                            }
                        }
                        provider.unbindAll()
                        val boundCamera = provider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, analysis)
                        camera = boundCamera
                        boundCamera.cameraControl.enableTorch(isTorchOn)
                    }, ContextCompat.getMainExecutor(viewContext))
                    previewView
                },
            )

            // QR Viewfinder Overlay
            Canvas(modifier = Modifier.fillMaxSize()) {
                val boxSize = size.minDimension * 0.65f
                val left = (size.width - boxSize) / 2f
                val top = (size.height - boxSize) / 2.3f

                // Semi-transparent dim background
                drawRect(color = Color(0x66000000))

                // Clear center scanning hole
                drawRoundRect(
                    color = Color.Transparent,
                    topLeft = Offset(left, top),
                    size = Size(boxSize, boxSize),
                    cornerRadius = CornerRadius(24.dp.toPx(), 24.dp.toPx()),
                    blendMode = BlendMode.Clear,
                )

                // Outer border accent
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.8f),
                    topLeft = Offset(left, top),
                    size = Size(boxSize, boxSize),
                    cornerRadius = CornerRadius(24.dp.toPx(), 24.dp.toPx()),
                    style = Stroke(width = 2.dp.toPx()),
                )
            }

            IconButton(
                onClick = {
                    val newState = !isTorchOn
                    isTorchOn = newState
                    camera?.cameraControl?.enableTorch(newState)
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(16.dp)
                    .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Paper92Alpha),
            ) {
                Icon(
                    imageVector = if (isTorchOn) Icons.Filled.FlashOn else Icons.Filled.FlashOff,
                    contentDescription = if (isTorchOn) "Turn torch off" else "Turn torch on",
                    tint = Ink,
                    modifier = Modifier.size(20.dp),
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 90.dp, start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(status, color = Color.White)
            }
        }
    }
}
