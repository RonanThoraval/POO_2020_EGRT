package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Position;

public class Door extends Decor {
	private Position position;
	
	// Si state=1, DoorNextClosed
	// Si state=2, DoorPrevOpened
	// Si state=3, DoorNextOpened
	private int state;
	
	public Door(int etat) {
		this.state=etat;
	}
	
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
	
	public int getState() {
		return state;
	}
	
	public void setState(int state) {
		this.state=state;
	}

}
