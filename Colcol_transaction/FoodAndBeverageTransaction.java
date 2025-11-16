import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FoodAndBeverageTransaction{
    private final Map<Integer, MenuItem> items = new HashMap<>();
    private int nextID = 1;
    
    //A.
    public MenuItem addItem(String name, BigDecimal price, int inventory, LocalTime availableFrom, LocalTime availableUntil){
        MenuItem item = new MenuItem();
        item.ID = nextID++;
        item.name = name;
        item.price = price;
        item.inventory = inventory;
        item.availableFrom = availableFrom;
        item.availableUntil = availableUntil;
        item.available = inventory > 0;
        items.put(item.ID, item);
        return item;
    }

    //B. 
    public BigDecimal placeOrder(int itemID, int quantity, LocalTime orderTime){
        if(quantity <= 0){
            throw new IllegalArgumentException("Quantity must be positive");
        }

        MenuItem item = getRequiredItem(itemID);
        updateAvailabilityInventoryAndTime(item, orderTime);
        if(!item.available){
            throw new IllegalStateException("Item not available");
        }

        if(item.inventory < quantity){
            throw new IllegalStateException("Not enough inventory for " + item.name);
        }

        item.inventory -= quantity;
        BigDecimal total = item.price.multiply(BigDecimal.valueOf(quantity));
        item.income = item.income.add(total);

        if(item.inventory == 0){
            item.available = false;
        }
        return total;
    }

    //C.
    public void refreshAvailability(LocalTime currentTime){
        for(MenuItem item : items.values()){
            updateAvailabilityInventoryAndTime(item, currentTime);
        }
    }

    private void updateAvailabilityInventoryAndTime(MenuItem item, LocalTime time){
        boolean withinTime = true;
        if(item.availableFrom != null && item.availableUntil != null && time  != null){
            withinTime = !time.isBefore(item.availableFrom) && !time.isAfter(item.availableUntil);
        }
        item.available = item.inventory > 0 && withinTime;
    }

    //D.
    public void removeItem(int itemID){
        items.remove(itemID);
    }

    public Collection<MenuItem> getAllItems(){
        return items.values();
    }

    private MenuItem getRequiredItem(int itemID){
        MenuItem item = items.get(itemID);
        if(item == null){
            throw new IllegalArgumentException("Menu item id " + itemID + " not found");
        }
        return item;
    }

    public static class MenuItem{
        public int ID;
        public String name;
        public BigDecimal price;
        public int inventory;
        public BigDecimal income = BigDecimal.ZERO;
        public boolean available = true;
        public LocalTime availableFrom;
        public LocalTime availableUntil;
    }
}