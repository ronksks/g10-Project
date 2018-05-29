package Server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;


import Logic.DbGetter;
import Logic.DbQuery;
import Logic.DbUpdater;
import Logic.ISelect;
import Logic.IUpdate;
import PacketSender.Command;
import PacketSender.Packet;
import Student.student;
import Users.Permission;
import Users.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lecturer.lecturer;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

/**
 * This class contain GUI and buttons behavior on the server side Available to
 * set up connection to the database and selecting port to listen Also contains
 * main function to get command from client and send it to relevant function in
 * the class
 */
public class SystemServer extends AbstractServer {

	public SystemServer(int port) {
		super(port);
	}

	/**
	 * default user
	 */
	private String user = "root";
	/**
	 * default password
	 */
	private String password = "1q2w3e!";
	private String database;
	private Timer timer = new Timer();
	/**
	 * default port
	 */
	private static final int DEFAULT_PORT = 5555;
	private DbQuery dbConnection;
	/**
	 * Menu button run immediate scheduler
	 */
	@FXML
	private MenuButton btnSchedule;
	/**
	 * Charge all memberships account which didn't pay until now
	 */
	@FXML
	private MenuItem btnCharging;
	/**
	 * Generate reports
	 */
	@FXML
	private MenuItem btnReport;
	/**
	 * Clear memberships which are out of date
	 */
	@FXML
	private MenuItem btnDelete;
	@FXML
	private TextField txtPort;
	/**
	 * Run/Stop server
	 */
	@FXML
	private Button btnSubmit;
	@FXML
	private TextArea txtLog;
	@FXML
	private TextField txtDb;
	@FXML
	private TextField txtUser;
	@FXML
	private PasswordField txtPass;
	/**
	 * clear log textField
	 */
	@FXML
	private Button btnClear;
	@FXML
	private Pane paneDetails;
	int port = 0; // Port to listen on

	public SystemServer() {
		super(DEFAULT_PORT);
	}

	/**
	 * print message to textField with date and time
	 * 
	 * @param msg
	 *            message to write to the log
	 */
	public void printlogMsg(String msg) {
		String time = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());// get datetime for log print
		txtLog.setText(time + "---" + msg + "\n\r" + txtLog.getText());
	}

	/**
	 * if button pressed check the function check if server already listen to port
	 * if yes then stop to listen otherwise start listen and update button text
	 * 
	 * @param event
	 *            actual event
	 */
	public void onSubmitClicked(ActionEvent event) {
		if (!isListening())// check if not listen
		{
			try {
				port = Integer.parseInt(txtPort.getText()); // Get port from command line
				this.setPort(port);
			} catch (Throwable t) {// if port is wrong or listening already
				printlogMsg("ERROR - Could not listen for clients from this port! Using default port");
				this.setPort(DEFAULT_PORT);
				paneDetails.setDisable(false);
				return;
			}
		}
		if (changeListening(txtDb.getText(), txtUser.getText(), txtPass.getText()))// check if switch listening is
																					// complete
		{
			if (btnSubmit.getText().equals("Start service")) {// if it wasn't listening
				database = txtDb.getText();
				user = txtUser.getText();
				password = txtPass.getText();
				printlogMsg("Server has started listening on port:" + port);// write to log

				// start scheduling task every night at 0:00 am
				dbConnection = new DbQuery(user, password, database);
				Calendar today = Calendar.getInstance();
				today.set(Calendar.HOUR_OF_DAY, 0);
				today.set(Calendar.MINUTE, 0);
				today.set(Calendar.SECOND, 0);
				// timer = new Timer();
				// timer.schedule(new ScheduleTask(dbConnection, this), today.getTime(),
				// TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // period: 1 day

				paneDetails.setDisable(true);
				btnSchedule.setDisable(false);
				btnSubmit.setText("Stop service");// update button

			} else// if it was listen
			{
				printlogMsg("Server has finished listening on port:" + port);

				// cancel the scheduling
				timer.cancel();

				paneDetails.setDisable(false);
				btnSchedule.setDisable(true);
				btnSubmit.setText("Start service");/// update button
			}
		}
	}

	/**
	 * printing error from scheduling
	 * 
	 * @param msgError
	 *            error message from scheduling
	 */
	public void logErrorSchedule(String msgError) {
		printlogMsg("Failed: " + msgError);
	}

	/***
	 * clear log text area
	 * 
	 * @param event
	 *            actual event
	 */
	public void onClearClicked(ActionEvent event) {
		txtLog.clear();
	}

	/**
	 * Show the scene view of the server *
	 * 
	 * @param arg0
	 *            current stage to build
	 * @throws Exception
	 *             if failed to display
	 */
	public void start(Stage arg0) throws Exception {

		String title = "Server";
		String srcFXML = "/Server/App.fxml";
		String srcCSS = "/Server/application.css";
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(srcFXML));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource(srcCSS).toExternalForm());
			arg0.setTitle(title);
			arg0.setScene(scene);
			arg0.show();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}

		arg0.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				// cancel the scheduling
				timer.cancel();

				System.exit(0);
			}
		});
	}

	/**
	 * turn on/off listening print the result to the log
	 * 
	 * @param database
	 *            to check the connection before starting to listening
	 * @param user
	 *            user to connect to the database
	 * @param password
	 *            password to connect to the database
	 * @return true if success else false
	 */
	public boolean changeListening(String database, String user, String password) {
		if (!isListening())// if start service has been pressed
		{
			if (database.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Please Fill DataBase name", "Error", JOptionPane.ERROR_MESSAGE);
				printlogMsg("database name missing\n\r");
				return false;
			}
			if (user.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Please Fill user name", "Error", JOptionPane.ERROR_MESSAGE);
				printlogMsg("user name missing");
				return false;
			}
			try {
				DbQuery db = new DbQuery(user, password, database);// check connection to database
				db.connectToDB();
				db.connectionClose();
				listen(); // Start listening for connections
			} catch (Exception e) {
				printlogMsg(e.getMessage());
				return false;
			}
		} else// if stop service has been pressed
		{
			try {
				stopListening();
				close();
			} catch (IOException e) {
				printlogMsg(e.getMessage());
			}
		}
		return true;
	}
	
	
	/**
	  * getting customer by user id
	  * @param db -Stores database information 
	  * @param key  - Command operation which is performed
	  */
	public void getStudentKeyByuIdHandler(DbQuery db, Command key)
	{
		DbGetter dbGet = new DbGetter(db, key);
		dbGet.performAction(new ISelect() {
		/**
		 * Perform a Select query to get customer
		 */
		@Override
		public String getQuery() {
		return "SELECT sId,uId FROM student where uId=?";
	}
	/**
	 * Parse the result set in to a Customer object
	 */	
	@Override
	public Object createObject(ResultSet rs) throws SQLException {
		int sId = rs.getInt(1);
		int uId=rs.getInt(2);
		student stu;
		stu=new student(sId, uId);
		return (Object)stu;
	}
	/**
	 *	adding customer user id  field for this query
	 */
	@Override
	public void setStatements(PreparedStatement stmt, Packet packet) throws SQLException { 
		Integer stu = (Integer) packet.getParameterForCommand(Command.getStudentByuId).get(0);
		stmt.setInt(1, stu);
		}
	});
}
	
	
	 /**
	  * getting employee by user id  
	  * @param db -Stores database information 
	  * @param key  - Command operation which is performed
	  */
	public void getlecturerByuIdHandler(DbQuery db, Command key)
	{
		DbGetter dbGet = new DbGetter(db, key);
		dbGet.performAction(new ISelect() {
		/**
		 * Perform a Select query to get all the employee by user id
		 */
		@Override
		public String getQuery() {
		return "SELECT uId, lId, dId FROM lecturer where uId=?";
	}
	/**
	 * Parse the result set in to a Employee object
	 */
	@Override
	public Object createObject(ResultSet rs) throws SQLException {
		int lId = rs.getInt(1);
		int uId = rs.getInt(2);
		int dId = rs.getInt(3);
		//getting the role
		
		
		return new lecturer(lId, uId);
	}
	/**
	 *	setting user id field for this query
	 */
	@Override
	public void setStatements(PreparedStatement stmt, Packet packet) throws SQLException { 
		Integer uId = (Integer) packet.getParameterForCommand(Command.getlecturerByUid).get(0);
		stmt.setInt(1, uId);
		}
	});
}
	
	
	
	/**
	  * getting user by user id
	  * @param db -Stores database information 
	  * @param key  - Command operation which is performed
	  */
	public void getUserByuIdHandler(DbQuery db, Command key)
	{
		
		DbGetter dbGet = new DbGetter(db, key);
		dbGet.performAction(new ISelect() {
			/**
			 *	adding user id field for this query
			 */
			@Override
			public void setStatements(PreparedStatement stmt, Packet packet) throws SQLException {
				stmt.setInt(1, (Integer)packet.getParameterForCommand(Command.getUserByuId).get(0));
			}
			/**
			 * Perform a Select query to get user
			 */
			@Override
			public String getQuery() {
				// TODO Auto-generated method stub
				return "SELECT * "+ "FROM User u where uId=?";
			}
			/**
			 * Parse the result set in to a User object
			 */
			@Override
			public Object createObject(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				int uId = rs.getInt(1);
				String user =rs.getString(2);
				String password = rs.getString(3);
				int islogged = rs.getInt(4);
				String perm=rs.getString(5);
				Permission permission = null;
				boolean isloggedbool=(islogged==1);
				User newuser;
				/**
				 * checking the permission
				 */
				if(perm.equals((Permission.Administrator).toString()))
				permission= Permission.Administrator;
				else if(perm.equals((Permission.Blocked).toString()))
				permission= Permission.Blocked;
				else if(perm.equals((Permission.Limited).toString()))
				permission= Permission.Limited;
				/**
				 * building the user information 
				 */
				newuser=new User(uId, user, password, isloggedbool, permission);
			return (Object)newuser;
			}
		});
		
	}
	
	/** 
	 * Get User instance by it's username and password
	 * @param db -Stores database information 
	 * @param key  - Command operation which is performed
	 *  */
	public void getUserByNameAndPassHandler(DbQuery db, Command key)
	{
		DbGetter dbGet = new DbGetter(db, key);
		dbGet.performAction(new ISelect() {
			@Override
			public String getQuery() {
				return "SELECT uId, user, password, isLogged, permission FROM user where user=? AND password=?";
			}
			@Override
			public Object createObject(ResultSet rs) throws SQLException {
				int uId = rs.getInt(1);
				String user = rs.getString(2);
				String password = rs.getString(3);
				int islogged = rs.getInt(4);
				String perm = rs.getString(5);
				Permission permission = null;
				boolean isloggedbool = (islogged == 1);

				if (perm.equals((Permission.Administrator).toString()))
					permission = Permission.Administrator;
				else if (perm.equals((Permission.Blocked).toString()))
					permission = Permission.Blocked;
				else if (perm.equals((Permission.Limited).toString()))
					permission = Permission.Limited;

				return new User(uId, user, password, isloggedbool, permission);
			}
			@Override
			public void setStatements(PreparedStatement stmt, Packet packet) throws SQLException {
				ArrayList<Object> params = packet.getParameterForCommand(Command.getUserByNameAndPass);
				User user = (User) params.get(0);

				stmt.setString(1, user.getUser());
				stmt.setString(2, user.getPassword());
			}
		});
	}
	
	
	
	
	/**
	 * receive the package 
	 * complete commands
	 * return data to the client
	 * and status of execution
	 * if failed return exception message
	 * @param msg - message from client with command and data for the query 
	 * @param client - from who the message come
	 */
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		Packet packet = (Packet) msg;
		printlogMsg("from: "+client+" commands: "+packet.getCommands());
		DbQuery db = new DbQuery(user, password, packet, client,database);
		try {
			db.connectToDB();
			for (Command key : packet.getCommands())
			{
				switch(key)
				{
	
	
				case getUserByuId:
					getUserByuIdHandler(db, key);
					break;	
				case getStudentByuId:
					getStudentKeyByuIdHandler(db, key);
					break;			
	
				case getlecturerByUid:
					getlecturerByuIdHandler(db, key);
					break;		
				
				case getUserByNameAndPass:
					getUserByNameAndPassHandler(db, key);
					break;	
					
					
				}		
			}
			db.connectionClose();
		}
		catch (Exception e) {
			printlogMsg(e.getMessage());
			packet.setExceptionMessage(e.getMessage());
		}
		finally {
			try {
				db.sendToClient();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				printlogMsg(e.getMessage());
			}
		}
	}
				
				
				
				}
