package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;

public class Princess extends Decor {
	private Position position;
	
	@Override
    public String toString() {
        return "Princess";
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

	@Override
	public void doPlayerGo(Player player) {
		// TODO Auto-generated method stub
		
	}
}
