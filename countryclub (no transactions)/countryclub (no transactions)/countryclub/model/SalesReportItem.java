package com.countryclub.model; // <-- This package is correct

import java.sql.Date;

/**
 * A simple model class (DTO) to hold data for one row of the Sales Report.
 */
public class SalesReportItem { // <-- The public class matches the filename
    private Date saleDate;
    private double totalSales;
    private double averageSales;

    public SalesReportItem(Date saleDate, double totalSales, double averageSales) {
        this.saleDate = saleDate;
        this.totalSales = totalSales;
        this.averageSales = averageSales;
    }

    // Getters
    public Date getSaleDate() { return saleDate; }
    public double getTotalSales() { return totalSales; }
    public double getAverageSales() { return averageSales; }
}