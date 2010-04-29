import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

//1 Jan 1900 was a Monday.
//Thirty days has September,
//April, June and November.
//All the rest have thirty-one,
//Saving February alone,
//Which has twenty-eight, rain or shine.
//And on leap years, twenty-nine.
//A leap year occurs on any year evenly divisible by 4, but not on a century unless it is divisible by 400.

public class Program {
	
	final int[] MONTHS = new int[] {
			31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
	};

	final int[] MONTHS_LEAP = new int[] {
			31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
	};
	
	private int day = 0;
	private int firstSundays = 0;
	
	public void run()
	{
		for(int year = 1900; year < 2001; year++)
		{
			final int[] months = ((year%4 == 0) && !(year%100 == 0 && year%400 != 0) ? MONTHS_LEAP : MONTHS);
			for (int month = 0; month < months.length; month++) {
				if(day%7 == 6)
				{
					if(year > 1900)
						firstSundays++;
					printDate(year, month, 1);
				}
				for(int j = 0; j < months[month]; j++)
				{
					day++;
				}
			}
		}
		System.out.println("Number of sunday the 1st: " + firstSundays);
	}
	
	public static void printDate(int year, int month, int day)
	{
		GregorianCalendar calendar = new GregorianCalendar(year, month, day);
		System.out.println(DateFormat.getDateInstance(DateFormat.FULL, Locale.US).format(calendar.getTime()));
	}
	
	public static void main(String[] args) {
		new Program().run();
	}
}
