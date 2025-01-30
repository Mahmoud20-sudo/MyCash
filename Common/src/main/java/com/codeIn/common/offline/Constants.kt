package com.codeIn.common.offline

class Constants {
    companion object {

        const val MODE = "mode"
        const val LANG = "lang"
        const val QUICK_INVOICE_MODE = "quick_invoice_mode"

        fun getToken(): String {
            return "tokenUser"
        }
        fun getPhone(): String {
            return "phoneUser"
        }
        fun getImage(): String {
            return "imageUser"
        }
        fun getPassword(): String {
            return "passwordUser"
        }
        fun isRememberMe(): String {
            return "0"
        }
        fun getEmail(): String {
            return "emailUser"
        }
        fun getLang(): String {
            return "lang"
        }
        fun getCurrency(): String {
            return "currency"
        }
        fun isSaved(): String {// if 1 is saved , 0 is not saved
            return "isSaved"
        }
        fun idSelectedCountry(): String {
            return "idSelectedCountry"
        }
        // Now we will write keys of registration ...
        fun registerSubscriptionID(): String {
            return "registerSubscriptionID"
        }
        fun registerSubscriptionPrice(): String {
            return "registerSubscriptionPrice"
        }
        fun registerSubscriptionDeviceId(): String {
            return "registerSubscriptionDeviceId"
        }
        fun registerSubscriptionDevicePrice(): String {
            return "registerSubscriptionDevicePrice"
        }
        fun registerToken(): String {
            return "registerToken"
        }
        fun registerEmail(): String {
            return "registerEmail"
        }
        fun registerPhone(): String {
            return "registerPhone"
        }
        fun registerPassword(): String {
            return "registerPassword"
        }
        fun registerCountryID(): String {
            return "registerCountryID"
        }
        fun registerAcceptConditions(): String { // 1 is accept , 0 is refuse
            return "registerAcceptConditions"
        }
        // Forget password
        fun forgetToken(): String {
            return "forgetToken"
        }
        fun forgetType(): String {
            return "forgetType"
        }
        fun forgetKey(): String {
            return "forgetKey"
        }
        fun taxValue(): String {
            return "taxValue"
        }
        fun nameStore(): String {
            return "nameStore"
        }
        fun logoStore(): String {
            return "logoStore"
        }

        fun taxRecordStore(): String {
            return "taxRecord"
        }
        fun quickInvoiceMode(): String {
            return "quickInvoiceMode"
        }

        fun expenseStore(): String {
            return "expenseStore"
        }

        fun completeInoStore(): String {
            return "completeInoStore"
        }

        fun showNewInvoiceButton(): String {
            return "showNewInvoiceButton"
        }
        fun paymentStatus(): String {
            return "paymentStatus"
        }

        fun branchIdStore(): String {
            return "branchIdStore"
        }
    }
}