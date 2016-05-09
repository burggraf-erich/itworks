package application;

import java.sql.*;

//import jfx.messagebox.MessageBox;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ComboBox;

public class MyFlightController {
	
	Connection conn;
	int highest_custID = 0;

	@FXML Button btn_close;
	@FXML Button btn_login;
	@FXML PasswordField pwf_password;
	@FXML Label lbl_dbconnect;
	@FXML TextField txt_username;
	@FXML AnchorPane apa_welcome;
	@FXML AnchorPane apa_login;
	@FXML Label lbl_username;
	@FXML TitledPane acc_charter;
	@FXML Hyperlink hlk_create_offer;
	@FXML AnchorPane apa_create_offer;
	@FXML TextField txt_companyname;
	@FXML TextField txt_street;
	@FXML TextField txt_place;
	@FXML TextField txt_homenumber;
	@FXML TextField txt_customerid;
	@FXML TextField txt_homeext;
	@FXML AnchorPane apa_btn_login;
	@FXML AnchorPane apa_btn_createoffer;
	@FXML Button btn_stop;
	@FXML Button btn_createoffer;
	@FXML AnchorPane apa_charter;
	@FXML TextField txt_mail;
	@FXML TextField txt_mobile;
	@FXML TextField txt_name;
	@FXML TextField txt_phone;
	@FXML TextField txt_prename;
	@FXML ComboBox cbo_salutation;
	@FXML ComboBox cbo_title;
	@FXML Button btn_searchcustid;

	@FXML TextField txt_companyname_new;

	@FXML TextField txt_street_new;

	@FXML TextField txt_place_new;

	@FXML TextField txt_homenumber_new;

	@FXML TextField txt_customerid_new;

	@FXML TextField txt_homeext_new;

	@FXML TextField txt_name_new;

	@FXML TextField txt_mobile_new;

	@FXML TextField txt_mail_new;

	@FXML TextField txt_phone_new;

	@FXML TextField txt_prename_new;

	@FXML ComboBox cbo_salutation_new;

	@FXML Hyperlink hlk_create_cust;

	@FXML AnchorPane apa_create_cust;

	@FXML AnchorPane apa_btn_create_cust;

	@FXML Button btn_stop_cust;

	@FXML Button btn_creat_cust;
	@FXML TextField txt_postcode_new;
	@FXML ComboBox cbo_country_new;
	@FXML ComboBox cbo_custstatus_new;
    
	
	@FXML public void btn_login_click(ActionEvent event) {
		
		
		final String hostname = "172.20.1.24"; 
        final String port = "3306"; 
        final String dbname = "myflight"; 
        final String user = txt_username.getText();
        final String password = pwf_password.getText(); 
		

		
	    try { 
	      	 Class.forName("org.gjt.mm.mysql.Driver").newInstance(); 
	        } 
	        catch (Exception e) 
	        { 
	        	//lbl_dbconnect.setText("Verbindung fehlgeschlagen");
	            //e.printStackTrace(); 
	        } 
	        try 
	        { 
		    String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
		    conn = DriverManager.getConnection(url, user, password); 
		    
		    
		    lbl_dbconnect.setText("Datenbankverbindung erfolgreich hergestellt");
		    apa_login.setVisible(false);
		    apa_welcome.setVisible(true);
		    lbl_username.setText(user);
		    

		    //conn.close();
		    //
		    } 
	        catch (SQLException sqle) 
	        { 
	        
	        lbl_dbconnect.setText("Datenbankverbindung fehlgeschlagen");
	        //System.out.println("geht nicht");   
	        //sqle.printStackTrace();
	        
	                
	        }    
		
	}

	@FXML public void btn_close_click(ActionEvent event) {
				
		System.exit(0);
	}

	@FXML public void acc_chart_click(MouseEvent event) {}

	@FXML public void hlk_create_offer(ActionEvent event) {
		
		set_allunvisible();
		apa_create_offer.setVisible(true);
		apa_btn_createoffer.setVisible(true);
		
		cbo_salutation.getItems().addAll("Herr","Frau");
		
	}
	
	public void set_allunvisible(){
	
	    apa_login.setVisible(false);
	    apa_welcome.setVisible(false);
	    apa_create_offer.setVisible(false);
	    apa_btn_login.setVisible(false);
	    apa_btn_createoffer.setVisible(false);
	    apa_create_cust.setVisible(false);
	    apa_btn_create_cust.setVisible(false);
	}

	@FXML public void btn_createoffer_click(ActionEvent event) {
		
//		final String companyname = txt_companyname.getText();
//		final String street = txt_street.getText();
//		final String place = txt_place.getText();
//		final String custid = txt_customerid.getText();
//		final String prename = txt_prename.getText();
//		final String name = txt_name.getText();
//		final String phone = txt_phone.getText();
//		final String mobile = txt_mobile.getText();
//		final String email = txt_mail.getText();
//		int i = 0;
//		String new_custID;
//	
//		
//
//		
//		
//		try { 
			//statement.executeUpdate("INSERT INTO myflight.kunde " + "VALUES (123,"+name+","+prename+","+companyname+",1,"+phone+","+mobile+"," +email+",,,,)");
			//i = statement.executeUpdate("SELECT * FROM myflight.kunde WHERE MAX(Kunde_ID)");
			//a = statement.executeQuery("SELECT * FROM myflight.kunde WHERE MAX(Kunde_ID)");
			
//			Statement statement = conn.createStatement();
//			ResultSet rs = statement.executeQuery("SELECT MAX(Kunde_ID) AS hoch FROM myflight.kunden");
//			
//			while (rs.next())
//				{
//				i = rs.getInt("hoch");
//				}			
//			statement.close();
//			
//			i = i+1;
//			new_custID = Integer.toString(i);
//			txt_customerid.setText(new_custID);
//			
//			}
//		
//		catch(Exception e){
//			System.err.println("Got an exception! "); 
//            System.err.println(e.getMessage()); 
//			}
//	
	}

	@FXML public void hlk_create_cust(ActionEvent event) {
		
		//int i = 0;
		String new_custID;
		
		set_allunvisible();
		apa_create_cust.setVisible(true);
		apa_btn_create_cust.setVisible(true);
		
		cbo_salutation_new.getItems().addAll("Herr","Frau");
		cbo_country_new.getItems().addAll("Germany", "United States", "China");
		cbo_custstatus_new.getItems().addAll("PRE","CORP","VIP");
		
		
		try { 
	    	Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT MAX(Kunde_ID) AS hoch FROM myflight.kunden");
			
			while (rs.next())
				{
				highest_custID = rs.getInt("hoch");
				}			
			statement.close();
			
			highest_custID += 1 ;
			new_custID = Integer.toString(highest_custID);
			txt_customerid_new.setText(new_custID);
			
			}
		
		catch(Exception e){
			System.err.println("Got an exception! "); 
            System.err.println(e.getMessage()); 
			}
	
	}
		
	

	@FXML public void btn_create_cust_click(ActionEvent event) {
		
		final String companyname_new = txt_companyname_new.getText();
		final String street_new = txt_street_new.getText();
		final String homenumber_new = txt_homenumber_new.getText();
		final String homeext_new = txt_homeext_new.getText();
		final String place_new = txt_place_new.getText();
		final String custid_new = txt_customerid_new.getText();
		final String prename_new = txt_prename_new.getText();
		final String name_new = txt_name_new.getText();
		final String phone_new = txt_phone_new.getText();
		final String mobile_new = txt_mobile_new.getText();
		final String email_new = txt_mail_new.getText();
		final String postcode_new = txt_postcode_new.getText();
		final String country_new = cbo_country_new.getValue().toString();
		final String custstatus_new = cbo_custstatus_new.getValue().toString();
		
		try { 

			Statement statement = conn.createStatement();			
			statement.executeUpdate(
					"INSERT INTO myflight.kunden " + "VALUES("
							+custid_new+",'"
							+name_new+"','"
							+prename_new+"','"
							+companyname_new+"','"
							+phone_new+"','"
							+mobile_new+"','"
							+email_new+"','" 
							+street_new+" "+homenumber_new+"','"
							+homeext_new+"','"
							+postcode_new+"','"
							+place_new+"','"
							+country_new+"','"
							+custstatus_new+"')");

			}
	
		catch(Exception e){
			System.err.println("Got an exception! "); 
            System.err.println(e.getMessage()); 
			}
//	
}

	@FXML public void btn_stop_click(ActionEvent event) {}
	
}


