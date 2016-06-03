package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Cache {
    public static final int MAX_SIZE = 100;
    private static List<Chunk> chunks = new ArrayList<Chunk>();

    public static void addChunk(double x, double y) {
	Chunk chunk = new Chunk(x, y);
	if (chunks.contains(chunk)) {
	    return;
	}
	Chunk saved = fromFile(chunk);
	if (saved != null) {
	    chunk = saved;
	}
	addChunk(chunk);
    }

    public static List<Chunk> getChunks() {
	return new LinkedList<Chunk>(chunks);
    }

    public static Chunk getChunk(double x, double y) {
	for (Chunk chunk : chunks) {
	    if (chunk.contains(x, y)) {
		return chunk;
	    }
	}
	Chunk chunk = new Chunk(x, y);
	addChunk(chunk);
	return chunk;
    }

    public static String getDataDirectory() {
	String directory = System.getenv("AppData");
	if (directory != null) {
	    return directory + "/infinite-world";
	} else {
	    return System.getProperty("user.home") + "/.infinite-world/";
	}
    }

    public static void save() {
	for (Chunk chunk : chunks) {
	    if (chunk.isModified() || isInFile(chunk)) {
		toFile(chunk);
	    }
	}
	clean();
    }

    public static boolean isInFile(Chunk chunk) {
	File file = new File(getPath(chunk));
	return file.exists();
    }

    public static void clean() {
	File directory = new File(getDataDirectory());
	for (File file : directory.listFiles()) {
	    FileInputStream stream;
	    ObjectInputStream in;
	    try {
		stream = new FileInputStream(file);
		in = new ObjectInputStream(stream);
		Chunk chunk = (Chunk) in.readObject();
		if (!chunk.isModified()) {
		    file.delete();
		}
		in.close();
		stream.close();
	    } catch (IOException | ClassNotFoundException e) {
	    }
	}
    }

    public static int getSaveFileSize() {
	File directory = new File(getDataDirectory());
	int size = 0;
	for (File file : directory.listFiles()) {
	    size += file.length();
	}
	return size;
    }

    private static Chunk fromFile(Chunk reference) {
	try {
	    FileInputStream stream = new FileInputStream(getPath(reference));
	    ObjectInputStream in = new ObjectInputStream(stream);
	    Chunk chunk = (Chunk) in.readObject();

	    in.close();
	    stream.close();

	    return chunk;
	} catch (IOException e) {
	    return null;
	} catch (ClassNotFoundException e) {
	    return null;
	}
    }

    private static void toFile(Chunk chunk) {
	try {
	    FileOutputStream stream = new FileOutputStream(getPath(chunk));
	    ObjectOutputStream out = new ObjectOutputStream(stream);
	    out.writeObject(chunk);
	    out.close();
	    stream.close();
	} catch (IOException e) {
	}
    }

    private static String getPath(Chunk chunk) {
	StringBuilder str = new StringBuilder();
	str.append(getDataDirectory()).append(chunk.getX()).append('_').append(chunk.getY()).append(".iw");
	return str.toString();
    }

    private static void addChunk(Chunk chunk) {
	chunks.add(chunk);
	while (chunks.size() > MAX_SIZE) {
	    toFile(chunks.get(0));
	    chunks.remove(0);
	}
    }

    public static Tile getTile(double x, double y) {
	return getChunk(x, y).getTile(Chunk.getTileCoordinate(x), Chunk.getTileCoordinate(y));
    }

    public static void setTile(Tile tile, double x, double y) {
	getChunk(x, y).setTile(tile, Chunk.getTileCoordinate(x), Chunk.getTileCoordinate(y));
    }
}
