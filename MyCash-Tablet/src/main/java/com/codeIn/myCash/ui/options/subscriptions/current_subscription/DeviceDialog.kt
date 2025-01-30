package com.codeIn.myCash.ui.options.subscriptions.current_subscription

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogChangeDeviceBinding

class DeviceDialog (
    context: Context,
    private val communicator: Communicator
) :
    Dialog(context, R.style.PauseDialog){
    private lateinit var binding: DialogChangeDeviceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogChangeDeviceBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.currentDeviceTv.setOnClickListener {
            communicator.changeCurrentDevice(false)
            dismiss()
        }
        binding.chooseNewDevice.setOnClickListener {
            communicator.changeCurrentDevice(true)
            dismiss()
        }

    }

    interface Communicator {
        fun changeCurrentDevice(change : Boolean= true )
    }
}