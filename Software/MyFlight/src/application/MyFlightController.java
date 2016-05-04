package application;

import java.sql.*;

import jfx.messagebox.MessageBox;

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

public class MyFlightController {

	@FXML Button btn_close;
	@FXML Button btn_login;
	@FXML PasswordField pwf_password;
	@FXML Label lbl_dbconnect;
	@FXML TextField txt_username;
	@FXML AnchorPane apa_welcome;
	@FXML AnchorPane apa_login;
	@FXML Label lbl_username;
	
	
	
	@FXML public void btn_login_click(ActionEvent event) {
		
		
		final String hostname = "172.20.1.24"; 
        final String port = "3306"; 
        final String dbname = "testflight"; 
        final String user = txt_username.getText();
        final String password = pwf_password.getText(); 
		
	    Connection conn;
		
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
		
	    apa_login.setVisible(false);
	    apa_welcome.setVisible(true);
		
		//System.exit(0);
	}
	
}


