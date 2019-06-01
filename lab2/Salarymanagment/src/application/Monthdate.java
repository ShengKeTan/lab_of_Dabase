package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Monthdate {
	private final StringProperty eid;
	private final StringProperty ename;
	private final StringProperty dname;
	private final StringProperty pname;
	private final StringProperty rest;
	private final StringProperty duty;
	private final StringProperty extra;
	private final StringProperty bpay;
	private final StringProperty salary;
	private final StringProperty time;
	
	public Monthdate(String eid, String ename, String dname, String pname, String rest, String duty, 
			String extra, String bpay, String salary, String time) {
		
		this.eid = new SimpleStringProperty(eid); 
		this.ename = new SimpleStringProperty(ename); 
		this.dname = new SimpleStringProperty(dname); 
		this.pname = new SimpleStringProperty(pname); 
		this.rest = new SimpleStringProperty(rest); 
		this.duty = new SimpleStringProperty(duty);
		this.extra = new SimpleStringProperty(extra);
		this.bpay = new SimpleStringProperty(bpay);
		this.salary = new SimpleStringProperty(salary);
		this.time = new SimpleStringProperty(time); 
	}
	
	public String getEid() {
		return eid.get();
	}
	public String getEname() {
		return ename.get();
	}
	public String getDname() {
		return dname.get();
	}
	public String getPname() {
		return pname.get();
	}
	public String getRest() {
		return rest.get();
	}
	public String getDuty() {
		return duty.get();
	}
	public String getExtra() {
		return extra.get();
	}
	public String getBpay() {
		return bpay.get();
	}
	public String getSalary() {
		return salary.get();
	}
	public String getTime() {
		return time.get();
	}
}
