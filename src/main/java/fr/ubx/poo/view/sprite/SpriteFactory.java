/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import static fr.ubx.poo.view.image.ImageResource.*;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Box;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Stone;
import fr.ubx.poo.model.decor.Tree;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.Door;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.Heart;
import fr.ubx.poo.model.go.Key;
import fr.ubx.poo.model.go.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.character.Princess;
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
        return null;
    }

    public static Sprite createPlayer(Pane layer, Player player) {
        return new SpritePlayer(layer, player);
    }
    
    public static Sprite createGameObject(Pane layer, GameObject go) {
    	ImageFactory factory= ImageFactory.getInstance();
    	if (go instanceof Heart )
    		return new SpriteGameObject(layer,factory.get(HEART),go);
    	if (go instanceof Key )
    		return new SpriteGameObject(layer,factory.get(KEY),go);
    	if (go instanceof Monster )
    		return new SpriteGameObject(layer,factory.get(MONSTER),go);
    	if (go instanceof Princess )
    		return new SpriteGameObject(layer,factory.get(PRINCESS),go);
    	if (go instanceof Bomb )
    		return new SpriteGameObject(layer,factory.get(BOMB),go);
    	if (go instanceof Door )
    		return new SpriteGameObject(layer,factory.get(DOOR),go);
    	return null;
    }
    
}
