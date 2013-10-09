package game;

import java.awt.Color;
import java.awt.Dimension;
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
import engine.Keyboard;
import engine.events.GameEvent;
import engine.events.GameEvent.GameEventType;
import engine.events.GameEventDispatcher;
import engine.events.GameEventKeyboard;
import engine.events.GameEventMouse;
import engine.interfaces.ImageInterface;
import engine.interfaces.LevelInterface;
import engine.tools.DebugInfo;
import game.levels.Level01;
import game.levels.MainMenu;

public class Start implements Game, GameEventMouse, GameEventKeyboard {
	/**
	 * Our list of sprites
	 */
	private static final String GAME_NAME = "Mech Game";
	private static String title = "Game";
	private static int displayHeight = 720; // 720p
	private static int displayWidth = 1280;
	private static int currentGameProgress = 0;

	private LevelInterface currentGameLevel;
	private LevelInterface loadedLevel;

	private BufferedImage loadingImage;
	private BufferedImage icon;

	private long tPlus = 0;
	private int count;

	private enum Load {
		MENU, LEVEL, CREDITS
	}

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
		DebugInfo.debugLog("Stoped");
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


		currentGameLevel = new Startup();
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
			DebugInfo.debugLog("Icon Image Not Loaded");
		}
		if (icon != null) {
			GameDisplay.icon(icon);
		}
	}

	/*--END-Of-Constructor------------------------------------------------------------------------------------*/

	@Override
	public void collisions() {
		currentGameLevel.checkCollision();
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
		synchronized (currentGameLevel) {
			currentGameLevel.draw(offscreenGraphics);
		}
		DebugInfo.debugShort(GAME_NAME);
		DebugInfo.debugShort("Level: " + currentGameProgress);
		DebugInfo.debugShort("State: " + gameState);
		DebugInfo.debugShort("Ticks: " + GameEngine.getTicks() + " FPS: " + GameEngine.getFrames());
		
		DebugInfo.display(offscreenGraphics);
	}

	@Override
	public void update() {
		switch (gameState) {
		case RUNNING: {
			synchronized (currentGameLevel) {
				currentGameLevel.update();
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
				DebugInfo.debugLog("Starting " + GAME_NAME);
				tPlus = GameEngine.getCurrentTime();
			}
			if (System.nanoTime() > tPlus + (1 * 1000000000.0) && count == 0) {
				count = 1;
				loadedLevel = new Loading();
				currentGameLevel = loadedLevel;
				tPlus = System.nanoTime();
				DebugInfo.debugLog("Loading");
			}
			if (System.nanoTime() > tPlus + (1 * 1000000000.0) && count == 1) {
				count = 2;
				loadedLevel = new MainMenu();
				currentGameLevel = loadedLevel;
				gameState = GameState.RUNNING;
				tPlus = System.nanoTime();
				DebugInfo.debugLog("Running");
			}
			break;
		}
		case RESTARTING: {
			DebugInfo.debugLog("Restarting");
			gameState = GameState.RUNNING;
			GameEngine.stop();
			break;
		}
		case ENDING: {
			DebugInfo.debugLog("Stopping");
			GameEngine.stop();
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void keyboardEvent(KeyEvent ke) {
		
		//Fix Repeated calls when looped
		
		Keyboard.keyboardEvent(ke);

		// Exit
		if (Keyboard.isPressed(KeyEvent.VK_ESCAPE) && Keyboard.isPressed(KeyEvent.VK_SHIFT)) {
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.End, this));
		}
		if (Keyboard.isPressed(KeyEvent.VK_ESCAPE)) {
			if (currentGameLevel instanceof MainMenu)
				if ((loadedLevel instanceof MainMenu)) {
					// Do Nothing
				} else {
					currentGameLevel = loadedLevel;
				}
			else {
				Load(Load.MENU);
			}
		}

		/**
		 * Check Key Events before sending to sprite
		 */
			// Pause or Run
			if (Keyboard.isPressed(KeyEvent.VK_P)) {
				if (gameState == GameState.RUNNING) {
					gameState = GameState.PAUSED;
				} else if (gameState == GameState.PAUSED) {
					gameState = GameState.RUNNING;
				}
			}
			// Menu
			if (Keyboard.isPressed(KeyEvent.VK_M)) {
				GameEventDispatcher.dispatchEvent(new GameEvent(this,
						GameEventType.Menu, this));
			}

		synchronized (currentGameLevel) {
			currentGameLevel.keyboardEvent(ke);
		}
	}

	@Override
	public void mouseEvent(MouseEvent me) {
		synchronized (currentGameLevel) {
			currentGameLevel.mouseEvent(me);
		}
	}

	@Override
	public synchronized void manageGameEvent(GameEvent ge) {
		switch (ge.getType()) {
		case AddFirst: {
			synchronized (currentGameLevel) {
				currentGameLevel.addFirst((ImageInterface) ge.getAttachment());
			}
		}
			break;
		case Remove: {
			synchronized (currentGameLevel) {
				currentGameLevel.remove((ImageInterface) ge.getAttachment());
			}
		}
			break;
		case AddLast: {
			synchronized (currentGameLevel) {
				currentGameLevel.addLast((ImageInterface) ge.getAttachment());
			}
		}
			break;
		case EnemyDown: {
			break;
		}
		case NextLevel: // change level before loading
		{
			currentGameProgress = currentGameProgress + 1;
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
			Load(Load.LEVEL);
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
			Load(Load.MENU);
		}
			break;
		case End: {
			currentGameProgress = 0;
			currentGameLevel.clearLists();
			gameState = GameState.ENDING;
			Load(Load.MENU);
			break;
		}
		case Restart: {
			currentGameProgress = 0;
			currentGameLevel.clearLists();
			gameState = GameState.RESTARTING;
			Load(Load.MENU);
			break;
		}
		default:
			break;
		}
	}


	public void Load(Load state) {
		synchronized (currentGameLevel) {
			switch (state) {
			case MENU: // Main Menu
			{
				currentGameLevel = new MainMenu();
				// Load the Menu
				break;
			}

			case LEVEL: // Game
			{
				// Intro of Game
				LoadLevel(currentGameProgress);
				break;
			}

			case CREDITS: // Credits
			{
				// Credits
				break;
			}
			default: // Menu
			{
				// Reset
				currentGameLevel = new MainMenu();
				currentGameProgress = 0;
				break;
			}
			}
		}
	}

	public void LoadLevel(int level) {
		// list of levels
		switch (level) {
		// add first
		case 0: // level 0 -
		{
			DebugInfo.debugShort("New Level 01");
			loadedLevel = new Level01();
			currentGameLevel = loadedLevel;
			break;
		}
		}
	}

	/**
	 * @return the gameProgress
	 */
	public static int getGameProgress() {
		return currentGameProgress;
	}

	/**
	 * @param gameProgress
	 *            the gameProgress to set
	 */
	public static void setGameProgress(int gameProgress) {
		Start.currentGameProgress = gameProgress;
	}
}