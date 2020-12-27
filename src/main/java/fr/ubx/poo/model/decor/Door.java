package fr.ubx.poo.model.decor;

import java.io.IOException;
import fr.ubx.poo.model.go.character.Player;

public class Door extends Decor {
	
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
	
	public int getState() {
		return state;
	}
	
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
	
	

}
