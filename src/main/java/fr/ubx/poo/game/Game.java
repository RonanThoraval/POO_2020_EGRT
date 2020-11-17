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

import fr.ubx.poo.model.go.Door;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.Heart;
import fr.ubx.poo.model.go.Key;
import fr.ubx.poo.model.go.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.go.character.Princess;

public class Game {

    private final World world;
    private final List<GameObject> gameObjects;
    private final Player player;
    private final String worldPath;
    public int initPlayerLives;

    public Game(String worldPath) {
        world = new WorldStatic();
        gameObjects=build(world.getRaw());
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
    
    public List<GameObject> build(WorldEntity[][] raw) {
    	List<GameObject> gameObjects=new ArrayList<>();
		for (int x=0 ; x<world.dimension.width ; x++) {
			for (int y=0 ; y<world.dimension.height ; y++) {
				Position pos=new Position(x,y);
				GameObject object=processEntity(raw[y][x],pos);
				if (object!=null) {
					System.out.println(object);
					gameObjects.add(object);
				}
			}
		}
		return gameObjects;
	}
	
	public GameObject processEntity(WorldEntity entity,Position pos) {
		switch(entity) {
		case Monster : 
			return new Monster(this,pos);
		case Heart :
			return new Heart(this,pos);
		case Key :
			return new Key(this,pos);
		case DoorNextClosed :
			return new Door(this,pos);
		case Princess :
			return new Princess(this,pos);
		default:
			return null;
		}
	}
    
	public void forEach(Consumer <GameObject> go) {
		gameObjects.forEach(go);
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
    
    public List<GameObject> getGameObjects() {
    	return this.gameObjects;
    }


}
