package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteDoor extends SpriteDecor {
	private Position position;
	int etat;

    public SpriteDoor(Pane layer, Image image, Position position,int etat) {
        super(layer, image,position);
        this.etat=etat;
    }

    @Override
    public void updateImage() {
    	setImage(ImageFactory.getInstance().getDoor(etat));
    }
}
