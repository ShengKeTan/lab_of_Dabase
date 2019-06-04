package application;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ApplyController implements Initializable{
	
	@FXML
	private Button back;
	@FXML
	private TextField input_byear, input_bmonth, input_bday;
	@FXML
	private TextField input_eyear, input_emonth, input_eday;
	
	
	@FXML
	private ChoiceBox<String>type, bhour, ehour;
	private ObservableList<String> tbox = FXCollections.observableArrayList();
	private ObservableList<String> hbox = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		init_box();
		init_textfield();
	}
	
	private int checkstate() {
		int state = 1;
		if(input_byear.getText().equals("")) state = -1;
		if(input_bmonth.getText().equals("")) state = -1;
		if(input_bday.getText().equals("")) state = -1;
		if(input_eyear.getText().equals(""))state = -1;
		if(input_emonth.getText().equals(""))state = -1;
		if(input_eday.getText().equals(""))state = -1;
		if(type.getValue()==null || bhour.getValue()==null || ehour.getValue()==null) state = -1;
		return state;
	}
	
	private BigDecimal getextra(int type, int hours) {
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		
		BigDecimal temp = null;
		BigDecimal times = new BigDecimal(hours);
		BigDecimal sum = null;
		
		try {
			sql = "select pay from work_type where wid=?";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setInt(1, type);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			while(rs.next()) {
				temp = rs.getBigDecimal("work_type.pay");
			}	
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		sum = times.multiply(temp);
		
		return sum;
		
	}
	
	private void change_Date() {
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		String sql = null;
		
		Date dNow = new Date();
		SimpleDateFormat regtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = regtime.format(dNow);
		
		String year = input_byear.getText().toString().trim();
		String month = input_bmonth.getText().toString().trim();
		String day = input_bday.getText().toString().trim();
		String ibtime = bhour.getValue().toString().trim().substring(0, 2);
		String ietime = ehour.getValue().toString().trim().substring(0, 2);
		int hours = Integer.parseInt(ietime) - Integer.parseInt(ibtime);
		String gettype = type.getValue().toString().trim().substring(0, 1);
		BigDecimal ex = getextra(Integer.parseInt(gettype), hours);
		System.out.println(ex);
		
		try {
			sql = "insert into allowance values(?,?,?,?,?,?,?,?,?,?,?)";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setInt(1, LogonController.getusrid);
			pStatement.setInt(2, Integer.parseInt(year));
			pStatement.setInt(3, Integer.parseInt(month));
			pStatement.setInt(4, Integer.parseInt(day));
			pStatement.setInt(5, Integer.parseInt(ibtime));
			pStatement.setInt(6, Integer.parseInt(ietime));
			pStatement.setInt(7, hours);
			pStatement.setInt(8, Integer.parseInt(gettype));
			pStatement.setBigDecimal(9, ex);
			pStatement.setInt(10, 0);
			pStatement.setString(11, time);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			int sure = pStatement.executeUpdate();
			if(sure > 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText("申请成功");
				alert.setContentText("申请成功！");
				alert.showAndWait();
			}
		}catch(SQLException e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("申请失败");
			alert.setContentText("申请失败！");
			alert.showAndWait();
			e.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	
	
	@FXML void on_ok_click() {
		if(checkstate()==-1) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("数据输入错误");
			alert.setContentText("请检查输入");
			alert.showAndWait();
			return;
		}
		change_Date();
		on_back_click();
	}
	
	@FXML
	private void on_back_click() {
		Stage stage = (Stage)back.getScene().getWindow();
	    stage.close();
	}
	
	private void init_textfield() {
		//init time
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        
        input_byear.setText(String.valueOf(year));
        input_eyear.setText(String.valueOf(year));
        input_bmonth.setText(String.valueOf(month));
        input_emonth.setText(String.valueOf(month));
        input_bday.setText(String.valueOf(day));
        input_eday.setText(String.valueOf(day));
	}
	
	private void init_box() {
		tbox.clear();
		hbox.clear();
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select * from work_type";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			int wid;
			String wname;
			while(rs.next()) {
				wid = rs.getInt("wid");
				wname = rs.getString("wname");
				tbox.add(wid+wname);
			}
			type.setItems(tbox);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		for(int i=0; i<=9; i++) {
			hbox.addAll("0" +i+":00");
		}
		for(int i=10; i<=23; i++) {
			hbox.addAll(i+":00");
		}
		bhour.setItems(hbox);
		ehour.setItems(hbox);
	}

}
