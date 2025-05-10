import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

public class GUI extends JFrame {

    private static final int GRID_SIZE = 9;
    private JTextField[][] cells = new JTextField[GRID_SIZE][GRID_SIZE];


    public GUI() {

        setTitle("Sudoku Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,450);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        
        Font cellFont = new Font("SansSerif", Font.BOLD, 24);

        // Initialize each cell in the grid
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                cells[i][j] = new JTextField(1);  // Limit to 1 character
                cells[i][j].setHorizontalAlignment(JTextField.CENTER);
                cells[i][j].setDocument(new JTextFieldLimit(1));  // Custom Document to limit input
                cells[i][j].setFont(cellFont); 

                // Set borders to highlight 3x3 quadrants
                int top = (i % 3 == 0) ? 5 : 1;
                int left = (j % 3 == 0) ? 5 : 1;
                int bottom = (i == GRID_SIZE - 1) ? 5 : 1;
                int right = (j == GRID_SIZE - 1) ? 5 : 1;

                cells[i][j].setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, new Color(0x36454F)));
               
                gridPanel.add(cells[i][j]);
            }
        }

        // Button to submit the grid input
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String gridValues = collectGridValues();
                clearcells();
                SudokuSolver.StartSolver(gridValues);
                //submitButton.setEnabled(false);
            }
        });

        // Add components to the main frame
        add(gridPanel, BorderLayout.CENTER);
        add(submitButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void clearcells() {
        for(int i=0; i<GRID_SIZE; i++) {
            for(int j=0; j<GRID_SIZE; j++) {
                cells[i][j].setText("");
            }
        }
    }

    // Method to collect values from the grid
    private String collectGridValues() {
        StringBuilder values = new StringBuilder(GRID_SIZE * GRID_SIZE);
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                String cellValue = cells[i][j].getText().trim();
                values.append(cellValue.isEmpty() ? "0" : cellValue);  // Use "." for empty cells
            }
        }
        return values.toString();
    }



    // Method to open a new window with a 9x9 grid of labels
    public void showOutputWindow(String[][] gridValues) {
        JFrame outputFrame = new JFrame("Sudoku Grid Output");
        outputFrame.setSize(400, 400);
        outputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel outputPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        
        Font labelFont = new Font("SansSerif", Font.BOLD, 24);

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                String value;
                if(gridValues[i][j].equals("0")) {
                    value = "";
                } else {
                    value = String.valueOf(gridValues[i][j]);
                }
                JLabel label = new JLabel(value, SwingConstants.CENTER);
                label.setFont(labelFont);
                // Determine border thickness for quadrant separation
                int top = (i % 3 == 0) ? 5 : 1;
                int left = (j % 3 == 0) ? 5 : 1;
                int bottom = (i == GRID_SIZE - 1) ? 5 : 1;
                int right = (j == GRID_SIZE - 1) ? 5 : 1;
                label.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, new Color(0x36454F)));
                outputPanel.add(label);
            }
        }
        
        outputFrame.add(outputPanel);
        outputFrame.setVisible(true);
    }

    // Custom Document class to restrict JTextField input to a single character
    private class JTextFieldLimit extends javax.swing.text.PlainDocument {
        private int limit;

        JTextFieldLimit(int limit) {
            this.limit = limit;
        }

        @Override
        public void insertString(int offset, String str, javax.swing.text.AttributeSet attr) {
            if (str == null || (getLength() + str.length()) > limit) {
                return;
            }
            try {
                super.insertString(offset, str, attr);
            } catch (javax.swing.text.BadLocationException e) {
                e.printStackTrace(); // Handle exception if needed
            }
        }
    }
}