package com.mistakenotebook.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mistakenotebook.R
import com.mistakenotebook.data.model.QuizSession
import com.mistakenotebook.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 兼容 API 33+ 的 getParcelable 调用方式
        @Suppress("DEPRECATION")
        val session: QuizSession = arguments?.getParcelable("session") ?: QuizSession()
        showResult(session)

        binding.btnRetry.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_quizFragment)
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_addFragment)
        }
    }

    private fun showResult(session: QuizSession) {
        val acc = session.accuracy
        binding.tvScore.text = "%.0f%%".format(acc)
        val scoreColor = when {
            acc >= 80 -> R.color.success
            acc >= 60 -> R.color.warning
            else -> R.color.error
        }
        binding.tvScore.setTextColor(ContextCompat.getColor(requireContext(), scoreColor))

        // 幽默评语（对应 Python 版）
        binding.tvComment.text = when {
            acc < 70 -> "啊这……错题本都哭了。没事，失败是成功他妈，下次稳住！"
            acc < 80 -> "嗯，还行吧。没有垫底的尴尬，也没优等生的压力。再稳一点就更好了"
            acc < 90 -> "差一口气！90分就在前面招手，加把劲！优秀不等人"
            else     -> "随便做做，没想到……错题本遇到你算找对主人了。天才不能浪费天赋"
        }

        binding.tvTotal.text   = session.total.toString()
        binding.tvCorrect.text = session.correct.toString()
        binding.tvWrong.text   = session.wrong.toString()
        binding.tvTimeout.text = session.timeoutCount.toString()

        binding.detailContainer.removeAllViews()
        val wrongRecords = session.records.filter { !it.isCorrect }

        if (wrongRecords.isEmpty()) {
            binding.detailContainer.addView(TextView(requireContext()).apply {
                text = "全部答对，太厉害了！"
                setTextColor(ContextCompat.getColor(requireContext(), R.color.success))
                textSize = 15f
                setPadding(0, 24, 0, 24)
            })
            return
        }

        wrongRecords.forEachIndexed { i, rec ->
            val statusIcon = if (rec.isTimeout) "【超时】" else "【错误】"
            val card = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(24, 16, 24, 16)
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.surface))
            }
            card.addView(TextView(requireContext()).apply {
                text = "${i + 1}. $statusIcon ${rec.questionContent}"
                setTextColor(ContextCompat.getColor(requireContext(), R.color.text))
                textSize = 13f
            })
            listOf("A", "B", "C", "D").forEach { key ->
                val optText = rec.options[key] ?: return@forEach
                val isCorrectOpt  = key == rec.correctAnswer
                val isUserWrong   = key == rec.userAnswer && !rec.isTimeout
                val prefix = when { isCorrectOpt -> "V" ; isUserWrong -> "X" ; else -> " " }
                val colorRes = when { isCorrectOpt -> R.color.success ; isUserWrong -> R.color.error ; else -> R.color.text_muted }
                card.addView(TextView(requireContext()).apply {
                    text = "  $prefix $key. $optText"
                    setTextColor(ContextCompat.getColor(requireContext(), colorRes))
                    textSize = 13f
                })
            }
            binding.detailContainer.addView(card)
            binding.detailContainer.addView(View(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 8
                )
            })
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
