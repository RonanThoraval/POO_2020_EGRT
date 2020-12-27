package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.Monster;
import fr.ubx.poo.model.go.character.Player;

public class Box extends Decor {

	@Override
    public String toString() {
        return "Box";
    }

	@Override
	public boolean canPlayerGo(Player player) {
		Position newPos = player.getDirection().nextPosition(player.getPosition());
		Position newPos2=player.getDirection().nextPosition(newPos);
		for (Monster monster : player.getGame().getMonsters() ) {
        	if ( monster.getPosition().equals(newPos2)) {
        		return false;
        	}
        }
		return (player.getGame().getWorld().isEmpty(newPos2) && newPos2.inside(player.getGame().getWorld().dimension));
	}

	@Override
	public void doPlayerGo(Player player) {
		player.manageBox(player.getDirection().nextPosition(player.getPosition()));
	}
}