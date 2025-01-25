package edu.penzgtu.game;

import edu.penzgtu.services.GameService;
import edu.penzgtu.ui.GameUI;

import java.io.IOException;

public interface GameEvent {
    void execute(GameUI ui, GameService gameService) throws IOException;
}