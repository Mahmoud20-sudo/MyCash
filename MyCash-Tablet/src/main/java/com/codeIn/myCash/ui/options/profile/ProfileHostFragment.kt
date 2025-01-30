package com.codeIn.myCash.ui.options.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentProfileHostBinding
import com.codeIn.myCash.databinding.FragmentSettingsHostBinding
import com.codeIn.myCash.ui.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileHostFragment : Fragment() {

    private lateinit var controller: NavController
    private var _binding: FragmentProfileHostBinding? =null
    private val binding get() = _binding!!

    @Inject
    lateinit var prefs: SharedPrefsModule

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileHostBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controller = Navigation.findNavController(view.findViewById(R.id.profile_nav_host_fragment))

    }
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

                    (requireActivity() as MainActivity).bindDrawerData(
                        userImage = prefs.getValue(Constants.logoStore()),
                        storeName = prefs.getValue(Constants.nameStore()) ?: getString(R.string.app_name),
                        taxNumber = prefs.getValue(Constants.taxRecordStore())
                            ?: getString(R.string.unknown)
                    )

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