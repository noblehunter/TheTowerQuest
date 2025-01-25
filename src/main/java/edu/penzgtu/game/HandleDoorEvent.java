package edu.penzgtu.game;

import edu.penzgtu.services.GameService;
import edu.penzgtu.ui.GameUI;
import java.io.IOException;
import java.util.Random;

public class HandleDoorEvent implements GameEvent{
    private final Random random;
    public HandleDoorEvent(Random random) {
        this.random = random;
    }
    @Override
    public void execute(GameUI ui, GameService gameService) throws IOException {
        if (gameService.hasArtifactInCurrentRoom()) {
            gameService.handleDoor(ui, random);
        } else {
            ui.showOutput("В этой комнате нет двери.\n");
        }
    }
}