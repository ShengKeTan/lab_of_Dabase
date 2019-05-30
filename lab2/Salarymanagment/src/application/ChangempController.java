package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class ChangempController implements Initializable{
	
	@FXML
	private Button back;
	@FXML
	private TextField noweid, nowename, nowdname, nowpname, nowage, nowsex;
	@FXML
	private TextField neweid, newename, newage;
	
	private ObservableList<String> pbox = FXCollections.observableArrayList();
	private ObservableList<String> dbox = FXCollections.observableArrayList();
	private ObservableList<String> sbox = FXCollections.observableArrayList();
	@FXML
	private ChoiceBox<String> dchoice, pchoice, schoice;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		sbox.addAll("男","女");
		schoice.setItems(sbox);
		init_choicebox();
		init_date();
	}
	
	private void init_date() {
		noweid.setText(EmployeeController.old_eid);
		noweid.setEditable(false);
		nowename.setText(EmployeeController.old_ename);
		nowename.setEditable(false);
		nowdname.setText(EmployeeController.old_dname);
		nowdname.setEditable(false);
		nowpname.setText(EmployeeController.old_pname);
		nowpname.setEditable(false);
		nowage.setText(EmployeeController.old_age);
		nowage.setEditable(false);
		nowsex.setText(EmployeeController.old_sex);
		nowsex.setEditable(false);
	}
	
	@FXML
	private void on_ok_click() {
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		String sql = null;
		String geid = neweid.getText().toString().trim();
		String gage = newage.getText().toString().trim();
		String gname = newename.getText().toString().trim(); 
		String gsex = schoice.getValue().toString().trim();
		int gdid = get_departid();
		int gpid = get_postid();
		try {
			sql = "update employee set"
			   + " eid=?,"
			   + " did=?,"
			   + " pid=?,"
			   + " ename=?,"
			   + " age=?,"
			   + " sex=?"
			   + " where eid=?";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setInt(1,Integer.parseInt(geid));
			pStatement.setInt(2, gdid);
			pStatement.setInt(3, gpid);
			pStatement.setString(4, gname);
			pStatement.setInt(5, Integer.parseInt(gage));
			pStatement.setString(6, gsex);
			pStatement.setString(7, EmployeeController.old_eid);
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
		on_back_click();
	}
	
	@FXML
	private void on_back_click() {
		Stage stage = (Stage)back.getScene().getWindow();
	    stage.close();
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
			dchoice.setItems(dbox);
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
			pchoice.setItems(pbox);
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

}
