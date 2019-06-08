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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AmountController implements Initializable{
	
	String old_uname = null;
	
	@FXML
	private Button back;
	@FXML
	private TextField inputname;
	private ObservableList<String> pbox = FXCollections.observableArrayList();
	private ObservableList<String> dbox = FXCollections.observableArrayList();
	@FXML
	private ChoiceBox<String> dchoice, pchoice;
	
	private ObservableList<AmountDate> amountdata = FXCollections.observableArrayList();
	@FXML
	private TableView<AmountDate> amounttable;
	@FXML
	private TableColumn<AmountDate,String> eid, ename, dname, pname, uname, admin;

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
		uname.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getUname()));
		admin.setCellValueFactory(cellDate -> new 
				SimpleStringProperty(cellDate.getValue().getAdmin()));
		
		//监听列表选中事件
		amounttable.getSelectionModel().selectedItemProperty().addListener(// 选中某一行 
				new ChangeListener<AmountDate>() { 
					@Override 
					public void changed( ObservableValue<? extends AmountDate> observableValue, 
							AmountDate oldItem, AmountDate newItem) { 
					if(newItem != null) {
						 old_uname = newItem.getUname();
						 System.out.println(old_uname); 
						}
						}
				});
	}
	
	@FXML
	private void on_sure_click() {
		check_choice(true);
	}
	@FXML
	private void on_false_click() {
		check_choice(false);
	}
	
	private void check_choice(boolean authority) {
		if(old_uname==null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("请选中一项");
			alert.setContentText("您为未选中数据");
			alert.showAndWait();
			return;
		}
		else {
			adminchange(authority);
		}
		on_search_click();
	}
	
	private void adminchange(boolean authority) {
		//connect to mysql
		Con2mysql con = new Con2mysql();
		Connection mycon = con.connect2mysql();
		//get info
		PreparedStatement pStatement = null;
		String sql = null;
		try {
			sql = "update _usr set"
			   + " authority=?"
			   + " where usr=?";
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
			pStatement.setBoolean(1, authority);
			pStatement.setString(2, old_uname);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			int sure = pStatement.executeUpdate();
			if(sure > 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText("设置成功");
				alert.setContentText("设置成功！");
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
		amountdata.clear();
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
			sql = "select _usr.eid, employee.ename, department.dname, post.pname, _usr.usr, _usr.authority"
			   + " from _usr, employee, department, post"
			   + " where _usr.eid=employee.eid"
			   + " and employee.did=department.did"
			   + " and employee.pid=post.pid"
			   + inpname + indname + getinid;
			pStatement = (PreparedStatement)mycon.prepareStatement(sql);
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			rs = pStatement.executeQuery();
			String geid = null;
			String gename = null;
			String gdname = null;
			String gpname = null;
			String guname = null;
			String gadmin = null;
			while(rs.next()) {
				geid = rs.getString("_usr.eid");
				gename = rs.getString("employee.ename");
				gdname = rs.getString("department.dname");
				gpname = rs.getString("post.pname");
				guname = rs.getString("_usr.usr");
				if(rs.getBoolean("_usr.authority")) {
					gadmin = "是";
				}
				else gadmin = "否";
				amountdata.addAll(new AmountDate(geid,gename,gdname,gpname,guname,gadmin));
			}
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		try {
			mycon.close();
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
		amounttable.setItems(amountdata);
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

}
