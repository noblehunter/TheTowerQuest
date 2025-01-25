package edu.penzgtu.game;

import edu.penzgtu.services.GameService;
import edu.penzgtu.ui.GameUI;
import java.io.IOException;

public class PickUpItemEvent implements GameEvent {
    private final String itemName;

    public PickUpItemEvent(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public void execute(GameUI ui, GameService gameService) throws IOException {
        gameService.pickUpItem(itemName);
    }
}
