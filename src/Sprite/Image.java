package Sprite;

import java.awt.Graphics2D;

/**
 * Sprite class
 * 
 * @author williamhooper
 */
public interface Image
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
