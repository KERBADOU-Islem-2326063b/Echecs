package com.example.echecs.controllers;

import java.io.File;

public class GameController {
    private static int initialTimeInSeconds;
    // 0 -> on charge pas, 1 -> on charge la game a partir d'un fichier
    private static int charge;
    private static File chargedFile;

    public static void setInitialTimeInSeconds(int initialTimeInSeconds) {
        GameController.initialTimeInSeconds = initialTimeInSeconds;
    }
    public static int getInitialTimeInSeconds() {
        return initialTimeInSeconds;
    }

    public static void setCharge(int charge) {
        GameController.charge = charge;
    }
    public static int getCharge() {
        return charge;
    }
    public static void setChargedFile(File chargedFile) {
        GameController.chargedFile = chargedFile;
    }
    public static File getChargedFile() {
        return chargedFile;
    }
}
