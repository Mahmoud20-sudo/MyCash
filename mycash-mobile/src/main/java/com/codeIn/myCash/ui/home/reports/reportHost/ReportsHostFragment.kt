package com.codeIn.myCash.ui.home.reports.reportHost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentReportsHostBinding
import com.codeIn.myCash.ui.home.MainActivity


class ReportsHostFragment : Fragment() {

    private var _binding: FragmentReportsHostBinding? = null
    private val binding get() = _binding!!

    private lateinit var controller: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportsHostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controller = Navigation.findNavController(view.findViewById(R.id.reports_nav_host_fragment))
    }

    override fun onResume() {
        controller.addOnDestinationChangedListener(destinationChangedListener)
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        controller.removeOnDestinationChangedListener(destinationChangedListener)
    }


    /**
     * listener for navigation component to hide the bottom navigation bar when the user is not in the clients fragment
     */
    private val destinationChangedListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            if (destination.id != R.id.navigation_reportsFragment) {
                (requireActivity() as MainActivity).hideNavBottom()
            } else {
                (requireActivity() as MainActivity).showNavBottom()
            }
        }
}