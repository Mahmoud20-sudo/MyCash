package com.codeIn.myCash.ui.home.invoices.madaTransactions

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.PermissionChecker
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.databinding.FragmentShowTransactionsBinding
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.features.stock.data.madaTransactions.remote.response.TransactionState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MadaTransactionsFragment : Fragment() {
    private val viewModel: MadaTransactionsViewModel by viewModels()
    private var _binding: FragmentShowTransactionsBinding? = null
    private val binding get() = _binding!!
    private val infoDialog: InfoDialog = InfoDialog()
    private val checker = PermissionChecker()

    @Inject
    lateinit var prefs : SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override  fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set the click listeners for the main section and filter
        binding.apply {
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        val adapter = TransactionAdapter(requireContext() , viewModel.currency)
        binding.transactionRecycleView.adapter = adapter

        //observe the live vendorsData and update the views accordingly
        viewModel.apply {

            transactionResult.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is TransactionState.SuccessShowTransactions -> {
                        if (response.data?.data.isNullOrEmpty()) {
                            binding.transactionRecycleView.gone()
                        } else {
                            binding.transactionRecycleView.visible()
                            adapter.submitList(response.data?.data)
                        }
                        clearState()
                    }

                    is TransactionState.Loading -> handleLoading()
                    is TransactionState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is TransactionState.Idle -> handleIdle()
                    is TransactionState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else -> {

                    }
                }
            }
        }

    }

    private fun handleLoading() = binding.loadingLayout.root.visible()

    private fun handleIdle() = binding.loadingLayout.root.gone()

    private fun handleError(message: String) {
        binding.loadingLayout.root.gone()

    }

}