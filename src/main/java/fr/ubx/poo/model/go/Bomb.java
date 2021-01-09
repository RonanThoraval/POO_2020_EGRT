package fr.ubx.poo.model.go;

import java.util.ArrayList;
import java.util.List;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

public class Bomb extends GameObject {
	long start; //Timer for when the bomb was created
	int state; //State changes every second until bomb explodes
	boolean exploded=false;
	int range;
	//states if the bomb was already created in the game ( if not, it has to be created ).
	boolean created=false;
	
	public Bomb(Game game, Position position, long start, int range) {
		super(game,position);
		this.state=0;
		this.start=start;
		this.range=range;
	}
	
	/**
	 * 
	 * @return the progress of the bomb explosion
	 * Getter for state.
	 */
	public int getState() {
		return state;
	}
	
	/**
	 * 
	 * @param state
	 * Getter for state.
	 */
	public void setState(int state) {
		this.state=state;
	}
	
	/**
	 * 
	 * @return the moment when the bomb has started to explode
	 */
	public long getStart() {
		return start;
	}
	
	public String toString() {
		return "Bomb";
	}
	
	/**
	 * 
	 * @return true if the bomb has exploded, false else
	 */
	public boolean exploded() {
		return exploded;
	}
	
	/**
	 * 
	 * @return the range of the bomb
	 */
	public int getRange() {
		return range;
	}
	/**
	 * 
	 * Sets the creation of the bomb
	 */
	public void setHasBeenCreated() {
		created=true;
	}
	
	/**
	 * 
	 * @return True if the bomb has already been created in the game.
	 */
	public boolean hasBeenCreated() {
		return created;
	}
	
	
	/**
	 * 
	 * @param now
	 * Changes the state of the bomb every second until it explodes
	 */
	public void update(long now) {
		if(now-  start>=5*Math.pow(10,9)) {
			exploded=true;
    		state=5;
    	} else if(now- start>=4*Math.pow(10,9)) {
    		state=4;
    	} else if( now- start>=3*Math.pow(10,9)) {
    		state=3;
    	} else if(now- start>=2*Math.pow(10,9)) {
    		state=2;
    	} else if(now- start>=1*Math.pow(10,9)) {
    		state=1;
    	}
	}
	
	
	/**
	 * 
	 * @param range, the range of bombs
	 * @param level, the level where the player is
	 * @return the list of all the possible cases where the explosion can be
	 */
	public List<Position> positionsAroundBomb(int range,int level) {
		List<Position> l=new ArrayList<>();
		int y=getPosition().y;
		int x=getPosition().x;
		for(int i=1; i<=range; i++) {
			Position p1=new Position(x,y-i);
			Position p2=new Position(x,y+i);
			Position p3=new Position(x-i,y);
			Position p4=new Position(x+i,y);
			
			if (p1.inside(this.game.getWorld(level).dimension)) {
				l.add(p1);
			}
			if (p2.inside(this.game.getWorld(level).dimension)) {
				l.add(p2);
			}
			if (p3.inside(this.game.getWorld(level).dimension)) {
				l.add(p3);
			}
			if (p4.inside(this.game.getWorld(level).dimension)) {
				l.add(p4);
			}
		}
		l.add(getPosition());
		return l;
	}

	
}
