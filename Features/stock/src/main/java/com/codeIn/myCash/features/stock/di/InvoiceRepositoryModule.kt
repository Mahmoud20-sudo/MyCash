package com.codeIn.myCash.features.stock.di

import com.codeIn.myCash.features.stock.data.invoice.repository.CartInQuickInvoiceRepositoryImpl
import com.codeIn.myCash.features.stock.data.invoice.repository.CartRepositoryImpl
import com.codeIn.myCash.features.stock.data.invoice.repository.InvoiceCalculatorRepositoryImpl
import com.codeIn.myCash.features.stock.data.invoice.repository.InvoiceRepositoryImpl
import com.codeIn.myCash.features.stock.data.invoice.repository.ReturnInvoiceCalculatorRepositoryImpl
import com.codeIn.myCash.features.stock.data.invoice.repository.CartInReturnInvoiceRepositoryImpl
import com.codeIn.myCash.features.stock.data.invoice.repository.QuickInvoiceCalculatorRepositoryImpl
import com.codeIn.myCash.features.stock.domain.invoice.repository.CartInQuickInvoiceRepository
import com.codeIn.myCash.features.stock.domain.invoice.repository.InvoiceRepository
import com.codeIn.myCash.features.stock.domain.invoice.repository.CartRepository
import com.codeIn.myCash.features.stock.domain.invoice.repository.InvoiceCalculatorRepository
import com.codeIn.myCash.features.stock.domain.invoice.repository.ReturnInvoiceCalculatorRepository
import com.codeIn.myCash.features.stock.domain.invoice.repository.CartInReturnInvoiceRepository
import com.codeIn.myCash.features.stock.domain.invoice.repository.QuickInvoiceCalculatorRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InvoiceRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindInvoiceRepository(repository: InvoiceRepositoryImpl): InvoiceRepository

    @Singleton
    @Binds
    abstract fun bindCartRepository(repository: CartRepositoryImpl):CartRepository

    @Singleton
    @Binds
    abstract fun bindInvoiceCalculatorRepository(repository: InvoiceCalculatorRepositoryImpl): InvoiceCalculatorRepository

    @Singleton
    @Binds
    abstract fun bindReturnInvoiceRepository(repository: CartInReturnInvoiceRepositoryImpl):CartInReturnInvoiceRepository

    @Singleton
    @Binds
    abstract fun bindReturnInvoiceCalculatorRepository(repository: ReturnInvoiceCalculatorRepositoryImpl): ReturnInvoiceCalculatorRepository


    @Singleton
    @Binds
    abstract fun bindCartInQuickInvoiceRepository(repository: CartInQuickInvoiceRepositoryImpl):CartInQuickInvoiceRepository

    @Singleton
    @Binds
    abstract fun bindQuickInvoiceCalculatorRepository(repository: QuickInvoiceCalculatorRepositoryImpl): QuickInvoiceCalculatorRepository


}