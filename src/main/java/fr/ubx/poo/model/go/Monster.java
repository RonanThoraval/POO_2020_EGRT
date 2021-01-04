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
	
	public Direction getDirection() {
		return direction;
	}
	
	/**
	 *@return true if the monster can go towards direction passed in parameters, false else
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
	
	public void setDeath() {
		alive=false;
	}
	
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
		if((now-start)>=(2-1.5*this.game.getCurrentLevel()/(this.game.getNbLevels()-1))*Math.pow(10,9)) {
			if(Math.random() > (this.game.getCurrentLevel())/(this.game.getNbLevels()-1)) {
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
					direction = Direction.random();
				}
				triedN=false;
				triedS=false;
				triedE=false;
				triedW=false;
			}else {
				direction = AI();
			}
			doMove(direction, now);
			start = now;
		}
    }
	
	/**
	 * 
	 * @param direction1 
	 * @param direction2
	 * @return one of the both directions, selected randomly 
	 */
	public Direction DirAl(Direction direction1, Direction direction2) {
		if (!canMove(direction1) && !canMove(direction1)) {
			Direction c = Direction.random();
			while(!canMove(c)) {
				c = Direction.random();
			}
			return c;
		}
		if (canMove(direction1) && !canMove(direction1)) {
			return direction1;
		}
		if (!canMove(direction1) && canMove(direction1)) {
			return direction1;
		}
		if(Math.random()>1/2) {
			return direction1;
		}
		return direction1;
	}
	
	public Direction AI() {
		int xp = this.game.getPlayer().getPosition().x;
		int yp = this.game.getPlayer().getPosition().y;
		int xm = getPosition().x;
		int ym = getPosition().y;
		if (xp > xm) {
			if (yp > ym) {
				return DirAl(Direction.E, Direction.S);
			}
			return DirAl(Direction.E, Direction.N);
		}
		if (yp > ym){
			return DirAl(Direction.W, Direction.S);
		}
		return DirAl(Direction.W, Direction.N);
	}

}


