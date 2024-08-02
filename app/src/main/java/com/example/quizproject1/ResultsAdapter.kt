package com.example.quizproject1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ResultsAdapter(private val results: List<Map<String, Any>>) : RecyclerView.Adapter<ResultsAdapter.ResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_result, parent, false)
        return ResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val result = results[position]
        val quizTitle = result["quizTitle"] as? String ?: "Unknown Quiz"
        val score = result["score"] as? Int ?: 0
        holder.quizTitleTextView.text = quizTitle
        holder.scoreTextView.text = holder.itemView.context.getString(R.string.score, score)
    }

    override fun getItemCount(): Int = results.size

    class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quizTitleTextView: TextView = itemView.findViewById(R.id.quizTitleTextView)
        val scoreTextView: TextView = itemView.findViewById(R.id.scoreTextView)
    }
}
