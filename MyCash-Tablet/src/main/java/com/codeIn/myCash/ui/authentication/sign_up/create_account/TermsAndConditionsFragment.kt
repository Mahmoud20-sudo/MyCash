package com.codeIn.myCash.ui.authentication.sign_up.create_account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codeIn.myCash.databinding.FragmentTermsAndConditionsBinding
import com.codeIn.myCash.ui.authentication.sign_up.termsCondition.TermsAndConditionsFragmentArgs


class TermsAndConditionsFragment : Fragment()   {

    val args : TermsAndConditionsFragmentArgs by navArgs()
    private var _binding: FragmentTermsAndConditionsBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTermsAndConditionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.haveReadTermsCheckbox.isChecked = args.checked

        binding.haveReadTermsCheckbox.setOnCheckedChangeListener { _, b ->
            findNavController().previousBackStackEntry?.savedStateHandle?.set("isChecked", b)
        }


    }



}