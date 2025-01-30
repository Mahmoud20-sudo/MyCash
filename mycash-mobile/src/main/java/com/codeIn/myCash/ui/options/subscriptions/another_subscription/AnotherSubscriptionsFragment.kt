package com.codeIn.myCash.ui.options.subscriptions.another_subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentSubscriptionsBinding
import com.codeIn.myCash.databinding.FragmentSubscriptionsInBinding
import com.codeIn.myCash.ui.authentication.sign_up.subscriptions.SubscriptionsSwipePagerAdapter
import com.codeIn.myCash.ui.authentication.sign_up.subscriptions.SubscriptionsViewModel
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.utilities.views.MyViewPager
import com.codeIn.myCash.features.user.data.settings.remote.response.device.DeviceModel
import com.codeIn.myCash.features.user.data.settings.remote.response.subscription.Package
import com.codeIn.myCash.features.user.data.settings.remote.response.subscription.PackagesState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AnotherSubscriptionsFragment : Fragment(), SubscriptionsSwipePagerAdapter.Communicator {

    private val viewModel: SubscriptionsViewModel by viewModels()
    private val infoDialog: InfoDialog = InfoDialog()
    override var showImage: Boolean = true

    private var _binding: FragmentSubscriptionsInBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscriptionsInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {

        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        val swipePagerAdapter = SubscriptionsSwipePagerAdapter(this)
        binding.viewpager.adapter = swipePagerAdapter

        viewModel.apply {
            subscriptionsResult.collectOnLifecycle(viewModelScope, viewLifecycleOwner) { result ->
                when (result) {
                    is PackagesState.Loading -> handleLoading()
                    is PackagesState.Success -> handleSuccess(
                        result.data as ArrayList<Package>,
                        swipePagerAdapter
                    )

                    is PackagesState.StateError -> handleError(result.message.toString())
                    is PackagesState.ServerError -> handleError(
                        result.error.getErrorMessage(requireContext())
                    )

                    is PackagesState.Idle -> handleIdle()
                    else -> {}
                }
            }
        }

        val myViewPager = MyViewPager()
        myViewPager.initViewPager(binding.viewpager, requireContext(), resources)
        myViewPager.initWithTabLayout(binding.tabLayout, binding.viewpager)
    }

    private fun handleLoading() {
        binding.loadingLayout.root.visible()
    }

    private fun handleError(message: String) {
        viewModel.clearState()
        infoDialog.show(
            context = requireContext(),
            title = getString(R.string.error),
            message = message,
            positiveButtonText = getString(R.string.try_again),
            negativeButtonText = getString(R.string.cancel),
            onPositiveButtonClick = {
                viewModel.getPackages()
            },
            onNegativeButtonClick = {
                findNavController().popBackStack()
            }
        )
    }

    private fun handleIdle() {
        binding.loadingLayout.root.gone()
    }

    private fun handleSuccess(
        packages: ArrayList<Package>?,
        swipePagerAdapter: SubscriptionsSwipePagerAdapter
    ) {
        binding.loadingLayout.root.gone()
        if (packages.isNullOrEmpty()) {
            infoDialog.show(
                context = requireContext(),
                title = getString(R.string.error),
                message = getString(R.string.error_getting_packages_please_try_again),
                positiveButtonText = getString(R.string.try_again),
                negativeButtonText = getString(R.string.cancel),
                onPositiveButtonClick = {
                    viewModel.getPackages()
                },
                onNegativeButtonClick = {
                    findNavController().popBackStack()
                })
        } else {
            swipePagerAdapter.setList(packages)
        }
    }


    override fun startPackage(newPackage: Package) {
        viewModel.subscripePackage(newPackage)
        if (viewModel.state.contains("change_device")){
            val value = viewModel.state["change_device"]?:"0"
            if(value == "1"){
                findNavController().navigate(
                    com.codeIn.myCash.ui.options.subscriptions.another_subscription.AnotherSubscriptionsFragmentDirections.actionAnotherSubscriptionsFragmentToDeviceSelectionFragment(
                        newPackage.id, viewModel.selectedPackage.value!!
                    )
                )
            }
            else {
                val deviceModel = DeviceModel(
                    device= null ,
                    discount = "0.0",
                    discountType = "0" ,
                    finalPrice = "0.0" ,
                    id = 0 ,
                    isDiscount = "0" ,
                    price = "0"
                )
                findNavController().navigate(
                    com.codeIn.myCash.ui.options.subscriptions.another_subscription.AnotherSubscriptionsFragmentDirections.actionAnotherSubscriptionsFragmentToPaymentMethodFragment(
                        deviceModel, newPackage
                    )
                )
            }
        }

    }
}