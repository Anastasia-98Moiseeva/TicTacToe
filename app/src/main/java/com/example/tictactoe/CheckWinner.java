package com.example.tictactoe;

import android.util.Pair;

import java.util.ArrayList;

import static com.example.tictactoe.Symbol.CROSS;
import static com.example.tictactoe.Symbol.CIRCLE;

public class CheckWinner {

    int tableSize = 3;

    public boolean existWinner = false;
    private Symbol winner = null;
    private int winIndent = 6;

    Symbol[][] table = new Symbol[tableSize][tableSize];

    ArrayList<Pair<Integer, Integer>> winnerPairs = new ArrayList<>();

    public Symbol tryToFindWinner(){
        Symbol winner = null;
        for (int i = 0; i < tableSize; i++){
            if(table[i][0] == CROSS && table[i][1] == CROSS && table[i][2] == CROSS){
                winner = CROSS;
                winnerPairs.add(new Pair<>(i, 0));
                winnerPairs.add(new Pair<>(i, 1));
                winnerPairs.add(new Pair<>(i, 2));
            }
            if(table[i][0] == CIRCLE && table[i][1] == CIRCLE && table[i][2] == CIRCLE){
                winner = CIRCLE;
                winnerPairs.add(new Pair<>(i, 0));
                winnerPairs.add(new Pair<>(i, 1));
                winnerPairs.add(new Pair<>(i, 2));
            }

            if(table[0][i] == CROSS && table[1][i] == CROSS && table[2][i] == CROSS){
                winner = CROSS;
                winnerPairs.add(new Pair<>(0, i));
                winnerPairs.add(new Pair<>(1, i));
                winnerPairs.add(new Pair<>(2, i));
            }
            if(table[0][i] == CIRCLE && table[1][i] == CIRCLE && table[2][i] == CIRCLE){
                winner = CIRCLE;
                winnerPairs.add(new Pair<>(0, i));
                winnerPairs.add(new Pair<>(1, i));
                winnerPairs.add(new Pair<>(2, i));
            }
        }

        if(table[0][0] == CROSS && table[1][1] == CROSS && table[2][2] == CROSS){
            winner = CROSS;
            winnerPairs.add(new Pair<>(0, 0));
            winnerPairs.add(new Pair<>(1, 1));
            winnerPairs.add(new Pair<>(2, 2));
        }
        if(table[0][0] == CIRCLE && table[1][1] == CIRCLE && table[2][2] == CIRCLE){
            winner = CIRCLE;
            winnerPairs.add(new Pair<>(0, 0));
            winnerPairs.add(new Pair<>(1, 1));
            winnerPairs.add(new Pair<>(2, 2));
        }

        if(table[2][0] == CROSS && table[1][1] == CROSS && table[0][2] == CROSS){
            winner = CROSS;
            winnerPairs.add(new Pair<>(2, 0));
            winnerPairs.add(new Pair<>(1, 1));
            winnerPairs.add(new Pair<>(0, 2));
        }
        if(table[2][0] == CIRCLE && table[1][1] == CIRCLE && table[0][2] == CIRCLE){
            winner = CIRCLE;
            winnerPairs.add(new Pair<>(2, 0));
            winnerPairs.add(new Pair<>(1, 1));
            winnerPairs.add(new Pair<>(0, 2));
        }
        return winner;
    }

    public boolean getIsWinner() {
        return existWinner;
    }

    public void setExistWinner(boolean existWinner) {
        this.existWinner = existWinner;
    }

    public Symbol getWinner() {
        return winner;
    }

    public void setWinner(Symbol mWinner) {
        this.winner = mWinner;
    }

    public int getWinIndent() {
        return winIndent;
    }
}
