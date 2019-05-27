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
	//界面根结点
	private Parent employee_root = null;
	//界面窗口
	private static Scene employee_scene = null;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
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
			setSalaryUI();
			//show stage
			employeestage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void  setSalaryUI() {
		employeestage.setTitle("employee scene");
		employeestage.setScene(employee_scene);
	}

}
