package com.mistakenotebook.ui.add

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mistakenotebook.data.model.Question
import com.mistakenotebook.databinding.ItemQuestionBinding

class QuestionAdapter(
    private val onDelete: (Question) -> Unit,
    private val onEdit: (Question) -> Unit
) : ListAdapter<Question, QuestionAdapter.VH>(DIFF) {

    inner class VH(private val b: ItemQuestionBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(q: Question, idx: Int) {
            b.tvIndex.text = "${idx + 1}."
            b.tvContent.text = q.content.let { if (it.length > 40) it.take(40) + "…" else it }
            b.btnDelete.setOnClickListener { onDelete(q) }
            b.root.setOnClickListener { onEdit(q) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position), position)

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Question>() {
            override fun areItemsTheSame(a: Question, b: Question) = a.id == b.id
            override fun areContentsTheSame(a: Question, b: Question) = a == b
        }
    }
}
