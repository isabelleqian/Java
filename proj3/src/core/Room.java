package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Room {

    private TETile[][] map;
    public int x;
    public int xc;
    public int y;
    public int yc;
    public int w;
    public int h;
    public Set<Room> adj;

    public Room(TETile[][] m, int x, int y, int w, int h) {
        Random random = new Random(h);
        this.map = m;
        this.x = x;
        this.xc = x + random.nextInt(1, w - 1);
        this.y = y;
        this.yc = y + random.nextInt(1, h - 1);
        this.w = w;
        this.h = h;
        adj = new HashSet<>();
    }

    public void createRoom() {
        for (int i = x; i < x + w; i++) {
            for (int j = y; j < y + h; j++) {
                if (!map[i][j].equals(Tileset.FLOOR) && ((i == x) || (j == y)
                        || (i == x + w - 1) || (j == y + h - 1))) {
                    map[i][j] = Tileset.WALL;
                } else {
                    map[i][j] = Tileset.FLOOR;
                }
            }
        }
    }

    public void connect(Room r) {
        if (!adj.contains(r) && !r.equals(this)) {
            adj.add(r);
            r.connect(this);
        }
    }

    public Set<Room> connected() {
        return adj;
    }

    public static boolean intersect(Room r1, Room r2) {
        return !(r1.x + r1.w <= r2.x || r2.x + r2.w <= r1.x || r1.y + r1.h <= r2.y || r2.y + r2.h <= r1.y);
    }

}
