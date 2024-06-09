package com.example.echecs;

import java.util.ArrayList;
import java.util.List;

public class Tournament {
    private List<String> players;
    private int currentMatchIndex;
    private List<Match> matches;
    private List<String> winners;

    public Tournament(List<String> players) {
        this.players = players;
        this.currentMatchIndex = 0;
        this.matches = new ArrayList<>();
        this.winners = new ArrayList<>();

        // Générer les matchs pour un tournoi à élimination simple
        generateMatches();
    }


    private void generateMatches() {
        int numPlayers = players.size();
        List<String> remainingPlayers = new ArrayList<>(players);

        // Match de qualification pour G/H
        Match ghQualification = new Match(remainingPlayers.get(6), remainingPlayers.get(7));
        matches.add(ghQualification);
        remainingPlayers.remove(7); // Remove player H
        remainingPlayers.remove(6); // Remove player G

        // Match de qualification pour I/J
        Match ijQualification = new Match(remainingPlayers.get(6), remainingPlayers.get(7));
        matches.add(ijQualification);
        remainingPlayers.remove(7); // Remove player J
        remainingPlayers.remove(6); // Remove player I

        // Ajouter les matchs du premier tour
        for (int i = 0; i < remainingPlayers.size(); i += 2) {
            if (i + 1 < remainingPlayers.size()) {
                matches.add(new Match(remainingPlayers.get(i), remainingPlayers.get(i + 1)));
            }
        }
    }

    public Match getCurrentMatch() {
        if (currentMatchIndex < matches.size()) {
            return matches.get(currentMatchIndex);
        }
        return null;
    }

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

    public boolean isTournamentOver() {
        return currentMatchIndex >= matches.size();
    }

    public String getWinner() {
        if (isTournamentOver() && !winners.isEmpty()) {
            return winners.get(0);
        }
        return null;
    }

    public static class Match {
        private String player1;
        private String player2;
        private String winner;

        public Match(String player1, String player2) {
            this.player1 = player1;
            this.player2 = player2;
            this.winner = null;
        }

        public String getPlayer1() {
            return player1;
        }

        public String getPlayer2() {
            return player2;
        }

        public String getWinner() {
            return winner;
        }

        public void setWinner(String winner) {
            this.winner = winner;
        }
    }
}
