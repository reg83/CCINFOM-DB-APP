import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class StaffTransaction{
    private final Map<Integer, Staff> staffByID = new HashMap<>();
    private int nextID = 1;

    //A.
    public Staff addStaff(String name, String address, int age, String contract, String role, BigDecimal monthlySalary, String notes){
        Staff staff = new Staff();
        staff.ID = nextID++;
        staff.name = name;
        staff.address = address;
        staff.age = age;
        staff.contract = contract;
        staff.role = role;
        staff.monthlySalary = monthlySalary;
        staff.notes = notes;
        staff.active = true;
        staffByID.put(staff.id, staff);
        return staff;
    }

    //B.
    public void DistributeSalary(int staffID, BigDecimal amountPaid){
        Staff staff = getRequiredStaff(staffID);
        if(amountPaid == null || amountPaid.signnum() <= 0){
            throw new IllegalArgumentException("Amount must be positive");
        }
        staff.totalPaid = staff.totalPaid.add(amountPaid);
    }

    //C.
    public void removeStaff(int staffID){
        staffByID.remove(staffID);
    }

    public Collection<Staff> getAllStaff(){
        return staffByID.values();
    }

    private Staff getRequiredStaff(int staffID){
        Staff staff = staffByID.get(staffID);
        if(staff == null){
            throw new IllegalArgumentException("Staff id " + staffID + " not found"); 
        }
        return staff;
    }

    public static class Staff{
        public int id;
        public String name;
        public String address;
        public int age; 
        public String contract; 
        public String role;
        public BigDecimal monthlySalary;
        public BigDecimal totalPaid = BigDecimal.ZERO; 
        public String notes;
        public boolean active;
    }
}