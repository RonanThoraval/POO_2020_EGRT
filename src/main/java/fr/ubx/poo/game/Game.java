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
    //player devrait être final et worldpath aussi
    //player=null à changer mais je vois pas d'autre solution pour l'instant
    private Player player=null;
    private String worldPath;
    public int initPlayerLives;
    private int nb_levels;
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
    		world[i]=new WorldConstructor(prefix,i+1,worldPath);  //Les fichiers level commencent � level1
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
	
	public Monster processEntity(WorldEntity entity,Position pos) {
		switch(entity) {
		case Monster : 
			return new Monster(this,pos,0);
		default:
			return null;
		}
	}
    
	public List<Monster> getMonsters() {
		return monsters.get(current_level);
	}
	
	public List<List<Explosion>> getExplosion(){
		return explosions;
	}

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

    public World getWorld() {
        return world[current_level];
    }

    public Player getPlayer() {
        return this.player;
    }
    
    public void addExplosion(int level, Explosion explosion) {
    	explosions.get(level).add(explosion);
    }
    
    public void addMonster(Monster monster, int level) {
    	monsters.get(current_level).add(monster);
    }
    
    public int getCurrentLevel() {
    	return current_level;
    }
    
    public int getBeforeLevel() {
    	return before_level;
    }
    
    
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
    
    public boolean hasChangedLevel() {
    	return hasChangedLevel;
    }
    
    public int getNbLevels() {
    	return nb_levels;
    }
    
    public void setChangedLevel(boolean b) {
    	hasChangedLevel=b;
    }

}
