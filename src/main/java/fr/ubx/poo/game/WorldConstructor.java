package fr.ubx.poo.game;

import static fr.ubx.poo.game.WorldEntity.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import fr.ubx.poo.game.*;
import fr.ubx.poo.Proprietes;

public class WorldConstructor extends World{

	public WorldConstructor(Proprietes p, int level) throws IOException {
		super(WorldConstruct(p,level));
	}
															// si on ne met pas throws blabla, et qu'on enl�ve le "int j=0;"
															// pas de probl�me, mais avec le "int j=0;", il le faut. Pourquoi ?
	public static WorldEntity[][] WorldConstruct(Proprietes p, int level) throws IOException {
		//Pour connaitre la taille de de notre jeu, il faudrait parcourir une
		//premi�re fois le fichier, pour r�cuperer sa taille (bof bof)
		WorldEntity[][] mapEntities = new WorldEntity[999999999][999999999];
			BufferedReader lecteurAvecBuffer = null;
			String ligne;											//			  ||
		    try														// 	    	  ||
		      {									   //J'suis pas s�r du tout de �a \/
			lecteurAvecBuffer = new BufferedReader(new FileReader(p.getPrefix() + "." + level));
		      }
		    catch(FileNotFoundException exc)
		      {
			System.out.println("Erreur d'ouverture");
		      }
		    int j=0;
		    //ligne est cens� �tre chaque ligne de notre fichier sous forme de string
		    while ((ligne = lecteurAvecBuffer.readLine()) != null) {
		    	for( int i =0; i <ligne.length(); i++) {
		    		for(int index = 0; index<16; index++) {
		    			if(WorldEntity.values()[index].getCode()==ligne.charAt(i))
		    				mapEntities[i][j]= WorldEntity.values()[index];
		    		}
		    	}
		    	j++;
		    }
		    lecteurAvecBuffer.close();
		    return mapEntities;
		  }
}
