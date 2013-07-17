package game.menu;

import java.awt.Graphics2D;

import engine.event.GameEvent;
import engine.event.GameEventDispatcher;
import engine.event.GameEvent.GameEventType;
import engine.sprite.Image;
import game.Start;

public class MainMenu implements Image {
	private MainMenuSprite menu;

	public MainMenu() {
		menu = new MainMenuSprite();
		GameEventDispatcher.dispatchEvent(new GameEvent(this,
				GameEventType.AddLast, menu));
	}

	@Override
	public void update() {
		
		if(menu.isDone()){
			change(menu.getSelectedValue());
		}

	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub

	}

	private void change(int selected) {
		switch (selected) {
		case 0:// NEW(0)
			Start.setGameProgress(1);
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.Load, this));
			break;
		case 1:// LOAD(1)
			GameEventDispatcher.dispatchEvent(new GameEvent(this,
					GameEventType.Load, this));
			break;
		case 2:// HELP(2)
			menu = new HelpSprite();
			break;
		case 3:// SETTINGS(3)
			menu = new SettingsSprite();
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
			menu = new MainMenuSprite();
			break;
		}
	}
}