package edu.penzgtu.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Player {
    private int health;
    private String name;
    private String equippedWeaponName;
    private boolean hasArtifact = false;
    private Item equippedWeapon;
    private Item equippedArmor;
    private Item equippedShield;
    private List<Item> inventory;

    public Player(){
    }

    public Player(String name, int health) {
        this.name = name;
        this.health = health;
        this.inventory = new ArrayList<>();
        this.equippedWeapon = null;
        this.equippedArmor = null;
        this.equippedShield = null;
    }

    public Player(int health, String name, String equippedWeaponName, int armor) {
        this.health = health;
        this.name = name;
        this.equippedWeaponName = equippedWeaponName;
        this.inventory = new ArrayList<>();
    }
    // ... (остальные методы Player, getter'ы, setter'ы, toString, equals, hashCode, и т.д.)

    public void addItem(Item item) {
        this.inventory.add(item);
    }
    public Optional<Item> getItem(String itemName) {
        return this.inventory.stream().filter(item -> item.getName().equals(itemName)).findFirst();
    }
    public void removeItem(Item item) {
        this.inventory.remove(item);
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setEquippedWeapon(Item equippedWeapon) {
        this.equippedWeapon = equippedWeapon;
    }
    public Item getEquippedWeapon() {
        return equippedWeapon;
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
    public List<Item> getInventory() {
        return inventory;
    }
    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    @Override
    public String toString() {
        String weaponName = equippedWeapon != null ? equippedWeapon.getName() : "Нет";
        String armorName = equippedArmor != null ? equippedArmor.getName() : "Нет";
        String shieldName = equippedShield != null ? equippedShield.getName() : "Нет";
        return "Имя: " + name + "\n" +
                "Здоровье: " + health + "\n" +
                "Экипированное оружие: " + weaponName + "\n"+
                "Экипированная броня: " + armorName + "\n"+
                "Экипированный щит: " + shieldName + "\n";

    }
    public int calculateDamageReduction(int damage) {
        int totalArmor = 0;

        if (equippedArmor != null) {
            if (equippedArmor.getName().equals("Кожаный доспех")){
                totalArmor = 0;
            } else if (equippedArmor.getName().equals("Защитные магические перчатки")){
                totalArmor = 40;
            } else {
                String effect = equippedArmor.getEffect();
                String[] parts = effect.split(" ");
                if(parts[0].equals("armor")) {
                    totalArmor =  Integer.parseInt(parts[1]);
                }
            }
        }
        if (equippedShield != null && equippedShield.getName().equals("Щит")) {
            totalArmor += 10;
        }
        return Math.max(0, damage - totalArmor);
    }


    public boolean hasShield() {
        return this.equippedArmor != null && this.equippedArmor.getName().equals("Щит");
    }

    public void setHasArtifact(boolean hasArtifact) {
        this.hasArtifact = hasArtifact;
    }

    public boolean hasArtifact() {
        return hasArtifact;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Player player = (Player) obj;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}


