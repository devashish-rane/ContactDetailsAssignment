import java.util.*;

public class Contact {
	
	private int contactID;
	private String ContactName;
	private String EmailAddress;
	List<String> contactNumber;

	

	public int getContactID() {
		return contactID;
	}


	public void setContactID(int contactID) {
		this.contactID = contactID;
	}


	public String getContactName() {
		return ContactName;
	}


	public void setContactName(String contactName) {
		ContactName = contactName;
	}


	public String getEmailAddress() {
		return EmailAddress;
	}


	public void setEmailAddress(String emailAddress) {
		EmailAddress = emailAddress;
	}


	public List<String> getContactNumber() {
		return contactNumber;
	}


	public void setContactNumber(List<String> contactNumber) {
		this.contactNumber = contactNumber;
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


}
