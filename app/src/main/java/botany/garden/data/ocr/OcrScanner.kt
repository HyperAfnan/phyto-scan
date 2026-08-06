package botany.garden.data.ocr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.util.Log
import botany.garden.data.model.Plant
import botany.garden.data.repository.PlantRepository
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File

class OcrScanner(private val context: Context) {
    private var tessApi: TessBaseAPI? = null
    @Volatile
    private var isInitialized = false

    fun init() {
        if (isInitialized) return
        val root = File(context.filesDir, "tesseract")
        val data = File(root, "tessdata/eng.traineddata")
        if (!data.exists()) {
            data.parentFile?.mkdirs()
            context.assets.open("tessdata/eng.traineddata").use { input ->
                data.outputStream().use(input::copyTo)
            }
        }
        val api = TessBaseAPI()
        check(api.init(root.path, "eng")) { "Tesseract initialization failed" }
        api.setPageSegMode(11)
        tessApi = api
        isInitialized = true
    }

    fun recognize(bitmap: Bitmap, repository: PlantRepository): Plant? {
        if (!isInitialized || tessApi == null) {
            init()
        }
        val api = tessApi ?: return null
        val variants = preprocess(bitmap)
        return try {
            variants.asSequence()
                .map { variant ->
                    api.setImage(variant)
                    val text = api.utF8Text ?: ""
                    Log.d("OcrScanner", "Recognized text: ${text.trim()}")
                    text
                }
                .mapNotNull(repository::findBestMatch)
                .firstOrNull()
        } finally {
            variants.forEach { if (!it.isRecycled) it.recycle() }
        }
    }

    fun release() {
        tessApi?.let {
            try {
                it.clear()
                it.recycle()
            } catch (e: Exception) {
                Log.e("OcrScanner", "Error recycling TessBaseAPI", e)
            }
        }
        tessApi = null
        isInitialized = false
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
}
