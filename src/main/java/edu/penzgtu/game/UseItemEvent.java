package edu.penzgtu.game;

import edu.penzgtu.services.GameService;
import edu.penzgtu.ui.GameUI;
import java.io.IOException;

public class UseItemEvent implements GameEvent{
    private final String itemNameForUse;
    private final String itemNameForEquip;

    public UseItemEvent(String itemNameForUse, String itemNameForEquip) {
        this.itemNameForUse = itemNameForUse;
        this.itemNameForEquip = itemNameForEquip;
    }

    @Override
    public void execute(GameUI ui, GameService gameService) throws IOException {
        ui.showOutput(gameService.showInventory());
        ui.showOutput("Какой предмет вы хотите использовать?\n");
        ui.showOutput(gameService.useItem(itemNameForUse, ui));
        ui.showOutput("Какой предмет вы хотите экипировать?\n");
        ui.showOutput(gameService.equipItem(itemNameForEquip));
    }
}