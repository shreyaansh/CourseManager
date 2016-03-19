import java.util.*;
import java.util.Date;
import java.sql.*;
import java.text.*;

public class CourseManager {
 Connection con;
 
 Scanner scan = new Scanner(System.in);
 
 public CourseManager() {
  try {
   Class.forName( "oracle.jdbc.driver.OracleDriver" );
  }
  catch ( ClassNotFoundException e ) {
   e.printStackTrace();
  }
  try {
   con = DriverManager.getConnection( "jdbc:oracle:thin:@claros.cs.purdue.edu:1524:strep","sbassi", "gC23HoaX" );
  }
  catch ( SQLException e ){
   e.printStackTrace();
  }
 }

 public boolean checkIfStudentExistsInCourse (int sid, int cid) {
	 String query = "Select * from Enrollment where studentID = " + sid + " and courseID = " + cid;
	 try {
		 Statement stmt = con.createStatement();
		 ResultSet rs = stmt.executeQuery(query);
		 while (rs.next()) {
			 return true;
		 }
	 } catch ( SQLException e ) {
		 e.printStackTrace();
	 }
	 return false;
 }
 
 public boolean checkIfSIDExists(int id) {
	 String query = "Select * from Students where studentID = " + id;
	 try {
		 Statement stmt = con.createStatement();
		 ResultSet rs = stmt.executeQuery(query);
		 while (rs.next()) {
			 int checkID = rs.getInt("studentID");
			 System.out.printf("Student Name = %s\n", rs.getString("studentName"));
			 if (checkID == id) return true;
			 else return false;
		 }
	 } catch ( SQLException e ) {
		 e.printStackTrace();
	 }
	 return false;
 }
 
 public boolean checkIfCIDExists(int id) {
	 String query = "Select * from Courses where courseID = " + id;
	 try {
		 Statement stmt = con.createStatement();
		 ResultSet rs = stmt.executeQuery(query);
		 while (rs.next()) {
			 int checkID = rs.getInt("courseID");
			 System.out.printf("Course Name = %s\n", rs.getString("courseName"));
			 if (checkID == id) return true;
			 else return false;
		 }
	 } catch ( SQLException e ) {
		 e.printStackTrace();
	 }
	 return false;
 }
 
 public boolean checkIfFIDExists(int id) {
	 String query = "Select * from Faculty where facultyID = " + id;
	 try {
		 Statement stmt = con.createStatement();
		 ResultSet rs = stmt.executeQuery(query);
		 while (rs.next()) {
			 int checkID = rs.getInt("facultyID");
			 System.out.printf("Faculty Name = %s\n", rs.getString("facultyName"));
			 if (checkID == id) return true;
			 else return false;
		 }
	 } catch ( SQLException e ) {
		 e.printStackTrace();
	 }
	 return false;
 }
 
 public ArrayList<Integer> arr = new ArrayList<Integer>();
 
 public boolean checkIfEIDExists(int id) {
	 String query = "Select * from CourseEvaluations where evaluationID = " + id;
	 
	 try {
		 Statement stmt = con.createStatement();
		 ResultSet rs = stmt.executeQuery(query);
		 while (rs.next()) {
			 int checkID = rs.getInt("evaluationID");
			 System.out.printf("Evaluation Name = %s\n", rs.getString("evaluationName"));
			 if (checkID == id) return true;
			 else return false;
		 }
	 } catch ( SQLException e ) {
		 e.printStackTrace();
	 }
	 return false;
 }
 
 public void printEnrolledCourses(int id) {
	 String query = "select cs.courseName from Courses cs join Enrollment en on en.courseID = cs.courseID where en.studentID = " + id;
	 try {
		 Statement stmt = con.createStatement();
		 ResultSet rs = stmt.executeQuery(query);
		 
		 int i = 1;
		 
		 while (rs.next()) {
			 System.out.printf("Course %d: %s\n", i, rs.getString("courseName"));
			 i++;
		 }
		 
		 System.out.println();
		 
	 } catch (SQLException e) {
		 e.printStackTrace();
	 }
 }
 
 public void printDepartmentInfo() {
	 String query = "select * from Departments";
	 try {
		 Statement stmt = con.createStatement();
		 ResultSet rs = stmt.executeQuery(query);
		 
		 while (rs.next()) {
			 System.out.printf("Department ID: %d\tDepartment Name: %-20s\tDepartment Head: %-20s\n", rs.getInt("departmentID"), rs.getString("name"), rs.getString("headName"));
		 }
		 
		 System.out.println();
		 
	 } catch (SQLException e) {
		 e.printStackTrace();
	 }
 }
 
 public void printFacultyInfo() {
	 String query = "select fn.facultyID, fn.facultyName, db.name from Faculty fn join Departments db on db.departmentID = fn.departmentID";
	 try {
		 Statement stmt = con.createStatement();
		 ResultSet rs = stmt.executeQuery(query);
		 
		 while (rs.next()) {
			 System.out.printf("Faculty ID: %d\tFaculty Name: %-20s\t Faculty Department: %-20s\n", rs.getInt("facultyID"), rs.getString("facultyName"), rs.getString("name"));
		 }
		 
		 System.out.println();
		 
	 } catch (SQLException e) {
		 e.printStackTrace();
	 }
 }
 
 public int getLastCourseID() {
	 String query = "select * from (select courseID from Courses order by courseID desc) where rownum = 1";
	 try {
		 Statement stmt = con.createStatement();
		 ResultSet rs = stmt.executeQuery(query);
		 while (rs.next()) {
		 	return rs.getInt("courseID");
		 }
	 } catch (SQLException e) {
		 e.printStackTrace();
	 }
	 return 0;
 }
 
 public int getLastEvaluationID() {
	 String query = "select * from (select evaluationID from CourseEvaluations order by evaluationID desc) where rownum = 1";
	 try {
		 Statement stmt = con.createStatement();
		 ResultSet rs = stmt.executeQuery(query);
		 while (rs.next()) {
		 	return rs.getInt("evaluationID");
		 }
	 } catch (SQLException e) {
		 e.printStackTrace();
	 }
	 return 0;
 }
 
 public void createCourse() {
	 
	 int id = getLastCourseID();
	 
	 System.out.printf("Last Course ID: %d\n", id);
	 
	 int courseID = ++id;
	 
	 System.out.print("Enter Course Name: ");
	 String courseName = scan.nextLine();
	 
	 String semester = "default";
	 while (true) {
		 System.out.printf("Choose Semester:\n1. Fall\n2. Spring\n3. Summer\n");
		 int semesterInt = 0;
		 while (true) {
			 try {
				 System.out.print("Semester: ");
				 semesterInt = scan.nextInt();
				 break;
			 } catch (InputMismatchException e) {
				 scan.nextLine();
				 System.out.println("Enter a number!");
			 }
		 }
		 switch(semesterInt) {
		 case 1: semester = "Fall"; break;
		 case 2: semester = "Spring"; break;
		 case 3: semester = "Summer"; break;
		 default: System.out.println("Enter a vaild number!");
		 }
		 if (!semester.equals("default")) break;
	 }
	 
	 int year = Calendar.getInstance().get(Calendar.YEAR);
	 
	 while (true) {
		 int yearInput = 0;
		 while (true) {
			 try {
				 System.out.print("Enter a year (Hint: Should be greater than or equal to the current year): ");
				 yearInput = scan.nextInt();
				 break;
			 } catch (InputMismatchException e) {
				 scan.nextLine();
				 System.out.println("Enter a number!");
			 }
		 }
		 if (yearInput < year || yearInput > 2100) {
			 System.out.println("Enter a valid year between the current year and 2100AD");
		 } else {
			 year = yearInput;
			 break;
		 }
	 }
	 
	 scan.nextLine();
	 
	 System.out.print("Enter Class Time: ");
	 String meetsAt = scan.nextLine();
	 
	 System.out.print("Enter Room Number: ");
	 String room = scan.nextLine();
	 
	 int facultyID = 0;
	 while (true) {
		 while (true) {
			 try {
				 System.out.print("Enter a faculty ID for the professor teching the course: ");
				 facultyID = scan.nextInt();
				 break;
			 } catch (InputMismatchException e) {
				 scan.nextLine();
				 System.out.println("Enter a number!");
			 }
		 }
		 
		 
		 if (checkIfFIDExists(facultyID))
			 break;
		 else
			 System.out.println("Enter a valid Faculty ID");
	 }
	 
	 System.out.println();
	 
	 String update = "Insert into Courses (courseID, courseName, semester, year, meetsAt, room, facultyID) values (" 
	 + courseID + ", '" + courseName + "', '" + semester + "', " + year + ", '" + meetsAt + "', '" + room + "', " + facultyID + ")";
	 
	 try {
		 Statement stmt = con.createStatement();
		 stmt.executeUpdate(update);
		 stmt.close();
		 System.out.println("-----Successfully Created Course-----");
	 } catch (SQLException e) {
		 e.printStackTrace();
	 }
 }
 
 public void printAvailableCourses(int id) {
	 String query = "select * from Courses where facultyID = " + id;
	 
	 try {
		 Statement stmt = con.createStatement();
		 ResultSet rs = stmt.executeQuery(query);
		 System.out.printf("CourseID: \tCourse Name: \n");
		 System.out.println("-----------------------------");
		 while (rs.next()) {
			 System.out.printf("%-20d%-20s\n", rs.getInt("courseID"), rs.getString("courseName"));
		 }
	 } catch (SQLException e) {
		 e.printStackTrace();
	 }
 }
 
 public void printAvailableEvaluations(int cid) {
	 String query = "select * from CourseEvaluations where courseID = " + cid;
	 
	 try {
		 Statement stmt = con.createStatement();
		 ResultSet rs = stmt.executeQuery(query);
		 System.out.printf("\nEvaluation ID: \tCourse ID: \tEvaluation Name \n");
		 System.out.println("--------------------------------------------------");
		 while (rs.next()) {
			 System.out.printf("%-20d%-20d%-20s\n\n", rs.getInt("evaluationID"), rs.getInt("courseID"), rs.getString("evaluationName"));
			 arr.add(rs.getInt("evaluationID"));
		 }
	 } catch (SQLException e) {
		 e.printStackTrace();
	 }
 }
 
 public void deleteCourse(int cid, int fid) {
	 String update = "Delete from Courses where courseID = " + cid + " and facultyID = " + fid;
	 try {
		 Statement stmt = con.createStatement();
		 stmt.executeUpdate(update);
		 stmt.close();
		 System.out.println("-----Successfully Deleted Course-----");
	 } catch (SQLException e) {
		 e.printStackTrace();
	 }
 }
 
 public void modifyCourse(int id) {
	 CourseManager cm = new CourseManager();
	 
	 int courseID = 0;
	 
	 System.out.println("Here is the list of the Courses you can modify/delete:");
	 System.out.println();
	 
	 cm.printAvailableCourses(id);
	 
	 System.out.print("\nWarning: Entering a courseID not taught by you will result \nin that course not getting updated/deleted!\n");
	 System.out.println();
	 
	 while (true) {
		 try {
			 System.out.print("Enter the Course ID of the course that you want to modify: ");
			 courseID = scan.nextInt();
			 if (!checkIfCIDExists(courseID)) {
				 throw new InputMismatchException();
			 }
			 break;
		 } catch (InputMismatchException e) {
			 scan.nextLine();
			 System.out.println("Enter a valid course number!");
		 }
	 }
	 
	 scan.nextLine();
	 
	//Delete course
	 String quit = "no";
	 while (true) {
		System.out.print("Do you want to delete this Course (Y/N) ?");
		quit = scan.nextLine();
		
		if (quit.toLowerCase().equals("yes") || quit.toLowerCase().equals("y")) {
			cm.deleteCourse(courseID, id);
			return;
		} else if (quit.toLowerCase().equals("no") || quit.toLowerCase().equals("n")) {
			break;
		} else {
			System.out.println("Enter in the correct format");
		}
	 }
	 
	 System.out.println("\nEdit the fields you want to modify or enter the old value if you don't want to change the values\n");
	 
	 System.out.print("Enter Course Name: ");
	 String courseName = scan.nextLine();
	 
	 String semester = "default";
	 while (true) {
		 System.out.printf("Choose Semester:\n1. Fall\n2. Spring\n3. Summer\n");
		 int semesterInt = 0;
		 while (true) {
			 try {
				 System.out.print("Semester: ");
				 semesterInt = scan.nextInt();
				 break;
			 } catch (InputMismatchException e) {
				 scan.nextLine();
				 System.out.println("Enter a number!");
			 }
		 }
		 switch(semesterInt) {
		 case 1: semester = "Fall"; break;
		 case 2: semester = "Spring"; break;
		 case 3: semester = "Summer"; break;
		 default: System.out.println("Enter a vaild number!");
		 }
		 if (!semester.equals("default")) break;
	 }
	 
	 int year = Calendar.getInstance().get(Calendar.YEAR);
	 
	 while (true) {
		 int yearInput = 0;
		 while (true) {
			 try {
				 System.out.print("Enter a year (Hint: Should be greater than or equal to the current year): ");
				 yearInput = scan.nextInt();
				 break;
			 } catch (InputMismatchException e) {
				 scan.nextLine();
				 System.out.println("Enter a number!");
			 }
		 }
		 if (yearInput < year || yearInput > 2100) {
			 System.out.println("Enter a valid year between the current year and 2100AD");
		 } else {
			 year = yearInput;
			 break;
		 }
	 }
	 
	 scan.nextLine();
	 
	 System.out.print("Enter Class Time: ");
	 String meetsAt = scan.nextLine();
	 
	 System.out.print("Enter Room Number: ");
	 String room = scan.nextLine();
	 
	 int facultyID = 0;
	 while (true) {
		 while (true) {
			 try {
				 System.out.print("Enter a faculty ID for the professor teching the course: ");
				 facultyID = scan.nextInt();
				 break;
			 } catch (InputMismatchException e) {
				 scan.nextLine();
				 System.out.println("Enter a number!");
			 }
		 }
		 
		 
		 if (checkIfFIDExists(facultyID))
			 break;
		 else
			 System.out.println("Enter a valid Faculty ID");
	 }
	 
	 System.out.println();
	 //update Courses set courseName = 'STAT 114', semester = 'Summer', year = '2015', meetsAt = '11:30 PM', room = 'ME 472', facultyID = 16 where courseID = 18 and facultyID = 14;
	 String update = "Update Courses set courseName = '" + courseName + "', semester = '" + semester + "', year = " + year + ", meetsAt = '"
	 + meetsAt + "', room = '" + room + "', facultyID = " + facultyID + " where courseID = " + courseID + " and facultyID = " + id;
	 
	 //System.out.println(update);
	 
	 try {
		 Statement stmt = con.createStatement();
		 stmt.executeUpdate(update);
		 stmt.close();
		 System.out.println("-----Sucessfully Modified Course-----");
	 } catch (SQLException e) {
		 e.printStackTrace();
	 }
 }
 
 public void assignCourses(int fid) {
	 int sid = 0;
	 while (true) {
		 try {
			 System.out.print("\nSelect Student ID of the student you want to add to your course: ");
			 sid = scan.nextInt();
			 if (!checkIfSIDExists(sid)) {
				 System.out.println("\nEnter a valid StudentID!");
				 return;
			 }
			 break;
		 } catch (InputMismatchException e) {
			 scan.nextLine();
			 System.out.println("\nEnter a number!");
		 }
	 }
	 
	 scan.nextLine();
	 
	 ArrayList<Integer> list = new ArrayList<Integer>();
	 
	 String query = "Select * from Courses where facultyID = " + fid;
	 
	 try {
		 Statement stmt = con.createStatement();
		 ResultSet rs = stmt.executeQuery(query);
		 while (rs.next()) {
			 list.add(rs.getInt("courseID"));
			 //System.out.println(rs.getInt("courseID"));
		 }
	 } catch (SQLException e) {
		 e.printStackTrace();
	 }
	 
	 int cid = 0;
	 while (true) {
		try {
			System.out.print("\nEnter the Course ID of the course you want to add the student to: ");
			cid = scan.nextInt();
			if (!checkIfCIDExists(cid)) {
				System.out.println("\nEnter a valid Course ID!");
				throw new InputMismatchException();
			}
			if (!list.contains(cid)) {
				System.out.println("\nEnter a Course ID that of a course that you teach!");
				throw new InputMismatchException();
			}
			break;
		} catch (InputMismatchException e) {
			scan.nextLine();
			System.out.println("Enter a number!");
		}
	}
	 
	 String update = "Insert into Enrollment (courseID, studentID) values ("+ cid + ", " + sid +")";
	 
	 try {
		 Statement stmt = con.createStatement();
		 stmt.executeUpdate(update);
		 stmt.close();
	 } catch (SQLException e) {
		 e.printStackTrace();
	 }
 }
 
 public void createEvaluation(int fid) {
	 
	 ArrayList<Integer> list = new ArrayList<Integer>();
	 
	 String query = "Select * from Courses where facultyID = " + fid;
	 
	 try {
		 Statement stmt = con.createStatement();
		 ResultSet rs = stmt.executeQuery(query);
		 while (rs.next()) {
			 list.add(rs.getInt("courseID"));
			 //System.out.println(rs.getInt("courseID"));
		 }
	 } catch (SQLException e) {
		 e.printStackTrace();
	 }
	 
	 int cid = 0;
	 
	 while (true) {
		 try {
			 System.out.print("Enter the Course ID of the course you want to create an evaluation for: ");
			 cid = scan.nextInt();
			 if (!checkIfCIDExists(cid)) {
				 System.out.println("\nEnter a valid Course ID!");
				 throw new InputMismatchException();
			 }
			 if (!list.contains(cid)) {
					System.out.println("\nEnter a Course ID that of a course that you teach!");
					throw new InputMismatchException();
			 }
			 break;
		 } catch (InputMismatchException e) {
			 scan.nextLine();
			 System.out.println("\nEnter a number!");
		 }
	 }
	 
	 scan.nextLine();
	 
	 int eid = getLastEvaluationID();
	 int evaluationID = ++eid;
	 
	 int courseID = cid;
	 
	 System.out.print("Enter the name of the evaluation: ");
	 String evaluationName = scan.nextLine();
	 
	 
	 System.out.println("\nSelect evaluation type:\n1. H.W.\n2. Midterm\n3. Final Exam\n4. Project");
	 int selection = 0;
	 while (true) {
		 try {
			 System.out.print("\nYour Selection: ");
			 selection = scan.nextInt();
			 if (selection <= 0 || selection >= 5) {
				 System.out.println("\nPrint a vaild value!");
				 throw new InputMismatchException();
			 }
			 break;
		 } catch (InputMismatchException e) {
			 scan.nextLine();
			 System.out.println("\nEnter a number!");
		 }
	 }
	 
	 scan.nextLine();
	 
	 Double weightage = 0.0;
	 while (true) {
		 try {
			 System.out.print("\nEnter a number between 0.00 and 1.00: ");
			 weightage = scan.nextDouble();
			 if (weightage <= 0.00 || weightage > 1.00) {
				 System.out.println("\nEnter a number BETWEEN 0.00 and 1.00");
				 throw new InputMismatchException();
			 }
			 break;
		 } catch (InputMismatchException e) {
			 scan.nextLine();
			 System.out.print("\nEnter a number!\n");
		 }
	 }
	 
	 int currentYear = Calendar.getInstance().get(Calendar.YEAR);
	 int year = 0;
	 while (true) {
		 try {
			 System.out.print("\nEnter a year (between now and 2100): ");
			 year = scan.nextInt();
			 if (year < currentYear || year > 2100) {
				 System.out.println("\nEnter a valid year!");
				 throw new InputMismatchException();
			 }
			 break;
		 } catch (InputMismatchException e) {
			 scan.nextLine();
			 System.out.println("\nEnter a number!");
		 }
	 }
	 
	 int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
	 int month = 0;
	 while (true) {
		 try {
			 System.out.print("\nEnter a month (between 1 and 12): ");
			 month = scan.nextInt();
			 if (month < 1 || month > 12) {
				 System.out.println("\nEnter a valid month!");
				 throw new InputMismatchException();
			 }
			 if (year == currentYear &&  month < currentMonth) {
				 System.out.println("\nEnter a valid month (any month that this or anything after this month)!");
				 throw new InputMismatchException();
			 }
			 break;
		 } catch (InputMismatchException e) {
			 scan.nextLine();
			 System.out.println("\nEnter a number!");
		 }
	 }
	 
	 scan.nextLine();
	 
	 int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	 //System.out.println(currentDay);
	 int day = 0;
	 while (true) {
		 try {
			 System.out.print("\nEnter a day (between 1 and 31): ");
			 day = scan.nextInt();
			 if (day < 1 || day > 31) {
				 System.out.println("\nEnter a valid day!");
				 throw new InputMismatchException();
			 }
			 if (month == currentMonth &&  day < currentDay && year == currentYear) {
				 System.out.println("\nEnter a valid day (any day that is today or anything after this day)!");
				 throw new InputMismatchException();
			 }
			 switch (month) {
			 case 1:
			 case 3:
			 case 5:
			 case 7:
			 case 8:
			 case 10:
			 case 12: if (!(day >= 1 && day <= 31)) {System.out.println("\nIncorrect day number for this month!");
			 throw new InputMismatchException();} break;
			 case 4:
			 case 6:
			 case 9:
			 case 11: if (!(day >= 1 && day <= 30)) {System.out.println("\nIncorrect day number for this month!");
			 throw new InputMismatchException();} break;
			 case 2: if (year%4 == 0) { if (!(day >= 1 && day <= 29)) {System.out.println("\nIncorrect day number for this month!");
			 throw new InputMismatchException();} else if (!(day >= 1 && day <= 28)) {System.out.println("\nIncorrect day number for this month!");
			 throw new InputMismatchException();}} break;
			 }
			 break;
		 } catch (InputMismatchException e) {
			 scan.nextLine();
			 System.out.println("\nEnter a number!");
		 }
	 }
	 
	 scan.nextLine();
	 
	 String DOM = month + "-" + day + "-" + year;
	 
	 System.out.println(DOM);
	 
	 System.out.print("\nEnter Meeting Room: ");
	 String meetingRoom = scan.nextLine();
	 
	 String update = "Insert into CourseEvaluations (evaluationID, courseID, "
	 		+ "evaluationName, evaluationType, weightage, dueDate, meetingRoom) values ("
	 		+ evaluationID + ", " + courseID + ", '" + evaluationName + "', " + selection + ", "
	 		+ weightage + ", " + "to_date('" + DOM + "', 'MM-DD-YYYY'), '" + meetingRoom + "')";
	 
	 System.out.println(update);
	 
	 try {
		 Statement stmt = con.createStatement();
		 stmt.executeUpdate(update);
		 stmt.close();
		 System.out.println("-----Successfully Created Course Evaluation-----");
	 } catch (SQLException e) {
		 e.printStackTrace();
	 }
 }
 
 public void modifyEvaluations (int fid) throws ParseException {
	 CourseManager cm = new CourseManager();
	 System.out.print("Do you want to\n1. Create Evaluation\n2. Modify Evaluation\n");
	 int choice = 0;
	 int dateFlag = 0;
	 
	 while (true) {
		 try {
			 System.out.print("\nYour Selection: ");
			 choice = scan.nextInt();
			 if (choice <= 0 || choice >= 3) {
				 throw new InputMismatchException();
			 }
			 break;
		 } catch (InputMismatchException e) {
			 scan.nextLine();
			 System.out.println("\nChoose between 1 and 2!");
		 }
	 }
	 
	 scan.nextLine();
	 
	 if (choice == 1) cm.createEvaluation(fid);
	 
	 //Check if the date of the evaluation is older than current date or not
	 int eid = 0;
	 int cid = 0;
	 while (true) {
		 try {
			 System.out.print("\nEnter the Evaluation ID of the Evaluation you want to modify: ");
			 eid = scan.nextInt();
			 if (!checkIfEIDExists(eid)) {
				 System.out.println("This Evaluation ID does not exist. Select a valid one!");
				 throw new InputMismatchException();
			 }
			 
			 scan.nextLine();
			 
			 ArrayList<Integer> list = new ArrayList<Integer>();
			 
			 String query = "Select * from Courses where facultyID = " + fid;
			 
			 try {
				 Statement stmt = con.createStatement();
				 ResultSet rs = stmt.executeQuery(query);
				 while (rs.next()) {
					 list.add(rs.getInt("courseID"));
					 //System.out.println(rs.getInt("courseID"));
				 }
			 } catch (SQLException e) {
				 e.printStackTrace();
			 }
			 
			 
			 //Check if the Course Evaluation you want to modify is of your course or not
			 query = "Select * from CourseEvaluations where evaluationID = " + eid;
			 try {
				 Statement stmt = con.createStatement();
				 ResultSet rs = stmt.executeQuery(query);
				 while (rs.next()) {
					 cid = rs.getInt("courseID");
				 }
			 } catch (SQLException e) {
				 e.printStackTrace();
			 }
			 
			 System.out.println("CID: " + cid);
			 
			 if (!list.contains(cid)) {
				 System.out.println("\nEnter an Evaluation ID of a course you teach in!\nPress ENTER to continue:");
				 throw new InputMismatchException();
			 }
			 
			 //Check the date
			 query = "Select dueDate from CourseEvaluations where evaluationID = " + eid;
			 
			 Date date = new Date();
			 Date dateCheck = new Date();
			 
			 try {
				 Statement stmt = con.createStatement();
				 ResultSet rs = stmt.executeQuery(query);
				 while (rs.next()) {
					 date = rs.getDate("dueDate");
				 }
			 } catch (SQLException e) {
				 e.printStackTrace();
			 }
			 
			 String ddMMMyy = date.toString();
			 System.out.println(ddMMMyy);
		     DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		     Date convertedDate = formatter.parse(ddMMMyy);
		     System.out.println("\nDate is: " + convertedDate.toString());
			 
		     if (convertedDate.before(dateCheck)) {
		    	 System.out.println("\nDate is before!\nYou cannot modify!");
		    	 throw new NumberFormatException();
		     }
		     
			 break;
		 } catch (InputMismatchException e) {
			 scan.nextLine();
			 System.out.println("\nEnter a number!");
		 } catch (NumberFormatException e) {
			 dateFlag = 1;
			 break;
		 }
	 }
	 
	 if (dateFlag == 1) {
		 System.out.println("Bye!");
		 return;
	 }
	 System.out.print("Enter the name of the evaluation: ");
	 String evaluationName = scan.nextLine();
	 
	 
	 System.out.println("\nSelect evaluation type:\n1. H.W.\n2. Midterm\n3. Final Exam\n4. Project");
	 int selection = 0;
	 while (true) {
		 try {
			 System.out.print("\nYour Selection: ");
			 selection = scan.nextInt();
			 if (selection <= 0 || selection >= 5) {
				 System.out.println("\nPrint a vaild value!");
				 throw new InputMismatchException();
			 }
			 break;
		 } catch (InputMismatchException e) {
			 scan.nextLine();
			 System.out.println("\nEnter a number!");
		 }
	 }
	 
	 scan.nextLine();
	 
	 Double weightage = 0.0;
	 while (true) {
		 try {
			 System.out.print("\nEnter a number between 0.00 and 1.00: ");
			 weightage = scan.nextDouble();
			 if (weightage <= 0.00 || weightage > 1.00) {
				 System.out.println("\nEnter a number BETWEEN 0.00 and 1.00");
				 throw new InputMismatchException();
			 }
			 break;
		 } catch (InputMismatchException e) {
			 scan.nextLine();
			 System.out.print("\nEnter a number!\n");
		 }
	 }
	 
	 int currentYear = Calendar.getInstance().get(Calendar.YEAR);
	 int year = 0;
	 while (true) {
		 try {
			 System.out.print("\nEnter a year (between now and 2100): ");
			 year = scan.nextInt();
			 if (year < currentYear || year > 2100) {
				 System.out.println("\nEnter a valid year!");
				 throw new InputMismatchException();
			 }
			 break;
		 } catch (InputMismatchException e) {
			 scan.nextLine();
			 System.out.println("\nEnter a number!");
		 }
	 }
	 
	 int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
	 int month = 0;
	 while (true) {
		 try {
			 System.out.print("\nEnter a month (between 1 and 12): ");
			 month = scan.nextInt();
			 if (month < 1 || month > 12) {
				 System.out.println("\nEnter a valid month!");
				 throw new InputMismatchException();
			 }
			 if (year == currentYear &&  month < currentMonth) {
				 System.out.println("\nEnter a valid month (any month that this or anything after this month)!");
				 throw new InputMismatchException();
			 }
			 break;
		 } catch (InputMismatchException e) {
			 scan.nextLine();
			 System.out.println("\nEnter a number!");
		 }
	 }
	 
	 scan.nextLine();
	 
	 int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	 //System.out.println(currentDay);
	 int day = 0;
	 while (true) {
		 try {
			 System.out.print("\nEnter a day (between 1 and 31): ");
			 day = scan.nextInt();
			 if (day < 1 || day > 31) {
				 System.out.println("\nEnter a valid day!");
				 throw new InputMismatchException();
			 }
			 if (month == currentMonth &&  day < currentDay && year == currentYear) {
				 System.out.println("\nEnter a valid day (any day that is today or anything after this day)!");
				 throw new InputMismatchException();
			 }
			 switch (month) {
			 case 1:
			 case 3:
			 case 5:
			 case 7:
			 case 8:
			 case 10:
			 case 12: if (!(day >= 1 && day <= 31)) {System.out.println("\nIncorrect day number for this month!");
			 throw new InputMismatchException();} break;
			 case 4:
			 case 6:
			 case 9:
			 case 11: if (!(day >= 1 && day <= 30)) {System.out.println("\nIncorrect day number for this month!");
			 throw new InputMismatchException();} break;
			 case 2: if (year%4 == 0) { if (!(day >= 1 && day <= 29)) {System.out.println("\nIncorrect day number for this month!");
			 throw new InputMismatchException();} else if (!(day >= 1 && day <= 28)) {System.out.println("\nIncorrect day number for this month!");
			 throw new InputMismatchException();}} break;
			 }
			 break;
		 } catch (InputMismatchException e) {
			 scan.nextLine();
			 System.out.println("\nEnter a number!");
		 }
	 }
	 
	 scan.nextLine();
	 
	 String DOM = month + "-" + day + "-" + year;
	 
	 System.out.println(DOM);
	 
	 System.out.print("\nEnter Meeting Room: ");
	 String meetingRoom = scan.nextLine();
	 
	 String update = "Update CourseEvaluations set evaluationName = '" + evaluationName + "', evaluationType = " + selection + 
			 ", weightage = " + weightage + ", dueDate = " + "to_date('" + DOM + "', 'MM-DD-YYYY'), meetingRoom = '" + meetingRoom + 
			 "' where evaluationID = " + eid;
	 
	 System.out.println(update);
	 
	 try {
		 Statement stmt = con.createStatement();
		 stmt.executeUpdate(update);
		 stmt.close();
		 System.out.println("-----Successfully Modified Course Evaluation-----");
	 } catch (SQLException e) {
		 e.printStackTrace();
	 }
 }
 
 public void enterGrades(int fid) {
	 //A teacher can only enter grades for a student for the course he/she teaches and the student is enrolled in
	 System.out.println("\nHere is the list of courses you can grade students in:");
	 System.out.println();
	 printAvailableCourses(fid);
	 
	 ArrayList<Integer> list = new ArrayList<Integer>();
	 
	 String query = "Select * from Courses where facultyID = " + fid;
	 
	 try {
		 Statement stmt = con.createStatement();
		 ResultSet rs = stmt.executeQuery(query);
		 while (rs.next()) {
			 list.add(rs.getInt("courseID"));
			 //System.out.println(rs.getInt("courseID"));
		 }
	 } catch (SQLException e) {
		 e.printStackTrace();
	 }
	 
	 int cid = 0;
	 
	 while (true) {
		 try {
			 System.out.print("Enter the Course ID of the course you want to grade for: ");
			 cid = scan.nextInt();
			 if (!checkIfCIDExists(cid)) {
				 System.out.println("\nEnter a valid Course ID!");
				 throw new InputMismatchException();
			 }
			 if (!list.contains(cid)) {
					System.out.println("\nEnter a Course ID that of a course that you teach!");
					throw new InputMismatchException();
			 }
			 break;
		 } catch (InputMismatchException e) {
			 scan.nextLine();
			 System.out.println("\nEnter a number!");
		 }
	 }
	 
	 int sid;
	 while (true) {
		 try {
			 System.out.print("\nEnter the Student ID for the Student you want to grade: ");
			 sid = scan.nextInt();
			 if (!checkIfSIDExists(sid)) {
				 System.out.println("\nPlease select a valid student: ");
				 throw new InputMismatchException();
			 }
			 if (!checkIfStudentExistsInCourse(sid, cid)) {
				 System.out.println("\nStudent doesn't exist in course, try again!");
				 throw new InputMismatchException();
			 }
			 break;
		 } catch (InputMismatchException e) {
			 System.out.println("\nEnter a number!");
			 scan.nextLine();
		 }
	 }
	 
	 scan.nextLine();
	 
	 //Select a Course Evaluation for the course
	 printAvailableEvaluations(cid);
	 
	 int eid = 0;
	 while (true) {
		 try {
			 System.out.print("\nEnter the Evaluation ID of the evaluation you want to fill: ");
			 eid = scan.nextInt();
			 if (!checkIfEIDExists(eid)) {
				 System.out.println("\nEvaluation ID doesn't exist. Try again! (Press ENTER to continue)");
				 throw new NumberFormatException();
			 }
			 if (!arr.contains(eid)) {
				 System.out.println("\nEnter one of the Evaluation ID's displayed above ONLY");
				 throw new NumberFormatException();
			 }
			 break;
		 } catch (InputMismatchException e) {
			 System.out.println("\nEnter a number!");
			 scan.nextLine();
		 } catch (NumberFormatException e) {
			 scan.nextLine();
		 }
	 }
	 
	 Double Grade = 0.00;
	 
	 while(true) {
		 try {
			 System.out.print("\nEnter a grade out of 100.00: ");
			 Grade = scan.nextDouble();
			 if (Grade < 0 || Grade > 100) {
				 System.out.println("Enter a valid Grade");
				 throw new InputMismatchException();
			 }
			 break;
		 } catch (InputMismatchException e) {
			 System.out.println("\nEnter a valid number!");
			 scan.nextLine();
		 }
	 }
	 
	 //Everything checks out
	 String update = "Insert into Evaluation Grades (evaluationID, studentID, Grade) values (" + eid + ", " + sid + ", " + Grade;
	 
	 try {
		 Statement stmt = con.createStatement();
		 stmt.executeUpdate(update);
		 stmt.close();
	 } catch (SQLException e) {
		 e.printStackTrace();
	 }
 }
 
 public void printCalendar(int sid) {
	 String query = "select ce.evaluationName as eval, ce.courseID as cd from CourseEvaluations ce join Enrollment en on en.courseID = ce.courseID where en.studentID = " + sid;
	 
	 try {
		 Statement stmt = con.createStatement();
		 ResultSet rs = stmt.executeQuery(query);
		 System.out.println("\nYour Evaluations: ");
		 System.out.println("\nEvaluation Names\t Course ID");
		 System.out.println("---------------------------------------------------");
		 while (rs.next()) {
			 System.out.printf("%-25s %-25d\n", rs.getString("eval"), rs.getInt("cd"));
		 }
	 } catch (SQLException e) {
		 e.printStackTrace();
	 }
	 
	 System.out.println();
 }
 
 public void classReport() {
	 String query1 = "Select courseID, courseName, meetsAt, room from Courses";
	 
	 try {
		 Statement stmt1 = con.createStatement();
		 ResultSet rs1 = stmt1.executeQuery(query1);
		 
		 System.out.println("\nCourse Name\t\t Meets At\t\t Room No.\t\tNo. of Students\t\tNo. of Evaluations");
		 
		 while (rs1.next()) {
			 System.out.println(rs1.getInt("courseID"));
			 String query2 = "select count(studentID) as counts from Enrollment where courseID = " + rs1.getInt("courseID");
			 String query3 = "select count(evaluationID) as counte from CourseEvaluations where courseID = " +rs1.getInt("courseID");
			 
			 int numStudents = 0;
			 int numEvaluations = 0;
			 try {
				 Statement stmt2 = con.createStatement();
				 Statement stmt3 = con.createStatement();
				 ResultSet rs2 = stmt2.executeQuery(query2);
				 ResultSet rs3 = stmt3.executeQuery(query3);
				 
				 while (rs2.next()) {
					 numStudents = rs2.getInt("counts");
				 }
				 while (rs3.next()) {
					 numEvaluations = rs3.getInt("counte");
				 }
			 } catch (SQLException e) {
				 e.printStackTrace();
			 }
			 
			 System.out.printf("%-25s %-25s %-25s %-25d %-25d", rs1.getString("courseName"), rs1.getString("meetsAt"), rs1.getString("room"), numStudents, numEvaluations);
		 } 
	 } catch (SQLException e) {
		 e.printStackTrace();
	 }
 }
 
 /* Nested Query: Names of students enrolled in a course */
 public void getStudentsInClass( String cname ) {
  String query = "Select snum from Enrolled where cname = '" + cname + "'";
  try {
   Statement stmt = con.createStatement();
   ResultSet rs = stmt.executeQuery( query );
   while ( rs.next() ) {
    int snum = rs.getInt( "snum" );
    String nestedQuery = "Select sname from Student where snum=" + snum;
    Statement stmtNested = con.createStatement();
    ResultSet rsNested = stmtNested.executeQuery( nestedQuery );
    while ( rsNested.next() ) {
     String sname = rsNested.getString( "sname" );
     System.out.println( sname );
    }
    rsNested.close();
    stmtNested.close();
   }
   rs.close();
   stmt.close();
  }
  catch ( SQLException e ) {}
 }

 /* Update: Insert data into database */
 public void enroll( int snum, String cname, String grade ) {
  String update = "insert into Enrolled values(" + snum + ",'" + cname +"','"+grade+"')";
  System.out.println(update);
  try {
   Statement stmt = con.createStatement();
   stmt.executeUpdate( update );
   stmt.close();
  }
  catch ( SQLException e ) {}
 }

 /* Update: Insert user input into database */
 public void insertNewDepartment() {
  try {
   Scanner scanner = new Scanner(System.in);
   System.out.print("Enter department id:");
   int did = scanner.nextInt();
   scanner.nextLine();
   System.out.print("Enter department name:");
   String depName = scanner.nextLine();
   System.out.print("Enter department location:");
   String depLocation = scanner.nextLine();
   scanner.close();
   String insertDepartment = "insert into Department " + " (deptid, dname, location)" + " values (?, ?, ?)";
   PreparedStatement stmt = con.prepareStatement(insertDepartment);
            stmt.setInt(1, did);
            stmt.setString(2, depName);
            stmt.setString(3, depLocation);
            stmt.executeUpdate();
            stmt.close();
      }
      catch(SQLException e){}
 }
 
 public void print() throws ParseException {
	 CourseManager cm = new CourseManager();
	 
	 while (true) {
		 System.out.println("------------------");
		 System.out.println("Select User Type: ");
		 System.out.println("------------------");
		 System.out.println("1. Student\n2. Faculty\n3. Administrator\n4. Exit");

		 int userType = 0;
		 
		 while (true) {
			 try {
				 System.out.print("\nYour selection: ");
				 userType = scan.nextInt();
				 break;
			 } catch (InputMismatchException e) {
				 scan.nextLine();
				 System.out.println("\nInput a number!");
			 }
		 }
		 System.out.println();
		 
		 if (userType == 4) {
			 System.out.println("Bye!");
			 break;
		 };
		 
		 switch (userType) {
		 case 1: cm.printStudent(); break;
		 case 2: cm.printFaculty(); break;
		 case 3: cm.printAdministrator(); break;
		 default: System.out.println("Invalid User Type, try again\n"); break;
		 }
	 }
	 
	 scan.close();
 }
 
 public void printStudent() {
	 CourseManager cm = new CourseManager();
	 int id;
	 while (true) {
		 System.out.println("------------------");
		 
		 while (true) {
			 try {
				 System.out.printf("Enter your ID: ");
				 id = scan.nextInt();
				 break;
			 } catch (InputMismatchException e) {
				 scan.nextLine();
				 System.out.println("Input a number!");
			 }
		 }
		 
		 if (!cm.checkIfSIDExists(id)) {
			 System.out.println("This Student ID does not exist. Try again!");
			 return;
		 } else {
			 break;
		 }
	 }
	
	 while (true) {
		 System.out.println("------------------");
		 System.out.println("Your options are:");
		 System.out.println("------------------");
		 System.out.println("1. Calendar of Evaluations\n2. My Courses\n3. My Grades\n4.Exit");
		 
		 int selection = 0;
		 
		 while (true) {
			 try {
				 System.out.print("\nYour Selection: ");
				 selection = scan.nextInt();
				 break;
			 } catch (InputMismatchException e) {
				 scan.nextLine();
				 System.out.println("\nInput a number!");
			 }
		 }
		 
		 System.out.println();
		 
		 if (selection == 4) break;
		 
		 switch (selection) {
		 case 1: cm.printCalendar(id); break;
		 case 2: cm.printEnrolledCourses(id); break;
		 case 3: break;
		 default: System.out.println("Please select a valid option");
		 }
	 }
 }
 
 public void printFaculty() throws ParseException {
	 CourseManager cm = new CourseManager();
	 int id;
	 while (true) {
		 System.out.println("------------------");
		 
		 while (true) {
			 try {
				 System.out.printf("Enter your ID: ");
				 id = scan.nextInt();
				 break;
			 } catch (InputMismatchException e) {
				 scan.nextLine();
				 System.out.println("Input a number!");
			 }
		 }
		 
		 if (!cm.checkIfFIDExists(id)) {
			 System.out.println("This Faculty ID does not exist. Try again!");
			 return;
		 } else {
			 break;
		 }
	 }
	 
	 while (true) {
		 System.out.println("------------------");
		 System.out.println("Your options are:");
		 System.out.println("------------------");
		 System.out.println("1. Create a course\n2. Modify a course\n3. Assign students to a course\n4. Create/Modify an Evaluation\n5. Enter Grades\n6. Report of Classes\n7. Report of Students and Grades\n8. Exit");
		 
		 int selection = 0;
		 
		 while (true) {
			 try {
				 System.out.print("\nYour Selection: ");
				 selection = scan.nextInt();
				 break;
			 } catch (InputMismatchException e) {
				 scan.nextLine();
				 System.out.println("\nInput a number!");
			 }
		 }
		 
		 scan.nextLine();
		 
		 System.out.println();
		 
		 if (selection == 8) break;
		 
		 switch (selection) {
		 case 1: createCourse(); break;
		 case 2: modifyCourse(id); break;
		 case 3: assignCourses(id); break;
		 case 4: modifyEvaluations(id); break;
		 case 5: enterGrades(id); break;
		 case 6: classReport(); break;
		 case 7: break;
		 default: System.out.println("Please select a valid option");
		 } 
	 }
 }
 
 public void printAdministrator() {
	 while (true) {
		 System.out.println("------------------");
		 System.out.println("Your options are:");
		 System.out.println("------------------");
		 System.out.println("1. Department Report\n2. Faculty Report\n3. Exit");
		 
		 int selection = 0;
		 
		 while (true) {
			 try {
				 System.out.print("\nYour Selection: ");
				 selection = scan.nextInt();
				 break;
			 } catch (InputMismatchException e) {
				 scan.nextLine();
				 System.out.print("\nInput a number!\n");
			 }
		 }
		 
		 System.out.println();
		 
		 if (selection == 3) break;
		 
		 switch (selection) {
		 case 1: printDepartmentInfo(); break;
		 case 2: printFacultyInfo(); break;
		 default: System.out.println("Please select a vaild option");
		 }
	 }
 }
 
 public static void main( String [] args ) throws ParseException {
  CourseManager cm = new CourseManager();
  cm.print();
 }
}