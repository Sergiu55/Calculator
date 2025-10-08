import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class calculator implements ActionListener {

    JFrame frame;
    JTextField textfield;
    JButton[] numberButtons = new JButton[10];
    JButton[] functionButtons;
    JButton addButton, subButton, mulButton, divButton;
    JButton decButton, equButton, delButton, clrButton;
    JButton sqrButton, sqrtButton, modButton;
    JButton mcButton, mrButton, mPlusButton, mMinusButton, msButton;

    JPanel panel;
    Font myFont = new Font("Ink Free", Font.BOLD, 25);

    double num1 = 0, num2 = 0, result = 0, memory = 0;
    char operator = '\0';

    calculator() {
        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(430, 650);
        frame.setLayout(null);

        textfield = new JTextField();
        textfield.setBounds(50, 25, 320, 50);
        textfield.setFont(myFont);
        textfield.setEditable(false);

        // Butoane principale
        addButton = new JButton("+");
        subButton = new JButton("-");
        mulButton = new JButton("*");
        divButton = new JButton("/");
        decButton = new JButton(".");
        equButton = new JButton("=");
        delButton = new JButton("Delete");
        clrButton = new JButton("Clear");

        // Suplimentare
        sqrButton = new JButton("x²");
        sqrtButton = new JButton("v");
        modButton = new JButton("%");

        // Memorie
        mcButton = new JButton("MC");
        mrButton = new JButton("MR");
        mPlusButton = new JButton("M+");
        mMinusButton = new JButton("M-");
        msButton = new JButton("MS");

        functionButtons = new JButton[]{
                addButton, subButton, mulButton, divButton,
                decButton, equButton, delButton, clrButton,
                sqrButton, sqrtButton, modButton,
                mcButton, mrButton, mPlusButton, mMinusButton, msButton
        };

        for (JButton button : functionButtons) {
            button.addActionListener(this);
            button.setFont(myFont);
            button.setFocusable(false);
        }

        for (int i = 0; i < 10; i++) {
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].addActionListener(this);
            numberButtons[i].setFont(myFont);
            numberButtons[i].setFocusable(false);
        }

        delButton.setBounds(50, 540, 150, 50);
        clrButton.setBounds(220, 540, 150, 50);

        panel = new JPanel();
        panel.setBounds(50, 100, 320, 420);
        panel.setLayout(new GridLayout(6, 4, 10, 10));

        // Linie Memorie
        panel.add(mcButton);
        panel.add(mrButton);
        panel.add(mPlusButton);
        panel.add(mMinusButton);

        // Linie Funcții suplimentare
        panel.add(msButton);
        panel.add(sqrButton);
        panel.add(sqrtButton);
        panel.add(modButton); // % -> împarte la 100 când e apăsat

        // Rânduri numerice și operatori
        panel.add(numberButtons[7]);
        panel.add(numberButtons[8]);
        panel.add(numberButtons[9]);
        panel.add(divButton);

        panel.add(numberButtons[4]);
        panel.add(numberButtons[5]);
        panel.add(numberButtons[6]);
        panel.add(mulButton);

        panel.add(numberButtons[1]);
        panel.add(numberButtons[2]);
        panel.add(numberButtons[3]);
        panel.add(subButton);

        panel.add(decButton);
        panel.add(numberButtons[0]);
        panel.add(equButton);
        panel.add(addButton);

        frame.add(panel);
        frame.add(delButton);
        frame.add(clrButton);
        frame.add(textfield);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new calculator();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Butoane cifre
        for (int i = 0; i < 10; i++) {
            if (e.getSource() == numberButtons[i]) {
                textfield.setText(textfield.getText().concat(String.valueOf(i)));
            }
        }

        if (e.getSource() == decButton) {
            // verifică dacă deja există un punct
            if (!textfield.getText().contains(".")) {
                if (textfield.getText().isEmpty()) textfield.setText("0.");
                else textfield.setText(textfield.getText().concat("."));
            }
        }

        if (e.getSource() == addButton) setOperator('+');
        if (e.getSource() == subButton) setOperator('-');
        if (e.getSource() == mulButton) setOperator('*');
        if (e.getSource() == divButton) setOperator('/');

        if (e.getSource() == equButton) {
            if (textfield.getText().isEmpty()) return;
            try {
                num2 = Double.parseDouble(textfield.getText());
            } catch (NumberFormatException ex) {
                textfield.setText("Error");
                return;
            }

            switch (operator) {
                case '+':
                    result = num1 + num2;
                    break;
                case '-':
                    result = num1 - num2;
                    break;
                case '*':
                    result = num1 * num2;
                    break;
                case '/':
                    if (num2 == 0) {
                        textfield.setText("Error: div/0");
                        operator = '\0';
                        return;
                    } else {
                        result = num1 / num2;
                    }
                    break;
                default:
                    // dacă nu e operator (de exemplu s-a apăsat "=" fără operator), afișează ce e în text
                    result = num2;
                    break;
            }
            textfield.setText(formatResult(result));
            num1 = result;
            operator = '\0';
        }

        if (e.getSource() == clrButton) {
            textfield.setText("");
            operator = '\0';
        }

        if (e.getSource() == delButton) {
            String text = textfield.getText();
            if (!text.isEmpty()) {
                textfield.setText(text.substring(0, text.length() - 1));
            }
        }

        // √
        if (e.getSource() == sqrtButton) {
            if (textfield.getText().isEmpty()) return;
            try {
                double val = Double.parseDouble(textfield.getText());
                if (val < 0) {
                    textfield.setText("Error");
                } else {
                    textfield.setText(formatResult(Math.sqrt(val)));
                }
            } catch (NumberFormatException ex) {
                textfield.setText("Error");
            }
        }

        // x^2
        if (e.getSource() == sqrButton) {
            if (textfield.getText().isEmpty()) return;
            try {
                double val = Double.parseDouble(textfield.getText());
                textfield.setText(formatResult(val * val));
            } catch (NumberFormatException ex) {
                textfield.setText("Error");
            }
        }

        // % -> transformă valoarea curentă în procent (împarte cu 100)
        if (e.getSource() == modButton) {
            if (textfield.getText().isEmpty()) return;
            try {
                double val = Double.parseDouble(textfield.getText());
                val = val / 100.0;
                textfield.setText(formatResult(val));
            } catch (NumberFormatException ex) {
                textfield.setText("Error");
            }
        }

        // Memorie
        if (e.getSource() == mcButton) {
            memory = 0;
        }
        if (e.getSource() == mrButton) {
            textfield.setText(formatResult(memory));
        }
        if (e.getSource() == mPlusButton) {
            if (textfield.getText().isEmpty()) return;
            try {
                memory += Double.parseDouble(textfield.getText());
            } catch (NumberFormatException ex) {
                // ignore sau afișează eroare
                textfield.setText("Error");
            }
        }
        if (e.getSource() == mMinusButton) {
            if (textfield.getText().isEmpty()) return;
            try {
                memory -= Double.parseDouble(textfield.getText());
            } catch (NumberFormatException ex) {
                textfield.setText("Error");
            }
        }
        if (e.getSource() == msButton) {
            if (textfield.getText().isEmpty()) return;
            try {
                memory = Double.parseDouble(textfield.getText());
            } catch (NumberFormatException ex) {
                textfield.setText("Error");
            }
        }
    }

    private void setOperator(char op) {
        if (textfield.getText().isEmpty()) return;
        try {
            num1 = Double.parseDouble(textfield.getText());
        } catch (NumberFormatException ex) {
            textfield.setText("Error");
            return;
        }
        operator = op;
        textfield.setText("");
    }

    // Formatează afișarea: dacă e număr întreg -> afișează fără .0
    private String formatResult(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) return "Error";
        if (value == (long) value) {
            return String.format("%d", (long) value);
        } else {
            return String.valueOf(value);
        }
    }
}
