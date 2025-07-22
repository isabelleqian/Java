package core;

import tileengine.TETile;
import tileengine.Tileset;

public class Player {
    private int x;
    private int y;
    private TETile[][] map;

    public Player(int x, int y, TETile[][] m) {
        this.x = x;
        this.y = y;
        map = m;
        map[x][y] = Tileset.AVATAR;
    }

    public void up() {
        if (!map[x][y+1].equals(Tileset.WALL)) {
            map[x][y] = Tileset.FLOOR;
            map[x][y+1] = Tileset.AVATAR;
            y++;
        }
    }

    public void left() {
        if (!map[x-1][y].equals(Tileset.WALL)) {
            map[x][y] = Tileset.FLOOR;
            map[x-1][y] = Tileset.AVATAR;
            x--;
        }
    };

    public void right() {
        if (!map[x+1][y].equals(Tileset.WALL)) {
            map[x][y] = Tileset.FLOOR;
            map[x+1][y] = Tileset.AVATAR;
            x++;
        }
    }

    public void down() {
        if (!map[x][y-1].equals(Tileset.WALL)) {
            map[x][y] = Tileset.FLOOR;
            map[x][y-1] = Tileset.AVATAR;
            y--;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
