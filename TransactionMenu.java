import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class TransactionMenu extends JFrame{

    final private Font mainFont = new Font("Arial", Font.PLAIN, 16);
    final private Font buttonFont = new Font("Arial", Font.BOLD, 16);

    private JButton btnMembership = new JButton("Membership");
    private JButton btnEvents = new JButton("Events");
    private JButton btnFoodAndBeverages = new JButton("Food and Beverages");
    private JButton btnStaff = new JButton("Staff");
    private JButton btnViewReports = new JButton("View Reports");

    public void initialize() {
        // form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 1, 10, 10));

        btnMembership.setFont(buttonFont);
        btnEvents.setFont(buttonFont);
        btnFoodAndBeverages.setFont(buttonFont);
        btnStaff.setFont(buttonFont);
        btnViewReports.setFont(buttonFont);

        formPanel.add(btnMembership);
        formPanel.add(btnEvents);
        formPanel.add(btnFoodAndBeverages);
        formPanel.add(btnStaff);
        formPanel.add(btnViewReports);

        // welcome label
        JLabel lblWelcome = new JLabel("Country Club Database Application Transactions");
        lblWelcome.setFont(mainFont);
        add(lblWelcome, BorderLayout.NORTH);

        // main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(lblWelcome, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setTitle("Country Club Database Application - Transactions");
        setSize(500, 600);
        setMinimumSize(new Dimension(300, 400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true); 

    }
}   
