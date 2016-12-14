package ru.ksemenov.battleship.game;

import java.util.Observable;

/**
 * Created by ksemenov on 04.12.16.
 */
public class Controller extends Observable {
    private Player firstPLayer;
    private Player secondPLayer;
    private Player currentPlayer;
    private boolean gameActive;

    public Controller() {
        this.firstPLayer = new Player(this, "Компьютер");
        this.secondPLayer = new Player(this, "Человек", true);
        this.currentPlayer = firstPLayer;
    }

    public void changePlayer(){
        if (currentPlayer == null)
            return;

        currentPlayer = (currentPlayer == firstPLayer) ? secondPLayer : firstPLayer;
    }

    public boolean isGameActive() {
        return gameActive;
    }

    public void setGameActive(boolean gameActive) {
        this.gameActive = gameActive;
    }

    public boolean isEndOfGame(){
        boolean result = getWinner() != null;
        if (result && gameActive){
            setChanged();;
            setGameActive(false);
            notifyObservers(getWinner());
            setChanged();
        }
        return result;
    }

    public Player getFirstPLayer() {
        return firstPLayer;
    }

    public Player getSecondPLayer() {
        return secondPLayer;
    }

    public void attack() {

        if (isEndOfGame() || !gameActive){
            return;
        }

        changePlayer();
        currentPlayer.turn();
        changePlayer();
    }

    private Player getWinner(){
        if (firstPLayer.getBattleFieldController().getModel().isAllShipsDestroyed()){
            return secondPLayer;
        }else if (secondPLayer.getBattleFieldController().getModel().isAllShipsDestroyed()){
            return firstPLayer;
        }else
            return null;
    }
}
