package com.codeIn.myCash.ui.options.branches.branch_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentBranchDetailsBinding
import com.codeIn.myCash.databinding.FragmentNewBranchBinding
import com.codeIn.myCash.ui.options.branches.branches.BranchesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BranchDetailsFragment : Fragment() {
    private var _binding: FragmentBranchDetailsBinding? = null
    private val viewModel: BranchesViewModel by viewModels()
    private val binding get() = _binding!!
    private var id: Int? = null
    private var phone: String? = null
    private var city: String? = null
    private var status: Int? = null
    private var address: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = com.codeIn.myCash.ui.options.branches.branch_details.BranchDetailsFragmentArgs.fromBundle(
            requireArguments()
        ).id
        phone = com.codeIn.myCash.ui.options.branches.branch_details.BranchDetailsFragmentArgs.fromBundle(
            requireArguments()
        ).phone
        city = com.codeIn.myCash.ui.options.branches.branch_details.BranchDetailsFragmentArgs.fromBundle(
            requireArguments()
        ).city
        status = com.codeIn.myCash.ui.options.branches.branch_details.BranchDetailsFragmentArgs.fromBundle(
            requireArguments()
        ).status
        address = com.codeIn.myCash.ui.options.branches.branch_details.BranchDetailsFragmentArgs.fromBundle(
            requireArguments()
        ).address
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBranchDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialization()
    }

    private fun initialization() {
        initListeners()
    }

    private fun initListeners() {
        binding.apply {
            userNumberTextView.text = id.toString()
            phoneNumberTextView.text = phone
            cityTextView.text = city
            if (status == 1)
                statusTextView.text = getString(R.string.activate_branch)
            else
                statusTextView.text = getString(R.string.not_activated)
//            statusTextView.text =status.toString()
            addressTextView.text= address

            deleteButton.setOnClickListener {
                viewModel.deleteAllBranches(id!!)
            }
            backArrow.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

}