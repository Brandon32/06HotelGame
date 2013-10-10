package engine.interfaces;

import java.awt.event.KeyEvent;

public interface UserInputInterface {

	public void keyboardEvent(KeyEvent ke);
	
	public boolean isActive(int keyCode);
	
}
