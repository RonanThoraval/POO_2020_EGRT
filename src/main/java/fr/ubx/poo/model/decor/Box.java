package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;

public class Box extends Decor {
	private Position position;
	
	@Override
    public String toString() {
        return "Box";
    }
	
	public Position getPosition() {
		return this.position;
	}
	
	public void setPosition(Position p) {
		this.position=p;
	}

	@Override
	public boolean canPlayerGo(Player player) {
		Position newPos=player.getDirection().nextPosition(position);
		return (player.getGame().getWorld().isEmpty(newPos) && newPos.inside(player.getGame().getWorld().dimension));
	}

	@Override
	public void doPlayerGo(Player player) {
		player.manageBox(position);
	}
}

