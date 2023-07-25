package com.example.aplikacijazaprognozuvremena.homeview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.aplikacijazaprognozuvremena.R
import com.example.aplikacijazaprognozuvremena.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    private val viewModel: HomeFragmentModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = HomeFragmentBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        return binding.root

    }




}