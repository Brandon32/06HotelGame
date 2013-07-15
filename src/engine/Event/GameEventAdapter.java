package engine.Event;

/**
 * An abstract adapter class for receiving game events. The methods in this class are empty. This class exists as convenience for
 * creating listener objects.
 * 
 * @author williamhooper 
 */

public class GameEventAdapter implements GameEventListener
{
    /**
     * Invoked when a game event occurs
     */
    public void gameEvent( GameEvent ge )
    {

    }
}
