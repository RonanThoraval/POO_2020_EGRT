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
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.decor.Heart;
import fr.ubx.poo.model.decor.Key;
import fr.ubx.poo.model.decor.NbBombMoins;
import fr.ubx.poo.model.decor.NbBombPlus;
import fr.ubx.poo.model.decor.RangeBombMoins;
import fr.ubx.poo.model.decor.RangeBombPlus;
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
        
        return null;
    }

    public static Sprite createPlayer(Pane layer, Player player) {
        return new SpritePlayer(layer, player);
    }
    
    public static Sprite createGameObject(Pane layer, GameObject go) {
    	ImageFactory factory= ImageFactory.getInstance();
    	if (go instanceof Monster )
    		return new SpriteGameObject(layer,factory.get(MONSTER),go);
    	if (go instanceof Princess )
    		return new SpriteGameObject(layer,factory.get(PRINCESS),go);
    	if (go instanceof Bomb )
    		return new SpriteGameObject(layer,factory.get(BOMB4),go);
    	return null;
    }
    
}
