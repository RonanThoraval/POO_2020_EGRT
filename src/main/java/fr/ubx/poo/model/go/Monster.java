package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

public class Monster extends GameObject {
	public Monster(Game game, Position position ) {
		super(game,position);
	}
	
	public String toString() {
		return "Monster";
	}

}
