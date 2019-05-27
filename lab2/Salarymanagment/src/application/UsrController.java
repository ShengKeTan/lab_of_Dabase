package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class UsrController implements Initializable{
	
	String geteid = null;
	String gettime = null;
	String pname = null;
	
	private ObservableList<Usrdata> usrdata = FXCollections.observableArrayList();
	@FXML
	private TableView<Usrdata> data;
	@FXML
	private TableColumn<Usrdata,String> time, name, department, post, noduty, allowance, total;
	
	@FXML
	private Button seek, quit;
	
	private ObservableList<String> pbox = FXCollections.observableArrayList();
	@FXML
	private ChoiceBox<String> pchoice, dchoice;
	@FXML
	private TextField inputname;
	
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		pchoice.setValue("nihao");
		
		time.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getTime()));
		name.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getName()));
		department.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getDepartment()));
		post.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getPost()));
		noduty.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getNoduty()));
		allowance.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getAllowance()));
		total.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getTotal()));
		//监听列表选中事件
		data.getSelectionModel().selectedItemProperty().addListener(// 选中某一行 
				new ChangeListener<Usrdata>() { 
					@Override 
					public void changed( ObservableValue<? extends Usrdata> observableValue, 
							Usrdata oldItem, Usrdata newItem) { 
						if(newItem != null) {
						geteid = newItem.getName();
						gettime = newItem.getTime();
						System.out.println(geteid + gettime); 
						}
						}
				});
		
		if(pchoice == null) {
			System.out.println("is null");
			pchoice.setValue("name");
		}
		if(pchoice.getValue()==null) {
			System.out.println("is null");
			pchoice.setValue("name");
		}
		System.out.println(pchoice.getValue());
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
			sql = "select post.pname from post, employee"
				+ " where employee.pid=post.pid"
				+ " and employee.eid=?";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setInt(1, LogonController.getusrid);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			while(rs.next()) {
				pname = rs.getString("post.pname");
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
	private String sql_make(String pname) {
		String sql = null;
		if(pname.equals("职员")) {
			String temp = null;
			temp = "select salary.syear, salary.smonth, salary.eid, department.dname, post.pname,"
				+ " salary.noduty, salary.extra, salary.pay"
				+ " from salary, department, post, employee"
				+ " where salary.eid=employee.eid"
				+ " and empoyee.pid=post.pid"
				+ " and employee.did=department.did"
				+ " and salary.eid=%2$d";
			sql = String.format(temp, LogonController.getusrid);
		}
		else if(pname.equals("部门经理")) {
			
		}
		else {
			
		}
		return sql;
	}
	
	@FXML
	private void show_data() {
		usrdata.clear();
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		try {
			
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setInt(1, LogonController.getusrid);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		
		
		//close connet
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		
	}
	
	@FXML
	private void on_seek_click() {
		
	}
	
	@FXML
	private void on_quit_click() {
		usrdata.clear();
		Main.setLoginUI();
	}
	

}
