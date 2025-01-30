package com.plcoding.reports.di

import com.plcoding.reports.data.expense.repository.GetExpensesRepositoryImpl
import com.plcoding.reports.data.inventory.repository.GetInventoryRepositoryImpl
import com.plcoding.reports.data.postPaid.repository.GetPostPaidRepositoryImp
import com.plcoding.reports.data.productsQuantitiesReport.repository.GetProductsQuantityReportRepositoryImp
import com.plcoding.reports.data.returnReport.repository.GetReturnInvoicesReportRepositoryImp
import com.plcoding.reports.data.salesReport.repository.GetSalesReportRepositoryImp
import com.plcoding.reports.data.salesorbuy.repository.GetSaleInvoicesRepositoryImpl
import com.plcoding.reports.data.tax.repository.GetTaxRepositoryImpl
import com.plcoding.reports.domain.expense.repository.GetExpensesRepository
import com.plcoding.reports.domain.inventory.repository.GetInventoryRepository
import com.plcoding.reports.domain.postPaidReport.repository.GetPostPaidRepository
import com.plcoding.reports.domain.productsQuantitiesReport.repository.GetProductsQuantityRepository
import com.plcoding.reports.domain.returnReport.repository.GetReturnInvoicesRepository
import com.plcoding.reports.domain.salesReport.repository.GetSalesReportRepository
import com.plcoding.reports.domain.salesorbuy.repository.GetSaleInvoicesRepository
import com.plcoding.reports.domain.tax.repository.GetTaxRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReportsRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindGetExpensesRepository(repositoryImpl: GetExpensesRepositoryImpl): GetExpensesRepository
    @Singleton
    @Binds
    abstract fun bindGetInventoryRepository(repositoryImpl: GetInventoryRepositoryImpl): GetInventoryRepository

    @Singleton
    @Binds
    abstract fun bindGetTaxRepository(repositoryImpl: GetTaxRepositoryImpl): GetTaxRepository

    @Singleton
    @Binds
    abstract fun bindGetSaleOrBuyRepository(repositoryImpl: GetSaleInvoicesRepositoryImpl): GetSaleInvoicesRepository

    @Singleton
    @Binds
    abstract fun bindGetSalesReportRepositoryImp(repositoryImpl: GetSalesReportRepositoryImp): GetSalesReportRepository

    @Singleton
    @Binds
    abstract fun bindGetReturnReportRepositoryImp(repositoryImpl: GetReturnInvoicesReportRepositoryImp): GetReturnInvoicesRepository

    @Singleton
    @Binds
    abstract fun bindGetProductsQuantityReportRepository(repositoryImpl: GetProductsQuantityReportRepositoryImp): GetProductsQuantityRepository
    @Singleton
    @Binds
    abstract fun bindGetPostPaidReportRepository(repositoryImpl: GetPostPaidRepositoryImp): GetPostPaidRepository

}