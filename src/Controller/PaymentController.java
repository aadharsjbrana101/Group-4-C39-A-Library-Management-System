/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.List;
/**
 *
 * @author aadha
 */
public class PaymentController {
    private JTable table;
    private JLabel totalFeesLabel;
    private JLabel pendingFeesLabel;
    private JLabel paidFeesLabel;

    public PaymentController(JTable table,
                             JLabel totalFeesLabel,
                             JLabel pendingFeesLabel,
                             JLabel paidFeesLabel) {
        this.table = table;
        this.totalFeesLabel = totalFeesLabel;
        this.pendingFeesLabel = pendingFeesLabel;
        this.paidFeesLabel = paidFeesLabel;
    }

    public void loadPaymentData(List<Object[]> rows) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Object[] row : rows) {
            model.addRow(row);
        }
        calculateFees();
    }

    public void calculateFees() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int total = 0, pending = 0, paid = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            Object amountObj = model.getValueAt(i, 2);
            Object statusObj = model.getValueAt(i, 3);

            if (amountObj == null || statusObj == null) continue;

            String amountText = amountObj.toString().replace("Rs", "").trim();
            String status = statusObj.toString().trim();

            if (amountText.equals("-") || amountText.isEmpty()) continue;

            try {
                int amount = Integer.parseInt(amountText);
                total += amount;
                if (status.equalsIgnoreCase("Pending")) {
                    pending += amount;
                } else if (status.equalsIgnoreCase("Paid")) {
                    paid += amount;
                }
            } catch (NumberFormatException e) {
                // skip bad rows
            }
        }

        totalFeesLabel.setText("Rs " + total);
        pendingFeesLabel.setText("Rs " + pending);
        paidFeesLabel.setText("Rs " + paid);
    }
}
