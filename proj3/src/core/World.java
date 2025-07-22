package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import utils.FileUtils;

import java.io.*;
import java.util.*;

public class World {

    private static final int WIDTH = 120;
    private static final int HEIGHT = 70;
    private static final int MAX_NUM_ROOMS = 30;
    private static final int MIN_ROOM_SIZE = 5;
    private static final int MAX_ROOM_SIZE = 20;

    private int seed;
    private TERenderer renderer;
    public List<TETile[][]> maps;
    private Random random;
    private List<Room> rooms;
    private Player player;
    private int light; // 0 is on 1 is off

    public World(int s) {
        renderer = new TERenderer();
        renderer.initialize(WIDTH, HEIGHT);
        seed = s;
        maps = new ArrayList<>();
        for (int i = 0; i < 2; i++){
            maps.add(new TETile[WIDTH][HEIGHT]);
        }
        random = new Random(seed);
        rooms = new ArrayList<>();
        light = 0;

        emptyWorld(0);
        generateRooms();
        connectRooms();
        while(!allRoomsConnected()){
            connectRooms();
        }
        hallGenerator();

        Room room = rooms.getFirst();
        player = new Player(room.xc, room.yc, maps.getFirst());
        render();

        while (true) {
            move();
        }
    }

    public World(List<Integer> s) {
        renderer = new TERenderer();
        renderer.initialize(WIDTH, HEIGHT);
        seed = s.get(0);
        maps = new ArrayList<>();
        for (int i = 0; i < 2; i++){
            maps.add(new TETile[WIDTH][HEIGHT]);
        }
        random = new Random(seed);
        rooms = new ArrayList<>();
        light = 0;


        emptyWorld(0);
        generateRooms();
        connectRooms();
        while(!allRoomsConnected()){
            connectRooms();
        }
        hallGenerator();

        player = new Player(s.get(1), s.get(2), maps.getFirst());
        render();

        while (true) {
            move();
        }

    }

    private void emptyWorld(int i) {
        for (int w = 0; w < WIDTH; w++) {
            for (int h = 0; h < HEIGHT; h++) {
                maps.get(i)[w][h] = Tileset.NOTHING;
            }
        }
    }

    public void generateRooms() {
        for (int i = 0 ; i < MAX_NUM_ROOMS; i++){
            int x = random.nextInt(WIDTH - MAX_ROOM_SIZE);
            int y = random.nextInt(HEIGHT - MAX_ROOM_SIZE);
            int w = random.nextInt(MIN_ROOM_SIZE, MAX_ROOM_SIZE);
            int h = random.nextInt(MIN_ROOM_SIZE, MAX_ROOM_SIZE);
            Room r = new Room(maps.getFirst(), x, y, w, h);
            boolean intersect = false;
            for (Room room: rooms){
                if (Room.intersect(room, r)) {
                    intersect = true;
                    break;
                }
            }
            if (!intersect) {
                rooms.add(r);
            }
        }
        for (Room room: rooms) {
            room.createRoom();
        }
    }

    public void connectRooms() {
        for(Room room: rooms){
            for (int i = 0 ; i < 1; i ++) {
                room.connect(rooms.get(random.nextInt(rooms.size())));
            }
        }
    }

    public boolean allRoomsConnected() {
        if (rooms.isEmpty()) {
            return true;
        }

        Room startRoom = rooms.getFirst();
        Set<Room> visited = new HashSet<>();
        Stack<Room> stack = new Stack<>();
        stack.push(startRoom);

        while (!stack.isEmpty()) {
            Room current = stack.pop();
            if (!visited.contains(current)) {
                visited.add(current);
                for (Room neighbor : current.connected()) {
                    if (!visited.contains(neighbor)) {
                        stack.push(neighbor);
                    }
                }
            }
        }

        return visited.size() == rooms.size();
    }

    public void hallGenerator() {
        if (rooms.isEmpty()) {
            return;
        }
        Room currentRoom;
        Set<Room> visited = new HashSet<>();
        Queue<Room> queue = new LinkedList<>();
        queue.add(rooms.getFirst());
        while (!queue.isEmpty()) {
            currentRoom = queue.poll();
            visited.add(currentRoom);
            for (Room room : currentRoom.connected()) {
                hallGeneratorHelper(currentRoom, room);
                if (!visited.contains(room)) {
                    queue.add(room);
                }
            }
        }
    }


    private void hallGeneratorHelper(Room r1, Room r2) {
        HashMap<Integer, Integer> xToY = new HashMap<>();
        HashMap<Integer, Integer> yToX = new HashMap<>();
        xToY.put(r1.xc, r1.yc);
        xToY.put(r2.xc, r2.yc);

        for (int x = Math.min(r1.xc, r2.xc); x <= Math.max(r1.xc, r2.xc); x++) {
            for (int y = xToY.get(Math.min(r1.xc, r2.xc)) - 1; y <= xToY.get(Math.min(r1.xc, r2.xc)) + 1; y++){
                if(y == xToY.get(Math.min(r1.xc, r2.xc))){
                    maps.getFirst()[x][y] = Tileset.FLOOR;
                } else if (maps.getFirst()[x][y].equals((Tileset.NOTHING))) {
                    maps.getFirst()[x][y] = Tileset.WALL;
                }
            }
        }
        for (int y = Math.min(r1.yc, r2.yc); y <= Math.max(r1.yc, r2.yc); y++) {
            for (int x = Math.max(r1.xc, r2.xc) - 1; x <= Math.max(r1.xc, r2.xc) + 1; x++){
                if(x == Math.max(r1.xc, r2.xc)){
                    maps.getFirst()[x][y] = Tileset.FLOOR;
                } else if (maps.getFirst()[x][y].equals((Tileset.NOTHING))) {
                    maps.getFirst()[x][y] = Tileset.WALL;
                }
            }
        }
    }

    public List<Room> getRooms() {
        return rooms;
    }

    private void move() {
        if (StdDraw.hasNextKeyTyped()) {
            char keyPressed = StdDraw.nextKeyTyped();
            if (keyPressed == 'w' || keyPressed == 'W') {
                player.up();
            }
            if (keyPressed == 'a' || keyPressed == 'A') {
                player.left();
            }
            if (keyPressed == 'd' || keyPressed == 'D') {
                player.right();
            }
            if (keyPressed == 's' || keyPressed == 'S') {
                player.down();
            }
            if (keyPressed == 'l' || keyPressed == 'L') {
                light = 1 - light;
            }
            if (keyPressed == ':') {
                while (true) {
                    if (StdDraw.hasNextKeyTyped()) {
                        keyPressed = StdDraw.nextKeyTyped();
                        if (keyPressed == 'q' || keyPressed == 'Q') {
                            saveAndQuit();
                        } else {
                            break;
                        }
                    }
                }
            }
            render();
        }
    }

    private void saveAndQuit() {
        File file = new File("save.txt");
        String content = seed + "\n" + player.getX() + "\n" + player.getY();

        FileUtils.writeFile("save.txt", content);
        System.exit(0);

    }

    private void render() {
        emptyWorld(1);
        for (int i = -5; i <= 5; i ++) {
            for (int j = -5; j <= 5; j ++) {
                int x = player.getX() + i;
                int y = player.getY() + j;
                if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
                    maps.get(1)[x][y] = maps.get(0)[x][y];
                }
            }
        }
        renderer.renderFrame(maps.get(light));
    }
    

}
