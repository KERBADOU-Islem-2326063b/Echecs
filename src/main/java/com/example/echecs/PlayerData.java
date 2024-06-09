package com.example.echecs;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe représente les données des joueurs d'un tournoi à élimination simple.
 */
public class PlayerData {
    private List<String> players; // Liste des noms des joueurs
    private int currentMatchIndex; // Indice du match actuel
    private List<Match> matches; // Liste des matchs du tournoi
    private List<String> winners; // Liste des gagnants des matchs

    /**
     * Constructeur de la classe PlayerData.
     *
     * @param players La liste des noms des joueurs participant au tournoi.
     */
    public PlayerData(List<String> players) {
        this.players = players;
        this.currentMatchIndex = 0;
        this.matches = new ArrayList<>();
        this.winners = new ArrayList<>();

        // Générer les matchs pour un tournoi à élimination simple
        generateMatches();
    }

    /**
     * Génère les matchs du tournoi à élimination simple.
     */
    private void generateMatches() {
        int numPlayers = players.size();
        List<String> remainingPlayers = new ArrayList<>(players);

        // Match de qualification pour G/H
        Match ghQualification = new Match(remainingPlayers.get(6), remainingPlayers.get(7));
        matches.add(ghQualification);
        remainingPlayers.remove(7); // Supprimer le joueur H
        remainingPlayers.remove(6); // Supprimer le joueur G

        // Match de qualification pour I/J
        Match ijQualification = new Match(remainingPlayers.get(6), remainingPlayers.get(7));
        matches.add(ijQualification);
        remainingPlayers.remove(7); // Supprimer le joueur J
        remainingPlayers.remove(6); // Supprimer le joueur I

        // Ajouter les matchs du premier tour
        for (int i = 0; i < remainingPlayers.size(); i += 2) {
            if (i + 1 < remainingPlayers.size()) {
                matches.add(new Match(remainingPlayers.get(i), remainingPlayers.get(i + 1)));
            }
        }
    }

    /**
     * Récupère le match actuel.
     *
     * @return Le match actuel.
     */
    public Match getCurrentMatch() {
        if (currentMatchIndex < matches.size()) {
            return matches.get(currentMatchIndex);
        }
        return null;
    }

    /**
     * Enregistre le résultat d'un match.
     *
     * @param winner Le nom du joueur gagnant.
     */
    public void recordMatchResult(String winner) {
        Match currentMatch = getCurrentMatch();
        if (currentMatch != null) {
            currentMatch.setWinner(winner);
            winners.add(winner);
            currentMatchIndex++;
            if (currentMatchIndex % (matches.size() / 2) == 0) {
                advanceWinnersToNextRound();
            }
        }
    }

    /**
     * Fait avancer les gagnants vers le prochain tour.
     */
    private void advanceWinnersToNextRound() {
        List<String> nextRoundPlayers = new ArrayList<>(winners);
        winners.clear();
        for (int i = 0; i < nextRoundPlayers.size(); i += 2) {
            if (i + 1 < nextRoundPlayers.size()) {
                matches.add(new Match(nextRoundPlayers.get(i), nextRoundPlayers.get(i + 1)));
            } else {
                winners.add(nextRoundPlayers.get(i));
            }
        }
    }

    /**
     * Vérifie si le tournoi est terminé.
     *
     * @return true si le tournoi est terminé, sinon false.
     */
    public boolean isTournamentOver() {
        return currentMatchIndex >= matches.size();
    }

    /**
     * Récupère le nom du vainqueur du tournoi.
     *
     * @return Le nom du vainqueur du tournoi.
     */
    public String getWinner() {
        if (isTournamentOver() && !winners.isEmpty()) {
            return winners.get(0);
        }
        return null;
    }

    /**
     * Classe interne représentant un match.
     */
    public static class Match {
        private String player1; // Joueur 1
        private String player2; // Joueur 2
        private String winner; // Le nom du gagnant

        /**
         * Constructeur de la classe Match.
         *
         * @param player1 Le nom du premier joueur.
         * @param player2 Le nom du deuxième joueur.
         */
        public Match(String player1, String player2) {
            this.player1 = player1;
            this.player2 = player2;
            this.winner = null;
        }

        /**
         * Récupère le nom du premier joueur.
         *
         * @return Le nom du premier joueur.
         */
        public String getPlayer1() {
            return player1;
        }

        /**
         * Récupère le nom du deuxième joueur.
         *
         * @return Le nom du deuxième joueur.
         */
        public String getPlayer2() {
            return player2;
        }

        /**
         * Récupère le nom du gagnant du match.
         *
         * @return Le nom du gagnant du match.
         */
        public String getWinner() {
            return winner;
        }

        /**
         * Définit le nom du gagnant du match.
         *
         * @param winner Le nom du gagnant du match.
         */
        public void setWinner(String winner) {
            this.winner = winner;
        }
    }
}

