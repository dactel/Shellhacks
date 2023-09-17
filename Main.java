import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Main extends JComponent implements MouseMotionListener {

	private static final long serialVersionUID = 1L;

	private Rectangle player = new Rectangle(-10, -10, 10, 10);
	private Rectangle start = new Rectangle(0, 0, 50, 50);
	private Rectangle end = new Rectangle(800, 600, -50, -50);

	private List<Rectangle> walls = new ArrayList<>();
	private boolean isPlaying = false;
	private static int difficulty = 1;
	private static boolean stroke = false;
	private static boolean cheat = false;
	private static boolean win = false;

	private static int level = 1;
	private int maxLevel = 4;

	public Main() {
		loadMaze(1);
	}

	public static void main(String[] args) {
		Main main = new Main();

		JFrame screen = new JFrame("Maze Game");
		screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		screen.setVisible(true);
		screen.setSize(624, 647);
		screen.setResizable(false);

		KeyAdapter listener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println(e.getKeyCode());
				testForKey(e.getKeyCode());
			}
		};

		screen.addKeyListener(listener);

		screen.addMouseMotionListener(main);
		screen.add(main);
	}

	public void paintComponent(Graphics g) {

		if (win) { 
			g.setColor(Color.BLACK);
			g.setFont(new Font("Times New Roman", Font.PLAIN, 100));
			g.drawString("YOU WIN", 100, 300);
			return;
		}
		
		// paint the background.
		if (stroke)
			g.setColor(randomColor());
		else
			g.setColor(Color.BLACK);
		g.fillRect(0, 0, 610, 610);
		// paint the starting location.
		if (stroke)
			g.setColor(randomColor());
		else
			g.setColor(Color.LIGHT_GRAY);
		g.fillRect(start.x, start.y, start.width, start.height);

		// paint the winning location.
		if (stroke)
			g.setColor(randomColor());
		else
			g.setColor(Color.LIGHT_GRAY);
		g.fillRect(end.x, end.y, end.width, end.height);

		// paint each wall.

		for (Rectangle r : walls) {
			if (stroke)
				g.setColor(randomColor());
			else
				g.setColor(Color.DARK_GRAY);
			g.fillRect(r.x, r.y, r.width, r.height);
		}

		// paint the player.
		if (isPlaying) { // if the user touches the starting point.
			if (stroke)
				g.setColor(randomColor());
			else
				g.setColor(Color.YELLOW);
			g.fillRect(player.x, player.y, player.width, player.height);

			if (stroke == true)
				g.setColor(randomColor());
			else
				g.setColor(Color.BLACK);
			// Checking difficulty
			// create fog on the x-axis of the player.
			if (difficulty == 1) {
				g.fillRect(player.x - 1040, player.y - 950, 1000, 1000); // left
				g.fillRect(player.x - 40, player.y - 1040, 1000, 1000); // top

				g.fillRect(player.x - 950, player.y + 50, 1000, 1000); // bottom
				g.fillRect(player.x + 49, player.y - 40, 1000, 1000); // right
			} else if (difficulty == 2) {
				g.fillRect(player.x - 1030, player.y - 960, 1000, 1000); // left
				g.fillRect(player.x - 30, player.y - 1030, 1000, 1000); // top

				g.fillRect(player.x - 960, player.y + 40, 1000, 1000); // bottom
				g.fillRect(player.x + 39, player.y - 30, 1000, 1000); // right
			} else if (difficulty == 3) {
				g.fillRect(player.x - 1020, player.y - 970, 1000, 1000); // left
				g.fillRect(player.x - 20, player.y - 1020, 1000, 1000); // top

				g.fillRect(player.x - 970, player.y + 30, 1000, 1000); // bottom
				g.fillRect(player.x + 29, player.y - 20, 1000, 1000); // right
			} else if (difficulty == 4) {
				g.fillRect(player.x - 1010, player.y - 960, 1000, 1000); // left
				g.fillRect(player.x - 10, player.y - 1010, 1000, 1000); // top

				g.fillRect(player.x - 960, player.y + 20, 1000, 1000); // bottom
				g.fillRect(player.x + 19, player.y - 10, 1000, 1000); // right
			}

		} else { // paint the player in the middle of the starting point if the game is not
					// playing.
			if (stroke)
				g.setColor(randomColor());
			else
				g.setColor(Color.YELLOW);
			g.fillRect(start.x + start.width / 2 - player.width / 2, start.y + start.width / 2 - player.height / 2, 10,
					10);

			// create fog on the x-axis of the starting point.
			if (stroke)
				g.setColor(randomColor());
			else
				g.setColor(Color.BLACK);

			if (difficulty != 0) {
				g.fillRect(start.x - 1000, start.y - 950, 1000, 1000);
				g.fillRect(start.x + 50, start.y - 50, 1000, 1000);

				// create fog on the y-axis of the starting point.
				g.fillRect(start.x - 950, start.y + 50, 1000, 1000);
				g.fillRect(start.x - 50, start.y - 1000, 1000, 1000);
			}

			if (stroke)
				g.setColor(randomColor());
			else
				g.setColor(new Color(255, 255, 255));
			g.setFont(new Font("Times New Roman", Font.PLAIN, 100));
			g.drawString(Main.level + " ", 275, 300);
		}
	}

	public Color randomColor() {
		Color color = Color.BLACK;
		if (stroke) {
			int r = (int) (Math.random() * 255);
			int g = (int) (Math.random() * 255);
			int b = (int) (Math.random() * 255);
			color = new Color(r, g, b);
			randomDifficulty(0);
		}
		return color;
	}

	private static void testForKey(int k) {
		if (!cheat) {
			if (k == 49) // 1
				difficulty = 0;
			else if (k == 50) // 2
				difficulty = 1;
			else if (k == 51) // 3
				difficulty = 2;
			else if (k == 52) // 4
				difficulty = 3;
			else if (k == 53) // 5
				difficulty = 4;
		}

		if (k == 83) { // s
			if (!stroke) {
				stroke = true;
			} else
				stroke = false;
		}

		if (k == 67) { // c
			if (!cheat) {
				cheat = true;
				difficulty = 0;
			} else {
				cheat = false;
			}
		}
	}

	private void loadMaze(int currentLevel) {
		if (Main.level > this.maxLevel) {
//			Main.level = 1;
//			currentLevel = 1;
			difficulty = 0;
			win = true;
			return;
		}

		walls.clear();

		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream("l" + currentLevel + ".txt");
			Scanner reader = new Scanner(in);
			start = new Rectangle(reader.nextInt(), reader.nextInt(), reader.nextInt(), reader.nextInt());
			end = new Rectangle(reader.nextInt(), reader.nextInt(), reader.nextInt(), reader.nextInt());

			int numberOfWalls = reader.nextInt();

			for (int i = 0; i < numberOfWalls; i++) {
				walls.add(new Rectangle(reader.nextInt(), reader.nextInt(), reader.nextInt(), reader.nextInt()));
			}
			reader.close();
		} catch (Exception e) {
			System.err.println("End of levels");
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// if user drags the mouse return to the starting point.
		isPlaying = false;
		repaint();
	}

	public void randomDifficulty(int x) {
		difficulty = (int) (Math.random() * (5 - x)) + x;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// print mouse coordinates.
//		System.out.println(e.getX() + " " + e.getY());

		// start game if user touches the starting point.
		if (start.intersects(player)) {
			isPlaying = true;
		}

		// check collision and check winner.
		if (isPlaying) {
			for (Rectangle r : walls) {
				if (cheat == false) {
					if (r.intersects(player)) { // if the player touches a wall.
						isPlaying = false; // game over. the player loses.
					}
				}
			}

			if (end.intersects(player)) { // if the player touches the ending
				// point.
				isPlaying = false; // set isPlaying to false to not paint the
				// rectangle.
				loadMaze(++Main.level); // go to the next level.
			}
		}

		// update the player coordinates.
		player.x = e.getX() - 11;
		player.y = e.getY() - 36;

		repaint();
	}
}