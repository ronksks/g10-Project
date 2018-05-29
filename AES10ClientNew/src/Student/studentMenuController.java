package Student;

import Login.LoginController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class studentMenuController {

	/**
	 * current stage
	 */
	private static Stage mainStage;
	
	
	 /**
     * Contains login details
     */
    private static LoginController loginController;
    
	
	public void start(Stage primaryStage) throws Exception {
		this.mainStage = primaryStage;
		
		String title = "student";
		String srcFXML = "/Student/StudentMenu.fxml";

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(srcFXML));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setTitle(title);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
			
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		          public void handle(WindowEvent we) {
		        	 Platform.exit();
		          }
		      }); 
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	
	 public void setLoginController(LoginController login)
	    {
	    	loginController = login;
	    }
	    
}
