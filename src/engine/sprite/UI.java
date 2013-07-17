package engine.sprite;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface UI extends Image
{
    /**
     * Receive a keyboard event.
     * 
     * @param ke
     */
    public abstract void keyboardEvent( KeyEvent ke );

    /**
     * Receive a mouse event.
     * 
     * @param me
     */
    public abstract void mouseEvent( MouseEvent me );
}
