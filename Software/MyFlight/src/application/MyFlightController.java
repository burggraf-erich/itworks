package application;
// V2.02

import java.sql.*;
import application.PDFPrinter;

//import jfx.messagebox.MessageBox;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
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
import javafx.stage.Stage;
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
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// imports für PDF-Generator
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Font.FontStyle;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.TabSettings;

import application.ParagraphBorder;
import application.PdfGenerator;

//imports für Word-Generator
import org.docx4j.Docx4J;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.model.table.TblFactory;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.StyleDefinitionsPart;
import org.docx4j.wml.CTShd;
import org.docx4j.wml.Jc;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.Style;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.TcPr;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;



//imports fuer DatePicker
import java.time.DayOfWeek;
import java.time.LocalDate;
//import java.time.LocalTime;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import application.druckauftrag;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.DatePicker;

import application.SearchCustController;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.CheckBox;
import javafx.scene.input.InputMethodEvent;

import org.joda.time.LocalTime;

public class MyFlightController {

	
	// VAriablen für DB Connect
	public static boolean firstLogon = true;
	public static String hostname; 
    public static String port; 
    public static String dbname; 
    public static String user;
    public static String password; 
	
	
	// ObjectVariable für Word-Dokument
	private static ObjectFactory factory;
	private static final String WORD_STYLE_TITLE = "Title";
	private static final String WORD_STYLE_HEADING1 = "Heading1";
	private static final String WORD_STYLE_HEADING2 = "Heading2";
	
	// Variablen für Combobox und Ausdruck
	public String AuswahlDokutyp;
	public String AuswahlAktion;
	public static String strFilename = "test.pdf";
	public static String filename = System.getProperty("user.dir") + "/" + strFilename;
	public static File f = new File(filename);

	public static int Dialog = -1;

	
	// Erzeuge ArrayListe für Tabellenversorgung
	private ObservableList<Angebote> angebotedata = FXCollections.observableArrayList();

	// gib Daten der ArrayListe zurück
	public ObservableList<Angebote> getangebotedata() {
		return angebotedata;
	}

	private ObservableList<Aufträge> auftraegedata = FXCollections.observableArrayList();

	public ObservableList<Aufträge> getauftraegedata() {
		return auftraegedata;
	}

	private ObservableList<Rechnungen> billdata = FXCollections.observableArrayList();

	public ObservableList<Rechnungen> getbilldata() {
		return billdata;
	}
	
	private ObservableList<FHSuche> FHData = FXCollections.observableArrayList();
		
	
	public ObservableList<FHSuche> getFHData() {
		return FHData;
	}
	
	// zu Beginn besteht keine Autentifizierung und damit sind alle Menüpunkte und Buttons deaktiviert
	private boolean authenticated = false;
	
	Connection conn;
	int highest_custID = 0;
	
	//Variablen für Angebot erstellen
	
	public String phone;
	boolean StartFH = false;
	boolean ZielFH = false;
	
	String Str_StartFH = null;
	String Str_ZielFH = null;
	
	String StartKont = null;
	String ZielKont = null;
	
	String FHzw1 = null;
	String FHzw2 = null;
	String FHzw3 = null;
	String FHzw4 = null;
	String FHzw5 = null;
	
	String[] FHzw;
	String[] zw_an_h;
	String[] zw_an_m;
	String[] zw_ab_h;
	String[] zw_ab_m;
	LocalDate[] zw_an;
	LocalDate[] zw_ab;
	
	int countzw;
	int arrayzw = 0;
	
	float KostenSW = 0;
	String SWgetr = null;
	String SWspeisen = null;
	String SW = "";
	
	String FZHersteller = null;
	String FZTyp = null;
	String CAPvorname = null;
	String CAPnachname = null;
	String COPvorname = null;
	String COPnachname = null;
	String FA1vorname = null;
	String FA1nachname = null;
	String FA2vorname = null;
	String FA2nachname = null;
	String FA3vorname = null;
	String FA3nachname = null;
	
	LocalDate Start_offer = null;
	LocalDate Ziel_offer = null;
	
    int bestFA1 = 0;
    int bestFA2 = 0;
    int bestFA3 = 0;
    int bestCOP = 0;
    int bestCaptain = 0;
    int counter = 0;
    int bestFZ = 0;
    int alleFZ = 0;
    int count_cop = 0;
    int count_fa = 0;
    double reichweite = 0;
    int speed = 0;
    String Lizenz = null;
    
    float startfhlon = 0;
	float startfhlat = 0;
	float zielfhlon = 0;
	float zielfhlat = 0;
	float entfernung = 0;
	
	LocalDate zieldate = LocalDate.now();
	LocalTime zielzeit = LocalTime.parse("00:00:00");
	String Str_zielzeith = null;
	String Str_zielzeitm = null;
	LocalDate startdate = LocalDate.now();
	LocalTime startzeit = LocalTime.parse("00:00:00");
	
	//LocalTime dauercharter = LocalTime.parse("00:00:00");
	//LocalTime dauerflug = LocalTime.parse("00:00:00");
	float dauerflug = 0;
	float dauercharter = 0;
	String Str_startzeith = null;
	String Str_startzeitm = null;
	
	String charterart = null;
	
	int AngeboteID = 0;
	LocalDate AngDatum = LocalDate.now();
	
	float angbrutto = 0;
	float angnetto = 0;
	float angfix = 0;
	float angbetr = 0;
	float angpers = 0;
	float angpre = 0;
	float angpre_fakt = 0.05F;
	float pers_aufschlag = 1.2F;
	float mwst = 1.19F;
	
    int pax = 0;
    int pax_fix = 0;
    int FZpass = 0;
    int highpax = 0;
    
    float gehcap = 0;
    float gehcop = 0;
    float gehfa = 0;
    
    int FixkostenFZ = 0;
    int BetriebskFZ = 0;
    
    String CustState = null;
    
    boolean sonderw = false;
	
	//Variablen für Kalender
	
	Date date;

	@FXML Button btn_close;
	@FXML Button btn_login;
	@FXML Button btn_cancel_createorder;
	@FXML Button btn_cancel_changeorder;
	@FXML Button btn_stop;
	@FXML Button btn_createoffer;
	@FXML Button btncreateorder;
	@FXML Button btnprint;
	@FXML Button btnsend;
	@FXML Button btncreatebill;
	@FXML Button btn_save_order;
	@FXML PasswordField pwf_password;
	@FXML Button btn_searchcustid;
	@FXML Button btn_stop_cust;
	@FXML Button angebotedit;
	@FXML Button btn_creat_cust;
	@FXML Button btn_changebillstatus;
	@FXML Button btn_cancelchangebillstatus;
	
	@FXML AnchorPane apa_welcome;
	@FXML AnchorPane apa_login;
	@FXML AnchorPane Aufträgeübersicht;
	@FXML AnchorPane auftragübersichtbuttons;
	@FXML AnchorPane Rechnungenübersichtbuttons;
	@FXML AnchorPane Rechnungenübersicht;
		
	@FXML AnchorPane panebtnangebotübersicht;
	@FXML AnchorPane ancpane_createorder;
	@FXML AnchorPane ancpanebtn_createorder;
	@FXML AnchorPane apa_btn_login;
	@FXML AnchorPane apa_btn_createoffer;
	@FXML AnchorPane apa_create_offer;
	@FXML AnchorPane apa_charter;
	@FXML AnchorPane ancpanebtn_changeorder;
	@FXML AnchorPane auftragändernform;
	@FXML AnchorPane angebotübersicht;
	@FXML AnchorPane apa_create_cust;
	@FXML AnchorPane apa_btn_create_cust;
	@FXML AnchorPane apa_formchangebillstatus;
	@FXML AnchorPane ancpanebtn_changebillstatus;
	
	@FXML ScrollPane scroll_pane_order;
	@FXML ScrollPane scroll_pane_changeorder;
	@FXML ScrollPane scrollpane_changebillstatus;
	
	@FXML Label lbl_dbconnect;
	@FXML Label lbl_username;
	@FXML Label lblrolle;
	@FXML Label lblberechtigung;
	@FXML Label maskentitel;
	
	@FXML TitledPane mnudashboard;
	@FXML TitledPane mnufinanzverwaltung;
	@FXML TitledPane mnureporting;
	@FXML TitledPane mnuadministration;
	@FXML TitledPane mnucharter;
	@FXML TitledPane übersichtangebote;
	@FXML TitledPane übersichtaufträge;
	@FXML Hyperlink hlk_create_offer;
	@FXML Hyperlink mnuzusatzkosten;

	//Angebot erstellem
	@FXML TextField txt_username;
	@FXML TextField txt_companyname;
	@FXML TextField txt_street;
	@FXML TextField txt_place;
	@FXML TextField txt_homenumber;
	@FXML TextField txt_customerid;
	@FXML TextField txt_homeext;
	@FXML Button btn_setcust;	
	@FXML AnchorPane apa_create_order;
	@FXML TextField txt_test;
	@FXML TextField txt_phone1;
	@FXML TextField txt_companyname1;
	@FXML TextField txt_street1;
	@FXML TextField txt_place1;
	@FXML TextField txt_customerid1;
	@FXML TextField txt_homeext1;
	@FXML TextField txt_name1;
	@FXML TextField txt_mobile1;
	@FXML TextField txt_mail1;
	@FXML TextField txt_prename1;
	@FXML TextField txt_anrede1;
	@FXML ComboBox cbo_startfh;
	@FXML DatePicker dpi_startdat;
	@FXML ComboBox cbo_charterart;
	@FXML ComboBox cbo_zielfh;
	@FXML DatePicker dpi_zieldat;
	@FXML TextField txt_pass;
	@FXML Button btn_zw;
	@FXML TextArea txa_bem;
	@FXML TextField txt_startzeit_h;
	@FXML TextField txt_startzeit_m;
	@FXML TextField txt_zielzeit_m;
	@FXML TextField txt_zielzeit_h;
	@FXML Button btn_sw;
	@FXML ComboBox cbo_cal_ma;
	@FXML ComboBox cbo_cal_fz;
	@FXML RadioButton rbt_cal_all;
	@FXML ToggleGroup tgr_cal;
	@FXML RadioButton rbt_cal_ma;
	@FXML RadioButton rbt_cal_fz;
	@FXML Button btn_search_fh;
	@FXML Button btn_newSearch_fh;
	@FXML Button btn_check_fh;
	@FXML Button btn_close_fh;
	@FXML TextField txt_iata_search;
	@FXML TextField txt_stadt_search;
	@FXML AnchorPane apa_search_fh;
	@FXML TableView<FHSuche> tbl_fh;
	@FXML TableColumn<FHSuche, String> tbc_iata;
	@FXML TableColumn<FHSuche, String> tbc_fhname;
	@FXML TableColumn<FHSuche, String> tbc_stadt;
	@FXML TableColumn<FHSuche, String> tbc_land;
	@FXML TextField txt_zielfh;
	@FXML Button btn_zielfh;
	@FXML Button btn_startfh;
	@FXML TextField txt_startfh;

	
	
	@FXML TextField txt_mail;
	@FXML TextField txt_mobile;
	@FXML TextField txt_name;
	@FXML TextField txt_phone;
	@FXML TextField txt_prename;
	@FXML ComboBox<String> cbo_salutation;
	@FXML ComboBox<String> cbo_title;
	@FXML ComboBox<String> choiceorderstatus;
	


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
	@FXML TextField txt_postcode_new;
	

	@FXML Hyperlink hlk_create_cust;

	

	
	@FXML ComboBox<String> cbo_country_new;
	@FXML ComboBox<String> cbo_custstatus_new;
	@FXML ComboBox<String> cbo_salutation_new;
	

	
	
	@FXML	TableView<Angebote> angebotetabelle;
	@FXML	TableColumn<Angebote, Integer> Nummer;
	@FXML	TableColumn<Angebote, String> Kdname;
	@FXML	TableColumn<Angebote, String> Datum;
	@FXML	TableColumn<Angebote, String> Status;
	@FXML	TableColumn<Angebote, String> Kdgruppe;
	@FXML	TableColumn Kdvname;
	@FXML	TableColumn<Angebote, String> Aart;
	@FXML	TableColumn Flgztyp;
	@FXML	TableColumn Beginn;
	@FXML	TableColumn Ende;
	
	@FXML	TableView<Aufträge> auftragtable;
	@FXML	TableColumn<Aufträge, Integer> Nummerorder;
	@FXML	TableColumn<Aufträge, String> Kdnameorder;
	@FXML	TableColumn<Aufträge, String> Datumorder;
	@FXML	TableColumn<Aufträge, String> Statusorder;
	@FXML	TableColumn<Aufträge, String> Kdgruppeorder;
	@FXML	TableColumn<Aufträge, String> Aartorder;

	@FXML	TableView<Rechnungen> billtable;
	@FXML	TableColumn<Rechnungen, Integer> Nummerbill;
	@FXML	TableColumn<Rechnungen, String> Statusbill;
	@FXML	TableColumn<Rechnungen, String> Datumtopay;
	@FXML	TableColumn<Rechnungen, Float> Preisbill;
	@FXML	TableColumn<Rechnungen, Float> Preisbill_aufschlag;
	@FXML	TableColumn<Rechnungen, Float> Preisbill_zusatzkosten;
	@FXML	TableColumn<Rechnungen, String> Kdgruppebill;
	@FXML	TableColumn<Rechnungen, Integer> Nummerorder_forbilltable;
	@FXML	TableColumn<Rechnungen, String> Statusorder_forbilltable;
	
	//####Kalender
	@FXML TitledPane tpa_calendar;
	@FXML ListView lst_A1;
	@FXML AnchorPane apa_calendar;
	@FXML TextArea txa_B1;
	@FXML TextArea txt_mon0;
	@FXML TextArea txt_mon1;
	@FXML TextArea txt_mon2;
	@FXML TextArea txt_mon5;
	@FXML TextArea txt_mon4;
	@FXML TextArea txt_mon3;
	@FXML Button btn_cal;
	@FXML DatePicker dap_cal;
	@FXML TextArea txt_tue0;
	@FXML TextArea txt_thu0;
	@FXML TextArea txt_wed0;
	@FXML TextArea txt_sat0;
	@FXML TextArea txt_fri0;
	@FXML TextArea txt_sun0;
	@FXML Label lbl_mon;
	@FXML Label lbl_die;
	@FXML Label lbl_mit;
	@FXML Label lbl_don;
	@FXML Label lbl_fre;
	@FXML Label lbl_sam;
	@FXML Label lbl_son;
	
	//@FXML
	//private Parent SearchCust; //embeddedElement
	//@FXML
	//private SearchCustController SearchCustController; // $embeddedElement+Controller
	
	//@FXML SearchCustController SearchCustController;
	
//	public Connection conn;
	public int cust_id_chosen = 3001;
	public String Str_cust_id_chosen;
	public boolean cust_chosen = false;
//	public static boolean firstLogon = true;
//	public static String hostname; 
//    public static String port; 
//    public static String dbname; 
//    public static String user;
//    public static String password; 
    public String custname = null;
    KundenSuche Kunde_neu;

	@FXML	
	private void initialize() {
		
		// Initialize the person table with the two columns.
		Nummer.setCellValueFactory(cellData -> cellData.getValue().NummerProperty().asObject());
		Status.setCellValueFactory(cellData -> cellData.getValue().StatusProperty());
		Aart.setCellValueFactory(cellData -> cellData.getValue().AartProperty());
		Kdgruppe.setCellValueFactory(cellData -> cellData.getValue().KdgruppeProperty());

		Nummerorder.setCellValueFactory(cellData -> cellData.getValue().NummerorderProperty().asObject());
		Statusorder.setCellValueFactory(cellData -> cellData.getValue().StatusorderProperty());
		Aartorder.setCellValueFactory(cellData -> cellData.getValue().AartorderProperty());
		Kdgruppeorder.setCellValueFactory(cellData -> cellData.getValue().KdgruppeorderProperty());

		Nummerbill.setCellValueFactory(cellData -> cellData.getValue().NummerbillProperty().asObject());
		Statusbill.setCellValueFactory(cellData -> cellData.getValue().StatusbillProperty());
		Datumtopay.setCellValueFactory(cellData -> cellData.getValue().DatumtopayProperty());
		Preisbill.setCellValueFactory(cellData -> cellData.getValue().PreisbillProperty().asObject());
		Preisbill_aufschlag.setCellValueFactory(cellData -> cellData.getValue().Preisbill_aufschlagProperty().asObject());
		Preisbill_zusatzkosten.setCellValueFactory(cellData -> cellData.getValue().Preisbill_zusatzkostenProperty().asObject());
		Kdgruppebill.setCellValueFactory(cellData -> cellData.getValue().KdgruppebillProperty());

		
		angebotetabelle.setItems(getangebotedata());
		auftragtable.setItems(getauftraegedata());
		billtable.setItems(getbilldata());
		
		apa_btn_login.setVisible(true);
		apa_login.setVisible(true);
	    btncreateorder.disableProperty().bind(Bindings.isEmpty(angebotetabelle.getSelectionModel().getSelectedIndices()));
	    btnprint.disableProperty().bind(Bindings.isEmpty(auftragtable.getSelectionModel().getSelectedIndices()));
		btnsend.disableProperty().bind(Bindings.isEmpty(auftragtable.getSelectionModel().getSelectedIndices()));
		btncreatebill.disableProperty().bind(Bindings.isEmpty(auftragtable.getSelectionModel().getSelectedIndices()));
		angebotedit.disableProperty().bind(Bindings.isEmpty(auftragtable.getSelectionModel().getSelectedIndices()));
		btn_changebillstatus.disableProperty().bind(Bindings.isEmpty(billtable.getSelectionModel().getSelectedIndices()));
	
		//Flughafen Suche
	     tbc_iata.setCellValueFactory (cellData -> cellData.getValue().IATAProperty());
	     tbc_fhname.setCellValueFactory (cellData -> cellData.getValue().NameProperty());
	     tbc_stadt.setCellValueFactory (cellData -> cellData.getValue().StadtProperty());
	     tbc_land.setCellValueFactory (cellData -> cellData.getValue().LandProperty());
		 tbl_fh.setItems(getFHData());
		
} 
	
	
	
	public void connectDB(){
		try { 
	      	 Class.forName("org.gjt.mm.mysql.Driver").newInstance(); 
	        } 
	        catch (Exception e) 
	        { 
	         e.printStackTrace(); 
	        } 
	        try 
	        { 
		    String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
		    conn = DriverManager.getConnection(url, user, password); 
		    		    
//		    if (firstLogon == true){
//		    
//		    lbl_dbconnect.setText("Datenbankverbindung erfolgreich hergestellt");
//		    apa_login.setVisible(false);
//		    apa_welcome.setVisible(true);
//		    lbl_username.setText(user);
//		    firstLogon = false;
//		    }
//		    
		    } 
	        catch (SQLException sqle) 
        { 
//	        
//	        if (firstLogon == true){	
//	        lbl_dbconnect.setText("Datenbankverbindung fehlgeschlagen");
//	        }
//	        System.out.println("geht nicht");   
//	        sqle.printStackTrace();
	        
	                
	        }
		
	}
	 
	
	
	@FXML public void btn_login_click(ActionEvent event) {
		
		
		//final String 
		hostname = "172.20.1.24"; 
        //final String 
		port = "3306"; 
        //final String 
		dbname = "myflight"; 
        //final String 
		user = txt_username.getText();
        //final String 
		password = pwf_password.getText(); 
		

		
	    try { 
	      	 Class.forName("org.gjt.mm.mysql.Driver").newInstance(); 
	        } 
	        catch (Exception e) 
	        { 
	        	//lbl_dbconnect.setText("Verbindung fehlgeschlagen 1.");
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
		    
		    btn_login.setVisible(false);
		    
		    
		    // Vor- und Nachnamen ermitteln
		    int pos = user.indexOf(".");
		    String vorname = user.substring(0, pos);
		    String nachname = user.substring(pos+1,user.length());
		    
		    User userobject = new User(vorname, nachname,"Mitarbeiter",3);
		    authenticated = true;
		    String userrolle = userobject.getrolle();
		    lblrolle.setText(userrolle);
		    lblberechtigung.setText(String.valueOf(userobject.getberechtigung()));	
		    
		    // setze nach erfolgreicher Anmeldung je nach Berechetigungsgruppe die Menüpunkte und Buttons aktiv
		    if (authenticated) {
		    	mnudashboard.setDisable(false);
		    	mnufinanzverwaltung.setDisable(false);
		    	mnureporting.setDisable(false);
		    	mnucharter.setDisable(false);
		    }
		    
		    if (userobject.getberechtigung() >=2) {
		    	mnuzusatzkosten.setDisable(false);
		    }
		    if (userobject.getberechtigung() == 3) {
		    	mnuadministration.setDisable(false);
		    }
		    	
		    	
		    
		    
		    //conn.close();
		    //
		    } 
	        catch (SQLException sqle) 
	        { 
	        
	        lbl_dbconnect.setText("Datenbankverbindung fehlgeschlagen");
	        //System.out.println("geht nicht");   
	        //sqle.printStackTrace();
	    // Anwendung auch bei fehlenden Berechtigungen freischalten - Beginn
	        apa_login.setVisible(false);
		    apa_welcome.setVisible(true);
		    lbl_username.setText(user);
		    
		    btn_login.setVisible(false);
		    
		    
		    // Vor- und Nachnamen ermitteln
		    int pos = user.indexOf(".");
		    String vorname = user.substring(0, pos);
		    String nachname = user.substring(pos+1,user.length());
		    
		    User userobject = new User(vorname, nachname,"Mitarbeiter",3);
		    authenticated = true;
		    String userrolle = userobject.getrolle();
		    lblrolle.setText(userrolle);
		    lblberechtigung.setText(String.valueOf(userobject.getberechtigung()));	
	        authenticated = true;
	        if (authenticated) {
		    	mnudashboard.setDisable(false);
		    	mnufinanzverwaltung.setDisable(false);
		    	mnureporting.setDisable(false);
		    	mnucharter.setDisable(false);
		    }
		    
		    if (userobject.getberechtigung() >=2) {
		    	mnuzusatzkosten.setDisable(false);
		    }
		    if (userobject.getberechtigung() == 3) {
		    	mnuadministration.setDisable(false);
		    }
	                
	        }    
	        // Anwendung auch bei fehlenden Berechtigungen freischalten - Ende
	}

	private char[] substringBefore(Object setText, String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@FXML public void btn_close_click(ActionEvent event) {
				
		System.exit(0);
	}

	@FXML
	public void actiongetangebote() {
		// lbl_dbconnect.setText("Mouse geklickt!");

		set_allunvisible();
		angebotübersicht.setVisible(true);
		maskentitel.setVisible(true);
		maskentitel.setText("Übersicht Angebote");
		panebtnangebotübersicht.setVisible(true);
		
		try {

			// connect method #1 - embedded driver
			
		    
			
			//String dbURL1 = "jdbc:derby:c:/daten/wirtschaftsinformatik/4. semester/Wirtschaftsinformatikprojekt - Einführung/eigenes Projekt/entwicklung/db/codejava/webdb1;create=true";
			//Connection conn1 = DriverManager.getConnection(dbURL1);
			//if (conn1 != null) {
			//	System.out.println("Connected to database #1");
			//}

			// Statement stmt = conn1.createStatement();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM angebote");
			angebotedata.remove(0, angebotedata.size());
			int i = 1;
			// Testbeginn
			// rs = null;
			 // Testende
			
			while ((rs != null) && (rs.next())) {
				System.out.println(i++ + " " + rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " "
						+ rs.getInt(4) + " " + rs.getString(5) + " " + rs.getInt(6));
				angebotedata.add(new Angebote(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(5)));
			}
			
			if (angebotedata.size()== 0 ) lbl_dbconnect.setText("keine Angebote vorhanden");
			
			if (rs != null) rs.close();
			stmt.close();

			// conn1.close();

		} catch (SQLException ex) {
			lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			ex.printStackTrace();
		}

	}
	
	
	@FXML public void acc_chart_click(MouseEvent event) {}

	@FXML public void hlk_create_offer(ActionEvent event) {
		
		set_allunvisible();
		apa_btn_createoffer.setVisible(true);
		apa_create_offer.setVisible(true);
		apa_btn_createoffer.setVisible(true);
		
		//cbo_salutation.getItems().addAll("Herr","Frau");
		
	}
	
	public void set_allunvisible(){
	
	    apa_login.setVisible(false);
	    apa_welcome.setVisible(false);
	    apa_create_offer.setVisible(false);
	    apa_btn_login.setVisible(false);
	    apa_btn_createoffer.setVisible(false);
	    apa_create_cust.setVisible(false);
	    apa_btn_create_cust.setVisible(false);
	    auftragändernform.setVisible(false);
	    angebotübersicht.setVisible(false);
	    Aufträgeübersicht.setVisible(false);
		auftragübersichtbuttons.setVisible(false);
		apa_charter.setVisible(false);
		lbl_dbconnect.setText("");
		maskentitel.setVisible(false);
		panebtnangebotübersicht.setVisible(false);
		ancpanebtn_createorder.setVisible(false);
		scroll_pane_order.setVisible(false);
		ancpanebtn_changeorder.setVisible(false);
		scroll_pane_changeorder.setVisible(false);
		Rechnungenübersichtbuttons.setVisible(false);
		Rechnungenübersicht.setVisible(false);
		scrollpane_changebillstatus.setVisible(false);
		apa_formchangebillstatus.setVisible(false);
		ancpanebtn_changebillstatus.setVisible(false);
		apa_calendar.setVisible(false);
		apa_search_fh.setVisible(false);
		apa_btn_createoffer.setVisible(false);
		apa_sonder.setVisible(false);
		apa_btn_sonder.setVisible(false);
		apa_zws.setVisible(false);
		apa_btn_zws.setVisible(false);
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
		
		//cbo_salutation_new.getItems().addAll("Herr","Frau");
		//cbo_country_new.getItems().addAll("Germany", "United States", "China");
		//cbo_custstatus_new.getItems().addAll("PRE","CORP","VIP");
		
		
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
	
	@FXML
	public void actiongetaufträge() {
		// lbl_dbconnect.setText("Mouse geklickt!");

		set_allunvisible();
		Aufträgeübersicht.setVisible(true);
		auftragübersichtbuttons.setVisible(true);
		maskentitel.setVisible(true);
		maskentitel.setText("Übersicht Aufträge");
		
		try {

			// connect method #1 - embedded driver
			
		    
			
			//String dbURL1 = "jdbc:derby:c:/daten/wirtschaftsinformatik/4. semester/Wirtschaftsinformatikprojekt - Einführung/eigenes Projekt/entwicklung/db/codejava/webdb1;create=true";
			//Connection conn1 = DriverManager.getConnection(dbURL1);
			//if (conn1 != null) {
			//	System.out.println("Connected to database #1");
			//}

			// Statement stmt = conn1.createStatement();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM angebote");
			auftraegedata.remove(0, auftraegedata.size());
			int i = 1;
			// Testbeginn
			// rs = null;
			 // Testende
			
			while ((rs != null) && (rs.next())) {
				System.out.println(i++ + " " + rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " "
						+ rs.getInt(4) + " " + rs.getString(5) + " " + rs.getInt(6));
				auftraegedata.add(new Aufträge(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(5)));
			}
			
			if (auftraegedata.size()== 0 ) lbl_dbconnect.setText("keine Aufträge vorhanden");
			
			if (rs != null) rs.close();
			stmt.close();

			// conn1.close();

		} catch (SQLException ex) {
			lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			ex.printStackTrace();
		}


		
	/*	try {

		 connect method #1 - embedded driver
			String dbURL1 = "jdbc:derby:c:/daten/wirtschaftsinformatik/4. semester/Wirtschaftsinformatikprojekt - Einführung/eigenes Projekt/entwicklung/db/codejava/webdb1;create=true";
			Connection conn1 = DriverManager.getConnection(dbURL1);
			if (conn1 != null) {
				System.out.println("Connected to database #1");
			}

			Statement stmt = conn1.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Angebote");
			angebotedata.remove(1, angebotedata.size());
			int i = 1;
			while (rs.next()) {
				angebotedata.add(new Angebote(rs.getInt(1), rs.getString(2), rs.getString(5)));
				System.out.println(i++ + " " + rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " "
						+ rs.getString(4) + " " + rs.getString(5) + " " + rs.getString(6) + " " + rs.getString(7) + " "
						+ rs.getString(8) + " " + rs.getDate(9) + " " + rs.getDate(10));
			}

			rs.close();
			stmt.close();

			conn1.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
*/
	}

	@FXML
	public void actiongetrechnungen() {
		// lbl_dbconnect.setText("Mouse geklickt!");

		set_allunvisible();
		Rechnungenübersicht.setVisible(true);
		Rechnungenübersichtbuttons.setVisible(true);
		maskentitel.setVisible(true);
		maskentitel.setText("Übersicht Rechnungen");
		
		try {

			// connect method #1 - embedded driver
			
		    
			
			//String dbURL1 = "jdbc:derby:c:/daten/wirtschaftsinformatik/4. semester/Wirtschaftsinformatikprojekt - Einführung/eigenes Projekt/entwicklung/db/codejava/webdb1;create=true";
			//Connection conn1 = DriverManager.getConnection(dbURL1);
			//if (conn1 != null) {
			//	System.out.println("Connected to database #1");
			//}

			// Statement stmt = conn1.createStatement();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM angebote");
//			auftraegedata.remove(0, auftraegedata.size());
			int i = 1;
			// Testbeginn
			// rs = null;
			 // Testende
			
			while ((rs != null) && (rs.next())) {
				System.out.println(i++ + " " + rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " "
						+ rs.getInt(4) + " " + rs.getString(5) + " " + rs.getInt(6));
				auftraegedata.add(new Aufträge(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(5)));
			}
		
			billdata.remove(0, billdata.size());
			billdata.add(new Rechnungen(30302,"erstellt","2016-05-16",2450.45F,150.00F,15.00F,"PRE"));
			billdata.add(new Rechnungen(30514,"verschickt","2016-05-14",5300.00F,0.00F,0.00F,"CORP"));
						
			
			if (billdata.size()== 0 ) lbl_dbconnect.setText("keine Rechnungen vorhanden");
			
			if (rs != null) rs.close();
			stmt.close();

			// conn1.close();

		} catch (SQLException ex) {
			lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			ex.printStackTrace();
		}


		
	/*	try {

		 connect method #1 - embedded driver
			String dbURL1 = "jdbc:derby:c:/daten/wirtschaftsinformatik/4. semester/Wirtschaftsinformatikprojekt - Einführung/eigenes Projekt/entwicklung/db/codejava/webdb1;create=true";
			Connection conn1 = DriverManager.getConnection(dbURL1);
			if (conn1 != null) {
				System.out.println("Connected to database #1");
			}

			Statement stmt = conn1.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Angebote");
			angebotedata.remove(1, angebotedata.size());
			int i = 1;
			while (rs.next()) {
				angebotedata.add(new Angebote(rs.getInt(1), rs.getString(2), rs.getString(5)));
				System.out.println(i++ + " " + rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " "
						+ rs.getString(4) + " " + rs.getString(5) + " " + rs.getString(6) + " " + rs.getString(7) + " "
						+ rs.getString(8) + " " + rs.getDate(9) + " " + rs.getDate(10));
			}

			rs.close();
			stmt.close();

			conn1.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
*/
	}	
	
	
	
	@FXML
	public void angebotedit_click(ActionEvent event) {

		 // System.out.println(Kdname.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex()));
		set_allunvisible(); 
		auftragändernform.setVisible(true);
		ancpanebtn_changeorder.setVisible(true);
		scroll_pane_changeorder.setVisible(true);
		maskentitel.setVisible(true);
		maskentitel.setText("Auftragstatus ändern");
		choiceorderstatus.getItems().addAll("offen","positiv","negativ");
	  }
/*
	@FXML        
	 
	 angebotetabelle.setRowFactory((TableView) -> {
        TableRow<String> row = new TableRow<>();
        row.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount()>=1 && (!row.isEmpty())) {
               btncreateorder.setDisable(false);
        });
        return row;
    });
	*/
	
/*	@FXML 
	angebotetabelle.setRowFactory( tv -> {
		   TableRow<String> row = new TableRow<>();
		   row.setOnMouseClicked(e -> {
		      if (e.getClickCount() == 2 && (!row.isEmpty()) ) {
		         System.out.println(angebotetabelle.getSelectionModel().getSelectedItem());                   
		      }
		   });
		   return row;
		});
*/

	@FXML
	public void setbtnenable() {
	   	            //   btncreateorder.setDisable(false);
	            }	
	@FXML
	public void createorder(ActionEvent event) {
		set_allunvisible();
		scroll_pane_order.setVisible(true);
		scroll_pane_order.setVvalue(0);;
		ancpane_createorder.setVisible(true);
		ancpanebtn_createorder.setVisible(true);
		maskentitel.setVisible(true);
		maskentitel.setText("Auftrag erstellen");
		
	}

	@FXML
	public void showdocumentdialog(ActionEvent event) throws Exception {
		List<String> choices = new ArrayList<>();
		choices.clear();
		choices.add("PDF");
		choices.add("Word");

		ChoiceDialog<String> dialog1 = new ChoiceDialog<>("PDF", choices);
		dialog1.setTitle("Dokumententyp");
		dialog1.setHeaderText("Bitte Dokumententyp auswählen:");
		dialog1.setContentText("Auswahl:");

		// Traditional way to get the response value.
		Optional<String> result1 = dialog1.showAndWait();
	//	if (result.isPresent()){
		//    System.out.println("Your choice: " + result.get());
//		}

		// The Java 8 way to get the response value (with lambda expression).
		result1.ifPresent(letter -> System.out.println("Your choice: " + letter));
		
		if (result1.isPresent()) {
		AuswahlDokutyp = result1.get();
		}
		
		if (AuswahlDokutyp == "PDF") {
			erzeugePdf();
			}
		
		if (AuswahlDokutyp == "Word") {
			erzeugeWord();
			
		}
		
		choices.clear();
		choices.add("Drucken");
		choices.add("Versenden");
		choices.add("keine Aktion");
		
		ChoiceDialog<String> dialog2 = new ChoiceDialog<>("keine Aktion", choices);
		dialog2.setTitle("weitere Aktionen");
		dialog2.setHeaderText("Bitte wählen Sie \neine weitere Aktion aus:");
		dialog2.setContentText("Auswahl:");

		// Traditional way to get the response value.
		Optional<String> result2 = dialog2.showAndWait();
		result2.ifPresent(letter -> System.out.println("Your choice: " + letter));
		actiongetangebote();
	
	
	
	if (result2.isPresent()) {
		AuswahlAktion = result2.get();
		}
		
	if (AuswahlAktion == "Drucken") {


	PDFPrinter druck = new PDFPrinter(f);
	lbl_dbconnect.setText("Ausdruck gestartet");
	
	}
	if (AuswahlAktion == "Versenden") {

		
		String Kunde = "Burggraf";
		int Nummer = 100302;
		String Datum = "10.06.2016";
		Mainmail mail = new Mainmail(Kunde,Nummer,Datum);
		
		}
	
	
	}
	
	
    
	
	
	@FXML
	public void change_billstatus(ActionEvent event) {
		set_allunvisible();
		scrollpane_changebillstatus.setVisible(true);
		scrollpane_changebillstatus.setVvalue(0);;
		apa_formchangebillstatus.setVisible(true);
		ancpanebtn_changebillstatus.setVisible(true);
		maskentitel.setVisible(true);
		maskentitel.setText("Rechnungsstatus ändern");
	}
	
	public static void erzeugePdf() throws Exception {
		// step 1
				Document document = new Document(PageSize.A4);
				document.setMargins(50f, 40f, 50f, 40f);
				// step 2
				String filename = System.getProperty("user.dir") + "/test.pdf";
				PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(filename));
				// nur für die Möglichkeit, dass wir einen Rahmen zeichnen können
				ParagraphBorder border = enableBordering(pdfWriter);
				// step 3
				document.open();

				// step 4
				Font styleTitel = new Font(FontFamily.HELVETICA);
				styleTitel.setSize(18);
				styleTitel.setStyle(FontStyle.BOLD.name());

				Font styleUeberschrift1 = new Font(FontFamily.HELVETICA);
				styleUeberschrift1.setSize(12);
				styleUeberschrift1.setColor(BaseColor.BLUE);
				styleUeberschrift1.setStyle(FontStyle.BOLD.name());

				Font styleUeberschrift2 = new Font(FontFamily.HELVETICA);
				styleUeberschrift2.setSize(14);
				styleUeberschrift2.setColor(BaseColor.BLUE);
				styleUeberschrift2.setStyle(FontStyle.NORMAL.name());

				Font styleText = new Font(FontFamily.HELVETICA);
				styleText.setSize(12);
				styleText.setStyle(FontStyle.NORMAL.name());

				Font styleTextunderline = new Font(FontFamily.HELVETICA);
				styleText.setSize(12);
				styleText.setStyle(FontStyle.UNDERLINE.name());

				Font styleFettText = new Font(FontFamily.HELVETICA);
				styleFettText.setSize(12);
				styleFettText.setStyle(FontStyle.BOLD.name());

				Font styleRot = new Font(FontFamily.HELVETICA);
				styleRot.setSize(12);
				styleRot.setColor(BaseColor.RED);
				styleRot.setStyle(FontStyle.BOLD.name());

				Font styleKursiv = new Font(FontFamily.HELVETICA);
				styleKursiv.setSize(12);
				styleKursiv.setStyle(FontStyle.ITALIC.name());

				Image image = Image.getInstance(PdfGenerator.class.getResource("logo2.jpg"));
				image.scaleAbsolute(507,40);
				document.add(image);
				// Dokumententitel (mit Rahmen!)
				border.setActive(true);
				Paragraph p = new Paragraph("", styleTitel);
				p.setAlignment(Element.ALIGN_LEFT);
				document.add(p);
				border.setActive(false);

				
			

				p = new Paragraph(" ", styleText);
				p.setAlignment(Element.ALIGN_CENTER);
				// etwas abstand hinter der überschrift
				p.setSpacingAfter(6f);
				document.add(p);p = new Paragraph("Chartervertrag", styleUeberschrift1);
				p.setAlignment(Element.ALIGN_CENTER);
				// etwas abstand hinter der überschrift
				p.setSpacingAfter(6f);
				document.add(p);
				p = new Paragraph("zwischen", styleText);
				p.setAlignment(Element.ALIGN_CENTER);
				// etwas abstand hinter der überschrift
				p.setSpacingAfter(6f);
				document.add(p);
				p = new Paragraph("HINOTORI Executive AG (Auftragnehmer)", styleUeberschrift1);
				p.setAlignment(Element.ALIGN_CENTER);
				// etwas abstand hinter der überschrift
				p.setSpacingAfter(6f);
				document.add(p);
				p = new Paragraph("und", styleText);
				p.setAlignment(Element.ALIGN_CENTER);
				// etwas abstand hinter der überschrift
				p.setSpacingAfter(6f);
				document.add(p);
				p = new Paragraph("Firma oder Person", styleUeberschrift1);
				p.setAlignment(Element.ALIGN_CENTER);
				// etwas abstand hinter der überschrift
				p.setSpacingAfter(20f);
				document.add(p);
				
				String AG = "Erich";
				String Typ = "Dornier";
				String Kennzeichen = "120";
				String Beginndatum = "20.05.2016";
				String Endedatum = "01.06.2016";
				String FlugAnfang = "München";
				String FlugEnde = "New York";
				String Zwischen1 = "Paris";
				String Zwischen2 = "London";
				String Zwischen3 = "Reykjavík";
				String Charterdauer = "124:30 h";
				String Flugzeit = "24:45";
				String Preisnetto = "1.450,00 EUR";
				String Mwst = "275,50 EUR";
				String Preisbrutto = "1.725,50 EUR";
				
				p = new Paragraph(
						AG+" chartert das Luftfahrzeug "+Typ+" "+Kennzeichen+" für die Zeit vom "+Beginndatum+" zum "+Endedatum+" zu einer Reise von "+FlugAnfang+" nach "+FlugEnde+" über "+Zwischen1+", "+Zwischen2+", "+Zwischen3+".",styleText);
				p.setAlignment(Element.ALIGN_JUSTIFIED);
				// zeilenabstand kleiner wählen
				p.setLeading(15f);
				p.setSpacingAfter(20f);
				document.add(p);

				p = new Paragraph("Flugplan:", styleUeberschrift2);
				p.setAlignment(Element.ALIGN_LEFT);
				// etwas abstand hinter der überschrift
				p.setSpacingAfter(6f);
				document.add(p);

				
				/*p = new Paragraph();
				p.add(new Chunk("Dies ist ein ", styleText));
				p.add(new Chunk("Satz", styleRot));
				p.add(new Chunk(" mit unterschiedlich formatierten ", styleFettText));
				p.add(new Chunk("Wörter", styleKursiv));
				p.add(new Chunk(".", styleText));
				// zeilenabstand kleiner wählen
				p.setSpacingBefore(6f);
				p.setAlignment(Element.ALIGN_JUSTIFIED);
				// zeilenabstand kleiner wählen
				p.setLeading(15f);
				document.add(p);
		*/
				addTable(document);

				p = new Paragraph(" ", styleText);
				p.setAlignment(Element.ALIGN_LEFT);
				// etwas abstand hinter dem Text
				p.setSpacingAfter(6f);
				document.add(p);
				
				p = new Paragraph();
				p.setTabSettings(new TabSettings(35f));
		        p.add(new Chunk("Charterdauer insgesamt (Stunden): ", styleText));
				p.add(Chunk.TABBING);
				
				
				p.add(new Chunk(Charterdauer, styleText));
				
				// etwas abstand hinter dem Text
				p.setSpacingAfter(15f);
				document.add(p);
				
				p = new Paragraph();
				p.setTabSettings(new TabSettings(35f));
		        p.add(new Chunk("Davon Flugzeit (h/min): ", styleText));
				p.add(Chunk.TABBING);
				p.add(Chunk.TABBING);
				p.add(Chunk.TABBING);
				
				p.add(new Chunk(Flugzeit, styleText));
				
				// etwas abstand hinter dem Text
				p.setSpacingAfter(15f);
				document.add(p);
				

				p = new Paragraph();
				p.setTabSettings(new TabSettings(35f));
		        p.add(new Chunk("Gesamtpreis netto (EUR): ", styleText));
				p.add(Chunk.TABBING);
				p.add(Chunk.TABBING);
				
				p.add(new Chunk(Preisnetto, styleText));
				
				// etwas abstand hinter dem Text
				p.setSpacingAfter(6f);
				document.add(p);
				
				
				
				p = new Paragraph();
				p.setTabSettings(new TabSettings(35f));
		        p.add(new Chunk("19 % Mwst: ", styleText));
				p.add(Chunk.TABBING);
				p.add(Chunk.TABBING);
				p.add(Chunk.TABBING);
				p.add(Chunk.TABBING);
				p.add(Chunk.TABBING);
				
				p.add(new Chunk(Mwst, styleText));
				p.setAlignment(Element.ALIGN_LEFT);
				// etwas abstand hinter dem Text
				p.setSpacingAfter(6f);
				document.add(p);
				
				p = new Paragraph();
				p.setTabSettings(new TabSettings(35f));
		        p.add(new Chunk("Gesamtpreis brutto (EUR): ", styleText));
				p.add(Chunk.TABBING);
				p.add(Chunk.TABBING);
				
				
				p.add(new Chunk(Preisbrutto, styleText));
				
				// etwas abstand hinter dem Text
				p.setSpacingAfter(25f);
				document.add(p);
				
				
				p = new Paragraph();
				p.setTabSettings(new TabSettings(35f));
		        p.add(new Chunk("Datum, Ort", styleText));
				p.add(Chunk.TABBING);
				p.add(Chunk.TABBING);
				p.add(Chunk.TABBING);
				p.add(Chunk.TABBING);
				p.add(Chunk.TABBING);
				p.add(Chunk.TABBING);
				p.add(Chunk.TABBING);
				
				p.add(new Chunk("Datum, Ort", styleText));
				p.setSpacingAfter(25);
				document.add(p);
				
				p = new Paragraph();
				p.setTabSettings(new TabSettings(35f));
				Chunk underline = new Chunk("                                                  ");
				underline.setUnderline(0.3f, -2f); //0.1 thick, -2 y-location
				p.add(underline);
				p.add(Chunk.TABBING);
				p.add(Chunk.TABBING);
				p.add(Chunk.TABBING);
				p.add(Chunk.TABBING);
				p.add(underline);
				p.setSpacingAfter(25);
				document.add(p);
				
				p = new Paragraph();
				p.setTabSettings(new TabSettings(35f));
		        p.add(new Chunk("HINOTORI Executive AG", styleText));
				p.add(Chunk.TABBING);
				p.add(Chunk.TABBING);
				p.add(Chunk.TABBING);
				p.add(Chunk.TABBING);
				p.add(Chunk.TABBING);
				
				p.add(new Chunk("Auftraggeber",styleText));
				p.setAlignment(Element.ALIGN_LEFT);
				// etwas abstand hinter dem Text
				p.setSpacingAfter(6f);
				document.add(p);
				
				// step 5
				document.close();

				File file = new File(filename);
				System.out.println("Saved " + file.getCanonicalPath());
			}

			private static ParagraphBorder enableBordering(PdfWriter pdfWriter) {
				ParagraphBorder border = new ParagraphBorder();
				pdfWriter.setPageEvent(border);
				return border;
			}

			private static void addTable(Document document) throws DocumentException {
				document.add(getSampleTable());
			}
			String AG = "Erich";
			String Typ = "Dornier";
			String Kennzeichen = "120";
			String Beginndatum = "20.05.2016";
			String Endedatum = "01.06.2016";
			String FlugAnfang = "München";
			String FlugEnde = "New York";
			String Zwischen1 = "Paris";
			String Zwischen2 = "London";
			String Zwischen3 = "Reykjavík";
			
			private static String[] SPALTENKOPF = new String[] { "Datum", "Zeit \n(Abflug)", "Ort \n(von)","Flugzeit","Zeit (Ankunft)","Ort \n(nach)","Anzahl Passagiere"};

			private static String[][] DATEN = new String[][] { { "20.05.2016","08:00","München","1:30","09:30","Paris","3" },
				{ "21.05.2016","15:00","Paris","2:00","17:00","London","2" }, { "22.05.2016","09:00","London","2:00","11:00","Reykjavik","5" } };

			private static PdfPTable getSampleTable() throws DocumentException {
				int rows = DATEN.length;
				int cols = SPALTENKOPF.length;
				PdfPTable table = new PdfPTable(cols);
				table.setSpacingBefore(16f);
				table.setWidthPercentage(100);
				

				// Spaltenkopf
				for (int colIndex = 0; colIndex < cols; colIndex++) {
					Phrase phrase = new Phrase(SPALTENKOPF[colIndex]);
					PdfPCell cell = new PdfPCell(phrase);
					// 0 = schwarz, 1 = weiß
					cell.setGrayFill(0.8f);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell);
					float[] columnWidths = new float[] {9f, 7f, 10f, 7f, 7f, 10f, 9f};
			        table.setWidths(columnWidths);
				}

				// mit Daten auffüllen
				for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
					// wegen Tabellenkopf eine Zeile tiefer anfangen
					for (int colIndex = 0; colIndex < cols; colIndex++) {
						table.addCell(DATEN[rowIndex][colIndex]);
					}
				}
				return table;
			}

			public static void erzeugeWord() throws Exception {
				factory = Context.getWmlObjectFactory();

				WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
				MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();

				// mann kann die "vordefinierten" Styles ausgeben, diese wären
				// Kandidaten für solche Konstanten dann, wie WORD_STYLE_TITLE

			//	 Set<String> styles = StyleDefinitionsPart.getKnownStyles().keySet();
			 //	System.out.println(Arrays.deepToString(styles.toArray()));

				Style styleTitel = StyleDefinitionsPart.getKnownStyles().get(WORD_STYLE_TITLE);
				Style styleUeberschrift1 = StyleDefinitionsPart.getKnownStyles().get(WORD_STYLE_HEADING1);
				Style styleUeberschrift2 = StyleDefinitionsPart.getKnownStyles().get(WORD_STYLE_HEADING2);
				// under construction Style orgStyle = createStyle(StyleDefinitionsPart.getKnownStyles().get(WORD_STYLE_HEADING2), " ", 14, true, JcEnumeration.CENTER);
				// Logo einfügen
				addLogo(mdp, wordMLPackage);
				
				
				// hier habe ich die Inhalte ins Dokument eingefügt
				//mdp.addStyledParagraphOfText(styleTitel.getStyleId(), "");

				
				
				//centerParagraph(mdp.addParagraphOfText(
				//		"Ganz normaler Text."));
				centerParagraph(mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), "Chartervertrag"));
				doBoldFormat(getFirstRunOfParagraph(getLastParagraph(mdp)));
				centerParagraph(mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), "zwischen"));
				centerParagraph(mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), "HINOTORI Executive AG (Auftragnehmer)"));
				doBoldFormat(getFirstRunOfParagraph(getLastParagraph(mdp)));
				centerParagraph(mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), "und"));
				centerParagraph(mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), "Firma oder Person"));
				doBoldFormat(getFirstRunOfParagraph(getLastParagraph(mdp)));
				
				String AG = "Erich";
				String Typ = "Dornier";
				String Kennzeichen = "120";
				String Beginndatum = "20.05.2016";
				String Endedatum = "01.06.2016";
				String Anfang = "München";
				String Ende = "New York";
				String Zwischen1 = "Paris";
				String Zwischen2 = "London";
				String Zwischen3 = "Reykjavík";
				String Charterdauer = "124:30 h";
				String Flugzeit = "24:45";
				String Preisnetto = "1.450,00 EUR";
				String Mwst = "275,50 EUR";
				String Preisbrutto = "1.725,50 EUR";
				
				mdp.addParagraphOfText(AG+" chartert das Luftfahrzeug "+Typ+" "+Kennzeichen+" für die Zeit vom "+Beginndatum+" zum "+Endedatum+" zu einer Reise von "+Anfang+" nach "+Ende+" über "+Zwischen1+", "+Zwischen2+", "+Zwischen3+".");
				mdp.addStyledParagraphOfText(styleUeberschrift2.getStyleId(), "Flugplan:");
						
				// das hier zeigt, wie ein ganzer Paragraph relativ einfach fett gemacht werden kann
				//mdp.addParagraphOfText("Fetter Text. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam.");
				// hier nehme ich den letzten Paragraphen des Dokumentes (der zuletzt ins Dokument eingefügte Paragraph)
				// und mach ihn bzw. seine erste Passage fett
				doBoldFormat(getFirstRunOfParagraph(getLastParagraph(mdp)));

				// hier schreiben wir einen Satz über die komplexe Art und weise, weil einige Wörter anders formattiert sein müssen
				// addMixedStyleParagraph(mdp);

				// Tabelle
				addTable(mdp, wordMLPackage);

				mdp.addParagraphOfText("");
				mdp.addParagraphOfText("Charterdauer insgesamt (Stunden): \t\t\t\t"+Charterdauer);
				
				mdp.addParagraphOfText("Davon Flugzeit (h/min): \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+Flugzeit);
				mdp.addParagraphOfText("");
				mdp.addParagraphOfText("Gesamtpreis netto (EUR): \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+Preisnetto);
				mdp.addParagraphOfText("19 % Mwst: \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+Mwst);
				mdp.addParagraphOfText("Gesamtpreis brutto (EUR): \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+Preisbrutto);
				doBoldFormat(getFirstRunOfParagraph(getLastParagraph(mdp)));
				mdp.addParagraphOfText("");
				mdp.addParagraphOfText("Datum, Ort                                                                                               Datum, Ort");
				mdp.addParagraphOfText("");
				mdp.addParagraphOfText("HINOTORI Executive AG                                                                         Auftraggeber");
				
				
				
				// speichern
				String filename = System.getProperty("user.dir") + "/test.docx";
				File file = new java.io.File(filename);
				Docx4J.save(wordMLPackage, file, Docx4J.FLAG_SAVE_ZIP_FILE);
				System.out.println("Saved " + file.getCanonicalPath());
			}

			private static R getFirstRunOfParagraph(P lastParagraph) {
				return (R) lastParagraph.getContent().get(0);
			}

			private static P getLastParagraph(MainDocumentPart mdp) {
				int lastContentItem = mdp.getContent().size() - 1;
				P lastParagraph = (P) mdp.getContent().get(lastContentItem);
				return lastParagraph;
			}

			private static void addLogo(MainDocumentPart mdp, WordprocessingMLPackage wordMLPackage) throws Exception {
				InputStream inputStream = WordGenerator.class.getResourceAsStream("logo2.jpg");
				long fileLength = 94712; // 97kB

				byte[] bytes = new byte[(int) fileLength];

				int offset = 0;
				int numRead = 0;

				while (offset < bytes.length && (numRead = inputStream.read(bytes, offset, bytes.length - offset)) >= 0) {
					offset += numRead;
				}

				inputStream.close();

				String filenameHint = "FreeLogo";
				String altText = "Einfach nur ein Logo";

				int id1 = 0;
				int id2 = 1;

				P p = newImage(wordMLPackage, bytes, filenameHint, altText, id1, id2);

				mdp.addObject(p);
			}

			public static P newImage(WordprocessingMLPackage wordMLPackage, byte[] bytes, String filenameHint, String altText,
					int id1, int id2) throws Exception {
				BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);
				Inline inline = imagePart.createImageInline(filenameHint, altText, id1, id2, 9100, false);
				
				
				P p = factory.createP();

				R run = factory.createR();
				p.getContent().add(run);

				org.docx4j.wml.Drawing drawing = factory.createDrawing();
				run.getContent().add(drawing);
				drawing.getAnchorOrInline().add(inline);

				return p;
			}

			private static void addTable(MainDocumentPart mdp, WordprocessingMLPackage wordMLPackage) {
				mdp.addObject(getSampleTable(wordMLPackage));
			}

				private static String[] SPALTENKOPFword = new String[] { "Datum", "Zeit \n(Abflug)", "Ort \n(von)","Flugzeit","Zeit (Ankunft)","Ort \n(nach)","Anzahl Passagiere"};


				private static String[][] DATENword = new String[][] { { "20.05.2016","08:00","München","1:30","09:30","Paris","3" },
					{ "21.05.2016","15:00","Paris","2:00","17:00","London","2" }, { "22.05.2016","09:00","London","2:00","11:00","Reykjavik","5" } };
				@FXML Button btn_create_offer;
				@FXML AnchorPane apa_sonder;
				@FXML ListView lst_crew;
				@FXML TextArea txa_getr;
				@FXML TextArea txa_speisen;
				@FXML CheckBox chb_getr;
				@FXML CheckBox chb_speisen;
				@FXML ComboBox cbo_fz;
				@FXML ComboBox cbo_cap;
				@FXML ComboBox cbo_cop;
				@FXML TextField txt_kostensw;
				@FXML AnchorPane apa_btn_sonder;
				@FXML Button btn_sonder_stop;
				@FXML Button btn_sonder_ok;
				@FXML TextField txt_pax_sw;
				@FXML ComboBox cbo_fa1;
				@FXML ComboBox cbo_fa2;
				@FXML ComboBox cbo_fa3;
				@FXML AnchorPane apa_zws;
				@FXML Button btn_zws_save;
				@FXML Button btn_zwscount;
				@FXML DatePicker dpi_zws_ab;
				@FXML Button btn_fh_zws;
				@FXML DatePicker dpi_zws_an;
				@FXML TextField txt_fh_zws;
				@FXML ComboBox cbo_zws;
				@FXML TextField txt_countzws;
				@FXML AnchorPane apa_btn_zws;
				@FXML Button btn_zws_stop;
				@FXML Button btn_zws_ok;
				@FXML TextField txt_zwsab_m;
				@FXML TextField txt_zwsab_h;
				@FXML TextField txt_zwsan_h;
				@FXML TextField txt_zwsan_m;
		
				

			private static Tbl getSampleTable(WordprocessingMLPackage wPMLpackage) {
				int writableWidthTwips = wPMLpackage.getDocumentModel().getSections().get(0).getPageDimensions()
						.getWritableWidthTwips();

				int rows = DATENword.length;
				int cols = SPALTENKOPFword.length;
				int cellWidthTwips = new Double(Math.floor((writableWidthTwips / cols))).intValue();

				// wegen Tabellenkopf eine Zeile mehr
				Tbl table = TblFactory.createTable(1 + rows, cols, cellWidthTwips);

				Tr headerRow = (Tr) table.getContent().get(0);

				// Spaltenkopf erstellen
				for (int colIndex = 0; colIndex < cols; colIndex++) {
					Tc column = (Tc) headerRow.getContent().get(colIndex);

					// grauer Hintergrund
					TcPr tcpr = factory.createTcPr();
					CTShd shd = factory.createCTShd();
					shd.setColor("auto");
					shd.setFill("E7E6E6");
					tcpr.setShd(shd);
					column.setTcPr(tcpr);

					P columnPara = (P) column.getContent().get(0);

					// zentriert darstellen
					centerParagraph(columnPara);

					Text text = factory.createText();
					text.setValue(SPALTENKOPFword[colIndex]);
					R run = factory.createR();
					run.getContent().add(text);

					// fettschrift
					doBoldFormat(run);

					columnPara.getContent().add(run);
				}

				// mit Daten auffüllen
				for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
					// wegen Tabellenkopf eine Zeile tiefer anfangen
					Tr row = (Tr) table.getContent().get(rowIndex + 1);

					for (int colIndex = 0; colIndex < cols; colIndex++) {
						Tc column = (Tc) row.getContent().get(colIndex);

						P columnPara = (P) column.getContent().get(0);

						Text tx = factory.createText();
						R run = factory.createR();
						tx.setValue(DATENword[rowIndex][colIndex]);
						run.getContent().add(tx);
						columnPara.getContent().add(run);
					}
				}
				return table;
			}

			private static void centerParagraph(P columnPara) {
				PPr paragraphProperties = factory.createPPr();
				Jc justification = factory.createJc();
				justification.setVal(JcEnumeration.CENTER);
				paragraphProperties.setJc(justification);
								

				RPr rpr = factory.createRPr();
				columnPara.setPPr(paragraphProperties);
			}

			private static void addMixedStyleParagraph(MainDocumentPart mdp) {
				org.docx4j.wml.P p = factory.createP();

				// 1. Bestandteil des Satzes
				p.getContent().add(createRun("Dies ist ein "));

				// 2. Bestandteil des Satzes
				org.docx4j.wml.R run2 = createRun("Satz");
				p.getContent().add(run2);
				doBoldRedFormat(run2);

				// 3. Bestandteil des Satzes
				p.getContent().add(createRun(" mit unterschiedlich formattierten "));

				// 4. Bestandteil des Satzes
				org.docx4j.wml.R run4 = createRun("Wörter");
				p.getContent().add(run4);
				doItalicsFormat(run4);

				// 5. Bestandteil des Satzes
				p.getContent().add(createRun("."));

				// Add it to the doc
				mdp.addObject(p);
			}

			private static void doItalicsFormat(R run) {
				org.docx4j.wml.RPr rpr = factory.createRPr();

				// kursiv
				org.docx4j.wml.BooleanDefaultTrue b = new org.docx4j.wml.BooleanDefaultTrue();
				b.setVal(true);
				rpr.setI(b);

				run.setRPr(rpr);
			}

			private static void doBoldFormat(org.docx4j.wml.R run) {
				org.docx4j.wml.RPr rpr = factory.createRPr();

				// fettschrift
				org.docx4j.wml.BooleanDefaultTrue b = new org.docx4j.wml.BooleanDefaultTrue();
				b.setVal(true);
				rpr.setB(b);
				
				
				run.setRPr(rpr);
			}

			private static void doBoldRedFormat(org.docx4j.wml.R run) {
				org.docx4j.wml.RPr rpr = factory.createRPr();

				// fettschrift
				org.docx4j.wml.BooleanDefaultTrue b = new org.docx4j.wml.BooleanDefaultTrue();
				b.setVal(true);
				rpr.setB(b);

				// rote farbe
				org.docx4j.wml.Color c = new org.docx4j.wml.Color();
				c.setVal("#FF0000");
				rpr.setColor(c);

				run.setRPr(rpr);
			}

			private static org.docx4j.wml.R createRun(String content) {
				org.docx4j.wml.Text t = factory.createText();
				t.setSpace("preserve");
				t.setValue(content);
				org.docx4j.wml.R run = factory.createR();
				run.getContent().add(t);
				return run;
			}




			@FXML public void action_get_calendar() {
				
				set_allunvisible(); 
				apa_calendar.setVisible(true);
				dap_cal.setValue(LocalDate.now());
				setCal();
				
				
				
			}



			@FXML public void btn_cal_click() {
				
				
				//dap_cal.setValue(LocalDate.now());
				setCal();
				//dap_cal.setValue(value);
				
			}
						
			public void setCal(){
				
				LocalDate Mon = dap_cal.getValue();
				LocalDate Die = dap_cal.getValue();
				LocalDate Mit = dap_cal.getValue();
				LocalDate Don = dap_cal.getValue();
				LocalDate Fre = dap_cal.getValue();
				LocalDate Sam = dap_cal.getValue();
				LocalDate Son = dap_cal.getValue();
				
				DayOfWeek Tag;
//				String Str_Datum_Mon;
//				String Str_Datum_Die;
				String Str_Tag = "";
				String MA_Art = "";
				String MA_Name = "";
				String MA_Vorname= "";
				String datum_von = "";
				String datum_bis = "";
				String uhr_von = "";
				String uhr_bis = "";
				//Mon = dap_cal.getValue();
				//Str_Datum_Mon = Datum.toString();
				String cal_where = "";
				//String sqlstat = "SELECT * FROM (SELECT * FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von='" + cal_where + "' UNION SELECT  * FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von='" + cal_where + "')dt ORDER BY Uhrzeit_von, Uhrzeit_bis DESC";
				
				String Ausgabe = "";
				Tag = dap_cal.getValue().getDayOfWeek();
				Str_Tag = Tag.toString();
			
				System.out.println(Str_Tag);
				if (Str_Tag.equals("MONDAY")){
					
					Die = dap_cal.getValue().plusDays(1);
					Mit = dap_cal.getValue().plusDays(2);
					Don = dap_cal.getValue().plusDays(3);
					Fre = dap_cal.getValue().plusDays(4);
					Sam = dap_cal.getValue().plusDays(5);
					Son = dap_cal.getValue().plusDays(6);
				}
				if (Str_Tag.equals("TUESDAY")){
					
					Mon = dap_cal.getValue().minusDays(1);
					Mit = dap_cal.getValue().plusDays(1);
					Don = dap_cal.getValue().plusDays(2);
					Fre = dap_cal.getValue().plusDays(3);
					Sam = dap_cal.getValue().plusDays(4);
					Son = dap_cal.getValue().plusDays(5);
				}
				if (Str_Tag.equals("WEDNESDAY")){
					
					Mon = dap_cal.getValue().minusDays(2);
					Die = dap_cal.getValue().minusDays(1);
					Don = dap_cal.getValue().plusDays(1);
					Fre = dap_cal.getValue().plusDays(2);
					Sam = dap_cal.getValue().plusDays(3);
					Son = dap_cal.getValue().plusDays(4);
				}
				if (Str_Tag.equals("THURSDAY")){
					
					Mon = dap_cal.getValue().minusDays(3);
					Die = dap_cal.getValue().minusDays(2);
					Mit = dap_cal.getValue().minusDays(1);
					Fre = dap_cal.getValue().plusDays(1);
					Sam = dap_cal.getValue().plusDays(2);
					Son = dap_cal.getValue().plusDays(3);
				}
				if (Str_Tag.equals("FRIDAY")){
					
					Mon = dap_cal.getValue().minusDays(4);
					Die = dap_cal.getValue().minusDays(3);
					Mit = dap_cal.getValue().minusDays(2);
					Don = dap_cal.getValue().minusDays(1);
					Sam = dap_cal.getValue().plusDays(2);
					Son = dap_cal.getValue().plusDays(3);
				}
				if (Str_Tag.equals("SATURDAY")){
					
					Mon = dap_cal.getValue().minusDays(5);
					Die = dap_cal.getValue().minusDays(4);
					Mit = dap_cal.getValue().minusDays(3);
					Don = dap_cal.getValue().minusDays(2);
					Fre = dap_cal.getValue().minusDays(1);
					Son = dap_cal.getValue().plusDays(3);
				}
				if (Str_Tag.equals("SUNDAY")){
					
					Mon = dap_cal.getValue().minusDays(6);
					Die = dap_cal.getValue().minusDays(5);
					Mit = dap_cal.getValue().minusDays(4);
					Don = dap_cal.getValue().minusDays(3);
					Fre = dap_cal.getValue().minusDays(2);
					Sam = dap_cal.getValue().minusDays(1);
				}

				
				lbl_mon.setText(Mon.toString());
				lbl_die.setText(Die.toString());
				lbl_mit.setText(Mit.toString());
				lbl_don.setText(Don.toString());
				lbl_fre.setText(Fre.toString());
				lbl_sam.setText(Sam.toString());
				lbl_son.setText(Son.toString());
			
				
				
				try { 
					
					
					Statement statement_cal = conn.createStatement();
					
					
					// Termine für Montag
					cal_where = Mon.toString();
					String sqlstat = "SELECT * FROM (SELECT * FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  * FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt ORDER BY Uhrzeit_von, Uhrzeit_bis DESC";
					System.out.println(cal_where);
					System.out.println(sqlstat);
			    	ResultSet rs_mon = statement_cal.executeQuery(sqlstat);
			    	while (rs_mon.next())
						{
						MA_Art = rs_mon.getString(1);
						datum_von = rs_mon.getString(3);
						datum_bis = rs_mon.getString(4);
						uhr_von = rs_mon.getString(5);
						uhr_bis = rs_mon.getString(6);
						MA_Name = rs_mon.getString(8);
						MA_Vorname = rs_mon.getString(9);
						
						if (datum_von.equals(datum_bis)){
							if (uhr_von.equals("00:00:00") && uhr_bis.equals("23:59:59")){
								Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n   ganztägig \n ------------------------------- \n";
								}
								else{
									Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n  " + uhr_von + " bis\n  " + uhr_bis +" \n ------------------------------- \n";
								}
						}
						else{
							if (uhr_von.equals("00:00:00") && uhr_bis.equals("23:59:59")){
								Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n   ganztägig von \n         " + datum_von + "\n   bis " + datum_bis + " \n ------------------------------- \n";
								}
								else{
									Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n   " + datum_von + " " + uhr_von + " bis\n   " + datum_bis + " " + uhr_bis +" \n ------------------------------- \n";
								}
							
						}
						}
			    	txt_mon0.setText(Ausgabe);
			    	Ausgabe = "";
			    	
			    	//Daten für Dienstag
					cal_where = Die.toString();
					sqlstat = "SELECT * FROM (SELECT * FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  * FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt ORDER BY Uhrzeit_von, Uhrzeit_bis DESC";
			    	ResultSet rs_die = statement_cal.executeQuery(sqlstat);
			    	while (rs_die.next())
						{
						MA_Art = rs_die.getString(1);
						datum_von = rs_die.getString(3);
						datum_bis = rs_die.getString(4);
						uhr_von = rs_die.getString(5);
						uhr_bis = rs_die.getString(6);
						MA_Name = rs_die.getString(8);
						MA_Vorname = rs_die.getString(9);
						
						if (datum_von.equals(datum_bis)){
							if (uhr_von.equals("00:00:00") && uhr_bis.equals("23:59:59")){
								Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n   ganztägig \n ------------------------------- \n";
								}
								else{
									Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n  " + uhr_von + " bis\n  " + uhr_bis +" \n ------------------------------- \n";
								}
						}
						else{
							if (uhr_von.equals("00:00:00") && uhr_bis.equals("23:59:59")){
								Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n   ganztägig von \n         " + datum_von + "\n   bis " + datum_bis + " \n ------------------------------- \n";
								}
								else{
									Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n   " + datum_von + " " + uhr_von + " bis\n   " + datum_bis + " " + uhr_bis +" \n ------------------------------- \n";
								}
							
						}
						}
					txt_tue0.setText(Ausgabe);
					Ausgabe = "";
					
					//Daten für Mittwoch
					cal_where = Mit.toString();
					sqlstat = "SELECT * FROM (SELECT * FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  * FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt ORDER BY Uhrzeit_von, Uhrzeit_bis DESC";
			    	ResultSet rs_mit = statement_cal.executeQuery(sqlstat);
			    	while (rs_mit.next())
						{
						MA_Art = rs_mit.getString(1);
						datum_von = rs_mit.getString(3);
						datum_bis = rs_mit.getString(4);
						uhr_von = rs_mit.getString(5);
						uhr_bis = rs_mit.getString(6);
						MA_Name = rs_mit.getString(8);
						MA_Vorname = rs_mit.getString(9);
						
						if (datum_von.equals(datum_bis)){
							if (uhr_von.equals("00:00:00") && uhr_bis.equals("23:59:59")){
								Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n   ganztägig \n ------------------------------- \n";
								}
								else{
									Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n  " + uhr_von + " bis\n  " + uhr_bis +" \n ------------------------------- \n";
								}
						}
						else{
							if (uhr_von.equals("00:00:00") && uhr_bis.equals("23:59:59")){
								Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n   ganztägig von \n         " + datum_von + "\n   bis " + datum_bis + " \n ------------------------------- \n";
								}
								else{
									Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n   " + datum_von + " " + uhr_von + " bis\n   " + datum_bis + " " + uhr_bis +" \n ------------------------------- \n";
								}
							
						}
						}
					txt_wed0.setText(Ausgabe);
					Ausgabe = "";
					
					//Daten für Donnerstag
					cal_where = Don.toString();
					sqlstat = "SELECT * FROM (SELECT * FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  * FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt ORDER BY Uhrzeit_von, Uhrzeit_bis DESC";
			    	ResultSet rs_don = statement_cal.executeQuery(sqlstat);
			    	while (rs_don.next())
						{
						MA_Art = rs_don.getString(1);
						datum_von = rs_don.getString(3);
						datum_bis = rs_don.getString(4);
						uhr_von = rs_don.getString(5);
						uhr_bis = rs_don.getString(6);
						MA_Name = rs_don.getString(8);
						MA_Vorname = rs_don.getString(9);
						
						if (datum_von.equals(datum_bis)){
							if (uhr_von.equals("00:00:00") && uhr_bis.equals("23:59:59")){
								Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n   ganztägig \n ------------------------------- \n";
								}
								else{
									Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n  " + uhr_von + " bis\n  " + uhr_bis +" \n ------------------------------- \n";
								}
						}
						else{
							if (uhr_von.equals("00:00:00") && uhr_bis.equals("23:59:59")){
								Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n   ganztägig von \n         " + datum_von + "\n   bis " + datum_bis + " \n ------------------------------- \n";
								}
								else{
									Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n   " + datum_von + " " + uhr_von + " bis\n   " + datum_bis + " " + uhr_bis +" \n ------------------------------- \n";
								}
							
						}
						}
					txt_thu0.setText(Ausgabe);
					Ausgabe = "";
					
					//Daten für Freitag
					cal_where = Fre.toString();
					sqlstat = "SELECT * FROM (SELECT * FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  * FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt ORDER BY Uhrzeit_von, Uhrzeit_bis DESC";
			    	ResultSet rs_fre = statement_cal.executeQuery(sqlstat);
			    	while (rs_fre.next())
						{
						MA_Art = rs_fre.getString(1);
						datum_von = rs_fre.getString(3);
						datum_bis = rs_fre.getString(4);
						uhr_von = rs_fre.getString(5);
						uhr_bis = rs_fre.getString(6);
						MA_Name = rs_fre.getString(8);
						MA_Vorname = rs_fre.getString(9);
						
						if (datum_von.equals(datum_bis)){
							if (uhr_von.equals("00:00:00") && uhr_bis.equals("23:59:59")){
								Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n   ganztägig \n ------------------------------- \n";
								}
								else{
									Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n  " + uhr_von + " bis\n  " + uhr_bis +" \n ------------------------------- \n";
								}
						}
						else{
							if (uhr_von.equals("00:00:00") && uhr_bis.equals("23:59:59")){
								Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n   ganztägig von \n         " + datum_von + "\n   bis " + datum_bis + " \n ------------------------------- \n";
								}
								else{
									Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n   " + datum_von + " " + uhr_von + " bis\n   " + datum_bis + " " + uhr_bis +" \n ------------------------------- \n";
								}
							
						}
						}
					txt_fri0.setText(Ausgabe);
					Ausgabe = "";
					
					//Daten für Samstag
					cal_where = Sam.toString();
					sqlstat = "SELECT * FROM (SELECT * FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  * FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt ORDER BY Uhrzeit_von, Uhrzeit_bis DESC";
			    	ResultSet rs_sam = statement_cal.executeQuery(sqlstat);
			    	while (rs_sam.next())
						{
						MA_Art = rs_sam.getString(1);
						datum_von = rs_sam.getString(3);
						datum_bis = rs_sam.getString(4);
						uhr_von = rs_sam.getString(5);
						uhr_bis = rs_sam.getString(6);
						MA_Name = rs_sam.getString(8);
						MA_Vorname = rs_sam.getString(9);
						
						if (datum_von.equals(datum_bis)){
							if (uhr_von.equals("00:00:00") && uhr_bis.equals("23:59:59")){
								Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n   ganztägig \n ------------------------------- \n";
								}
								else{
									Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n  " + uhr_von + " bis\n  " + uhr_bis +" \n ------------------------------- \n";
								}
						}
						else{
							if (uhr_von.equals("00:00:00") && uhr_bis.equals("23:59:59")){
								Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n   ganztägig von \n         " + datum_von + "\n   bis " + datum_bis + " \n ------------------------------- \n";
								}
								else{
									Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n   " + datum_von + " " + uhr_von + " bis\n   " + datum_bis + " " + uhr_bis +" \n ------------------------------- \n";
								}
							
						}
						}
					txt_sat0.setText(Ausgabe);
					Ausgabe = "";
					
					//Daten für Sonntag
					cal_where = Son.toString();
					sqlstat = "SELECT * FROM (SELECT * FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  * FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt ORDER BY Uhrzeit_von, Uhrzeit_bis DESC";
			    	ResultSet rs_son = statement_cal.executeQuery(sqlstat);
			    	while (rs_son.next())
						{
						MA_Art = rs_son.getString(1);
						datum_von = rs_son.getString(3);
						datum_bis = rs_son.getString(4);
						uhr_von = rs_son.getString(5);
						uhr_bis = rs_son.getString(6);
						MA_Name = rs_son.getString(8);
						MA_Vorname = rs_son.getString(9);
						
						if (datum_von.equals(datum_bis)){
							if (uhr_von.equals("00:00:00") && uhr_bis.equals("23:59:59")){
								Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n   ganztägig \n ------------------------------- \n";
								}
								else{
									Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n  " + uhr_von + " bis\n  " + uhr_bis +" \n ------------------------------- \n";
								}
						}
						else{
							if (uhr_von.equals("00:00:00") && uhr_bis.equals("23:59:59")){
								Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n   ganztägig von \n         " + datum_von + "\n   bis " + datum_bis + " \n ------------------------------- \n";
								}
								else{
									Ausgabe = Ausgabe + MA_Vorname + " " + MA_Name + " (" + MA_Art + ") \n   " + datum_von + " " + uhr_von + " bis\n   " + datum_bis + " " + uhr_bis +" \n ------------------------------- \n";
								}
							
						}
						}
					txt_sun0.setText(Ausgabe);
					Ausgabe = "";
					
					statement_cal.close();
//					conn.commit();
					
					}
				
				catch(Exception e){
					
					//System.out.println("Excep" + Str_cust_id_chosen);

					System.err.println("Got an exception! "); 
		            System.err.println(e.getMessage()); 
					
				}
				
				
				
			}




			@FXML public void btn_searchcustid_click() {
				 try { 
				        Statement statement_offer = conn.createStatement();
				        }
				        catch(Exception e){
							System.err.println("Got an exception! "); 
				            System.err.println(e.getMessage()); 
							}
				
				 
				 
				 openWindow();
				//filloffer();
				
			}
			
			
			public void openWindow() {
				 try {
					        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SearchCustomer.fxml"));
					                Parent root = (Parent) fxmlLoader.load();
					                Stage stage = new Stage();
					                stage.setScene(new Scene(root));
					                stage.setTitle("Kunden suchen");
					                stage.show();
					        } catch(Exception e) {
					           e.printStackTrace();
					        }
			}
	

			public void filloffer(int kid, String name, String vorname, String firma, String strasse, String pLZ, String ort,
					String phone, String mail, String land, String kG, String anrede, String zusatz){
			
			Kunde_neu = new KundenSuche(kid, name, vorname, firma, strasse, pLZ, ort, phone, mail, land, kG, anrede, zusatz);
			System.out.println(Kunde_neu.getOrt());
			//phone = Kunde_neu.getPhone();
			
			//txt_name.setText(Kunde_neu.getName());
			
			}
			




			@FXML public void btn_setcust_click() {
				
				//String test = "init";
				
				try{
			    	
			    	Statement statement = conn.createStatement();
			    	ResultSet rs = statement.executeQuery("SELECT * FROM benutzerverwaltung.kunde_auswahl");      
			        while((rs != null) && (rs.next())){

			        	Str_cust_id_chosen =  rs.getString(1);
			        }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");            
			    }
				
				

				System.out.println("klick geht");

			try{
		    	
		    	Statement statement = conn.createStatement();
		    	ResultSet rs = statement.executeQuery("SELECT Kunde_ID, KundeName, KundeVorname, IFNULL(KundeFirmenname,' ' ), KundeAdresse1, KundePLZ, KundenOrt, KundeTelefon, KundeEmail, KundenLand, Kundengruppen_Kundengruppen, KundeAnrede, KundeAdresse2  FROM myflight.kunden WHERE Kunde_ID ='" + Str_cust_id_chosen + "'" );      
		        while((rs != null) && (rs.next())){

		        	filloffer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13));
		        	txt_customerid1.setText(Str_cust_id_chosen);
		        	txt_name1.setText(Kunde_neu.getName()); 
					txt_prename1.setText(Kunde_neu.getVorname()); 
					txt_companyname1.setText(Kunde_neu.getFirma()); 
					txt_phone1.setText(Kunde_neu.getPhone());
	//				txt_mobile1.setText(Kunde_neu.getMobile()); 
					txt_mail1.setText(Kunde_neu.getMail()); 
					txt_street1.setText(Kunde_neu.getStrasse());
					txt_homeext1.setText(Kunde_neu.getZusatz());
					txt_anrede1.setText(Kunde_neu.getAnrede());
					txt_place1.setText(Kunde_neu.getOrt());

		        }
		        
		    }
		    catch(Exception e){
		          e.printStackTrace();
		          System.out.println("Error on Building Data");            
		    }
		}



			@FXML public void btn_zw_click() {
				
				set_allunvisible();
				apa_zws.setDisable(false);
			}



			@FXML public void btn_sw_click() {
				
				getEntfernung();
				
				Start_offer = dpi_startdat.getValue();
				Ziel_offer = dpi_zieldat.getValue();

				
				if(txt_pass.getText().equals("")){System.out.println("Bitte Passagiere ausfüllen");} //TODO
				else{
					
					set_allunvisible();
					apa_sonder.setVisible(true);
					apa_btn_sonder.setVisible(true);
					
					cbo_fz.setDisable(false);
					cbo_fz.getItems().clear();
					cbo_fz.setValue(null);
				
				
				try{
			    	
			    	Statement statement = conn.createStatement();
			    	ResultSet rs = statement.executeQuery("Select distinct(Flugzeugtypen_Flugzeugtypen_ID), FlugzeugHersteller, FlugzeugTyp, AnzahlPassagiere from myflight.flugzeuge Join myflight.Flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID  Where AnzahlPassagiere >=" + txt_pass.getText() +"  AND Flugzeug_ID not in(SELECT DISTINCT Flugzeug_ID FROM(SELECT rep.flugzeuge_Flugzeug_ID, rep.Datum_von, rep.Datum_bis FROM benutzerverwaltung.flugzeug_termine_reparatur rep UNION SELECT ang.flugzeuge_Flugzeug_ID, ang.Datum_von, ang.Datum_bis FROM benutzerverwaltung.flugzeug_termine_angebote INNER JOIN myflight.angebote ang ON benutzerverwaltung.flugzeug_termine_angebote.angebote_Angebote_ID=ang.Angebote_ID UNION SELECT war.flugzeuge_Flugzeug_ID, war.Datum_von, war.Datum_bis FROM benutzerverwaltung.flugzeug_termine_wartung war )dt INNER JOIN myflight.flugzeuge ON flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID INNER JOIN myflight.flugzeugtypen ON Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_bis between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_von < '"+Start_offer+"' and Datum_bis > '"+Ziel_offer+"' ORDER BY Flugzeug_ID asc)");      
			   while((rs != null) && (rs.next())){
			        	
			        	//cbo_fz.setValue(rs.getString(3));
			        	cbo_fz.getItems().add(rs.getString(2)+" "+rs.getString(3));
			        	

			        }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");            
			    }
				
				}
				
				
				
			}	
							
			
				
			
			



			@FXML public void btn_search_fh_click() {
				
				FHData.remove(0, FHData.size());
				
				String where = "";
				String filter = "";
				String and = "AND";
				String quote = "'";
				
				boolean iata_set = true;
				String Str_iata = txt_iata_search.getText();
				String Str_whereiata = " lower(FlughafenKuerzel)=";
				
				boolean stadt_set= true;
				String Str_stadt = txt_stadt_search.getText();
				String Str_wherestadt = " lower(FlughafenStadt)=";

				boolean where_set = false;
								
				if (txt_iata_search.getText().trim().isEmpty()){iata_set = false;}
				if (txt_stadt_search.getText().trim().isEmpty()){stadt_set = false;}
				
				if (iata_set == true){
					
					if (where_set == false){			
					filter = filter + Str_whereiata + quote + Str_iata + quote;
					where_set = true;
					}		
					else{
					filter = filter + and + Str_whereiata + quote + Str_iata + quote;	
					}
				}
				
				
				if (stadt_set == true) {
					
					if (where_set == false){			
					filter = filter + Str_wherestadt + quote + Str_stadt + quote;
					where_set = true;
					}		
					else{
					filter = filter + and +Str_wherestadt + quote + Str_stadt + quote;	
					}
					
				}
				
				
				if(where_set == false)
				{System.out.println("min. 1 Feld");} //TODO
					
				else {where = "WHERE"  + filter;
				
			    try{
			    	
			    	Statement statement = conn.createStatement();
			    	ResultSet rs = statement.executeQuery("SELECT * FROM myflight.flughafen " + where);      
			        while((rs != null) && (rs.next())){
			        	
			        	FHData.add(new FHSuche(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
			        	
			         }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");            
			    }
			    
				}
			}
				
			@FXML public void btn_newSearch_fh_click() {

			FHData.remove(0, FHData.size());
			txt_iata_search.clear();
			txt_stadt_search.clear();
			
			
			
			}

			@FXML public void btn_check_fh_click() {
				
				
				TablePosition pos = tbl_fh.getSelectionModel().getSelectedCells().get(0);
				int row = pos.getRow();
				FHSuche item = tbl_fh.getItems().get(row);
				TableColumn col = pos.getTableColumn();
				String data = (String) tbc_iata.getCellObservableValue(item).getValue();

	
				if (StartFH == true){

					txt_startfh.setText(data);
				
				}
				if (ZielFH == true){

					txt_zielfh.setText(data);	
				}
				StartFH = false;
				ZielFH = false;
				
				FHData.remove(0, FHData.size());
				txt_iata_search.clear();
				txt_stadt_search.clear();
				
				set_allunvisible();
				apa_create_offer.setVisible(true);
				apa_btn_createoffer.setVisible(true);

			}



			@FXML public void btn_close_fh_click() {
				
				StartFH = false;
				ZielFH = false;
				set_allunvisible();
				apa_create_offer.setVisible(true);
				apa_btn_createoffer.setVisible(true);
				
			}



			@FXML public void btn_startfh_click() {
				
				StartFH = true;
				set_allunvisible();
				apa_search_fh.setVisible(true);
			}



			@FXML public void btn_zielfh_click() {
				
				ZielFH = true;
				set_allunvisible();
				apa_search_fh.setVisible(true); 
			}



			@FXML public void btn_create_offer() {//TODO 
				
				Start_offer = dpi_startdat.getValue();
				Ziel_offer = dpi_zieldat.getValue();
				
				
				try{
			    	
			    	Statement statement_ang = conn.createStatement();
			    	ResultSet rs = statement_ang.executeQuery("SELECT MAX(Angebote_ID) FROM myflight.angebote");      
			        while((rs != null) && (rs.next())){
			        	
			        	//cbo_fz.setValue(rs.getString(3));
			        	AngeboteID = rs.getInt(1);

			        }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");            
			    }
		    	
		    	AngeboteID = AngeboteID +1;
		    	System.out.print(AngeboteID);
		    	
		    	
		    	
		    	try{
			    	
			    	Statement statement_ang = conn.createStatement();
			    	ResultSet rs = statement_ang.executeQuery("SELECT * FROM myflight.position_gehalt WHERE Position ='Captain'");      
			        while((rs != null) && (rs.next())){
			        	
			        	//cbo_fz.setValue(rs.getString(3));
			        	 gehcap = rs.getInt(2);

			        }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");            
			    }
		    	
		    	try{
			    	
			    	Statement statement_ang = conn.createStatement();
			    	ResultSet rs = statement_ang.executeQuery("SELECT * FROM myflight.position_gehalt WHERE Position ='First Officer'");      
			        while((rs != null) && (rs.next())){
			        	
			        	//cbo_fz.setValue(rs.getString(3));
			        	 gehcop = rs.getInt(2);

			        }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");            
			    }
		    	try{
			    	
			    	Statement statement_ang = conn.createStatement();
			    	ResultSet rs = statement_ang.executeQuery("SELECT * FROM myflight.position_gehalt WHERE Position ='Flight Attendant'");      
			        while((rs != null) && (rs.next())){
			        	
			        	//cbo_fz.setValue(rs.getString(3));
			        	 gehfa = rs.getInt(2);

			        }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");            
			    }
				
				if(charterart.equals("Zeitcharter")){
					
					entfernung = 4500;
					StartKont = "Europa";
					ZielKont = "Europa";
					
					Str_startzeith = txt_startzeit_h.getText();
			    	Str_startzeitm = txt_startzeit_m.getText();
			    	Str_zielzeith = txt_zielzeit_h.getText();
			    	Str_zielzeitm = txt_zielzeit_m.getText();
//			    	startdate = dpi_startdat.getValue();
//			    	zieldate = startdate;
//			    	
			    	long zwtage = 0;
			    	long startdat = dpi_startdat.getValue().toEpochDay();
			    	long zieldat = dpi_zieldat.getValue().toEpochDay();
			    	zwtage = zieldat - startdat;
			    	System.out.println("TAGE dazwisch " + zwtage);
					
			    	//dauer = (entfernung/speed)*60;
			    	//int idauer = Double.valueOf(dauer).intValue();
			    	
			    	double szh = Double.parseDouble(Str_startzeith);
			    	double szm = Double.parseDouble(Str_startzeitm);
			    	
			    	double szg = (szh * 60)+szm;
			    	
			    	
			    	double zzh =  Double.parseDouble(Str_zielzeith);
			    	double zzm =  Double.parseDouble(Str_zielzeitm);
			    	
			    	double zzg = (zzh * 60) +zzm;
			    	
			    	
			    	System.out.println("DAUER GESAMT: " + szg);
			    	System.out.println("DAUER GESAMT: " + zzg);
			    	
			    	double rest = 0;
			    	rest = zzg - szg;
			    	dauercharter = (float) ((zwtage * 1440) + rest )/60;
			    	dauerflug = dauercharter / 2;
			    	
			    	
			    	
			    	
				}
				else{
				getEntfernung();
				}
				
				
				
				
				
				
				if(sonderw == true){
					
					try{
				    	
				    	Statement statement = conn.createStatement();
				    	ResultSet rs = statement.executeQuery("Select distinct(Flugzeugtypen_Flugzeugtypen_ID), FlugzeugHersteller, FlugzeugTyp, AnzahlPassagiere,Geschwindigkeit, Reichweite, Fixkosten, Betriebskosten from myflight.flugzeuge Join myflight.Flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID Where FlugzeugHersteller ='" + FZHersteller +"' AND FlugzeugTyp ='" + FZTyp +"'  AND Flugzeug_ID not in(SELECT DISTINCT Flugzeug_ID FROM(SELECT rep.flugzeuge_Flugzeug_ID, rep.Datum_von, rep.Datum_bis FROM benutzerverwaltung.flugzeug_termine_reparatur rep UNION SELECT ang.flugzeuge_Flugzeug_ID, ang.Datum_von, ang.Datum_bis FROM benutzerverwaltung.flugzeug_termine_angebote INNER JOIN myflight.angebote ang ON benutzerverwaltung.flugzeug_termine_angebote.angebote_Angebote_ID=ang.Angebote_ID UNION SELECT war.flugzeuge_Flugzeug_ID, war.Datum_von, war.Datum_bis FROM benutzerverwaltung.flugzeug_termine_wartung war )dt INNER JOIN myflight.flugzeuge ON flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID INNER JOIN myflight.flugzeugtypen ON Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_bis between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_von < '"+Start_offer+"' and Datum_bis > '"+Ziel_offer+"' ORDER BY Flugzeug_ID asc)");      
				   while((rs != null) && (rs.next())){
				        	
				        	//cbo_fz.setValue(rs.getString(3));
				        	bestFZ = rs.getInt(1);
				        	speed = rs.getInt(5);
				        	reichweite = rs.getInt(6);
				        	FixkostenFZ =rs.getInt(7);
				        	BetriebskFZ = rs.getInt(8);

				        }
				        
				    }
				    catch(Exception e){
				          e.printStackTrace();
				          System.out.println("Error on Building Data");            
				    }
					
					
					
					 String SQL_per_cap = "SELECT Distinct(Personal_ID),Position_Gehalt_Position, Lizenz_Lizenz,PersonalName, PersonalVorname  FROM myflight.personal join myflight.personal_has_lizenz ON Personal_ID=myflight.personal_has_lizenz.Personal_Personal_ID WHERE PersonalName ='" + CAPnachname +"' AND PersonalVorname ='" + CAPvorname +"' AND Position_Gehalt_Position='Captain' AND Personalstatus_Personalstatus ='aktiv' AND Lizenz_Lizenz ='"+ Lizenz+ "' AND Personal_ID not in( SELECT  Distinct(personal_Personal_ID)FROM ( SELECT kra.personal_Personal_ID, kra.Datum_von, kra.Datum_bis FROM benutzerverwaltung.personal_termine_krankheit kra UNION SELECT ang.Captain, ang.Datum_von, ang.Datum_bis FROM benutzerverwaltung.personal_termine_angebote JOIN myflight.angebote ang ON benutzerverwaltung.personal_termine_angebote.angebote_Angebote_ID=ang.Angebote_ID UNION SELECT url.personal_Personal_ID, url.Datum_von, url.Datum_bis FROM benutzerverwaltung.personal_termine_urlaub url)dt join myflight.personal ON personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_bis between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_von < '"+Start_offer+"' and Datum_bis > '"+Ziel_offer+"')";

						
					    try{
					    	
					    	Statement statement_cap = conn.createStatement();
					    	ResultSet rs = statement_cap.executeQuery(SQL_per_cap);//"SELECT * FROM myflight.personal WHERE Position_Gehalt_Position ='Captain' AND Personalstatus_Personalstatus ='aktiv'");      
					        while((rs != null) && (rs.next())){
					        	
					        	//cbo_fz.setValue(rs.getString(3));
					        	bestCaptain = rs.getInt(1);

					        }
					        
					    }
					    catch(Exception e){
					          e.printStackTrace();
					          System.out.println("Error on Building Data");            
					    }
					
					
					
					    if(cbo_cop.isDisabled()){}
					    else{
					    	
					    	count_cop = 1;
					    	String SQL_per_cop = "SELECT Distinct(Personal_ID),Position_Gehalt_Position, PersonalName, PersonalVorname  FROM myflight.personal WHERE PersonalName ='" + COPnachname +"' AND PersonalVorname ='" + COPvorname +"' AND Position_Gehalt_Position='First Officer' AND Personalstatus_Personalstatus ='aktiv' AND Personal_ID not in( SELECT  Distinct(personal_Personal_ID)FROM ( SELECT kra.personal_Personal_ID, kra.Datum_von, kra.Datum_bis FROM benutzerverwaltung.personal_termine_krankheit kra UNION SELECT ang.Captain, ang.Datum_von, ang.Datum_bis FROM benutzerverwaltung.personal_termine_angebote JOIN myflight.angebote ang ON benutzerverwaltung.personal_termine_angebote.angebote_Angebote_ID=ang.Angebote_ID UNION SELECT url.personal_Personal_ID, url.Datum_von, url.Datum_bis FROM benutzerverwaltung.personal_termine_urlaub url)dt join myflight.personal ON personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_bis between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_von < '"+Start_offer+"' and Datum_bis > '"+Ziel_offer+"')";

						    
					    	
					    	try{
						    	
						    	Statement statement_co = conn.createStatement();
						    	ResultSet rs = statement_co.executeQuery(SQL_per_cop);      
						        while((rs != null) && (rs.next())){
						        	
						        	//cbo_fz.setValue(rs.getString(3));
						        	bestCOP = rs.getInt(1);

						        }
						        
						    }
						    catch(Exception e){
						          e.printStackTrace();
						          System.out.println("Error on Building Data");            
						    }
					    	
					    	
					    	
					    }
					    
					    if(cbo_fa1.isDisabled()){}
					    else{
					    	count_fa=1;
					    	String SQL_per_fa1 = "SELECT Distinct(Personal_ID),Position_Gehalt_Position, PersonalName, PersonalVorname  FROM myflight.personal WHERE PersonalName ='" + FA1nachname +"' AND PersonalVorname ='" + FA1vorname +"' AND Position_Gehalt_Position='Flight Attendant' AND Personalstatus_Personalstatus ='aktiv' AND Personal_ID not in( SELECT  Distinct(personal_Personal_ID)FROM ( SELECT kra.personal_Personal_ID, kra.Datum_von, kra.Datum_bis FROM benutzerverwaltung.personal_termine_krankheit kra UNION SELECT ang.Captain, ang.Datum_von, ang.Datum_bis FROM benutzerverwaltung.personal_termine_angebote JOIN myflight.angebote ang ON benutzerverwaltung.personal_termine_angebote.angebote_Angebote_ID=ang.Angebote_ID UNION SELECT url.personal_Personal_ID, url.Datum_von, url.Datum_bis FROM benutzerverwaltung.personal_termine_urlaub url)dt join myflight.personal ON personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_bis between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_von < '"+Start_offer+"' and Datum_bis > '"+Ziel_offer+"')";

					    	try{
						    	
						    	Statement statement_co = conn.createStatement();
						    	ResultSet rs = statement_co.executeQuery(SQL_per_fa1);      
						        while((rs != null) && (rs.next())){
						        	
						        	//cbo_fz.setValue(rs.getString(3));
						        	bestFA1 = rs.getInt(1);

						        }
						        
						    }
						    catch(Exception e){
						          e.printStackTrace();
						          System.out.println("Error on Building Data");            
						    }
					    	
					    }
					    
					
					
					    if(cbo_fa2.isDisabled()){}
					    else{
					    	count_fa=2;
					    	String SQL_per_fa2 = "SELECT Distinct(Personal_ID),Position_Gehalt_Position, PersonalName, PersonalVorname  FROM myflight.personal WHERE PersonalName ='" + FA2nachname +"' AND PersonalVorname ='" + FA2vorname +"' AND Position_Gehalt_Position='Flight Attendant' AND Personalstatus_Personalstatus ='aktiv' AND Personal_ID not in( SELECT  Distinct(personal_Personal_ID)FROM ( SELECT kra.personal_Personal_ID, kra.Datum_von, kra.Datum_bis FROM benutzerverwaltung.personal_termine_krankheit kra UNION SELECT ang.Captain, ang.Datum_von, ang.Datum_bis FROM benutzerverwaltung.personal_termine_angebote JOIN myflight.angebote ang ON benutzerverwaltung.personal_termine_angebote.angebote_Angebote_ID=ang.Angebote_ID UNION SELECT url.personal_Personal_ID, url.Datum_von, url.Datum_bis FROM benutzerverwaltung.personal_termine_urlaub url)dt join myflight.personal ON personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_bis between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_von < '"+Start_offer+"' and Datum_bis > '"+Ziel_offer+"')";

					    	try{
						    	
						    	Statement statement_co = conn.createStatement();
						    	ResultSet rs = statement_co.executeQuery(SQL_per_fa2);      
						        while((rs != null) && (rs.next())){
						        	
						        	//cbo_fz.setValue(rs.getString(3));
						        	bestFA2 = rs.getInt(1);

						        }
						        
						    }
						    catch(Exception e){
						          e.printStackTrace();
						          System.out.println("Error on Building Data");            
						    }
					    	
					    }
					    if(cbo_fa3.isDisabled()){}
					    else{
					    	count_fa=3;
					    	String SQL_per_fa3 = "SELECT Distinct(Personal_ID),Position_Gehalt_Position,PersonalName, PersonalVorname  FROM myflight.personal WHERE PersonalName ='" + FA3nachname +"' AND PersonalVorname ='" + FA3vorname +"' AND Position_Gehalt_Position='Flight Attendant' AND Personalstatus_Personalstatus ='aktiv' AND Personal_ID not in( SELECT  Distinct(personal_Personal_ID)FROM ( SELECT kra.personal_Personal_ID, kra.Datum_von, kra.Datum_bis FROM benutzerverwaltung.personal_termine_krankheit kra UNION SELECT ang.Captain, ang.Datum_von, ang.Datum_bis FROM benutzerverwaltung.personal_termine_angebote JOIN myflight.angebote ang ON benutzerverwaltung.personal_termine_angebote.angebote_Angebote_ID=ang.Angebote_ID UNION SELECT url.personal_Personal_ID, url.Datum_von, url.Datum_bis FROM benutzerverwaltung.personal_termine_urlaub url)dt join myflight.personal ON personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_bis between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_von < '"+Start_offer+"' and Datum_bis > '"+Ziel_offer+"')";

					    	try{
						    	
						    	Statement statement_co = conn.createStatement();
						    	ResultSet rs = statement_co.executeQuery(SQL_per_fa3);      
						        while((rs != null) && (rs.next())){
						        	
						        	//cbo_fz.setValue(rs.getString(3));
						        	bestFA3 = rs.getInt(1);

						        }
						        
						    }
						    catch(Exception e){
						          e.printStackTrace();
						          System.out.println("Error on Building Data");            
						    }
					    	
					    }
					
					
				}
				else{
					
					
					getBestFZ();
			    	getBestPerson();
					
				}
				
				double dauer = 0;
				
				if(charterart.equals("Zeitcharter")){
			    	float dauerh = (float)dauer;
			    	dauerh = dauerh / 60;
			    	
			    	int zwischenstop = 0;

			    	
			    	angbetr = BetriebskFZ * dauerflug;
			    	angfix = (FixkostenFZ/2000) *(dauerflug + 1.5F);
			    	//dauercharter = dauerh + 1.5F + (zwischenstop * 0.75F);
			    	angpers = ((gehcap/1600) + ((count_cop*gehcop)/1600) + ((count_fa*gehfa)/1600))* dauercharter;
			    	angpers = angpers*pers_aufschlag;
			    	
			    	angnetto = angpers + angbetr + angfix + KostenSW;
			    	
			    	try{
				    	
				    	Statement statement = conn.createStatement();
				    	ResultSet rs = statement.executeQuery("SELECT Kundengruppen_Kundengruppen FROM myflight.kunden WHERE Kunde_ID="+Str_cust_id_chosen);      
				        while((rs != null) && (rs.next())){
				        	
				        	//cbo_fz.setValue(rs.getString(3));
				        	CustState = rs.getString(1);

				        }
				        
				    }
				    catch(Exception e){
				          e.printStackTrace();
				          System.out.println("Error on Building Data");            
				    }
			    	
			    	if(CustState.equals("PRE")){
			    		
			    		angpre = angnetto * angpre_fakt;
			    		
			    	}
			  
			    	

			    	angnetto = angnetto + angpre;
			    	angbrutto = angnetto * mwst;
			    	
			    	SW = "";
			    	if(sonderw ==true){
			    	SW = "Speisen:( " + SWspeisen + " ) Getränke:( " + SWgetr + " )";
			    	}
			    	
			    	pax_fix = Integer.parseInt(txt_pass.getText());
			    	
			    	//dauerflug = dauerflug/60;
					
				}
							
				
				
				
				
				
				if(charterart.equals("Einzelflug")){			    	
			    	Str_startzeith = txt_startzeit_h.getText();
			    	Str_startzeitm = txt_startzeit_m.getText();
			    	Str_zielzeith = txt_startzeit_h.getText();
			    	Str_zielzeitm = txt_startzeit_m.getText();
			    	startdate = dpi_startdat.getValue();
			    	zieldate = startdate;
			    	
			    	
		    	
			    	dauer = (entfernung/speed)*60;
			    	int idauer = Double.valueOf(dauer).intValue();
			    	
			    	double szh = Double.parseDouble(Str_startzeith);
			    	double szm = Double.parseDouble(Str_startzeitm);
			    	
			    	double szg = (szh * 60)+szm;
			    	szg = szg + dauer;
			    	double tage = 1440-szg;
			    				    	
			    	System.out.println(zieldate);
			    	if(tage<0){
			    		
			    		System.out.println("ist drin");
			    		zieldate = dpi_startdat.getValue().plusDays(1);
			    	}
			    	if(tage < -1440){
			    		
			    		
			    		zieldate = dpi_startdat.getValue().plusDays(2);
			    		
			    	}
			    	
			    	
			    	startzeit = LocalTime.parse(Str_startzeith+":"+Str_startzeitm+":00");
			    	zielzeit = startzeit.plusMinutes(idauer);
			    	
			    	
			    	//dauerflug = dauerflug.plusMinutes(idauer);
			    	dauerflug = dauerflug + idauer;
			    	System.out.println("FLUG DAUER " + dauerflug);
			    	
			    	//dauerflug = dauerflug.plusMinutes(1300);
			    	
			    	System.out.println("FLUG DAUER " + dauerflug);
			    	
			       //LocalTime test = LocalTime.parse("28:00:00");
			       
			       //System.out.println("FLUG TEST " + test);
			       
			       
			    	
			    	System.out.println("idauer: " +idauer);
			    	System.out.println(dauer);
			    	System.out.println(szg);
			    	System.out.println(tage);
			    	System.out.println("Zielzeit: "+ zieldate);
			    	System.out.println(zielzeit);
			    	
			
			    	
			    	
			    	
			    	
			    	float dauerh = (float)dauer;
			    	dauerh = dauerh / 60;
			    	
			    	int zwischenstop = 0;
			    	zwischenstop = (int) (entfernung/reichweite);
			    	
			    	
			    	System.out.println("Stopps   " + zwischenstop);
			    	
			    	angbetr = BetriebskFZ * dauerh;
			    	
			    	
			    	angfix = (FixkostenFZ/2000) *(dauerh + 1.5F);
			    	
			    	dauercharter = dauerh + 1.5F + (zwischenstop * 0.75F);
			    	
			    	angpers = ((gehcap/1600) + ((count_cop*gehcop)/1600) + ((count_fa*gehfa)/1600))* dauercharter;
			    	angpers = angpers*pers_aufschlag;
			    	
			    	angnetto = angpers + angbetr + angfix + KostenSW;
			    	
			    	try{
				    	
				    	Statement statement = conn.createStatement();
				    	ResultSet rs = statement.executeQuery("SELECT Kundengruppen_Kundengruppen FROM myflight.kunden WHERE Kunde_ID="+Str_cust_id_chosen);      
				        while((rs != null) && (rs.next())){
				        	
				        	//cbo_fz.setValue(rs.getString(3));
				        	CustState = rs.getString(1);

				        }
				        
				    }
				    catch(Exception e){
				          e.printStackTrace();
				          System.out.println("Error on Building Data");            
				    }
			    	
			    	if(CustState.equals("PRE")){
			    		
			    		angpre = angnetto * angpre_fakt;
			    		
			    	}
			  
			    	

			    	angnetto = angnetto + angpre;
			    	angbrutto = angnetto * mwst;
			    	
			    	SW = "";
			    	if(sonderw ==true){
			    	SW = "Speisen:( " + SWspeisen + " ) Getränke:( " + SWgetr + " )";
			    	}
			    	
			    	pax_fix = Integer.parseInt(txt_pass.getText());
			    	
			    	dauerflug = dauerflug/60;
			    	
			    	
				}
				
				
			    	
			    	try { 

						Statement statement = conn.createStatement();			
						statement.executeUpdate(
								"INSERT INTO myflight.angebote " + "VALUES("
										+AngeboteID+",'"
										+AngDatum+"','Offen','"
										+charterart+"','"
										+Str_cust_id_chosen+"','"
										+pax_fix+"','"
										+angbrutto+"','" 
										+angnetto+"','"
										+KostenSW+ "','"
										+angfix+"','"
										+angbetr+"','"
										+angpers+"','"
										+angpre+"','"
										+dauercharter+"','"
										+dauerflug+"','"//
										+bestFZ+"','" 
										+bestCaptain+"','"
										+bestCOP+ "','"
										+bestFA1+"','"
										+bestFA2+"','"
										+startdate+"','"
										+zieldate+"','"
										+startzeit+"','"
										+zielzeit+"','"
										+SW+"')");

						}
				
					catch(Exception e){
						System.err.println("Got an exception! "); 
			            System.err.println(e.getMessage()); 
						}
				
			    	
			    }
			   



			@FXML public void chb_getr_check() {
				
				txa_getr.setText("");
				txa_getr.setDisable(false);
				txt_kostensw.setDisable(false);
				
			
				
			}



			@FXML public void chb_speisen_check() {
				
				txa_speisen.setText("");
				txa_speisen.setDisable(false);
				txt_kostensw.setDisable(false);
				
			}



			@FXML public void btn_sonder_ok_click() {
				
				sonderw = true;
				
				if(cbo_fz.isDisabled() == false){
				
				String FZ = cbo_fz.getValue().toString();
				int pos = FZ.indexOf(" ");
			    FZHersteller = FZ.substring(0, pos);
			    FZTyp = FZ.substring(pos+1,FZ.length());
			    
				} 
			
				if(cbo_cop.isDisabled() == false){
					
				String COP = cbo_cop.getValue().toString();
				int pos4 = COP.indexOf(" ");
			    COPvorname = COP.substring(0, pos4);
			    COPnachname  = COP.substring(pos4+1,COP.length());
				
				}
				
				if(cbo_cap.isDisabled() == false){
					
					String CAP = cbo_cap.getValue().toString();
				    int pos5 = CAP.indexOf(" ");
				    CAPvorname = CAP.substring(0, pos5);
				    CAPnachname  = CAP.substring(pos5+1,CAP.length());
				}
					
				if(cbo_fa1.isDisabled() == false){
				
				String FA1 = cbo_fa1.getValue().toString();
			    int pos1 = FA1.indexOf(" ");
			    FA1vorname = FA1.substring(0, pos1);
			    FA1nachname = FA1.substring(pos1+1,FA1.length());
				}
								
				if(cbo_fa2.isDisabled() == false){
				
				String FA2 = cbo_fa2.getValue().toString();
			    int pos2 = FA2.indexOf(" ");
			    FA2vorname = FA2.substring(0, pos2);
			    FA2nachname  = FA2.substring(pos2+1,FA2.length());
			    
				}
				
				if(cbo_fa3.isDisabled() == false){
					
				String FA3 = cbo_fa3.getValue().toString();
				int pos3 = FA3.indexOf(" ");
				FA3vorname = FA3.substring(0, pos3);
				FA3nachname  = FA3.substring(pos3+1,FA3.length());
				}			   
			    
			    
			    SWgetr = txa_getr.getText();
			    SWspeisen = txa_speisen.getText();
			    
			    if (txt_kostensw.getText().matches("[0-9]*") || txt_kostensw.getText() == ""){
			    KostenSW = Integer.valueOf(txt_kostensw.getText());
			    
			    
			    set_allunvisible();
			    apa_create_offer.setVisible(true);
			    apa_btn_createoffer.setVisible(true);
			    }
			    else {System.out.println("nur zahlen");} //TODO
			    
			    
			    
				
				
			}



			@FXML public void btn_sonder_stop_click() {}



			@FXML public void cbo_fz_click() {
				
				cbo_cap.setDisable(false);
				cbo_cap.getItems().clear();
				cbo_cap.setValue(null);
				
				if(cbo_fz.isDisabled() == false){
					
					String FZ = cbo_fz.getValue().toString();
					int pos = FZ.indexOf(" ");
				    FZHersteller = FZ.substring(0, pos);
				    FZTyp = FZ.substring(pos+1,FZ.length());
				    
					}
				
				
				try{
			    	
			    	Statement statement_cap = conn.createStatement();
			    	ResultSet rs = statement_cap.executeQuery("SELECT Lizenz FROM myflight.lizenz join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID Where FlugzeugHersteller = '" + FZHersteller + "' and FlugzeugTyp = '" + FZTyp +"'");
			        while((rs != null) && (rs.next())){
			        	
			        	//cbo_fz.setValue(rs.getString(3));
			        	Lizenz = rs.getString(1);

			        }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");            
			    }
				
				
				
			    String SQL_per_cap = "SELECT Distinct(Personal_ID),Position_Gehalt_Position, Lizenz_Lizenz,PersonalName, PersonalVorname  FROM myflight.personal join myflight.personal_has_lizenz ON Personal_ID=myflight.personal_has_lizenz.Personal_Personal_ID WHERE Position_Gehalt_Position='Captain' AND Personalstatus_Personalstatus ='aktiv' AND Lizenz_Lizenz ='" + Lizenz + "' AND Personal_ID not in( SELECT  Distinct(personal_Personal_ID)FROM ( SELECT kra.personal_Personal_ID, kra.Datum_von, kra.Datum_bis FROM benutzerverwaltung.personal_termine_krankheit kra UNION SELECT ang.Captain, ang.Datum_von, ang.Datum_bis FROM benutzerverwaltung.personal_termine_angebote JOIN myflight.angebote ang ON benutzerverwaltung.personal_termine_angebote.angebote_Angebote_ID=ang.Angebote_ID UNION SELECT url.personal_Personal_ID, url.Datum_von, url.Datum_bis FROM benutzerverwaltung.personal_termine_urlaub url)dt join myflight.personal ON personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_bis between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_von < '"+Start_offer+"' and Datum_bis > '"+Ziel_offer+"')";

				
			    try{
			    	
			    	Statement statement_cap = conn.createStatement();
			    	ResultSet rs = statement_cap.executeQuery(SQL_per_cap);//"SELECT * FROM myflight.personal WHERE Position_Gehalt_Position ='Captain' AND Personalstatus_Personalstatus ='aktiv'");      
			        while((rs != null) && (rs.next())){
			        	
			        	//cbo_fz.setValue(rs.getString(3));
			        	cbo_cap.getItems().add(rs.getString(5)+" "+ rs.getString(4));

			        }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");            
			    }
			    
			    
								
			}



			@FXML public void cbo_cap_click() {
				
				
				cbo_cop.getItems().clear();
				cbo_cop.setValue(null);
				
				String FZ = cbo_fz.getValue().toString();
				
			    int pos = FZ.indexOf(" ");
			    String hersteller = FZ.substring(0, pos);
			    String typ = FZ.substring(pos+1,FZ.length());
			    int cop_zahl = 0;
			    
			    try{
			    	
			    	Statement statement_cop_zahl = conn.createStatement();
			    	ResultSet rs = statement_cop_zahl.executeQuery("SELECT * FROM myflight.flugzeugtypen WHERE FlugzeugHersteller ='"+hersteller+"' AND FlugzeugTyp ='"+typ+"'");      
			        while((rs != null) && (rs.next())){
			        	
			        	//cbo_fz.setValue(rs.getString(3));
			        	cop_zahl = rs.getInt(13);

			        }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");            
			    }
			    
			    
			    if(cop_zahl>0){
			    	
			    	cbo_cop.setDisable(false);
				    String SQL_per_cop = "SELECT Distinct(Personal_ID),Position_Gehalt_Position,PersonalName, PersonalVorname  FROM myflight.personal WHERE Position_Gehalt_Position='First Officer' AND Personalstatus_Personalstatus ='aktiv' AND Personal_ID not in( SELECT  Distinct(personal_Personal_ID)FROM ( SELECT kra.personal_Personal_ID, kra.Datum_von, kra.Datum_bis FROM benutzerverwaltung.personal_termine_krankheit kra UNION SELECT ang.Captain, ang.Datum_von, ang.Datum_bis FROM benutzerverwaltung.personal_termine_angebote JOIN myflight.angebote ang ON benutzerverwaltung.personal_termine_angebote.angebote_Angebote_ID=ang.Angebote_ID UNION SELECT url.personal_Personal_ID, url.Datum_von, url.Datum_bis FROM benutzerverwaltung.personal_termine_urlaub url)dt join myflight.personal ON personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_bis between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_von < '"+Start_offer+"' and Datum_bis > '"+Ziel_offer+"')";

			    
			    	
			    	try{
				    	
				    	Statement statement_co = conn.createStatement();
				    	ResultSet rs = statement_co.executeQuery(SQL_per_cop);      
				        while((rs != null) && (rs.next())){
				        	
				        	//cbo_fz.setValue(rs.getString(3));
				        	cbo_cop.getItems().add(rs.getString(4)+" "+ rs.getString(3));

				        }
				        
				    }
				    catch(Exception e){
				          e.printStackTrace();
				          System.out.println("Error on Building Data");            
				    }
			    	
			    	
			    }
			    
			}
			
			@FXML public void cbo_cop_click() {
				
				cbo_fa1.getItems().clear();
				cbo_fa1.setValue(null);
				
				String FZ = cbo_fz.getValue().toString();
				
			    int pos = FZ.indexOf(" ");
			    String hersteller = FZ.substring(0, pos);
			    String typ = FZ.substring(pos+1,FZ.length());
			    int fa_zahl = 0;
			    
			    try{
			    	
			    	Statement statement_fa_zahl = conn.createStatement();
			    	ResultSet rs = statement_fa_zahl.executeQuery("SELECT * FROM myflight.flugzeugtypen WHERE FlugzeugHersteller ='"+hersteller+"' AND FlugzeugTyp ='"+typ+"'");      
			        while((rs != null) && (rs.next())){
			        	
			        	//cbo_fz.setValue(rs.getString(3));
			        	fa_zahl = rs.getInt(14);

			        }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");            
			    }
			    
			    
			    if(fa_zahl>0){
			    	
				    String SQL_per_fa1 = "SELECT Distinct(Personal_ID),Position_Gehalt_Position,PersonalName, PersonalVorname  FROM myflight.personal WHERE Position_Gehalt_Position='Flight Attendant' AND Personalstatus_Personalstatus ='aktiv' AND Personal_ID not in( SELECT  Distinct(personal_Personal_ID)FROM ( SELECT kra.personal_Personal_ID, kra.Datum_von, kra.Datum_bis FROM benutzerverwaltung.personal_termine_krankheit kra UNION SELECT ang.Captain, ang.Datum_von, ang.Datum_bis FROM benutzerverwaltung.personal_termine_angebote JOIN myflight.angebote ang ON benutzerverwaltung.personal_termine_angebote.angebote_Angebote_ID=ang.Angebote_ID UNION SELECT url.personal_Personal_ID, url.Datum_von, url.Datum_bis FROM benutzerverwaltung.personal_termine_urlaub url)dt join myflight.personal ON personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_bis between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_von < '"+Start_offer+"' and Datum_bis > '"+Ziel_offer+"')";

			    	cbo_fa1.setDisable(false);
			    	
			    	
			    	
			    	try{
				    	
				    	Statement statement_co = conn.createStatement();
				    	ResultSet rs = statement_co.executeQuery(SQL_per_fa1);      
				        while((rs != null) && (rs.next())){
				        	
				        	//cbo_fz.setValue(rs.getString(3));
				        	cbo_fa1.getItems().add(rs.getString(4)+" "+ rs.getString(3));

				        }
				        
				    }
				    catch(Exception e){
				          e.printStackTrace();
				          System.out.println("Error on Building Data");            
				    }
			    	
			    	
			    }
				
				
			}



			@FXML public void cbo_fa3_click() {}



			@FXML public void cbo_fa2_click() {
				
				cbo_fa3.getItems().clear();
				cbo_fa3.setValue(null);
				
				String FZ = cbo_fz.getValue().toString();
				String FA1 = cbo_fa1.getValue().toString();
				String FA2 = cbo_fa2.getValue().toString();
				
			    int pos = FZ.indexOf(" ");
			    String hersteller = FZ.substring(0, pos);
			    String typ = FZ.substring(pos+1,FZ.length());
			    
			    int pos1 = FA1.indexOf(" ");
			    String vorname = FA1.substring(0, pos1);
			    String nachname = FA1.substring(pos1+1,FA1.length());
			    
			    int pos2 = FA2.indexOf(" ");
			    String vorname2 = FA2.substring(0, pos2);
			    String nachname2 = FA2.substring(pos2+1,FA2.length());
			    
			    int fa_zahl = 0;
			    
			    try{
			    	
			    	Statement statement_fa_zahl = conn.createStatement();
			    	ResultSet rs = statement_fa_zahl.executeQuery("SELECT * FROM myflight.flugzeugtypen WHERE FlugzeugHersteller ='"+hersteller+"' AND FlugzeugTyp ='"+typ+"'");      
			        while((rs != null) && (rs.next())){
			        	
			        	//cbo_fz.setValue(rs.getString(3));
			        	fa_zahl = rs.getInt(14);

			        }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");            
			    }
			    
			    
			    if(fa_zahl>2){
			    	
			    	cbo_fa3.setDisable(false);
				    String SQL_per_fa2 = "SELECT Distinct(Personal_ID),Position_Gehalt_Position,PersonalName, PersonalVorname  FROM myflight.personal WHERE Position_Gehalt_Position='Flight Attendant' AND Personalstatus_Personalstatus ='aktiv' AND NOT PersonalName = '" + nachname + "'AND NOT PersonalVorname = '"+vorname+"' AND NOT PersonalName = '" + nachname2 + "'AND NOT PersonalVorname = '"+vorname2+"' AND Personal_ID not in( SELECT  Distinct(personal_Personal_ID)FROM ( SELECT kra.personal_Personal_ID, kra.Datum_von, kra.Datum_bis FROM benutzerverwaltung.personal_termine_krankheit kra UNION SELECT ang.Captain, ang.Datum_von, ang.Datum_bis FROM benutzerverwaltung.personal_termine_angebote JOIN myflight.angebote ang ON benutzerverwaltung.personal_termine_angebote.angebote_Angebote_ID=ang.Angebote_ID UNION SELECT url.personal_Personal_ID, url.Datum_von, url.Datum_bis FROM benutzerverwaltung.personal_termine_urlaub url)dt join myflight.personal ON personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_bis between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_von < '"+Start_offer+"' and Datum_bis > '"+Ziel_offer+"')";

			    	try{
				    	
				    	Statement statement_co = conn.createStatement();
				    	ResultSet rs = statement_co.executeQuery(SQL_per_fa2);      
				        while((rs != null) && (rs.next())){
				        	
				        	//cbo_fz.setValue(rs.getString(3));
				        	cbo_fa3.getItems().add(rs.getString(4)+" "+ rs.getString(3));

				        }
				        
				    }
				    catch(Exception e){
				          e.printStackTrace();
				          System.out.println("Error on Building Data");            
				    }
			    	
			    	
			    }
				
				
			}



			@FXML public void cbo_fa1_click() {
				
				cbo_fa2.getItems().clear();
				cbo_fa2.setValue(null);
				
				String FZ = cbo_fz.getValue().toString();
				String FA1 = cbo_fa1.getValue().toString();
				
			    int pos = FZ.indexOf(" ");
			    String hersteller = FZ.substring(0, pos);
			    String typ = FZ.substring(pos+1,FZ.length());
			    
			    int pos1 = FA1.indexOf(" ");
			    String vorname = FA1.substring(0, pos1);
			    String nachname = FA1.substring(pos1+1,FA1.length());
			    
			    int fa_zahl = 0;
			    
			    try{
			    	
			    	Statement statement_fa_zahl = conn.createStatement();
			    	ResultSet rs = statement_fa_zahl.executeQuery("SELECT * FROM myflight.flugzeugtypen WHERE FlugzeugHersteller ='"+hersteller+"' AND FlugzeugTyp ='"+typ+"'");      
			        while((rs != null) && (rs.next())){
			        	
			        	//cbo_fz.setValue(rs.getString(3));
			        	fa_zahl = rs.getInt(14);

			        }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");            
			    }
			    
			    
			    if(fa_zahl>1){
			    	
			    	cbo_fa2.setDisable(false);
			    	
				    String SQL_per_fa2 = "SELECT Distinct(Personal_ID),Position_Gehalt_Position,PersonalName, PersonalVorname  FROM myflight.personal WHERE Position_Gehalt_Position='Flight Attendant' AND Personalstatus_Personalstatus ='aktiv' AND NOT PersonalName = '" + nachname + "'AND NOT PersonalVorname = '"+vorname+"' AND Personal_ID not in( SELECT  Distinct(personal_Personal_ID)FROM ( SELECT kra.personal_Personal_ID, kra.Datum_von, kra.Datum_bis FROM benutzerverwaltung.personal_termine_krankheit kra UNION SELECT ang.Captain, ang.Datum_von, ang.Datum_bis FROM benutzerverwaltung.personal_termine_angebote JOIN myflight.angebote ang ON benutzerverwaltung.personal_termine_angebote.angebote_Angebote_ID=ang.Angebote_ID UNION SELECT url.personal_Personal_ID, url.Datum_von, url.Datum_bis FROM benutzerverwaltung.personal_termine_urlaub url)dt join myflight.personal ON personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_bis between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_von < '"+Start_offer+"' and Datum_bis > '"+Ziel_offer+"')";

			    	
			    	try{
				    	
				    	Statement statement_co = conn.createStatement();
				    	ResultSet rs = statement_co.executeQuery(SQL_per_fa2);      
				        while((rs != null) && (rs.next())){
				        	
				        	//cbo_fz.setValue(rs.getString(3));
				        	cbo_fa2.getItems().add(rs.getString(4)+" "+ rs.getString(3));

				        }
				        
				    }
				    catch(Exception e){
				          e.printStackTrace();
				          System.out.println("Error on Building Data");            
				    }
			    	
			    	
			    }
				
			}



			@FXML public void txt_pax_sw_in() {}



			@FXML public void dpi_startdat_click() {}



			@FXML public void dpi_zieldat_click() {}



			@FXML public void cbo_charterart_click() {
				
				charterart = cbo_charterart.getValue().toString();
				
				if(charterart.equals("Zeitcharter")){
					

					dpi_startdat.setDisable(false);
					dpi_zieldat.setDisable(false);
					txt_pass.setDisable(false);
					txt_startzeit_h.setDisable(false);
					txt_startzeit_m.setDisable(false);
					txt_zielzeit_h.setDisable(false);
					txt_zielzeit_m.setDisable(false);
					btn_sw.setDisable(false);
				}
				
				
				if(charterart.equals("Einzelflug")){
					
					btn_startfh.setDisable(false);
					btn_zielfh.setDisable(false);
					dpi_startdat.setDisable(false);
					txt_pass.setDisable(false);
					txt_startzeit_h.setDisable(false);
					txt_startzeit_m.setDisable(false);
					btn_sw.setDisable(false);
				}
				
				if(charterart.equals("Flug mit Zwischenstationen")){
					
					btn_startfh.setDisable(false);
					btn_zielfh.setDisable(false);
					dpi_startdat.setDisable(false);
					txt_pass.setDisable(false);
					txt_startzeit_h.setDisable(false);
					txt_startzeit_m.setDisable(false);
					txt_zielzeit_h.setDisable(false);
					txt_zielzeit_m.setDisable(false);
					btn_sw.setDisable(false);
					btn_zw.setDisable(false);
					
				}
				
			}
			
			public void getBestFZ(){
				
 //##############Flugzeug finden
			    	    

			    
			    try{			    	
			    	Statement statement = conn.createStatement();
			    	ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM myflight.flugzeuge");      
			        while((rs != null) && (rs.next())){
			        	
			        	alleFZ = rs.getInt(1);
			         }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");            
			    }
			    
			    try{
			    	
			    	Statement statement = conn.createStatement();
			    	ResultSet rs = statement.executeQuery("SELECT count(distinct flugzeuge_Flugzeug_ID) FROM(SELECT rep.flugzeuge_Flugzeug_ID, rep.Datum_von, rep.Datum_bis FROM benutzerverwaltung.flugzeug_termine_reparatur rep UNION SELECT ang.flugzeuge_Flugzeug_ID, ang.Datum_von, ang.Datum_bis FROM benutzerverwaltung.flugzeug_termine_angebote INNER JOIN myflight.angebote ang ON benutzerverwaltung.flugzeug_termine_angebote.angebote_Angebote_ID=ang.Angebote_ID UNION SELECT war.flugzeuge_Flugzeug_ID, war.Datum_von, war.Datum_bis FROM benutzerverwaltung.flugzeug_termine_wartung war )dt INNER JOIN myflight.flugzeuge ON flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID INNER JOIN myflight.flugzeugtypen ON Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_bis between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_von < '"+Start_offer+"' and Datum_bis > '"+Ziel_offer+"'");      
			        while((rs != null) && (rs.next())){
			        	
			        	counter = rs.getInt(1);
			         }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");            
			    }
			    
			    System.out.println(counter);
			    
			    int[] FZbelegt = new int[alleFZ];
			    int i = 0;
			    
			    try{
			    	
			    	Statement statement = conn.createStatement();
			    	ResultSet rs = statement.executeQuery("SELECT DISTINCT Flugzeug_ID FROM(SELECT rep.flugzeuge_Flugzeug_ID, rep.Datum_von, rep.Datum_bis FROM benutzerverwaltung.flugzeug_termine_reparatur rep UNION SELECT ang.flugzeuge_Flugzeug_ID, ang.Datum_von, ang.Datum_bis FROM benutzerverwaltung.flugzeug_termine_angebote INNER JOIN myflight.angebote ang ON benutzerverwaltung.flugzeug_termine_angebote.angebote_Angebote_ID=ang.Angebote_ID UNION SELECT war.flugzeuge_Flugzeug_ID, war.Datum_von, war.Datum_bis FROM benutzerverwaltung.flugzeug_termine_wartung war )dt INNER JOIN myflight.flugzeuge ON flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID INNER JOIN myflight.flugzeugtypen ON Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_bis between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_von < '"+Start_offer+"' and Datum_bis > '"+Ziel_offer+"' ORDER BY Flugzeug_ID asc");      
			        while((rs != null) && (rs.next())){
			        	
			        	FZbelegt[i] = rs.getInt(1);
			        	i = i + 1;
			         }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");            
			    }
			    
			    for (int a=0; a<FZbelegt.length; a++){
			    System.out.println("ARRAY: " + FZbelegt[a]);
			    }
			    

			    
			    System.out.println(alleFZ);
			    
			    boolean FZgefunden = false;
			    boolean FZpassend = false;
			    

			    
			    try{
			    	
			    	Statement statement = conn.createStatement();
			    	ResultSet rs = statement.executeQuery("SELECT MAX(AnzahlPassagiere) FROM myflight.flugzeuge inner join myflight.flugzeugtypen ON myflight.flugzeuge.Flugzeugtypen_Flugzeugtypen_ID = myflight.flugzeugtypen.Flugzeugtypen_ID");      
			        while((rs != null) && (rs.next())){
			        	
			        	highpax = rs.getInt(1);
			         }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");            
			    }	
			    	
			    System.out.println("JETZT kommt das ARRAY für passendes " + alleFZ + " " + highpax);
			    
			    FZpass = alleFZ - counter;
			
			    System.out.println(FZpass + " wurden gefunden!!!!!");
			    if(FZpass == 0){System.out.println(FZpass + " nichts frei");} //TODO
			    else{
			    	
			    	int[] A_FZpass = new int[FZpass];
			    	int p = 0;
			    	int vergleich = 1;
			    	
			    	for (int q=0;vergleich<=alleFZ;q++){
					    
				    	if(vergleich == FZbelegt[q]){System.out.println(FZbelegt[q] + " " + q + " " + vergleich +" NICHTS gefunden!!!!!");}
				    	else{
				    	System.out.println(FZbelegt[q] + " " + q + " " + vergleich +" gefunden!!!!!");	
				    	A_FZpass[p] = vergleich;
				    	p = p + 1;
				    	if(FZbelegt[q] == 0){}
				    	else{q = q - 1;}	
				    	}
				    	
				    	vergleich = vergleich + 1;
				     }
			    
			    
			    	for (int a=0; a<A_FZpass.length; a++){
					    System.out.println("ARRAY: " + A_FZpass[a]);
					    }	
			    	
			    pax = Integer.valueOf(txt_pass.getText());
			    int FZ_pax = 0;
			    
			    do{
			    for(int z = 0; z<A_FZpass.length; z++){
			    
			    	 try{
					    	
					    	Statement statement = conn.createStatement();
					    	ResultSet rs = statement.executeQuery("SELECT * FROM myflight.flugzeuge inner join myflight.flugzeugtypen ON myflight.flugzeuge.Flugzeugtypen_Flugzeugtypen_ID = myflight.flugzeugtypen.Flugzeugtypen_ID JOIN myflight.lizenz ON Flugzeugtypen_ID=myflight.lizenz.Flugzeugtypen_Flugzeugtypen_ID WHERE Flugzeug_ID ="+A_FZpass[z]);      
					        while((rs != null) && (rs.next())){
					        	
					        	FZ_pax = rs.getInt(9);
					        	reichweite = rs.getInt(7);
					        	FixkostenFZ = rs.getInt(12);
					        	BetriebskFZ = rs.getInt(13);
					        	speed = rs.getInt(14);
					        	count_cop = rs.getInt(16);
					        	count_fa = rs.getInt(17);
					        	Lizenz = rs.getString(18);
					        	
					        	System.out.println("PAX:  " + FZ_pax + " " + pax);
					        	System.out.println("Kont:  " + StartKont + " " + ZielKont);
							    if(pax == FZ_pax){
							    	if(StartKont.equals("Europa") && ZielKont.equals("Amerika")){
							    		if(reichweite > 6000){
							    			bestFZ = A_FZpass[z];
									    	z = A_FZpass.length;
									    	FZgefunden = true;
							     		}
							    		if(StartKont.equals("Amerika") && ZielKont.equals("Europa")){
								    		if(reichweite > 6000){
								    			bestFZ = A_FZpass[z];
										    	z = A_FZpass.length;
										    	FZgefunden = true;
								     		}
							    		
							    		
							    	}

							    }
							    	else if(reichweite>entfernung || entfernung/reichweite < 2){
								       	bestFZ = A_FZpass[z];
								    	z = A_FZpass.length;
								    	FZgefunden = true;
								    	}
					         }
					        } 
					     }
					    catch(Exception e){
					          e.printStackTrace();
					          System.out.println("Error on Building Data");            
					    }	
			    	

			    	 
			    }
			    
			    pax = pax + 1;
			    if(pax>highpax){FZgefunden = true;
			    System.out.println("nichts"); //TODO
			    }
			  
			    
			    
			    }while(!FZgefunden);
			}
			    		    
			    System.out.println(bestFZ);
				
			}
			
			public void getBestPerson(){
				

				    
				    String SQL_per_cap = "SELECT Distinct(Personal_ID),Position_Gehalt_Position, Lizenz_Lizenz FROM myflight.personal join myflight.personal_has_lizenz ON Personal_ID=myflight.personal_has_lizenz.Personal_Personal_ID WHERE Position_Gehalt_Position='Captain' AND Lizenz_Lizenz ='"+ Lizenz+ "' AND Personal_ID not in( SELECT  Distinct(personal_Personal_ID)FROM ( SELECT kra.personal_Personal_ID, kra.Datum_von, kra.Datum_bis FROM benutzerverwaltung.personal_termine_krankheit kra UNION SELECT ang.Captain, ang.Datum_von, ang.Datum_bis FROM benutzerverwaltung.personal_termine_angebote JOIN myflight.angebote ang ON benutzerverwaltung.personal_termine_angebote.angebote_Angebote_ID=ang.Angebote_ID UNION SELECT url.personal_Personal_ID, url.Datum_von, url.Datum_bis FROM benutzerverwaltung.personal_termine_urlaub url)dt join myflight.personal ON personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_bis between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_von < '"+Start_offer+"' and Datum_bis > '"+Ziel_offer+"')";
				    
				    try{
				    	
				    	Statement statement = conn.createStatement();
				    	ResultSet rs = statement.executeQuery(SQL_per_cap);
				        while((rs != null) && (rs.next())){
				        	
				        	bestCaptain = rs.getInt(1);
				         }
				        
				    }
				    catch(Exception e){
				          e.printStackTrace();
				          System.out.println("Error on Building Data");            
				    }	
				    

					String SQL_per_cop = "SELECT Distinct(Personal_ID),Position_Gehalt_Position FROM myflight.personal WHERE Position_Gehalt_Position ='First Officer' AND Personal_ID not in( SELECT  Distinct(personal_Personal_ID)FROM ( SELECT kra.personal_Personal_ID, kra.Datum_von, kra.Datum_bis FROM benutzerverwaltung.personal_termine_krankheit kra UNION SELECT ang.Captain, ang.Datum_von, ang.Datum_bis FROM benutzerverwaltung.personal_termine_angebote JOIN myflight.angebote ang ON benutzerverwaltung.personal_termine_angebote.angebote_Angebote_ID=ang.Angebote_ID UNION SELECT url.personal_Personal_ID, url.Datum_von, url.Datum_bis FROM benutzerverwaltung.personal_termine_urlaub url)dt join myflight.personal ON personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_bis between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_von < '"+Start_offer+"' and Datum_bis > '"+Ziel_offer+"')";

				    if(count_cop != 0){
		
					    try{
					    	
					    	Statement statement = conn.createStatement();
					    	ResultSet rs = statement.executeQuery(SQL_per_cop);
					        while((rs != null) && (rs.next())){
					        	
					        	bestCOP = rs.getInt(1);
					         }
					        
					    }
					    catch(Exception e){
					          e.printStackTrace();
					          System.out.println("Error on Building Data");            
					    }	
				    }
				    

					String SQL_per_fa1 = "SELECT Distinct(Personal_ID),Position_Gehalt_Position FROM myflight.personal WHERE Position_Gehalt_Position ='Flight Attendant' AND Personal_ID not in( SELECT  Distinct(personal_Personal_ID)FROM ( SELECT kra.personal_Personal_ID, kra.Datum_von, kra.Datum_bis FROM benutzerverwaltung.personal_termine_krankheit kra UNION SELECT ang.Captain, ang.Datum_von, ang.Datum_bis FROM benutzerverwaltung.personal_termine_angebote JOIN myflight.angebote ang ON benutzerverwaltung.personal_termine_angebote.angebote_Angebote_ID=ang.Angebote_ID UNION SELECT url.personal_Personal_ID, url.Datum_von, url.Datum_bis FROM benutzerverwaltung.personal_termine_urlaub url)dt join myflight.personal ON personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_bis between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_von < '"+Start_offer+"' and Datum_bis > '"+Ziel_offer+"')";
					String SQL_per_fa2 = "SELECT Distinct(Personal_ID),Position_Gehalt_Position FROM myflight.personal WHERE Position_Gehalt_Position ='Flight Attendant' AND NOT Personal_ID=" + bestFA1 + " AND Personal_ID not in( SELECT  Distinct(personal_Personal_ID)FROM ( SELECT kra.personal_Personal_ID, kra.Datum_von, kra.Datum_bis FROM benutzerverwaltung.personal_termine_krankheit kra UNION SELECT ang.Captain, ang.Datum_von, ang.Datum_bis FROM benutzerverwaltung.personal_termine_angebote JOIN myflight.angebote ang ON benutzerverwaltung.personal_termine_angebote.angebote_Angebote_ID=ang.Angebote_ID UNION SELECT url.personal_Personal_ID, url.Datum_von, url.Datum_bis FROM benutzerverwaltung.personal_termine_urlaub url)dt join myflight.personal ON personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_bis between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_von < '"+Start_offer+"' and Datum_bis > '"+Ziel_offer+"')";
					String SQL_per_fa3 = "SELECT Distinct(Personal_ID),Position_Gehalt_Position FROM myflight.personal WHERE Position_Gehalt_Position ='Flight Attendant' AND NOT Personal_ID=" + bestFA1 + " AND NOT Personal_ID=" + bestFA2 + " AND Personal_ID not in( SELECT  Distinct(personal_Personal_ID)FROM ( SELECT kra.personal_Personal_ID, kra.Datum_von, kra.Datum_bis FROM benutzerverwaltung.personal_termine_krankheit kra UNION SELECT ang.Captain, ang.Datum_von, ang.Datum_bis FROM benutzerverwaltung.personal_termine_angebote JOIN myflight.angebote ang ON benutzerverwaltung.personal_termine_angebote.angebote_Angebote_ID=ang.Angebote_ID UNION SELECT url.personal_Personal_ID, url.Datum_von, url.Datum_bis FROM benutzerverwaltung.personal_termine_urlaub url)dt join myflight.personal ON personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_bis between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_von < '"+Start_offer+"' and Datum_bis > '"+Ziel_offer+"')";

				    
				    
				    if(count_fa != 0){
				    
				    	try{
					    	
					    	Statement statement = conn.createStatement();
					    	ResultSet rs = statement.executeQuery(SQL_per_fa1);
					        while((rs != null) && (rs.next())){
					        	
					        	bestFA1 = rs.getInt(1);
					         }
					        
					    }
					    catch(Exception e){
					          e.printStackTrace();
					          System.out.println("Error on Building Data");            
					    }	
				    	
				    	
				    }
				    
				    if(count_fa > 1){
				    
				    	try{
					    	
					    	Statement statement = conn.createStatement();
					    	ResultSet rs = statement.executeQuery(SQL_per_fa2);
					        while((rs != null) && (rs.next())){
					        	
					        	bestFA2 = rs.getInt(1);
					         }
					        
					    }
					    catch(Exception e){
					          e.printStackTrace();
					          System.out.println("Error on Building Data");            
					    }	
				    	
				    	
				    }
				    
				    
				    if(count_fa > 2){
				    
				    	try{
					    	
					    	Statement statement = conn.createStatement();
					    	ResultSet rs = statement.executeQuery(SQL_per_fa3);
					        while((rs != null) && (rs.next())){
					        	
					        	bestFA3 = rs.getInt(1);
					         }
					        
					    }
					    catch(Exception e){
					          e.printStackTrace();
					          System.out.println("Error on Building Data");            
					    }	
				    	
				    	
				    }
				    System.out.println("Reichweite " + reichweite);
				    System.out.println("entfernung: " + entfernung);
				    System.out.println("FZ: " + bestFZ);
				    System.out.println("CAP: " + bestCaptain);
				    System.out.println("Anzahl COP " + count_cop);
				    System.out.println("COP: " + bestCOP);
				    System.out.println("Anzahl FA " + count_fa);
				    System.out.println("FA1: " + bestFA1);
				    System.out.println("FA2: " + bestFA2);
				    System.out.println("FA3: " + bestFA3);
				    
				    

				    
				   	    

				
			}
			
			public void getEntfernung(){
				

				
				Str_StartFH = txt_startfh.getText();
				Str_ZielFH = txt_zielfh.getText();
				
			    try{
			    	
			    	Statement statement_start = conn.createStatement();
			    	ResultSet rs = statement_start.executeQuery("SELECT * FROM myflight.flughafen WHERE FlughafenKuerzel ='" + Str_StartFH + "'");      
			        while((rs != null) && (rs.next())){
			        	
			        	startfhlon = rs.getFloat(5);
			        	startfhlat = rs.getFloat(6);
			        	StartKont = rs.getString(7);
			        	
			         }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");            
			    }
				
			    try{
			    	
			    	Statement statement_ziel = conn.createStatement();
			    	ResultSet rs = statement_ziel.executeQuery("SELECT * FROM myflight.flughafen WHERE FlughafenKuerzel ='" + Str_ZielFH + "'");      
			        while((rs != null) && (rs.next())){
			        	
			        	zielfhlon = rs.getFloat(5);
			        	zielfhlat = rs.getFloat(6);
			        	ZielKont = rs.getString(7);
			         }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");            
			    }			    
			    
			    entfernung = (float) (Math.acos(Math.sin(startfhlat)*Math.sin(zielfhlat) + Math.cos(startfhlat)*Math.cos(zielfhlat)*Math.cos(zielfhlon - startfhlon))* 6371);
//			   //entfernung = 0.1;
//			    double test = Math.toDegrees(entfernung);
//			    double test2 = Math.toRadians(entfernung);
//			    double test3 = test * 6378.388;
			  
//			    entfernung = Math.acos(Math.sin(48.34)*Math.sin(53.87) + Math.cos(48.34)*Math.cos(53.87)*Math.cos(14.15 - 11.78)) * 6378.388  ;//6378.137;

			    
				
			}



			@FXML public void btn_fh_zws_click() {}



			@FXML public void btn_zwscount_click() {
				
				 if (txt_countzws.getText().matches("[0-5]") || txt_countzws.getText() == ""){//TODO
				 
				countzw = Integer.valueOf(txt_countzws.getText());
				 }
				 
				String[] FHzw = new String[countzw];
				String[] zw_an_h = new String[countzw];
				String[] zw_an_m = new String[countzw];
				String[] zw_ab_h = new String[countzw];
				String[] zw_ab_m = new String[countzw];
				LocalDate[] zw_an = new LocalDate[countzw];
				LocalDate[] zw_ab = new LocalDate[countzw];
				
				int x = 1;
				for(int i = 0; i < countzw; i++){
				cbo_zws.getItems().addAll(x);
				x = x +1;
				}
				
			}



			@FXML public void btn_zws_save_click() {}



			@FXML public void btn_zws_ok_click() {}



			@FXML public void btn_zws_stop_click() {}



			@FXML public void cbo_zws_click() {
				
				
			}
								
			
}
