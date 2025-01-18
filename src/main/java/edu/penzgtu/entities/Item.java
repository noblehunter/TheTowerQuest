package edu.penzgtu.entities;

// --- Предметы ---

public class Item {
    private String name;
    private String description;
    private String type;
    private String effect;

    public Item() {
        // Конструктор без аргументов (default constructor)
    }

    public Item(String name, String description, String type, String effect) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.effect = effect;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getEffect() {
        return effect;
    }
    public void setType(String type) {
        this.type = type;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item item = (Item) obj;
        return name.equals(item.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}