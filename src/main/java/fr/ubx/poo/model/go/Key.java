package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

public class Key extends GameObject{
	public Key(Game game, Position position ) {
		super(game,position);
	}
	
	public String toString() {
		return "Key";
	}
 
}
