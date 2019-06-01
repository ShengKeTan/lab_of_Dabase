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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class ApplyController implements Initializable{
	
	@FXML
	private Button back;
	
	@FXML
	private ChoiceBox<String>type, bhour, ehour;
	private ObservableList<String> tbox = FXCollections.observableArrayList();
	private ObservableList<String> hbox = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		init_box();
		
	}
	
	@FXML void on_ok_click() {
		
	}
	
	@FXML
	private void on_back_click() {
		Stage stage = (Stage)back.getScene().getWindow();
	    stage.close();
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
		for(int i=0; i<=23; i++) {
			hbox.addAll(i+":00");
		}
		bhour.setItems(hbox);
		ehour.setItems(hbox);
	}

}
