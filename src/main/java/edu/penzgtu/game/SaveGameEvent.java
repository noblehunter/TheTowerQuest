package edu.penzgtu.game;

import edu.penzgtu.services.GameService;
import edu.penzgtu.services.SaveLoadService;
import edu.penzgtu.ui.GameUI;
import java.io.IOException;

public class SaveGameEvent implements GameEvent{
    private final int saveSlot;
    private final SaveLoadService saveLoadService;
    public SaveGameEvent(int saveSlot, SaveLoadService saveLoadService) {
        this.saveSlot = saveSlot;
        this.saveLoadService = saveLoadService;
    }

    @Override
    public void execute(GameUI ui, GameService gameService) throws IOException {
        ui.showOutput("Введите номер слота для сохранения (1-10):");
        try{
            saveLoadService.saveGame(gameService.getGameState(),saveSlot);
            ui.showOutput("Игра сохранена");
        }catch (IOException e){
            ui.showOutput("Не удалось сохранить игру.");
        }
    }
}