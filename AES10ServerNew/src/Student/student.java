package Student;

import java.io.Serializable;

import Users.User;

/**
 * Entity class that contains the student details
 *
 */
public class student extends User implements Serializable {
	/** student id */
	private int sId;
	/** The avg of student */
	private float avg;
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
	 * Getter for get the avg 
	 * @return avg number
	 */
	public float getavg() {
		return avg;
	}
		
	/**
	 * Setter for set the avg
	 * @param avg 
	 */
	public void setavg(float avg) {
		this.avg = avg;
	}


	/**
	 * Getter of get the student id
	 * @return student number
	 */
	public int getsId() {
		return sId;
	}

	/**
	 * Setter for set the student id
	 * @param sId to set
	 */
	public void setsId(int sId) {
		this.sId = sId;
	}

	/**
	 * Constructor that initialize instance attributes of student
     * @param uId user Id
	 * @param user user name
	 * @param password password
	 * @param isLogged state if the user is logged in
	 * @param permission user permission
	 * @param sId student id
	 * @param avg 
	 */
	public student(int uId, String user, String password, boolean isLogged,Users.Permission permission,int sId,float avg, int did) {
		super(uId, user, password, isLogged,permission);
		// TODO Auto-generated constructor stub
		setsId(sId);
		setavg(avg);
		setdId(did);
	}
	
	/**
	 * Constructor that initialize instance attributes of student
	 * @param user user name
	 * @param sId student id
	 * @param avg 
	 */
	public student(User user, int sId, float avg)
	{
		super(user);
		setsId(sId);
	    setavg(avg);
	}
	
	
	/**
	 * Constructor that initialize instance attributes of student
	 * @param user user name
	 * @param sId student id
	
	 */
	public student(User user, int sId)
	{
		super(user);
		setsId(sId);
	  
	}
	
	
	
	
	/**
	 * Constructor that initialize instance attributes of student
	 * @param uId user id
	 * @param sId student id
	 */
	public student(int uId, int sId)
	{
		super(uId);
		setsId(sId);
		
	}

}