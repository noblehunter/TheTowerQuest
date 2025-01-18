package edu.penzgtu.services;

import edu.penzgtu.entities.Item;
import edu.penzgtu.entities.Monster;
import edu.penzgtu.entities.Room;
import edu.penzgtu.entities.GameState;
import edu.penzgtu.entities.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.util.List;
import java.util.Arrays;

public class GameService {
    private Map<Integer, Room> rooms;
    private GameState gameState;
    private Map<Integer, List<Integer>> roomConnections;
    private final int GHOST_DAMAGE = 13;


    public GameService() {
        rooms = new HashMap<>();
        roomConnections = new HashMap<>();
    }

    public void setGameState(GameState gameState){
        this.gameState = gameState;
    }

    public void createRooms() {
        // Создаем начальную комнату - "Поляна"
        Room field = new Room(0, "Вы стоите на поляне у подножия башни мага. Перед вами высокая башня.");
        field.addItem(new Item("Лечебные травы", "Лечебные травы восстанавливают немного здоровья", "consumable", "health 15"));

        // Создаем новую комнату перед дверью
        Room beforeDoor = new Room(22, "входу в башню.");

        // Создаем комнаты башни
        Room entrance = new Room(1, "Вы вошли в прихожую башни. Зловещий холод обдает вас. Вы видите несколько проходов.");
        Room storage = new Room(2, "Вы в складском помещении. Здесь много пыльных ящиков и старого барахла.");
        storage.addItem(new Item("Зелье здоровья", "Зелье которое может восстановить здоровье", "consumable", "health 50"));
        Room kitchen = new Room(3, "Вы на кухне, кажется, кто-то недавно здесь готовил.");
        kitchen.addItem(new Item("Острый нож", "Острый нож", "weapon", "damage 30"));
        kitchen.addItem(new Item("Кочерга", "Кочерга", "weapon", "damage 50"));
        Room stairs = new Room(6, "Вы у подножия лестницы, ведущей выше и ниже.");
        Room lichRoom = new Room(4, "Вы в комнате лича. Вы чувствуете присутствие темной магии");
        lichRoom.addMonster(new Monster("Лич", 100, 30, true));
        Room basement = new Room(5, "Вы спустились в подвал, здесь пахнет сыростью и плесенью. Есть закрытая магическая дверь ведущая в катакомбы, кажется в ней чего-то не хватает");
        basement.addMonster(new Monster("призрак", 50, 15, false));
        basement.addItem(new Item("Железный лом", "Стальной лом", "weapon", "damage 40"));
        basement.addItem(new Item("Щит", "Старый щит", "armor", "armor 30"));
        Room bedroom = new Room(7, "Вы в спальне. Здесь тихо и спокойно.");
        bedroom.addItem(new Item("Семь золотых", "Семь золотых монет", "money", ""));
        bedroom.addItem(new Item("Защитный костюм", "Этот костюм защищает от воздействия темной магии", "armor", "armor 40"));
        Room catacombs = new Room(8, "Вы в катакомбах. Здесь темно и воняет смертью.");
        Room pedestal = new Room(9, "Вы в комнате с постаментом. Посреди комнаты стоит постамент из темного камня.");
        pedestal.addItem(new Item("Артефакт Хаоса", "Артефакт Хаоса источает темную энергию.", "artifact", ""));
        pedestal.setHasArtifact(true);
        Room bath = new Room(10, "Вы в ванной. В воздухе витает запах мыла.");
        bath.addItem(new Item("Бинты", "Бинты могут восстановить немного здоровья", "consumable", "health 30"));
        Room library = new Room(11, "Вы в библиотеке. Здесь много старых книг.");
        library.addItem(new Item("Приметная книга", "В ней написано, что с призраками можно сражаться имея чугунное оружие и что такое вполне может находится в этой башне.", "lore", ""));
        // Добавляем новые заметки в библиотеку
        library.addItem(new Item("Интересные заметки", "Артефакт Хаоса опасен, его не стоит переносить и хранить без серьезной магической защиты, даже с магической защитой через некоторое время эманации Артефакта Хаоса могут повилять на живых существ, превращая их в монстров...", "lore", ""));

        // Добавляем все комнаты в map
        createRoom(field);
        createRoom(beforeDoor);
        createRoom(entrance);
        createRoom(storage);
        createRoom(kitchen);
        createRoom(stairs);
        createRoom(lichRoom);
        createRoom(basement);
        createRoom(bedroom);
        createRoom(catacombs);
        createRoom(pedestal);
        createRoom(bath);
        createRoom(library);

        // Define room connections
        roomConnections.put(0, Arrays.asList(22));   // Поляна
        roomConnections.put(22, Arrays.asList(0, 1)); // Перед дверью
        roomConnections.put(1, Arrays.asList(2, 3, 6, 22)); // Прихожая
        roomConnections.put(2, Arrays.asList(1));   // Склад
        roomConnections.put(3, Arrays.asList(1));   // Кухня
        roomConnections.put(6, Arrays.asList(1, 4, 5));   // Лестница
        roomConnections.put(4, Arrays.asList(6, 7));   // Комната Лича
        roomConnections.put(5, Arrays.asList(6, 8));   // Подвал
        roomConnections.put(7, Arrays.asList(4, 10, 11));  // Спальня
        roomConnections.put(8, Arrays.asList(5, 9)); // Катакомбы
        roomConnections.put(9, Arrays.asList(8)); // Постамент
        roomConnections.put(10, Arrays.asList(7));  // Ванная
        roomConnections.put(11, Arrays.asList(7));  // Библиотека
    }

    public void createRoom(Room room) {
        rooms.put(room.getId(), room);
    }
    public void removeArtifact(int roomId) {
        rooms.get(roomId).setHasArtifact(false);
    }

    public void startNewGame(Player player) {
        gameState = new GameState(0, player);
        Item sword = new Item("Меч", "Обычный меч", "weapon", "damage 20");
        Item leatherArmor = new Item("Кожаный доспех", "Обычный кожаный доспех", "armor", "armor 15");

        player.addItem(sword);
        player.addItem(leatherArmor);
        // Экипируем молча
        equipItemSilent("Меч");
        equipItemSilent("Кожаный доспех");
    }

    private void interactWithPedestal() {
        Player player = gameState.getPlayer();
        Room pedestal = rooms.get(9);

        if (pedestal.hasArtifact()) {
            if (player.getEquippedArmor() != null && player.getEquippedArmor().getName().equals("Защитный костюм")) {
                System.out.println("Перед вами темный, пульсирующий постамент с Артефактом Хаоса.");
                System.out.println("1. Извлечь Артефакт Хаоса.");
                System.out.println("2. Оставить артефакт на месте и пойти домой.");
                System.out.println("3. Уничтожить артефакт");

                Scanner scanner = new Scanner(System.in);
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        System.out.println("Эманации артефакта хаоса проникают через защитный костюм, превращая вас в лича. Конец игры.");
                        gameState.getPlayer().setHasArtifact(true);
                        break;
                    case 2:
                        System.out.println("Артефакт Хаоса оставленный в башне постепенно наберет достаточно энергии чтобы внутри себя восстановить Лича. Правда с ним похоже будет разбираться другой герой.");
                        gameState.getPlayer().setHasArtifact(true);
                        pedestal.removeArtifact();
                        break;
                    case 3:
                        System.out.println("Вы победили! Артефакт Хаоса разрушен и теперь жителям близлежащих деревень ничего не угрожает.");
                        gameState.getPlayer().setHasArtifact(true);
                        pedestal.removeArtifact();
                        break;
                    default:
                        System.out.println("Неверный выбор.");
                }
            } else {
                System.out.println("Вы попытались извлечь артефакт без защитного костюма! Энергия Хаоса наполняет вас, вы превращаетесь в лича.");
                gameState.getPlayer().setHasArtifact(true);
            }
        } else {
            System.out.println("Постамент пуст.");
        }
    }
    public void movePlayerToRoom(int roomId) {
        if (!gameState.isDoorBroken() && gameState.getCurrentRoomId() != 0 && roomId != 0) {
            System.out.println("Вы не можете покинуть башню, пока дверь не взломана или выбита.");
            return;
        }
        if (gameState.getCurrentRoomId() == 22 && !gameState.isDoorBroken() && roomId != 0) {
            System.out.println("Вы не можете перейти в другую комнату, пока дверь не взломана или выбита.");
            return;
        }
        if (roomConnections.containsKey(gameState.getCurrentRoomId())) {
            if (roomConnections.get(gameState.getCurrentRoomId()).contains(roomId)) {
                if (gameState.getCurrentRoomId() == 9 && roomId != 8) {
                    System.out.println("Из постамента можно перейти только в катакомбы.");
                    return;
                }
                if (gameState.getCurrentRoomId() == 8 && roomId == 8) {
                    System.out.println("Вы уже находитесь в катакомбах.");
                    return;
                }
                if(roomId == 5 && hasMonsterInRoom(5)) {
                    Player player = gameState.getPlayer();
                    if(player.getEquippedWeapon() != null && player.getEquippedWeapon().getName().equals("Кочерга")) {
                        System.out.println("Вы отразили удар призрака и изгнали его!");
                        rooms.get(5).removeMonster("призрак");
                        gameState.setCurrentRoomId(roomId);
                        System.out.println("Вы перешли " + rooms.get(roomId).getDescription());
                        return;
                    } else {
                        System.out.println("Призрак в подвале атакует вас нанося вам " + GHOST_DAMAGE + " урона. Вы отброшены назад к лестнице.");
                        gameState.setCurrentRoomId(6);
                        int playerHealth = player.getHealth();
                        player.setHealth(playerHealth - GHOST_DAMAGE);
                        return;
                    }
                }
                else {
                    if(gameState.getPlayer().hasArtifact() && gameState.getCurrentRoomId() != 9) {
                        System.out.println("Вы не уничтожили артефакт и его эманации начинают просачиваться сквозь перчатки, вы проходя через мучительную агонию и попутно сойдя с ума вы трансформируетесь в лича");
                        gameState.getPlayer().getInventory().stream().filter(item -> item.getName().equals("Артефакт Хаоса")).findFirst().ifPresent(item ->{
                            gameState.getPlayer().removeItem(item);
                        });
                        gameState.getPlayer().setHasArtifact(true);
                        return;
                    }
                    if (!gameState.isLichKilled() && roomId == 7) {
                        System.out.println("Вы не можете пройти в спальню пока жив лич!");
                        return;
                    }
                    if (roomId == 9 && rooms.get(9).hasArtifact()) {
                        interactWithPedestal();
                        return; // Предотвращаем обычный переход после взаимодействия с постаментом
                    }
                    gameState.setCurrentRoomId(roomId);
                    System.out.println("Вы перешли " + rooms.get(roomId).getDescription());
                }
            } else {
                System.out.println("Недопустимый переход.");
            }
        } else {
            System.out.println("Комната не найдена.");
        }
    }

    public void pickUpItem(String itemName) {
        Room currentRoom = rooms.get(gameState.getCurrentRoomId());
        if (currentRoom == null) {
            System.out.println("Ошибка: Текущая комната не найдена.");
            return;
        }
        Optional<Item> itemToPickup = currentRoom.getItems().stream().filter(item -> item.getName().equals(itemName)).findFirst();
        if (itemToPickup.isPresent()) {
            gameState.getPlayer().addItem(itemToPickup.get());
            currentRoom.getItems().remove(itemToPickup.get());
            System.out.println("Подобран " + itemName);
        }
        else {
            System.out.println("Предмет не найден в этой комнате.");
        }
    }

    public boolean battle(String monsterName) {
        Room currentRoom = rooms.get(gameState.getCurrentRoomId());
        if (currentRoom == null) {
            System.out.println("Ошибка: Текущая комната не найдена.");
            return false;
        }
        Optional<Monster> monsterOptional = currentRoom.getMonsters().values().stream().filter(monster -> monster.getName().equals(monsterName)).findFirst();
        if(monsterOptional.isPresent()) {
            Monster monster = monsterOptional.get();
            Player player = gameState.getPlayer();
            int playerHealth = player.getHealth();
            int monsterHealth = monster.getHealth();
            Random random = new Random();
            while(playerHealth > 0 && monsterHealth > 0) {
                // Игрок атакует
                int playerDamage = 0;
                if(player.getEquippedWeapon() != null && player.getEquippedWeapon().getName().equals("Кочерга") && monster.getName().equals("призрак")) {
                    playerDamage = 50;
                    System.out.println("Вы атакуете кочергой, нанося " + playerDamage + " урона. ");
                } else if(player.getEquippedWeapon() != null && !player.getEquippedWeapon().getName().equals("Кочерга") && monster.getName().equals("призрак")){
                    playerDamage = 0;
                    System.out.println("Экипированное оружие бессильно против призрака. Вы не наносите урона");
                } else {
                    playerDamage = 20;
                    System.out.println("Вы атакуете, нанося " + playerDamage + " урона. ");
                }
                monsterHealth = monsterHealth - playerDamage;
                if (monsterHealth <= 0) {
                    System.out.println("Монстр " + monster.getName() + " повержен.");
                    currentRoom.removeMonster(monster.getName());
                    switch (monsterName) {
                        case "Лич":
                            gameState.setLichKilled(true);
                            gameState.setHasMagicKey(true);
                            System.out.println("Вы получили магический ключ!");
                            break;
                        case "призрак":
                            gameState.setGhostKilled(true);
                            break;
                    }
                    return true;
                }
                System.out.println("Монстр " + monster.getName() + " здоровье: " + monsterHealth);

                // Монстр атакует в ответ
                int monsterDamage = monster.getDamage();
                if (monsterDamage > 0) {
                    int damageTaken = monsterDamage; // Изначально урон равен урону монстра
                    if (monsterName.equals("Лич")) {
                        // Учитываем броню от "Кожаный доспех"
                        if (player.getEquippedArmor() != null && player.getEquippedArmor().getName().equals("Кожаный доспех")) {
                            damageTaken -= 15;  // Вычитаем броню
                            if (damageTaken < 0) {
                                damageTaken = 0; // Урон не может быть отрицательным
                            }
                        }
                        // Учитываем броню от "Щита"
                        if (player.getEquippedShield() != null) {
                            damageTaken -= 25;
                            if (damageTaken < 0) {
                                damageTaken = 0;
                            }
                        }

                    }
                    playerHealth = playerHealth - damageTaken;
                    player.setHealth(playerHealth);
                    System.out.println("Монстр " + monster.getName() + " атакует в ответ нанося вам " + monsterDamage + " урона, вы получаете " + damageTaken + " урона");
                    System.out.println("Ваше текущее здоровье равно: " + playerHealth);
                    if (monsterName.equals("Лич") && playerHealth < 50) {
                        Optional<Item> healthPotion = player.getItem("Зелье здоровья");
                        if (healthPotion.isPresent()) {
                            useItem("Зелье здоровья");
                            System.out.println("Вы автоматически использовали зелье здоровья.");
                            playerHealth = player.getHealth(); // Обновляем здоровье после использования зелья
                        }
                    }
                }
                if (playerHealth <= 0) {
                    System.out.println("Вы погибли!");
                    return false;
                }
            }
        } else {
            System.out.println("Монстр не найден!");
        }
        return true;
    }

    public boolean hasMonsterInRoom(int roomId) {
        return rooms.get(roomId).hasMonster("призрак");
    }
    public boolean hasMonsterInCurrentRoom() {
        Room currentRoom = rooms.get(gameState.getCurrentRoomId());
        return currentRoom != null && !currentRoom.getMonsters().isEmpty();
    }

    public void showCurrentRoomInfo(){
        Room currentRoom = rooms.get(gameState.getCurrentRoomId());
        if (currentRoom == null) {
            System.out.println("Ошибка: Текущая комната не найдена.");
            return;
        }
        switch (currentRoom.getId()) {
            case 0:
                System.out.println("Вы стоите на **поляне**, освещенной тусклым светом заходящего солнца. Высокая башня мага возвышается перед вами, ее вершина теряется в облаках. Вокруг трава колышется на ветру, а в воздухе чувствуется запах диких цветов и сырой земли.");
                break;
            case 22:
                if (!gameState.isDoorBroken()) {
                    System.out.println("Вы стоите перед **высокой башней**, её деревянная дверь выглядит очень старой, но на удивление прочной. С каждым порывом ветра чувствуете исходящую от неё магическую ауру.");
                } else {
                    System.out.println("Вы стоите перед **высокой башней**, её дверь выбита с такой силой, что древесина оставила вдавленный след на стене башни, за проёмом вас ждёт полумрак.");
                }
                break;
            case 1:
                System.out.println("Вы вошли в **прихожую башни**. Зловещий холод скользит по вашим костям, стены кажутся высокими и темными. Вы видите несколько проходов, каждый из которых скрыт в тени. Пыль покрывает старую мебель, а в воздухе витает запах гнили.");
                break;
            case 2:
                System.out.println("Вы находитесь в **складском помещении**. Здесь царит хаос: пыльные ящики, разбитые горшки и старое барахло разбросаны по всей комнате. Но все же возможно здесь есть что-нибудь полезное...");
                break;
            case 3:
                System.out.println("Вы на **кухне**. Кажется, здесь кто-то готовил довольно давно, на столе разбросаны различные кухонные принадлежности покрытые пылью. Помимо них вы также видите печку, покрытую копотью, рядом с которой лежит чугунная кочерга, а на полу разбросаны обгоревшие угли.");
                break;
            case 6:
                System.out.println("Вы у подножия **лестницы**, ведущей выше и ниже. Ступени выглядят потрепанными временем, а в воздухе витает запах сырости. Вы слышите отголоски шагов, кажущиеся эхом из далеких комнат.");
                break;
            case 4:
                if (gameState.isLichKilled()) {
                    System.out.println("Вы в кабинете мага. Стены украшены какими-то зловещими символами, а воздух наполнен магическими чарами. Монстр повержен, вы можете безбоязненно пройти");
                } else {
                    System.out.println("Вы в кабинете мага. Стены украшены какими-то зловещими символами, а воздух наполнен магическими чарами. Посреди комнаты как буддто в спячке парит Лич прямо за ним располагается еще какая-то дверь");
                }
                break;
            case 5:
                System.out.println("Вы спустились в **подвал**, здесь пахнет сыростью и плесенью. Стены покрыты зеленоватым налетом, а со сводов капает вода. Вы видите закрытую магическую дверь, ведущую в катакомбы, кажется, в ней чего-то не хватает.");
                break;
            case 7:
                System.out.println("Вы в **спальне**. Здесь на удивление тихо и спокойно. Вы видите старую кровать, покрытую пыльным балдахином, и небольшой столик с зеркалом, отражающим только тьму. Комната наполнена покоем, но вы чувствуете что-то тревожное в ней.");
                break;
            case 8:
                System.out.println("Вы в **катакомбах**. Здесь темно и воняет смертью. Каменные стены пропитаны сыростью, а в воздухе ощущается присутствие темных сил.");
                break;
            case 9:
                System.out.println("Вы в **комнате с постаментом**. Посреди комнаты стоит постамент из темного камня.");
                break;
            case 10:
                System.out.println("Вы в **ванной**. В воздухе витает запах мыла, а стены украшены старой керамической плиткой. Ванна наполнена мутной водой, а рядом стоит небольшой шкафчик с приоткрытой дверцой. Возможно, тут можно найти что нибудь полезное... ");
                break;
            case 11:
                System.out.println("Вы в **библиотеке**. Здесь множество старых книг, а воздух пропитан ароматом старой бумаги. Книжные полки доходят до потолка, а в центре комнаты стоит стол с несколькими раскрытыми фолиантами.");
                break;
            default:
                System.out.println(currentRoom.getDescription());
        }
        if (!currentRoom.getItems().isEmpty()) {
            System.out.println("В комнате находятся следующие предметы:");
            for (Item item : currentRoom.getItems()) {
                System.out.println("- " + item.getName() + ": " + item.getDescription());
            }
        }
        if (!currentRoom.getMonsters().isEmpty()) {
            System.out.println("В комнате находятся следующие монстры:");
            for (Monster monster : currentRoom.getMonsters().values()) {
                System.out.println("- " + monster.getName() + " в кол-ве: 1");
            }
        }
    }
    public GameState getGameState(){
        return gameState;
    }
    public void setDoorBroken(boolean doorBroken) {
        this.gameState.setDoorBroken(doorBroken);
    }
    public Player getPlayer() {
        return gameState.getPlayer();
    }
    public void showAvailableRooms() {
        int currentRoomId = gameState.getCurrentRoomId();
        if (roomConnections.containsKey(currentRoomId)) {
            System.out.println("Доступные переходы: ");
            for (int connection : roomConnections.get(currentRoomId)) {
                String roomName = "";
                switch(connection) {
                    case 0: roomName = "Поляна"; break;
                    case 1: roomName = "Прихожая"; break;
                    case 2: roomName = "Склад"; break;
                    case 3: roomName = "Кухня"; break;
                    case 4: roomName = "Комната Лича"; break;
                    case 5: roomName = "Подвал"; break;
                    case 6: roomName = "Лестница"; break;
                    case 7: roomName = "Спальня"; break;
                    case 8: roomName = "Катакомбы"; break;
                    case 9: roomName = "Постамент"; break;
                    case 10: roomName = "Ванна"; break;
                    case 11: roomName = "Библиотека"; break;
                    case 22: roomName = "Перед башней"; break;
                }
                System.out.println(connection + " " + roomName);
            }
        } else {
            System.out.println("Вы не можете перейти в другую комнату.");
        }
    }
    public void showInventory() {
        Player player = gameState.getPlayer();
        if (player.getInventory().isEmpty()) {
            System.out.println("Инвентарь пуст.");
            return;
        }
        System.out.println("1. Использовать предмет");
        System.out.println("2. Экипировать предмет");
        System.out.println("Инвентарь:");
        for (int i = 0; i < player.getInventory().size(); i++) {
            Item item = player.getInventory().get(i);
            System.out.println((i + 1) + ". " + item.getName() + " - " + item.getDescription() +
                    (player.getEquippedWeapon() != null && player.getEquippedWeapon().equals(item) ? " (Экипировано как оружие)" : "") +
                    (player.getEquippedArmor() != null && player.getEquippedArmor().equals(item) ? " (Экипировано как броня)" : "")+
                    (player.getEquippedShield() != null && player.getEquippedShield().equals(item) ? " (Экипировано как щит)" : ""));
        }
    }
    public void equipItem(String itemName) {
        Player player = gameState.getPlayer();
        Optional<Item> itemToEquipOptional = player.getItem(itemName);
        if(itemToEquipOptional.isPresent()) {
            Item itemToEquip = itemToEquipOptional.get();
            if (itemToEquip.getType().equals("weapon")) {
                player.setEquippedWeapon(itemToEquip);
                System.out.println("Вы экипировали " + itemName + " как оружие.");
            } else if (itemToEquip.getType().equals("armor")) {
                player.setEquippedArmor(itemToEquip);
                System.out.println("Вы экипировали " + itemName + " как броню.");
            }
            else if (itemToEquip.getName().equals("Щит")){
                player.setEquippedShield(itemToEquip);
                System.out.println("Вы экипировали " + itemName + " как щит.");
            }
            else {
                System.out.println("Этот предмет не может быть экипирован.");
            }
        } else {
            System.out.println("Предмет не найден!");
        }
    }
    public void useItem(String itemName) {
        Player player = gameState.getPlayer();
        Optional<Item> itemToUseOptional = player.getItem(itemName);
        if(itemToUseOptional.isPresent()) {
            Item itemToUse = itemToUseOptional.get();
            if(itemToUse.getType().equals("consumable")) {
                String effect = itemToUse.getEffect();
                String[] parts = effect.split(" ");
                if(parts[0].equals("health")) {
                    int healthAmount = Integer.parseInt(parts[1]);
                    int currentHealth = player.getHealth();
                    if (currentHealth + healthAmount > 100) {
                        player.setHealth(100);
                        player.removeItem(itemToUse);
                        System.out.println("Вы использовали " + itemName + ". Ваше здоровье восстановлено до максимума. Текущее здоровье: " + player.getHealth());
                    } else {
                        player.setHealth(currentHealth + healthAmount);
                        player.removeItem(itemToUse);
                        System.out.println("Вы использовали " + itemName + ". Ваше здоровье восстановлено на " + healthAmount + ". Текущее здоровье: " + player.getHealth());
                    }
                } else {
                    System.out.println("Вы не можете использовать этот предмет");
                }
            }  else if (itemToUse.getName().equals("Артефакт Хаоса")) {
                Item equippedArmor = player.getEquippedArmor();
                if (equippedArmor != null && equippedArmor.getName().equals("Защитный костюм")) {
                    System.out.println("Вы извлекли артефакт, что вы хотите с ним сделать?");
                    System.out.println("1. Уничтожить артефакт.");
                    System.out.println("2. Забрать Артефакт Хаоса с собой");
                    Scanner scanner = new Scanner(System.in);
                    int choice = scanner.nextInt();
                    scanner.nextLine();
                    if (choice == 1) {
                        System.out.println("Вы уничтожили артефакт и победили!");
                        gameState.getPlayer().setHasArtifact(true);
                    }
                    else if (choice == 2){
                        System.out.println("Вы решили забрать артефакт, помните о его силе и о том что вам с ним не выжить.");
                        gameState.getPlayer().setHasArtifact(true);
                    }
                    else {
                        System.out.println("Вы не уничтожили артефакт и он начинает просачиваться сквозь перчатки!");
                        gameState.getPlayer().setHasArtifact(true);
                    }
                } else {
                    System.out.println("Вы попытались извлечь артефакт без защитного костюма! Энергия Хаоса наполняет вас, вы превращаетесь в лича.");
                    gameState.getPlayer().setHasArtifact(true);
                }
            } else if (itemToUse.getName().equals("Приметная книга")) {
                System.out.println("В ней написано, что с призраками можно сражаться имея чугунное оружие и что такое вполне может находится в этой башне...");
                System.out.println("В ней так же написано, что с Артефакт Хаоса слишком опасен, его не стоит переносить без серьезной магической защиты, даже с магической защитой через некоторое время эманации Артефакта хаоса могут повилять на живых существ, превращая их в монстров...");
            }
            else {
                System.out.println("Вы не можете использовать этот предмет");
            }
        }  else {
            System.out.println("Предмет не найден!");
        }
    }
    private void equipItemSilent(String itemName) {
        Player player = gameState.getPlayer();
        Optional<Item> itemToEquipOptional = player.getItem(itemName);
        if (itemToEquipOptional.isPresent()) {
            Item itemToEquip = itemToEquipOptional.get();
            if (itemToEquip.getType().equals("weapon")) {
                player.setEquippedWeapon(itemToEquip);
            } else if (itemToEquip.getType().equals("armor")) {
                player.setEquippedArmor(itemToEquip);
            } else if (itemToEquip.getName().equals("Щит")) {
                player.setEquippedShield(itemToEquip);
            }
            else {
                System.out.println("Этот предмет не может быть экипирован.");
            }
        } else {
            System.out.println("Предмет не найден!");
        }
    }
}