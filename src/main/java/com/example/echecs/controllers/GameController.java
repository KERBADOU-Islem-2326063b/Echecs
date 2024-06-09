package com.example.echecs.controllers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    private static int initialTimeInSeconds;
    // 0 -> on charge pas, 1 -> on charge la game a partir d'un fichier
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

    public static void setFirstName(String firstName) {
        GameController.firstName = firstName;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static void setLastName(String lastName) {
        GameController.lastName = lastName;
    }

    public static String getLastName() {
        return lastName;
    }

    public static void setGamesPlayed(int gamesPlayed) {
        GameController.gamesPlayed = gamesPlayed;
    }


    public static void setGamesWon(int gamesWon) {
        GameController.gamesWon = gamesWon;
    }


    public static void setEnemyFirstName(String enemyFirstName) {
        GameController.ennemyFirstName = enemyFirstName;
    }

    public static String getEnemyFirstName() {
        return ennemyFirstName;
    }

    public static void setEnemyLastName(String enemyLastName) {
        GameController.ennemyLastName = enemyLastName;
    }

    public static String getEnemyLastName() {
        return ennemyLastName;
    }

    public static void setEnemyGamesPlayed(int enemyGamesPlayed) {
        GameController.ennemyGamesPlayed = enemyGamesPlayed;
    }



    public static void setEnemyGamesWon(int enemyGamesWon) {
        GameController.ennemyGamesWon = enemyGamesWon;
    }



    public static void updateStats(String firstName, String lastName, boolean win) {
        if (win == false ) ++gamesPlayed;
        else ++gamesWon;
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

    public static void ennemyUpdateStats(String firstName, String lastName, boolean win) {
        if (win == false) ++ennemyGamesPlayed;
        else ++ennemyGamesWon;
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
