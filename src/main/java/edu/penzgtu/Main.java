package edu.penzgtu;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import edu.penzgtu.services.GameService;
import edu.penzgtu.services.SaveLoadService;
import edu.penzgtu.entities.*;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        GameService gameService = new GameService();
        SaveLoadService saveLoadService = new SaveLoadService();
        gameService.createRooms();

        while (true) { // Бесконечный цикл главного меню
            System.out.println("\n--- Главное меню ---");
            System.out.println("1. Новая игра");
            System.out.println("2. Загрузить игру");
            System.out.println("3. Выход");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    Player player = createPlayer(scanner);
                    gameService.startNewGame(player);
                    gameLoop(scanner, gameService, saveLoadService);
                    break;
                case 2:
                    System.out.println("Выберите слот для загрузки (1-10):");
                    int loadSlot = scanner.nextInt();
                    scanner.nextLine();
                    try {
                        GameState loadedGameState = saveLoadService.loadGame(loadSlot);
                        if (loadedGameState != null) {
                            gameService.setGameState(loadedGameState);
                            System.out.println("Игра загружена из слота " + loadSlot);
                            gameLoop(scanner, gameService, saveLoadService);
                            break; // Выходим из меню загрузки, не из главного
                        } else {
                            System.out.println("Не удалось загрузить игру.");
                        }
                    } catch (IOException e) {
                        System.out.println("Ошибка загрузки: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Выход из игры.");
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }


    private static Player createPlayer(Scanner scanner) {
        System.out.println("\n--- Создание персонажа ---");
        System.out.print("Введите имя вашего персонажа: ");
        String playerName = scanner.nextLine();
        return new Player(playerName, 100);
    }

    private static void gameLoop(Scanner scanner, GameService gameService, SaveLoadService saveLoadService) throws IOException {
        Random random = new Random();
        while (true) { // Бесконечный цикл игры
            GameState gameState = gameService.getGameState();
            Player player = gameState.getPlayer();

            if (gameState.getCurrentRoomId() == 0) {
                System.out.println("Вы стоите на поляне. Вы можете пойти к башне или вернуться домой.");
            }
            if (gameState.getCurrentRoomId() == 22) {
                if (!gameState.isDoorBroken()) {
                    System.out.println("Вы находитесь перед высокой башней, c прочной деревянной дверью.");
                } else {
                    System.out.println("Вы находитесь перед входом в башню,  путь открыт.");
                }
            }

            System.out.println("1. Перейти к");
            System.out.println("2. Подобрать предмет");
            if (gameService.hasMonsterInCurrentRoom() && gameService.getGameState().getCurrentRoomId() != 0 &&  !gameService.getGameState().getPlayer().hasArtifact()  && !gameService.getGameState().getPlayer().getInventory().stream().anyMatch(item -> item.getName().equals("Артефакт Хаоса")) ) {
                System.out.println("3. Сразиться с монстром?");
            }
            System.out.println("4. Что я вижу вокруг?");
            System.out.println("5. Персонаж");
            System.out.println("6. Инвентарь");
            System.out.println("7. Сохранить игру");
            if (gameState.getCurrentRoomId() == 22 && !gameState.isDoorBroken()) {
                System.out.println("8. Попробовать взломать дверь");
            }
            System.out.println("9. Выйти в главное меню");


            System.out.print("Введите свой выбор: ");
            int action = scanner.nextInt();
            scanner.nextLine();
            if (player.hasArtifact()) {
                if(player.hasArtifact() && player.getInventory().stream().anyMatch(item -> item.getName().equals("Артефакт Хаоса"))) {
                    System.out.println("Вы проиграли, так как превратились в лича.");
                } else {
                    System.out.println("Игра окончена!");
                }
                return;
            }
            switch (action) {
                case 1:
                    if (gameState.getCurrentRoomId() == 0) {
                        System.out.println("1. Пойти к башне");
                        System.out.println("2. Вернуться домой");
                        int initialChoice = scanner.nextInt();
                        scanner.nextLine();
                        if (initialChoice == 1) {
                            gameService.movePlayerToRoom(22);
                        } else if (initialChoice == 2) {
                            System.out.println("Вы вернулись домой. Надеемся, что найдется другой герой, который сможет выполнить это.");
                            return;
                        } else {
                            System.out.println("Неверный выбор. Попробуйте снова.");
                        }
                    } else {
                        System.out.println("Перейти к:");
                        gameService.showAvailableRooms();
                        System.out.print("Введите номер комнаты для перехода: ");
                        int nextRoom = scanner.nextInt();
                        scanner.nextLine();
                        if(gameState.getCurrentRoomId() != 22 && nextRoom == 0 && !gameState.isDoorBroken()) {
                            System.out.println("Вы не можете вернуться на поляну, пока дверь не взломана или выбита.");
                            continue;
                        }
                        if(gameState.getCurrentRoomId() == 22 && !gameState.isDoorBroken() && nextRoom != 0) {
                            System.out.println("Вы не можете перейти в эту комнату, пока дверь не взломана или выбита.");
                            continue;
                        }

                        gameService.movePlayerToRoom(nextRoom);
                        continue;
                    }
                    break;
                case 2:
                    System.out.print("Введите название предмета, который вы хотите подобрать: ");
                    String itemName = scanner.nextLine();
                    gameService.pickUpItem(itemName);
                    break;
                case 3:
                    if (gameService.hasMonsterInCurrentRoom() && gameService.getGameState().getCurrentRoomId() != 0 &&  !gameService.getGameState().getPlayer().hasArtifact() && !gameService.getGameState().getPlayer().getInventory().stream().anyMatch(item -> item.getName().equals("Артефакт Хаоса"))) {
                        System.out.print("Введите имя монстра, с которым вы хотите сразиться: ");
                        String monsterName = scanner.nextLine();
                        if (!battleMenu(scanner, gameService, monsterName)) {
                            return; // Если в бою сбежали или проиграли - выходим из цикла
                        }
                    } else {
                        System.out.println("В этой комнате нет монстров!");
                    }
                    break;
                case 4:
                    gameService.showCurrentRoomInfo();
                    break;
                case 5:
                    displayCharacterInfo(gameService);
                    break;
                case 7:
                    System.out.println("Выберите слот для сохранения (1-10):");
                    int saveSlot = scanner.nextInt();
                    scanner.nextLine();
                    saveLoadService.saveGame(gameState, saveSlot);
                    System.out.println("Игра сохранена в слот " + saveSlot);
                    break;
                case 8:
                    if (gameState.getCurrentRoomId() == 22 && !gameState.isDoorBroken()) {
                        int attempts = 3;
                        boolean success = false;
                        for(int i = 0; i < attempts; i++) {
                            if (random.nextInt(100) < 30) {
                                success = true;
                                System.out.println("Вы взломали дверь!");
                                gameService.setDoorBroken(true);
                                gameService.movePlayerToRoom(1);
                                break;
                            } else {
                                System.out.println("Попытка взлома неудачна. Осталось попыток: " + (attempts - i - 1));
                            }
                        }
                        if (!success) {
                            int damage = 5 + random.nextInt(11);
                            System.out.println("Вы выбили дверь, получив " + damage + " урона.");
                            int currentHealth = gameService.getPlayer().getHealth() - damage;
                            gameService.getPlayer().setHealth(currentHealth);
                            System.out.println("Текущее здоровье: " + currentHealth);
                            gameService.setDoorBroken(true);
                            gameService.movePlayerToRoom(1);
                        }
                    } else {
                        System.out.println("Вы не можете взломать дверь здесь.");
                    }
                    break;
                case 6:
                    System.out.println("Инвентарь:");
                    gameService.showInventory();
                    System.out.print("Выберите действие с предметом или введите 0 для выхода: ");
                    int inventoryAction = scanner.nextInt();
                    scanner.nextLine();
                    if(inventoryAction != 0) {
                        if(inventoryAction == 1) {
                            System.out.print("Введите название предмета, который вы хотите использовать: ");
                            String useItemName = scanner.nextLine();
                            gameService.useItem(useItemName);
                        } else if (inventoryAction == 2) {
                            System.out.print("Введите название предмета, который вы хотите экипировать: ");
                            String equipItemName = scanner.nextLine();
                            gameService.equipItem(equipItemName);
                        }
                    }
                    break;
                case 9:
                    break; // Выход из цикла while, возвращаемся в главное меню
                default:
                    System.out.println("Недопустимое действие.");
                    break;
            }
            if(action == 9) {
                break;
            }
        }
    }
    private static boolean battleMenu(Scanner scanner, GameService gameService, String monsterName) {
        Player player = gameService.getGameState().getPlayer();
        while (true) {
            System.out.println("\n--- Бой ---");
            System.out.println("1. Атака");
            if(player.hasShield()) {
                System.out.println("2. Защита");
            }
            System.out.println("3. Инвентарь");
            System.out.println("4. Сбежать");
            System.out.print("Выберите действие: ");
            int battleAction = scanner.nextInt();
            scanner.nextLine();

            switch (battleAction) {
                case 1:
                    if(!gameService.battle(monsterName)) {
                        return false; // Если проиграли выходим.
                    }
                    if (!gameService.hasMonsterInCurrentRoom()) {
                        return true; // Если победили выходим.
                    }
                    break;
                case 2:
                    if(player.hasShield()) {
                        if (gameService.hasMonsterInRoom(gameService.getGameState().getCurrentRoomId())) { // Проверка на призрака
                            System.out.println("Вы отразили атаку щитом!");
                        }
                    } else {
                        System.out.println("У вас нет щита!");
                    }
                    break;
                case 3:
                    System.out.println("Инвентарь:");
                    gameService.showInventory();
                    System.out.print("Выберите действие с предметом или введите 0 для выхода: ");
                    int inventoryAction = scanner.nextInt();
                    scanner.nextLine();
                    if(inventoryAction != 0) {
                        if(inventoryAction == 1) {
                            System.out.print("Введите название предмета, который вы хотите использовать: ");
                            String useItemName = scanner.nextLine();
                            gameService.useItem(useItemName);
                        } else if (inventoryAction == 2) {
                            System.out.print("Введите название предмета, который вы хотите экипировать: ");
                            String equipItemName = scanner.nextLine();
                            gameService.equipItem(equipItemName);
                        }
                    }
                    break;
                case 4:
                    System.out.println("Вы сбежали из боя.");
                    return true;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
                    break;
            }
        }
    }
    private static void displayCharacterInfo(GameService gameService) {
        Player player = gameService.getGameState().getPlayer();
        System.out.println("\nИнформация о персонаже:");
        System.out.println(player.toString());
        System.out.println("Инвентарь:");
        gameService.showInventory();
        System.out.println("\nНажмите Enter для возврата в главное меню.");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
}