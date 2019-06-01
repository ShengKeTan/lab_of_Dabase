package application;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MonthController implements Initializable{
	
	@FXML
	private Button back;
	@FXML
	private TextField inputname;
	
	private ObservableList<String> pbox = FXCollections.observableArrayList();
	private ObservableList<String> dbox = FXCollections.observableArrayList();
	@FXML
	private ChoiceBox<String> dchoice, pchoice;
	
	private ObservableList<Monthdate> monthdata = FXCollections.observableArrayList();
	@FXML
	private TableView<Monthdate> monthtable;
	@FXML
	private TableColumn<Monthdate,String> eid, ename, dname, pname, rest, duty, allowance, bpay, salary, time;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		init_choicebox();
		eid.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getEid()));
		ename.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getEname()));
		dname.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getDname()));
		pname.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getPname()));
		rest.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getRest()));
		duty.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getDuty()));
		allowance.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getExtra()));
		bpay.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getBpay()));
		salary.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getSalary()));
		time.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getTime()));
	}
	
	@FXML
	private void test() {
		int id = get_inputname_id();
		System.out.println(id);
		int sum = sum_check(id,"duty");
		System.out.println(sum);
		sum = sum_check(id,"rest");
		System.out.println(sum);
		sum = sum_check(id,"late");
		System.out.println(sum);
	}
	
	private int sum_check(int eid, String state) {
		int sum = -1;
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		
		Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        
        String temp = " ";
        
        if(state.equals("rest")) {
        	temp = " and ch_eck.rest=true";
        }
        else if(state.equals("duty")) {
        	temp = " and ch_eck.duty=true";
        }
        else {
        	temp = " and ch_eck.late=true";
        }
        
		try {
			sql = "select eid, count(*) as times from ch_eck"
			   + " where eid=?"
			   + " and cyear=?"
			   + " and cmonth=?"
			   + temp
			   + " group by eid, cyear, cmonth";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setInt(1, eid);
			pStatement.setInt(2, year);
			pStatement.setInt(3, month);
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			while(rs.next()) {
				sum = rs.getInt("times");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return sum;
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
	
	private int get_postid() {
		int id = 0;
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select post.pid from post where post.pname=?";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setString(1,pchoice.getValue().toString().trim());
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			while(rs.next()) {
				id = rs.getInt("post.pid");
			}	
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		return id;
	}
	private int get_departid() {
		int id = 0;
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select department.did from department where department.dname=?";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setString(1,dchoice.getValue().toString().trim());
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			while(rs.next()) {
				id = rs.getInt("department.did");
			}	
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		return id;
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
	private void on_ok_click() {
		
	}
	
	@FXML
	private void on_back_click() {
		Stage stage = (Stage)back.getScene().getWindow();
	    stage.close();
	}

}
