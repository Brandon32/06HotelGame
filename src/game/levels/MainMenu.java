package game.levels;

import engine.events.GameEvent;
import engine.events.GameEvent.GameEventType;
import engine.events.GameEventDispatcher;
import game.LevelSuper;
import game.Start;
import game.menu.HelpSprite;
import game.menu.MainMenuSprite;
import game.menu.SettingsSprite;

import java.awt.Graphics2D;

public class MainMenu extends LevelSuper {
	private MainMenuSprite menu;
	private SettingsSprite settings;
	private HelpSprite help;

	public MainMenu() {
		super();
		menu = new MainMenuSprite();
		GameEventDispatcher.dispatchEvent(new GameEvent(this,
				GameEventType.AddLast, menu));
		settings = new SettingsSprite();
		help = new HelpSprite();
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
		drawSprites(g);
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