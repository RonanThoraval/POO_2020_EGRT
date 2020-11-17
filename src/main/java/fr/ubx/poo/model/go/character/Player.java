/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Box;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Heart;
import fr.ubx.poo.model.decor.Key;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.Monster;
import fr.ubx.poo.game.Game;

public class Player extends GameObject implements Movable {

    private final boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private int lives = 3;
    private int keys=0;
    private boolean winner;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
    }

    public int getLives() {
        return lives;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getKeys() {
    	return keys;
    }
    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    @Override
    public boolean canMove(Direction direction) {
    	Position newPos=direction.nextPosition(getPosition());
    	if (!newPos.inside(this.game.getWorld().dimension)) {
    		return false;
    	}
    	if (!this.game.getWorld().isEmpty(newPos)) {
    		if (this.game.getWorld().get(newPos) instanceof Box) {
    			Position newPos2=direction.nextPosition(newPos);
    			if (this.game.getWorld().isEmpty(newPos2) && newPos.inside(this.game.getWorld().dimension)) {
    				return true;
    			}
    			else {
    				return false;
    			}
    		} else if (this.game.getWorld().get(newPos) instanceof Heart || this.game.getWorld().get(newPos) instanceof Key ) {
    			return true ;
    		}
    		return false;
    	}
        return true;
    }

    public void doMove(Direction direction) {
    	if (canMove(direction)) {
    		Position nextPos = direction.nextPosition(getPosition());
            if (game.getWorld().get(nextPos) instanceof Box) {
            	game.getWorld().clear(nextPos);
            	game.getWorld().set(direction.nextPosition(nextPos), new Box());
            } else if (game.getWorld().get(nextPos) instanceof Heart) {
            	game.getWorld().clear(nextPos);
            	lives++;
            } else if (game.getWorld().get(nextPos) instanceof Key) {
            	game.getWorld().clear(nextPos);
            	keys++;
            }
            for (GameObject go : this.game.getGameObjects() )
        		if (go instanceof Monster && go.getPosition().equals(nextPos)) {
        			lives=lives-1;
        		}
            
            setPosition(nextPos);
    	}
        
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
    }

    public boolean isWinner() {
    	if (lives==0) {
    		return false;
    	}
    	for (GameObject go : this.game.getGameObjects())
    		if (go instanceof Princess && go.getPosition().equals(this.getPosition())) {
    			return true;
    		}
    	return false;
    	
    }

    public boolean isAlive() {
    	if (lives==0) {
    		return false;
    	}
    	return true;
    }
    
    public String toString() {
    	return "Player";
    }

}
