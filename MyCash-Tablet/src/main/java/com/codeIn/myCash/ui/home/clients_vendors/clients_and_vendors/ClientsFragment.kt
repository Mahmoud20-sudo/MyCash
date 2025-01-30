package com.codeIn.myCash.ui.home.clients_vendors.clients_and_vendors

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.AppConstants.DELAY_TIME_SEARCH
import com.codeIn.common.data.ClientsFilter
import com.codeIn.common.data.ClientsSection
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.getQueryTextChangeStateFlow
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentClientsBinding
import com.codeIn.myCash.ui.ViewStrokes
import com.codeIn.myCash.ui.hideKeyboard
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.ui.updateSectionsViews
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientModel
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class ClientsFragment : Fragment() {
    private val viewModel: ClientsViewModel by viewModels()
    private var _binding: FragmentClientsBinding? = null
    private val binding get() = _binding!!
    private val infoDialog: InfoDialog = InfoDialog()
    @Inject
    lateinit var prefs : SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            addImageView.setOnClickListener {
                // checks if the current destination is the clients fragment
                if (findNavController().currentDestination?.id == R.id.navigation_clientsFragment)
                    if (viewModel.mainSection.value == ClientsSection.CLIENTS){
                        findNavController().navigate(
                            com.codeIn.myCash.ui.home.clients_vendors.clients_and_vendors.ClientsFragmentDirections.actionNavigationClientsFragmentToNavigationAddClientFragment()
                        )
                    }
                    else if (viewModel.mainSection.value == ClientsSection.VENDORS){
                        findNavController().navigate(
                            com.codeIn.myCash.ui.home.clients_vendors.clients_and_vendors.ClientsFragmentDirections.actionNavigationClientsFragmentToAddVendorFragment()
                        )
                    }
            }
            clientsTextView.setOnClickListener {
                viewModel.updateMainSection(ClientsSection.CLIENTS)
            }
            vendorsTextView.setOnClickListener {
                viewModel.updateMainSection(ClientsSection.VENDORS)
            }

            allTextView.setOnClickListener {
                viewModel.updateFilter(ClientsFilter.ALL)
            }
            paymentCompletedTextView.setOnClickListener {
                viewModel.updateFilter(ClientsFilter.PAYMENT_COMPLETED)
            }
            installmentPaymentTextView.setOnClickListener {
                viewModel.updateFilter(ClientsFilter.INSTALLMENT_PAYMENT)
            }

            searchView
                .getQueryTextChangeStateFlow()
                .debounce(DELAY_TIME_SEARCH)
                .distinctUntilChanged()
                .flowOn(Dispatchers.Default)
                .onEach { query ->
                    viewModel.updateSearchText(query)
                }.launchIn(viewModel.viewModelScope)
        }

        var clientAdapter = ClientsAdapter(clientsCommunicator , ClientsSection.CLIENTS)
        binding.recycleView.adapter = clientAdapter

        viewModel.apply {
            mainSection.collectOnLifecycle(viewModelScope , viewLifecycleOwner){clientSection->
                updateClientsSectionsViews(clientSection)
                when(clientSection){
                    ClientsSection.CLIENTS -> {
                        clientAdapter = ClientsAdapter(clientsCommunicator , ClientsSection.CLIENTS)
                        getClients()
                    }
                    ClientsSection.VENDORS -> {
                        clientAdapter = ClientsAdapter(clientsCommunicator , ClientsSection.VENDORS)
                        getClients()
                    }
                    else -> {}
                }
            }
            searchText.observe(viewLifecycleOwner){
                getClients()

            }
            filter.observe(viewLifecycleOwner){clientFilter->
                updatePaymentSectionsViews(clientFilter)
            }
            dataResult.collectOnLifecycle(viewModelScope , viewLifecycleOwner){response->
                when(response){
                    is ClientState.SuccessShowClients-> {
                        if (response.data?.data.isNullOrEmpty()){
                            binding.recycleView.gone()
                        }
                        else
                        {
                            binding.recycleView.visible()
                            binding.recycleView.adapter = clientAdapter
                            clientAdapter.submitList(response.data?.data)
                        }
                        clearClientState()
                    }
                    is ClientState.Loading -> handleLoading()
                    is ClientState.Idle -> handleIdle()
                    is ClientState.InputError -> {
                    }
                    is ClientState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    is ClientState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }  else ->{

                    }
                }
            }
        }

    }
    private fun handleLoading() = binding.loadingLayout.root.visible()
    private fun handleIdle() = binding.loadingLayout.root.gone()
    private fun handleError(message: String) {
        binding.loadingLayout.root.gone()
        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = false
        )
    }
    private val clientsCommunicator = object : ClientsAdapter.Communicator {
        override fun showDetails(client: ClientModel) {
            if (findNavController().currentDestination?.id == R.id.navigation_clientsFragment) {
                // checks if the main section is clients or vendors and navigates to the clients details fragment or vendors details fragment accordingly
                if (viewModel.mainSection.value == ClientsSection.CLIENTS)
                    findNavController().navigate(
                        com.codeIn.myCash.ui.home.clients_vendors.clients_and_vendors.ClientsFragmentDirections.actionNavigationClientsFragmentToNavigationClientsDetailsFragment(
                            client
                        )
                    )
                else if (viewModel.mainSection.value == ClientsSection.VENDORS)
                    findNavController().navigate(
                        com.codeIn.myCash.ui.home.clients_vendors.clients_and_vendors.ClientsFragmentDirections.actionNavigationClientsFragmentToNavigationVendorsDetailsFragment(
                            client
                        )
                    )
            }

        }
    }
    private fun updateClientsSectionsViews(clientsSection: ClientsSection) {
        val context = requireContext()
        val binding = binding

        // Apply secondary style to all views initially
        val viewsToStyle = listOf(
            binding.clientsTextView,
            binding.vendorsTextView
        )

        val selectedView = when (clientsSection) {
            ClientsSection.CLIENTS -> {
                binding.titleTextView.text = getText(R.string.clients)
                binding.clientsTextView
            }

            ClientsSection.VENDORS -> {
                binding.titleTextView.text = getText(R.string.vendors)
                binding.vendorsTextView
            }
        }
        updateSectionsViews(
            context = context,
            viewsToStyle = viewsToStyle,
            selectedView = selectedView,
            stroke = ViewStrokes.R12_S1
        )
    }
    private fun updatePaymentSectionsViews(clientsFilter: ClientsFilter) {
        val context = requireContext()
        val binding = binding

        // Apply secondary style to all views initially
        val viewsToStyle = listOf(
            binding.allTextView,
            binding.paymentCompletedTextView,
            binding.installmentPaymentTextView
        )

        // Apply primary style based on the selected section
        val selectedView = when (clientsFilter) {
            ClientsFilter.ALL -> binding.allTextView
            ClientsFilter.PAYMENT_COMPLETED -> binding.paymentCompletedTextView
            ClientsFilter.INSTALLMENT_PAYMENT -> binding.installmentPaymentTextView
        }

        updateSectionsViews(context, viewsToStyle, selectedView)
    }
}