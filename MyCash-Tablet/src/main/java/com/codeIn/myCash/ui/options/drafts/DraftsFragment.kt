package com.codeIn.myCash.ui.options.drafts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.DraftsType
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentDraftsBinding
import com.codeIn.myCash.ui.updateSectionsViews
import com.codeIn.myCash.utilities.bottom_sheets.WarningBottomSheet
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass. This fragment shows the user a list of his drafts. The user can filter the drafts by type (ALL, PRODUCTS, INVOICES, CLIENTS).
 * The user can also delete all the drafts.
 * @see DraftsViewModel
 * @see DraftsAdapter
 * @see DraftsType
 */
@AndroidEntryPoint
class DraftsFragment : Fragment() {

    private val viewModel: DraftsViewModel by viewModels()

    private var _binding: FragmentDraftsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDraftsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        //initiate the Adapter and attach it to the RecyclerView
        val adapter = DraftsAdapter(draftsCommunicator)
        binding.draftsRecycleView.adapter = adapter

        //listen to user clicks on the draftsType buttons
        binding.apply {
            val typeViewMap = mapOf(
                allTextView to DraftsType.ALL,
                productsTextView to DraftsType.PRODUCTS,
                invoicesTextView to DraftsType.INVOICES,
                clientsTextView to DraftsType.CLIENTS,
                usersTextView to DraftsType.USERS
            )

            typeViewMap.forEach { (view, type) ->
                view.setOnClickListener {
                    viewModel.setDraftsType(type)
                }
            }

            deleteButton.setOnClickListener {
                deleteAllDrafts()
            }
        }


        //observe the draftsType and update the UI accordingly
        viewModel.apply {
            draftsType.observe(viewLifecycleOwner) {
                updateDraftsType(it)
            }
            draftsList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }


    }


    /**
     * DraftsAdapter communicator to handle the user clicks on the drafts items..
     */
    private val draftsCommunicator = object : DraftsAdapter.Communicator {
        override fun onClick(draft: Draft) {

        }

        override fun deleteDraft(draft: Draft) {
            viewModel.deleteDraft(draft)
        }
    }

    /**
     * function to update the UI based on the selected filter (draftsType).
     * @param draftsType [DraftsType] the new value of the draftsType, it can be ALL, PRODUCTS, INVOICES or CLIENTS.
     * @return [Unit]
     * @see DraftsType
     */
    private fun updateDraftsType(draftsType: DraftsType) {

        // Apply secondary style to all views initially
        val viewsToStyle = listOf(
            binding.allTextView,
            binding.productsTextView,
            binding.invoicesTextView,
            binding.clientsTextView,
            binding.usersTextView
        )

        // Apply primary style based on the selected filter
        val selectedView = when (draftsType) {
            DraftsType.ALL -> binding.allTextView
            DraftsType.PRODUCTS -> binding.productsTextView
            DraftsType.INVOICES -> binding.invoicesTextView
            DraftsType.CLIENTS -> binding.clientsTextView
            DraftsType.USERS -> binding.usersTextView
        }

        updateSectionsViews(
            context = requireContext(),
            viewsToStyle = viewsToStyle,
            selectedView = selectedView
        )
    }


    //the warning bottom sheet to show when the user clicks on the delete all drafts button
    //we need to keep a reference to it to dismiss it when the user clicks multiple times on the delete all drafts button.
    //if we don't dismiss it, it will be shown multiple times.
    private var warningBottomSheet: WarningBottomSheet? = null

    /**
     * function to show a warning bottom sheet to the user when he clicks on the delete all drafts button.
     * @return [Unit]
     */
    private fun deleteAllDrafts() {
        warningBottomSheet?.dismiss()
        warningBottomSheet = WarningBottomSheet(
            title = getString(R.string.delete_drafts),
            message = getString(R.string.delete_drafts_message),
            communicator = deleteAllDraftsCommunicator
        )
        warningBottomSheet?.show(childFragmentManager, WarningBottomSheet.TAG)
    }

    /**
     * WarningBottomSheet communicator to handle the user clicks on the warning bottom sheet buttons.
     * @see WarningBottomSheet
     */
    private val deleteAllDraftsCommunicator = object : WarningBottomSheet.Communicator {
        override fun confirm() {
            viewModel.deleteAllDrafts()
        }
    }

}