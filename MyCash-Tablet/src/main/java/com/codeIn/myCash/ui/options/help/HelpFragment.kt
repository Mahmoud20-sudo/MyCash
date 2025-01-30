package com.codeIn.myCash.ui.options.help

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codeIn.help.data.model.response.HowWorkDTO
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentHelpBinding
import com.codeIn.myCash.utilities.dialogs.ContactUsDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HelpFragment : Fragment()  {

    private var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HelpViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = HowWorkAdapter(howWorkCommunicator)
        binding.includeHelpQuide.helpRecyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dataHowWorkResult.collect { helpList ->
                adapter.submitList(helpList)
            }
        }
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.mycashGuideTextView.setOnClickListener {
            binding.includeHelpQuide.root.visibility =View.VISIBLE
            binding.includeHelpQuide.backArrow.setOnClickListener {
                binding.includeHelpQuide.root.visibility =View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dataResult.collect { contactInfoList ->
                val contactInfo = contactInfoList.firstOrNull()
                binding.contactUsTextView.setOnClickListener {
                    ContactUsDialog().show(
                        requireContext(),
                        "qr",
                        contactInfo?.data?.whatsapp.toString()
                    )
                }
                binding.privacyPolicyTextView.setOnClickListener {
                    binding.includeHelpPolicy.root.visibility =View.VISIBLE
                    binding.includeHelpPolicy.titleTextView.text =  getString(R.string.privacy_policy)
                    binding.includeHelpPolicy.descriptionTv.text = contactInfo?.data?.privacy?:""
                    binding.includeHelpPolicy.backArrow.setOnClickListener {
                        binding.includeHelpPolicy.root.visibility =View.GONE
                    }
                }
                binding.paymentAndRefundPolicyTextView.setOnClickListener {
                    binding.includeHelpPolicy.root.visibility =View.VISIBLE
                    binding.includeHelpPolicy.titleTextView.text = getString(R.string.payment_and_refund_policy)
                    binding.includeHelpPolicy.descriptionTv.text = contactInfo?.data?.payment?:""
                    binding.includeHelpPolicy.backArrow.setOnClickListener {
                        binding.includeHelpPolicy.root.visibility =View.GONE
                    }
                }
                binding.termsAndConditionsTextView.setOnClickListener {
                    binding.includeHelpPolicy.root.visibility =View.VISIBLE
                    binding.includeHelpPolicy.titleTextView.text = getString(R.string.terms_and_conditions)
                    binding.includeHelpPolicy.descriptionTv.text =   contactInfo?.data?.terms?:""
                    binding.includeHelpPolicy.backArrow.setOnClickListener {
                        binding.includeHelpPolicy.root.visibility =View.GONE
                    }

                }
            }
        }
    }
    private val howWorkCommunicator = object : HowWorkAdapter.Communicator {
        override fun onCardClick(item: HowWorkDTO.Data.Data) {
            val videoLink = item.link
            if (!videoLink.isNullOrEmpty()) {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoLink))
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoLink))
                    startActivity(intent)
                }
            }
        }
    }
}