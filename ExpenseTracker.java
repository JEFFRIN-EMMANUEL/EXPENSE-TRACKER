import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ExpenseTracker extends JFrame {
    private ArrayList<Expense> expenses = new ArrayList<>();
    private double totalBudget = 0;

    // Components
    private JTextField amountField, dateField, budgetField;
    private JComboBox<String> categoryComboBox;
    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private JLabel totalExpensesLabel, balanceLabel;

    public ExpenseTracker() {
        setTitle("Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLayout(new BorderLayout());

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 248, 255));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(new TitledBorder("Add Expense"));

        inputPanel.add(new JLabel("Category:"));
        categoryComboBox = new JComboBox<>(new String[]{"Food", "Transport", "Shopping", "Bills", "Miscellaneous"});
        inputPanel.add(categoryComboBox);

        inputPanel.add(new JLabel("Amount (₹):"));
        amountField = new JTextField();
        inputPanel.add(amountField);

        inputPanel.add(new JLabel("Date (DD/MM/YYYY):"));
        dateField = new JTextField();
        inputPanel.add(dateField);

        inputPanel.add(new JLabel("Set Budget (₹):"));
        budgetField = new JTextField();
        inputPanel.add(budgetField);

        JButton addExpenseButton = new JButton("Add Expense");
        JButton setBudgetButton = new JButton("Set Budget");

        inputPanel.add(addExpenseButton);
        inputPanel.add(setBudgetButton);

        // Output Panel
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(new TitledBorder("Expense Summary"));

        String[] columnNames = {"Category", "Amount (₹)", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        expenseTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(expenseTable);
        outputPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Summary Panel
        JPanel summaryPanel = new JPanel(new GridLayout(2, 1));
        totalExpensesLabel = new JLabel("Total Expenses: ₹0");
        balanceLabel = new JLabel("Remaining Budget: ₹0");
        summaryPanel.add(totalExpensesLabel);
        summaryPanel.add(balanceLabel);
        summaryPanel.setBackground(new Color(245, 255, 250));

        // Add Panels
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(outputPanel, BorderLayout.CENTER);
        mainPanel.add(summaryPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Event Listeners
        addExpenseButton.addActionListener(e -> addExpense());
        setBudgetButton.addActionListener(e -> setBudget());

        setVisible(true);
    }

    private void addExpense() {
        try {
            String category = (String) categoryComboBox.getSelectedItem();
            double amount = Double.parseDouble(amountField.getText());
            String date = dateField.getText();

            if (amount <= 0 || date.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter valid details!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            expenses.add(new Expense(category, amount, date));
            updateSummary();
            addExpenseToTable(category, amount, date);

            amountField.setText("");
            dateField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addExpenseToTable(String category, double amount, String date) {
        tableModel.addRow(new Object[]{category, "₹" + String.format("%.2f", amount), date});
    }

    private void setBudget() {
        try {
            totalBudget = Double.parseDouble(budgetField.getText());
            updateSummary();
            budgetField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid budget amount!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSummary() {
        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
        totalExpensesLabel.setText("Total Expenses: ₹" + totalExpenses);
        balanceLabel.setText("Remaining Budget: ₹" + (totalBudget - totalExpenses));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpenseTracker::new);
    }

    // Inner Class to Represent an Expense
    class Expense {
        private String category;
        private double amount;
        private String date;

        public Expense(String category, double amount, String date) {
            this.category = category;
            this.amount = amount;
            this.date = date;
        }

        public String getCategory() {
            return category;
        }

        public double getAmount() {
            return amount;
        }

        public String getDate() {
            return date;
        }
    }
}