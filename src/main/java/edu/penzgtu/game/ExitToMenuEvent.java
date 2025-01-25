package edu.penzgtu.game;

import edu.penzgtu.entities.Player;
import edu.penzgtu.entities.RoomType;
import edu.penzgtu.services.GameService;
import edu.penzgtu.entities.GameState;
import edu.penzgtu.services.SaveLoadService;
import edu.penzgtu.ui.GameUI;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import edu.penzgtu.Main;
import edu.penzgtu.entities.Item;


public class ExitToMenuEvent implements GameEvent{
    private final SaveLoadService saveLoadService;
    public ExitToMenuEvent(SaveLoadService saveLoadService) {
        this.saveLoadService = saveLoadService;
    }

    @Override
    public void execute(GameUI ui, GameService gameService) throws IOException {
        initialScreen(ui, saveLoadService);
    }
    private void initialScreen(GameUI ui, SaveLoadService saveLoadService) throws IOException {
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

    private  Player createPlayer(GameUI ui) {
        ui.showOutput("\n--- Создание персонажа ---");
        ui.showOutput("Введите имя вашего персонажа:");
        String playerName = ui.getItemNameInput();
        ui.showOutput("Имя персонажа: " + playerName);
        return new Player(playerName);
    }
    private void gameLoop(GameUI ui, GameService gameService, SaveLoadService saveLoadService) throws IOException {
        Random random = new Random();
        Player player = gameService.getPlayer();
        Main MainInstance = new Main();
        String prevMessage = "";

        while (player.getHealth() > 0 && !player.hasArtifact()) {
            gameService.showGameScreen();
            gameService.showMenu(getMenuOptions());
            int action = ui.getUserInput();
            String message = "";
            switch (action) {
                case 1:
                    ui.clearScreen();
                    String availableRooms = gameService.showAvailableRooms();
                    gameService.showMenu(availableRooms);
                    int roomChoice = ui.getUserInput();
                    int roomId = 0;
                    if(gameService.getGameState().getCurrentRoomId() == 0) {
                        if (roomChoice == 1) {
                            initialScreen(ui, saveLoadService);
                            return;
                        }
                        else if (roomChoice == 2) {
                            roomId = RoomType.BEFORE_TOWER.ordinal();
                        }
                    }  else{
                        if (gameService.getGameState().getRoomConnections().containsKey(RoomType.values()[gameService.getGameState().getCurrentRoomId()])) {
                            List<RoomType> connections = gameService.getGameState().getRoomConnections().get(RoomType.values()[gameService.getGameState().getCurrentRoomId()]);
                            if(roomChoice > 0 && roomChoice <= connections.size()) {
                                roomId = connections.get(roomChoice - 1).ordinal();
                            } else {
                                ui.showOutput("Неверный номер комнаты.");
                            }
                        }
                    }
                    gameService.movePlayerToRoom(roomId);
                    if (gameService.getGameState().getCurrentRoomId() == RoomType.GO_HOME.ordinal() || gameService.getGameState().getCurrentRoomId() ==  RoomType.END_GAME_1.ordinal() || gameService.getGameState().getCurrentRoomId() ==  RoomType.END_GAME_2.ordinal() || gameService.getGameState().getCurrentRoomId() ==  RoomType.END_GAME_3.ordinal() || gameService.getGameState().getCurrentRoomId() ==  RoomType.END_GAME_4.ordinal()){
                        endGame(ui, saveLoadService, gameService);
                        return;
                    }
                    gameService.showGameScreen();
                    break;
                case 2:
                    ui.clearScreen();
                    ui.showOutput(gameService.showAvailableItems());
                    String itemName = ui.getItemNameInput();
                    gameService.pickUpItem(itemName);
                    break;
                case 3:
                    ui.clearScreen();
                    if (gameService.hasMonsterInCurrentRoom()) {
                        String monsterName = ui.getMonsterNameInput();
                        if (!battleMenu(ui, gameService, monsterName, "battle", prevMessage)) {
                            initialScreen(ui, saveLoadService);
                            return;
                        }
                    }
                    break;
                case 4:
                    detailedRoomLoop(ui, gameService);
                    break;
                case 5:
                    ui.showOutput(player.toString());
                    break;
                case 6:
                    ui.showOutput(gameService.showInventory());
                    ui.showOutput("Какой предмет вы хотите использовать?\n");
                    String itemNameForUse = ui.getItemNameInput();
                    ui.showOutput(gameService.useItem(itemNameForUse, ui));
                    ui.showOutput("Какой предмет вы хотите экипировать?\n");
                    String itemNameForEquip = ui.getItemNameInput();
                    ui.showOutput(gameService.equipItem(itemNameForEquip));
                    break;
                case 7:
                    ui.showOutput("Введите номер слота для сохранения (1-10):");
                    int saveSlot = ui.getUserInput();
                    try {
                        saveLoadService.saveGame(gameService.getGameState(), saveSlot);
                        ui.showOutput("Игра сохранена");
                    } catch (IOException e) {
                        ui.showOutput("Не удалось сохранить игру.");
                    }
                    break;
                case 8:
                    if (gameService.hasArtifactInCurrentRoom()) {
                        handleDoor(ui, gameService, random);
                    } else {
                        ui.showOutput("В этой комнате нет двери.\n");
                    }
                    break;
                case 9:
                    initialScreen(ui, saveLoadService);
                    return;
            }
        }
        if (player.hasArtifact()) {
            ui.showOutput("Поздравляем, вы победили!");
        } else if(player.getHealth() <= 0){
            ui.showOutput("Вы проиграли. Игра окончена.");
        }
        initialScreen(ui, saveLoadService);
    }
    private  void endGame(GameUI ui, SaveLoadService saveLoadService, GameService gameService) throws IOException{
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
    private boolean battleMenu(GameUI ui, GameService gameService, String monsterName, String type, String prevMessage) throws IOException {
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
    private void detailedRoomLoop(GameUI ui, GameService gameService) {
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
    private  void handleDoor(GameUI ui, GameService gameService, Random random) {
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
    private  void inventoryMenu(GameUI ui, GameService gameService, String prevMessage) throws IOException {
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
                Item selectedItem = player.getInventory().get(choice - 1);
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
}