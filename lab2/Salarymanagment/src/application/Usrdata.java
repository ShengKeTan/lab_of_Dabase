package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Usrdata {
	
	private final StringProperty time;
	private final StringProperty name;
	private final StringProperty department;
	private final StringProperty post;
	private final StringProperty noduty;
	private final StringProperty allowance;
	private final StringProperty total;
	
	public Usrdata(String time, String name, String department, String post, String noduty, String allowance, String total) {
		this.time = new SimpleStringProperty(time);
		this.name = new SimpleStringProperty(name);
		this.department = new SimpleStringProperty(department);
		this.post = new SimpleStringProperty(post);
		this.noduty = new SimpleStringProperty(noduty);
		this.allowance = new SimpleStringProperty(allowance);
		this.total = new SimpleStringProperty(total);
	}
	
	public String getTime() {
		return time.get();
	}
	public String getName() {
		return name.get();
	}
	public String getDepartment() {
		return department.get();
	}
	public String getPost() {
		return post.get();
	}
	public String getNoduty() {
		return noduty.get();
	}
	public String getAllowance() {
		return allowance.get();
	}
	public String getTotal() {
		return total.get();
	}
	
}
