package lecturer;
//
import java.io.Serializable;

import Users.User;
import department.department;

/**
 * Entity class that contains the lecturer details
 *
 */
public class lecturer extends User implements Serializable {
	/** lecturer id */
	private int lId;
	
	/** The department of lecturer */
	private int dId;
	
	
	/**
	 * Getter for get the department id
	 * @return department number
	 */
	public int getdId() {
		return dId;
	}

	
	/**
	 * Setter for set the department id
	 * @param dId branch id
	 */
	public void setdId(int dId) {
		this.dId = dId;
	}
	
	
	
	

	/**
	 * Getter of get the lecturer id
	 * @return lecturer number
	 */
	public int getlId() {
		return lId;
	}

	/**
	 * Setter for set the lecturer id
	 * @param lId to set
	 */
	public void setlId(int lId) {
		this.lId = lId;
	}

	/**
	 * Constructor that initialize instance attributes of lecturer
     * @param uId user Id
	 * @param user user name
	 * @param password password
	 * @param isLogged state if the user is logged in
	 * @param permission user permission
	 * @param lId lecturer id
	 */
	public lecturer(int uId, String user, String password, boolean isLogged,Users.Permission permission,int lId,int did) {
		super(uId, user, password, isLogged,permission);
		// TODO Auto-generated constructor stub
		setlId(lId);
		setdId(did);
		
	}
	
	
	
	/**
	 * Constructor that initialize instance attributes of lecturer
	 * @param user user name
	 * @param lId lecturer id
	 
	 */
	public lecturer(User user, int lId)
	{
		super(user);
		setlId(lId);
		
	}
	
	
	/**
	 * Constructor that initialize instance attributes of lecturer
	 * @param uId user id
	 * @param lId lecturer id
	 
	 */
	public lecturer(int uId, int lId)
	{
		super(uId);
		setlId(lId);
		
	}

}