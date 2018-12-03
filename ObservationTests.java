import org.junit.Assert;
import org.junit.Test;


/**
 * 
 */

/**
 * @author Da'Von
 *
 */
public class ObservationTests
{

    /**
     * 
     */
    @Test
    public void testIsValid()
    {
        try
        {
            Observation ob = new Observation(-999, "Test");
            Assert.assertEquals(false, ob.isValid());
        }
        catch (Exception e)
        {
            Assert.fail("Some exception was caught");
        }
    }

    /**
     * 
     */
    @Test
    public void testObservation()
    {
        try
        {
            Observation ob = new Observation(0, "Test");
            Assert.assertEquals(ob.getStid(), "Test");
        }
        catch (Exception e)
        {
            Assert.fail("Exception caught while creating observation object");
        }
    }
    

    /**
     * 
     */
    @Test
    public void testGetValue()
    {
        try
        {
            Observation ob = new Observation(10.0, "STID");
            Assert.assertEquals(10.0, ob.getValue(), .1);
        }
        catch (Exception e)
        {
            Assert.fail("Exception caught when testing getValue");
        }
    }

    /**
     * 
     */
    @Test
    public void testGetStid()
    {
        try
        {
            Observation ob = new Observation(0.0, "station");
            Assert.assertEquals("station", ob.getStid());
        }
        catch (Exception e)
        {
            Assert.fail("Exception caught when testing getStid");
        }
    }

    /**
     * 
     */
    @Test
    public void testToString()
    {
        try
        {
            Observation ob = new Observation(12.515, "Test");
            Assert.assertEquals("Value: 12.5" + "\n" + "Station: Test", ob.toString());
        }
        catch (Exception e)
        {
            System.out.println(e);
            Assert.fail("Exception caught when calling toString");
        }
    }
    
}
