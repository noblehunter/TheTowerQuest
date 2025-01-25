package edu.penzgtu.game;

import edu.penzgtu.services.GameService;
import edu.penzgtu.ui.GameUI;
import java.io.IOException;

public class BattleEvent implements GameEvent {
    private final String monsterName;
    private String battle = "battle";
    private String prevMessage = "";
    public BattleEvent(String monsterName) {
        this.monsterName = monsterName;
    }
    @Override
    public void execute(GameUI ui, GameService gameService) throws IOException {
        battleMenu(ui, gameService, monsterName, battle, prevMessage);
    }
    private boolean battleMenu(GameUI ui, GameService gameService, String monsterName, String battle, String prevMessage) throws IOException {
        while (battle.equals("battle")){
            if(gameService.battle(monsterName, ui)){
                if (gameService.getPlayer().getHealth() <= 0) {
                    return false;
                }
                battle = "";
            }
        }
        return true;
    }
}