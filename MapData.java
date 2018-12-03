import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Da'Von Horn
 * @version 10/3/2018 Lab 12
 * 
 *          A class that will read in data from a file, parse it, and calculate
 *          statistics based on read data
 */

public class MapData
{
    /**
     * Collection for holding all of our data
     */
    private HashMap<String, ArrayList<Observation>> dataCatalog;

    /**
     * Variable for holding our statistics
     */
    private EnumMap<StatsType, TreeMap<String, Statistics>> statistics;

    /**
     * Variable for deciding what position each parameter is at
     */
    private TreeMap<String, Integer> paramPositions;

    /**
     * Number of observations that were missing when data was collected
     */
    private static final int NUMBER_OF_MISSING_OBSERVATIONS = 10;

    /**
     * An Integer object that contains the total number of stations
     */
    private Integer numberOfStations = 0;

    /**
     * Parameter ID for air temperatures taken at 9m
     */
    private static final String TA9M = "TA9M";

    /**
     * Parameter ID for air temperatures taken at 1.5m
     */
    private static final String TAIR = "TAIR";

    /**
     * Parameter ID for solar radiation stats
     */
    private static final String SRAD = "SRAD";

    /**
     * Parameter ID for finding observation's location
     */
    private static final String STID = "STID";

    /**
     * ID used for all average calculations
     */
    private static final String MESONET = "Mesonet";

    /**
     * String that will contain the name of the data file in a specific format
     */
    private String fileName;

    /**
     * A calendar object that takes in the date and time that the data was gathered.
     * Holds the year, month, day, hour, and minute.
     */
    private GregorianCalendar utcDateTime;

    /**
     * Constructor for creating a map that holds data from different areas of
     * Oklahoma. Data is taken from Mesonet which is a weather system with stations
     * throughout Oklahoma.
     * 
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param directory
     */
    public MapData(int year, int month, int day, int hour, int minute, String directory)
    {
        this.utcDateTime = new GregorianCalendar(year, month, day, hour, minute);
        createFileName(year, month, day, hour, minute, directory);
    }

    /**
     * Creates the name of the file in which MapData comes from
     * 
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param directory
     * @return String that contains the file name
     */
    public String createFileName(int year, int month, int day, int hour, int minute, String directory)
    {
        fileName = directory;
        fileName += "/";
        fileName += String.format("%04d", year);
        fileName += String.format("%02d", month);
        fileName += String.format("%02d", day);
        fileName += String.format("%02d", hour);
        fileName += String.format("%02d", minute);
        fileName += ".mdf";

        return fileName;
    }

    /**
     * Method created to set the paramId position values. Allows for specific
     * columns of data to be read in based on the paramId values.
     * 
     * @param inParamStr
     */
    private void parseParamHeader(String inParamStr)
    {
        String[] parsedParams = inParamStr.split("\\s+");
        paramPositions = new TreeMap<String, Integer>();
        int index = 0;
        while (index < parsedParams.length)
        {
            if (parsedParams[index].equalsIgnoreCase(TAIR))
            {
                paramPositions.put(TAIR, index);
            }
            if (parsedParams[index].equalsIgnoreCase(TA9M))
            {
                paramPositions.put(TA9M, index);
            }
            if (parsedParams[index].equalsIgnoreCase(SRAD))
            {
                paramPositions.put(SRAD, index);
            }
            if (parsedParams[index].equalsIgnoreCase(STID))
            {
                paramPositions.put(STID, index);
            }
            ++index;
        }
    }

    /**
     * Gets the index of a parameter in a string
     */
    public Integer getIndexOf(String inParamStr)
    {
        return paramPositions.get(inParamStr);
    }

    /**
     * Takes the file and parses it so it can be read
     * 
     * @return void
     */
    public void parseFile() throws IOException
    {
        // Initialize arrays for holding data
        ArrayList<Observation> sradData = new ArrayList<Observation>();
        ArrayList<Observation> tairData = new ArrayList<Observation>();
        ArrayList<Observation> ta9mData = new ArrayList<Observation>();

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String strg; // String that will contain a line of data from the file

        strg = br.readLine(); // Reads in first line of file which is just title info
        strg = br.readLine(); // Reads in next line which is information about time and date observations were
                              // made
        strg = br.readLine(); // Reads in the headers
        parseParamHeader(strg); // Creates positions of tair, ta9m, stid and srad.
        strg = br.readLine(); // First line of actual data

        int index = 0;
        while (strg != null)// Once file has no more lines, will exit loop
        {
            String[] strgSplit = strg.split("\\s+");
            sradData.add(index,
                    (new Observation(Double.parseDouble(strgSplit[getIndexOf(SRAD)]), strgSplit[getIndexOf(SRAD)])));
            tairData.add(index,
                    (new Observation(Double.parseDouble(strgSplit[getIndexOf(TAIR)]), strgSplit[getIndexOf(TAIR)])));
            ta9mData.add(index,
                    (new Observation(Double.parseDouble(strgSplit[getIndexOf(TA9M)]), strgSplit[getIndexOf(TA9M)])));

            strg = br.readLine(); // Reads in a new line of data

            numberOfStations++; // Documents each new station
            index++;
        }
        br.close();

        // Create the data catalog
        prepareDataCatalog();
        dataCatalog.put(TAIR, tairData);
        dataCatalog.put(SRAD, sradData);
        dataCatalog.put(TA9M, ta9mData);

        // Calculates the statistics
        calculateStatistics();
    }

    /**
     * Calculates statistics for all parameters
     */
    private void calculateAllStatistics()
    {
        statistics = new EnumMap<StatsType, TreeMap<String, Statistics>>(StatsType.class);
        Set<String> parameterIds = dataCatalog.keySet();
        TreeMap<String, Statistics> maxStats = new TreeMap<String, Statistics>();
        TreeMap<String, Statistics> minStats = new TreeMap<String, Statistics>();
        TreeMap<String, Statistics> avgStats = new TreeMap<String, Statistics>();
        TreeMap<String, Statistics> totStats = new TreeMap<String, Statistics>();
        for (String paramId : parameterIds)
        {
            ArrayList<Observation> data = dataCatalog.get(paramId);
            double totalTemp = 0.0;
            double maxTemp = data.get(0).getValue();
            double minTemp = data.get(0).getValue();
            String maxStid = data.get(0).getStid();
            String minStid = data.get(0).getStid();
            for(Observation observ : data)
            {
                if (observ.isValid())
                {
                    if (observ.getValue() > maxTemp)
                    {
                        maxTemp = observ.getValue();
                        maxStid = observ.getStid();
                    }
                    if (observ.getValue() < minTemp)
                    {
                        minTemp = observ.getValue();
                        minStid = observ.getStid();
                    }
                    totalTemp += observ.getValue();
                }
            }
            Statistics max = new Statistics(maxTemp, maxStid, this.utcDateTime,
                    numberOfStations - NUMBER_OF_MISSING_OBSERVATIONS, StatsType.MAXIMUM);
            maxStats.put(paramId, max);

            Statistics min = new Statistics(minTemp, minStid, this.utcDateTime,
                    numberOfStations - NUMBER_OF_MISSING_OBSERVATIONS, StatsType.MINIMUM);
            minStats.put(paramId, min);

            Statistics average = new Statistics((totalTemp / numberOfStations), MESONET, this.utcDateTime,
                    numberOfStations - NUMBER_OF_MISSING_OBSERVATIONS, StatsType.AVERAGE);
            avgStats.put(paramId, average);

            Statistics total = new Statistics(totalTemp, MESONET, this.utcDateTime,
                    numberOfStations - NUMBER_OF_MISSING_OBSERVATIONS, StatsType.TOTAL);
            totStats.put(paramId, total);
            
            statistics.put(StatsType.MAXIMUM, maxStats);
            statistics.put(StatsType.MINIMUM, minStats);
            statistics.put(StatsType.AVERAGE, avgStats);
            statistics.put(StatsType.TOTAL, totStats);
        }
    }

    /**
     * Creates the data catalog
     */
    private void prepareDataCatalog()
    {
        dataCatalog = new HashMap<String, ArrayList<Observation>>();
    }

    /**
     * Calculates the max, min, average and total values for tairData, ta9mData and
     * sradData.
     */
    private void calculateStatistics()
    {
        calculateAllStatistics();
    }

    /**
     * Gets statistics for a certain type and parameter (SRD, TAIR, etc...)
     */
    public Statistics getStatistics(StatsType type, String paramid)
    {
        TreeMap<String, Statistics> stats = statistics.get(type);
        return stats.get(paramid);
    }

    /**
     * Outputs stats about map data in a specific format.
     * 
     * @return String
     */
    public String toString()
    {
        String out = "";
        String seperator = ("===========================================================\n");
        String date = String.format("%04d-%02d-%02d %02d:%02d", utcDateTime.get(Calendar.YEAR),
                utcDateTime.get(Calendar.MONTH), utcDateTime.get(Calendar.DAY_OF_MONTH), utcDateTime.get(Calendar.HOUR),
                utcDateTime.get(Calendar.MINUTE));
        out += ("=== " + date + " ===\n");
        out += seperator;
        out += ("Maximum Air Temperature[1.5m] = " + getStatistics(StatsType.MAXIMUM, TAIR).getValue() + " C at " + getStatistics(StatsType.MAXIMUM, TAIR).getStid() + "\n");
        out += ("Minimum Air Temperature[1.5m] = " + statistics.get(StatsType.MINIMUM).get(TAIR).getValue() + " C at " + statistics.get(StatsType.MINIMUM).get(TAIR).getStid() + "\n");
        out += String.format("Average Air Temperature[1.5m] = " + "%.1f" + " C at " + statistics.get(StatsType.AVERAGE).get(TAIR).getStid() + "\n",
                statistics.get(StatsType.AVERAGE).get(TAIR).getValue());
        out += seperator + seperator;
        out += ("Maximum Air Temperature[9.0m] = " + statistics.get(StatsType.MAXIMUM).get(TA9M).getValue() + " C at " + statistics.get(StatsType.MAXIMUM).get(TA9M).getStid()) + "\n";
        out += ("Minimum Air Temperature[9.0m] = " + statistics.get(StatsType.MINIMUM).get(TA9M).getValue() + " C at " + statistics.get(StatsType.MINIMUM).get(TA9M).getStid() + "\n");
        out += String.format("Average Air Temperature[9.0m] = " + "%.1f" + " C at " + statistics.get(StatsType.AVERAGE).get(TA9M).getStid() + "\n",
                statistics.get(StatsType.AVERAGE).get(TA9M).getValue());
        out += seperator + seperator;
        out += ("Maximum Solar Radiation[1.5m] = " + statistics.get(StatsType.MAXIMUM).get(SRAD).getValue() + " W/m^2 at " + statistics.get(StatsType.MAXIMUM).get(SRAD).getStid() + "\n");
        out += ("Minimum Solar Radiation[1.5m] = " + statistics.get(StatsType.MINIMUM).get(SRAD).getValue() + " W/m^2 at " + statistics.get(StatsType.MINIMUM).get(SRAD).getStid() + "\n");
        out += String.format("Average Solar Radiation[1.5m] = " + "%.1f" + " W/m^2 at " + statistics.get(StatsType.AVERAGE).get(SRAD).getStid() + "\n",
                statistics.get(StatsType.AVERAGE).get(SRAD).getValue());
        out += seperator;

        return out;
    }
}
