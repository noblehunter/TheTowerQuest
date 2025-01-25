package edu.penzgtu.entities;


public class Room {
    private int id;
    private String description;
    private Item item;
    private Monster monster;
    private boolean hasArtifact;



    public Room(int id, String description, Item item, Monster monster) {
        this.id = id;
        this.description = description;
        this.item = item;
        this.monster = monster;
    }

    // ... (Getters and Setters)

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public void removeMonster() {
        this.monster = null;
    }


    public boolean hasArtifact() {
        return hasArtifact;
    }

    public void setHasArtifact(boolean hasArtifact) {
        this.hasArtifact = hasArtifact;
    }
    public void removeArtifact() {
        this.hasArtifact = false;
    }
}