package game.levels;

import engine.events.GameEvent;
import engine.events.GameEvent.GameEventType;
import engine.events.GameEventDispatcher;
import game.sprite.Mech;

import java.awt.Graphics2D;

public class Level01 extends LevelSuper {
	private Mech myMech;

	public Level01() {
		myMech = new Mech();
		GameEventDispatcher.dispatchEvent(new GameEvent(this,
				GameEventType.AddFirst, myMech));
	}

	@Override
	public void update() {

	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub

	}
}
