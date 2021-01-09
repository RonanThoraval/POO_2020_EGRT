/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Box;
import fr.ubx.poo.model.decor.Door;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.Monster;
import fr.ubx.poo.game.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject implements Movable {

    private final boolean alive = true;
    private List<List<Bomb>> listBomb=new ArrayList<>();
    private boolean OpenDoorRequest = false;
    Direction direction;
    private boolean moveRequested = false;
    private int lives;
    private int keys=0;
    private int nbBombs =1;
    private int rangeBombs=1;
    private boolean winner=false;
    private boolean bombRequest=false;
    private boolean invincible=false;
    private long start;

    public Player(Game game, Position position, int lives) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = lives;
        for (int i=0; i<game.getNbLevels(); i++) {
        	listBomb.add(new ArrayList<>());
        }
    }
    
    /**
     * 
     * @return the player's number of lives
     */
    public int getLives() {
        return lives;
    }
    
    /**
     * Decreases the player's lives by one, when it's possible
     * @param now
     */
    public void decreaseLives(long now) {
    	if(!invincible) {
    		invincible=true;
    		lives--;
    		start=now;
    	}
    }
    
    /**
     * Increases the player's lives by one
     */
    public void increaseLives() {
    	lives++;
    }

    public Direction getDirection() {
        return direction;
    }
    
    /**
     * 
     * @return the current player's number of bombs
     */
    public int getNbBombs() {
    	return nbBombs;
    }
    
    /**
     * Increases the player's number of bombs by one
     */
    public void increaseNbBombs() {
    	nbBombs++;
    }
    
    /**
     * Decreases the player's number of bombs by one
     */
    public void decreaseNbBombs() {
    	nbBombs--;
    }
    
    /**
     * 
     * @return the player's range of bombs
     */
    public int getRangeBombs() {
    	return rangeBombs;
    }
    
    /**
     * Decreases the player's range of bombs by one
     */
    public void decreaseRangeBombs() {
    	rangeBombs--;
    }
    
    /**
     * Increases the player's range of bombs by one
     */
    public void increaseRangeBombs() {
    	rangeBombs++;
    }
    
    /**
     * 
     * @return the player's number of keys
     */
    public int getKeys() {
    	return keys;
    }
    
    /**
     * Increases the player's number of keys by one
     */
    public void increaseKeys() {
    	keys++;
    }
    
    /**
     * Decreases the player's numbers of keys by one
     */
    public void decreaseKeys() {
    	keys--;
    }
    
    /**
     * Sets the player winner
     */
    public void setWinner() {
    	winner=true;
    }
    
    /**
     * 
     * @return the list of the player's bombs posed
     */
    public List<List<Bomb>> getListBombs() {
    	return listBomb;
    }
    
    /**
     * Sets the direction asked, and says if a move is requested
     * @param direction
     */
    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    @Override
    /**
     * @param direction where the player wants to go
     * 
     * @return true if the player can go towards direction given in parameters, false else
     * 
     */
    public boolean canMove(Direction direction) {
    	Position newPos=direction.nextPosition(getPosition());
    	
        return (newPos.inside(this.game.getWorld().dimension)) && 
        (this.game.getWorld().isEmpty(newPos) || this.game.getWorld().get(newPos).canPlayerGo(this));
    }

    /**
     * Sets the new player's position and decreases his lives if necessary
     */
    public void doMove(Direction direction, long now) throws IOException {
    	Position nextPos = direction.nextPosition(getPosition());
    	if (this.game.getWorld().isEmpty(nextPos)) {
            for (Monster monster : this.game.getMonsters() ) {
            	if ( monster.getPosition().equals(nextPos)) {
		       		decreaseLives(now);
		       	}
		    }
	        setPosition(nextPos);
    	}else {
   			game.getWorld().get(nextPos).doPlayerGo(this.game.getPlayer());
    	}
        
    }
    
    public void update(long now) throws IOException {
    	
    	if(invincible && now-start>=Math.pow(10,9)) {
    		invincible = false;
    		System.out.println("b");
    	}
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction, now);
            }
            moveRequested = false;
        }
        if (OpenDoorRequest) {
        	if(canOpenDoor(direction)) {
        	OpenDoor(direction);
        	}
        	OpenDoorRequest=false;
        }
        if (bombRequest) {
        	if(canPoseBomb()) {
        		PoseBomb(now);
        	}
        	bombRequest=false;
        }
    }

    /**
     * 
     * @return true if the player has won the game, false else
     */
    public boolean isWinner() {
    	return winner;
    }

    /**
     * 
     * @return true if the player is alive (if his number of lives is not 0), false else
     */
    public boolean isAlive() {
    	return (lives!=0);
    }
    
    public String toString() {
    	return "Player";
    }

    public void requestOpenDoor() {
		OpenDoorRequest=true;
	}
	
	public void setRequestOpenDoor(boolean b) {
		OpenDoorRequest=false;
	}
	
	public void requestBomb() {
		bombRequest=true;
	}
	
	/**
	 * 
	 * @param direction
	 * 
	 * @return true if the player can open the door he is looking at (if he has a key, and the door is not already open), false else 
	 */
	public boolean canOpenDoor(Direction direction) {
		Position newPos=direction.nextPosition(getPosition());
		if(game.getWorld().get(newPos) instanceof Door && keys!=0) {
			Door d=(Door) game.getWorld().get(newPos);
			if (d.getState()==1) {
				return (d.getState()==1);
			}
		}
		return false;
	}
	
	/**
	 * Opens the door and decreases the number of keys
	 * @param direction
	 */
	public void OpenDoor(Direction direction) {
		Position newPos=direction.nextPosition(getPosition());
		Door d=(Door) game.getWorld().get(newPos);
		d.setState(3);
		game.getWorld().setChanged(true);
		setRequestOpenDoor(false);
		keys--;
	}
	
	/**
	 * 
	 * @return true if the player can pose a bomb
	 */
	public boolean canPoseBomb() {
		return nbBombs!=0;
	}
	
	/**
	 * Poses a bomb and decreases the number of bombs
	 * @param now
	 */
	public void PoseBomb(long now) {
		long start=now;
		Bomb b=new Bomb(this.game,getPosition(),start);
		listBomb.get(game.getCurrentLevel()).add(b);
		nbBombs=nbBombs-1;
			
	}
	
	/**
	 * 
	 * @param position
	 * Puts the box further.
	 */
	public void manageBox(Position position) {
		this.game.getWorld().clear(position);
    	this.game.getWorld().set(this.direction.nextPosition(position), new Box());
    	setPosition(position);
	}
	
	/**
	 * 
	 * @param position
	 * For the bonus, the player removes the bonus, and takes its place.
	 */
	public void manage(Position position) {
		this.game.getWorld().clear(position);
		setPosition(position);
	}

}