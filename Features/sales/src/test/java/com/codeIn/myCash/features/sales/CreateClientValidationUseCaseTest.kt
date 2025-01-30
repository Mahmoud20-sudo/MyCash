package com.codeIn.myCash.features.sales

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.usecases.ValidateCommercialOrTaxRecordUseCase
import com.codeIn.common.util.Validation
import com.codeIn.myCash.features.sales.data.clients.remote.request.AddClientRequest
import com.codeIn.myCash.features.sales.domain.clients.usecases.CreateClientValidationUseCase
import org.junit.Test

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before

class CreateClientValidationUseCaseTest {

    private lateinit var validationUseCase: Validation
    private lateinit var commercialOrTaxRecord: ValidateCommercialOrTaxRecordUseCase
    private lateinit var createClient: CreateClientValidationUseCase

    @Before
    fun setUp() {
        validationUseCase = mockk()
        commercialOrTaxRecord = mockk()
        createClient = CreateClientValidationUseCase(validationUseCase, commercialOrTaxRecord)
    }

    @Test
    fun `should return InvalidInput_EMPTY when phone is empty`() {
        val request = AddClientRequest(
            name = "John Doe",
            phone = "",
            taxNo = "12345",
            commercialRecordNo = "67890",
            email = "email@example.com",
            type = 1,
            extra = null,
            address = null,
            countryId = null,
            clientId = null
        )
        val result =
            createClient(request)
        assertEquals(InvalidInput.EMPTY, result)
    }

    @Test
    fun `should return InvalidInput_EMPTY when name is empty`() {
        val request = AddClientRequest(
            name = "",
            phone = "0555555555",
            taxNo = "12345",
            commercialRecordNo = "67890",
            email = "email@example.com",
            type = 1,
            extra = null,
            address = null,
            countryId = null,
            clientId = null
        )
        val result = createClient(request)
        assertEquals(InvalidInput.EMPTY, result)
    }

    @Test
    fun `should return InvalidInput_PHONE_SAUDI when phone is invalid`() {
        every { validationUseCase.validatePhoneSaudi("0555555555") } returns false
        val request = AddClientRequest(
            name = "John Doe",
            phone = "0555555555",
            taxNo = "12345",
            commercialRecordNo = "67890",
            email = "email@example.com",
            type = 1,
            extra = null,
            address = null,
            countryId = null,
            clientId = null
        )
        val result = createClient(request)
        assertEquals(InvalidInput.PHONE_SAUDI, result)

        verify { validationUseCase.validatePhoneSaudi("0555555555") }
    }

    @Test
    fun `should return InvalidInput_EMAIL when email is invalid`() {
        every { validationUseCase.validatePhoneSaudi("0555555555") } returns true
        every { validationUseCase.validateEmail("invalidemail") } returns false
        val request = AddClientRequest(
            name = "John Doe",
            phone = "0555555555",
            taxNo = "12345",
            commercialRecordNo = "67890",
            email = "invalidemail",
            type = 1,
            extra = null,
            address = null,
            countryId = null,
            clientId = null
        )

        val result = createClient(request)
        assertEquals(InvalidInput.EMAIL, result)

        verify { validationUseCase.validateEmail("invalidemail") }
    }

    @Test
    fun `should return InvalidInput_TAX_RECORD when taxNo length is less than 15 digits`() {
        every { validationUseCase.validatePhoneSaudi("0555555555") } returns true
        every { validationUseCase.validateEmail("email@example.com") } returns true
        every { commercialOrTaxRecord.taxRecord("12345678901234") } returns false // 14 digits

        val request = AddClientRequest(
            name = "John Doe",
            phone = "0555555555",
            taxNo = "12345678901234", // Invalid taxNo (14 digits)
            commercialRecordNo = "67890",
            email = "email@example.com",
            type = 1,
            extra = null,
            address = null,
            countryId = null,
            clientId = null
        )

        val result = createClient(request)
        assertEquals(InvalidInput.TAX_RECORD, result)

        verify { commercialOrTaxRecord.taxRecord("12345678901234") }
    }

    @Test
    fun `should return InvalidInput_TAX_RECORD when taxNo length is more than 15 digits`() {
        every { validationUseCase.validatePhoneSaudi("0555555555") } returns true
        every { validationUseCase.validateEmail("email@example.com") } returns true
        every { commercialOrTaxRecord.taxRecord("1234567890123456") } returns false // 16 digits

        val request = AddClientRequest(
            name = "John Doe",
            phone = "0555555555",
            taxNo = "1234567890123456",  // Invalid taxNo (16 digits)
            commercialRecordNo = "67890",
            email = "email@example.com",
            type = 1,
            extra = null,
            address = null,
            countryId = null,
            clientId = null
        )

        val result = createClient(request)

        assertEquals(InvalidInput.TAX_RECORD, result)

        verify { commercialOrTaxRecord.taxRecord("1234567890123456") }
    }

    @Test
    fun `should return InvalidInput_COMMERCIAL_RECORD when commercialRecordNo length is less than 10 digits`() {
        every { validationUseCase.validatePhoneSaudi("0555555555") } returns true
        every { validationUseCase.validateEmail("email@example.com") } returns true
        every { commercialOrTaxRecord.taxRecord("123456789012345") } returns true // 15 digits valid taxNo
        every { commercialOrTaxRecord.commercial("123456789") } returns false // 9 digits

        val request = AddClientRequest(
            name = "John Doe",
            phone = "0555555555",
            taxNo = "123456789012345", // Valid taxNo
            commercialRecordNo = "123456789", // Invalid commercialRecordNo (9 digits)
            email = "email@example.com",
            type = 1,
            extra = null,
            address = null,
            countryId = null,
            clientId = null
        )

        val result = createClient(request)

        assertEquals(InvalidInput.COMMERCIAL_RECORD, result)

        verify { commercialOrTaxRecord.commercial("123456789") }
    }

    @Test
    fun `should return InvalidInput_COMMERCIAL_RECORD when commercialRecordNo length is more than 10 digits`() {
        every { validationUseCase.validatePhoneSaudi("0555555555") } returns true
        every { validationUseCase.validateEmail("email@example.com") } returns true
        every { commercialOrTaxRecord.taxRecord("123456789012345") } returns true // 15 digits valid taxNo
        every { commercialOrTaxRecord.commercial("12345678901") } returns false // 11 digits
        val request = AddClientRequest(
            name = "John Doe",
            phone = "0555555555",
            taxNo = "123456789012345", // Valid taxNo
            commercialRecordNo = "12345678901", // Invalid commercialRecordNo (11 digits)
            email = "email@example.com",
            type = 1,
            extra = null,
            address = null,
            countryId = null,
            clientId = null
        )

        val result = createClient(request)

        assertEquals(InvalidInput.COMMERCIAL_RECORD, result)

        verify { commercialOrTaxRecord.commercial("12345678901") }
    }
}
