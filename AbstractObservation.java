
public abstract class AbstractObservation
{
    /**
     * 
     */
    protected boolean valid;

    /**
     * 
     */
    public AbstractObservation()
    {
        this.valid = false;
    }

    /**
     * 
     */
    public abstract boolean isValid();
}
