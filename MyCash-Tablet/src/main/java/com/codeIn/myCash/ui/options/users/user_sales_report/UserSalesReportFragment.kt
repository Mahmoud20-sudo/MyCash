package com.codeIn.myCash.ui.options.users.user_sales_report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.codeIn.myCash.databinding.FragmentUserSalesReportBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserSalesReportFragment : Fragment() {

    private val viewModel: UserSalesReportsViewModel by viewModels()

    private var _binding: FragmentUserSalesReportBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserSalesReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val salesReportsAdapter = SalesReportsAdapter(salesReportsCommunicator)

        binding.apply {
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            salesReportRecyclerView.adapter = salesReportsAdapter
        }

        viewModel.apply {
            salesReports.observe(viewLifecycleOwner) { salesReportsAdapter.submitList(it) }
        }
    }


    private val salesReportsCommunicator = object : SalesReportsAdapter.Communicator {
        override fun onClick(salesReport: SalesReport) {

        }
    }

}