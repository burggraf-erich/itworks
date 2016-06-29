package application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;

public class Kunden {


		private IntegerProperty Nummer = null;
		private SimpleStringProperty Kdname = null;
		private SimpleStringProperty Kdprename = null;
		private SimpleStringProperty Companyname = null;
		private SimpleStringProperty address = null;
		private SimpleStringProperty postcode = null;
		private SimpleStringProperty location = null;
		private SimpleStringProperty phonenumber = null;
		private SimpleStringProperty email = null;
		private SimpleStringProperty country = null;
		private SimpleStringProperty cust_state = null;
		

		public Kunden(Integer Nummer, String Kdname, String Kdprename, String Companyname, String address, String postcode, String location, String phonenumber, String email, String country, String cust_state) {
	        this.Nummer = new SimpleIntegerProperty(Nummer);
	        this.Kdname = new SimpleStringProperty(Kdname);
	        this.Kdprename = new SimpleStringProperty(Kdprename);
	        this.Companyname = new SimpleStringProperty(Companyname);
	        this.address = new SimpleStringProperty(address);
	        this.postcode = new SimpleStringProperty(postcode);
	        this.location = new SimpleStringProperty(location);
	        this.phonenumber = new SimpleStringProperty(phonenumber);
	        this.email = new SimpleStringProperty(email);
	        this.country = new SimpleStringProperty(country);
	        this.cust_state = new SimpleStringProperty(cust_state);
	        }
		
 	    public String getcust_state() {
	        return cust_state.get();
	    }

	    public void setcust_state(String cust_state) {
	        this.cust_state.set(cust_state);
	    }

	    public StringProperty cust_stateProperty() {
	        return cust_state;
	    }


 	    public String getcountry() {
	        return country.get();
	    }

	    public void setcountry(String country) {
	        this.country.set(country);
	    }

	    public StringProperty countryProperty() {
	        return country;
	    }


 	    public String getemail() {
	        return email.get();
	    }

	    public void setemail(String email) {
	        this.email.set(email);
	    }

	    public StringProperty emailProperty() {
	        return email;
	    }


 	    public String getphonenumber() {
	        return phonenumber.get();
	    }

	    public void setphonenumber(String phonenumber) {
	        this.phonenumber.set(phonenumber);
	    }

	    public StringProperty phonenumberProperty() {
	        return phonenumber;
	    }

 
 	    public String getlocation() {
	        return location.get();
	    }

	    public void setlocation(String location) {
	        this.location.set(location);
	    }

	    public StringProperty locationProperty() {
	        return location;
	    }


 	    public String getpostcode() {
	        return postcode.get();
	    }

	    public void setpostcode(String postcode) {
	        this.postcode.set(postcode);
	    }

	    public StringProperty postcodeProperty() {
	        return postcode;
	    }


		
 	    public String getaddress() {
	        return address.get();
	    }

	    public void setaddress(String address) {
	        this.address.set(address);
	    }

	    public StringProperty addressProperty() {
	        return address;
	    }


		
 	    public String getCompanyname() {
	        return Companyname.get();
	    }

	    public void setCompanyname(String Companyname) {
	        this.Companyname.set(Companyname);
	    }

	    public StringProperty CompanynameProperty() {
	        return Companyname;
	    }

	    
		public  String getKdprename() {
	        return Kdprename.get();
	    }

	    public void setKdprename(String Kdprename) {
	        this.Kdprename.set(Kdprename);
	    }

	    public StringProperty KdprenameProperty() {
	        return Kdprename;
	    }
	    
		public int getNummer() {
	        return Nummer.get();
	    }

	    public void setNummer(int Nummer) {
	        this.Nummer.set(Nummer);
	    }

	    public IntegerProperty NummerProperty() {
	        return Nummer;
	    }
	    public String getKdname() {
	        return Kdname.get();
	    }

	    public void setKdname(String Kdname) {
	        this.Kdname.set(Kdname);
	    }

	    public StringProperty KdnameProperty() {
	        return Kdname;
	    }

	}
	
	

