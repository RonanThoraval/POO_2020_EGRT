package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class Key extends Decor{

	@Override
    public String toString() {
        return "Key";
    }
	
	public void doPlayerGo(Player player) {
		player.manage(player.getDirection().nextPosition(player.getPosition()));
		player.increaseKeys();
	}

	@Override
	public boolean canExplose() {
		return false;
	}
}
