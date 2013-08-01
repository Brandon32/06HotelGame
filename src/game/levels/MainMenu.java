package game.levels;

import engine.GameDisplay;
import engine.ImageUtil;
import engine.events.GameEvent;
import engine.events.GameEvent.GameEventType;
import engine.events.GameEventDispatcher;
import game.LevelSuper;
import game.Start;
import game.menu.HelpSprite;
import game.menu.MainMenuSprite;
import game.menu.SettingsSprite;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MainMenu extends LevelSuper {
	private MainMenuSprite menu;
	private SettingsSprite settings;
	private HelpSprite help;
	private BufferedImage backgroundImage;
	private Dimension displayBounds;

	public MainMenu() {
		super();
		displayBounds = GameDisplay.getBounds();
		try {
			backgroundImage = ImageUtil.loadBufferedImage(this,
					"/Backgrounds/Menu.png");
		} catch (IOException e) {
			System.out.println("Menu Image Not Loaded");
		}

		menu = new MainMenuSprite();
		settings = new SettingsSprite();
		help = new HelpSprite();

		GameEventDispatcher.dispatchEvent(new GameEvent(this,
				GameEventType.AddFirst, menu));
	}

	@Override
	public void update() {
		super.update();
		if (menu.isDone()) {
			change(menu.getSelectedValue());
			menu.setDone(false);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, displayBounds.width,
					displayBounds.height, null);
		}

		super.draw(g);
	}

	private void change(int selected) {
		switch (selected) {
		case 0:// NEW(0)
			Start.setGameProgress(0);
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.Load, this));
			break;
		case 1:// LOAD(1)
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.Load, this));
			break;
		case 2:// HELP(2)
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.Remove, menu));
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.Remove, settings));
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.AddLast, help));
			break;
		case 3:// SETTINGS(3)
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.Remove, menu));
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.Remove, help));
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.AddLast, settings));
			break;
		case 4:// END(4)
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.End, this));
			break;
		case 5:// RESTART(5)
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.Restart, this));
			break;
		case 6:// MENU(6)
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.Remove, help));
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.Remove, settings));
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.AddLast, menu));
			break;
		}
	}
}