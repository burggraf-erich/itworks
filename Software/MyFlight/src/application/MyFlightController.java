package application;
// V2.48


import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.lang.String;
import java.net.URISyntaxException;
 
import javafx.util.Callback;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.awt.image.BufferedImage;
// imports für PDF-Generator
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
//import com.levigo.jbig2.Bitmap;
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
//import java.time.LocalTime;
import java.util.Date;
import application.druckauftrag;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import application.SearchCustController;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.CheckBox;
import javafx.scene.input.InputMethodEvent;
import org.joda.time.LocalTime;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;

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
	private static final String WORD_STYLE_NORMAL = "Normal";
	
	//damit die ursprünglichen zusatzkosten vom preis wieder abgezogen werden können
	public float tmpzusatzkostenextracostedit;
	
	
	// Variablen für Combobox und Ausdruck
	public String AuswahlDokutyp;
	public String AuswahlAktion;
	public static String strFilename = "test.pdf";
	public static String strFilenamedoc = "test.docx";
	public static String filename = System.getProperty("user.dir") + "/" + strFilename;
	public static File f = new File(filename);

	public static int Dialog = -1;

	//Variablen für angemeldeten Benutzer
	private String vorname;
	private String nachname;
	private int berechtigungsstufe;
	private String Rolle;
//	private String user;
//	private String password;
	
	// Erzeuge ArrayListe für Tabellenversorgung
	private ObservableList<Angebote> angebotedata = FXCollections.observableArrayList();

	// gib Daten der ArrayListe zurück
	public ObservableList<Angebote> getangebotedata() {
		return angebotedata;
	}
	private ObservableList<Flugzeugdaten> flugzeugdata = FXCollections.observableArrayList();

	// gib Daten der ArrayListe zurück
	public ObservableList<Flugzeugdaten> getflugzeugdata() {
		return flugzeugdata;
	}
	private ObservableList<Flugziele> flugzieledata = FXCollections.observableArrayList();

	// gib Daten der ArrayListe zurück
	public ObservableList<Flugziele> getflugzieledata() {
		return flugzieledata;
	}
	private ObservableList<Kundendaten> kundendatendata = FXCollections.observableArrayList();

	// gib Daten der ArrayListe zurück
	public ObservableList<Kundendaten> getkundendatendata() {
		return kundendatendata;
	}
	private ObservableList<Fluege> tablefluegedata = FXCollections.observableArrayList();

	// gib Daten der ArrayListe zurück
	public ObservableList<Fluege> gettablefluegedata() {
		return tablefluegedata;
	}
	private ObservableList<Aufträge> auftraegedata = FXCollections.observableArrayList();

	public ObservableList<Aufträge> getauftraegedata() {
		return auftraegedata;
	}

	private ObservableList<Rechnungen> billdata = FXCollections.observableArrayList();

	public ObservableList<Rechnungen> getbilldata() {
		return billdata;
	}

	private ObservableList<RechnungenCost> costbilldata = FXCollections.observableArrayList();

	public ObservableList<RechnungenCost> getcostbilldata() {
		return costbilldata;
	}

	private ObservableList<RechnungenCostreminder> costreminder_warnings_billdata = FXCollections.observableArrayList();

	public ObservableList<RechnungenCostreminder> getcostreminder_warnings_billdata() {
		return costreminder_warnings_billdata;
	}
	private ObservableList<Personaldaten> personaldata = FXCollections.observableArrayList();
	
private ObservableList<FHSuche> FHData = FXCollections.observableArrayList();
private ObservableList<termbearb> termData = FXCollections.observableArrayList();
public ObservableList<FHSuche> getFHData() {

		return FHData;
	}
public ObservableList<termbearb> gettermData() {

	return termData;
}
	public ObservableList<Personaldaten> getpersonaldata() {
		return personaldata;
	}

	
	// zu Beginn besteht keine Autentifizierung und damit sind alle Menüpunkte und Buttons deaktiviert
	private boolean authenticated = false;
	
	public Connection conn;
	public Connection conn_benutzerverwaltung;
	Connection conn_new;
	int highest_custID = 0;
	public Statement stmt;
	public Statement stmt_benutzerverwaltung;
	
	//Variablen für Angebot erstellen
	
	public String phone;
	boolean StartFH = false;
	boolean ZielFH = false;
	boolean zwFH = false;
	
	String Str_StartFH = null;
	String Str_ZielFH = null;
	
	String StartKont = null;
	String ZielKont = null;
	String StartKont_zw = "Europa";
	String ZielKont_zw = "Europa";
	
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
	double[] zw_dauer;
	double zw_dauer_end;
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
	float entfernung_zw = 0;
	float[] hochentf;
	
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
    
    int zwischenstop_zw = 0;
    
    boolean fromzw = false;
    String zwstartfh = null;
    String zwzielfh = null;
    String CustState = null;
    
    boolean sonderw = false;
    
    boolean cust_set =false;
    
//<<<<<<< HEAD
    LocalDate profityear = null;
    int year = 0;
    
    boolean offer_new = false;
	
//=======
    String anrede;
    String kdgruppe_string;
	String lizenz_string;
	String position_string;
	String gehalt_string;
	String Flugzeit;
	static String modus;
    
	int newpersonal_id = 0;
	int newkunden_id = 0;
	
//>>>>>>> branch 'master' of https://github.com/burggraf-erich/itworks.git
	//Variablen für Kalender
	
	Date date;
	String sqlstat ="";	
	String cal_fzid = "";
    String cal_maid = "";

	String ori_startd = "";
	String ori_zield ="";
	String ori_szh ="";
	String ori_szm ="";
	String ori_zzh ="";
	String ori_zzm ="";
	String ori_art ="";
	int ori_id = 0;

    
		
	@FXML Button btn_close;
	@FXML Button btn_login;
	@FXML Button btn_cancel_createorder;
	@FXML Button btn_cancel_changeorder;
	@FXML Button btn_stop;
	@FXML Button btn_createoffer;
	@FXML Button btncreateorder;
	@FXML Button btnprint;
	@FXML Button btnprintangebot;
	@FXML Button btnsend;
	@FXML Button btnsendangebot;
	@FXML Button btncreatebill;
	@FXML Button btn_save_order;
	@FXML PasswordField pwf_password;
	@FXML Button btn_searchcustid;
	@FXML Button btn_stop_cust;
	@FXML Button angebotedit;
	@FXML Button btn_angebotstatusedit;
	@FXML Button btn_creat_cust;
	@FXML Button btn_changebillstatus;
	@FXML Button btn_cancelchangebillstatus;
	@FXML Button btn_costextracostedit;
	@FXML Button btn_createreminder;
	@FXML Button btn_canceltrackingedit;
	@FXML Button btn_costtrackingedit;
	@FXML Button btn_cancelcostextracostedit;
	@FXML Button btn_delete_order;
	@FXML Button btn_change_user;
	@FXML Button btn_save_billstatus;
	@FXML Button btn_save_costtrackingedit;
	@FXML Button btncreatepersonal;
	@FXML Button btnpersonaledit;
	@FXML Button btn_cancel_personaledit;
	@FXML Button btn_save_personal;
	@FXML Button btn_save_personalcreate;
	
	@FXML Button btnflugzeugedit;
	@FXML Button btn_cancel_flugzeugedit;
	@FXML Button btn_save_flugzeug;
	@FXML Button btn_save_flugzeugcreate;
	
	@FXML Button btnflugzieleedit;
	@FXML Button btn_cancel_flugzieleedit;
	@FXML Button btn_save_flugziele;
	@FXML Button btn_save_flugzielecreate;
	
	@FXML Button btnkundendatenedit;
	@FXML Button btn_cancel_kundendatenedit;
	@FXML Button btn_save_kundendaten;
	@FXML Button btn_save_kundendatencreate;
	
	@FXML AnchorPane apa_welcome;
	@FXML AnchorPane apa_login;
	@FXML AnchorPane Aufträgeübersicht;
	@FXML AnchorPane auftragübersichtbuttons;
	@FXML AnchorPane Rechnungenübersichtbuttons;
	@FXML AnchorPane Rechnungenübersicht;
	@FXML AnchorPane costtrackingoverview;
	@FXML AnchorPane costtrackingreminder_warnings;
	@FXML AnchorPane costtrackingedit;
	@FXML AnchorPane costextracostedit;
	
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
	@FXML AnchorPane anc_pane_personaldatenübersicht;
	@FXML AnchorPane ancpanebtn_changebillstatus;
	@FXML AnchorPane apa_btn_costtrackingreminder;
	@FXML AnchorPane apa_btn_costtrackingedit;
	@FXML AnchorPane apa_btn_costtrackingoverview;
	@FXML AnchorPane apa_btn_costextracostedit;
	@FXML AnchorPane apa_btn_personaldatenoverview;
	@FXML AnchorPane apa_personaledit;
	@FXML AnchorPane apa_btn_personaledit;

	@FXML AnchorPane anc_pane_flugzeugdatenübersicht;
	@FXML AnchorPane apa_btn_flugzeugdatenoverview;
	@FXML AnchorPane apa_flugzeugedit;
	@FXML AnchorPane apa_btn_flugzeugedit;

	@FXML AnchorPane anc_pane_flugzieleübersicht;
	@FXML AnchorPane apa_btn_flugzieleoverview;
	@FXML AnchorPane apa_flugzieleedit;
	@FXML AnchorPane apa_btn_flugzieleedit;
	
	@FXML AnchorPane anc_pane_kundendatenübersicht;
	@FXML AnchorPane apa_btn_kundendatenoverview;
	@FXML AnchorPane apa_kundendatenedit;
	@FXML AnchorPane apa_btn_kundendatenedit;
	
	@FXML AnchorPane apa_konfig;
	@FXML TextArea txa_history;
	@FXML Label Versionsnr;
	
	@FXML ScrollPane scroll_pane_order;
	@FXML ScrollPane scroll_pane_changeorder;
	@FXML ScrollPane scrollpane_changebillstatus;
	@FXML ScrollPane scroll_pane_angebotübersicht;
	@FXML ScrollPane scroll_pane_auftragübersicht;
	@FXML ScrollPane scroll_pane_rechnungenübersicht;
	@FXML ScrollPane scroll_pane_costtrackingoverview;
	@FXML ScrollPane scroll_pane_costtrackingreminder_warnings;
	@FXML ScrollPane scroll_pane_personaldaten;
	
	
	
	@FXML ScrollPane scroll_pane_flugzeugdaten;
	
	@FXML ScrollPane scroll_pane_flugziele;
	
	@FXML ScrollPane scroll_pane_kundendaten;
	
	
	@FXML Label lbl_dbconnect;
	@FXML Label lbl_username;
	@FXML Label lblrolle;
	@FXML Label lblberechtigung;
	@FXML Label maskentitel;
	@FXML Label Version;
	@FXML Label Version1;
	
	@FXML TitledPane mnudashboard;
	@FXML TitledPane mnufinanzverwaltung;
	@FXML TitledPane mnureporting;
	@FXML TitledPane mnuadministration;
	@FXML TitledPane mnucharter;
	@FXML TitledPane übersichtangebote;
	@FXML TitledPane übersichtaufträge;
	@FXML Hyperlink hlk_create_offer;
	@FXML Hyperlink mnuzusatzkosten;
	@FXML Hyperlink hlk_konfig;
	@FXML Hyperlink hlk_karenz_mahnung;
	
//Angebot erstellem
	@FXML TextField txt_username;
	@FXML TextField txt_companyname;
	@FXML TextField txt_street;
	@FXML TextField txt_place;
	@FXML TextField txt_homenumber;
	@FXML TextField txt_customerid;
	@FXML TextField txt_homeext;
	
//Angebot erstellen


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
	
	//Felder für Maske Erstelle Auftrag - Beginn
	
	@FXML TextField kdname;
	@FXML TextField kdvname;
	@FXML Label artcharter;
	@FXML TextField flgztyp;
	@FXML Label flgzkz;
	@FXML DatePicker datumvon;
	@FXML DatePicker datumbis;
	@FXML TextField abflugort;
	@FXML TextField ankunftort;
	@FXML TextField preisnetto;
	@FXML TextField preismwst;
	@FXML TextField preisbrutto;
	@FXML TextField charterdauer;
	@FXML TextField flugzeit;
	@FXML ImageView flgzpicture;
	@FXML TextField zwischenstop1; 
	@FXML TextField zwischenstop2;
	@FXML TextField zwischenstop3;
	
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
	
	
	//Felder für Maske Erstelle Auftrag - Ende
	
	//Felder für Maske Ändere Auftrag - Beginn
	
	@FXML TextField kdname1;
	@FXML TextField kdvname1;
	@FXML Label artcharter1;
	@FXML TextField flgztyp1;
	@FXML ImageView flgzpicture1;
	@FXML Label flgzkz1;
	@FXML DatePicker datumvon1;
	@FXML DatePicker datumbis1;
	@FXML TextField abflugort1;
	@FXML TextField ankunftort1;
	@FXML TextField preisnetto1;
	@FXML TextField preismwst1;
	@FXML TextField preisbrutto1;
	
	//Felder für Maske Ändere Auftrag - Ende

	//Felder für Maske Rechnungsstatus ändern - Beginn
	
	@FXML TextField kdname2;
	@FXML TextField kdvname2;
	@FXML Label artcharter2;
	@FXML TextField flgztyp2;
	@FXML Label flgzkz2;
	@FXML DatePicker datumvon2;
	@FXML DatePicker datumbis2;
	@FXML TextField abflugort2;
	@FXML TextField ankunftort2;
	@FXML TextField preisnetto2;
	@FXML TextField preismwst2;
	@FXML TextField preisbrutto2;
	//Felder für Maske Rechnungsstatus ändern - Ende
	
	//Felder für Maske Rechnungsstatus ändern - Beginn
	 @FXML TextField auftrag_idcosttrackingedit;
     @FXML TextField Auftragstatuscosttrackingedit;
     @FXML TextField Kdgruppecosttrackingedit;
     @FXML TextField preisaufschlagcosttrackingedit;
     @FXML TextField Kunden_idcosttrackingedit;
     @FXML TextField preisbruttocosttrackingedit;
     @FXML TextField Kdnamecosttrackingedit;
     @FXML TextField rechnung_idcosttrackingedit;
     @FXML TextField zusatzkostencosttrackingedit;
     @FXML TextField zahlungstermincosttrackingedit;
     @FXML Label rechnungstatuscosttrackingedit;
  //Felder für Maske Rechnungsstatus ändern - Ende
  
   //Felder für Maske Zusatzkosten für Rechnung - Beginn
   	 @FXML TextField auftrag_idextracostedit;
        @FXML TextField Auftragstatusextracostedit;
        @FXML TextField Kdgruppeextracostedit;
        @FXML TextField preisaufschlagextracostedit;
        @FXML TextField Kunden_idextracostedit;
        @FXML TextField preisbruttoextracostedit;
        @FXML TextField Kdnameextracostedit;
        @FXML TextField rechnung_idextracostedit;
        @FXML TextField zusatzkostenextracostedit;
        @FXML TextField zahlungsterminextracostedit;
        @FXML TextField rechnungstatusextracostedit;
      //Felder für Maske Zusatzkosten für Rechnung - Ende
     
        //Felder für Maske Personaledit  - Beginn
      	 @FXML TextField pid;
           @FXML TextField pname;
           @FXML TextField pvname;
           @FXML TextField pstatus;
           @FXML TextField pflugzeugtyp;
           @FXML ComboBox<String> cbo_lizenz;
           @FXML ComboBox<String> cbo_ppos;
           @FXML ComboBox<String> cbo_gehalt;
           
         //Felder für Maske Personaledit  - Ende
           
           //Felder für Maske Flugzeugedit  - Beginn
        	 @FXML TextField fid;
             @FXML TextField fstatus;
             @FXML TextField fname;
             @FXML TextField ftypid;
             @FXML TextField ftyp;
             
             
             @FXML TextField freichw;
             @FXML TextField fkm;
             @FXML TextField fpax;
             @FXML TextField ftrieb;
             @FXML TextField ftriebanz;
             @FXML TextField ffixk;
             @FXML TextField fbetriebk;
             @FXML TextField fgeschw;
             @FXML TextField fpilot;
             @FXML TextField fcopilot;
             @FXML TextField fcrew;
             
           //Felder für Maske Flugzeugedit  - Ende

             //Felder für Maske Flugzieleedit  - Beginn
             @FXML TextField fzflgh;
             @FXML TextField fzname;
             @FXML TextField fzstadt;
             @FXML TextField fzland;
             @FXML TextField fzlon;
             @FXML TextField fzlat;
             
             //Felder für Maske Flugzieleedit  - Ende

             //Felder für Maske Kundendatenedit  - Beginn
             @FXML TextField kdid;
             @FXML TextField kdverwname;
             @FXML TextField kdverwvname;
             @FXML TextField kdfirma;
          
             
                     
    
	
	@FXML TextField txt_mail;
	@FXML TextField txt_mobile;
	@FXML TextField txt_name;
	@FXML TextField txt_phone;
	@FXML TextField txt_prename;
    //Felder für Maske Kundendatenedit  - Ende

	
	@FXML ComboBox<String> cbo_salutation;
	@FXML ComboBox<String> cbo_title;  
	@FXML ComboBox<String> choiceorderstatus; 
	@FXML ComboBox<String> choicebillstatus;
	@FXML ComboBox<String> choicecostbillstatus;
	


	@FXML TextField txt_companyname_new;
	@FXML TextField txt_street_new;
	@FXML TextField txt_place_new;
	@FXML TextField txt_customerid_new;
	@FXML TextField txt_homeext_new;
	@FXML TextField txt_name_new;
	@FXML TextField txt_mobile_new;
	@FXML TextField txt_mail_new;
	@FXML TextField txt_phone_new;
	@FXML TextField txt_prename_new;
	@FXML TextField txt_postcode_new;
	@FXML TextField txt_country_new;

	//@FXML Hyperlink hlk_create_cust;

	

	
	@FXML ComboBox<String> cbo_country_new;
	@FXML ComboBox<String> cbo_kdgruppe;
	@FXML ComboBox<String> cbo_salutation_new;
	

	
	
	@FXML	TableView<Angebote> angebotetabelle;
	@FXML	TableColumn<Angebote, Integer> Nummer;
	@FXML	TableColumn<Angebote, String> Kdname;
	//@FXML	TableColumn<Angebote, String> Datum;
	@FXML	TableColumn<Angebote, String> Status;
	@FXML	TableColumn<Angebote, String> Kdgruppe;
	@FXML	TableColumn<Angebote, String> Kdvname;
	@FXML	TableColumn<Angebote, String> Aart;
	@FXML	TableColumn<Angebote, String>  Beginn;
	@FXML	TableColumn<Angebote, String>  Ende;
	
	@FXML	TableView<Aufträge> auftragtable;
	@FXML	TableColumn<Aufträge, Integer> Nummerorder;
	@FXML	TableColumn<Aufträge, String> datumauftragorder;
	@FXML	TableColumn<Aufträge, String> Kdnameorder;
	@FXML	TableColumn<Aufträge, String> Kdvnameorder;
	@FXML	TableColumn<Aufträge, String> Flgztyporder;
	@FXML	TableColumn<Aufträge, String> Beginnorder;
	@FXML	TableColumn<Aufträge, String> Endeorder;
	@FXML	TableColumn<Aufträge, Integer> billorder;
	@FXML	TableColumn<Aufträge, String> Statusorder;
	@FXML	TableColumn<Aufträge, String> Kdgruppeorder;
	@FXML	TableColumn<Aufträge, String> Aartorder;
	//@FXML	TableColumn<Aufträge, String> Datumorder;
	
	@FXML	TableView<Fluege> tablefluege;
	@FXML	TableColumn<Fluege, String> tablecoldateabflug;
	@FXML	TableColumn<Fluege, String> tablecoltimeabflug;
	@FXML	TableColumn<Fluege, String> tablecolortabflug;
	@FXML	TableColumn<Fluege, String> tablecolflugzeit;
	@FXML	TableColumn<Fluege, String> tablecoltimeankunft;
	@FXML	TableColumn<Fluege, String> tablecolortankunft;
	@FXML	TableColumn<Fluege, Integer> tablecolanzahlpax;
	
	
	
	

	@FXML	TableView<Rechnungen> billtable;
	@FXML	TableColumn<Rechnungen, Integer> Nummerbill;
	@FXML	TableColumn<Rechnungen, String> Statusbill;
	@FXML	TableColumn<Rechnungen, String> Datumtopay;
	@FXML	TableColumn<Rechnungen, Float> Preisbill;
	@FXML	TableColumn<Rechnungen, Float> Preisbill_aufschlag;
	@FXML	TableColumn<Rechnungen, Float> Preisbill_zusatzkosten;
	@FXML	TableColumn<Rechnungen, String> Kdgruppebill;
	@FXML	TableColumn<Rechnungen, String> Kdnamebill;
	@FXML	TableColumn<Rechnungen, Integer> Nummerorder_forbilltable;
	@FXML	TableColumn<Rechnungen, String> Statusorder_forbilltable;
	
	@FXML	TableView<RechnungenCost> costbilltable;
	@FXML	TableColumn<RechnungenCost, Integer> Nummercostbill;
	@FXML	TableColumn<RechnungenCost, String> Statuscostbill;
	@FXML	TableColumn<RechnungenCost, String> Kdnamecostbill;
	@FXML	TableColumn<RechnungenCost, String> Datumcosttopay;
	@FXML	TableColumn<RechnungenCost, Float> Preiscostbill;
	@FXML	TableColumn<RechnungenCost, Float> Preiscostbill_aufschlag;
	@FXML	TableColumn<RechnungenCost, Float> Preiscostbill_zusatzkosten;
	@FXML	TableColumn<RechnungenCost, String> Kdgruppecostbill;
	@FXML	TableColumn<RechnungenCost, Integer> Nummerorder_forcostbilltable;
	@FXML	TableColumn<RechnungenCost, String> Statusorder_forcostbilltable;
	
	@FXML	TableView<RechnungenCostreminder> costreminder_warnings_billtable;
	@FXML	TableColumn<RechnungenCostreminder, Integer> Nummercostreminder_warnings_bill;
	@FXML	TableColumn<RechnungenCostreminder, String> Statuscostreminder_warnings_bill;
	@FXML	TableColumn<RechnungenCostreminder, String> Kdnamecostreminder_warnings_bill;
	@FXML	TableColumn<RechnungenCostreminder, String> Datumcostreminder_warnings_topay;
	@FXML	TableColumn<RechnungenCostreminder, Float> Preiscostreminder_warnings_bill;
	@FXML	TableColumn<RechnungenCostreminder, Float> Preiscostreminder_warnings_bill_aufschlag;
	@FXML	TableColumn<RechnungenCostreminder, Float> Preiscostreminder_warnings_bill_zusatzkosten;
	@FXML	TableColumn<RechnungenCostreminder, String> Kdgruppecostreminder_warnings_bill;
	@FXML	TableColumn<RechnungenCostreminder, Integer> Nummerorder_forcostreminder_warnings_billtable;
	@FXML	TableColumn<RechnungenCostreminder, String> Statusorder_forcostreminder_warnings_billtable;

	@FXML	TableView<Personaldaten> personaltable;
	@FXML	TableColumn<Personaldaten, Integer> Personal_ID;
	@FXML	TableColumn<Personaldaten, Integer> Gehalt;
	@FXML	TableColumn<Personaldaten, String> PersonalName;
	@FXML	TableColumn<Personaldaten, String> PersonalVorname;
	@FXML	TableColumn<Personaldaten, String> Position_Gehalt_Position;
	@FXML	TableColumn<Personaldaten, String> Personalstatus_Personalstatus;
      
	@FXML	TableView<Flugzeugdaten> flugzeugtable;
	@FXML	TableColumn<Flugzeugdaten, Integer>  Flugzeug_ID;
	@FXML	TableColumn<Flugzeugdaten, String>  Flugzeugstatus_Flugzeugstatus;
	@FXML	TableColumn<Flugzeugdaten, String>  FlugzeugHersteller;
	@FXML	TableColumn<Flugzeugdaten, String>  FlugzeugTyp;
	
	@FXML	TableView<Flugziele> flugzieletable;
	@FXML	TableColumn<Flugziele, String>  FlughafenKuerzel;
	@FXML	TableColumn<Flugziele, String>   FlughafenName;
	@FXML	TableColumn<Flugziele, String>   FlughafenStadt;
	@FXML	TableColumn<Flugziele, String>   FlughafenLand;
	@FXML	TableColumn<Flugziele, Float>   FlughafenLon;
	@FXML	TableColumn<Flugziele, Float>   FlughafenLat;
	
	@FXML	TableView<Kundendaten> kundendatentable;
	@FXML	TableColumn<Kundendaten, Integer>  Kunde_ID;
	@FXML	TableColumn<Kundendaten, String>   KundeName;
	@FXML	TableColumn<Kundendaten, String>   KundeVorname;
	@FXML	TableColumn<Kundendaten, String>   KundeFirmenname;
	@FXML	TableColumn<Kundendaten, String>   Kundengruppen_Kundengruppen;
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
	
	@FXML Button btn_create_offer;
	@FXML HBox hbox_show_status;
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
	@FXML TitledPane acc_cal;
	@FXML AnchorPane apa_zws_new;
	@FXML AnchorPane apa_kf;
	@FXML RadioButton tgl_sg_per;
	@FXML ToggleGroup tgg_pers;
	@FXML RadioButton tgl_g_per;
	@FXML RadioButton tgl_b_per;
	@FXML RadioButton tgl_s_per;
	@FXML RadioButton tgl_ss_per;
	@FXML RadioButton tgl_na_per;
	@FXML RadioButton tgl_na_sw;
	@FXML RadioButton tgl_ss_sw;
	@FXML RadioButton tgl_s_sw;
	@FXML RadioButton tgl_b_sw;
	@FXML RadioButton tgl_g_sw;
	@FXML RadioButton tgl_sg_sw;
	@FXML RadioButton tgl_na_kom;
	@FXML ToggleGroup tgg_kom;
	
	
	@FXML AnchorPane apa_term_bearb;
	@FXML TableView<termbearb> tbl_term;
	@FXML TableColumn<termbearb, Integer> col_term_mafz;
	@FXML TableColumn<termbearb, String>  col_term_art;
	@FXML TableColumn<termbearb, String>  col_term_startd;
	@FXML TableColumn<termbearb, String>  col_term_startz;
	@FXML TableColumn<termbearb, String>  col_term_endd;
	@FXML TableColumn<termbearb, String>  col_term_endzeit;
	@FXML RadioButton tgb_term_bearb_ma;
	@FXML ToggleGroup tgg_term_bearb;
	@FXML RadioButton tgb_term_bearb_fz;
	@FXML DatePicker dpi_term_bearb_start;
	@FXML DatePicker dpi_term_bearb_end;
	@FXML ComboBox cbo_term_bearb_mafz;
	@FXML Button btn_term_bearb_search;
	@FXML Button btn_term_bearb_delete;
	@FXML Button btn_term_bearb_bearb;
	@FXML Label lbl_term_bearb_mafz;
	@FXML AnchorPane apa_term_1bearb;
	@FXML DatePicker dpi_term_1bearb_startd;
	@FXML DatePicker dpi_term_1bearb_endd;
	@FXML Label lbl_term_1bearb_mafz;
	@FXML Label lbl_term_bearb_mafz1;
	@FXML TextField txt_term_1bearb_mafz;
	@FXML TextField txt_term_1bearb_art;
	@FXML TextField txt_term_1bearb_startz_h;
	@FXML TextField txt_term_1bearb_startz_m;
	@FXML TextField txt_term_1bearb_endz_m;
	@FXML TextField txt_term_1bearb_endz_h;
	@FXML AnchorPane apa_termn_bearb_btn;
	@FXML Button btn_term_bearb_cancel;
	@FXML AnchorPane apa_term_1bearb_btn;
	@FXML Button btn_term_1bearb_save;
	@FXML Button btn_term_1bearb_cancel;
	
	@FXML AnchorPane apa_profit;
	@FXML ComboBox cbo_profit_year;
	
	@FXML Label lbl_profit_topfz1;
	@FXML Label lbl_profit_topfz2;
	@FXML Label lbl_profit_topfz3;
	@FXML Label lbl_profit_topfz4;
	@FXML Label lbl_profit_topfz5;
	@FXML Label lbl_profit_topfd4;
	@FXML Label lbl_profit_topfd5;
	@FXML Label lbl_profit_topfd3;
	@FXML Label lbl_profit_topfd2;
	@FXML Label lbl_profit_topfd1;
	@FXML Label lbl_profit_toppro1;
	@FXML Label lbl_profit_toppro2;
	@FXML Label lbl_profit_toppro3;
	@FXML Label lbl_profit_toppro4;
	@FXML Label lbl_profit_toppro5;
	@FXML Label lbl_profit_floppro1;
	@FXML Label lbl_profit_flopfd1;
	@FXML Label lbl_profit_flopfd2;
	@FXML Label lbl_profit_flopfd3;
	@FXML Label lbl_profit_flopfd4;
	@FXML Label lbl_profit_flopfd5;
	@FXML Label lbl_profit_flopfz5;
	@FXML Label lbl_profit_flopfz4;
	@FXML Label lbl_profit_flopfz3;
	@FXML Label lbl_profit_flopfz2;
	@FXML Label lbl_profit_flopfz1;
	@FXML ComboBox cbo_profit_fz;
	@FXML Label lbl_profit_pro;
	@FXML Label lbl_profit_dauer;
	@FXML Hyperlink hlk_profit;
	@FXML Label lbl_profit_floppro2;
	@FXML Label lbl_profit_floppro3;
	@FXML Label lbl_profit_floppro4;
	@FXML Label lbl_profit_floppro5;
	
	
	@FXML AnchorPane apa_btn_kf;
	@FXML Button btn_cancel_kf;
	@FXML Button btn_save_kf;
	@FXML RadioButton tgl_ss_kom;
	@FXML RadioButton tgl_s_kom;
	@FXML RadioButton tgl_b_kom;
	@FXML RadioButton tgl_g_kom;
	@FXML RadioButton tgl_sg_kom;
	@FXML RadioButton tgl_na_pue;
	@FXML ToggleGroup tgg_pue;
	@FXML RadioButton tgl_ss_pue;
	@FXML RadioButton tgl_s_pue;
	@FXML RadioButton tgl_b_pue;
	@FXML RadioButton tgl_g_pue;
	@FXML RadioButton tgl_sg_pue;
	@FXML RadioButton tgl_na_pre;
	@FXML ToggleGroup tgg_pre;
	@FXML RadioButton tgl_ss_pre;
	@FXML RadioButton tgl_s_pre;
	@FXML RadioButton tgl_b_pre;
	@FXML RadioButton tgl_g_pre;
	@FXML RadioButton tgl_sg_pre;
	@FXML Hyperlink hlk_create_feedback;
	@FXML Hyperlink hlk_create_ablehnung;
	@FXML ToggleGroup tgg_sw;
	@FXML AnchorPane apa_zufr;
	@FXML PieChart pie_zufr;
	@FXML ComboBox cbo_artzuf;
	@FXML DatePicker dpi_zuf_start;
	@FXML DatePicker dpi_zufr_end;
	@FXML Button btn_zufr_start;
	@FXML Hyperlink hlk_report_feedback;
	@FXML AnchorPane apa_ableh_ang;
	@FXML PieChart pie_ablehn;
	@FXML DatePicker dpi_ableh_start;
	@FXML DatePicker dpi_ableh_end;
	@FXML Button btn_ableh_start;
	@FXML ComboBox cbo_ang;
	@FXML ComboBox cbo_ablehn;
	@FXML Button btn_ablehn_ang_save;
	@FXML AnchorPane apa_btn_term;
	@FXML Button btn_term_edit;
	@FXML Button btn_term_create;
	@FXML AnchorPane apa_termnew_btn;
	@FXML Button btn_newterm_save;
	@FXML Button btn_newterm_cancel;
	@FXML AnchorPane apa_term_new;
	@FXML DatePicker dpi_term_ma_start;
	@FXML DatePicker dpi_term_ma_end;
	@FXML ComboBox cbo_term_ma;
	@FXML TextField txt_term_ma_starth;
	@FXML TextField txt_term_ma_endh;
	@FXML TextField txt_term_ma_endm;
	@FXML TextField txt_term_ma_startm;
	@FXML CheckBox chb_term_ma;
	@FXML CheckBox chb_term_fz;
	@FXML TextField txt_term_fz_startm;
	@FXML TextField txt_term_fz_endm;
	@FXML TextField txt_term_fz_endh;
	@FXML TextField txt_term_fz_starth;
	@FXML ComboBox cbo_term_fz;
	@FXML DatePicker dpi_term_fz_end;
	@FXML DatePicker dpi_term_fz_start;
	@FXML ComboBox cbo_term_maart;
	@FXML ComboBox cbo_term_fzart;
	@FXML RadioButton tgb_term_ma;
	@FXML ToggleGroup tgg_term;
	@FXML RadioButton tgb_term_fz;
	
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

		Version.setText("V2.48");
		Version1.setText("V2.48");

		// Initialize the person table with the two columns.
		Nummer.setCellValueFactory(cellData -> cellData.getValue().NummerProperty().asObject());
		Status.setCellValueFactory(cellData -> cellData.getValue().StatusProperty());
		Beginn.setCellValueFactory(cellData -> cellData.getValue().Datum_vonProperty());
		Ende.setCellValueFactory(cellData -> cellData.getValue().Datum_bisProperty());
		Aart.setCellValueFactory(cellData -> cellData.getValue().AartProperty());
		Kdgruppe.setCellValueFactory(cellData -> cellData.getValue().KdgruppeProperty());
		Kdname.setCellValueFactory(cellData -> cellData.getValue().KdnameProperty());
		Kdvname.setCellValueFactory(cellData -> cellData.getValue().KdvnameProperty());
		
		// Datenverknüpfung auftragtable

		Nummerorder.setCellValueFactory(cellData -> cellData.getValue().NummerorderProperty().asObject());
		datumauftragorder.setCellValueFactory(cellData -> cellData.getValue().datumauftragorderProperty());
		Flgztyporder.setCellValueFactory(cellData -> cellData.getValue().FlgztyporderProperty());
		Statusorder.setCellValueFactory(cellData -> cellData.getValue().StatusorderProperty());
		Aartorder.setCellValueFactory(cellData -> cellData.getValue().AartorderProperty());
		Kdgruppeorder.setCellValueFactory(cellData -> cellData.getValue().KdgruppeorderProperty());
		Kdnameorder.setCellValueFactory(cellData -> cellData.getValue().KdnameorderProperty());
		Kdvnameorder.setCellValueFactory(cellData -> cellData.getValue().KdvnameorderProperty());
		Beginnorder.setCellValueFactory(cellData -> cellData.getValue().BeginnorderProperty());
		Endeorder.setCellValueFactory(cellData -> cellData.getValue().EndeorderProperty());
		billorder.setCellValueFactory(cellData -> cellData.getValue().billorderProperty().asObject());
		
		// Datenverknüpfung billtable
		Nummerbill.setCellValueFactory(cellData -> cellData.getValue().NummerbillProperty().asObject());
		Statusbill.setCellValueFactory(cellData -> cellData.getValue().StatusbillProperty());
		Datumtopay.setCellValueFactory(cellData -> cellData.getValue().DatumtopayProperty());
		Preisbill.setCellValueFactory(cellData -> cellData.getValue().PreisbillProperty().asObject());
		Preisbill_aufschlag.setCellValueFactory(cellData -> cellData.getValue().Preisbill_aufschlagProperty().asObject());
		Preisbill_zusatzkosten.setCellValueFactory(cellData -> cellData.getValue().Preisbill_zusatzkostenProperty().asObject());
		Kdgruppebill.setCellValueFactory(cellData -> cellData.getValue().KdgruppebillProperty());
		Kdnamebill.setCellValueFactory(cellData -> cellData.getValue().KdnamebillProperty());
		
		// Datenverknüpfung costbilltable
		Nummercostbill.setCellValueFactory(cellData -> cellData.getValue().NummercostbillProperty().asObject());
		Statuscostbill.setCellValueFactory(cellData -> cellData.getValue().StatuscostbillProperty());
		Kdnamecostbill.setCellValueFactory(cellData -> cellData.getValue().KdnamecostbillProperty());
		Datumcosttopay.setCellValueFactory(cellData -> cellData.getValue().DatumcosttopayProperty());
		Preiscostbill.setCellValueFactory(cellData -> cellData.getValue().PreiscostbillProperty().asObject());
		Preiscostbill_aufschlag.setCellValueFactory(cellData -> cellData.getValue().Preiscostbill_aufschlagProperty().asObject());
		Preiscostbill_zusatzkosten.setCellValueFactory(cellData -> cellData.getValue().Preiscostbill_zusatzkostenProperty().asObject());
		Kdgruppecostbill.setCellValueFactory(cellData -> cellData.getValue().KdgruppecostbillProperty());
		
		// Datenverknüpfung costreminder_warnings_billtable
		Nummercostreminder_warnings_bill.setCellValueFactory(cellData -> cellData.getValue().Nummercostreminder_warnings_billProperty().asObject());
		Statuscostreminder_warnings_bill.setCellValueFactory(cellData -> cellData.getValue().Statuscostreminder_warnings_billProperty());
		Kdnamecostreminder_warnings_bill.setCellValueFactory(cellData -> cellData.getValue().Kdnamecostreminder_warnings_billProperty());
		Datumcostreminder_warnings_topay.setCellValueFactory(cellData -> cellData.getValue().Datumcostreminder_warnings_topayProperty());
		Preiscostreminder_warnings_bill.setCellValueFactory(cellData -> cellData.getValue().Preiscostreminder_warnings_billProperty().asObject());
		Preiscostreminder_warnings_bill_aufschlag.setCellValueFactory(cellData -> cellData.getValue().Preiscostreminder_warnings_bill_aufschlagProperty().asObject());
		Preiscostreminder_warnings_bill_zusatzkosten.setCellValueFactory(cellData -> cellData.getValue().Preiscostreminder_warnings_bill_zusatzkostenProperty().asObject());
		Kdgruppecostreminder_warnings_bill.setCellValueFactory(cellData -> cellData.getValue().Kdgruppecostreminder_warnings_billProperty());
		
		// Datenverknüpfung personaltable
		Personal_ID.setCellValueFactory(cellData -> cellData.getValue().pidProperty().asObject());
		PersonalName.setCellValueFactory(cellData -> cellData.getValue().pnameProperty());
		PersonalVorname.setCellValueFactory(cellData -> cellData.getValue().pvnameProperty());
		Position_Gehalt_Position.setCellValueFactory(cellData -> cellData.getValue().pposProperty());
		Gehalt.setCellValueFactory(cellData -> cellData.getValue().pgehaltProperty().asObject());
		Personalstatus_Personalstatus.setCellValueFactory(cellData -> cellData.getValue().pstatusProperty());
		
		// Datenverknüpfung flugzeugtable
		
	
		Flugzeug_ID.setCellValueFactory(cellData -> cellData.getValue().fidProperty().asObject());
		Flugzeugstatus_Flugzeugstatus.setCellValueFactory(cellData -> cellData.getValue().fstatusProperty());
		FlugzeugHersteller.setCellValueFactory(cellData -> cellData.getValue().fnameProperty());
		FlugzeugTyp.setCellValueFactory(cellData -> cellData.getValue().ftypProperty());
	
		// Datenverknüpfung flugzieletable
		
		FlughafenKuerzel.setCellValueFactory(cellData -> cellData.getValue().fzflghProperty());
		FlughafenName.setCellValueFactory(cellData -> cellData.getValue().fznameProperty());	
		FlughafenStadt.setCellValueFactory(cellData -> cellData.getValue().fzstadtProperty());
		FlughafenLand.setCellValueFactory(cellData -> cellData.getValue().fzlandProperty());
		FlughafenLon.setCellValueFactory(cellData -> cellData.getValue().fzlonProperty().asObject());
		FlughafenLat.setCellValueFactory(cellData -> cellData.getValue().fzlatProperty().asObject());
		
		// Datenverknüpfung kundendatentable
		
	
		Kunde_ID.setCellValueFactory(cellData -> cellData.getValue().kdidProperty().asObject());
		KundeName.setCellValueFactory(cellData -> cellData.getValue().kdverwnameProperty());
		KundeVorname.setCellValueFactory(cellData -> cellData.getValue().kdverwvnameProperty());	
		KundeFirmenname.setCellValueFactory(cellData -> cellData.getValue().kdfirmaProperty());
		Kundengruppen_Kundengruppen.setCellValueFactory(cellData -> cellData.getValue().kdgruppeProperty());
		
		// Datenverknüpfung tablefluege
		
		
		tablecolanzahlpax.setCellValueFactory(cellData -> cellData.getValue().tablecolanzahlpaxProperty().asObject());
		tablecolflugzeit.setCellValueFactory(cellData -> cellData.getValue().tablecolflugzeitProperty());
		tablecoldateabflug.setCellValueFactory(cellData -> cellData.getValue().tablecoldateabflugProperty());
		tablecoltimeabflug.setCellValueFactory(cellData -> cellData.getValue().tablecoltimeabflugProperty());
		tablecolortabflug.setCellValueFactory(cellData -> cellData.getValue().tablecolortabflugProperty());
		tablecoltimeankunft.setCellValueFactory(cellData -> cellData.getValue().tablecoltimeankunftProperty());
		tablecolortankunft.setCellValueFactory(cellData -> cellData.getValue().tablecolortankunftProperty());
		
//*********************************************************************************************************************************************
//*********************************************************************************************************************************************		
		angebotetabelle.setItems(getangebotedata());
		auftragtable.setItems(getauftraegedata());
		billtable.setItems(getbilldata());
		costbilltable.setItems(getcostbilldata());
		costreminder_warnings_billtable.setItems(getcostreminder_warnings_billdata());
		personaltable.setItems(getpersonaldata());
		
		flugzeugtable.setItems(getflugzeugdata());
		
		flugzieletable.setItems(getflugzieledata());
		
		kundendatentable.setItems(getkundendatendata());
		
		tablefluege.setItems(gettablefluegedata());
		
		
		//btn_login.setDefaultButton(true); 
		
	
	    
		//Buttons werden erst aktiv, wenn in der Tabelle ein Eintrag ausgewählt wurde
		btncreateorder.disableProperty().bind(Bindings.isEmpty(angebotetabelle.getSelectionModel().getSelectedIndices()));
	    btnprint.disableProperty().bind(Bindings.isEmpty(auftragtable.getSelectionModel().getSelectedIndices()));
	    btnprintangebot.disableProperty().bind(Bindings.isEmpty(angebotetabelle.getSelectionModel().getSelectedIndices()));
	    btnsend.disableProperty().bind(Bindings.isEmpty(auftragtable.getSelectionModel().getSelectedIndices()));
	//	btncreatebill.disableProperty().bind(Bindings.isEmpty(auftragtable.getSelectionModel().getSelectedIndices()));
		btnsendangebot.disableProperty().bind(Bindings.isEmpty(angebotetabelle.getSelectionModel().getSelectedIndices()));
		angebotedit.disableProperty().bind(Bindings.isEmpty(auftragtable.getSelectionModel().getSelectedIndices()));
		btn_changebillstatus.disableProperty().bind(Bindings.isEmpty(billtable.getSelectionModel().getSelectedIndices()));
		btn_costtrackingedit.disableProperty().bind(Bindings.isEmpty(costbilltable.getSelectionModel().getSelectedIndices()));
	//	btn_costextracostedit.disableProperty().bind(Bindings.isEmpty(costreminder_warnings_billtable.getSelectionModel().getSelectedIndices()));
		btn_createreminder.disableProperty().bind(Bindings.isEmpty(costreminder_warnings_billtable.getSelectionModel().getSelectedIndices()));
	//	btncreatepersonal.disableProperty().bind(Bindings.isEmpty(personaltable.getSelectionModel().getSelectedIndices()));
		btnpersonaledit.disableProperty().bind(Bindings.isEmpty(personaltable.getSelectionModel().getSelectedIndices()));
		btn_angebotstatusedit.disableProperty().bind(Bindings.isEmpty(angebotetabelle.getSelectionModel().getSelectedIndices()));
		
		btnflugzeugedit.disableProperty().bind(Bindings.isEmpty(flugzeugtable.getSelectionModel().getSelectedIndices()));
		btnflugzieleedit.disableProperty().bind(Bindings.isEmpty(flugzieletable.getSelectionModel().getSelectedIndices()));
		btnkundendatenedit.disableProperty().bind(Bindings.isEmpty(kundendatentable.getSelectionModel().getSelectedIndices()));
	
		//Flughafen Suche
	     tbc_iata.setCellValueFactory (cellData -> cellData.getValue().IATAProperty());
	     tbc_fhname.setCellValueFactory (cellData -> cellData.getValue().NameProperty());
	     tbc_stadt.setCellValueFactory (cellData -> cellData.getValue().StadtProperty());
	     tbc_land.setCellValueFactory (cellData -> cellData.getValue().LandProperty());
		 tbl_fh.setItems(getFHData());
		 
		//Termin bearbeiten Suche
		 col_term_mafz.setCellValueFactory (cellData -> cellData.getValue().idProperty().asObject());
		 col_term_art.setCellValueFactory (cellData -> cellData.getValue().termartProperty());
		 col_term_startd.setCellValueFactory (cellData -> cellData.getValue().startdProperty());
		 col_term_startz.setCellValueFactory (cellData -> cellData.getValue().zieldProperty());
		 col_term_endd.setCellValueFactory (cellData -> cellData.getValue().startzProperty());
		 col_term_endzeit.setCellValueFactory (cellData -> cellData.getValue().zielzProperty());
		
		 tbl_term.setItems(gettermData());
		
			apa_login.setVisible(true);
			apa_btn_login.setVisible(true);
			btn_login.setDefaultButton(true); 
} 
	
	
	
	
	public void connectDB(){
		
		String new_dbname = "myflight";
		String new_host = "172.20.1.24";
		String new_port = "3306";

 		
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
  //      System.out.println("geht nicht");   
//	        sqle.printStackTrace();
	        
	                
	        }
		
	}
	 
	
	
	@FXML public void btn_login_click(ActionEvent event) {
		
		
		final String hostname = "172.20.1.24"; 
        final String port = "3306"; 
        //final String dbname = "myflight"; 
        //String dbname = "mydb";
      String dbname = "benutzerverwaltung";
        user = txt_username.getText();
        password = pwf_password.getText(); 
		
     // Vor- und Nachnamen ermitteln
	    int pos = user.indexOf(".");
	    System.out.println(pos);
		if (pos <= 0)
			lbl_dbconnect.setText("Bitte User mit Vorname.Nachname eingeben");
		else {
			vorname = user.substring(0, pos);
			nachname = user.substring(pos + 1, user.length());
			try {
				Class.forName("org.gjt.mm.mysql.Driver").newInstance();
			} catch (Exception e) {
				lbl_dbconnect.setText("mysql-Treiber nicht geladen");
				e.printStackTrace();
			}
			try {
				String url = "jdbc:mysql://" + hostname + ":" + port + "/" + dbname;
				conn_benutzerverwaltung = DriverManager.getConnection(url, user, password);

				stmt_benutzerverwaltung = conn_benutzerverwaltung.createStatement();
				ResultSet rs = stmt_benutzerverwaltung
						.executeQuery("SELECT berechtigungen_berechtigungen_id FROM benutzer where benutzervorname='"
								+ vorname + "' and benutzernachname='" + nachname + "'");

				if ((rs != null) && (rs.next())) {

					berechtigungsstufe = rs.getInt(1);
					rs = stmt_benutzerverwaltung.executeQuery("SELECT Berechtigungen FROM berechtigungen where Berechtigungen_ID='"
							+ berechtigungsstufe + "'");
					rs.next();
					switch (rs.getString(1)) {
					case "Administrator":
						berechtigungsstufe = 3;
						Rolle = rs.getString(1);
						break;
					case "Key-User":
						berechtigungsstufe = 2;
						Rolle = rs.getString(1);
						break;
					case "User":
						berechtigungsstufe = 1;
						Rolle = rs.getString(1);
						break;
					default:
						berechtigungsstufe = 0;
						Rolle = "";
					}
				}

				lbl_dbconnect.setText("Anmeldung erfolgreich");
				apa_login.setVisible(false);
				apa_welcome.setVisible(true);
				lbl_username.setText(user);

				btn_login.setVisible(false);
				btn_change_user.setVisible(true);

				User userobject = new User(vorname, nachname, Rolle, berechtigungsstufe);
				System.out.println(berechtigungsstufe);
				authenticated = true;
				String userrolle = userobject.getrolle();
				lblrolle.setText(userrolle);
				lblberechtigung.setText(String.valueOf(userobject.getberechtigung()));

				// setze nach erfolgreicher Anmeldung je nach
				// Berechtigungsgruppe die Menüpunkte und Buttons aktiv
				if (authenticated) {
					mnudashboard.setDisable(false);
					mnufinanzverwaltung.setDisable(false);
					mnureporting.setDisable(false);
					mnucharter.setDisable(false);
				}

				if (userobject.getberechtigung() >= 2) {
					btn_costextracostedit.disableProperty().bind(
							Bindings.isEmpty(costreminder_warnings_billtable.getSelectionModel().getSelectedIndices()));
					btn_delete_order.disableProperty()
							.bind(Bindings.isEmpty(auftragtable.getSelectionModel().getSelectedIndices()));
					btn_createreminder.disableProperty().bind(
							Bindings.isEmpty(costreminder_warnings_billtable.getSelectionModel().getSelectedIndices()));
					btncreatebill.disableProperty().bind(
							Bindings.isEmpty(auftragtable.getSelectionModel().getSelectedIndices()));
				}
				if (userobject.getberechtigung() == 3) {
					mnuadministration.setDisable(false);
				}

				// conn.close();
				//
			} catch (SQLException sqle) {

				lbl_dbconnect.setText("Datenbankverbindung fehlgeschlagen");
				// System.out.println("geht nicht");
				sqle.printStackTrace();


			}
			
			
		}
		connectDB(); 
	}

	// private char[] substringBefore(Object setText, String string) {
	// 
	// return null;
	// }

	@FXML public void btn_close_click(ActionEvent event) {
		
		System.exit(0);
	}

	@FXML  public void actiongetangebote() throws Exception{
		actiongetangebotepgm(false);
	}
	
	public void actiongetangebotepgm(boolean showmessage) throws Exception, SQLException {
		// lbl_dbconnect.setText("Mouse geklickt!");

		set_allunvisible(showmessage);
		scroll_pane_angebotübersicht.setVisible(true);
		scroll_pane_angebotübersicht.setVvalue(0);
		angebotübersicht.setVisible(true);
		maskentitel.setVisible(true);
		maskentitel.setText("Übersicht Angebote");
		panebtnangebotübersicht.setVisible(true);
	
		
		try {
			String stringtmp;
			int inttmp;
			// connect method #1 - embedded driver im Falle einer lokalen Datenbankanbindung
			
		    
			
			//String dbURL1 = "jdbc:derby:c:/daten/wirtschaftsinformatik/4. semester/Wirtschaftsinformatikprojekt - Einführung/eigenes Projekt/entwicklung/db/codejava/webdb1;create=true";
			//Connection conn1 = DriverManager.getConnection(dbURL1);
			//if (conn1 != null) {
			//	System.out.println("Connected to database #1");
			//}

			
			
			// Statement stmt = conn1.createStatement();
		
			final String hostname = "172.20.1.24"; 
	        final String port = "3306"; 
	        String dbname = "myflight";
			String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
			Connection conn = DriverManager.getConnection(url, user, password);
		    if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
		    Statement stmt = conn.createStatement();
		
		
			// angebote-übersicht abrufen

			
//			ResultSet rs = stmt.executeQuery("SELECT angebote.*, fluege.datum_von, fluege.datum_bis, kunden.*, flugzeugtypen.flugzeugtyp FROM angebote INNER JOIN fluege on angebote.angebote_id=fluege.angebote_Angebote_ID inner join kunden inner join flugzeuge inner join flugzeugtypen on angebote.kunden_kunde_id= kunden.kunde_id and angebote.flugzeuge_Flugzeug_ID=flugzeuge.Flugzeug_ID and flugzeuge.Flugzeugtypen_Flugzeugtypen_ID=flugzeugtypen.Flugzeugtypen_ID left outer join auftraege on auftraege.Angebote_Angebote_ID=angebote.Angebote_ID where auftraege.Angebote_Angebote_ID is null group by angebote.angebote_id");
			
		    ResultSet rs = stmt.executeQuery("SELECT angebote.*, kunden.* FROM angebote INNER JOIN kunden on  angebote.kunden_kunde_id= kunden.kunde_id left outer join auftraege on auftraege.Angebote_Angebote_ID=angebote.Angebote_ID where auftraege.Angebote_Angebote_ID is null");		
			angebotedata.remove(0, angebotedata.size());
			int i = 1;
			// Testbeginn
			// rs = null;
			 // Testende
			
			while ((rs != null) && (rs.next())) {
				System.out.println(i++ + " " + rs.getInt(1) + " " + rs.getString(3) + " " +rs.getString(4) + " " + rs.getString(39) 
						+ " " + rs.getString(21) + " " + rs.getString(22) + " " + rs.getString(28) + " " + rs.getString(29));
				angebotedata.add(new Angebote(rs.getInt(1), rs.getString(3), rs.getString(4), rs.getString(39), rs.getString(21),rs.getString(22), rs.getString(28), rs.getString(29)));
			}
			
			//wenn die Datenbank bei der Entwicklung leer ist
			//angebotedata.add(new Angebote(303043,"22.05.2016","Einzelflug","CORP"));
			
			if (angebotedata.size()== 0 ) lbl_dbconnect.setText("keine Angebote vorhanden");
			
			if (rs != null) rs.close();
			//stmt.close();

			// conn1.close();

		} catch (SQLException ex) {
			lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			ex.printStackTrace();
		}

	}
	
	
	@FXML public void acc_chart_click(MouseEvent event) {}

	@FXML public void hlk_create_offer(ActionEvent event) {
		
		set_allunvisible(false);
		apa_create_offer.setVisible(true);
		apa_btn_createoffer.setVisible(true);
		
		maskentitel.setVisible(true);
		maskentitel.setText("Angebot erstellen");
		
		txt_zielzeit_h.setEditable(false);
		txt_zielzeit_m.setEditable(false);
		dpi_zieldat.setDisable(true);
		
		//@FXML TextField txt_test;
		txt_phone1.setText("");
		txt_companyname1.setText("");
		txt_street1.setText("");
		txt_place1.setText("");
		txt_customerid1.setText("");
		txt_homeext1.setText("");
		txt_name1.setText("");
		txt_mail1.setText("");
		txt_prename1.setText("");
		txt_anrede1.setText("");
		dpi_startdat.setValue(null);
		cbo_charterart.setValue("");
		dpi_zieldat.setValue(null);
		txt_pass.setText("");
		txt_startzeit_h.setText("");
		txt_startzeit_m.setText("");
		txt_zielzeit_h.setText("");
		txt_zielzeit_m.setText("");
		txt_startfh.setText("");
		txt_zielfh.setText("");
		cust_set = false;
		offer_new = true;
		
		
	
		//cbo_salutation.getItems().addAll("Herr","Frau");
		
	}
	
	public void set_allunvisible(boolean showmessage){
	
	    apa_login.setVisible(false);
	    apa_welcome.setVisible(false);
	    apa_create_offer.setVisible(false);
	    apa_btn_login.setVisible(false);
	    apa_btn_createoffer.setVisible(false);
	    apa_btn_create_cust.setVisible(false);
	    auftragändernform.setVisible(false);
	    angebotübersicht.setVisible(false);
	    Aufträgeübersicht.setVisible(false);
		auftragübersichtbuttons.setVisible(false);
		apa_charter.setVisible(false);
		
		//wenn showmessage = true, dann wird weiterhin die Statusmeldung vom Vorgänger-GV angezeigt
		if (!showmessage) lbl_dbconnect.setText("");
		
		maskentitel.setVisible(false);
		panebtnangebotübersicht.setVisible(false);
		ancpanebtn_createorder.setVisible(false);
	//  scroll_pane_order.setVisible(false);
		ancpanebtn_changeorder.setVisible(false);
		scroll_pane_changeorder.setVisible(false);
		Rechnungenübersichtbuttons.setVisible(false);
		Rechnungenübersicht.setVisible(false);
	//	scrollpane_changebillstatus.setVisible(false);
	//	apa_formchangebillstatus.setVisible(false);
		ancpanebtn_changebillstatus.setVisible(false);	
		scroll_pane_angebotübersicht.setVisible(false);
		scroll_pane_auftragübersicht.setVisible(false);
		costtrackingoverview.setVisible(false);
		costtrackingreminder_warnings.setVisible(false);
		costtrackingedit.setVisible(false);
		costextracostedit.setVisible(false);
		apa_btn_costtrackingreminder.setVisible(false);
		apa_btn_costtrackingedit.setVisible(false);
		apa_btn_costtrackingoverview.setVisible(false);
		apa_btn_costextracostedit.setVisible(false);
		apa_btn_personaldatenoverview.setVisible(false);
		scroll_pane_costtrackingoverview.setVisible(false);
		scroll_pane_costtrackingreminder_warnings.setVisible(false);
		scroll_pane_rechnungenübersicht.setVisible(false);	
		scroll_pane_personaldaten.setVisible(false);
		anc_pane_personaldatenübersicht.setVisible(false);
		apa_personaledit.setVisible(false);
		apa_btn_personaledit.setVisible(false);
		
		scroll_pane_flugzeugdaten.setVisible(false);
		anc_pane_flugzeugdatenübersicht.setVisible(false);
		apa_btn_flugzeugdatenoverview.setVisible(false);
		apa_flugzeugedit.setVisible(false);
		apa_btn_flugzeugedit.setVisible(false);
		
		scroll_pane_flugziele.setVisible(false);
		anc_pane_flugzieleübersicht.setVisible(false);
		apa_btn_flugzieleoverview.setVisible(false);
		apa_flugzieleedit.setVisible(false);
		apa_btn_flugzieleedit.setVisible(false);

		scroll_pane_kundendaten.setVisible(false);
		anc_pane_kundendatenübersicht.setVisible(false);
		apa_btn_kundendatenoverview.setVisible(false);
		apa_kundendatenedit.setVisible(false);
		apa_btn_kundendatenedit.setVisible(false);
		
		apa_search_fh.setVisible(false);
		apa_sonder.setVisible(false);
		apa_btn_sonder.setVisible(false);
		apa_zws_new.setVisible(false);
		apa_btn_zws.setVisible(false);
		apa_calendar.setVisible(false);
		
		apa_kf.setVisible(false);
		apa_btn_kf.setVisible(false);
		
		apa_zufr.setVisible(false);
		apa_ableh_ang.setVisible(false);

		apa_termnew_btn.setVisible(false);
		apa_term_new.setVisible(false);
		apa_btn_term.setVisible(false);
		
//<<<<<<< HEAD
		apa_profit.setVisible(false);
		apa_termn_bearb_btn.setVisible(false);
		apa_term_bearb.setVisible(false);
		apa_term_1bearb.setVisible(false);
		apa_term_1bearb_btn.setVisible(false);
//=======
		
		apa_konfig.setVisible(false);
//>>>>>>> branch 'master' of https://github.com/burggraf-erich/itworks.git
		
	}

	@FXML public void btn_createoffer_click(ActionEvent event) {
		

	}

	@FXML public void hlk_create_cust(ActionEvent event) {
		
		//int i = 0;
		String new_custID;
		
		set_allunvisible(false);
		apa_create_cust.setVisible(true);
		apa_btn_create_cust.setVisible(true);
		
		cbo_salutation_new.getItems().addAll("Herr","Frau");
		cbo_country_new.getItems().addAll("Germany", "United States", "China");
		cbo_kdgruppe.getItems().clear();
		cbo_kdgruppe.getItems().addAll("PRE","CORP","VIP");
		
		
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
	//	final String custstatus_new = cbo_custstatus_new.getValue().toString();
		
	/*	try { 

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
				//			+custstatus_new+"')");

			}
	
		catch(Exception e){
			System.err.println("Got an exception! "); 
            System.err.println(e.getMessage()); 
			}
//	
} */
	}
	@FXML public void btn_stop_click(ActionEvent event) {}
	
	@FXML  public void actiongetaufträge(){
		actiongetaufträgepgm(false);
	}
	public void actiongetaufträgepgm(boolean showmessage) {
		// lbl_dbconnect.setText("Mouse geklickt!");

		set_allunvisible(showmessage);
		scroll_pane_auftragübersicht.setVisible(true);
		scroll_pane_auftragübersicht.setVvalue(0);
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
	/*		final String hostname = "172.20.1.24"; 
	        final String port = "3306"; 
	        String dbname = "myflight";
			String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
		    if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
		*/	
		//	Statement stmt = conn.createStatement();
			
			final String hostname = "172.20.1.24"; 
	        final String port = "3306"; 
	        String dbname = "myflight";
			String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
			conn = DriverManager.getConnection(url, user, password);
			//if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
			
			stmt = conn.createStatement();
			dbname = "benutzerverwaltung";
			conn_benutzerverwaltung = DriverManager.getConnection(url, user, password);
			stmt_benutzerverwaltung = conn_benutzerverwaltung.createStatement();
			
			// Aufträge-übersicht abrufen
	//		ResultSet rs = stmt.executeQuery("SELECT auftraege.*, angebote.*, fluege.datum_von, fluege.datum_bis, kunden.*, flugzeugtypen.flugzeugtyp, rechnungen.Rechnungen_ID FROM auftraege inner join angebote INNER JOIN fluege inner join kunden inner join flugzeuge inner join flugzeugtypen on angebote.angebote_id=fluege.angebote_Angebote_ID and angebote.kunden_kunde_id= kunden.kunde_id and angebote.flugzeuge_Flugzeug_ID=flugzeuge.Flugzeug_ID and flugzeuge.Flugzeugtypen_Flugzeugtypen_ID=flugzeugtypen.Flugzeugtypen_ID and angebote.angebote_id=auftraege.Angebote_Angebote_ID left outer join rechnungen on auftraege.Auftraege_ID=rechnungen.Auftraege_Auftraege_ID group by auftraege.auftraege_id");
			ResultSet rs = stmt.executeQuery("SELECT auftraege.*, angebote.*, kunden.*, flugzeugtypen.flugzeugtyp, rechnungen.Rechnungen_ID FROM auftraege inner join angebote inner join kunden inner join flugzeuge inner join flugzeugtypen on angebote.kunden_kunde_id= kunden.kunde_id and angebote.flugzeuge_Flugzeug_ID=flugzeuge.Flugzeug_ID and flugzeuge.Flugzeugtypen_Flugzeugtypen_ID=flugzeugtypen.Flugzeugtypen_ID and angebote.angebote_id=auftraege.Angebote_Angebote_ID left outer join rechnungen on auftraege.Auftraege_ID=rechnungen.Auftraege_Auftraege_ID group by auftraege.auftraege_id");
			auftraegedata.remove(0, auftraegedata.size());
			int i = 1;
			// Testbeginn
			// rs = null;
			 // Testende
			
			while ((rs != null) && (rs.next())) {
				System.out.println(i++ + " " + rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " "
						+ rs.getString(8) + " " + rs.getString(43) + " " + rs.getString(32) + " " + rs.getString(33) + " " + rs.getString(44)+ " " + rs.getString(25)+ " " + rs.getString(26)+ " " + rs.getInt(45));
				auftraegedata.add(new Aufträge(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(8), rs.getString(43),rs.getString(32), rs.getString(33), rs.getString(44), rs.getString(25), rs.getString(26), rs.getInt(45)));
			}
			
			//wenn die Datenbank bei der Entwicklung leer ist
			//angebotedata.add(new Angebote(303043,"22.05.2016","Einzelflug","CORP"));
			
			if (auftraegedata.size()== 0 ) lbl_dbconnect.setText("keine Aufträge vorhanden");
						
			if (rs != null) rs.close();
			//stmt.close();

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

	@FXML	public void actiongetrechnungen() {
		// lbl_dbconnect.setText("Mouse geklickt!");

		set_allunvisible(false);
		scroll_pane_rechnungenübersicht.setVisible(true);
		scroll_pane_rechnungenübersicht.setVvalue(0);
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

			final String hostname = "172.20.1.24"; 
	        final String port = "3306"; 
	        String dbname = "myflight";
			String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
			conn = DriverManager.getConnection(url, user, password);
			//if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
			
			stmt = conn.createStatement();
			dbname = "benutzerverwaltung";
			conn_benutzerverwaltung = DriverManager.getConnection(url, user, password);
			stmt_benutzerverwaltung = conn_benutzerverwaltung.createStatement();
		
			
			
			
			// Rechnungen-übersicht abrufen
	//		ResultSet rs = stmt.executeQuery("SELECT auftraege.*, angebote.*, fluege.datum_von, fluege.datum_bis, kunden.*, flugzeugtypen.flugzeugtyp, rechnungen.* FROM auftraege inner join angebote INNER JOIN fluege on angebote.angebote_id=fluege.angebote_Angebote_ID inner join kunden inner join flugzeuge inner join flugzeugtypen on angebote.kunden_kunde_id= kunden.kunde_id inner join rechnungen on rechnungen.auftraege_auftraege_id=auftraege.auftraege_id and angebote.flugzeuge_Flugzeug_ID=flugzeuge.Flugzeug_ID and flugzeuge.Flugzeugtypen_Flugzeugtypen_ID=flugzeugtypen.Flugzeugtypen_ID and auftraege.angebote_angebote_id = angebote.angebote_id group by rechnungen.rechnungen_id");
			ResultSet rs = stmt.executeQuery("SELECT auftraege.*, angebote.*, kunden.*, flugzeugtypen.flugzeugtyp, rechnungen.* FROM auftraege inner join angebote inner join kunden inner join flugzeuge inner join flugzeugtypen on angebote.kunden_kunde_id= kunden.kunde_id inner join rechnungen on rechnungen.auftraege_auftraege_id=auftraege.auftraege_id and angebote.flugzeuge_Flugzeug_ID=flugzeuge.Flugzeug_ID and flugzeuge.Flugzeugtypen_Flugzeugtypen_ID=flugzeugtypen.Flugzeugtypen_ID and auftraege.angebote_angebote_id = angebote.angebote_id group by rechnungen.rechnungen_id");
			
			
			
			billdata.remove(0, billdata.size());
			int i = 1;
			// Testbeginn
			// rs = null;
			 // Testende
			
			while ((rs != null) && (rs.next())) {
				System.out.println(i++ + " " + rs.getInt(45) + " " + rs.getString(48) + " " + rs.getString(32) + " "
						+ rs.getString(47) + " " + rs.getFloat(11)+ " " + rs.getFloat(17)+ " " + rs.getFloat(13)+ " " + rs.getString(43) );
				billdata.add(new Rechnungen(rs.getInt(45), rs.getString(48), rs.getString(32), rs.getString(47), rs.getFloat(11), rs.getFloat(17), rs.getFloat(13), rs.getString(43)));
			}
						
			
			if (billdata.size()== 0 ) lbl_dbconnect.setText("keine Rechnungen vorhanden");
			
			if (rs != null) rs.close();
		//	stmt.close();

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
	
	@FXML	public void actiongetcosttrackingoverview() {
		// lbl_dbconnect.setText("Mouse geklickt!");

		set_allunvisible(false);
		scroll_pane_costtrackingoverview.setVisible(true);
		scroll_pane_costtrackingoverview.setVvalue(0);
		costtrackingoverview.setVisible(true);
		apa_btn_costtrackingoverview.setVisible(true);
		maskentitel.setVisible(true);
		maskentitel.setText("Zahlungseingänge verfolgen");
		try {

			// connect method #1 - embedded driver
			
		    
			
			//String dbURL1 = "jdbc:derby:c:/daten/wirtschaftsinformatik/4. semester/Wirtschaftsinformatikprojekt - Einführung/eigenes Projekt/entwicklung/db/codejava/webdb1;create=true";
			//Connection conn1 = DriverManager.getConnection(dbURL1);
			//if (conn1 != null) {
			//	System.out.println("Connected to database #1");
			//}

			// Statement stmt = conn1.createStatement();

			final String hostname = "172.20.1.24"; 
	        final String port = "3306"; 
	        String dbname = "myflight";
			String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
			Connection conn = DriverManager.getConnection(url, user, password);
		    if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
		    Statement stmt = conn.createStatement();
			
		//	Statement stmt = conn.createStatement();
			
			// Rechnungen-übersicht abrufen, die noch nicht bezahlt sind
			
			// Rechnungen-übersicht abrufen
	//		ResultSet rs = stmt.executeQuery("SELECT auftraege.*, angebote.*, fluege.datum_von, fluege.datum_bis, kunden.*, flugzeugtypen.flugzeugtyp, rechnungen.* FROM auftraege inner join angebote INNER JOIN fluege on angebote.angebote_id=fluege.angebote_Angebote_ID inner join kunden inner join flugzeuge inner join flugzeugtypen on angebote.kunden_kunde_id= kunden.kunde_id inner join rechnungen on rechnungen.auftraege_auftraege_id=auftraege.auftraege_id and angebote.flugzeuge_Flugzeug_ID=flugzeuge.Flugzeug_ID and flugzeuge.Flugzeugtypen_Flugzeugtypen_ID=flugzeugtypen.Flugzeugtypen_ID and auftraege.angebote_angebote_id = angebote.angebote_id and rechnungen.rechnungsstatus_rechnungsstatus<>'bezahlt' and rechnungen.Rechnungsstatus_Rechnungsstatus<>'erstellt'  group by rechnungen.rechnungen_id");
		    ResultSet rs = stmt.executeQuery("SELECT auftraege.*, angebote.*, kunden.*, flugzeugtypen.flugzeugtyp, rechnungen.* FROM auftraege inner join angebote INNER JOIN kunden inner join flugzeuge inner join flugzeugtypen on angebote.kunden_kunde_id= kunden.kunde_id inner join rechnungen on rechnungen.auftraege_auftraege_id=auftraege.auftraege_id and angebote.flugzeuge_Flugzeug_ID=flugzeuge.Flugzeug_ID and flugzeuge.Flugzeugtypen_Flugzeugtypen_ID=flugzeugtypen.Flugzeugtypen_ID and auftraege.angebote_angebote_id = angebote.angebote_id and rechnungen.rechnungsstatus_rechnungsstatus<>'bezahlt' and rechnungen.Rechnungsstatus_Rechnungsstatus<>'erstellt'  group by rechnungen.rechnungen_id");

		
			
			
			
			costbilldata.remove(0, costbilldata.size());
			int i = 1;
			// Testbeginn
			// rs = null;
			 // Testende
			
			while ((rs != null) && (rs.next())) {
				System.out.println(i++ + " " + rs.getInt(45) + " " + rs.getString(48) + " " + rs.getString(32) + " " + rs.getString(47) + " " + rs.getFloat(11)+ " " + rs.getFloat(17)+ " " + rs.getFloat(13)+ " " + rs.getString(43) );
				
				costbilldata.add(new RechnungenCost(rs.getInt(45), rs.getString(48), rs.getString(32), rs.getString(47), rs.getFloat(11), rs.getFloat(17), rs.getFloat(13), rs.getString(43)));
		
				
		}
		//wenn die Datenbank bei der Entwicklung leer ist
//			billdata.remove(0, billdata.size());
//			billdata.add(new Rechnungen(30302,"erstellt","2016-05-16",2450.45F,150.00F,15.00F,"PRE"));
//			billdata.add(new Rechnungen(30514,"verschickt","2016-05-14",5300.00F,0.00F,0.00F,"CORP"));
						
			
			if (costbilldata.size()== 0 ) lbl_dbconnect.setText("keine Rechnungen vorhanden");
			
			if (rs != null) rs.close();
		//	stmt.close();

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
	
@FXML public void actiongetcosttrackingreminder_warnings() {
	actiongetcosttrackingreminder_warningspgm(false);
}
	
	public void actiongetcosttrackingreminder_warningspgm(boolean showmessage) {
		// lbl_dbconnect.setText("Mouse geklickt!");

		set_allunvisible(showmessage);
		scroll_pane_costtrackingreminder_warnings.setVisible(true);
		scroll_pane_costtrackingreminder_warnings.setVvalue(0);
		costtrackingreminder_warnings.setVisible(true);
		apa_btn_costtrackingreminder.setVisible(true);
		maskentitel.setVisible(true);
		maskentitel.setText("Zahlungserinnerungen/-Mahnungen");
		
		LocalDate tagheute;
		tagheute = LocalDate.now( );
		
		
	
		
		try {

			// connect method #1 - embedded driver
			
		    
			
			//String dbURL1 = "jdbc:derby:c:/daten/wirtschaftsinformatik/4. semester/Wirtschaftsinformatikprojekt - Einführung/eigenes Projekt/entwicklung/db/codejava/webdb1;create=true";
			//Connection conn1 = DriverManager.getConnection(dbURL1);
			//if (conn1 != null) {
			//	System.out.println("Connected to database #1");
			//}

			// Statement stmt = conn1.createStatement();

			final String hostname = "172.20.1.24"; 
	        final String port = "3306"; 
	        String dbname = "myflight";
			String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
			Connection conn = DriverManager.getConnection(url, user, password);
		    if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
		    Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select mahndauer.dauer from mahndauer");
			rs.next();
			int karenz_mahntage = rs.getInt(1);
		    
	//		Statement stmt = conn.createStatement();
			
			// Rechnungen-übersicht abrufen, deren Zahlungstermin überschritten ist
			
		//rs = stmt.executeQuery("SELECT auftraege.*, angebote.*, fluege.datum_von, fluege.datum_bis, kunden.*, flugzeugtypen.flugzeugtyp, rechnungen.* FROM auftraege inner join angebote INNER JOIN fluege on angebote.angebote_id=fluege.angebote_Angebote_ID inner join kunden inner join flugzeuge inner join flugzeugtypen on angebote.kunden_kunde_id= kunden.kunde_id inner join rechnungen on rechnungen.auftraege_auftraege_id=auftraege.auftraege_id and angebote.flugzeuge_Flugzeug_ID=flugzeuge.Flugzeug_ID and flugzeuge.Flugzeugtypen_Flugzeugtypen_ID=flugzeugtypen.Flugzeugtypen_ID and auftraege.angebote_angebote_id=angebote.angebote_id and rechnungen.rechnungsstatus_rechnungsstatus<>'bezahlt' and rechnungen.Rechnungsstatus_Rechnungsstatus<>'erstellt'  group by rechnungen.rechnungen_id");
		  rs = stmt.executeQuery("SELECT auftraege.*, angebote.*, kunden.*, flugzeugtypen.flugzeugtyp, rechnungen.* FROM auftraege inner join angebote INNER JOIN kunden inner join flugzeuge inner join flugzeugtypen on angebote.kunden_kunde_id= kunden.kunde_id inner join rechnungen on rechnungen.auftraege_auftraege_id=auftraege.auftraege_id and angebote.flugzeuge_Flugzeug_ID=flugzeuge.Flugzeug_ID and flugzeuge.Flugzeugtypen_Flugzeugtypen_ID=flugzeugtypen.Flugzeugtypen_ID and auftraege.angebote_angebote_id=angebote.angebote_id and rechnungen.rechnungsstatus_rechnungsstatus<>'bezahlt' and rechnungen.Rechnungsstatus_Rechnungsstatus<>'erstellt'  group by rechnungen.rechnungen_id");
			
				
			costreminder_warnings_billdata.remove(0, costreminder_warnings_billdata.size());
			int i = 1;
			// Testbeginn
			// rs = null;
			 // Testende
			
			while ((rs != null) && (rs.next())) {
				System.out.println(i++ + " " + rs.getInt(45) + " " + rs.getString(48) + " " + rs.getString(32) + " " + rs.getString(47) + " " + rs.getFloat(11)+ " " + rs.getFloat(17)+ " " + rs.getFloat(50)+ " " + rs.getString(43) );
			
				LocalDate zahlungstermin = rs.getDate(47).toLocalDate();
				LocalDate mahnungstermin = zahlungstermin.plusDays(karenz_mahntage);
				System.out.println(mahnungstermin.toString());
				
				
				if(mahnungstermin.isBefore(tagheute))
				{
				if (!rs.getString(43).equals("VIP")) {		
				costreminder_warnings_billdata.add(new RechnungenCostreminder(rs.getInt(45), rs.getString(48), rs.getString(32), rs.getString(47), rs.getFloat(11), rs.getFloat(17), rs.getFloat(50), rs.getString(43)));
				}
				}
			}
		//	}
		//wenn die Datenbank bei der Entwicklung leer ist
//			billdata.remove(0, billdata.size());
//			billdata.add(new Rechnungen(30302,"erstellt","2016-05-16",2450.45F,150.00F,15.00F,"PRE"));
//			billdata.add(new Rechnungen(30514,"verschickt","2016-05-14",5300.00F,0.00F,0.00F,"CORP"));
						
			
			if (costreminder_warnings_billdata.size()== 0 ) lbl_dbconnect.setText("keine Rechnungen vorhanden");
			
			if (rs != null) rs.close();
		//	stmt.close();

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
	


		
		
	
	
	@FXML	public void angebotedit_click(ActionEvent event) throws SQLException, IOException {

		// System.out.println(Kdname.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex()));
		set_allunvisible(false); 
		auftragändernform.setVisible(true);
		ancpanebtn_changeorder.setVisible(true);
		scroll_pane_changeorder.setVisible(true);
		scroll_pane_changeorder.setVvalue(0);
		maskentitel.setVisible(true);
		maskentitel.setText("Auftragstatus ändern");
		choiceorderstatus.setVisible(true);
		hbox_show_status.setVisible(true);
		choicebillstatus.setVisible(false);
		choiceorderstatus.getItems().clear();
		choiceorderstatus.getItems().addAll("offen","positiv","negativ");
		String tmpAuftragstatus = Statusorder.getCellData(auftragtable.getSelectionModel().getSelectedIndex());
		choiceorderstatus.setPromptText(tmpAuftragstatus);
		
		
		final String hostname = "172.20.1.24"; 
        final String port = "3306"; 
        String dbname = "myflight";
		String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
		conn = DriverManager.getConnection(url, user, password);
		//if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
		
		stmt = conn.createStatement();
		dbname = "benutzerverwaltung";
		conn_benutzerverwaltung = DriverManager.getConnection(url, user, password);
		stmt_benutzerverwaltung = conn_benutzerverwaltung.createStatement();
		
		//angebote_id übernehmen
		int auftrag_id = Nummerorder.getCellData(auftragtable.getSelectionModel().getSelectedIndex());
		String Angebotsart = Aartorder.getCellData(auftragtable.getSelectionModel().getSelectedIndex());
		String sql = "select auftraege.Angebote_Angebote_ID from auftraege where auftraege.auftraege_id = '"+auftrag_id+"'";
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
	int angebot_id = rs.getInt(1);
	System.out.println(rs.getInt(1));

		// Kundenname
				sql = "select kunden.kundename from kunden inner join angebote on kunden.kunde_id=angebote.kunden_kunde_id and angebote.angebote_id='"
				+ angebot_id + "'";

		//		Statement stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				rs.next();
			kdname1.setText(rs.getString(1));
			System.out.println(rs.getString(1));

		// Kundenvorname
				sql = "select kunden.kundevorname from kunden inner join angebote on kunden.kunde_id=angebote.kunden_kunde_id and angebote.angebote_id='"
				+ angebot_id + "'";
				rs = stmt.executeQuery(sql);
				rs.next();
				kdvname1.setText(rs.getString(1));
				System.out.println(rs.getString(1));

		//Art des Charters
				sql = "select angebote.chartertyp_chartertyp from angebote where angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				artcharter1.setText(rs.getString(1));
				System.out.println(rs.getString(1));
		//Flugzeugtyp
				sql = "select Flugzeugtyp from flugzeugtypen inner join flugzeuge inner join angebote on flugzeugtypen.Flugzeugtypen_ID =flugzeuge.Flugzeugtypen_Flugzeugtypen_ID and flugzeuge.flugzeug_ID = angebote.flugzeuge_Flugzeug_ID where angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				flgztyp1.setText(rs.getString(1));
				System.out.println(rs.getString(1));
		//Flugzeugkennzeichen
				sql = "select angebote.flugzeuge_flugzeug_id from angebote where angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				flgzkz1.setText(rs.getString(1));
				int flgzid = rs.getInt(1);
				System.out.println(rs.getString(1));
				
				
			
				
				//Charterdauer
				sql = "select angebote.charterdauer from angebote where angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getFloat(1));		
				
				
				double chartzeit = Math.round(rs.getFloat(1) * 100)/ 100.0; 
				charterdauer.setText(Double.toString(chartzeit));
		//Flugzeit
				sql = "select angebote.flugzeit from angebote where angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getFloat(1));		
				
				
				int h = (int)rs.getFloat(1);
				int min = (int)(Math.round((rs.getFloat(1)-(int)rs.getFloat(1))*60));
				String Flugstunden = Integer.toString(h)+" h "+Integer.toString(min)+" min";
				
				flugzeit.setText(Flugstunden);

			//    	stmt = conn_benutzerverwaltung.createStatement();
			    
		//Flugzeugbild
				sql = "select benutzerverwaltung.flugzeug_bilder.flugzeuge_Flugzeug_ID from benutzerverwaltung.flugzeug_bilder where benutzerverwaltung.flugzeug_bilder.flugzeuge_flugzeug_id = '"+flgzid+"'";
				rs = stmt_benutzerverwaltung.executeQuery(sql);
				rs.next();
				String filenamepic = System.getProperty("user.dir") + "/picture"+rs.getString(1)+".jpg";
		//		File image = new File (filenamepic);
			      BufferedImage imgbuf = ImageIO.read(new File(filenamepic));
			      javafx.scene.image.Image picture = SwingFXUtils.toFXImage(imgbuf, null);
			      flgzpicture1.setImage(picture);
				
			
				
			//    	stmt = conn.createStatement();
		//Datum von
				sql = "select angebote.datum_von from angebote where angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				// Create an instance of SimpleDateFormat used for formatting 
				// the string representation of date (month/day/year)
				DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

					      
				// Using DateFormat format method we can create a string 
				// representation of a date with the defined format.
				String reportDate = df.format(rs.getObject(1));
				System.out.println(reportDate);
				datumvon1.setPromptText(reportDate);
				//datumvon.setPromptText(rs.getString(1));
				//System.out.println(rs.getString(1));
		//Datum bis
				sql = "select angebote.datum_bis from angebote where angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				// Create an instance of SimpleDateFormat used for formatting 
				// the string representation of date (month/day/year)
				//DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					      
				// Using DateFormat format method we can create a string 
				// representation of a date with the defined format.
				reportDate = df.format(rs.getObject(1));
				System.out.println(reportDate);
				datumbis1.setPromptText(reportDate);

				// Flüge zum Angebot ermitteln
				// Anzahl Passagiere ermitteln
				sql = "select angebote.pax from angebote where angebote.angebote_id='" + angebot_id + "'";
				rs = stmt.executeQuery(sql);
				rs.next();
				int pax = rs.getInt(1);

				if (!Angebotsart.equals("Zeitcharter")) {	
				// Fluege ermitteln
				ResultSet rs1;
				String zielflughafen = "";
				String zwischenflughafen = "";
				zwischenstop1.clear();
				zwischenstop2.clear();
				zwischenstop3.clear();
				int zaehler = 0;
				Statement stmt1 = conn.createStatement();
				sql = "select fluege.*, flughafen_von.FlughafenStadt from fluege left outer join flughafen_von on fluege.flughafen_von_FlughafenKuerzel = flughafen_von.FlughafenKuerzel  where fluege.angebote_Angebote_ID ='"
						+ angebot_id + "'";
				rs = stmt.executeQuery(sql);
				tablefluegedata.remove(0, tablefluegedata.size());
				int i = 1;
				// Testbeginn
				// rs = null;
				// Testende

				while ((rs != null) && (rs.next())) {

					// Flughafenstadt vom Zielflughafen ermitteln
					sql = "select flughafen_bis.FlughafenStadt from flughafen_bis where flughafen_bis.FlughafenKuerzel ='"
							+ rs.getString(6) + "'";
					rs1 = stmt1.executeQuery(sql);
					rs1.first();
					// System.out.println(rs.getString(6));
					// if (rs1.first()) System.out.println("here it
					// is:"+rs1.getString(1));
					if (rs1.first()) {
						zielflughafen = rs1.getString(1);
						System.out.println(rs.getString(6) + " " + zielflughafen);
					} else {
						zielflughafen = "";

					}
					System.out.println(i++ + " " + rs.getString(1) + " " + rs.getString(3) + " " + rs.getString(9) + " "
							+ rs.getFloat(7) + " " + rs.getString(4) + " " + rs.getString(6));
					
					h = (int)rs.getFloat(7);
					min = (int)(Math.round((rs.getFloat(7)-(int)rs.getFloat(7))*60));
					Flugstunden = Integer.toString(h)+" h "+Integer.toString(min)+" min";
					
					
					tablefluegedata.add(new Fluege(rs.getString(1), rs.getString(3), rs.getString(9), Flugstunden,
							rs.getString(4), zielflughafen, pax));
					ankunftort1.setText(zielflughafen);
					zaehler++;
					switch (zaehler) {
					case 1:
						zwischenflughafen = zielflughafen;
						break;
					case 2:
						zwischenstop1.setText(zwischenflughafen);
						zwischenflughafen = zielflughafen;
						break;
					case 3:
						zwischenstop2.setText(zwischenflughafen);
						zwischenflughafen = zielflughafen;
						break;
					case 4:
						zwischenstop3.setText(zwischenflughafen);
						zwischenflughafen = zielflughafen;
						break;

					}

				}
				
				
				
				
		//Abflugort
				sql = "select flughafen_von.flughafenstadt from flughafen_von inner join fluege on flughafen_von.FlughafenKuerzel=fluege.flughafen_von_FlughafenKuerzel inner join angebote on fluege.angebote_Angebote_ID = angebote.angebote_id and angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				abflugort1.setText(rs.getString(1));
				System.out.println(rs.getString(1));				
/*		//Ankunftort
				sql = "select flughafen_bis.flughafenname from flughafen_bis inner join fluege on flughafen_bis.FlughafenKuerzel=fluege.flughafen_bis_FlughafenKuerzel inner join angebote on fluege.angebote_Angebote_ID = angebote.angebote_id and angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				ankunftort1.setText(rs.getString(1));
				System.out.println(rs.getString(1));				
	*/				
				} //endif		
		//Preis netto
				sql = "select angebote.angebotspreis_netto from angebote where angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getInt(1));		
				preisnetto1.setText(Integer.toString(rs.getInt(1)));
				int Preisnetto = rs.getInt(1);
		//Preis brutto
				sql = "select angebote.angebotspreis_brutto from angebote where angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getInt(1));		
				preisbrutto1.setText(Integer.toString(rs.getInt(1)));
				int Preisbrutto = rs.getInt(1);					
		//Preis Mwst
				int Preismwst = Preisbrutto - Preisnetto;
				System.out.println(Preismwst);		
				preismwst1.setText(Integer.toString(Preismwst));
					

		
	
	
	}

	@FXML	public void action_costtrackingedit(ActionEvent event) throws SQLException {

		 // System.out.println(Kdname.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex()));
		set_allunvisible(false); 
		costtrackingedit.setVisible(true);
		apa_btn_costtrackingedit.setVisible(true);
		maskentitel.setVisible(true);
		maskentitel.setText("Zahlungseingang bearbeiten");
		choicecostbillstatus.getItems().clear();
		choicecostbillstatus.getItems().addAll("bezahlt");
		choicecostbillstatus.getItems().addAll("nicht bezahlt");
		
		//Werte aus ausgewählter Tabellenzeile übernehmen
		int rechnung_id = Nummercostbill.getCellData(costbilltable.getSelectionModel().getSelectedIndex());
//		String tmpAuftragstatuscosttrackingedit = Statuscostbill.getCellData(costbilltable.getSelectionModel().getSelectedIndex());
		String tmprechnungstatuscosttrackingedit = Statuscostbill.getCellData(costbilltable.getSelectionModel().getSelectedIndex());
		String tmpKdnamecosttrackingedit = Kdnamecostbill.getCellData(costbilltable.getSelectionModel().getSelectedIndex());
		float tmppreisbruttocosttrackingedit = Preiscostbill.getCellData(costbilltable.getSelectionModel().getSelectedIndex());
//		float tmppreisaufschlagcosttrackingedit = Preiscostbill_aufschlag.getCellData(costbilltable.getSelectionModel().getSelectedIndex());
//		float tmpzusatzkostencosttrackingedit = Preiscostbill_zusatzkosten.getCellData(costbilltable.getSelectionModel().getSelectedIndex());
		String tmpKdgruppecosttrackingedit = Kdgruppecostbill.getCellData(costbilltable.getSelectionModel().getSelectedIndex());
		

		//Felder für Maske Rechnungsstatus ändern - mit Daten versorgen Beginn
	
		rechnung_idcosttrackingedit.setText(Integer.toString(rechnung_id));
		rechnungstatuscosttrackingedit.setText(tmprechnungstatuscosttrackingedit);
		Kdnamecosttrackingedit.setText(tmpKdnamecosttrackingedit);
		preisbruttocosttrackingedit.setText(Float.toString(tmppreisbruttocosttrackingedit));
//		preisaufschlagcosttrackingedit.setText(Float.toString(tmppreisaufschlagcosttrackingedit));
//		zusatzkostencosttrackingedit.setText(Float.toString(tmpzusatzkostencosttrackingedit));
		Kdgruppecosttrackingedit.setText(tmpKdgruppecosttrackingedit);
		
		
	   
	   
	  //Felder für Maske Rechnungsstatus ändern - mit Daten versorgen Ende
		
		// Auftragsnummer
				String sql = "select auftraege.auftraege_id from auftraege inner join rechnungen on auftraege.auftraege_id = rechnungen.auftraege_auftraege_id where rechnungen.rechnungen_ID = '"+rechnung_id+"'";

				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				rs.next();
				auftrag_idcosttrackingedit.setText(rs.getString(1));
			System.out.println(rs.getString(1));

		// Auftragsstatus
				sql = "select auftraege.Auftragsstatus_Auftragsstatus from auftraege inner join rechnungen on auftraege.auftraege_id = rechnungen.auftraege_auftraege_id where rechnungen.rechnungen_ID = '"+rechnung_id+"'"; 
				rs = stmt.executeQuery(sql);
				rs.next();
				Auftragstatuscosttrackingedit.setText(rs.getString(1));
				System.out.println(rs.getString(1));

		//Kunden_ID
				sql = "select angebote.kunden_kunde_id from angebote inner join rechnungen inner join auftraege on angebote.angebote_id = auftraege.angebote_angebote_id and auftraege.auftraege_id = rechnungen.auftraege_auftraege_id where rechnungen.rechnungen_ID = '"+rechnung_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				Kunden_idcosttrackingedit.setText(rs.getString(1));
				System.out.println(rs.getString(1));
					

		
	
	
	
	}

	@FXML	public void action_costextracostedit(ActionEvent event) throws SQLException {

		 // System.out.println(Kdname.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex()));
		set_allunvisible(false); 
		costextracostedit.setVisible(true);
		apa_btn_costextracostedit.setVisible(true);
		maskentitel.setVisible(true);
		maskentitel.setText("Zusatzkosten bearbeiten");
	
		//Werte aus ausgewählter Tabellenzeile übernehmen
		int rechnung_id = Nummercostreminder_warnings_bill.getCellData(costreminder_warnings_billtable.getSelectionModel().getSelectedIndex());
		String tmpAuftragstatusextracostedit = Statuscostreminder_warnings_bill.getCellData(costreminder_warnings_billtable.getSelectionModel().getSelectedIndex());
		String tmprechnungstatusextracostedit = Statuscostreminder_warnings_bill.getCellData(costreminder_warnings_billtable.getSelectionModel().getSelectedIndex());
		String tmpKdnameextracostedit = Kdnamecostreminder_warnings_bill.getCellData(costreminder_warnings_billtable.getSelectionModel().getSelectedIndex());
		float tmppreisbruttoextracostedit = Preiscostreminder_warnings_bill.getCellData(costreminder_warnings_billtable.getSelectionModel().getSelectedIndex());
		float tmppreisaufschlagextracostedit = Preiscostreminder_warnings_bill_aufschlag.getCellData(costreminder_warnings_billtable.getSelectionModel().getSelectedIndex());
		tmpzusatzkostenextracostedit = Preiscostreminder_warnings_bill_zusatzkosten.getCellData(costreminder_warnings_billtable.getSelectionModel().getSelectedIndex());
		String tmpKdgruppeextracostedit = Kdgruppecostreminder_warnings_bill.getCellData(costreminder_warnings_billtable.getSelectionModel().getSelectedIndex());
		

		//Felder für Maske Rechnung Zusatzkosten bearbeiten - mit Daten versorgen Beginn
	
		rechnung_idextracostedit.setText(Integer.toString(rechnung_id));
		rechnungstatusextracostedit.setText(tmprechnungstatusextracostedit);
		Kdnameextracostedit.setText(tmpKdnameextracostedit);
		preisbruttoextracostedit.setText(Float.toString(tmppreisbruttoextracostedit));
		preisaufschlagextracostedit.setText(Float.toString(tmppreisaufschlagextracostedit));
		zusatzkostenextracostedit.setText(Float.toString(tmpzusatzkostenextracostedit));
		Kdgruppeextracostedit.setText(tmpKdgruppeextracostedit);
		
		
	   
	   
		//Felder für Maske Rechnung Zusatzkosten bearbeiten - mit Daten versorgen Ende
		
		// Auftragsnummer
				String sql = "select auftraege.auftraege_id from auftraege inner join rechnungen on auftraege.auftraege_id = rechnungen.auftraege_auftraege_id where rechnungen.rechnungen_ID = '"+rechnung_id+"'";

				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				rs.next();
				auftrag_idextracostedit.setText(rs.getString(1));
			System.out.println(rs.getString(1));

		// Auftragsstatus
				sql = "select auftraege.Auftragsstatus_Auftragsstatus from auftraege inner join rechnungen on auftraege.auftraege_id = rechnungen.auftraege_auftraege_id where rechnungen.rechnungen_ID = '"+rechnung_id+"'"; 
				rs = stmt.executeQuery(sql);
				rs.next();
				Auftragstatusextracostedit.setText(rs.getString(1));
				System.out.println(rs.getString(1));

		//Kunden_ID
				sql = "select angebote.kunden_kunde_id from angebote inner join rechnungen inner join auftraege on angebote.angebote_id = auftraege.angebote_angebote_id and auftraege.auftraege_id = rechnungen.auftraege_auftraege_id where rechnungen.rechnungen_ID = '"+rechnung_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				Kunden_idextracostedit.setText(rs.getString(1));
				System.out.println(rs.getString(1));
					

		
	
	
	}
	
	@FXML
	public void action_createreminder(ActionEvent event) throws Exception {
		boolean change = false;
		int rechnung_id = Nummercostreminder_warnings_bill
				.getCellData(costreminder_warnings_billtable.getSelectionModel().getSelectedIndex());
		String tmprechnungstatusextracostedit = Statuscostreminder_warnings_bill
				.getCellData(costreminder_warnings_billtable.getSelectionModel().getSelectedIndex());

		// angebote_id ermitteln
		String sql = "select auftraege.auftraege_id from auftraege inner join rechnungen on auftraege.auftraege_id = rechnungen.auftraege_auftraege_id where rechnungen.rechnungen_ID = '"+rechnung_id+"'";

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		int angebot_id = rs.getInt(1);
		System.out.println(rs.getInt(1));

		switch (tmprechnungstatusextracostedit) {
		case "verschickt":
			tmprechnungstatusextracostedit = "Erinnerung 1";
			change = true;
			break;
		case "Erinnerung 1":
			tmprechnungstatusextracostedit = "Erinnerung 2";
			change = true;
			break;
		case "Erinnerung 2":
			tmprechnungstatusextracostedit = "Mahnung 1";
			change = true;
			break;
		case "Mahnung 1":
			tmprechnungstatusextracostedit = "Mahnung 2";
			change = true;
			break;
		case "nicht bezahlt":
			tmprechnungstatusextracostedit = "Mahnung 2";
			change = true;
			break;
		default:
		}
		if (change) {
			try {
				stmt.executeUpdate("Update rechnungen set rechnungsstatus_rechnungsstatus='"
						+ tmprechnungstatusextracostedit + "' where rechnungen.rechnungen_id='" + rechnung_id + "'");
				lbl_dbconnect.setText("nächste Erinnerungs-/Mahnstufe gespeichert");
			} catch (SQLException sqle) {

				lbl_dbconnect.setText("Datenbankverbindung fehlgeschlagen");
				// System.out.println("geht nicht");
				sqle.printStackTrace();
			}
		}
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
		// if (result.isPresent()){
		// System.out.println("Your choice: " + result.get());
		// }

		// The Java 8 way to get the response value (with lambda
		// expression).
		result1.ifPresent(letter -> System.out.println("Your choice: " + letter));

		if (result1.isPresent()) {
			AuswahlDokutyp = result1.get();

			if (AuswahlDokutyp == "PDF") {
				erzeugePdf(angebot_id, tmprechnungstatusextracostedit);
			}

			if (AuswahlDokutyp == "Word") {
				erzeugeWord(angebot_id, tmprechnungstatusextracostedit);

			}
		}
		
		choices.clear();
		choices.add("Drucken");
		choices.add("keine Aktion");

		ChoiceDialog<String> dialog2 = new ChoiceDialog<>("keine Aktion", choices);
		dialog2.setTitle("weitere Aktionen");
		dialog2.setHeaderText("Bitte wählen Sie \neine weitere Aktion aus:");
		dialog2.setContentText("Auswahl:");

		// Traditional way to get the response value.
		Optional<String> result2 = dialog2.showAndWait();
		result2.ifPresent(letter -> System.out.println("Your choice: " + letter));
		actiongetcosttrackingreminder_warnings();

		if (result2.isPresent()) {
			AuswahlAktion = result2.get();
		

		if (AuswahlAktion == "Drucken" && AuswahlDokutyp == "PDF") {

			filename = System.getProperty("user.dir") + "/" + Integer.toString(angebot_id) + "m.pdf";
			f = new File(filename);

			
			try {
				PDFPrinter druck = new PDFPrinter(f);
				lbl_dbconnect.setText("PDF-Ausdruck gestartet");
			} catch (Exception e) {
				lbl_dbconnect.setText("Druckdatei nicht vorhanden");
				e.printStackTrace();
			}
			

		}
		if (AuswahlAktion == "Drucken" && AuswahlDokutyp == "Word") {

			strFilenamedoc = Integer.toString(angebot_id) + "m.docx";
			Druckjob druck = new Druckjob(strFilenamedoc);
			lbl_dbconnect.setText("Docx-Ausdruck gestartet");

		}
		}
	}
	
	@FXML public void actiongetcosttrackingreminder (ActionEvent event) {
		actiongetcosttrackingreminder_warnings();
	}
	



	@FXML
	public void setbtnenable() {
	   	            //   btncreateorder.setDisable(false);
	            }	
	@FXML
	public void createorder(ActionEvent event) throws SQLException, IOException {
		System.out.println(Nummer.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex()));
		set_allunvisible(false);
		scroll_pane_changeorder.setVisible(true);
		scroll_pane_changeorder.setVvalue(0);
		
		auftragändernform.setVisible(true);
		ancpanebtn_createorder.setVisible(true);
		maskentitel.setVisible(true);
		choiceorderstatus.setVisible(false);
		choicebillstatus.setVisible(false);
		hbox_show_status.setVisible(false);
		maskentitel.setText("Auftrag erstellen");
		
		final String hostname = "172.20.1.24"; 
        final String port = "3306"; 
        String dbname = "myflight";
		String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
		Connection conn = DriverManager.getConnection(url, user, password);
	    if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
	    Statement stmt = conn.createStatement();
			
		
		//angebote_id übernehmen
		int angebot_id = Nummer.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex());
		// Angebotsart übernehmen
		String Angebotsart = Aart.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex());
		
		// Daten für Auftrag erstellen einlesen
		String sql = "";
		switch (Angebotsart) {
		case "Zeitcharter" :
				sql = "SELECT angebote.*, kunden.*,flugzeugtypen.flugzeugtyp  FROM angebote INNER JOIN kunden on  angebote.kunden_kunde_id= kunden.kunde_id inner join flugzeuge inner join flugzeugtypen on angebote.flugzeuge_Flugzeug_ID=flugzeuge.Flugzeug_ID and flugzeuge.Flugzeugtypen_Flugzeugtypen_ID=flugzeugtypen.Flugzeugtypen_ID where angebote.angebote_id = '"+ angebot_id + "'";
				break;
		default :
				sql = "SELECT angebote.*, fluege.datum_von, fluege.datum_bis, kunden.*, flugzeugtypen.flugzeugtyp FROM angebote INNER JOIN fluege on angebote.angebote_id=fluege.angebote_Angebote_ID inner join kunden inner join flugzeuge inner join flugzeugtypen on angebote.kunden_kunde_id= kunden.kunde_id and angebote.flugzeuge_Flugzeug_ID=flugzeuge.Flugzeug_ID and flugzeuge.Flugzeugtypen_Flugzeugtypen_ID=flugzeugtypen.Flugzeugtypen_ID where angebote.angebote_id = '"+ angebot_id + "'";
				break;
		}		
				ResultSet rs = stmt.executeQuery(sql);
				rs.next();
		switch (Angebotsart) {
				case "Zeitcharter" :		
					kdname1.setText(rs.getString(28));
					System.out.println(rs.getString(28));

					kdvname1.setText(rs.getString(29));
						System.out.println(rs.getString(29));
					flgztyp1.setText(rs.getString(40));
						System.out.println(rs.getString(40));
						break;
				default :
			kdname1.setText(rs.getString(30));
			System.out.println(rs.getString(30));

			kdvname1.setText(rs.getString(31));
				System.out.println(rs.getString(31));

			flgztyp1.setText(rs.getString(42));
			System.out.println(rs.getString(42));
				break;
			}
			artcharter1.setText(rs.getString(4));
				System.out.println(rs.getString(4));

			flgzkz1.setText(rs.getString(16));
			int flgzid = rs.getInt(16);
				System.out.println(rs.getString(16));
				
				//Charterdauer
				System.out.println(rs.getFloat(14));		
				
				
				double chartzeit = Math.round(rs.getFloat(14) * 100)/ 100.0; 
				charterdauer.setText(Double.toString(chartzeit));
				
				//Flugzeit
				System.out.println(rs.getFloat(15));		
				
				
				int h = (int)rs.getFloat(15);
				int min = (int)(Math.round((rs.getFloat(15)-(int)rs.getFloat(15))*60));
				String Flugstunden = Integer.toString(h)+" h "+Integer.toString(min)+" min";
				
				flugzeit.setText(Flugstunden);
				
				//Flugzeugbild
				sql = "select benutzerverwaltung.flugzeug_bilder.flugzeuge_Flugzeug_ID from benutzerverwaltung.flugzeug_bilder where benutzerverwaltung.flugzeug_bilder.flugzeuge_flugzeug_id = '"+flgzid+"'";
				rs = stmt_benutzerverwaltung.executeQuery(sql);
				rs.next();
				String filenamepic = System.getProperty("user.dir") + "/picture"+rs.getString(1)+".jpg";
		//		File image = new File (filenamepic);
			      BufferedImage imgbuf = ImageIO.read(new File(filenamepic));
			      javafx.scene.image.Image picture = SwingFXUtils.toFXImage(imgbuf, null);
			      flgzpicture1.setImage(picture);
			      
			    //Datum von
					sql = "select angebote.datum_von from angebote where angebote.angebote_id='"+angebot_id+"'";
					rs = stmt.executeQuery(sql);
					rs.next();
					// Create an instance of SimpleDateFormat used for formatting 
					// the string representation of date (month/day/year)
					DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

						      
					// Using DateFormat format method we can create a string 
					// representation of a date with the defined format.
					String reportDate = df.format(rs.getObject(1));
					System.out.println(reportDate);
					datumvon1.setPromptText(reportDate);
			    
			
				//Datum bis
				sql = "select angebote.datum_bis from angebote where angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				// Create an instance of SimpleDateFormat used for formatting 
				// the string representation of date (month/day/year)
				//DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					      
				// Using DateFormat format method we can create a string 
				// representation of a date with the defined format.
				reportDate = df.format(rs.getObject(1));
				System.out.println(reportDate);
				datumbis1.setPromptText(reportDate);
				
	

				
		// Flüge zum Angebot ermitteln
		// Anzahl Passagiere ermitteln
		sql = "select angebote.pax from angebote where angebote.angebote_id='" + angebot_id + "'";
		rs = stmt.executeQuery(sql);
		rs.next();
		int pax = rs.getInt(1);
		
		String zielflughafen = "";
		String zwischenflughafen = "";
		zwischenstop1.clear();
		zwischenstop2.clear();
		zwischenstop3.clear();
		ankunftort1.clear();
		int zaehler = 0;
		tablefluegedata.remove(0, tablefluegedata.size());
		
		if (!Angebotsart.equals("Zeitcharter")) {
		System.out.println("kein Zeitcharter");
			// Fluege ermitteln
		ResultSet rs1;
		
		
		Statement stmt1 = conn.createStatement();
		sql = "select fluege.*, flughafen_von.FlughafenStadt from fluege left outer join flughafen_von on fluege.flughafen_von_FlughafenKuerzel = flughafen_von.FlughafenKuerzel  where fluege.angebote_Angebote_ID ='"
				+ angebot_id + "'";
		rs = stmt.executeQuery(sql);
		tablefluegedata.remove(0, tablefluegedata.size());
		int i = 1;
		// Testbeginn
		// rs = null;
		// Testende

		while ((rs != null) && (rs.next())) {

			// Flughafenstadt vom Zielflughafen ermitteln
			sql = "select flughafen_bis.FlughafenStadt from flughafen_bis where flughafen_bis.FlughafenKuerzel ='"
					+ rs.getString(6) + "'";
			rs1 = stmt1.executeQuery(sql);
			rs1.first();
			// System.out.println(rs.getString(6));
			// if (rs1.first()) System.out.println("here it
			// is:"+rs1.getString(1));
			if (rs1.first()) {
				zielflughafen = rs1.getString(1);
				System.out.println(rs.getString(6) + " " + zielflughafen);
			} else {
				zielflughafen = "";

			}
			System.out.println(i++ + " " + rs.getString(1) + " " + rs.getString(3) + " " + rs.getString(9) + " "
					+ rs.getFloat(7) + " " + rs.getString(4) + " " + rs.getString(6));
			
			h = (int)rs.getFloat(7);
			min = (int)(Math.round((rs.getFloat(7)-(int)rs.getFloat(7))*60));
			Flugstunden = Integer.toString(h)+" h "+Integer.toString(min)+" min";
			System.out.println(rs.getFloat(7));
			System.out.println(Flugstunden);
			
			
			tablefluegedata.add(new Fluege(rs.getString(1), rs.getString(3), rs.getString(9), Flugstunden,
					rs.getString(4), zielflughafen, pax));
			ankunftort1.setText(zielflughafen);
			zaehler++;
			switch (zaehler) {
			case 1:
				zwischenflughafen = zielflughafen;
				break;
			case 2:
				zwischenstop1.setText(zwischenflughafen);
				zwischenflughafen = zielflughafen;
				break;
			case 3:
				zwischenstop2.setText(zwischenflughafen);
				zwischenflughafen = zielflughafen;
				break;
			case 4:
				zwischenstop3.setText(zwischenflughafen);
				zwischenflughafen = zielflughafen;
				break;

			}

		}
		} //endif	
		//Preis netto
				sql = "select angebote.angebotspreis_netto from angebote where angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getInt(1));		
				preisnetto1.setText(Integer.toString(rs.getInt(1)));
				int Preisnetto = rs.getInt(1);
		//Preis brutto
				sql = "select angebote.angebotspreis_brutto from angebote where angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getInt(1));		
				preisbrutto1.setText(Integer.toString(rs.getInt(1)));
				int Preisbrutto = rs.getInt(1);					
		//Preis Mwst
				int Preismwst = Preisbrutto - Preisnetto;
				System.out.println(Preismwst);		
				preismwst1.setText(Integer.toString(Preismwst));
				
		abflugort1.clear();
		
		if (!Angebotsart.equals("Zeitcharter")) {	
		//Abflugort
				
				sql = "select flughafen_von.flughafenstadt from flughafen_von inner join fluege on flughafen_von.FlughafenKuerzel=fluege.flughafen_von_FlughafenKuerzel inner join angebote on fluege.angebote_Angebote_ID = angebote.angebote_id and angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				abflugort1.setText(rs.getString(1));
				System.out.println(rs.getString(1));				
	/*	//Ankunftort
				sql = "select flughafen_bis.flughafenname from flughafen_bis inner join fluege on flughafen_bis.FlughafenKuerzel=fluege.flughafen_bis_FlughafenKuerzel inner join angebote on fluege.angebote_Angebote_ID = angebote.angebote_id and angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				ankunftort1.setText(rs.getString(1));
				System.out.println(rs.getString(1));				
		*/			

				
	}//endif
	}

	@FXML
	public void action_createorder(ActionEvent event) throws Exception {
		// gewähltes Angebot dessen Daten für Speicherung Auftrag übernehmen
		int angebot_id = Nummer.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex());
		// String tmpstatus =
		// Status.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex());
		String tmpAart = Aart.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex());
		String tmpkdgruppe = Kdgruppe.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex());

		final String hostname = "172.20.1.24"; 
        final String port = "3306"; 
        String dbname = "myflight";
		String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
		Connection conn = DriverManager.getConnection(url, user, password);
	    if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
	    Statement stmt = conn.createStatement();
		
		// Prüfung, ob Auftrag bereits angelegt ist
		String sql = "select auftraege.angebote_angebote_id from auftraege where auftraege.angebote_angebote_id='" + angebot_id
				+ "'";
		ResultSet rs = stmt.executeQuery(sql);
		if ((rs != null) && (rs.next()))
			lbl_dbconnect.setText("Auftrag bereits vorhanden");
		else {

			// ermittle nächste Auftrags-ID Speichern eines Auftrags

			sql = "select max(auftraege_id) from auftraege";
			rs = stmt.executeQuery(sql);
			rs.next();
			//int newauftraege_id = (rs.getInt(1) / 10000 + 1) * 10000 + 2016;
			int newauftraege_id = rs.getInt(1) +1;
			
			System.out.println(newauftraege_id);

			String tmpAuftragstatus = "offen";
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        Calendar c = df.getCalendar();
	        System.out.println (c);
	        c.setTimeInMillis(System.currentTimeMillis());
	        //String tagesdatum = c.get(Calendar.DAY_OF_MONTH) + "." + (c.get(Calendar.MONTH) + 1) + "." + c.get(Calendar.YEAR);
	        String tagesdatum = c.get(Calendar.YEAR)+ "-"+ (c.get(Calendar.MONTH) + 1) + "-"+c.get(Calendar.DAY_OF_MONTH);
	        
	        System.out.println (tagesdatum);
			// ermittle Kunden_ID
			sql = "select Kunden_kunde_id,angebotsstatus_angebotsstatus from angebote where angebote.angebote_id='"
					+ angebot_id + "'";
			rs = stmt.executeQuery(sql);
			rs.next();
			int tmpkunde_id = rs.getInt(1);
			String tmpstatus = rs.getString(2);

			System.out.println(newauftraege_id + " " + tmpAuftragstatus + " " + angebot_id + " " + c + " "
					+ tmpAart + " " + tmpkunde_id + " " + tmpkdgruppe);
			// speichere Auftragsdaten
			try {
				stmt.executeUpdate(
						"insert into auftraege (Auftraege_id, auftragsstatus_auftragsstatus, angebote_angebote_id, Auftragsdatum) values ('"+ newauftraege_id + "','" + tmpAuftragstatus + "','" + angebot_id + "','" + tagesdatum+ "')");
								lbl_dbconnect.setText("Auftrag gespeichert");
			} catch (SQLException sqle) {

				lbl_dbconnect.setText("Datenbankverbindung fehlgeschlagen");
				// System.out.println("geht nicht");
				sqle.printStackTrace();
			}
			// angebot_id von auftraege_id übernehmen
			angebot_id = newauftraege_id;
			
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
			// if (result.isPresent()){
			// System.out.println("Your choice: " + result.get());
			// }

			// The Java 8 way to get the response value (with lambda
			// expression).
			result1.ifPresent(letter -> System.out.println("Your choice: " + letter));

			if (result1.isPresent()) {
				AuswahlDokutyp = result1.get();
				if (AuswahlDokutyp == "PDF") {
				erzeugePdf(angebot_id,"Auftrag");
			}

			if (AuswahlDokutyp == "Word") {
				try {
					erzeugeWord(angebot_id, "Auftrag") ;
				}
				catch (Exception e) {
					lbl_dbconnect.setText("Es ist ein Fehler aufgetreten");
					e.printStackTrace();
				}

			}
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
			actiongetangebotepgm(false);

			if (result2.isPresent()) {
				AuswahlAktion = result2.get();
			

			if (AuswahlAktion == "Drucken" && AuswahlDokutyp == "PDF") {

				filename = System.getProperty("user.dir") + "/" + Integer.toString(angebot_id) + ".pdf";
				f = new File(filename);

				PDFPrinter druck = new PDFPrinter(f);
				lbl_dbconnect.setText("PDF-Ausdruck gestartet");

			}
			if (AuswahlAktion == "Drucken" && AuswahlDokutyp == "Word") {

				strFilenamedoc = Integer.toString(angebot_id) + ".docx";
				Druckjob druck = new Druckjob(strFilenamedoc);
				lbl_dbconnect.setText("Docx-Ausdruck gestartet");

			}

			if (AuswahlAktion == "Versenden") {
				// Kundenanrede
				sql = "select kunden.KundeAnrede from kunden inner join angebote inner join auftraege on kunden.kunde_id=angebote.kunden_kunde_id and auftraege.angebote_angebote_id=angebote.angebote_id and auftraege.auftraege_id='"
						+ angebot_id + "'";

				// Statement stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getString(1));
				String kundenanrede = rs.getString(1);

				// Kundenname
				sql = "select kunden.kundename from kunden inner join angebote inner join auftraege on kunden.kunde_id=angebote.kunden_kunde_id and auftraege.angebote_angebote_id=angebote.angebote_id and auftraege.auftraege_id = '"
						+ angebot_id + "'";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getString(1));
				String Kunde = rs.getString(1);
				// Datum von
				sql = "select angebote.angebotsdatum from angebote inner join auftraege on auftraege.angebote_angebote_id=angebote.angebote_id and auftraege.auftraege_id='"+ angebot_id + "'";
				rs = stmt.executeQuery(sql);
				rs.next();
				// Create an instance of SimpleDateFormat used for formatting
				// the string representation of date (month/day/year)
				df = new SimpleDateFormat("dd.MM.yyyy");

				// Using DateFormat format method we can create a string
				// representation of a date with the defined format.
				String reportDate = df.format(rs.getObject(1));
				System.out.println(reportDate);
				String Datum = reportDate;
				// Kundenmailadresse
				sql = "select kunden.kundeemail from kunden inner join angebote inner join auftraege on kunden.kunde_id=angebote.kunden_kunde_id and auftraege.angebote_angebote_id=angebote.angebote_id and auftraege.auftraege_id='"
						+ angebot_id + "'";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getString(1));
				String mailadresse = rs.getString(1);

				// String Kunde = "Burggraf";
				// int Nummer = 100302;
				// String Datum = "10.06.2016";
				Mainmail mail = new Mainmail(kundenanrede, Kunde, angebot_id, Datum, mailadresse, "Auftrag");

			}
			}
		}

	}
	
    
	
	
	@FXML
	public void change_billstatus(ActionEvent event) throws SQLException, IOException {
		set_allunvisible(false);
		scroll_pane_changeorder.setVisible(true);
		scroll_pane_changeorder.setVvalue(0);
		auftragändernform.setVisible(true);
		ancpanebtn_changebillstatus.setVisible(true);
		maskentitel.setVisible(true);
		choiceorderstatus.setVisible(false);
		choicebillstatus.setVisible(true);
		hbox_show_status.setVisible(true);
		maskentitel.setText("Rechnungsstatus ändern");
		

		//rechnung_id übernehmen, Rechnungsstatus anzeigen
		int rechnung_id = Nummerbill.getCellData(billtable.getSelectionModel().getSelectedIndex());
		String tmpbillstatus = Statusbill.getCellData(billtable.getSelectionModel().getSelectedIndex());
		choicebillstatus.getItems().clear();
		choicebillstatus.getItems().addAll("erstellt","verschickt");
		choicebillstatus.setPromptText(tmpbillstatus);
		
		final String hostname = "172.20.1.24"; 
        final String port = "3306"; 
        String dbname = "myflight";
		String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
		conn = DriverManager.getConnection(url, user, password);
		//if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
		
		dbname = "benutzerverwaltung";
		conn_benutzerverwaltung = DriverManager.getConnection(url, user, password);
		stmt_benutzerverwaltung = conn_benutzerverwaltung.createStatement();		
		
		// angebote_id ermitteln + Angebotsart
		String sql = "select angebote.angebote_id, angebote.Chartertyp_Chartertyp from angebote inner join auftraege inner join rechnungen on auftraege.angebote_angebote_id = angebote.angebote_id and auftraege.auftraege_id = rechnungen.auftraege_auftraege_id where rechnungen.rechnungen_ID = '"+rechnung_id+"'";

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		int angebot_id = rs.getInt(1);
		System.out.println(rs.getInt(1));
		String Angebotsart = rs.getString(2);
		System.out.println(rs.getString(2));
		
		
		// Kundenname
				sql = "select kunden.kundename from kunden inner join angebote inner join rechnungen inner join auftraege on rechnungen.auftraege_auftraege_id = auftraege.auftraege_id and auftraege.angebote_angebote_id = angebote.angebote_id and kunden.kunde_id=angebote.kunden_kunde_id and rechnungen.rechnungen_id='"
				+ rechnung_id + "'";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				rs.next();
			kdname1.setText(rs.getString(1));
			System.out.println(rs.getString(1));

		// Kundenvorname
				sql = "select kunden.kundevorname from kunden inner join angebote inner join rechnungen inner join auftraege on rechnungen.auftraege_auftraege_id = auftraege.auftraege_id and auftraege.angebote_angebote_id = angebote.angebote_id and kunden.kunde_id=angebote.kunden_kunde_id and rechnungen.rechnungen_id='"
				+ rechnung_id + "'";
				rs = stmt.executeQuery(sql);
				rs.next();
				kdvname1.setText(rs.getString(1));
				System.out.println(rs.getString(1));

		//Art des Charters
				sql = "select angebote.chartertyp_chartertyp from angebote inner join auftraege inner join rechnungen on rechnungen.auftraege_auftraege_id=auftraege.auftraege_id and auftraege.angebote_angebote_id=angebote.angebote_id where rechnungen.rechnungen_id='"+rechnung_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				artcharter1.setText(rs.getString(1));
				System.out.println(rs.getString(1));
		//Flugzeugtyp
				sql = "select flugzeugtypen.flugzeugtyp from flugzeugtypen inner join flugzeuge on flugzeugtypen.flugzeugtypen_id=flugzeuge.flugzeugtypen_flugzeugtypen_id inner join angebote on flugzeuge.flugzeug_id=angebote.flugzeuge_flugzeug_id inner join auftraege inner join rechnungen on rechnungen.auftraege_auftraege_id=auftraege.auftraege_id and auftraege.angebote_angebote_id=angebote.angebote_id where rechnungen.rechnungen_id='"+rechnung_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				flgztyp1.setText(rs.getString(1));
				System.out.println(rs.getString(1));

				
	
		//Charterdauer
				sql = "select angebote.charterdauer from angebote inner join auftraege inner join rechnungen on rechnungen.auftraege_auftraege_id=auftraege.auftraege_id and auftraege.angebote_angebote_id=angebote.angebote_id where rechnungen.rechnungen_id='"+rechnung_id+"'"; 
				rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getFloat(1));		
				
				
				double chartzeit = Math.round(rs.getFloat(1) * 100)/ 100.0; 
				charterdauer.setText(Double.toString(chartzeit));
				
		//Flugzeit
				sql = "select angebote.flugzeit from angebote inner join auftraege inner join rechnungen on rechnungen.auftraege_auftraege_id=auftraege.auftraege_id and auftraege.angebote_angebote_id=angebote.angebote_id where rechnungen.rechnungen_id='"+rechnung_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getFloat(1));		
				
				
				int h = (int)rs.getFloat(1);
				int min = (int)(Math.round((rs.getFloat(1)-(int)rs.getFloat(1))*60));
				String Flugstunden = Integer.toString(h)+" h "+Integer.toString(min)+" min";
				
				flugzeit.setText(Flugstunden);
				
		//Flugzeugkennzeichen
				sql = "select flugzeuge.flugzeug_id from flugzeuge inner join angebote on flugzeuge.flugzeug_id=angebote.flugzeuge_flugzeug_id inner join auftraege inner join rechnungen on rechnungen.auftraege_auftraege_id=auftraege.auftraege_id and auftraege.angebote_angebote_id=angebote.angebote_id where rechnungen.rechnungen_id='"+rechnung_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				flgzkz1.setText(rs.getString(1));
				int flgzid = rs.getInt(1);
				System.out.println(rs.getString(1));
				
				//Flugzeugbild
				sql = "select benutzerverwaltung.flugzeug_bilder.flugzeuge_Flugzeug_ID from benutzerverwaltung.flugzeug_bilder where benutzerverwaltung.flugzeug_bilder.flugzeuge_flugzeug_id = '"+flgzid+"'";
				rs = stmt_benutzerverwaltung.executeQuery(sql);
				rs.next();
				String filenamepic = System.getProperty("user.dir") + "/picture"+rs.getString(1)+".jpg";
				System.out.println(filenamepic);
		//		File image = new File (filenamepic);
			      BufferedImage imgbuf = ImageIO.read(new File(filenamepic));
			      javafx.scene.image.Image picture = SwingFXUtils.toFXImage(imgbuf, null);
			      flgzpicture1.setImage(picture);
				
		//Datum von
				sql = "select angebote.datum_von from angebote inner join auftraege inner join rechnungen on rechnungen.auftraege_auftraege_id=auftraege.auftraege_id and auftraege.angebote_angebote_id=angebote.angebote_id where rechnungen.rechnungen_id='"+rechnung_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				// Create an instance of SimpleDateFormat used for formatting 
				// the string representation of date (month/day/year)
				DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

					      
				// Using DateFormat format method we can create a string 
				// representation of a date with the defined format.
				String reportDate = df.format(rs.getObject(1));
				System.out.println(reportDate);
				datumvon1.setPromptText(reportDate);
				//datumvon.setPromptText(rs.getString(1));
				//System.out.println(rs.getString(1));
		//Datum bis
				sql = "select angebote.datum_bis from angebote inner join auftraege inner join rechnungen on rechnungen.auftraege_auftraege_id=auftraege.auftraege_id and auftraege.angebote_angebote_id=angebote.angebote_id where rechnungen.rechnungen_id='"+rechnung_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				// Create an instance of SimpleDateFormat used for formatting 
				// the string representation of date (month/day/year)
				//DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					      
				// Using DateFormat format method we can create a string 
				// representation of a date with the defined format.
				reportDate = df.format(rs.getObject(1));
				System.out.println(reportDate);
				datumbis1.setPromptText(reportDate);
				
				// Flüge zum Angebot ermitteln
				// Anzahl Passagiere ermitteln
				sql = "select angebote.pax from angebote where angebote.angebote_id='" + angebot_id + "'";
				rs = stmt.executeQuery(sql);
				rs.next();
				int pax = rs.getInt(1);

				String zielflughafen = "";
				String zwischenflughafen = "";
				zwischenstop1.clear();
				zwischenstop2.clear();
				zwischenstop3.clear();
				int zaehler = 0;
				tablefluegedata.remove(0, tablefluegedata.size());
				ankunftort1.clear();
				abflugort1.clear();
				
				if (!Angebotsart.equals("Zeitcharter")) {	
				// Fluege ermitteln
				ResultSet rs1;
				Statement stmt1 = conn.createStatement();
				sql = "select fluege.*, flughafen_von.FlughafenStadt from fluege left outer join flughafen_von on fluege.flughafen_von_FlughafenKuerzel = flughafen_von.FlughafenKuerzel  where fluege.angebote_Angebote_ID ='"
						+ angebot_id + "'";
				rs = stmt.executeQuery(sql);
				tablefluegedata.remove(0, tablefluegedata.size());
				int i = 1;
				// Testbeginn
				// rs = null;
				// Testende

				while ((rs != null) && (rs.next())) {

					// Flughafenstadt vom Zielflughafen ermitteln
					sql = "select flughafen_bis.FlughafenStadt from flughafen_bis where flughafen_bis.FlughafenKuerzel ='"
							+ rs.getString(6) + "'";
					rs1 = stmt1.executeQuery(sql);
					rs1.first();
					// System.out.println(rs.getString(6));
					// if (rs1.first()) System.out.println("here it
					// is:"+rs1.getString(1));
					if (rs1.first()) {
						zielflughafen = rs1.getString(1);
						System.out.println(rs.getString(6) + " " + zielflughafen);
					} else {
						zielflughafen = "";

					}
					System.out.println(i++ + " " + rs.getString(1) + " " + rs.getString(3) + " " + rs.getString(9) + " "
							+ rs.getFloat(7) + " " + rs.getString(4) + " " + rs.getString(6));
					
					
					h = (int)rs.getFloat(7);
					min = (int)(Math.round((rs.getFloat(7)-(int)rs.getFloat(7))*60));
					Flugstunden = Integer.toString(h)+" h "+Integer.toString(min)+" min";
					
					tablefluegedata.add(new Fluege(rs.getString(1), rs.getString(3), rs.getString(9), Flugstunden,
							rs.getString(4), zielflughafen, pax));
					ankunftort1.setText(zielflughafen);
					zaehler++;
					switch (zaehler) {
					case 1:
						zwischenflughafen = zielflughafen;
						break;
					case 2:
						zwischenstop1.setText(zwischenflughafen);
						zwischenflughafen = zielflughafen;
						break;
					case 3:
						zwischenstop2.setText(zwischenflughafen);
						zwischenflughafen = zielflughafen;
						break;
					case 4:
						zwischenstop3.setText(zwischenflughafen);
						zwischenflughafen = zielflughafen;
						break;

					}

				}				
				
		//Abflugort
				sql = "select flughafen_von.flughafenstadt from flughafen_von inner join fluege on flughafen_von.FlughafenKuerzel=fluege.flughafen_von_FlughafenKuerzel inner join angebote inner join rechnungen inner join auftraege on fluege.angebote_angebote_id=angebote.angebote_id and auftraege.angebote_angebote_id=angebote.angebote_id and rechnungen.auftraege_auftraege_id=auftraege.auftraege_id where rechnungen.rechnungen_id='"+rechnung_id+"'";    
				rs = stmt.executeQuery(sql);
				rs.next();
				abflugort1.setText(rs.getString(1));
				System.out.println(rs.getString(1));				
/*		//Ankunftort
				sql = "select flughafen_bis.flughafenname from flughafen_bis inner join fluege on flughafen_bis.FlughafenKuerzel=fluege.flughafen_bis_FlughafenKuerzel inner join angebote inner join rechnungen inner join auftraege on fluege.angebote_angebote_id=angebote.angebote_id and auftraege.angebote_angebote_id=angebote.angebote_id and rechnungen.auftraege_auftraege_id=auftraege.auftraege_id where rechnungen.rechnungen_id='"+rechnung_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				ankunftort1.setText(rs.getString(1));
				System.out.println(rs.getString(1));	
	*/			
				} //endif
		//Preis netto
				sql = "select angebote.angebotspreis_netto from angebote inner join rechnungen inner join auftraege on auftraege.angebote_angebote_id=angebote.angebote_id and rechnungen.auftraege_auftraege_id=auftraege.auftraege_id where rechnungen.rechnungen_id='"+rechnung_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getFloat(1));		
				preisnetto1.setText(Float.toString(rs.getFloat(1)));
				float Preisnetto = rs.getFloat(1);
		//Preis brutto
				sql = "select angebote.angebotspreis_brutto from angebote inner join rechnungen inner join auftraege on auftraege.angebote_angebote_id=angebote.angebote_id and rechnungen.auftraege_auftraege_id=auftraege.auftraege_id where rechnungen.rechnungen_id='"+rechnung_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getInt(1));		
				preisbrutto1.setText(Float.toString(rs.getFloat(1)));
				float Preisbrutto = rs.getFloat(1);					
		//Preis Mwst
				float Preismwst = Preisbrutto - Preisnetto;
				System.out.println(Preismwst);		
				preismwst1.setText(Float.toString(Preismwst));

	
	
	
	
	}
	
	public void erzeugePdf(int angebot_id, String modus) throws Exception {
	
		Document document = new Document(PageSize.A4);
		document.setMargins(50f, 40f, 50f, 40f);
		
		switch (modus) {
		case "Auftrag":
			filename = System.getProperty("user.dir") + "/" + Integer.toString(angebot_id) + ".pdf";
			break;
		case "Angebot":
			filename = System.getProperty("user.dir") + "/" + Integer.toString(angebot_id) + "angebot.pdf";
			break;
		default :
			filename = System.getProperty("user.dir") + "/" + Integer.toString(angebot_id) + "m.pdf";
			break;
		}
		PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(filename));
		// nur für die Möglichkeit, dass wir einen Rahmen zeichnen können
		ParagraphBorder border = enableBordering(pdfWriter);
		
		document.open();
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
		styleTextunderline.setSize(12);
		styleTextunderline.setStyle(FontStyle.UNDERLINE.name());

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

		Image image = Image.getInstance(PdfGenerator.class.getResource("Logo2.jpg"));
		image.scaleAbsolute(507,40);
		document.add(image);
		// Dokumententitel (mit Rahmen!)
		border.setActive(true);
		Paragraph p = new Paragraph("", styleTitel);
		p.setAlignment(Element.ALIGN_LEFT);
		document.add(p);
		border.setActive(false);
		// Parameter für Dokumenterstellung
		String sql;
		switch (modus) {
		case "Angebot" :
			// Daten für Auftrag erstellen einlesen
			sql = "SELECT angebote.*, kunden.*, flugzeugtypen.flugzeugtyp FROM angebote INNER JOIN kunden inner join flugzeuge inner join flugzeugtypen on angebote.kunden_kunde_id= kunden.kunde_id and angebote.flugzeuge_Flugzeug_ID=flugzeuge.Flugzeug_ID and flugzeuge.Flugzeugtypen_Flugzeugtypen_ID=flugzeugtypen.Flugzeugtypen_ID where angebote.angebote_id = '"+ angebot_id + "'";
			break;
		default:
			// Daten für Auftrag erstellen einlesen
			sql = "SELECT angebote.*, kunden.*, flugzeugtypen.flugzeugtyp, auftraege.auftraege_id FROM angebote INNER JOIN auftraege inner join kunden inner join flugzeuge inner join flugzeugtypen on angebote.kunden_kunde_id= kunden.kunde_id and angebote.flugzeuge_Flugzeug_ID=flugzeuge.Flugzeug_ID and flugzeuge.Flugzeugtypen_Flugzeugtypen_ID=flugzeugtypen.Flugzeugtypen_ID and angebote.angebote_id=auftraege.Angebote_Angebote_ID where auftraege.auftraege_id = '"+ angebot_id + "'";
			break;
		}
			final String hostname = "172.20.1.24"; 
	        final String port = "3306"; 
	        String dbname = "myflight";
			String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
			Connection conn = DriverManager.getConnection(url, user, password);
		    if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
		    Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
		String AG = rs.getString(27);
		AG = AG+" "+rs.getString(28);
		String Typ=rs.getString(40);
		String Kennzeichen =rs.getString(16);
		String vorname = rs.getString(29);
		String nachname = rs.getString(28);
		String strasse = rs.getString(34);
		String plz = rs.getString(36);
		String ort = rs.getString(37);
		
		
		 //artcharter.setText(rs.getString(4));
			
			// Create an instance of SimpleDateFormat used for formatting 
			// the string representation of date (month/day/year)
			DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
			
			// Using DateFormat format method we can create a string 
			// representation of a date with the defined format.
			String Beginndatum = df.format(rs.getObject(21));
			
			// Create an instance of SimpleDateFormat used for formatting 
			// the string representation of date (month/day/year)
			//DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				      
			// Using DateFormat format method we can create a string 
			// representation of a date with the defined format.
			
			String Endedatum = df.format(rs.getObject(22));

			
			//Charterdauer
			System.out.println(rs.getFloat(14));		
			
			
			double chartzeit = Math.round(rs.getFloat(14) * 100)/ 100.0; 
			String Charterdauer = Double.toString(chartzeit);
			
			//Flugzeit
			System.out.println(rs.getFloat(15));		
			
			
			int h = (int)rs.getFloat(15);
			int min = (int)(Math.round((rs.getFloat(15)-(int)rs.getFloat(15))*60));
			Flugzeit = Integer.toString(h)+" h "+Integer.toString(min)+" min";
			
			
			
			
			//Preis netto
					System.out.println(rs.getInt(8));		
					String Preisnetto = Integer.toString(rs.getInt(8))+" EUR";
					int intpreisnetto = rs.getInt(8);
					// Preis Brutto
					System.out.println(rs.getInt(7));		
					String Preisbrutto = Integer.toString(rs.getInt(7))+ " EUR";
					int intpreisbrutto = rs.getInt(7);					
			//Preis Mwst
					int Preismwst = intpreisbrutto - intpreisnetto;
					String Mwst = Integer.toString(Preismwst)+" EUR";
					int angebote_angebote_id;
					String Angebotsart = "";
					switch (modus) {
					case "Angebot" :
						// Angebot_id ermitteln & Angebotsart
						angebote_angebote_id = angebot_id;
						sql = "select angebote.angebote_id, angebote.Chartertyp_Chartertyp from angebote where angebote.angebote_id ='"+angebot_id+"'";
						stmt = conn.createStatement();
						rs = stmt.executeQuery(sql);
						rs.next();
						Angebotsart = rs.getString(2);
						System.out.println(rs.getString(2));
						break;
					default:
						// Angebot_id ermitteln & Angebotsart
						sql = "select angebote.angebote_id, angebote.Chartertyp_Chartertyp from angebote inner join auftraege on auftraege.angebote_angebote_id = angebote.angebote_id and auftraege.auftraege_id='"+angebot_id+"'";
						stmt = conn.createStatement();
						rs = stmt.executeQuery(sql);
						rs.next();
						angebote_angebote_id = rs.getInt(1);
						Angebotsart = rs.getString(2);
						System.out.println(rs.getString(2));
						break;
					}
			String FlugAnfang = "";
			String FlugEnde = "";
			if (!Angebotsart.equals("Zeitcharter")) {	
			//Abflugort
			sql = "select flughafen_von.flughafenstadt from flughafen_von inner join fluege on flughafen_von.FlughafenKuerzel=fluege.flughafen_von_FlughafenKuerzel inner join angebote on fluege.angebote_angebote_id = angebote.angebote_id and angebote.angebote_id='"+angebote_angebote_id+"'";
			rs = stmt.executeQuery(sql);
			rs.next();
			FlugAnfang = rs.getString(1);
			System.out.println(rs.getString(1));
							
			//Ankunftort
			sql = "select flughafen_bis.flughafenstadt from flughafen_bis inner join fluege on flughafen_bis.FlughafenKuerzel=fluege.flughafen_bis_FlughafenKuerzel inner join angebote on fluege.angebote_angebote_id = angebote.angebote_id and angebote.angebote_id='"+angebote_angebote_id+"'";
			rs = stmt.executeQuery(sql);
			rs.next();
			FlugEnde = rs.getString(1);
							
			} //endif		
			
				
			
								
							//String AG = "Erich";
							//String Typ = "Dornier";
							//String Kennzeichen = "120";
							//String Beginndatum = "20.05.2016";
							//String Endedatum = "01.06.2016";
							//String FlugAnfang = "München";
							//String FlugEnde = "New York";
							String Zwischen1 = "";
							String Zwischen2 = "";
							String Zwischen3 = "";
						//	String Charterdauer = "124:30 h";
						//	String Flugzeit = "24:45";
							//String Preisnetto = "1.450,00 EUR";
							//String Mwst = "275,50 EUR";
							//String Preisbrutto = "1.725,50 EUR";
							
							Chunk underline = new Chunk("                                                  ");
			
			switch (modus) {

			case "Angebot":
				p = new Paragraph(" ", styleText);
				p.setAlignment(Element.ALIGN_CENTER);
				// etwas abstand hinter der überschrift
				p.setSpacingAfter(6f);
				document.add(p);p = new Paragraph("Angebot", styleUeberschrift1);
				p.setAlignment(Element.ALIGN_CENTER);
				// etwas abstand hinter der überschrift
				p.setSpacingAfter(6f);
				document.add(p);
				p = new Paragraph("für", styleText);
				p.setAlignment(Element.ALIGN_CENTER);
				// etwas abstand hinter der überschrift
				p.setSpacingAfter(6f);
				document.add(p);
				p = new Paragraph(vorname+" "+nachname, styleUeberschrift1);
				p.setAlignment(Element.ALIGN_CENTER);
				// etwas abstand hinter der überschrift
				p.setSpacingAfter(6f);
				document.add(p);
				p = new Paragraph("von", styleText);
				p.setAlignment(Element.ALIGN_CENTER);
				// etwas abstand hinter der überschrift
				p.setSpacingAfter(6f);
				document.add(p);
				p = new Paragraph("HINOTORI Executive AG", styleUeberschrift1);
				p.setAlignment(Element.ALIGN_CENTER);
				// etwas abstand hinter der überschrift
				p.setSpacingAfter(20f);
				document.add(p);
				break;
			case "Auftrag":			
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
				p = new Paragraph(vorname+" "+nachname, styleUeberschrift1);
				p.setAlignment(Element.ALIGN_CENTER);
				// etwas abstand hinter der überschrift
				p.setSpacingAfter(20f);
				document.add(p);
				break;
			}
			
		

			
			
										
			if (modus=="Auftrag" || modus=="Angebot") {
			

				String[][] DATEN = new String[10][7];
				int zaehler = 0;
		
				if (!Angebotsart.equals("Zeitcharter")) {	
				// Flüge zum Angebot ermitteln
				
				// Fluege ermitteln
				ResultSet rs1;
				String zielflughafen = "";
				String zwischenflughafen = "";
				
				
				Statement stmt1 = conn.createStatement();
				sql = "select fluege.*, flughafen_von.FlughafenStadt from fluege left outer join flughafen_von on fluege.flughafen_von_FlughafenKuerzel = flughafen_von.FlughafenKuerzel where fluege.angebote_Angebote_ID ='"
						+angebote_angebote_id+ "'";
				rs = stmt.executeQuery(sql);
				
				int i = 1;
				// Testbeginn
				// rs = null;
				// Testende
				int x = 0; int y = 0;
				while ((rs != null) && (rs.next())) {

					// Flughafenstadt vom Zielflughafen ermitteln
					sql = "select flughafen_bis.FlughafenStadt from flughafen_bis where flughafen_bis.FlughafenKuerzel ='"
							+ rs.getString(6) + "'";
					rs1 = stmt1.executeQuery(sql);
					rs1.first();
					// System.out.println(rs.getString(6));
					// if (rs1.first()) System.out.println("here it
					// is:"+rs1.getString(1));
					if (rs1.first()) {
						zielflughafen = rs1.getString(1);
						System.out.println(rs.getString(6) + " " + zielflughafen);
					} else {
						zielflughafen = "";

					}
					System.out.println(i++ + " " + rs.getString(1) + " " + rs.getString(3) + " " + rs.getString(9) + " "
							+ rs.getFloat(7) + " " + rs.getString(4) + " " + rs.getString(6));
					
					
					h = (int)rs.getFloat(7);
					min = (int)(Math.round((rs.getFloat(7)-(int)rs.getFloat(7))*60));
					String tmpFlugzeit = Integer.toString(h)+" h "+Integer.toString(min)+" min";
					
					
					DATEN[x][y] = rs.getString(1); y++;
					DATEN[x][y] = rs.getString(3); y++;
					DATEN[x][y] = rs.getString(9); y++;
					DATEN[x][y] = tmpFlugzeit; y++;
					DATEN[x][y] = rs.getString(4); y++;
					DATEN[x][y] = zielflughafen; y++;
					DATEN[x][y] = Integer.toString(pax); x++;y=0;
					
					
					FlugEnde = zielflughafen;
					zaehler++;
					switch (zaehler) {
					case 1:
						zwischenflughafen = zielflughafen;
						break;
					case 2:
						Zwischen1 = zwischenflughafen;
						zwischenflughafen = zielflughafen;
						break;
					case 3:
						Zwischen2 = zwischenflughafen;
						zwischenflughafen = zielflughafen;
						break;
					case 4:
						Zwischen3 = zwischenflughafen;
						zwischenflughafen = zielflughafen;
						break;

					}

				}	
				} //endif
				
				
				switch (zaehler) {
				
				case 0: 
					p = new Paragraph(
							AG+" chartert das Luftfahrzeug "+Typ+" "+Kennzeichen+" für die Zeit vom "+Beginndatum+" zum "+Endedatum+".",styleText);
					p.setAlignment(Element.ALIGN_JUSTIFIED);
					// zeilenabstand kleiner wählen
					p.setLeading(15f);
					p.setSpacingAfter(20f);
					document.add(p);
					break;
				case 1: 
					p = new Paragraph(
							AG+" chartert das Luftfahrzeug "+Typ+" "+Kennzeichen+" für die Zeit vom "+Beginndatum+" zum "+Endedatum+" zu einer Reise von "+FlugAnfang+" nach "+FlugEnde+".",styleText);
					p.setAlignment(Element.ALIGN_JUSTIFIED);
					// zeilenabstand kleiner wählen
					p.setLeading(15f);
					p.setSpacingAfter(20f);
					document.add(p);
					break;
				case 2:
					p = new Paragraph(
							AG+" chartert das Luftfahrzeug "+Typ+" "+Kennzeichen+" für die Zeit vom "+Beginndatum+" zum "+Endedatum+" zu einer Reise von "+FlugAnfang+" nach "+FlugEnde+" über "+Zwischen1+".",styleText);
					p.setAlignment(Element.ALIGN_JUSTIFIED);
					// zeilenabstand kleiner wählen
					p.setLeading(15f);
					p.setSpacingAfter(20f);
					document.add(p);
					break;
				case 3:
					p = new Paragraph(
							AG+" chartert das Luftfahrzeug "+Typ+" "+Kennzeichen+" für die Zeit vom "+Beginndatum+" zum "+Endedatum+" zu einer Reise von "+FlugAnfang+" nach "+FlugEnde+" über "+Zwischen1+", "+Zwischen2+", "+".",styleText);
					p.setAlignment(Element.ALIGN_JUSTIFIED);
					// zeilenabstand kleiner wählen
					p.setLeading(15f);
					p.setSpacingAfter(20f);
					document.add(p);
					break;
				default:	
				p = new Paragraph(
						AG+" chartert das Luftfahrzeug "+Typ+" "+Kennzeichen+" für die Zeit vom "+Beginndatum+" zum "+Endedatum+" zu einer Reise von "+FlugAnfang+" nach "+FlugEnde+" über "+Zwischen1+", "+Zwischen2+", "+Zwischen3+".",styleText);
				p.setAlignment(Element.ALIGN_JUSTIFIED);
				// zeilenabstand kleiner wählen
				p.setLeading(15f);
				p.setSpacingAfter(20f);
				document.add(p);
				}
				
				if (!Angebotsart.equals("Zeitcharter")) {
				p = new Paragraph("Flugplan:", styleUeberschrift2);
				p.setAlignment(Element.ALIGN_LEFT);
				// etwas abstand hinter der überschrift
				//p.setSpacingAfter(6f);
				document.add(p);
				} // endif
				//Flugzeugbild
		//		sql = "select benutzerverwaltung.flugzeug_bilder.flugzeuge_Flugzeug_ID from benutzerverwaltung.flugzeug_bilder where benutzerverwaltung.flugzeug_bilder.flugzeuge_flugzeug_id = '"+Kennzeichen+"'";
		//		rs = stmt_benutzerverwaltung.executeQuery(sql);
		//		rs.next();
				String filenamepic = "picture"+Kennzeichen+".jpg";
		//		String filenamepic = System.getProperty("user.dir") + "/picture"+Kennzeichen+".jpg";
		//		image = new File (filenamepic);
				System.out.println(filenamepic);
				Image image1 = Image.getInstance(PdfGenerator.class.getResource(filenamepic));
				image1.scaleAbsolute(250,40);
				image1.setAbsolutePosition(307,509);
				document.add(image1);
			 //     p.setSpacingAfter(6f);
				//	document.add(p);
				
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
				if (!Angebotsart.equals("Zeitcharter")) {
				addTable(document,angebot_id,modus);
				} // endif
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
				if (Angebotsart.equals("Zeitcharter")) {
				p = new Paragraph();
				p.add(new Chunk("Der Charterpreis setzt sich aus einem Fixpreisanteil und einem Flugpreisanteil zusammen. Der Flugpreisanteil basiert auf den verflogenen Flugstunden und entspricht der Triebwerkslaufzeit. Sie ist aus der Anzeige der Triebwerkslaufzeit im Cockpit ersichtlich.", styleText));
				document.add(p);
				p.setSpacingAfter(15f);
				
				}
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
			}
			else {
				
			
		p = new Paragraph(" ", styleText);
		p.setAlignment(Element.ALIGN_CENTER);
		// etwas abstand hinter der überschrift
		p.setSpacingAfter(6f);
		document.add(p);
		p = new Paragraph(" ", styleUeberschrift1);
		p.setAlignment(Element.ALIGN_LEFT);
		// etwas abstand hinter der überschrift
		p.setSpacingAfter(6f);
		document.add(p);
		p = new Paragraph(vorname+" "+nachname, styleText);
		p.setAlignment(Element.ALIGN_LEFT);
		// etwas abstand hinter der überschrift
		p.setSpacingAfter(6f);
		document.add(p);
		p = new Paragraph(strasse, styleText);
		p.setAlignment(Element.ALIGN_LEFT);
		// etwas abstand hinter der überschrift
		p.setSpacingAfter(6f);
		document.add(p);
		p = new Paragraph(" ", styleText);
		p.setAlignment(Element.ALIGN_LEFT);
		// etwas abstand hinter der überschrift
		p.setSpacingAfter(6f);
		document.add(p);
		p = new Paragraph(plz+" "+ort, styleText);
		p.setAlignment(Element.ALIGN_LEFT);
		// etwas abstand hinter der überschrift
		p.setSpacingAfter(20f);
		document.add(p);
		p = new Paragraph(modus, styleUeberschrift1);
		p.setAlignment(Element.ALIGN_LEFT);
		// etwas abstand hinter der überschrift
		p.setSpacingAfter(20f);
		document.add(p);
		p = new Paragraph("Sehr geehrte(r) "+AG+",", styleText);
		p.setAlignment(Element.ALIGN_LEFT);
		// etwas abstand hinter der überschrift
		p.setSpacingAfter(6f);
		document.add(p);
		p = new Paragraph("nachdem Sie auf unsere Erinnerungen zur Begleichung unserer Rechnung nicht reagiert haben, mahnen wir Sie nun an, die Rechnung binnen 7 Tage zu begleichen. Andernfalls sehen wir uns gezwungen, gerichtliche Schritte gegen Sie einzuleiten. ", styleText);
		p.setAlignment(Element.ALIGN_LEFT);
		// etwas abstand hinter der überschrift
		p.setSpacingAfter(6f);
		document.add(p);
		p = new Paragraph("Mit freundlichen Grüßen,", styleText);
		p.setAlignment(Element.ALIGN_LEFT);
		// etwas abstand hinter der überschrift
		p.setSpacingAfter(6f);
		document.add(p);
		p = new Paragraph("HINOTORI Executive AG", styleUeberschrift1);
		p.setAlignment(Element.ALIGN_LEFT);
		// etwas abstand hinter der überschrift
		p.setSpacingAfter(20f);
		document.add(p);
					
									
				// step 5
			}
		
		
		document.close();

		File file = new File(filename);
		System.out.println("Saved " + file.getCanonicalPath());

		
	}

	// ErzeugePDF - Ende-************************************************************************************************************
	
	private static ParagraphBorder enableBordering(PdfWriter pdfWriter) {
				ParagraphBorder border = new ParagraphBorder();
				pdfWriter.setPageEvent(border);
				return border;
			}

			private static void addTable(Document document, int angebot_id, String modus) throws DocumentException, SQLException {
			
			
				document.add(getSampleTable(angebot_id, modus));
			
			}
			
					
			
			
		//	private static String[][] DATEN = new String[][] { { "20.05.2016","08:00","München","1:30","09:30","Paris","3" },
		//		{ "21.05.2016","15:00","Paris","2:00","17:00","London","2" }, { "22.05.2016","09:00","London","2:00","11:00","Reykjavik","5" } };

				
			
			private static PdfPTable getSampleTable(int angebot_id, String modus) throws DocumentException, SQLException {
				
				String AG = "Erich";
				String Typ = "Dornier";
				String Kennzeichen = "120";
				String Beginndatum = "20.05.2016";
				String Endedatum = "01.06.2016";
				String FlugAnfang = "München";
				String FlugEnde = "";
				String Zwischen1 = "";
				String Zwischen2 = "";
				String Zwischen3 = "";
				String[] SPALTENKOPF = new String[] { "Datum", "Zeit \n(Abflug)", "Ort \n(von)","Flugzeit","Zeit (Ankunft)","Ort \n(nach)","Anzahl Passagiere"};

				String[][] DATEN = new String[10][7];

				final String hostname = "172.20.1.24"; 
				Connection conn;
		        final String port = "3306"; 
		        String dbname = "myflight";
				String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
				conn = DriverManager.getConnection(url, user, password);
				//if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password)
				int pax = 0;
				int angebote_angebote_id = 0;
				switch (modus) {
				case "Auftrag" :
				
				// Flüge zum Angebot ermitteln
				// Anzahl Passagiere ermitteln
				String sql = "select angebote.pax from angebote inner join auftraege on auftraege.angebote_angebote_id = angebote.angebote_id and auftraege.auftraege_id='"+angebot_id+"'";
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				
				rs.next();
				pax = rs.getInt(1);
				// Angebot_id ermitteln
				sql = "select angebote.angebote_id from angebote inner join auftraege on auftraege.angebote_angebote_id = angebote.angebote_id and auftraege.auftraege_id='"+angebot_id+"'";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				rs.next();
				angebote_angebote_id = rs.getInt(1);
				// Fluege ermitteln
				break;
				case "Angebot":
					// Flüge zum Angebot ermitteln
					// Anzahl Passagiere ermitteln
					sql = "select angebote.pax from angebote where angebote.angebote_id='"+angebot_id+"'";
					stmt = conn.createStatement();
					rs = stmt.executeQuery(sql);
					
					rs.next();
					pax = rs.getInt(1);
					// Angebot_id ermitteln
					angebote_angebote_id = angebot_id;
					// Fluege ermitteln
					break;
				}
				ResultSet rs1;
				String zielflughafen = "";
				String zwischenflughafen = "";
				Statement stmt = conn.createStatement();
				int zaehler = 0;
				Statement stmt1 = conn.createStatement();
				String sql = "select fluege.*, flughafen_von.FlughafenStadt from fluege left outer join flughafen_von on fluege.flughafen_von_FlughafenKuerzel = flughafen_von.FlughafenKuerzel where fluege.angebote_Angebote_ID ='"
						+angebote_angebote_id+ "'";
				ResultSet rs = stmt.executeQuery(sql);
				
				int i = 1;
				// Testbeginn
				// rs = null;
				// Testende
				int x = 0; int y = 0;
				while ((rs != null) && (rs.next())) {

					// Flughafenstadt vom Zielflughafen ermitteln
					sql = "select flughafen_bis.FlughafenStadt from flughafen_bis where flughafen_bis.FlughafenKuerzel ='"
							+ rs.getString(6) + "'";
					rs1 = stmt1.executeQuery(sql);
					rs1.first();
					// System.out.println(rs.getString(6));
					// if (rs1.first()) System.out.println("here it
					// is:"+rs1.getString(1));
					if (rs1.first()) {
						zielflughafen = rs1.getString(1);
						System.out.println(rs.getString(6) + " " + zielflughafen);
					} else {
						zielflughafen = "";

					}
					System.out.println(i++ + " " + rs.getString(1) + " " + rs.getString(3) + " " + rs.getString(9) + " "
							+ rs.getFloat(7) + " " + rs.getString(4) + " " + rs.getString(6));
		
					int h = (int)rs.getFloat(7);
					int min = (int)(Math.round((rs.getFloat(7)-(int)rs.getFloat(7))*60));
					String tmpFlugzeit = Integer.toString(h)+" h "+Integer.toString(min)+" min";
					
					
					DATEN[x][y] = rs.getString(1); y++;
					DATEN[x][y] = rs.getString(3); y++;
					DATEN[x][y] = rs.getString(9); y++;
					DATEN[x][y] = tmpFlugzeit; y++;
					DATEN[x][y] = rs.getString(4); y++;
					DATEN[x][y] = zielflughafen; y++;
					DATEN[x][y] = Integer.toString(pax); x++;y=0;
					
					
					FlugEnde = zielflughafen;
					zaehler++;
					switch (zaehler) {
					case 1:
						zwischenflughafen = zielflughafen;
						break;
					case 2:
						Zwischen1 = zwischenflughafen;
						zwischenflughafen = zielflughafen;
						break;
					case 3:
						Zwischen2 = zwischenflughafen;
						zwischenflughafen = zielflughafen;
						break;
					case 4:
						Zwischen3 = zwischenflughafen;
						zwischenflughafen = zielflughafen;
						break;

					}

				}	

			
				
				
				
				//int rows = DATEN.length;
				int rows = zaehler;
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
					float[] columnWidths = new float[] {9f, 7f, 10f, 9f, 7f, 8f, 9f};
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
	
			
	//*********************************************************************************************************************************************
	//*********************************************************************************************************************************************		
			public void erzeugeWord(int angebot_id, String modus) throws Exception {
				
				WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
				MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();
				
			try {	
				if (modus=="Auftrag"){
					filename = System.getProperty("user.dir") + "/"+Integer.toString(angebot_id)+".docx";}
				else {
					filename = System.getProperty("user.dir") + "/"+Integer.toString(angebot_id)+"m.docx";}
				
					
				factory = Context.getWmlObjectFactory();

				

				// mann kann die "vordefinierten" Styles ausgeben, diese wären
				// Kandidaten für solche Konstanten dann, wie WORD_STYLE_TITLE

		//		 Set<String> styles = StyleDefinitionsPart.getKnownStyles().keySet();
		//	 	System.out.println(Arrays.deepToString(styles.toArray()));

		Style styleTitel = StyleDefinitionsPart.getKnownStyles().get(WORD_STYLE_TITLE);
				Style styleUeberschrift1 = StyleDefinitionsPart.getKnownStyles().get(WORD_STYLE_HEADING1);
				Style styleUeberschrift2 = StyleDefinitionsPart.getKnownStyles().get(WORD_STYLE_HEADING2);
				Style styleText = StyleDefinitionsPart.getKnownStyles().get(WORD_STYLE_NORMAL);
	
				// under construction Style orgStyle = createStyle(StyleDefinitionsPart.getKnownStyles().get(WORD_STYLE_HEADING2), " ", 14, true, JcEnumeration.CENTER);
				// Logo einfügen
			
				
				
				// hier habe ich die Inhalte ins Dokument eingefügt
				//mdp.addStyledParagraphOfText(styleTitel.getStyleId(), "");

				
				
				// Parameter für Dokumenterstellung
				
				String sql = "SELECT angebote.*, kunden.*, flugzeugtypen.flugzeugtyp, auftraege.auftraege_id FROM angebote INNER JOIN auftraege inner join kunden inner join flugzeuge inner join flugzeugtypen on angebote.kunden_kunde_id= kunden.kunde_id and angebote.flugzeuge_Flugzeug_ID=flugzeuge.Flugzeug_ID and flugzeuge.Flugzeugtypen_Flugzeugtypen_ID=flugzeugtypen.Flugzeugtypen_ID and angebote.angebote_id=auftraege.Angebote_Angebote_ID where auftraege.auftraege_id = '"+ angebot_id + "'";
				final String hostname = "172.20.1.24"; 
		        final String port = "3306"; 
		        String dbname = "myflight";
				String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
				Connection conn = DriverManager.getConnection(url, user, password);
			    if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
			   	Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				rs.next();
			String AG = rs.getString(27);
			AG = AG+" "+rs.getString(28);
			String Typ=rs.getString(40);
			String Kennzeichen =rs.getString(16);
			String vorname = rs.getString(29);
			String nachname = rs.getString(28);
			String strasse = rs.getString(34);
			String plz = rs.getString(36);
			String ort = rs.getString(37);
			
			addLogo(mdp, wordMLPackage, Kennzeichen);
			 //artcharter.setText(rs.getString(4));
				
				// Create an instance of SimpleDateFormat used for formatting 
				// the string representation of date (month/day/year)
				DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
				
				// Using DateFormat format method we can create a string 
				// representation of a date with the defined format.
				String Beginndatum = df.format(rs.getObject(21));
				
				// Create an instance of SimpleDateFormat used for formatting 
				// the string representation of date (month/day/year)
				//DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					      
				// Using DateFormat format method we can create a string 
				// representation of a date with the defined format.
				
				String Endedatum = df.format(rs.getObject(22));
				//Charterdauer
				System.out.println(rs.getFloat(14));		
				
				
				double chartzeit = Math.round(rs.getFloat(14) * 100)/ 100.0; 
				String Charterdauer = Double.toString(chartzeit);
				
				//Flugzeit
				System.out.println(rs.getFloat(15));		
				
				
				int h = (int)rs.getFloat(15);
				int min = (int)(Math.round((rs.getFloat(15)-(int)rs.getFloat(15))*60));
				Flugzeit = Integer.toString(h)+" h "+Integer.toString(min)+" min";
				
				//Preis netto
				System.out.println(rs.getInt(8));		
				String Preisnetto = Integer.toString(rs.getInt(8))+" EUR";
				int intpreisnetto = rs.getInt(8);
				// Preis Brutto
				System.out.println(rs.getInt(7));		
				String Preisbrutto = Integer.toString(rs.getInt(7))+ " EUR";
				int intpreisbrutto = rs.getInt(7);					
		//Preis Mwst
				int Preismwst = intpreisbrutto - intpreisnetto;
				String Mwst = Integer.toString(Preismwst)+" EUR";
	
				// Angebot_id ermitteln & Angebotsart
				sql = "select angebote.angebote_id,angebote.Chartertyp_Chartertyp from angebote inner join auftraege on auftraege.angebote_angebote_id = angebote.angebote_id and auftraege.auftraege_id='"+angebot_id+"'";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				rs.next();
				int angebote_angebote_id = rs.getInt(1);
				String Angebotsart = rs.getString(2);
				System.out.println(rs.getString(2));
		
				String FlugAnfang = "";
				String FlugEnde = "";
				if (!Angebotsart.equals("Zeitcharter")) {
				//Abflugort
		sql = "select flughafen_von.flughafenstadt from flughafen_von inner join fluege on flughafen_von.FlughafenKuerzel=fluege.flughafen_von_FlughafenKuerzel inner join angebote on fluege.angebote_angebote_id = angebote.angebote_id and angebote.angebote_id='"+angebote_angebote_id+"'";
		rs = stmt.executeQuery(sql);
		rs.next();
		FlugAnfang = rs.getString(1);
		System.out.println(rs.getString(1));
						
		//Ankunftort
		sql = "select flughafen_bis.flughafenstadt from flughafen_bis inner join fluege on flughafen_bis.FlughafenKuerzel=fluege.flughafen_bis_FlughafenKuerzel inner join angebote on fluege.angebote_angebote_id = angebote.angebote_id and angebote.angebote_id='"+angebote_angebote_id+"'";
		rs = stmt.executeQuery(sql);
		rs.next();
		FlugEnde = rs.getString(1);
					
				}
								
						
				
					
				
									
								//String AG = "Erich";
								//String Typ = "Dornier";
								//String Kennzeichen = "120";
								//String Beginndatum = "20.05.2016";
								//String Endedatum = "01.06.2016";
								//String FlugAnfang = "München";
								//String FlugEnde = "New York";
								String Zwischen1 = "";
								String Zwischen2 = "";
								String Zwischen3 = "";
								//String Charterdauer = "124:30 h";
								//String Flugzeit = "24:45";
								//String Preisnetto = "1.450,00 EUR";
								//String Mwst = "275,50 EUR";
								//String Preisbrutto = "1.725,50 EUR";
								

				
				
				

			switch (modus) {
			case "Auftrag" :
						
						//centerParagraph(mdp.addParagraphOfText(
						//		"Ganz normaler Text."));
						centerParagraph(mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), "Chartervertrag"));
						doBoldFormat(getFirstRunOfParagraph(getLastParagraph(mdp)));
						centerParagraph(mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), "zwischen"));
						centerParagraph(mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), "HINOTORI Executive AG (Auftragnehmer)"));
						doBoldFormat(getFirstRunOfParagraph(getLastParagraph(mdp)));
						centerParagraph(mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), "und"));
						centerParagraph(mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), vorname+" "+nachname));
						doBoldFormat(getFirstRunOfParagraph(getLastParagraph(mdp)));
		
		
						String[][] DATEN = new String[10][7];
						int zaehler = 0;
				
						//if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password)
						
						if (!Angebotsart.equals("Zeitcharter")) {
						// Flüge zum Angebot ermitteln
						
						// Fluege ermitteln
						ResultSet rs1;
						String zielflughafen = "";
						String zwischenflughafen = "";
						
						zaehler = 0;
						Statement stmt1 = conn.createStatement();
						sql = "select fluege.*, flughafen_von.FlughafenStadt from fluege left outer join flughafen_von on fluege.flughafen_von_FlughafenKuerzel = flughafen_von.FlughafenKuerzel where fluege.angebote_Angebote_ID ='"
								+angebote_angebote_id+ "'";
						rs = stmt.executeQuery(sql);
						
						int i = 1;
						// Testbeginn
						// rs = null;
						// Testende
						int x = 0; int y = 0;
						while ((rs != null) && (rs.next())) {

							// Flughafenstadt vom Zielflughafen ermitteln
							sql = "select flughafen_bis.FlughafenStadt from flughafen_bis where flughafen_bis.FlughafenKuerzel ='"
									+ rs.getString(6) + "'";
							rs1 = stmt1.executeQuery(sql);
							rs1.first();
							// System.out.println(rs.getString(6));
							// if (rs1.first()) System.out.println("here it
							// is:"+rs1.getString(1));
							if (rs1.first()) {
								zielflughafen = rs1.getString(1);
								System.out.println(rs.getString(6) + " " + zielflughafen);
							} else {
								zielflughafen = "";

							}
							System.out.println(i++ + " " + rs.getString(1) + " " + rs.getString(3) + " " + rs.getString(9) + " "
									+ rs.getFloat(7) + " " + rs.getString(4) + " " + rs.getString(6));
							
							
							h = (int)rs.getFloat(7);
							min = (int)(Math.round((rs.getFloat(7)-(int)rs.getFloat(7))*60));
							String tmpFlugzeit = Integer.toString(h)+" h "+Integer.toString(min)+" min";
							
							DATEN[x][y] = rs.getString(1); y++;
							DATEN[x][y] = rs.getString(3); y++;
							DATEN[x][y] = rs.getString(9); y++;
							DATEN[x][y] = tmpFlugzeit; y++;
							DATEN[x][y] = rs.getString(4); y++;
							DATEN[x][y] = zielflughafen; y++;
							DATEN[x][y] = Integer.toString(pax); x++;y=0;
							
							
							FlugEnde = zielflughafen;
							zaehler++;
							switch (zaehler) {
							case 1:
								zwischenflughafen = zielflughafen;
								break;
							case 2:
								Zwischen1 = zwischenflughafen;
								zwischenflughafen = zielflughafen;
								break;
							case 3:
								Zwischen2 = zwischenflughafen;
								zwischenflughafen = zielflughafen;
								break;
							case 4:
								Zwischen3 = zwischenflughafen;
								zwischenflughafen = zielflughafen;
								break;

							}

						}	
						} //endif
						switch (zaehler) {
						
						case 0: 
							mdp.addParagraphOfText(AG+" chartert das Luftfahrzeug "+Typ+" "+Kennzeichen+" für die Zeit vom "+Beginndatum+" zum "+Endedatum+".");
							break;
						case 1: 
							mdp.addParagraphOfText(AG+" chartert das Luftfahrzeug "+Typ+" "+Kennzeichen+" für die Zeit vom "+Beginndatum+" zum "+Endedatum+" zu einer Reise von "+FlugAnfang+" nach "+FlugEnde+".");
							break;
						case 2:
							mdp.addParagraphOfText(AG+" chartert das Luftfahrzeug "+Typ+" "+Kennzeichen+" für die Zeit vom "+Beginndatum+" zum "+Endedatum+" zu einer Reise von "+FlugAnfang+" nach "+FlugEnde+" über "+Zwischen1+".");
							break;
						case 3:
							mdp.addParagraphOfText(AG+" chartert das Luftfahrzeug "+Typ+" "+Kennzeichen+" für die Zeit vom "+Beginndatum+" zum "+Endedatum+" zu einer Reise von "+FlugAnfang+" nach "+FlugEnde+" über "+Zwischen1+", "+Zwischen2+".");
							break;
						default:	
							mdp.addParagraphOfText(AG+" chartert das Luftfahrzeug "+Typ+" "+Kennzeichen+" für die Zeit vom "+Beginndatum+" zum "+Endedatum+" zu einer Reise von "+FlugAnfang+" nach "+FlugEnde+" über "+Zwischen1+", "+Zwischen2+", "+Zwischen3+".");
						}
							
						
				
			addLogo1(mdp, wordMLPackage, Kennzeichen);
			if (!Angebotsart.equals("Zeitcharter")) {
			mdp.addStyledParagraphOfText(styleUeberschrift2.getStyleId(), "Flugplan:");
			}
				// das hier zeigt, wie ein ganzer Paragraph relativ einfach fett gemacht werden kann
				//mdp.addParagraphOfText("Fetter Text. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam.");
				// hier nehme ich den letzten Paragraphen des Dokumentes (der zuletzt ins Dokument eingefügte Paragraph)
				// und mach ihn bzw. seine erste Passage fett
				doBoldFormat(getFirstRunOfParagraph(getLastParagraph(mdp)));

				// hier schreiben wir einen Satz über die komplexe Art und weise, weil einige Wörter anders formattiert sein müssen
				// addMixedStyleParagraph(mdp);

				// Tabelle
				if (!Angebotsart.equals("Zeitcharter")) {
				addTable(mdp, wordMLPackage, angebot_id);
				}
				mdp.addParagraphOfText("");
				mdp.addParagraphOfText("Charterdauer insgesamt (Stunden): \t\t\t\t"+Charterdauer);
				
				if (Angebotsart.equals("Zeitcharter")) {
					mdp.addParagraphOfText("Der Charterpreis setzt sich aus einem Fixpreisanteil und einem Flugpreisanteil zusammen. Der Flugpreisanteil basiert auf den verflogenen Flugstunden und entspricht der Triebwerkslaufzeit. Sie ist aus der Anzeige der Triebwerkslaufzeit im Cockpit ersichtlich.");
					mdp.addParagraphOfText("");
										}
				
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
		
				break;
			default :
			
				//centerParagraph(mdp.addParagraphOfText(
				//		"Ganz normaler Text."));
				
				mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), " ");
				mdp.addStyledParagraphOfText(styleText.getStyleId(), vorname+" "+nachname);
				mdp.addStyledParagraphOfText(styleText.getStyleId(), strasse);
				mdp.addStyledParagraphOfText(styleText.getStyleId(), plz+" "+ort);
				mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), modus);
				doBoldFormat(getFirstRunOfParagraph(getLastParagraph(mdp)));
				mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), " ");
				mdp.addStyledParagraphOfText(styleText.getStyleId(), "Sehr geehrte(r) "+AG+",");
				mdp.addStyledParagraphOfText(styleText.getStyleId(), "nachdem Sie auf unsere Erinnerungen zur Begleichung unserer Rechnung nicht reagiert haben, mahnen wir Sie nun an, die Rechnung binnen 7 Tage zu begleichen. Andernfalls sehen wir uns gezwungen, gerichtliche Schritte gegen Sie einzuleiten. ");
				// doBoldFormat(getFirstRunOfParagraph(getLastParagraph(mdp)));
				mdp.addStyledParagraphOfText(styleText.getStyleId(), "Mit freundlichen Grüßen,");
				mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), "HINOTORI Executive AG");
				// doBoldFormat(getFirstRunOfParagraph(getLastParagraph(mdp)));

				
			break;
			
			
			
			}
			
				
			}
			catch (Exception e) {
				lbl_dbconnect.setText("Es ist ein Fehler aufgetreten");
				e.printStackTrace();
			}
				try {
				// speichern
				File file = new java.io.File(filename);
				Docx4J.save(wordMLPackage, file, Docx4J.FLAG_SAVE_ZIP_FILE);
				System.out.println("Saved " + file.getCanonicalPath());
				}
				catch (Exception e) {
					lbl_dbconnect.setText("Es ist ein Fehler aufgetreten");
					e.printStackTrace();
				}
				
			}

			private static R getFirstRunOfParagraph(P lastParagraph) {
				return (R) lastParagraph.getContent().get(0);
			}

			private static P getLastParagraph(MainDocumentPart mdp) {
				int lastContentItem = mdp.getContent().size() - 1;
				P lastParagraph = (P) mdp.getContent().get(lastContentItem);
				return lastParagraph;
			}

			private static void addLogo(MainDocumentPart mdp, WordprocessingMLPackage wordMLPackage, String Kennzeichen) throws Exception {
				
						InputStream inputStream = WordGenerator.class.getResourceAsStream("Logo2.jpg");			
						
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

				
				P p = newImage(wordMLPackage, bytes, filenameHint, altText, id1, id2, 9100);
				
				mdp.addObject(p);
				
			}
			private static void addLogo1(MainDocumentPart mdp, WordprocessingMLPackage wordMLPackage, String Kennzeichen) throws Exception {
				
				String filenamepic = "picture"+Kennzeichen+".jpg";
				//		String filenamepic = System.getProperty("user.dir") + "/picture"+Kennzeichen+".jpg";
				//		image = new File (filenamepic);
						System.out.println(filenamepic);
						InputStream inputStream1 = WordGenerator.class.getResourceAsStream(filenamepic);		
						
				long fileLength1 = 1050712; // 480kB
				byte[] bytes1 = new byte[(int) fileLength1];

				int offset = 0;
				int numRead = 0;

				while (offset < bytes1.length && (numRead = inputStream1.read(bytes1, offset, bytes1.length - offset)) >= 0) {
					offset += numRead;
				}

				inputStream1.close();
					
				String filenameHint = "FreeLogo";
				String altText = "Einfach nur ein Logo";

				int id1 = 0;
				int id2 = 1;

				
				P p = newImage(wordMLPackage, bytes1, filenameHint, altText, id1, id2, 5100);
				mdp.addObject(p);
				
			}
	
			
			public static P newImage(WordprocessingMLPackage wordMLPackage, byte[] bytes, String filenameHint, String altText,
					int id1, int id2, int laenge) throws Exception {
				BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);
				Inline inline = imagePart.createImageInline(filenameHint, altText, id1, id2, laenge, false);
			
				
				P p = factory.createP();

				R run = factory.createR();
				p.getContent().add(run);

				org.docx4j.wml.Drawing drawing = factory.createDrawing();
				run.getContent().add(drawing);
				drawing.getAnchorOrInline().add(inline);

				return p;
			}

			private static void addTable(MainDocumentPart mdp, WordprocessingMLPackage wordMLPackage, int angebot_id) throws SQLException {
				mdp.addObject(getSampleTable(wordMLPackage, angebot_id));
			}

				private static String[] SPALTENKOPFword = new String[] { "Datum", "Zeit \n(Abflug)", "Ort \n(von)","Flugzeit","Zeit (Ankunft)","Ort \n(nach)","Anzahl Passagiere"};


//<<<<<<< HEAD
				private static String[][] DATENword = new String[][] { { "20.05.2016","08:00","München","1:30","09:30","Paris","3" },
					{ "21.05.2016","15:00","Paris","2:00","17:00","London","2" }, { "22.05.2016","09:00","London","2:00","11:00","Reykjavik","5" } };
				@FXML Button btn_offer_newcust;

				
//=======
//		//		private static String[][] DATENword = new String[][] { { "20.05.2016","08:00","München","1:30","09:30","Paris","3" },
//		//			{ "21.05.2016","15:00","Paris","2:00","17:00","London","2" }, { "22.05.2016","09:00","London","2:00","11:00","Reykjavik","5" } };
//					
//					
//>>>>>>> branch 'master' of https://github.com/burggraf-erich/itworks.git
//				
					
			
			private static Tbl getSampleTable(WordprocessingMLPackage wPMLpackage, int angebot_id) throws SQLException {
				int writableWidthTwips = wPMLpackage.getDocumentModel().getSections().get(0).getPageDimensions()
						.getWritableWidthTwips();

				String Zwischen1 = "";
				String Zwischen2 = "";
				String Zwischen3 = "";
				String FlugEnde = "";
				String[][] DATEN = new String[10][7];

				final String hostname = "172.20.1.24"; 
				Connection conn;
		        final String port = "3306"; 
		        String dbname = "myflight";
				String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
				conn = DriverManager.getConnection(url, user, password);
				//if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password)
				
				// Flüge zum Angebot ermitteln
				// Anzahl Passagiere ermitteln
				String sql = "select angebote.pax from angebote inner join auftraege on auftraege.angebote_angebote_id = angebote.angebote_id and auftraege.auftraege_id='"+angebot_id+"'";
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				rs.next();
				int pax = rs.getInt(1);
				// Angebot_id ermitteln
				sql = "select angebote.angebote_id from angebote inner join auftraege on auftraege.angebote_angebote_id = angebote.angebote_id and auftraege.auftraege_id='"+angebot_id+"'";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				rs.next();
				int angebote_angebote_id = rs.getInt(1);
				// Fluege ermitteln
				ResultSet rs1;
				String zielflughafen = "";
				String zwischenflughafen = "";
				
				int zaehler = 0;
				Statement stmt1 = conn.createStatement();
				sql = "select fluege.*, flughafen_von.FlughafenStadt from fluege left outer join flughafen_von on fluege.flughafen_von_FlughafenKuerzel = flughafen_von.FlughafenKuerzel where fluege.angebote_Angebote_ID ='"
						+angebote_angebote_id+ "'";
				rs = stmt.executeQuery(sql);
				
				int i = 1;
				// Testbeginn
				// rs = null;
				// Testende
				int x = 0; int y = 0;
				while ((rs != null) && (rs.next())) {

					// Flughafenstadt vom Zielflughafen ermitteln
					sql = "select flughafen_bis.FlughafenStadt from flughafen_bis where flughafen_bis.FlughafenKuerzel ='"
							+ rs.getString(6) + "'";
					rs1 = stmt1.executeQuery(sql);
					rs1.first();
					// System.out.println(rs.getString(6));
					// if (rs1.first()) System.out.println("here it
					// is:"+rs1.getString(1));
					if (rs1.first()) {
						zielflughafen = rs1.getString(1);
						System.out.println(rs.getString(6) + " " + zielflughafen);
					} else {
						zielflughafen = "";

					}
					System.out.println(i++ + " " + rs.getString(1) + " " + rs.getString(3) + " " + rs.getString(9) + " "
							+ rs.getFloat(7) + " " + rs.getString(4) + " " + rs.getString(6));
					
					
					int h = (int)rs.getFloat(7);
					int min = (int)(Math.round((rs.getFloat(7)-(int)rs.getFloat(7))*60));
					String tmpFlugzeit = Integer.toString(h)+" h "+Integer.toString(min)+" min";
					
					
					DATEN[x][y] = rs.getString(1); y++;
					DATEN[x][y] = rs.getString(3); y++;
					DATEN[x][y] = rs.getString(9); y++;
					DATEN[x][y] = tmpFlugzeit; y++;
					DATEN[x][y] = rs.getString(4); y++;
					DATEN[x][y] = zielflughafen; y++;
					DATEN[x][y] = Integer.toString(pax); x++;y=0;
					
					
					FlugEnde = zielflughafen;
					zaehler++;
					switch (zaehler) {
					case 1:
						zwischenflughafen = zielflughafen;
						break;
					case 2:
						Zwischen1 = zwischenflughafen;
						zwischenflughafen = zielflughafen;
						break;
					case 3:
						Zwischen2 = zwischenflughafen;
						zwischenflughafen = zielflughafen;
						break;
					case 4:
						Zwischen3 = zwischenflughafen;
						zwischenflughafen = zielflughafen;
						break;

					}

				}	

			
				
				
				
				//int rows = DATEN.length;
				int rows = zaehler;
				
				
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
						tx.setValue(DATEN[rowIndex][colIndex]);
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
			
	public void action_drucken() throws Exception {
		int angebot_id = Nummerorder.getCellData(auftragtable.getSelectionModel().getSelectedIndex());

		filename = System.getProperty("user.dir") + "/" + Integer.toString(angebot_id) + ".pdf";
		f = new File(filename);

		erzeugePdf(angebot_id, "Auftrag");

		try {
			PDFPrinter druck = new PDFPrinter(f);
			lbl_dbconnect.setText("Ausdruck gestartet");
		} catch (Exception e) {
			lbl_dbconnect.setText("Druckdatei nicht vorhanden");
			e.printStackTrace();
		}

	}
	public void action_drucken_angebot() throws Exception {
		int angebot_id = Nummer.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex());

		filename = System.getProperty("user.dir") + "/" + Integer.toString(angebot_id) + "angebot.pdf";
		f = new File(filename);

		erzeugePdf(angebot_id, "Angebot");

		try {
			PDFPrinter druck = new PDFPrinter(f);
			lbl_dbconnect.setText("Ausdruck gestartet");
		} catch (Exception e) {
			lbl_dbconnect.setText("Druckdatei nicht vorhanden");
			e.printStackTrace();
		}

	}

			public void action_versenden() throws IOException, URISyntaxException, SQLException {
				int angebot_id = Nummerorder.getCellData(auftragtable.getSelectionModel().getSelectedIndex());
				
				
				// Kundenanrede
				String sql = "select kunden.KundeAnrede from kunden inner join angebote inner join auftraege on kunden.kunde_id=angebote.kunden_kunde_id and auftraege.angebote_angebote_id=angebote.angebote_id and auftraege.auftraege_id='"
						+ angebot_id + "'";

				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getString(1));
				String kundenanrede = rs.getString(1);
				
			// Kundenname
				sql = "select kunden.kundename from kunden inner join angebote inner join auftraege on kunden.kunde_id=angebote.kunden_kunde_id and auftraege.angebote_angebote_id=angebote.angebote_id and auftraege.auftraege_id='"
						+ angebot_id + "'";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getString(1));
				String Kunde = rs.getString(1);
			//Datum von
				sql = "select angebote.angebotsdatum from angebote inner join auftraege on auftraege.angebote_angebote_id=angebote.angebote_id and auftraege.auftraege_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				// Create an instance of SimpleDateFormat used for formatting 
				// the string representation of date (month/day/year)
				DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

					      
				// Using DateFormat format method we can create a string 
				// representation of a date with the defined format.
				String reportDate = df.format(rs.getObject(1));
				System.out.println(reportDate);
				String Datum = reportDate;
			// Kundenmailadresse
				sql = "select kunden.kundeemail from kunden inner join angebote inner join auftraege on kunden.kunde_id=angebote.kunden_kunde_id and auftraege.angebote_angebote_id=angebote.angebote_id and auftraege.auftraege_id='"
						+ angebot_id + "'";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getString(1));
				String mailadresse = rs.getString(1);			
			
			//String Kunde = "Burggraf";
			// int Nummer = 100302;
			//String Datum = "10.06.2016";
			Mainmail mail = new Mainmail(kundenanrede,Kunde,angebot_id,Datum,mailadresse,"Auftrag");
			
				
				
//				String Kunde = "Burggraf";
//				int Nummer = 100302;
//				String Datum = "10.06.2016";
			//	Mainmail mail = new Mainmail(Kunde,Nummer,Datum);
				
				}
			
			
			public void action_versenden_angebot() throws IOException, URISyntaxException, SQLException {
				int angebot_id = Nummer.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex());
				
				
				// Kundenanrede
				String sql = "select kunden.KundeAnrede from kunden inner join angebote on kunden.kunde_id=angebote.kunden_kunde_id and angebote.angebote_id='"
						+ angebot_id + "'";
				final String hostname = "172.20.1.24"; 
		        final String port = "3306"; 
		        String dbname = "myflight";
				String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
				conn = DriverManager.getConnection(url, user, password);
				//if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
				
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getString(1));
				String kundenanrede = rs.getString(1);
				
			// Kundenname
				sql = "select kunden.Kundename from kunden inner join angebote on kunden.kunde_id=angebote.kunden_kunde_id and angebote.angebote_id='"
						+ angebot_id + "'";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getString(1));
				String Kunde = rs.getString(1);
			//Datum von
				sql = "select angebote.angebotsdatum from angebote where angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				// Create an instance of SimpleDateFormat used for formatting 
				// the string representation of date (month/day/year)
				DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

					      
				// Using DateFormat format method we can create a string 
				// representation of a date with the defined format.
				String reportDate = df.format(rs.getObject(1));
				System.out.println(reportDate);
				String Datum = reportDate;
			// Kundenmailadresse
				sql = "select kunden.kundeemail from kunden inner join angebote on kunden.kunde_id=angebote.kunden_kunde_id and angebote.angebote_id='"
						+ angebot_id + "'";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getString(1));
				String mailadresse = rs.getString(1);			
			
			//String Kunde = "Burggraf";
			// int Nummer = 100302;
			//String Datum = "10.06.2016";
			Mainmail mail = new Mainmail(kundenanrede,Kunde,angebot_id,Datum,mailadresse,"Angebot");
			
				
				
//				String Kunde = "Burggraf";
//				int Nummer = 100302;
//				String Datum = "10.06.2016";
			//	Mainmail mail = new Mainmail(Kunde,Nummer,Datum);
				
				}			
@FXML		public void saveorderchange() throws SQLException {
					int angebot_id = Nummerorder.getCellData(auftragtable.getSelectionModel().getSelectedIndex());
					String orderchange = choiceorderstatus.getValue().toString();
					System.out.println(orderchange);
					Statement stmt = conn.createStatement();
					try {
					stmt.executeUpdate("Update auftraege set Auftragsstatus_Auftragsstatus='"+orderchange+"' where auftraege.auftraege_id='"+angebot_id+"'");
					lbl_dbconnect.setText("Änderung gespeichert");	
					actiongetaufträgepgm(true);					
				} catch (SQLException sqle) {

					lbl_dbconnect.setText("Datenbankverbindung fehlgeschlagen");
					// System.out.println("geht nicht");
					sqle.printStackTrace();
				}
				}

	@FXML
	public void action_createbill(ActionEvent event) throws Exception {
		// gewählter Auftrag dessen Daten für Speicherung Rechnung übernehmen
		int auftrag_id = Nummerorder.getCellData(auftragtable.getSelectionModel().getSelectedIndex());
		String tmpflgztyp = Flgztyporder.getCellData(auftragtable.getSelectionModel().getSelectedIndex());
		String tmpAart = Aartorder.getCellData(auftragtable.getSelectionModel().getSelectedIndex());
		String tmpkdgruppe = Kdgruppeorder.getCellData(auftragtable.getSelectionModel().getSelectedIndex());
		String tmpauftragstatus = Statusorder.getCellData(auftragtable.getSelectionModel().getSelectedIndex());

		String sql = "select rechnungen.auftraege_auftraege_id from rechnungen where rechnungen.auftraege_auftraege_id='"
				+ auftrag_id + "'";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if ((rs != null) && (rs.next()))
			lbl_dbconnect.setText("Rechnung bereits vorhanden");
		else {
			// ermittle nächste Rechnungen-ID für speichern einer Rechnung

			sql = "select max(rechnungen_id) from rechnungen";
			rs = stmt.executeQuery(sql);
			rs.next();
			int newrechnungen_id = rs.getInt(1) + 1;
			System.out.println(newrechnungen_id);

			String tmprechnungstatus = "erstellt";

			// ermittle Rechnungsdatum
		
		     GregorianCalendar now = new GregorianCalendar();

		     
		      //   date.add(GregorianCalendar.DAY_OF_MONTH, +7);
		     String tagesdatum = now.get(GregorianCalendar.YEAR)+ "-"+ (now.get(GregorianCalendar.MONTH) + 1) + "-"+now.get(GregorianCalendar.DAY_OF_MONTH);
		     //    	System.out.println(mahndate.before(date));
			
			 // ermittle Zahlungstermin
	        GregorianCalendar target = new GregorianCalendar();
	        target.add(GregorianCalendar.DAY_OF_MONTH, +14);
	        String zahlungstermin = target.get(GregorianCalendar.YEAR)+ "-"+ (target.get(GregorianCalendar.MONTH) + 1) + "-"+target.get(GregorianCalendar.DAY_OF_MONTH);
	        	       
			
			System.out.println(" " + newrechnungen_id + " " + tagesdatum + " " + zahlungstermin + " " + " "
					+ tmprechnungstatus + " " + auftrag_id);
			// speichere Rechnungsdaten
			try {
				stmt.executeUpdate(
						"insert into rechnungen (Rechnungen_ID ,Rechnungsdatum,Zahlungstermin,Rechnungsstatus_Rechnungsstatus,Auftraege_Auftraege_ID ) values ('"
								+ newrechnungen_id + "','" + tagesdatum + "','" + zahlungstermin + "','"
								+ tmprechnungstatus + "','" + auftrag_id + "')");
				lbl_dbconnect.setText("Rechnung gespeichert");
				actiongetaufträgepgm(true);
			} catch (SQLException sqle) {

				lbl_dbconnect.setText("Datenbankverbindung fehlgeschlagen");
				// System.out.println("geht nicht");
				sqle.printStackTrace();
			}
		}	

		
		}
		


	@FXML
	public void action_delete_order(ActionEvent event) throws Exception {
		int auftrag_id = Nummerorder.getCellData(auftragtable.getSelectionModel().getSelectedIndex());
		int tmpbillorder = billorder.getCellData(auftragtable.getSelectionModel().getSelectedIndex());
		if (tmpbillorder != 0)
			lbl_dbconnect.setText("Auftrag nicht löschbar, Rechnung vorhanden");
		else {
			List<String> choices = new ArrayList<>();
			choices.clear();
			choices.add("Ja");
			choices.add("Nein");

			ChoiceDialog<String> dialog1 = new ChoiceDialog<>("Ja", choices);
			dialog1.setTitle("Auftrag löschen");
			dialog1.setHeaderText("Wollen Sie den Auftrag \nwirklich löschen?");
			dialog1.setContentText("Auswahl:");

			// Traditional way to get the response value.
			Optional<String> result1 = dialog1.showAndWait();
			// if (result.isPresent()){
			// System.out.println("Your choice: " + result.get());
			// }

			// The Java 8 way to get the response value (with lambda
			// expression).
			result1.ifPresent(letter -> System.out.println("Your choice: " + letter));

			if (result1.isPresent()) {
				AuswahlDokutyp = result1.get();

				if (AuswahlDokutyp == "Ja") {
					String sql = "delete from auftraege where auftraege.auftraege_id ='" + auftrag_id + "'";
					Statement statement = conn.createStatement();
					try {
						statement.executeUpdate(sql);
						lbl_dbconnect.setText("Auftrag gelöscht");
						actiongetaufträgepgm(true);
					} catch (SQLException sqle) {

						lbl_dbconnect.setText("Datenbankverbindung fehlgeschlagen");
						sqle.printStackTrace();
					}
				}
			}
		}

	}
	
	
	@FXML
	public void action_editangebotstatus(ActionEvent event) throws Exception {
		int angebot_id = Nummer.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex());
		String tmpangebotstatus = Status.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex());

		List<String> choices = new ArrayList<>();
		choices.clear();
		choices.add("offen");
		choices.add("positiv");
		choices.add("negativ");

		ChoiceDialog<String> dialog1 = new ChoiceDialog<>(tmpangebotstatus, choices);
		dialog1.setTitle("Angebotstatus ändern");
		dialog1.setHeaderText("Bitte wählen Sie\nden neuen Status aus");
		dialog1.setContentText("Auswahl:");

		// Traditional way to get the response value.
		Optional<String> result1 = dialog1.showAndWait();
		// if (result.isPresent()){
		// System.out.println("Your choice: " + result.get());
		// }

		// The Java 8 way to get the response value (with lambda
		// expression).
		result1.ifPresent(letter -> System.out.println("Your choice: " + letter));

		if (result1.isPresent()) {
			String tmp_new_angebotstatus = result1.get();

			String sql = "update angebote set angebote.angebotsstatus_angebotsstatus = '"+tmp_new_angebotstatus+"' where angebote.angebote_id = '"
					+ angebot_id + "'";
			final String hostname = "172.20.1.24"; 
			final String port = "3306"; 
			String dbname = "myflight";		
			String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
			Connection conn = DriverManager.getConnection(url, user, password);
			Statement stmt = conn.createStatement();
			
			try {
				stmt.executeUpdate(sql);
				lbl_dbconnect.setText("Angebotstatus geändert");
				actiongetangebotepgm(true);
			} catch (SQLException sqle) {

				lbl_dbconnect.setText("Datenbankverbindung fehlgeschlagen");
				sqle.printStackTrace();
			}
		}

	}
	@FXML
	public void action_karenz_mahnung(ActionEvent event) throws Exception {
		
		final String hostname = "172.20.1.24"; 
        final String port = "3306"; 
        String dbname = "myflight";
		String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
		conn = DriverManager.getConnection(url, user, password);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select mahndauer.dauer from mahndauer");
		rs.next();
		int karenz_mahntage = rs.getInt(1);
		
		List<String> choices = new ArrayList<>();
		choices.clear();
		for (int i = 0;i<101;i++) choices.add(Integer.toString(i));
		
		ChoiceDialog<String> dialog1 = new ChoiceDialog<>(Integer.toString(karenz_mahntage), choices);
		dialog1.setTitle("Karenztage für Mahnungen ändern");
		dialog1.setHeaderText("Bitte wählen Sie\ndie Karenztage für die \nMahnungsüberwachung aus");
		dialog1.setContentText("Auswahl:");

		// Traditional way to get the response value.
		Optional<String> result1 = dialog1.showAndWait();
		// if (result.isPresent()){
		// System.out.println("Your choice: " + result.get());
		// }

		// The Java 8 way to get the response value (with lambda
		// expression).
		result1.ifPresent(letter -> System.out.println("Your choice: " + letter));

		if (result1.isPresent()) {
			String tmp_new_mahntage = result1.get();

			String sql = "update mahndauer set dauer = "+Integer.parseInt(tmp_new_mahntage);
			Statement statement = conn.createStatement();

			try {
				statement.executeUpdate(sql);
				lbl_dbconnect.setText("Karenztage geändert");
				
			} catch (SQLException sqle) {

				lbl_dbconnect.setText("Datenbankverbindung fehlgeschlagen");
				sqle.printStackTrace();
			}
		}

	}

			@FXML public void action_get_calendar() {
				set_allunvisible(false);
				
				apa_calendar.setVisible(true);
				apa_btn_term.setVisible(true);
				dap_cal.setValue(LocalDate.now());
				cbo_cal_ma.setDisable(true);
				cbo_cal_fz.setDisable(true);
				cbo_cal_fz.getItems().clear();
				cbo_cal_ma.getItems().clear();
				
				try{
			    	
			    	Statement statement = conn.createStatement();
			    	ResultSet rs2 = statement.executeQuery("SELECT Distinct(Flugzeug_ID), FlugzeugHersteller, FlugzeugTyp  FROM myflight.flugzeuge join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=Flugzeugtypen_ID order by Flugzeug_ID ");      
			        while((rs2 != null) && (rs2.next())){
			        	
			        	cbo_cal_fz.getItems().add(rs2.getInt(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
		        }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");     
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				try{
			    	
			    	Statement statement = conn.createStatement();
			    	ResultSet rs2 = statement.executeQuery("SELECT * FROM myflight.personal");      
			        while((rs2 != null) && (rs2.next())){
			        	
			        	cbo_cal_ma.getItems().add(rs2.getInt(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
		        }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");    
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				cbo_cal_ma.setValue(" ");
				cbo_cal_fz.setValue(" ");
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
				
				//String sqlstat = "SELECT * FROM (SELECT * FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  * FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt ORDER BY Uhrzeit_von, Uhrzeit_bis DESC";

				
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
			
				cal_maid ="leer";
				cal_fzid ="leer";

				
				if(rbt_cal_ma.isSelected()){
				
		    	String maid = cbo_cal_ma.getValue().toString();
		    	int pos_z2 = maid.indexOf(" ");
			    cal_maid = maid.substring(0, pos_z2);
				
				}
				if(rbt_cal_fz.isSelected()){
					
		    	String fzid = cbo_cal_fz.getValue().toString();
		    	int pos_z3 = fzid.indexOf(" ");
			    cal_fzid = fzid.substring(0, pos_z3);
				
				}
				
				System.out.println("MAID:" + cal_maid +"WEITER");
				try { 
					
					
					Statement statement_cal = conn.createStatement();
					
					// Termine für Montag
					cal_where = Mon.toString();
					
					if(rbt_cal_all.isSelected()){
					sqlstat = "SELECT * FROM (SELECT personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "'Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on captain=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on First_Officer=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant1=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant2=myflight.personal.Personal_ID Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID)dt ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
					}
					else if(rbt_cal_ma.isSelected() && !cal_maid.equals("")){
					sqlstat = "SELECT * FROM (SELECT personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on captain=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on First_Officer=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant1=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant2=myflight.personal.Personal_ID)dt Where personal_Personal_ID ="+cal_maid+" ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}
					else if(rbt_cal_ma.isSelected()){
					sqlstat = "SELECT * FROM (SELECT personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on captain=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on First_Officer=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant1=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant2=myflight.personal.Personal_ID)dt  ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
					}
					else if(rbt_cal_fz.isSelected() && !cal_fzid.equals("")){
					sqlstat = "SELECT * FROM (Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID as FZid, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID)dt Where FZid ="+cal_fzid+" ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
					}
					else{
					sqlstat = "SELECT * FROM (Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID as FZid, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID)dt  ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
					}	
					System.out.println(cal_where);
					System.out.println(sqlstat);
			    	ResultSet rs_mon = statement_cal.executeQuery(sqlstat);
			    	while (rs_mon.next())
						{
			    		MA_Art = rs_mon.getString(1)+ " " + rs_mon.getString(12);
			    		datum_von = rs_mon.getString(3);
			    		datum_bis = rs_mon.getString(4);
			    		uhr_von = rs_mon.getString(5);
			    		uhr_bis = rs_mon.getString(6);
			    		MA_Name = rs_mon.getString(8);
			    		MA_Vorname = rs_mon.getString(13) + " " +  rs_mon.getString(9);
						
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
					if(rbt_cal_all.isSelected()){
						sqlstat = "SELECT * FROM (SELECT personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "'Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on captain=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on First_Officer=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant1=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant2=myflight.personal.Personal_ID Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID)dt ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}
						else if(rbt_cal_ma.isSelected() && !cal_maid.equals("")){
						sqlstat = "SELECT * FROM (SELECT personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on captain=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on First_Officer=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant1=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant2=myflight.personal.Personal_ID)dt Where personal_Personal_ID ="+cal_maid+" ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
							}
						else if(rbt_cal_ma.isSelected()){
						sqlstat = "SELECT * FROM (SELECT personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on captain=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on First_Officer=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant1=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant2=myflight.personal.Personal_ID)dt  ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}
						else if(rbt_cal_fz.isSelected() && !cal_fzid.equals("")){
						sqlstat = "SELECT * FROM (Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID as FZid, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID)dt Where FZid ="+cal_fzid+" ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}
						else{
						sqlstat = "SELECT * FROM (Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID as FZid, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID)dt  ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}			
					ResultSet rs_die = statement_cal.executeQuery(sqlstat);
			    	while (rs_die.next())
						{
						MA_Art = rs_die.getString(1)+ " " + rs_die.getString(12);
						datum_von = rs_die.getString(3);
						datum_bis = rs_die.getString(4);
						uhr_von = rs_die.getString(5);
						uhr_bis = rs_die.getString(6);
						MA_Name = rs_die.getString(8);
						MA_Vorname = rs_die.getString(13) + " " +  rs_die.getString(9);
						
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
					if(rbt_cal_all.isSelected()){
						sqlstat = "SELECT * FROM (SELECT personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "'Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on captain=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on First_Officer=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant1=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant2=myflight.personal.Personal_ID Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID)dt ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}
						else if(rbt_cal_ma.isSelected() && !cal_maid.equals("")){
						sqlstat = "SELECT * FROM (SELECT personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on captain=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on First_Officer=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant1=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant2=myflight.personal.Personal_ID)dt Where personal_Personal_ID ="+cal_maid+" ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
							}
						else if(rbt_cal_ma.isSelected()){
						sqlstat = "SELECT * FROM (SELECT personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on captain=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on First_Officer=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant1=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant2=myflight.personal.Personal_ID)dt  ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}
						else if(rbt_cal_fz.isSelected() && !cal_fzid.equals("")){
						sqlstat = "SELECT * FROM (Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID as FZid, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID)dt Where FZid ="+cal_fzid+" ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}
						else{
						sqlstat = "SELECT * FROM (Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID as FZid, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID)dt  ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}	
					ResultSet rs_mit = statement_cal.executeQuery(sqlstat);
			    	while (rs_mit.next())
						{
			    		MA_Art = rs_mit.getString(1)+ " " + rs_mit.getString(12);
			    		datum_von = rs_mit.getString(3);
			    		datum_bis = rs_mit.getString(4);
			    		uhr_von = rs_mit.getString(5);
			    		uhr_bis = rs_mit.getString(6);
			    		MA_Name = rs_mit.getString(8);
			    		MA_Vorname = rs_mit.getString(13) + " " +  rs_mit.getString(9);
						
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
					if(rbt_cal_all.isSelected()){
						sqlstat = "SELECT * FROM (SELECT personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "'Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on captain=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on First_Officer=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant1=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant2=myflight.personal.Personal_ID Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID)dt ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}
						else if(rbt_cal_ma.isSelected() && !cal_maid.equals("")){
						sqlstat = "SELECT * FROM (SELECT personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on captain=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on First_Officer=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant1=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant2=myflight.personal.Personal_ID)dt Where personal_Personal_ID ="+cal_maid+" ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
							}
						else if(rbt_cal_ma.isSelected()){
						sqlstat = "SELECT * FROM (SELECT personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on captain=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on First_Officer=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant1=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant2=myflight.personal.Personal_ID)dt  ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}
						else if(rbt_cal_fz.isSelected() && !cal_fzid.equals("")){
						sqlstat = "SELECT * FROM (Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID as FZid, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID)dt Where FZid ="+cal_fzid+" ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}
						else{
						sqlstat = "SELECT * FROM (Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID as FZid, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID)dt  ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}	
					ResultSet rs_don = statement_cal.executeQuery(sqlstat);
			    	while (rs_don.next())
						{
			    		MA_Art = rs_don.getString(1)+ " " + rs_don.getString(12);
			    		datum_von = rs_don.getString(3);
			    		datum_bis = rs_don.getString(4);
			    		uhr_von = rs_don.getString(5);
			    		uhr_bis = rs_don.getString(6);
			    		MA_Name = rs_don.getString(8);
			    		MA_Vorname = rs_don.getString(13) + " " +  rs_don.getString(9);
						
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
					if(rbt_cal_all.isSelected()){
						sqlstat = "SELECT * FROM (SELECT personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "'Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on captain=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on First_Officer=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant1=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant2=myflight.personal.Personal_ID Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID)dt ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}
						else if(rbt_cal_ma.isSelected() && !cal_maid.equals("")){
						sqlstat = "SELECT * FROM (SELECT personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on captain=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on First_Officer=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant1=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant2=myflight.personal.Personal_ID)dt Where personal_Personal_ID ="+cal_maid+" ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
							}
						else if(rbt_cal_ma.isSelected()){
						sqlstat = "SELECT * FROM (SELECT personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on captain=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on First_Officer=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant1=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant2=myflight.personal.Personal_ID)dt  ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}
						else if(rbt_cal_fz.isSelected() && !cal_fzid.equals("")){
						sqlstat = "SELECT * FROM (Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID as FZid, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID)dt Where FZid ="+cal_fzid+" ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}
						else{
						sqlstat = "SELECT * FROM (Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID as FZid, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID)dt  ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}	
					ResultSet rs_fre = statement_cal.executeQuery(sqlstat);
			    	while (rs_fre.next())
						{
			    		MA_Art = rs_fre.getString(1)+ " " + rs_fre.getString(12);
			    		datum_von = rs_fre.getString(3);
			    		datum_bis = rs_fre.getString(4);
			    		uhr_von = rs_fre.getString(5);
			    		uhr_bis = rs_fre.getString(6);
			    		MA_Name = rs_fre.getString(8);
			    		MA_Vorname = rs_fre.getString(13) + " " +  rs_fre.getString(9);
						
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
					//sqlstat = "SELECT * FROM (SELECT * FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  * FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt ORDER BY Uhrzeit_von, Uhrzeit_bis DESC";
					if(rbt_cal_all.isSelected()){
						sqlstat = "SELECT * FROM (SELECT personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "'Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on captain=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on First_Officer=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant1=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant2=myflight.personal.Personal_ID Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID)dt ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}
						else if(rbt_cal_ma.isSelected() && !cal_maid.equals("")){
						sqlstat = "SELECT * FROM (SELECT personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on captain=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on First_Officer=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant1=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant2=myflight.personal.Personal_ID)dt Where personal_Personal_ID ="+cal_maid+" ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
							}
						else if(rbt_cal_ma.isSelected()){
						sqlstat = "SELECT * FROM (SELECT personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on captain=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on First_Officer=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant1=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant2=myflight.personal.Personal_ID)dt  ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}
						else if(rbt_cal_fz.isSelected() && !cal_fzid.equals("")){
						sqlstat = "SELECT * FROM (Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID as FZid, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID)dt Where FZid ="+cal_fzid+" ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}
						else{
						sqlstat = "SELECT * FROM (Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID as FZid, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID)dt  ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}	
					ResultSet rs_sam = statement_cal.executeQuery(sqlstat);
			    	while (rs_sam.next())
						{
			    		MA_Art = rs_sam.getString(1)+ " " + rs_sam.getString(12);
			    		datum_von = rs_sam.getString(3);
			    		datum_bis = rs_sam.getString(4);
			    		uhr_von = rs_sam.getString(5);
			    		uhr_bis = rs_sam.getString(6);
			    		MA_Name = rs_sam.getString(8);
			    		MA_Vorname = rs_sam.getString(13) + " " +  rs_sam.getString(9);
						
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
					if(rbt_cal_all.isSelected()){
						sqlstat = "SELECT * FROM (SELECT personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "'Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on captain=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on First_Officer=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant1=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant2=myflight.personal.Personal_ID Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID)dt ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}
						else if(rbt_cal_ma.isSelected() && !cal_maid.equals("")){
						sqlstat = "SELECT * FROM (SELECT personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on captain=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on First_Officer=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant1=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant2=myflight.personal.Personal_ID)dt Where personal_Personal_ID ="+cal_maid+" ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
							}
						else if(rbt_cal_ma.isSelected()){
						sqlstat = "SELECT * FROM (SELECT personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_urlaub INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_urlaub.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' UNION SELECT  personal_terminarten_Personal_Terminarten, personal_Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, '' as Angebote_ID,'' as Flugzeug_ID FROM benutzerverwaltung.personal_termine_krankheit INNER JOIN myflight.personal ON benutzerverwaltung.personal_termine_krankheit.personal_Personal_ID=myflight.personal.Personal_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on captain=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on First_Officer=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant1=myflight.personal.Personal_ID Union select personal_terminarten_Personal_Terminarten, Personal_ID, Datum_von, Datum_bis, Uhrzeit_Von, Uhrzeit_Bis, Personal_ID, PersonalName, PersonalVorname, Position_Gehalt_Position, Personalstatus_Personalstatus, Angebote_ID,'' as Flugzeug_ID from (Select * FROM benutzerverwaltung.personal_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.personal on Flight_Attendant2=myflight.personal.Personal_ID)dt  ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}
						else if(rbt_cal_fz.isSelected() && !cal_fzid.equals("")){
						sqlstat = "SELECT * FROM (Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID as FZid, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID)dt Where FZid ="+cal_fzid+" ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}
						else{
						sqlstat = "SELECT * FROM (Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID as FZid, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,'' as Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID)dt join myflight.flugzeugtypen on  Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "' Union Select flugzeug_terminarten_Flugzeug_Terminarten, flugzeuge_Flugzeug_ID, Datum_von, Datum_bis, Uhrzeit_von, Uhrzeit_bis, Flugzeug_ID, FlugzeugHersteller, FlugzeugTyp, Reichweite, KMStand,Angebote_ID ,flugzeuge_Flugzeug_ID from(SELECT * FROM benutzerverwaltung.flugzeug_termine_angebote join myflight.angebote on angebote_Angebote_ID = myflight.angebote.Angebote_ID Where Datum_von <='" + cal_where + "' AND Datum_bis >='" + cal_where + "')dt join myflight.flugzeuge on flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID)dt  ORDER BY Uhrzeit_von, Uhrzeit_bis DESC;";
						}
					
					ResultSet rs_son = statement_cal.executeQuery(sqlstat);
			    	while (rs_son.next())
						{
			    		MA_Art = rs_son.getString(1)+ " " + rs_son.getString(12);
			    		datum_von = rs_son.getString(3);
			    		datum_bis = rs_son.getString(4);
			    		uhr_von = rs_son.getString(5);
			    		uhr_bis = rs_son.getString(6);
			    		MA_Name = rs_son.getString(8);
			    		MA_Vorname = rs_son.getString(13) + " " +  rs_son.getString(9);
						
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
					
					cbo_cal_ma.setValue(" ");
					cbo_cal_fz.setValue(" ");
//					conn.commit();
					
					}
				
				catch(Exception e){
					
					//System.out.println("Excep" + Str_cust_id_chosen);

					System.err.println("Got an exception! "); 
		            System.err.println(e.getMessage()); 
					
				}

			}

	@FXML public void action_get_dashboard () {
		 	
			set_allunvisible(false);
			apa_login.setVisible(false);
			apa_btn_login.setVisible(true);
		    apa_welcome.setVisible(true);
		    lbl_username.setText(user);
		    
		    btn_login.setVisible(false);
		    btn_change_user.setVisible(true);
		    
	}
	@FXML public void action_change_user (ActionEvent event) {
	 	
		set_allunvisible(false);
		apa_btn_login.setVisible(true);
		apa_login.setVisible(true);
		btn_change_user.setVisible(false);
		btn_login.setVisible(true);
		txt_username.setText("");
        pwf_password.setText(""); 
        authenticated = false;			    
        mnudashboard.setDisable(true);
		mnufinanzverwaltung.setDisable(true);
		mnureporting.setDisable(true);
		mnucharter.setDisable(true);
		mnuadministration.setDisable(true);
		btn_costextracostedit.disableProperty().unbind();
		btn_delete_order.disableProperty().unbind();
		btn_createreminder.disableProperty().unbind();
		btncreatebill.disableProperty().unbind();
		btn_costextracostedit.setDisable(true);
		btn_delete_order.setDisable(true);
		btn_createreminder.setDisable(true);
		btncreatebill.setDisable(true);
	}

	@FXML		public void action_save_billstatus() throws SQLException {
		int rechnung_id = Nummerbill.getCellData(billtable.getSelectionModel().getSelectedIndex());
		String billstatuschange = choicebillstatus.getValue().toString();
		Statement stmt = conn.createStatement();
		try {
		stmt.executeUpdate("Update rechnungen set rechnungsstatus_Rechnungsstatus='"+billstatuschange+"' where rechnungen.rechnungen_id='"+rechnung_id+"'");
		lbl_dbconnect.setText("Änderung gespeichert");	
		actiongetrechnungen();
		
	} catch (SQLException sqle) {

		lbl_dbconnect.setText("Datenbankverbindung fehlgeschlagen");
		// System.out.println("geht nicht");
		sqle.printStackTrace();
	}
	}	
	
	@FXML		public void action_save_costtrackingedit() throws SQLException {
		int rechnung_id = Nummercostbill.getCellData(costbilltable.getSelectionModel().getSelectedIndex());
		String costbillstatuschange = choicecostbillstatus.getValue().toString();
		Statement stmt = conn.createStatement();
		try {
		stmt.executeUpdate("Update rechnungen set rechnungsstatus_Rechnungsstatus='"+costbillstatuschange+"' where rechnungen.rechnungen_id='"+rechnung_id+"'");
		lbl_dbconnect.setText("Änderung gespeichert");	
		actiongetcosttrackingoverview();
	} catch (SQLException sqle) {

		lbl_dbconnect.setText("Datenbankverbindung fehlgeschlagen");
		// System.out.println("geht nicht");
		sqle.printStackTrace();
	}
	}	
	
	
	@FXML		public void action_save_costextracostedit() throws SQLException {
		int rechnung_id = Nummercostreminder_warnings_bill.getCellData(costreminder_warnings_billtable.getSelectionModel().getSelectedIndex());
		float tmpneuzusatzkostenextracostedit = Float.parseFloat(zusatzkostenextracostedit.getText());
		float tmppreisbruttoextracostedit = Float.parseFloat(preisbruttoextracostedit.getText())+tmpneuzusatzkostenextracostedit-tmpzusatzkostenextracostedit;
		
		Statement stmt = conn.createStatement();
		try {
		stmt.executeUpdate("Update rechnungen set zusatzkosten_mahnungen='"+tmpneuzusatzkostenextracostedit+"' where rechnungen.rechnungen_id='"+rechnung_id+"'");
		ResultSet rs = stmt.executeQuery("SELECT angebote.angebote_id FROM angebote inner join auftraege INNER JOIN rechnungen on rechnungen.auftraege_auftraege_id=auftraege.auftraege_id and auftraege.angebote_angebote_id = angebote.angebote_id where rechnungen.rechnungen_id = '"+rechnung_id+"'");
		if ((rs != null) && (rs.next())) {
			int angebot_id = rs.getInt(1);
	
		stmt.executeUpdate("Update angebote set Angebotspreis_Brutto='"+tmppreisbruttoextracostedit+"' where angebote.angebote_id='"+angebot_id+"'");
		}
		lbl_dbconnect.setText("Änderung gespeichert");	
		actiongetcosttrackingreminder_warningspgm(true);
	} catch (SQLException sqle) {

		lbl_dbconnect.setText("Datenbankverbindung fehlgeschlagen");
		// System.out.println("geht nicht");
		sqle.printStackTrace();
	}
	}	
//***************************************************************************************************************************************
//***************************************************************************************************************************************
	
	@FXML  public void actiongetpersonaldaten(){
		System.out.println("geklickt");
		actiongetpersonaldatenpgm(false);
	}
	public void actiongetpersonaldatenpgm(boolean showmessage) {
		// lbl_dbconnect.setText("Mouse geklickt!");


		
		set_allunvisible(showmessage);
		scroll_pane_personaldaten.setVisible(true);
		anc_pane_personaldatenübersicht.setVisible(true);
		apa_btn_personaldatenoverview.setVisible(true);
		maskentitel.setVisible(true);
		maskentitel.setText("Übersicht Personaldaten");
	
		try {

			// connect method #1 - embedded driver
			
		    
			
			//String dbURL1 = "jdbc:derby:c:/daten/wirtschaftsinformatik/4. semester/Wirtschaftsinformatikprojekt - Einführung/eigenes Projekt/entwicklung/db/codejava/webdb1;create=true";
			//Connection conn1 = DriverManager.getConnection(dbURL1);
			//if (conn1 != null) {
			//	System.out.println("Connected to database #1");
			//}

			// Statement stmt = conn1.createStatement();
			final String hostname = "172.20.1.24"; 
	        final String port = "3306"; 
	        String dbname = "myflight";
			String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
			Connection conn = DriverManager.getConnection(url, user, password);
		    if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
		    Statement stmt = conn.createStatement();
						
						
			// Aufträge-übersicht abrufen
			ResultSet rs = stmt.executeQuery("select personal.*, position_gehalt.* from personal inner join position_gehalt on personal.Position_Gehalt_Position=position_gehalt.Position");
			
			personaldata.remove(0, personaldata.size());
			int i = 1;
			// Testbeginn
			// rs = null;
			 // Testende
			
			while ((rs != null) && (rs.next())) {
				System.out.println(i++ + " " + rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " "
						+ rs.getString(4) + " " + rs.getString(5) + " " + rs.getInt(7));
				personaldata.add(new Personaldaten(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(7)));
			}
			
			//wenn die Datenbank bei der Entwicklung leer ist
			//angebotedata.add(new Angebote(303043,"22.05.2016","Einzelflug","CORP"));
			
			if (personaldata.size()== 0 ) lbl_dbconnect.setText("keine Personaldaten vorhanden");
						
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
				Str_cust_id_chosen=" ";
				//String test = "init";
				if(zwFH==true){
					
					Str_cust_id_chosen = Integer.toString(newkunden_id);
					zwFH = false;
				}
				else{
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
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");

			    }
					System.out.println("klick geht");
				}
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
					cust_set = true;

		        }
		        
		    }
		    catch(Exception e){
		          e.printStackTrace();
		          System.out.println("Error on Building Data"); 
		          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
		    }
			
	    	System.out.println("ID"+Str_cust_id_chosen);
			if(Str_cust_id_chosen.equals(" ")){
				lbl_dbconnect.setText("Kein Kunde gewählt!");
			}
			
		}
	
	
	//***************************************************************************************************************************************
	//***************************************************************************************************************************************
		
		@FXML  public void actiongetflugzeugdaten(){
			System.out.println("geklickt");
			actiongetflugzeugdatenpgm(false);
		}
		public void actiongetflugzeugdatenpgm(boolean showmessage) {
			// lbl_dbconnect.setText("Mouse geklickt!");

			set_allunvisible(showmessage);
			scroll_pane_flugzeugdaten.setVisible(true);
			anc_pane_flugzeugdatenübersicht.setVisible(true);
			apa_btn_flugzeugdatenoverview.setVisible(true);
			maskentitel.setVisible(true);
			maskentitel.setText("Übersicht Flugzeugdaten");
		
			try {

				// connect method #1 - embedded driver
				
			    
				
				//String dbURL1 = "jdbc:derby:c:/daten/wirtschaftsinformatik/4. semester/Wirtschaftsinformatikprojekt - Einführung/eigenes Projekt/entwicklung/db/codejava/webdb1;create=true";
				//Connection conn1 = DriverManager.getConnection(dbURL1);
				//if (conn1 != null) {
				//	System.out.println("Connected to database #1");
				//}

				// Statement stmt = conn1.createStatement();
				final String hostname = "172.20.1.24"; 
		        final String port = "3306"; 
		        String dbname = "myflight";
				String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
				Connection conn = DriverManager.getConnection(url, user, password);
			    if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
			    Statement stmt = conn.createStatement();
				
				// Aufträge-übersicht abrufen
	
				ResultSet rs = stmt.executeQuery("select flugzeuge.*,flugzeugtypen.* from flugzeuge inner join flugzeugtypen on flugzeuge.flugzeugtypen_flugzeugtypen_id=flugzeugtypen.flugzeugtypen_id");
				
				flugzeugdata.remove(0, flugzeugdata.size());
				int i = 1;
				// Testbeginn
				// rs = null;
				 // Testende
				
				while ((rs != null) && (rs.next())) {
					System.out.println(i++ + " " + rs.getInt(1) + " " + rs.getString(3) + " " + rs.getString(5) + " "
							+ rs.getString(6));
					flugzeugdata.add(new Flugzeugdaten(rs.getInt(1), rs.getString(3), rs.getString(5), rs.getString(6)));
				}
				
				//wenn die Datenbank bei der Entwicklung leer ist
				//angebotedata.add(new Angebote(303043,"22.05.2016","Einzelflug","CORP"));
				
				if (flugzeugdata.size()== 0 ) lbl_dbconnect.setText("keine Flugzeugdaten vorhanden");
							
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


		//***************************************************************************************************************************************
		//***************************************************************************************************************************************
			
			@FXML  public void actiongetflugziele(){
				System.out.println("geklickt");
				actiongetflugzielepgm(false);
			}
			public void actiongetflugzielepgm(boolean showmessage) {
				// lbl_dbconnect.setText("Mouse geklickt!");

				set_allunvisible(showmessage);
				scroll_pane_flugziele.setVisible(true);
				anc_pane_flugzieleübersicht.setVisible(true);
				apa_btn_flugzieleoverview.setVisible(true);
				maskentitel.setVisible(true);
				maskentitel.setText("Übersicht Flugziele");
			
				try {

					// connect method #1 - embedded driver
					
				    
					
					//String dbURL1 = "jdbc:derby:c:/daten/wirtschaftsinformatik/4. semester/Wirtschaftsinformatikprojekt - Einführung/eigenes Projekt/entwicklung/db/codejava/webdb1;create=true";
					//Connection conn1 = DriverManager.getConnection(dbURL1);
					//if (conn1 != null) {
					//	System.out.println("Connected to database #1");
					//}

					// Statement stmt = conn1.createStatement();
					final String hostname = "172.20.1.24"; 
			        final String port = "3306"; 
			        String dbname = "myflight";
					String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
					Connection conn = DriverManager.getConnection(url, user, password);
				    if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
				    Statement stmt = conn.createStatement();
					
					
					// Aufträge-übersicht abrufen
					ResultSet rs = stmt.executeQuery("select * from flughafen_bis");
					
					flugzieledata.remove(0, flugzieledata.size());
					int i = 1;
					// Testbeginn
					// rs = null;
					 // Testende
					
					while ((rs != null) && (rs.next())) {
						System.out.println(i++ + " " + rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " "
								+ rs.getString(4) + " " + rs.getFloat(5) + " " + rs.getFloat(6));
						flugzieledata.add(new Flugziele(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getFloat(5), rs.getFloat(6)));
					}
					
					//wenn die Datenbank bei der Entwicklung leer ist
					//angebotedata.add(new Angebote(303043,"22.05.2016","Einzelflug","CORP"));
					
					if (flugzieledata.size()== 0 ) lbl_dbconnect.setText("keine Flugziele vorhanden");
								
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

			//***************************************************************************************************************************************
			//***************************************************************************************************************************************
				
				@FXML  public void actiongetkundendaten(){
					System.out.println("geklickt");
					actiongetkundendatenpgm(false);
				}
				public void actiongetkundendatenpgm(boolean showmessage) {
					// lbl_dbconnect.setText("Mouse geklickt!");

					if(zwFH == true){
						
						set_allunvisible(false);
						apa_create_offer.setVisible(true);
						apa_btn_createoffer.setVisible(true);
						btn_setcust_click();
									
					}
					else{
					
					set_allunvisible(showmessage);
					scroll_pane_kundendaten.setVisible(true);
					anc_pane_kundendatenübersicht.setVisible(true);
					apa_btn_kundendatenoverview.setVisible(true);
					maskentitel.setVisible(true);
					maskentitel.setText("Übersicht Kundendaten");
				
					try {

						// connect method #1 - embedded driver
						
					    
						
						//String dbURL1 = "jdbc:derby:c:/daten/wirtschaftsinformatik/4. semester/Wirtschaftsinformatikprojekt - Einführung/eigenes Projekt/entwicklung/db/codejava/webdb1;create=true";
						//Connection conn1 = DriverManager.getConnection(dbURL1);
						//if (conn1 != null) {
						//	System.out.println("Connected to database #1");
						//}

						// Statement stmt = conn1.createStatement();
						final String hostname = "172.20.1.24"; 
				        final String port = "3306"; 
				        String dbname = "myflight";
						String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
						conn = DriverManager.getConnection(url, user, password);
						//if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
						
						Statement stmt = conn.createStatement();
						
						// Aufträge-übersicht abrufen
						ResultSet rs = stmt.executeQuery("select * from kunden");
						
						kundendatendata.remove(0, kundendatendata.size());
						int i = 1;
						// Testbeginn
						// rs = null;
						 // Testende
						
						while ((rs != null) && (rs.next())) {
							System.out.println(i++ + " " + rs.getInt(1) + " " + rs.getString(3) + " " + rs.getString(4) + " "
									+ rs.getString(5) + " " + rs.getString(14));
							kundendatendata.add(new Kundendaten(rs.getInt(1), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(14)));
						}
						
						//wenn die Datenbank bei der Entwicklung leer ist
						//angebotedata.add(new Angebote(303043,"22.05.2016","Einzelflug","CORP"));
						
						if (kundendatendata.size()== 0 ) lbl_dbconnect.setText("keine Kundendaten vorhanden");
									
						if (rs != null) rs.close();
						stmt.close();

						// conn1.close();

					} catch (SQLException ex) {
						lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
						ex.printStackTrace();
					}
					
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

//****************************************************************************************************************************
				
@FXML	public void action_editpersonal(ActionEvent event) throws SQLException {

	 // System.out.println(Kdname.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex()));
	set_allunvisible(false); 
	apa_personaledit.setVisible(true);
	apa_btn_personaledit.setVisible(true);
	btn_save_personal.setVisible(true);
	btn_save_personalcreate.setVisible(false);
	maskentitel.setVisible(true);
	maskentitel.setText("Profil ändern");

	//Werte aus ausgewählter Tabellenzeile übernehmen
	
		
	int tmppid = Personal_ID.getCellData(personaltable.getSelectionModel().getSelectedIndex());
	int tmppgehalt = Gehalt.getCellData(personaltable.getSelectionModel().getSelectedIndex());
	
	String tmppname = PersonalName.getCellData(personaltable.getSelectionModel().getSelectedIndex());
	String tmppvname = PersonalVorname.getCellData(personaltable.getSelectionModel().getSelectedIndex());
	String tmppos = Position_Gehalt_Position.getCellData(personaltable.getSelectionModel().getSelectedIndex());
	String tmppstatus = Personalstatus_Personalstatus.getCellData(personaltable.getSelectionModel().getSelectedIndex());
	
	
	

	//Felder für Maske Personaldaten belegen - Beginn

	pid.setText(Integer.toString(tmppid));
	pname.setText(tmppname);
	pvname.setText(tmppvname);
	pstatus.setText(tmppstatus);
	gehalt_string=Integer.toString(tmppgehalt);
	
	String sql = "select * from position_gehalt";
	final String hostname = "172.20.1.24"; 
    final String port = "3306"; 
    String dbname = "myflight";
	String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
	Connection conn = DriverManager.getConnection(url, user, password);
    if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
    Statement stmt = conn.createStatement();
	ResultSet rs = stmt.executeQuery(sql);
	cbo_ppos.getItems().clear();
	cbo_gehalt.getItems().clear();
	while ((rs != null) && (rs.next())) {
		System.out.println(rs.getString(1));
		cbo_ppos.getItems().add(rs.getString(1));
		cbo_gehalt.getItems().add(Integer.toString(rs.getInt(2)));
		}
	
	cbo_ppos.setPromptText(tmppos);
	cbo_gehalt.setPromptText(gehalt_string);
	
  
  
	//Felder für Maske Personaldaten belegen - Ende
	
	// Lizenz & Flugzeugztyp
			sql = "select lizenz.lizenz, lizenz.flugzeugtypen_flugzeugtypen_id from lizenz inner join personal_has_lizenz on lizenz.lizenz = personal_has_lizenz.lizenz_lizenz and personal_has_lizenz.personal_personal_id= '"+tmppid+"'";

			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if ((rs != null) && (rs.next())) {
			lizenz_string = rs.getString(1);
			pflugzeugtyp.setText(rs.getString(2));
			}
			cbo_lizenz.getItems().clear();
			sql = "select * from lizenz";
			rs = stmt.executeQuery(sql);
			while ((rs != null) && (rs.next())) {
				System.out.println(rs.getString(1));
				cbo_lizenz.getItems().add(rs.getString(1));
				}
			
			cbo_lizenz.setPromptText(lizenz_string);
			
	}

//****************************************************************************************************************************

@FXML	public void action_editflugzeug(ActionEvent event) throws SQLException {

	 // System.out.println(Kdname.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex()));
	set_allunvisible(false); 
	apa_flugzeugedit.setVisible(true);
	apa_btn_flugzeugedit.setVisible(true);
	btn_save_flugzeug.setVisible(true);
	btn_save_flugzeugcreate.setVisible(false);
	maskentitel.setVisible(true);
	maskentitel.setText("Flugzeug ändern");

	//Werte aus ausgewählter Tabellenzeile übernehmen
	
	
	int tmpfid = Flugzeug_ID.getCellData(flugzeugtable.getSelectionModel().getSelectedIndex());
	String tmpfstatus = Flugzeugstatus_Flugzeugstatus.getCellData(flugzeugtable.getSelectionModel().getSelectedIndex());
	
	String tmpfname = FlugzeugHersteller.getCellData(flugzeugtable.getSelectionModel().getSelectedIndex());
	String tmpftyp = FlugzeugTyp.getCellData(flugzeugtable.getSelectionModel().getSelectedIndex());

	
	

	//Felder für Maske Flugzeugdaten belegen - Beginn

	fid.setText(Integer.toString(tmpfid));
	fstatus.setText(tmpfstatus);
	fname.setText(tmpfname);
	ftyp.setText(tmpftyp);
	
	
	
 
 
	//Felder für Maske Flugzeugdaten belegen - Ende
	
	// Lizenz & Flugzeugztyp
			String sql = "select flugzeuge.*,flugzeugtypen.* from flugzeuge inner join flugzeugtypen on flugzeuge.flugzeugtypen_flugzeugtypen_id = flugzeugtypen.flugzeugtypen_id and flugzeuge.flugzeug_id = '"+tmpfid+"'";

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if ((rs != null) && (rs.next())) {
			freichw.setText(Integer.toString(rs.getInt(7)));
			fkm.setText(Integer.toString(rs.getInt(8)));
			fpax.setText(Integer.toString(rs.getInt(9)));
			ftrieb.setText(rs.getString(10));
			ftriebanz.setText(Integer.toString(rs.getInt(11)));
			ffixk.setText(Integer.toString(rs.getInt(12)));
			fbetriebk.setText(Integer.toString(rs.getInt(13)));
			fgeschw.setText(Integer.toString(rs.getInt(14)));
			fpilot.setText(Integer.toString(rs.getInt(15)));
			fcopilot.setText(Integer.toString(rs.getInt(16)));
			fcrew.setText(Integer.toString(rs.getInt(17)));
			ftypid.setText(Integer.toString(rs.getInt(2)));
			}
	}

//****************************************************************************************************************************

@FXML	public void action_editflugziele(ActionEvent event) throws SQLException {

	 // System.out.println(Kdname.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex()));
	set_allunvisible(false); 
	apa_flugzieleedit.setVisible(true);
	apa_btn_flugzieleedit.setVisible(true);
	btn_save_flugziele.setVisible(true);
	btn_save_flugzielecreate.setVisible(false);
	maskentitel.setVisible(true);
	maskentitel.setText("Flugziele ändern");

	//Werte aus ausgewählter Tabellenzeile übernehmen
	
		
	String tmpfzflgh = FlughafenKuerzel.getCellData(flugzieletable.getSelectionModel().getSelectedIndex());
	String tmpfzname = FlughafenName.getCellData(flugzieletable.getSelectionModel().getSelectedIndex());
	
	String tmpfzstadt = FlughafenStadt.getCellData(flugzieletable.getSelectionModel().getSelectedIndex());
	String tmpfzland = FlughafenLand.getCellData(flugzieletable.getSelectionModel().getSelectedIndex());
	Float tmpfzlon = FlughafenLon.getCellData(flugzieletable.getSelectionModel().getSelectedIndex());
	Float tmpfzlat = FlughafenLat.getCellData(flugzieletable.getSelectionModel().getSelectedIndex());
	
	

	//Felder für Maske Flugzieledaten belegen - Beginn

	fzflgh.setText(tmpfzflgh);
	fzname.setText(tmpfzname);
	fzstadt.setText(tmpfzstadt);
	fzland.setText(tmpfzland);
	fzlon.setText(Float.toString(tmpfzlon));
	fzlat.setText(Float.toString(tmpfzlat));
	
	

	}


//****************************************************************************************************************************

@FXML	public void action_editkundendaten(ActionEvent event) throws SQLException {

	 // System.out.println(Kdname.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex()));
	set_allunvisible(false); 
	apa_kundendatenedit.setVisible(true);
	apa_btn_kundendatenedit.setVisible(true);
	btn_save_kundendaten.setVisible(true);
	btn_save_kundendatencreate.setVisible(false);
	maskentitel.setVisible(true);
	maskentitel.setText("Kundendaten ändern");

	//Werte aus ausgewählter Tabellenzeile übernehmen
	
		
	int tmpkdid = Kunde_ID.getCellData(kundendatentable.getSelectionModel().getSelectedIndex());
	String tmpkdverwname = KundeName.getCellData(kundendatentable.getSelectionModel().getSelectedIndex());
	
	String tmpkdverwvname = KundeVorname.getCellData(kundendatentable.getSelectionModel().getSelectedIndex());
	String tmpkdfirma = KundeFirmenname.getCellData(kundendatentable.getSelectionModel().getSelectedIndex());
	String tmpkdgruppe = Kundengruppen_Kundengruppen.getCellData(kundendatentable.getSelectionModel().getSelectedIndex());
	
	

	//Felder für Maske Kundendaten belegen - Beginn

	kdid.setText(Integer.toString(tmpkdid));
	kdverwname.setText(tmpkdverwname);
	kdverwvname.setText(tmpkdverwvname);
	kdfirma.setText(tmpkdfirma);
	cbo_kdgruppe.getItems().clear();
	cbo_kdgruppe.getItems().addAll("PRE","CORP","VIP");
/*	String sql = "select * from kundengruppen";
	Statement stmt = conn.createStatement();
	ResultSet rs = stmt.executeQuery(sql);
	while ((rs != null) && (rs.next())) {
		System.out.println(rs.getString(1));
		cbo_kdgruppe.getItems().add(rs.getString(1));
		}
	*/
	cbo_kdgruppe.setPromptText(tmpkdgruppe);
	kdgruppe_string = tmpkdgruppe;
	
	
	
	


	//Felder für Maske Kundendaten belegen - Ende
	
	// Lizenz & Flugzeugztyp
			final String hostname = "172.20.1.24"; 
			final String port = "3306"; 
			String dbname = "myflight";		
			String sql = "select * from kunden where kunden.kunde_id = '"+tmpkdid+"'";
			String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
			Connection conn = DriverManager.getConnection(url, user, password);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if ((rs != null) && (rs.next())) {
			
				txt_street_new.setText(rs.getString(9));
				txt_homeext_new.setText(rs.getString(10));
				txt_place_new.setText(rs.getString(12));
				txt_phone_new.setText(rs.getString(6));
				txt_mobile_new.setText(rs.getString(7));
				txt_mail_new.setText(rs.getString(8));
				txt_postcode_new.setText(rs.getString(11));
				
				txt_country_new.setText(rs.getString(13));
				// cbo_custstatus_new.getValue().toString();
				cbo_salutation_new.getItems().clear();
				cbo_salutation_new.getItems().addAll("Herr","Frau");
				cbo_salutation_new.setPromptText(rs.getString(2));
				anrede =rs.getString(2); 
			}
	}





//****************************************************************************************************************************

@FXML	public void action_createpersonal(ActionEvent event) throws SQLException {

	 // System.out.println(Kdname.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex()));
	set_allunvisible(false); 
	apa_personaledit.setVisible(true);
	apa_btn_personaledit.setVisible(true);
	btn_save_personal.setVisible(false);
	btn_save_personalcreate.setVisible(true);
	maskentitel.setVisible(true);
	maskentitel.setText("Profil anlegen");

	String sql = "select * from position_gehalt";
	final String hostname = "172.20.1.24"; 
    final String port = "3306"; 
    String dbname = "myflight";
	String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
	Connection conn = DriverManager.getConnection(url, user, password);
    if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
    Statement stmt = conn.createStatement();
	
	// ermittle nächste Personal-ID für Speichern eines Mitarbeiters

		sql = "select max(personal_id) from personal";
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		//int newauftraege_id = (rs.getInt(1) / 10000 + 1) * 10000 + 2016;
		newpersonal_id = rs.getInt(1) +1;
		
				
	int tmppid = newpersonal_id;
	int tmppgehalt = 0;
	
	String tmppname = "";
	String tmppvname = "";
	String tmppos = "";
	String tmppstatus = "";
	
	
	

	//Felder für Maske Personaldaten belegen - Beginn

	pid.setText(Integer.toString(tmppid));
	pname.setText(tmppname);
	pvname.setText(tmppvname);
	pstatus.setText(tmppstatus);
	gehalt_string="";
	lizenz_string = "";
	pflugzeugtyp.setText("");

	
	sql = "select * from position_gehalt";
	stmt = conn.createStatement();
	rs = stmt.executeQuery(sql);
	cbo_ppos.getItems().clear();
	cbo_gehalt.getItems().clear();
	while ((rs != null) && (rs.next())) {
		System.out.println(rs.getString(1));
		cbo_ppos.getItems().add(rs.getString(1));
		cbo_gehalt.getItems().add(Integer.toString(rs.getInt(2)));
		}
	
	cbo_ppos.setPromptText(tmppos);
	cbo_lizenz.getItems().clear();
	sql = "select * from lizenz";
	rs = stmt.executeQuery(sql);
	while ((rs != null) && (rs.next())) {
		System.out.println(rs.getString(1));
		cbo_lizenz.getItems().add(rs.getString(1));
		}
	
	cbo_lizenz.setPromptText(lizenz_string);
	cbo_gehalt.setPromptText(gehalt_string);
	
	}

//*****************************************************************************************************************************
@FXML
public void action_save_personaledit(ActionEvent event) throws Exception {
	System.out.println("Update!");
	if (cbo_ppos.getValue() != null && 
			!cbo_ppos.getValue().toString().isEmpty()) {
			position_string = cbo_ppos.getValue().toString();
			}
			if (cbo_lizenz.getValue() != null && 
			!cbo_lizenz.getValue().toString().isEmpty()) {
			lizenz_string = cbo_lizenz.getValue().toString();
			}
			if (cbo_gehalt.getValue() != null && 
					!cbo_gehalt.getValue().toString().isEmpty()) {
					gehalt_string = cbo_gehalt.getValue().toString();
					}
	if (pid.getText().length()==0 || Integer.parseInt(pid.getText())==0 || position_string == "" || pstatus.getText().length()==0) {
	
		lbl_dbconnect.setText("Pflichtfeld(er) füllen");
	}
	else {
		try {


			System.out.println(pid.getText()+pname.getText()+pvname.getText()+position_string+pstatus.getText()+gehalt_string);
			System.out.println(lizenz_string+pflugzeugtyp.getText());
			System.out.println(pstatus.getText().length()==0);	    
			String sql = "select * from position_gehalt";
			final String hostname = "172.20.1.24"; 
		    final String port = "3306"; 
		    String dbname = "myflight";
			String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
			Connection conn = DriverManager.getConnection(url, user, password);
		    if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
		    Statement stmt = conn.createStatement();
			
		
			stmt.executeUpdate("Update personal set "
					+ "personalname = '"+ pname.getText()+"', "
							+ "personalvorname = '"+pvname.getText()+"', "
									+ "position_gehalt_position = '"+position_string+"', "
											+ "personalstatus_personalstatus = '"+pstatus.getText()+"' "
													+ "where personal.personal_id = '"+Integer.parseInt(pid.getText())+"'");
				    
		/*	stmt.executeUpdate("Update personal_has_lizenz set "
					+ "lizenz_lizenz = '"+ plizenz.getText()+"', "
							+ "Lizenz_Flugzeugtypen_flugzeugtypen_id = '"+pflugzeugtyp.getText()+"' "
									+ "where personal_has_lizenz.personal_personal_id = '"+Integer.parseInt(pid.getText())+"'");
			*/	    
			
			
			lbl_dbconnect.setText("Personaldaten gespeichert");
			actiongetpersonaldatenpgm(true);
		} catch (SQLException sqle) {

			lbl_dbconnect.setText("Ungültige(r) Wert(e) erfasst");
			// System.out.println("geht nicht");
			sqle.printStackTrace();
		}
		}
	
}

//****************************************************************************************************************************

//*****************************************************************************************************************************
@FXML
public void action_save_kundendatenedit(ActionEvent event) throws Exception {
	System.out.println("Update!");
	
	
	
	
	
	if (kdid.getText().length()==0 || Integer.parseInt(kdid.getText())==0 ) {
	
		lbl_dbconnect.setText("Pflichtfeld(er) füllen");
	}
	else {
		try {

			  if (cbo_salutation_new.getValue() != null && 
	                    !cbo_salutation_new.getValue().toString().isEmpty()) {
				  anrede = cbo_salutation_new.getValue().toString();
			  }
			  if (cbo_kdgruppe.getValue() != null && 
	                    !cbo_kdgruppe.getValue().toString().isEmpty()) {
				  kdgruppe_string = cbo_kdgruppe.getValue().toString();
			  }	
			  
			System.out.println(anrede);		
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("Update kunden set "
					+ "KundeName = '"+ kdverwname.getText()+"', "
					  + "KundeAnrede = '"+anrede+"', "
							+ "KundeVorname = '"+kdverwvname.getText()+"', "
									+ "KundeFirmenname = '"+kdfirma.getText()+"', "
											+ "KundeAdresse1 = '"+txt_street_new.getText()+"', "
											+ "KundeAdresse2 = '"+txt_homeext_new.getText()+"', "
											+ "Kundengruppen_Kundengruppen = '"+kdgruppe_string+"', "
											+ "KundeTelefon ='"+txt_phone_new.getText()+"', "
											+ "KundeHandy ='"+ txt_mobile_new.getText()+"', "
													+ "KundeEmail ='"+ txt_mail_new.getText()+"', "
															+ "KundePLZ ='"+ txt_postcode_new.getText()+"', "
																	+ "KundenOrt ='"+ txt_place_new.getText()+"' "
																			+ "where kunden.kunde_id = '"+Integer.parseInt(kdid.getText())+"'");
				    
		
			
			lbl_dbconnect.setText("Kundendaten gespeichert");
			actiongetkundendatenpgm(true);
		} catch (SQLException sqle) {

			lbl_dbconnect.setText("Ungültige(r) Wert(e) erfasst");
			// System.out.println("geht nicht");
			sqle.printStackTrace();
		}
		}
	
}


//************************************************************************************************************************

@FXML	public void action_createflugzeug(ActionEvent event) throws SQLException {

	 // System.out.println(Kdname.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex()));
	set_allunvisible(false); 
	apa_flugzeugedit.setVisible(true);
	apa_btn_flugzeugedit.setVisible(true);
	btn_save_flugzeug.setVisible(false);
	btn_save_flugzeugcreate.setVisible(true);
	maskentitel.setVisible(true);
	maskentitel.setText("Flugzeug anlegen");


	Statement stmt = conn.createStatement();
	// ermittle nächste Personal-ID für Speichern eines Mitarbeiters

		String sql = "select max(flugzeug_id) from flugzeuge";
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		//int newauftraege_id = (rs.getInt(1) / 10000 + 1) * 10000 + 2016;
		int newflugzeug_id = rs.getInt(1) +1;
		
		
		int tmpfid = newflugzeug_id;
		String tmpfstatus = "";
		
		String tmpfname = "";
		String tmpftyp = "";

		
		

		//Felder für Maske Flugzeugdaten belegen - Beginn

		fid.setText(Integer.toString(tmpfid));
		fstatus.setText(tmpfstatus);
		fname.setText(tmpfname);
		ftyp.setText(tmpftyp);
		
		
		
	 
	 
		//Felder für Maske Flugzeugdaten belegen - Ende
		
		// Lizenz & Flugzeugztyp
				freichw.setText("");
				fkm.setText("");
				fpax.setText("");
				ftrieb.setText("");
				ftriebanz.setText("");
				ffixk.setText("");
				fbetriebk.setText("");
				fgeschw.setText("");
				fpilot.setText("");
				fcopilot.setText("");
				fcrew.setText("");
				
				
		
		
		
		
		

	
	}

//****************************************************************************************************************************************
@FXML
public void action_save_flugzeugedit(ActionEvent event) throws Exception {
	System.out.println("Update!");
	if (fid.getText().length()==0 || Integer.parseInt(fid.getText())==0 || ftypid.getText().length()==0 || Integer.parseInt(ftypid.getText())==0 || fstatus.getText().length()==0 ) {
	
		lbl_dbconnect.setText("Pflichtfeld(er) füllen");
	}
	else {
		try {


			Statement stmt = conn.createStatement();
			stmt.executeUpdate("Update flugzeuge set "
					+ "Flugzeug_ID = '"+ fid.getText()+"', "
					+"Flugzeugtypen_Flugzeugtypen_ID = '"+Integer.parseInt(ftypid.getText())+"', "
									+ "Flugzeugstatus_Flugzeugstatus = '"+fstatus.getText()+"' "
											+ "where flugzeuge.flugzeug_id = '"+Integer.parseInt(fid.getText())+"'");
				    
	/*		stmt.executeUpdate("Update flugzeugtypen set "
					+ "Flugzeugtypen_ID = '"+ plizenz.getText()+"', "
							+ "Lizenz_Flugzeugtypen_flugzeugtypen_id = '"+pflugzeugtyp.getText()+"' "
									+ "where flugzeugtypen.flugzeugtypen_id = '"+Integer.parseInt(pid.getText())+"'");
		*/		    
			
			
			lbl_dbconnect.setText("Flugzeugdaten geändert");
			actiongetflugzeugdatenpgm(true);
		} catch (SQLException sqle) {

			lbl_dbconnect.setText("Ungültige(r) Wert(e) erfasst");
			// System.out.println("geht nicht");
			sqle.printStackTrace();
		}
		}
	
}

//****************************************************************************************************************************************
@FXML
public void action_save_flugzieleedit(ActionEvent event) throws Exception {
	System.out.println("Update!");
	
		
	
	if (fzflgh.getText().length()==0) {
	
		lbl_dbconnect.setText("Pflichtfeld(er) füllen");
	}
	else {
		try {


			Statement stmt = conn.createStatement();
			stmt.executeUpdate("Update flughafen_bis set "
					+ "FlughafenKuerzel = '"+ fzflgh.getText()+"', "
					+"FlughafenName = '"+fzname.getText()+"', "
									+ "FlughafenStadt = '"+fzstadt.getText()+"', "
									+ "FlughafenLand = '"+fzland.getText()+"', "
									+ "FlughafenLon = '"+Float.parseFloat(fzlon.getText())+"', "
									+ "FlughafenLat = '"+Float.parseFloat(fzlat.getText())+"' "
											+ "where flughafen_bis.flughafenKuerzel = '"+fzflgh.getText()+"'");
			
			
			stmt.executeUpdate("Update flughafen_von set "
					+ "FlughafenKuerzel = '"+ fzflgh.getText()+"', "
					+"FlughafenName = '"+fzname.getText()+"', "
									+ "FlughafenStadt = '"+fzstadt.getText()+"', "
									+ "FlughafenLand = '"+fzland.getText()+"', "
									+ "FlughafenLon = '"+Float.parseFloat(fzlon.getText())+"', "
									+ "FlughafenLat = '"+Float.parseFloat(fzlat.getText())+"' "
											+ "where flughafen_von.flughafenKuerzel = '"+fzflgh.getText()+"'");
	
			
			/*		stmt.executeUpdate("Update flugzeugtypen set "
					+ "Flugzeugtypen_ID = '"+ plizenz.getText()+"', "
							+ "Lizenz_Flugzeugtypen_flugzeugtypen_id = '"+pflugzeugtyp.getText()+"' "
									+ "where flugzeugtypen.flugzeugtypen_id = '"+Integer.parseInt(pid.getText())+"'");
		*/		    
			
			
			lbl_dbconnect.setText("Flugziele geändert");
			actiongetflugzielepgm(true);
		} catch (SQLException sqle) {

			lbl_dbconnect.setText("Ungültige(r) Wert(e) erfasst");
			// System.out.println("geht nicht");
			sqle.printStackTrace();
		}
		}
	
}




//****************************************************************************************************************************

@FXML	public void action_createflugziele(ActionEvent event) throws SQLException {

	 // System.out.println(Kdname.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex()));
	set_allunvisible(false); 
	apa_flugzieleedit.setVisible(true);
	apa_btn_flugzieleedit.setVisible(true);
	btn_save_flugziele.setVisible(false);
	btn_save_flugzielecreate.setVisible(true);
	maskentitel.setVisible(true);
	maskentitel.setText("Flugziele anlegen");


	String tmpfzflgh = "";
	String tmpfzname = "";
	
	String tmpfzstadt = "";
	String tmpfzland = "";
	Float tmpfzlon = 0.F;
	Float tmpfzlat = 0.F;
	
	

	//Felder für Maske Flugzieledaten belegen - Beginn

	fzflgh.setText(tmpfzflgh);
	fzname.setText(tmpfzname);
	fzstadt.setText(tmpfzstadt);
	fzland.setText(tmpfzland);
	fzlon.setText(Float.toString(tmpfzlon));
	fzlat.setText(Float.toString(tmpfzlat));
	
					
			}


//****************************************************************************************************************************

@FXML	public void action_createkundendaten() throws SQLException {

	 // System.out.println(Kdname.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex()));
	set_allunvisible(false); 
	apa_kundendatenedit.setVisible(true);
	apa_btn_kundendatenedit.setVisible(true);
	btn_save_kundendaten.setVisible(false);
	btn_save_kundendatencreate.setVisible(true);
	maskentitel.setVisible(true);
	maskentitel.setText("Kundendaten anlegen");


	Statement stmt = conn.createStatement();
	// ermittle nächste Personal-ID für Speichern eines Mitarbeiters

		String sql = "select max(kunde_id) from kunden";
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		//int newauftraege_id = (rs.getInt(1) / 10000 + 1) * 10000 + 2016;
		newkunden_id = rs.getInt(1) +1;
		
		int tmpkdid = newkunden_id;
		String tmpkdverwname = "";
		String tmpkdverwvname = "";
		String tmpkdfirma = "";
		String tmpkdgruppe = "";
		

		kdid.setText(Integer.toString(tmpkdid));
		kdverwname.setText(tmpkdverwname);
		kdverwvname.setText(tmpkdverwvname);
		kdfirma.setText(tmpkdfirma);
		cbo_kdgruppe.getItems().clear();
		cbo_kdgruppe.getItems().addAll("PRE","CORP","VIP");
	/*	String sql = "select * from kundengruppen";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		while ((rs != null) && (rs.next())) {
			System.out.println(rs.getString(1));
			cbo_kdgruppe.getItems().add(rs.getString(1));
			}
		*/
		cbo_kdgruppe.setPromptText(tmpkdgruppe);
		kdgruppe_string = tmpkdgruppe;
		
		
		
		//Felder für Maske Kundendaten belegen - Ende
						
					txt_street_new.setText("");
					txt_homeext_new.setText("");
					txt_place_new.setText("");
					txt_phone_new.setText("");
					txt_mobile_new.setText("");
					txt_mail_new.setText("");
					txt_postcode_new.setText("");
					cbo_salutation_new.getItems().clear();
					cbo_salutation_new.getItems().addAll("Herr","Frau");
					
					//cbo_country_new.getValue().toString();
					// cbo_custstatus_new.getValue().toString();

	
	}


//****************************************************************************************************************************
@FXML
public void action_save_personalcreate(ActionEvent event) throws Exception {
System.out.println("Neuanlage!");
if (cbo_ppos.getValue() != null && 
!cbo_ppos.getValue().toString().isEmpty()) {
position_string = cbo_ppos.getValue().toString();
}
if (cbo_lizenz.getValue() != null && 
!cbo_lizenz.getValue().toString().isEmpty()) {
lizenz_string = cbo_lizenz.getValue().toString();
}
if (cbo_gehalt.getValue() != null && 
!cbo_gehalt.getValue().toString().isEmpty()) {
gehalt_string = cbo_gehalt.getValue().toString();
}
	if (pid.getText().length()==0 || Integer.parseInt(pid.getText())==0 || position_string == "" || pstatus.getText().length()==0) {
	
		lbl_dbconnect.setText("Pflichtfeld(er) füllen");
	}
	else {
		try {


			System.out.println(pid.getText()+pname.getText()+pvname.getText()+position_string+pstatus.getText()+gehalt_string);
			System.out.println(lizenz_string+pflugzeugtyp.getText());
			System.out.println(pstatus.getText().length()==0);	    
			
			String sql = "select * from position_gehalt";
			final String hostname = "172.20.1.24"; 
		    final String port = "3306"; 
		    String dbname = "myflight";
			String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
			Connection conn = DriverManager.getConnection(url, user, password);
		    if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
		    Statement stmt = conn.createStatement();
			
			stmt.executeUpdate("INSERT INTO personal (Personal_ID, PersonalName, PersonalVorname,Position_Gehalt_Position, Personalstatus_Personalstatus) VALUES ("+Integer.parseInt(pid.getText())+",'"+pname.getText()+"', '"+pvname.getText()+"', '"+position_string+"', '"+pstatus.getText()+"')");
			
			stmt.executeUpdate("INSERT INTO personal_has_lizenz (Personal_Personal_ID, Personal_Position_Gehalt_Position,Lizenz_Lizenz,Lizenz_Flugzeugtypen_Flugzeugtypen_ID) VALUES ("+Integer.parseInt(pid.getText())+", '"+position_string+"', '"+lizenz_string+"','"+pflugzeugtyp.getText()+"')");
			
			
			lbl_dbconnect.setText("Personaldaten gespeichert");
			actiongetpersonaldatenpgm(true);
		} catch (SQLException sqle) {

			lbl_dbconnect.setText("Ungültige(r) Wert(e) erfasst");
			// System.out.println("geht nicht");
			sqle.printStackTrace();
		}
		}
	
}
	


//****************************************************************************************************************************
@FXML
public void action_save_flugzeugcreate(ActionEvent event) throws Exception {
System.out.println("Neuanlage!");
if (fid.getText().length()==0 || Integer.parseInt(fid.getText())==0 || ftypid.getText().length()==0 || Integer.parseInt(ftypid.getText())==0 || fstatus.getText().length()==0 ) {
	
	lbl_dbconnect.setText("Pflichtfeld(er) füllen");
}
	else {
		try {

		
			Statement stmt = conn.createStatement();

			stmt.executeUpdate("INSERT INTO flugzeuge (Flugzeug_ID,Flugzeugtypen_Flugzeugtypen_ID,Flugzeugstatus_Flugzeugstatus) Values ('"+ fid.getText()+"', '"+Integer.parseInt(ftypid.getText())+"', '"+fstatus.getText()+"')");
			
			lbl_dbconnect.setText("Flugzeugdaten gespeichert");
			actiongetflugzeugdatenpgm(true);
		} catch (SQLException sqle) {

			lbl_dbconnect.setText("Ungültige(r) Wert(e) erfasst");
			// System.out.println("geht nicht");
			sqle.printStackTrace();
		}
		}
	
}


//****************************************************************************************************************************
@FXML
public void action_save_flugzielecreate(ActionEvent event) throws Exception {
System.out.println("Neuanlage!");
if (fzflgh.getText().length()==0) {
	
	lbl_dbconnect.setText("Pflichtfeld(er) füllen");
}
	else {
		try {

		
			Statement stmt = conn.createStatement();

			stmt.executeUpdate("INSERT INTO flughafen_bis (FlughafenKuerzel,FlughafenName,FlughafenStadt ,FlughafenLand,FlughafenLon,FlughafenLat) values('"+ fzflgh.getText()+"', '"+fzname.getText()+"', '"+fzstadt.getText()+"','"+fzland.getText()+"', '"+Float.parseFloat(fzlon.getText())+"', '"+Float.parseFloat(fzlat.getText())+"')");
			stmt.executeUpdate("INSERT INTO flughafen_von (FlughafenKuerzel,FlughafenName,FlughafenStadt ,FlughafenLand,FlughafenLon,FlughafenLat) values('"+ fzflgh.getText()+"', '"+fzname.getText()+"', '"+fzstadt.getText()+"','"+fzland.getText()+"', '"+Float.parseFloat(fzlon.getText())+"', '"+Float.parseFloat(fzlat.getText())+"')");
			
			lbl_dbconnect.setText("Flugziele gespeichert");
			actiongetflugzielepgm(true);
		} catch (SQLException sqle) {

			lbl_dbconnect.setText("Ungültige(r) Wert(e) erfasst");
			// System.out.println("geht nicht");
			sqle.printStackTrace();
		}
		}
	
}

//****************************************************************************************************************************
@FXML
public void action_save_kundendatencreate(ActionEvent event) throws Exception {
System.out.println("Neuanlage!");
if (cbo_kdgruppe.getValue() != null && 
!cbo_kdgruppe.getValue().toString().isEmpty()) {
kdgruppe_string = cbo_kdgruppe.getValue().toString();
}	
if (cbo_salutation_new.getValue() != null && 
!cbo_salutation_new.getValue().toString().isEmpty()) {
anrede = cbo_salutation_new.getValue().toString();
}

if (kdid.getText().length()==0 || Integer.parseInt(kdid.getText())==0 || kdgruppe_string=="") {
	
	lbl_dbconnect.setText("Pflichtfeld(er) füllen");
}
	else {
		try {

			final String hostname = "172.20.1.24"; 
		    final String port = "3306"; 
		    String dbname = "myflight";
			
			String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
			Connection conn = DriverManager.getConnection(url, user, password);
			Statement stmt = conn.createStatement();
					
			stmt.executeUpdate("INSERT INTO kunden (Kunde_ID,KundeAnrede,KundenLand,KundeName,KundeVorname,KundeFirmenname,KundeAdresse1,KundeAdresse2 ,Kundengruppen_Kundengruppen,KundeTelefon,KundeHandy,KundeEmail,KundePLZ,KundenOrt) values('"+Integer.parseInt(kdid.getText())+"', '"+ anrede+"', '"+txt_country_new.getText()+"', '"+ kdverwname.getText()+"', '"+kdverwvname.getText()+"', '"+kdfirma.getText()+"','"+txt_street_new.getText()+"', '"+txt_homeext_new.getText()+"', '"+kdgruppe_string+"', '"+txt_phone_new.getText()+"', '"+txt_mobile_new.getText()+"', '"+txt_mail_new.getText()+"', '"+txt_postcode_new.getText()+"', '"+txt_place_new.getText()+"')");
		
			
			lbl_dbconnect.setText("Kundendaten gespeichert");
			actiongetkundendatenpgm(true);
		} catch (SQLException sqle) {

			lbl_dbconnect.setText("Ungültige(r) Wert(e) erfasst");
			sqle.printStackTrace();
		}
		}
	
}

			@FXML public void btn_zw_click() {
				
				set_allunvisible(false);
				System.out.println("ZW klickt");
				apa_zws_new.setVisible(true);
				apa_btn_zws.setVisible(true);

				dpi_zws_an.setDisable(true);
				txt_zwsan_h.setEditable(false);
				txt_zwsan_m.setEditable(false);
				btn_zwscount.setDisable(false);

			}

			@FXML public void btn_sw_click() {
	
				
				getEntfernung();
				Start_offer = dpi_startdat.getValue();
				Ziel_offer = dpi_zieldat.getValue();

				if(txt_pass.getText().equals("")){lbl_dbconnect.setText("Passagierinformation Notwendig! ");}
				else{
					
					set_allunvisible(false);
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
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
				{ lbl_dbconnect.setText("Bitte min. 1 Feld ausfüllen");}
	
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
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
				
				if(data.equals("")){
					lbl_dbconnect.setText("Bitte eine Zeile wählen!");
				}
				
				if (StartFH == true){

					txt_startfh.setText(data);
	
				}

				if (ZielFH == true){

					txt_zielfh.setText(data);	
				}
				if (zwFH == true){

					txt_fh_zws.setText(data);	
				}
				
				
				if (zwFH == true){

					FHData.remove(0, FHData.size());
					zwFH = false;
					
					
					fromzw = true;
					
					if(arrayzw == 0){
		    	
			    	zwstartfh = txt_startfh.getText();
					zwzielfh = data;
					Str_startzeith = txt_startzeit_h.getText();
			    	Str_startzeitm = txt_startzeit_m.getText();
			    	startdate = dpi_startdat.getValue();
			    	
			    	
					}
//					else if(arrayzw==countzw-1){
//						
//						zwstartfh = FHzw[arrayzw];
//						zwzielfh = txt_zielfh.getText();
//						Str_startzeith = zw_ab_h[arrayzw-1];
//				    	Str_startzeitm = zw_ab_m[arrayzw-1];
//				    	startdate = zw_ab[arrayzw-1];
//					}
					else{
					
					zwstartfh = FHzw[arrayzw-1];
					zwzielfh = data;
					Str_startzeith = zw_ab_h[arrayzw-1];
			    	Str_startzeitm = zw_ab_m[arrayzw-1];
			    	startdate = zw_ab[arrayzw-1];
					}
				
					
					getEntfernung();
					
					
					//Str_zielzeith = txt_startzeit_h.getText();
			    	//Str_zielzeitm = txt_startzeit_m.getText();
			    	zieldate = startdate;
			    	
			    	if(entfernung < 4000){speed=600;}
			    	if(entfernung > 4000){speed=750;}
					
					double dauer = (entfernung/speed)*60;
			    	int idauer = Double.valueOf(dauer).intValue();
			    	
			    	double szh = Double.parseDouble(Str_startzeith);
			    	double szm = Double.parseDouble(Str_startzeitm);
			    	
			    	double szg = (szh * 60)+szm;
			    	szg = szg + dauer;
			    	double tage = 1440-szg;
			    				    	
			    	System.out.println(zieldate);
			    	if(tage<0){
			    		
			    		System.out.println("ist drin");
			    		zieldate = startdate.plusDays(1);
			    	}
			    	if(tage < -1440){
			    		
			    		
			    		zieldate = startdate.plusDays(2);
			    		
			    	}
			    	
			    	startzeit = LocalTime.parse(Str_startzeith+":"+Str_startzeitm+":00");
			    	zielzeit = startzeit.plusMinutes(idauer);
			    	
			    	String data6 = zielzeit.toString();
			    	int pos_z2 = data6.indexOf(":");
				    String zwanh  = data6.substring(0, pos_z2);
			    	String zwanm = data6.substring(pos_z2+1,5);
			    	
	//		    	if(arrayzw==0){
						
			    		txt_zwsan_h.setText(zwanh);
			    		txt_zwsan_m.setText(zwanm);
			    		dpi_zws_an.setValue(zieldate);
				    	zw_an_h[arrayzw] = zwanh;
					    zw_an_m[arrayzw] = zwanm;
					    zw_an[arrayzw] = zieldate;
					    zw_dauer[arrayzw] = dauer;
//					 }
//			    	
//			    	else if(arrayzw==countzw-1){
//						
//						
//			    		txt_zwsan_h.setText(zwanh);
//			    		txt_zwsan_m.setText(zwanm);
//			    		dpi_zws_an.setValue(zieldate);
//				    	zw_an_h[arrayzw] = zwanh;
//					    zw_an_m[arrayzw] = zwanm;
//					    zw_an[arrayzw] = zieldate;
//					}
//			    	else{
//			    	txt_zwsan_h.setText(zwanh);
//			    	txt_zwsan_m.setText(zwanm);
//			    	dpi_zws_an.setValue(zieldate);
//			    	zw_an_h[arrayzw+1] = zwanh;
//				    zw_an_m[arrayzw+1] = zwanm;
//				    zw_an[arrayzw+1] = zieldate;
//			    	}
			    	
					
						
					set_allunvisible(false);
					apa_zws_new.setVisible(true);
					apa_btn_zws.setVisible(true);
					
				}
				else{
				StartFH = false;
				ZielFH = false;
				
				FHData.remove(0, FHData.size());
				txt_iata_search.clear();
				txt_stadt_search.clear();
				

				
				set_allunvisible(false);
				apa_create_offer.setVisible(true);
				apa_btn_createoffer.setVisible(true);
				}
			}
			
			@FXML public void btn_close_fh_click() {
				
				if (zwFH == true){
					set_allunvisible(false);
					apa_zws_new.setVisible(true);
					FHData.remove(0, FHData.size());
					zwFH = false;
				}
				else{
				StartFH = false;
				ZielFH = false;
				set_allunvisible(false);
				apa_create_offer.setVisible(true);
				apa_btn_createoffer.setVisible(true);
				}
			}

			@FXML public void btn_startfh_click() {
				
				StartFH = true;
				set_allunvisible(false);
				apa_search_fh.setVisible(true);
			}

			@FXML public void btn_zielfh_click() {
				
				ZielFH = true;
				set_allunvisible(false);
				apa_search_fh.setVisible(true); 
			}

			@FXML public void btn_create_offer() {//TODO 
				
				if(cust_set==false || txt_startfh.getText().equals("") && !charterart.equals("Zeitcharter")|| txt_zielfh.getText().equals("") && !charterart.equals("Zeitcharter") || dpi_startdat.getValue().toString().equals("") || txt_startzeit_h.getText().equals("") ||txt_startzeit_m.getText().equals("") || txt_pass.getText().equals(""))              
				{
					lbl_dbconnect.setText("Bitte Pflichtfelder ausfüllen!");
				}
				
				else if(txt_startfh.getText().equals(txt_zielfh.getText()) && !charterart.equals("Zeitcharter")){
					lbl_dbconnect.setText("Start- und Zielflughafen gleich");
				}
				else if(offer_new == false){
					lbl_dbconnect.setText("Angebot wurde bereits erstellt. Bitte neu erstellen!");
				}
				else if(dpi_startdat.getValue().isBefore(AngDatum)){
					lbl_dbconnect.setText("Startdatum ist in der Vergangenheit");
				}
				else if (!txt_startzeit_h.getText().matches("[0-9]*") || txt_startzeit_h.getText().equals("")|| !txt_zielzeit_h.getText().matches("[0-9]*")|| !txt_startzeit_m.getText().matches("[0-9]*") || txt_startzeit_m.getText().equals("")|| !txt_zielzeit_m.getText().matches("[0-9]*")){
				
					 lbl_dbconnect.setText("ungültige(r) Wert(e)!");
					
				}
				else if(charterart.equals("Zeitcharter") && txt_zielzeit_h.getText().equals("")||charterart.equals("Zeitcharter") && txt_zielzeit_m.getText().equals("")){
					lbl_dbconnect.setText("Bitte Pflichtfelder ausfüllen!");
				}
				else{
				
				// a
				
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
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
		    	
		    	
		    	if(charterart.equals("Flug mit Zwischenstationen")){
					
		    		
			String	Str_StartFH_zw = txt_startfh.getText();
			String	Str_ZielFH_zw = txt_zielfh.getText();
		    		
		    		hochentf = new float[countzw+1];
			
		    		for (int i=0;i<=countzw;i++){
		    			
		    			if(i==0){
		    				
		    				txt_zielfh.setText(FHzw[i]);
		    				
		    			}
		    			else if(i==(countzw)){
		    				
		    				txt_startfh.setText(FHzw[i-1]);
		    				txt_zielfh.setText(Str_ZielFH_zw);
		    				
		    			}
		    			else{
		    				
		    				txt_startfh.setText(FHzw[i-1]);
		    				txt_zielfh.setText(FHzw[i]);
		    				
		    			}
		    			getEntfernung();
		    			hochentf[i] = entfernung;
		    			if(i != 0){
		    			for(int z=0;z<hochentf.length;z++){
		    				
		    				if(hochentf[i]>hochentf[i-1]){
		    					
		    					float abl = 0;
		    					abl = hochentf[i-1] ;		    					
		    					hochentf[i-1]=hochentf[i];
		    					hochentf[i] = abl;
		    				}
		    				
		    				}
		    			}		    			
		    			
				    	
				    	
		    			entfernung_zw = entfernung_zw + entfernung;
		    			
		    			if(StartKont.equals("Amerika") || ZielKont.equals("Amerika") && StartKont_zw.equals("Europa")){
		    				
		    				ZielKont_zw = "Amerika";
		    				
		    			}
		    	}
					
		    		txt_startfh.setText(Str_StartFH_zw);
		    		txt_zielfh.setText(Str_ZielFH_zw);
					StartKont = StartKont_zw;
					ZielKont = ZielKont_zw;
					
					
//					zw_an_h[arrayzw] = txt_zwsan_h.getText();
//					zw_an_m[arrayzw] = txt_zwsan_m.getText();
//					zw_ab_h[arrayzw] = txt_zwsab_h.getText();
//					zw_ab_m[arrayzw] = txt_zwsab_m.getText();
//					zw_an[arrayzw] = dpi_zws_an.getValue();
//					zw_ab[arrayzw] = dpi_zws_ab.getValue();
					
					
					
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
			    			    	
			    	
			    	
				}
				

		    	else if(charterart.equals("Zeitcharter")){
					
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
				    	ResultSet rs = statement.executeQuery("Select distinct(Flugzeug_ID), FlugzeugHersteller, FlugzeugTyp, AnzahlPassagiere,Geschwindigkeit, Reichweite, Fixkosten, Betriebskosten from myflight.flugzeuge Join myflight.Flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID Where FlugzeugHersteller ='" + FZHersteller +"' AND FlugzeugTyp ='" + FZTyp +"'  AND Flugzeug_ID not in(SELECT DISTINCT Flugzeug_ID FROM(SELECT rep.flugzeuge_Flugzeug_ID, rep.Datum_von, rep.Datum_bis FROM benutzerverwaltung.flugzeug_termine_reparatur rep UNION SELECT ang.flugzeuge_Flugzeug_ID, ang.Datum_von, ang.Datum_bis FROM benutzerverwaltung.flugzeug_termine_angebote INNER JOIN myflight.angebote ang ON benutzerverwaltung.flugzeug_termine_angebote.angebote_Angebote_ID=ang.Angebote_ID UNION SELECT war.flugzeuge_Flugzeug_ID, war.Datum_von, war.Datum_bis FROM benutzerverwaltung.flugzeug_termine_wartung war )dt INNER JOIN myflight.flugzeuge ON flugzeuge_Flugzeug_ID=myflight.flugzeuge.Flugzeug_ID INNER JOIN myflight.flugzeugtypen ON Flugzeugtypen_Flugzeugtypen_ID=myflight.flugzeugtypen.Flugzeugtypen_ID WHERE Datum_von between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_bis between '" + Start_offer + "' AND '" + Ziel_offer + "' OR Datum_von < '"+Start_offer+"' and Datum_bis > '"+Ziel_offer+"' ORDER BY Flugzeug_ID asc)");      
				   while((rs != null) && (rs.next())){
				        	
					   System.out.println("HS: " + FZHersteller);
					    
					    System.out.println("TYP: " + FZTyp);
				        	//cbo_fz.setValue(rs.getString(3));
				        	bestFZ = rs.getInt(1);
				        	System.out.println("FZ " + bestFZ);
				        	speed = rs.getInt(5);
				        	reichweite = rs.getInt(6);
				        	FixkostenFZ =rs.getInt(7);
				        	BetriebskFZ = rs.getInt(8);

				        }
				        
				    }
				    catch(Exception e){
				          e.printStackTrace();
				          System.out.println("Error on Building Data"); 
				          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
					          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
						          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
						          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");           
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
						          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
						          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
						    }
					    	
					    }
					
					
				}
				else{
					
					if(charterart.equals("Flug mit Zwischenstationen")){
						entfernung = hochentf[0];
						Start_offer = dpi_startdat.getValue();
						Ziel_offer = dpi_zieldat.getValue();
						
					}

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
				          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
			    	
			    	String data6 = zielzeit.toString();
			    	int pos_z2 = data6.indexOf(":");
				    String anh  = data6.substring(0, pos_z2);
			    	String anm = data6.substring(pos_z2+1,5);
			    	
			    	txt_zielzeit_h.setText(anh);
			    	txt_zielzeit_m.setText(anm);
			    	dpi_zieldat.setValue(zieldate);
			    	
			
			    	
			    	
			    	
			    	
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
				          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
				
				if(charterart.equals("Flug mit Zwischenstationen")){			    	
			    	   	
			    	
		    	
			    	dauer = (entfernung_zw/speed)*60;
			    	int idauer = Double.valueOf(dauer).intValue();
			    	
			    	
			    	//dauerflug = dauerflug.plusMinutes(idauer);
			    	dauerflug = dauerflug + idauer;
			    	System.out.println("FLUG DAUER " + dauerflug);
			    	
//			    	
//			    	
//			    	System.out.println("idauer: " +idauer);
//			    	System.out.println(dauer);
//			    	System.out.println(szg);
//			    	System.out.println(tage);
//			    	System.out.println("Zielzeit: "+ zieldate);
//			    	System.out.println(zielzeit);
			    	
			
			    	
			    	
			    	
			    	
			    	float dauerh = (float)dauer;
			    	dauerh = dauerh / 60;
			    	
			    	for(int t = 0;t<hochentf.length; t++){
			    		
			    		zwischenstop_zw = (int) (zwischenstop_zw + (hochentf[t]/reichweite));
			    		
			    		
			    	}
			    	
			    	
			    	int zwischenstop = zwischenstop_zw + countzw;
			    	
			    	
			    	
			    	System.out.println("Stopps   " + zwischenstop);
			    	
			    	angbetr = BetriebskFZ * dauerh;
			    	
			    	
			    	angfix = (FixkostenFZ/2000) *(dauerh + 1.5F);
			    	
			    	dauercharter = dauercharter + 1.5F + (zwischenstop * 0.75F);
			    	
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
				          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
			    	
			    	charterart = "Flug m Zwischenstops";
				}
				
				
				
				
				if(charterart.equals("Flug m Zwischenstops")){			    	
		    	   	
			    	
			    	
			    	dauer = (entfernung_zw/speed)*60;
			    	int idauer = Double.valueOf(dauer).intValue();
			    	
			    	
			    	//dauerflug = dauerflug.plusMinutes(idauer);
			    	dauerflug = dauerflug + idauer;
			    	System.out.println("FLUG DAUER " + dauerflug);
			    	System.out.println("idauer: " +idauer);
//			    	System.out.println(dauer);
//			    	System.out.println(szg);
//			    	System.out.println(tage);
//			    	System.out.println("Zielzeit: "+ zieldate);
//			    	System.out.println(zielzeit);
			    	
			
			    	
			    	
			    	
			    	
			    	float dauerh = (float)dauer;
			    	dauerh = dauerh / 60;
			    	
			    	for(int t = 0;t<hochentf.length; t++){
			    		
			    		zwischenstop_zw = (int) (zwischenstop_zw + (hochentf[t]/reichweite));
			    		
			    		
			    	}

			    	int zwischenstop = zwischenstop_zw + countzw;
			    	
			    	
			    	
			    	System.out.println("Stopps   " + zwischenstop);
			    	
			    	angbetr = BetriebskFZ * dauerh;
			    	
			    	
			    	angfix = (FixkostenFZ/2000) *(dauerh + 1.5F);
			    	
			    	dauercharter = dauercharter + 1.5F + (zwischenstop * 0.75F);
			    	
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
				          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
				
				
				if(charterart.equals("Flug m Zwischenstops")){
					
					startdate = dpi_startdat.getValue();
				}
				
				if(dpi_zieldat.getValue().isBefore(dpi_startdat.getValue())){
					lbl_dbconnect.setText("Ungültige(r) Wert(e) erfasst");
				}
				
				
				
				else{
				
					if(charterart.equals("Zeitcharter")){
						
						startdate = dpi_startdat.getValue();
						zieldate = dpi_zieldat.getValue();
						
						Str_startzeith = txt_startzeit_h.getText();
				    	Str_startzeitm = txt_startzeit_m.getText();		
				    	startzeit = LocalTime.parse(Str_startzeith+":"+Str_startzeitm+":00");
				    										
				    	Str_zielzeith = txt_zielzeit_h.getText();
				    	Str_zielzeitm = txt_zielzeit_m.getText();
				    	zielzeit = LocalTime.parse(Str_zielzeith+":"+Str_zielzeitm+":00");
						
					}
					
					
					if (txt_zielzeit_h.getText().equals("") && charterart.equals("Zeitcharter") || txt_zielzeit_m.getText().equals("") && charterart.equals("Zeitcharter")){
						 
							 lbl_dbconnect.setText("ungültige(r) Wert(e)!");
						 
						 }

					
					else{
					
					
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

				
						System.out.println(startdate);
						System.out.println(charterart);
						if(charterart.equals("Flug m Zwischenstops")){
							
							System.out.println("Zwischenstopp");
							System.out.println(countzw);
							
							
							for(int a = 0; a<countzw+1; a++)
							{
								System.out.println("Schleife " + a);
							
								if (a==0){
									
									startdate = dpi_startdat.getValue();
									zieldate = zw_an[a];
									
									Str_startzeith = txt_startzeit_h.getText();
							    	Str_startzeitm = txt_startzeit_m.getText();		
							    	startzeit = LocalTime.parse(Str_startzeith+":"+Str_startzeitm+":00");
							    										
							    	Str_zielzeith = zw_an_h[a];
							    	Str_zielzeitm = zw_an_m[a];
							    	zielzeit = LocalTime.parse(Str_zielzeith+":"+Str_zielzeitm+":00");
							    	
							    	Str_StartFH =txt_startfh.getText();
							    	Str_ZielFH = FHzw[a];
							    	
							    	dauerflug = (float) zw_dauer[a]/60;
								
								}
								else if(a == countzw ){
									
									startdate = zw_ab[a-1];
									zieldate = dpi_zieldat.getValue();
									
									Str_startzeith = zw_ab_h[a-1];
							    	Str_startzeitm = zw_ab_m[a-1];		
							    	startzeit = LocalTime.parse(Str_startzeith+":"+Str_startzeitm+":00");
							    										
							    	Str_zielzeith = txt_zielzeit_h.getText();
							    	Str_zielzeitm = txt_zielzeit_m.getText();
							    	zielzeit = LocalTime.parse(Str_zielzeith+":"+Str_zielzeitm+":00");
							    	
							    	Str_StartFH =FHzw[a-1];
							    	Str_ZielFH = txt_zielfh.getText();
							    	
							    	dauerflug = (float) zw_dauer_end/60;
																	
								}
								else{
									
									startdate = zw_ab[a-1];
									zieldate = zw_an[a];
									
									Str_startzeith = zw_ab_h[a-1];
							    	Str_startzeitm = zw_ab_m[a-1];		
							    	startzeit = LocalTime.parse(Str_startzeith+":"+Str_startzeitm+":00");
							    										
							    	Str_zielzeith = zw_an_h[a];
							    	Str_zielzeitm = zw_an_m[a];
							    	zielzeit = LocalTime.parse(Str_zielzeith+":"+Str_zielzeitm+":00");
							    	
							    	Str_StartFH =FHzw[a-1];
							    	Str_ZielFH = FHzw[a];
							    	
							    	dauerflug = (float) zw_dauer[a]/60;
									
								}
								
								if(!charterart.equals("Zeitcharter")){
								statement.executeUpdate(
										"INSERT INTO myflight.fluege " + "VALUES('"
												+startdate+"','"
												+zieldate+"','"
												+startzeit+"','"
												+zielzeit+"','"
												+Str_StartFH+"','"
												+Str_ZielFH+"','" 
												+dauerflug+"','"
												+AngeboteID+"')");
								}
						}	
						
						}	
						else{
							
							System.out.println("Kein Zwischenstopp");
							System.out.println(charterart);
						if(!charterart.equals("Zeitcharter")){	
						statement.executeUpdate(
								"INSERT INTO myflight.fluege " + "VALUES('"
										+startdate+"','"
										+zieldate+"','"
										+startzeit+"','"
										+zielzeit+"','"
										+Str_StartFH+"','"
										+Str_ZielFH+"','" 
										+dauerflug+"','"
										+AngeboteID+"')");
							}

						}
						System.out.println("Termine setzen");
						
						statement.executeUpdate(
								"INSERT INTO benutzerverwaltung.personal_termine_angebote " + "VALUES('Angebotstermin','"
										+AngeboteID+"')");

						statement.executeUpdate(
								"INSERT INTO benutzerverwaltung.flugzeug_termine_angebote " + "VALUES('Angebotstermin','"
										+AngeboteID+"')");
						
						}
			    	
			    	
					catch(Exception e){
						System.err.println("Got an exception! "); 
			            System.err.println(e.getMessage()); 
						}
				
			    	sonderw = false;
			    	
			    	
			    	
			    	try { 

						Statement statement = conn.createStatement();			
						statement.executeUpdate("DELETE FROM benutzerverwaltung.kunde_auswahl");
						Str_cust_id_chosen = "";
						}
				
					catch(Exception e){
						System.err.println("Got an exception! "); 
			            System.err.println(e.getMessage()); 
						}
			    	
			    	
			    	offer_new = false;
			    	
			    	lbl_dbconnect.setText("Angebot wurde erstellt!");
						}
					}
				}
			    	
			   }
			    
 			
			
			
			public void insertMA(String MA_ins){
				
		    	try { 

					Statement statement = conn.createStatement();			
					statement.executeUpdate(
							"INSERT INTO myflight.fluege " + "VALUES('"
									+startdate+"','"
									+zieldate+"','"
									+startzeit+"','"
									+zielzeit+"','"
									+Str_StartFH+"','"
									+Str_ZielFH+"','" 
									+dauerflug+"','"
									+AngeboteID+"')");
					
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
			    System.out.println("HS: " + FZHersteller);
			    FZTyp = FZ.substring(pos+1,FZ.length());
			    System.out.println("TYP: " + FZTyp);
			    
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
			    
			    
			    set_allunvisible(false);
			    apa_create_offer.setVisible(true);
			    apa_btn_createoffer.setVisible(true);
			    }
			    else {lbl_dbconnect.setText("Ungültige(r) Wert(e) erfasst");}
			    
			    
				

				
				
			}

			@FXML public void btn_sonder_stop_click() {
				
				txa_getr.setText("");
				txa_speisen.setText("");
				
				
				set_allunvisible(false);
			    apa_create_offer.setVisible(true);
			    apa_btn_createoffer.setVisible(true);
				
			}
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
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
			    
			    
								
			}

			@FXML public void cbo_cap_click() {
				cbo_cop.getItems().clear();
				cbo_cop.setValue(null);
				cbo_cop.setDisable(true);
				
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
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
				          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
				    }
			    	
			    	
			    }
			    
			}
	
			@FXML public void cbo_cop_click() {
				
				cbo_fa1.getItems().clear();
				cbo_fa1.setValue(null);
				cbo_fa1.setDisable(true);
				
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
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");

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
				          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }

			    	
			    	
			    }
				
				
			}

			@FXML public void cbo_fa3_click() {}

			@FXML public void cbo_fa2_click() {
				
				cbo_fa3.getItems().clear();
				cbo_fa3.setValue(null);
				cbo_fa3.setDisable(true);
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
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
				          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
				    }
			    	
			    	
			    }
				
				
			}
			@FXML public void cbo_fa1_click() {
				
				cbo_fa2.getItems().clear();
				cbo_fa2.setValue(null);
				cbo_fa2.setDisable(true);
	
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
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
				          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
					txt_startzeit_h.setEditable(true);
					txt_startzeit_m.setEditable(true);
					txt_zielzeit_h.setEditable(true);
					txt_zielzeit_m.setEditable(true);
					btn_sw.setDisable(false);
					btn_startfh.setDisable(true);
					btn_zielfh.setDisable(true);
				}
				
				
				if(charterart.equals("Einzelflug")){
					
					btn_startfh.setDisable(false);
					btn_zielfh.setDisable(false);
					dpi_startdat.setDisable(false);
					dpi_zieldat.setDisable(true);
					txt_pass.setDisable(false);
					txt_startzeit_h.setDisable(false);
					txt_startzeit_m.setDisable(false);
					btn_sw.setDisable(false);
					txt_startzeit_h.setEditable(true);
					txt_startzeit_m.setEditable(true);
					txt_zielzeit_h.setEditable(false);
					txt_zielzeit_m.setEditable(false);
				}
				
				if(charterart.equals("Flug mit Zwischenstationen")){
					
					btn_startfh.setDisable(false);
					btn_zielfh.setDisable(false);
					dpi_startdat.setDisable(false);
					dpi_zieldat.setDisable(true);
					txt_pass.setDisable(false);
					txt_startzeit_h.setDisable(true);
					txt_startzeit_m.setDisable(true);
					txt_zielzeit_h.setDisable(false);
					txt_zielzeit_m.setDisable(false);
					txt_startzeit_h.setEditable(true);
					txt_startzeit_m.setEditable(true);
					txt_zielzeit_h.setEditable(false);
					txt_zielzeit_m.setEditable(false);
					txt_startzeit_h.setDisable(false);
					txt_startzeit_m.setDisable(false);
					btn_sw.setDisable(false);
					btn_zw.setDisable(false);
					
				}
				
			}
			
			public void getBestFZ(){
				
 //###############Flugzeug finden
			    	    

			    
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
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }	
			    	
			    System.out.println("JETZT kommt das ARRAY für passendes " + alleFZ + " " + highpax);
			    
			    FZpass = alleFZ - counter;
			
			    System.out.println(FZpass + " wurden gefunden!!!!!");
			    if(FZpass == 0){lbl_dbconnect.setText("Kein passendes Flugzeug gefunden");}
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
					          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
					    }	
			    	

			    	 
			    }
			    
			    pax = pax + 1;
			    if(pax>highpax){FZgefunden = true;
			    System.out.println("nichts"); 
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
				          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
					          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
					          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");

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
					          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
					          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
				

				if(fromzw == true){
					
					Str_StartFH = zwstartfh;
					Str_ZielFH = zwzielfh;
					
				}
				else{
				Str_StartFH = txt_startfh.getText();
				Str_ZielFH = txt_zielfh.getText();
				}
				fromzw = false;
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
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
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
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }			    
			    
			    entfernung = (float) (Math.acos(Math.sin(startfhlat)*Math.sin(zielfhlat) + Math.cos(startfhlat)*Math.cos(zielfhlat)*Math.cos(zielfhlon - startfhlon))* 6371);
//			   //entfernung = 0.1;
//			    double test = Math.toDegrees(entfernung);
//			    double test2 = Math.toRadians(entfernung);
//			    double test3 = test * 6378.388;
			  
//			    entfernung = Math.acos(Math.sin(48.34)*Math.sin(53.87) + Math.cos(48.34)*Math.cos(53.87)*Math.cos(14.15 - 11.78)) * 6378.388  ;//6378.137;

			    
				
			}

			@FXML public void btn_fh_zws_click() {
				
				zwFH = true;
				set_allunvisible(false);
				apa_search_fh.setVisible(true);
				
			}

			@FXML public void btn_zwscount_click() {
				
				cbo_zws.getItems().clear();
				cbo_zws.setValue(null);
				
				 if (txt_countzws.getText().matches("[0-5]") || txt_countzws.getText() == ""){
				 
				countzw = Integer.valueOf(txt_countzws.getText());
				 }
				 else{
					 lbl_dbconnect.setText("Ungültige(r) Wert(e) erfasst");
				 }
				 

				FHzw = new String[countzw];
				zw_an_h = new String[countzw];
				zw_an_m = new String[countzw];
				zw_ab_h = new String[countzw];
				zw_ab_m = new String[countzw];
				zw_dauer = new double[countzw];
				zw_an = new LocalDate[countzw];
				zw_ab = new LocalDate[countzw];

				
				int x = 1;
				for(int i = 0; i < countzw; i++){
				cbo_zws.getItems().addAll(x);
				x = x +1;
				}
				txt_fh_zws.setDisable(false);
				txt_zwsan_h.setDisable(false);
				txt_zwsan_m.setDisable(false);
				txt_zwsab_h.setDisable(false);
				txt_zwsab_m.setDisable(false);
				dpi_zws_an.setDisable(true);
				dpi_zws_ab.setDisable(false);	
				btn_zws_save.setDisable(false);
				btn_zwscount.setDisable(true);
			}

			@FXML public void btn_zws_save_click() {
				
				System.out.println("Array: " + arrayzw);
				
				System.out.println(txt_fh_zws.getText());
				
				FHzw[arrayzw] = txt_fh_zws.getText();
				//zw_an_h[arrayzw] = txt_zwsan_h.getText();
				//zw_an_m[arrayzw] = txt_zwsan_m.getText();
				zw_ab_h[arrayzw] = txt_zwsab_h.getText();
				zw_ab_m[arrayzw] = txt_zwsab_m.getText();
				//zw_an[arrayzw] = dpi_zws_an.getValue();
				zw_ab[arrayzw] = dpi_zws_ab.getValue();
				
				cbo_zws.setDisable(false);
				
				
				
				
		    	
												
			}

			@FXML public void btn_zws_ok_click() {

				

				set_allunvisible(false);
				apa_create_offer.setVisible(true);
				apa_btn_createoffer.setVisible(true);
				
				
				fromzw = true;
				
				
								
					zwstartfh = FHzw[countzw-1];
					zwzielfh = txt_zielfh.getText();
					Str_startzeith = zw_ab_h[countzw-1];
			    	Str_startzeitm = zw_ab_m[countzw-1];
			    	startdate = zw_ab[countzw-1];
				
				
				getEntfernung();
				
				
				//Str_zielzeith = txt_startzeit_h.getText();
		    	//Str_zielzeitm = txt_startzeit_m.getText();
		    	zieldate = startdate;
		    	
		    	if(entfernung < 4000){speed=600;}
		    	if(entfernung > 4000){speed=750;}
				
				double dauer = (entfernung/speed)*60;
		    	int idauer = Double.valueOf(dauer).intValue();
		    	
		    	double szh = Double.parseDouble(Str_startzeith);
		    	double szm = Double.parseDouble(Str_startzeitm);
		    	
		    	double szg = (szh * 60)+szm;
		    	szg = szg + dauer;
		    	double tage = 1440-szg;
		    				    	
		    	System.out.println(zieldate);
		    	if(tage<0){
		    		
		    		System.out.println("ist drin");
		    		zieldate = startdate.plusDays(1);
		    	}
		    	if(tage < -1440){
		    		
		    		
		    		zieldate = startdate.plusDays(2);
		    		
		    	}
		    	
		    	startzeit = LocalTime.parse(Str_startzeith+":"+Str_startzeitm+":00");
		    	zielzeit = startzeit.plusMinutes(idauer);
		    	
		    	String data6 = zielzeit.toString();
		    	int pos_z2 = data6.indexOf(":");
			    String zwanh  = data6.substring(0, pos_z2);
		    	String zwanm = data6.substring(pos_z2+1,5);
		    	
	
		    					
					txt_zielzeit_h.setText(zwanh);
					txt_zielzeit_m.setText(zwanm);
					dpi_zieldat.setValue(zieldate);
					zw_dauer_end = dauer;
			    	//zw_an_h[arrayzw] = zwanh;
				    //zw_an_m[arrayzw] = zwanm;
				    //zw_an[arrayzw] = zieldate;
				
				
				
			}


			@FXML public void btn_zws_stop_click() {
				
				set_allunvisible(false);
				apa_create_offer.setVisible(true);
				apa_btn_createoffer.setVisible(true);
			}

			@FXML public void cbo_zws_click() {
				
				arrayzw = Integer.parseInt(cbo_zws.getValue().toString()) - 1;	
				btn_zws_save.setText("Station " + (arrayzw + 1) + " übernehmen");
				
				txt_fh_zws.setText(FHzw[arrayzw]);
				txt_zwsan_h.setText(zw_an_h[arrayzw]);
				txt_zwsan_m.setText(zw_an_m[arrayzw]);
				txt_zwsab_h.setText(zw_ab_h[arrayzw]);
				txt_zwsab_m.setText(zw_ab_m[arrayzw]);
				dpi_zws_an.setValue(zw_an[arrayzw]);
				dpi_zws_ab.setValue(zw_ab[arrayzw]);
				
				
				txt_fh_zws.setText(FHzw[arrayzw]);
				txt_zwsan_h.setText(zw_an_h[arrayzw]);
				txt_zwsan_m.setText(zw_an_m[arrayzw]);
				txt_zwsab_h.setText(zw_ab_h[arrayzw]);
				txt_zwsab_m.setText(zw_ab_m[arrayzw]);
				dpi_zws_an.setValue(zw_an[arrayzw]);
				dpi_zws_ab.setValue(zw_ab[arrayzw]);
				
				cbo_zws.setDisable(true);
				
			}
			@FXML public void acc_cal_click() {
				
				action_get_calendar();
				
			}
			@FXML public void btn_cancel_kf_click() {}
			@FXML public void btn_save_kf_click() {
				
				int z_id = 0;
				String per = null;
				String sw = null;
				String kom = null;
				String pre = null;
				String pue = null;
				LocalDate heute = LocalDate.now();
				
				String tgg_per = tgg_pers.getSelectedToggle().toString();
				String tgg_sws = tgg_sw.getSelectedToggle().toString();
				String tgg_koms = tgg_kom.getSelectedToggle().toString();
				String tgg_pres = tgg_pre.getSelectedToggle().toString();
				String tgg_pues = tgg_pue.getSelectedToggle().toString();
				
				if(tgg_per.contains("_sg_")){
					per = "sehr gut";					
				}
				else if(tgg_per.contains("_g_")){
					per = "gut";					
				}
				else if(tgg_per.contains("_b_")){
					per = "befriedigend";					
				}
				else if(tgg_per.contains("_s_")){
					per = "schlecht";	
				}
				else if(tgg_per.contains("_ss_")){
					per = "sehr schlecht";					
				}
				else{
					per = "keine Bewertung";					
				}
				
				
				
				if(tgg_sws.contains("_sg_")){
					sw = "sehr gut";					
				}
				else if(tgg_sws.contains("_g_")){
					sw = "gut";					
				}
				else if(tgg_sws.contains("_b_")){
					sw = "befriedigend";					
				}
				else if(tgg_sws.contains("_s_")){
					sw = "schlecht";	
				}
				else if(tgg_sws.contains("_ss_")){
					sw = "sehr schlecht";					
				}
				else{
					sw = "keine Bewertung";					
				}
				
				
				
				if(tgg_koms.contains("_sg_")){
					kom = "sehr gut";					
				}
				else if(tgg_koms.contains("_g_")){
					kom = "gut";					
				}
				else if(tgg_koms.contains("_b_")){
					kom = "befriedigend";					
				}
				else if(tgg_koms.contains("_s_")){
					kom = "schlecht";	
				}
				else if(tgg_koms.contains("_ss_")){
					kom = "sehr schlecht";					
				}
				else{
					kom = "keine Bewertung";					
				}
				
				
				if(tgg_pres.contains("_sg_")){
					pre = "sehr gut";					
				}
				else if(tgg_pres.contains("_g_")){
					pre = "gut";					
				}
				else if(tgg_pres.contains("_b_")){
					pre = "befriedigend";					
				}
				else if(tgg_pres.contains("_s_")){
					pre = "schlecht";	
				}
				else if(tgg_pres.contains("_ss_")){
					pre = "sehr schlecht";					
				}
				else{
					pre = "keine Bewertung";					
				}
				
				
				if(tgg_pues.contains("_sg_")){
					pue = "sehr gut";					
				}
				else if(tgg_pues.contains("_g_")){
					pue = "gut";					
				}
				else if(tgg_pues.contains("_b_")){
					pue = "befriedigend";					
				}
				else if(tgg_pues.contains("_s_")){
					pue = "schlecht";	
				}
				else if(tgg_pues.contains("_ss_")){
					pue = "sehr schlecht";					
				}
				else{
					pue = "keine Bewertung";					
				}
				
				try{
			    	
			    	Statement statement_zid = conn.createStatement();
			    	ResultSet rs = statement_zid.executeQuery("SELECT MAX(Z_ID) FROM myflight.zufriedenheit");      
			        while((rs != null) && (rs.next())){
			        	
			        	//cbo_fz.setValue(rs.getString(3));
			        	z_id = rs.getInt(1);

			        }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				if(z_id != 0){
				z_id = z_id +1;
				}
				try { 

					Statement statement = conn.createStatement();			
					statement.executeUpdate(
							"INSERT INTO myflight.zufriedenheit " + "VALUES("
									+z_id+",'"
									+per+"','" 
									+sw+"','"
									+kom+"','"
									+pue+"','"
									+pre+"','"
									+heute+"')");
					lbl_dbconnect.setText("Kundenfeedback wurde erfasst!");
					}
			
				catch(Exception e){
					System.err.println("Got an exception! "); 
		            System.err.println(e.getMessage()); 
					}
				
				
				
				
			}
			@FXML public void hlk_create_feedback() {
				
				set_allunvisible(false);
				apa_kf.setVisible(true);
				apa_btn_kf.setVisible(true);
				btn_cancel_kf.setVisible(false);
				
			}
			@FXML public void hlk_create_ablehnung() {
				
				set_allunvisible(false);
				apa_ableh_ang.setVisible(true);
				cbo_ang.getItems().clear();
				cbo_ang.setValue(null);
				
				
				try{
			    	
					Statement statement_rep = conn.createStatement();
			    	ResultSet rs = statement_rep.executeQuery("Select Angebote_ID from myflight.angebote Where Angebotsstatus_Angebotsstatus = 'Negativ' and angebote.Angebote_ID not in (SELECT Angebote_ID FROM myflight.ablehnungen);");      
			        while((rs != null) && (rs.next())){
			        	
			        	cbo_ang.getItems().add(rs.getInt(1)); }
			    				        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data"); 
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				
				
				
			}
			@FXML public void btn_zufr_start_click() {
				//connectDB();
				int sg = 0;
				int g = 0;
				int b = 0;
				int s = 0;
				int ss = 0;
				int na = 0;
				
				
				
				LocalDate start = dpi_zuf_start.getValue();
				LocalDate end = dpi_zufr_end.getValue();
				
//				<String fx:value="Personal" />
//				<String fx:value="Sonderwünsche" />
//				<String fx:value="Komfort" />
//				<String fx:value="Pünktlichkeit" />
//				<String fx:value="Preis" />
				
				String art = cbo_artzuf.getValue().toString();
				if(art.equals("Sonderwünsche"))art="Sonderwuensche";
				if(art.equals("Pünktlichkeit"))art="Puenktlichkeit";
								
				try{
			    	
					Statement statement_rep = conn.createStatement();
			    	ResultSet rs = statement_rep.executeQuery("SELECT Count("+art+") FROM myflight.zufriedenheit Where "+art+" = 'sehr gut' AND Datum between '"+start+"' and  '"+end+"'  ");      
			        while((rs != null) && (rs.next())){sg=rs.getInt(1); }
			    				        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data"); 
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				
				try{
			    	
					Statement statement_rep = conn.createStatement();
					ResultSet rs1 = statement_rep.executeQuery("SELECT Count("+art+") FROM myflight.zufriedenheit Where "+art+" = 'gut' AND Datum between '"+start+"' and  '"+end+"'  ");      
			        while((rs1 != null) && (rs1.next())){g=rs1.getInt(1); }
			    				        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");   
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				
				try{
			    	
					Statement statement_rep = conn.createStatement();
					ResultSet rs2 = statement_rep.executeQuery("SELECT Count("+art+") FROM myflight.zufriedenheit Where "+art+" = 'befriedigend' AND Datum between '"+start+"' and  '"+end+"'  ");      
			        while((rs2 != null) && (rs2.next())){b=rs2.getInt(1); }
			    			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data"); 
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				
				try{
			    	
					Statement statement_rep = conn.createStatement();
					ResultSet rs3 = statement_rep.executeQuery("SELECT Count("+art+") FROM myflight.zufriedenheit Where "+art+" = 'schlecht' AND Datum between '"+start+"' and  '"+end+"'  ");      
			        while((rs3 != null) && (rs3.next())){s=rs3.getInt(1); }
			    	}
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");            
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				
				try{
			    	
					Statement statement_rep = conn.createStatement();
					ResultSet rs4 = statement_rep.executeQuery("SELECT Count("+art+") FROM myflight.zufriedenheit Where "+art+" = 'sehr schlecht' AND Datum between '"+start+"' and  '"+end+"'  ");      
			        while((rs4 != null) && (rs4.next())){ss=rs4.getInt(1); }
			    			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data"); 
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				
				try{
			    	
					Statement statement_rep = conn.createStatement();
					ResultSet rs5 = statement_rep.executeQuery("SELECT Count("+art+") FROM myflight.zufriedenheit Where "+art+" = 'keine Bewertung' AND Datum between '"+start+"' and  '"+end+"'  ");      
			        while((rs5 != null) && (rs5.next())){na=rs5.getInt(1); }			        
			    			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data"); 
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			          
			    }
				
				
				
				ObservableList<PieChart.Data> pieChartData =
			            FXCollections.observableArrayList(
			            new PieChart.Data("sehr gut", sg),
			            new PieChart.Data("gut", g),
			            new PieChart.Data("befriedigend", b),
			            new PieChart.Data("schlecht", s),
			            new PieChart.Data("sehr schlecht", ss),
			            new PieChart.Data("keine Bewertung", na));
				
				pieChartData.forEach(data ->data.nameProperty().bind(Bindings.concat(data.getName(), " ", data.pieValueProperty())));

			 pie_zufr.setData(pieChartData);

			        
				
				
			}
			@FXML public void hlk_report_feedback() {
				
				set_allunvisible(false);
				apa_zufr.setVisible(true);
				
			}
			@FXML public void btn_ablehn_ang_save_click() {
				
				int a_id = Integer.parseInt(cbo_ang.getValue().toString());
				String agr = cbo_ablehn.getValue().toString();
				LocalDate heute = LocalDate.now();
				
				if(agr.equals("Angebotsqualität"))agr = "Angebotsqualitaet";
				
				try { 

					Statement statement = conn.createStatement();			
					statement.executeUpdate(
							"INSERT INTO myflight.ablehnungen " + "VALUES("
									+a_id+",'"
									+agr+"','" 
									+heute+"')");
					
					lbl_dbconnect.setText("Ablehnungsgrund wurde erfasst!");
					
					}
			
				catch(Exception e){
					System.err.println("Got an exception! "); 
		            System.err.println(e.getMessage()); 
					}
				
				
				cbo_ang.getItems().clear();
				cbo_ang.setValue(null);
				cbo_ablehn.setValue(" ");
				
				
				
				
				try{
			    	
					Statement statement_rep = conn.createStatement();
			    	ResultSet rs = statement_rep.executeQuery("Select Angebote_ID from myflight.angebote Where Angebotsstatus_Angebotsstatus = 'Negativ' and angebote.Angebote_ID not in (SELECT Angebote_ID FROM myflight.ablehnungen);");      
			        while((rs != null) && (rs.next())){
			        	
			        	cbo_ang.getItems().add(rs.getInt(1)); }
			    				        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");   
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				
				
				
				
			}
			
			
			@FXML public void btn_ableh_start_click() {
				
//				<String fx:value=" " />
//					<String fx:value="Preis" />
//					<String fx:value="Konkurrenz" />
//					<String fx:value="Angebotsqualität" />
//					<String fx:value="Termin nicht passend" />
//					<String fx:value="Sonstiges" />
				
				
				int pre = 0;
				int kon = 0;
				int anq = 0;
				int term = 0;
				int son = 0;
				
				
				LocalDate start = dpi_ableh_start.getValue();
				LocalDate end = dpi_ableh_end.getValue();
				
//				<String fx:value="Personal" />
//				<String fx:value="Sonderwünsche" />
//				<String fx:value="Komfort" />
//				<String fx:value="Pünktlichkeit" />
//				<String fx:value="Preis" />
				
//				String art = cbo_ablehn.getValue().toString();
//				if(art.equals("Angebots"))art="Sonderwuensche";
//				if(art.equals("Pünktlichkeit"))art="Puenktlichkeit";
								
				try{
			    	
					Statement statement_rep = conn.createStatement();
			    	ResultSet rs = statement_rep.executeQuery("SELECT Count(Angebote_ID) FROM myflight.ablehnungen Where Ablehnungsgrund = 'Preis' AND Datum between '"+start+"' and  '"+end+"'  ");      
			        while((rs != null) && (rs.next())){pre=rs.getInt(1); }
			    				        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data"); 
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				
				try{
			    	
					Statement statement_rep = conn.createStatement();
					ResultSet rs1 = statement_rep.executeQuery("SELECT Count(Angebote_ID) FROM myflight.ablehnungen Where Ablehnungsgrund = 'Konkurrenz' AND Datum between '"+start+"' and  '"+end+"'  ");      
			        while((rs1 != null) && (rs1.next())){kon=rs1.getInt(1); }
			    				        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data"); 
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				
				try{
			    	
					Statement statement_rep = conn.createStatement();
					ResultSet rs2 = statement_rep.executeQuery("SELECT Count(Angebote_ID) FROM myflight.ablehnungen Where Ablehnungsgrund = 'Angebotsqualitaet'  AND Datum between '"+start+"' and  '"+end+"'  ");      
			        while((rs2 != null) && (rs2.next())){anq=rs2.getInt(1); }
			    			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data"); 
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				
				try{
			    	
					Statement statement_rep = conn.createStatement();
					ResultSet rs3 = statement_rep.executeQuery("SELECT Count(Angebote_ID) FROM myflight.ablehnungen Where Ablehnungsgrund = 'Termin nicht passend' AND Datum between '"+start+"' and  '"+end+"'  ");      
			        while((rs3 != null) && (rs3.next())){term=rs3.getInt(1); }
			    	}
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");  
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");          
			    }
				
				try{
			    	
					Statement statement_rep = conn.createStatement();
					ResultSet rs4 = statement_rep.executeQuery("SELECT Count(Angebote_ID) FROM myflight.ablehnungen Where Ablehnungsgrund = 'Sonstiges' AND Datum between '"+start+"' and  '"+end+"'  ");      
			        while((rs4 != null) && (rs4.next())){son=rs4.getInt(1); }
			    			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");  
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				
				
				
				ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
			            new PieChart.Data("Preis", pre),
			            new PieChart.Data("Konkurrenz", kon),
			            new PieChart.Data("Angebotsqualität", anq),
			            new PieChart.Data("Termin nicht passend", term ),
			            new PieChart.Data("Sonstiges", son));
				
				pieChartData.forEach(data ->data.nameProperty().bind(Bindings.concat(data.getName(), " ", data.pieValueProperty())));

			 pie_ablehn.setData(pieChartData);

				
				
			}
			@FXML public void rbt_cal_all_clcik() {
								
				cbo_cal_ma.setDisable(true);
				cbo_cal_fz.setDisable(true);
				
			}
			@FXML public void rbt_cal_ma_click() {
				
				cbo_cal_fz.setDisable(true);
				cbo_cal_ma.setDisable(false);
				
			}
			@FXML public void rbt_cal_fz_click() {
				
				cbo_cal_ma.setDisable(true);
				cbo_cal_fz.setDisable(false);
				
			}
			@FXML public void btn_newterm_cancel_click() {
				
				set_allunvisible(false);
				
				apa_calendar.setVisible(true);
				apa_btn_term.setVisible(true);
				
			}
			@FXML public void btn_newterm_save_click() {
				//TODO 
				
				

							    
				
				String sek = "59";
				int FZid = 0;
				int MAid = 0;
				LocalTime t_start;
				LocalTime t_end;
				LocalDate d_start;
				LocalDate d_end;
				
				
				
				if(tgb_term_ma.isSelected()){
					System.out.println("drin term ma");
					if(dpi_term_ma_start.getValue() == null || dpi_term_ma_end.getValue() == null || cbo_term_ma.getValue() == null || cbo_term_maart.getValue() == null ) {
						lbl_dbconnect.setText("Bitte Pflichtfelder ausfüllen!");
					}
					else if(txt_term_ma_starth.getText().equals("") || txt_term_ma_startm.getText().equals("") || txt_term_ma_endh.getText().equals("") || txt_term_ma_endm.getText().equals("") ){
						
						lbl_dbconnect.setText("Bitte Pflichtfelder ausfüllen!");
						
					}
					else if(dpi_term_ma_end.getValue().isBefore(dpi_term_ma_start.getValue())){
						
						lbl_dbconnect.setText("Ungültige(r) Wert(e) erfasst");
					}
					else{
					
					String MA = cbo_term_ma.getValue().toString();
					int pos1 = MA.indexOf(" ");
				    MAid = Integer.parseInt(MA.substring(0, pos1));
				    
				    if(chb_term_ma.isSelected()){
				    	System.out.println("drin chb term ma");
				    	t_start = LocalTime.parse(txt_term_ma_starth.getText() + ":" + txt_term_ma_startm.getText());
				    	t_end = LocalTime.parse(txt_term_ma_endh.getText() + ":" + txt_term_ma_endm.getText() + ":" + sek );
				    	
				    }
				    else{
				    	
				    	t_start = LocalTime.parse(txt_term_ma_starth.getText() + ":" + txt_term_ma_startm.getText());
				    	t_end = LocalTime.parse(txt_term_ma_endh.getText() + ":" + txt_term_ma_endm.getText());
				    }
				    
				    d_start = dpi_term_ma_start.getValue();
				    d_end = dpi_term_ma_end.getValue();
				    
				    if(cbo_term_maart.getValue().toString().equals("Krankheit")){
				    	System.out.println("vor Krank");				    
				    try { 

						Statement statement = conn.createStatement();			
						statement.executeUpdate(
								"INSERT INTO benutzerverwaltung.personal_termine_krankheit " + "VALUES('"
										+"Krankheit"+"',"
										+MAid+",'"
										+d_start+"','"
										+d_end+"','"
										+t_start+"','"
										+t_end+"')");
					    lbl_dbconnect.setText("Termin wurde erstellt");

						}
				
					catch(Exception e){
						System.err.println("Got an exception! "); 
			            System.err.println(e.getMessage()); 
						}
				    }	
				    if(cbo_term_maart.getValue().toString().equals("Urlaub")){
				    	System.out.println("drin chb urlaub");   
					    try { 

							Statement statement = conn.createStatement();			
							statement.executeUpdate(
									"INSERT INTO benutzerverwaltung.personal_termine_urlaub " + "VALUES('"
											+"Urlaub"+"',"
											+MAid+",'"
											+d_start+"','"
											+d_end+"','"
											+t_start+"','"
											+t_end+"')");

							
							System.out.println("Urlaub " +MAid+",'"	+d_start+"','"+d_end+"','"+t_start+"','"+t_end+"')");
						    lbl_dbconnect.setText("Termin wurde erstellt");

							}
					
						catch(Exception e){
							System.err.println("Got an exception! "); 
				            System.err.println(e.getMessage()); 
							}
				
				    }
				    else{
				    	System.out.println("Urlaub geht nicht");
				    }
					
					
				}
				   
				    
				    
				}    
				    
			
				
				if(tgb_term_fz.isSelected()){
					
					if(dpi_term_fz_start.getValue() == null || dpi_term_fz_end.getValue() == null|| cbo_term_fz.getValue()== null||cbo_term_fzart.getValue()== null ) {
						lbl_dbconnect.setText("Bitte Pflichtfelder ausfüllen!");
					}
					else if(txt_term_fz_starth.getText().equals("") || txt_term_fz_startm.getText().equals("") || txt_term_fz_endh.getText().equals("") || txt_term_fz_endm.getText().equals("") ){
						
						lbl_dbconnect.setText("Bitte Pflichtfelder ausfüllen!");
						
					}
					else if(dpi_term_fz_end.getValue().isBefore(dpi_term_fz_start.getValue())){
						
						lbl_dbconnect.setText("Ungültige(r) Wert(e) erfasst");
					}
					else{
					
					String FZ = cbo_term_fz.getValue().toString();
					int pos = FZ.indexOf(" ");
				    FZid = Integer.parseInt(FZ.substring(0, pos));
				    
				    
				    if(chb_term_fz.isSelected()){
					    
				    	t_start = LocalTime.parse(txt_term_fz_starth.getText() + ":" + txt_term_fz_startm.getText());
				    	t_end = LocalTime.parse(txt_term_fz_endh.getText() + ":" + txt_term_fz_endm.getText() + ":" + sek );
				    	
				    }
				    else{
				    	
				    	t_start = LocalTime.parse(txt_term_fz_starth.getText() + ":" + txt_term_fz_startm.getText());
				    	t_end = LocalTime.parse(txt_term_fz_endh.getText() + ":" + txt_term_fz_endm.getText());
				    }
				    
				    d_start = dpi_term_fz_start.getValue();
				    d_end = dpi_term_fz_end.getValue();
				    
				    
				    if(cbo_term_fzart.getValue().toString().equals("Reparatur")){
				    					    
				    try { 

						Statement statement = conn.createStatement();			
						statement.executeUpdate(
								"INSERT INTO benutzerverwaltung.flugzeug_termine_reparatur " + "VALUES('"
										+"Reparatur"+"',"
										+FZid+",'"
										+d_start+"','"
										+d_end+"','"
										+t_start+"','"
										+t_end+"')");
						lbl_dbconnect.setText("Termin wurde erstellt");
						
						}
				
					catch(Exception e){
						System.err.println("Got an exception! "); 
			            System.err.println(e.getMessage()); 
						}
				    }
				    if(cbo_term_fzart.getValue().toString().equals("Wartung")){
					    
					    try { 

							Statement statement = conn.createStatement();			
							statement.executeUpdate(
									"INSERT INTO benutzerverwaltung.flugzeug_termine_wartung " + "VALUES('"
											+"Wartung"+"',"
											+FZid+",'"
											+d_start+"','"
											+d_end+"','"
											+t_start+"','"
											+t_end+"')");
							lbl_dbconnect.setText("Termin wurde erstellt");
							
							}
					
						catch(Exception e){
							System.err.println("Got an exception! "); 
				            System.err.println(e.getMessage()); 
							}
					    
				    }
				    
					
					
				}
				    
					}
				
				    }
				    

			@FXML public void btn_term_create_click() {
				
				set_allunvisible(false);
				apa_term_new.setVisible(true);
				apa_termnew_btn.setVisible(true);
				
				cbo_term_ma.getItems().clear();
				cbo_term_ma.setValue(null);
				
				cbo_term_fz.getItems().clear();
				cbo_term_fz.setValue(null);
				
				dpi_term_ma_start.setDisable(true);
				dpi_term_ma_end.setDisable(true);
				cbo_term_ma.setDisable(true);
				txt_term_ma_starth.setDisable(true);
				txt_term_ma_startm.setDisable(true);
				txt_term_ma_endh.setDisable(true);
				txt_term_ma_endm.setDisable(true);
				chb_term_ma.setDisable(true);
				cbo_term_maart.setDisable(true);
				
				try{
			    	
			    	Statement statement = conn.createStatement();
			    	ResultSet rs = statement.executeQuery("SELECT * FROM myflight.personal");      
			        while((rs != null) && (rs.next())){
			        	
			        	cbo_term_ma.getItems().add(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3));
		        }
			        ResultSet rs2 = statement.executeQuery("SELECT Distinct(Flugzeug_ID), FlugzeugHersteller, FlugzeugTyp  FROM myflight.flugzeuge join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=Flugzeugtypen_ID order by Flugzeug_ID ");      
			        while((rs2 != null) && (rs2.next())){
			        	
			        	cbo_term_fz.getItems().add(rs2.getInt(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
		        }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data"); 
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				
				
				
			}
			@FXML public void btn_term_edit_click() {
				
				set_allunvisible(false);
				apa_termn_bearb_btn.setVisible(true);
				apa_term_bearb.setVisible(true);
				
				cbo_term_bearb_mafz.getItems().clear();
				
				try{
			    	
			    	Statement statement = conn.createStatement();
			    	ResultSet rs = statement.executeQuery("SELECT * FROM myflight.personal");      
			        while((rs != null) && (rs.next())){
			        	
			        	cbo_term_bearb_mafz.getItems().add(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3));
		        }
			       
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");  
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				
				
			}
			@FXML public void chb_term_ma_click() {
				
				if(chb_term_ma.isSelected()){
					
					txt_term_ma_starth.setText("00");
					txt_term_ma_startm.setText("00");
					txt_term_ma_endh.setText("23");
					txt_term_ma_endm.setText("59");
					txt_term_ma_starth.setDisable(true);
					txt_term_ma_startm.setDisable(true);
					txt_term_ma_endh.setDisable(true);
					txt_term_ma_endm.setDisable(true);
										
				}
				else{
				txt_term_ma_starth.setText("");
				txt_term_ma_startm.setText("");
				txt_term_ma_endh.setText("");
				txt_term_ma_endm.setText("");
				txt_term_ma_starth.setDisable(false);
				txt_term_ma_startm.setDisable(false);
				txt_term_ma_endh.setDisable(false);
				txt_term_ma_endm.setDisable(false);
				}
				
			}
			@FXML public void chb_term_fz_click() {
				
				if(chb_term_fz.isSelected()){
					
					txt_term_fz_starth.setText("00");
					txt_term_fz_startm.setText("00");
					txt_term_fz_endh.setText("23");
					txt_term_fz_endm.setText("59");
					txt_term_fz_starth.setDisable(true);
					txt_term_fz_startm.setDisable(true);
					txt_term_fz_endh.setDisable(true);
					txt_term_fz_endm.setDisable(true);
					
				}
				else{
				txt_term_fz_starth.setText("");
				txt_term_fz_startm.setText("");
				txt_term_fz_endh.setText("");
				txt_term_fz_endm.setText("");
				txt_term_fz_starth.setDisable(false);
				txt_term_fz_startm.setDisable(false);
				txt_term_fz_endh.setDisable(false);
				txt_term_fz_endm.setDisable(false);
				}
				
			}
			@FXML public void tgb_term_ma_click() {
				

				dpi_term_ma_start.setDisable(false);
				dpi_term_ma_end.setDisable(false);
				cbo_term_ma.setDisable(false);
				txt_term_ma_starth.setDisable(false);
				txt_term_ma_startm.setDisable(false);
				txt_term_ma_endh.setDisable(false);
				txt_term_ma_endm.setDisable(false);
				chb_term_ma.setDisable(false);
				cbo_term_maart.setDisable(false);
				
				dpi_term_fz_start.setDisable(true);
				dpi_term_fz_end.setDisable(true);
				cbo_term_fz.setDisable(true);
				txt_term_fz_starth.setDisable(true);
				txt_term_fz_startm.setDisable(true);
				txt_term_fz_endh.setDisable(true);
				txt_term_fz_endm.setDisable(true);
				chb_term_fz.setDisable(true);
				cbo_term_fzart.setDisable(true);				
				
			}
			@FXML public void tgb_term_fz_click() {
				
				dpi_term_ma_start.setDisable(true);
				dpi_term_ma_end.setDisable(true);
				cbo_term_ma.setDisable(true);
				txt_term_ma_starth.setDisable(true);
				txt_term_ma_startm.setDisable(true);
				txt_term_ma_endh.setDisable(true);
				txt_term_ma_endm.setDisable(true);
				chb_term_ma.setDisable(true);
				cbo_term_maart.setDisable(true);
				
				dpi_term_fz_start.setDisable(false);
				dpi_term_fz_end.setDisable(false);
				cbo_term_fz.setDisable(false);
				txt_term_fz_starth.setDisable(false);
				txt_term_fz_startm.setDisable(false);
				txt_term_fz_endh.setDisable(false);
				txt_term_fz_endm.setDisable(false);
				chb_term_fz.setDisable(false);
				cbo_term_fzart.setDisable(false);
				
			}
			
//<<<<<<< HEAD
			@FXML public void tgb_term_bearb_ma_click() {
				
				lbl_term_bearb_mafz.setText("Mitarbeiter:");
				col_term_mafz.setText("Mitarbeiter");
				cbo_term_bearb_mafz.getItems().clear();
				try{
			    	
			    	Statement statement = conn.createStatement();
			    	ResultSet rs = statement.executeQuery("SELECT * FROM myflight.personal");      
			        while((rs != null) && (rs.next())){
			        	
			        	cbo_term_bearb_mafz.getItems().add(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3));
		        }
			       
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");    
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				
			}
			@FXML public void tgb_term_bearb_fz_click() {
				
				lbl_term_bearb_mafz.setText("Flugzeug:");
				col_term_mafz.setText("Flugzeug");
				cbo_term_bearb_mafz.getItems().clear();
				
				try{
			    	
			    	Statement statement = conn.createStatement();
			    	ResultSet rs2 = statement.executeQuery("SELECT Distinct(Flugzeug_ID), FlugzeugHersteller, FlugzeugTyp  FROM myflight.flugzeuge join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=Flugzeugtypen_ID order by Flugzeug_ID ");      
			        while((rs2 != null) && (rs2.next())){
			        	
			        	cbo_term_bearb_mafz.getItems().add(rs2.getInt(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
			        	
		        }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data"); 
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				
				
			}
			@FXML public void btn_term_bearb_search_click() {
//TODO
				
			if(dpi_term_bearb_start.getValue() == null || dpi_term_bearb_end.getValue() == null || cbo_term_bearb_mafz.getValue() == null){
				
				lbl_dbconnect.setText("Pflichtfeld(er) füllen");
				
			}
			else{	
				
			termData.remove(0, termData.size());	
			String startd = dpi_term_bearb_start.getValue().toString();
			String zield = dpi_term_bearb_end.getValue().toString();
				

				
				String id = cbo_term_bearb_mafz.getValue().toString();
				int pos2 = id.indexOf(" ");
				int i_id =  Integer.parseInt(id.substring(0, pos2));
				
				String sql = "";
				String sqlfz = "SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur where flugzeuge_Flugzeug_ID="+i_id+" and Datum_von='"+startd+"' and Datum_bis='"+zield+"'  union SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung where flugzeuge_Flugzeug_ID= "+i_id+" and Datum_von='"+startd+"' and Datum_bis='"+zield+"'";
				String sqlma = "SELECT * FROM benutzerverwaltung.personal_termine_krankheit where personal_Personal_ID="+i_id+" and Datum_von='"+startd+"' and Datum_bis='"+zield+"' union SELECT * FROM benutzerverwaltung.personal_termine_urlaub Where personal_Personal_ID="+i_id+" and Datum_von='"+startd+"' and Datum_bis='"+zield+"'";
				
				if(tgb_term_bearb_fz.isSelected()){sql = sqlfz;
				System.out.println("Flugzeugsuche");}
				else{sql = sqlma;}
				
				try{
			    	
			    	Statement statement = conn.createStatement();
			    	ResultSet rs = statement.executeQuery(sql); 
			    	System.out.println(sql);
			        while((rs != null) && (rs.next())){
		        	
			        	termData.add(new termbearb(rs.getInt(2), rs.getString(1), rs.getString(3), rs.getString(4),rs.getString(5),rs.getString(6)));
			        	
			         }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data"); 
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				
			}
				
				
			}
			@FXML public void btn_term_bearb_delete_click() {
				
								
				TablePosition pos = tbl_term.getSelectionModel().getSelectedCells().get(0);
				int row = pos.getRow();
				termbearb item = tbl_term.getItems().get(row);
				TableColumn col = pos.getTableColumn();
				Integer data = col_term_mafz.getCellObservableValue(item).getValue();
				String data2 = col_term_art.getCellObservableValue(item).getValue();
				String data3 = col_term_startd.getCellObservableValue(item).getValue();
				String data4 = col_term_startz.getCellObservableValue(item).getValue();
				String data5 = col_term_endd.getCellObservableValue(item).getValue();
				String data6 = col_term_endzeit.getCellObservableValue(item).getValue();
				
				
				ori_id = data;
				
				ori_art = data2;
				
				LocalDate startd = LocalDate.parse(data3);
				ori_startd = data3;

				int pos_z = data4.indexOf(":");
			    String szh = data4.substring(0, pos_z);
			    String szm = data4.substring(pos_z+1,5);
				ori_szh =szh;
				ori_szm =szm;
				
				LocalDate endd = LocalDate.parse(data5);
				ori_zield =data5;
				
				int pos_z2 = data6.indexOf(":");
			    String zzh = data6.substring(0, pos_z2);
			    String zzm = data6.substring(pos_z2+1,5);
				ori_zzm =zzm;
				ori_zzh =zzh;
				
			
				int i_id = ori_id;
				String art = ori_art;
				String table = "";
				String f_art = "";
				String f_id = "";
				
				String ori_startz = ori_szh+":"+ori_szm+":00";
				String ori_zielz = ori_zzh+":"+ori_zzm+":00";
				
				if(art.equals("Wartung") || art.equals("Reperatur")){
					
					table = "benutzerverwaltung.flugzeug_termine_"+art;
					f_art ="flugzeug_terminarten_Flugzeug_Terminarten";
					f_id = "flugzeuge_Flugzeug_ID";
					
				}
				else{
					table = "benutzerverwaltung.personal_termine_"+art;
					f_art = "personal_terminarten_Personal_Terminarten";
					f_id = "personal_Personal_ID";
				}
				
				List<String> choices = new ArrayList<>();
				choices.clear();
				choices.add("Ja");
				choices.add("Nein");

				ChoiceDialog<String> dialog1 = new ChoiceDialog<>("Ja", choices);
				dialog1.setTitle("Termin löschen");
				dialog1.setHeaderText("Wollen Sie den Termin \nwirklich löschen?");
				dialog1.setContentText("Auswahl:");

				Optional<String> result1 = dialog1.showAndWait();
				
				result1.ifPresent(letter -> System.out.println("Your choice: " + letter));

				if (result1.isPresent()) {
					AuswahlDokutyp = result1.get();

					if (AuswahlDokutyp == "Ja") {
						try {
							Statement statement = conn.createStatement();
					    	
							statement.executeUpdate("delete from "+table+" where " + f_art + " ='" + art +"' and " + f_id + "= " +  i_id + " and Datum_von=' " + ori_startd + "' and Datum_bis=' " + ori_zield + "' and Uhrzeit_von='" + ori_startz + "' and Uhrzeit_bis='" + ori_zielz+"'" );

							lbl_dbconnect.setText("Termin wurde gelöscht");

						} 
						catch (SQLException sqle) {

							lbl_dbconnect.setText("Datenbankverbindung fehlgeschlagen");
							sqle.printStackTrace();
						}
				
					}
				}
			}
			@FXML public void btn_term_bearb_bearb_click() {
				
				if(tgb_term_bearb_fz.isSelected()){lbl_term_1bearb_mafz.setText("Flugzeug:");}
				else{lbl_term_1bearb_mafz.setText("Mitarbeiter:");}
				
				TablePosition pos = tbl_term.getSelectionModel().getSelectedCells().get(0);
				int row = pos.getRow();
				termbearb item = tbl_term.getItems().get(row);
				TableColumn col = pos.getTableColumn();
				Integer data = col_term_mafz.getCellObservableValue(item).getValue();
				String data2 = col_term_art.getCellObservableValue(item).getValue();
				String data3 = col_term_startd.getCellObservableValue(item).getValue();
				String data4 = col_term_startz.getCellObservableValue(item).getValue();
				String data5 = col_term_endd.getCellObservableValue(item).getValue();
				String data6 = col_term_endzeit.getCellObservableValue(item).getValue();
				

				if(data2.equals("")){
					lbl_dbconnect.setText("Bitte eine Zeile wählen!");
				}
				else{				
				txt_term_1bearb_mafz.setText(data.toString());
				ori_id = data;
				txt_term_1bearb_art.setText(data2);
				ori_art = data2;
				
				LocalDate startd = LocalDate.parse(data3);
				dpi_term_1bearb_startd.setValue(startd);
				ori_startd = data3;

				int pos_z = data4.indexOf(":");
			    String szh = data4.substring(0, pos_z);
			    String szm = data4.substring(pos_z+1,5);
				txt_term_1bearb_startz_h.setText(szh);
				ori_szh =szh;
				txt_term_1bearb_startz_m.setText(szm);
				ori_szm =szm;
				
				LocalDate endd = LocalDate.parse(data5);
				dpi_term_1bearb_endd.setValue(endd);
				ori_zield =data5;
				
				int pos_z2 = data6.indexOf(":");
			    String zzh = data6.substring(0, pos_z2);
			    String zzm = data6.substring(pos_z2+1,5);
				txt_term_1bearb_endz_m.setText(zzm);
				ori_zzm =zzm;
				txt_term_1bearb_endz_h.setText(zzh);
				ori_zzh =zzh;
				

				
				set_allunvisible(false);

				apa_term_1bearb.setVisible(true);
				apa_term_1bearb_btn.setVisible(true);
				}
								
			}
			@FXML public void btn_term_bearb_cancel_click() {
				
				set_allunvisible(false);
				
				apa_calendar.setVisible(true);
				apa_btn_term.setVisible(true);
				
			}
			@FXML public void btn_term_1bearb_save_click() {
				

				if(dpi_term_1bearb_startd.getValue()==null || dpi_term_1bearb_endd.getValue() == null || txt_term_1bearb_startz_h.getText().equals("") || txt_term_1bearb_startz_m.getText().equals("")|| txt_term_1bearb_endz_h.getText().equals("") || txt_term_1bearb_endz_m.getText().equals("")){
				lbl_dbconnect.setText("Pflichtfeld(er) füllen");	
				}
				else if(dpi_term_1bearb_endd.getValue().isBefore(dpi_term_1bearb_startd.getValue())){
				lbl_dbconnect.setText("Ungültige(r) Wert(e) erfasst");
				}
				else{			
				
				int i_id = Integer.parseInt(txt_term_1bearb_mafz.getText());
				String startd = dpi_term_1bearb_startd.getValue().toString();
				String zield = dpi_term_1bearb_endd.getValue().toString();
				String art = txt_term_1bearb_art.getText();
				String table = "";
				String f_art = "";
				String f_id = "";
				String starth = txt_term_1bearb_startz_h.getText();
				String startm = txt_term_1bearb_startz_m.getText();
				String zielh = txt_term_1bearb_endz_h.getText();
				String zielm = txt_term_1bearb_endz_m.getText();
				LocalTime startz = LocalTime.parse("00:00:00");
				LocalTime zielz = LocalTime.parse("00:00:00");
//				LocalTime ori_startz = LocalTime.parse("00:00:00");
//				LocalTime ori_zielz = LocalTime.parse("00:00:00");
				startz = LocalTime.parse(starth+":"+startm+":00");
				zielz = LocalTime.parse(zielh+":"+zielm+":00");
//				ori_startz = LocalTime.parse(ori_szh+":"+ori_szm+":00");
//				ori_zielz = LocalTime.parse(ori_zzh+":"+ori_zzm+":00");
				String ori_startz = ori_szh+":"+ori_szm+":00";
				String ori_zielz = ori_zzh+":"+ori_zzm+":00";
				
				if(art.equals("Wartung") || art.equals("Reperatur")){
					
					table = "benutzerverwaltung.flugzeug_termine_"+art;
					f_art ="flugzeug_terminarten_Flugzeug_Terminarten";
					f_id = "flugzeuge_Flugzeug_ID";
					
				}
				else{
					table = "benutzerverwaltung.personal_termine_"+art;
					f_art = "personal_terminarten_Personal_Terminarten";
					f_id = "personal_Personal_ID";
				}
				
				
				String sql = "";
				String sqlfz = "SELECT * FROM benutzerverwaltung.flugzeug_termine_reparatur where flugzeuge_Flugzeug_ID="+i_id+" and Datum_von <='"+startd+"' and Datum_bis >='"+zield+"'  union SELECT * FROM benutzerverwaltung.flugzeug_termine_wartung where flugzeuge_Flugzeug_ID= "+i_id+" and Datum_von <='"+startd+"' and Datum_bis >='"+zield+"'";
				String sqlma = "SELECT * FROM benutzerverwaltung.personal_termine_krankheit where personal_Personal_ID="+i_id+" and Datum_von <='"+startd+"' and Datum_bis >='"+zield+"' union SELECT * FROM benutzerverwaltung.personal_termine_urlaub Where personal_Personal_ID="+i_id+" and Datum_von <='"+startd+"' and Datum_bis >='"+zield+"'";
				
				if(tgb_term_bearb_fz.isSelected()){sql = sqlfz;}
				else{sql = sqlma;}
				
				try{
			    	
			    	Statement statement = conn.createStatement();
			    	Statement statement2 = conn.createStatement();
			    	Statement statement3 = conn.createStatement();
			    	ResultSet rs = statement.executeQuery(sql);      
			        
			    	System.out.println(rs);
			    	if(!rs.next()){
			    		
			    		statement2.executeUpdate(			    		
								"INSERT INTO " +table + " VALUES('"
										+art+"',"
										+i_id+",'"
										+startd+"','"
										+zield+"','"
										+startz+"','"
										+zielz+"')");
							
								
							statement.executeUpdate("delete from "+table+" where " + f_art + " ='" + art +"' and " + f_id + "= " +  i_id + " and Datum_von=' " + ori_startd + "' and Datum_bis=' " + ori_zield + "' and Uhrzeit_von='" + ori_startz + "' and Uhrzeit_bis='" + ori_zielz+"'" );

//							set_allunvisible(false);
//							apa_termn_bearb_btn.setVisible(true);
//							apa_term_bearb.setVisible(true);
							lbl_dbconnect.setText("Termin wurde geändert");
			    	}
			    	
			    	else{
			    		
			    		lbl_dbconnect.setText("Termin nicht verfügbar");
			    		
			    	
//			    	while((rs != null) && (rs.next())){
//		        	
//			        	termData.add(new termbearb(rs.getInt(2), rs.getString(1), rs.getString(3), rs.getString(4),rs.getString(5),rs.getString(6)));
//			        	
//			         }
			    	}
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data"); 
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				
				
				}	
			
				
			}
			@FXML public void btn_term_1bearb_cancel_click() {
				
				set_allunvisible(false);
				apa_termn_bearb_btn.setVisible(true);
				apa_term_bearb.setVisible(true);
				
				
				
			}
			@FXML public void cbo_profit_year_click() {
			
				year = Integer.parseInt(cbo_profit_year.getValue().toString());
				getProfit();		
		
			}
			@FXML public void cbo_profit_fz_click() {
				
				//Flugzeug ID herausfinden
				String FZ = cbo_profit_fz.getValue().toString();
				int pos = FZ.indexOf(" ");
				int fzid =  Integer.parseInt(FZ.substring(0, pos));
				
				//Auswertungsjahr holen
				year = Integer.parseInt(cbo_profit_year.getValue().toString());
				
				//Abfrage der Profit Tabellen 
				try{
			    	
					Statement statement_rep = conn.createStatement();
			    	ResultSet rs = statement_rep.executeQuery("SELECT sum(myflight.fluege.flugzeit), flugzeuge_Flugzeug_ID  FROM myflight.fluege join myflight.angebote on myflight.fluege.angebote_Angebote_ID=myflight.angebote.Angebote_ID WHERE myflight.fluege.Datum_von between '"+year+"-01-01' and '"+year+"-12-31' and flugzeuge_Flugzeug_ID ="+ fzid);      
			        while((rs != null) && (rs.next())){
			        	
			        	lbl_profit_dauer.setText(Integer.toString(rs.getInt(1)/60)); 
			        	lbl_profit_pro.setText(Integer.toString(((rs.getInt(1)/60)*100)/2000)+" %");

			        }
			    				        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");   
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				
				
			}
		
			
			@FXML public void hlk_profit_click() {
				
				set_allunvisible(false);
				apa_profit.setVisible(true);
				profityear = LocalDate.now();
				year = profityear.getYear();
				System.out.println(year);
				cbo_profit_year.getItems().clear();
				cbo_profit_year.getItems().addAll(year, year-1, year-2,year-3,year-4,year-5);
				cbo_profit_year.setValue(year);
				cbo_profit_fz.getItems().clear();
				
				try{
			    	
			    	Statement statement = conn.createStatement();
			    	ResultSet rs2 = statement.executeQuery("SELECT Distinct(Flugzeug_ID), FlugzeugHersteller, FlugzeugTyp  FROM myflight.flugzeuge join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=Flugzeugtypen_ID order by Flugzeug_ID ");      
			        while((rs2 != null) && (rs2.next())){
			        	
			        	cbo_profit_fz.getItems().add(rs2.getInt(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
		        }
			        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data"); 
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				
				getProfit();
				
				
				

				
			}
			
			public void getProfit(){
				
				int countf = 0;
				
				try{
			    	
					Statement statement_rep = conn.createStatement();
			    	ResultSet rs = statement_rep.executeQuery("SELECT count(Flugzeug_ID) FROM myflight.flugzeuge");      
			        while((rs != null) && (rs.next())){
			        	
			        	countf = rs.getInt(1);
			        	
			        }
			    				        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				
				int profitdaten [][] = new int[countf][2];
				
				for(int z=1;z<countf+1;z++){
				try{
			    	
					Statement statement_rep = conn.createStatement();
			    	ResultSet rs = statement_rep.executeQuery("SELECT sum(myflight.fluege.flugzeit), flugzeuge_Flugzeug_ID  FROM myflight.fluege join myflight.angebote on myflight.fluege.angebote_Angebote_ID=myflight.angebote.Angebote_ID WHERE myflight.fluege.Datum_von between '"+year+"-01-01' and '"+year+"-12-31' and flugzeuge_Flugzeug_ID ="+ z);      
			        while((rs != null) && (rs.next())){
			        	
			        	profitdaten[z-1][0] = rs.getInt(1);
			        	profitdaten[z-1][1] = z;
			        	System.out.println(profitdaten[z-1][0] + " " + profitdaten[z-1][1] );
			        }
			    				        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");   
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				
				}
				
				int temp1 = 0;
				int temp2 = 0;
				boolean getauscht = false;
				
				do{
					getauscht = false;
				for(int s = 1; s<profitdaten.length;s++){
					
					if(profitdaten[s][0]>profitdaten[s-1][0]){
						
						temp1 = profitdaten[s-1][0];
						temp2 = profitdaten[s-1][1];
						
						profitdaten[s-1][0] = profitdaten[s][0];
						profitdaten[s-1][1] = profitdaten[s][1];
						
						profitdaten[s][0] = temp1;
						profitdaten[s][1] = temp2;	
						
						getauscht = true;
						
					}
					
									
				}
				
				for(int i = 0; i<profitdaten.length;i++){
					
					
					System.out.println(profitdaten[i][0] + " " + profitdaten[i][1] );
									
				}
				}while(getauscht);
				
				
				for(int top=0;top<5;top++){
				try{
			    	
					Statement statement_rep = conn.createStatement();
			    	ResultSet rs = statement_rep.executeQuery("SELECT Distinct(Flugzeug_ID), FlugzeugHersteller, FlugzeugTyp  FROM myflight.flugzeuge join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=Flugzeugtypen_ID Where Flugzeug_ID ="+profitdaten[top][1]);      
			        while((rs != null) && (rs.next())){
			        	
			        	if(top==0){
			        	lbl_profit_topfz1.setText(rs.getString(2) +" "+rs.getString(3)+" ("+rs.getInt(1)+")");
			        	lbl_profit_topfd1.setText(Integer.toString((profitdaten[top][0])/60));
			        	lbl_profit_toppro1.setText(Integer.toString((((profitdaten[top][0])/60)*100)/2000)+" %");
			        	
			        	}
			        	if(top==1){
				        	lbl_profit_topfz2.setText(rs.getString(2) +" "+rs.getString(3)+" ("+rs.getInt(1)+")");
				        	lbl_profit_topfd2.setText(Integer.toString((profitdaten[top][0])/60));
				        	lbl_profit_toppro2.setText(Integer.toString((((profitdaten[top][0])/60)*100)/2000)+" %");
				        	
				       }
			        	if(top==2){
				        	lbl_profit_topfz3.setText(rs.getString(2) +" "+rs.getString(3)+" ("+rs.getInt(1)+")");
				        	lbl_profit_topfd3.setText(Integer.toString((profitdaten[top][0])/60));
				        	lbl_profit_toppro3.setText(Integer.toString((((profitdaten[top][0])/60)*100)/2000)+" %");
				        	
				       }
			        	if(top==3){
				        	lbl_profit_topfz4.setText(rs.getString(2) +" "+rs.getString(3)+" ("+rs.getInt(1)+")");
				        	lbl_profit_topfd4.setText(Integer.toString((profitdaten[top][0])/60));
				        	lbl_profit_toppro4.setText(Integer.toString((((profitdaten[top][0])/60)*100)/2000)+" %");
				        	
				       }
			        	if(top==4){
				        	lbl_profit_topfz5.setText(rs.getString(2) +" "+rs.getString(3)+" ("+rs.getInt(1)+")");
				        	lbl_profit_topfd5.setText(Integer.toString((profitdaten[top][0])/60));
				        	lbl_profit_toppro5.setText(Integer.toString((((profitdaten[top][0])/60)*100)/2000)+" %");
				       }
			        }
			    				        
			    }
			    catch(Exception e){
			          e.printStackTrace();
			          System.out.println("Error on Building Data");  
			          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
			    }
				
				}
				
				for(int flop=countf-1;flop>countf-6;flop--){
					try{
				    	
						Statement statement_rep = conn.createStatement();
				    	ResultSet rs = statement_rep.executeQuery("SELECT Distinct(Flugzeug_ID), FlugzeugHersteller, FlugzeugTyp  FROM myflight.flugzeuge join myflight.flugzeugtypen on Flugzeugtypen_Flugzeugtypen_ID=Flugzeugtypen_ID Where Flugzeug_ID ="+profitdaten[flop][1]);      
				        while((rs != null) && (rs.next())){
				        	
				        	if(flop==countf-1){
				        	lbl_profit_flopfz1.setText(rs.getString(2) +" "+rs.getString(3)+" ("+rs.getInt(1)+")");
				        	lbl_profit_flopfd1.setText(Integer.toString((profitdaten[flop][0])/60));
				        	lbl_profit_floppro1.setText(Integer.toString((((profitdaten[flop][0])/60)*100)/2000)+" %");
				        	
				        	}
				        	if(flop==countf-2){
					        	lbl_profit_flopfz2.setText(rs.getString(2) +" "+rs.getString(3)+" ("+rs.getInt(1)+")");
					        	lbl_profit_flopfd2.setText(Integer.toString((profitdaten[flop][0])/60));
					        	lbl_profit_floppro2.setText(Integer.toString((((profitdaten[flop][0])/60)*100)/2000)+" %");
					        	
					       }
				        	if(flop==countf-3){
					        	lbl_profit_flopfz3.setText(rs.getString(2) +" "+rs.getString(3)+" ("+rs.getInt(1)+")");
					        	lbl_profit_flopfd3.setText(Integer.toString((profitdaten[flop][0])/60));
					        	lbl_profit_floppro3.setText(Integer.toString((((profitdaten[flop][0])/60)*100)/2000)+" %");
					        	
					       }
				        	if(flop==countf-4){
					        	lbl_profit_flopfz4.setText(rs.getString(2) +" "+rs.getString(3)+" ("+rs.getInt(1)+")");
					        	lbl_profit_flopfd4.setText(Integer.toString((profitdaten[flop][0])/60));
					        	lbl_profit_floppro4.setText(Integer.toString((((profitdaten[flop][0])/60)*100)/2000)+" %");
					        	
					       }
				        	if(flop==countf-5){
					        	lbl_profit_flopfz5.setText(rs.getString(2) +" "+rs.getString(3)+" ("+rs.getInt(1)+")");
					        	lbl_profit_flopfd5.setText(Integer.toString((profitdaten[flop][0])/60));
					        	lbl_profit_floppro5.setText(Integer.toString((((profitdaten[flop][0])/60)*100)/2000)+" %");
					       }
				        }
				    				        
				    }
				    catch(Exception e){
				          e.printStackTrace();
				          System.out.println("Error on Building Data");    
				          lbl_dbconnect.setText("technischer Fehler in Datenbankverbindung aufgetreten");
				    }
					
					}
				
				
			}
			

			
//
//=======
@FXML public void action_konfig() {
	set_allunvisible(false);
	
	apa_konfig.setVisible(true);
	Versionsnr.setText("V2.48");
	txa_history.setText("V2.48\nBugfix Angebot\n------------------------------------------------------------------------------------------\nV2.47\nBugfix Einzelflug Angebot\n------------------------------------------------------------------------------------------\nV2.46\nBugfix VIP + Zahltermin\n------------------------------------------------------------------------------------------\nV2.45\nBugfix zeitcharter\n------------------------------------------------------------------------------------------\nV2.43\nBugfix Angebot\n------------------------------------------------------------------------------------------\nV2.42\nBugfix Versenden bei Auftrag-Erstellung\n------------------------------------------------------------------------------------------\nV2.41\nTerminerstellung\nFehlerbehandlungsroutine\n------------------------------------------------------------------------------------------\nV2.40\nBugfix Word-Ausgabe\n------------------------------------------------------------------------------------------\nV2.35\nTerminverwaltung / Bugfixes Angebotserstellung\n------------------------------------------------------------------------------------------\nV2.34\nFlug mit Zwischenstationen\nTermine bearbeiten\nBugfix DBConnect\n------------------------------------------------------------------------------------------\nV2.33\nBugfix Druck Angebot");
	
}
//>>>>>>> branch 'master' of https://github.com/burggraf-erich/itworks.git
@FXML public void btn_offer_newcust_click() throws SQLException {
	
	action_createkundendaten();

	
	zwFH = true;
	
	
}

}
