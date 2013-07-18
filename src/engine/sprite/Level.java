package engine.sprite;

import java.awt.Graphics2D;

/**
 * Sprite class
 * 
 * @author Brandon Marshall
 */
public interface Level
{
    /**
     * Update the sprite's state.
     * 
     */
    public abstract void update();
    
    /**
     * Draw method
     * 
     * @param g
     */
    
    public abstract void draw( Graphics2D g );
}
