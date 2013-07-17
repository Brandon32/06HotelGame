package engine.sprite;

import java.util.LinkedList;

/**
 * Sprite pool class for managing sprite object pools
 * 
 * @author williamhooper 
 */
public abstract class SpritePool
{
    private int poolSize;
    private boolean poolWait;
    private LinkedList< SpritePoolObject > spritePoolInList;
    private LinkedList< SpritePoolObject > spritePoolOutList;

    /**
     * Constructor
     * 
     */
    public SpritePool( )
    {
        spritePoolOutList = new LinkedList< SpritePoolObject >();
        spritePoolInList = new LinkedList< SpritePoolObject >();

        poolSize = 5;
        poolWait = false;
    }

    /**
     * Check an object back into the pool
     * 
     * @param obj
     */
    public void checkIn( SpritePoolObject obj )
    {
        spritePoolOutList.remove( obj );
        spritePoolInList.add( obj );
        if ( poolWait )
        {
            spritePoolInList.notify();
        }
    }

    /**
     * Check an object from out of the pool
     * 
     * @return SpritePoolObject
     */
    public SpritePoolObject checkOut()
    {
        if ( ( spritePoolOutList.size() + spritePoolInList.size() ) == poolSize )
        {
            if ( spritePoolInList.size() != 0 )
            {
                /**
                 * Create a sprite pool object, set the parent pool and return
                 */
                SpritePoolObject obj = spritePoolInList.remove();
                spritePoolOutList.add( obj );
                obj.setParentPool( this );
                return obj;
            }
            else if ( poolWait )
            {
                /**
                 * Wait for a sprite pool object to show up
                 */
                synchronized ( spritePoolInList )
                {
                    try
                    {
                        spritePoolInList.wait();
                    }
                    catch ( InterruptedException exception )
                    {
                    }
                    SpritePoolObject obj = spritePoolInList.remove();
                    spritePoolOutList.add( obj );
                    obj.setParentPool( this );
                    return obj;
                }
            }
            else
            {
                /**
                 * Since we do not have any available sprite objects and we are not waiting return null
                 */
                return null;
            }
        }
        else
        {
            /**
             * Create a sprite pool object, set the parent pool and return
             */
            SpritePoolObject obj = create();
            spritePoolOutList.add( obj );
            obj.setParentPool( this );
            return obj;
        }
    }

    /**
     * @return the poolSize
     */
    public int getPoolSize()
    {
        return poolSize;
    }

    /**
     * @return the poolWait
     */
    public boolean isPoolWait()
    {
        return poolWait;
    }

    /**
     * @param poolSize
     *            the poolSize to set
     */
    public void setPoolSize( int poolSize )
    {
        this.poolSize = poolSize;
    }

    /**
     * @param poolWait
     *            the poolWait to set
     */
    public void setPoolWait( boolean poolWait )
    {
        this.poolWait = poolWait;
    }

    /**
     * Abstract class to create a pool object
     * 
     * @return
     */
    protected abstract SpritePoolObject create();

}
