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
        if (isTooDark(bitmap)) {
            Log.d("OcrScanner", "Image too dark for OCR, skipping")
            return null
        }
        val variants = preprocess(bitmap)
        return try {
            variants.asSequence()
                .mapNotNull { variant ->
                    api.setImage(variant)
                    val text = api.utF8Text ?: ""
                    val confidence = api.meanConfidence()
                    Log.d("OcrScanner", "Recognized text: '${text.trim()}', confidence: $confidence")
                    if (confidence >= 50 && text.isNotBlank()) text else null
                }
                .mapNotNull(repository::findBestMatch)
                .firstOrNull()
        } finally {
            variants.forEach { if (!it.isRecycled) it.recycle() }
        }
    }

    private fun isTooDark(bitmap: Bitmap): Boolean {
        var totalLuminance = 0.0
        var count = 0
        val stepX = (bitmap.width / 20).coerceAtLeast(1)
        val stepY = (bitmap.height / 20).coerceAtLeast(1)
        for (x in 0 until bitmap.width step stepX) {
            for (y in 0 until bitmap.height step stepY) {
                val pixel = bitmap.getPixel(x, y)
                val r = (pixel shr 16) and 0xFF
                val g = (pixel shr 8) and 0xFF
                val b = pixel and 0xFF
                val luminance = 0.299 * r + 0.587 * g + 0.114 * b
                totalLuminance += luminance
                count++
            }
        }
        val avg = if (count > 0) totalLuminance / count else 0.0
        return avg < 20.0
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
