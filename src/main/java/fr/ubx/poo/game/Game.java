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

import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.decor.DoorClosed;
import fr.ubx.poo.model.decor.Heart;
import fr.ubx.poo.model.decor.Key;
import fr.ubx.poo.model.go.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.decor.Princess;

public class Game {

    private final World world;
    private final List<Monster> monsters;
    private final Player player;
    private final String worldPath;
    public int initPlayerLives;

    public Game(String worldPath) {
        world = new WorldStatic();
        monsters=build(world.getRaw());
        this.worldPath = worldPath;
        loadConfig(worldPath);
        Position positionPlayer = null;
        try {
            positionPlayer = world.findPlayer();
            player = new Player(this, positionPlayer);
        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }
    
    public List<Monster> build(WorldEntity[][] raw) {
    	List<Monster> monsters=new ArrayList<>();
		for (int x=0 ; x<world.dimension.width ; x++) {
			for (int y=0 ; y<world.dimension.height ; y++) {
				Position pos=new Position(x,y);
				Monster monster=processEntity(raw[y][x],pos);
				if (monster!=null) {
					System.out.println(monster);
					monsters.add(monster);
				}
			}
		}
		return monsters;
	}
	
	public Monster processEntity(WorldEntity entity,Position pos) {
		switch(entity) {
		case Monster : 
			return new Monster(this,pos);
		default:
			return null;
		}
	}
    
	public List<Monster> getMonsters() {
		return monsters;
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
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }

    public World getWorld() {
        return world;
    }

    public Player getPlayer() {
        return this.player;
    }
    
    
    public void addMonster(Monster monster) {
    	monsters.add(monster);
    }
    
    public void removeMonster(Monster monster ){
    	monsters.remove(monster);
    }
    

}
