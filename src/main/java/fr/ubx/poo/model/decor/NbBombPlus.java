package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;

public class NbBombPlus extends Decor{
private Position position;
	
	@Override
    public String toString() {
        return "NbBombPlus";
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
		player.manage(position);
		player.increaseNbBombs();
	}
}