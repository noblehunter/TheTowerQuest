package edu.penzgtu.game;

import edu.penzgtu.services.GameService;
import edu.penzgtu.ui.GameUI;
import java.io.IOException;

public class ShowPlayerInfoEvent implements GameEvent{

    @Override
    public void execute(GameUI ui, GameService gameService) throws IOException {
        ui.showOutput(gameService.getPlayer().toString());
    }
}
