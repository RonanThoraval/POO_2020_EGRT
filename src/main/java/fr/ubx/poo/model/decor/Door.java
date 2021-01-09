package fr.ubx.poo.model.decor;

import java.io.IOException;
import fr.ubx.poo.model.go.character.Player;

public class Door extends Decor {
	
	
	/**
	 *  If state=1, DoorNextClosed
	 * If state=2, DoorPrevOpened
	 * If state=3, DoorNextOpened
	 */
	private int state;
	
	public Door(int etat) {
		this.state=etat;
	}
	
	@Override
    public String toString() {
        return "NbBombMoins";
    }
	
	/**
	 * 
	 * @return state
	 * Getter for state.
	 */
	public int getState() {
		return state;
	}
	
	/**
	 * 
	 * @param state
	 * Setter for state.
	 */
	public void setState(int state) {
		this.state=state;
	}

	@Override
	public boolean canPlayerGo(Player player) {
		Door d=(Door) player.getGame().getWorld().get(player.getDirection().nextPosition(player.getPosition()));
		return (d.getState()!=1);
	}

	@Override
	public void doPlayerGo(Player player) throws IOException {
    	if (state==3) {
    		player.getGame().changeLevel("next");
    		player.getGame().getWorld().setChanged(true);
    	}
    	if (state==2) {
    		player.getGame().changeLevel("prev");
    		player.getGame().getWorld().setChanged(true);
    	}
	}
	
	public boolean canExplose() {
		return false;
	}
	
	public boolean canMonsterGo() {
		return false;
	}

}
