package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class EmployeeController implements Initializable{
	
	static String old_eid = null;
	static String old_ename = null;
	static String old_dname = null;
	static String old_pname = null;
	static String old_age = null;
	static String old_sex = null;
	
	//舞台
	static Stage addemployee = null;
	static Stage changeemployee = null;
	//界面根结点
	private Parent add_root = null;
	private Parent change_root = null;
	//界面窗口
	private static Scene add_scene = null;
	private static Scene change_scene = null;
	
	
	private ObservableList<String> pbox = FXCollections.observableArrayList();
	private ObservableList<String> dbox = FXCollections.observableArrayList();
	@FXML
	private ChoiceBox<String> pchoice, dchoice;
	@FXML
	private TextField inputname;
	@FXML
	private Button exitButton;
	
	private ObservableList<EmployeeDate> employeedate = FXCollections.observableArrayList();
	@FXML
	private TableColumn<EmployeeDate,String> ename, department, post, eid, age, sex;
	@FXML
	private TableView<EmployeeDate> emptable;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		init_choicebox();
		
		ename.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getEname()));
		department.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getDepartment()));
		post.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getPost()));
		eid.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getEid()));
		age.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getAge()));
		sex.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getSex()));
		//监听列表选中事件
		emptable.getSelectionModel().selectedItemProperty().addListener(// 选中某一行 
				new ChangeListener<EmployeeDate>() { 
					@Override 
					public void changed( ObservableValue<? extends EmployeeDate> observableValue, 
							EmployeeDate oldItem, EmployeeDate newItem) { 
					if(newItem != null) {
						 old_eid = newItem.getEid();
						 old_ename = newItem.getEname();
						 old_dname = newItem.getDepartment();
						 old_pname = newItem.getPost();
						 old_age = newItem.getAge();
						 old_sex = newItem.getSex();
						System.out.println(old_eid + old_ename + old_dname + old_pname); 
						}
						}
				});
		
	}
	@FXML
	private void on_add_click() {
		try {
			//set root
			add_root = FXMLLoader.load(getClass().getClassLoader().getResource("addemployeeUI.fxml"));
			//set scene
			add_scene = new Scene(add_root);
			//set stage
			addemployee = new Stage();
			//change scene
			setAddUI();
			//show stage
			addemployee.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@FXML
	private void on_change_click() {
		if(old_eid==null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("请选中一个职员");
			alert.setContentText("您未选择职员");
			alert.showAndWait();
			return;
		}
		try {
			//set root
			change_root = FXMLLoader.load(getClass().getClassLoader().getResource("changeemployeeUI.fxml"));
			//set scene
			change_scene = new Scene(change_root);
			//set stage
			changeemployee = new Stage();
			//change scene
			setChangeUI();
			//show stage
			changeemployee.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void init_choicebox() {
		dbox.clear();
		pbox.clear();
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select department.dname from department";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			String dpart = null;
			while(rs.next()) {
				dpart = rs.getString("department.dname");
				dbox.add(dpart);
				System.out.println(dpart);
			}
			dbox.addAll("ALL");
			dchoice.setItems(dbox);
			dchoice.setValue("ALL");
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			sql = "select post.pname from post";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			String po = null;
			while(rs.next()) {
				po = rs.getString("post.pname");
				pbox.add(po);
				System.out.println(po);
			}
			pbox.addAll("ALL");
			pchoice.setItems(pbox);
			pchoice.setValue("ALL");
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	private int get_inputname_id() {
		int id = 0;
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select employee.eid from employee where ename=?";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setString(1,inputname.getText().toString().trim());
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			while(rs.next()) {
				id = rs.getInt("employee.eid");
			}	
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		return id;
	}
	@FXML
	private void on_delect_click() {
		if(old_eid==null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("请选中一个职员");
			alert.setContentText("您未选择职员");
			alert.showAndWait();
			return;
		}
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("信息删除");
		alert.setContentText("与该职员有关的所有信息都将被删除！");
		Optional<ButtonType>result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			System.out.println("ok");
			delect_date();
		}
		else {
			System.out.println("no");
		}
	}
	private void delect_date() {
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		String sql = null;
		try {
			sql = "delete from employee where employee.eid=?";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setInt(1, Integer.parseInt(old_eid));
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			int sure = pStatement.executeUpdate();
			if(sure > 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText("删除成功");
				alert.setContentText("您已完成删除");
				alert.showAndWait();
			}
		}catch(SQLException e1) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("删除失败");
			alert.setContentText("删除失败！");
			alert.showAndWait();
			e1.printStackTrace();
		}
	}
	
	@FXML
	private void show_empdate() {
		employeedate.clear();
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		try {
			String inpname = " and post.pname=" + "'" + pchoice.getValue().toString().trim() + "'";
			String indname = " and department.dname=" + "'" + dchoice.getValue().toString().trim() + "'";
			int inid = get_inputname_id();
			String getinid = " and employee.eid="+inid;
			if(pchoice.getValue().toString().trim().equals("ALL")) {
				inpname = " ";
			}
			if(dchoice.getValue().toString().trim().equals("ALL")) {
				indname = " ";
			}
			if(inid==0) {
				getinid = " ";
			}
			sql = "select post.pname, department.dname, employee.eid, employee.ename, age, sex from"
			   + " employee, department, post"
			   + " where employee.did=department.did"
			   + " and post.pid=employee.pid"
			   + inpname + indname + getinid;
			System.out.println(sql);
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			String geteid = null;
			String getename = null;
			String getdname = null;
			String getpname = null;
			String getage = null;
			String getsex = null;
			while(rs.next()) {
				geteid = rs.getString("employee.eid");
				getename = rs.getString("employee.ename");
				getdname = rs.getString("department.dname");
				getpname = rs.getString("post.pname");
				getage = rs.getString("age");
				getsex = rs.getString("sex");
				employeedate.add(new EmployeeDate(geteid,getename,getdname,getpname,getage,getsex));
			}
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		emptable.setItems(employeedate);
	}
	
	@FXML
	private void on_quit_click() {
		Stage stage = (Stage)exitButton.getScene().getWindow();
	    stage.close();
	}
	
	public static void  setAddUI() {
		addemployee.setTitle("add info");
		addemployee.setScene(add_scene);
	}
	public static void  setChangeUI() {
		changeemployee.setTitle("change info");
		changeemployee.setScene(change_scene);
	}

}
