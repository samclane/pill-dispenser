package com.baseStationPackage;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.baseStationPackage.ViewPatientDataDAO.getPatientResultSet;

public class ViewPatientData extends JFrame implements ActionListener {

    private JButton lookUpButton;
    private String[] cols = {"Name", "Age"};
    private JTable table;

    public ViewPatientData() {

        super("View Patient Data");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 250));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20) );
        setLayout(new FlowLayout());


        lookUpButton = new JButton("Look Up");
        lookUpButton.setActionCommand("Look Up");
        lookUpButton.addActionListener(this);


        add(lookUpButton);
        pack();
        setVisible(true);
        setResizable(false);
    }
    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals("Look Up")) {
            //table = new JTable(getPatientResultSet(), cols);
        }


    }
    public static void main(String[] args)
    {
        new ViewPatientData();
    }

}
