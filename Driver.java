import java.io.IOException;

/**
 * @author Da'Von Horn
 * @version 9/19/2018 Lab 12
 * 
 *          The main class
 */
public class Driver
{
    public static void main(String[] args) throws IOException
    {
        final int YEAR = 2018;
        final int MONTH = 8;
        final int DAY = 1;
        final int HOUR = 7;
        final int MINUTE = 0;

        final String directory = "data";

        MapData mesoMap = new MapData(YEAR, MONTH, DAY, HOUR, MINUTE, directory);

        mesoMap.parseFile();

        System.out.println(mesoMap);

    }
}
