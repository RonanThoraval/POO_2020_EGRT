/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import fr.ubx.poo.model.go.Explosion;
import fr.ubx.poo.model.go.Monster;
import fr.ubx.poo.model.go.character.Player;


public class Game {

    private final World[] world;
    private final List<List<Monster>> monsters;
    private List<List<Explosion>> explosions = new ArrayList<>();
    private Player player=null;
    private final String worldPath;
    public int initPlayerLives;
    private int nb_levels;
    //current_level starts at 0, because it's easier for the lists.
    private int current_level=0;
    private int before_level=0;
    private String prefix;
    private boolean hasChangedLevel;

    public Game(String worldPath) throws IOException {
		this.worldPath = worldPath;
		loadConfig(worldPath);
    	world = new World[nb_levels];
    	monsters = new ArrayList<List<Monster>>();
    	for(int i = 0; i<nb_levels; i++) {
    		world[i]=new World(prefix,i+1,worldPath);
    		monsters.add(build(world[i].getRaw(),i));
    		explosions.add(new ArrayList<>());
    		Position positionPlayer = null;
    		try {
    			positionPlayer = world[0].findPlayer();
    			player = new Player(this, positionPlayer,initPlayerLives);
    		} catch (PositionNotFoundException e) {
    			System.err.println("Position not found : " + e.getLocalizedMessage());
    			throw new RuntimeException(e);
    		}
    	}
    }
    
    /**
     * 
     * @param raw
     * @param i, the level
     * @return a list of monsters who are in the level
     */
    public List<Monster> build(WorldEntity[][] raw, int i) {
    	List<Monster> monsters=new ArrayList<>();
		for (int x=0 ; x<world[i].dimension.width ; x++) {
			for (int y=0 ; y<world[i].dimension.height ; y++) {
				Position pos=new Position(x,y);
				Monster monster=processEntity(raw[y][x],pos);
				if (monster!=null) {
					monsters.add(monster);
				}
			}
		}
		return monsters;
	}
	
    /**
     * 
     * @param entity
     * @param pos
     * @return the monster if "entity" is a monster, null else
     */
	public Monster processEntity(WorldEntity entity,Position pos) {
		switch(entity) {
		case Monster : 
			return new Monster(this,pos,0);
		default:
			return null;
		}
	}
    
	/**
	 * 
	 * @return the list of monsters in the level where the player is
	 */
	public List<Monster> getMonsters() {
		return monsters.get(current_level);
	}
	
	/**
	 * 
	 * @param level
	 * @return the list of monsters in the level "level"
	 */
	public List<Monster> getMonsters(int level) {
		return monsters.get(level);
	}
	
	/**
	 * 
	 * @return the lists of explosions of all levels
	 */
	public List<List<Explosion>> getExplosion(){
		return explosions;
	}
	
	/**
	 * 
	 * @return the initial numbers of lives of the player
	 */
    public int getInitPlayerLives() {
        return initPlayerLives;
    }

    private void loadConfig(String path) {
        try (InputStream input = new FileInputStream(new File(path, "config.properties"))) {
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            initPlayerLives = Integer.parseInt(prop.getProperty("lives", "3"));
            nb_levels=Integer.parseInt(prop.getProperty("levels","1"));
            prefix=prop.getProperty("prefix");
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }

    /**
     * 
     * @return the world where the player is
     */
    public World getWorld() {
        return world[current_level];
    }
    
    /**
     * 
     * @param level
     * @return the world of level "level"
     */
    public World getWorld(int level) {
    	return world[level];
    }

    /**
     * 
     * @return the player of the game
     */
    public Player getPlayer() {
        return this.player;
    }
    
    /**
     * 
     * @param level
     * @param explosion
     * Adds an explosion in the level "level".
     */
    public void addExplosion(int level, Explosion explosion) {
    	explosions.get(level).add(explosion);
    }
    
    /**
     * Adds an monster in the level "level"
     * @param monster
     * @param level
     */
    public void addMonster(Monster monster, int level) {
    	monsters.get(current_level).add(monster);
    }
    
    /**
     * 
     * @return the "digit of level" where the player is
     */
    public int getCurrentLevel() {
    	return current_level;
    }
    
    /**
     * 
     * @return the the digit of the previous level where the player was.
     */
    public int getBeforeLevel() {
    	return before_level;
    }
    
    /**
     * 
     * @param whichLevel, a string ( "next" or "prev") which states if the player goes to the next or the previous level
     * @throws IOException
     * The method changes level, puts the player where the door is.
     */
    public void changeLevel(String whichLevel) throws IOException {
    	if (whichLevel=="next") {
    		before_level =current_level;
    		current_level+=1;
    		try {
    			player.setPosition(world[current_level].findDoor("PrevOpened"));
    		} catch (PositionNotFoundException e) {
    			System.err.println("Position not found : " + e.getLocalizedMessage());
    			throw new RuntimeException(e);
    		}
    	}
    	else if (whichLevel=="prev") {
    		before_level=current_level;
    		current_level=current_level-1;
    		try {
    			player.setPosition(world[current_level].findDoor("NextOpened"));
    		} catch (PositionNotFoundException e) {
    			System.err.println("Position not found : " + e.getLocalizedMessage());
    			throw new RuntimeException(e);
    		}
    	}
		hasChangedLevel=true;
    }
    
    /**
     * 
     * @return true if the player has changed level.
     */
    public boolean hasChangedLevel() {
    	return hasChangedLevel;
    }
    
    /**
     * 
     * @return the total numbers of levels in the game.
     */
    public int getNbLevels() {
    	return nb_levels;
    }
    
    /**
     * 
     * @param b, a boolean
     * Sets b in hasChangedLevel.
     */
    public void setChangedLevel(boolean b) {
    	hasChangedLevel=b;
    }

}
