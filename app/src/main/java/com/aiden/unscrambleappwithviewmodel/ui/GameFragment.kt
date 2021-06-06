package com.aiden.unscrambleappwithviewmodel.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.aiden.unscrambleappwithviewmodel.databinding.GameFragmentBinding
import kotlin.system.exitProcess

class GameFragment:Fragment() {

    private var _binding: GameFragmentBinding? = null

    private val binding get() = _binding!!

    private val viewModel : GameViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         super.onCreateView(inflater, container, savedInstanceState)
        _binding = GameFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.score.observe(viewLifecycleOwner){
            binding.score.text = "Score: $it"
        }

        viewModel.wordCount.observe(viewLifecycleOwner){
            binding.wordCount.text = "Word count: $it"
        }
        viewModel.currentScrambledWord.observe(viewLifecycleOwner) {
            binding.textViewUnscrambledWord.text = it
        }
        binding.textViewInstructions.text = "Instruction"
        binding.skip.setOnClickListener { updateNextWordOnScreen() }
        binding.submit.setOnClickListener { onSubmitButton() }
        viewModel.nextWord()
    }

    private fun onSubmitButton() {
        val guessedWord = binding.textInputEditText.text.toString()
        if(viewModel.isUserWordCorrect(guessedWord)){
            updateNextWordOnScreen()
        }else binding.textField.error = "Please enter correct word"
    }


    private fun updateNextWordOnScreen(){
        binding.textInputEditText.setText("")
        binding.textField.error = null
        if (!viewModel.nextWord()) {
            val dialog = AlertDialog.Builder(requireActivity())
                .setTitle("Your Score: ${viewModel.score.value}")
                .setMessage("Press play to play again and exit to exit the game.")
                .setCancelable(false)
                .setPositiveButton("Play") { _, di ->
                    viewModel.reset()
                    viewModel.nextWord()
                }.setNegativeButton("Exit") { _, di ->
                    requireActivity().finish()
                    exitProcess(0)
                }.create()

            dialog.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}