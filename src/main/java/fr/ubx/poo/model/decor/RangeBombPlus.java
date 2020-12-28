package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class RangeBombPlus extends Decor{

	@Override
    public String toString() {
        return "RangeBombPlus";
    }
	
	public void doPlayerGo(Player player) {
		player.manage(player.getDirection().nextPosition(player.getPosition()));
		player.increaseRangeBombs();
	}

}