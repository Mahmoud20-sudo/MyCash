package com.codeIn.myCash.ui.options.subscriptions.current_subscription

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.SubscriptionsOptions
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentSubscriptionItemBinding
import com.codeIn.myCash.databinding.FragmentSubscriptionsOptionsBinding
import com.codeIn.myCash.ui.authentication.sign_up.subscriptions.FeaturesAdapter
import com.codeIn.myCash.ui.authentication.sign_up.subscriptions.SubscriptionsSwipePagerAdapter
import com.codeIn.myCash.ui.updateSectionsViews
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.ui.home.MainActivity
import com.codeIn.myCash.ui.home.products.products.ProductsFragmentDirections
import com.codeIn.myCash.ui.home.products.products.dialogs.ProductOptionsDialog
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.User
import com.codeIn.myCash.features.user.data.settings.remote.response.device.DeviceModel
import dagger.hilt.android.AndroidEntryPoint
import com.codeIn.myCash.features.user.data.settings.remote.response.subscription.Package
import com.codeIn.myCash.features.user.data.settings.remote.response.subscription.Subscription
import com.codeIn.myCash.utilities.CustomToaster
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class CurrentSubscriptionFragment : Fragment(), SubscriptionsSwipePagerAdapter.Communicator {

    override var showImage: Boolean = false
    override fun startPackage(newPackage: Package) {
        Toast.makeText(
            requireContext(),
            "start subscription  ${newPackage.name}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private val viewModel: CurrentSubscriptionViewModel by viewModels()

    private var _binding: FragmentSubscriptionsOptionsBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var prefs : SharedPrefsModule

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscriptionsOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set the click listeners for the subscriptions options buttons
        binding.apply {
            currentPackageTextView.setOnClickListener {
                showDeviceOptions(false)
            }
            otherPackagesTextView.setOnClickListener {
                showDeviceOptions(true)
            }
            backArrow.setOnClickListener {
                Log.d("TAG" , "We arevheree")
                if ((requireActivity() as MainActivity).viewModel.expire.value == true)
                    activity?.finish()
                else
                    requireActivity()?.onBackPressed()
            }
        }

        //observe the subscriptions options live vendorsData and update the UI accordingly
        viewModel.apply {
            dataResult.observe(viewLifecycleOwner
            ) { response ->
                when (response) {
                    is AuthenticationState.Loading -> handleLoading()
                    is AuthenticationState.Idle -> handleIdle()
                    is AuthenticationState.StateError -> {}
                    is AuthenticationState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is AuthenticationState.Success ->{
                       handleSuccess(response.data)
                    }
                    is AuthenticationState.InputError ->{}
                    is AuthenticationState.ServerError -> {}
                    else -> {}
                }
            }

        }
    }

    private fun FragmentSubscriptionItemBinding.bindPackage(subscription: Subscription?) {
        imageView.gone()
        startNowButton.gone()

        nameTextView.text = subscription?.`package`?.name

        finalPriceTextView.text = subscription?.`package`?.finalPrice

        val durationText = "/${subscription?.`package`?.duration}"

        durationTextView.text = durationText

        val adapter = FeaturesAdapter()
        adapter.submitList(subscription?.`package`?.features)
        featuresRecycleView.adapter = adapter
    }
    private fun handleLoading() {
        binding.loadingLayout.root.visible()
    }
    private fun handleIdle() {
        binding.loadingLayout.root.gone()
    }
    private fun handleSuccess(user : User?){
        binding.loadingLayout.root.gone()
        binding.currentPackageLayout.packageRemainingDaysTextView.text = HtmlCompat.fromHtml(
            getString(R.string.package_remaining_days, user?.subscription?.daysLeft.toString()),
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        binding.currentPackageLayout.packageExpiresDateTextView.text = user?.subscription?.endDate

        binding.currentPackageLayout.subscriptionItemFragment.bindPackage(user?.subscription)

        viewModel.subscripePackage(user?.subscription?.`package`)
    }

    private var deviceOptionsDialog: DeviceDialog? = null
    private fun showDeviceOptions(changePackage : Boolean = true ) {
        deviceOptionsDialog?.dismiss()
        deviceOptionsDialog = DeviceDialog(requireContext(),
            object : DeviceDialog.Communicator {
                override fun changeCurrentDevice(change: Boolean) {
                    if (changePackage && change){
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
                            com.codeIn.myCash.ui.options.subscriptions.current_subscription.CurrentSubscriptionFragmentDirections.actionCurrentSubscriptionFragmentToAnotherSubscriptionsFragment(
                            deviceModel , "1"
                            )
                        )
                    }else if (!changePackage && change){
                        findNavController().navigate(
                            com.codeIn.myCash.ui.options.subscriptions.current_subscription.CurrentSubscriptionFragmentDirections.actionCurrentSubscriptionFragmentToDeviceSelectionFragment(
                                viewModel.selectedPackage.value?.id?:0 , viewModel.selectedPackage.value!!
                            )
                        )
                    }else if(!changePackage && !change){
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
                            com.codeIn.myCash.ui.options.subscriptions.current_subscription.CurrentSubscriptionFragmentDirections.actionCurrentSubscriptionFragmentToPaymentMethodFragment(
                            deviceModel , viewModel.selectedPackage.value!!
                        )
                        )
                    } else if(changePackage && !change){
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
                            com.codeIn.myCash.ui.options.subscriptions.current_subscription.CurrentSubscriptionFragmentDirections.actionCurrentSubscriptionFragmentToAnotherSubscriptionsFragment(
                            deviceModel , "0"
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
                            com.codeIn.myCash.ui.options.subscriptions.current_subscription.CurrentSubscriptionFragmentDirections.actionCurrentSubscriptionFragmentToAnotherSubscriptionsFragment(
                                deviceModel , "1"
                            )
                        )
                    }
                }
            })
        deviceOptionsDialog?.show()
    }
}