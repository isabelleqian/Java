package core;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import utils.FileUtils;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    private Integer seed = 0;
    private boolean lang = false;

    public void initialize() {
        mainMenu();
        menuCheck();
    }

    private void mainMenu() {
        StdDraw.setCanvasSize(800, 800);
        StdDraw.setFont(new Font(Font.SERIF, Font.PLAIN, 80));
        StdDraw.clear(StdDraw.BOOK_BLUE);
        StdDraw.text(.5, .9, "CS61B: BYOW");
        StdDraw.setFont(new Font(Font.SERIF, Font.PLAIN, 40));
        StdDraw.text(.5, .6, "(N) New Game");
        StdDraw.text(.5, .5, "(L) Load Game");
        StdDraw.text(.5, .4, "(Q) Quit Game");
        StdDraw.text(.5, .1, "(C) Change Language");
    }

    private void menuCheck() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char keyPressed = StdDraw.nextKeyTyped();
                if (keyPressed == 'n' || keyPressed == 'N') {
                    newWorld();
                }
                if (keyPressed == 'l' || keyPressed == 'L') {
                    loadFile();
                    break;
                }
                if (keyPressed == 'q' || keyPressed == 'Q') {
                    System.exit(0);
                    break;
                }
                if (keyPressed == 'c' || keyPressed == 'C') {
                    changeLang();
                }
            }
        }
    }

    private void changeLang() {
        if (lang) {
            StdDraw.setFont(new Font(Font.SERIF, Font.PLAIN, 80));
            StdDraw.clear(StdDraw.BOOK_BLUE);
            StdDraw.text(.5, .9, "CS61B: 最後的游戲");
            StdDraw.setFont(new Font(Font.SERIF, Font.PLAIN, 40));
            StdDraw.text(.5, .6, "(N) 新游戯");
            StdDraw.text(.5, .5, "(L) 接著玩");
            StdDraw.text(.5, .4, "(Q) 放棄");
            StdDraw.text(.5, .1, "(C) 語言");
        } else {
            StdDraw.setFont(new Font(Font.SERIF, Font.PLAIN, 80));
            StdDraw.clear(StdDraw.BOOK_BLUE);
            StdDraw.text(.5, .9, "CS61B: BYOW");
            StdDraw.setFont(new Font(Font.SERIF, Font.PLAIN, 40));
            StdDraw.text(.5, .6, "(N) New Game");
            StdDraw.text(.5, .5, "(L) Load Game");
            StdDraw.text(.5, .4, "(Q) Quit Game");
            StdDraw.text(.5, .1, "(C) Change Language");
        }
        lang = !lang;
    }

    private void newWorld() {
        while (StdDraw.hasNextKeyTyped()) {
            StdDraw.nextKeyTyped();
        }
        StdDraw.clear(StdDraw.BOOK_BLUE);
        StdDraw.setFont(new Font(Font.SERIF, Font.PLAIN, 60));
        if (lang) {
            StdDraw.text(.5, .5, "輸入數字,結束按S");
        } else {
            StdDraw.text(.5, .5, "Enter seed followed by S");
        }
        char keyPressed = ' ';
        while (true){
            if (StdDraw.hasNextKeyTyped()){
                keyPressed = StdDraw.nextKeyTyped();
                if (keyPressed == 's' || keyPressed == 'S'){
                    break;
                }
                seed = seed * 10 + Character.getNumericValue(keyPressed);
                StdDraw.clear(StdDraw.BOOK_BLUE);
                StdDraw.setFont(new Font(Font.SERIF, Font.PLAIN, 60));
                if (lang) {
                    StdDraw.text(.5, .5, "輸入數字,結束按S");
                } else {
                    StdDraw.text(.5, .5, "Enter seed followed by S");
                }
                StdDraw.setFont(new Font(Font.SERIF, Font.PLAIN, 60));
                StdDraw.text(.5, .3, Integer.toString(seed));

            }
        }
        World world = new World(seed);
    }

    private void loadFile() {
        File f = new File("save.txt");
        if(!f.isFile()) {
            System.exit(0);
        }

        List<Integer> saveLines = new ArrayList<>();
        In save = new In("save.txt");
        while (save.hasNextLine()) {
            saveLines.add(Integer.parseInt(save.readLine()));
        }
        World world = new World(saveLines);

    }

}
