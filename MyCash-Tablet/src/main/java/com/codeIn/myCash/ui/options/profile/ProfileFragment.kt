package com.codeIn.myCash.ui.options.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.util.PermissionChecker
import com.codeIn.common.util.Validation
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentProfileBinding
import com.codeIn.myCash.ui.options.profile.dialog.EmailDialog
import com.codeIn.myCash.ui.options.profile.dialog.PhoneDialog
import com.codeIn.myCash.utilities.pickers.ImagePickerBottomSheet
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
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

        if (profileImage != null)
            binding.includeEdittext.imageImageView.setImageURI(profileImage?.toUri())
        else
            binding.includeEdittext.imageImageView.setImageResource(R.drawable.ic_images_placeholder)

        binding.apply {
            backArrow.setOnClickListener {
                activity?.onBackPressed()
            }
            binding.includeEdittext.cancelButton.setOnClickListener {
                binding.scrool.gone()
                binding.includeEdittext.root.visible()
            }

            editTextView.setOnClickListener {
                binding.scrool.visibility = View.GONE
                binding.includeEdittext.root.visibility = View.VISIBLE

            }

            includeEdittext.cancelButton.setOnClickListener {
                binding.scrool.visibility = View.VISIBLE
                binding.includeEdittext.root.visibility = View.GONE
            }
            includeEdittext.editEmailTextView.setOnClickListener {
                showEmailDialog()
            }
            includeEdittext.editPhoneTextView.setOnClickListener {
                showPhoneDialog()
            }
            includeEdittext.imageImageView.setOnClickListener {
                if (checker?.checkPermissionCamera(activity) == true) {
                    checker?.askForPermissionCamera(activity)
                } else {
                    ImagePickerBottomSheet(communicator = imagePickerCommunicator).show(
                        childFragmentManager,
                        ImagePickerBottomSheet.TAG
                    )
                }

            }

            binding.includeEdittext.continueButton.setOnClickListener {
                val commercialRecord = binding.includeEdittext.nameEditText.text.toString()
                val commercialRegistrationNo =
                    binding.includeEdittext.commercialRegistrationNoEditText.text.toString()
                val taxNumberTextString = binding.includeEdittext.taxNumberEditText.text.toString()
                val taxRecord = binding.includeEdittext.taxRecordEditText.text.toString()

                if (commercialRecord.isEmpty()) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.please_enter_valid_name),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else if (commercialRegistrationNo.isEmpty()) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.please_enter_valid_commercial_record),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else if (taxNumberTextString.isEmpty() && taxNumberTextString.length <= 9) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.please_enter_valid_tax_record),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else if (taxRecord.isEmpty()) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.please_enter_valid_tax),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else {
                    viewModel.profileUpdate(
                        commercialRecordName = commercialRecord,
                        logo = profileImage,
                        commercialRecord = commercialRegistrationNo,
                        tax = taxNumberTextString,
                        taxRecord = taxRecord,
                        context = requireContext()
                    )
                }


            }


        }

        viewModel.apply {
            dataResult.collectOnLifecycle(
                viewModelScope,
                viewLifecycleOwner
            ) { response ->
                when (response) {
                    is AuthenticationState.Loading -> {}
                    is AuthenticationState.Idle -> {}
                    is AuthenticationState.StateError -> {}
                    is AuthenticationState.Success -> {
                        Log.d("TAG", "Hello in profile $response")
                        binding.phoneTv.text = response.data?.phone

                        binding.emailTv.text = response.data?.email
                        binding.nameTv.text = response.data?.accountInfo?.commercialRecordName
                        binding.commercialRegistrationNoTv.text =
                            response.data?.accountInfo?.commercialRecord
                        binding.taxNumberTv.text = response.data?.accountInfo?.tax
                        binding.taxRecordNumberTv.text = response.data?.accountInfo?.taxRecord

                        Glide.with(requireContext())
                            .load(response.data?.accountInfo?.logo)
                            .placeholder(R.drawable.ic_images_placeholder)
                            .error(R.drawable.ic_images_placeholder)
                            .into(binding.userImageView)

                        binding.apply {
                            includeEdittext.phoneLayout.phoneNumberEditText.setText(response.data?.phone)
                            includeEdittext.emailEditText.setText(response.data?.email)

                            includeEdittext.phoneLayout.phoneNumberEditText.isEnabled = false
                            includeEdittext.emailEditText.isEnabled = false

                            includeEdittext.commercialRegistrationNoEditText.setText(
                                response.data?.accountInfo?.commercialRecord ?: ""
                            )
                            Glide.with(requireContext())
                                .load(response.data?.accountInfo?.logo)
                                .placeholder(R.drawable.ic_images_placeholder)
                                .error(R.drawable.ic_images_placeholder)
                                .into(binding.includeEdittext.imageImageView)
                            includeEdittext.nameEditText.setText(response.data?.accountInfo?.commercialRecordName)

                            includeEdittext.taxNumberEditText.setText(
                                response.data?.accountInfo?.tax ?: ""
                            )
                            includeEdittext.taxRecordEditText.setText(
                                response.data?.accountInfo?.taxRecord ?: ""
                            )

                        }
                    }

                    is AuthenticationState.InputError -> {}
                    is AuthenticationState.ServerError -> {}
                    else -> {}
                }
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