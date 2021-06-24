package com.aiden.unscrambleappwithviewmodel.ui

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aiden.unscrambleappwithviewmodel.data.WordViewModel
import com.aiden.unscrambleappwithviewmodel.databinding.GameFragmentBinding
import com.aiden.unscrambleappwithviewmodel.model.MAX_NO_OF_WORDS
import com.aiden.unscrambleappwithviewmodel.util.CountDownTimerExt
import kotlin.system.exitProcess

class GameFragment : Fragment() {


    private val args: GameFragmentArgs by navArgs()

    private var _binding: GameFragmentBinding? = null
    private val nextQuestionTimer = Timer(30000L, 1000L)

    private val binding get() = _binding!!

    private var isItNotFirstTime: Boolean = false

    private var hasNextWord: Boolean = false

    private var isAnswerGiven: Boolean = false

    private  var wordCount:Int = 0

    private var colorRed: Int = 0
    private var colorGreen: Int = 255
    private var colorBlue: Int = 0



    private val wordsViewModel by viewModels<WordViewModel>()

    companion object {
        private const val TAG = "GameFragment"

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = GameFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wordsViewModel.getWords(args.difficulty)
        wordsViewModel.score.observe(viewLifecycleOwner) {
            binding.score.text = "Score: $it"
        }

        wordsViewModel.wordCount.observe(viewLifecycleOwner) {
            binding.wordCount.text = "Word count: $it"
        }


        binding.skip.setOnClickListener { updateNextWordOnScreen() }
        binding.submit.setOnClickListener { onSubmitButton() }
        binding.hintText.visibility = View.GONE

        wordsViewModel.currentWord.observe(viewLifecycleOwner) {
            binding.hintText.text = it
        }


        binding.imageButton.setOnClickListener {
            it.visibility = View.GONE
            binding.hintText.visibility = View.VISIBLE
            binding.textViewInstructions.visibility = View.INVISIBLE
            hintTimer.start()
        }

        wordsViewModel.words.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                wordsViewModel.nextWord()
            }
        }

        wordsViewModel.currentScrambledWord.observe(viewLifecycleOwner) {
            Log.d(TAG, "onViewCreated: ")
            if (it.equals("test")) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                if (!isItNotFirstTime) {
                    isItNotFirstTime = true
                }
                binding.textViewUnscrambledWord.text = it
                binding.progressBar.visibility = View.GONE

            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        nextQuestionTimer.pause()
    }

    override fun onResume() {

        this.colorRed = wordsViewModel.viewModelColorRed
        this.colorGreen = wordsViewModel.viewModelColorGreen
        this.colorBlue = wordsViewModel.viewModelColorBlue

        super.onResume()
        if (!isItNotFirstTime) {
            nextQuestionTimer.start()
        } else nextQuestionTimer.onResume(viewLifecycleOwner)

        val backButtonCallback = OnBackPressed(true) {
            // callback
            val alertDialog = AlertDialog.Builder(requireContext())
                .setMessage("Do you really want to leave this game? Your progress will be lost")
                .setTitle("Alert")
                .setPositiveButton("NO") { dialog, _ ->
                    dialog.dismiss()
                }
                .setNegativeButton("YES") { _, _ ->
                    // handle
                    findNavController().popBackStack()
                }.create()

            try {
                alertDialog.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(backButtonCallback)
    }

    private fun onSubmitButton() {
        val guessedWord = binding.textInputEditText.text.toString()
        if (!isAnswerGiven) {
            if (wordsViewModel.isUserWordCorrect(guessedWord)) {
                isAnswerGiven = true
                updateNextWordOnScreen()
            } else binding.textField.error = "Please enter correct word"

        } else binding.textField.error = "Already Answered"
    }


    private fun showDialog() {
        nextQuestionTimer.pause()
        val dialog = AlertDialog.Builder(requireActivity())
            .setTitle("Your Score: ${wordsViewModel.score.value}")
            .setMessage("Press play to play again and exit to exit the game.")
            .setCancelable(false)
            .setPositiveButton("Play") { dialog, di ->
                nextQuestionTimer.start()
                isItNotFirstTime = false
                wordsViewModel.reset()
                binding.textViewUnscrambledWord.text = ""
                wordsViewModel.getWords(args.difficulty)
                dialog.cancel()
            }.setNegativeButton("Exit") { _, di ->
                requireActivity().finish()
                exitProcess(0)
            }.create()

        dialog.show()

    }

    private fun updateNextWordOnScreen() {
        binding.textInputEditText.setText("")
        binding.textField.error = null
        hasNextWord = wordsViewModel.nextWord()
        setColorToGreen()

        if (!hasNextWord) {
            showDialog()
        }
        if (hasNextWord) {
            isAnswerGiven = false
            Log.d(TAG, "updateNextWordOnScreen: hasNextWord $hasNextWord")
            nextQuestionTimer.restart()
        }
        binding.hintText.visibility = View.GONE
        binding.imageButton.visibility = View.VISIBLE
        binding.textViewInstructions.visibility = View.VISIBLE
    }

    private val hintTimer = object : CountDownTimer(5000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
        }

        override fun onFinish() {
            binding.hintText.visibility = View.GONE
            binding.imageButton.visibility = View.VISIBLE
            binding.textViewInstructions.visibility = View.VISIBLE
        }
    }


    inner class Timer(
        milinFuture: Long,
        interval: Long
    ) : CountDownTimerExt(milinFuture, interval) {
        override fun onTimerTick(millisUntilFinished: Long) {

            var counter = 30 - (millisUntilFinished / 1000).toInt()
            if (counter < 16) {
                colorRed += 17
            }
            if (counter > 15) {
                colorGreen -= 17
            }
            wordsViewModel.viewModelColorRed = colorRed
            wordsViewModel.viewModelColorGreen = colorGreen
            wordsViewModel.viewModelColorGreen = colorBlue

            binding.progressBar2.progress = (100 - (millisUntilFinished / 300).toInt())
            binding.progressBar2.progressTintList = ColorStateList.valueOf(Color.rgb(colorRed, colorGreen, colorBlue))
            binding.progressBar2.isEnabled = true
        }

        override fun onTimerFinish() {
                updateNextWordOnScreen()
                setColorToGreen()
                Log.d(TAG, "onTimerFinish: ")
        }
    }

    private fun setColorToGreen() {
        colorRed = 0
        colorGreen = 255
        colorBlue = 0
    }

}


fun OnBackPressed(enabled: Boolean, callback: () -> Unit) =
    object : OnBackPressedCallback(enabled) {
        override fun handleOnBackPressed() {
            callback()
        }
    }




