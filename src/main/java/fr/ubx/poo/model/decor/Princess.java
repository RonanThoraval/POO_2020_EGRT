package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class Princess extends Decor {

	@Override
    public String toString() {
        return "Princess";
    }
	
	public boolean canPlayerGo(Player player) {
		return true;
	}

	@Override
	public void doPlayerGo(Player player) {
		// TODO Auto-generated method stub
		
	}
}
