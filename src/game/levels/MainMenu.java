package game.levels;

import engine.event.GameEvent;
import engine.event.GameEvent.GameEventType;
import engine.event.GameEventDispatcher;
import engine.sprite.Image;
import game.MMenu;

import java.awt.Graphics2D;

public class MainMenu implements Image{
			private MMenu menu;

			public MainMenu() {
				menu = new MMenu();
				GameEventDispatcher.dispatchEvent(new GameEvent(this,GameEventType.AddLast, menu));
			}

			@Override
			public void update() {

			}

			@Override
			public void draw(Graphics2D g) {
				// TODO Auto-generated method stub

			}
		}