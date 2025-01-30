package com.codeIn.myCash.ui.home.products

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
import com.codeIn.myCash.databinding.FragmentProductsHostBinding


class ProductsHostFragment : Fragment() {


    private lateinit var controller: NavController

    private var _binding: FragmentProductsHostBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsHostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controller = Navigation.findNavController(view.findViewById(R.id.nav_host_fragment))

    }

    /**
     * callback for the back button to handle the back button press when the user is in the products fragment
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
     * listener for navigation controller to hide the bottom navigation bar when the user navigates to any fragment other than the products fragment
     */
    private val destinationChangedListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            if (destination.id != R.id.navigation_productsFragment) {
                (requireActivity() as MainActivity).hideNavBottom()
            } else {
                (requireActivity() as MainActivity).showNavBottom()
            }
        }


}