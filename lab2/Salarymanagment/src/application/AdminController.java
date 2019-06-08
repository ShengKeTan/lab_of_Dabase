package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
	static Stage salarystage = null;
	static Stage awardsstage = null;
	static Stage yearstage = null;
	static Stage amountstage = null;
	static Stage departstage = null;
	//界面根结点
	private Parent employee_root = null;
	private Parent setsalary_root = null;
	private Parent setmonth_root = null;
	private Parent extra_root = null;
	private Parent check_root = null;
	private Parent salary_root = null;
	private Parent awards_root = null;
	private Parent year_root = null;
	private Parent amount_root = null;
	private Parent depart_root = null;
	//界面窗口
	private static Scene employee_scene = null;
	private static Scene setsalary_scene = null;
	private static Scene setmonth_scene = null;
	private static Scene extra_scene = null;
	private static Scene check_scene = null;
	private static Scene salary_scene = null;
	private static Scene awards_scene = null;
	private static Scene year_scene = null;
	private static Scene amount_scene = null;
	private static Scene depart_scene = null;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private void on_depart_click() {
		try {
			//set root
			depart_root = FXMLLoader.load(getClass().getClassLoader().getResource("departmentUI.fxml"));
			//set scene
			depart_scene = new Scene(depart_root);
			//set stage
			departstage = new Stage();
			//change scene
			setDepartUI();
			//show stage
			departstage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void on_amount_click() {
		try {
			//set root
			amount_root = FXMLLoader.load(getClass().getClassLoader().getResource("amountUI.fxml"));
			//set scene
			amount_scene = new Scene(amount_root);
			//set stage
			amountstage = new Stage();
			//change scene
			setAmountUI();
			//show stage
			amountstage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void on_year_cilck() {
		try {
			//set root
			year_root = FXMLLoader.load(getClass().getClassLoader().getResource("yearsalaryUI.fxml"));
			//set scene
			year_scene = new Scene(year_root);
			//set stage
			yearstage = new Stage();
			//change scene
			setYearUI();
			//show stage
			yearstage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void on_awards_click() {
		try {
			//set root
			awards_root = FXMLLoader.load(getClass().getClassLoader().getResource("awardsUI.fxml"));
			//set scene
			awards_scene = new Scene(awards_root);
			//set stage
			awardsstage = new Stage();
			//change scene
			setAwardsUI();
			//show stage
			awardsstage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void on_salary_click() {
		try {
			//set root
			salary_root = FXMLLoader.load(getClass().getClassLoader().getResource("SalaryUI.fxml"));
			//set scene
			salary_scene = new Scene(salary_root);
			//set stage
			salarystage = new Stage();
			//change scene
			setNewsalaryUI();
			//show stage
			salarystage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
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
	
	@FXML
	private void on_about_click() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText("作者信息");
		alert.setContentText("CS1602谭胜克");
		alert.showAndWait();
	}
	
	@FXML
	private void setDepartUI() {
		departstage.setTitle("department info");
		departstage.setScene(depart_scene);
	}
	
	@FXML
	private void setAmountUI() {
		amountstage.setTitle("amount");
		amountstage.setScene(amount_scene);
	}
	
	public static void setYearUI() {
		yearstage.setTitle("year salary");
		yearstage.setScene(year_scene);
	}
	
	public static void setAwardsUI() {
		awardsstage.setTitle("awards");
		awardsstage.setScene(awards_scene);
	}
	
	public static void setSalaryUI() {
		setsalarystage.setTitle("set salary");
		setsalarystage.setScene(setsalary_scene);
	}
	public static void setNewsalaryUI() {
		salarystage.setTitle("set salary");
		salarystage.setScene(salary_scene);
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
