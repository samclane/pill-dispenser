package com.baseStationPackage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BaseStation implements ActionListener {

    private JMenuBar menuBar;
    private JMenu file;
    private JMenuItem viewPatientData;
    private JMenuItem viewUnitData;
    private JMenuItem remoteControl;
    private JMenu options;
    private JMenuItem resetPassword;
    private JMenuItem addNewUser;
    private JFrame frame;

    public BaseStation() {

        frame = new JFrame("Home");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 250));
        frame.setLayout(new FlowLayout());
        frame.setResizable(false);

        //buttons
        menuBar= new JMenuBar();

        file = new JMenu("File");
        file.setActionCommand("File");
        file.addActionListener(this);

        viewPatientData = new JMenuItem("View Patient Data");
        viewPatientData.setActionCommand("View Patient Data");
        viewPatientData.addActionListener(this);

        viewUnitData = new JMenuItem("View Unit Data");
        viewUnitData.setActionCommand("View Unit Data");
        viewUnitData.addActionListener(this);

        remoteControl = new JMenuItem("Remote Control");
        remoteControl.setActionCommand("Remote Control");
        remoteControl.addActionListener(this);

        options = new JMenu("Options");
        options.setActionCommand("Options");
        options.addActionListener(this);

        resetPassword = new JMenuItem("Reset Password");
        resetPassword.setActionCommand("Reset Password");
        resetPassword.addActionListener(this);

        addNewUser = new JMenuItem("Add New User");
        addNewUser.setActionCommand("Add New User");
        addNewUser.addActionListener(this);

        menuBar.add(file);
        menuBar.add(options);
        file.add(viewPatientData);
        file.add(viewUnitData);
        file.add(remoteControl);
        options.add(addNewUser);
        options.add(resetPassword);


        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
        new BaseStation();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("View Patient Data")) {
            frame.dispose();
            new ViewPatientData();
        }

    }
}
