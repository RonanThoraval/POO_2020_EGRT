package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class Heart extends Decor{
	
	@Override
    public String toString() {
        return "Heart";
    }
	
	public boolean canPlayerGo(Player player) {
		return true;
	}

	@Override
	public void doPlayerGo(Player player) {
		player.increaseLives();
		player.manage(player.getDirection().nextPosition(player.getPosition()));
	}
}
