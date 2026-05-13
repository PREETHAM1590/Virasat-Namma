package com.example.virasat.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface

object CheckInCardRenderer {
    fun render(siteName: String, userName: String, stampIcon: String): Bitmap {
        val bitmap = Bitmap.createBitmap(1080, 1080, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.parseColor("#FFF5E6"))

        val titlePaint = Paint().apply {
            color = Color.parseColor("#5D4037")
            textSize = 64f
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }
        val subtitlePaint = Paint().apply {
            color = Color.parseColor("#8D6E63")
            textSize = 48f
            isAntiAlias = true
        }
        val stampPaint = Paint().apply {
            color = Color.parseColor("#FF6F00")
            textSize = 200f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        canvas.drawText("\u2713", 540f, 450f, stampPaint)
        canvas.drawText(siteName, 80f, 650f, titlePaint)
        canvas.drawText("Checked in by $userName", 80f, 750f, subtitlePaint)
        canvas.drawText("Virasat Heritage Explorer", 80f, 950f, subtitlePaint)

        return bitmap
    }
}
