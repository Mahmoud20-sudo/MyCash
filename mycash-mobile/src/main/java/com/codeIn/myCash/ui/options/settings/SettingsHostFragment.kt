package com.codeIn.myCash.ui.options.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.codeIn.myCash.ui.home.MainActivity
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentSettingsHostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsHostFragment : Fragment()  {

    private lateinit var controller: NavController

    private var _binding: FragmentSettingsHostBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsHostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controller = Navigation.findNavController(view.findViewById(R.id.settings_nav_host_fragment))

    }


    /**
     * callback for the back button to handle the back button press when the user is in the fragment
     * because navigation component doesn't handle the back button press when the user presses it rapidly
     * so we have to handle it manually
     * to keep navigation component working properly and always updated with the current fragment
     */
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            Log.d("TAG", "handleOnBackPressed: ")
            if (controller.popBackStack()) {
                // Fragment popped successfully
                Log.d("TAG", "Fragment popped successfully: ")
            } else
                if (isEnabled) {
                    // No more fragments in the back stack, handle back press as normal
                    Log.d("TAG", "No more fragments in the back stack, handle back press as normal: ")
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