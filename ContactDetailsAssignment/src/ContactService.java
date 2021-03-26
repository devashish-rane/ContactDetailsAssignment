import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;



public class ContactService {
	static List<Contact> contacts;
	
	void addContact(Contact contact,List<Contact> contacts) {
		contacts.add(contact);
	}
	
	static void removeContact(Contact contact, List<Contact> contacts) throws ContactNotFoundException{
		Iterator it = contacts.iterator();
		int t = 0;
		while(it.hasNext()) {
			Contact con = (Contact)it.next();
			String n1 = con.getContactName();
			String name=contact.getContactName();
			if(n1.equals(name)) {
				t=1;
				contacts.remove(con);
				break;
			}
		}
		if(t==0)
			throw new ContactNotFoundException("there is no contact with this..");
	}
	
	 static Contact searchContactByName(String name, List<Contact> contact) throws ContactNotFoundException{
		Contact res=null;
 		 Iterator it=contact.iterator();
		while(it.hasNext()) {
			Contact con=(Contact) it.next();
			if(name.equals(con.getContactName())) {
				res= con;
			}else {
				System.out.println("No contact found");
			}
		}
		
		return res;
	}
	 
	 List<Contact> SearchContactByNumber(String number, List<Contact> contact) throws ContactNotFoundException{
		 List<Contact> res=new ArrayList();
		 Iterator it=contact.iterator();
		 Contact con=null;
		 while(it.hasNext()) {
			  con=(Contact) it.next();
			  List<String> num=con.getContactNumber();
			  for(String  x:num) {
				  if(x.contains(number)) {
					  res.add(con);
				  }
			  }
			 
		 }
		 return res;
	 }
	 
	 static void addContactNumber(int contactId,String contactNo,List<Contact> contacts) {
			for(Contact con : contacts) {
				if(con.getContactID()==contactId) {
					List<String> ls = con.getContactNumber();
					ls.add(contactNo);
					break;
				}
			}
		}
	 
	static void sortContactsByName(List<Contact> contacts) {
		contacts.sort(new SortByName());
	}
	 
	static void readContactsFromFile(List<Contact> contacts, String fileName) {
		try {
			FileReader fr=new FileReader(fileName);
			BufferedReader br=new BufferedReader(fr);
			String str=br.readLine();
			String count[] = str.split("\n");
			for(int i=0;i<count.length;i++) {
				Contact con = new Contact();
				String[] data = count[i].split(",");
				con.setContactID(Integer.parseInt(data[0]));
				con.setContactName(data[1]);
				con.setEmailAddress(data[2]);
				if(data.length>=4) {
					List<String> cno = new ArrayList<String>();
					for(int j=3;j<data.length;j++) {
						cno.add(data[j]);
					}
					con.setContactNumber(cno);
				}	
				contacts.add(con);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	 
	static void serializeContactDetails(List<Contact> contacts , String fileName) {
		try {
			FileOutputStream fr=new FileOutputStream(fileName);
			ObjectOutputStream os= new ObjectOutputStream(fr);
			os.writeObject(contacts);
			os.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	static List<Contact> deserializeContact(String filename){
		
		FileInputStream fr;
		List<Contact> ls = new ArrayList<Contact>();
		try {
			fr = new FileInputStream(filename);
			ObjectInputStream os=new ObjectInputStream(fr);
			ls=(ArrayList<Contact>)os.readObject();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ls;
	}
	
	static	Set<Contact> populateContactFromDb(){
		Set<Contact> set = new HashSet<Contact>();
		Contact c ;
		List<String> ls ;
			
			try{  
				Class.forName("oracle.jdbc.driver.OracleDriver");   
				Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","123");  
				
					PreparedStatement ps =con.prepareStatement("Select * from contact_tbl");
					
					ResultSet rs=ps.executeQuery();
					
					while(rs.next()) {
						c=new Contact();
						c.setContactID(rs.getInt(1));
						c.setContactName(rs.getString(2));
						c.setEmailAddress(rs.getString(3));
						ls=new ArrayList<String>();
						String s = rs.getString(4);
						if(!(s==null)) {
							String[] cno = s.split(",");
							for(int i=0;i<cno.length;i++) {
								ls.add(cno[i]);
							}
						}
						c.setContactNumber(ls);
						set.add(c);
					}	
				}catch(Exception e){ 
					System.out.println(e);
				}
				
			return set;
		}
		
	static Boolean addContacts(List<Contact> existingContact,Set<Contact> newContacts){
		existingContact.addAll(newContacts);
		return null;
	}
	static void display(List<Contact> contacts) {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Iterator it = contacts.iterator();
		while(it.hasNext()) {
			System.out.println(it.next());
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		contacts =new ArrayList<Contact>();
		List<String> contactNo = new ArrayList<String>();
		Contact contact = new Contact();
		contact.setContactID(5);
		contact.setContactName("Devashish");
		contact.setEmailAddress("ranedevashish2@gmail.com");
		contactNo.add("12323489");
		contactNo.add("12334555");
		contact.setContactNumber(contactNo);
				contacts.add(contact);
		contactNo = new ArrayList<String>();
		contact=new Contact();
		contact.setContactID(6);
		
		contacts.add(contact);
		display(contacts);
		
	
//		
		try {
			Contact con = searchContactByName("Gohan",contacts);
			System.out.println(con);
		}catch(ContactNotFoundException e) {
			e.printStackTrace();
		}

//		display(contacts);
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		readContactsFromFile(contacts,"E:\\Persi\\ContactDetailsAssignment\\src\\Contact.txt");
	

		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Set<Contact> set = populateContactFromDb();
		for(Contact c : set)
			System.out.println(c);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		addContacts(contacts,set);
		display(contacts);
	}

}

class SortByName implements Comparator<Contact>{

	@Override
	public int compare(Contact c1, Contact c2) {
		// TODO Auto-generated method stub
		return c1.getContactName().compareTo(c2.getContactName());
	}
	
}























class ContactNotFoundException extends Exception{
	ContactNotFoundException(){
		
	}
	ContactNotFoundException(String message){
		super(message);
	}
}