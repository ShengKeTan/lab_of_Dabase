package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ExtraController implements Initializable{
	
	static String getworkname;
	static String getwyear;
	static String getwmonth;
	static String getwday;
	static String getwbtime;
	static String getwetime;
	
	@FXML
	private Button back;
	@FXML
	private TextField inputname;
	@FXML
	private DatePicker date_end,date_begin;
	
	private ObservableList<String> pbox = FXCollections.observableArrayList();
	private ObservableList<String> dbox = FXCollections.observableArrayList();
	private ObservableList<String> sbox = FXCollections.observableArrayList();
	@FXML
	private ChoiceBox<String> dchoice, pchoice, schoice;
	
	private ObservableList<Workdate> workdate = FXCollections.observableArrayList();
	@FXML
	private TableView<Workdate> worktable;
	@FXML
	private TableColumn<Workdate, String> w1, w2, w3, w4, w5, w6, w7;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		init_choicebox();
		//init time
		date_end.setValue(LocalDate.now());
		date_begin.setValue(LocalDate.now());
		
		w1.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getName()));
		w2.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getBtime()));
		w3.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getEtime()));
		w4.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getType()));
		w5.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getTimes()));
		w6.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getExtra()));
		w7.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getState()));
		//监听列表选中事件
		worktable.getSelectionModel().selectedItemProperty().addListener(// 选中某一行 
				new ChangeListener<Workdate>() { 
					@Override 
					public void changed( ObservableValue<? extends Workdate> observableValue, 
							Workdate oldItem, Workdate newItem) { 
						if(newItem != null) {
						String temp[] = newItem.getBtime().toString().split("-", 4);
						getworkname = newItem.getName();
						getwyear = temp[0];
						getwmonth = temp[1];
						getwday = temp[2];
						String tt[] = temp[3].split(":");
						getwbtime = tt[0];
						String te[] = newItem.getEtime().toString().split("-", 4);
						String t[] = te[3].split(":");
						getwetime = t[0];
						System.out.println(getworkname+getwyear+getwmonth+getwday+getwbtime+getwetime);
						}
						}
				});
		
	}
	
	private void init_choicebox() {
		dbox.clear();
		pbox.clear();
		sbox.clear();
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
		sbox.addAll("已通过","未通过","待审核","ALL");
		schoice.setItems(sbox);
		schoice.setValue("ALL");
	}
	
	private String makesql() {
		String sql = " ";
		
		String time_begin,time_end;
		time_begin = date_begin.getValue().toString();
		time_end = date_end.getValue().toString();
		time_begin += " 00:00:00";
		time_end += " 23:59:59";
		
		String temp = null;
		String inpname = " and post.pname=" + "'" + pchoice.getValue().toString().trim() + "'";
		String indname = " and department.dname=" + "'" + dchoice.getValue().toString().trim() + "'";
		int inid = get_inputname_id();
		String getinid = " and allowance.eid="+inid;
		if(pchoice.getValue().toString().trim().equals("ALL")) {
			inpname = " ";
		}
		if(dchoice.getValue().toString().trim().equals("ALL")) {
			indname = " ";
		}
		if(inid==0) {
			getinid = " ";
		}
		temp = "select employee.ename, ayear, amonth, aday, begin_time, end_time,"
			 + " _hours, work_type.wname, extra, state"
			 + " from employee, work_type, allowance, department, post"
			 + " where employee.eid=allowance.eid"
			 + " and employee.did=department.did"
			 + " and employee.pid=post.pid"
			 + " and work_type.wid=allowance.wid"
			 + " and nowtime>='%1$s'"
			 + " and nowtime<='%2$s'"
			 + inpname + indname + getinid;
		sql = String.format(temp, time_begin, time_end);
		return sql;
	}
	
	private String type_make() {
		String sql = " ";
		if(schoice.getValue().toString().trim().equals("已通过")) {
			sql = " and allowance.state=" + 1;
		}
		else if(schoice.getValue().toString().trim().equals("待审核")) {
			sql = " and allowance.state=" + 0;
		}
		else if(schoice.getValue().toString().trim().equals("未通过")) {
		    sql = " and allowance.state=" + 2;
		}
		return sql;
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
		workdate.clear();
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		try {
			String type = type_make();
			sql = makesql() + type;
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			String wname = null;
			int wyear;
			int wmonth, wday;
			int wbtime, wetime, wtimes;
			String wtype = null;
			String wstate = null;
			String wextra = null;
			while(rs.next()) {
				wname = rs.getString("employee.ename");
				wyear = rs.getInt("ayear");
				wmonth = rs.getInt("amonth");
				wday = rs.getInt("aday");
				wbtime = rs.getInt("begin_time");
				wetime = rs.getInt("end_time");
				wtype = rs.getString("work_type.wname");
				wtimes = rs.getInt("_hours");
				wextra = rs.getBigDecimal("extra").toString();
				if(rs.getInt("state")==0) {
					wstate = "待审核";
				}
				if(rs.getInt("state")==1) {
					wstate = "已通过";
				}
				if(rs.getInt("state")==2) {
					wstate = "未通过";
				}
				String bt = wyear + "-" + wmonth + "-" + wday + "-";
				workdate.add(new Workdate(wname,bt+wbtime+":00",bt+wetime+":00",wtype,wtimes+"",wextra,wstate));
				System.out.println(bt);
			}
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		worktable.setItems(workdate);
	}
	
	@FXML
	private void on_sure_click() {
		if(getworkname==null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("请选中一个职员");
			alert.setContentText("您未选择职员");
			alert.showAndWait();
			return;
		}
		update_Date(1);
		on_search_click();
	}
	@FXML
	private void on_failed_click() {
		if(getworkname==null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("请选中一个职员");
			alert.setContentText("您未选择职员");
			alert.showAndWait();
			return;
		}
		update_Date(2);
		on_search_click();
	}
	
	private int searcheid() {
		int eid = 0;
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select eid from employee where ename=?";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setString(1, getworkname);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			while(rs.next()) {
				eid = rs.getInt("eid");
			}
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		return eid;
	}
	private void update_Date(int state) {
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		String sql = null;
		int eid = searcheid();
		try {
			sql = "update allowance set"
			   + " allowance.state=?"
			   + " where allowance.eid=?"
			   + " and ayear=?"
			   + " and amonth=?"
			   + " and aday=?"
			   + " and begin_time=?"
			   + " and end_time=?";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setInt(1,state);
			pStatement.setInt(2,eid);
			pStatement.setInt(3,Integer.parseInt(getwyear));
			pStatement.setInt(4,Integer.parseInt(getwmonth));
			pStatement.setInt(5,Integer.parseInt(getwday));
			pStatement.setInt(6,Integer.parseInt(getwbtime));
			pStatement.setInt(7,Integer.parseInt(getwetime));
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			int sure = pStatement.executeUpdate();
			if(sure > 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText("操作成功");
				alert.setContentText("操作成功！");
				alert.showAndWait();
			}
		}catch(SQLException e1) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("操作失败");
			alert.setContentText("操作失败！");
			alert.showAndWait();
			e1.printStackTrace();
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
