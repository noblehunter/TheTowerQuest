package edu.penzgtu;

import edu.penzgtu.entities.GameState;
import edu.penzgtu.entities.Player;
import edu.penzgtu.entities.RoomType;
import edu.penzgtu.game.*;
import edu.penzgtu.services.GameService;
import edu.penzgtu.services.SaveLoadService;
import edu.penzgtu.ui.GameUI;
import edu.penzgtu.ui.SwingUI;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;


public class Main {
    private static Map<Integer, GameAction> actions;

    public static void main(String[] args) throws IOException {
        GameUI ui = new SwingUI();
        SaveLoadService saveLoadService = new SaveLoadService();
        initialScreen(ui, saveLoadService);
    }
    private static void initialScreen(GameUI ui, SaveLoadService saveLoadService) throws IOException {
        boolean inInitialScreen = true;
        String menuOptions = "1. Новая игра\n" +
                "2. Загрузить игру\n" +
                "3. Выход";
        ui.clearScreen();
        ui.showInitialScreenMenu();
        while (inInitialScreen) {

            int action = ui.getUserInput();
            switch (action) {
                case 1:
                    Player player = createPlayer(ui);
                    GameService gameService = new GameService(ui);
                    gameService.startNewGame(player);
                    gameLoop(ui, gameService, saveLoadService);
                    inInitialScreen = false;
                    break;
                case 2:
                    ui.showOutput("Введите номер слота для загрузки (1-10):");
                    int slot = ui.getUserInput();
                    try {
                        GameState loadedState = saveLoadService.loadGame(slot);
                        GameService gameService1 = new GameService(ui);
                        gameService1.setGameState(loadedState);
                        gameLoop(ui, gameService1, saveLoadService);
                        inInitialScreen = false;
                    } catch (Exception e) {
                        ui.showOutput("Сохранения не найдены");
                    }
                    break;
                case 3:
                    inInitialScreen = false;
                    break;
                default:
                    String errorMessage = "Неверный ввод, выберете другое доступное число\n";
                    ui.showInitialScreenError(errorMessage, "", "", "");
                    break;
            }
        }
    }


    private static Player createPlayer(GameUI ui) {
        ui.showOutput("\n--- Создание персонажа ---");
        ui.showOutput("Введите имя вашего персонажа:");
        String playerName = ui.getItemNameInput();
        ui.showOutput("Имя персонажа: " + playerName);
        return new Player(playerName);
    }

    private static void gameLoop(GameUI ui, GameService gameService, SaveLoadService saveLoadService) throws IOException {
        Random random = new Random();
        Player player = gameService.getPlayer();
        Main MainInstance = new Main();
        String prevMessage = "";
        actions = createActions(saveLoadService, random);


        while (player.getHealth() > 0 && !player.hasArtifact()) {
            gameService.showGameScreen();
            ui.showMenu(getMenuOptions());
            int action = ui.getUserInput();
            GameEvent event = actions.get(action).getEvent();
            if(event != null) {
                event.execute(ui, gameService);
                if (gameService.getGameState().getCurrentRoomId() == RoomType.GO_HOME.ordinal() || gameService.getGameState().getCurrentRoomId() ==  RoomType.END_GAME_1.ordinal() || gameService.getGameState().getCurrentRoomId() ==  RoomType.END_GAME_2.ordinal() || gameService.getGameState().getCurrentRoomId() ==  RoomType.END_GAME_3.ordinal() || gameService.getGameState().getCurrentRoomId() ==  RoomType.END_GAME_4.ordinal()){
                    endGame(ui, saveLoadService, gameService);
                    return;
                }
            } else {
                if (gameService.getGameState().getCurrentRoomId() == 0) {
                    if (action == 1){
                        endGame(ui, saveLoadService, gameService);
                        return;
                    }
                    if (action == 2) {
                        gameService.movePlayerToRoom(RoomType.BEFORE_TOWER.ordinal());
                    }
                }else {
                    ui.showOutput("Неверный выбор действия.");
                }
            }

        }
        if (player.hasArtifact()) {
            ui.showOutput("Поздравляем, вы победили!");
        } else if(player.getHealth() <= 0){
            ui.showOutput("Вы проиграли. Игра окончена.");
        }
        initialScreen(ui, saveLoadService);
    }
    private static void endGame(GameUI ui, SaveLoadService saveLoadService, GameService gameService) throws IOException{
        while(true){
            ui.showMenu("0. Выйти в главное меню");
            int action = ui.getUserInput();
            if(action == 0){
                initialScreen(ui, saveLoadService);
                return;
            } else{
                ui.showOutput("Неверный ввод");
            }
        }
    }
    private static String getMenuOptions() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, GameAction> entry : actions.entrySet()) {
            sb.append(entry.getKey()).append(". ").append(entry.getValue().getName()).append("\n");
        }
        return sb.toString();
    }
    private static boolean battleMenu(GameUI ui, GameService gameService, String monsterName, String type, String prevMessage) throws IOException {
        while (type.equals("battle")){
            if(gameService.battle(monsterName, ui)){
                if (gameService.getPlayer().getHealth() <= 0) {
                    return false;
                }
                type = "";
            }
        }
        return true;
    }
    private static void detailedRoomLoop(GameUI ui, GameService gameService) {
        boolean inDetailedRoom = true;
        while (inDetailedRoom) {
            ui.clearScreen();
            ui.showOutput(gameService.showBriefRoomDescription());
            ui.showMenu("1. Назад");
            int action = ui.getUserInput();
            if (action == 1) {
                inDetailedRoom = false;
            }
        }
    }
    private static void handleDoor(GameUI ui, GameService gameService, Random random) {
        if (gameService.getGameState().getCurrentRoomId() == RoomType.BEFORE_TOWER.ordinal() && !gameService.getGameState().isDoorBroken()) {
            int attempts = 3;
            boolean success = false;
            for (int i = 0; i < attempts; i++) {
                if (random.nextInt(100) < 30) {
                    success = true;
                    ui.showOutput("Вы взломали дверь!");
                    gameService.getGameState().setDoorBroken(true);
                    gameService.movePlayerToRoom(RoomType.ENTRANCE.ordinal());
                    break;
                } else {
                    ui.showOutput("Попытка взлома неудачна. Осталось попыток: " + (attempts - i - 1));
                }
            }
            if (success) {
                int damage = 5 + random.nextInt(11);
                ui.showOutput("Вы выбили дверь, получив " + damage + " урона.");
                int currentHealth = gameService.getPlayer().getHealth() - damage;
                gameService.getPlayer().setHealth(currentHealth);
                ui.showOutput("Текущее здоровье: " + currentHealth);
                gameService.getGameState().setDoorBroken(true);
                gameService.movePlayerToRoom(RoomType.ENTRANCE.ordinal());
            } else {
                ui.showOutput("Вы не можете взломать дверь здесь.");
            }
        }
        else {
            ui.showOutput("Вы не можете взломать дверь здесь.");
        }
    }
    private static void inventoryMenu(GameUI ui, GameService gameService, String prevMessage) throws IOException {
        while (true) {
            String inventory = gameService.showInventory();
            if (inventory.equals("Инвентарь пуст.\n")) {
                ui.showOutput(inventory);
                return;
            }
            ui.showOutput(inventory);
            ui.showOutput("Введите номер предмета для взаимодействия или 0 для выхода в меню:");
            int choice = ui.getUserInput();

            if (choice == 0) {
                return;
            }
            Player player = gameService.getPlayer();
            if(choice > 0 && choice <= player.getInventory().size()) {
                edu.penzgtu.entities.Item selectedItem = player.getInventory().get(choice - 1);
                ui.showOutput("Вы выбрали " + selectedItem.getName() + ".\n");
                ui.showOutput("1. Использовать предмет\n");
                ui.showOutput("2. Экипировать предмет\n");
                int actionChoice = ui.getUserInput();
                String resultMessage = "";
                switch (actionChoice) {
                    case 1:
                        resultMessage = gameService.useItem(selectedItem.getName(), ui);
                        break;
                    case 2:
                        resultMessage = gameService.equipItem(selectedItem.getName());
                        break;
                    default:
                        String inventoryMenuOptions = "1. Использовать предмет\n" +
                                "2. Экипировать предмет\n";
                        ui.showErrorAndMenu("Неверный выбор действия.\n", inventoryMenuOptions, inventory, prevMessage);
                        return;
                }
                if (!resultMessage.isEmpty()) {
                    ui.showOutput(resultMessage);
                    inventoryMenu(ui, gameService, prevMessage);
                    return;
                }
            } else {
                String inventoryMenuOptions = "1. Использовать предмет\n" +
                        "2. Экипировать предмет\n";
                ui.showErrorAndMenu("Неверный выбор предмета\n", inventoryMenuOptions, inventory, prevMessage);
            }
        }
    }
    private static Map<Integer, GameAction> createActions(SaveLoadService saveLoadService, Random random){
        Map<Integer, GameAction> actions = new HashMap<>();
        actions.put(1, new GameAction("Перейти к", null));
        actions.put(2, new GameAction("Подобрать предмет", new PickUpItemEvent(null)));
        actions.put(3, new GameAction("Сразиться с монстром?", new BattleEvent(null)));
        actions.put(4, new GameAction("Что я вижу вокруг?", new DetailedRoomEvent()));
        actions.put(5, new GameAction("Персонаж", new ShowPlayerInfoEvent()));
        actions.put(6, new GameAction("Инвентарь", new UseItemEvent(null, null)));
        actions.put(7, new GameAction("Сохранить игру", new SaveGameEvent(0, saveLoadService)));
        actions.put(8, new GameAction("Попробовать взломать дверь", new HandleDoorEvent(random)));
        actions.put(9, new GameAction("Выйти в главное меню", new ExitToMenuEvent(saveLoadService)));
        return actions;
    }
}