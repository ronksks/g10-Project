package Login;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller
 * Manage connection setting to the server
 *
 */
public class ConfigurationController implements Initializable {

    @FXML
    private TextField txtPort;

    @FXML
    private Button btnSave;

    @FXML
    private TextField txtAddress;
	/**
	 * default ip address
	 */
	public static String address = "localhost";
	/**
	 * default port number
	 */
	public static int port = 5555;
	
	/**
	 * Show an Alert dialog with custom info
	 * @param type type alert
	 * @param title title of the window
	 * @param header header of the message
	 * @param content message
	 */
	public void displayAlert(AlertType type , String title , String header , String content)
	{
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	/**
	 * saving configuration to connect and closing the window
	 * @param event actual event
	 */
	public void saveConfigFields(ActionEvent event)
	{
		try
		{
			address = txtAddress.getText();
			port = Integer.valueOf(txtPort.getText());
			Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		primaryStage.close(); //closing primary window
		}
		catch (NumberFormatException e)
		{
			displayAlert(AlertType.ERROR, "Error", "Invalid Port", "The Port must be a valid Number!");
		}
	}
	/**
	 * initialize window
	 * @param primaryStage stage to build
	 * @throws Exception messge if failed
	 */
	public void start(Stage primaryStage) throws Exception {
		String title = "Configuration";
		String srcFXML = "/Login/ConfigurationUI.fxml";

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(srcFXML));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setTitle(title);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		txtAddress.setText(address);
		txtPort.setText(""+port);
	}

}