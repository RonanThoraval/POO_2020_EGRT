package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class Princess extends Decor {

	@Override
    public String toString() {
        return "Princess";
    }

	@Override
	public void doPlayerGo(Player player) {
		player.setWinner();
	}

	@Override
	public boolean canExplose() {
		return false;
	}
	
	public boolean canMonsterGo() {
		return false;
	}

}
