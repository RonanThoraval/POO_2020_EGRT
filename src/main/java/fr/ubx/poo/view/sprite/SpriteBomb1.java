package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;

public class SpriteBomb1 extends SpriteGameObject {
	
	public SpriteBomb1(Pane layer, Bomb bomb) {
        super(layer, null, bomb);
        updateImage();
        
    }
	

    @Override
    public void updateImage() {
       // Monster monster = (Monster) go;
       // setImage(ImageFactory.getInstance().getMonster());
    }

}