package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;

public class Explosion extends GameObject {
	long start;
	boolean explosed=false;
	
	public Explosion(Game game, Position position, long start) {
		super(game,position);
		this.start=start;
	}
	
	public long getStart() {
		return start;
	}
	
	public String toString() {
		return "Explosion";
	}
	
	public boolean getExplosed() {
		return explosed;
	}
	
	public void update(long now) {
		if(now-  start>=1*Math.pow(10,9)) { //une seconde
			explosed=true;
		}
	}
		
}
