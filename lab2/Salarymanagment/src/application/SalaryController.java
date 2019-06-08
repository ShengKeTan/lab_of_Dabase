package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SalaryController implements Initializable {
	
	@FXML
	private Button back;
	@FXML
	private TextField inputname;
	
	@FXML
	private DatePicker date_end,date_begin;
	
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
		date_end.setValue(LocalDate.now());
		date_begin.setValue(LocalDate.now());
		//initbox
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
	
	@FXML
	private void on_serach_click() {
		monthdata.clear();
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		
		int bdatayear, edatayear;
		int bdatamonth, edatamonth;
		
		bdatayear = date_begin.getValue().getYear();
		bdatamonth = date_begin.getValue().getMonthValue();
		edatayear = date_end.getValue().getYear();
		edatamonth = date_end.getValue().getMonthValue();
		
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
			String temp = null;
			temp = "select salary.eid, employee.ename, department.dname, post.pname,"
			   + " salary.latetimes, salary.ctimes, salary.mtimes, salary.bpay,"
			   + " salary.extra, salary.pay, salary.syear, salary.smonth"
			   + " from salary, employee, department, post"
			   + " where salary.eid=employee.eid"
			   + " and employee.did=department.did"
			   + " and employee.pid=post.pid"
			   + " and salary.syear between %1$d and %2$d"
			   + " and salary.smonth between %3$d and %4$d"
			   + inpname + indname + getinid;
			sql = String.format(temp, bdatayear, edatayear, bdatamonth, edatamonth);
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			int eid_tmp = 0;
			String ename_tmp = null;
			String dname_tmp = null;
			String pname_tmp = null;
			//int rest_tmp = 0;
			int late_tmp = 0;
			int duty_tmp = 0;
			int noduty_tmp = 0;
			String extra_tmp = null;
			String bpay_sum = null;
			String total_tmp = null;
			String time_tmp;
			while(rs.next()) {
				eid_tmp = rs.getInt("salary.eid");
				ename_tmp = rs.getString("employee.ename");
				dname_tmp = rs.getString("department.dname");
				pname_tmp = rs.getString("post.pname");
				late_tmp = rs.getInt("salary.latetimes");
				duty_tmp = rs.getInt("salary.ctimes");
				noduty_tmp = rs.getInt("salary.mtimes");
				//rest_tmp = date_of_month() - late_tmp - duty_tmp - noduty_tmp;
				extra_tmp = rs.getBigDecimal("salary.extra").toString();
				bpay_sum = rs.getBigDecimal("salary.bpay").toString();
				total_tmp = rs.getBigDecimal("salary.pay").toString();
				time_tmp = rs.getString("salary.syear") + "-" + rs.getString("salary.smonth");
				
				monthdata.addAll(new Monthdate(eid_tmp + "",ename_tmp,dname_tmp,pname_tmp,
						late_tmp+"",duty_tmp+"/"+noduty_tmp,extra_tmp,bpay_sum,total_tmp,time_tmp));
			}
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		monthtable.setItems(monthdata);
	}
	
	@FXML
	private void on_printput_click() {
		if(monthdata.isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("生成失败");
			alert.setContentText("生成失败！");
			alert.showAndWait();
			return;
		}
		String[] headers = {"职工编号","姓名","部门","职务","迟到次数","考勤/缺勤","津贴","基本工资","总工资","时间"};
		Out2Excel.exportExcel("test", monthdata, headers, "table");
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText("打印成功");
		alert.setContentText("打印成功");
		alert.showAndWait();
	}
	
	/*private int date_of_month() {
		int days = 0;
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.DATE, 1);
		cal.roll(Calendar.DATE, -1);
		days = cal.get(Calendar.DATE);
		return days;
	}*/

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
	private void on_back_click() {
		Stage stage = (Stage)back.getScene().getWindow();
	    stage.close();
	}

}
