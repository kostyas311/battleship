package ru.ksemenov.battleship;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by ksemenov on 04.12.16.
 */
public class ResourceLoader {
    private BufferedImage shipImage;
    private BufferedImage shipDestroyedImage;
    private BufferedImage emptyCellImage;
    private BufferedImage battleFieldImage;
    private static ResourceLoader instance = null;

    private ResourceLoader() {
        try{
            shipImage = ImageIO.read(new File("resources/ship.png"));
            shipDestroyedImage = ImageIO.read(new File("resources/killed.png"));
            emptyCellImage = ImageIO.read(new File("resources/empty.png"));
            battleFieldImage = ImageIO.read(new File("resources/battlefield.png"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ResourceLoader getInstance(){
        if (instance == null){
            instance = new ResourceLoader();
        }

        return instance;
    }

    public BufferedImage getShipImage() {
        return shipImage;
    }

    public BufferedImage getShipDestroyedImage() {
        return shipDestroyedImage;
    }

    public BufferedImage getEmptyCellImage() {
        return emptyCellImage;
    }

    public BufferedImage getBattleFieldImage() {
        return battleFieldImage;
    }
}
