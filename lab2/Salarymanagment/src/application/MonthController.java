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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MonthController implements Initializable{
	
	@FXML
	private Button back;
	@FXML
	private Label note,set;
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
		
		note.setText("当月天数: " + date_of_month() + " 天");
		set.setText("注:" + "迟到:-30/次，缺勤:-60/次");
		
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
		show_date_table();
	}
	
	private int sum_check(int eid, String state) {
		int sum = 0;
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
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		return sum;
	}
	
	private BigDecimal getextra(int eid) {
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
		
		BigDecimal temp = null;
		
		try {
			sql = "select sum(extra) as money from allowance"
			   + " where eid=?"
			   + " and amonth=?"
			   + " and ayear=?"
			   + " and state=?"
			   + " group by eid, ayear, amonth";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setInt(1, eid);
			pStatement.setInt(3, year);
			pStatement.setInt(2, month);
			pStatement.setInt(4, 1);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			while(rs.next()) {
				temp = rs.getBigDecimal("money");
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
	
	private int date_of_month() {
		int days = 0;
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.DATE, 1);
		cal.roll(Calendar.DATE, -1);
		days = cal.get(Calendar.DATE);
		return days;
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
	
	
	private void show_date_table() {
		
		monthdata.clear();
		//init time
		Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        
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
		
		//connet to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select employee.eid, employee.ename, department.dname,"
			   + " post.pname, post.bpay"
			   + " from employee, post, department"
			   + " where employee.pid=post.pid"
			   + " and employee.did=department.did"
			   + inpname + indname + getinid;
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			String eid_tmp = null;
			String ename_tmp = null;
			String dname_tmp = null;
			String pname_tmp = null;
			int rest_tmp = 0;
			int late_tmp = 0;
			int duty_tmp = 0;
			int noduty_tmp = 0;
			BigDecimal extra_tmp = null;
			BigDecimal bpay_tmp = null;
			BigDecimal bpay_sum = null;
			BigDecimal total_tmp = null;
			String time_tmp;
			
			while(rs.next()) {
				
				eid_tmp = rs.getString("employee.eid");
				ename_tmp = rs.getString("employee.ename");
				dname_tmp = rs.getString("department.dname");
				pname_tmp = rs.getString("post.pname");
				rest_tmp = sum_check(Integer.parseInt(eid_tmp), "rest");
				duty_tmp = sum_check(Integer.parseInt(eid_tmp), "duty");
				late_tmp = sum_check(Integer.parseInt(eid_tmp), "late");
				noduty_tmp = date_of_month() - rest_tmp - duty_tmp - late_tmp;
				extra_tmp = getextra(Integer.parseInt(eid_tmp));
				if(getextra(Integer.parseInt(eid_tmp))==null)
					extra_tmp = new BigDecimal(0);
				
				
				bpay_tmp =  rs.getBigDecimal("post.bpay");
				BigDecimal temp = new BigDecimal(late_tmp*30 + noduty_tmp*60);
				bpay_sum = bpay_tmp.subtract(temp);
				total_tmp = bpay_sum.add(extra_tmp);
				time_tmp = year + "-" + month;
				
				monthdata.addAll(new Monthdate(eid_tmp,ename_tmp,dname_tmp,pname_tmp,
						late_tmp+"",duty_tmp+"/"+noduty_tmp,extra_tmp.toString(),bpay_sum.toString(),
						total_tmp.toString(),time_tmp));
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
	
	private void insert_salary(int eid, int did, int pid, int miss, int check,
			int late, BigDecimal bpay, BigDecimal extra,
			BigDecimal total, int year, int month) {
		//connet to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		String sql = null;
		try {
			sql = "insert into salary values(?,?,?,?,?,?,?,?,?,?,?)";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setInt(1, eid);
			pStatement.setInt(2, did);
			pStatement.setInt(3, pid);
			pStatement.setInt(4, miss);
			pStatement.setInt(5, check);
			pStatement.setInt(6, late);
			pStatement.setBigDecimal(7, bpay);
			pStatement.setBigDecimal(8, extra);
			pStatement.setBigDecimal(9, total);
			pStatement.setInt(10, year);
			pStatement.setInt(11, month);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			int sure = pStatement.executeUpdate();
			if(sure > 0) {
				System.out.println("insert success");
			}
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	private void redy_for_insert(String choice) {
		//init time
		Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
		
		//connet to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select employee.eid, employee.did, employee.pid,"
			   + " post.bpay"
			   + " from employee, post"
			   + " where employee.pid=post.pid";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			int eid_tmp = 0;
			int did_tmp = 0;
			int pid_tmp = 0;
			int rest_tmp = 0;
			int late_tmp = 0;
			int duty_tmp = 0;
			int noduty_tmp = 0;
			BigDecimal extra_tmp = null;
			BigDecimal bpay_tmp = null;
			BigDecimal bpay_sum = null;
			BigDecimal total_tmp = null;
			
			while(rs.next()) {
				
				eid_tmp = rs.getInt("employee.eid");
				did_tmp = rs.getInt("employee.did");
				pid_tmp = rs.getInt("employee.pid");
				rest_tmp = sum_check(eid_tmp, "rest");
				duty_tmp = sum_check(eid_tmp, "duty");
				late_tmp = sum_check(eid_tmp, "late");
				noduty_tmp = date_of_month() - rest_tmp - duty_tmp - late_tmp;
				extra_tmp = getextra(eid_tmp);
				if(getextra(eid_tmp) == null)
					extra_tmp = new BigDecimal(0);
				
				
				bpay_tmp =  rs.getBigDecimal("post.bpay");
				BigDecimal temp = new BigDecimal(late_tmp*30 + noduty_tmp*60);
				bpay_sum = bpay_tmp.subtract(temp);
				total_tmp = bpay_sum.add(extra_tmp);
				
				if(choice.equals("insert")) {
					insert_salary(eid_tmp,did_tmp,pid_tmp,noduty_tmp,duty_tmp,late_tmp,bpay_sum,extra_tmp,total_tmp,year,month);
				}
				else if(choice.equals("update")) {
					update_salary(eid_tmp,noduty_tmp,duty_tmp,late_tmp,bpay_sum,extra_tmp,total_tmp,year,month);
				}
			}
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	private void update_salary(int eid, int miss, int check,
			int late, BigDecimal bpay, BigDecimal extra,
			BigDecimal total, int year, int month) {
		//connet to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		String sql = null;
		try {
			sql = "update salary set"
			   + " mtimes=?,"
			   + " ctimes=?,"
			   + " latetimes=?,"
			   + " bpay=?,"
			   + " extra=?,"
			   + " pay=?"
			   + " where eid=?"
			   + " and syear=?"
			   + " and smonth=?";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setInt(7, eid);
			pStatement.setInt(1, miss);
			pStatement.setInt(2, check);
			pStatement.setInt(3, late);
			pStatement.setBigDecimal(4, bpay);
			pStatement.setBigDecimal(5, extra);
			pStatement.setBigDecimal(6, total);
			pStatement.setInt(8, year);
			pStatement.setInt(9, month);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			int sure = pStatement.executeUpdate();
			if(sure > 0) {
				System.out.println("update success");
			}
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	@FXML
	private void on_update_click() {
		redy_for_insert("update");
	}
	
	@FXML
	private void on_ok_click() {
		redy_for_insert("insert");
		//if(redy_for_insert() == 0) {
			/*Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("生成失败");
			alert.setContentText("生成失败！");
			alert.showAndWait();
		}
		else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("生成成功");
			alert.setContentText("生成成功！");
			alert.showAndWait();
		}*/
	}
	
	@FXML
	private void on_back_click() {
		Stage stage = (Stage)back.getScene().getWindow();
	    stage.close();
	}

}
