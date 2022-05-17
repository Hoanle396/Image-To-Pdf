package com.example.pdf.helper


/**
 * Created by maanav on 25/3/18.
 */
class OtsuThresholder {
    val histData: IntArray
    var maxLevelValue = 0
        private set
    var threshold = 0
        private set

    fun doThreshold(srcData: ByteArray): Int {
        var ptr: Int

        // Clear histogram data
        // Set all values to zero
        ptr = 0
        while (ptr < histData.size) histData[ptr++] = 0

        // Calculate histogram and find the level with the max value
        // Note: the max level value isn't required by the Otsu method
        ptr = 0
        maxLevelValue = 0
        while (ptr < srcData.size) {
            val h = 0xFF and srcData[ptr].toInt()
            histData[h]++
            if (histData[h] > maxLevelValue) maxLevelValue = histData[h]
            ptr++
        }

        // Total number of pixels
        val total = srcData.size
        var sum = 0f
        for (t in 0..255) sum += (t * histData[t]).toFloat()
        var sumB = 0f
        var wB = 0
        var wF = 0
        var varMax = 0f
        threshold = 0
        for (t in 0..255) {
            wB += histData[t] // Weight Background
            if (wB == 0) continue
            wF = total - wB // Weight Foreground
            if (wF == 0) break
            sumB += (t * histData[t]).toFloat()
            val mB = sumB / wB // Mean Background
            val mF = (sum - sumB) / wF // Mean Foreground

            // Calculate Between Class Variance
            val varBetween = wB.toFloat() * wF.toFloat() * (mB - mF) * (mB - mF)

            // Check if new maximum found
            if (varBetween > varMax) {
                varMax = varBetween
                threshold = t
            }
        }
        return threshold
    }

    init {
        histData = IntArray(256)
    }
}
