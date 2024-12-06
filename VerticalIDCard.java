import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class VerticalIDCard extends JFrame {
    private JTextField nameField, phoneField, emailField;
    private JComboBox<String> departmentDropdown;
    private ImageIcon photoIcon;
    private ImageIcon backgroundImage;
    private ImageIcon logoIcon;
    private String studentId;
    private static int idCounter = 1;  // Counter for generating unique IDs

    public VerticalIDCard() {
        setTitle("Vertical ID Card Generator");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Load background and logo images
        try {
            BufferedImage bgImg = ImageIO.read(new File("src/background.jpg"));
            backgroundImage = new ImageIcon(bgImg.getScaledInstance(275, 400, Image.SCALE_SMOOTH));
            BufferedImage logoImg = ImageIO.read(new File("src/logo.png"));
            logoIcon = new ImageIcon(logoImg.getScaledInstance(200, 170, Image.SCALE_SMOOTH));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Image files not found! Please ensure the files are in the specified path.");
        }

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Enter Student Details"));

        // Input fields
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Department:"));
        departmentDropdown = new JComboBox<>(new String[]{"Computer Science", "Mathematics", "Physics", "Engineering"});
        inputPanel.add(departmentDropdown);

        inputPanel.add(new JLabel("Phone Number:"));
        phoneField = new JTextField();
        inputPanel.add(phoneField);

        inputPanel.add(new JLabel("Email ID:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        JButton uploadPhotoButton = new JButton("Upload Photo");
        uploadPhotoButton.addActionListener(e -> uploadPhoto());
        inputPanel.add(new JLabel("Photo:"));
        inputPanel.add(uploadPhotoButton);

        // Generate and Save buttons
        JButton generateButton = new JButton("Generate ID Card");
        generateButton.addActionListener(e -> generateStudentId());

        JButton saveButton = new JButton("Save ID Card");
        saveButton.addActionListener(e -> saveIDCard());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(generateButton);
        buttonPanel.add(saveButton);

        // Add components to the frame
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // Input validation
        addInputValidation();
    }

    private void uploadPhoto() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                BufferedImage img = ImageIO.read(selectedFile);
                photoIcon = new ImageIcon(img.getScaledInstance(110, 110, Image.SCALE_SMOOTH));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error uploading photo. Please try again.");
            }
        }
    }

    private void generateStudentId() {
        // Automatically generate student ID in format "JUW001", "JUW002", etc.
        studentId = String.format("JUW%03d", idCounter++);
        repaint();  // Repaint to show the generated ID
    }

    private void saveIDCard() {
        // Create an image with the same width and height of the ID card area
        BufferedImage image = new BufferedImage(800, 700, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Set a background color to white to avoid transparent background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());

        // Draw the front and back ID cards
        int frontCardX = 50, frontCardY = 300;
        int backCardX = 400, backCardY = 300;

        // Draw Front Card
        drawFrontIDCard(g2d, frontCardX, frontCardY);

        // Draw Back Card
        drawBackIDCard(g2d, backCardX, backCardY);

        g2d.dispose();

        // Open a file chooser to let the user select where to save the image
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save ID Card Image");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JPEG Image", "jpg", "jpeg"));
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File outputFile = fileChooser.getSelectedFile();

            // Ensure the file has the ".jpg" extension
            if (!outputFile.getName().endsWith(".jpg") && !outputFile.getName().endsWith(".jpeg")) {
                outputFile = new File(outputFile.getParent(), outputFile.getName() + ".jpg");
            }

            try {
                // Save the image in JPEG format
                ImageIO.write(image, "JPEG", outputFile);
                JOptionPane.showMessageDialog(this, "ID Card saved successfully as " + outputFile.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving ID card. Please try again.");
            }
        }
    }


    private void addInputValidation() {
        // Name validation
        nameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (!nameField.getText().matches("[a-zA-Z ]+")) {
                    JOptionPane.showMessageDialog(null, "Name should contain only alphabets!");
                    nameField.requestFocus();
                }
            }
        });

        // Phone number validation
        phoneField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (!phoneField.getText().matches("\\d+")) {
                    JOptionPane.showMessageDialog(null, "Phone number should contain only digits!");
                    phoneField.requestFocus();
                }
            }
        });

        // Email validation
        emailField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (!Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", emailField.getText())) {
                    JOptionPane.showMessageDialog(null, "Invalid email format!");
                    emailField.requestFocus();
                }
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int frontCardX = 50, frontCardY = 300;
        int backCardX = 400, backCardY = 300;

        // Draw Front Card
        drawFrontIDCard(g, frontCardX, frontCardY);

        // Draw Back Card
        drawBackIDCard(g, backCardX, backCardY);
    }

    private void drawFrontIDCard(Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;

        // Draw background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage.getImage(), x, y, null);
        }

        // Title
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 15));
        g.drawString("STUDENT ID CARD", x + 60, y + 23);

        // Photo with border
        int photoX = x + 84, photoY = y + 70, photoWidth = 110, photoHeight = 110;

        if (photoIcon != null) {
            g.drawImage(photoIcon.getImage(), photoX, photoY, null);

            // Draw steel blue border
            g2d.setColor(new Color(70, 130, 180));
            g2d.setStroke(new BasicStroke(4));
            g2d.drawRect(photoX, photoY, photoWidth, photoHeight);
        } else {
            g.setColor(Color.GRAY);
            g.fillRect(photoX, photoY, photoWidth, photoHeight);

            // Border
            g2d.setColor(new Color(70, 130, 180));
            g2d.setStroke(new BasicStroke(5));
            g2d.drawRect(photoX, photoY, photoWidth, photoHeight);
        }

        // Details
        g.setColor(new Color(25, 25, 110));
        g.setFont(new Font("Verdana", Font.BOLD, 12));
        g.drawString("Name: " + nameField.getText(), x + 40, y + 220);
        g.drawString("Student ID: " + studentId, x + 40, y + 240);
        g.drawString("Department: " + departmentDropdown.getSelectedItem(), x + 40, y + 260);
        g.drawString("Phone: " + phoneField.getText(), x + 40, y + 280);
        g.drawString("Email: " + emailField.getText(), x + 40, y + 300);
    }

    private void drawBackIDCard(Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;

        // Draw background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage.getImage(), x, y, null);
        }

        // Center the logo on the back card
        int logoX = x + 40, logoY = y + 100;
        g.drawImage(logoIcon.getImage(), logoX, logoY, null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VerticalIDCard().setVisible(true));
    }
}