import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MonthView extends JPanel implements ChangeListener {	
	private final int CELL_WIDTH = 25;
	
	private JTextArea[][] dateGrid;
	private JTextArea selected;
	private JPanel dateSelectorPane;
	private JTextArea monthYear;
	private EventCalendar calendar;
	
	////////////////
	// Constructor
	////////////
	
	public MonthView(int width, int height, EventCalendar calendar) {
		dateGrid = new JTextArea[7][7];
		selected = new JTextArea();
		
		this.calendar = calendar;
		this.calendar.attach(this);
				
		this.setPreferredSize(new Dimension(width/4, height));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(Color.WHITE);

		// Create event button
		JButton createBtn = new JButton("CREATE");
		createBtn.setPreferredSize(new Dimension(70, 35));
		createBtn.setBackground(Color.RED);
		createBtn.setForeground(Color.WHITE);
		createBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createEvent();
			}
		
		});

		createBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(createBtn);
		
		// Calendar month and year header
		monthYear = new JTextArea();
		monthYear.setPreferredSize(new Dimension(70,40));
		monthYear.setText(EventCalendar.getMonth(calendar.getDate().getMonth()) + " " + calendar.getDate().getYear());
		monthYear.setEditable(false);
		monthYear.setFocusable(false);
		monthYear.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(monthYear);

		// Creating calendar components wrapper
		JPanel datesPane = new JPanel();
		datesPane.setPreferredSize(new Dimension(width, height));
		
		// Creating invisible pane for date selection
		dateSelectorPane = new JPanel();
		dateSelectorPane.setPreferredSize(new Dimension(CELL_WIDTH*7, CELL_WIDTH*7));
		
		// Creating calendar grid
		JPanel calendarGrid = new JPanel();
		calendarGrid.setLayout(new GridLayout(7,7));
		calendarGrid.setPreferredSize(new Dimension(CELL_WIDTH*7, CELL_WIDTH*7));
		calendarGrid.setBackground(Color.WHITE);
		calendarGrid.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		
		// Populating calendar day of week header
		String[] days = {"S", "M", "T", "W", "T", "F", "S"};
		
		for(int i = 0; i < 7; i++) {
			dateGrid[0][i] = new JTextArea(days[i]);
			dateGrid[0][i].setEditable(false);
			dateGrid[0][i].setFocusable(false);
			calendarGrid.add(dateGrid[0][i]);
		}
		
		// Populating calendar grid
		int monthStart = EventCalendar.getDayIntValue(calendar.getDate().getMonth() + "/01/" + calendar.getDate().getYear()) + 1;
		int monthSize = EventCalendar.getMonthSize(Integer.parseInt(calendar.getDate().getMonth()));
		int date = 1;

		for(int j = 1; j < 7; j++) {
			for(int k = 0; k < 7; k++) {
				int dayCursor = j*7+k-6;
				
				dateGrid[j][k] = new JTextArea(" ");
				dateGrid[j][k].setEditable(false);
				dateGrid[j][k].setFocusable(false);
				dateGrid[j][k].addMouseListener(new MouseAdapter() {
		
					public void mouseClicked(MouseEvent e) {
						JTextArea clickedArea = ((JTextArea) e.getSource()); 
						
						if(!clickedArea.getText().equals(" ")) {
							calendar.setDay(clickedArea.getText());
						}
					}
				});
				
				if(dayCursor >= monthStart && date <= monthSize) {
					dateGrid[j][k].setText(String.valueOf(date));
					date++;
				}
				
				if(calendar.getDate().getDay().equals(dateGrid[j][k].getText())) {
					selectDate(dateGrid[j][k]);
				}
				
				calendarGrid.add(dateGrid[j][k]);
			}
		}
				
		dateSelectorPane.setPreferredSize(new Dimension(datesPane.getPreferredSize()));
		
		datesPane.add(calendarGrid);
		datesPane.add(dateSelectorPane);
		
		datesPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(datesPane);
	}
	
	////////////
	// Methods
	////////
	
	public void createEvent() {
		EventCreationFrame cFrame = new EventCreationFrame();
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		// Updating month and year header		
		monthYear.setText(EventCalendar.getMonth(calendar.getDate().getMonth()) + " " + calendar.getDate().getYear());
		
		// Updating calendar grid dates
		updateDateGrid();
		
		// Updating selected date on calendar		
		for(int i = 1; i < 7; i++) {
			for(JTextArea day : dateGrid[i]) {
				if(day.getText().equals(calendar.getDate().getDay())) {
					selectDate(day);
				}
			}
		}
	}
	
	public void updateDateGrid() {
		//get week day for 1st of month
		//start loop there, loop for getmonthsize days
			//set text
			//if current date then selectDate()
		int monthStart = EventCalendar.getDayIntValue(calendar.getDate().getMonth() + "/01/" + calendar.getDate().getYear()) + 1;
		int monthSize = EventCalendar.getMonthSize(Integer.parseInt(calendar.getDate().getMonth()));
		int date = 1;

		for(int i = 1; i < 7; i++) {
			for(int j = 0; j < 7; j++) {
				int dayCursor = i*7+j-6;
				
				if(dayCursor >= monthStart && date <= monthSize) {
					dateGrid[i][j].setText(String.valueOf(date));
					date++;
				} else {
					dateGrid[i][j].setText(" ");
				}
			}
		}
	}
	
	public void selectDate(JTextArea textArea) {
		selected.setBorder(BorderFactory.createEmptyBorder());
		textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		selected = textArea;
	}
	
	////////////////////////////////////
	// inner class: EventCreationFrame
	////////////////////////////////
	
	private class EventCreationFrame {
		public EventCreationFrame() {
			JFrame cFrame = new JFrame();
			cFrame.setSize(500, 200);
			cFrame.setTitle("New Event: " + calendar.getDate());
			
			JPanel northPane = new JPanel();
			northPane.setPreferredSize(new Dimension(500,50));
			
			// Event name or info entry field
			JTextArea eventName = new JTextArea();
			eventName.setText("Untitled event");
			eventName.setPreferredSize(new Dimension(450,30));
			northPane.add(eventName);
			
			JPanel southPane = new JPanel();
			southPane.setPreferredSize(new Dimension(500,100));
			
			// Event date from selected date in calendar
			JTextField createDate = new JTextField(calendar.getDate().toString());
			createDate.setPreferredSize(new Dimension(75,50));
			createDate.setEditable(false);
			createDate.setFocusable(false);
			southPane.add(createDate);
			
			// Event start time with default 09:30am to hint at format
			JTextArea startTimeArea = new JTextArea("09:30am");
			startTimeArea.setPreferredSize(new Dimension(75,50));
			southPane.add(startTimeArea);
			
			// "to" label
			JTextField toLabel = new JTextField("to");
			toLabel.setPreferredSize(new Dimension(20,20));
			toLabel.setBackground(Color.LIGHT_GRAY);
			toLabel.setEditable(false);
			toLabel.setFocusable(false);
			southPane.add(toLabel);
			
			// Event end time with default 11:30am to hint at format
			JTextArea endTimeArea = new JTextArea("11:30am");
			endTimeArea.setPreferredSize(new Dimension(75,50));
			southPane.add(endTimeArea);
			
			// Save button
			JButton saveBtn = new JButton("SAVE");
			saveBtn.setPreferredSize(new Dimension(75,50));
			saveBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean added = calendar.addEvent(calendar.getDate().toString(), startTimeArea.getText(), endTimeArea.getText(), eventName.getText());
					
					if(added) {
						cFrame.dispose();
					} else {
						JFrame errorFrame = new JFrame();
						
						// Creating error text
						JTextArea error = new JTextArea("There is already an event scheduled during that time. \nPlease try again.");
						error.setEditable(false);
						error.setFocusable(false);
						errorFrame.add(error, BorderLayout.NORTH);

						// Creating OK button
						JButton btnOfAcceptance = new JButton("OK");
						btnOfAcceptance.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								errorFrame.dispose();
							}
						});						
						
						errorFrame.add(btnOfAcceptance, BorderLayout.SOUTH);
						
						errorFrame.pack();
						errorFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
						errorFrame.setVisible(true);
					}
				}
			});
			
			southPane.add(saveBtn);
			
			cFrame.add(northPane, BorderLayout.NORTH);
			cFrame.add(southPane, BorderLayout.SOUTH);
			cFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			cFrame.setVisible(true);
		}
	}
}
