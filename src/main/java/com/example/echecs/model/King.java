package com.example.echecs.model;

import java.util.ArrayList;
import java.util.List;

public class King extends ChessPiece {
    public King(String color, int columnIndex, int rowIndex) {
        super(color, columnIndex, rowIndex);
    }

    // Cette méthode copie le roi, mais elle crée actuellement un pion à la place.
    public ChessPiece copy() {
        return new Pawn(this.getColor(), this.getColumnIndex(), this.getRowIndex());
    }

    @Override
    public boolean canMove(int targetCol, int targetRow, ChessPiece[][] board) {
        int columnDifference = Math.abs(targetCol - columnIndex);
        int rowDifference = Math.abs(targetRow - rowIndex);

        // Le roi peut se déplacer d'une case dans n'importe quelle direction.
        if (columnDifference <= 1 && rowDifference <= 1) {
            ChessPiece targetPiece = board[targetRow][targetCol];
            // Vérifie si la case cible est vide ou contient une pièce ennemie.
            if (targetPiece == null || !targetPiece.getColor().equals(this.color)) {
                // Simulation du déplacement.
                ChessPiece originalPiece = board[targetRow][targetCol];
                board[targetRow][targetCol] = this;
                board[this.rowIndex][this.columnIndex] = null;

                // Vérifie si le roi serait en sécurité après ce déplacement.
                boolean isSafeMove = !isUnderAttack(targetCol, targetRow, board);

                // Annule le déplacement simulé.
                board[this.rowIndex][this.columnIndex] = this;
                board[targetRow][targetCol] = originalPiece;

                return isSafeMove;
            }
        }
        return false;
    }

    // Vérifie si le roi est en échec.
    public boolean isCheck(ChessPiece[][] board) {
        return isUnderAttack(this.columnIndex, this.rowIndex, board);
    }

    // Vérifie si le roi est en échec et mat.
    public boolean isCheckmate(ChessPiece[][] board) {
        // Si le roi n'est pas en échec, il n'est pas en échec et mat.
        if (!isCheck(board)) {
            return false;
        }

        // Si le roi peut se déplacer en sécurité, il n'est pas en échec et mat.
        if (hasSafeMove(board)) {
            return false;
        }

        // Si une pièce alliée peut capturer ou bloquer la menace, il n'est pas en échec et mat.
        if (canBlockOrCapture(board)) {
            return false;
        }

        return true;
    }

    // Vérifie si le roi a un déplacement sûr.
    private boolean hasSafeMove(ChessPiece[][] board) {
        for (int row = -1; row <= 1; row++) {
            for (int col = -1; col <= 1; col++) {
                int newRow = this.rowIndex + row;
                int newCol = this.columnIndex + col;
                if (isInBounds(newRow, newCol) && canMove(newCol, newRow, board)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Vérifie si une pièce alliée peut capturer ou bloquer la menace.
    private boolean canBlockOrCapture(ChessPiece[][] board) {
        List<ChessPiece> threateningPieces = getThreateningPieces(board);

        for (ChessPiece threateningPiece : threateningPieces) {
            List<ChessPiece> alliedPieces = getAlliedPieces(this.color, board);

            for (ChessPiece alliedPiece : alliedPieces) {
                if (canCapture(threateningPiece, alliedPiece, board)) {
                    return true;
                }
            }

            if (canBlock(threateningPiece, alliedPieces, board)) {
                return true;
            }
        }
        return false;
    }

    // Vérifie si une pièce alliée peut bloquer la menace.
    private boolean canBlock(ChessPiece threateningPiece, List<ChessPiece> alliedPieces, ChessPiece[][] board) {
        List<int[]> path = getPath(threateningPiece);

        for (int[] position : path) {
            int row = position[0];
            int col = position[1];

            for (ChessPiece alliedPiece : alliedPieces) {
                if (alliedPiece.canMove(col, row, board)) {
                    // Simule le déplacement pour vérifier s'il laisse le roi en sécurité.
                    ChessPiece originalPiece = board[row][col];
                    int originalRow = alliedPiece.getRowIndex();
                    int originalCol = alliedPiece.getColumnIndex();
                    board[row][col] = alliedPiece;
                    board[originalRow][originalCol] = null;

                    boolean isSafeMove = !isUnderAttack(this.columnIndex, this.rowIndex, board);

                    // Annule le déplacement simulé.
                    board[originalRow][originalCol] = alliedPiece;
                    board[row][col] = originalPiece;

                    if (isSafeMove) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Obtient le chemin de la pièce menaçante vers le roi.
    private List<int[]> getPath(ChessPiece threateningPiece) {
        List<int[]> path = new ArrayList<>();
        int dRow = Integer.compare(threateningPiece.getRowIndex(), this.rowIndex);
        int dCol = Integer.compare(threateningPiece.getColumnIndex(), this.columnIndex);

        int row = this.rowIndex + dRow;
        int col = this.columnIndex + dCol;

        while (row != threateningPiece.getRowIndex() || col != threateningPiece.getColumnIndex()) {
            path.add(new int[]{row, col});
            row += dRow;
            col += dCol;
        }

        return path;
    }

    // Obtient toutes les pièces alliées.
    private List<ChessPiece> getAlliedPieces(String color, ChessPiece[][] board) {
        List<ChessPiece> alliedPieces = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board[row][col];
                if (piece != null && piece.getColor().equals(color)) {
                    alliedPieces.add(piece);
                }
            }
        }
        return alliedPieces;
    }

    // Vérifie si une pièce alliée peut capturer la pièce menaçante.
    private boolean canCapture(ChessPiece threateningPiece, ChessPiece alliedPiece, ChessPiece[][] board) {
        return alliedPiece.canMove(threateningPiece.getColumnIndex(), threateningPiece.getRowIndex(), board);
    }

    // Obtient toutes les pièces ennemies menaçant le roi.
    private List<ChessPiece> getThreateningPieces(ChessPiece[][] board) {
        List<ChessPiece> threateningPieces = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board[row][col];
                if (piece != null && !piece.getColor().equals(this.color) && piece.canMove(this.columnIndex, this.rowIndex, board)) {
                    threateningPieces.add(piece);
                }
            }
        }
        return threateningPieces;
    }

    // Vérifie si une case est attaquée par une pièce ennemie.
    private boolean isUnderAttack(int targetCol, int targetRow, ChessPiece[][] board) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board[row][col];
                if (piece != null && !piece.getColor().equals(this.color) && piece.canMove(targetCol, targetRow, board)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Vérifie si une position est dans les limites du plateau.
    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
}
