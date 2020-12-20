package fr.ubx.poo.game;

import static fr.ubx.poo.game.WorldEntity.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import fr.ubx.poo.game.*;

public class WorldConstructor extends World{

	public WorldConstructor(String prefix, int level,String path) throws IOException {
		super (prefix,level,path);
	}
	
	
}
