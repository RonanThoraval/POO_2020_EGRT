/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import java.io.IOException;

import fr.ubx.poo.model.Entity;
import fr.ubx.poo.model.go.character.Player;

/***
 * A decor is an element that does not know its own position in the grid.
 */
public abstract class Decor extends Entity {

	/**
	 * 
	 * @param player
	 * @return A boolean stating if the player is allowed to walk on the decor.
	 */
	public boolean canPlayerGo(Player player) {
		return true;
	}

	/**
	 * 
	 * @param player
	 * @throws IOException
	 * Do the consequences of the player walking on the decor ( for example, new key, new life, etc.. )
	 */
	public abstract void doPlayerGo(Player player) throws IOException;

	/**
	 * 
	 * @return a boolean stating if this decor can explode or not
	 */
	public boolean canExplose() {
		return true;
	}

	/**
	 * 
	 * @return a boolean stating if a monster is allowed to walk on this decor.
	 */
	public boolean canMonsterGo() {
		return true;
	}

}
