package com.baseStationPackage;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import static com.baseStationPackage.SignInBO.passwordCorrect;

public class SignIn implements ActionListener {


    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JTextField userNameField;
    private JTextField passwordField;
    private JButton signInButton;
    private JFrame frame;
    private boolean access = false;

    public SignIn() {

        frame = new JFrame("Home");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(450, 250));
        frame.setLayout(new FlowLayout());
        frame.setResizable(false);

        signInButton = new JButton("Sign In");
        signInButton.setActionCommand("Sign In");
        signInButton.addActionListener(this);

        userNameLabel = new JLabel("User Name: ");
        passwordLabel = new JLabel("Password: ");
        userNameField = new JTextField(30);
        passwordField = new JPasswordField(30);

        frame.getContentPane().add(userNameLabel);
        frame.getContentPane().add(userNameField);
        frame.getContentPane().add(passwordLabel);
        frame.getContentPane().add(passwordField);
        frame.getContentPane().add(signInButton);
        frame.pack();
        frame.setVisible(true);
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
                frame.dispose();
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
