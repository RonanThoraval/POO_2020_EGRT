package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.Explosion;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;

public class SpriteExplosion extends SpriteGameObject {
	
	public SpriteExplosion(Pane layer, Explosion explosion) {
        super(layer, null, explosion);
        updateImage();
        
    }
	

    @Override
    public void updateImage() {
        setImage(ImageFactory.getInstance().getBomb(5));
    }
    

    public Bomb getBomb() {
    	Bomb b=(Bomb) go;
    	return b;
    }

}