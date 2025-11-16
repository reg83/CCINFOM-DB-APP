import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AccomodationTransaction{
    private final Map<Integer, Room> rooms = new HashMap<>();
    private int nextID = 1;
    
    //A.
    public Room addRoom(String roomNumber, BigDecimal pricePerNight, BigDecimal maintenanceCost, boolean available){
        Room r = new Room();
        r.id = nextID++;
        r.roomNumber = roomNumber;
        r.pricePerNight = pricePerNight;
        r.maintenanceCost = maintenanceCost;
        r.status = available ? RoomStatus.VACANT : RoomStatus.REMOVED;
        rooms.put(r.id, r);
        
        return r;
    }
    
    //B.
    public void checkIn(int roomID, LocalDate checkInDate, LocalDate expectedCheckoutDate){
        if (r.status != RoomStatus.VACANT){
            throw new IllegalStateException("Room not available for check-in");
        }
        r.status = RoomStatus.OCCUPIED;
        r.checkInDate = checkInDate;
        r.checkOutDate = expectedCheckoutDate;
    }

    //C.
    public void checkOut(int roomID, LocalDate actualCheckOutDate){
        Room r = getRequiredRoom(roomID);
        if(r.status != RoomStatus.OCCUPIED){
            throw new IllegalStateException("Room is not currently occupied");
        }

        LocalDate endDate = (actualCheckOutDate != null) ? actualCheckOutDate : r.checkOutDate;

        if(r.checkInDate != null && endDate != null && r.pricePerNight != null){
            long nights  = ChronoUnit.DAYS.between(r.checkInDate, endDate);
            if(nights < 1){
                nights = 1;
            }
            BigDecimal stayIncome = r.pricePerNight.multiply(BigDecimal.valueOf(nights));
            r.income = r.income.add(stayIncome);
        }

        r.status = RoomStatus.VACANT;
        r.checkInDate = null;
        r.checkOutDate = null;
    }

    //D.
    public void removeRoom(int roomID){
        rooms.remove(roomID);
    }

    public Collection<Room> getAllRooms(){
        return rooms.values();
    }

    private Room getRequiredRoom(int roomID){
        Room r = rooms.get(roomID);
        if(r == null){
            throw new IllegalArgumentException("Room ID " + roomID + " not found");
        }
        return r;
    }

    public enum RoomStatus{
        VACANT, OCCUPIED, REMOVED
    }

    public static class Room{
        public int ID;
        public String roomNumber;
        public BigDecimal pricePerNight;
        public BigDecimal income = BigDecimal.ZERO;
        public BigDecimal maintenanceCost = BigDecimal.ZERO;
        public RoomStatus status = RoomStatus.VACANT;
        public LocalDate checkInDate;
        public LocalDate checkOutDate;
    }
}