/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Box;
import fr.ubx.poo.model.decor.Door;
import fr.ubx.poo.model.decor.Heart;
import fr.ubx.poo.model.decor.Key;
import fr.ubx.poo.model.decor.NbBombMoins;
import fr.ubx.poo.model.decor.NbBombPlus;
import fr.ubx.poo.model.decor.Princess;
import fr.ubx.poo.model.decor.RangeBombMoins;
import fr.ubx.poo.model.decor.RangeBombPlus;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.Monster;
import fr.ubx.poo.game.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
    private boolean winner;
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

    public Direction getDirection() {
        return direction;
    }
    
    public int getNbBombs() {
    	return nbBombs;
    }
    
    public void increaseNbBombs() {
    	nbBombs++;
    }
    
    public int getRangeBombs() {
    	return rangeBombs;
    }
    
    public int getKeys() {
    	return keys;
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
    	if (!newPos.inside(this.game.getWorld().dimension)) {
    		return false;
    	}
    	if (!this.game.getWorld().isEmpty(newPos)) {
    		if (this.game.getWorld().get(newPos) instanceof Box) {
    			Position newPos2=direction.nextPosition(newPos);
    			if (this.game.getWorld().isEmpty(newPos2) && newPos2.inside(this.game.getWorld().dimension)) {
    				return true;
    			}
    			else {
    				return false;
    			}
    		} else if (this.game.getWorld().get(newPos) instanceof Heart || this.game.getWorld().get(newPos) instanceof Key 
    				|| this.game.getWorld().get(newPos) instanceof RangeBombPlus || this.game.getWorld().get(newPos) instanceof RangeBombMoins
    				|| this.game.getWorld().get(newPos) instanceof NbBombPlus || this.game.getWorld().get(newPos) instanceof NbBombMoins
    				|| this.game.getWorld().get(newPos) instanceof Princess){
    			return true ;
    		} else if (this.game.getWorld().get(newPos) instanceof Door) {
    			Door d=(Door) this.game.getWorld().get(newPos);
    			return (d.getEtat()!=1);
    		}
    		return false;
    	}
        return true;
    }

    public void doMove(Direction direction) throws IOException {
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
            } else if (game.getWorld().get(nextPos) instanceof RangeBombPlus) {
            	game.getWorld().clear(nextPos);
            	rangeBombs++;
            } else if (game.getWorld().get(nextPos) instanceof RangeBombMoins) {
            	game.getWorld().clear(nextPos);
            	if(rangeBombs!=1) {
            	rangeBombs--;
            	}
            }else if (game.getWorld().get(nextPos) instanceof NbBombPlus) {
            	game.getWorld().clear(nextPos);
            	nbBombs++;
            } else if (game.getWorld().get(nextPos) instanceof NbBombMoins) {
            	game.getWorld().clear(nextPos);
            	if(nbBombs!=1) {
            	nbBombs--;
            	}
            } else if (game.getWorld().get(nextPos) instanceof Door) {
            	Door d= (Door) game.getWorld().get(nextPos);
            	if (d.getEtat()==3) {
            		game.changeLevel("next");
            		game.getWorld().setChanged(true);
            		return ;
            	}
            	if (d.getEtat()==2) {
            		game.changeLevel("prev");
            		game.getWorld().setChanged(true);
            		return ;
            	}
            }
            for (Monster monster : this.game.getMonsters() ) {
        		if ( monster.getPosition().equals(nextPos)) {
        			decreaseLives();
        		}
            }
            setPosition(nextPos);
    	}
        
    }

    public void update(long now) throws IOException {
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
        }
        if (bombRequest) {
        	if(canPoseBomb()) {
        		PoseBomb(now);
        	}
        	bombRequest=false;
        }
    }

    public boolean isWinner() {
    	if (lives==0) {
    		return false;
    	}
    	if (game.getWorld().get(getPosition()) instanceof Princess) {
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
			if (d.getEtat()==1) {
				return (d.getEtat()==1);
			}
		}
		return false;
	}
	
	public void OpenDoor(Direction direction) {
		Position newPos=direction.nextPosition(getPosition());
		Door d=(Door) game.getWorld().get(newPos);
		d.setEtat(3);
		game.getWorld().setChanged(true);
		setRequestOpenDoor(false);
		keys--;
	}
	
	public boolean canPoseBomb() {
		return nbBombs!=0;
	}
	
	
	public void removeBombExplosed() {
		Iterator<Bomb> it=listBomb.get(game.getCurrentLevel()).iterator();
    	while (it.hasNext()) {
    		Bomb next=it.next();
    		if (next.explosed()) {
    			it.remove();
    		}
    	}
	}
	
	public void removeBomb(Bomb b) {
		Iterator<Bomb> iterator=listBomb.get(game.getCurrentLevel()).iterator();
		while (iterator.hasNext()) {
			Bomb next=iterator.next();
			if (next==b) {
				iterator.remove();
			}
		}
	}
	public void PoseBomb(long now) {
		long start=now;
		Bomb b=new Bomb(this.game,getPosition(),start);
		listBomb.get(game.getCurrentLevel()).add(b);
		nbBombs=nbBombs-1;
			
	}
	
	public void decreateBombs() {
		for (Bomb b : listBomb.get(game.getCurrentLevel()))
			b.decreate();
	}

}
