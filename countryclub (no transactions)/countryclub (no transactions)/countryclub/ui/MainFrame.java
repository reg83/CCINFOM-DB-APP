package com.countryclub.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.sql.SQLException;

// Import your DAOs
import com.countryclub.dao.ReportDAO;
import com.countryclub.dao.MemberRecords;
import com.countryclub.dao.StaffRecords;
import com.countryclub.dao.InventoryRecords;
import com.countryclub.dao.EventRecords;
import com.countryclub.dao.TransactionDAO;

// Import your Models
import com.countryclub.model.EventReportItem;
import com.countryclub.model.SalesReportItem;
import com.countryclub.model.StaffReportItem;
import com.countryclub.model.Member;
import com.countryclub.model.Staff;
import com.countryclub.model.Inventory;
import com.countryclub.model.Event;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTabbedPane tabbedPane;

    // DAOs
    private ReportDAO reportDAO;
    private MemberRecords memberRecords;
    private StaffRecords staffRecords;
    private InventoryRecords inventoryRecords;
    private EventRecords eventRecords;
    private TransactionDAO transactionDAO;

    public MainFrame() {
        setTitle("Country Club Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        try {
            setLocationRelativeTo(null);
        } catch (Exception e) {
            // Ignore
        }

        // Initialize DAOs
        try {
            reportDAO = new ReportDAO();
            memberRecords = new MemberRecords();
            staffRecords = new StaffRecords();
            inventoryRecords = new InventoryRecords();
            eventRecords = new EventRecords();
            transactionDAO = new TransactionDAO(); 
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error connecting to database: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        tabbedPane = new JTabbedPane();

        // Add Tabs
        tabbedPane.addTab("Reports", createReportsPanel());
        tabbedPane.addTab("Members", createMembersPanel());
        tabbedPane.addTab("Staff", createStaffPanel());
        tabbedPane.addTab("Inventory", createInventoryPanel());
        tabbedPane.addTab("Events", createEventsPanel());
        tabbedPane.addTab("Transactions", createTransactionPanel());

        add(tabbedPane);
    }

    // --- 1. REPORTS PANEL ---
    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] reportTypes = {"Event Report", "Sales Report", "Staff Schedule Report", "Monthly Membership", "Yearly Membership"};
        JComboBox<String> reportTypeCombo = new JComboBox<>(reportTypes);
        JTextField monthField = new JTextField("4", 3);
        JTextField yearField = new JTextField("2025", 5);
        JButton generateButton = new JButton("Generate Report");
        
        controlPanel.add(new JLabel("Report Type:"));
        controlPanel.add(reportTypeCombo);
        controlPanel.add(new JLabel("Month:"));
        controlPanel.add(monthField);
        controlPanel.add(new JLabel("Year:"));
        controlPanel.add(yearField);
        controlPanel.add(generateButton);

        JTable reportTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(reportTable);
        
        JLabel resultLabel = new JLabel("Select a report and click Generate.");
        resultLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(resultLabel, BorderLayout.NORTH);

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);

        generateButton.addActionListener(e -> {
            String type = (String) reportTypeCombo.getSelectedItem();
            int month = Integer.parseInt(monthField.getText());
            int year = Integer.parseInt(yearField.getText());
            
            DefaultTableModel model = new DefaultTableModel();
            resultLabel.setText("Generating " + type + "...");
            
            try {
                switch (type) {
                    case "Event Report":
                        List<EventReportItem> events = reportDAO.getEventReport(month, year);
                        model.setColumnIdentifiers(new String[]{"Event ID", "Event Name", "Total Cost"});
                        for (EventReportItem item : events) {
                            model.addRow(new Object[]{item.getEventId(), item.getEventName(), item.getTotalCost()});
                        }
                        reportTable.setModel(model);
                        resultLabel.setText("Event Report generated.");
                        break;

                    case "Sales Report":
                        List<SalesReportItem> sales = reportDAO.getSalesReport(month, year);
                        model.setColumnIdentifiers(new String[]{"Sale Date", "Total Sales", "Avg Sales"});
                        for (SalesReportItem item : sales) {
                            model.addRow(new Object[]{item.getSaleDate(), item.getTotalSales(), item.getAverageSales()});
                        }
                        reportTable.setModel(model);
                        resultLabel.setText("Sales Report generated.");
                        break;
                        
                    case "Staff Schedule Report":
                        List<StaffReportItem> staff = reportDAO.getStaffReport(month, year);
                        model.setColumnIdentifiers(new String[]{"Staff ID", "Name", "Event", "Date", "Start", "End"});
                        for (StaffReportItem item : staff) {
                            model.addRow(new Object[]{
                                item.getStaffId(), item.getStaffName(), item.getEventNameString(), 
                                item.getFromDate(), item.getFromTime(), item.getToTime()
                            });
                        }
                        reportTable.setModel(model);
                        resultLabel.setText("Staff Schedule Report generated.");
                        break;

                    case "Monthly Membership":
                        int mCount = reportDAO.getMonthlyMembershipCount(month, year);
                        reportTable.setModel(new DefaultTableModel());
                        resultLabel.setText("New Members in " + month + "/" + year + ": " + mCount);
                        break;

                    case "Yearly Membership":
                        int yCount = reportDAO.getYearlyMembershipCount(year);
                        reportTable.setModel(new DefaultTableModel());
                        resultLabel.setText("Total New Members in " + year + ": " + yCount);
                        break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(MainFrame.this, "Error generating report: " + ex.getMessage());
            }
        });

        return panel;
    }

    // --- 2. MEMBERS PANEL ---
    private JPanel createMembersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JButton refreshButton = new JButton("Refresh List");
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        
        panel.add(refreshButton, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        refreshButton.addActionListener(e -> {
            try {
                List<Member> members = memberRecords.getAllMembers();
                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(new String[]{"ID", "Name", "Email", "Join Date", "Expiry Date"});
                for (Member m : members) {
                    model.addRow(new Object[]{
                        m.getMemberId(), m.getFirstName() + " " + m.getLastName(), 
                        m.getEmail(), m.getJoinDate(), m.getExpiryDate()
                    });
                }
                table.setModel(model);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        return panel;
    }

    // --- 3. STAFF PANEL ---
    private JPanel createStaffPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JButton refreshButton = new JButton("Refresh List");
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        
        panel.add(refreshButton, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        refreshButton.addActionListener(e -> {
            try {
                List<Staff> staffList = staffRecords.getAllStaff();
                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(new String[]{"ID", "Name", "Role", "Email", "Status"});
                for (Staff s : staffList) {
                    model.addRow(new Object[]{
                        s.getStaffId(), s.getStaffName(), s.getRoleName(),
                        s.getEmail(), s.getAvailabilityStatus()
                    });
                }
                table.setModel(model);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        return panel;
    }
    
    // --- 4. INVENTORY PANEL ---
    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JButton refreshButton = new JButton("Refresh List");
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        
        panel.add(refreshButton, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        refreshButton.addActionListener(e -> {
            try {
                List<Inventory> items = inventoryRecords.getAllItems();
                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(new String[]{"ID", "Item Name", "Category", "Quantity", "Unit Price"});
                for (Inventory i : items) {
                    model.addRow(new Object[]{
                        i.getItemId(), i.getItemName(), i.getItemCategory(),
                        i.getItemQuantity(), i.getItemUnitPrice()
                    });
                }
                table.setModel(model);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        return panel;
    }

    // --- 5. EVENTS PANEL ---
    private JPanel createEventsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JButton refreshButton = new JButton("Refresh List");
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        
        panel.add(refreshButton, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        refreshButton.addActionListener(e -> {
            try {
                List<Event> events = eventRecords.getAllEvents();
                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(new String[]{"ID", "Event Name", "Date", "Time"});
                for (Event ev : events) {
                    model.addRow(new Object[]{
                        ev.getEventId(), ev.getEventName(), ev.getFromDate(),
                        ev.getFromTime() + " - " + ev.getToTime()
                    });
                }
                table.setModel(model);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        return panel;
    }

    // --- 6. TRANSACTION PANEL ---
    private JPanel createTransactionPanel() {
        JPanel panel = new JPanel(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;

        JTextField memberIdField = new JTextField(10);
        JTextField amountField = new JTextField(10);
        String[] types = {"Food", "Beverage", "Merchandise", "Fee"};
        JComboBox<String> typeCombo = new JComboBox<>(types);
        JButton submitButton = new JButton("Record Transaction");
        JLabel statusLabel = new JLabel("Enter details and click Record.");

        // Row 1: Member ID
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Member ID (0 for Guest):"), gbc);
        gbc.gridx = 1;
        panel.add(memberIdField, gbc);

        // Row 2: Amount
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Amount ($):"), gbc);
        gbc.gridx = 1;
        panel.add(amountField, gbc);

        // Row 3: Type
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        panel.add(typeCombo, gbc);

        // Row 4: Button
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(submitButton, gbc);
        
        // Row 5: Status
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(statusLabel, gbc);

        submitButton.addActionListener(e -> {
            try {
                String memIdText = memberIdField.getText().trim();
                int memId = memIdText.isEmpty() ? 0 : Integer.parseInt(memIdText);
                double amount = Double.parseDouble(amountField.getText().trim());
                String type = (String) typeCombo.getSelectedItem();
                
                // Use current date
                java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
                
                transactionDAO.addTransaction(memId, amount, type, today);
                
                statusLabel.setText("Success! Recorded $" + amount + " for " + type);
                amountField.setText(""); // clear field
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(MainFrame.this, "Invalid Number Format. Please check ID and Amount.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(MainFrame.this, "Database Error: " + ex.getMessage());
            }
        });

        return panel;
    }

    // --- MAIN METHOD ---
    public static void main(String[] args) {
        // Use SwingUtilities to run GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
