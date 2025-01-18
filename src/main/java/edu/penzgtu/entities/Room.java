package edu.penzgtu.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
    private int id;
    private String description;
    private List<Item> items;
    private Map<String, Monster> monsters;

    private boolean hasArtifact;

    public Room(int id, String description) {
        this.id = id;
        this.description = description;
        this.items = new ArrayList<>();
        this.monsters = new HashMap<>();
        this.hasArtifact = false;

    }

    public void setHasArtifact(boolean hasArtifact) {
        this.hasArtifact = hasArtifact;
    }

    public boolean hasArtifact() {
        return hasArtifact;
    }
    public void removeArtifact() {
        this.hasArtifact = false;
    }


    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public List<Item> getItems() {
        return items;
    }
    public void addItem(Item item) {
        this.items.add(item);
    }
    public void removeMonster(String monsterName) {
        this.monsters.remove(monsterName);
    }

    public Map<String, Monster> getMonsters() {
        return monsters;
    }
    public void addMonster(Monster monster) {
        monsters.put(monster.getName(), monster);
    }
    public boolean hasMonster(String monsterName) {
        return monsters.containsKey(monsterName);
    }


}