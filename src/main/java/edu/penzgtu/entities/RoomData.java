package edu.penzgtu.entities;

import java.util.List;

public class RoomData {
    private final RoomType type;
    private final String description;
    private final Item item;
    private final Monster monster;
    private final boolean hasArtifact;
    private final List<RoomType> connections;


    public RoomData(RoomType type, String description, Item item, Monster monster, boolean hasArtifact, List<RoomType> connections) {
        this.type = type;
        this.description = description;
        this.item = item;
        this.monster = monster;
        this.hasArtifact = hasArtifact;
        this.connections = connections;
    }

    public RoomType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Item getItem() {
        return item;
    }

    public Monster getMonster() {
        return monster;
    }

    public boolean hasArtifact() {
        return hasArtifact;
    }
    public List<RoomType> getConnections() {
        return connections;
    }
}