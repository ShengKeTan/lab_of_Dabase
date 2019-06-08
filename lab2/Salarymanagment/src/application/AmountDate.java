package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AmountDate {
	private final StringProperty eid;
	private final StringProperty ename;
	private final StringProperty dname;
	private final StringProperty pname;
	private final StringProperty usrname;
	private final StringProperty admin;
	
	public AmountDate(String eid, String ename, String dname, String pname, String usrname, String admin) {
		this.eid = new SimpleStringProperty(eid); 
		this.ename = new SimpleStringProperty(ename); 
		this.dname = new SimpleStringProperty(dname); 
		this.pname = new SimpleStringProperty(pname); 
		this.usrname = new SimpleStringProperty(usrname); 
		this.admin = new SimpleStringProperty(admin); 
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
	public String getUname() {
		return usrname.get();
	}
	public String getAdmin() {
		return admin.get();
	}
	
	
}
