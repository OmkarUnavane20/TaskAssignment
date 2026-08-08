package Assignment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

class Complaint {

	private Integer complaintId;
	private String citizenName;
	private String type;
	private String location;
	private String status;

	public Complaint(Integer complaintId, String citizenName, String type, String location, String status) {
		this.complaintId = complaintId;
		this.citizenName = citizenName;
		this.type = type;
		this.location = location;
		this.status = status;
	}

	public void display() {
		System.out.println("-------------------------------");
		System.out.println("Complaint ID   : " + complaintId);
		System.out.println("Citizen Name   : " + citizenName);
		System.out.println("Complaint Type : " + type);
		System.out.println("Location       : " + location);
		System.out.println("Status         : " + status);
		System.out.println("-------------------------------");
	}
}

public class complaint_management {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/complaintdb", "root",
					"root")) {

				int choice;

				do {
					System.out.println("\n========== Digital Complaint Management ==========");
					System.out.println("1. Register Complaint");
					System.out.println("2. View Complaints");
					System.out.println("3. Search Complaint");
					System.out.println("4. Update Complaint Status");
					System.out.println("5. Delete Complaint");
					System.out.println("6. Exit");
					System.out.print("Enter Choice : ");

					choice = sc.nextInt();
					sc.nextLine(); // Clear buffer

					switch (choice) {

					case 1:
						System.out.print("Enter Complaint ID : ");
						int id = sc.nextInt();
						sc.nextLine();

						System.out.print("Enter Citizen Name : ");
						String name = sc.nextLine();

						System.out.print("Enter Complaint Type : ");
						String type = sc.nextLine();

						System.out.print("Enter Location : ");
						String location = sc.nextLine();

						System.out.print("Enter Status : ");
						String status = sc.nextLine();

						String insertSql = "INSERT INTO complaint(complaint_id, citizen_name, complaint_type, location, status) VALUES(?,?,?,?,?)";
						try (PreparedStatement ps = con.prepareStatement(insertSql)) {
							ps.setInt(1, id);
							ps.setString(2, name);
							ps.setString(3, type);
							ps.setString(4, location);
							ps.setString(5, status);

							int insert = ps.executeUpdate();
							if (insert > 0) {
								System.out.println("Complaint Registered Successfully.");
							} else {
								System.out.println("Registration Failed.");
							}
						}
						break;

					case 2:
						String selectSql = "SELECT complaint_id, citizen_name, complaint_type, location, status FROM complaint";
						try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(selectSql)) {

							boolean found = false;
							while (rs.next()) {
								found = true;
								Complaint c = new Complaint(rs.getInt("complaint_id"), rs.getString("citizen_name"),
										rs.getString("complaint_type"), rs.getString("location"),
										rs.getString("status"));
								c.display();
							}
							if (!found) {
								System.out.println("No complaints found.");
							}
						}
						break;

					case 3:
						System.out.print("Enter Complaint ID : ");
						int sid = sc.nextInt();
						sc.nextLine();

						String searchSql = "SELECT complaint_id, citizen_name, complaint_type, location, status FROM complaint WHERE complaint_id=?";
						try (PreparedStatement ps1 = con.prepareStatement(searchSql)) {
							ps1.setInt(1, sid);

							try (ResultSet rs1 = ps1.executeQuery()) {
								if (rs1.next()) {
									Complaint c = new Complaint(rs1.getInt("complaint_id"),
											rs1.getString("citizen_name"), rs1.getString("complaint_type"),
											rs1.getString("location"), rs1.getString("status"));
									c.display();
								} else {
									System.out.println("Complaint Not Found.");
								}
							}
						}
						break;

					case 4:
						System.out.print("Enter Complaint ID : ");
						int uid = sc.nextInt();
						sc.nextLine();

						System.out.print("Enter New Status : ");
						String newStatus = sc.nextLine();

						String updateSql = "UPDATE complaint SET status=? WHERE complaint_id=?";
						try (PreparedStatement ps2 = con.prepareStatement(updateSql)) {
							ps2.setString(1, newStatus);
							ps2.setInt(2, uid);

							int update = ps2.executeUpdate();
							if (update > 0)
								System.out.println("Status Updated Successfully.");
							else
								System.out.println("Complaint Not Found.");
						}
						break;

					case 5:
						System.out.print("Enter Complaint ID : ");
						int did = sc.nextInt();
						sc.nextLine();

						String deleteSql = "DELETE FROM complaint WHERE complaint_id=?";
						try (PreparedStatement ps3 = con.prepareStatement(deleteSql)) {
							ps3.setInt(1, did);

							int delete = ps3.executeUpdate();
							if (delete > 0)
								System.out.println("Complaint Deleted Successfully.");
							else
								System.out.println("Complaint Not Found.");
						}
						break;

					case 6:
						System.out.println("Thank You...");
						break;

					default:
						System.out.println("Invalid Choice.");
					}

				} while (choice != 6);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sc.close();
		}
	}
}