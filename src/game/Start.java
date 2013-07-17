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
import java.util.LinkedList;

import engine.Game;
import engine.GameDisplay;
import engine.GameEngine;
import engine.ImageUtil;
import engine.event.GameEvent;
import engine.event.GameEvent.GameEventType;
import engine.event.GameEventDispatcher;
import engine.event.GameEventKeyboard;
import engine.event.GameEventMouse;
import engine.sprite.Image;
import engine.sprite.Sprite;
import engine.sprite.UI;
import game.levels.Level01;
import game.menu.MainMenu;

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

	private LinkedList<Sprite> colisionList;
	private LinkedList<Image> drawList;
	private LinkedList<UI> keyList;
	private Image gameLevel;

	private BufferedImage loadingImage;
	private BufferedImage icon;

	private long time = 0;
	private Font f1;

	private enum GameState {
		STARTING, RUNNING, PAUSED, ENDING, RESTARTING
	}

	private static GameState gameState = GameState.STARTING;

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
//		 displayWidth = (int) screenSize.getWidth();
//		 displayHeight = (int) screenSize.getHeight();

		/**
		 * Create our game display
		 */
		GameDisplay.create(displayWidth, displayHeight);
		// GameDisplay.setFullScreen();

		f1 = new Font("Times New Roman", Font.PLAIN, (int) 12);
		gameLevel = new Startup();
		/**
		 * Create our list of sprites
		 */

		colisionList = new LinkedList<Sprite>();
		drawList = new LinkedList<Image>();
		keyList = new LinkedList<UI>();

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
		/**
		 * Check collisions on the Sprite objects
		 */
		synchronized (colisionList) {
			for (Sprite spriteObj : colisionList) {
				for (Sprite otherSprite : colisionList) {
					if (!otherSprite.equals(spriteObj)) {
						spriteObj.checkCollision(otherSprite);
					}
				}
			}
		}
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
		 * Draw the background
		 */
		offscreenGraphics.setColor(Color.WHITE);
		offscreenGraphics.fillRect(0, 0, displayWidth, displayHeight);
		gameLevel.draw(offscreenGraphics);

		/**
		 * Draw the Image objects
		 */
		synchronized (drawList) {
			for (Image imageObj : drawList) {
				imageObj.draw(offscreenGraphics);
			}
		}
		if (debug == true) {
			debug(offscreenGraphics);
		}
	}

	@Override
	public void update() {
		switch (gameState) {
		case RUNNING: {
			/**
			 * Update the Sprite objects and Level
			 */
			gameLevel.update();
			synchronized (drawList) {
				for (Image imageObj : drawList) {
					imageObj.update();
				}
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
			if (time == 0) {
				System.out.println("Starting " + GAME_NAME);
				time = GameEngine.getCurrentTime(); // TODO get system time from
													// engine
			}
			if (GameEngine.getCurrentTime() > time + (5 * 1000000000.0)) {
				gameLevel = new Loading();
				time = GameEngine.getCurrentTime();
			}
			if (GameEngine.getCurrentTime() > time + (1 * 1000000000.0)) {
				gameLevel = new MainMenu();
				gameState = GameState.RUNNING;
				time = GameEngine.getCurrentTime();
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
			if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {// TODO &&
														// ke.getKeyCode() ==
														// KeyEvent.VK_SHIFT
				GameEventDispatcher.dispatchEvent(new GameEvent(this,
						GameEventType.End, this));
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
		/**
		 * Send the keyboard event to each Sprite
		 */
		synchronized (keyList) {
			for (UI spriteObj : keyList) {
				spriteObj.keyboardEvent(ke);
			}
		}
	}

	@Override
	public void mouseEvent(MouseEvent me) {
		/**
		 * Send the mouse event to each Sprite
		 */
		synchronized (keyList) {
			for (UI spriteObj : keyList) {
				spriteObj.mouseEvent(me);
			}
		}
	}

	@Override
	public synchronized void manageGameEvent(GameEvent ge) {
		switch (ge.getType()) {
		case AddFirst:
			if (ge.getAttachment() instanceof Sprite)
				synchronized (colisionList) {
					colisionList.addFirst((Sprite) ge.getAttachment());
				}
			if (ge.getAttachment() instanceof Image)
				synchronized (drawList) {
					drawList.addFirst((Image) ge.getAttachment());
				}
			if (ge.getAttachment() instanceof UI)
				synchronized (keyList) {
					keyList.addFirst((UI) ge.getAttachment());
				}
			break;
		case Remove:
			if (ge.getAttachment() instanceof Sprite)
				synchronized (colisionList) {
					colisionList.remove((Sprite) ge.getAttachment());
				}
			if (ge.getAttachment() instanceof Image)
				synchronized (drawList) {
					drawList.remove((Image) ge.getAttachment());
				}
			if (ge.getAttachment() instanceof UI)
				synchronized (keyList) {
					keyList.remove((UI) ge.getAttachment());
				}
			break;
		case AddLast:
			if (ge.getAttachment() instanceof Sprite)
				synchronized (colisionList) {
					colisionList.addLast((Sprite) ge.getAttachment());
				}
			if (ge.getAttachment() instanceof Image)
				synchronized (drawList) {
					drawList.addLast((Image) ge.getAttachment());
				}
			if (ge.getAttachment() instanceof UI)
				synchronized (keyList) {
					keyList.addLast((UI) ge.getAttachment());
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
			synchronized (colisionList) {
				// Update the graphics once to get something on the screen
				Graphics2D offscreenGraphics = (Graphics2D) GameDisplay
						.getContext();
				offscreenGraphics.drawImage(loadingImage, null, 0, 0);
				GameDisplay.update();

				// Clear what was loaded
				clearLists();

				// Load new level
				Load(gameProgress);
			}
			break;
		}
		case Menu:
			synchronized (drawList) {
				// Update the graphics once to get something on the screen
				Graphics2D offscreenGraphics = (Graphics2D) GameDisplay
						.getContext();
				offscreenGraphics.drawImage(loadingImage, null, 0, 0);
				GameDisplay.update();

				// Clear what was loaded
				clearLists();

				// Load new level
				Load(0);
			}
			break;
		case End: {
			gameProgress = 0;
			clearLists();
			gameState = GameState.ENDING;
			Load(0);
			break;
		}
		case Restart: {
			gameProgress = 0;
			clearLists();
			gameState = GameState.RESTARTING;
			Load(0);
			break;
		}
		default:
			break;
		}
	}

	private void clearLists() {
		synchronized (drawList) {
			drawList.clear();
		}
		synchronized (colisionList) {
			colisionList.clear();
		}
		synchronized (keyList) {
			keyList.clear();
		}
	}

	private void debug(Graphics2D g) {
		// TODO
		g.setFont(f1);
		g.setColor(Color.RED);
		g.drawString(GAME_NAME, displayWidth - 100, 20);
		g.drawString("Level: " + gameProgress, displayWidth - 100, 30);
		g.drawString("State: " + gameState, displayWidth - 100, 40);
		g.drawString(
				"Ticks: " + GameEngine.getTicks() + " FPS: "
						+ GameEngine.getFrames(), displayWidth - 100, 50);
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