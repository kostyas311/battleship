package ru.ksemenov.battleship.field;

import ru.ksemenov.battleship.game.Controller;
import ru.ksemenov.battleship.game.Player;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by ksemenov on 04.12.16.
 */

public class BattleFieldController extends MouseAdapter{

    private BattleFieldView fieldView;
    private BattleFieldModel fieldModel;
    private Player player;
    private Cell currentCell;

    public BattleFieldController(Player player) {
        this.fieldView = new BattleFieldView(this);
        this.fieldModel = new BattleFieldModel(this);
        this.player = player;
    }

    public BattleFieldView getView() {
        return fieldView;
    }

    public BattleFieldModel getModel() {
        return fieldModel;
    }

    public void updateView() {
        fieldView.update(fieldModel);
    }

    public Player getPlayer() {
        return player;
    }

    public void mousePressed(MouseEvent e) {
        if (!player.isProtectedView() && !player.getGameController().isEndOfGame() && player.getGameController().isGameActive()) {
            currentCell = fieldView.getCellAt(fieldModel, e.getX(), e.getY());
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (currentCell != null && currentCell.getState() != Cell.CELL_STATE_DESTROYED) {
            fieldModel.destroy(currentCell);
            updateView();
            if (!currentCell.isShip()){
                Controller controller = player.getGameController();
                controller.attack();
            }
            currentCell = null;
        }
    }

    public void mouseMoved(MouseEvent e) {
        if (!player.isProtectedView() && !player.getGameController().isEndOfGame() && player.getGameController().isGameActive()) {
            Cell cell = fieldView.getCellAt(fieldModel, e.getX(), e.getY());
            if (cell != null) {
                fieldModel.forEach(c -> c.setFocused(false));
                cell.setFocused(true);
                updateView();
            }
        }
    }

    public void destroy(Cell cell) {
        if (cell != null){
            fieldModel.destroy(cell);
            updateView();
        }
    }
}
