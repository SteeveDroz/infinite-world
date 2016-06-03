package application;

public class DirectionWidget {
    private static final Direction[][] DIRECTIONS = { { Direction.UP_LEFT, Direction.UP, Direction.UP_RIGHT },
	    { Direction.LEFT, Direction.NONE, Direction.RIGHT },
	    { Direction.DOWN_LEFT, Direction.DOWN, Direction.DOWN_RIGHT } };

    private int horizontal;
    private int vertical;

    public DirectionWidget() {
	this.horizontal = 0;
	this.vertical = 0;
    }

    public void addLeft() {
	horizontal = -1;
    }

    public void addRight() {
	horizontal = 1;
    }

    public void addUp() {
	vertical = -1;
    }

    public void addDown() {
	vertical = 1;
    }

    public void removeLeft() {
	horizontal = 0;
    }

    public void removeRight() {
	horizontal = 0;
    }

    public void removeUp() {
	vertical = 0;
    }

    public void removeDown() {
	vertical = 0;
    }

    public Direction getDirection() {
	return DIRECTIONS[vertical + 1][horizontal + 1];
    }
}
