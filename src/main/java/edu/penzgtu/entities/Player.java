package edu.penzgtu.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Player {
    private String name;
    private int health;
    private int damage;
    private List<Item> inventory;
    private Item equippedWeapon;
    private Item equippedArmor;
    private Item equippedShield;
    private boolean hasArtifact;
    private List<Monster> defeatedMonsters;

    public boolean hasShield() {
        return equippedShield != null;
    }

    public void setShield(boolean shield) {
        this.shield = shield;
    }
    private boolean shield;
    private boolean hasEquipment;

    public boolean hasEquipment() {
        return hasEquipment;
    }

    public void setHasEquipment(boolean hasEquipment) {
        this.hasEquipment = hasEquipment;
    }

    public Player(String name) {
        this.name = name;
        this.health = 100;
        this.damage = 20;
        this.inventory = new ArrayList<>();
        this.defeatedMonsters = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void addItem(Item item){
        this.inventory.add(item);
    }
    public void removeItem(Item item){
        this.inventory.remove(item);
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    public Item getEquippedWeapon() {
        return equippedWeapon;
    }

    public void setEquippedWeapon(Item equippedWeapon) {
        this.equippedWeapon = equippedWeapon;
    }

    public Item getEquippedArmor() {
        return equippedArmor;
    }

    public void setEquippedArmor(Item equippedArmor) {
        this.equippedArmor = equippedArmor;
    }

    public Item getEquippedShield() {
        return equippedShield;
    }

    public void setEquippedShield(Item equippedShield) {
        this.equippedShield = equippedShield;
    }
    public Optional<Item> getItem(String itemName){
        return inventory.stream().filter(item -> item.getName().equals(itemName)).findFirst();
    }
    public boolean hasArtifact() {
        return hasArtifact;
    }

    public void setHasArtifact(boolean hasArtifact) {
        this.hasArtifact = hasArtifact;
    }
    public void addDefeatedMonster(Monster monster) {
        this.defeatedMonsters.add(monster);
    }

    public List<Monster> getDefeatedMonsters() {
        return defeatedMonsters;
    }
    @Override
    public String toString() {
        return "Имя: " + name + "\n" +
                "Здоровье: " + health + "\n" +
                "Атака: " + damage + "\n" +
                "Экипированное оружие: " + (equippedWeapon != null ? equippedWeapon.getName() : "Нет") + "\n" +
                "Экипированная броня: " + (equippedArmor != null ? equippedArmor.getName() : "Нет") + "\n" +
                "Экипированный щит: " + (equippedShield != null ? equippedShield.getName() : "Нет");
    }
}