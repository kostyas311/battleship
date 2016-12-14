package ru.ksemenov.battleship;

import ru.ksemenov.battleship.game.Controller;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by ksemenov on 04.12.16.
 */
public class BattleShipGame extends JFrame implements Observer {

    Controller controller;
    JPanel gamePanel;
    JButton btnStartGame;

    public BattleShipGame() throws HeadlessException {

        this.controller = new Controller();
        controller.addObserver(this);

        this.gamePanel = new JPanel();
        this.gamePanel.setLayout(new GridLayout(1, 2));
        this.btnStartGame = new JButton("Начать игру");

        this.setTitle("Морской бой");
        this.setSize(R.MAIN_WINDOW_WIDTH, R.MAIN_WINDOW_HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.btnStartGame.addActionListener(e -> start());
        this.gamePanel.add(controller.getSecondPLayer().getBattleFieldController().getView());
        this.gamePanel.add(controller.getFirstPLayer().getBattleFieldController().getView());
        this.add(gamePanel, BorderLayout.CENTER);
        this.add(btnStartGame, BorderLayout.SOUTH);
        controller.getFirstPLayer().render();
        controller.getSecondPLayer().render();
        this.setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        JOptionPane.showMessageDialog(this, "Выиграл " + arg);
    }

    public void start() {
        btnStartGame.setVisible(false);
        controller.getFirstPLayer().generate();
        controller.getSecondPLayer().generate();
        controller.getFirstPLayer().render();
        controller.getSecondPLayer().render();
        controller.setGameActive(true);
        btnStartGame.setVisible(true);
    }

    public static void main(String[] args) {
        new BattleShipGame();
    }
}
