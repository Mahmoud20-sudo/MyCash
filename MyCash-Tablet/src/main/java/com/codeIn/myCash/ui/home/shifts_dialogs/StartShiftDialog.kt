package com.codeIn.myCash.ui.home.shifts_dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.data.NumberHelper
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogShiftStartBinding
import com.codeIn.myCash.ui.authentication.showError
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.User
import com.codeIn.myCash.utilities.CustomToaster

class StartShiftDialog (
    context: Context,
    private var userModel : User?,
    private val listener: ShiftListener
)  : Dialog(context, R.style.PauseDialog){

    private lateinit var binding: DialogShiftStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogShiftStartBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.apply {
            startButton.setOnClickListener {
                listener.startShift(cashEditText.text.toString() ,
                    cardEditText.text.toString() ,
                    differentCashEditText.text.toString() ,
                    differentCardEditText.text.toString() ,
                    )
            }

            cashEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                    differentCashEditText.text =
                        NumberHelper.persianToEnglishText(userModel?.lastShift?.endCash?: "0.0")
                }
                override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                    if (charSequence.isNotEmpty()){
                        val currentValue = NumberHelper.persianToEnglishText(
                            charSequence.toString()?:"0.0"
                        )
                        if (currentValue.toDouble()<= NumberHelper.persianToEnglishText(
                                userModel?.lastShift?.endCash?:"0.0").toDouble())
                        {
                            val dif = NumberHelper.persianToEnglishText(
                                userModel?.lastShift?.endCash?:"0.0"
                            ).toDouble() - currentValue.toDouble()

                            differentCashEditText.text = NumberHelper.persianToEnglishText(dif.toString())
                        }
                        else
                        {
                            differentCashEditText.text =
                                NumberHelper.persianToEnglishText(userModel?.lastShift?.endCash?: "0.0")
                            cashEditText.setText("0.0")
                            CustomToaster.show(context , context.getString(R.string.please_enter_valid_cash_money) , false )
                        }
                    }
                    else {
                        differentCashEditText.text =
                            NumberHelper.persianToEnglishText(userModel?.lastShift?.endCash?: "0.0")
                    }
                }
                override fun afterTextChanged(editable: Editable) {}
            })
            cardEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                    differentCardEditText.text =
                        NumberHelper.persianToEnglishText(userModel?.lastShift?.endVisa?: "0.0")
                }
                override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                    if (charSequence.isNotEmpty()){
                        val currentValue = NumberHelper.persianToEnglishText(
                            charSequence.toString()?:"0.0"
                        )
                        if (currentValue.toDouble()<= NumberHelper.persianToEnglishText(
                                userModel?.lastShift?.endVisa?:"0.0").toDouble())
                        {
                            val dif = NumberHelper.persianToEnglishText(
                                userModel?.lastShift?.endVisa?:"0.0"
                            ).toDouble() - currentValue.toDouble()

                            differentCardEditText.text = NumberHelper.persianToEnglishText(dif.toString())
                        }
                        else
                        {
                            differentCardEditText.text =
                                NumberHelper.persianToEnglishText(userModel?.lastShift?.endVisa?: "0.0")
                            cardEditText.setText("0.0")
                            CustomToaster.show(context , context.getString(R.string.please_enter_valid_cash_money) , false )
                        }
                    }
                    else {
                        differentCardEditText.text =
                            NumberHelper.persianToEnglishText(userModel?.lastShift?.endVisa?: "0.0")
                    }
                }
                override fun afterTextChanged(editable: Editable) {}
            })
            val endCash = userModel?.lastShift?.endCash ?: "0.0"
            val endVisa = userModel?.lastShift?.endVisa ?: "0.0"
            cash.text = "${context.resources.getString(R.string.cash_money_value)} $endCash ${userModel?.country?.currency}"
            card.text = "${context.resources.getString(R.string.card_money_value)} $endVisa ${userModel?.country?.currency}"

            nameTextView.text = userModel?.name?: context.resources.getString(R.string.unknown)
            when(userModel?.type){
                "1" -> authorityTextView.text = context.resources.getString(R.string.Admin)
                "2" -> authorityTextView.text = context.resources.getString(R.string.Employee)
            }

            val endDate = userModel?.lastShift?.endDate ?: " - "
            val endTime = userModel?.lastShift?.statistics?.endTime ?: ""

            timeTextView.text = "$endDate  $endTime"
            branchTextView.text = userModel?.mainBranch?.name?: context.resources.getString(R.string.unknown)
            differentCardEditText.text = userModel?.lastShift?.endVisa?:"0.0"
            differentCashEditText.text = userModel?.lastShift?.endCash?:"0.0"
        }

        setCanceledOnTouchOutside(false)
    }

    fun setData(userModel : User?,){
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
            InvalidInput.DIFFERENT_CASH -> {
                binding.differentCashEditText.showError()
                binding.differentCashEditText.error = this.context.resources.getString(R.string.please_enter_valid_cash_money)
            }
            InvalidInput.DIFFERENT_VISA -> {
                binding.differentCardEditText.showError()
                binding.differentCardEditText.error = this.context.resources.getString(R.string.please_enter_valid_visa_money)
            }
            else -> {}
        }
    }
}