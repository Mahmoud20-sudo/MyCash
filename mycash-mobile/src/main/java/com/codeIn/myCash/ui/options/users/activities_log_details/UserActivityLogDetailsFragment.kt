package com.codeIn.myCash.ui.options.users.activities_log_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.codeIn.myCash.databinding.FragmentUserActivityLogDetailsBinding
import com.codeIn.myCash.utilities.pickers.DatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserActivityLogDetailsFragment : Fragment() {

    private val viewModel: UserActivitiesLogsDetailsViewModel by viewModels()
    private val datePicker = DatePicker()

    private var _binding: FragmentUserActivityLogDetailsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserActivityLogDetailsBinding.inflate(inflater, container, false)
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
                findNavController().popBackStack()
            }
            dateEditText.setOnClickListener {
                datePicker.showDatePicker(
                    childFragmentManager = parentFragmentManager,
                    editText = binding.dateEditText,
                    preventFutureDates = true
                )
            }
        }

        viewModel.apply {
            activityLog.observe(viewLifecycleOwner) { activityLog ->
                binding.apply {
                    nameTextView.text = activityLog.name
                    dateTextView.text = activityLog.date
                    branchTextView.text = activityLog.branch
                    totalSalesTextView.text = activityLog.totalSales

                    beginningDateTextView.text = activityLog.beginDay?.time
                    beginningCashTextView.text = activityLog.beginDay?.cash
                    beginningCreditCardTextView.text = activityLog.beginDay?.credit

                    endingDateTextView.text = activityLog.endDay?.time
                    endingCashTextView.text = activityLog.endDay?.cash
                    endingCreditCardTextView.text = activityLog.endDay?.credit

                    totalSalesTextView2.text = activityLog.totalSales
                }
            }
        }
    }
}