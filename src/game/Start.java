package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import engine.Game;
import engine.GameDisplay;
import engine.GameEngine;
import engine.ImageUtil;
import engine.events.GameEvent;
import engine.events.GameEvent.GameEventType;
import engine.events.GameEventDispatcher;
import engine.events.GameEventKeyboard;
import engine.events.GameEventMouse;
import engine.interfaces.ImageInterface;
import engine.interfaces.LevelInterface;
import game.levels.Level01;
import game.levels.MainMenu;

public class Start implements Game, GameEventMouse, GameEventKeyboard {
	/**
	 * Our list of sprites
	 */
	static final String GAME_NAME = "Mech Game";
	private static String title = "Game";
	private static int displayHeight = 720; // 720p
	private static int displayWidth = 1280;
	public static int score = 0;
	private static int gameProgress = 0;
	private static Boolean debug = true; // switch to false on release

	private LevelInterface gameLevel;

	private BufferedImage loadingImage;
	private BufferedImage icon;

	private long tPlus = 0;
	private Font f1;
	private boolean Esc = false;
	private boolean Shift = false;
	private int count;

	private enum GameState {
		STARTING, RUNNING, PAUSED, ENDING, RESTARTING
	}

	private static GameState gameState;

	/*--Main--------------------------------------------------------------------------------------------------*/

	public static void main(String[] args) {
		// if (args[1] == "debug"){
		// debug = true;
		// }
		while (gameState != GameState.ENDING) {
			gameState = GameState.STARTING;
			Start game = new Start();
			GameEngine.start(game);

			/**
			 * If we are here we have stopped the game engine
			 */
			GameDisplay.dispose();
		}
	}

	/*--Constructor-------------------------------------------------------------------------------------------*/
	public Start() {
		/**
		 * / Get the native resolution / TODO: 16:9 Recommended, Account for 4:3
		 * and 16:10
		 */
		@SuppressWarnings("unused")
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// displayWidth = (int) screenSize.getWidth();
		// displayHeight = (int) screenSize.getHeight();

		/**
		 * Create our game display
		 */
		GameDisplay.create(displayWidth, displayHeight);
		// GameDisplay.setFullScreen();

		f1 = new Font("Times New Roman", Font.PLAIN, (int) 12);
		gameLevel = new Startup();
		gameState = GameState.STARTING;
		/**
		 * Add a mouse listener so we can get mouse events
		 */
		GameDisplay.addMouseListener(this);
		/**
		 * Add a keyboard listener so we can get key presses
		 */
		GameDisplay.addKeyboardListener(this);
		GameDisplay.captureCursor(false);
		GameDisplay.title(title);

		// Very Start of Game
		try {
			icon = ImageUtil.loadBufferedImage(this, "/Icon.png");
		} catch (IOException e) {
			System.out.println("Icon Image Not Loaded");
		}
		if (icon != null) {
			GameDisplay.icon(icon);
		}
	}

	/*--END-Of-Constructor------------------------------------------------------------------------------------*/

	@Override
	public void collisions() {
		gameLevel.checkCollision();
	}

	@Override
	public void draw() {
		/**
		 * Update the graphics we drew on
		 */
		GameDisplay.update();
	}

	@Override
	public void render() {
		/**
		 * Get the current graphics
		 */
		Graphics2D offscreenGraphics = (Graphics2D) GameDisplay.getContext();

		/**
		 * Clear the background
		 */
		offscreenGraphics.setColor(Color.WHITE);
		offscreenGraphics.fillRect(0, 0, displayWidth, displayHeight);
		synchronized (gameLevel) {
			gameLevel.draw(offscreenGraphics);
		}
		if (debug == true) {
			debug(offscreenGraphics);
		}
	}

	@Override
	public void update() {
		switch (gameState) {
		case RUNNING: {
			synchronized (gameLevel) {
				gameLevel.update();
			}
			break;
		}
		case PAUSED: {
			/**
			 * Do Not Do Anything
			 */
			return;
		}
		case STARTING: {
			if (tPlus == 0) {
				count = 0;
				System.out.println("Starting " + GAME_NAME);
				tPlus = GameEngine.getCurrentTime();
			}
			if (System.nanoTime() > tPlus + (1 * 1000000000.0) && count == 0) {
				count = 1;
				gameLevel = new Loading();
				tPlus = System.nanoTime();
				System.out.println("Loading");
			}
			if (System.nanoTime() > tPlus + (1 * 1000000000.0) && count == 1) {
				count = 2;
				gameLevel = new MainMenu();
				gameState = GameState.RUNNING;
				tPlus = System.nanoTime();
				System.out.println("Running");
			}
			break;
		}
		case RESTARTING: {
			System.out.println("Restarting");
			gameState = GameState.RUNNING;
			GameEngine.stop();
			break;
		}
		case ENDING: {
			System.out.println("Stopping");
			GameEngine.stop();
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void keyboardEvent(KeyEvent ke) {
		/**
		 * Check Key Events before sending to sprite
		 */
		if (ke.getID() == KeyEvent.KEY_PRESSED) {
			// Exit
			if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
				Esc = true;
				if (gameLevel instanceof MainMenu) {
					Load(gameProgress);
				} else {
					gameLevel = new MainMenu();
				}
			}
			if (ke.getKeyCode() == KeyEvent.VK_SHIFT) {
				Shift = true;
			}
			// Pause or Run
			if (ke.getKeyCode() == KeyEvent.VK_P) {
				if (gameState == GameState.RUNNING) {
					gameState = GameState.PAUSED;
				} else if (gameState == GameState.PAUSED) {
					gameState = GameState.RUNNING;
				}
			}
			// Menu
			if (ke.getKeyCode() == KeyEvent.VK_M) {
				GameEventDispatcher.dispatchEvent(new GameEvent(this,
						GameEventType.Menu, this));
			}
		}
		if (ke.getID() == KeyEvent.KEY_RELEASED) {
			if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {// TODO &&
				Esc = false;
			}
			if (ke.getKeyCode() == KeyEvent.VK_SHIFT) {
				Shift = false;
			}
		}
		if (Esc && Shift) {
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.End, this));
		}
		synchronized (gameLevel) {
			gameLevel.keyboardEvent(ke);
		}
	}

	@Override
	public void mouseEvent(MouseEvent me) {
		synchronized (gameLevel) {
			gameLevel.mouseEvent(me);
		}
	}

	@Override
	public synchronized void manageGameEvent(GameEvent ge) {
		switch (ge.getType()) {
		case AddFirst: {
			synchronized (gameLevel) {
				gameLevel.addFirst((ImageInterface) ge.getAttachment());
			}
		}
			break;
		case Remove: {
			synchronized (gameLevel) {
				gameLevel.remove((ImageInterface) ge.getAttachment());
			}
		}
			break;
		case AddLast: {
			synchronized (gameLevel) {
				gameLevel.addLast((ImageInterface) ge.getAttachment());
			}
		}
			break;
		case EnemyDown: {
			break;
		}
		case NextLevel: // change level before loading
		{
			gameProgress = gameProgress + 1;
			break;
		}
		case Load: {
			// Update the graphics once to get something on the screen
			Graphics2D offscreenGraphics = (Graphics2D) GameDisplay
					.getContext();
			offscreenGraphics.drawImage(loadingImage, null, 0, 0);
			GameDisplay.update();

			// Clear what was loaded
			// gameLevel.clearLists();

			// Load new level
			Load(gameProgress);
			break;

		}
		case Menu: {
			// Update the graphics once to get something on the screen
			// Graphics2D offscreenGraphics = (Graphics2D) GameDisplay
			// .getContext();
			// offscreenGraphics.drawImage(loadingImage, null, 0, 0);
			GameDisplay.update();

			// Clear what was loaded
			// gameLevel.clearLists();

			// Load new level
			Load(0);
		}
			break;
		case End: {
			gameProgress = 0;
			gameLevel.clearLists();
			gameState = GameState.ENDING;
			Load(0);
			break;
		}
		case Restart: {
			gameProgress = 0;
			gameLevel.clearLists();
			gameState = GameState.RESTARTING;
			Load(0);
			break;
		}
		default:
			break;
		}
	}

	private void debug(Graphics2D g) {
		g.setFont(f1);
		g.setColor(Color.RED);
		g.drawString(GAME_NAME, displayWidth - 100, 20);
		g.drawString("Level: " + gameProgress, displayWidth - 100, 30);
		g.drawString("State: " + gameState, displayWidth - 100, 40);
		g.drawString(
				"Ticks: " + GameEngine.getTicks() + " FPS: "
						+ GameEngine.getFrames(), displayWidth - 100, 50);
	}

	public static void debug(String st) {
		System.out.println(st);
	}

	public void Load(int state) {
		switch (state) {
		case 0: // Main Menu
		{
			gameLevel = new MainMenu();
			// Load the Menu
			break;
		}

		case 1: // Game
		{
			// Intro of Game
			LoadLevel(0);
			break;
		}

		case 99: // Credits
		{
			// Credits
			break;
		}
		default: // Menu
		{
			gameLevel = new MainMenu();
			gameProgress = 0;
			break;
		}
		}
	}

	public void LoadLevel(int level) {
		// list of levels
		switch (level) {
		// add first
		case 0: // level 0 -
		{
			gameLevel = new Level01();
			break;
		}
		}
	}

	/**
	 * @return the gameProgress
	 */
	public static int getGameProgress() {
		return gameProgress;
	}

	/**
	 * @param gameProgress
	 *            the gameProgress to set
	 */
	public static void setGameProgress(int gameProgress) {
		Start.gameProgress = gameProgress;
	}
}