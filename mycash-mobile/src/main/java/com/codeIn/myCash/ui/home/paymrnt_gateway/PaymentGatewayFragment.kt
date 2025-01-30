package com.codeIn.myCash.ui.home.paymrnt_gateway

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.AuthConsts
import com.codeIn.common.payment.PaymentFactoryImpl
import com.codeIn.common.util.GeneralResponse
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentPaymentGatewayHomeBinding
import com.codeIn.myCash.ui.home.MainActivity
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import java.net.URI
import javax.inject.Inject


@AndroidEntryPoint
class PaymentGatewayFragment : Fragment()  {

    private val viewModel: PaymentGatewayViewModel by viewModels()
    private var _binding: FragmentPaymentGatewayHomeBinding? = null
    private val binding get() = _binding!!
    private val infoDialog: InfoDialog = InfoDialog()

    @Inject
    lateinit var paymentFactoryImpl: PaymentFactoryImpl

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPaymentGatewayHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback() {
//            activity?.finish()
        }

        viewModel.apply {

            payResult.collectOnLifecycle(viewModelScope, this@PaymentGatewayFragment) { response ->
                when (response) {
                    is GeneralResponse.Loading -> handleLoading()
                    is GeneralResponse.Idle -> handleIdle()
                    is GeneralResponse.ResponseError -> handleError(
                        response.message ?: getString(R.string.genericErrorMessage)
                    )

                    is GeneralResponse.Success -> handleSuccess()
                    is GeneralResponse.ServerError -> handleError(
                        response.error.getErrorMessage(
                            requireContext()
                        )
                    )

                    else -> {}
                }
            }
        }

        setUpView()
    }

    private fun handleLoading() = binding.loadingLayout.root.visible()

    private fun handleIdle() = binding.loadingLayout.root.gone()

    private fun handleError(message: String) {
        infoDialog.show(
            context = requireContext(),
            title =  paymentFactoryImpl.handleFailureCode(requireContext(), message),
            message = message,
            positiveButtonText = getString(R.string.try_again),
        )
        viewModel.updatePaymentStatus(GeneralResponse.Idle)
    }

    private fun handleSuccess() {
        viewModel.updatePaymentStatus(GeneralResponse.Idle)
        val intent = Intent(activity , MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpView(){
        binding.webView.settings.javaScriptEnabled = true

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val link: String = URI(url).normalize().toString()
                if (link.contains(AuthConsts.PAYMENT_SUCCESS_LINK)) {
                    (requireActivity() as MainActivity).viewModel.isRefreshSubscription(false)
                    viewModel.prefs.putValue(AppConstants.PAYMENT_LINK , "")
                    viewModel.updatePaymentStatus(GeneralResponse.Success(message = null))
                } else if (link.contains(AuthConsts.PAYMENT_FAIL_LINK)) {
                    viewModel.updatePaymentStatus(GeneralResponse.ResponseError(message = null))
                }
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                handleLoading()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                handleIdle()
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                handleIdle()
            }
        }

        binding.webView.loadUrl(viewModel.prefs.getValue(AppConstants.PAYMENT_LINK)?:"")

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