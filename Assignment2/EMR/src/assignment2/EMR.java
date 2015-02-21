package assignment2;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import assignment2.Patient.Insurance;

/*
 * Assignment 2
 * Jeffrey Kirman (260493368) (Group 157)
 * 
 * LOG
 * 10/02/2015: Completed Doctor, Visit, sortDoctors (using bubblesort) classes.
 * 10/02/2015: Attempted importing classes.
 * 14/02/2015: Finished options 1 and 2, sortPatients (using merge sort).
 * 15/02/2015: Finished options 3 to 5 and associated methods. Problem with reading files.
 * 16/02/2015: Finished option 6. Fixed file reading issue. Various bug fixes.
 * 19/02/2015: Finished option 7.
 * 20/02/2015: Finished option 8 and 9.
 * 21/02/2015: Refined comments, enhanced UI and tested code for bugs.
 */

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
 *  	7.	Print a Patient's record
 *  	8.	Print a Doctor's record
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
		EMR system = new EMR("../Data/Doctors.csv", "../Data/Patients.csv", "../Data/Visits.csv");
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
		
		sortDoctors(this.doctorList);
		sortPatients(this.patientList);
		
		importVisitData(this.aVisitsFilePath);
		
	}

	/**
	 * This method should sort the doctorList in time O(n^2). It should sort the Doctors
	 * based on their ID 
	 */
	private void sortDoctors(ArrayList<Doctor> docs){
		
		boolean sorted = false;
		Doctor DocLeft;
		Doctor DocRight;
		
		while(!sorted) {
			sorted = true;
			
			// If ArrayList is null break out of the loop
			if (docs == null) {
				break;
			}
			
			// Bubblesort the elements of the Arraylist
			if (docs.size() > 1) {
				
				// Iterate through all adjacent pairs of IDs
				for (int i = 0; i < docs.size() - 1; i++) {
					DocLeft = docs.get(i);
					DocRight = docs.get(i+1);
					
					// If the left ID of a pair is greater than the right ID, swap them
					if (DocLeft.getID() > DocRight.getID()) {
						docs.set(i, DocRight);
						docs.set(i+1, DocLeft);
						sorted = false;
					}
				}
			}
		}
	}
	
	/**
	 * This method should sort the patientList in time O(n log n). It should sort the 
	 * patients based on the hospitalID
	 */
	private void sortPatients(ArrayList<Patient> patients){
		
		ArrayList<Patient> out = new ArrayList<>();
		
		out = mergeSortPatient(patients);
		
		// Clear the current patients array and add in all the elements of the sorted array
		patients.clear();
		patients.addAll(out);
		
	}
	
	/**
	 * Sorts an ArrayList of Patients by their ID using merge sort
	 * @param patients An ArrayList of Patients
	 * @return The original ArrayList of patients but sorted
	 */
	private static ArrayList<Patient> mergeSortPatient(ArrayList<Patient> patients) {
		
		ArrayList<Patient> out = new ArrayList<>();
		
		// For when the list of patients is 1 or less, it is already sorted so it can be returned as is
		if (patients.size() <= 1) {
			return patients;
		}
		
		// Initiate variables for left and right lists for the merge sort
		ArrayList<Patient> left = new ArrayList<>();
		ArrayList<Patient> right = new ArrayList<>();
		
		// Split the patients list into two
		for (int i = 0; i < patients.size()/2; i++) {
			left.add(patients.get(i));
		}
		
		for (int i = patients.size()/2; i < patients.size(); i++) {
			right.add(patients.get(i));
		}
		
		// Recursively merge sort the patients
		left = mergeSortPatient(left);
		right = mergeSortPatient(right);
		out = mergePatient(left, right);
		
		return out;
		
	}
	
	/**
	 * Merges two sorted Patient ArrayLists, sorting them in the process
	 * @param left A sorted Patient ArrayList
	 * @param right A sorted Patient ArrayList
	 * @return An ArrayList that contains all the elements of both the left and right ArrayLists, and is sorted
	 */
	private static ArrayList<Patient> mergePatient(ArrayList<Patient> left, ArrayList<Patient> right) {
		
		// Initialized variables
		ArrayList<Patient> out = new ArrayList<>();
		Long leftID;
		Long rightID;
		int lefti = 0;
		int righti = 0;
		
		for (int i = 0; i < left.size() + right.size(); i++) {
			
			// If all the elements of the left ArrayList are already in the merged ArrayList
			// successively add all the remaining elements in the right ArrayList into the 
			// merged ArrayList
			if (lefti >= left.size()) {
				out.add(right.get(righti));
				righti++;
			}
			
			// If all the elements of the right ArrayList are already in the merged ArrayList
			// successively add all the remaining elements in the left ArrayList into the 
			// merged ArrayList
			else if (righti >= right.size()) {
				out.add(left.get(lefti));
				lefti++;
			}
			
			else {
				
				// Add the lower valued ID at the current right and left index
				leftID = Long.parseLong(left.get(lefti).getHospitalID());
				rightID = Long.parseLong(right.get(righti).getHospitalID());	
				if (leftID <= rightID) {
					out.add(left.get(lefti));
					lefti++;
				}
				else {
					out.add(right.get(righti));
					righti++;
				}
			}
			
		}

		return out;
	}
	
	/**
	 * This method adds takes in the path of the Doctor sheet csv file and imports
	 * all doctors data into the doctorList ArrayList
	 */
	private ArrayList<Doctor> importDoctorsInfo(String doctorFilePath) {
		
		String fName;
		String lName;
		String specialty;
		String id;
		
		doctorList = new ArrayList<Doctor>();
		
		try {

			Scanner reader = new Scanner(new File(doctorFilePath));
			
			// Checks for commas as well as line breaks
			reader.useDelimiter(",|\r\n");
			
			// To disregard the headers
			for (int i = 0; i < 4; i++) {
				reader.next();
			}
			
			// Cycle through a full line of data and retrieve data in Strings
			// If necessary convert these strings into the appropriate data type
			// for the constructor of the Doctor class
			while (reader.hasNext()) {
				fName = reader.next();
				lName = reader.next();
				specialty = reader.next();
				id = reader.next();
				doctorList.add(new Doctor(fName, lName, specialty, Long.parseLong(id)));
			}
		}
		catch(FileNotFoundException a) {
		}
		return null;
	}
	
	/**
	 * This method adds takes in the path of the Patient sheet csv file and imports
	 * all Patient data into the patientList ArrayList
	 */
	private ArrayList<Patient> importPatientInfo(String patientFilePath){

		String fName;
		String lName;
		String height;
		String insuranceS;
		String gender;
		String hID;
		String DoB;
		Patient.Insurance insurance;
		
		patientList = new ArrayList<Patient>();
		
		try {
			Scanner reader = new Scanner(new File(patientFilePath));
			
			// Checks for commas as well as line breaks
			reader.useDelimiter(",|\r\n");
			
			// To disregard the headers
			for (int i = 0; i < 7; i++) {
				reader.next();
			}
			
			// Cycle through a full line of data and retrieve data in Strings
			// If necessary convert these strings into the appropriate data type
			// for the constructor of the Patient class
			while (reader.hasNext()) {
				fName = reader.next();
				lName = reader.next();
				height = reader.next();
				insuranceS = reader.next();
				gender = reader.next();
				hID = reader.next();
				DoB = reader.next();
				
				if (insuranceS.equals("RAMQ")) {
					insurance = Patient.Insurance.RAMQ;
				}
				else if (insuranceS.equals("Private")) {
					insurance = Patient.Insurance.Private;
				}
				else {
					insurance = Patient.Insurance.NONE;
				}
				
				patientList.add(new Patient(fName, lName, Double.parseDouble(height), gender, insurance, Long.parseLong(hID), DoB));
			}
		}
		catch(FileNotFoundException a) {}
		return null;
	}
	
	/**
	 * This method adds takes in the path of the Visit sheet csv file and imports
	 * every Visit data. It appends Visit objects to their respective Patient
	 */
	private void importVisitData(String visitsFilePath){
		
		String hID;
		String dID;
		String date;
		String note;
		
		try {
			Scanner reader = new Scanner(new File(visitsFilePath));
			
			// Checks for commas as well as line breaks
			reader.useDelimiter(",|\r\n");
			
			// To disregard headers
			for (int i = 0; i < 4; i++) {
				reader.next();
			}
			
			// Cycle through a full line of data and retrieve data in Strings
			while (reader.hasNext()) {
				hID = reader.next();
				dID = reader.next();
				date = reader.next();
				note = reader.next();
				
				// Find the Doctor and Patient object based on the IDs retrieved
				// from the CSV file and use them along with the other String
				// data in the constructor for each Visit
				for (int i = 0; i < patientList.size(); i++) {
					if (patientList.get(i).getHospitalID().equals(hID)) {
						patientList.get(i).aVisitList.add(new Visit(findDoctor(Long.parseLong(dID)), patientList.get(i), date, note));
						break;
					}
				}
			}
		}
		catch(FileNotFoundException a) {}

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
								"   (7) Print a Patient's record\n" +
								"   (8) Print a Doctor's record\n" +
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
					exit = true;
					break;	
				default:
					System.out.println("   *** ERROR: You entered an invalid input, please try again ***\n");
					break;
			}
		}
		scan.close();
	}
	
	/**
	 * This method adds a patient to the end of the patientList ArrayList. It 
	 * should ask the user to provide all the input to create a Patient object. The 
	 * user should not be able to enter empty values. The input should be supplied
	 * to the addPatient method
	 */
	private void option1(){

		String firstname = null;
		String lastname = null;
		double height = 0;
		String Gender = null;
		Patient.Insurance type = null;
		Long hospitalID = null;
		String DOB = null;
		Scanner scan = new Scanner(System.in);
		
		// Asks the user for the hospital ID of the patient, retrieves a String then
		// parses it into a Long
		System.out.print("Enter the patient's hospital ID (only decimal digits): ");
		String hID = null;
		boolean enteredHID = false;
			while (!enteredHID) {
			try {
				while (hID == null) {
					hID = scan.nextLine();
					hospitalID = Long.parseLong(hID);
					enteredHID = true;
				}
			}
			catch (NumberFormatException e) {
				System.out.print("Invalid number entered, please reenter the hospital ID: ");
				hID = null;
				enteredHID = false;
			}
		}

		// Asks the user for the last name of the patient, retrieves a String
		System.out.print("Enter the patient's last name: ");
		while (lastname == null) {
			lastname = scan.nextLine();
		}
		
		// Asks the user for the first name of the patient, retrieves a String
		System.out.print("Enter the patient's first name: ");
		while (firstname == null) {
			firstname = scan.nextLine();
		}
		
		// Asks the user for the height of the patient, retrieves a String then
		// parses it into a double
		System.out.print("Enter the patient's height (in cm): ");
		String hgt = null;
		boolean enteredHeight = false;
			while (!enteredHeight) {
			try {
				while (hgt == null) {
					hgt = scan.nextLine();
					height = Double.parseDouble(hgt);
					enteredHeight = true;
				}
			}
			catch (NumberFormatException e) {
				System.out.print("Invalid number entered, please reenter the height (in cm): ");
				hgt = null;
				enteredHeight = false;
			}
		}
		
		// Asks the user for the gender of the patient, retrieves a String
		System.out.print("Enter the patient's gender: ");
		while (Gender == null) {
			Gender = scan.nextLine();
		}
		
		// Asks the user for the date of birth of the patient, retrieves a String
		System.out.print("Enter the patient's date of birth (mm-dd-yyyy): ");
		while (DOB == null) {
			DOB = scan.nextLine();
		}
		
		// Asks the user for the Insurance type of the patient by giving 3 choices using a
		// switch case
		System.out.println("What is the patient's insurance type?");
		System.out.println("Please select one of the following options and click enter:");
		System.out.println("1. RAMQ\n" + "2. Private\n" + "3. None");
		System.out.print("Enter here: ");
		
		int choice = 0;
		String c = null;
	
		while ((choice != 1 && choice !=2 && choice !=3) || c == null) {
			
			try{
				c = scan.nextLine();
				choice = Integer.parseInt(c);
			}
			catch(Exception e){
				c = null;
				choice = 0;
			}
			switch(choice) {
			case 1:
				type = Patient.Insurance.RAMQ;
				break;
			case 2:
				type = Patient.Insurance.Private;
				break;
			case 3:
				type = Patient.Insurance.NONE;
				break;
			default:
				System.out.print("Invalid number entered, please reenter: ");
				break;
			}
		}
		
		addPatient(firstname, lastname, height, Gender, type, hospitalID, DOB);
	}
	
	/**
	 * This method adds a patient object to the end of the patientList ArrayList. 
	 */
	private void addPatient(String firstname, String lastname, double height, String Gender, Patient.Insurance type, Long hospitalID, String DOB){
		patientList.add(new Patient(firstname, lastname, height, Gender, type, hospitalID, DOB));
		return;
	}
	
	/**
	 * This method adds a doctor to the end of the doctorList ArrayList. It 
	 * should ask the user to provide all the input to create a Doctor object. The 
	 * user should not be able to enter empty values.
	 */
	private void option2(){

		String firstname = null;
		String lastname = null;
		String specialty = null;
		Long doctor_id = null;
		
		Scanner scan = new Scanner(System.in);
		
		// Asks for the last name of the doctor, retrieves a String
		System.out.print("Enter the doctor's last name: ");
		while (lastname == null) {
			lastname = scan.nextLine();
		}
		
		// Asks for the first name of the doctor, retrieves a String
		System.out.print("Enter the doctor's first name: ");
		while (firstname == null) {
			firstname = scan.nextLine();
		}
		
		// Asks for the specialty of the doctor, retrieves a String
		System.out.print("Enter the doctor's specialty: ");
		while (specialty == null) {
			specialty = scan.nextLine();
		}
		
		// Asks for the ID of a doctor, retrieves a String and then parses
		// it into a Long
		System.out.print("Enter the doctor's ID (only decimal digits): ");
		String ID = null;
		boolean enteredID = false;
			while (!enteredID) {
			try {
				while (ID == null) {
					ID = scan.nextLine();
					doctor_id = Long.parseLong(ID);
					enteredID = true;
				}
			}
			catch (NumberFormatException e) {
				System.out.print("Invalid number entered, please reenter the doctor's ID: ");
				ID = null;
				enteredID = false;
			}
		}
		
		addDoctor(firstname, lastname, specialty, doctor_id);
	}
	
	/**
	 * This method adds a doctor to the end of the doctorList ArrayList.
	 */
	private void addDoctor(String firstname, String lastname, String specialty, Long docID){
		doctorList.add(new Doctor(firstname, lastname, specialty, docID));
	}
	
	/**
	 * This method creates a Visit record. 
	 */
	private void option3(){

		Long doctorID = null;
		Long patientID = null;
		String date = null;
		
		//Use above variables to find which Doctor the patient saw
		Doctor d = null;
		Patient p = null;
		
		Scanner scan = new Scanner(System.in);
		
		// Entering the doctor's ID, retrieves String and then is parsed to a Long
		System.out.println("What is the doctor's ID (only decimal digits)?");
		String dID = null;
		boolean enteredDID = false;
			while (!enteredDID) {
			try {
				while (dID == null) {
					dID = scan.nextLine();
					doctorID = Long.parseLong(dID);
					enteredDID = true;
				}
			}
			catch (NumberFormatException e) {
				System.out.println("Invalid number entered, please reenter the doctor's ID.");
				dID = null;
				enteredDID = false;
			}
			
			// Uses the find doctor method to find the Doctor object associated with the ID
			// specified. If no such doctor exists, the visit entry is aborted.
			d = findDoctor(doctorID);
			if (d == null) {
				enteredDID = false;
				System.out.println("The ID entered does not exist, entry aborted.");
				return;
			}
		}
		
		// Entering the patient's ID, retrieves String and then is parsed to a Long
		System.out.println("What is the patient's ID (only decimal digits)?");
		String pID = null;
		boolean enteredPID = false;
			while (!enteredPID) {
			try {
				while (pID == null) {
					pID = scan.nextLine();
					patientID = Long.parseLong(pID);
					enteredPID = true;
				}
			}
			catch (NumberFormatException e) {
				System.out.println("Invalid number entered, please reenter the patient's ID.");
				pID = null;
				enteredPID = false;
			}
			
			// Find the patient with the specified ID, if no such doctor exists, the visit entry is aborted.
			for (int i = 0; i < patientList.size(); i++) {
				if (Long.parseLong(patientList.get(i).getHospitalID()) == patientID) {
					p = patientList.get(i);
				}
			}
			if (p == null) {
				enteredPID = false;
				System.out.println("The ID entered does not exist, entry aborted.");
				return;
			}
		}
		
		// Enter the date of the visit
		System.out.println("What is the date of the visit (mm-dd-yyyy)?");
		while (date == null) {
			date = scan.nextLine();
		}
		
		recordPatientVisit(d, p, date);
	}
	
	/**
	 * This method creates a Visit record. It adds the Visit to a Patient object.
	 */
	private void recordPatientVisit(Doctor doctor, Patient patient, String date){
		patient.addVisit(date, doctor);
	}
	
	/**
	 * This method edits a Patient record. Only the firstname, lastname, height,
	 * Insurance type, and date of birth could be changed. You should ask the user to supply the input.
	 */
	private void option4(){

		String newFirstname = null;
		String newLastname = null;
		double newHeight = 0;
		Insurance newType = null;
		String newDOB = null;
		Long ID = null;
		Patient p = null;
		Scanner scan = new Scanner(System.in);
		
		// Asks the user for the hospital ID of the patient, retrieves a String then
		// parses it into a Long
		System.out.print("Enter the patient's ID to be editted (only decimal digits: ?");
		String pID = null;
		boolean enteredPID = false;
			while (!enteredPID) {
			try {
				while (pID == null) {
					pID = scan.nextLine();
					ID = Long.parseLong(pID);
					enteredPID = true;
				}
			}
			catch (NumberFormatException e) {
				System.out.print("Invalid number entered, please reenter the patient's ID: ");
				pID = null;
				enteredPID = false;
			}
			
		
			// Find the patient with the specified ID, if no such doctor exists, the visit entry is aborted.
			for (int i = 0; i < patientList.size(); i++) {
				if (Long.parseLong(patientList.get(i).getHospitalID()) == ID) {
					p = patientList.get(i);
				}
			}
			if (p == null) {
				enteredPID = false;
				System.out.println("The ID entered does not exist, edit aborted.");
				return;
			}
		}

		// Asks the user for the last name of the patient, retrieves a String
		System.out.print("Enter the patient's new last name: ");
		while (newLastname == null) {
			newLastname = scan.nextLine();
		}
		
		// Asks the user for the first name of the patient, retrieves a String
		System.out.print("Enter the patient's new first name?");
		while (newFirstname == null) {
			newFirstname = scan.nextLine();
		}
		
		// Asks the user for the height of the patient, retrieves a String then
		// parses it into a double
		System.out.print("Enter the patient's new height (in cm): ");
		String hgt = null;
		boolean enteredHeight = false;
			while (!enteredHeight) {
			try {
				while (hgt == null) {
					hgt = scan.nextLine();
					newHeight = Double.parseDouble(hgt);
					enteredHeight = true;
				}
			}
			catch (NumberFormatException e) {
				System.out.print("Invalid number entered, please reenter the height (in cm): ");
				hgt = null;
				enteredHeight = false;
			}
		}
		
		// Asks the user for the date of birth of the patient, retrieves a String
		System.out.print("Enter the patient's date of birth (mm-dd-yyyy): ");
		while (newDOB == null) {
			newDOB = scan.nextLine();
		}
		
		// Asks the user for the Insurance type of the patient by giving 3 choices using a
		// switch case
		System.out.println("What is the patient's insurance type?");
		System.out.println("Please select one of the following options and click enter:");
		System.out.println("1. RAMQ\n" + "2. Private\n" + "3. None");
		System.out.print("Enter here: ");
		
		int choice = 0;
		String c = null;
	
		while ((choice != 1 && choice !=2 && choice !=3) || c == null) {
			
			try{
				c = scan.nextLine();
				choice = Integer.parseInt(c);
			}
			catch(Exception e){
				c = null;
				choice = 0;
			}
			switch(choice) {
			case 1:
				newType = Patient.Insurance.RAMQ;
				break;
			case 2:
				newType = Patient.Insurance.Private;
				break;
			case 3:
				newType = Patient.Insurance.NONE;
				break;
			default:
				System.out.print("Invalid number entered, please reenter: ");
				break;
			}
		}
		
		editPatient(newFirstname, newLastname, newHeight, newType, newDOB, ID);
	}
	
	/**
	 * This method edits a Patient record. Only the firstname, lastname, height, 
	 * Insurance type, address could be changed, and date of birth. 
	 */
	private void editPatient(String firstname, String lastname, double height, Insurance type, String DOB, Long ID){

		Patient p = null;
		
		// Find the patient with the specified ID
		for (int i = 0; i < patientList.size(); i++) {
			if (Long.parseLong(patientList.get(i).getHospitalID()) == ID) {
				p = patientList.get(i);
			}
		}
		
		// Change all the values of the specified patient
		if (p == null) {
			System.out.println("The ID entered does not exist, edit aborted.");
		}
		else {
			p.setFirstName(firstname);
			p.setLastName(lastname);
			p.setHeight(height);
			p.setInsurance(type);
			p.setDateOfBirth(DOB);
		}
		
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

		// Loops and prints out the values of all patients
		System.out.println("List of all patients:");
		System.out.println("Hospital ID" + ",\t" +
				"Last Name" + ",\t" +
				"First Name" + ",\t" +
				"Gender" + ",\t" +
				"Height" + ",\t" +
				"Date Of Birth" + ",\t" +
				"Insurance");
		System.out.println();
		for (int i = 0; i < patients.size(); i++) {
			System.out.println(patients.get(i).toString());
		}
		System.out.println();
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
		
		// Loops and prints out the values of all doctors
		System.out.println("List of all doctors:");
		System.out.println("ID" + ",\t" +
				"Last Name" + ",\t" +
				"First Name" + ",\t" +
				"Specialty");
		System.out.println();
		for (int i = 0; i < docs.size(); i++) {
			System.out.println(docs.get(i).toString());
		}
		System.out.println();
	}
	
	/**
	 * This method should ask the user to supply an id of the patient they want info about
	 */
	private void option7(){
		
		Long patientID = null;
		Scanner scan = new Scanner(System.in);

		// Asks the user for the hospital ID of the patient, retrieves a String then
		// parses it into a Long
		System.out.print("Enter the patient's ID (only decimal digits): ");
		String pID = null;
		boolean enteredPID = false;
		while (!enteredPID) {
			try {
				while (pID == null) {
					pID = scan.nextLine();
					patientID = Long.parseLong(pID);
					enteredPID = true;
				}
			}
			catch (NumberFormatException e) {
				System.out.print("Invalid number entered, please reenter the patient's ID: ");
				pID = null;
				enteredPID = false;
			}
		}

		printPatientRecord(patientID);

	}
	
	/**
	 * This method should call the toString method of a specific Patient. It should
	 * also list all the patient's Visit objects sorted in order by date (earliest first). For
	 * every Visit, the doctor's firstname, lastname and id should be printed as well.
	 */
	private void printPatientRecord(Long patientID){

		Patient currentPatient = null;
		Long currentID;
		
		// Find the patient with the given ID
		for (int i = 0; i < patientList.size(); i++) {
			currentID = Long.parseLong(patientList.get(i).getHospitalID());
			if (currentID.equals(patientID)) {
				currentPatient = patientList.get(i);
			}
		}
		
		// Returns nothing if the patient does not exist
		if (currentPatient == null) {
			System.out.println("This patient is not in the records.");
			System.out.println();
			return;
		}
		
		else {
			
			// Prints the values of the patient specified
			System.out.println("Patient:");
			System.out.println("Hospital ID" + ",\t" +
					"Last Name" + ",\t" +
					"First Name" + ",\t" +
					"Gender" + ",\t" +
					"Height" + ",\t" +
					"Date Of Birth" + ",\t" +
					"Insurance");
			System.out.println();
			System.out.println(currentPatient.toString());
			System.out.println();
			
			// If the patient has not visited any doctors, return
			if (currentPatient.aVisitList == null) {
				System.out.println("This patient has not visited any doctors.");
				System.out.println();
				return;
			}
			
			else {
				sortVisitList(currentPatient);
				
				// Loops through all the visits of the patient and prints the data
				System.out.println("Visits:");
				System.out.println("Doctor ID" + ",\t" +
						"Last Name" + ",\t" +
						"First Name" + ",\t" +
						"Visit Date" + ",\t" +
						"Note");
				System.out.println();
				for (int i = 0; i < currentPatient.aVisitList.size(); i++) {
					System.out.println(currentPatient.aVisitList.get(i).toString());
				}
				System.out.println();
			}
			
		}
		
	}

	/**
	 * Takes in a string that represents the date and returns a chronological value used for sorting.
	 * @param date String in format mm-dd-yyyy
	 * @return a chronological value used for sorting in format yyyymmdd
	 */
	private int toDayCount(String date) {
		
		int dayCount;
		
		int year = Integer.parseInt(date.substring(6, 10));
		int month = Integer.parseInt(date.substring(0, 2));
		int day = Integer.parseInt(date.substring(3, 5));
		
		dayCount = year*10000 + month*100 + day;
		
		return dayCount;
		
	}
	
	/**
	 * Sorts the visit list of a patient using bubble sort.
	 * @param p A Patient whose visit list should be sorted.
	 */
	private void sortVisitList(Patient p) {
		boolean sorted = false;
		Visit visitLeft;
		Visit visitRight;
		
		while(!sorted) {
			sorted = true;
			
			// If ArrayList is null break out of the loop
			if (p.aVisitList == null) {
				break;
			}
			
			// Bubble sort the elements of the ArrayList
			if (p.aVisitList.size() > 1) {
				for (int i = 0; i < p.aVisitList.size() - 1; i++) {
					visitLeft = p.aVisitList.get(i);
					visitRight = p.aVisitList.get(i+1);
					if (toDayCount(visitLeft.getDate()) > toDayCount(visitRight.getDate())) {
						p.aVisitList.set(i, visitRight);
						p.aVisitList.set(i+1, visitLeft);
						sorted = false;
					}
				}
			}
		}
	}
	
	/**
	 * This method should ask the user to supply an id of a doctor they want info about
	 */
	private void option8(){

		Long doc_id = null;
		
		Scanner scan = new Scanner(System.in);
		
		// Asks the user to enter the doctor's ID, retrieves a String and then
		// parses it into a Long
		System.out.print("Enter the doctor's ID (only decimal digits): ");
		String dID = null;
		boolean enteredDID = false;
		while (!enteredDID) {
			try {
				while (dID == null) {
					dID = scan.nextLine();
					doc_id = Long.parseLong(dID);
					enteredDID = true;
				}
			}
			catch (NumberFormatException e) {
				System.out.print("Invalid number entered, please reenter the doctor's ID: ");
				dID = null;
				enteredDID = false;
			}
		}
		
		// Finds the specified doctor and then prints its records
		Doctor d = findDoctor(doc_id);
		printDoctorRecord(d);
	}
	
	/**
	 * Searches in O(log n) time the doctorList to find the correct doctor with doctorID = id
	 * @param id The ID number of the doctor
	 * @return
	 */
	private Doctor findDoctor(Long id){
		
		int iterator; // Variable for the current position in the ArrayList 
		int action; // Variable to indicate what action should be taken at each iteration
		
		// If there are no doctors return nothing.
		if (doctorList == null) {
			return null;
		}
		
		// Begin with the iterator at the middle of the ArrayList
		iterator = doctorList.size()/2;
		
		// If the iterator ever points to a position outside the ArrayList this indicates
		// that there are no doctors with the specified id in the list, thus null is
		// returned
		while ((iterator >= 0) && (iterator < doctorList.size())) {
			
			/* Check if the ID is greater than, less than, or equal to the
			*  ID of the doctor which is pointed by the position of the
			*  iterator. If equal (action == 0) return the doctor at iterator,
			*  if less than (action < 0) halve the iterator, if greater than
			*  (action > 0) add half to the iterator.
			*/ 
			action = id.compareTo(doctorList.get(iterator).getID());
			
			if (action == 0) {
				return doctorList.get(iterator);
			}
			else if (action < 0) {
				iterator = iterator/2;
			}
			else {
				iterator = iterator + (iterator + 1)/2;
			}
		}
		
		return null;
	}
	
	/**
	 * This method should call the toString() method of a specific Doctor. It should
	 * also find and list all the patients that a Doctor has seen by calling their toString()
	 * method as well. It should also list the date that the doctor saw a particular patient
	 */
	private void printDoctorRecord(Doctor d){
		
		boolean patientSeen;
		
		// Prints out the information of the doctor
		System.out.println("Doctor:");
		System.out.println("ID" + ",\t" +
				"Last Name" + ",\t" +
				"First Name" + ",\t" +
				"Specialty");
		System.out.println(d.toString());
		System.out.println();

		// Cycles through each patient and checks to see if they have made any visits to the
		// specified doctor. If they have it prints out all the days they have visited the
		// specified doctor.
		if (patientList != null) {
			for (int i = 0; i < patientList.size(); i++) {
				patientSeen = false;
				if (patientList.get(i).aVisitList != null) {
					sortVisitList(patientList.get(i));
					for (int j = 0; j < patientList.get(i).aVisitList.size(); j++) {
						if (patientList.get(i).aVisitList.get(j).getDoctor().equals(d)) {
							if (!patientSeen) {
								System.out.println("Patient:");
								System.out.println("Hospital ID" + ",\t" +
										"Last Name" + ",\t" +
										"First Name" + ",\t" +
										"Gender" + ",\t" +
										"Height" + ",\t" +
										"Date Of Birth" + ",\t" +
										"Insurance");
								System.out.println(patientList.get(i).toString());
								System.out.println("Seen on:");
								System.out.println(patientList.get(i).aVisitList.get(j).getDate());
								patientSeen = true;
							}
							else {
								System.out.println(patientList.get(i).aVisitList.get(j).getDate());
							}
						}						
					}
				}
				
				if (patientSeen) { 
					System.out.println();
				}
				
			}
		}
		
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
		
		// Write doctor file
		try
		{
			
			// Deletes the old CSV file to make a new one with all the new data
			File docFile = new File(aDoctorFilePath);
			docFile.delete();
		    FileWriter writer = new FileWriter(aDoctorFilePath);
	 
		    // Header
		    writer.append("Firstname");
		    writer.append(',');
		    writer.append("Lastname");
		    writer.append(',');
		    writer.append("Specialty");
		    writer.append(',');
		    writer.append("DoctorID");
		    writer.append("\r\n");
	 
		    // Add all data in the doctorList ArrayList to doctor file
		    for (int i = 0; i < doctorList.size(); i++) {
			    writer.append(doctorList.get(i).getFirstName());
			    writer.append(',');
			    writer.append(doctorList.get(i).getLastName());
			    writer.append(',');
			    writer.append(doctorList.get(i).getSpecialty());
			    writer.append(',');
			    writer.append(doctorList.get(i).getID().toString());
			    writer.append("\r\n");
		    }
	  
		    writer.flush();

		}
		catch(IOException e)
		{
		     e.printStackTrace();
		}
		
		// Write patient file
		try
		{
			// Deletes the old CSV file to make a new one with all the new data
			File docFile = new File(aPatientFilePath);
			docFile.delete();
		    FileWriter writer = new FileWriter(aPatientFilePath);
		    
		    // Header
		    writer.append("FirstName");
		    writer.append(',');
		    writer.append("LastName");
		    writer.append(',');
		    writer.append("Hight (cm)");
		    writer.append(',');
		    writer.append("Insurance");
		    writer.append(',');
		    writer.append("Gender");
		    writer.append(',');
		    writer.append("HospitalID");
		    writer.append(',');
		    writer.append("Date of Birth (mm-dd-yyyy)");
		    writer.append("\r\n");
	 
		    // Add all data in the patientList ArrayList to patients file
		    for (int i = 0; i < patientList.size(); i++) {
			    writer.append(patientList.get(i).getFirstName());
			    writer.append(',');
			    writer.append(patientList.get(i).getLastName());
			    writer.append(',');
			    writer.append(patientList.get(i).getHeight());
			    writer.append(',');
			    writer.append(patientList.get(i).getInsurance());
			    writer.append(',');
			    writer.append(patientList.get(i).getGender());
			    writer.append(',');
			    writer.append(patientList.get(i).getHospitalID());
			    writer.append(',');
			    writer.append(patientList.get(i).getDateOfBirth());
			    writer.append("\r\n");
		    }
	  
		    writer.flush();

		}
		catch(IOException e)
		{
		     e.printStackTrace();
		}
		
		
		// Write visits file
		try
		{
			// Deletes the old CSV file to make a new one with all the new data
			File docFile = new File(aVisitsFilePath);
			docFile.delete();
		    FileWriter writer = new FileWriter(aVisitsFilePath);
		    
		    // Header
		    writer.append("HospitalID");
		    writer.append(',');
		    writer.append("DoctorID");
		    writer.append(',');
		    writer.append("Date");
		    writer.append(',');
		    writer.append("DoctorNote");
		    writer.append("\r\n");
	 
		    // Add all data from aVisitList from each Patient in the patientList ArrayList to visits file
		    for (int i = 0; i < patientList.size(); i++) {
		    	for (int j = 0; j < patientList.get(i).aVisitList.size(); j++) {
		    		writer.append(patientList.get(i).aVisitList.get(j).getPatient().getHospitalID());
		    		writer.append(',');
		    		writer.append(patientList.get(i).aVisitList.get(j).getDoctor().getID().toString());
		    		writer.append(',');
		    		writer.append(patientList.get(i).aVisitList.get(j).getDate());
		    		writer.append(',');
		    		writer.append(patientList.get(i).aVisitList.get(j).getNote());
		    		writer.append("\r\n");
		    	}
		    }
	  
		    writer.flush();

		}
		catch(IOException e)
		{
		     e.printStackTrace();
		}
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
			Long pHospitalID, String pDateOfBirth)
	{
		aFirstName = pFirstName;
		aLastName = pLastName;
		aHeight = pHeight;
		aGender = pGender;
		aInsurance = pInsurance;
		aHospitalID = pHospitalID;
		aDateOfBirth = pDateOfBirth;
		
		aVisitList = new ArrayList<>();
	}
	
	public String getFirstName()
	{
		return aFirstName;
	}
	
	public String getLastName()
	{
		return aLastName;
	}

	public String getHeight() {
		return Double.toString(aHeight);
	}
	
	public String getGender() {
		return aGender;
	}
	
	public String getInsurance() {
		return aInsurance.toString();
	}
	
	public String getHospitalID()
	{
		return aHospitalID.toString();
	}

	public String getDateOfBirth()
	{
		return aDateOfBirth;
	}

	public void addVisit(String vDate, Doctor vDoctor){
		aVisitList.add(new Visit(vDoctor, this, vDate, ""));
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
		
		// Concatenates a string using all the required information
		String output = this.getHospitalID() + ",\t" +
				this.getLastName() + ",\t" +
				this.getFirstName() + ",\t" +
				this.aGender + ",\t" +
				this.aHeight + ",\t" +
				this.getDateOfBirth() + ",\t" +
				this.aInsurance;
		return output;
		
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
		aFirstName = pFirstName;
		aLastName = pLastName;
		aSpecialty = pSpecialty;
		aID = ID;
	}
	
	public String getFirstName()
	{
		return aFirstName;
	}
	
	public String getLastName()
	{
		return aLastName;
	}

	public String getSpecialty(){
		return aSpecialty;
	}

	public Long getID(){
		return aID;
	}
	
	/**
	 * This method should print all the Doctor's info. "ID, Lastname, Firstname, Specialty"
	 */
	public String toString(){
		
		// Concatenates a String using all the required information
		String output = this.getID() + ",\t" +
				this.getLastName() + ",\t" +
				this.getFirstName() + ",\t" +
				this.getSpecialty();
		return output;
		
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
		aDoctor = vDoctor;
		aPatient = vPatient;
		aDate = vDate;
		anote = vNote;
	}
	
	public Doctor getDoctor()
	{
		return aDoctor;
	}
	
	public Patient getPatient()
	{
		return aPatient;
	}

	public String getDate(){
		return aDate;
	}
	
	public String getNote(){

		return anote;
	}

	public String toString() {
		
		// Concatenates a String using all the required information
		String output = this.getDoctor().getID() + ",\t" +
				this.getDoctor().getLastName() + ",\t" +
				this.getDoctor().getFirstName() + ",\t" +
				this.getDate() + ",\t" +
				this.getNote();
		return output;
		
	}

}