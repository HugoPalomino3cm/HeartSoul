package com.heartsoul;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

/**
 * Clase maneja todos los sonidos/música del juego usando Sound de LibGDX.
 * Implementa un singleton para acceso global.
 **/
public class SoundManager implements Disposable {

    private Long currentMusicId = null;
    private Sound currentMusic = null;
    private boolean musicPaused = false;

    private static SoundManager instance;

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    // Carga un Sound nuevo cada vez (sin cache)
    private Sound getSound(String soundPath) {
        if (soundPath == null || soundPath.isEmpty()) {
            System.err.println("Ruta de sonido vacía o nula: " + soundPath);
            return null;
        }
        try {
            return Gdx.audio.newSound(Gdx.files.internal(soundPath));
        } catch (Exception e) {
            System.err.println("No se pudo cargar el sonido: " + soundPath + " - " + e.getMessage());
            return null;
        }
    }

    // Reproducir efecto simple
    public void playSound(String soundPath) {
        Sound sound = getSound(soundPath);
        if (sound != null) {
            sound.play();
            // No se guarda en cache; no se dispone inmediatamente para mantener compatibilidad
        }
    }

    // Reproducir efecto con volumen y loop
    public void playSound(String soundPath, float volume, boolean loop) {
        Sound sound = getSound(soundPath);
        if (sound != null) {
            long id = sound.play(volume);
            sound.setLooping(id, loop);
            // Si se quiere optimizar, se puede disponer aquí cuando no haya loop, pero lo dejamos así por simplicidad
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
            try {
                currentMusic.dispose();
            } catch (Exception e) {
                System.err.println("Error al disponer la música: " + e.getMessage());
            }
            currentMusicId = null;
            currentMusic = null;
            musicPaused = false;
        }
    }

    @Override
    public void dispose() {
        stopMusic();
        // Sin cache, nada más que limpiar
    }
}
