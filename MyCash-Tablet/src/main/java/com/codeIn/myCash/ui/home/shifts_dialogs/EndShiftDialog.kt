package com.codeIn.myCash.ui.home.shifts_dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.codeIn.common.data.InvalidInput
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogShiftEndBinding
import com.codeIn.myCash.ui.authentication.showError
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.User
import com.codeIn.myCash.features.user.data.shift.remote.reponse.ShiftData

class EndShiftDialog (
    context: Context,
    private var shiftData : ShiftData?,
    private var userModel : User?,
    private val listener: ShiftListener
) : Dialog(context, R.style.PauseDialog) {

    private lateinit var binding: DialogShiftEndBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogShiftEndBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.apply {
                endButton.setOnClickListener {
                    listener.endShift(cashEditText.text.toString() , cardEditText.text.toString())
                }
                cash.text = "${context.resources.getString(R.string.cash_money_value)} ${shiftData?.startCash} ${userModel?.country?.currency}"
                card.text = "${context.resources.getString(R.string.card_money_value)} ${shiftData?.startVisa} ${userModel?.country?.currency}"

                nameTextView.text = userModel?.accountInfo?.commercialRecordName ?: context.resources.getString(R.string.unknown)
                when(userModel?.type){
                    "1" -> authorityTextView.text = context.resources.getString(R.string.Admin)
                    "2" -> authorityTextView.text = context.resources.getString(R.string.Employee)
                }

            val endTime = userModel?.lastShift?.statistics?.startTime ?: ""

            timeTextView.text = shiftData?.startDate+" "+ endTime
            branchTextView.text = userModel?.mainBranch?.name?: context.resources.getString(R.string.unknown)

        }
        setCanceledOnTouchOutside(false)
    }
    fun setData(shiftData : ShiftData?, userModel : User?,){
        this.shiftData = shiftData
        this.userModel = userModel
    }

    fun setError(invalidInput: InvalidInput){
        when(invalidInput){
            InvalidInput.CASH -> {
                binding.cashEditText.showError()
                binding.cashEditText.error = this.context.resources.getString(R.string.please_enter_valid_cash_money)
            }
            InvalidInput.VISA -> {
                binding.cardEditText.showError()
                binding.cardEditText.error = this.context.resources.getString(R.string.please_enter_valid_visa_money)
            }
            else -> {}
        }
    }

}