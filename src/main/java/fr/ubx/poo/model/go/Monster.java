package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

public class Monster extends GameObject {
	//private Direction direction;
	
	
	
	public Monster(Game game, Position position ) {
		super(game,position);
	}
	
/*	public Direction getDirection() {
		return direction;
	} */
	
	public String toString() {
		return "Monster";
	}

}
