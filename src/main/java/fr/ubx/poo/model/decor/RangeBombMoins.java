package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class RangeBombMoins extends Decor{

	@Override
    public String toString() {
        return "RangeBombMoins";
    }
	
	public boolean canPlayerGo(Player player) {
		return true;
	}
	
	public void doPlayerGo(Player player) {
		player.manage(player.getDirection().nextPosition(player.getPosition()));
		if(player.getRangeBombs()!=1) {
			player.decreaseRangeBombs();
		}
	}
}