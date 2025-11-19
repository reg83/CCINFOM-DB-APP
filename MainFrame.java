package com.countryclub.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.sql.SQLException;
import com.countryclub.dao.*;
import com.countryclub.model.*;
import com.countryclub.model.Event; 
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;


public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private MemberRecords memberDAO;
    private StaffRecords staffDAO;
    private EventRecords eventDAO;
    private InventoryRecords inventoryDAO;
    private VenueDAO venueDAO;
    private ReportDAO reportDAO;
    private TransactionDAO transactionDAO;

    private JTable memberTable;
    private JTable staffTable;
    private JTable eventTable;
    private JTable inventoryTable;
    private JTable venueTable;
    private JTable reportTable;
    private JTable transactionTable;

    private JComboBox<String> reportTypeCombo;
    private JTextField monthField;
    private JTextField yearField;

    public MainFrame() {

        try {
            memberDAO = new MemberRecords();
            staffDAO = new StaffRecords();
            eventDAO = new EventRecords();
            inventoryDAO = new InventoryRecords();
            venueDAO = new VenueDAO();
            reportDAO = new ReportDAO();
            transactionDAO = new TransactionDAO();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to database: " + e.getMessage());
            System.exit(1);
        }

        setTitle("Country Club Management System");
        setSize(1300, 850); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Reports", createReportPanel());
        tabbedPane.addTab("Members", createMemberPanel());
        tabbedPane.addTab("Staff", createStaffPanel());
        tabbedPane.addTab("Events", createEventPanel());
        tabbedPane.addTab("Inventory & Orders", createInventoryPanel());
        tabbedPane.addTab("Facilities", createVenuePanel());
        tabbedPane.addTab("Sales", createSalesPanel());

        add(tabbedPane);
    }

    private JPanel createMemberPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        memberTable = new JTable();
        panel.add(new JScrollPane(memberTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        JButton addBtn = new JButton("Add Member");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton renewBtn = new JButton("Renew");
        JButton balanceBtn = new JButton("Adjust Balance");

        buttonPanel.add(refreshBtn); buttonPanel.add(addBtn); buttonPanel.add(updateBtn);
        buttonPanel.add(renewBtn); buttonPanel.add(balanceBtn); buttonPanel.add(deleteBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> refreshMemberTable());

        addBtn.addActionListener(e -> {
            JTextField fn = new JTextField();
            JTextField ln = new JTextField();
            JTextField contact = new JTextField();
            JTextField email = new JTextField();
            String[] types = {"Regular", "Premium", "VIP"};
            JComboBox<String> typeBox = new JComboBox<>(types);

            Object[] message = {"First Name:", fn, "Last Name:", ln, "Contact:", contact, "Email:", email, "Type:", typeBox};
            if (JOptionPane.showConfirmDialog(this, message, "Add Member", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    Member m = new Member(0, typeBox.getSelectedIndex() + 1, contact.getText(), fn.getText(), ln.getText(),
                            LocalDate.of(1990, 1, 1), LocalDate.now(), LocalDate.now().plusYears(1), email.getText(), 0.0);
                    memberDAO.addMember(m);
                    refreshMemberTable();
                } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
            }
        });
        
        updateBtn.addActionListener(e -> {
            int row = memberTable.getSelectedRow();
            if(row == -1) return;
            try {
                int id = (int) memberTable.getValueAt(row, 0);
                Member old = memberDAO.getAllMembers().stream().filter(m -> m.getMemberId() == id).findFirst().orElse(null);
                if(old != null) {
                     JTextField fn = new JTextField(old.getFirstName());
                     JTextField ln = new JTextField(old.getLastName());
                     JTextField contact = new JTextField(old.getContactNo());
                     JTextField email = new JTextField(old.getEmail());
                     Object[] message = { "First Name:", fn, "Last Name:", ln, "Contact:", contact, "Email:", email };
                     if (JOptionPane.showConfirmDialog(this, message, "Update Member", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                         Member m = new Member(id, old.getMembershipTypeId(), contact.getText(), fn.getText(), ln.getText(), old.getBirthDate(), old.getJoinDate(), old.getExpiryDate(), email.getText(), old.getBalance());
                         memberDAO.updateMember(m);
                         refreshMemberTable();
                     }
                }
            } catch(Exception ex) { ex.printStackTrace(); }
        });

        balanceBtn.addActionListener(e -> {
            int row = memberTable.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Select a member."); return; }
            int id = (int) memberTable.getValueAt(row, 0);
            String input = JOptionPane.showInputDialog("Enter amount to add (use negative for charge):");
            if(input != null) {
                try {
                    double amount = Double.parseDouble(input);
                    memberDAO.updateBalance(id, amount);
                    transactionDAO.addTransaction(new Transaction(id, null, java.sql.Date.valueOf(LocalDate.now()), BigDecimal.valueOf(amount), "Adjustment", "Balance Adjustment"));
                    refreshMemberTable();
                    refreshTransactionTable();
                    JOptionPane.showMessageDialog(this, "Balance Updated!");
                } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
            }
        });

        renewBtn.addActionListener(e -> {
            int row = memberTable.getSelectedRow();
            if (row == -1) return;
            int memberId = (int) memberTable.getValueAt(row, 0);
            String[] types = {"Regular (1000)", "Premium (2500)", "VIP (5000)"};
            JComboBox<String> typeBox = new JComboBox<>(types);
            if (JOptionPane.showConfirmDialog(this, typeBox, "Renew", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    int typeId = typeBox.getSelectedIndex() + 1;
                    double amount = (typeId == 1) ? 1000.0 : (typeId == 2) ? 2500.0 : 5000.0;
                    memberDAO.renewMembership(memberId, typeId);
                    transactionDAO.addTransaction(new Transaction(memberId, null, java.sql.Date.valueOf(LocalDate.now()), BigDecimal.valueOf(amount), "Membership", "Renewal"));
                    refreshMemberTable();
                    refreshTransactionTable();
                } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = memberTable.getSelectedRow();
            if (row != -1) {
                try {
                    memberDAO.deleteMember((int) memberTable.getValueAt(row, 0));
                    refreshMemberTable();
                } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
            }
        });

        refreshMemberTable();
        return panel;
    }

    private void refreshMemberTable() {
        try {
            List<Member> members = memberDAO.getAllMembers();
            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"ID", "Name", "Type", "Contact", "Expiry", "Balance"}); 
            for (Member m : members) {
                model.addRow(new Object[]{
                    m.getMemberId(), m.getFirstName() + " " + m.getLastName(), m.getMembershipTypeId(),
                    m.getContactNo(), m.getExpiryDate(), m.getBalance()
                });
            }
            memberTable.setModel(model);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private JPanel createStaffPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        staffTable = new JTable();
        panel.add(new JScrollPane(staffTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton refresh = new JButton("Refresh");
        JButton add = new JButton("Add Staff");
        JButton update = new JButton("Update");
        JButton payrollBtn = new JButton("Process Payroll"); 
        JButton delete = new JButton("Delete");
        
        btnPanel.add(refresh); btnPanel.add(add); btnPanel.add(update); 
        btnPanel.add(payrollBtn); btnPanel.add(delete);
        panel.add(btnPanel, BorderLayout.SOUTH);

        refresh.addActionListener(e -> refreshStaffTable());
        
        add.addActionListener(e -> {
            JTextField name = new JTextField();
            JTextField role = new JTextField();
            JTextField salary = new JTextField("15000");
            Object[] msg = {"Name:", name, "Role:", role, "Salary:", salary};
            if(JOptionPane.showConfirmDialog(this, msg, "Add Staff", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                try {
                    Staff s = new Staff(0, "", "", name.getText(), LocalDate.now(), role.getText(), "Available", Double.parseDouble(salary.getText()));
                    staffDAO.addStaff(s);
                    refreshStaffTable();
                } catch (Exception ex) { ex.printStackTrace(); }
            }
        });

        update.addActionListener(e -> {
            int row = staffTable.getSelectedRow();
            if(row == -1) { JOptionPane.showMessageDialog(this, "Select staff to update."); return; }

            int id = (int) staffTable.getValueAt(row, 0);
            String currentName = (String) staffTable.getValueAt(row, 1);
            String currentRole = (String) staffTable.getValueAt(row, 2);
            String currentStatus = (String) staffTable.getValueAt(row, 3);
            String currentContact = (String) staffTable.getValueAt(row, 4);
            String currentEmail = (String) staffTable.getValueAt(row, 5);
            double currentSalary = (double) staffTable.getValueAt(row, 6);

            JTextField nameF = new JTextField(currentName);
            JTextField roleF = new JTextField(currentRole);
            JTextField statusF = new JTextField(currentStatus);
            JTextField contactF = new JTextField(currentContact);
            JTextField emailF = new JTextField(currentEmail);
            JTextField salaryF = new JTextField(String.valueOf(currentSalary));

            Object[] msg = {"Name:", nameF, "Role:", roleF, "Status:", statusF, "Contact:", contactF, "Email:", emailF, "Salary:", salaryF};
            if(JOptionPane.showConfirmDialog(this, msg, "Update Staff", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    Staff s = new Staff(id, contactF.getText(), emailF.getText(), nameF.getText(), LocalDate.now(), roleF.getText(), statusF.getText(), Double.parseDouble(salaryF.getText()));
                    staffDAO.updateStaff(s);
                    refreshStaffTable();
                } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
            }
        });

        payrollBtn.addActionListener(e -> {
            int row = staffTable.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Select a staff member to pay."); return; }
            
            String name = (String) staffTable.getValueAt(row, 1);
            double salary = (double) staffTable.getValueAt(row, 6); 

            if (JOptionPane.showConfirmDialog(this, "Pay " + name + " amount: " + salary + "?", "Confirm Payroll", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                Transaction tx = new Transaction(null, null, java.sql.Date.valueOf(LocalDate.now()), BigDecimal.valueOf(-salary), "Payroll", "Salary for " + name);
                transactionDAO.addTransaction(tx);
                refreshTransactionTable();
                JOptionPane.showMessageDialog(this, "Payroll processed and recorded in Sales.");
            }
        });
        
        delete.addActionListener(e -> {
            int row = staffTable.getSelectedRow();
            if(row != -1) {
                try { staffDAO.deleteStaff((int)staffTable.getValueAt(row, 0)); refreshStaffTable(); } catch (Exception ex) {}
            }
        });

        refreshStaffTable();
        return panel;
    }

    private void refreshStaffTable() {
        try {
            List<Staff> list = staffDAO.getAllStaff();
            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"ID", "Name", "Role", "Status", "Contact", "Email", "Salary"}); 
            for (Staff s : list) {
                model.addRow(new Object[]{s.getStaffId(), s.getStaffName(), s.getRoleName(), s.getAvailabilityStatus(), s.getContactNo(), s.getEmail(), s.getSalary()});
            }
            staffTable.setModel(model);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private JPanel createEventPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        eventTable = new JTable();
        panel.add(new JScrollPane(eventTable), BorderLayout.CENTER);
        JPanel btnPanel = new JPanel();
        JButton refresh = new JButton("Refresh");
        JButton add = new JButton("Add Event");
        JButton update = new JButton("Update Event");
        JButton delete = new JButton("Delete Event");
        btnPanel.add(refresh); btnPanel.add(add); btnPanel.add(update); btnPanel.add(delete);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        refresh.addActionListener(e -> refreshEventTable());
        
        add.addActionListener(e -> showEventDialog(null));
        
        update.addActionListener(e -> {
            int row = eventTable.getSelectedRow();
            if(row != -1) {
                try {
                    int id = (int)eventTable.getValueAt(row, 0);
                    String name = (String)eventTable.getValueAt(row, 1);
                    LocalDate d1 = LocalDate.parse(eventTable.getValueAt(row, 2).toString());
                    LocalTime t1 = LocalTime.parse(eventTable.getValueAt(row, 3).toString());
                    Event ev = new Event(id, name, d1, d1, t1, t1, 1, 1, 1, 1); 
                    showEventDialog(ev);
                } catch(Exception ex) {}
            }
        });
        
        delete.addActionListener(e -> { if(eventTable.getSelectedRow()!=-1) try{eventDAO.deleteEvent((int)eventTable.getValueAt(eventTable.getSelectedRow(),0)); refreshEventTable();}catch(Exception ex){} });
        
        refreshEventTable();
        return panel;
    }

    private void showEventDialog(Event existingEvent) {
        JTextField nameField = new JTextField((existingEvent!=null)?existingEvent.getEventName():"");
        JTextField dateField = new JTextField((existingEvent!=null)?existingEvent.getFromDate().toString():"2024-01-01");
        JTextField timeField = new JTextField((existingEvent!=null)?existingEvent.getFromTime().toString():"09:00");
        
        JComboBox<Venue> venueBox = new JComboBox<>();
        JComboBox<Inventory> itemBox = new JComboBox<>();
        JComboBox<Member> memberBox = new JComboBox<>(); 
        JComboBox<Staff> staffBox = new JComboBox<>();
        
        try {
            for(Venue v : venueDAO.getAllVenues()) venueBox.addItem(v);
            for(Inventory i : inventoryDAO.getAllItems()) itemBox.addItem(i);
            for(Member m : memberDAO.getAllMembers()) memberBox.addItem(m); 
            for(Staff s : staffDAO.getAllStaff()) staffBox.addItem(s); 
        } catch (SQLException e) { e.printStackTrace(); }
        
        Object[] message = {
            "Event Name:", nameField,
            "Date (YYYY-MM-DD):", dateField,
            "Start Time (HH:MM):", timeField,
            "Venue:", venueBox,
            "Main Item Used:", itemBox,
            "Host Member:", memberBox,
            "Staff In-Charge:", staffBox
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, (existingEvent == null ? "Add" : "Update") + " Event", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Event ev = new Event(
                    (existingEvent == null) ? 0 : existingEvent.getEventId(),
                    nameField.getText(),
                    LocalDate.parse(dateField.getText()), LocalDate.parse(dateField.getText()), 
                    LocalTime.parse(timeField.getText()), LocalTime.parse(timeField.getText()).plusHours(2), 
                    ((Inventory)itemBox.getSelectedItem()).getItemId(), 
                    ((Member)memberBox.getSelectedItem()).getMemberId(), 
                    ((Staff)staffBox.getSelectedItem()).getStaffId(), 
                    ((Venue)venueBox.getSelectedItem()).getVenueId()
                );
                
                if (existingEvent == null) {
                    eventDAO.addEvent(ev);
                } else {
                    eventDAO.updateEvent(ev);
                }
                refreshEventTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void refreshEventTable() {
        try {
            List<Event> events = eventDAO.getAllEvents();
            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"ID", "Name", "Date", "Time", "Venue", "Host", "Staff"});
            for (Event e : events) {
                model.addRow(new Object[]{
                    e.getEventId(), 
                    e.getEventName(), 
                    e.getFromDate(), 
                    e.getFromTime(), 
                    e.getVenueName(), 
                    e.getHostName(),  
                    e.getStaffName()  
                });
            }
            eventTable.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        inventoryTable = new JTable();
        panel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton refresh = new JButton("Refresh");
        JButton add = new JButton("Add Item");
        JButton orderBtn = new JButton("Order Item (Sale)"); 
        JButton update = new JButton("Update Item");
        JButton delete = new JButton("Delete Item");

        btnPanel.add(refresh); btnPanel.add(add); btnPanel.add(orderBtn); btnPanel.add(update); btnPanel.add(delete);
        panel.add(btnPanel, BorderLayout.SOUTH);

        refresh.addActionListener(e -> refreshInventoryTable());

        add.addActionListener(e -> {
             JTextField name = new JTextField();
             JTextField qty = new JTextField();
             JTextField price = new JTextField();
             JTextField start = new JTextField("00:00");
             JTextField end = new JTextField("23:59");
             Object[] msg = {"Name:", name, "Qty:", qty, "Price:", price, "Avail Start (HH:MM):", start, "Avail End:", end};
             if (JOptionPane.showConfirmDialog(this, msg, "Add Inventory", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                 try {
                     Inventory i = new Inventory(0, name.getText(), "General", Integer.parseInt(qty.getText()), new BigDecimal(price.getText()), 1,
                             LocalTime.parse(start.getText()+":00"), LocalTime.parse(end.getText()+":00"));
                     inventoryDAO.addItem(i);
                     refreshInventoryTable();
                 } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
             }
        });

        orderBtn.addActionListener(e -> {
            int row = inventoryTable.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Select an item to order."); return; }
            
            int itemId = (int) inventoryTable.getValueAt(row, 0);
            String itemName = (String) inventoryTable.getValueAt(row, 1);
            int currentStock = (int) inventoryTable.getValueAt(row, 3);
            BigDecimal price = (BigDecimal) inventoryTable.getValueAt(row, 4);
            LocalTime start = (LocalTime) inventoryTable.getValueAt(row, 5);
            LocalTime end = (LocalTime) inventoryTable.getValueAt(row, 6);
            
            LocalTime now = LocalTime.now();
            if (now.isBefore(start) || now.isAfter(end)) {
                JOptionPane.showMessageDialog(this, "Item unavailable! Time window: " + start + " - " + end);
                return;
            }

            String qtyStr = JOptionPane.showInputDialog("Enter Quantity to Order:");
            if(qtyStr != null) {
                try {
                    int qty = Integer.parseInt(qtyStr);
                    if(qty > currentStock) { JOptionPane.showMessageDialog(this, "Not enough stock!"); return; }
                    
                    inventoryDAO.reduceStock(itemId, qty);
                    BigDecimal total = price.multiply(BigDecimal.valueOf(qty));
                    Transaction tx = new Transaction(null, null, java.sql.Date.valueOf(LocalDate.now()), total, "F&B Sales", itemName + " x" + qty);
                    transactionDAO.addTransaction(tx);
                    
                    JOptionPane.showMessageDialog(this, "Order Placed! Total: $" + total);
                    refreshInventoryTable();
                    refreshTransactionTable();
                    
                } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
            }
        });
        
        update.addActionListener(e -> {
            int selectedRow = inventoryTable.getSelectedRow();
            if (selectedRow == -1) { JOptionPane.showMessageDialog(this, "Please select an item to update."); return; }

            int itemId = (int) inventoryTable.getValueAt(selectedRow, 0);
            String currentName = (String) inventoryTable.getValueAt(selectedRow, 1);
            String currentCategory = (String) inventoryTable.getValueAt(selectedRow, 2);
            int currentQty = (int) inventoryTable.getValueAt(selectedRow, 3);
            BigDecimal currentPrice = (BigDecimal) inventoryTable.getValueAt(selectedRow, 4);
            LocalTime currentStart = (LocalTime) inventoryTable.getValueAt(selectedRow, 5);
            LocalTime currentEnd = (LocalTime) inventoryTable.getValueAt(selectedRow, 6);

            JTextField nameField = new JTextField(currentName);
            JTextField qtyField = new JTextField(String.valueOf(currentQty));
            JTextField priceField = new JTextField(currentPrice.toString());
            JTextField startField = new JTextField(currentStart.toString());
            JTextField endField = new JTextField(currentEnd.toString());

            Object[] message = { "Name:", nameField, "Qty:", qtyField, "Price:", priceField, "Start (HH:MM):", startField, "End (HH:MM):", endField };

            if (JOptionPane.showConfirmDialog(this, message, "Update Inventory Item", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    String sText = startField.getText().length() == 5 ? startField.getText() + ":00" : startField.getText();
                    String eText = endField.getText().length() == 5 ? endField.getText() + ":00" : endField.getText();

                    Inventory updatedItem = new Inventory(
                        itemId, 
                        nameField.getText(), 
                        currentCategory, 
                        Integer.parseInt(qtyField.getText()), 
                        new BigDecimal(priceField.getText()), 
                        1,
                        LocalTime.parse(sText),
                        LocalTime.parse(eText)
                    );
                    inventoryDAO.updateItem(updatedItem);
                    refreshInventoryTable();
                } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error updating item: " + ex.getMessage()); }
            }
        });

        delete.addActionListener(e -> {
             int row = inventoryTable.getSelectedRow();
             if(row != -1) { try { inventoryDAO.deleteItem((int)inventoryTable.getValueAt(row, 0)); refreshInventoryTable(); } catch(Exception ex){} }
        });

        refreshInventoryTable();
        return panel;
    }

    private void refreshInventoryTable() {
        try {
            List<Inventory> items = inventoryDAO.getAllItems();
            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"ID", "Name", "Category", "Qty", "Price", "Start", "End"}); 
            for (Inventory i : items) {
                model.addRow(new Object[]{i.getItemId(), i.getItemName(), i.getItemCategory(), i.getItemQuantity(), i.getItemUnitPrice(), i.getAvailabilityStart(), i.getAvailabilityEnd()});
            }
            inventoryTable.setModel(model);
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private JPanel createVenuePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        venueTable = new JTable();
        panel.add(new JScrollPane(venueTable), BorderLayout.CENTER);
        JPanel btnPanel = new JPanel();
        JButton refresh = new JButton("Refresh");
        JButton add = new JButton("Add Venue");
        JButton update = new JButton("Update Venue");
        JButton delete = new JButton("Delete Venue");
        btnPanel.add(refresh); btnPanel.add(add); btnPanel.add(update); btnPanel.add(delete);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        refresh.addActionListener(e -> refreshVenueTable());

        add.addActionListener(e -> {
            JTextField name = new JTextField();
            JTextField type = new JTextField();
            JTextField cap = new JTextField();
            String[] statuses = {"Available", "Maintenance", "Occupied"};
            JComboBox<String> statusBox = new JComboBox<>(statuses);

            Object[] message = {"Name:", name, "Type:", type, "Capacity:", cap, "Status:", statusBox};
            if (JOptionPane.showConfirmDialog(this, message, "Add Venue", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    Venue v = new Venue(0, name.getText(), type.getText(), Integer.parseInt(cap.getText()), (String) statusBox.getSelectedItem());
                    venueDAO.addVenue(v);
                    refreshVenueTable();
                } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
            }
        });

        update.addActionListener(e -> {
            int row = venueTable.getSelectedRow();
            if(row == -1) { JOptionPane.showMessageDialog(this, "Select a venue."); return; }
            
            int id = (int) venueTable.getValueAt(row, 0);
            String currentName = (String) venueTable.getValueAt(row, 1);
            String currentType = (String) venueTable.getValueAt(row, 2);
            int currentCap = (int) venueTable.getValueAt(row, 3);
            String currentStatus = (String) venueTable.getValueAt(row, 4);

            JTextField name = new JTextField(currentName);
            JTextField type = new JTextField(currentType);
            JTextField cap = new JTextField(String.valueOf(currentCap));
            String[] statuses = {"Available", "Maintenance", "Occupied"};
            JComboBox<String> statusBox = new JComboBox<>(statuses);
            statusBox.setSelectedItem(currentStatus);

            Object[] message = {"Name:", name, "Type:", type, "Capacity:", cap, "Status:", statusBox};
            if (JOptionPane.showConfirmDialog(this, message, "Update Venue", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    Venue v = new Venue(id, name.getText(), type.getText(), Integer.parseInt(cap.getText()), (String) statusBox.getSelectedItem());
                    venueDAO.updateVenue(v);
                    refreshVenueTable();
                } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
            }
        });
        
        delete.addActionListener(e -> {
             int row = venueTable.getSelectedRow();
             if(row != -1) { try { venueDAO.deleteVenue((int)venueTable.getValueAt(row, 0)); refreshVenueTable(); } catch(SQLException ex){} }
        });
        refreshVenueTable();
        return panel;
    }
    
    private void refreshVenueTable() {
        try {
            List<Venue> venues = venueDAO.getAllVenues();
            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"ID", "Name", "Type", "Capacity", "Status"});
            for (Venue v : venues) {
                model.addRow(new Object[]{v.getVenueId(), v.getVenueName(), v.getVenueType(), v.getCapacity(), v.getAvailabilityStatus()});
            }
            venueTable.setModel(model);
        } catch (SQLException e) {}
    }

    private JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] reports = {"Event Financials", "Staff Schedule", "Membership Report", "Sales Report"};
        reportTypeCombo = new JComboBox<>(reports);
        monthField = new JTextField(5);
        yearField = new JTextField(5);
        JButton generateBtn = new JButton("Generate Report");
        
        filterPanel.add(new JLabel("Report:")); filterPanel.add(reportTypeCombo);
        filterPanel.add(new JLabel("Month:")); filterPanel.add(monthField);
        filterPanel.add(new JLabel("Year:")); filterPanel.add(yearField);
        filterPanel.add(generateBtn);
        panel.add(filterPanel, BorderLayout.NORTH);
        
        reportTable = new JTable();
        panel.add(new JScrollPane(reportTable), BorderLayout.CENTER);

        generateBtn.addActionListener(e -> {
            String type = (String) reportTypeCombo.getSelectedItem();
            try {
                int m = Integer.parseInt(monthField.getText());
                int y = Integer.parseInt(yearField.getText());
                DefaultTableModel model = new DefaultTableModel();
                
                switch (type) {
                    case "Event Financials":
                        List<EventReportItem> events = reportDAO.getEventReport(m, y);
                        model.setColumnIdentifiers(new String[]{"ID", "Event Name", "Total Income", "Maint. Cost", "Profit"});
                        for(EventReportItem i : events) {
                            model.addRow(new Object[]{i.getEventId(), i.getEventName(), i.getTotalIncome(), i.getTotalCost(), (i.getTotalIncome() - i.getTotalCost())});
                        }
                        break;
                        
                    case "Staff Schedule":
                        List<StaffReportItem> staff = reportDAO.getStaffReport(m, y);
                        model.setColumnIdentifiers(new String[]{"Staff Name", "Event", "Date", "Time"});
                        for(StaffReportItem s : staff) {
                            model.addRow(new Object[]{s.getStaffName(), s.getEventName(), s.getFromDate(), s.getFromTime()});
                        }
                        break;
                        
                    case "Membership Report":
                        List<Member> activeMembers = reportDAO.getMembershipReport(m, y);
                        model.setColumnIdentifiers(new String[]{"ID", "Name", "Join Date", "Expiry", "Type", "Contact", "Email"});
                        for(Member mem : activeMembers) {
                            String typeStr = (mem.getMembershipTypeId() == 1) ? "Regular" : (mem.getMembershipTypeId() == 2 ? "Premium" : "VIP");
                            model.addRow(new Object[]{
                                mem.getMemberId(),
                                mem.getFirstName() + " " + mem.getLastName(),
                                mem.getJoinDate(),
                                mem.getExpiryDate(),
                                typeStr,
                                mem.getContactNo(),
                                mem.getEmail()
                            });
                        }
                        model.addRow(new Object[]{"", "", "", "", "", "", ""});
                        model.addRow(new Object[]{"TOTAL:", activeMembers.size() + " Active Members", "", "", "", "", ""});
                        break;
                        
                    case "Sales Report":
                        List<SalesReportItem> sales = reportDAO.getSalesReport(m, y);
                        model.setColumnIdentifiers(new String[]{"Date", "Total Sales", "Avg Sales"});
                        double grandTotal = 0; 
                        for(SalesReportItem i : sales) {
                            model.addRow(new Object[]{i.getSaleDate(), i.getTotalSales(), i.getAverageSales()});
                            grandTotal += i.getTotalSales();
                        }
                        model.addRow(new Object[]{"", "", ""});
                        model.addRow(new Object[]{"MONTHLY TOTAL:", grandTotal, ""});
                        break;
                }
                reportTable.setModel(model);
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });
        return panel;
    }

    private JPanel createSalesPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        transactionTable = new JTable();
        JScrollPane scroll = new JScrollPane(transactionTable);
        mainPanel.add(scroll, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh Transactions");
        btnPanel.add(refreshBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        
        refreshTransactionTable();
        
        refreshBtn.addActionListener(e -> refreshTransactionTable());
        return mainPanel;
    }
    
    private void refreshTransactionTable() {
        try {
            List<Transaction> transactions = transactionDAO.getAllTransactions();
            DefaultTableModel model = new DefaultTableModel();
            
            model.setColumnIdentifiers(new String[]{"ID", "Member ID", "Event ID", "Date", "Type", "Description", "Amount"});
            
            for (Transaction tx : transactions) {
                model.addRow(new Object[]{
                    tx.getTransactionId(),
                    tx.getMemberId() == null ? "Guest" : tx.getMemberId(), 
                    tx.getEventId() == null ? "N/A" : tx.getEventId(), 
                    tx.getTransactionDate(),
                    tx.getTransactionType(),
                    tx.getDescription(),
                    tx.getAmount()
                });
            }
            transactionTable.setModel(model);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error refreshing transaction list: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}