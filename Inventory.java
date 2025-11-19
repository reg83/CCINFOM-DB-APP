package com.countryclub.model;

import java.math.BigDecimal;
import java.time.LocalTime;

public class Inventory {
    private int itemId;
    private String itemName;
    private String itemCategory;
    private int itemQuantity;
    private BigDecimal itemUnitPrice;
    private int supplierId;
    private LocalTime availabilityStart;
    private LocalTime availabilityEnd;

    public Inventory(int itemId, String itemName, String itemCategory, int itemQuantity, BigDecimal itemUnitPrice,
            int supplierId, LocalTime availabilityStart, LocalTime availabilityEnd) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.itemQuantity = itemQuantity;
        this.itemUnitPrice = itemUnitPrice;
        this.supplierId = supplierId;
        this.availabilityStart = availabilityStart;
        this.availabilityEnd = availabilityEnd;
    }

    public int getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public String getItemCategory() { return itemCategory; }
    public int getItemQuantity() { return itemQuantity; }
    public BigDecimal getItemUnitPrice() { return itemUnitPrice; }
    public int getSupplierId() { return supplierId; }
    public LocalTime getAvailabilityStart() { return availabilityStart; }
    public LocalTime getAvailabilityEnd() { return availabilityEnd; }

    @Override
    public String toString() {
        return itemName + " ($" + itemUnitPrice + ")";
    }
}