package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

public class Heart extends GameObject{
	public Heart(Game game, Position position ) {
		super(game,position);
	}
	
	public String toString() {
		return "Heart";
	}
}
