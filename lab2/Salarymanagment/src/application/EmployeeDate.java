package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmployeeDate {
	
	private final StringProperty eid;
	private final StringProperty ename;
	private final StringProperty department;
	private final StringProperty post;
	private final StringProperty age;
	private final StringProperty sex;
	
	public EmployeeDate(String eid, String ename, String department, String post, String age, String sex) {
		this.eid = new SimpleStringProperty(eid);
		this.ename = new SimpleStringProperty(ename);
		this.department = new SimpleStringProperty(department);
		this.post = new SimpleStringProperty(post);
		this.age = new SimpleStringProperty(age);
		this.sex = new SimpleStringProperty(sex);
	}
	
	public String getEname() {
		return ename.get();
	}
	public String getEid() {
		return eid.get();
	}
	public String getDepartment() {
		return department.get();
	}
	public String getPost() {
		return post.get();
	}
	public String getAge() {
		return age.get();
	}
	public String getSex() {
		return sex.get();
	}
}
