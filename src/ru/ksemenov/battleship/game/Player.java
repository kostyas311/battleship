package ru.ksemenov.battleship.game;

import ru.ksemenov.battleship.field.BattleFieldController;
import ru.ksemenov.battleship.field.Cell;

import java.util.Random;

/**
 * Created by ksemenov on 04.12.16.
 */
public class Player {

    private BattleFieldController battleFieldController;
    private String name;
    private boolean protectedView;
    private Controller gameController;
    private Cell lastDestroyedCell;
    private boolean findNewShip;
    private int direction;
    private int x;
    private int y;
    private boolean needToChangeDirection;

    public Player(Controller сontroller, String name) {
        this.name = name;
        this.battleFieldController = new BattleFieldController(this);
        this.gameController = сontroller;
        this.findNewShip = true;
    }

    public Player(Controller сontroller, String name, boolean protectedView) {
        this(сontroller, name);
        this.protectedView = protectedView;
    }

    public Controller getGameController() {
        return gameController;
    }

    public BattleFieldController getBattleFieldController() {
        return battleFieldController;
    }

    public boolean isProtectedView() {
        return protectedView;
    }

    public void render() {
        battleFieldController.updateView();
    }

    public void generate() {
        battleFieldController.getModel().generate();
    }
    
    public void turn() {
        Cell[][] cells = battleFieldController.getModel().getCells();
        boolean isDestroyed = true;

        Random random = new Random();
        while (isDestroyed) {

            if (findNewShip) {
                needToChangeDirection = true;
                do {
                    x = random.nextInt(battleFieldController.getModel().getFieldSize());
                    y = random.nextInt(battleFieldController.getModel().getFieldSize());
                } while (cells[x][y].getState() == Cell.CELL_STATE_DESTROYED );

            } else {
                Cell cell = battleFieldController.getModel().getInjured();
                if (cell == null) {
                    findNewShip = true;
                    continue;
                } else {
                    if (lastDestroyedCell != null && !needToChangeDirection) {
                        if (direction == 0) {
                            if (checkPoint(x, y - 1)) y--;
                            else y++;
                        } else {
                            if (checkPoint(x - 1, y)) x--;
                            else x++;
                        }
                    } else {
                        if (checkPoint(x, y - 1)) y--;
                        else if (checkPoint(x, y + 1)) y++;
                        else if (checkPoint(x - 1, y)) x--;
                        else if (checkPoint(x + 1, y)) x++;
                        else {
                            findNewShip = true;
                            continue;
                        }
                    }

                    if (cells[x][y].getState() == Cell.CELL_STATE_DESTROYED)
                        continue;
                }
            }

            battleFieldController.destroy(cells[x][y]);
            isDestroyed = cells[x][y].isShip();

            findNewShip = battleFieldController.getModel().getInjured() == null;

            if (isDestroyed) {
                if (lastDestroyedCell != null) {
                    needToChangeDirection = false;
                    if (lastDestroyedCell.getRow() == x) {
                        direction = 0;
                    } else if (lastDestroyedCell.getColumn() == y) {
                        direction = 1;
                    }
                }

                if (!findNewShip)
                    lastDestroyedCell = cells[x][y];
                else {
                    lastDestroyedCell = null;
                }
            } else {
                if (!findNewShip) {
                    x = lastDestroyedCell.getRow();
                    y = lastDestroyedCell.getColumn();
                } else {
                    lastDestroyedCell = null;
                }
            }
        }
    }

    private boolean checkPoint(int x, int y) {
        Cell[][] cells = battleFieldController.getModel().getCells();
        try {
            return cells[x][y].getState() != Cell.CELL_STATE_DESTROYED;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }

    }
    
    @Override
    public String toString() {
        return name;
    }
}
