package com.codeIn.myCash.ui.home.invoices.memorandums

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.InvoiceFilter
import com.codeIn.common.data.NoteType
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentCreditorDebtorNotesBinding
import com.codeIn.myCash.ui.home.invoices.CreditorDebtorNote
import com.codeIn.myCash.ui.home.invoices.invoices.InvoicesFragmentDirections
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.ui.updateSectionsViews
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.data.memorandum.remote.response.MemorandumModel
import com.codeIn.myCash.features.stock.data.memorandum.remote.response.MemorandumState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MemorandumsFragment : Fragment() {
    private val viewModel: MemorandumsViewModel by viewModels()
    private var _binding: FragmentCreditorDebtorNotesBinding? = null
    private val binding get() = _binding!!
    private val infoDialog: InfoDialog = InfoDialog()

    @Inject
    lateinit var prefs : SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreditorDebtorNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set the click listeners for the main section and filter
        binding.apply {
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            allTextView.setOnClickListener {
                viewModel.updateFilter(NoteType.ALL)
            }
            creditorNoteTextView.setOnClickListener {
                viewModel.updateFilter(NoteType.CREDITOR)
            }
            debtorNoteTextView.setOnClickListener {
                viewModel.updateFilter(NoteType.DEBTOR)
            }
            searchView.inputType = InputType.TYPE_CLASS_NUMBER
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.getSingleMemorandum(query)
                    return false
                }
            })
        }

        val adapter = MemorandumsAdapter(requireContext(), notesCommunicator , viewModel.currency)
        binding.recycleView.adapter = adapter

        //observe the live memorandumsData and update the views accordingly
        viewModel.apply{


            filter.observe(viewLifecycleOwner) {
                updateFiltersViews(it)
            }

            memorandumDataResult.collectOnLifecycle(viewModelScope , viewLifecycleOwner) { response ->
                when (response) {
                    is MemorandumState.SuccessShowMemorandums -> {
                        if (response.data?.data.isNullOrEmpty()) {
                            binding.recycleView.gone()
                        } else {
                            binding.recycleView.visible()
                            adapter.submitList(response.data?.data)
                        }
                        clearState()
                    }
                    is MemorandumState.SuccessShowSingleMemorandum ->{
                        navigateTo(
                            com.codeIn.myCash.ui.home.invoices.memorandums.MemorandumsFragmentDirections.actionNavigationCreditorDebtorNotesFragmentToNavigationPaymentSummaryFragment(
                                response?.data?.id.toString()
                            )
                        )
                    }
                    is MemorandumState.Loading -> handleLoading()
                    is MemorandumState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is MemorandumState.Idle -> handleIdle()
                    is MemorandumState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else -> {}
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
    private fun updateFiltersViews(filter: NoteType) {
        val context = requireContext()
        val binding = binding

        binding.apply {
            val viewsToStyle = listOf(
                allTextView,
                creditorNoteTextView,
                debtorNoteTextView
            )

            val selectedView = when (filter) {
                NoteType.ALL -> allTextView
                NoteType.CREDITOR -> creditorNoteTextView
                NoteType.DEBTOR -> debtorNoteTextView

            }

            updateSectionsViews(context, viewsToStyle, selectedView)
        }

    }

    private val notesCommunicator = object : MemorandumsAdapter.Communicator {
        override fun onClick(note: MemorandumModel) {
            navigateTo(
                com.codeIn.myCash.ui.home.invoices.memorandums.MemorandumsFragmentDirections.actionNavigationCreditorDebtorNotesFragmentToNavigationPaymentSummaryFragment(
                    note.id.toString()
                )
            )
        }

    }

    private fun navigateTo(navDirections: NavDirections) {
        if (findNavController().currentDestination?.id == R.id.navigation_creditorDebtorNotesFragment) {
            findNavController().navigate(navDirections)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.handleMemorandum(viewModel.filter.value?: NoteType.ALL)
    }
}