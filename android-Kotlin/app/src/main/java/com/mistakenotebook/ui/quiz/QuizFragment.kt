package com.mistakenotebook.ui.quiz

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mistakenotebook.R
import com.mistakenotebook.databinding.FragmentQuizBinding
import com.mistakenotebook.ui.quiz.QuizViewModel

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private val vm: QuizViewModel by viewModels()

    private var countDownTimer: CountDownTimer? = null
    private var timeoutSeconds = 10
    private var questionCount: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSetup()
    }

    // ── 做题配置界面 ───────────────────────────────────────────────────────────

    private fun showSetup() {
        binding.layoutSetup.isVisible = true
        binding.layoutQuiz.isVisible = false

        // 题目数量选择
        val countOptions = listOf<Int?>(null, 100, 50, 20)
        val countBtns = listOf(binding.btnCountAll, binding.btnCount100, binding.btnCount50, binding.btnCount20)
        fun selectCount(opt: Int?) {
            questionCount = opt
            countOptions.forEachIndexed { i, c ->
                val active = c == opt
                countBtns[i].setBackgroundColor(ContextCompat.getColor(requireContext(), if (active) R.color.primary else R.color.btn_option))
                countBtns[i].setTextColor(ContextCompat.getColor(requireContext(), if (active) R.color.white else R.color.text_muted))
            }
        }
        countBtns.forEachIndexed { i, btn -> btn.setOnClickListener { selectCount(countOptions[i]) } }
        selectCount(null)

        // 时间选择
        val timeOptions = listOf(5, 10, 15, 20, 30, 60)
        val timeBtns = listOf(binding.btnTime5, binding.btnTime10, binding.btnTime15, binding.btnTime20, binding.btnTime30, binding.btnTime60)
        fun selectTime(sec: Int) {
            timeoutSeconds = sec
            timeOptions.forEachIndexed { i, s ->
                val active = s == sec
                timeBtns[i].setBackgroundColor(ContextCompat.getColor(requireContext(), if (active) R.color.primary else R.color.btn_option))
                timeBtns[i].setTextColor(ContextCompat.getColor(requireContext(), if (active) R.color.white else R.color.text_muted))
            }
        }
        timeBtns.forEachIndexed { i, btn -> btn.setOnClickListener { selectTime(timeOptions[i]) } }
        selectTime(10)

        binding.btnStart.setOnClickListener {
            vm.start(questionCount) {
                if (vm.totalCount == 0) {
                    Toast.makeText(requireContext(), "题库为空，请先录入题目", Toast.LENGTH_SHORT).show()
                    return@start
                }
                showQuiz()
            }
        }
    }

    // ── 做题界面 ──────────────────────────────────────────────────────────────

    private fun showQuiz() {
        binding.layoutSetup.isVisible = false
        binding.layoutQuiz.isVisible = true
        loadQuestion()
    }

    private fun loadQuestion() {
        val q = vm.currentQuestion ?: return
        val colorText = ContextCompat.getColor(requireContext(), R.color.text)
        val colorOption = ContextCompat.getColor(requireContext(), R.color.btn_option)

        binding.tvProgress.text = "第 ${vm.currentIndex + 1} / ${vm.totalCount} 题"
        binding.progressBar.progress = (vm.currentIndex.toFloat() / vm.totalCount * 100).toInt()
        binding.tvQuestion.text = q.content

        val optionBtns: List<Button> = listOf(binding.btnOptA, binding.btnOptB, binding.btnOptC, binding.btnOptD)
        val keys = listOf("A", "B", "C", "D")
        val opts = q.optionsMap()
        optionBtns.forEachIndexed { i, btn ->
            btn.text = "  ${keys[i]}.  ${opts[keys[i]] ?: ""}"
            btn.setBackgroundColor(colorOption)
            btn.setTextColor(colorText)
            btn.isEnabled = true
            btn.setOnClickListener { onAnswer(keys[i]) }
        }
        binding.tvFeedback.text = ""

        startCountdown(timeoutSeconds)
    }

    private fun startCountdown(totalSec: Int) {
        countDownTimer?.cancel()
        var remaining = totalSec
        updateCountdownDisplay(remaining, totalSec)
        countDownTimer = object : CountDownTimer(totalSec * 1000L, 1000L) {
            override fun onTick(ms: Long) {
                remaining = (ms / 1000).toInt()
                updateCountdownDisplay(remaining, totalSec)
            }
            override fun onFinish() {
                onTimeout()
            }
        }.start()
    }

    private fun updateCountdownDisplay(remaining: Int, total: Int) {
        binding.tvCountdown.text = remaining.toString()
        val ratio = remaining.toFloat() / total
        val colorRes = when {
            ratio > 0.5f -> R.color.success
            ratio > 0.2f -> R.color.warning
            else -> R.color.error
        }
        binding.tvCountdown.setTextColor(ContextCompat.getColor(requireContext(), colorRes))
    }

    private fun onTimeout() {
        if (vm.answered) return
        vm.submitAnswer(null)
        val q = vm.currentQuestion ?: return
        val colorSuccess = ContextCompat.getColor(requireContext(), R.color.success)
        val colorWhite = ContextCompat.getColor(requireContext(), R.color.white)
        listOf(binding.btnOptA, binding.btnOptB, binding.btnOptC, binding.btnOptD)
            .forEachIndexed { i, btn ->
                btn.isEnabled = false
                if (listOf("A", "B", "C", "D")[i] == q.answer) {
                    btn.setBackgroundColor(colorSuccess)
                    btn.setTextColor(colorWhite)
                }
            }
        binding.tvFeedback.text = "超时！正确答案是 ${q.answer}"
        binding.tvFeedback.setTextColor(ContextCompat.getColor(requireContext(), R.color.warning))
        binding.root.postDelayed({ nextOrFinish() }, 1500)
    }

    private fun onAnswer(selected: String) {
        if (vm.answered) return
        countDownTimer?.cancel()
        val isCorrect = vm.submitAnswer(selected)
        val q = vm.currentQuestion ?: return
        listOf(binding.btnOptA, binding.btnOptB, binding.btnOptC, binding.btnOptD)
            .forEachIndexed { i, btn ->
                btn.isEnabled = false
                val key = listOf("A", "B", "C", "D")[i]
                when {
                    key == selected && isCorrect ->
                        btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.success))
                    key == selected && !isCorrect ->
                        btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.error))
                    key == q.answer ->
                        btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.success))
                }
                if (key == selected || key == q.answer)
                    btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
        binding.tvFeedback.text = if (isCorrect) "回答正确！" else "答错了，正确答案是 ${q.answer}"
        binding.tvFeedback.setTextColor(ContextCompat.getColor(requireContext(), if (isCorrect) R.color.success else R.color.error))
        binding.root.postDelayed({ nextOrFinish() }, 1200)
    }

    private fun nextOrFinish() {
        if (vm.nextQuestion()) {
            loadQuestion()
        } else {
            findNavController().navigate(
                R.id.action_quizFragment_to_resultFragment,
                Bundle().apply {
                    putParcelable("session", vm.session)
                }
            )
        }
    }

    override fun onDestroyView() {
        countDownTimer?.cancel()
        super.onDestroyView()
        _binding = null
    }
}
