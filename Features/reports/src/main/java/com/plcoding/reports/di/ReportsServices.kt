package com.plcoding.reports.di

import com.plcoding.reports.data.expense.remote.ApiExpenseReports
import com.plcoding.reports.data.inventory.remote.ApiInventoryReports
import com.plcoding.reports.data.postPaid.remote.PostpaidApiService
import com.plcoding.reports.data.productsQuantitiesReport.remote.ProductsQuantityApiService
import com.plcoding.reports.data.returnReport.remote.ReturnInvoicesApiService
import com.plcoding.reports.data.salesReport.remote.SalesReportApiService
import com.plcoding.reports.data.salesorbuy.remote.SaleBuyReportsApiService
import com.plcoding.reports.data.tax.remote.ApiTaxReports
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ReportsServices {
    @Singleton
    @Provides
    fun provideExpense(retrofit: Retrofit): ApiExpenseReports {
        return retrofit.create(ApiExpenseReports::class.java)
    }

    @Singleton
    @Provides
    fun provideApiInventory(retrofit: Retrofit): ApiInventoryReports {
        return retrofit.create(ApiInventoryReports::class.java)
    }

    @Singleton
    @Provides
    fun provideApiSaleBuy(retrofit: Retrofit): SaleBuyReportsApiService {
        return retrofit.create(SaleBuyReportsApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideApiTax(retrofit: Retrofit): ApiTaxReports {
        return retrofit.create(ApiTaxReports::class.java)
    }

    @Singleton
    @Provides
    fun provideSalesReportApi(retrofit: Retrofit): SalesReportApiService {
        return retrofit.create(SalesReportApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideReturnInvoicesApi(retrofit: Retrofit): ReturnInvoicesApiService {
        return retrofit.create(ReturnInvoicesApiService::class.java)
    }
    @Singleton
    @Provides
    fun provideProductsQuantity(retrofit: Retrofit): ProductsQuantityApiService {
        return retrofit.create(ProductsQuantityApiService::class.java)
    }

    @Singleton
    @Provides
    fun providePostpaidReportsApi(retrofit: Retrofit): PostpaidApiService {
        return retrofit.create(PostpaidApiService::class.java)
    }

}