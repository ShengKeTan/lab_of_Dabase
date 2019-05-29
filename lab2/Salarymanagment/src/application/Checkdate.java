package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Checkdate {
	
	private final StringProperty ename;
	private final StringProperty dname;
	private final StringProperty time;
	private final StringProperty state;
	
	public Checkdate(String time, String ename, String dname, String state) {
		this.time = new SimpleStringProperty(time);
		this.ename = new SimpleStringProperty(ename);
		this.dname = new SimpleStringProperty(dname);
		this.state = new SimpleStringProperty(state);
	}
	
	public String getTime() {
		return time.get();
	}
	public String getDname() {
		return dname.get();	
	}
	public String getEname() {
		return ename.get();
	}
	public String getState() {
		return state.get();
	}

}
