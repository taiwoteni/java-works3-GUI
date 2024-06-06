package com.example.gui;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Snakegame extends Application {
    //variable
    static int speed = 5;
    static int foodcolor = 0;
    static int width = 20;
    static int height = 20;
    static int foodX = 0;
    static int foodY = 0;
    static int cornersize = 25;
    static List<Corner> snake = new ArrayList<>();
    static Dir direction = Dir.left;
    static boolean gameOver = false;
    static Random rand = new Random();

    //controls
    public enum Dir {
        left, right, up, down
    }

    public static class Corner {
        int x;
        int y;

        public Corner(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        newFood();

        VBox root = new VBox();
        root.setFillWidth(true);
        root.setMaxWidth(Double.MAX_VALUE);
        root.setMaxHeight(Double.MAX_VALUE);


        Canvas c = new Canvas(800, 800);
        c.maxWidth(Double.MAX_VALUE);
        c.maxHeight(Double.MAX_VALUE);
        GraphicsContext gc = c.getGraphicsContext2D();
        root.getChildren().add(c);

        startTickerAnimation(gc);

        Scene scene = new Scene(root, 800,800);

        //control
        addGameInputListener(scene);

        //add start snake parts
        snake.add(new Corner(width/2,height/2));
        snake.add(new Corner(width/2,height/2));
        snake.add(new Corner(width/2,height/2));

        stage.setScene(scene);
        stage.setTitle("🐍GREEN ANACONDA🐉");
        stage.show();
    }

    private void startTickerAnimation(GraphicsContext gc){
        new AnimationTimer() {
            long lastTick = 0;

            public void handle(long now) {
                if (lastTick == 0) {
                    lastTick = now;
                    tick(gc);
                    return;
                }

                if (now - lastTick > 1000000000 / speed) {
                    lastTick = now;
                    tick(gc);
                }
            }
        }.start();
    }
    private void addGameInputListener(Scene scene){
        scene.addEventFilter(KeyEvent.KEY_PRESSED, key ->{
            if (key.getCode()== KeyCode.UP){
                direction = Dir.up;
            }
            if (key.getCode()== KeyCode.LEFT){
                direction = Dir.left;
            }
            if (key.getCode()== KeyCode.DOWN){
                direction = Dir.down;
            }
            if (key.getCode()== KeyCode.RIGHT){
                direction = Dir.right;
            }

        });

    }

    //tick
    public static void tick (GraphicsContext gc){
        if (gameOver){
            gc.setFill(Color.RED);
            gc.setFont(new Font("",50));
            gc.fillText("GAME OVER😵‍💫",90,250);
            return;
        }
        for (int i = snake.size()-1; i>=1; i--){
            snake.get(i).x = snake.get(i-1).x;
            snake.get(i).y = snake.get(i-1).y;
        }


        switch (direction) {
            case up -> {
                snake.get(0).y--;
                if (snake.get(0).y < 0) {
                    gameOver = true;
                }
            }
            case down -> {
                snake.get(0).y++;
                if (snake.get(0).y > height) {
                    gameOver = true;
                }
            }
            case left -> {
                snake.get(0).x--;
                if (snake.get(0).x < 0) {
                    gameOver = true;
                }
            }
            case right -> {
                snake.get(0).x++;
                if (snake.get(0).x > width) {
                    gameOver = true;
                }
            }
        }

        //eat food
        if (foodX==snake.get(0).x && foodY==snake.get(0).y){
            snake.add(new Corner(-1,-1));
            newFood();
        }
        //self destroy
        for (int i = 1; i<snake.size();i++){
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y){
                gameOver=true;
            }
        }

        //fill
        //background
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,Double.MAX_VALUE,Double.MAX_VALUE);

        //score
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("",30));
        gc.fillText("Score: " + (speed - 6),10,30);


        //random foodcolor
        Color cc = Color.WHITE;

        switch (foodcolor){
            case 0: cc = Color.PURPLE;
            break;
            case 1: cc = Color.LIGHTBLUE;
            break;
            case 2: cc = Color.YELLOW;
            break;
            case 3: cc = Color.PINK;
            break;
            case 4: cc = Color.ORANGE;
            break;
        }
            gc.setFill(cc);
            gc.fillOval(foodX*cornersize,foodY*cornersize,cornersize,cornersize);

            //snake
            for (Corner c:snake){
                gc.setFill(Color.LIGHTGREEN);
                gc.fillRect(c.x*cornersize,c.y*cornersize,cornersize-1,cornersize-1);
                gc.setFill(Color.GREEN);
                gc.fillRect(c.x*cornersize,c.y*cornersize,cornersize-2,cornersize-2);


            }

    }

    //food
    public static void newFood(){
       start: while (true){
         foodX = rand.nextInt(width);
         foodY = rand.nextInt(height);

         for (Corner c:snake){
            if (c.x==foodX && c.y==foodY){
                continue start;
            }
         }
         foodcolor=rand.nextInt(5);
         speed++;
         break;

       }
    }


}
