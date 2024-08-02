package com.example.quizproject1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuizListAdapter(
    private val quizList: List<Quiz>,
    private val onItemClick: (Quiz) -> Unit
) : RecyclerView.Adapter<QuizListAdapter.QuizViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quiz_list_item, parent, false)
        return QuizViewHolder(view)
    }



    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = quizList[position]
        holder.bind(quiz, onItemClick)
    }

    override fun getItemCount(): Int {
        return quizList.size
    }

    class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvQuizTitle: TextView = itemView.findViewById(R.id.tvQuizTitle)
        private val tvQuizTimer: TextView = itemView.findViewById(R.id.tvQuizTimer)

        fun bind(quiz: Quiz, onItemClick: (Quiz) -> Unit) {
            tvQuizTitle.text = quiz.title
            tvQuizTimer.text = "Timer: ${quiz.timer} seconds"
            itemView.setOnClickListener { onItemClick(quiz) }
        }
    }
}

