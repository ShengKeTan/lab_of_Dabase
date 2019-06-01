package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	//舞台
	static Stage primarystage = null;
	//界面根结点
	private Parent login_root = null;
	//界面窗口
	private static Scene logon_scene = null;
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			//set root
			login_root = FXMLLoader.load(getClass().getClassLoader().getResource("logonUI.fxml"));
			//set scene
			logon_scene = new Scene(login_root);
			//set stage
			primarystage = primaryStage;
			setLoginUI(); //first scene chose
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	public static void setPrimaryStage(Stage stage) {
		primarystage = stage;
	}
	public static void  setLoginUI() {
		primarystage.setTitle("用户登陆");
		primarystage.setScene(logon_scene);
	}
	public static Stage getPrimaryStage() {
		return primarystage;
	}
}
