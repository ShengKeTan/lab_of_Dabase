package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class AdminController implements Initializable{

	@FXML
	private MenuItem empinfo,postinfo;
	
	//舞台
	static Stage employeestage = null;
	static Stage setsalarystage = null;
	static Stage setmonthstage = null;
	static Stage extrastage = null;
	static Stage checkstage = null;
	//界面根结点
	private Parent employee_root = null;
	private Parent setsalary_root = null;
	private Parent setmonth_root = null;
	private Parent extra_root = null;
	private Parent check_root = null;
	//界面窗口
	private static Scene employee_scene = null;
	private static Scene setsalary_scene = null;
	private static Scene setmonth_scene = null;
	private static Scene extra_scene = null;
	private static Scene check_scene = null;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private void on_check_click() {
		try {
			//set root
			check_root = FXMLLoader.load(getClass().getClassLoader().getResource("checkUI.fxml"));
			//set scene
			check_scene = new Scene(check_root);
			//set stage
			checkstage = new Stage();
			//change scene
			setCheckUI();
			//show stage
			checkstage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void on_extra_click() {
		try {
			//set root
			extra_root = FXMLLoader.load(getClass().getClassLoader().getResource("extraUI.fxml"));
			//set scene
			extra_scene = new Scene(extra_root);
			//set stage
			extrastage = new Stage();
			//change scene
			setExtraUI();
			//show stage
			extrastage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void on_setmonth_click() {
		try {
			//set root
			setmonth_root = FXMLLoader.load(getClass().getClassLoader().getResource("monthUI.fxml"));
			//set scene
			setmonth_scene = new Scene(setmonth_root);
			//set stage
			setmonthstage = new Stage();
			//change scene
			setMonthUI();
			//show stage
			setmonthstage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void on_setsalary_click() {
		try {
			//set root
			setsalary_root = FXMLLoader.load(getClass().getClassLoader().getResource("setsalaryUI.fxml"));
			//set scene
			setsalary_scene = new Scene(setsalary_root);
			//set stage
			setsalarystage = new Stage();
			//change scene
			setSalaryUI();
			//show stage
			setsalarystage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void on_empinfo_click() {
		try {
			//set root
			employee_root = FXMLLoader.load(getClass().getClassLoader().getResource("EmployeeUI.fxml"));
			//set scene
			employee_scene = new Scene(employee_root);
			//set stage
			employeestage = new Stage();
			//change scene
			setEmploinfoUI();
			//show stage
			employeestage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void setSalaryUI() {
		setsalarystage.setTitle("set salary");
		setsalarystage.setScene(setsalary_scene);
	}
	
	public static void  setEmploinfoUI() {
		employeestage.setTitle("employee scene");
		employeestage.setScene(employee_scene);
	}
	public static void setMonthUI() {
		setmonthstage.setTitle("salary");
		setmonthstage.setScene(setmonth_scene);
	}
	public static void setExtraUI() {
		extrastage.setTitle("extra");
		extrastage.setScene(extra_scene);
	}
	public static void setCheckUI() {
		checkstage.setTitle("check");
		checkstage.setScene(check_scene);
	}

}
