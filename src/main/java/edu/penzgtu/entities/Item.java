package edu.penzgtu.entities;

public class Item {
    private String name;

    private String type;
    private String description;
    private String effect;
    public Item() {
    }

    public Item(String name) {
        this.name = name;
    }
    public Item(String name, String type, String description, String effect) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.effect = effect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }
    @Override
    public String toString() {
        return  name;
    }
}