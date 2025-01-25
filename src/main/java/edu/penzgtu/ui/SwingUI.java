package edu.penzgtu.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SwingUI implements GameUI {
    private JFrame frame;
    private JTextArea outputArea;
    private JTextField inputField;
    private JButton submitButton;

    private CompletableFuture<String> inputFuture;

    private String currentMenuOptions;
    private String mesedge = "Неверный ввод, введите целое число.";
    private int errorCount;
    private String prevText;
    private boolean initialScreenErrorToggle = true;


    public SwingUI() {
        frame = new JFrame("The Tower Quest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputField = new JTextField(20);
        inputField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (inputFuture != null && !inputFuture.isDone()) {
                        inputFuture.complete(inputField.getText());
                        inputField.setText("");
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });

        submitButton = new JButton("Ввод");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputFuture != null && !inputFuture.isDone()) {
                    inputFuture.complete(inputField.getText());
                    inputField.setText("");
                }
            }
        });
        inputPanel.add(inputField);
        inputPanel.add(submitButton);
        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
    @Override
    public void showGameScreen(String message) {
        clearScreen();
        showOutput(message);
        currentMenuOptions = getMenuOptions();
        prevText = message + "\n" + "------------------------------\n" + currentMenuOptions;
    }

    @Override
    public void showMenu(String menuOptions) {
        showOutput(menuOptions);
        currentMenuOptions = menuOptions;
    }

    @Override
    public int getUserInput() {
        try {
            return Integer.parseInt(getInput());
        } catch (NumberFormatException e) {
            errorCount++;
            if (currentMenuOptions == null) {
                String errorMessage = initialScreenErrorToggle ? "Неверный ввод, введите целое число." : "Снова неверный ввод, введите целое число.";
                initialScreenErrorToggle = !initialScreenErrorToggle;
                showInitialScreenError(errorMessage, "", "", "");
            } else {
                if (errorCount % 2 != 0) {
                    mesedge = "Неверный ввод, введите целое число.";
                } else {
                    mesedge = "Снова неверный ввод, введите целое число.";
                }
                showErrorAndMenu(mesedge, currentMenuOptions, "", "");
            }
            return getUserInput();
        }
    }

    @Override
    public String getItemNameInput() {
        return getInput();
    }



    @Override
    public String getMonsterNameInput() {
        return getInput();
    }

    private String getInput() {
        inputFuture = new CompletableFuture<>();
        try {
            return inputFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return "";
        }

    }
    @Override
    public void showOutput(String message) {
        if(message != null && !message.isEmpty()){
            outputArea.append("------------------------------\n");
            outputArea.append(message + "\n");
            outputArea.append("------------------------------\n");
        }
    }
    @Override
    public void clearScreen() {
        outputArea.setText("");
    }


    public  void showErrorAndMenu(String message, String menuOptions, String content, String prevMessage) {

        showOutput(prevText);
        showOutput(message);
    }
    @Override
    public void showInitialScreenError(String message, String menuOptions, String content, String prevMessage) {
        clearScreen();
        showOutput(message);
        showInitialScreenMenu();
    }
    private String getMenuOptions() {
        return "1. Перейти к \n" +
                "2. Подобрать предмет\n" +
                "3. Сразиться с монстром?\n" +
                "4. Что я вижу вокруг?\n" +
                "5. Персонаж\n" +
                "6. Инвентарь\n" +
                "7. Сохранить игру\n" +
                "8. Попробовать взломать дверь\n" +
                "9. Выйти в главное меню\n";
    }
    public void showDetailedRoomDialog(String message) {
        showOutput("------ Подробное описание комнаты ------");
        showOutput(message);
        showOutput("----------------------------------------");
    }
    @Override
    public void showInitialScreenMenu(){
        showMenu("1. Новая игра\n" +
                "2. Загрузить игру\n" +
                "3. Выход");
    }
}
