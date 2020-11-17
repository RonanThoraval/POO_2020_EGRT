package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

public class Door extends GameObject {
	public Door(Game game, Position position ) {
		super(game,position);
	}
	
	public String toString() {
		return "Door";
	}
}
