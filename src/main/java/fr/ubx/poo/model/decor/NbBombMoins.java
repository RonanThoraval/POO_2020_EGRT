package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;

public class NbBombMoins extends Decor{
private Position position;
	
	@Override
    public String toString() {
        return "NbBombMoins";
    }
	
	public Position getPosition() {
		return this.position;
	}
	
	public void setPosition(Position p) {
		this.position=p;
	}
	
	public boolean canPlayerGo(Player player) {
		return true;
	}
	
	public void doPlayerGo(Player player) {
		player.getGame().getWorld().clear(position);
		if(player.getNbBombs()!=1) {
			player.decreaseNbBombs();
		}
	}
}