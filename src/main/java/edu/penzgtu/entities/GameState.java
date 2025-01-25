package edu.penzgtu.entities;

import java.util.List;
import java.util.Map;

public class GameState {
    private Player player;
    private int currentRoomId;
    private boolean isDoorBroken;
    private boolean hasArtifact;
    private Map<RoomType, List<RoomType>> roomConnections;
    private List<Room> rooms;
    private boolean isLichKilled;
    public GameState() {
        this.player = new Player("Безымянный герой");
        this.currentRoomId = 0;
        this.isDoorBroken = false;
    }
    public GameState(int currentRoomId, Player player) {
        this.currentRoomId = currentRoomId;
        this.player = player;
        this.isDoorBroken = false;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getCurrentRoomId() {
        return currentRoomId;
    }

    public void setCurrentRoomId(int currentRoomId) {
        this.currentRoomId = currentRoomId;
    }

    public boolean isDoorBroken() {
        return isDoorBroken;
    }

    public void setDoorBroken(boolean doorBroken) {
        isDoorBroken = doorBroken;
    }

    public boolean hasArtifact() {
        return hasArtifact;
    }

    public void setHasArtifact(boolean hasArtifact) {
        this.hasArtifact = hasArtifact;
    }
    public Map<RoomType, List<RoomType>> getRoomConnections() {
        return roomConnections;
    }

    public void setRoomConnections(Map<RoomType, List<RoomType>> roomConnections) {
        this.roomConnections = roomConnections;
    }
    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
    public boolean isLichKilled() {
        return isLichKilled;
    }

    public void setLichKilled(boolean lichKilled) {
        isLichKilled = lichKilled;
    }
}
