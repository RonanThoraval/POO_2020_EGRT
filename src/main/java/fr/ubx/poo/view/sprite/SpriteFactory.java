/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import static fr.ubx.poo.view.image.ImageResource.*;


import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Box;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.DoorClosed;
import fr.ubx.poo.model.decor.DoorOpen;
import fr.ubx.poo.model.decor.Stone;
import fr.ubx.poo.model.decor.Tree;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.Explosion;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.decor.Heart;
import fr.ubx.poo.model.decor.Key;
import fr.ubx.poo.model.decor.NbBombMoins;
import fr.ubx.poo.model.decor.NbBombPlus;
import fr.ubx.poo.model.decor.RangeBombMoins;
import fr.ubx.poo.model.decor.RangeBombPlus;
import fr.ubx.poo.model.go.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.decor.Princess;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;


public final class SpriteFactory {

    public static Sprite createDecor(Pane layer, Position position, Decor decor) {
        ImageFactory factory = ImageFactory.getInstance();
        if (decor instanceof Stone)
            return new SpriteDecor(layer, factory.get(STONE), position);
        if (decor instanceof Tree)
            return new SpriteDecor(layer, factory.get(TREE), position);
        if (decor instanceof Box)
        	return new SpriteDecor(layer,factory.get(BOX),position);
        if (decor instanceof Heart)
        	return new SpriteDecor(layer,factory.get(HEART),position);
        if (decor instanceof Key)
        	return new SpriteDecor(layer,factory.get(KEY),position);
        if (decor instanceof NbBombPlus)
            return new SpriteDecor(layer, factory.get(NBBOMBPLUS), position);
        if (decor instanceof NbBombMoins)
            return new SpriteDecor(layer, factory.get(NBBOMBMOINS), position);
        if (decor instanceof RangeBombPlus)
            return new SpriteDecor(layer, factory.get(RANGEBOMBPLUS), position);
        if (decor instanceof RangeBombMoins)
            return new SpriteDecor(layer, factory.get(RANGEBOMBMOINS), position);
        if (decor instanceof DoorOpen)
            return new SpriteDecor(layer, factory.get(DOOROPEN), position);
        if (decor instanceof DoorClosed)
            return new SpriteDecor(layer, factory.get(DOORCLOSED), position);
        if (decor instanceof Princess)
        	return new SpriteDecor(layer,factory.get(PRINCESS),position);
        return null;
    }

    public static Sprite createPlayer(Pane layer, Player player) {
        return new SpritePlayer(layer, player);
    }
    
    public static SpriteBomb createBomb(Pane layer, Bomb bomb) {
    	return new SpriteBomb(layer, bomb);
    }
    
    public static SpriteExplosion createExplosion(Pane layer, Explosion explosion) {
    	return new SpriteExplosion(layer,explosion);
    }
    
    public static SpriteMonster createMonster(Pane layer, Monster monster) {
    	ImageFactory factory= ImageFactory.getInstance();
    	return new SpriteMonster(layer,monster);
    }
    
}
