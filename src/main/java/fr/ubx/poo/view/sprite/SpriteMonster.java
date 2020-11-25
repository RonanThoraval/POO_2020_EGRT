package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;

public class SpriteMonster extends SpriteGameObject {
	
	public SpriteMonster(Pane layer, Monster monster) {
        super(layer, null, monster);
        updateImage();
        
    }
	

    @Override
    public void updateImage() {
        Monster monster = (Monster) go;
        setImage(ImageFactory.getInstance().getMonster(monster.getDirection()));
    }
    
    public Monster getMonster() {
    	Monster m=(Monster) go;
    	return m;
    }

}
