package com.codeIn.myCash.ui.options.subscriptions

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentProfileHostBinding
import com.codeIn.myCash.databinding.FragmentSettingsHostBinding
import com.codeIn.myCash.databinding.FragmentSubscriptionHostBinding
import com.codeIn.myCash.ui.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint

class SubscriptionHostFragment : Fragment() {

    private lateinit var controller: NavController
    private var _binding: FragmentSubscriptionHostBinding? =null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscriptionHostBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controller = Navigation.findNavController(view.findViewById(R.id.subscription_nav_host_fragment))

    }

    val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (controller.popBackStack()) {
                // Fragment popped successfully
            } else
                if (isEnabled) {
                    // No more fragments in the back stack, handle back press as normal
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
        }
    }


    override fun onResume() {
        (requireActivity() as MainActivity).setOnBackPressedCallback(onBackPressedCallback)
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        onBackPressedCallback.remove()
    }
}