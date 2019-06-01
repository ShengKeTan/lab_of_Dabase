package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Workdate {
	private final StringProperty name;
	private final StringProperty begin_time;
	private final StringProperty end_time;
	private final StringProperty type;
	private final StringProperty times;
	private final StringProperty extra;
	private final StringProperty state;
	
	public Workdate(String name, String begin_time, String end_time, String type, String times, String extra, String state) {
		this.name = new SimpleStringProperty(name);
		this.begin_time = new SimpleStringProperty(begin_time);
		this.end_time = new SimpleStringProperty(end_time);
		this.type = new SimpleStringProperty(type);
		this.times = new SimpleStringProperty(times);
		this.extra = new SimpleStringProperty(extra);
		this.state = new SimpleStringProperty(state);
	}
	public String getName() {
		return name.get();
	}
	public String getBtime() {
		return begin_time.get();
	}
	public String getEtime() {
		return end_time.get();
	}
	public String getType() {
		return type.get();
	}
	public String getTimes() {
		return times.get();
	}
	public String getExtra() {
		return extra.get();
	}
	public String getState() {
		return state.get();
	}

}
