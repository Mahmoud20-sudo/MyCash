package com.codeIn.myCash.ui.authentication.sign_up.subscriptions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codeIn.myCash.features.user.data.settings.remote.response.subscription.Package
import com.codeIn.myCash.databinding.FragmentSubscriptionItemBinding
import com.google.gson.Gson

class SubscriptionItemFragment : Fragment() {

    private var _binding: FragmentSubscriptionItemBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscriptionItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parentFragment =
            (requireParentFragment() as SubscriptionsSwipePagerAdapter.Communicator)

        var newPackage: Package? = null
        arguments?.takeIf { it.containsKey(PACKAGE) }?.apply {
            newPackage = Gson().fromJson(getString(PACKAGE), Package::class.java)
        }

        newPackage?.apply {
            binding.nameTextView.text = this.name

            binding.finalPriceTextView.text = this.finalPrice

            val durationText = "/${this.duration}"

            binding.durationTextView.text = durationText

            val adapter = FeaturesAdapter()
            adapter.submitList(this.features)
            binding.featuresRecycleView.adapter = adapter

            binding.startNowButton.setOnClickListener {
                parentFragment.startPackage(this)
            }
        }
    }

    companion object {
        const val PACKAGE = "package"
    }

}