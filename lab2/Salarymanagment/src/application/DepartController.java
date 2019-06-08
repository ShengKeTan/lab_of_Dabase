package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class DepartController implements Initializable{
	
	String old_did = null;
	String old_dname = null;
	
	@FXML
	private Button back;
	@FXML
	private TextField changename, addname;
	
	private ObservableList<DepartmentDate> departdata = FXCollections.observableArrayList();
	@FXML
	private TableView<DepartmentDate> table_depart;
	@FXML
	private TableColumn<DepartmentDate,String> did, dname;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		did.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getDid()));
		dname.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getDname()));
		
		//监听列表选中事件
		table_depart.getSelectionModel().selectedItemProperty().addListener(// 选中某一行 
				new ChangeListener<DepartmentDate>() { 
					@Override 
					public void changed( ObservableValue<? extends DepartmentDate> observableValue, 
							DepartmentDate oldItem, DepartmentDate newItem) { 
					if(newItem != null) {
						 old_did = newItem.getDid();
						 old_dname = newItem.getDname();
						 changename.setText(old_dname);
						 System.out.println(old_did + old_dname); 
						}
						}
				});
		
	}
	
	@FXML
	private void on_change_click() {
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		String sql = null;
		try {
			sql = "update department set"
			   + " dname=?"
			   + " where did=?";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setString(1, changename.getText().toString().trim());
			pStatement.setInt(2, Integer.parseInt(old_did));
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
				changename.clear();
			}
		}catch(SQLException e1) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("修改失败");
			alert.setContentText("修改失败！");
			alert.showAndWait();
			e1.printStackTrace();
			changename.clear();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	private int get_did() {
		int did = 0;
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select count(*) from department";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			while(rs.next()) {
				did = rs.getInt("count(*)");
			}
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		return (did+1);
	}
	
	@FXML
	private void on_add_click() {
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		String sql = null;
		int getdid = get_did();
		try {
			sql = "insert into department values(?,?)";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setInt(1, getdid);
			pStatement.setString(2, addname.getText().toString().trim());
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			int sure = pStatement.executeUpdate();
			if(sure > 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText("添加成功");
				alert.setContentText("部门编号：" + getdid);
				alert.showAndWait();
				addname.clear();
			}
		}catch(SQLException e1) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("添加失败");
			alert.setContentText("添加失败！");
			alert.showAndWait();
			e1.printStackTrace();
			addname.clear();
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
	
	@FXML
	private void on_refresh_click() {
		departdata.clear();
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql = "select * from department";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			String getdid = null;
			String getdname = null;
			while(rs.next()) {
				getdid = rs.getString("did");
				getdname = rs.getString("dname");
				departdata.addAll(new DepartmentDate(getdid,getdname));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		table_depart.setItems(departdata);
	}

}
