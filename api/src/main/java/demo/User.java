package demo;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection="users")
public class User {
	
	@Id
	private long id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String dob;
	private String phoneNo;
	private String cardDetails;
	private ArrayList<VM> vMachine= new ArrayList<VM>();
	private String userCreatedDate;
	private String userEditedDate;
	private long userIdCounter;
	//private Object totalAlloc;

	public long getUserIdCounter() {
		return userIdCounter;
	}

	public void setUserIdCounter(long userIdCounter) {
		this.userIdCounter = userIdCounter;
	}

	public String getUserCreatedDate() {
		return userCreatedDate;
	}

	public void setUserCreatedDate(String userCreatedDate) {
		this.userCreatedDate = userCreatedDate;
	}

	public String getUserEditedDate() {
		return userEditedDate;
	}

	public void setUserEditedDate(String userEditedDate) {
		this.userEditedDate = userEditedDate;
	}

	public long getUserId() {
		return id;
	}
	
	public void setUserId(long userId) {
		this.id = userId;
	}
	public String getCardDetails() {
		return cardDetails;
	}
	public void setCardDetails(String cardDetails) {
		this.cardDetails = cardDetails;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	public ArrayList<VM> getvMachine() {
		return vMachine;
	}

	public void setvMachine(VM vMachine) {
		this.vMachine.add(vMachine);
	}

}
