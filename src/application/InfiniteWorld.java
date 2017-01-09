package application;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Arrays;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;

public class InfiniteWorld extends Canvas {
    private static final double WIDTH = 400;
    private static final double HEIGHT = 400;

    private static final KeyCode[] LEFT = { KeyCode.LEFT, KeyCode.A };
    private static final KeyCode[] RIGHT = { KeyCode.RIGHT, KeyCode.D };
    private static final KeyCode[] UP = { KeyCode.UP, KeyCode.W };
    private static final KeyCode[] DOWN = { KeyCode.DOWN, KeyCode.S };
    private static final KeyCode[] ZOOM_IN = { KeyCode.PLUS, KeyCode.PAGE_DOWN };
    private static final KeyCode[] ZOOM_OUT = { KeyCode.MINUS, KeyCode.PAGE_UP };
    private static final KeyCode[] BUILD = { KeyCode.SPACE };
    private static final KeyCode[] DEBUG = { KeyCode.F3 };
    private static final KeyCode[] SPEED = { KeyCode.SHIFT };

    private static final double ZOOM_RATIO = 2;
    private static final long SAVE_INTERVAL = 10000; // 10 seconds

    private Point2D location;
    private DirectionWidget direction;
    private double zoom;
    private double speed;
    private long lastSave;
    private boolean debug;

    public InfiniteWorld() {
	super(WIDTH, HEIGHT);

	this.location = new Point2D(0.5, 0.5);
	this.direction = new DirectionWidget();
	this.zoom = 16;
	this.speed = 0.1;
	this.lastSave = System.currentTimeMillis();
	this.debug = false;

	AnimationTimer timer = new AnimationTimer() {
	    @Override
	    public void handle(long now) {
		tick();
		paint();
	    }
	};
	timer.start();

	setFocusTraversable(true);
	addEventHandler(KeyEvent.KEY_PRESSED, evt -> keyPress(evt));
	addEventHandler(KeyEvent.KEY_RELEASED, evt -> keyRelease(evt));

	try {
	    Files.createDirectory(FileSystems.getDefault().getPath(Cache.getDataDirectory()));
	} catch (IOException e) {
	}
    }

    private void keyPress(KeyEvent event) {
	if (isKey(event, LEFT)) {
	    direction.addLeft();
	}
	if (isKey(event, RIGHT)) {
	    direction.addRight();
	}
	if (isKey(event, UP)) {
	    direction.addUp();
	}
	if (isKey(event, DOWN)) {
	    direction.addDown();
	}
	if (isKey(event, SPEED)) {
	    speed = 1;
	}
    }

    private void keyRelease(KeyEvent event) {
	if (isKey(event, LEFT)) {
	    direction.removeLeft();
	}
	if (isKey(event, RIGHT)) {
	    direction.removeRight();
	}
	if (isKey(event, UP)) {
	    direction.removeUp();
	}
	if (isKey(event, DOWN)) {
	    direction.removeDown();
	}
	if (isKey(event, ZOOM_IN)) {
	    zoom *= ZOOM_RATIO;
	}
	if (isKey(event, ZOOM_OUT)) {
	    zoom /= ZOOM_RATIO;
	}
	if (isKey(event, BUILD)) {
	    buildOrDestroy();
	}
	if (isKey(event, DEBUG)) {
	    debug ^= true;
	}
	if (isKey(event, SPEED)) {
	    speed = 0.1;
	}

    }

    private boolean isKey(KeyEvent event, KeyCode[] keys) {
	return Arrays.asList(keys).contains(event.getCode());
    }

    private void tick() {
	location = location.add(direction.getDirection().getHorizontal() * speed,
		direction.getDirection().getVertical() * speed);
	Cache.addChunk(location.getX(), location.getY());

	int size = Chunk.SIZE;
	for (int y = -size; y <= size; y += size) {
	    for (int x = -size; x <= size; x += size) {
		Cache.addChunk(location.getX() + x, location.getY() + y);
	    }
	}

	if (System.currentTimeMillis() - lastSave > SAVE_INTERVAL) {
	    Cache.save();
	    lastSave = System.currentTimeMillis();
	}
    }

    private Tile getTile() {
	return Cache.getTile(location.getX(), location.getY());
    }

    private void paint() {
	Tile tile;
	GraphicsContext gc = getGraphicsContext2D();
	gc.setTransform(1, 0, 0, 1, 0, 0);
	gc.clearRect(0, 0, getWidth(), getHeight());
	gc.scale(zoom, zoom);
	gc.translate((1 / zoom - 1) * getWidth() / 2, (1 / zoom - 1) * getHeight() / 2);
	gc.setFill(Color.GRAY);
	for (Chunk chunk : Cache.getChunks()) {
	    for (int y = 0; y < chunk.getTiles().length; y++) {
		for (int x = 0; x < chunk.getTiles()[y].length; x++) {
		    tile = chunk.getTiles()[y][x];
		    SimpleColor color = tile.getTerrain().color();
		    gc.setFill(new Color(color.getR(), color.getG(), color.getB(), 1));
		    gc.fillRect(getViewportX(chunk.getX() * Chunk.SIZE + x),
			    getViewportY(chunk.getY() * Chunk.SIZE + y), 1, 1);
		    if (tile.isBuilding()) {
			gc.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.BLACK),
				new Stop(0.5, Color.TRANSPARENT), new Stop(1, Color.BLACK)));
			gc.fillRect(getViewportX(chunk.getX() * Chunk.SIZE + x),
				getViewportY(chunk.getY() * Chunk.SIZE + y), 1, 1);
		    }
		}
	    }
	}
	gc.setFill(Color.RED);
	gc.setFill(new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, new Stop(0, Color.WHITE),
		new Stop(1, Color.RED)));
	gc.fillOval(getViewportX(location.getX() - 0.5), getViewportY(location.getY() - 0.5), 1, 1);

	if (debug) {
	    gc.setTransform(1, 0, 0, 1, 0, 0);

	    StringBuilder display = new StringBuilder();

	    // Coordinates
	    display.append("x: ").append((int) location.getX()).append(", y: ").append((int) location.getY())
		    .append(System.lineSeparator());

	    // Terrain
	    display.append("terrain: ").append(getTile().getTerrain().name()).append(System.lineSeparator());

	    // Cache
	    display.append("cache: ").append(Cache.getChunks().size());

	    gc.setFont(new Font("FreeMono", 16));
	    gc.setFill(Color.BLACK);
	    gc.setStroke(Color.BLACK);
	    gc.setLineWidth(1);

	    gc.fillText(display.toString(), 2, 12);
	    gc.strokeText(display.toString(), 2, 12);
	}
    }

    private double getViewportX(double worldX) {
	return worldX - location.getX() + getWidth() / 2;
    }

    private double getViewportY(double worldY) {
	return worldY - location.getY() + getHeight() / 2;
    }

    private void buildOrDestroy() {
	getTile().setBuilding(!getTile().isBuilding());
    }
}
