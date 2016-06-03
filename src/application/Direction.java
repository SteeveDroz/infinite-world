package application;

public enum Direction {
    UP_LEFT {
	@Override
	public int getHorizontal() {
	    return -1;
	}

	@Override
	public int getVertical() {
	    return -1;
	}
    },
    UP {
	@Override
	public int getHorizontal() {
	    return 0;
	}

	@Override
	public int getVertical() {
	    return -1;
	}
    },
    UP_RIGHT {
	@Override
	public int getHorizontal() {
	    return 1;
	}

	@Override
	public int getVertical() {
	    return -1;
	}
    },
    LEFT {
	@Override
	public int getHorizontal() {
	    return -1;
	}

	@Override
	public int getVertical() {
	    return 0;
	}
    },
    NONE {
	@Override
	public int getHorizontal() {
	    return 0;
	}

	@Override
	public int getVertical() {
	    return 0;
	}
    },
    RIGHT {
	@Override
	public int getHorizontal() {
	    return 1;
	}

	@Override
	public int getVertical() {
	    return 0;
	}
    },
    DOWN_LEFT {
	@Override
	public int getHorizontal() {
	    return -1;
	}

	@Override
	public int getVertical() {
	    return 1;
	}
    },
    DOWN {
	@Override
	public int getHorizontal() {
	    return 0;
	}

	@Override
	public int getVertical() {
	    return 1;
	}
    },
    DOWN_RIGHT {
	@Override
	public int getHorizontal() {
	    return 1;
	}

	@Override
	public int getVertical() {
	    return 1;
	}
    };

    public abstract int getHorizontal();

    public abstract int getVertical();
}
