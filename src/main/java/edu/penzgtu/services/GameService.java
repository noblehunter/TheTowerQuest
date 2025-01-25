package edu.penzgtu.services;

import edu.penzgtu.entities.*;
import edu.penzgtu.game.*;
import edu.penzgtu.ui.GameUI;

import java.io.IOException;
import java.util.*;


public class GameService {
    private GameState gameState;
    private final GameUI ui;
    private String currentScreen = "";
    private Map<RoomType, List<RoomType>> roomConnections;
    private List<Room> rooms;
    private final int GHOST_DAMAGE = 13;
    private final Map<RoomType, RoomData> roomDataMap;


    public GameService(GameUI ui) {
        this.ui = ui;
        this.gameState = new GameState();
        roomDataMap = createRoomDataMap();
        createRooms();
    }
    private Map<RoomType, RoomData> createRoomDataMap() {
        Map<RoomType, RoomData> roomDataMap = new HashMap<>();

        roomDataMap.put(RoomType.PLAINS, new RoomData(RoomType.PLAINS, "Вы стоите на поляне перед башней.", new Item("Лечебные травы"), null, false, Arrays.asList(RoomType.GO_HOME, RoomType.BEFORE_TOWER)));
        roomDataMap.put(RoomType.ENTRANCE, new RoomData(RoomType.ENTRANCE, "Вы в прихожей.", new Item("Книга заклинаний"), null, false,  Arrays.asList(RoomType.PLAINS, RoomType.STORAGE, RoomType.KITCHEN, RoomType.STAIRS)));
        roomDataMap.put(RoomType.STORAGE, new RoomData(RoomType.STORAGE, "Вы в складе.", new Item("Щит"), null, false, Arrays.asList(RoomType.ENTRANCE, RoomType.LICH_ROOM)));
        roomDataMap.put(RoomType.KITCHEN, new RoomData(RoomType.KITCHEN, "Вы на кухне.", new Item("Эликсир здоровья"), null, false,  Arrays.asList(RoomType.ENTRANCE, RoomType.BASEMENT)));
        roomDataMap.put(RoomType.LICH_ROOM, new RoomData(RoomType.LICH_ROOM, "Вы в Комнате Лича.", new Item("Старый меч"), new Monster("Лич", 200), false,  Arrays.asList(RoomType.STORAGE, RoomType.STAIRS)));
        roomDataMap.put(RoomType.BASEMENT, new RoomData(RoomType.BASEMENT, "Вы в подвале.", new Item("Золотые монеты"), new Monster("Призрак", 100), false,  Arrays.asList(RoomType.KITCHEN, RoomType.BEDROOM)));
        roomDataMap.put(RoomType.STAIRS, new RoomData(RoomType.STAIRS, "Вы на лестнице.", new Item("Молот"), new Monster("Паук", 50), false,  Arrays.asList(RoomType.LICH_ROOM, RoomType.CATACOMBS)));
        roomDataMap.put(RoomType.BEDROOM, new RoomData(RoomType.BEDROOM, "Вы в спальне.", new Item("Вяленое мясо"), null, false,  Arrays.asList(RoomType.BASEMENT, RoomType.BATHROOM)));
        roomDataMap.put(RoomType.CATACOMBS, new RoomData(RoomType.CATACOMBS, "Вы в катакомбах.", new Item("Целебные ягоды"), null, false,  Arrays.asList(RoomType.STAIRS, RoomType.LIBRARY)));
        roomDataMap.put(RoomType.PEDESTAL, new RoomData(RoomType.PEDESTAL, "Вы на постаменте.", new Item("Кольцо защиты"), null, true,  Arrays.asList(RoomType.CATACOMBS)));
        roomDataMap.put(RoomType.BATHROOM, new RoomData(RoomType.BATHROOM, "Вы в ванной.", new Item("Магический посох"), null, false, Arrays.asList(RoomType.BEDROOM, RoomType.LIBRARY)));
        roomDataMap.put(RoomType.LIBRARY, new RoomData(RoomType.LIBRARY, "Вы в библиотеке.", new Item("Факел"), null, false,  Arrays.asList(RoomType.CATACOMBS, RoomType.BATHROOM)));
        roomDataMap.put(RoomType.BEFORE_TOWER, new RoomData(RoomType.BEFORE_TOWER, "Вы перед запертой дверью.", new Item("Отмычки"), null, false,  Arrays.asList(RoomType.PLAINS, RoomType.ENTRANCE)));

        roomDataMap.put(RoomType.GO_HOME, new RoomData(RoomType.GO_HOME, "Вы пошли домой. Конец игры.", null, null, false,  new ArrayList<>()));
        roomDataMap.put(RoomType.END_GAME_1, new RoomData(RoomType.END_GAME_1, "Концовка игры 1", null, null, false,  new ArrayList<>()));
        roomDataMap.put(RoomType.END_GAME_2, new RoomData(RoomType.END_GAME_2, "Концовка игры 2", null, null, false,  new ArrayList<>()));
        roomDataMap.put(RoomType.END_GAME_3, new RoomData(RoomType.END_GAME_3, "Концовка игры 3", null, null, false,  new ArrayList<>()));
        roomDataMap.put(RoomType.END_GAME_4, new RoomData(RoomType.END_GAME_4, "Концовка игры 4", null, null, false, new ArrayList<>()));

        return roomDataMap;
    }
    public void startNewGame(Player player) {
        gameState = new GameState();
        gameState.setPlayer(player);
        createRooms();
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public String getGameScreen() {
        return currentScreen;
    }

    public String showBriefRoomDescription() {
        for (Room room : rooms) {
            if (room.getId() == gameState.getCurrentRoomId()) {
                String description = room.getDescription();
                int firstNewline = description.indexOf('\n');
                if (firstNewline != -1) {
                    currentScreen = description.substring(0, firstNewline);
                    return currentScreen;
                } else {
                    currentScreen = description;
                    return currentScreen;
                }
            }
        }
        return "";
    }


    public void setGameScreen(String message) {
        currentScreen = message;
    }


    public void createRooms() {
        rooms = new ArrayList<>();
        roomConnections = new HashMap<>();
        for (Map.Entry<RoomType, RoomData> entry : roomDataMap.entrySet()) {
            RoomData roomData = entry.getValue();
            Room room = new Room(roomData.getType().ordinal(), roomData.getDescription(), roomData.getItem(), roomData.getMonster());
            room.setHasArtifact(roomData.hasArtifact());
            rooms.add(room);
            roomConnections.put(roomData.getType(), roomData.getConnections());

        }
        gameState.setRooms(rooms);
        gameState.setRoomConnections(roomConnections);
    }
    public void removeMonster(String monsterName) {
        for (Room room : rooms) {
            if (room.getMonster() != null && room.getMonster().getName().equals(monsterName)) {
                room.removeMonster();
                break;
            }
        }
    }
    public void removeArtifact(int roomId) {
        rooms.get(roomId).setHasArtifact(false);
    }
    public boolean hasMonsterInCurrentRoom() {
        for (Room room : rooms) {
            if (room.getId() == gameState.getCurrentRoomId() && room.getMonster() != null) {
                return true;
            }
        }
        return false;
    }

    public boolean hasMonsterInRoom(int roomId) {
        for (Room room : rooms) {
            if (room.getId() == roomId && room.getMonster() != null) {
                return true;
            }
        }
        return false;
    }

    public void movePlayerToRoom(int roomId) {
        RoomType roomType = RoomType.values()[roomId];
        if (!gameState.isDoorBroken() && gameState.getCurrentRoomId() != 0 &&  roomId != RoomType.GO_HOME.ordinal() &&  roomId != RoomType.END_GAME_1.ordinal() &&  roomId != RoomType.END_GAME_2.ordinal() &&  roomId != RoomType.END_GAME_3.ordinal() &&  roomId != RoomType.END_GAME_4.ordinal()  ) {
            ui.showOutput("Вы не можете покинуть башню, пока дверь не взломана или выбита.");
            return;
        }
        if (gameState.getCurrentRoomId() == RoomType.BEFORE_TOWER.ordinal() && !gameState.isDoorBroken() && roomId != 0 &&  roomId != RoomType.GO_HOME.ordinal() &&  roomId != RoomType.END_GAME_1.ordinal() &&  roomId != RoomType.END_GAME_2.ordinal() &&  roomId != RoomType.END_GAME_3.ordinal() &&  roomId != RoomType.END_GAME_4.ordinal()) {
            ui.showOutput("Вы не можете перейти в другую комнату, пока дверь не взломана или выбита.");
            return;
        }
        if (roomConnections.containsKey(RoomType.values()[gameState.getCurrentRoomId()])) {
            if (roomConnections.get(RoomType.values()[gameState.getCurrentRoomId()]).contains(roomType)) {
                if (gameState.getCurrentRoomId() == RoomType.PEDESTAL.ordinal() && roomId != RoomType.CATACOMBS.ordinal()) {
                    ui.showOutput("Из постамента можно перейти только в катакомбы.");
                    return;
                }
                if (gameState.getCurrentRoomId() == RoomType.CATACOMBS.ordinal() && roomId == RoomType.CATACOMBS.ordinal()) {
                    ui.showOutput("Вы уже находитесь в катакомбах.");
                    return;
                }
                if(roomId == RoomType.BASEMENT.ordinal() && hasMonsterInRoom(RoomType.BASEMENT.ordinal())) {
                    Player player = gameState.getPlayer();
                    if(player.getEquippedWeapon() != null && player.getEquippedWeapon().getName().equals("Кочерга")) {
                        ui.showOutput("Вы отразили удар призрака и изгнали его!");
                        rooms.get(RoomType.BASEMENT.ordinal()).removeMonster();
                        gameState.setCurrentRoomId(roomId);
                        ui.showOutput("Вы перешли " + rooms.get(roomId).getDescription());
                        return;
                    } else {
                        ui.showOutput("Призрак в подвале атакует вас нанося вам " + GHOST_DAMAGE + " урона. Вы отброшены назад к лестнице.");
                        gameState.setCurrentRoomId(RoomType.STAIRS.ordinal());
                        int playerHealth = player.getHealth();
                        player.setHealth(playerHealth - GHOST_DAMAGE);
                        return;
                    }
                }
                else {
                    if (gameState.getPlayer().hasArtifact() && gameState.getCurrentRoomId() != RoomType.PEDESTAL.ordinal()) {
                        ui.showOutput("Вы не уничтожили артефакт и его эманации начинают просачиваться сквозь перчатки, вы проходя через мучительную агонию и попутно сойдя с ума вы трансформируетесь в лича");
                        gameState.getPlayer().getInventory().stream().filter(item -> item.getName().equals("Артефакт Хаоса")).findFirst().ifPresent(item -> {
                            gameState.getPlayer().removeItem(item);
                        });
                        gameState.getPlayer().setHasArtifact(true);
                        return;
                    }
                    if (!gameState.isLichKilled() && roomId == RoomType.BEDROOM.ordinal()) {
                        ui.showOutput("Вы не можете пройти в спальню пока жив лич!");
                        return;
                    }
                    if (roomId == RoomType.PEDESTAL.ordinal() && rooms.get(RoomType.PEDESTAL.ordinal()).hasArtifact()) {
                        interactWithPedestal();
                        return;
                    }
                    gameState.setCurrentRoomId(roomId);
                    ui.showOutput("Вы перешли " + rooms.get(roomId).getDescription());
                }
            } else {
                ui.showOutput("Недопустимый переход.");
            }
        } else {
            ui.showOutput("Комната не найдена.");
        }
    }
    public void showGameScreen() {
        ui.showGameScreen(showBriefRoomDescription());
    }

    public void showMenu(String menuOptions) {
        ui.showMenu(menuOptions);
    }
    public String showAvailableRooms() {
        StringBuilder sb = new StringBuilder();
        sb.append("Доступные комнаты:\n");
        if (gameState.getCurrentRoomId() == 0) {
            sb.append("1. Пойти домой\n");
            sb.append("2. Ко входу в башню\n");
        } else {
            if (roomConnections.containsKey(RoomType.values()[gameState.getCurrentRoomId()])) {
                List<RoomType> connections = roomConnections.get(RoomType.values()[gameState.getCurrentRoomId()]);
                for (int i = 0; i < connections.size(); i++) {
                    String roomName = "";
                    switch(connections.get(i)){
                        case PLAINS: roomName = "Поляна"; break;
                        case ENTRANCE: roomName = "Прихожая"; break;
                        case STORAGE: roomName = "Склад"; break;
                        case KITCHEN: roomName = "Кухня"; break;
                        case LICH_ROOM: roomName = "Комната Лича"; break;
                        case BASEMENT: roomName = "Подвал"; break;
                        case STAIRS: roomName = "Лестница"; break;
                        case BEDROOM: roomName = "Спальня"; break;
                        case CATACOMBS: roomName = "Катакомбы"; break;
                        case PEDESTAL: roomName = "Постамент"; break;
                        case BATHROOM: roomName = "Ванна"; break;
                        case LIBRARY: roomName = "Библиотека"; break;
                        case BEFORE_TOWER: roomName = "Перед башней"; break;
                        case GO_HOME: roomName = "Пойти домой"; break;

                    }
                    sb.append((i + 1) + ". " + roomName + "\n");
                }
            }
        }
        return sb.toString();
    }

    public String showAvailableItems() {
        StringBuilder sb = new StringBuilder();
        for (Room room : rooms) {
            if (room.getId() == gameState.getCurrentRoomId() && room.getItem() != null) {
                sb.append("- " + room.getItem().getName() + ": " + room.getItem().toString() + "\n");
            }
        }
        if (sb.toString().isEmpty()) {
            return "В этой комнате нет предметов\n";
        }
        return sb.toString();
    }

    public void pickUpItem(String itemName) {
        Player player = gameState.getPlayer();
        for (Room room : rooms) {
            if (room.getId() == gameState.getCurrentRoomId() && room.getItem() != null && room.getItem().getName().equals(itemName)) {
                player.addItem(room.getItem());
                room.setItem(null);
                ui.showOutput("Вы подобрали " + itemName + ".\n");
                return;
            }
        }
        ui.showOutput("Предмет не найден.\n");
    }

    public int getUserInput() {
        return ui.getUserInput();
    }

    public String showInventory() {
        Player player = gameState.getPlayer();
        StringBuilder sb = new StringBuilder();
        if (player.getInventory().isEmpty()) {
            return "Инвентарь пуст.\n";
        }
        sb.append("Инвентарь:\n");
        for (int i = 0; i < player.getInventory().size(); i++) {
            sb.append((i + 1) + ". " + player.getInventory().get(i) + "\n");
        }
        return sb.toString();
    }

    public boolean battle(String monsterName, GameUI ui) {
        Player player = gameState.getPlayer();
        for (Room room : rooms) {
            if (room.getId() == gameState.getCurrentRoomId() && room.getMonster() != null && room.getMonster().getName().equals(monsterName)) {
                Monster monster = room.getMonster();
                int damage = player.getDamage();
                int monsterDamage = (int) (Math.random() * (40 - 10 + 1)) + 10;
                if (player.getEquippedWeapon() != null && player.getEquippedWeapon().getType().equals("weapon")) {
                    damage = Integer.parseInt(player.getEquippedWeapon().getEffect().replaceAll("[^0-9]", ""));
                }
                if (player.getEquippedArmor() != null && player.getEquippedArmor().getType().equals("armor")) {
                    int armor = Integer.parseInt(player.getEquippedArmor().getEffect().replaceAll("[^0-9]", ""));
                    monsterDamage = Math.max(0, monsterDamage - armor);
                }
                monster.setHealth(monster.getHealth() - damage);
                ui.showOutput("Вы нанесли " + damage + " урона.\n");
                if (monster.getHealth() <= 0) {
                    ui.showOutput("Вы победили " + monsterName + ".\n");
                    player.addDefeatedMonster(monster);
                    removeMonster(monsterName);
                    return true;
                } else {
                    ui.showOutput(monsterName + " наносит " + monsterDamage + " урона\n");
                    player.setHealth(player.getHealth() - monsterDamage);
                    if (player.getHealth() <= 0) {
                        ui.showOutput("Вы погибли.\n");
                        return false;
                    }
                }
                return true;
            }
        }
        ui.showOutput("Монстр не найден.\n");
        return true;
    }

    public boolean hasArtifactInCurrentRoom() {
        for (Room room : rooms) {
            if (room.getId() == gameState.getCurrentRoomId() && room.hasArtifact()) {
                return true;
            }
        }
        return false;
    }

    public String useItem(String itemName, GameUI ui) {
        Player player = gameState.getPlayer();
        for (Item item : player.getInventory()) {
            if (item.getName().equals(itemName)) {
                if (item.getName().equals("Эликсир здоровья")) {
                    player.setHealth(100);
                    ui.showOutput("Здоровье полностью восстановлено!\n");
                    gameState.getPlayer().removeItem(item);
                    return "";
                } else if (item.getName().equals("Вяленое мясо")) {
                    player.setHealth(player.getHealth() + 20);
                    ui.showOutput("Здоровье восстановлено на 20\n");
                    gameState.getPlayer().removeItem(item);
                    return "";
                } else if (item.getName().equals("Целебные ягоды")) {
                    player.setHealth(player.getHealth() + 10);
                    ui.showOutput("Здоровье восстановлено на 10\n");
                    gameState.getPlayer().removeItem(item);
                    return "";
                } else if (item.getName().equals("Лечебные травы")) {
                    player.setHealth(player.getHealth() + 5);
                    ui.showOutput("Здоровье восстановлено на 5\n");
                    gameState.getPlayer().removeItem(item);
                    return "";
                }
            }
        }
        return "Предмет не найден!\n";
    }

    public String equipItem(String itemName) {
        Player player = gameState.getPlayer();
        for (Item item : player.getInventory()) {
            if (item.getName().equals(itemName)) {
                if (item.getType().equals("armor")) {
                    player.setEquippedArmor(item);
                    return "Вы экипировали " + item.getName() + ".\n";
                } else if (item.getType().equals("weapon")) {
                    player.setEquippedWeapon(item);
                    return "Вы экипировали " + item.getName() + ".\n";
                } else {
                    return "Невозможно экипировать " + item.getName() + ".\n";
                }
            }
        }
        return "Предмет не найден.\n";
    }

    public void handleDoor(GameUI ui, Random random) {
        int chance = random.nextInt(100);
        if (chance < 60) {
            ui.showOutput("Вы не смогли взломать дверь.\n");
        } else {
            ui.showOutput("Вы успешно взломали дверь.\n");
            gameState.setDoorBroken(true);
            for (Room room : rooms) {
                if (room.getId() == RoomType.BEFORE_TOWER.ordinal() && room.getItem() != null) {
                    gameState.getPlayer().addItem(room.getItem());
                    removeArtifact(gameState.getCurrentRoomId());
                    ui.showOutput("Вы подобрали " + room.getItem().getName() + "\n");
                    return;
                }
            }
        }
    }

    public Player getPlayer() {
        return gameState.getPlayer();
    }

    private void interactWithPedestal() {
        Player player = gameState.getPlayer();
        Room pedestal = rooms.get(RoomType.PEDESTAL.ordinal());

        if (pedestal.hasArtifact()) {
            if (player.getEquippedArmor() != null && player.getEquippedArmor().getName().equals("Защитный костюм")) {
                ui.showOutput("Перед вами темный, пульсирующий постамент с Артефактом Хаоса.");
                ui.showOutput("1. Извлечь Артефакт Хаоса.");
                ui.showOutput("2. Оставить артефакт на месте и пойти домой.");
                ui.showOutput("3. Уничтожить артефакт");

                Scanner scanner = new Scanner(System.in);
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        ui.showOutput("Эманации артефакта хаоса проникают через защитный костюм, превращая вас в лича. Конец игры.");
                        gameState.getPlayer().setHasArtifact(true);
                        break;
                    case 2:
                        ui.showOutput("Артефакт Хаоса оставленный в башне постепенно наберет достаточно энергии чтобы внутри себя восстановить Лича. Правда с ним похоже будет разбираться другой герой.");
                        gameState.getPlayer().setHasArtifact(true);
                        pedestal.setHasArtifact(false);
                        break;
                    case 3:
                        ui.showOutput("Вы победили! Артефакт Хаоса разрушен и теперь жителям близлежащих деревень ничего не угрожает.");
                        gameState.getPlayer().setHasArtifact(true);
                        pedestal.setHasArtifact(false);
                        break;
                    default:
                        ui.showOutput("Неверный выбор.");
                }
            } else {
                ui.showOutput("Вы попытались извлечь артефакт без защитного костюма! Энергия Хаоса наполняет вас, вы превращаетесь в лича.");
                gameState.getPlayer().setHasArtifact(true);
            }
        } else {
            ui.showOutput("Постамент пуст.");
        }
    }
    public void executeEvent(GameEvent event, GameUI ui, String itemNameForUse, String itemNameForEquip) throws IOException{
        if (event instanceof PickUpItemEvent) {
            pickUpItem(itemNameForUse);
        } else if (event instanceof BattleEvent) {
            if (hasMonsterInCurrentRoom()){
                battle(itemNameForUse, ui);
            }
        }else if (event instanceof UseItemEvent){
            useItem(itemNameForUse, ui);
            equipItem(itemNameForEquip);
        }
        else if (event instanceof SaveGameEvent){
            // Nothing
        }
        else if (event instanceof  HandleDoorEvent){
            // Nothing
        }
        else if (event instanceof MoveEvent){
            movePlayerToRoom(Integer.parseInt(itemNameForUse));
        }
        else{
            event.execute(ui, this);
        }
    }
}