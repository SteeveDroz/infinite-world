package application;

import java.io.Serializable;
import java.util.List;

public class Chunk implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int SIZE = 16;

    private int x;
    private int y;
    private Tile[][] tiles;

    public Chunk(double x, double y) {
	this.x = (int) Math.floor(x / SIZE);
	this.y = (int) Math.floor(y / SIZE);
	this.tiles = new Tile[SIZE][SIZE];
	fill();
    }

    @Override
    public boolean equals(Object obj) {
	try {
	    Chunk chunk = (Chunk) obj;
	    return this.x == chunk.x && this.y == chunk.y;
	} catch (ClassCastException exception) {
	    return false;
	}
    }

    public void fill() {
	for (int i = 0; i < tiles.length; i++) {
	    for (int j = 0; j < tiles[i].length; j++) {

		List<Terrain> allButVoid = Terrain.all();
		allButVoid.remove(Terrain.VOID);

		tiles[i][j] = Tile.getRandomTileWith(allButVoid);
	    }
	}
    }

    public boolean contains(double x, double y) {
	return (int) Math.floor(x / SIZE) == this.x && (int) Math.floor(y / SIZE) == this.y;
    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }

    public Tile[][] getTiles() {
	return tiles;
    }

    public static int getTileCoordinate(double coordinate) {
	return (((int) Math.floor(coordinate) % SIZE) + SIZE) % SIZE;
    }

    public Tile getTile(int tileX, int tileY) {
	return tiles[tileY][tileX];
    }

    public boolean isModified() {
	for (int y = 0; y < tiles.length; y++) {
	    for (int x = 0; x < tiles[y].length; x++) {
		if (tiles[y][x].isBuilding()) {
		    return true;
		}
	    }
	}
	return false;
    }

    public void setTile(Tile tile, int tileX, int tileY) {
	tiles[tileY][tileX] = tile;
    }
}
