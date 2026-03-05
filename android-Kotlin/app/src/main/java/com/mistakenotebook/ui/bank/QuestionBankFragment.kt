package com.mistakenotebook.ui.bank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mistakenotebook.R
import com.mistakenotebook.data.model.Question
import com.mistakenotebook.ui.add.AddViewModel
import com.mistakenotebook.ui.add.QuestionAdapter

class QuestionBankFragment : Fragment() {

    private val vm: AddViewModel by viewModels()
    private lateinit var adapter: QuestionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View =
        inflater.inflate(R.layout.fragment_question_bank, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = QuestionAdapter(
            onDelete = { q ->
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("删除确认").setMessage("确定要删除这道题吗？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("删除") { _, _ -> vm.delete(q.id) }.show()
            },
            onEdit = { q -> showEditDialog(view, q) }
        )

        val rv = view.findViewById<RecyclerView>(R.id.rv_questions)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        rv.adapter = adapter

        vm.questions.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            view.findViewById<TextView>(R.id.tv_count).text = "共 ${list.size} 题"
            val isEmpty = list.isEmpty()
            rv.isVisible = !isEmpty
            view.findViewById<View>(R.id.layout_empty).isVisible = isEmpty
        }
    }

    private fun showEditDialog(root: View, q: Question) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_question, null)
        val etC  = dialogView.findViewById<EditText>(R.id.et_content)
        val etA  = dialogView.findViewById<EditText>(R.id.et_option_a)
        val etB  = dialogView.findViewById<EditText>(R.id.et_option_b)
        val etCC = dialogView.findViewById<EditText>(R.id.et_option_c)
        val etD  = dialogView.findViewById<EditText>(R.id.et_option_d)
        etC.setText(q.content); etA.setText(q.optionA)
        etB.setText(q.optionB); etCC.setText(q.optionC); etD.setText(q.optionD)
        var editAnswer = q.answer
        val editBtns = mapOf(
            "A" to dialogView.findViewById<Button>(R.id.btn_a),
            "B" to dialogView.findViewById(R.id.btn_b),
            "C" to dialogView.findViewById(R.id.btn_c),
            "D" to dialogView.findViewById(R.id.btn_d)
        )
        fun selBtn(key: String) {
            editAnswer = key
            val active   = requireContext().getColor(R.color.success)
            val inactive = requireContext().getColor(R.color.btn_option)
            val ta = requireContext().getColor(R.color.white)
            val ti = requireContext().getColor(R.color.text_muted)
            editBtns.forEach { (k, b) ->
                b.setBackgroundColor(if (k == key) active else inactive)
                b.setTextColor(if (k == key) ta else ti)
            }
        }
        editBtns.forEach { (k, b) -> b.setOnClickListener { selBtn(k) } }
        selBtn(q.answer)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("编辑题目").setView(dialogView)
            .setNegativeButton("取消", null)
            .setPositiveButton("保存") { _, _ ->
                vm.update(q.copy(
                    content = etC.text.toString().trim(),
                    optionA = etA.text.toString().trim(),
                    optionB = etB.text.toString().trim(),
                    optionC = etCC.text.toString().trim(),
                    optionD = etD.text.toString().trim(),
                    answer  = editAnswer
                )) { Toast.makeText(requireContext(), "修改已保存", Toast.LENGTH_SHORT).show() }
            }.show()
    }
}
