package engine.event;

import java.awt.event.KeyEvent;

/**
 * Keyboard event listener
 * 
 * @author williamhooper 
 */

public interface GameEventKeyboard
{
    /**
     * Receive a keyboard event
     * 
     * @param ke
     */
    public void keyboardEvent( KeyEvent ke );
}
