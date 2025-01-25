package edu.penzgtu.ui;

public interface GameUI {
    void showGameScreen(String message);
    void showMenu(String menuOptions);
    int getUserInput();
    String getItemNameInput();
    String getMonsterNameInput();
    void showOutput(String message);
    void clearScreen();
    void showErrorAndMenu(String message, String menuOptions, String content, String prevMessage);
    void showInitialScreenError(String message, String menuOptions, String content, String prevMessage);
    void showDetailedRoomDialog(String message);
    void showInitialScreenMenu();
}