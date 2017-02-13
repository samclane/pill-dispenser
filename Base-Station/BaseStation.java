package com.baseStationPackage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BaseStation extends JFrame implements ActionListener {

    private JMenuBar menuBar;
    private JMenu file;
    private JMenuItem viewPatientData;
    private JMenuItem viewUnitData;
    private JMenuItem remoteControl;
    private JMenu options;
    private JMenuItem resetPassword;
    private JMenuItem addNewUser;

    public BaseStation() {

        super("Base Station");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 250));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20) );
        setLayout(new FlowLayout());

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


        setJMenuBar(menuBar);
        pack();
        setVisible(true);
        setResizable(false);
    }

    public static void main(String[] args)
    {
        new BaseStation();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
