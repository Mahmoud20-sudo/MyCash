package com.codeIn.myCash.ui.home.expenses.add_expense

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.PermissionChecker
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentNewExpenseBinding
import com.codeIn.myCash.ui.authentication.showError
import com.codeIn.myCash.ui.hideKeyboard
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.utilities.pickers.DatePicker
import com.codeIn.myCash.utilities.pickers.ImagePickerBottomSheet
import com.codeIn.myCash.features.sales.data.expenses.remote.response.ExpenseState
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class AddExpenseFragment : Fragment() {

    private var attachment: File? = null
    private val datePicker = DatePicker()
    private var _binding: FragmentNewExpenseBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddExpenseViewModel by viewModels()
    private val checker = PermissionChecker()

    @Inject
    lateinit var prefs : SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (attachment != null)
            binding.attachmentsButton.text = "${getString(R.string.upload_import_your_files_here)} \n ${attachment?.toUri()}"
        else
            binding.attachmentsButton.text = "${getString(R.string.upload_import_your_files_here)}"


        binding.apply {
            expenseDateEditText.setOnClickListener {
                datePicker.showDatePicker(
                    childFragmentManager = parentFragmentManager,
                    textView = binding.expenseDateEditText
                )
            }
            uploadingImageLayout.attachmentsButton.setOnClickListener {
                if (checker?.checkPermissionCamera(activity) == true) {
                    checker?.askForPermissionCamera(activity)
                } else {
                    ImagePickerBottomSheet(
                        communicator = imagePickerCommunicator,
                        attachment = attachment?.toUri()
                    ).show(
                        childFragmentManager,
                        ImagePickerBottomSheet.TAG
                    )
                }
            }
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            cancelButton.setOnClickListener {
                findNavController().popBackStack()
            }
            proceedButton.setOnClickListener {
                hideKeyboard(requireContext(), view)
                viewModel.createExpense(
                    statement = expenseStatementEditText.text.toString(),
                    date = expenseDateEditText.text.toString() ,
                    amount = amountEditText.text.toString() ,
                    note = commentsEditText.text.toString() ,
                    additionalInfo = extraInfoEditText.text.toString(),
                    file = attachment , tax = taxEditText.text.toString()
                )
            }
        }

        viewModel.apply {
            expenseDataResult.collectOnLifecycle(viewModelScope , viewLifecycleOwner){response->
                when(response){
                    is ExpenseState.SuccessShowSingleExpense->handleSuccess()
                    is ExpenseState.Loading -> handleLoading()
                    is ExpenseState.Idle -> handleIdle()
                    is ExpenseState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is ExpenseState.InputError -> handleInputError(response.inputError)
                    is ExpenseState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else ->{

                    }
                }

            }
        }
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

    private fun handleInputError(invalidInput: InvalidInput) {
        viewModel.clearState()
        when (invalidInput) {
            InvalidInput.PRICE -> {
                binding.amountEditText.showError()
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.please_enter_valid_price),
                    isSuccess = false
                )
            }
            InvalidInput.EMPTY -> {
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.please_fill_all_the_fields),
                    isSuccess = false
                )

            }

            else -> {}
        }
    }

    private fun handleSuccess() {
        binding.loadingLayout.root.gone()
        CustomToaster.show(
            context = requireContext(),
            message = getString(R.string.success_message),
            isSuccess = true
        )
        findNavController().popBackStack()
    }

    private val imagePickerCommunicator = object : ImagePickerBottomSheet.Communicator {
        override fun setImage(file: File) {
            attachment = file
            setAttachmentData(file.absolutePath)
        }
    }

    private fun setAttachmentData(attachment: String?) {
        val attachmentTitle = getString(R.string.upload_import_your_files_here)
        attachment?.let { attach ->
            val imagePath = if (URLUtil.isValidUrl(attach)) attach else File(attach)
            binding.uploadingImageLayout.apply {
                attachmentsButton.text = attachmentTitle
                imageImageView.visible()
                Glide.with(requireContext()).load(imagePath)
                    .placeholder(R.drawable.icon_app)
                    .error(R.drawable.icon_app)
                    .into(imageImageView)
            }
        }
    }
}
