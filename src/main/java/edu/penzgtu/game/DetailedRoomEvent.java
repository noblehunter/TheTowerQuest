package edu.penzgtu.game;

import edu.penzgtu.services.GameService;
import edu.penzgtu.ui.GameUI;
import java.io.IOException;

public class DetailedRoomEvent implements GameEvent {
    @Override
    public void execute(GameUI ui, GameService gameService) throws IOException {
        detailedRoomLoop(ui, gameService);
    }
    private void detailedRoomLoop(GameUI ui, GameService gameService) {
        boolean inDetailedRoom = true;
        while (inDetailedRoom) {
            ui.clearScreen();
            ui.showOutput(gameService.showBriefRoomDescription());
            ui.showMenu("1. Назад");
            int action = ui.getUserInput();
            if (action == 1) {
                inDetailedRoom = false;
            }
        }
    }
}
