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
import com.codeIn.myCash.databinding.FragmentGuideWithMyCashBinding
import com.codeIn.myCash.ui.options.help.viewmodel.HelpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class GuideWithMyCashFragment : Fragment() {
    private var _binding: FragmentGuideWithMyCashBinding? =null
    private val binding get() = _binding!!
    private val viewModel: HelpViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGuideWithMyCashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        val adapter = HowWorkAdapter(howWorkCommunicator)
        binding.helpRecyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dataHowWorkResult.collect { helpList ->
                adapter.submitList(helpList)
            }
        }
    }

    private val howWorkCommunicator = object : HowWorkAdapter.Communicator {
        override fun onCardClick(item: HowWorkDTO.Data.Data) {
            val videoLink = item.link
            if (!videoLink.isNullOrEmpty()) {
                try {
                    // Try to open the YouTube app
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoLink))
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    // YouTube app is not installed, open the link in a web browser
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoLink))
                    startActivity(intent)
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
