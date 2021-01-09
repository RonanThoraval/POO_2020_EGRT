package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;

public class Monster extends GameObject implements Movable {
	private boolean triedN = false;
	private boolean triedS = false;
	private boolean triedE = false;
	private boolean triedW = false;
	private long start;
	private boolean alive = true;
	private Direction direction;
	
	
	
	public Monster(Game game, Position position, long now ) {
		super(game,position);
		this.direction = Direction.S;
		start=now;
	}
	
	/**
	 * 
	 * @return the direction of the monster
	 */
	public Direction getDirection() {
		return direction;
	}
	
	/**
	 *@return true if the monster can go towards direction given in parameters, false else
	 */
	public boolean canMove(Direction direction) {
    	Position newPos=direction.nextPosition(getPosition());
    	return (newPos.inside(this.game.getWorld().dimension)) && 
    	        (this.game.getWorld().isEmpty(newPos) || this.game.getWorld().get(newPos).canMonsterGo());	}
	
	/**
	 * 
	 * @return true if the monster is alive
	 */
	public boolean isAlive() {
		return alive;
	}
	
	/**
	 * Sets the death of the monster. RIP.
	 */
	public void setDeath() {
		alive=false;
	}
	
	/**
	 * Sets the new Monster's position and decreases player's lives if necessary
	 */
	public void doMove(Direction direction, long now) {
		if (canMove(direction) ) {
	    	Position nextPos = direction.nextPosition(getPosition());
	    	setPosition(nextPos);
	    	if (game.getPlayer().getPosition().equals(getPosition())) {
	    		game.getPlayer().decreaseLives(now);
	    	}
		}
	}
	
	public void update(long now) {
		if((now-start)>=(2-1.5*(float)(this.game.getCurrentLevel()/(this.game.getNbLevels()-1)))*Math.pow(10,9)) {
			if(Math.random() > (float) (this.game.getCurrentLevel())/(this.game.getNbLevels()-1)) {
				setAl();
			}else {
				AI();
			}
			if(canMove(direction)) {
				doMove(direction, now);
				start = now;
			}
		}
    }
	
	/**
	 * Sets a random direction for the monster
	 * If the monster is blocked, he keeps his initial direction
	 */
	private void setAl() {
		triedN=false;
		triedS=false;
		triedE=false;
		triedW=false;
		Direction initDirection = direction;
		direction = Direction.random();
		while(!canMove(direction)) {
			if(direction==Direction.N) {
				triedN=true;
			} else if(direction==Direction.S) {
				triedS=true;
			} else if(direction==Direction.E) {
				triedE=true;
			} else if(direction==Direction.W) {
				triedW=true;
			}
			if( triedN && triedS && triedE && triedW) {
				direction = initDirection;
				return;
			}
			direction=Direction.random();
		}
	}
	
	/**
	 * Sets a random direction for the monster between two directions
	 * If one of the two directions is not valid, the other is chosen
	 * If both directions are not valid, do like setAl
	 * 
	 * @param direction1 
	 * @param direction2
	 */
	public void DirAl(Direction direction1, Direction direction2) {
		if (!canMove(direction1) && !canMove(direction2)) {
			setAl();
		}else if (canMove(direction1) && !canMove(direction2)) {
			direction= direction1;
		}else if (!canMove(direction1) && canMove(direction2)) {
			direction= direction2;
		}else if(Math.random()>0.5) {
			direction = direction1;
		}else {
		direction = direction2;
		}
	}
	
	/**
	 * Sets the best monster's direction to go towards the player
	 */
	public void AI() {
		int xp = this.game.getPlayer().getPosition().x;
		int yp = this.game.getPlayer().getPosition().y;
		int xm = getPosition().x;
		int ym = getPosition().y;
		if (xp > xm) {
			if (yp > ym) {
				DirAl(Direction.E, Direction.S);
			}else {
				DirAl(Direction.E, Direction.N);
			}
		}else if (yp > ym){
			DirAl(Direction.W, Direction.S);
		}else {
			DirAl(Direction.W, Direction.N);
		}
	}

}


