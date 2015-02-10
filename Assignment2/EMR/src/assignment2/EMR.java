package assignment2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import assignment2.Patient.Insurance;

/* ACADEMIC INTEGRITY STATEMENT
 * 
 * By submitting this file, we state that all group members associated
 * with the assignment understand the meaning and consequences of cheating, 
 * plagiarism and other academic offenses under the Code of Student Conduct 
 * and Disciplinary Procedures (see www.mcgill.ca/students/srr for more information).
 * 
 * By submitting this assignment, we state that the members of the group
 * associated with this assignment claim exclusive credit as the authors of the
 * content of the file (except for the solution skeleton provided).
 * 
 * In particular, this means that no part of the solution originates from:
 * - anyone not in the assignment group
 * - Internet resources of any kind.
 * 
 * This assignment is subject to inspection by plagiarism detection software.
 * 
 * Evidence of plagiarism will be forwarded to the Faculty of Science's disciplinary
 * officer.
 */

/* A basic command line interface for an Electronic Medical Record System.
 * 
 * The simplest way to complete this assignment is to perform 1 functionality at a time. Start
 * with the code for the EMR constructor to import all data and then perform tasks 1-10
 * 		1.	Add a new patient to the EMR system
 *  	2.	Add a new Doctor to the EMR system
 *  	3.	Record new patient visit to the department
 *  	4.	Edit patient information
 *  	5.	Display list of all Patient IDs
 *  	6.	Display list of all Doctor IDs
 *  	7.	Print a Doctor's record
 *  	8.	Print a Patient's record
 *  	9.	Exit and save modifications
 * 	
 *	Complete the code provided as part of the assignment package. Fill in the \\TODO sections
 *  
 *  Do not change any of the function signatures. However, you can write additional helper functions 
 *  and test functions if you want.
 *  
 *  Do not define any new classes. Do not import any data structures. Do not call the sort functions
 *  of ArrayList class. Implement your own sorting functions and implement your own search function.
 *  
 *  Make sure your entire solution is in this file.
 *  
 *  We have simplified the task of reading the data from the Excel files. Instead of reading directly
 *  from Excel, each Sheet of the Excel file is saved as a comma separated file (csv) 
 * 
 */


public class EMR
{
	private String aDoctorFilePath;
	private String aPatientFilePath;
	private String aVisitsFilePath;
	private ArrayList<Doctor> doctorList;
	private ArrayList<Patient> patientList;
	
	/**
     * Used to invoke the EMR command line interface. You only need to change
     * the 3 filepaths.
	 */
	public static void main(String[] args) throws IOException
	{
		EMR system = new EMR("filepath1", "filepath2", "filepath3");
		system.displayMenu();
	}
	
	
	/**
	 * You don't have to modify the constructor, nor its code
	 * @param pDoctorFilePath
	 * @param pPatientFilePath
	 * @param pVisitsFilePath
	 */
	public EMR(String pDoctorFilePath, String pPatientFilePath, String pVisitsFilePath){
		this.aDoctorFilePath = pDoctorFilePath;
		this.aPatientFilePath = pPatientFilePath;
		this.aVisitsFilePath = pVisitsFilePath;
		
		importDoctorsInfo(this.aDoctorFilePath);
		importPatientInfo(this.aPatientFilePath);
		importVisitData(this.aVisitsFilePath);
		
		sortDoctors(this.doctorList);
		sortPatients(this.patientList);
	}

	/**
	 * This method should sort the doctorList in time O(n^2). It should sort the Doctors
	 * based on their ID 
	 */
	private void sortDoctors(ArrayList<Doctor> docs){
		//TODO: Fill code here
	}
	
	/**
	 * This method should sort the patientList in time O(n log n). It should sort the 
	 * patients based on the hospitalID
	 */
	private void sortPatients(ArrayList<Patient> patients){
		//TODO: Fill code here
	}
	
	/**
	 * This method adds takes in the path of the Doctor sheet csv file and imports
	 * all doctors data into the doctorList ArrayList
	 */
	private ArrayList<Doctor> importDoctorsInfo(String doctorFilePath){
		//TODO: Fill code here
		return null;
	}
	
	/**
	 * This method adds takes in the path of the Patient sheet csv file and imports
	 * all Patient data into the patientList ArrayList
	 */
	private ArrayList<Patient> importPatientInfo(String patientFilePath){
		//TODO: Fill code here
		return null;
	}
	
	/**
	 * This method adds takes in the path of the Visit sheet csv file and imports
	 * every Visit data. It appends Visit objects to their respective Patient
	 */
	private void importVisitData(String visitsFilePath){
		//TODO: Fill code here
	}
	
	/**
	 * This method uses an infinite loop to simulate the interface of the EMR system.
	 * A user should be able to select 10 options. The loop terminates when a user 
	 * chooses option 10: EXIT. You do not have to modify this code.
	 */
	public void displayMenu(){
		System.out.println();
		System.out.println("****************************************************************");
		System.out.println();
		System.out.println("Welcome to The Royal Victoria EMR Interface V1.0");
		System.out.println("");
		System.out.println("This system will allow you to access and modify the health records of the hospital");
		System.out.println();
		System.out.println("****************************************************************");
		System.out.println();
		
		Scanner scan = new Scanner(System.in);
		boolean exit = false;
		while(!exit){
			
			System.out.println("Please select one of the following options and click enter:");
			System.out.println("   (1) Add a new patient to the EMR system\n" +
								"   (2) Add a new Doctor to the EMR system\n" +
								"   (3) Record new patient visit to the department\n" +
								"   (4) Edit patient information\n" +
								"   (5) Display list of all Patient IDs\n" +
								"   (6) Display list of all Doctor IDs\n" +
								"   (7) Print a Doctor's record\n" +
								"   (8) Print a Patient's record\n" +
								"   (9) Exit and save modifications\n");
			System.out.print("   ENTER YOUR SELECTION HERE: ");
			
			int choice = 0;
			try{
				choice = Integer.parseInt(scan.next());
			}
			catch(Exception e){
				;
			}
			
			System.out.println("\n");
			
			switch(choice){
				case 1: 
					option1();
					break;
				case 2: 
					option2();
					break;
				case 3: 
					option3();
					break;
				case 4: 
					option4();
					break;
				case 5: 
					option5();
					break;
				case 6: 
					option6();
					break;
				case 7: 
					option7();
					break;
				case 8: 
					option8();
					break;
				case 9: 
					option9();
					break;	
				default:
					System.out.println("   *** ERROR: You entered an invalid input, please try again ***\n");
					break;
			}
		}
	}
	
	/**
	 * This method adds a patient to the end of the patientList ArrayList. It 
	 * should ask the user to provide all the input to create a Patient object. The 
	 * user should not be able to enter empty values. The input should be supplied
	 * to the addPatient method
	 */
	private void option1(){
		//TODO: Fill code here. Ask the user to supply by command-line values for all the variables below.
		String firstname = null;
		String lastname = null;
		double height = 0;
		String Gender = null;
		Insurance type = null;
		Long hospitalID = null;
		String DOB = null;
		
		addPatient(firstname, lastname, height, Gender, type, hospitalID, DOB);
	}
	
	/**
	 * This method adds a patient object to the end of the patientList ArrayList. 
	 */
	private void addPatient(String firstname, String lastname, double height, String Gender, Insurance type, Long hospitalID, String DOB){
		//TODO: Fill code here
		return;
	}
	
	
	/**
	 * This method adds a doctor to the end of the doctorList ArrayList. It 
	 * should ask the user to provide all the input to create a Doctor object. The 
	 * user should not be able to enter empty values.
	 */
	private void option2(){
		//TODO: Fill code here. Ask the user to supply by command-line values for all the variables below.
		String firstname = null;
		String lastname = null;
		String specialty = null;
		Long doctor_id = null;
		
		addDoctor(firstname, lastname, specialty, doctor_id);
	}
	
	/**
	 * This method adds a doctor to the end of the doctorList ArrayList.
	 */
	private void addDoctor(String firstname, String lastname, String specialty, Long docID){
		//TODO: Fill code here
	}
	
	/**
	 * This method creates a Visit record. 
	 */
	private void option3(){
		//TODO: Fill code here. Ask the user to supply by command-line values for all the variables below.
		Long doctorID = null;
		Long patientID = null;
		String date = null;
		String note = null;
		
		//Use above variables to find which Doctor the patient saw
		Doctor d = null;
		Patient p = null;
		
		recordPatientVisit(d, p, date, note);
	}
	
	/**
	 * This method creates a Visit record. It adds the Visit to a Patient object.
	 */
	private void recordPatientVisit(Doctor doctor, Patient patient, String date, String note){
		//TODO: Fill code here. Remember, the visit objects are stored within the Patient objects.
	}
	
	/**
	 * This method edits a Patient record. Only the firstname, lastname, height,
	 * Insurance type, and date of birth could be changed. You should ask the user to supply the input.
	 */
	private void option4(){
		//TODO: These are the 5 values that could change. You must ask the user to input new values 
		// for each of the 5 variables
		String newFirstname = null;
		String newLastname = null;
		double newHeight = 0;
		Insurance newType = null;
		String newDOB = null;
		
		editPatient(newFirstname, newLastname, newHeight, newType, newDOB);
	}
	
	/**
	 * This method edits a Patient record. Only the firstname, lastname, height, 
	 * Insurance type, address could be changed, and date of birth. 
	 */
	private void editPatient(String firstname, String lastname, double height, Insurance type, String DOB){
		//TODO: Fill code here
	}
	
	/**
	 * This method should first sort the patientList and then print to screen 
	 * one Patient at a time by calling the displayPatients() method
	 */
	private void option5(){
		sortPatients(this.patientList);
		displayPatients(this.patientList);
	}
	
	/**
	 * This method should print to screen 
	 * one Patient at a time by calling the Patient toString() method
	 */
	private void displayPatients(ArrayList<Patient> patients){
		//TODO: Fill code here. Loop through all patients and call toString method
	}
	
	/**
	 * This method should first sort the doctorList and then print to screen 
	 * one Doctor at a time by calling the displayDoctors() method
	 */
	private void option6(){
		sortDoctors(this.doctorList);
		displayDoctors(this.doctorList);
	}

	/**
	 * This method should first sort the doctorList and then print to screen 
	 * one Doctor at a time by calling the Doctor toString() method
	 */
	private void displayDoctors(ArrayList<Doctor> docs){
		//TODO: Fill code here
	}

	
	/**
	 * This method should ask the user to supply an id of the patient they want info about
	 */
	private void option7(){
		//TODO: ask the user to specify the id of the patient
		Long patientID = null;
		
		printPatientRecord(patientID);
		
	}
	
	/**
	 * This method should call the toString method of a specific Patient. It should
	 * also list all the patient's Visit objects sorted in order by date (earliest first). For
	 * every Visit, the doctor's firstname, lastname and id should be printed as well.
	 */
	private void printPatientRecord(Long patientID){
		//TODO: Fill code here
	}
	
	/**
	 * This method should ask the user to supply an id of a doctor they want info about
	 */
	private void option8(){
		//TODO: ask the user to specify the id of the doctor
		Long doc_id = null;
		
		Doctor d = findDoctor(doc_id);
		printDoctorRecord(d);
	}
	
	/**
	 * Searches in O(log n) time the doctorList to find the correct doctor with doctorID = id
	 * @param id
	 * @return
	 */
	private Doctor findDoctor(Long id){
		//TODO: Fill code here
		return null;
	}
	
	/**
	 * This method should call the toString() method of a specific Doctor. It should
	 * also find and list all the patients that a Doctor has seen by calling their toString()
	 * method as well. It should also list the date that the doctor saw a particular patient
	 */
	private void printDoctorRecord(Doctor d){
		//TODO: Fill code here
	}
	
	/**
	 * This method should be invoked from the command line interface if the user
	 * would like to quit the program. This method should export all the Doctor, Patient and 
	 * Visit data by overwriting the contents of the 3 original files.
	 */
	private void option9(){
		exitAndSave();
	}
	
	
	/**
	 * Export all the Doctor, Patient and Visit data by overwriting the contents of the 3 original csv files.
	 */
	private void exitAndSave(){
		//TODO: Fill code here
	}

	
}

/**
 * This simple class just keeps the information about
 * a Patient together. You will have to Modify this class
 * and fill in missing data.
 */
class Patient
{
	public enum Insurance {RAMQ, Private, NONE};
	
	private String aFirstName;
	private String aLastName;
	private double aHeight;
	private String aGender;
	private Insurance aInsurance;
	private Long aHospitalID;
	private String aDateOfBirth; //ex. 12-31-1988 (Dec. 31st, 1988)
	ArrayList<Visit> aVisitList;
	
	public Patient(String pFirstName, String pLastName, double pHeight, String pGender, Insurance pInsurance,
			Long pHostpitalID, String pDateOfBirth)
	{
		//TODO: Fill code here
	}
	
	public String getFirstName()
	{
		//TODO: Fill code here
		return null;
	}
	
	public String getLastName()
	{
		//TODO: Fill code here
		return null;
	}

	public String getHospitalID()
	{
		//TODO: Fill code here
		return null;
	}

	public String getDateOfBirth()
	{
		//TODO: Fill code here
		return null;
	}

	public void addVisit(String vDate, Doctor vDoctor){
		//TODO: Fill code here
	}
	
	public void setFirstName(String fname){
		this.aFirstName = fname;
	}
	
	public void setLastName(String lname){
		this.aLastName = lname;
	}
	
	public void setHeight(double height){
		this.aHeight = height;
	}
	
	public void setInsurance(Insurance type){
		this.aInsurance = type;
	}
	
	public void setDateOfBirth(String dob){
		this.aDateOfBirth = dob;
	}
	
	/**
	 * This method should print all the Patient's info. "ID, Lastname, Firstname, etc..."
	 */
	public String toString(){
		//TODO: Fill code here
		return null;
	}
}

/**
 * This simple class just keeps the information about
 * a Doctor together. Do modify this class as needed.
 */
class Doctor
{
	private String aFirstName;
	private String aLastName;
	private String aSpecialty; 
	private Long aID;
	
	public Doctor(String pFirstName, String pLastName, String pSpecialty, Long ID)
	{
		//TODO: Fill code here
	}
	
	public String getFirstName()
	{
		//TODO: Fill code here
		return aFirstName;
	}
	
	public String getLastName()
	{
		//TODO: Fill code here
		return null;
	}

	public String getSpecialty(){
		//TODO: Fill code here
		return null;
	}

	public Long getID(){
		//TODO: Fill code here
		return null;
	}
	
	/**
	 * This method should print all the Doctor's info. "ID, Lastname, Firstname, Specialty"
	 */
	public String toString(){
		//TODO: Fill code here
		return null;
	}

}

/**
 * This simple class just keeps the information about
 * a Visit together. Do modify this class as needed.
 */
class Visit
{
	private Doctor aDoctor;
	private Patient aPatient;
	private String aDate; 
	private String anote;
	
	public Visit(Doctor vDoctor, Patient vPatient, String vDate, String vNote)
	{
		//TODO: Fill code here
	}
	
	public Doctor getDoctor()
	{
		//TODO: Fill code here
		return null;
	}
	
	public Patient getPatient()
	{
		//TODO: Fill code here
		return null;
	}

	public String getDate(){
		//TODO: Fill code here
		return null;
	}
	
	public String getNote(){
		//TODO: Fill code here
		return null;
	}


}