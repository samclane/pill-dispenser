package com.baseStationPackage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BaseStation extends JFrame implements ActionListener {


    private JButton viewPatientData;
    private JButton viewUnitData;
    private JButton remoteControl;


    public BaseStation() {

        super("Base Station");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 250));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20) );
        setLayout(new FlowLayout());

        //buttons
        viewPatientData = new JButton("View Patient Data");
        viewPatientData.setActionCommand("View Patient Data");
        viewPatientData.addActionListener(this);

        viewUnitData = new JButton("View Unit Data");
        viewUnitData.setActionCommand("View Unit Data");
        viewUnitData.addActionListener(this);

        remoteControl = new JButton("Remote Control");
        remoteControl.setActionCommand("Remote Control");
        remoteControl.addActionListener(this);

        add(viewPatientData);
        add(viewUnitData);
        add(remoteControl);

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
