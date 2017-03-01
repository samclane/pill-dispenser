package com.baseStationPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ViewPatientData extends ViewPatientDataBO implements ActionListener {

    private JFrame frame;
    private JButton lookUpButton;
    private JButton saveChangesButton;
    private JButton cancelChangesButton;
    private JComboBox searchByCombo;
    private JTextField searchByTextField;
    private JLabel searchByLabel;
    private JTable table;
    private TableModel model;
    private Vector rowData;
    private Vector dataLabels;
    private TableModelListener tableListener;

    String select = "";
    String from = "";
    String where = "";
    String is = "";



    public ViewPatientData() {
        initialize();


    }
    public void initialize() {

        frame = new JFrame("View Patient Data");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 250));
        frame.setLayout(new FlowLayout());


        searchByLabel = new JLabel("Search By: ");
        searchByTextField = new JTextField(30);
        dataLabels = getLabelData();
        searchByCombo = new JComboBox(dataLabels);

        lookUpButton = new JButton("Look Up");
        lookUpButton.setActionCommand("Look Up");
        lookUpButton.addActionListener(this);

        saveChangesButton = new JButton("Save Changes");
        saveChangesButton.setActionCommand("Save Changes");
        saveChangesButton.addActionListener(this);

        cancelChangesButton = new JButton("Cancel Changes");
        cancelChangesButton.setActionCommand("Cancel Changes");
        cancelChangesButton.addActionListener(this);

        table = new JTable();


        frame.getContentPane().add(searchByLabel);
        frame.getContentPane().add(searchByCombo);
        frame.getContentPane().add(searchByTextField);
        frame.getContentPane().add(lookUpButton);
        frame.getContentPane().add(saveChangesButton);
        frame.getContentPane().add(cancelChangesButton);
        frame.getContentPane().add(table);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        isEditing(false);
    }


    public void lookUp() {
        String searchBySelection = searchByCombo.getSelectedItem().toString();
        String searchByText = searchByTextField.getText();
        rowData = getRowData(searchBySelection, searchByText);
        //update rows of data
        ((DefaultTableModel) table.getModel()).setDataVector(rowData, dataLabels);
        isEditing(true);
    }

    public void saveChanges() {
     //edit database here
        Vector changedData = new Vector(table.getColumnCount());
        for(int i = 0; i<table.getColumnCount();i++) {
            Object value = table.getValueAt(1,i);
            changedData.add(i,value);
        }
        updateDatabase(changedData);
        isEditing(false);
    }

    public void cancelChanges() {


        isEditing(false);
    }

    public void isEditing(boolean editing) {
        saveChangesButton.setVisible(editing);
        cancelChangesButton.setVisible(editing);
        searchByLabel.setVisible(!editing);
        searchByCombo.setVisible(!editing);
        searchByTextField.setVisible(!editing);
        lookUpButton.setVisible(!editing);
        table.setVisible(editing);
    }



    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Look Up")) {
            lookUp();
        }
        if(e.getActionCommand().equals("Save Changes")) {
            saveChanges();
        }
        if(e.getActionCommand().equals("Cancel Changes")) {
            cancelChanges();
        }


    }
    public static void main(String[] args)
    {
        new ViewPatientData();
    }

}
