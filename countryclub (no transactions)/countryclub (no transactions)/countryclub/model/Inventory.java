package com.countryclub.model;
import com.countryclub.model.Member;
import java.math.BigDecimal;
import com.countryclub.model.Staff;

public class Inventory {
    private int itemId;
    private String itemName;
    private String itemCategory;
    private int itemQuantity;
    private BigDecimal itemUnitPrice;
    private int supplierId;

    // Constructor
    public Inventory(int itemId, String itemName, String itemCategory, int itemQuantity, BigDecimal itemUnitPrice,
            int supplierId) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.itemQuantity = itemQuantity;
        this.itemUnitPrice = itemUnitPrice;
        this.supplierId = supplierId;
    }

    // Getters
    public int getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public String getItemCategory() { return itemCategory; }
    public int getItemQuantity() { return itemQuantity; }
    public BigDecimal getItemUnitPrice() { return itemUnitPrice; }
    public int getSupplierId() { return supplierId; }
}