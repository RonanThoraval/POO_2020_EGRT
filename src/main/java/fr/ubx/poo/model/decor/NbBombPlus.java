package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class NbBombPlus extends Decor{

	@Override
    public String toString() {
        return "NbBombPlus";
    }
	
	public void doPlayerGo(Player player) {
		player.manage(player.getDirection().nextPosition(player.getPosition()));
		player.increaseNbBombs();
	}

}