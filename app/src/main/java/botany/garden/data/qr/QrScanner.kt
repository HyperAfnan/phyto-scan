package botany.garden.data.qr

import androidx.camera.core.ImageProxy
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer

class QrScanner {
    private val reader = MultiFormatReader().apply {
        setHints(mapOf(DecodeHintType.POSSIBLE_FORMATS to listOf(BarcodeFormat.QR_CODE)))
    }

    fun scan(image: ImageProxy): String? {
        val plane = image.planes.getOrNull(0) ?: return null
        val buffer = plane.buffer
        val yBytes = ByteArray(buffer.remaining())
        buffer.get(yBytes)

        val rotation = image.imageInfo.rotationDegrees
        val width = image.width
        val height = image.height

        val (finalWidth, finalHeight, finalData) = when (rotation) {
            90 -> Triple(height, width, rotate90(yBytes, width, height))
            180 -> Triple(width, height, rotate180(yBytes))
            270 -> Triple(height, width, rotate270(yBytes, width, height))
            else -> Triple(width, height, yBytes)
        }

        val source = PlanarYUVLuminanceSource(
            finalData,
            finalWidth,
            finalHeight,
            0,
            0,
            finalWidth,
            finalHeight,
            false,
        )
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

        return try {
            val result = reader.decodeWithState(binaryBitmap)
            reader.reset()
            result?.text
        } catch (_: Exception) {
            reader.reset()
            null
        }
    }

    private fun rotate90(data: ByteArray, width: Int, height: Int): ByteArray {
        val rotated = ByteArray(data.size)
        var i = 0
        for (x in 0 until width) {
            for (y in height - 1 downTo 0) {
                rotated[i++] = data[y * width + x]
            }
        }
        return rotated
    }

    private fun rotate180(data: ByteArray): ByteArray {
        val rotated = ByteArray(data.size)
        for (i in data.indices) {
            rotated[i] = data[data.size - 1 - i]
        }
        return rotated
    }

    private fun rotate270(data: ByteArray, width: Int, height: Int): ByteArray {
        val rotated = ByteArray(data.size)
        var i = 0
        for (x in width - 1 downTo 0) {
            for (y in 0 until height) {
                rotated[i++] = data[y * width + x]
            }
        }
        return rotated
    }
}
