package makza.afonsky.searchpair.data

import makza.afonsky.searchpair.R

object CardAssets {

    private val faces = intArrayOf(
        0,
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3,
        R.drawable.image4,
        R.drawable.image5,
        R.drawable.image6,
        R.drawable.image7,
        R.drawable.image8,
        R.drawable.image9,
        R.drawable.image10,
        R.drawable.image11,
        R.drawable.image12,
        R.drawable.image13,
        R.drawable.image14,
        R.drawable.image15,
        R.drawable.image16,
        R.drawable.image17,
        R.drawable.image18,
        R.drawable.image19,
        R.drawable.image20,
    )

    val maxSymbol: Int = faces.size - 1

    fun faceDrawable(symbol: Int): Int = faces[symbol.coerceIn(1, maxSymbol)]
}
