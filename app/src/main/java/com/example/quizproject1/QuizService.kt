package com.example.quizproject1

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.File

class QuizService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val email = intent?.getStringExtra("EMAIL") ?: return START_NOT_STICKY
        val score = intent.getIntExtra("SCORE", 0)
        val quizId = intent.getIntExtra("QUIZ_ID", -1)

        generatePDF(email, score, quizId)

        return START_NOT_STICKY
    }

    private fun generatePDF(email: String, score: Int, quizId: Int) {
        try {
            val filePath = File(getExternalFilesDir(null), "QuizResults.pdf")
            val writer = PdfWriter(filePath)
            val pdfDocument = PdfDocument(writer)
            val document = Document(pdfDocument)

            document.add(Paragraph("Quiz Results"))
            document.add(Paragraph("Email: student1@gmail.com"))
            document.add(Paragraph("Quiz ID: $quizId"))
            document.add(Paragraph("Score: $score"))

            document.close()
            Log.d("PDFGenerationService", "PDF created at ${filePath.absolutePath}")

        } catch (e: Exception) {
            Log.e("PDFGenerationService", "Error creating PDF", e)
        }
    }
}
