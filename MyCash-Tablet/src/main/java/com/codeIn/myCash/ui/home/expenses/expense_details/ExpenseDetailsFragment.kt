package com.codeIn.myCash.ui.home.expenses.expense_details

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.codeIn.common.data.NumberHelper
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.ImageHelper.getImageSize
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.myCash.databinding.FragmentExpenseDetailsBinding
import com.codeIn.common.util.gone
import com.codeIn.common.util.toTwoDigits
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.ui.home.clients_vendors.cleint_details.ClientDetailsFragmentDirections
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientModel
import com.codeIn.myCash.features.sales.data.expenses.remote.response.ExpenseModel
import com.codeIn.myCash.features.sales.data.expenses.remote.response.ExpenseState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ExpenseDetailsFragment : Fragment() {

    private val viewModel: ExpenseDetailsViewModel by viewModels()
    private var _binding: FragmentExpenseDetailsBinding? = null
    private val binding get() = _binding!!

    private val infoDialog: InfoDialog = InfoDialog()

    @Inject
    lateinit var prefs : SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpenseDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            deleteExpenseButton.setOnClickListener {
                viewModel.deleteExpense()
            }
            editButton.setOnClickListener {
                findNavController().navigate(
                    com.codeIn.myCash.ui.home.expenses.expense_details.ExpenseDetailsFragmentDirections.actionNavigationExpenseDetailsFragmentToUpdateExpenseFragment(
                        viewModel.expense.value ?: ExpenseModel(
                            statement = null,
                            expenseFile = null,
                            amount = null,
                            note = null,
                            createdAt = null,
                            additionalInfo = null,
                            date = null,
                            id = 0 ,
                            tax = null ,
                            totalAmount = null
                        )
                    )
                )
            }
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            attachmentContainer.setOnClickListener {
                showImageFullScreen()
            }
            imageViewer.btnClose.setOnClickListener { imageViewer.root.gone() }
            imageViewer.root.setOnClickListener { imageViewer.root.gone() }
        }

        viewModel.apply {
            expenseDataResult.collectOnLifecycle(viewModelScope , viewLifecycleOwner){response->
                when(response){
                    is ExpenseState.SuccessDeleteExpense-> {
                        handleSuccessDeleteExpense(response.message)
                    }
                    is ExpenseState.SuccessShowSingleExpense ->{
                        updateExpense(response.data)
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
                    is ExpenseState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else ->{

                    }
                }
            }

            expense.collectOnLifecycle(viewModelScope , viewLifecycleOwner){expense->
                handleSuccess(expense)
            }
        }
    }

    private fun showImageFullScreen() {
        binding.apply {
            imageViewer.root.visible()
            Glide.with(requireContext()).load(viewModel.expense.value?.expenseFile)
                .placeholder(R.drawable.icon_app)
                .error(R.drawable.icon_app)
                .into(imageViewer.imgDisplay)
        }
    }

    private fun handleSuccess(expense: ExpenseModel?) {
        viewModel.clearState()
        binding.loadingLayout.root.gone()
        binding.apply {
            expenseNumberTextView.text = "#${expense?.id}"
            expenseItem.titleTextView.text = expense?.statement?:"----"
            expenseItem.dateTextView.text = expense?.createdAt
            expenseItem.expensesPriceTextView.text = "${NumberHelper.persianToEnglishText(expense?.totalAmount?:"0.0").toDouble().toTwoDigits()} ${viewModel.currency}"
            if (expense?.expenseFile?.isNotEmpty() == true)
            {
                attachmentHeader.visible()
                attachmentContainer.visible()
                Glide
                    .with(binding.root.context)
                    .asBitmap()
                    .load(expense.expenseFile)
                    .into(object : SimpleTarget<Bitmap?>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?
                        ) {
                            val byteCount  = resource.allocationByteCount
                            val sizeInKB = byteCount / 1024
                            Log.d("TAG" , "SIZE ,  $sizeInKB")
                            attachmentSizeTextView.text = "$sizeInKB ${getString(R.string.KB)}"
                        }
                    })

                attachmentDateTextView.text = expense.date
            }
            else
            {
                attachmentHeader.gone()
                attachmentContainer.gone()
            }
            if (expense?.note?.isNotEmpty() == true)
            {
                noteItem.root.visible()
                noteItem.titleTextView.text = expense?.note
            }
            else
                noteItem.root.gone()

            if (expense?.additionalInfo?.isNotEmpty() == true)
            {
                additionalItem.root.visible()
                additionalItem.titleTextView.text = expense.additionalInfo
            }
            else
                additionalItem.root.gone()

        }
    }


    private fun handleSuccessDeleteExpense(message: String?){
        viewModel.clearState()
        CustomToaster.show(
            context = requireContext(),
            message = message!!,
            isSuccess = true
        )
        findNavController().popBackStack()
    }

    private fun handleLoading() = binding.loadingLayout.root.visible()

    private fun handleIdle() = binding.loadingLayout.root.gone()

    private fun handleError(message: String) {
        viewModel.clearState()
        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = false
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSingleExpense()
    }
}