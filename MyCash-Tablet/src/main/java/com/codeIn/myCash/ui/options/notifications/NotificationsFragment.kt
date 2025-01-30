package com.codeIn.myCash.ui.options.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.NotificationsType
import com.codeIn.myCash.databinding.FragmentNotificationsBinding
import com.codeIn.myCash.ui.updateSectionsViews
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private val viewModel: NotificationsViewModel by viewModels()

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set the click listener for the back arrow
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        //set the click listeners for the notifications type buttons
        binding.apply {
            allTextView.setOnClickListener {
                viewModel.updateNotificationsType(NotificationsType.ALL)
            }
            unseenTextView.setOnClickListener {
                viewModel.updateNotificationsType(NotificationsType.UNSEEN)
            }
        }

        val notificationsAdapter = NotificationsAdapter(notificationsCommunicator)
        binding.notificationsRecyclerView.adapter = notificationsAdapter


        //observe the notifications type and update the UI accordingly
        viewModel.apply {
            notificationsType.observe(viewLifecycleOwner) {
                updateNotificationType(it)
            }
            notificationsList.observe(viewLifecycleOwner) {
                notificationsAdapter.submitList(it)
            }
        }


    }


    private val notificationsCommunicator = object : NotificationsAdapter.Communicator {
        override fun onClick(notification: Notification) {

        }

        override fun deleteNotification(notification: Notification) {

        }
    }


    /**
     * function to update the notifications type that is called to update the UI when the user updates the notifications type
     * it takes the new type as a parameter and updates the UI with the new vendorsData
     * @param notificationsType [NotificationsType] the new type that the user is viewing
     * @return [Unit]
     */
    private fun updateNotificationType(notificationsType: NotificationsType) {

        // Apply secondary style to all views initially
        val viewsToStyle = listOf(
            binding.allTextView,
            binding.unseenTextView
        )

        // Apply primary style based on the selected filter
        val selectedView = when (notificationsType) {
            NotificationsType.ALL -> binding.allTextView
            NotificationsType.UNSEEN -> binding.unseenTextView
        }

        updateSectionsViews(
            context = requireContext(),
            viewsToStyle = viewsToStyle,
            selectedView = selectedView
        )
    }


}