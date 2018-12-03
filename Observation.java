/**
 * @author Da'Von Horn
 * @version 9/26/2018
 * Lab 12
 * 
 * A class that defines Observation objects. MapData will consists of these objects.
 */
public class Observation extends AbstractObservation
{
    /** 
     * Value contained as a double
     */
    private double value;
    
    /**
     * ID of area value is taken from
     */
    private String stid;
    
    /**
     * Constructor for the Observations
     * @param value and stid information
     */
    public Observation(double value, String stid)
    {
        this.value = value;
        this.stid = stid;
    }
    
    /**
     * Gets the value of the observation
     * @return double value of the observation
     */
    public double getValue()
    {
        return value;
    }
    
    /**
     * Checks if value is within allowed range
     * @return boolean valid
     */
    public boolean isValid()
    {
        if(this.value>-900)
        {
            valid = true;
        }
        else
        {
            valid = false;
        }
        return valid;
    }
    
    /**
     * Gets the value of the observation's stid
     * @return String containing the stid of the observation
     */
    public String getStid()
    {
        return stid;
    }
    
    /**
     * Takes observation and converts data to a string
     * @return String containing this data
     */
    public String toString()
    {
        String out = "";
        out += ("Value: " + String.format("%.1f", this.getValue()) + "\n" + "Station: " + String.format("%s",
                this.getStid()));
        return out;
    }
}
