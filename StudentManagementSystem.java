import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentManagementSystem extends JFrame {
    private List<Student> students = new ArrayList<>();

    
    private JTextField nameField, rollField, classField, gpaField;
    private JButton addButton, updateButton, deleteButton, viewButton, exportButton;
    private JTable studentTable;
    private DefaultTableModel tableModel;

    public EnhancedStudentManagementSystem() {
        // Seting up the frame
        setTitle("Enhanced Student Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                ImageIcon imageIcon = new ImageIcon("background.jpg");
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Creating components
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setOpaque(false); // Make panel transparent

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Roll Number:"));
        rollField = new JTextField();
        inputPanel.add(rollField);

        inputPanel.add(new JLabel("Class:"));
        classField = new JTextField();
        inputPanel.add(classField);

        inputPanel.add(new JLabel("GPA:"));
        gpaField = new JTextField();
        inputPanel.add(gpaField);

        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        viewButton = new JButton("View");
        exportButton = new JButton("Export to CSV");

        // Setting button colors and font
        Color buttonColor = new Color(59, 89, 182);
        Color textColor = Color.BLACK; // Text color for buttons
        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        addButton.setBackground(buttonColor);
        addButton.setForeground(textColor);
        addButton.setFont(buttonFont);

        updateButton.setBackground(buttonColor);
        updateButton.setForeground(textColor);
        updateButton.setFont(buttonFont);

        deleteButton.setBackground(buttonColor);
        deleteButton.setForeground(textColor);
        deleteButton.setFont(buttonFont);

        viewButton.setBackground(buttonColor);
        viewButton.setForeground(textColor);
        viewButton.setFont(buttonFont);

        exportButton.setBackground(buttonColor);
        exportButton.setForeground(textColor);
        exportButton.setFont(buttonFont);

        // Adjusting button sizes
        Dimension buttonSize = new Dimension(100, 40); // Adjust as needed

        addButton.setPreferredSize(buttonSize);
        updateButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        viewButton.setPreferredSize(buttonSize);
        exportButton.setPreferredSize(buttonSize);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Adjust horizontal and vertical gaps
        buttonPanel.setOpaque(false); // Make panel transparent

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(exportButton);

        // Table setup
        studentTable = new JTable();
        tableModel = new DefaultTableModel(new Object[]{"Name", "Roll Number", "Class", "GPA"}, 0);
        studentTable.setModel(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(studentTable);
        tableScrollPane.setOpaque(false);
        tableScrollPane.getViewport().setOpaque(false);

        // Adding components to background panel
        backgroundPanel.add(inputPanel, BorderLayout.NORTH);
        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);
        backgroundPanel.add(tableScrollPane, BorderLayout.SOUTH);

        // Adding background panel to frame
        add(backgroundPanel);

        // Event handling
        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        viewButton.addActionListener(e -> viewStudents());
        exportButton.addActionListener(e -> exportToCSV());

        setVisible(true);
    }

    private void addStudent() {
        String name = nameField.getText();
        String rollNumber = rollField.getText();
        String studentClass = classField.getText();
        double gpa = 0;

        try {
            gpa = Double.parseDouble(gpaField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid GPA.");
            return;
        }

        if (name.isEmpty() || rollNumber.isEmpty() || studentClass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        students.add(new Student(name, rollNumber, studentClass, gpa));
        updateTable();
        JOptionPane.showMessageDialog(this, "Student added successfully!");
        clearFields();
    }

    private void updateStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow >= 0) {
            String name = nameField.getText();
            String rollNumber = rollField.getText();
            String studentClass = classField.getText();
            double gpa = 0;

            try {
                gpa = Double.parseDouble(gpaField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid GPA.");
                return;
            }

            if (name.isEmpty() || rollNumber.isEmpty() || studentClass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            Student student = students.get(selectedRow);
            student.setName(name);
            student.setRollNumber(rollNumber);
            student.setStudentClass(studentClass);
            student.setGpa(gpa);

            updateTable();
            JOptionPane.showMessageDialog(this, "Student updated successfully!");
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to update.");
        }
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow >= 0) {
            students.remove(selectedRow);
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Student deleted successfully!");
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.");
        }
    }

    private void viewStudents() {
        updateTable();
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Student student : students) {
            tableModel.addRow(new Object[]{student.getName(), student.getRollNumber(), student.getStudentClass(), student.getGpa()});
        }
    }

    private void exportToCSV() {
        try (PrintWriter writer = new PrintWriter(new File("students.csv"))) {
            StringBuilder sb = new StringBuilder();
            sb.append("Name,Roll Number,Class,GPA\n");
            for (Student student : students) {
                sb.append(student.getName()).append(",")
                        .append(student.getRollNumber()).append(",")
                        .append(student.getStudentClass()).append(",")
                        .append(student.getGpa()).append("\n");
            }
            writer.write(sb.toString());
            JOptionPane.showMessageDialog(this, "Data exported to students.csv");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error exporting data.");
        }
    }

    private void clearFields() {
        nameField.setText("");
        rollField.setText("");
        classField.setText("");
        gpaField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new EnhancedStudentManagementSystem();
        });
    }
}

class Student {
    private String name;
    private String rollNumber;
    private String studentClass;
    private double gpa;

    public Student(String name, String rollNumber, String studentClass, double gpa) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.studentClass = studentClass;
        this.gpa = gpa;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }
    public String getStudentClass() { return studentClass; }
    public void setStudentClass(String studentClass) { this.studentClass = studentClass; }
    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }
}
