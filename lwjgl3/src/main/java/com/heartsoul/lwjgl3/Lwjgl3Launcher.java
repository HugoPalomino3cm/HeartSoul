package com.heartsoul.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.heartsoul.Main;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        try {
            if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
            createApplication();
        } catch (Throwable t) {
            t.printStackTrace();
            System.err.println("La aplicación falló al iniciarse. Verifique recursos (iconos, assets) y la traza arriba.");
            try {
                Thread.sleep(5000); // Mantener la consola abierta un momento para ver el error
            } catch (InterruptedException ignored) {}
            System.exit(1);
        }
    }

    private static void createApplication() {
        new Lwjgl3Application(new Main(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("HeartSoul");
        // Eliminada la llamada a configuration.setWindowIcon("img.png"); porque causa redundancia/posible fallo si el recurso no está.
        //// Vsync limits the frames per second to what your hardware can display, and helps eliminate
        //// screen tearing. This setting doesn't always work on Linux, so the line after is a safeguard.
        configuration.useVsync(true);
        //// Limits FPS to the refresh rate of the currently active monitor, plus 1 to try to match fractional
        //// refresh rates. The Vsync setting above should limit the actual FPS to match the monitor.
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        //// Start the game in fullscreen on the primary monitor so the virtual viewports keep the same aspect and layout.
        //configuration.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
        //// You can change these files; they are in lwjgl3/src/main/resources/ .
        try {
            configuration.setWindowIcon("Icon128.png", "Icon64.png", "Icon32.png", "Icon16.png");
        } catch (Throwable e) {
            // Evitar que fallos al cargar los iconos detengan el arranque; mostrar aviso para depuración.
            System.err.println("Aviso: no se pudieron cargar los iconos de la ventana: " + e.getMessage());
        }
        return configuration;
    }
}
