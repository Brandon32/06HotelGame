package engine.interfaces;

import java.awt.Rectangle;

/**
 * Sprite class
 * 
 * @author Brandon Marshall
 */
public interface ColisionInterface extends ImageInterface {

	/**
	 * Determine if the passed Sprite object collided with this object.
	 * 
	 * @param obj
	 */
	public abstract void checkCollision(ColisionInterface obj);

	/**
	 * Check to see if the passed bounding box intersects with our bounding box.
	 * Returns a new Rectangle that represents the intersection of the two
	 * rectangles. If the two rectangles do not intersect, the result will be an
	 * empty rectangle.
	 * 
	 * @param boundingBox
	 */
	public abstract Rectangle intersects(Rectangle boundingBox);

}
