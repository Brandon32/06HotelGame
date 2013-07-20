package engine.events;

import java.awt.event.MouseEvent;

/**
 * Mouse event listener
 * 
 * @author williamhooper 
 */

public interface GameEventMouse
{
    /**
     * Receive a mouse event
     * 
     * @param me
     */
    public void mouseEvent( MouseEvent me );
}
