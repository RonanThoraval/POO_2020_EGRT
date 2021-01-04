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

	public boolean canPlayerGo(Player player) {
		return true;
	}

	public abstract void doPlayerGo(Player player) throws IOException;

	public boolean canExplose() {
		return true;
	}

	public boolean canMonsterGo() {
		return true;
	}

	public boolean isDoor() {
		return false;
	}

}
