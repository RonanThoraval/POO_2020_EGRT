/*
 * Copyright (c) 2020. Laurent RÃ©veillÃ¨re
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
import fr.ubx.poo.Proprietes;
import fr.ubx.poo.model.decor.DoorClosed;
import fr.ubx.poo.model.decor.Heart;
import fr.ubx.poo.model.decor.Key;
import fr.ubx.poo.model.go.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.model.decor.Princess;


//Il va falloir réussir à gérer List<Explosion> en tant que tableau
//comme List<Monster>[]  à mon avis, ou trouuver une alternative pour
//stocker chaque donnée suivant les différents niveaux

//J'ai pas tout checké donc y'a sûrement des trucs faux ou qui n'ont pas
//de sens peut-être

public class Game {

    private final World[] world;
    private final List<Monster>[] monsters;
    private List<Explosion> explosions = new ArrayList<>();
    private final Player player;
    private final String worldPath;
    public int initPlayerLives;

    public Game(String worldPath) {
    	Proprietes p = new Proprietes();
    	world = new World[p.getNbLevels()];
    	//Je ne sais absolument pas pourquoi, on peut pas faire un tableau de listes
    	//Ce qui m'embête bien
    	monsters = new List<Monster>[p.getNbLevels()];
    	for(int i = 0; i<p.getNbLevels(); i++) {
    		world[i]=new WorldConstructor(p,i+1);  //Les fichiers level commencent à level1
    		monsters[i]=build(world[i].getRaw(),i);
    		this.worldPath = worldPath;
    		loadConfig(worldPath);
    		Position positionPlayer = null;
    		try {
    			positionPlayer = world[0].findPlayer();
    			player = new Player(this, positionPlayer);
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
			return new Monster(this,pos,0);
		default:
			return null;
		}
	}
    
	public List<Monster> getMonsters(int level) {
		return monsters[level];
	}
	
	public List<Explosion> getExplosion(){
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
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }

    public World getWorld(int level) {
        return world[level];
    }

    public Player getPlayer() {
        return this.player;
    }
    
    public void addExplosion(Explosion explosion) {
    	explosions.add(explosion);
    }
    
    public void removeExplosion(Explosion explosion) {
    	explosions.remove(explosion);
    }
    
    public void addMonster(Monster monster, int level) {
    	monsters[level].add(monster);
    }
    
    public void removeMonster(Monster monster, int level){
    	monsters[level].remove(monster);
    }
    

}
