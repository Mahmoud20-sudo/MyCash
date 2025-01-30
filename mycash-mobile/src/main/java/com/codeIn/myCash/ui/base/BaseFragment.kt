package com.codeIn.myCash.ui.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.codeIn.common.base.MyCashBaseActivity
import com.codeIn.common.offline.Constants
import com.codeIn.myCash.R
import com.codeIn.myCash.ui.home.MainActivity
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.bottom_sheets.WarningBottomSheet
import com.google.android.material.snackbar.Snackbar


abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) : Fragment() {

    private var mainActivityViewModel: Boolean = false

    private var _binding: VB? = null
    val binding: VB get() = _binding as VB

    private lateinit var snackBar: Snackbar
    private var warningBottomSheet: WarningBottomSheet? = null

    private val baseViewModel by lazy { (requireActivity() as MainActivity).viewModel }

    internal open val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isEnabled) {
                isEnabled = false
                requireActivity().finish()
            }
        }
    }

    override fun onResume() {
        (requireActivity() as MyCashBaseActivity).setOnBackPressedCallback(onBackPressedCallback)
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        onBackPressedCallback.remove()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bindingInflater.invoke(inflater)
        return binding.root
    }

    // we have new feature and this bottom sheet is canceled
    private fun warningExpireDialog() {
        warningBottomSheet?.dismiss()
        warningBottomSheet = WarningBottomSheet(
            title = getString(R.string.terminatedAccount),
            message = getString(R.string.alertPackage)+"\n"+getString(R.string.alarmExpire),
            confirmText = getString(R.string.renew_subscription),
            cancelText = getString(R.string.sign_out) ,
            cancelable = false ,
            communicator = object : WarningBottomSheet.Communicator {
                override fun confirm() {
                    navigateToSubscriptionHost()
                    warningBottomSheet?.dismiss()
                }
                override fun cancel() {
                    baseViewModel.currentShift()
                }
            }
        )
        warningBottomSheet?.show(childFragmentManager, WarningBottomSheet.TAG)
    }

    fun navigateToSubscriptionHost() {
        val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.fade_in_with_scale)
            .setExitAnim(R.anim.fade_out)
            .setPopEnterAnim(R.anim.fade_in)
            .setPopExitAnim(R.anim.fade_out)
            .build()
        ((requireActivity() as MainActivity).navController).navigate(
            R.id.navigation_subscriptionHostFragment,
            null,
            navOptions
        )
    }


    fun showMessage(message: String, useToast: Boolean) =
        if (useToast) showMessage(message) else showSnackBar(message)

    fun onLogout(){
        val intent = Intent(activity , IntroActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showSnackBar(message: String) {
        val snackBar =
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_LONG
            )
        val sbView = snackBar.view

        val textView = sbView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        snackBar.show()
    }

    private fun showShortSnackBar(message: String) {
        snackBar = Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        )
        if (mainActivityViewModel) { // mainActivityViewModel.isBottomBarVisible
            val snackBarView = snackBar.view
            snackBarView.translationY = -(400 / requireContext().resources.displayMetrics.density)
        }
        snackBar.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}