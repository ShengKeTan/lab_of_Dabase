package application;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class YearsalaryController implements Initializable{
	
	@FXML
	private Button back;
	@FXML
	private TextField inputname;
	
	private ObservableList<String> pbox = FXCollections.observableArrayList();
	private ObservableList<String> dbox = FXCollections.observableArrayList();
	private ObservableList<String> ybox = FXCollections.observableArrayList();
	@FXML
	private ChoiceBox<String> pchoice, dchoice, ychoice;
	
	private ObservableList<Yeardate> yeardata = FXCollections.observableArrayList();
	@FXML
	private TableView<Yeardate> table_year;
	@FXML
	private TableColumn<Yeardate,String> ename, m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, year;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		init_choicebox();
		
		ename.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getName()));
		year.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getYear()));
		m1.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getMonth_1()));
		m2.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getMonth_2()));
		m3.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getMonth_3()));
		m4.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getMonth_4()));
		m5.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getMonth_5()));
		m6.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getMonth_6()));
		m7.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getMonth_7()));
		m8.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getMonth_8()));
		m9.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getMonth_9()));
		m10.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getMonth_10()));
		m11.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getMonth_11()));
		m12.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getMonth_12()));
		
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
			sql = "select distinct syear from salary";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			String po = null;
			while(rs.next()) {
				po = rs.getString("syear");
				ybox.add(po);
				System.out.println(po);
			}
			ybox.addAll("ALL");
			ychoice.setItems(ybox);
			ychoice.setValue("ALL");
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
	private void on_search_click() {
		yeardata.clear();
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		
		String inpname = " and post.pname=" + "'" + pchoice.getValue().toString().trim() + "'";
		String indname = " and department.dname=" + "'" + dchoice.getValue().toString().trim() + "'";
		if(pchoice.getValue().toString().trim().equals("ALL")) {
			inpname = " ";
		}
		if(dchoice.getValue().toString().trim().equals("ALL")) {
			indname = " ";
		}
		
		int inid = get_inputname_id();
		String getinid = " and employee.eid="+inid;
		if(inid==0) {
			getinid = " ";
		}
		
	
		try {
			sql = "select employee.ename, employee.eid"
			    + " from employee, post, department"
				+ " where employee.did=department.did"
				+ " and employee.pid=post.pid"
				+ inpname + indname + getinid;
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			String getename = null;
			BigDecimal total_tmp = new BigDecimal("0");
			int geteid = 0;
			while(rs.next()) {
				String temp[] = new String[12];
				BigDecimal total = new BigDecimal("0");
				getename = rs.getString("employee.ename");
				geteid = rs.getInt("employee.eid");
				for(int i = 1; i <= 12; i++) {
					total_tmp = getextra(geteid,i,"pay");
					total = total.add(total_tmp);
					temp[i-1] = total_tmp.toString();
				}
				yeardata.addAll(new Yeardate(getename,temp[0],temp[1],temp[2],temp[3],temp[4],temp[5],temp[6]
						,temp[7],temp[8],temp[9],temp[10],temp[11],total.toString()));
			}
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		table_year.setItems(yeardata);
	}
	private BigDecimal getextra(int eid, int month, String w) {
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		
		String inyear = " and syear=" + ychoice.getValue().toString().trim();
		
		if(ychoice.getValue().toString().trim().equals("ALL")) {
			inyear = " ";
		}
		
		BigDecimal temp = new BigDecimal("0");
		String tt = null;
		
		try {
			if(w.equals("extra")) {
			 tt = "select sum(extra) as money from salary" + " where salary.eid=%1$d and smonth=%2$d";
			}
			else tt = "select sum(pay) as money from salary" + " where salary.eid=%1$d and smonth=%2$d";
			
			tt = tt + inyear + " group by salary.eid, salary.syear";
			sql = String.format(tt, eid, month);
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			while(rs.next()) {
				temp = rs.getBigDecimal("money");
				System.out.println(temp);
			}
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		return temp;
	}
	
	@FXML
	private void on_back_click() {
		Stage stage = (Stage)back.getScene().getWindow();
	    stage.close();
	}

}
