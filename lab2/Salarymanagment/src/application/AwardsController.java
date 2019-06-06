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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AwardsController implements Initializable{
	
	@FXML
	private Button back;
	@FXML
	private TextField inputname;
	@FXML
	private DatePicker date_end,date_begin;
	
	private ObservableList<String> pbox = FXCollections.observableArrayList();
	private ObservableList<String> dbox = FXCollections.observableArrayList();
	private ObservableList<String> ybox = FXCollections.observableArrayList();
	@FXML
	private ChoiceBox<String> pchoice, dchoice, ychoice;
	
	private ObservableList<AwardsDate> awardsdata = FXCollections.observableArrayList();
	@FXML
	private TableView<AwardsDate> table_award;
	@FXML
	private TableColumn<AwardsDate,String> eid, ename, dname, pname, bpay, extra, awards;

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
		bpay.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getBpay()));
		extra.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getExtra()));
		awards.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getAward()));
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
	private void on_search_click() {
		awardsdata.clear();
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		
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
		try {
			sql = "select employee.eid, employee.ename, department.dname, post.pname"
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
			int geteid = 0;
			String getename = null;
			String getdname = null;
			String getpname = null;
			BigDecimal extra = null;
			BigDecimal pay = null;
			BigDecimal aw = null;
			while(rs.next()) {
				geteid = rs.getInt("employee.eid");
				getename = rs.getString("employee.ename");
				getdname = rs.getString("department.dname");
				getpname = rs.getString("post.pname");
				if((extra = getextra(geteid,"extra"))==null) {
					extra = new BigDecimal(0);
				}
				if((pay = getextra(geteid,"pay"))==null) {
					pay = new BigDecimal(0);
				}
				BigDecimal temp = pay.add(extra);
				BigDecimal base = new BigDecimal(12);
				aw = temp.divide(base,0,BigDecimal.ROUND_CEILING);
				awardsdata.addAll(new AwardsDate(geteid+"",getename,getdname,getpname,pay.toString(),extra.toString(),aw.toString()));
			}
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		
		table_award.setItems(awardsdata);
	}
	
	private BigDecimal getextra(int eid, String w) {
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
		
		BigDecimal temp = null;
		String tt = null;
		
		try {
			if(w.equals("extra")) {
			 tt = "select sum(extra) as money from salary" + " where salary.eid=%1$d";
			}
			else tt = "select sum(pay) as money from salary" + " where salary.eid=%1$d";
			
			tt = tt + inyear + " group by salary.eid, salary.syear";
			sql = String.format(tt, eid);
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
	private void on_back_click() {
		Stage stage = (Stage)back.getScene().getWindow();
	    stage.close();
	}
	

}
