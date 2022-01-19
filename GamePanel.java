package snakeGame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//dimensions in pixels
	static final int screenWidth = 600;
	static final int screenHeight = 600;
	static final int unitSize = 25;
	//how many units can fit on the game:
	static final int gameUnits = ((screenWidth*screenHeight)/unitSize);
	
	//timer delayer. higher number = slower game
	static final int delay = 75;
	
	//arrays x and y to hold all coordinates for body parts of snake
	final int x[] = new int[gameUnits];
	final int y[] = new int[gameUnits];
	
	//starting number of snake body parts
	int bodyParts = 6;
	int applesEaten = 0;
	int appleX;
	int appleY;
	
	//R = right, L = left, U = up, D = down
	char direction = 'R';
	
	boolean running = false;
	Timer timer;
	Random random;
	
	
	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(screenWidth,screenHeight));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
		
	}
	
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(delay,this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		if (running) {
			//turn into a grid
			for (int i = 0; i < screenHeight/unitSize; i++) {
				g.setColor(Color.gray);
				g.drawLine(i*unitSize, 0, i*unitSize, screenHeight);
				g.drawLine(0, i*unitSize, screenWidth, i*unitSize);
			}
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, unitSize, unitSize);
			
			for (int i = 0; i < bodyParts;i++) {
				//go through body parts to fill them in
				if (i == 0) {
					//head
					g.setColor(Color.yellow);
					g.fillRect(x[i], y[i], unitSize, unitSize);
				} else {
					g.setColor(Color.green);
					g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i], y[i], unitSize, unitSize);
				}
			}
			
			g.setColor(Color.red);
			g.setFont(new Font("Courier", Font.BOLD, 20));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten*10, (screenWidth - metrics.stringWidth("Score: " + applesEaten*10))/2,
					g.getFont().getSize());
			
		} else {
			gameOver(g);
		}
	}
	
	public void newApple() {
		appleX = random.nextInt((int)(screenWidth/unitSize))*unitSize;
		appleY = random.nextInt((int)(screenHeight/unitSize))*unitSize;
	}
	
	public void move() {
		for (int i = bodyParts; i>0; i--) {
			//shifting all coordinates over by one spot in the direction that the snake moves
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		switch(direction) {
		case 'U':
			y[0] = y[0] - unitSize;
			break;
		case 'D':
			y[0] = y[0] + unitSize;
			break;
		case 'L':
			x[0] = x[0] - unitSize;
			break;
		case 'R':
			x[0] = x[0] + unitSize;
			break;
		}
		
	}
	
	public void checkApple() {
		//check coordinates of snake and of apple
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			//apples eaten = score
			applesEaten++;
			newApple();
		}
		
	}
	
	public void checkCollisions() {
		//iterate through body parts
		//check if location of head is the same as location of any of the
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0]==x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		
		//check if head touches borders
		if (x[0] < 0) {
			running = false;
		}
		
		if (x[0] > screenWidth-unitSize) {
			running = false;
		}
		
		if (y[0] < 0) {
			running = false;
		}
		
		if (y[0] > screenHeight-unitSize) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
		
	}
	
	public void gameOver(Graphics g) {
		//display score
		g.setColor(Color.red);
		g.setFont(new Font("Courier", Font.BOLD, 20));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten*10, (screenWidth - metrics1.stringWidth("Score: " + applesEaten*10))/2,
				g.getFont().getSize());
		
		//game over message
		g.setColor(Color.yellow);
		g.setFont(new Font("Courier", Font.BOLD, 25));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over! Try again.", (screenWidth - metrics2.stringWidth("Game Over! Try again."))/2,
				screenHeight/2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//if game is running, move the snake
		if(running) {
			move();
			checkApple();
			checkCollisions();
			
		}
		repaint();
		
	}
	
	public class MyKeyAdapter extends KeyAdapter {
		
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
					break;
				}
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
					break;
				}
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
					break;
				}
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
					break;
				}

			}
			
			
		}
	}

}
