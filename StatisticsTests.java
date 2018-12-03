import org.junit.Assert;
import java.util.GregorianCalendar;
import java.time.ZonedDateTime;
import java.util.Calendar;
import org.junit.Test;

/**
 * @author Da'Von
 * @param <ZonedDatetime>
 *
 */
public class StatisticsTests
{

    /**
     * Tests the constructor using a string as a time
     */
    @Test
    public void testStatisticsWithString()
    {
        try
        {
            Statistics test = new Statistics(5.0, "TEST", "2000-10-03T07:00:00 UTC", 10, StatsType.TOTAL);
            Assert.assertEquals(5.0, test.getValue(), .1);
            Assert.assertEquals("TEST", test.getStid());
        }
        catch (Exception e)
        {
            Assert.fail("Legal construction failed");
        }
    }

    /**
     * Tests to create a Statistic using ZDT
     */
    @Test
    public void tesStatisticsZDateConstructor()
    {
       try
       {
           ZonedDateTime date = ZonedDateTime.parse("2010-05-10T07:00:00+01:00[Europe/Paris]");
           Statistics stats = new Statistics(10.0, "Test", date, 10, StatsType.AVERAGE);
           stats.isValid();
       }
       catch (Exception e)
       {
           Assert.fail("Exception caught while constructing using a ZonedDateTime");
       }
    }
    
    /**
     *  
     */
    @Test
    public void testStatisticsDoubleStringGregorianCalendarIntStatsType()
    {
        try
        {
            GregorianCalendar gc = new GregorianCalendar(2005, 12, 31, 12, 00);
            Statistics test = new Statistics(10.564, "stid", gc, 10, StatsType.MAXIMUM);
            Assert.assertEquals(10.6, test.getValue(), .1);
            Assert.assertEquals("stid", test.getStid());
        }
        catch (Exception e)
        {
            Assert.fail("Exception caught during legal construction");
        }
    }

    /**
    * 
    */
    @Test
    public void testGetNumberOfReportingStations()
    {
        try
        {
            GregorianCalendar gc = new GregorianCalendar(2005, 10, 10, 12, 00);
            Statistics stats = new Statistics(10.5, "stid", gc, 10, StatsType.MAXIMUM);
            Assert.assertEquals(10, stats.getNumberOfReportingStations());
        }
        catch (Exception e)
        {
            Assert.fail("Exception caught while getting number of stations");
        }
    }

    /**
     * 
     */
    @Test
    public void testGetUTCDateTimeString()
    {
        try
        {
            GregorianCalendar gc = new GregorianCalendar();
            gc.set(Calendar.YEAR, 450);
            gc.set(Calendar.MONTH, 05);
            gc.set(Calendar.DAY_OF_MONTH, 20);
            gc.set(Calendar.HOUR, 5);
            gc.set(Calendar.MINUTE, 0);
            gc.set(Calendar.SECOND, 0);
            
            Statistics stats = new Statistics(4.20, "stid", gc, 5, StatsType.TOTAL);
            Assert.assertEquals("0450-06-20T17:00:00 CST", stats.getUTCDateTimeString());
        }
        catch (Exception e)
        {
            Assert.fail("Exception caught while testing getUTCDate");
        }
    }
    
    /**
     * Tests creating a String from a date
     */
    @Test
    public void testGetStringFromZDate()
    {
        ZonedDateTime date = ZonedDateTime.parse("2007-02-24T10:00:00+01:00[Europe/Paris]");
        Statistics stats = new Statistics(10.0, "Test", date, 10, StatsType.AVERAGE);
        String time = stats.createStringFromDate(date);
        Assert.assertEquals("2007-02-24T10:00:00 CET", time);
        System.out.println(stats.toString());
    }
    
    /**
     * Tests two GregorianCalendars' times compared to each other
     */
    @Test
    public void testGregorianCalendarComparisons()
    {
        GregorianCalendar gc1 = new GregorianCalendar(1,0,0);
        GregorianCalendar gc2 = new GregorianCalendar(2,0,0);
        
        Statistics stat1 = new Statistics(10, "Test1", gc1, 0, StatsType.AVERAGE);
        Statistics stat2 = new Statistics(10, "Test2", gc2, 0, StatsType.AVERAGE);
        
        boolean newer1 = stat1.newerThan(gc2);
        boolean older1 = stat1.olderThan(gc2);
        boolean same1 = stat1.sameAs(gc2);
        
        boolean newer2 = stat2.newerThan(gc1);
        boolean older2 = stat2.olderThan(gc1);
        boolean same2 = stat2.sameAs(gc1);
        
        Assert.assertEquals(newer1, older2);
        Assert.assertEquals(older1, newer2);
        Assert.assertEquals(same1, same2);
    }
    
    /**
     * Tests two ZonedDateTimes' times compared to each other
     */
    @Test
    public void testZonedDateTimeComparisons()
    {
        ZonedDateTime time1 = ZonedDateTime.parse("2007-02-24T10:00:00+01:00[Europe/Paris]");
        ZonedDateTime time2 = ZonedDateTime.parse("2007-03-24T10:00:00+01:00[Europe/Paris]");
        
        Statistics stat1 = new Statistics(10, "Test1", time1, 0, StatsType.AVERAGE);
        Statistics stat2 = new Statistics(10, "Test2", time2, 0, StatsType.AVERAGE);
        
        boolean newer1 = stat1.newerThan(time2);
        boolean older1 = stat1.olderThan(time2);
        boolean same1 = stat1.sameAs(time2);
        
        boolean newer2 = stat2.newerThan(time1);
        boolean older2 = stat2.olderThan(time1);
        boolean same2 = stat2.sameAs(time1);
        
        Assert.assertEquals(newer1, older2);
        Assert.assertEquals(older1, newer2);
        Assert.assertEquals(same1, same2);
    }
}
