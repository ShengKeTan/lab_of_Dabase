package application;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class SetsalaryController implements Initializable{
	@FXML
	private Button back;
	@FXML
	private TextField salary;
	
	private ObservableList<String> pbox = FXCollections.observableArrayList();
	@FXML
	private ChoiceBox<String> pchoice;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		init_choicebox();
		//监听职务选项
		pchoice.getSelectionModel().selectedIndexProperty().addListener(new 
				ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number>observable, Number oldValue, Number
							newValue) {
							System.out.println(newValue);
							System.out.println(pbox.get(newValue.intValue()));
							init_textfield(pbox.get(newValue.intValue()));
							System.out.println(LogonController.getpname);
						}
				});
		
	}
	private void init_textfield(String value) {
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select bpay from post where pname=?";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setString(1, value);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			String getsalary = null;
			while(rs.next()) {
				getsalary = rs.getString("bpay");
				salary.setPromptText(getsalary);
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
	
	private void init_choicebox() {
		pbox.clear();
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
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
	
	@FXML
	private void on_ok_click() {
		if(salary.getText().equals("")) {
			System.out.println("null");
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("修改失败");
			alert.setContentText("请输入正确的工资数额");
			alert.showAndWait();
			return;
		}
		set_salary();
	}
	
	private void set_salary() {
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		String sql = null;
		BigDecimal getbpay = new BigDecimal(salary.getText().toString());
		try {
			sql = "update post set"
			   + " bpay=?"
			   + " where post.pname=?";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setBigDecimal(1, getbpay);
			pStatement.setString(2, pchoice.getValue().toString());
		}catch(SQLException e1) {
			e1.printStackTrace();
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
		}catch(SQLException e1) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("修改失败");
			alert.setContentText("修改失败！");
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
