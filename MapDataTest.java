import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 */

/**
 * @author Da'Von
 *
 */
public class MapDataTest
{

    /**
     * Test method for MapData constructor.
     */
    @Test
    public void testMapData()
    {
        try
        {
            MapData map = new MapData(10, 05, 0, 10, 10, "data");
            map.toString();
        }
        catch (Exception e)
        {
            Assert.fail("Exception caught during legal construction of MapData");
        }
    }

    /**
     * Test method for creating a file name from a MapData object.
     */
    @Test
    public void testCreateFileName()
    {
        try
        {
            MapData map = new MapData(10, 03, 10, 05, 00, "data");
            String test = map.createFileName(10, 03, 10, 05, 00, "data");
            Assert.assertEquals("data/001003100500.mdf", test);
        }
        catch (Exception e)
        {
            Assert.fail("Exception was caught while creating file name");
        }
    }

    /**
     * Test method for parsing a file.
     */
    @Test
    public void testParseFile() throws IOException
    {
        try
        {
            final int YEAR = 2018;
            final int MONTH = 8;
            final int DAY = 1;
            final int HOUR = 7;
            final int MINUTE = 0;

            final String directory = "data";

            MapData mesoMap = new MapData(YEAR, MONTH, DAY, HOUR, MINUTE, directory);

            mesoMap.parseFile();
        }
        catch (Exception e)
        {
            Assert.fail("Exception caught while parsing file");
        }
    }

    /**
     * Test method for toString.
     */
    @Test
    public void testToString() throws IOException
    {
        final int YEAR = 2018;
        final int MONTH = 8;
        final int DAY = 1;
        final int HOUR = 7;
        final int MINUTE = 0;

        final String directory = "data";

        MapData mesoMap = new MapData(YEAR, MONTH, DAY, HOUR, MINUTE, directory);

        mesoMap.parseFile();
        mesoMap.toString();
        
        Assert.assertEquals(mesoMap.toString(), mesoMap.toString());
    }

}
