package logic;
import java.util.Calendar;

public class Event extends DeadlineTask {

	protected Calendar start;
	
	public Event() {
		
	}
	
	public Event(userData parsedInput) {
		super();
		this.start = parsedInput.start;
	}
	
	public void createEvent(Event newEvent) {
		Storage storage = new Storage();
		storage.add(newEvent);
	}
	

	public Calendar getStart() {
		return start;
	}

	protected void setStart(Calendar start) {
		this.start = start;
	}
}
