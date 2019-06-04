package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AwardsDate {
	private final StringProperty eid;
	private final StringProperty ename;
	private final StringProperty dname;
	private final StringProperty pname;
	private final StringProperty bpay;
	private final StringProperty extra;
	private final StringProperty award;
	//private final StringProperty year;
	
	public AwardsDate(String eid, String ename, String dname, String pname, String bpay, String extra, String award) {
		this.eid = new SimpleStringProperty(eid);
		this.ename = new SimpleStringProperty(ename);
		this.dname = new SimpleStringProperty(dname);
		this.pname = new SimpleStringProperty(pname);
		this.bpay = new SimpleStringProperty(bpay);
		this.extra = new SimpleStringProperty(extra);
		this.award = new SimpleStringProperty(award);
		//this.year = new SimpleStringProperty(year);
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
	public String getBpay() {
		return bpay.get();
	}
	public String getExtra() {
		return extra.get();
	}
	public String getAward() {
		return award.get();
	}
	/*public String getYear() {
		return year.get();
	}*/
}
