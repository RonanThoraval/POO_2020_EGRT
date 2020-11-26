package fr.ubx.poo;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
 
public class Proprietes {
	private String prefix;
	private int nbLevels;
	private int PlayerLives;
	
	public void setProprietes() throws IOException {
	BufferedReader lecteurAvecBuffer = null;
	String ligne;
    try
      {
	lecteurAvecBuffer = new BufferedReader(new FileReader("config.properties"));
      }
    catch(FileNotFoundException exc)
      {
	System.out.println("Erreur d'ouverture");
      }
    while ((ligne = lecteurAvecBuffer.readLine()) != null) {
    	int l = ligne.indexOf( "prefix" );
    	if(l!=-1) {
    		prefix = ligne.substring( l, ligne.length() );
    	}else {
    		int l1= ligne.indexOf("levels");
    		if(l1!=-1) {
    			nbLevels = Integer.parseInt(ligne.substring( l1, ligne.length() ));
    		}else {
    			int l2 = ligne.indexOf( "lives" );
    			if(l2!=-1) {
    				PlayerLives = Integer.parseInt(ligne.substring( l2, ligne.length() ));
    			}
    		}
    	}
    }
    lecteurAvecBuffer.close();
  }
	
	public String getPrefix() {
		return prefix;
	}

	public int getNbLevels() {
		return nbLevels;
	}

	public int getPlayerLives() {
		return PlayerLives;
	}
	
}