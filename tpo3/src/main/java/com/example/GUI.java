package com.example;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GUI {

    private String message;
    private String languageCode;

    public GUI(){
        SwingUtilities.invokeLater(() -> createGui());
    }

    protected void createGui() {
        JFrame frame = new JFrame();
        frame.setTitle("Tranlator Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        JTextField textField = new JTextField();
        JButton button = new JButton("Submit");

        frame.add(textField, BorderLayout.CENTER);
        frame.add(button);

        frame.setResizable(true);
        frame.pack();
        frame.setVisible(true);
    }
}
