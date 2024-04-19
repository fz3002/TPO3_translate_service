package com.example;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GUI {

    private String answer = "";
    private String[] userInput = new String[2];
    volatile public Boolean newInput = false;
    volatile private JLabel labelAnswer = new JLabel("Answer: " + answer);
    final JFrame frame = new JFrame();

    public GUI() {
        SwingUtilities.invokeLater(() -> createGui());
    }

    protected void createGui() {
        frame.setTitle("Tranlator Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu menu= new JMenu("Options");
        JMenuItem optionButton = new JMenuItem("Add new server");
        optionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField textField = new JTextField(20);
                JButton chooseFileButton = new JButton("Choose File");

                JPanel panel = new JPanel();
                panel.add(new JLabel("File Path: "));
                panel.add(textField);
                panel.add(chooseFileButton);

                chooseFileButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();
                        int returnValue = fileChooser.showOpenDialog(null);
                        if (returnValue == JFileChooser.APPROVE_OPTION) {
                            File selectedFile = fileChooser.getSelectedFile();
                            textField.setText(selectedFile.getAbsolutePath());
                        }
                    }
                });

                int option = JOptionPane.showConfirmDialog(null, panel, "Add Server", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String filePath = textField.getText();
                }
            }
        });

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
                userInput[0] = textField.getText().toLowerCase();
                userInput[1] = textFieldLangCode.getText().toUpperCase();
            }

        });

        menu.add(optionButton);
        menuBar.add(menu);

        upperPanel.add(labelWord);
        upperPanel.add(textField);
        centralPanel.add(labelLangCode);
        centralPanel.add(textFieldLangCode);
        centralPanel.add(button);
        lowerPanel.add(labelAnswer);

        frame.add(upperPanel, BorderLayout.NORTH);
        frame.add(centralPanel, BorderLayout.CENTER);
        frame.add(lowerPanel, BorderLayout.SOUTH);

        frame.setJMenuBar(menuBar);

        frame.setResizable(true);
        frame.pack();
        frame.setVisible(true);
    }

    public String[] getUserInput() {
        newInput = false;
        return userInput;
    }

    public JLabel getLabel() {
        return labelAnswer;
    }

    public boolean newInputAvailable() {
        return newInput;
    }

    public void raiseError(String errorMessage) {
        JOptionPane.showMessageDialog(frame, errorMessage);
    }
}
