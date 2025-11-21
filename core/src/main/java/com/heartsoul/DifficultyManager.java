package com.heartsoul;

public class DifficultyManager {
    private static DifficultyManager instance;

    public enum Difficulty {
        EASY("Fácil", "Enemigos más lentos, power-ups duran más"),
        NORMAL("Normal", "Experiencia balanceada"),
        HARD("Difícil", "Balas multidireccionales, spawn 2x más rápido, power-ups duran 50% menos");

        private final String displayName;
        private final String description;

        Difficulty(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getDescription() {
            return description;
        }
    }

    private Difficulty currentDifficulty;

    private DifficultyManager() {
        this.currentDifficulty = Difficulty.NORMAL; // Dificultad por defecto
    }

    public static DifficultyManager getInstance() {
        if (instance == null) {
            instance = new DifficultyManager();
        }
        return instance;
    }

    public Difficulty getCurrentDifficulty() {
        return currentDifficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.currentDifficulty = difficulty;
    }

    public void reset() {
        this.currentDifficulty = Difficulty.NORMAL;
    }
}
