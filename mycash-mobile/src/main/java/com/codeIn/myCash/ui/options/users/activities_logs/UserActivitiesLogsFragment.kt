package com.codeIn.myCash.ui.options.users.activities_logs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.codeIn.myCash.databinding.FragmentUserActivitiesLogsBinding
import com.codeIn.myCash.utilities.pickers.DatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserActivitiesLogsFragment : Fragment() {

    private val viewModel: UserActivitiesLogsViewModel by viewModels()
    private val datePicker = DatePicker()

    private var _binding: FragmentUserActivitiesLogsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserActivitiesLogsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activitiesLogsAdapter = ActivitiesLogsAdapter(activitiesLogsCommunicator)
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
            activitiesLogsRecyclerView.adapter = activitiesLogsAdapter
        }

        viewModel.apply {
            activityLogs.observe(viewLifecycleOwner) { activitiesLogsAdapter.submitList(it) }
        }
    }


    private val activitiesLogsCommunicator = object : ActivitiesLogsAdapter.Communicator {
        override fun onClick(activityLog: ActivityLog) {
            val action =
                com.codeIn.myCash.ui.options.users.activities_logs.UserActivitiesLogsFragmentDirections.actionNavUserActivitiesLogsFragmentToNavUserActivityLogDetailsFragment(
                    activityLog
                )
            findNavController().navigate(action)
        }
    }

}