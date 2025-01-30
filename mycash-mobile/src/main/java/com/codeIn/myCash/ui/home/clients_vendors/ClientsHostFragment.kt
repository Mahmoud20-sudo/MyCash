package com.codeIn.myCash.ui.home.clients_vendors

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
import com.codeIn.myCash.databinding.FragmentClientsHostBinding


class ClientsHostFragment : Fragment() {

    private lateinit var controller: NavController


    private var _binding: FragmentClientsHostBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientsHostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controller = Navigation.findNavController(view.findViewById(R.id.clients_nav_host_fragment))

    }

    /**
     * callback for the back button to handle the back button press when the user is in the client fragment
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
        controller.addOnDestinationChangedListener(destinationChangedListener)
        (requireActivity() as MainActivity).setOnBackPressedCallback(onBackPressedCallback)

        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        controller.removeOnDestinationChangedListener(destinationChangedListener)
        onBackPressedCallback.remove()

    }

    /**
     * listener for navigation component to hide the bottom navigation bar when the user is not in the clients fragment
     */
    private val destinationChangedListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            Log.d("TAG dro", ": ${destination.label}")
            if (destination.id != R.id.navigation_clientsFragment) {
                (requireActivity() as MainActivity).hideNavBottom()
            } else {
                (requireActivity() as MainActivity).showNavBottom()
            }
        }

}