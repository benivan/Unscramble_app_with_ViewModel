package com.aiden.unscrambleappwithviewmodel.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.DialogFragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.findNavController
import com.aiden.unscrambleappwithviewmodel.databinding.DifficultyFragmentBinding

class DifficultyFragment:Fragment() {

    private var _binding: DifficultyFragmentBinding? = null

    private val binding get() = _binding!!
    private var sliderValue :Int = 5


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DifficultyFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.slider.value = sliderValue.toFloat()
        binding.textView2.text = "Word with number of Letters: ${sliderValue}"

        binding.slider.addOnChangeListener { _, value, _ ->
            sliderValue = value.toInt()
            binding.textView2.text = "Word with number of Letters: ${sliderValue}"
        }

        binding.materialButton.setOnClickListener {
            val action = DifficultyFragmentDirections.actionDifficultyFragmentToGameFragment(difficulty = sliderValue)
            it.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}