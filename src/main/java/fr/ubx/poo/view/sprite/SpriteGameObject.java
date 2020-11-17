package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteGameObject extends Sprite {
    protected final GameObject go;

    public SpriteGameObject(Pane layer, Image image, GameObject go) {
        super(layer, image);
        this.go = go;
    }

    @Override
    public Position getPosition() {
        return go.getPosition();
    }
    
    @Override
    public void updateImage() {
    	
    }
    
}
