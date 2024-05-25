package snake;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener{
	
	private class Tile{
		int x;
		int y;
		Tile(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
	
	int boardW;
	int boardH;
	int tileS = 25;
	
	Tile snakeHead;
	ArrayList<Tile> snakeBody;
	
	Tile food;
	
	Random random;
	
	//game logic
	Timer gameLoop;
	int veloX;
	int veloY;
	boolean gameOver=false;
	
	
	SnakeGame(int boardW, int boardH){
		this.boardH = boardH;
		this.boardW = boardW;
		setPreferredSize(new Dimension(this.boardW, this.boardH));
		setBackground(Color.black);
		addKeyListener(this);
		setFocusable(true);
		
		snakeHead = new Tile(5,5);
		snakeBody = new ArrayList<Tile>();
		
		food = new Tile(0, 0);
		random = new Random();
		placeFood();
		
		veloX=0;
		veloY=1;
		
		gameLoop = new Timer(100,this);
		gameLoop.start();
	}
	
	public void placeFood() {
		food.x = random.nextInt(boardW/tileS);
		food.y = random.nextInt(boardH/tileS);
	}

	public boolean tileCol(Tile t1, Tile t2) {
		return t1.x==t2.x&&t1.y==t2.y;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
		
	}
	public void draw(Graphics g) {
		g.setColor(Color.red);
		g.fill3DRect(tileS*food.x, tileS*food.y, tileS, tileS, true);
		
		//Snake
		g.setColor(Color.green);
		g.fill3DRect(tileS*snakeHead.x, tileS*snakeHead.y, tileS, tileS, true);
		
		//Snake Body
		for(int i=0; i<snakeBody.size();i++) {
			Tile snakePart = snakeBody.get(i);
			g.fill3DRect(tileS*snakePart.x, tileS*snakePart.y, tileS, tileS, true);
		}
		
		//Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileS - 16, tileS);
        }
        else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileS - 16, tileS);
        }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		move();
		repaint();// calls draw again and again
		if(gameOver) {
			gameLoop.stop();
		}
		
	}

	public void move() {
		if(tileCol(snakeHead, food)) {
			snakeBody.add(new Tile(food.x, food.y));
			placeFood();
		}
		
		for(int i=snakeBody.size()-1;i>=0;i--) {
			Tile snakePart = snakeBody.get(i);
            if (i == 0) { //right before the head
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
		}
		
		snakeHead.x+=veloX;
		snakeHead.y+=veloY;
		
		//game over conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);

            //collide with snake head
            if (tileCol(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        if (snakeHead.x*tileS < 0 || snakeHead.x*tileS > boardW || //passed left border or right border
            snakeHead.y*tileS < 0 || snakeHead.y*tileS > boardH ) { //passed top border or bottom border
            gameOver = true;
        }
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// System.out.println("KeyEvent: " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP && veloY != 1) {
            veloX = 0;
            veloY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && veloY != -1) {
            veloX = 0;
            veloY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && veloX != 1) {
            veloX = -1;
            veloY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && veloX != -1) {
            veloX = 1;
            veloY = 0;
        }
		
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
	
}
