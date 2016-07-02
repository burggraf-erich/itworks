package application;
//https://www.youtube.com/watch?v=Vh7XDjWlm_w
//  <fx:include fx:id="SearchCust" source="SearchCustomer.fxml"/>
//import jfx.messagebox.MessageBox;
import java.sql.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ComboBox;
import java.lang.String;
import javafx.util.Callback;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.EventHandler;
import javafx.scene.control.TableRow;
import javafx.beans.value.ChangeListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.stage.Stage;
import application.MyFlightController;


public class SearchCustController extends MyFlightController{
	
	private MyFlightController main;
	public int cust_id_search;
	public String Str_custid_search;
	
	//public SearchCustController (){this.Str_custid_search = Str_custid_search;};

	private ObservableList<Kunden> KundenData = FXCollections.observableArrayList();
	String where = null;
	
	
	public ObservableList<Kunden> getKundenData() {
		return KundenData;
	}



	@FXML AnchorPane apa_searchcust;
	@FXML TextField txt_prename_search;
	@FXML TextField txt_custname_search;
	@FXML TextField txt_name_search;
	@FXML TextField txt_custid_search;
	@FXML Button btn_searchcust;
	@FXML Button btn_newsearch;
	@FXML TableColumn<Kunden, Integer> col_custid;
	@FXML TableColumn<Kunden, String> col_name;
	@FXML TableColumn<Kunden, String> col_prename;
	@FXML TableColumn<Kunden, String> col_custname;
	@FXML TableColumn<Kunden, String> col_address;
	@FXML TableColumn<Kunden, String> col_postcode;
	@FXML TableColumn<Kunden, String> col_place;
	@FXML TableColumn<Kunden, String> col_phone;
	@FXML TableColumn<Kunden, String> col_mail;
	@FXML TableColumn<Kunden, String> col_country;
	@FXML TableColumn<Kunden, String> col_custstate;
	@FXML Button btn_choosecust;
	@FXML Button btn_closesearch;
	@FXML ComboBox cbo_custstate_search;
	@FXML TableView<Kunden> tbl_search;
	@FXML Label lbl_cust1;
	@FXML Label lbl_custID;
	@FXML Label lbl_cust2;
	@FXML Label lbl_cust3;
	
	
	@FXML public void btn_choosecust_click(ActionEvent event) {
		
		TablePosition pos = tbl_search.getSelectionModel().getSelectedCells().get(0);
		int row = pos.getRow();
		// Item here is the table view type:
		Kunden item = tbl_search.getItems().get(row);
		TableColumn col = pos.getTableColumn();
		// this gives the value in the selected cell:
		String data = (String) col_place.getCellObservableValue(item).getValue();
		cust_id_search = col_custid.getCellObservableValue(item).getValue();		
		Str_cust_id_chosen = Integer.toString(cust_id_search);
		
		KundenSuche Kunde;
		
		System.out.println(Str_cust_id_chosen);
		
//		try{
//	    	
//	    	Statement statement = conn.createStatement();
//	    	ResultSet rs = statement.executeQuery("SELECT Kunde_ID, KundeName, KundeVorname, IFNULL(KundeFirmenname,' ' ), KundeAdresse1, KundePLZ, KundenOrt, KundeTelefon, KundeEmail, KundenLand, Kundengruppen_Kundengruppen FROM myflight.kunden WHERE Kunde_ID ='" + Str_cust_id_chosen + "'" );      
//	        while((rs != null) && (rs.next())){
//	        	
//	        	//Kunde = new KundenSuche (rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11));
//	        	//System.out.println(Kunde.getOrt());    	
////        	filloffer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11));
//	       // 	txt_test.setText(Kunde.getName());
//	      //  	main.setTexttoName(Kunde.getName());
//	        	//showCustdialog(event);
//	        //	MyFlightController.txt_name.setText(Kunde.getName());
//	        	
//
//	        }
//	        
//	    }
//	    catch(Exception e){
//	          e.printStackTrace();
//	          System.out.println("Error on Building Data");            
//	    }
//		
		
		
    	lbl_cust1.setVisible(true);
    	lbl_custID.setText("  " + Str_cust_id_chosen);
    	lbl_custID.setVisible(true);
    	lbl_cust2.setVisible(true);
    	lbl_cust3.setVisible(true);
		
    	
    	
		String new_dbname = "benutzerverwaltung";
		String new_host = "172.20.1.24";
		String new_port = "3306";

		Connection conn_cust = null;
 		
		try { 
	      	 Class.forName("org.gjt.mm.mysql.Driver").newInstance(); 
	        } 
	        catch (Exception e) 
	        { 
	         e.printStackTrace(); 
	        } 
	        try 
	        { 
		    String url = "jdbc:mysql://"+new_host+":"+new_port+"/"+new_dbname;
		    conn_cust = DriverManager.getConnection(url, user, password); 
		    		    
//		    
		    } 
	        catch (SQLException sqle) 
        { 
           
	        }
		
	
    	
    	
		try { 
			
			Statement statement = conn_cust.createStatement();
			statement.executeUpdate("DELETE FROM benutzerverwaltung.kunde_auswahl");
			statement.executeUpdate("INSERT INTO benutzerverwaltung.kunde_auswahl " + "VALUES('"+Str_cust_id_chosen+"')");
			//conn.close();
		}
		catch(Exception e){
			System.out.println(Str_cust_id_chosen);

			System.err.println("Got an exception! "); 
            System.err.println(e.getMessage()); 
			}
		
		
		
		
		//System.out.println(Str_cust_id_chosen);
		//test();
		//filloffer();
		//cust_chosen = true;
//	    Stage stage = (Stage) btn_choosecust.getScene().getWindow();
//	    stage.close();
		
		
	}
	

	@FXML public void btn_searchclose_click(ActionEvent event) {
		
	    Stage stage = (Stage) btn_closesearch.getScene().getWindow();
	    stage.close();
	    }

	
	@FXML
	void initialize(){
	     	     
	     col_custid.setCellValueFactory (cellData -> cellData.getValue().NummerProperty().asObject());//(new PropertyValueFactory<Usermaster,String>("userName"));        
	     col_name.setCellValueFactory (cellData -> cellData.getValue().KdnameProperty());
	     col_prename.setCellValueFactory (cellData -> cellData.getValue().KdprenameProperty());
	     col_custname.setCellValueFactory (cellData -> cellData.getValue().CompanynameProperty());
	     col_address.setCellValueFactory (cellData -> cellData.getValue().addressProperty());
	     col_postcode.setCellValueFactory (cellData -> cellData.getValue().postcodeProperty());
	     col_place.setCellValueFactory (cellData -> cellData.getValue().locationProperty());
	     col_phone.setCellValueFactory (cellData -> cellData.getValue().phonenumberProperty());
	     col_mail.setCellValueFactory (cellData -> cellData.getValue().emailProperty());
	     col_country.setCellValueFactory (cellData -> cellData.getValue().countryProperty());
	     col_custstate.setCellValueFactory (cellData -> cellData.getValue().cust_stateProperty());
	     
	    tbl_search.setItems(getKundenData());
	    
	    	        
	    
	}
	
	public void buildData(){        
		
		//int i
		String new_dbname = "myflight";
		String new_host = "172.20.1.24";
		String new_port = "3306";
		String url = "jdbc:mysql://" + new_host + ":" + new_port + "/" + new_dbname;
				
	    try{
	    	  conn_new = DriverManager.getConnection(url, user, password); 
  		    	    	
	    	Statement statement_new = conn_new.createStatement(); 
	    	
	 	ResultSet rs_new = statement_new.executeQuery("SELECT Kunde_ID, KundeName, KundeVorname, IFNULL(KundeFirmenname,' ' ), KundeAdresse1, KundePLZ, KundenOrt, KundeTelefon, KundeEmail, KundenLand, Kundengruppen_Kundengruppen FROM myflight.kunden " + where);      
	     while((rs_new != null) && (rs_new.next())){
	        	
	       	KundenData.add(new Kunden(rs_new.getInt(1), rs_new.getString(2), rs_new.getString(3), rs_new.getString(4), rs_new.getString(5), rs_new.getString(6), rs_new.getString(7), rs_new.getString(8), rs_new.getString(9), rs_new.getString(10), rs_new.getString(11)));
	
                  
	       }
	        
	    }
	    catch(Exception e){
	          e.printStackTrace();
	          System.out.println("Error on Building Data");            
	    }
	}
	
	public void searchCondition(){
		
		String filter = "";
		String and = "AND";
		String quote = "'";
		
		boolean name_set = true;
		String Str_name = txt_name_search.getText();
		String Str_wherename = " lower(KundeName)=";
		
		boolean prename_set = true;
		String Str_prename = txt_prename_search.getText();
		String Str_whereprename = " lower(KundeVorname)=";
		
		boolean custname_set = true;
		String Str_custname = txt_custname_search.getText();
		String Str_wherecustname = " lower(KundeFirmenname)=";
		
		boolean custid_set = true;
		String Str_custid = txt_custid_search.getText();
		String Str_wherecustid = " Kunde_ID =";
		
		boolean custstate_set = true;
		String Str_custstate = "1";
		Str_custstate = cbo_custstate_search.getValue().toString();
		String Str_wherecuststate = " lower(Kundengruppen_Kundengruppen)=";

		boolean where_set = false;
		
		
		
		
		if (txt_name_search.getText().trim().isEmpty()){name_set = false;}
		if (txt_prename_search.getText().trim().isEmpty()){prename_set = false;}
		if (txt_custname_search.getText().trim().isEmpty()){custname_set = false;}
		if (txt_custid_search.getText().trim().isEmpty()){custid_set = false;}
		if (Str_custstate.equals(" ")){custstate_set = false;}
//		cbo_custstate_search
		
		
		
		if (name_set == true){
			
			if (where_set == false){			
			filter = filter + Str_wherename + quote + Str_name + quote;
			where_set = true;
			}		
			else{
			filter = filter + and + Str_wherename + quote + Str_name + quote;	
			}
		}
		
		
		if (prename_set == true) {
			
			if (where_set == false){			
			filter = filter + Str_whereprename + quote + Str_prename + quote;
			where_set = true;
			}		
			else{
			filter = filter + and +Str_whereprename + quote + Str_prename + quote;	
			}
			
		}
		if (custname_set == true) {
			
			if (where_set == false){			
			filter = filter + Str_wherecustname + quote + Str_custname + quote;
			where_set = true;
			}		
			else{
			filter = filter + and +Str_wherecustname + quote + Str_custname + quote;	
			}
			
		}
		if (custid_set == true) {
			
			if (where_set == false){			
			filter = filter + Str_wherecustid + quote + Str_custid + quote;
			where_set = true;
			}		
			else{
			filter = filter + and +Str_wherecustid + quote + Str_custid + quote;	
			}
			
		}
	
		if (custstate_set == true) {
			
			if (where_set == false){			
			filter = filter + Str_wherecuststate + quote + Str_custstate + quote;
			where_set = true;
			}		
			else{
			filter = filter + and +Str_wherecuststate + quote + Str_custstate + quote;	
			}
			
		}
	
		if(where_set == false){where = "";}
		else {where = "WHERE"  + filter;}

		
		
	}
		
	@FXML public void btn_searchcust_click(ActionEvent event) {
		KundenData.remove(0, KundenData.size());
		connectDB();
		searchCondition();		
		buildData();
		initialize();
	}

	@FXML public void btn_newsearch_click(ActionEvent event) {
		txt_name_search.clear();
		txt_prename_search.clear();
		txt_custname_search.clear();
		txt_custid_search.clear();
		cbo_custstate_search.setValue(" ");
		KundenData.remove(0, KundenData.size());
		
	}
	
	public void in(MyFlightController MyFlight){
		
		//main = MyFlight;
		
	}
	//public String getKNummer(){return Str_custid_search; }


	public void init(MyFlightController myFlightController) {
		// TODO Auto-generated method stub
		main = myFlightController;
	}


	public void foo(String string) {
		// TODO Auto-generated method stub
		
	}
	
}