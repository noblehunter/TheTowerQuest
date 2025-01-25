package edu.penzgtu.ui;

import java.util.Scanner;

public class ConsoleUI implements GameUI {
    private Scanner scanner = new Scanner(System.in);
    private boolean initialScreenErrorToggle = true;
    private String currentMenuOptions;

    @Override
    public void showGameScreen(String message) {
        clearScreen();
        showOutput(message);
        currentMenuOptions = getMenuOptions();
    }

    @Override
    public void showMenu(String menuOptions) {
        showOutput(menuOptions);
        currentMenuOptions = menuOptions;
    }

    @Override
    public int getUserInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch(NumberFormatException e) {
            if (currentMenuOptions == null) {
                String errorMessage = initialScreenErrorToggle ? "Неверный ввод, введите целое число." : "Снова неверный ввод, введите целое число.";
                initialScreenErrorToggle = !initialScreenErrorToggle;
                showInitialScreenError(errorMessage, "", "", "");
            } else {
                String errorMessage = initialScreenErrorToggle ? "Неверный ввод, введите целое число." : "Снова неверный ввод, введите целое число.";
                initialScreenErrorToggle = !initialScreenErrorToggle;
                showErrorAndMenu(errorMessage, getMenuOptions(), "", "");
            }
            return getUserInput();
        }
    }

    @Override
    public String getItemNameInput() {
        return scanner.nextLine();
    }

    @Override
    public String getMonsterNameInput() {
        return scanner.nextLine();
    }

    @Override
    public void showOutput(String message) {
        System.out.println("------------------------------");
        System.out.println(message);
        System.out.println("------------------------------");
    }

    @Override
    public void clearScreen() {
        // Для консоли очистка не требуется
    }

    public void showErrorAndMenu(String message, String menuOptions, String content, String prevMessage) {
        clearScreen();
        showOutput(prevMessage);
        showOutput(message);
        showOutput(content);
        showMenu(menuOptions);
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

    @Override
    public void showDetailedRoomDialog(String message) {
        showOutput("------ Подробное описание комнаты ------");
        showOutput(message);
        showOutput("----------------------------------------");
    }

    @Override
    public void showInitialScreenError(String message, String menuOptions, String content, String prevMessage) {
        showOutput(message);
        showInitialScreenMenu();
    }
    @Override
    public void showInitialScreenMenu(){
        showMenu("1. Новая игра\n" +
                "2. Загрузить игру\n" +
                "3. Выход");
    }
}