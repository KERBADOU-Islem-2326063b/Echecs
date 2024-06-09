package com.example.echecs.controllers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur pour gérer les informations et les statistiques du jeu d'échecs.
 */
public class GameController {
    private static int initialTimeInSeconds;
    // 0 -> on ne charge pas, 1 -> on charge la partie à partir d'un fichier
    private static int charge;
    private static File chargedFile;
    private static String firstName;
    private static String lastName;
    private static int gamesPlayed;
    private static int gamesWon;

    private static String ennemyFirstName;
    private static String ennemyLastName;
    private static int ennemyGamesPlayed;
    private static int ennemyGamesWon;

    private static final String CSV_FILE_PATH = "src/main/resources/com/example/echecs/accountFiles/accounts.csv";

    /**
     * Définit le temps initial en secondes.
     *
     * @param initialTimeInSeconds Le temps initial en secondes.
     */
    public static void setInitialTimeInSeconds(int initialTimeInSeconds) {
        GameController.initialTimeInSeconds = initialTimeInSeconds;
    }

    /**
     * Retourne le temps initial en secondes.
     *
     * @return Le temps initial en secondes.
     */
    public static int getInitialTimeInSeconds() {
        return initialTimeInSeconds;
    }

    /**
     * Définit l'état de charge.
     *
     * @param charge L'état de charge (0 pour non chargé, 1 pour chargé).
     */
    public static void setCharge(int charge) {
        GameController.charge = charge;
    }

    /**
     * Retourne l'état de charge.
     *
     * @return L'état de charge.
     */
    public static int getCharge() {
        return charge;
    }

    /**
     * Définit le fichier chargé.
     *
     * @param chargedFile Le fichier chargé.
     */
    public static void setChargedFile(File chargedFile) {
        GameController.chargedFile = chargedFile;
    }

    /**
     * Retourne le fichier chargé.
     *
     * @return Le fichier chargé.
     */
    public static File getChargedFile() {
        return chargedFile;
    }

    /**
     * Définit le prénom du joueur.
     *
     * @param firstName Le prénom du joueur.
     */
    public static void setFirstName(String firstName) {
        GameController.firstName = firstName;
    }

    /**
     * Retourne le prénom du joueur.
     *
     * @return Le prénom du joueur.
     */
    public static String getFirstName() {
        return firstName;
    }

    /**
     * Définit le nom de famille du joueur.
     *
     * @param lastName Le nom de famille du joueur.
     */
    public static void setLastName(String lastName) {
        GameController.lastName = lastName;
    }

    /**
     * Retourne le nom de famille du joueur.
     *
     * @return Le nom de famille du joueur.
     */
    public static String getLastName() {
        return lastName;
    }

    /**
     * Définit le nombre de parties jouées par le joueur.
     *
     * @param gamesPlayed Le nombre de parties jouées.
     */
    public static void setGamesPlayed(int gamesPlayed) {
        GameController.gamesPlayed = gamesPlayed;
    }

    /**
     * Définit le nombre de parties gagnées par le joueur.
     *
     * @param gamesWon Le nombre de parties gagnées.
     */
    public static void setGamesWon(int gamesWon) {
        GameController.gamesWon = gamesWon;
    }

    /**
     * Définit le prénom de l'ennemi.
     *
     * @param enemyFirstName Le prénom de l'ennemi.
     */
    public static void setEnemyFirstName(String enemyFirstName) {
        GameController.ennemyFirstName = enemyFirstName;
    }

    /**
     * Retourne le prénom de l'ennemi.
     *
     * @return Le prénom de l'ennemi.
     */
    public static String getEnemyFirstName() {
        return ennemyFirstName;
    }

    /**
     * Définit le nom de famille de l'ennemi.
     *
     * @param enemyLastName Le nom de famille de l'ennemi.
     */
    public static void setEnemyLastName(String enemyLastName) {
        GameController.ennemyLastName = enemyLastName;
    }

    /**
     * Retourne le nom de famille de l'ennemi.
     *
     * @return Le nom de famille de l'ennemi.
     */
    public static String getEnemyLastName() {
        return ennemyLastName;
    }

    /**
     * Définit le nombre de parties jouées par l'ennemi.
     *
     * @param enemyGamesPlayed Le nombre de parties jouées par l'ennemi.
     */
    public static void setEnemyGamesPlayed(int enemyGamesPlayed) {
        GameController.ennemyGamesPlayed = enemyGamesPlayed;
    }

    /**
     * Définit le nombre de parties gagnées par l'ennemi.
     *
     * @param enemyGamesWon Le nombre de parties gagnées par l'ennemi.
     */
    public static void setEnemyGamesWon(int enemyGamesWon) {
        GameController.ennemyGamesWon = enemyGamesWon;
    }

    /**
     * Met à jour les statistiques du joueur.
     *
     * @param firstName Le prénom du joueur.
     * @param lastName  Le nom de famille du joueur.
     * @param win       Indique si le joueur a gagné la partie.
     */
    public static void updateStats(String firstName, String lastName, boolean win) {
        if (!win) {
            ++gamesPlayed;
        } else {
            ++gamesWon;
        }
        try {
            File csvFile = new File(CSV_FILE_PATH);
            BufferedReader reader = new BufferedReader(new FileReader(csvFile));
            List<String> lines = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
            for (int i = 0; i < lines.size(); i++) {
                String[] data = lines.get(i).split(",");
                if (data.length >= 2 && data[0].equals(firstName) && data[1].equals(lastName)) {
                    data[2] = String.valueOf(gamesPlayed);
                    data[3] = String.valueOf(gamesWon);
                    lines.set(i, String.join(",", data));
                    break;
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile));
            for (String updatedLine : lines) {
                writer.write(updatedLine);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Met à jour les statistiques de l'ennemi.
     *
     * @param firstName Le prénom de l'ennemi.
     * @param lastName  Le nom de famille de l'ennemi.
     * @param win       Indique si l'ennemi a gagné la partie.
     */
    public static void ennemyUpdateStats(String firstName, String lastName, boolean win) {
        if (!win) {
            ++ennemyGamesPlayed;
        } else {
            ++ennemyGamesWon;
        }
        try {
            File csvFile = new File(CSV_FILE_PATH);
            BufferedReader reader = new BufferedReader(new FileReader(csvFile));
            List<String> lines = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
            for (int i = 0; i < lines.size(); i++) {
                String[] data = lines.get(i).split(",");
                if (data.length >= 2 && data[0].equals(firstName) && data[1].equals(lastName)) {
                    data[2] = String.valueOf(ennemyGamesPlayed);
                    data[3] = String.valueOf(ennemyGamesWon);
                    lines.set(i, String.join(",", data));
                    break;
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile));
            for (String updatedLine : lines) {
                writer.write(updatedLine);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
