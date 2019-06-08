package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DepartmentDate {
	
	private final StringProperty did;
	private final StringProperty dname;
	
	public DepartmentDate(String did, String dname) {
		
		this.did = new SimpleStringProperty(did);
		this.dname = new SimpleStringProperty(dname); 
	}
	
	public String getDname() {
		return dname.get();
	}
	public String getDid() {
		return did.get();
	}

}
