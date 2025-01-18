package edu.penzgtu.entities;

import java.util.Random;


public class Monster {
    private String name;
    private int health;
    private int damage;
    private boolean boss;

    public Monster(String name, int health, int damage, boolean boss) {
        this.name = name;
        this.health = health;
        this.damage = damage;
        this.boss = boss;
    }


    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isBoss() {
        return boss;
    }
    @Override
    public String toString() {
        return "Название: " + name; // toString для Monster
    }
}



