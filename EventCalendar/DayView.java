import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DayView extends JPanel implements ChangeListener {
	private HashMap<String,JTextArea> events;
	private JTextArea dateArea;
	private EventCalendar calendar;
	
	////////////////
	// Constructor 
	////////////
	
	public DayView(int width, int height, EventCalendar calendar) {
		events = new HashMap<String,JTextArea>();
		
		this.calendar = calendar;
		this.calendar.attach(this);
		
		this.setPreferredSize(new Dimension(width-width/4, height));
		this.setBackground(Color.GRAY);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Creating date header
		JPanel header = new JPanel();
		header.setPreferredSize(new Dimension(width, height/5));
		header.setBackground(Color.WHITE);
		this.add(header);
		
		// Creating and date for header
		dateArea = new JTextArea();
		updateDateArea();
		dateArea.setBackground(Color.WHITE);
		header.add(dateArea);
		
		// Creating time line
		JPanel eventPane = new JPanel();
		eventPane.setPreferredSize(new Dimension(width, 1085));
		
		// Vertical scroll bar for eventPane
		JScrollPane scrollBars = new JScrollPane(eventPane);
		scrollBars.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollBars.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollBars.getVerticalScrollBar().setUnitIncrement(20);
		this.add(scrollBars);
		
		String[] ampm = {"am", "pm"};
		
		for(int i = 0; i < 2; i++) {			
			for(int j = 0; j < 12; j++) {
				// Creating hourly event views
				JPanel timeLinePane = new JPanel();
				timeLinePane.setPreferredSize(new Dimension(width-40, 40));
				timeLinePane.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
				eventPane.add(timeLinePane);
				
				// Wrapper for time component
				JPanel timePane = new JPanel();
				timePane.setPreferredSize(new Dimension(width/10, 40));
				timePane.setBackground(Color.WHITE);
				timePane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
				timeLinePane.add(timePane);
				
				// Creating time component: "5am"
				JTextArea timeArea = new JTextArea();
				if(j != 0) {
					timeArea.setText(j + ampm[i]);
				} else {
					if(i == 0) {
						timeArea.setText("12" + ampm[0]);
					} else {
						timeArea.setText(12 + ampm[1]);
					}
				}
				
				timeArea.setPreferredSize(new Dimension(35, 20));
				timeArea.setEditable(false);
				timeArea.setFocusable(false);
				timePane.add(timeArea);

				// Event bar one
				JTextArea eventInfoArea = new JTextArea(" ");
				eventInfoArea.setPreferredSize(new Dimension(500, 40));
				eventInfoArea.setEditable(false);
				eventInfoArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
				timeLinePane.add(eventInfoArea);
				
				events.put(j + ampm[i], eventInfoArea);
			}
			
			// Making sure events loaded from file are present
			updateEventsList();
		}
	}
	
	////////////
	// Methods
	////////
	
	@Override
	public void stateChanged(ChangeEvent e) {
		// Updating date header
		updateDateArea();
		
		// Updating list of scheduled events
		updateEventsList();
	}
	
	public void updateDateArea() {
		dateArea.setText(EventCalendar.getDay(calendar.getDate().toString()) + " " 
											+ calendar.getDate().getMonth() + "/" 
											+ calendar.getDate().getDay());
	}
	
	public void updateEventsList() {
		ArrayList<EventCalendar.Event> daysEvents = calendar.getEventsByDay(calendar.getDate().toString());
		
		if(daysEvents != null) {	
			for(EventCalendar.Event event : daysEvents) {
				String time = event.getStartTimeObj().getHrs() + event.getStartTimeObj().getPeriod();	
					
				if(events.containsKey(time)) {
					events.get(time).setText(event.getName() + "\n" + event.getStartTime() + " - " + event.getEndTime());
				} else {
					events.get(time).setText(" ");
				}
			}
		} else {
			for(String time : events.keySet()) {
				events.get(time).setText(" ");
			}
		}
	}
}
