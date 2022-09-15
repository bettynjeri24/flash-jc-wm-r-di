package com.ekenya.rnd.dashboard.datadashboard.model


data class StatisticData (
    val money_in:ArrayList<MonthData>,
    val money_out:ArrayList<MonthData>,
    val withdrawal:ArrayList<MonthData>,
    val year_cashflow_summary:ArrayList<MonthData>,
    val year_deposit_cashflow_summary:ArrayList<MonthData>,
    val total_deposit_cashflow:TotalCashFlow,
    val total_cashflow:TotalCashFlow,
    val fund_transfer:ArrayList<MonthData>,
    val total_income:ArrayList<MonthData>,
    val bills:ArrayList<MonthData>
        )