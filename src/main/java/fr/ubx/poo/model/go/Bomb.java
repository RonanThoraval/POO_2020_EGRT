package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;

public class Bomb extends GameObject {
	long start;
	int etat;
	boolean explosed=false;
	boolean created=false;
	
	public Bomb(Game game, Position position, long start) {
		super(game,position);
		this.etat=0;
		this.start=start;
	}
	
	public int getEtat() {
		return etat;
	}
	
	public void setEtat(int etat) {
		this.etat=etat;
	}
	
	public long getStart() {
		return start;
	}
	
	public String toString() {
		return "Bomb";
	}
	
	public boolean explosed() {
		return explosed;
	}
	
	public void setCreated() {
		created=true;
	}
	
	public boolean getCreated() {
		return created;
	}
	
	public void decreate() {
		created=false;
	}
	
	public void update(long now) {
		if(now-  start>=5*Math.pow(10,9)) {
			explosed=true;
    		etat=5;
    	} else if(now- start>=4*Math.pow(10,9)) {
    		etat=4;
    	} else if( now- start>=3*Math.pow(10,9)) {
    		etat=3;
    	} else if(now- start>=2*Math.pow(10,9)) {
    		etat=2;
    	} else if(now- start>=1*Math.pow(10,9)) {
    		etat=1;
    	}
	}

	
}
