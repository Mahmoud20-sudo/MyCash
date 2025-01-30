package com.codeIn.myCash.ui.options.subscriptions.devices_subscription

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
import com.codeIn.myCash.databinding.FragmentDeviceSelectionBinding
import com.codeIn.myCash.databinding.FragmentDeviceSelectionInBinding
import com.codeIn.myCash.ui.authentication.sign_up.device_selection.DeviceSelectionViewModel
import com.codeIn.myCash.ui.authentication.sign_up.device_selection.DevicesAdapter
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.features.user.data.settings.remote.response.device.DeviceModel
import com.codeIn.myCash.features.user.data.settings.remote.response.device.DevicesState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeviceSelectionFragment : Fragment() {

    private val viewModel: DeviceSelectionViewModel by viewModels()
    private val infoDialog: InfoDialog = InfoDialog()

    private var _binding: FragmentDeviceSelectionInBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceSelectionInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.nextButton.setOnClickListener {
                findNavController().navigate(
                    com.codeIn.myCash.ui.options.subscriptions.devices_subscription.DeviceSelectionFragmentDirections.actionDeviceSelectionFragmentToPaymentMethodFragment(
                        viewModel.selectedDevice.value!!,
                        viewModel.selectedPackage.value!!
                    )
                )
        }

        val devicesAdapter = DevicesAdapter(devicesCommunicator)
        binding.devicesRecyclerView.adapter = devicesAdapter
        viewModel.apply {
            devicesResult.collectOnLifecycle(viewModelScope, viewLifecycleOwner) { result ->
                when (result) {
                    is DevicesState.Loading -> handleLoading()
                    is DevicesState.Success -> handleSuccess(
                        result.data?.data as ArrayList<DeviceModel>,
                        devicesAdapter
                    )

                    is DevicesState.StateError -> handleError(result.message.toString())
                    is DevicesState.ServerError -> handleError(
                        result.error.getErrorMessage(requireContext())
                    )

                    is DevicesState.Idle -> handleIdle()
                    else -> {}
                }
            }

            selectedDevice.observe(viewLifecycleOwner) {
                binding.nextButton.isEnabled = it != null
            }
        }
    }

    private fun handleLoading() {
        binding.loadingLayout.root.visible()
    }

    private fun handleIdle() {
        binding.loadingLayout.root.gone()
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
                viewModel.getDevices()
            },
            onNegativeButtonClick = {
                findNavController().popBackStack()
            }
        )
    }

    private fun handleSuccess(devices: List<DeviceModel>, devicesAdapter: DevicesAdapter) {
        binding.loadingLayout.root.gone()
        if (devices.isEmpty()) {
            infoDialog.show(
                context = requireContext(),
                title = getString(R.string.error),
                message = getString(R.string.no_devices_found),
                positiveButtonText = getString(R.string.try_again),
                negativeButtonText = getString(R.string.cancel),
                onPositiveButtonClick = {
                    viewModel.getDevices()
                },
                onNegativeButtonClick = {
                    findNavController().popBackStack()
                }
            )
        } else {
            devicesAdapter.submitList(devices)
        }
    }

    private val devicesCommunicator = object : DevicesAdapter.Communicator {
        override fun onDeviceSelected(device: DeviceModel) {
            viewModel.payDevice(device)
        }
    }

}