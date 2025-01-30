package com.codeIn.myCash.ui.options.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.codeIn.common.util.PermissionChecker
import com.codeIn.common.util.Validation
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentProfileBinding
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.User
import com.codeIn.myCash.features.user.data.edit_profile.model.response.ProfileState
import com.codeIn.myCash.ui.options.profile.dialog.EmailDialog
import com.codeIn.myCash.ui.options.profile.dialog.PhoneDialog
import com.codeIn.myCash.ui.setErrorMsg
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.utilities.pickers.ImagePickerBottomSheet
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private var emailDialog: EmailDialog? = null
    private var phoneDialog: PhoneDialog? = null
    private var profileImage: File? = null
    private val checker = PermissionChecker()
    private var imageUrl :String? = null

    @Inject
    lateinit var validation: Validation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        renderProfileImage()
        viewModelCollectors()
        onClicks()
    }

    private fun viewModelCollectors() {
        viewModel.apply {
            viewModelScope.launch {

                profileInfo.collect { response ->
                    when (response) {
                        is ProfileState.Idle -> binding.progressBar.gone()

                        is ProfileState.Success -> {
                            binding.progressBar.gone()
                            response.result?.data?.let { populateProfileData(it) }
                        }

                        is ProfileState.Loading -> binding.progressBar.visible()

                        is ProfileState.StateError -> {
                            binding.progressBar.gone()
                            CustomToaster.show(
                                context = requireContext(),
                                message = response.message
                                    ?: resources.getString(R.string.unknown_error),
                                isSuccess = false
                            )
                        }

                        is ProfileState.ServerError -> {
                            binding.progressBar.gone()
                            CustomToaster.show(
                                context = requireContext(),
                                message = response.error.getErrorMessage(requireContext()),
                                isSuccess = false
                            )
                        }

                        is ProfileState.InputError -> {
                            binding.progressBar.gone()
                            CustomToaster.show(
                                context = requireContext(),
                                message = getString(R.string.phone_number_error_message),
                                isSuccess = false
                            )
                        }
                    }
                }
            }

            viewModelScope.launch {

                updateProfile.collect { response ->
                    when (response) {
                        is ProfileState.Success -> {
                            binding.progressBar.gone()
                            response.result?.data?.let { populateProfileData(it) }
                            CustomToaster.show(
                                requireContext(),
                                requireContext().getString(R.string.success_message),
                                true
                            )
                            binding.scrool.visible()
                            binding.includeEdittext.root.gone()
                        }

                        is ProfileState.Loading -> binding.progressBar.visible()

                        is ProfileState.StateError -> {
                            binding.progressBar.gone()
                            CustomToaster.show(
                                context = requireContext(),
                                message = response.message
                                    ?: resources.getString(R.string.unknown_error),
                                isSuccess = false
                            )
                        }

                        is ProfileState.ServerError -> {
                            binding.progressBar.gone()
                            CustomToaster.show(
                                context = requireContext(),
                                message = response.error.getErrorMessage(requireContext()),
                                isSuccess = false
                            )
                        }

                        is ProfileState.InputError -> {
                            binding.progressBar.gone()
                            CustomToaster.show(
                                context = requireContext(),
                                message = getString(R.string.phone_number_error_message),
                                isSuccess = false
                            )
                        }

                        is ProfileState.Idle -> binding.progressBar.gone()
                    }
                }
            }
        }
    }

    private fun renderProfileImage() {
        if (profileImage != null)
            binding.includeEdittext.imageImageView.setImageURI(profileImage?.toUri())
        else
            binding.includeEdittext.imageImageView.setImageResource(R.drawable.ic_images_placeholder)
    }

    private fun onClicks() {
        binding.apply {
            backArrow.setOnClickListener {
                activity?.onBackPressed()
            }

            editTextView.setOnClickListener {
                binding.scrool.visibility = View.GONE
                binding.includeEdittext.root.visibility = View.VISIBLE

            }

            includeEdittext.apply {

                cancelButton.setOnClickListener {
                    binding.scrool.visible()
                    binding.includeEdittext.root.gone()
                }
                editEmailTextView.setOnClickListener {
                    showEmailDialog()
                }
                editPhoneTextView.setOnClickListener {
                    showPhoneDialog()
                }
                imageImageView.setOnClickListener {
                    if (checker.checkPermissionCamera(activity)) checker.askForPermissionCamera(
                        activity
                    )
                    else ImagePickerBottomSheet(communicator = imagePickerCommunicator, attachment = imageUrl?.toUri())
                        .show(childFragmentManager, ImagePickerBottomSheet.TAG)
                }
                continueButton.setOnClickListener {
                    val name = nameEditText.text
                    val commercialRegistrationNo = commercialRegistrationNoEditText.text
                    val tax = taxNumberEditText.text
                    val taxRecord = taxRecordEditText.text

                    if (name.isEmpty()) {
                        CustomToaster.show(
                            context = requireContext(),
                            message = getString(R.string.please_enter_valid_name),
                            isSuccess = false
                        )
                        return@setOnClickListener
                    } else if (commercialRegistrationNo.isEmpty() || commercialRegistrationNo.length < 10) {
                        commercialRegistrationNoEditText.setErrorMsg(getString(R.string.please_enter_valid_commercial_record))
                        return@setOnClickListener
                    } else if (tax.isEmpty()) {
                        taxNumberEditText.setErrorMsg(getString(R.string.please_enter_valid_tax))
                        return@setOnClickListener
                    } else if (taxRecord.isEmpty() || taxRecord.length < 15) {
                        taxRecordEditText.setErrorMsg(getString(R.string.please_enter_valid_tax_record))
                        return@setOnClickListener
                    } else {

                        viewModel.profileUpdate(
                            commercialRecordName = name,
                            logo = profileImage,
                            commercialRecord = commercialRegistrationNo,
                            tax = tax,
                            taxRecord = taxRecord
                        )
                    }

                }
            }
        }
    }

    private fun showSnackBar(message: Int) {
        Snackbar.make(
            binding.root,
            getString(message),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun populateProfileData(user: User) {

        Glide.with(requireContext())
            .load(user.accountInfo?.logo)
            .placeholder(R.drawable.ic_images_placeholder)
            .error(R.drawable.ic_images_placeholder)
            .into(binding.userImageView)

        binding.apply { // render for view
            binding.phoneTv.text = user.phone
            binding.emailTv.text = user.email
            binding.nameTv.text = user.accountInfo?.commercialRecordName
            binding.commercialRegistrationNoTv.text = user.accountInfo?.commercialRecord
            binding.taxNumberTv.text = user.accountInfo?.tax
            binding.taxRecordNumberTv.text = user.accountInfo?.taxRecord
        }

        imageUrl = user.accountInfo?.logo

        binding.includeEdittext.apply { // render for edit

            Glide.with(requireContext())
                .load(user.accountInfo?.logo)
                .placeholder(R.drawable.ic_images_placeholder)
                .error(R.drawable.ic_images_placeholder)
                .into(binding.includeEdittext.imageImageView)


            phoneLayout.phoneNumberEditText.setText(user.phone)
            phoneLayout.phoneNumberEditText.isEnabled = false

            emailEditText.setText(user.email)
            emailEditText.isEnabled = false

            user.accountInfo?.let {
                commercialRegistrationNoEditText.binding.editText.setText(it.commercialRecord)
                nameEditText.binding.editText.setText(it.commercialRecordName)
                taxNumberEditText.binding.editText.setText(it.tax)
                taxRecordEditText.binding.editText.setText(it.taxRecord)
            }
        }
    }

    private fun showEmailDialog() {
        emailDialog?.dismiss()
        emailDialog = EmailDialog(requireContext(), optionsCommunicator, validation)
        emailDialog?.show()
    }

    private fun showPhoneDialog() {
        phoneDialog?.dismiss()
        phoneDialog = PhoneDialog(requireContext(), optionsPhoneCommunicator, validation)
        phoneDialog?.show()
    }

    private val optionsCommunicator = object : EmailDialog.Communicator {

        override fun onContinueClicked(email: String) {
            if (findNavController().currentDestination?.id == R.id.navigation_profileFragment) {
                val action =
                    ProfileFragmentDirections.actionProfileFragmentToOtpEmailFragment(
                        binding.phoneTv.text.toString(),
                        binding.emailTv.text.toString(),
                        email
                    )
                findNavController().navigate(action)
            }
        }
    }

    private val optionsPhoneCommunicator = object : PhoneDialog.Communicator {
        override fun onContinueClicked(phone: String) {
            if (findNavController().currentDestination?.id == R.id.navigation_profileFragment) {
                val action =
                    ProfileFragmentDirections.actionProfileFragmentToOtpUpdateProfile(
                        binding.phoneTv.text.toString(),
                        binding.emailTv.text.toString(),
                        phone
                    )
                findNavController().navigate(action)
            }
        }
    }

    private val imagePickerCommunicator = object : ImagePickerBottomSheet.Communicator {
        override fun setImage(file: File) {
            profileImage = file
            binding.includeEdittext.imageImageView.setImageURI(file.toUri())
        }
    }
}