package com.example.crud;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CrudFrame extends JFrame {
    private final PersonRepository repository = new PersonRepository();

    private final JTextField idField = new JTextField(10);
    private final JTextField nameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Name", "Email"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(tableModel);

    public CrudFrame() {
        setTitle("Simple Java CRUD Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(createFormPanel(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                populateFieldsFromSelectedRow();
            }
        });
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("Create");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearFieldsButton = new JButton("Clear Fields");
        JButton clearAllButton = new JButton("Delete All");

        addButton.addActionListener(event -> createPerson());
        updateButton.addActionListener(event -> updatePerson());
        deleteButton.addActionListener(event -> deletePerson());
        clearFieldsButton.addActionListener(event -> clearFields());
        clearAllButton.addActionListener(event -> deleteAll());

        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(clearFieldsButton);
        panel.add(clearAllButton);

        return panel;
    }

    private void createPerson() {
        try {
            int id = readId();
            String name = readRequired(nameField, "Name");
            String email = readRequired(emailField, "Email");

            repository.add(new Person(id, name, email));
            refreshTable();
            clearFields();
            showMessage("Person created successfully.");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private void updatePerson() {
        try {
            int id = readId();
            String name = readRequired(nameField, "Name");
            String email = readRequired(emailField, "Email");

            if (!repository.update(id, name, email)) {
                showError("No person exists with ID " + id + ".");
                return;
            }

            refreshTable();
            clearFields();
            showMessage("Person updated successfully.");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private void deletePerson() {
        try {
            int id = readId();

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete the person with ID " + id + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            if (!repository.delete(id)) {
                showError("No person exists with ID " + id + ".");
                return;
            }

            refreshTable();
            clearFields();
            showMessage("Person deleted successfully.");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private void deleteAll() {
        if (tableModel.getRowCount() == 0) {
            showMessage("There are no records to delete.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "This will permanently delete ALL " + tableModel.getRowCount()
                        + " records. Are you sure?",
                "Confirm Delete All",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        repository.clear();
        refreshTable();
        clearFields();
        showMessage("All records deleted.");
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Person person : repository.findAll()) {
            tableModel.addRow(new Object[]{person.getId(), person.getName(), person.getEmail()});
        }
    }

    private void populateFieldsFromSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
            nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            emailField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        }
    }

    private int readId() {
        String value = idField.getText().trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("ID is required.");
        }

        try {
            int id = Integer.parseInt(value);
            if (id <= 0) {
                throw new IllegalArgumentException("ID must be a positive number.");
            }
            return id;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("ID must be a whole number.");
        }
    }

    private String readRequired(JTextField field, String fieldName) {
        String value = field.getText().trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
        return value;
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
        table.clearSelection();
        idField.requestFocus();
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
