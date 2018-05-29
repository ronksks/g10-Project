package department;

import java.io.Serializable;
import java.util.ArrayList;

import Student.student;
import lecturer.lecturer;


/**
 * 
 *	Entity Class Contain department Details 
 */
public class department implements Serializable {
	private int did;
	private String dName;
	/** Contains all students */
	private ArrayList<student> studentList;
	/** Contains all lecturers */
	private ArrayList<lecturer> lecturerList;
	
	/**
	 * Getter for get the department name
	 * @return dName
	 */
	public String getdName() {
		return dName;
	}
	
	/**
	 * Setter for set the department name
	 * @param name The department name
	 */
	public void setdName(String dName) {
		this.dName = dName;
	}
	
	/**
	 * Get The student list of the department
	 * @return list of student
	 */
	public ArrayList<student> getstudentList() {
		return studentList;
	}
	
	/**
	 * Set The student list in The department
	 * @param studentList The student list
	 */
	public void setstudentList(ArrayList<student> studentList) {
		this.studentList = studentList;
	}
	
	
	/**
	 * Get The lecturer list of the department
	 * @return list of lecturer
	 */
	public ArrayList<lecturer> getlecturerList() {
		return lecturerList;
	}
	
	/**
	 * Set The lecturer list in The department
	 * @param lecturerList The lecturer list
	 */
	public void setlecturerList(ArrayList<lecturer> lecturerList) {
		this.lecturerList = lecturerList;
	}
	
	
	
	/**
	 * Getter for get the department id
	 * @return department number
	 * 	 */
	public int getdId() {
		return did;
	}
	

	
	/**
	 * Setter for set the department id
	 * @param id department id
	 */
	public void setdId(int did) {
		this.did = did;
	}

	/**
	 * Constructor to initialize all instance attributes
	 * @param id branch id
	 * @param name branch name
	 */
	public department(int did , String dName) {
		this.did = did;
		this.dName = dName;
	}
	
	@Override
	public String toString() {
		return dName;
	}
	
	
}
