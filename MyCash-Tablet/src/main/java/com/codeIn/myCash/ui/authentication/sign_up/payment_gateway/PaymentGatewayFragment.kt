package com.codeIn.myCash.ui.authentication.sign_up.payment_gateway

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import com.codeIn.common.data.AuthConsts
import com.codeIn.common.offline.Constants
import com.codeIn.common.util.GeneralResponse
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentPaymentGatewayBinding
import com.codeIn.myCash.ui.home.MainActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PaymentGatewayFragment : Fragment() {

    private val viewModel: PaymentGatewayViewModel by viewModels()
    private var _binding: FragmentPaymentGatewayBinding? = null
    private val binding get() = _binding!!
    private val infoDialog: InfoDialog = InfoDialog()
    private val args: com.codeIn.myCash.ui.authentication.sign_up.payment_gateway.PaymentGatewayFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPaymentGatewayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {

            payResult.collectOnLifecycle(viewModelScope, viewLifecycleOwner) { response ->
                when (response) {
                    is GeneralResponse.Loading -> handleLoading()
                    is GeneralResponse.Idle -> handleIdle()
                    is GeneralResponse.ResponseError -> handleError(response.message ?: getString(R.string.error))
                    is GeneralResponse.Success -> handleSuccess()
                    is GeneralResponse.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else -> {}
                }
            }
        }

        setUpView()
    }

    private fun handleLoading() = binding.loadingLayout.root.visible()

    private fun handleIdle() = binding.loadingLayout.root.gone()

    private fun handleError(message: String) {
        CustomToaster.show(
            context = requireContext(),
            message = message,
           isSuccess = false
        )
    }

    private fun handleSuccess() {
        val token = viewModel.prefs.getValue(Constants.getToken())
        viewModel.prefs.putValue(Constants.getToken() , token)
        viewModel.prefs.putValue(Constants.registerToken(), "")
        val intent = Intent(activity , MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpView(){
        binding.webView.settings.javaScriptEnabled = true

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val link: String = url.toString()
                if (link.contains(AuthConsts.PAYMENT_SUCCESS_LINK)) {
                    viewModel.updatePaymentStatus(GeneralResponse.Success(message = null))
                } else if (link.contains(AuthConsts.PAYMENT_FAIL_LINK)) {
                    viewModel.updatePaymentStatus(GeneralResponse.ResponseError(message = null))
                }
                return true
            }
        }

        binding.webView.loadUrl(args.paymentLink)

//        binding.webView.webChromeClient = object : WebChromeClient() {
//            override fun onProgressChanged(view: WebView?, newProgress: Int) {
//                if (newProgress < 100) {
//                    handleLoading()
//                }
//                if (newProgress == 100) {
//                    handleIdle()
//                }
//            }
//        }
    }
}