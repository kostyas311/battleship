package ru.ksemenov.battleship.field;

import ru.ksemenov.battleship.R;
import ru.ksemenov.battleship.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by ksemenov on 04.12.16.
 */

public class BattleFieldView extends JPanel {

    private final static int MARGIN_LEFT = 32;
    private final static int MARGIN_TOP = 29;

    private BattleFieldController battleFieldController;
    private BufferedImage bufferedImage;
    private ResourceLoader resourceLoader;

    public BattleFieldView(BattleFieldController battleFieldController) {
        this.resourceLoader = ResourceLoader.getInstance();
        this.battleFieldController = battleFieldController;
        this.bufferedImage = new BufferedImage(366, 362, BufferedImage.TYPE_INT_RGB);
        this.addMouseMotionListener(battleFieldController);
        this.addMouseListener(battleFieldController);
        setSize(R.BATTLE_FIELD_WIDTH, R.BATTLE_FIELD_HEIGHT);
    }

    public void update(BattleFieldModel model) {
        Cell[][] cells = model.getCells();

        drawBackground();

        int dx, dy = MARGIN_TOP;
        for (int i = 0; i < model.getFieldSize(); i++) {
            dx = MARGIN_LEFT;
            for (int j = 0; j < model.getFieldSize(); j++) {
                cells[i][j].setX(dx);
                cells[i][j].setY(dy);
                drawCell(bufferedImage.getGraphics(), cells[i][j]);
                dx += R.CELL_SIZE_PIXEL;
            }
            dy += R.CELL_SIZE_PIXEL;
        }

        repaint();
    }

    private void drawBackground() {
        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
        g.drawImage(resourceLoader.getBattleFieldImage(), 0, 0, null);
    }

    private void drawCell(Graphics graphics, Cell cell){
        cell.draw(graphics, battleFieldController.getPlayer().isProtectedView());
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(bufferedImage, 0, 0, null);
    }

    public Cell getCellAt(BattleFieldModel model, int x, int y) {
        Cell[][] cells = model.getCells();
        for (int i = 0; i < model.getFieldSize(); i++) {
            for (int j = 0; j < model.getFieldSize(); j++) {
                if (x >= cells[i][j].getX() && x < cells[i][j].getX() + R.CELL_SIZE_PIXEL && y >= cells[i][j].getY() && y < cells[i][j].getY() + R.CELL_SIZE_PIXEL) {
                    return cells[i][j];
                }
            }
        }
        return null;
    }
}
