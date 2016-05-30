import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class CalendarFrame extends JFrame {
	private final int WIDTH = 800;
	private final int HEIGHT = 350;
	
	private MonthView monthView;
	private DayView dayView;
	private EventCalendar calendar;
	
	////////////////
	// Constructor 
	////////////
	
	public CalendarFrame() {
		JPanel mainPane = new JPanel();
		JPanel northPanel = new JPanel();
		JPanel navBar = new JPanel();
		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");		
		calendar = new EventCalendar(dateFormat.format(new Date()));
		
		File eventsFile = new File("events.txt");
		
		if(eventsFile.exists()) { 
			loadEvents();
		}
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout());
		
		monthView = new MonthView(WIDTH, HEIGHT - 100, calendar);
		southPanel.add(monthView, BorderLayout.WEST);
		
		dayView = new DayView(WIDTH-30, HEIGHT - 100, calendar);
		southPanel.add(dayView, BorderLayout.EAST);
		
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT - 100));		
		this.setTitle("SimpleGUICalendar");
		this.setSize(WIDTH, HEIGHT);
		
		mainPane.setLayout(new BorderLayout());

		JButton navLeftBtn = new JButton("<");
		navLeftBtn.setPreferredSize(new Dimension(50, 40));
		navLeftBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// change active day to left
				calendar.incrementDate(false);
			}
		});
		
		navBar.add(navLeftBtn);		

		JButton navRightBtn = new JButton(">");
		navRightBtn.setPreferredSize(new Dimension(50, 40));
		navRightBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// change active day to right
				calendar.incrementDate(true);
			}
		});
			
		navBar.add(navRightBtn);
		northPanel.add(navBar);
		
		JButton closeBtn = new JButton("Quit");
		closeBtn.setPreferredSize(new Dimension(70, 40));
		closeBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Saving event data
				saveEvents();

				System.exit(0);
			}
			
		});
		
		northPanel.add(closeBtn, BorderLayout.EAST);
		
		mainPane.add(northPanel, BorderLayout.NORTH);
		mainPane.add(southPanel, BorderLayout.SOUTH);
		
		this.add(mainPane);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void saveEvents() {
		FileOutputStream fileOutStream = null;
		ObjectOutputStream objOutStream = null;
		
		try {
			fileOutStream = new FileOutputStream("events.txt");
			objOutStream = new ObjectOutputStream(fileOutStream);
			objOutStream.writeObject(calendar.getEvents());
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		if(fileOutStream != null) {
			try {
				fileOutStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(objOutStream != null) {
			try {
				objOutStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void loadEvents() {
		ObjectInputStream objInStream = null;
		FileInputStream fileInStream = null;
		
		try {
		    fileInStream = new FileInputStream("events.txt");
		    objInStream = new ObjectInputStream(fileInStream);
		    TreeMap<String, TreeMap<String, EventCalendar.Event>> events = null;  
		    events = (TreeMap<String, TreeMap<String, EventCalendar.Event>>) objInStream.readObject();
		    
		    if(events != null){
		    	calendar.setEvents(events);
		    } 
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
		    if(objInStream != null){
		    	try {
					objInStream .close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    } 
		}
	}
}
