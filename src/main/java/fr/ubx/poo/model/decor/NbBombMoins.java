package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class NbBombMoins extends Decor{

	@Override
    public String toString() {
        return "NbBombMoins";
    }
	
	public boolean canPlayerGo(Player player) {
		return true;
	}
	
	public void doPlayerGo(Player player) {
		player.manage(player.getDirection().nextPosition(player.getPosition()));
		if(player.getNbBombs()!=1) {
			player.decreaseNbBombs();
		}
	}
}