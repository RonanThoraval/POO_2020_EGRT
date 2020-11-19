package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.Bomb;

public class Explosion extends Bomb {

	public Explosion(Game game, Position position, long start) {
		super(game, position, start);
	}

}
