import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class MembershipTransaction{
    private final Map<Integer, Member> members = new Hashmap<>();
    private int nextID = 1;

    //A.
    public Member addMember(String name, String contactNumber, LocalDate startDate, LocalDate endDate, Set<String> privileges, BigDecimal initialBalance){
        Member m = new Member();
        m.id = nextID++;
        m.name = name;
        m.contactNumber = contactNumber;
        m.startDate = startDate;
        m.endDate = endDate;
        if (privileges != null){
            m.privileges.addAll(privileges);
        }
        if (initialBalance != null){
            m.balance = initialBalance;
        }
        members.put(m.id, m);
        return m;
    }

    //B
    public void renewMembership(int memberID,LocalDate newEndDate, Set<String> newPrivileges) {
        Member m = getRequiredMember(memberID);
        if (newEndDate != null) {
            m.endDate = newEndDate;
        }
        if (newPrivileges != null && !newPrivileges.isEmpty()) {
            m.privileges.clear();
            m.privileges.addAll(newPrivileges);
        }
    }

    //C.
    Public void adjustBalance(int memberID, BigDecimal adjustment){
        Member m = getRequiredMember(memberID);
        if(adjustment == null) return;
        m.balance = m.balance.add(adjustment);
    }

    //D.
    public void revokeMembership(int memberID){
        Member m = getRequiredMember(memberID);
        m.active = false;
        m.privileges.clear();
    }

    public Collection<Member> getAllMembers(){
        return members.values();
    }

    public Member findMemberById(int memberID){
        return members.get(memberID);
    }

    private Member getRequiredMember(int memberID){
        Member m = members.get(memberID);
        if(m == null){
            throw new IllegalArgumentException("Member ID " + memberID + " not found");
        }
        return m;
    }
    
    public static class Member {
        public int id;
        public String name;
        public String contactNumber;
        public LocalDate startDate;
        public LocalDate endDate;
        public Set<String> privileges = new HashSet<>();
        public BigDecimal balance = BigDecimal.ZERO;
        public boolean active = true;
    }
}