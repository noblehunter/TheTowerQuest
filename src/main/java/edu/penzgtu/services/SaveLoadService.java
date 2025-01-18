package edu.penzgtu.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.penzgtu.entities.GameState;

import java.io.File;
import java.io.IOException;

public class SaveLoadService {
    private static final String SAVE_FOLDER = "Save"; // Папка сохранений
    private static final String[] SAVE_FILE_NAMES = {
            "save1.json", "save2.json", "save3.json", "save4.json", "save5.json",
            "save6.json", "save7.json", "save8.json", "save9.json", "save10.json"
    };

    private final ObjectMapper objectMapper = new ObjectMapper();
    public void saveGame(GameState gameState, int slot) throws IOException {
        if (slot < 1 || slot > SAVE_FILE_NAMES.length) {
            System.out.println("Неверный номер слота для сохранения.");
            return;
        }
        File saveDir = new File(SAVE_FOLDER);
        if (!saveDir.exists()) {
            if(!saveDir.mkdirs()) {
                System.out.println("Не удалось создать папку для сохранений!");
                return;
            }
        }
        String fileName = SAVE_FILE_NAMES[slot - 1];
        File saveFile = new File(saveDir,fileName);
        objectMapper.writeValue(saveFile, gameState);
    }

    public GameState loadGame(int slot) throws IOException {
        if (slot < 1 || slot > SAVE_FILE_NAMES.length) {
            System.out.println("Неверный номер слота для загрузки.");
            return null;
        }

        String fileName = SAVE_FILE_NAMES[slot - 1];
        File saveDir = new File(SAVE_FOLDER);
        File saveFile = new File(saveDir,fileName);

        if (!saveFile.exists()) {
            System.out.println("Сохранение в слоте " + slot + " не найдено.");
            return null;
        }
        return objectMapper.readValue(saveFile, GameState.class);
    }
}