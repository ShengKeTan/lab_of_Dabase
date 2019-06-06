package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Yeardate {
	private final StringProperty name;
	private final StringProperty month_1;
	private final StringProperty month_2;
	private final StringProperty month_3;
	private final StringProperty month_4;
	private final StringProperty month_5;
	private final StringProperty month_6;
	private final StringProperty month_7;
	private final StringProperty month_8;
	private final StringProperty month_9;
	private final StringProperty month_10;
	private final StringProperty month_11;
	private final StringProperty month_12;
	private final StringProperty year;
	
	
	public Yeardate(String name, String month_1, String month_2, String month_3, String month_4, String month_5, 
			String month_6, String month_7, String month_8, String month_9, String month_10, String month_11, String month_12, 
			String year) {
		this.name = new SimpleStringProperty(name);
		this.month_1 = new SimpleStringProperty(month_1);
		this.month_2 = new SimpleStringProperty(month_2);
		this.month_3 = new SimpleStringProperty(month_3);
		this.month_4 = new SimpleStringProperty(month_4);
		this.month_5 = new SimpleStringProperty(month_5);
		this.month_6 = new SimpleStringProperty(month_6);
		this.month_7 = new SimpleStringProperty(month_7);
		this.month_8 = new SimpleStringProperty(month_8);
		this.month_9 = new SimpleStringProperty(month_9);
		this.month_10 = new SimpleStringProperty(month_10);
		this.month_11 = new SimpleStringProperty(month_11);
		this.month_12 = new SimpleStringProperty(month_12);
		this.year = new SimpleStringProperty(year);
	}
	public String getName() {
		return name.get();
	}
	public String getMonth_1() {
		return month_1.get();
	}
	public String getMonth_2() {
		return month_2.get();
	}
	public String getMonth_3() {
		return month_3.get();
	}
	public String getMonth_4() {
		return month_4.get();
	}
	public String getMonth_5() {
		return month_5.get();
	}
	public String getMonth_6() {
		return month_6.get();
	}
	public String getMonth_7() {
		return month_7.get();
	}
	public String getMonth_8() {
		return month_8.get();
	}
	public String getMonth_9() {
		return month_9.get();
	}
	public String getMonth_10() {
		return month_10.get();
	}
	public String getMonth_11() {
		return month_11.get();
	}
	public String getMonth_12() {
		return month_12.get();
	}
	public String getYear() {
		return year.get();
	}
}
