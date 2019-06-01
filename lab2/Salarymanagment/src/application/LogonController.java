package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LogonController implements Initializable{
	
	static int getusrid;
	static String getpname;
	static String getename;
	static String getdname;
	
	//舞台
	static Stage adminstage = null;
	static Stage usrstage = null;
	//界面根结点
	private Parent admin_root = null;
	private Parent usr_root = null;
	//界面窗口
	private static Scene admin_scene = null;
	private static Scene usr_scene = null;
	
	@FXML
	private TextField usrname, password;
	@FXML
	private Label note;
	@FXML
	private Button logon;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		note.setTextFill(Color.web("#00763a"));
	}
	
	private void usr_logon() {
		Stage stage = (Stage)logon.getScene().getWindow();
	    stage.close();
		try {
			//set root
			usr_root = FXMLLoader.load(getClass().getClassLoader().getResource("usrUI.fxml"));
			//set scene
			usr_scene = new Scene(usr_root);
			//set stage
			usrstage = new Stage();
			//change scene
			setUsrUI();
			//show stage
			usrstage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void admin_logon() {
		Stage stage = (Stage)logon.getScene().getWindow();
	    stage.close();
		try {
			//set root
			admin_root = FXMLLoader.load(getClass().getClassLoader().getResource("adminUI.fxml"));
			//set scene
			admin_scene = new Scene(admin_root);
			//set stage
			adminstage = new Stage();
			//change scene
			setAdminUI();
			//show stage
			adminstage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@FXML
	private void logon() {
		//null usrname flags
		int flag = 0;
		//authority
		boolean auth = false;
		//prepare to getdata form database
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String getpassword = "";
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get password from databeas
		try {
			String sql = "select pass, authority, eid from _usr where usr=?";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setString(1, usrname.getText().trim());
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			while(rs.next()) {
				getpassword = rs.getString("pass").trim();
				auth = rs.getBoolean("authority");
				getusrid = rs.getInt("eid");
				flag = 1;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		if(password.getText().trim().equals(getpassword)) {
			note.setText("");
			usrname.clear();
			password.clear();
			get_auth();
			if(auth) admin_logon();
			else usr_logon();
		}
		else {
			if(flag == 0) note.setText("用户不存在！");
			note.setText("密码错误！");
		}
		//close connect
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	private void get_auth() {
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select post.pname, employee.ename, department.dname from post, employee, department"
				+ " where employee.pid=post.pid"
				+ " and department.did=employee.did"
				+ " and employee.eid=?";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setInt(1, LogonController.getusrid);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			while(rs.next()) {
				getpname = rs.getString("post.pname");
				getename = rs.getString("employee.ename");
				getdname = rs.getString("department.dname");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	@FXML
	private void on_logon_click() {
		if(usrname.getText().equals("")) {
			note.setText("用户名不能为空，请输入用户名");
			System.out.println("用户名不能为空，请输入用户名");
			return;
		}
		if(password.getText().equals("")) {
			note.setText("密码不能为空，请输入密码");
			System.out.println("密码不能为空，请输入密码");
			return;
		}
		//check usr name and password
		logon();
	}
	
	public static void  setUsrUI() {
		usrstage.setTitle("普通用户");
		usrstage.setScene(usr_scene);
	}
	public static void setAdminUI() {
		adminstage.setTitle("管理员用户");
		adminstage.setScene(admin_scene);
	}
}
