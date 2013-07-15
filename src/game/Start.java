package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

import levels.Level01;
import Sprite.Sprite;
import Sprite.UI;
import engine.Game;
import engine.GameDisplay;
import engine.GameEngine;
import engine.ImageUtil;
import engine.Event.GameEvent;
import engine.Event.GameEvent.GameEventType;
import engine.Event.GameEventDispatcher;
import engine.Event.GameEventKeyboard;
import engine.Event.GameEventMouse;

public class Start implements Game, GameEventMouse, GameEventKeyboard {
	/**
	 * Our list of sprites
	 */
	private static String title = "Game";
	private static int displayHeight = 720; // 720p
	private static int displayWidth = 1280;
	public static int score = 0;
	private static int gameProgress = 0;
	private static Boolean debug = true; // switch to false on release

	private LinkedList<Sprite> spriteList;
	private UI gameLevel;

	private BufferedImage loadingImage;
	private BufferedImage icon;
	private int startType;

	private long time = 0;
	private Font f1;

	private enum GameState {
		STARTING, RUNNING, PAUSED, ENDING, RESTART
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
		// Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// displayWidth = (int) screenSize.getWidth();
		// displayHeight = (int) screenSize.getHeight();

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

		spriteList = new LinkedList<Sprite>();
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
		synchronized (spriteList) {
			for (Sprite spriteObj : spriteList) {
				for (Sprite otherSprite : spriteList) {
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
		 * Draw the Sprite objects
		 */
		synchronized (spriteList) {
			for (Sprite spriteObj : spriteList) {
				spriteObj.draw(offscreenGraphics);
			}
		}

		/**
		 * Draw the Sprite objects
		 */
		synchronized (spriteList) {
			for (Sprite SpriteObj : spriteList) {
				SpriteObj.draw(offscreenGraphics);
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
			synchronized (spriteList) {
				for (Sprite spriteObj : spriteList) {
					spriteObj.update();
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
				time = System.nanoTime(); // TODO get system time from engine to
											// save processing power!
			}
			if (System.nanoTime() > time + (5 * 1000000000.0)) {
				gameLevel = new Loading();
				time = System.nanoTime();
			}
			if (System.nanoTime() > time + (1 * 1000000000.0)) {
				gameLevel = new MainMenu();
				gameState = GameState.RUNNING;
				time = System.nanoTime();
			}
			break;
		}
		case RESTART:{
			gameState = GameState.RUNNING;
			GameEngine.stop();
			break;
		}
		case ENDING: {
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
				gameState = GameState.ENDING;
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
				GameEventDispatcher.dispatchEvent(new GameEvent(this,GameEventType.End, this));
			}
		}
		gameLevel.keyboardEvent(ke);
		/**
		 * Send the keyboard event to each Sprite
		 */
		synchronized (spriteList) {
			for (Sprite spriteObj : spriteList) {
				spriteObj.keyboardEvent(ke);
			}
		}
	}

	@Override
	public void mouseEvent(MouseEvent me) {
		gameLevel.mouseEvent(me);
		/**
		 * Send the mouse event to each Sprite
		 */
		synchronized (spriteList) {
			for (Sprite spriteObj : spriteList) {
				spriteObj.mouseEvent(me);
			}
		}
	}

	@Override
	public synchronized void manageGameEvent(GameEvent ge) {
		switch (ge.getType()) {
		case AddFirst:
			synchronized (spriteList) {
				spriteList.addFirst((Sprite) ge.getAttachment());
				break;
			}

		case AddLast:
			synchronized (spriteList) {
				spriteList.addLast((Sprite) ge.getAttachment());
				break;
			}

		case Remove:
			synchronized (spriteList) {
				synchronized (spriteList) {
					Sprite sprite = (Sprite) ge.getAttachment();
					spriteList.remove(sprite);
					break;
				}
			}

			// case Score: {
			// score += ((Integer) ge.getAttachment()).intValue();
			// break;
			// }
		case EnemyDown: {
			break;
		}
		case Start: // Save games
		{
			startType = ((Integer) ge.getAttachment()).intValue();
			synchronized (spriteList) {
				switch (startType) {
				case 0: // New game
				{
					gameProgress = 1;
					GameEventDispatcher.dispatchEvent(new GameEvent(this,
							GameEventType.Load, this));
					break;
				}
				case 1: // Load game
				{
					GameEventDispatcher.dispatchEvent(new GameEvent(this,
							GameEventType.Load, this));
					break;
				}
				case 2: // Help
				{
					break;
				}
				case 3: // Settings
				{
					gameState = GameState.RESTART;
					break;
				}
				case 4: // Exit
				{
					gameState = GameState.ENDING;
					break;
				}
				}
			}
			break;
		}
		case NextLevel: // change level before loading
		{
			gameProgress++;
			break;
		}
		case Load: {
			synchronized (spriteList) {
				// Update the graphics once to get something on the screen
				Graphics2D offscreenGraphics = (Graphics2D) GameDisplay
						.getContext();
				offscreenGraphics.drawImage(loadingImage, null, 0, 0);
				GameDisplay.update();

				// Clear what was loaded
				spriteList.clear();

				// Load new level
				Load(gameProgress);

			}
			break;
		}
		case End: {
			synchronized (spriteList) {
				gameProgress = 0;
				spriteList.clear();

				Load(0);
			}
			break;
		}
		default:
			break;
		}
	}

	public void debug(Graphics2D g) {
		// TODO
		g.setFont(f1);
		g.setColor(Color.RED);
		g.drawString("Level: " + gameProgress, displayWidth - 100, 20);
		g.drawString("State: " + gameState, displayWidth - 100, 30);
		g.drawString("test2", displayWidth - 100, 40);
		g.drawString("Ticks: " + GameEngine.getTicks() + " FPS: " + GameEngine.getFrames(), displayWidth - 100, 50);
	}

	public void Load(int state) {
		switch (state) {
		case 0: // Main Menu
		{
			gameLevel = new MainMenu();
			// Load the Menu
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.Menu, this));
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
        switch ( level )
        {
        // add first
            case 0: // level 0 -
            {
            	gameLevel = new Level01();
            	break;
            }
        }
	}
}