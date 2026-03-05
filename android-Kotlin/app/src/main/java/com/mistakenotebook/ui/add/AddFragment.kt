package com.mistakenotebook.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mistakenotebook.R
import com.mistakenotebook.data.model.Question

class AddFragment : Fragment() {

    private val vm: AddViewModel by viewModels()
    private var selectedAnswer = "A"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View =
        inflater.inflate(R.layout.fragment_add, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabs(view)
        setupSingleForm(view)
        setupBatchForm(view)
    }

    private fun setupTabs(v: View) {
        val tabSingle = v.findViewById<Button>(R.id.tab_single)
        val tabBatch  = v.findViewById<Button>(R.id.tab_batch)
        tabSingle.setOnClickListener { switchTab(v, true) }
        tabBatch.setOnClickListener  { switchTab(v, false) }
        switchTab(v, true)
    }

    private fun switchTab(v: View, single: Boolean) {
        v.findViewById<View>(R.id.card_single).isVisible = single
        v.findViewById<View>(R.id.card_batch).isVisible  = !single
        val colorActive   = requireContext().getColor(R.color.success)
        val colorInactive = requireContext().getColor(android.R.color.transparent)
        val textActive    = requireContext().getColor(R.color.success)
        val textInactive  = requireContext().getColor(R.color.text_muted)
        v.findViewById<Button>(R.id.tab_single).apply {
            setBackgroundColor(if (single) colorActive else colorInactive)
            setTextColor(if (single) requireContext().getColor(R.color.white) else textInactive)
        }
        v.findViewById<Button>(R.id.tab_batch).apply {
            setBackgroundColor(if (!single) colorActive else colorInactive)
            setTextColor(if (!single) requireContext().getColor(R.color.white) else textInactive)
        }
    }

    private fun setupSingleForm(v: View) {
        val etContent = v.findViewById<EditText>(R.id.et_content)
        val etA = v.findViewById<EditText>(R.id.et_option_a)
        val etB = v.findViewById<EditText>(R.id.et_option_b)
        val etC = v.findViewById<EditText>(R.id.et_option_c)
        val etD = v.findViewById<EditText>(R.id.et_option_d)
        val answerBtns = mapOf(
            "A" to v.findViewById<Button>(R.id.btn_a),
            "B" to v.findViewById(R.id.btn_b),
            "C" to v.findViewById(R.id.btn_c),
            "D" to v.findViewById(R.id.btn_d)
        )
        answerBtns.forEach { (key, btn) -> btn.setOnClickListener { selectAnswer(answerBtns, key) } }
        selectAnswer(answerBtns, "A")

        v.findViewById<Button>(R.id.btn_save).setOnClickListener {
            val content = etContent.text.toString().trim()
            val optA    = etA.text.toString().trim()
            val optB    = etB.text.toString().trim()
            val optC    = etC.text.toString().trim()
            val optD    = etD.text.toString().trim()
            if (content.isEmpty() || optA.isEmpty() || optB.isEmpty() || optC.isEmpty() || optD.isEmpty()) {
                Toast.makeText(requireContext(), "请填写题目和全部选项", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            vm.save(Question(content=content, optionA=optA, optionB=optB,
                optionC=optC, optionD=optD, answer=selectedAnswer)) {
                Toast.makeText(requireContext(), "保存成功！", Toast.LENGTH_SHORT).show()
                clearSingleForm(etContent, etA, etB, etC, etD, answerBtns)
            }
        }
        v.findViewById<Button>(R.id.btn_clear).setOnClickListener {
            clearSingleForm(etContent, etA, etB, etC, etD, answerBtns)
        }
    }

    private fun selectAnswer(btns: Map<String, Button>, key: String) {
        selectedAnswer = key
        val active   = requireContext().getColor(R.color.success)
        val inactive = requireContext().getColor(R.color.btn_option)
        val ta = requireContext().getColor(R.color.white)
        val ti = requireContext().getColor(R.color.text_muted)
        btns.forEach { (k, b) ->
            b.setBackgroundColor(if (k == key) active else inactive)
            b.setTextColor(if (k == key) ta else ti)
        }
    }

    private fun clearSingleForm(etContent: EditText, etA: EditText, etB: EditText,
                                etC: EditText, etD: EditText, btns: Map<String, Button>) {
        etContent.text.clear(); etA.text.clear(); etB.text.clear()
        etC.text.clear(); etD.text.clear()
        selectAnswer(btns, "A")
    }

    private fun setupBatchForm(v: View) {
        v.findViewById<Button>(R.id.btn_batch_import).setOnClickListener {
            val raw = v.findViewById<EditText>(R.id.et_batch).text.toString().trim()
            if (raw.isEmpty()) {
                Toast.makeText(requireContext(), "内容为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            vm.importBatch(raw) { count, _ ->
                Toast.makeText(requireContext(), "成功导入 $count 道题", Toast.LENGTH_SHORT).show()
                v.findViewById<EditText>(R.id.et_batch).text.clear()
            }
        }
    }
}
