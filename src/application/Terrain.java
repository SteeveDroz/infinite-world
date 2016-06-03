package application;

import java.util.ArrayList;
import java.util.List;

public enum Terrain {
    VOID {
	@Override
	public SimpleColor color() {
	    return new SimpleColor(1, 1, 1);
	}
    },
    GRASS {
	@Override
	public SimpleColor color() {
	    return new SimpleColor(0, 0.5, 0);
	}
    },
    STONE {
	@Override
	public SimpleColor color() {
	    return new SimpleColor(0.5, 0.5, 0.5);
	}
    },
    MEADOW {
	@Override
	public SimpleColor color() {
	    return new SimpleColor(0, 0.8, 0);
	}
    },
    WATER {
	@Override
	public SimpleColor color() {
	    return new SimpleColor(0, 0, 1);
	}
    },
    SAND {
	@Override
	public SimpleColor color() {
	    return new SimpleColor(1, 1, 0);
	}
    };

    public static List<Terrain> all() {
	List<Terrain> all = new ArrayList<Terrain>();
	for (Terrain terrain : values()) {
	    all.add(terrain);
	}
	return all;
    }

    public abstract SimpleColor color();
}
