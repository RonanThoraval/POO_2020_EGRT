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
import java.util.function.Consumer;

import fr.ubx.poo.model.go.Explosion;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.decor.DoorClosed;
import fr.ubx.poo.model.decor.Heart;
import fr.ubx.poo.model.decor.Key;
import fr.ubx.poo.model.go.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.decor.Princess;


//Il va falloir r�ussir � g�rer List<Explosion> en tant que tableau
//comme List<Monster>[]  � mon avis, ou trouuver une alternative pour
//stocker chaque donn�e suivant les diff�rents niveaux

//J'ai pas tout check� donc y'a s�rement des trucs faux ou qui n'ont pas
//de sens peut-�tre

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
    
    public void addExplosion(Explosion explosion) {
    	explosions.get(current_level).add(explosion);
    }
    
    public void removeExplosion(Explosion explosion) {
    	explosions.get(current_level).remove(explosion);
    }
    
    public void addMonster(Monster monster, int level) {
    	monsters.get(current_level).add(monster);
    }
    
    public void removeMonster(Monster monster, int level){
    	monsters.get(current_level).remove(monster);
    }
    
    public int getCurrentLevel() {
    	return current_level;
    }
    
    public Position findDoorPrevOpen() throws PositionNotFoundException {
        for (int x = 0; x < world[current_level].dimension.width; x++) {
            for (int y = 0; y < world[current_level].dimension.height; y++) {
                if (world[current_level].getRaw()[y][x] == WorldEntity.DoorPrevOpened) {
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("Player");
    }
    
    public void changeLevel() throws IOException {
    	current_level+=1;
    	hasChangedLevel=true;
    	try {
    		System.out.println("avant"+player.getPosition());
        	player.setPosition(findDoorPrevOpen());
        	System.out.println("après"+player.getPosition());
    	} catch (PositionNotFoundException e) {
    		System.err.println("Position not found : " + e.getLocalizedMessage());
			throw new RuntimeException(e);
    	}
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
