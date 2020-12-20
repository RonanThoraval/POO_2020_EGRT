package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.Box;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Door;
import fr.ubx.poo.model.decor.DoorClosed;
import fr.ubx.poo.model.decor.Heart;
import fr.ubx.poo.model.decor.Key;
import fr.ubx.poo.model.decor.NbBombMoins;
import fr.ubx.poo.model.decor.NbBombPlus;
import fr.ubx.poo.model.decor.Princess;
import fr.ubx.poo.model.decor.RangeBombMoins;
import fr.ubx.poo.model.decor.RangeBombPlus;
import fr.ubx.poo.model.decor.Stone;
import fr.ubx.poo.model.decor.Tree;

import java.util.Hashtable;
import java.util.Map;

public class WorldBuilder {
    private final Map<Position, Decor> grid = new Hashtable<>();

    private WorldBuilder() {
    }

    public static Map<Position, Decor> build(WorldEntity[][] raw, Dimension dimension) {
        WorldBuilder builder = new WorldBuilder();
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                Position pos = new Position(x, y);
                Decor decor = processEntity(raw[y][x]);
                if (decor != null)
                    builder.grid.put(pos, decor);
            }
        }
        return builder.grid;
    }

    private static Decor processEntity(WorldEntity entity) {
        switch (entity) {
            case Stone:
                return new Stone();
            case Tree:
                return new Tree();
            case Box :
            	return new Box();
            case Heart :
            	return new Heart();
            case Key :
            	return new Key();
            case BombNumberInc :
            	return new NbBombPlus();
            case BombNumberDec :
            	return new NbBombMoins();
            case BombRangeInc :
            	return new RangeBombPlus();
            case BombRangeDec :
            	return new RangeBombMoins();
            case DoorNextClosed :
            	return new Door(1);
            case DoorPrevOpened :
            	return new Door(2);
            case DoorNextOpened :
            	return new Door(3);
            case Princess :
            	return new Princess();
            default:
                return null;
        }
    }
}
