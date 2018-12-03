import java.util.Date;
import java.util.GregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Statistics extends Observation implements DateTimeComparable
{
    /**
     * The format for our toString method
     */
    protected String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss z";
    
    /**
     * Formatter for dates
     */
    protected DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

    /**
     * The time as a GregorianCalendar object
     */
    private GregorianCalendar utcDateTime;
    
    /**
     * The time as a ZonedDateTime object
     */
    private ZonedDateTime zdtDateTime;

    /**
     * The number of stations used for calculations
     */
    private int numberOfReportingStations;

    /**
     * Determines what kind of stat this statistic is
     */
    private StatsType statType;
    
    /**
     * Constructor using a String as the time
     */
    public Statistics(double value, String stid, String dateTimeStr, int numberOfValidStations, StatsType inStatType) throws ParseException
    {
        super(value, stid);
        this.utcDateTime = createDateFromString(dateTimeStr);
        this.numberOfReportingStations = numberOfValidStations;
        this.statType = inStatType;
        this.zdtDateTime = createZDateFromString(dateTimeStr);
    }

    /**
     * Constructor using a ZonedDateTime as the time
     */
    public Statistics(double value, String stid, ZonedDateTime dateTime, int numberOfValidStations, StatsType inStatType)
    {
        super(value, stid);
        this.zdtDateTime = dateTime;
        this.numberOfReportingStations = numberOfValidStations;
        this.statType = inStatType;
    }

    /**
     * Constructor using a GregorianCalendar as the time
     */
    public Statistics(double value, String stid, GregorianCalendar dateTime, int numberOfValidStations,
            StatsType inStatType)
    {
        super(value, stid);
        this.utcDateTime = dateTime;
        this.numberOfReportingStations = numberOfValidStations;
        this.statType = inStatType;
    }

    /**
     * Creates a date given a string
     */
    public GregorianCalendar createDateFromString(String dateTimeStr) throws ParseException
    {
        SimpleDateFormat formater = new SimpleDateFormat(DATE_TIME_FORMAT);
        Date time = formater.parse(dateTimeStr);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.getTime();
        calendar.setTime(time);
        return calendar;
    }
    
    /**
     * Creates a ZonedDateTime using a string
     */
    public ZonedDateTime createZDateFromString(String dateTimeStr)
    {
        ZonedDateTime time = ZonedDateTime.parse(dateTimeStr, format);
        return time;
    }

    /**
     * Creates a string given a GregorianCalendar date
     */
    public String createStringFromDate(GregorianCalendar calendar)
    {
        SimpleDateFormat formater = new SimpleDateFormat(DATE_TIME_FORMAT);
        String out = formater.format(calendar.getTime());
        return out;
    }
    
    /**
     * Creates a string given a ZonedDateTime date
     */
    public String createStringFromDate(ZonedDateTime calendar)
    {
        String out = calendar.format(format);
        return out;
    }

    /**
     * Gets the number of reporting stations in the list
     */
    public int getNumberOfReportingStations()
    {
        return this.numberOfReportingStations;
    }

    /**
     * Getter for the date as a string
     */
    public String getUTCDateTimeString()
    {
        String out;
        out = createStringFromDate(this.utcDateTime);
        return out;
    }

    /**
     * Checks if time was taken after another
     */
    public boolean newerThan(GregorianCalendar inDateTime)
    {
        boolean check;
        GregorianCalendar cal1 = this.utcDateTime; // Calendar that method is being called on
        check = cal1.after(inDateTime);
        return check;
    }

    /**
     * Checks if time was taken before another
     */
    public boolean olderThan(GregorianCalendar inDateTime)
    {
        boolean check;
        GregorianCalendar cal1 = this.utcDateTime;
        check = cal1.before(inDateTime);
        return check;
    }

    /**
     * Checks if data was taken at the same time
     */
    public boolean sameAs(GregorianCalendar inDateTime)
    {
        boolean check;
        GregorianCalendar cal1 = this.utcDateTime;
        check = cal1.equals(inDateTime);
        return check;
    }

    /**
     * The string format for a statistics.
     * Includes basic information about the value and location of Statistic.
     */
    public String toString()
    {
        String out = "";
        out += String.format("Value: " + "%.1f" + "\n" + "Station: " + "%s", this.getValue(), this.getStid());
        return out;
    }

    /**
     * Checks if time is taken after
     */
    public boolean newerThan(ZonedDateTime inDateTimeUTC)
    {
        boolean check;
        ZonedDateTime time = this.zdtDateTime;
        check = time.isAfter(inDateTimeUTC);
        return check;
    }

    /**
     * Checks if time was taken before
     */
    public boolean olderThan(ZonedDateTime inDateTimeUTC)
    {
        boolean check;
        ZonedDateTime time = this.zdtDateTime;
        check = time.isBefore(inDateTimeUTC);
        return check;
    }

    /**
     * Checks if data was taken at the same time
     */
    public boolean sameAs(ZonedDateTime inDateTimeUTC)
    {
        boolean check;
        ZonedDateTime time = this.zdtDateTime;
        check = time.isEqual(inDateTimeUTC);
        return check;
    }
}
