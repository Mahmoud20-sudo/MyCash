package com.codeIn.myCash.ui.options.help

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.codeIn.myCash.databinding.FragmentPrivacyPolicyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyPolicyFragment : Fragment() {
    private var _binding: FragmentPrivacyPolicyBinding? = null
    private val binding get() = _binding!!
    private var title: String? = null
    private var dec: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = com.codeIn.myCash.ui.options.help.PrivacyPolicyFragmentArgs.fromBundle(
            requireArguments()
        ).title
        dec = com.codeIn.myCash.ui.options.help.PrivacyPolicyFragmentArgs.fromBundle(
            requireArguments()
        ).dec

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrivacyPolicyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.titleTextView.text = title
        binding.descriptionTv.text = dec
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}