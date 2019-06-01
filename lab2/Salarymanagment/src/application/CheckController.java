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

public class CheckController implements Initializable{
	
	static String oldcename = null;
	static String oldctime = null;
	static String oldcstate = null;
	
	
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
	
	private ObservableList<Checkdate> checkdate = FXCollections.observableArrayList();
	@FXML
	private TableColumn<Checkdate,String> a1, a2, a3, a4;
	@FXML
	private TableView<Checkdate> check;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		init_choicebox();
		
		date_end.setValue(LocalDate.now());
		date_begin.setValue(LocalDate.now());
		
		a1.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getEname()));
		a2.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getDname()));
		a3.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getTime()));
		a4.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getState()));
		
		//监听列表选中事件
		check.getSelectionModel().selectedItemProperty().addListener(// 选中某一行 
				new ChangeListener<Checkdate>() { 
					@Override 
					public void changed( ObservableValue<? extends Checkdate> observableValue, 
							Checkdate oldItem, Checkdate newItem) { 
						if(newItem != null) {
						oldcename = newItem.getEname();
						oldctime = newItem.getTime();
						oldcstate = newItem.getState();
						System.out.println(oldcename + oldctime + oldcstate); 
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
			dbox.add("ALL");
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
			pbox.add("ALL");
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
		
		sbox.addAll("迟到","出勤","请假","ALL");
		schoice.setItems(sbox);
		schoice.setValue("ALL");
	}
	
	private int get_inputname_id(String name) {
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
			pStatement.setString(1,name);
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
	
	private String type_make() {
		String sql = " ";
		if(schoice.getValue().toString().trim().equals("迟到")) {
			sql = " and ch_eck.late=" + 1;
		}
		else if(schoice.getValue().toString().trim().equals("出勤")) {
			sql = " and ch_eck.duty=" + 1;
		}
		else if(schoice.getValue().toString().trim().equals("请假")) {
		    sql = " and ch_eck.rest=" + 1;
		}
		return sql;
	}
	
	private String makesql() {
		String sql = " ";
		
		String time_begin,time_end;
		time_begin = date_begin.getValue().toString();
		time_end = date_end.getValue().toString();
		time_begin += " 00:00:00";
		time_end += " 23:59:59";
		
		String inpname = " and post.pname=" + "'" + pchoice.getValue().toString().trim() + "'";
		String indname = " and department.dname=" + "'" + dchoice.getValue().toString().trim() + "'";
		String instate= type_make();
		int inid = get_inputname_id(inputname.getText().toString().trim());
		String getinid = " and ch_eck.eid="+inid;
		if(pchoice.getValue().toString().trim().equals("ALL")) {
			inpname = " ";
		}
		if(dchoice.getValue().toString().trim().equals("ALL")) {
			indname = " ";
		}
		if(schoice.getValue().toString().trim().equals("ALL")) {
			
		}
		if(inid==0) {
			getinid = " ";
		}
		String temp = null;
		temp = "select employee.ename, department.dname, ch_eck.ctimes, ch_eck.duty,"
				   + " ch_eck.rest, ch_eck.late, ch_eck.noduty"
				   + " from employee, department, ch_eck, post"
				   + " where employee.eid=ch_eck.eid"
				   + " and department.did=employee.did"
				   + " and post.pid=employee.pid"
				   + " and ch_eck.ctimes>='%1$s'"
				   + " and ch_eck.ctimes<='%2$s'"
				   + inpname + indname + getinid + instate;
				sql = String.format(temp,time_begin,time_end);
		System.out.println(sql);
		return sql;
	}
	
	@FXML
	private void on_checkseek_click() {
		checkdate.clear();
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = makesql();
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
	
	private int check_choice() {
		if(oldcename==null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("请选中一个职员");
			alert.setContentText("您未选择职员");
			alert.showAndWait();
			return -1;
		}
		else return 1;
	}
	
	@FXML
	private void on_duty_click() {
		if(check_choice()==-1) return;
		changedate("duty");
		on_checkseek_click();
	}
	@FXML
	private void on_late_click() {
		if(check_choice()==-1) return;
		changedate("late");
		on_checkseek_click();
	}
	@FXML
	private void on_rest_click(){
		if(check_choice()==-1) return;
		changedate("rest");
		on_checkseek_click();
	}
	@FXML
	private void on_delete_click() {
		if(check_choice()==-1) return;
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		String sql = null;
		int changeid = get_inputname_id(oldcename);
		try {
			sql = "delete from ch_eck where"
			   + " eid=?"
			   + " and ctimes=?";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setInt(1, changeid);
			pStatement.setString(2, oldctime);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			int sure = pStatement.executeUpdate();
			if(sure > 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText("删除成功");
				alert.setContentText("删除成功！");
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
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		
		on_checkseek_click();
	}
	
	private void changedate(String state) {
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		String sql = null;
		int changeid = get_inputname_id(oldcename);
		String chetype = " ";
		if(oldcstate.trim().equals("请假")) {
			chetype = " rest=0";
		}
		else if(oldcstate.trim().equals("迟到")){
			chetype = " late=0";
		}
		else {
			chetype = " duty=0";
		}
		
		try {
			sql = "update ch_eck set"
			   + chetype + ","
			   + " "+ state + "=1"
			   + " where eid=?"
			   + " and ctimes=?";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setInt(1, changeid);
			pStatement.setString(2, oldctime);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			int sure = pStatement.executeUpdate();
			if(sure > 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText("修改成功");
				alert.setContentText("修改成功！");
				alert.showAndWait();
			}
		}catch(SQLException e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("修改失败");
			alert.setContentText("修改失败！");
			alert.showAndWait();
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
