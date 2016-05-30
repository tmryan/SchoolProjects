import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Model for the SimpleGUICalendar project. Contains a data structure with 
 * year, month, and day. Each day contains a list of EventCalendar.Event objects.
 * 
 * @author Thomas Ryan
 *
 */
public class EventCalendar implements Serializable {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TreeMap<String, TreeMap<String, Event>> events;
	private ArrayList<ChangeListener> views;
	private Date dateCursor;
	
	// Lists of month and day String constants
	private static String[] months;
	private static String[] days;
	
	////////////////
	// Constructor
	/////////////
	
	/**
	 * Instantiates a new EventCalendar object complete with events data structure 
	 * as well as month and day lookup tables.
	 */
	public EventCalendar(String date) {
		events = new TreeMap<String, TreeMap<String, Event>>();
		months = new String[] {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		days = new String[] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
		views = new ArrayList<ChangeListener>();
		dateCursor = new Date(date);
	}
	
	////////////
	// Methods
	/////////
	
	/**
	 * Handles the addition of new events to the calendar.
	 * 
	 * @param date			A string representation of the initial date 
	 * 						in MM/DD/YYYY format.
	 * @param timeStart		The start time for the event.	
	 * @param timeEnd		The end time for the event.
	 * @param name			The name, or title, of the event.
	 * 
	 * @return				Returns true if the event was successfully added.
	 */
	public boolean addEvent(String date, String timeStart, String timeEnd, String name) {
		Event event = new Event(date, timeStart, timeEnd, name);
		boolean added = false;
				
		// If year not in calendar, add it
		if(!events.containsKey(date)) {
			events.put(date, new TreeMap<String, Event>());
		}
		
		// Add event to calendar if no overlap
		if(events.get(date).containsKey(timeStart)) {
			for(String time : events.get(date).keySet()) {
				if(!events.get(date).get(time).contains(event)) {
					events.get(date).put(timeStart, event);
					added = true;
				}
			}
		} else {
			events.get(date).put(timeStart, event);
			added = true;
		}
		
		notifyViews();
		
		return added;
	}
	
	/**
	 * Returns an ArrayList of Event objects for a given date.
	 * 	
	 * @param date			A string representation of the initial date 
	 * 						in MM/DD/YYYY format.
	 * @return				An ArrayList of String objects containing all
	 * 						events scheduled for the specified day.
	 */
	public ArrayList<Event> getEventsByDay(String date) {
		ArrayList<Event> daysEvents = null;
		
		if(events.containsKey(date)) {
			daysEvents = new ArrayList<Event>();
			
			for(String time : events.get(date).keySet()) {
				daysEvents.add(events.get(date).get(time));
			}
		}
		
		return daysEvents;
	}

	public void incrementDate(boolean increment) {
		if(increment) {
			dateCursor.nextDay();
		} else {
			dateCursor.prevDay();
		}
		
		notifyViews();
	}
	
	/**
	 * Returns the events data structure for serialization.
	 * 
	 * @return			This object's events data structure.
	 */
	public TreeMap<String, TreeMap<String, Event>> getEvents() {
		return events;
	}
	
	/**
	 * Takes a deserialized events data structure.
	 */
	public void setEvents(TreeMap<String, TreeMap<String, Event>> events) {
		this.events = events;
	}
	
	public EventCalendar.Date getDate() {
		return dateCursor;
	}
	
	public void setDay(String day) {
		dateCursor.setDay(day);
		
		notifyViews();
	}
	
	/**
	 * Notifies attached views of an update to the model.
	 */
	public void notifyViews() {
		for(ChangeListener view : views) {
			view.stateChanged(new ChangeEvent(this));
		}
	}
	
	/**
	 * Attaches views to the model.
	 * 
	 * @param view			View implementing ChangeListener to be added.
	 */
	public void attach(ChangeListener view) {
		views.add(view);
	}
	
	////////////////////
	// Utility Methods
	/////////////////
	
	/**
	 * Trims each String object in a given array of Strings.
	 * 
	 * @param strings 		Array of Strings to be trimmed.
	 */
	private static void trimAll(String[] strings) {
		for(String string : strings) {
			string = string.trim();
		}
	}
	
	////////////////////////
	// Date/Time Utilities
	/////////////////////
	
	/**
	 * Verifies that the given String matches the date format used by this 
	 * calendar. The method is static so that callers can verify their input
	 * prior to utilizing an EventCalendar object.
	 * 
	 * @param date			A string representation of the initial date 
	 * 						in MM/DD/YYYY format.
	 * @return				True if the String is a valid date format, false if not.
	 */
	public static boolean isValidDate(String date) {
		boolean valid = false;
		
		String[] dateInfo = date.split("/");
		trimAll(dateInfo);

		if(dateInfo[0].length() <= 2 && dateInfo[0].length() >= 1 && 
		   dateInfo[1].length() <= 2 && dateInfo[1].length() >= 1 && 
		   dateInfo[2].length() == 4) {
			int month = Integer.parseInt(dateInfo[0]);
			int day = Integer.parseInt(dateInfo[1]);
			int year = Integer.parseInt(dateInfo[2]);

			// Verify the month and year match proper formatting
			if(12 - month >= 0 && String.valueOf(year).equals(dateInfo[2])) {
				// Now verify the day is correct based on month	
				switch (month) {
					case 1:
					case 3:
					case 5:
					case 7:
					case 8:
					case 10:
					case 12:
						if(31 - day >= 0) {
							valid = true;
						}
						break;
					case 4:
					case 6:
					case 9:
					case 11:
						if(30 - day >= 0) {
							valid = true;
						}
						break;
					case 2:
						if(28 - day >= 0) {
							valid = true;
						}
						break;
					default:
						break;
				}
			}
		}
		
		return valid;		
	}
	
	/**
	 * Verifies that the given String matches the time format used by this 
	 * calendar. The method is static so that callers can verify their input
	 * prior to utilizing an EventCalendar object.
	 * 
	 * @param time			The time String to be validated.	
	 * @return				True if the time String is valid, false if not.
	 */
	public static boolean isValidTime(String time) {
		boolean valid = false;

		String[] timeInfo = time.split(":");

		// Verify hour and minute in 24 hour format
		if(timeInfo[0].length() <= 2 && timeInfo[0].length() >= 1 && timeInfo[1].length() == 2) {
			int hour = Integer.parseInt(timeInfo[0]);
			int min = Integer.parseInt(timeInfo[1]);
			
			if(23 - hour >= 0 && 59 - min >= 0) {
				valid = true;
			}
		}
		
		return valid;
	}
	
	/**
	 * Looks up a month based on its corresponding integer.
	 * 
	 * @param month				The month integer value to lookup.
	 * @return					A String representation of the month name.
	 */
	public static String getMonth(String month) {
		return months[Integer.parseInt(month) - 1];
	}
	
	/**
	 * Looks up a month based on its corresponding integer provided
	 * in String format.
	 * 
	 * @param month				The month String formatted integer value to lookup.
	 * @return					A String representation of the month name.
	 */
	public static String getMonth(int month) {
		return months[month - 1];
	}
	
	/**
	 * Looks up week day for a given date.
	 * 
	 * @param date			A string representation of the initial date 
	 * 						in MM/DD/YYYY format.
	 * @return				A String representation of the weekday name.
	 */
	public static String getDay(String date) {	
		int wDay = getDayIntValue(date);
		
		return days[wDay];
	}
	
	/**
	 * Calculates the weekday a given date fell on, or will fall on.
	 * 
	 * @param date			A string representation of the initial date 
	 * 						in MM/DD/YYYY format.
	 * @return				An integer value between 0 and 6 representing the day of the week
	 * 						the given date falls on.
	 */
	public static int getDayIntValue(String date) {
		String[] dateInfo = date.split("/");
		trimAll(dateInfo);

		int month = Integer.parseInt(dateInfo[0]);
		int day = Integer.parseInt(dateInfo[1]);
		int year = Integer.parseInt(dateInfo[2]);
		
		// Based on Gaussian algorithm for finding weekday
		return (day + (int)(2.6*((month-2)%12)-.2) + (5*(year%4)) + (4*(year%100)) + (6*(year)%400))%7;
	}
	
	/**
	 * Looks up the number of days in a given month.
	 * 
	 * @param month				The month integer value to lookup.
	 * @return					The number of days in the given month.
	 */
	public static int getMonthSize(int month) {
		int monthSize = 0;
		
		switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				monthSize = 31;
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				monthSize = 30;
				break;
			case 2:
				monthSize = 28;
				break;
			default:
				break;
		}
		
		return monthSize;
	}
	
	/**
	 * Tests whether a given year is a leap year.
	 * 
	 * @param year			The year to be tested.
	 * @return				True if it is a leap year, false if not.
	 */
	public static boolean isLeapYear(int year) {
		boolean leapYear = true;
		
		// Algorithm from Wikipedia
		if (year % 4 != 0) {
			leapYear = false;
		} else if (year % 100 != 0) {
			leapYear = true;
		} else if (year % 400 != 0) {
			leapYear = false;
		}
		
		return leapYear;
	}
		
	/////////////////////////
	//// nested class: Event
	//////////////////////
	
	/**
	 * Inner class Event for the EventCalendar class. This class is a model
	 * for a scheduled event.
	 * 
	 * @author Thomas Ryan
	 *
	 */
	public static class Event implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private Date date;
		private Time startTime;
		private Time endTime;
		private String name;
		
		////////////////
		// Constructors
		/////////////
		
		/**
		 * Instantiates a new Event object with the given state.
		 * 
		 * @param date			A string representation of the initial date 
		 * 						in MM/DD/YYYY format.			
		 * @param timeStart		Time that the event starts at.	
		 * @param timeEnd		Time that the event ends at.
		 * @param name			Name, or title, of the event.
		 */
		public Event(String date, String timeStart, String timeEnd, String name) {
			this.date = new Date(date);
			
			String[] dateInfo = date.split("/");
			trimAll(dateInfo);
			
			startTime = new Time(timeStart);
			endTime = new Time(timeEnd);
			this.name = name;
		}
		
		////////////
		// Methods
		/////////
		
		public boolean contains(Event event) {
			boolean contains = true;
			
			if(Integer.parseInt(this.startTime.hrs) > Integer.parseInt(event.endTime.hrs) 
			&& Integer.parseInt(this.endTime.hrs) < Integer.parseInt(event.startTime.hrs)) {
				if(Integer.parseInt(this.startTime.min) > (60 - Integer.parseInt(event.endTime.min))
				&& (60 - Integer.parseInt(this.endTime.min)) < Integer.parseInt(event.startTime.min)) {
					contains = false;
				}				
			}
			
			return contains;
		}
		
		/**
		 * Gets the current date state of an Event.
		 * 
		 * @return				A String representation of the object's current date state.
		 */
		public String getDate() {
			return date.toString();
		}
		
		/**
		 * Modifies the current date state of an Event.
		 * 
		 * @param date			The new date.
		 */
		public void setDate(String date) {
			this.date.setMonth(date.substring(0,2).replaceAll("0", ""));
			this.date.setDay(date.substring(3,5));
			this.date.setYear(date.substring(6));
		}
		
		/**
		 * Gets the current start time state of an Event.
		 * 
		 * @return				The object's current start time state.
		 */
		public String getStartTime() {
			return startTime.toString();
		}
		
		/**
		 * Modifies the current start time state of an Event.
		 * 
		 * @param startTime		The new start time.
		 */
		public void setStartTime(String startTime) {
			this.startTime.setHrs(startTime.substring(0, 3));
			this.startTime.setMin(startTime.substring(4, 6));
			this.startTime.setPeriod(startTime.substring(6));
		}
		
		/**
		 * Gets the current date state of an Event.
		 * 
		 * @return				The object's current end time state.
		 */
		public String getEndTime() {
			return endTime.toString();
		}
		
		/**
		 * Modifies the current end time state of an Event.
		 * 
		 * @param endTime		The new end time.
		 */
		public void setEndTime(String endTime) {
			this.endTime.setHrs(endTime.substring(0, 3));
			this.endTime.setMin(endTime.substring(4, 6));
			this.endTime.setPeriod(endTime.substring(6));
		}
		
		/**
		 * Gets the current name state of an Event.
		 * 
		 * @return				The object's current name or title.
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * Modifies the current start time state of an Event.
		 * 
		 * @param name			The new name.
		 */
		public void setName(String name) {
			this.name = name;
		}
		
		public Date getDateObj() {
			return date;
		}
		
		public Time getStartTimeObj() {
			return startTime;
		}
		
		public Time getEndTimeObj() {
			return startTime;
		}
	}
	
	/////////////////////////
	//// nested class: Time
	/////////////////////

	public static class Time implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		String hrs;
		String min;
		String period;
		
		public Time(String time) {
			// HH:MMpp
			hrs = time.substring(0, 2).replaceAll("0", "");
			min = time.substring(3, 5);
			period = time.substring(5);
		}
		
		public Time(String hrs, String min) {
			this.hrs = hrs;
			this.min = min;
		}

		public String getHrs() {
			return hrs;
		}

		public void setHrs(String hrs) {
			this.hrs = hrs;
		}

		public String getMin() {
			return min;
		}

		public void setMin(String min) {
			this.min = min;
		}
		
		public String getPeriod() {
			return period;
		}

		public void setPeriod(String period) {
			this.period = period;
		}
		
		@Override
		public String toString() {
			// Print time as "hh:mm", "0h:0m", ...
			return ((hrs.length() < 2) ? "0" + hrs : hrs) + ":" + ((min.length() < 2) ? "0" + min : min) + period;
		}
	}
	
	/////////////////////////
	//// nested class: Date
	/////////////////////
	
	public static class Date implements Serializable {
		/**
		 * Did somebody say cereal?
		 */
		private static final long serialVersionUID = 1L;
		
		private String month;
		private String day;
		private String year;
		
		public Date(String date) {
			month = date.substring(0,2).replaceAll("0", "");
			day = date.substring(3,5);
			year = date.substring(6);
		}
		
		public Date(String month, String day, String year) {
			this.month = month;
			this.day = day;
			this.year = year;
		}

		public void nextDay() {
			// Leaving these operations here instead of storing integers in object
			// Not noticeably slower, easier to follow, and less clutter
			// in other methods
			int intDay = Integer.parseInt(day);
			int intMonth = Integer.parseInt(month);
			
			// If not last day of month then increment
			if(intDay < getMonthSize(intMonth)) {
				day = String.valueOf(intDay+1);
			} else {
				// else increment month as well
				if(intMonth < 12) {
					month = String.valueOf(intMonth+1);
					day = "1";
				} else {
					month = "1";
					day = "1";
					year = String.valueOf(Integer.parseInt(year)+1);
				}
			}
		}
		
		public void prevDay() {
			int intDay = Integer.parseInt(day);
			int intMonth = Integer.parseInt(month);
			
			// If not first day of month then decrement
			if(intDay > 1) {
				day = String.valueOf(intDay-1);
			} else {
				// else decrement month as well
				if(intMonth > 1) {
					intMonth--;
					month = String.valueOf(intMonth);
					day = String.valueOf(getMonthSize(intMonth));
				} else {
					month = "12";
					day = String.valueOf(getMonthSize(intMonth));
					year = String.valueOf(Integer.parseInt(year)-1);
				}
			}
		}
		
		public String getMonth() {
			return month;
		}

		public void setMonth(String month) {
			this.month = month;
		}

		public String getDay() {
			return day;
		}

		public void setDay(String day) {
			this.day = day;
		}

		public String getYear() {
			return year;
		}

		public void setYear(String year) {
			this.year = year;
		}
		
		@Override
		public String toString() {
			return ((month.length() < 2) ? "0" + month : month) + "/" + ((day.length() < 2) ? "0" + day : day) + "/" + year; 
		}
	}
}
