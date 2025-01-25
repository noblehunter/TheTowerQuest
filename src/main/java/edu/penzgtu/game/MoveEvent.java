package edu.penzgtu.game;

import edu.penzgtu.services.GameService;
import edu.penzgtu.ui.GameUI;
import java.io.IOException;

public class MoveEvent implements GameEvent{
    private final int roomId;
    public MoveEvent(int roomId) {
        this.roomId = roomId;
    }
    @Override
    public void execute(GameUI ui, GameService gameService) throws IOException {
        gameService.movePlayerToRoom(roomId);
    }
}
