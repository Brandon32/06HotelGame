package engine;

import java.util.LinkedList;

import engine.events.GameEvent;
import engine.events.GameEventDispatcher;
import engine.events.GameEventListener;

/**
 * Threaded game engine, with support for game events and collisions
 * 
 * @author williamhooper
 */

public class GameEngine implements Runnable, GameEventListener {
	/**
	 * Game engine instance
	 */
	private static GameEngine gameEngineInstance = null;

	/**
	 * game thread object
	 */
	private static Thread gameThread;

	/**
	 * Return true if the game engine is running
	 * 
	 * @return boolean
	 */
	public static boolean isRunning() {
		return (gameEngineInstance != null) ? gameEngineInstance.running
				: false;
	}

	/**
	 * Get the animation thread going. Calls start() once the thread is created
	 * and running.
	 * 
	 * @param game
	 */
	public static void start(Game game) {
		if (gameEngineInstance == null) {
			/**
			 * Create an instance of the game engine, intialize variables and
			 * register the game event listener
			 */
			gameEngineInstance = new GameEngine();
			gameEngineInstance.game = game;
			gameEngineInstance.gameEventList = new LinkedList<GameEvent>();
			GameEventDispatcher.addGameEventListener(gameEngineInstance);

			/**
			 * Gentlemen, start your engines
			 */
			gameThread = new Thread(gameEngineInstance);
			gameThread.start();

			/**
			 * Go off and manage events
			 */
			gameEngineInstance.manageGameEvents();
		}
	}

	/**
	 * Stop the game engine. Will not immediately stop the game engine. It will
	 * stop at the beginning of the next frame update.
	 * 
	 */
	public static void stop() {
		if (gameEngineInstance != null) {
			gameEngineInstance.running = false;
			gameEngineInstance = null;
		}
	}

	/**
	 * Number of frames per second
	 */
	private final int DEFAULT_FPS = 60;

	/**
	 * Game object
	 */
	private Game game;

	/**
	 * Game event list for managing game events
	 */
	private LinkedList<GameEvent> gameEventList = new LinkedList<GameEvent>();

	/**
	 * Thread is running flag
	 */
	private boolean running;

	private static int lastTicks;
	private static int lastFrames;

	/**
	 * @return the ticks
	 */
	public static int getTicks() {
		return lastTicks;
	}

	/**
	 * @return the frames
	 */
	public static int getFrames() {
		return lastFrames;
	}

	/**
	 * Private constructor
	 * 
	 */
	private GameEngine() {
		/**
		 * no code required
		 */
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	@Override
	public void gameEvent(GameEvent ge) {
		synchronized (gameEventList) {
			gameEventList.add(ge);
			gameEventList.notify();
		}
	}

	/**
	 * The run method is part of the Runnable interface and needs to be
	 * implemented. It goes through an update-render-draw loop to drive the game
	 * engine.
	 * 
	 */
	private static long currentTime = System.nanoTime();

	public static long getCurrentTime() {
		return currentTime;
	}

	public void run() {
		long lastTime = System.nanoTime();
		int ticks = 0;
		int frames = 0;
		double unprocessed = 0;
		long lastTimer1 = System.currentTimeMillis();
		double nsPerTick = 1000000000.0 / DEFAULT_FPS;

		running = true;
		while (running) {
			currentTime = System.nanoTime();
			unprocessed += (currentTime - lastTime) / nsPerTick;
			lastTime = currentTime;
			boolean shouldRender = true;

			while (unprocessed >= 1) {
				ticks++;
				game.collisions();
				game.update();
				unprocessed -= 1;
			}

			if (shouldRender) {
				frames++;
				game.render();
				game.draw();
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (System.currentTimeMillis() - lastTimer1 > 1000) {
				lastTimer1 += 1000;
				lastFrames = frames;
				lastTicks = ticks;
				frames = 0;
				ticks = 0;
			}
		}

		/**
		 * If we get to this point, it's because running was set to false. In
		 * that case, notify the game event manager to quit and exit the thread.
		 */
		synchronized (gameEventList) {
			gameEventList.notify();
		}
		return;
	}

	/**
	 * Manage events in the event message queue
	 */
	private void manageGameEvents() {
		GameEvent gameEvent;

		while (true) {
			synchronized (gameEventList) {
				/**
				 * If the game event list is empty then wait for an event to
				 * show up
				 */
				if (gameEventList.isEmpty()) {
					try {
						gameEventList.wait();
					} catch (InterruptedException exception) {
					}
				}

				/**
				 * Check to see if the game engine thread has stopped running
				 * the we return
				 */
				if (!running) {
					return;
				}

				/**
				 * Fetch the game event
				 */
				gameEvent = gameEventList.removeFirst();
			}

			/**
			 * Deal with the game event
			 */
			game.manageGameEvent(gameEvent);
		}
	}
}
