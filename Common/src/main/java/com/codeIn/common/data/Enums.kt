package com.codeIn.common.data

/**
 * enum class for the different invoice filters
 * with values RETURNS(1), PAYMENT_COMPLETED(1), PAYMENT_NOT_COMPLETED(0), ALL(0)
 */
enum class InvoiceFilter(val value: Int) {
    TAX_INVOICE(2),
    SIMPLE_INVOICE(1),
    INSTANT_INVOICE(2),
    PURCHASE_INVOICE(2),
    SALE_INVOICE(1),
    PURCHASE_RETURNED(4),
    SALES_RETURNED(1),
    PAYMENT_COMPLETED(1),
    PAYMENT_NOT_COMPLETED(2),
    ALL(0);

    companion object {
        infix fun from(value: Int): InvoiceFilter =
            entries.firstOrNull { it.value == value } ?: TAX_INVOICE
    }
}


enum class ReceiptStatusFilter(val value: Int) {
    PAYMENT_NOT_COMPLETED(2),
    PAYMENT_COMPLETED(1),
    ALL(0);
}


/**
 * enum class for the different invoice sections
 * with values PURCHASES(1), SALES(2)
 */
enum class InvoiceSection(val value: Int) {
    PURCHASES(1),
    SALES(2);
}

/**
 * enum class for the different Clients sections
 * with values CLIENTS(1), VENDORS(2)
 */
enum class ClientsSection(val value: Int) {
    CLIENTS(1),
    VENDORS(2);
}

/**
 * enum class for the different Clients filters
 * with values PAYMENT_COMPLETED(1), INSTALLMENT_PAYMENT(2), ALL(0)
 */
enum class ClientsFilter(val value: Int) {
    PAYMENT_COMPLETED(1),
    INSTALLMENT_PAYMENT(2),
    ALL(0);
}

/**
 * enum class for the different Reports sections
 * with values SALES(1), PRODUCTS(2), PURCHASES(3), TAXES(4)
 */
enum class ReportsType(val value: Int) {
    SALES(1),
    PRODUCTS(2),
    INVENTORIES(3),
    TAXES(4);

    companion object {
        infix fun from(value: Int): ReportsType =
            entries.firstOrNull { it.value == value } ?: SALES
    }
}

/**
 * enum class for the different Reports filters
 * with values DAILY(1), WEEKLY(2), MONTHLY(3), YEARLY(4)
 */
enum class ReportsFilter(val value: Int) {
    DAILY(1),
    WEEKLY(2),
    MONTHLY(3),
    YEARLY(4);
}

/**
 * enum class for the different Transactions Filters
 * with values DAILY(1), WEEKLY(2), MONTHLY(3), YEARLY(4)
 */
enum class ExpensesFilter(val value: Int) {
    DAILY(1),
    WEEKLY(2),
    MONTHLY(3),
    YEARLY(4);
}

enum class ReceiptsFilter(val value: Int) {
    All(0),
    COMPLETED(1),
    UN_COMPLETED(2),
}


/**
 * enum class for the notifications types that the user can view
 * with values ALL(0), UNSEEN(1)
 */
enum class NotificationsType(val value: Int) {
    ALL(0),
    UNSEEN(1)
}


/**
 * enum class for the different subscriptions options that the user can view
 * with values CURRENT_PACKAGE(1), ALL_PACKAGES(2)
 */
enum class SubscriptionsOptions(val value: Int) {
    CURRENT_PACKAGE(1),
    ALL_PACKAGES(2)
}

enum class InvalidInput {
    EMAIL, PASSWORD, CONFIRM_PASSWORD, PHONE_EGYPT, PHONE_SAUDI, NAME, EMPTY, NONE, MATCH, CASH, VISA, PRICE,
    DISCOUNT_VALUE, DISCOUNT_PERCENTAGE, QUANTITY, TAX_RECORD, COMMERCIAL_RECORD, INITIAL, BUY_PRICE, BUY_PRICE_SMALL,
    DIFFERENT_CASH, DIFFERENT_VISA, TEXT, TAX, BRANCH_NAME, BRANCH_ADDRESS, COMMERCIAL_NAME, EMPTY_EMAIL, EMPTY_PHONE, EMPTY_PASSWORD,
}

/**
 * enum class for the different drafts types that the user can view (all drafts, products drafts, invoices drafts, clients drafts)
 */
enum class DraftsType(val value: Int) {
    ALL(0),
    PRODUCTS(1),
    INVOICES(2),
    CLIENTS(3),
    USERS(4)
}

enum class Limit(val value: Int) {
    TWO(2),
    HUNDRED(100)
}

enum class PaymentCode(val value: Int) {
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

enum class RegisterType(val value: Int) {
    FREE(1),
    PAID(2)
}

enum class InvoiceType(val value: Int) {
    SIMPLE(1),
    TAX(2),
}

enum class MainTypeInvoice(val value: Int) {
    SALE(1),
    PURCHASE(2),
}

enum class SaleBuyType(val value: Int) {
    SALE(1),
    BUY(2)
}

enum class Shift(val value: Int) {
    Start(1),
    End(2)
}

enum class Request(val value: Int) {
    SCANNER(100)
}

enum class Discount(val value: Int) {
    Value(1),
    Percentage(2),
    None(0)
}

enum class Taxable(val value: Int) {
    YES(1),
    NO(2)
}

enum class TaxType(val value: Int) {
    Excludes(1),
    INCLUDED(2)
}

enum class Cart(val value: Int) {
    ADD(1),
    DELETE(3),
    DELETE_ALL(4),
    INITIAL(5),
    UPDATE(6),
}

enum class PaymentType(val value: Int) {
    CASH(1),
    CREDIT_CARD(2),
    POST_PAID(3),
    CASH_AND_CREDIT_CARD(4),
    POST_PAID_AND_CREDIT_CARD(5),
    INSTALLMENT(6)
}

enum class PaymentStatus(val value: Int) {
    COMPLETED(1),
    UNCOMPLETED(2)
}

enum class NoteType(val value: Int) {
    ALL(0),
    CREDITOR(1),
    DEBTOR(2);

    companion object {
        infix fun from(value: Int): NoteType =
            NoteType.entries.firstOrNull { it.value == value } ?: CREDITOR
    }
}

enum class MemorandumType(val value: Int) {
    ALL(0),
    CREDITOR(1),
    DEBTOR(2)
}

enum class ProductFilterType(val value: Int) {
    LOWEST_PRICE(4),
    HIGHEST_PRICE(3),
    RECENTLY_ADDED(5),
    NAME(6),
    PERCENTAGE_DISCOUNT(2),
    PRICE_DISCOUNT(1),
    NONE(0)
}

enum class Code(val value: Int) {
    FORGET(1),
    REGISTER(2)
}