package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class UsrController implements Initializable{
	
	String geteid = null;
	String gettime = null;
	static int rate;
	
	private ObservableList<Usrdata> usrdata = FXCollections.observableArrayList();
	private ObservableList<Checkdate> checkdate = FXCollections.observableArrayList();
	@FXML
	private TableView<Usrdata> data;
	@FXML
	private TableView<Checkdate> check;
	@FXML
	private TableColumn<Usrdata,String> time, name, department, post, noduty, allowance, total;
	@FXML
	private TableColumn<Checkdate,String> a1, a2, a3, a4;
	
	@FXML
	private Button seek, quit;
	@FXML
	private Button reflash, back;
	@FXML
	private Label label1, label2;
	@FXML
	private Label namelabel, note;
	@FXML
	private DatePicker date_end,date_begin;
	@FXML
	private DatePicker first_begin, first_end;
	
	private ObservableList<String> pbox = FXCollections.observableArrayList();
	private ObservableList<String> dbox = FXCollections.observableArrayList();
	@FXML
	private ChoiceBox<String> pchoice, dchoice;
	
	private ObservableList<String> sbox = FXCollections.observableArrayList();
	@FXML
	private ChoiceBox<String> state;
	@FXML
	private TextField inputname;
	
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		//set statebox
		sbox.addAll("出勤","请假");
		state.setItems(sbox);
		state.setValue("出勤");
		//set dbox
		init_choicebox();
		//init time
		date_end.setValue(LocalDate.now());
		date_begin.setValue(LocalDate.now());
		first_begin.setValue(LocalDate.now());
		first_end.setValue(LocalDate.now()); 
		//set note
		note.setText("今日尚未签到");
		init_check_state();
		
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
		a1.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getEname()));
		a2.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getDname()));
		a3.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getTime()));
		a4.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getState()));
		//对日期选择时间进行监听
		date_begin.setOnAction(event->{
			usrdata.clear();
			show_data();
		});
		date_end.setOnAction(event->{
			usrdata.clear();
			show_data();
		});
		first_begin.setOnAction(event->{
			checkdate.clear();
			on_checkseek_click();
		});
		first_end.setOnAction(event->{
			checkdate.clear();
			on_checkseek_click();
		});
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
		//监听部门选项
		dchoice.getSelectionModel().selectedIndexProperty().addListener(new 
				ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number>observable, Number oldValue, Number
							newValue) {
							System.out.println(newValue);
							System.out.println(dbox.get(newValue.intValue()));
							System.out.println(LogonController.getpname);
						}
				});
		//监听职务选项
		pchoice.getSelectionModel().selectedIndexProperty().addListener(new 
				ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number>observable, Number oldValue, Number
							newValue) {
							System.out.println(newValue);
							System.out.println(pbox.get(newValue.intValue()));
							System.out.println(LogonController.getpname);
						}
				});
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
	
	private void init_check_state() {
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select * from ch_eck where"
			   + " cyear=year(now())"
			   + " and cmonth=month(now())"
			   + " and cday=day(now())"
			   + " and eid=?";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setInt(1, LogonController.getusrid);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			while(rs.next()) {
				System.out.println("今天签到了！");
				note.setText("已完成今日签到");
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
	private void on_check_click() {
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		String sql = null;
		
		Date dNow = new Date();
		SimpleDateFormat regtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = regtime.format(dNow);
		
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        
        boolean leave = false;
        boolean duty = false;
        boolean rest =false;
        boolean noduty =false;
        boolean late = false;
        if(state.getValue().toString().trim().equals("出勤")) {
        	duty = true;
        	rest = false;
        } 
        if(state.getValue().toString().trim().equals("请假")) {
        	duty = false;
        	rest = true;
        }
        if( Calendar.HOUR_OF_DAY > 8) {
        	late = true;
        	duty = false;
        }
		try {
			sql = "insert into ch_eck values(?,?,?,?,?,?,?,?,?,?)";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setInt(1,LogonController.getusrid);
			pStatement.setInt(2,year);
			pStatement.setInt(3,month);
			pStatement.setInt(4,day);
			pStatement.setString(5, time);
			pStatement.setBoolean(6,late);
			pStatement.setBoolean(7,leave);
			pStatement.setBoolean(8,duty);
			pStatement.setBoolean(9,noduty);
			pStatement.setBoolean(10,rest);
			System.out.println(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			int sure = pStatement.executeUpdate();
			if(sure > 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText("签到成功");
				alert.setContentText("您已完成签到");
				alert.showAndWait();
			}
			else {
				System.out.println("请勿重复签到");
			}
		}catch(SQLException e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("签到失败");
			alert.setContentText("请勿重复签到");
			alert.showAndWait();
			System.out.println("请勿重复签到");
			e.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
	}
	@FXML
	private void on_checkseek_click() {
		checkdate.clear();
		//get time
		//LocalDate datetmp; 
		//LocalDate datetmp2; 
		String time_begin,time_end;
		time_begin = first_begin.getValue().toString();
		time_end = first_end.getValue().toString();
		time_begin += " 00:00:00";
		time_end += " 23:59:59";
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		try {
			String temp = null;
			temp = "select employee.ename, department.dname, ch_eck.ctimes, ch_eck.duty,"
			   + " ch_eck.rest, ch_eck.late, ch_eck.noduty"
			   + " from employee, department, ch_eck"
			   + " where employee.eid=ch_eck.eid"
			   + " and department.did=employee.did"
			   + " and ch_eck.ctimes>='%1$s'"
			   + " and ch_eck.ctimes<='%2$s'"
			   + " and ch_eck.eid=%3$d";
			sql = String.format(temp,time_begin,time_end,LogonController.getusrid);
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			String showname = null;
			String showdepname = null;
			String showtime = null;
			String showstate = null;
			while(rs.next()) {
				showname = rs.getString("employee.ename");
				showdepname = rs.getString("department.dname");
				showtime = rs.getString("ch_eck.ctimes");
				showstate = rs.getBoolean("ch_eck.duty")?"出勤":rs.getBoolean("ch_eck.rest")?"请假":rs.getBoolean("ch_eck.late")?"迟到":"缺勤";
				System.out.println(showname + showdepname + showtime + showstate);
				checkdate.add(new Checkdate(showtime,showname,showdepname,showstate));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		check.setItems(checkdate);
	}
	
	@FXML
	private void change_view() {
		System.out.println(LogonController.getpname);
		namelabel.setText(LogonController.getename);
		init_check_state();
		//设置部门、职务选项默值
		dchoice.setValue("ALL");
		pchoice.setValue("ALL");
		//设置姓名输入框提示
		inputname.setPromptText("ALL");
		System.out.println(inputname.getText().toString());
		//根据权限设置界面,取消可见
		if(LogonController.getpname.trim().equals("职员")) {
			pchoice.setVisible(false);
			dchoice.setVisible(false);
			label2.setVisible(false);
			label1.setVisible(false);
			inputname.setEditable(false);
		}
		if(LogonController.getpname.trim().equals("部门经理")){
			label1.setVisible(false);
			dchoice.setVisible(false);
		}
		System.out.println(dchoice.getValue()+pchoice.getValue());
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
	private String sql_make(String pname) {
		String sql = null;
		//time changed
		int bdatayear, edatayear;
		int bdatamonth, edatamonth;
		//get time from datapicker
		bdatayear = date_begin.getValue().getYear();
		bdatamonth = date_begin.getValue().getMonthValue();
		edatayear = date_end.getValue().getYear();
		edatamonth = date_end.getValue().getMonthValue();
		if(pname.equals("职员")) {
			String temp = null;
			temp = "select salary.syear, salary.smonth, employee.ename, department.dname, post.pname,"
				+ " salary.mtimes, salary.extra, salary.pay"
				+ " from salary, department, post, employee"
				+ " where salary.eid=employee.eid"
				+ " and employee.pid=post.pid"
				+ " and employee.did=department.did"
				+ " and salary.syear between %1$d and %2$d"
				+ " and salary.smonth between %3$d and %4$d"
				+ " and salary.eid=%5$d";
			sql = String.format(temp, bdatayear, edatayear, bdatamonth, edatamonth, LogonController.getusrid);
			System.out.println(sql);
		}
		else if(pname.equals("部门经理")) {
			String temp = null;
			String inpname = " and post.pname=" + "'" + pchoice.getValue().toString().trim() + "'";
			int inid = get_inputname_id();
			String getinid = " and salary.eid="+inid;
			if(pchoice.getValue().toString().trim().equals("ALL")) {
				inpname = " ";
			}
			if(inid==0) {
				getinid = " ";
			}
			temp = "select salary.syear, salary.smonth, employee.ename, department.dname, post.pname,"
				+ " salary.mtimes, salary.extra, salary.pay"
				+ " from salary, department, post, employee"
				+ " where salary.eid=employee.eid"
				+ " and employee.pid=post.pid"
				+ " and employee.did=department.did"
				+ " and salary.syear between %1$d and %2$d"
				+ " and salary.smonth between %3$d and %4$d"
				+ " and department.dname='%5$s'"
				+ inpname + getinid;
			sql = String.format(temp, bdatayear, edatayear, bdatamonth, edatamonth, LogonController.getdname);
			System.out.println(sql);
		}
		else {
			String temp = null;
			String inpname = " and post.pname=" + "'" + pchoice.getValue().toString().trim() + "'";
			String indname = " and department.dname=" + "'" + dchoice.getValue().toString().trim() + "'";
			int inid = get_inputname_id();
			String getinid = " and salary.eid="+inid;
			if(pchoice.getValue().toString().trim().equals("ALL")) {
				inpname = " ";
			}
			if(dchoice.getValue().toString().trim().equals("ALL")) {
				indname = " ";
			}
			if(inid==0) {
				getinid = " ";
			}
			temp = "select salary.syear, salary.smonth, employee.ename, department.dname, post.pname,"
				+ " salary.mtimes, salary.extra, salary.pay"
				+ " from salary, department, post, employee"
				+ " where salary.eid=employee.eid"
				+ " and employee.pid=post.pid"
				+ " and employee.did=department.did"
				+ " and salary.syear between %1$d and %2$d"
				+ " and salary.smonth between %3$d and %4$d"
				+ getinid + inpname + indname;
			sql = String.format(temp, bdatayear, edatayear, bdatamonth, edatamonth);
			System.out.println(sql);
		}
		return sql;
	}
	
	@FXML
	private void on_seek_click() {
		 show_data();
	}
	
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
			sql = sql_make(LogonController.getpname.trim());
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			System.out.println(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			int syear;
			int smonth;
			String sname = null;
			String sdepartment = null;
			String spost = null;
			String snoduty;
			String sextra;
			String stotal;
			while(rs.next()) {
				syear = rs.getInt("salary.syear");
				smonth = rs.getInt("salary.smonth");
				sname = rs.getString("employee.ename");
				sdepartment = rs.getString("department.dname");
				spost = rs.getString("post.pname");
				snoduty = rs.getString("salary.mtimes");
				sextra = rs.getBigDecimal("salary.extra").toString();
				stotal = rs.getBigDecimal("salary.pay").toString();
				usrdata.add(new Usrdata(syear+"--"+smonth,sname,sdepartment,spost,snoduty,sextra,stotal));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		//close connet
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		data.setItems(usrdata);
	}
	
	@FXML
	private void on_quit_click() {
		usrdata.clear();
		Main.setLoginUI();
	}
	

}
