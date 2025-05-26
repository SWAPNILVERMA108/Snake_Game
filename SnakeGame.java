
package com.snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private static class Tile {
        int x, y;
        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private final int boardWidth = 600;
    private final int boardHeight = 600;
    private final int tileSize = 25;

    private Tile snakeHead;
    private ArrayList<Tile> snakeBody;
    private Tile food;
    private Random random;

    private Timer gameLoop;
    private int velocityX = 0, velocityY = 1;

    public SnakeGame() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<>();
        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        gameLoop = new Timer(200, this);
        gameLoop.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

        g.setColor(Color.green);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);

        g.setColor(Color.blue);
        for (Tile tile : snakeBody) {
            g.fillRect(tile.x * tileSize, tile.y * tileSize, tileSize, tileSize);
        }
    }

    private void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);
    }

    private void move() {
        if (!snakeBody.isEmpty()) {
            for (int i = snakeBody.size() - 1; i > 0; i--) {
                snakeBody.get(i).x = snakeBody.get(i - 1).x;
                snakeBody.get(i).y = snakeBody.get(i - 1).y;
            }
            snakeBody.get(0).x = snakeHead.x;
            snakeBody.get(0).y = snakeHead.y;
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        checkCollisions();
    }

    private void checkCollisions() {
        if (snakeHead.x < 0 || snakeHead.x >= boardWidth / tileSize ||
                snakeHead.y < 0 || snakeHead.y >= boardHeight / tileSize) {
            gameLoop.stop();
            JOptionPane.showMessageDialog(this, "Game Over! You hit the wall.");
        }

        if (snakeHead.x == food.x && snakeHead.y == food.y) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> {
                if (velocityY != 1) {
                    velocityX = 0;
                    velocityY = -1;
                }
            }
            case KeyEvent.VK_DOWN -> {
                if (velocityY != -1) {
                    velocityX = 0;
                    velocityY = 1;
                }
            }
            case KeyEvent.VK_LEFT -> {
                if (velocityX != 1) {
                    velocityX = -1;
                    velocityY = 0;
                }
            }
            case KeyEvent.VK_RIGHT -> {
                if (velocityX != -1) {
                    velocityX = 1;
                    velocityY = 0;
                }
            }
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame sg = new SnakeGame();
        frame.add(sg);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        sg.requestFocus();
    }
}
