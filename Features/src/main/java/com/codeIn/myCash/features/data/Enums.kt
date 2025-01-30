package com.codeIn.myCash.features.data

/**
 * enum class for the different invoice filters
 * with values RETURNS(1), PAYMENT_COMPLETED(1), PAYMENT_NOT_COMPLETED(0), ALL(0)
 */
enum class InvoiceFilter(val value:Int) {
    RETURNED(1),
    PAYMENT_COMPLETED(1),
    PAYMENT_NOT_COMPLETED(0),
    ALL(0);
}

/**
 * enum class for the different invoice sections
 * with values PURCHASES(1), SALES(2)
 */
enum class InvoiceSection(val value:Int) {
    PURCHASES(1),
    SALES(2);
}

/**
 * enum class for the different Clients sections
 * with values CLIENTS(1), VENDORS(2)
 */
enum class ClientsSection(val value:Int) {
    CLIENTS(1),
    VENDORS(2);
}

/**
 * enum class for the different Clients filters
 * with values PAYMENT_COMPLETED(1), INSTALLMENT_PAYMENT(2), ALL(0)
 */
enum class ClientsFilter(val value:Int) {
    PAYMENT_COMPLETED(1),
    INSTALLMENT_PAYMENT(2),
    ALL(0);
}

/**
 * enum class for the different Reports sections
 * with values SALES(1), PRODUCTS(2), PURCHASES(3), TAXES(4)
 */
enum class ReportsSection(val value:Int) {
    SALES(1),
    PRODUCTS(2),
    PURCHASES(3),
    TAXES(4);
}

/**
 * enum class for the different Reports filters
 * with values DAILY(1), WEEKLY(2), MONTHLY(3), YEARLY(4)
 */
enum class ReportsFilter(val value:Int) {
    DAILY(1),
    WEEKLY(2),
    MONTHLY(3),
    YEARLY(4);
}

/**
 * enum class for the different Transactions Filters
 * with values DAILY(1), WEEKLY(2), MONTHLY(3), YEARLY(4)
 */
enum class TransactionsFilter(val value:Int) {
    DAILY(1),
    WEEKLY(2),
    MONTHLY(3),
    YEARLY(4);
}


/**
 * enum class for the notifications types that the user can view
 * with values ALL(0), UNSEEN(1)
 */
enum class NotificationsType(val value:Int){
    ALL(0),
    UNSEEN(1)
}


/**
 * enum class for the different subscriptions options that the user can view
 * with values CURRENT_PACKAGE(1), ALL_PACKAGES(2)
 */
enum class SubscriptionsOptions(val value:Int){
    CURRENT_PACKAGE(1),
    ALL_PACKAGES(2)
}

enum class InvalidInput {
    EMAIL, PASSWORD, CONFIRM_PASSWORD, PHONE_EGYPT, PHONE_SAUDI , NAME , EMPTY , NONE
}

/**
 * enum class for the different drafts types that the user can view (all drafts, products drafts, invoices drafts, clients drafts)
 */
enum class DraftsType(val value:Int){
    ALL(0),
    PRODUCTS(1),
    INVOICES(2),
    CLIENTS(3),
    USERS(4)
}

enum class PaymentCode(val value:Int){
    GEIDEA_PURCHASE(0),
    NEWLEAB_PURCHASE(1),
    SUREPAY_PURCHASE(2),
    GEIDEA_REFUND(3),
    NEWLEAB_REFUND(4),
    SUREPAY_REFUND(5)
}

enum class AuthType(val value: Int) {
    PHONE(1),
    EMAIL(2)
}