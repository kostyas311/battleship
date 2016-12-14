package ru.ksemenov.battleship.field;

import ru.ksemenov.battleship.ResourceLoader;

import java.awt.*;

/**
 * Created by ksemenov on 27.11.16.
 */
public class Cell {
    public final static byte CELL_STATE_EMPTY = 0;
    public final static byte CELL_STATE_DESTROYED = 1;
    public final static byte CELL_STATE_ACTIVE = 2;

    private int size;
    private int x;
    private int y;
    private int row;
    private int column;
    private byte state;
    private boolean focused;
    private boolean ship;
    private ResourceLoader resourceLoader;

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isShip() {
        return ship;
    }

    public void setShip(boolean ship) {
        this.ship = ship;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public Cell(int row, int column, int size) {
        this.size = size;
        this.state = CELL_STATE_EMPTY;
        this.row = row;
        this.column = column;
        this.resourceLoader = ResourceLoader.getInstance();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void draw(Graphics g, boolean isUserField) {

        ((Graphics2D)g).setStroke(new BasicStroke(2));

        switch (state) {
            case CELL_STATE_ACTIVE:
                if (isUserField && isShip()) {
                    g.drawImage(resourceLoader.getShipImage(), x, y, size, size, null);
                }
                break;

            case Cell.CELL_STATE_DESTROYED:
                if (isShip()) {
                    g.drawImage(resourceLoader.getShipDestroyedImage(), x, y, size, size, null);
                }else {
                    g.drawImage(resourceLoader.getEmptyCellImage(), x, y, size, size, null);
                }
                break;
        }

        if (isFocused()) {
            drawCellFocusRect(g);
        }
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    private void drawCellFocusRect(Graphics graphics) {
        Color color = graphics.getColor();
        graphics.setColor(Color.BLUE);
        Stroke stroke = ((Graphics2D) graphics).getStroke();
        ((Graphics2D) graphics).setStroke(new BasicStroke(3));
        graphics.drawRect(x, y, size-2, size-2);
        ((Graphics2D) graphics).setStroke(stroke);
        graphics.setColor(color);
    }

    @Override
    public String toString() {
        return "Cell at (" + x + " ; " + y + ")";
    }

    public void destroy() {
        if (state != CELL_STATE_DESTROYED){
            state = CELL_STATE_DESTROYED;
        }
    }
}
