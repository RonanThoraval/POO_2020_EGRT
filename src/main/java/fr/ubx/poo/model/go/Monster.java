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
	
	public boolean canMove(Direction direction) {
    	Position newPos=direction.nextPosition(getPosition());
    	return (newPos.inside(this.game.getWorld().dimension) && (this.game.getWorld().get(newPos)==null));
	}
	
	
	public boolean isAlive() {
		return alive;
	}
	
	public void setDeath() {
		alive=false;
	}
	
	public void doMove(Direction direction) {
		if (canMove(direction) ) {
	    	Position nextPos = direction.nextPosition(getPosition());
	        setPosition(nextPos);
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
			}else {
				direction = AI();
			}
			doMove(direction);
			start = now;
		}
    }
	
	public Direction DirAl(Direction a, Direction b) {
		if (!canMove(a) && !canMove(b)) {
			Direction c = Direction.random();
			while(!canMove(c)) {
				c = Direction.random();
			}
			return c;
		}
		if (canMove(a) && !canMove(b)) {
			return a;
		}
		if (!canMove(a) && canMove(b)) {
			return b;
		}
		if(Math.random()>1/2) {
			return a;
		}
		return b;
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


