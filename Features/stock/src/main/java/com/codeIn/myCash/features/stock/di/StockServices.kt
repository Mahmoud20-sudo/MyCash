package com.codeIn.myCash.features.stock.di

import com.codeIn.myCash.features.stock.data.category.remote.Category
import com.codeIn.myCash.features.stock.data.invoice.remote.Invoice
import com.codeIn.myCash.features.stock.data.madaTransactions.remote.MadaTransactions
import com.codeIn.myCash.features.stock.data.memorandum.remote.Memorandum
import com.codeIn.myCash.features.stock.data.product.remote.Product
import com.codeIn.myCash.features.stock.data.receipt.remote.Receipt
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class StockServices {
    @Singleton
    @Provides
    fun provideCategory (retrofit: Retrofit) : Category{
        return retrofit.create(Category::class.java)
    }
    @Singleton
    @Provides
    fun provideProduct (retrofit: Retrofit) : Product{
        return retrofit.create(Product::class.java)
    }
    @Singleton
    @Provides
    fun provideMadaTransactions (retrofit: Retrofit) : MadaTransactions{
        return retrofit.create(MadaTransactions::class.java)
    }
    @Singleton
    @Provides
    fun provideInvoice (retrofit: Retrofit) : Invoice{
        return retrofit.create(Invoice::class.java)
    }

    @Singleton
    @Provides
    fun provideReceipt(retrofit: Retrofit):Receipt{
        return retrofit.create(Receipt::class.java)
    }

    @Singleton
    @Provides
    fun provideMemorandum(retrofit: Retrofit) : Memorandum{
        return retrofit.create(Memorandum::class.java)
    }

}