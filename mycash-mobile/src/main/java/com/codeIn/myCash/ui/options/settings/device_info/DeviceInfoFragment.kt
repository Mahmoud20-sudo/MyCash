package com.codeIn.myCash.ui.options.settings.device_info

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentDeviceInfoBinding
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.User
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DeviceInfoFragment : Fragment() {
    private var _binding: FragmentDeviceInfoBinding? = null
    private val viewModel: DeviceInfoViewModel by viewModels()
    private val binding get() = _binding!!

    @Inject
    lateinit var prefs : SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            backArrow.setOnClickListener {
                requireActivity().onBackPressed()
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


    private fun handleSuccess(user : User?){
        Glide.with(this)
            .load(user?.subscription?.device?.device?.image)
            .error(R.drawable.icon_app)
            .into(binding.deviceImage)

        binding.deviceBrand.text = user?.subscription?.device?.device?.brand?.name
        binding.deviceName.text = user?.subscription?.device?.device?.name
        val adapter = FeaturesAdapter()
        binding.features.adapter = adapter
        adapter.submitList(user?.subscription?.device?.device?.deviceFeatures)
    }

}