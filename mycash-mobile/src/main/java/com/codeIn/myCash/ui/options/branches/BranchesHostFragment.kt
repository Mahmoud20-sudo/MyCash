package com.codeIn.myCash.ui.options.branches

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.codeIn.myCash.ui.home.MainActivity
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentBranchesHostBinding


/**
 * A simple [Fragment] subclass. This fragment is a host fragment for the branches fragment.
 */
class BranchesHostFragment : Fragment() {

    private lateinit var controller: NavController

    private var _binding: FragmentBranchesHostBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBranchesHostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controller = Navigation.findNavController(view.findViewById(R.id.branches_nav_host_fragment))

    }


    /**
     * callback for the back button to handle the back button press when the user is in the branches fragment
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