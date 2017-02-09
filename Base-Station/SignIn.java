package com.baseStationPackage;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import static com.baseStationPackage.SignInBO.passwordCorrect;

public class SignIn extends JFrame implements ActionListener {


    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JTextField userNameField;
    private JTextField passwordField;
    private JButton signInButton;
    private boolean access = false;

    public SignIn() {

    super("Sign In");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setPreferredSize(new Dimension(500, 250));
    ((JPanel) getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20) );
    setLayout(new FlowLayout());
    signInButton = new JButton("Sign In");
    signInButton.setActionCommand("Sign In");
    signInButton.addActionListener(this);

    userNameLabel = new JLabel("User Name: ");
    passwordLabel = new JLabel("Password: ");
    userNameField = new JTextField(30);
    passwordField = new JPasswordField(30);

    add(userNameLabel);
    add(userNameField);
    add(passwordLabel);
    add(passwordField);
    add(signInButton);
    pack();
    setVisible(true);
    setResizable(false);
}
    public void actionPerformed(ActionEvent e)
    {

        if(e.getActionCommand().equals("Sign In")) {

            try {
                access = passwordCorrect(userNameField.getText(), passwordField.getText());
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            if(access) {
                access = true;
                dispose();
                new BaseStation();
            }
            else  {
                JOptionPane.showMessageDialog(null,
                        "Wrong User Name or Password. Please try again.");
            }
        }
    }
    public static void main(String[] args)
    {
        new SignIn();
    }

}
