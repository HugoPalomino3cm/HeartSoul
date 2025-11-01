package com.heartsoul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase maneja todos los sonidos/música del juego usando Sound de LibGDX.
 * Implementa un singleton para acceso global.
 */
public class SoundManager implements Disposable {

    private static SoundManager instance;
    private final Map<String, Sound> soundCache = new HashMap<>();
    private Long currentMusicId = null;
    private Sound currentMusic = null;
    private boolean musicPaused = false;

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    // Carga y cachea sonidos
    private Sound getSound(String soundPath) {
        if (!soundCache.containsKey(soundPath)) {
            try {
                Sound sound = Gdx.audio.newSound(Gdx.files.internal(soundPath));
                soundCache.put(soundPath, sound);
            } catch (Exception e) {
                System.err.println("No se pudo cargar el sonido: " + soundPath + " - " + e.getMessage());
                return null;
            }
        }
        return soundCache.get(soundPath);
    }

    // Reproducir efecto simple
    public void playSound(String soundPath) {
        Sound sound = getSound(soundPath);
        if (sound != null) {
            sound.play();
        }
    }

    // Reproducir efecto con volumen y loop
    public void playSound(String soundPath, float volume, boolean loop) {
        Sound sound = getSound(soundPath);
        if (sound != null) {
            long id = sound.play(volume);
            sound.setLooping(id, loop);
        }
    }

    // "Música": reproduce un sound en loop y detiene el anterior
    public void playMusic(String musicPath, float volume) {
        boolean wasPaused = musicPaused; // Guardar el estado de pausa
        stopMusic(); // Detiene la música anterior
        currentMusic = getSound(musicPath);
        if (currentMusic != null) {
            currentMusicId = currentMusic.play(volume);
            currentMusic.setLooping(currentMusicId, true);

            // Si la música estaba pausada, pausar la nueva también
            if (wasPaused) {
                currentMusic.pause(currentMusicId);
                musicPaused = true;
            } else {
                musicPaused = false;
            }
        }
    }

    // Pausa la "música" (sound en loop)
    public void pauseMusic() {
        if (currentMusic != null && currentMusicId != null) {
            currentMusic.pause(currentMusicId);
            musicPaused = true;
        }
    }

    // Resume la "música"
    public void resumeMusic() {
        if (currentMusic != null && currentMusicId != null) {
            currentMusic.resume(currentMusicId);
            musicPaused = false;
        }
    }

    // Verifica si la música está pausada
    public boolean isMusicPaused() {
        return musicPaused;
    }

    // Detiene la música
    public void stopMusic() {
        if (currentMusic != null && currentMusicId != null) {
            currentMusic.stop(currentMusicId);
            currentMusicId = null;
            currentMusic = null;
            musicPaused = false;
        }
    }

    @Override
    public void dispose() {
        stopMusic();
        for (Sound sound : soundCache.values()) {
            sound.dispose();
        }
        soundCache.clear();
    }
}
