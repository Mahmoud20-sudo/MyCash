package com.codeIn.myCash.ui.home.expenses.expenses

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.ExpensesFilter
import com.codeIn.common.data.NumberHelper
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.getQueryTextChangeStateFlow
import com.codeIn.common.util.gone
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentTransactionsBinding
import com.codeIn.myCash.ui.updateSectionsViews
import com.codeIn.common.util.toTwoDigits
import com.codeIn.common.util.visible
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.pickers.DatePicker
import com.codeIn.myCash.features.sales.data.expenses.remote.response.ExpenseModel
import com.codeIn.myCash.features.sales.data.expenses.remote.response.ExpenseState
import com.codeIn.myCash.features.sales.data.expenses.remote.response.ExpensesData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class ExpensesFragment : Fragment() {
    private val viewModel: ExpensesViewModel by viewModels()
    private var _binding: FragmentTransactionsBinding? = null
    private val binding get() = _binding!!
    private val datePicker = DatePicker()
    @Inject
    lateinit var prefs : SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            todayTextView.setOnClickListener {
                viewModel.updateDurationType(ExpensesFilter.DAILY)
            }
            weeklyTextView.setOnClickListener {
                viewModel.updateDurationType(ExpensesFilter.WEEKLY)
            }
            monthlyTextView.setOnClickListener {
                viewModel.updateDurationType(ExpensesFilter.MONTHLY)
            }
            annuallyTextView.setOnClickListener {
                viewModel.updateDurationType(ExpensesFilter.YEARLY)
            }
            addImageView.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.navigation_transactionsFragment)
                    findNavController().navigate(ExpensesFragmentDirections.actionNavigationTransactionsFragmentToNavigationNewExpenseFragment())
            }
            calender.setOnClickListener {
                datePicker.showDatePicker(
                    childFragmentManager = childFragmentManager,
                    textView = binding.dateView
                )
                dateView.visibility = View.VISIBLE
            }
            dateView.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.updateDateText(s.toString())
                    viewModel.getExpenses(type = viewModel.duration.value)
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
            dateView.setOnClickListener {
                viewModel.updateDateText("")
                dateView.gone()
            }
            showAllTextView.setOnClickListener {
                findNavController().navigate(
                    ExpensesFragmentDirections.actionNavigationTransactionsFragmentToSeeMoreExpensesFragment()
                )
            }

            searchView
                .getQueryTextChangeStateFlow()
                .debounce(AppConstants.DELAY_TIME_SEARCH)
                .distinctUntilChanged()
                .flowOn(Dispatchers.Default)
                .onEach { query ->
                    viewModel.updateSearchText(query)
                }.launchIn(viewModel.viewModelScope)
        }

        val adapter = ExpensesAdapter(requireContext(), expensesCommunicator , viewModel.currency)
        binding.expensesRecycleView.adapter = adapter


        viewModel.apply {

            duration.collectOnLifecycle(viewModelScope , viewLifecycleOwner){value->
                updateDurationSectionsViews(value)
                getExpenses(value)
            }

            dataResult.collectOnLifecycle(viewModelScope , viewLifecycleOwner){response->
                when(response){
                    is ExpenseState.SuccessShowExpenses ->{
                        handleTotalSuccess(response.data)
                        adapter.submitList(response.data?.data)
                    }
                    is ExpenseState.Loading -> handleLoading()
                    is ExpenseState.Idle -> handleIdle()
                    is ExpenseState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is ExpenseState.InputError -> {
                    }
                    else ->{

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
    private fun handleTotalSuccess(data : ExpensesData? ){
        binding.loadingLayout.root.gone()
        binding.expenseItem.expensesPriceTextView.text =
            "${NumberHelper.persianToEnglishText(data?.totalExpenses?:"0.0").toDouble().toTwoDigits()} ${viewModel.currency}"
        binding.expenseItem.titleTextView.text = getString(R.string.total_expenses)
        binding.expenseItem.dateTextView.text =  data?.text
        binding.dateTextView.text = data?.text
        binding.expensesCountTextView.text = "(${data?.pagination?.total})"
    }
    private fun updateDurationSectionsViews(filter: ExpensesFilter) {
        val context = requireContext()
        val binding = binding

        val viewsToStyle = listOf(
            binding.todayTextView,
            binding.weeklyTextView,
            binding.monthlyTextView,
            binding.annuallyTextView
        )

        val selectedView = when (filter) {
            ExpensesFilter.DAILY -> binding.todayTextView
            ExpensesFilter.WEEKLY -> binding.weeklyTextView
            ExpensesFilter.MONTHLY -> binding.monthlyTextView
            ExpensesFilter.YEARLY -> binding.annuallyTextView
        }

        updateSectionsViews(context, viewsToStyle, selectedView)
    }


    //communicator to communicate with the adapter when the user clicks on an item
    private val expensesCommunicator = object : ExpensesAdapter.Communicator {
        override fun onItemClicked(item: ExpenseModel) {
            if (findNavController().currentDestination?.id == R.id.navigation_transactionsFragment)
                findNavController().navigate(
                    ExpensesFragmentDirections.actionNavigationTransactionsFragmentToNavigationExpenseDetailsFragment(
                        item.id.toString()
                    )
                )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getExpenses(viewModel.duration.value)
    }
}