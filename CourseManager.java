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

 /* Main() */
 
 public void print() {
	 CourseManager cm = new CourseManager();
	 
	 while (true) {
		 System.out.println("------------------");
		 System.out.println("Select User Type: ");
		 System.out.println("Student: 1\nFaculty: 2\nAdministrator: 3\nExit: 4");
		 
		 System.out.print("Your selection: ");
		 
		 int userType = scan.nextInt();
		 
		 System.out.println();
		 
		 if (userType == 4) break;
		 
		 switch (userType) {
		 case 1: cm.printStudent(); break;
		 case 2: cm.printFaculty(); break;
		 case 3: cm.printAdministrator(); break;
		 default: System.out.println("Invalid User Type"); break;
		 }
	 }
	 
	 scan.close();
 }
 
 public void printStudent() {
	 while (true) {
		 //System.out.println("Got in Student");
		 System.out.println("------------------");
		 System.out.println("Your options are:");
		 System.out.println("1. Calendar of Evaluations\n2. My Courses\n3. My Grades\n4.Exit");
		 System.out.print("Your Selection: ");
		 
		 int selection = scan.nextInt();
		 
		 System.out.println();
		 
		 if (selection == 4) break;
		 
		 switch (selection) {
		 case 1: break;
		 case 2: break;
		 case 3: break;
		 default: System.out.println("Please select a valid option");
		 }
	 }
 }
 
 public void printFaculty() {
	 while (true) {
		 //System.out.println("Got in Faculty");
		 System.out.println("------------------");
		 System.out.println("Your options are:");
		 System.out.println("1. Department Report\n2. Faculty Report\n3. Exit");
		 System.out.print("Your Selection: ");
		 
		 int selection = scan.nextInt();
		 
		 System.out.println();
		 
		 if (selection == 4) break;
		 
		 switch (selection) {
		 case 1: break;
		 case 2: break;
		 case 3: break;
		 default: System.out.println("Please select a valid option");
		 } 
	 }
 }
 
 public void printAdministrator() {
	 while (true) {
		 //System.out.println("Got in Administrator");
		 System.out.println("------------------");
		 System.out.println("Your options are:");
		 System.out.println("1. Department Report\n2. Faculty Report\n3. Exit");
		 System.out.print("Your Selection: ");
		 
		 int selection = scan.nextInt();
		 
		 System.out.println();
		 
		 if (selection == 3) break;
		 
		 switch (selection) {
		 case 1: break;
		 case 2: break;
		 default: System.out.println("Please select a vaild option");
		 }
	 }
 }
 
 public static void main( String [] args ) {
  CourseManager cm = new CourseManager();
  cm.print();
 }
}

//select table_name from user_tables;