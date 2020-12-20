package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Position;

public class Door extends Decor {
	private Position position;
	private int etat;
	
	public Door(int etat) {
		this.position=position;
		this.etat=etat;
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
	
	public int getEtat() {
		return etat;
	}
	
	public void setEtat(int etat) {
		this.etat=etat;
	}

}
