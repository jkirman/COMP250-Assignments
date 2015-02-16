package assignment2;

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
		this.patientList = sortPatients(this.patientList);
		
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
				for (int i = 0; i < docs.size() - 1; i++) {
					DocLeft = docs.get(i);
					DocRight = docs.get(i+1);
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
	private ArrayList<Patient> sortPatients(ArrayList<Patient> patients){ // Ask if this is ok
		
		ArrayList<Patient> out = new ArrayList<>();
		
		out = mergeSortPatient(patients);
		
		return out;
	}
	
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
		
		left = mergeSortPatient(left);
		right = mergeSortPatient(right);
		out = mergePatient(left, right);
		
		return out;
		
	}
	
	private static ArrayList<Patient> mergePatient(ArrayList<Patient> left, ArrayList<Patient> right) {
		
		ArrayList<Patient> out = new ArrayList<>();
		Long leftID;
		Long rightID;
		int lefti = 0;
		int righti = 0;
		
		for (int i = 0; i < left.size() + right.size(); i++) {
				
			if (lefti >= left.size()) {
				out.add(right.get(righti));
				righti++;
			}
			else if (righti >= right.size()) {
				out.add(left.get(lefti));
				lefti++;
			}
			else {
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
				
			reader.useDelimiter(",|\r\n");
			
			for (int i = 0; i < 4; i++) {
				reader.next();
			}
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
			reader.useDelimiter(",|\r\n");
			
			for (int i = 0; i < 7; i++) {
				reader.next();
			}
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
				
			reader.useDelimiter(",|\r\n");
			
			for (int i = 0; i < 4; i++) {
				reader.next();
			}
			while (reader.hasNext()) {
				hID = reader.next();
				dID = reader.next();
				date = reader.next();
				note = reader.next();
				
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
		
		
		//TODO: Fill code here
	
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
		
		System.out.println("What is the patient's hospital ID (only decimal digits)?");
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
				System.out.println("Invalid number entered, please reenter the hospital ID.");
				hID = null;
				enteredHID = false;
			}
		}

		System.out.println("What is the patient's last name?");
		while (lastname == null) {
			lastname = scan.nextLine();
		}
		
		System.out.println("What is the patient's first name?");
		while (firstname == null) {
			firstname = scan.nextLine();
		}
		
		System.out.println("What is the patient's height (in cm)?");
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
				System.out.println("Invalid number entered, please reenter the height (in cm).");
				hgt = null;
				enteredHeight = false;
			}
		}
			
		System.out.println("What is the patient's gender?");
		while (Gender == null) {
			Gender = scan.nextLine();
		}
		
		System.out.println("What is the patient's date of birth (mm-dd-yyyy)?");
		while (DOB == null) {
			DOB = scan.nextLine();
		}
		
		System.out.println("What is the patient's insurance type?");
		System.out.println("Please select one of the following options and click enter:");
		System.out.println("1. RAMQ\n" + "2. Private\n" + "3. None");
		
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
				System.out.println("Invalid number entered, please reenter.");
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
		//TODO: Fill code here. Ask the user to supply by command-line values for all the variables below.
		String firstname = null;
		String lastname = null;
		String specialty = null;
		Long doctor_id = null;
		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("What is the doctor's last name?");
		while (lastname == null) {
			lastname = scan.nextLine();
		}
		
		System.out.println("What is the doctor's first name?");
		while (firstname == null) {
			firstname = scan.nextLine();
		}
		
		System.out.println("What is the doctor's specialty?");
		while (specialty == null) {
			specialty = scan.nextLine();
		}
		
		System.out.println("What is the doctor's ID (only decimal digits)?");
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
				System.out.println("Invalid number entered, please reenter the doctor's ID.");
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
		
		Scanner scan = new Scanner(System.in);
		
		// Doctor's ID
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
			d = findDoctor(doctorID);
			if (d == null) {
				enteredDID = false;
				System.out.println("The ID entered does not exist, entry aborted.");
				return;
			}
		}
		
		// Patients ID
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
			
			// Find the patient with the specified ID
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
		
		// Date
		System.out.println("What is the date of the visit (mm-dd-yyyy)?");
		while (date == null) {
			date = scan.nextLine();
		}
		
		// Note
		System.out.println("A note about the visit:");
		while (note == null) {
			note = scan.nextLine();
		}
		
		recordPatientVisit(d, p, date, note);
	}
	
	/**
	 * This method creates a Visit record. It adds the Visit to a Patient object.
	 */
	private void recordPatientVisit(Doctor doctor, Patient patient, String date, String note){
		patient.addVisit(date, doctor, note);
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
		Long ID = null;
		Patient p = null;
		Scanner scan = new Scanner(System.in);
		
		System.out.println("What is the patient's ID (only decimal digits)?");
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
				System.out.println("Invalid number entered, please reenter the patient's ID.");
				pID = null;
				enteredPID = false;
			}
			
			// Find the patient with the specified ID
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

		System.out.println("What is the patient's last name?");
		while (newLastname == null) {
			newLastname = scan.nextLine();
		}
		
		System.out.println("What is the patient's first name?");
		while (newFirstname == null) {
			newFirstname = scan.nextLine();
		}
		
		System.out.println("What is the patient's height (in cm)?");
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
				System.out.println("Invalid number entered, please reenter the height (in cm).");
				hgt = null;
				enteredHeight = false;
			}
		}
		
		System.out.println("What is the patient's date of birth (mm-dd-yyyy)?");
		while (newDOB == null) {
			newDOB = scan.nextLine();
		}
		
		System.out.println("What is the patient's insurance type?");
		System.out.println("Please select one of the following options and click enter:");
		System.out.println("1. RAMQ\n" + "2. Private\n" + "3. None");
		
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
				System.out.println("Invalid number entered, please reenter.");
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
		//TODO: Fill code here
		Patient p = null;
		
		// Find the patient with the specified ID
		for (int i = 0; i < patientList.size(); i++) {
			if (Long.parseLong(patientList.get(i).getHospitalID()) == ID) {
				p = patientList.get(i);
			}
		}
		
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
		//TODO: Fill code here. Loop through all patients and call toString method
		System.out.println("Hospital ID" + ",\t" +
				"Last Name" + ",\t" +
				"First Name" + ",\t" +
				"Gender" + ",\t" +
				"Height" + ",\t" +
				"Date Of Birth" + ",\t" +
				"Insurance");
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
		System.out.println("ID" + ",\t" +
				"Last Name" + ",\t" +
				"First Name" + ",\t" +
				"Specialty");
		for (int i = 0; i < docs.size(); i++) {
			System.out.println(docs.get(i).toString());
		}
		System.out.println();
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
		
		int iterator;
		int action;
		
		if (doctorList == null) {
			return null;
		}
		
		iterator = doctorList.size()/2;
		
		while ((iterator >= 0) && (iterator < doctorList.size())) {
			
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

	public String getHospitalID()
	{
		return aHospitalID.toString();
	}

	public String getDateOfBirth()
	{
		return aDateOfBirth;
	}

	public void addVisit(String vDate, Doctor vDoctor, String note){
		aVisitList.add(new Visit(vDoctor, this, vDate, note));
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


}