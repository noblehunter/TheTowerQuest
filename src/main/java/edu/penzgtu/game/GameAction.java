package edu.penzgtu.game;

public class GameAction {
    private final String name;
    private final GameEvent event;

    public GameAction(String name, GameEvent event) {
        this.name = name;
        this.event = event;
    }
    public String getName() {
        return name;
    }

    public GameEvent getEvent() {
        return event;
    }
}

