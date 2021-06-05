package com.aiden.unscrambleappwithviewmodel.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.aiden.unscrambleappwithviewmodel.databinding.GameFragmentBinding

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
        binding.score.text = "Hello"


        viewModel.wordCount.observe(viewLifecycleOwner){
            binding.wordCount.text = "Word count: ${it.toString()}"
        }
        viewModel.currentScrambledWord.observe(viewLifecycleOwner) {
            binding.textViewUnscrambledWord.text = it
        }
        binding.textViewInstructions.text = "Instruction"
        binding.skip.setOnClickListener { updateNextWordOnScreen() }
    }


    private fun updateNextWordOnScreen(){
        viewModel.nextWord()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}