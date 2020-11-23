package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Position;

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
}