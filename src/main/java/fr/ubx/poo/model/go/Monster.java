package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Box;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.DoorOpen;
import fr.ubx.poo.model.decor.Heart;
import fr.ubx.poo.model.decor.Key;
import fr.ubx.poo.model.decor.NbBombMoins;
import fr.ubx.poo.model.decor.NbBombPlus;
import fr.ubx.poo.model.decor.Princess;
import fr.ubx.poo.model.decor.RangeBombMoins;
import fr.ubx.poo.model.decor.RangeBombPlus;

public class Monster extends GameObject implements Movable {
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
	
	public String toString() {
		return "Monster";
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
		if((now-start)>=2*Math.pow(10,9)) {
			direction = Direction.random();
			while(!canMove(direction)) {
				direction = Direction.random();
			}
			doMove(direction);
			if(getPosition().equals(this.game.getPlayer().getPosition())) {
				this.game.getPlayer().decreaseLives();
			}
			start = now;
		}
    }

}


