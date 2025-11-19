package com.countryclub.model; 

import java.sql.Date;

public class SalesReportItem { 
    private Date saleDate;
    private double totalSales;
    private double averageSales;

    public SalesReportItem(Date saleDate, double totalSales, double averageSales) {
        this.saleDate = saleDate;
        this.totalSales = totalSales;
        this.averageSales = averageSales;
    }

    public Date getSaleDate() { return saleDate; }
    public double getTotalSales() { return totalSales; }
    public double getAverageSales() { return averageSales; }
}