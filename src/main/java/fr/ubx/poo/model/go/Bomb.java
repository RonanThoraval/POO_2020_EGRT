package fr.ubx.poo.model.go;

import java.util.ArrayList;
import java.util.List;

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
	
	public boolean hasNorth() {
		return (getPosition().y!=0);
	}
	
	public boolean hasSouth() {
		return (getPosition().y!=(game.getWorld().dimension.height)-1);
	}
	
	public boolean hasWest() {
		return (getPosition().x!=0);
	}
	
	public boolean hasEast() {
		return (getPosition().x!=(game.getWorld().dimension.width)-1);
	}
	
	
	public List<Position> positionsAroundBomb(int range) {
		List<Position> l=new ArrayList<>();
		int y=getPosition().y;
		int x=getPosition().x;
		for(int i=0; i<=range; i++) {
			if (hasNorth()) {
				Position p1=new Position(x,y-i);
				l.add(p1);
			}
			if (hasSouth()) {
				Position p2=new Position(x,y+i);
				l.add(p2);
			}
			if (hasWest()) {
				Position p3=new Position(x-i,y);
				l.add(p3);
			}
			if (hasEast()) {
				Position p4=new Position(x+i,y);
				l.add(p4);
			}
		}
		return l;
	}

	
}
