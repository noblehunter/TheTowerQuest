package edu.penzgtu.entities;

public class GameState {
    private int currentRoomId;
    private Player player;
    private boolean doorBroken = false;
    private boolean lichKilled = false;
    private boolean ghostKilled = false;
    private boolean hasMagicKey = false;
    private boolean hasArtifact = false;

    public GameState() {}

    public GameState(int currentRoomId, Player player) {
        this.currentRoomId = currentRoomId;
        this.player = player;
    }

    public int getCurrentRoomId() {
        return currentRoomId;
    }

    public void setCurrentRoomId(int currentRoomId) {
        this.currentRoomId = currentRoomId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isDoorBroken() {
        return doorBroken;
    }

    public void setDoorBroken(boolean doorBroken) {
        this.doorBroken = doorBroken;
    }

    public boolean isLichKilled() {
        return lichKilled;
    }

    public void setLichKilled(boolean lichKilled) {
        this.lichKilled = lichKilled;
    }

    public boolean isGhostKilled() {
        return ghostKilled;
    }

    public void setGhostKilled(boolean ghostKilled) {
        this.ghostKilled = ghostKilled;
    }

    public boolean hasMagicKey() {
        return hasMagicKey;
    }

    public void setHasMagicKey(boolean hasMagicKey) {
        this.hasMagicKey = hasMagicKey;
    }

    public boolean hasArtifact() {
        return hasArtifact;
    }

    public void setHasArtifact(boolean hasArtifact) {
        this.hasArtifact = hasArtifact;
    }

    @Override
    public String toString() {
        return "Current room id: " + currentRoomId + "\n" + "Player: " + player;
    }
}