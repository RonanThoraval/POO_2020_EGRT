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

    public Player(Game game, Position position, int lives) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = lives;
        for (int i=0; i<game.getNbLevels(); i++) {
        	listBomb.add(new ArrayList<>());
        }
    }
    
    public int getLives() {
        return lives;
    }
    
    public void decreaseLives() {
    	lives--;
    }
    
    public void increaseLives() {
    	lives++;
    }

    public Direction getDirection() {
        return direction;
    }
    
    public int getNbBombs() {
    	return nbBombs;
    }
    
    public void increaseNbBombs() {
    	nbBombs++;
    }
    
    public void decreaseNbBombs() {
    	nbBombs--;
    }
    
    public int getRangeBombs() {
    	return rangeBombs;
    }
    
    public void decreaseRangeBombs() {
    	rangeBombs--;
    }
    
    public void increaseRangeBombs() {
    	rangeBombs++;
    }
    
    public int getKeys() {
    	return keys;
    }
    
    public void increaseKeys() {
    	keys++;
    }
    
    public void decreaseKeys() {
    	keys--;
    }
    
    public void setWinner() {
    	winner=true;
    }
    
    public List<List<Bomb>> getListBombs() {
    	return listBomb;
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
    	
        return (newPos.inside(this.game.getWorld().dimension)) && 
        (this.game.getWorld().isEmpty(newPos) || this.game.getWorld().get(newPos).canPlayerGo(this));
    }

    public void doMove(Direction direction) throws IOException {
    	if (canMove(direction)) {
    		Position nextPos = direction.nextPosition(getPosition());
    		if (this.game.getWorld().isEmpty(nextPos)) {
	            for (Monster monster : this.game.getMonsters() ) {
		        	if ( monster.getPosition().equals(nextPos)) {
		        		decreaseLives();
		        	}
		        }
	            setPosition(nextPos);
    		}else {
    			game.getWorld().get(nextPos).doPlayerGo(this.game.getPlayer());
    		}
    	}
        
    }
    
    public void update(long now) throws IOException {
    	for (Monster monster : game.getMonsters()) {
    		if (getPosition().equals(monster.getPosition())) {
    			decreaseLives();
    		}
    	}
    	
    	
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
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

    public boolean isWinner() {
    	return winner;
    }

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
	
	public void OpenDoor(Direction direction) {
		Position newPos=direction.nextPosition(getPosition());
		Door d=(Door) game.getWorld().get(newPos);
		d.setState(3);
		game.getWorld().setChanged(true);
		setRequestOpenDoor(false);
		keys--;
	}
	
	public boolean canPoseBomb() {
		return nbBombs!=0;
	}
	
	public void PoseBomb(long now) {
		long start=now;
		Bomb b=new Bomb(this.game,getPosition(),start);
		listBomb.get(game.getCurrentLevel()).add(b);
		nbBombs=nbBombs-1;
			
	}

	public void manageBox(Position position) {
		this.game.getWorld().clear(position);
    	this.game.getWorld().set(this.direction.nextPosition(position), new Box());
    	setPosition(position);
	}
	
	public void manage(Position position) {
		this.game.getWorld().clear(position);
		setPosition(position);
	}

}