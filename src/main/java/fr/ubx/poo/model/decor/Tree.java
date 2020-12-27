/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class Tree extends Decor {
    @Override
    public String toString() {
        return "Tree";
    }

	@Override
	public boolean canPlayerGo(Player player) {
		return false;
	}

	@Override
	public void doPlayerGo(Player player) {}
}
