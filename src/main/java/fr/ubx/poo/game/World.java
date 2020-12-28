/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Door;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;

public class World {
    private final Map<Position, Decor> grid;
    private final WorldEntity[][] raw;
    public final Dimension dimension;
    private boolean changed=true;

    public World(WorldEntity[][] raw) {
        this.raw = raw;
        dimension = new Dimension(raw.length, raw[0].length);
        grid = WorldBuilder.build(raw, dimension);
    }
    
    public World(String prefix, int level, String path) throws IOException {
    	this.raw=WorldConstruct(prefix,level,path);
    	dimension=new Dimension(raw.length,raw[0].length);
    	grid=WorldBuilder.build(raw, dimension);
    	
    }

	public WorldEntity[][] WorldConstruct(String prefix, int level,String path) throws IOException {
		BufferedReader lecteurAvecBuffer = null;
		BufferedReader taille_du_fichier=null;
		String ligne2;
		String ligne;		
		try {
			lecteurAvecBuffer = new BufferedReader(new InputStreamReader( this.getClass().getResourceAsStream("/sample/"+prefix+level+".txt")));
			taille_du_fichier=new BufferedReader(new InputStreamReader( this.getClass().getResourceAsStream("/sample/"+prefix+level+".txt")));
			int nb_col=0;
			int nb_ligne=0;
			while ((ligne2 = taille_du_fichier.readLine()) != null) {
				nb_ligne++;
				nb_col=ligne2.length();
			}
			WorldEntity[][] mapEntities = new WorldEntity[nb_ligne][nb_col];
			
			int j=0;
			while ((ligne = lecteurAvecBuffer.readLine()) != null) {
				for( int i =0; i <ligne.length(); i++) {
					for (WorldEntity e : WorldEntity.values()) {
						if (e.getCode()==ligne.charAt(i)) {
							mapEntities[j][i]=e;
						}
					}
				}
				j++;
			}
			lecteurAvecBuffer.close();
			taille_du_fichier.close();
			return mapEntities;
	    }
	    catch(FileNotFoundException exc) {
		    System.out.println("Erreur d'ouverture");
		   	return null;
	    }	
	}

    public Position findPlayer() throws PositionNotFoundException {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.Player) {
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("Player");
    }
    
    public Position findDoor(String DoorState) throws PositionNotFoundException {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (get(new Position(x,y)) instanceof Door ) {
                	Door d=(Door) get(new Position(x,y));
                	if (DoorState=="NextOpened" && d.getState()==3) {
                		return new Position(x,y);
                	} else if (DoorState=="PrevOpened" && d.getState()==2) {
                        return new Position(x, y);
                		
                	} else if (DoorState=="NextClosed" && d.getState()==1) {
                		return new Position(x,y);
                	}
                }
            }
        }
        throw new PositionNotFoundException(DoorState);
    }
    
    public boolean hasChanged() {
    	return changed;
    }
    
    public void setChanged(boolean b) {
    	this.changed=b;
    }

    public Decor get(Position position) {
        return grid.get(position);
    }
    
    public WorldEntity[][] getRaw() {
    	return this.raw;
    }
    

    public void set(Position position, Decor decor) {
        grid.put(position, decor);
        changed=true;
    }

    public void clear(Position position) {
        grid.remove(position);
        changed=true;
    }

    public void forEach(BiConsumer<Position, Decor> fn) {
        grid.forEach(fn);
    }
    

    public Collection<Decor> values() {
        return grid.values();
    }

    public boolean isInside(Position position) {
        return true; // to update
    }

    public boolean isEmpty(Position position) {
        return grid.get(position) == null;
    }
}
