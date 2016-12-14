package ru.ksemenov.battleship.field;

import ru.ksemenov.battleship.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Created by ksemenov on 04.12.16.
 */
class Ship {
    private int x;
    private int y;
    private int length;
    private int direction;

    public Ship(int x, int y, int length, int direction) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "destroyed Ship{" +
                "x=" + x +
                ", y=" + y +
                ", length=" + length +
                '}';
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLength() {
        return length;
    }

    public int getRotation() {
        return direction;
    }

    public boolean isDestroyed(Cell[][] cells) {
        if (direction == 0) {
            for (int i = 0; i < length; i++) {
                if (cells[x][y + i].getState() != Cell.CELL_STATE_DESTROYED)
                    return false;
            }
            return true;
        } else {
            for (int i = 0; i < length; i++) {
                if (cells[x + i][y].getState() != Cell.CELL_STATE_DESTROYED)
                    return false;
            }
            return true;
        }
    }

    public boolean containsPoint(int x, int y) {
        if (direction == 0) {
            for (int i = this.y; i < this.y + length; i++) {
                if (this.x == x && i == y)
                    return true;
            }
            return false;
        } else {
            for (int i = this.x; i < this.x + length; i++) {
                if (i == x && this.y == y)
                    return true;
            }
            return false;
        }
    }

    public int getHealth(Cell[][] cells){
        int health = length;
        if (this.direction == 0){
            for (int i = 0; i < length; i++)
                if (cells[this.x][this.y + i].getState() == Cell.CELL_STATE_DESTROYED)
                    health--;
        }else {
            for (int i = 0; i < length; i++)
                if (cells[this.x + i][this.y].getState() == Cell.CELL_STATE_DESTROYED)
                    health--;
        }

        return health;
    }
}

public class BattleFieldModel{

    private Cell[][] cells;
    private int fieldSize = R.BATTLE_FIELD_CELL_COUNT;
    private List<Ship> shipList;
    private BattleFieldController battleFieldController;
    private Random random;

    public BattleFieldModel(BattleFieldController battleFieldController) {
        initCells();
        this.battleFieldController = battleFieldController;
        this.shipList = new LinkedList<>();
        this.random = new Random();
    }

    private void initCells() {
        cells = new Cell[fieldSize][fieldSize];
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                cells[i][j] = new Cell(i, j, R.CELL_SIZE_PIXEL);
            }
        }
    }

    private Ship getShipAt(int x, int y) {
        if (cells[x][y].isShip()) {
            for (Ship ship : shipList) {
                if (ship.containsPoint(x, y)) {
                    return ship;
                }
            }
        }
        return null;
    }

    public Cell getInjured(){
        for (Ship ship: shipList){
            int health = ship.getHealth(cells);
            if (health > 0 && health < ship.getLength()){
                return cells[ship.getX()][ship.getY()];
            }
        }
        return null;
    }

    public void destroy(Cell cell) {
        cell.setState(Cell.CELL_STATE_DESTROYED);
        Ship ship = getShipAt(cell.getRow(), cell.getColumn());
        if (ship != null) {
            if (ship.isDestroyed(this.cells)) {
                updateNeightbourCells(ship.getX(), ship.getY(), ship.getRotation(), ship.getLength(), Cell.CELL_STATE_DESTROYED);
            }
        }

    }

    public Cell[][] getCells() {
        return cells;
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public void clear() {
        shipList.clear();
        forEach(cell -> {
            cell.setState(Cell.CELL_STATE_EMPTY);
            cell.setShip(false);
        });
    }

    public void generate() {
        clear();

        createShip(4);
        createShip(3);
        createShip(3);
        createShip(2);
        createShip(2);
        createShip(2);
        createShip(1);
        createShip(1);
        createShip(1);
        createShip(1);
    }

    private void createShip(int length) {
        int x, y;
        while (true) {
            x = random.nextInt(fieldSize);
            y = random.nextInt(fieldSize);

            if (cells[x][y].getState() == Cell.CELL_STATE_ACTIVE) {
                continue;
            }

            int direction = random.nextInt(2);
            if (checkLine(x, y, direction, length)) {
                for (int k = 0; k < length; k++) {
                    if (direction == 0) {
                        cells[x][y + k].setShip(true);
                        cells[x][y + k].setState(Cell.CELL_STATE_ACTIVE);
                    } else {
                        cells[x + k][y].setShip(true);
                        cells[x + k][y].setState(Cell.CELL_STATE_ACTIVE);
                    }
                }
                updateNeightbourCells(x, y, direction, length, Cell.CELL_STATE_ACTIVE);
                shipList.add(new Ship(x, y, length, direction));
                return;
            }
        }
    }

    private void updateNeightbourCells(int x, int y, int direction, int length, byte state) {
        if (direction == 0) {
            for (int i = 0; i < length; i++) {
                if (x - 1 >= 0) {
                    cells[x - 1][y + i].setState(state);
                }

                if (x + 1 < fieldSize) {
                    cells[x + 1][y + i].setState(state);
                }
            }

            for (int i = -1; i < 2; i++) {
                if (x + i >= 0 && x + i < fieldSize) {
                    if (y - 1 >= 0) {
                        cells[x + i][y - 1].setState(state);
                    }

                    if (y + length < fieldSize) {
                        cells[x + i][y + length].setState(state);
                    }
                }
            }

        } else {
            for (int i = 0; i < length; i++) {
                if (y - 1 >= 0) {
                    cells[x + i][y - 1].setState(state);
                }

                if (y + 1 < fieldSize) {
                    cells[x + i][y + 1].setState(state);
                }
            }

            for (int i = -1; i < 2; i++) {
                if (y + i >= 0 && y + i < fieldSize) {
                    if (x - 1 >= 0) {
                        cells[x - 1][y + i].setState(state);
                    }

                    if (x + length < fieldSize) {
                        cells[x + length][y + i].setState(state);
                    }
                }
            }
        }
    }

    private boolean checkLine(int x, int y, int direction, int length) {
        if (direction == 0) {
            if (y + length <= fieldSize) {
                for (int i = 0; i < length; i++) {
                    if (cells[x][y + i].getState() == Cell.CELL_STATE_ACTIVE)
                        return false;
                }
                return true;
            }
        } else {
            if (x + length <= fieldSize) {
                for (int i = 0; i < length; i++) {
                    if (cells[x + i][y].getState() == Cell.CELL_STATE_ACTIVE)
                        return false;
                }
                return true;
            }
        }

        return false;
    }

    public void forEach(Consumer<Cell> consumer) {
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                consumer.accept(cells[i][j]);
            }
        }
    }

    public boolean isAllShipsDestroyed() {
        int count = 0;
        for (Ship ship : shipList) {
            if (ship.isDestroyed(this.cells)) {
                count++;
            }
        }
        return count == 10;
    }
}
