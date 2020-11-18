package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;

public class Bomb extends GameObject {
	public Bomb(Game game, Position position ) {
		super(game,position);
	}
	
	public String toString() {
		return "Bomb";
	}
	
/*	public void explose() {
		Player p=game.getPlayer();
		int range=p.getRangeBombs();
		for (int i=)
	}*/
}
