package com.example;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GUI {

    private String message, languageCode, answer = "";
    private String[] userInput = new String[2];
    public Boolean newInput = false;
    private JLabel labelAnswer = new JLabel("Answer: " + answer);

    public GUI() {
        SwingUtilities.invokeLater(() -> createGui());
    }

    protected void createGui() {
        JFrame frame = new JFrame();
        frame.setTitle("Tranlator Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel upperPanel = new JPanel();
        JPanel centralPanel = new JPanel();
        JPanel lowerPanel = new JPanel();
        upperPanel.setLayout(new FlowLayout());
        upperPanel.setSize(new Dimension(200, 200));
        centralPanel.setLayout(new FlowLayout());
        lowerPanel.setLayout(new FlowLayout());

        JLabel labelLangCode = new JLabel("Enter language code");
        JLabel labelWord = new JLabel("Enter word to translate");

        JTextField textField = new JTextField(80);
        JTextField textFieldLangCode = new JTextField(5);
        JButton button = new JButton("Submit");

        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                newInput = true;
                userInput[0] = textField.getText();
                userInput[1] = textFieldLangCode.getText();
            }

        });

        upperPanel.add(labelWord);
        upperPanel.add(textField);
        centralPanel.add(labelLangCode);
        centralPanel.add(textFieldLangCode);
        centralPanel.add(button);
        lowerPanel.add(labelAnswer);

        frame.add(upperPanel, BorderLayout.NORTH);
        frame.add(centralPanel, BorderLayout.CENTER);
        frame.add(lowerPanel, BorderLayout.SOUTH);

        frame.setResizable(true);
        frame.pack();
        frame.setVisible(true);
    }

    public String[] getUserInput() {
        newInput = false;
        return userInput;
    }

    public void updateAnswer(String translated) {
        labelAnswer.setText(translated);
    }
}
