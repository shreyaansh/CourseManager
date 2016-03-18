import java.util.*;
import java.sql.*;

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

 /* Simple SQL statement: Names of students in department */
 public void getStudentsInDepartment( int department ) {
  String query = "Select sname from Student where deptid=" + department;
  try {
   Statement stmt = con.createStatement();
   ResultSet rs = stmt.executeQuery( query );
   while ( rs.next() ) {
    String name = rs.getString( "sname" );
    System.out.println( name );
   }
   rs.close();
   stmt.close();
  }
  catch ( SQLException e ) {
   e.printStackTrace();
  }
 }

 /* Prepared Statement: Names of students in department */
 public void getStudentsInDepartmentPrepared ( int department ) {
  String stmt = "Select sname from Student where deptid=?";
  try {
   PreparedStatement ps = con.prepareStatement( stmt );
   ps.setInt( 1, department );
   ResultSet rs = ps.executeQuery();
   while ( rs.next() ) {
    String sname = rs.getString( "sname" );
    System.out.println( sname );
   }
   rs.close();
   ps.close();
  }
  catch (SQLException e){}
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
 
 public void createCourse() {
	 
	 int id = getLastCourseID();
	 
	 System.out.printf("Last Course ID: %d\n", id);
	 
	 int courseID = ++id;
	 
	 System.out.print("Enter Course Name: ");
	 String courseName = scan.nextLine();
	 
	 String semester = "default";
	 while (true) {
		 System.out.printf("Choose Semester:\n1. Fall\n2. Spring\n3. Summer\nSemester: ");
		 int semesterInt = scan.nextInt();
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
		 System.out.print("Enter a year (Hint: Should be greater than or equal to the current year): ");
		 int yearInput = scan.nextInt();
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
		 System.out.print("Enter a faculty ID for the professor teching the course: ");
		 facultyID = scan.nextInt();
		 
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
		 System.out.println("-----Sucessfully Created Course-----");
	 } catch (SQLException e) {
		 e.printStackTrace();
	 }
 }
 
 public void modifyCourse(int id) {
	 
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
 
 public void print() {
	 CourseManager cm = new CourseManager();
	 
	 while (true) {
		 System.out.println("------------------");
		 System.out.println("Select User Type: ");
		 System.out.println("Student: 1\nFaculty: 2\nAdministrator: 3\nExit: 4");
		 
		 System.out.print("Your selection: ");		 
		 
		 int userType = scan.nextInt();
			 
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
		 System.out.printf("Enter your ID: ");
		 
		 id = scan.nextInt();
		 
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
		 System.out.println("1. Calendar of Evaluations\n2. My Courses\n3. My Grades\n4.Exit");
		 System.out.print("Your Selection: ");
		 
		 int selection = scan.nextInt();
		 
		 System.out.println();
		 
		 if (selection == 4) break;
		 
		 switch (selection) {
		 case 1: break;
		 case 2: cm.printEnrolledCourses(id); break;
		 case 3: break;
		 default: System.out.println("Please select a valid option");
		 }
	 }
 }
 
 public void printFaculty() {
	 CourseManager cm = new CourseManager();
	 int id;
	 while (true) {
		 System.out.println("------------------");
		 System.out.printf("Enter your ID: ");
		 
		 id = scan.nextInt();
		 
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
		 System.out.println("1. Create a course\n2. Modify a course\n3. Assign students to a course\n4. Create/Modify an Evaluation\n5. Enter Grades\n6. Report of Classes\n7. Report of Students and Grades\n8. Exit");
		 System.out.print("Your Selection: ");
		 
		 int selection = scan.nextInt();
		 
		 scan.nextLine();
		 
		 System.out.println();
		 
		 if (selection == 8) break;
		 
		 switch (selection) {
		 case 1: createCourse(); break;
		 case 2: modifyCourse(id); break;
		 case 3: break;
		 case 4: break;
		 case 5: break;
		 case 6: break;
		 case 7: break;
		 default: System.out.println("Please select a valid option");
		 } 
	 }
 }
 
 public void printAdministrator() {
	 while (true) {
		 System.out.println("------------------");
		 System.out.println("Your options are:");
		 System.out.println("1. Department Report\n2. Faculty Report\n3. Exit");
		 System.out.print("Your Selection: ");
		 
		 int selection = scan.nextInt();
		 
		 System.out.println();
		 
		 if (selection == 3) break;
		 
		 switch (selection) {
		 case 1: printDepartmentInfo(); break;
		 case 2: printFacultyInfo(); break;
		 default: System.out.println("Please select a vaild option");
		 }
	 }
 }
 
 public static void main( String [] args ) {
  CourseManager cm = new CourseManager();
  cm.print();
 }
}