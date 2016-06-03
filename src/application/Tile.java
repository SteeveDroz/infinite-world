package application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tile implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean building;

    private static Random random = new Random();
    private Terrain terrain;

    public Tile(Terrain terrain) {
	this.terrain = terrain;
	this.building = false;
    }

    public static Tile getRandomTileWith(Terrain... authorized) {
	ArrayList<Terrain> authorizedList = new ArrayList<Terrain>();
	for (Terrain terrain : authorized) {
	    authorizedList.add(terrain);
	}
	return getRandomTileWith(authorizedList);
    }

    public static Tile getRandomTileWith(List<Terrain> authorized) {
	if (authorized.size() == 0) {
	    return null;
	}
	Tile tile;
	do {
	    tile = new Tile(Terrain.values()[random.nextInt(Terrain.values().length)]);
	} while (!authorized.contains(tile.getTerrain()));
	return tile;
    }

    public static Tile getRandomTile() {
	return getRandomTileWith(Terrain.values());
    }

    public Terrain getTerrain() {
	return terrain;
    }

    public boolean isBuilding() {
	return building;
    }

    public void setTerrain(Terrain terrain) {
	this.terrain = terrain;
    }

    public void setBuilding(boolean building) {
	this.building = building;
    }
}
