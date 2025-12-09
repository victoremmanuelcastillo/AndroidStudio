fun encodeResourceToBase64(context: Context, resId: Int): String {
// Obtener bitmap original
val original = BitmapFactory.decodeResource(context.resources, resId)

        // Tamaño máximo deseado (ejemplo 1080px de ancho o alto)
        val maxSize = 1080

        // --- Calcular proporción ---
        val width = original.width
        val height = original.height

        val ratio = minOf(
            maxSize.toFloat() / width,
            maxSize.toFloat() / height
        )

        val newWidth = (width * ratio).toInt()
        val newHeight = (height * ratio).toInt()

        // Redimensionar respetando aspect ratio
        val resized = Bitmap.createScaledBitmap(original, newWidth, newHeight, true)

        // Convertir a JPEG
        val outputStream = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)

        // Convertir a Base64
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    fun decodeBase64(encodedImage: String): Bitmap {
        val decodedString = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }