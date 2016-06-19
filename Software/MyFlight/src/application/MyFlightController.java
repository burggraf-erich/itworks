package application;
// V1.21

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import application.PDFPrinter;

//import jfx.messagebox.MessageBox;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
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
import javafx.scene.control.DatePicker;

import java.lang.String;
import java.net.URISyntaxException;

import javafx.util.Callback;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.EventHandler;
import javafx.scene.control.TableRow;
import javafx.beans.value.ChangeListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
// imports für PDF-Generator
import java.io.File;
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

import application.druckauftrag;


public class MyFlightController {

	// ObjectVariable für Word-Dokument
	private static ObjectFactory factory;
	private static final String WORD_STYLE_TITLE = "Title";
	private static final String WORD_STYLE_HEADING1 = "Heading1";
	private static final String WORD_STYLE_HEADING2 = "Heading2";
	
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
	private String user;
	private String password;
	
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

	private ObservableList<RechnungenCost> costbilldata = FXCollections.observableArrayList();

	public ObservableList<RechnungenCost> getcostbilldata() {
		return costbilldata;
	}

	private ObservableList<RechnungenCostreminder> costreminder_warnings_billdata = FXCollections.observableArrayList();

	public ObservableList<RechnungenCostreminder> getcostreminder_warnings_billdata() {
		return costreminder_warnings_billdata;
	}
	
	// zu Beginn besteht keine Autentifizierung und damit sind alle Menüpunkte und Buttons deaktiviert
	private boolean authenticated = false;
	
	Connection conn;
	int highest_custID = 0;

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
	@FXML Button btn_costextracostedit;
	@FXML Button btn_createreminder;
	@FXML Button btn_canceltrackingedit;
	@FXML Button btn_costtrackingedit;
	@FXML Button btn_cancelcostextracostedit;
	@FXML Button btn_delete_order;
	@FXML Button btn_change_user;
	@FXML Button btn_save_billstatus;
	@FXML Button btn_save_costtrackingedit;
	
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
	@FXML AnchorPane ancpanebtn_changebillstatus;
	@FXML AnchorPane apa_btn_costtrackingreminder;
	@FXML AnchorPane apa_btn_costtrackingedit;
	@FXML AnchorPane apa_btn_costtrackingoverview;
	@FXML AnchorPane apa_btn_costextracostedit;
	
	@FXML ScrollPane scroll_pane_order;
	@FXML ScrollPane scroll_pane_changeorder;
	@FXML ScrollPane scrollpane_changebillstatus;
	@FXML ScrollPane scroll_pane_angebotübersicht;
	@FXML ScrollPane scroll_pane_auftragübersicht;
	@FXML ScrollPane scroll_pane_rechnungenübersicht;
	@FXML ScrollPane scroll_pane_costtrackingoverview;
	@FXML ScrollPane scroll_pane_costtrackingreminder_warnings;
	
	
	
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

	@FXML TextField txt_username;
	@FXML TextField txt_companyname;
	@FXML TextField txt_street;
	@FXML TextField txt_place;
	@FXML TextField txt_homenumber;
	@FXML TextField txt_customerid;
	@FXML TextField txt_homeext;
	
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
	
	//Felder für Maske Erstelle Auftrag - Ende
	
	//Felder für Maske Ändere Auftrag - Beginn
	
	@FXML TextField kdname1;
	@FXML TextField kdvname1;
	@FXML Label artcharter1;
	@FXML TextField flgztyp1;
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
     
    @FXML TextField txt_mail;
	@FXML TextField txt_mobile;
	@FXML TextField txt_name;
	@FXML TextField txt_phone;
	@FXML TextField txt_prename;
	@FXML ComboBox<String> cbo_salutation;
	@FXML ComboBox<String> cbo_title;
	@FXML ComboBox<String> choiceorderstatus;
	@FXML ComboBox<String> choicebillstatus;
	@FXML ComboBox<String> choicecostbillstatus;
	


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
	//@FXML	TableColumn<Angebote, String> Datum;
	@FXML	TableColumn<Angebote, String> Status;
	@FXML	TableColumn<Angebote, String> Kdgruppe;
	@FXML	TableColumn<Angebote, String> Kdvname;
	@FXML	TableColumn<Angebote, String> Aart;
	@FXML	TableColumn<Angebote, String> Flgztyp;
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
	
	//@FXML	TableColumn<Aufträge, String> Datumorder;
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
	


	@FXML	
	private void initialize() {
		// Initialize the person table with the two columns.
		Nummer.setCellValueFactory(cellData -> cellData.getValue().NummerProperty().asObject());
		Flgztyp.setCellValueFactory(cellData -> cellData.getValue().FlgztypProperty());
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
		
		
		angebotetabelle.setItems(getangebotedata());
		auftragtable.setItems(getauftraegedata());
		billtable.setItems(getbilldata());
		costbilltable.setItems(getcostbilldata());
		costreminder_warnings_billtable.setItems(getcostreminder_warnings_billdata());
		
		apa_btn_login.setVisible(true);
		apa_login.setVisible(true);
	    btncreateorder.disableProperty().bind(Bindings.isEmpty(angebotetabelle.getSelectionModel().getSelectedIndices()));
	    btnprint.disableProperty().bind(Bindings.isEmpty(auftragtable.getSelectionModel().getSelectedIndices()));
		btnsend.disableProperty().bind(Bindings.isEmpty(auftragtable.getSelectionModel().getSelectedIndices()));
		btncreatebill.disableProperty().bind(Bindings.isEmpty(auftragtable.getSelectionModel().getSelectedIndices()));
		angebotedit.disableProperty().bind(Bindings.isEmpty(auftragtable.getSelectionModel().getSelectedIndices()));
		btn_changebillstatus.disableProperty().bind(Bindings.isEmpty(billtable.getSelectionModel().getSelectedIndices()));
		btn_costtrackingedit.disableProperty().bind(Bindings.isEmpty(costbilltable.getSelectionModel().getSelectedIndices()));
	//	btn_costextracostedit.disableProperty().bind(Bindings.isEmpty(costreminder_warnings_billtable.getSelectionModel().getSelectedIndices()));
		btn_createreminder.disableProperty().bind(Bindings.isEmpty(costreminder_warnings_billtable.getSelectionModel().getSelectedIndices()));

				
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
				conn = DriverManager.getConnection(url, user, password);

				Statement stmt = conn.createStatement();
				ResultSet rs = stmt
						.executeQuery("SELECT berechtigungen_berechtigungen_id FROM benutzer where benutzervorname='"
								+ vorname + "' and benutzernachname='" + nachname + "'");

				if ((rs != null) && (rs.next())) {

					berechtigungsstufe = rs.getInt(1);
					rs = stmt.executeQuery("SELECT Berechtigungen FROM berechtigungen where Berechtigungen_ID='"
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
				}
				if (userobject.getberechtigung() == 3) {
					mnuadministration.setDisable(false);
				}

				conn.close();
				//
			} catch (SQLException sqle) {

				lbl_dbconnect.setText("Datenbankverbindung fehlgeschlagen");
				// System.out.println("geht nicht");
				sqle.printStackTrace();

				// Anwendung auch bei fehlenden Berechtigungen freischalten -
				// Beginn
				apa_login.setVisible(false);
				apa_welcome.setVisible(true);
				lbl_username.setText(user);

				btn_login.setVisible(false);
				btn_change_user.setVisible(true);

				User userobject = new User(vorname, nachname, "Mitarbeiter", 3);
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

				if (userobject.getberechtigung() >= 2) {
					// mnuzusatzkosten.setDisable(false);
				}
				if (userobject.getberechtigung() == 3) {
					mnuadministration.setDisable(false);
				}

			}
		}
		// Anwendung auch bei fehlenden Berechtigungen freischalten - Ende
	}

	// private char[] substringBefore(Object setText, String string) {
	// TODO Auto-generated method stub
	// return null;
	// }

	@FXML public void btn_close_click(ActionEvent event) {
				
		System.exit(0);
	}

	@FXML
	public void actiongetangebote() {
		// lbl_dbconnect.setText("Mouse geklickt!");

		set_allunvisible();
		scroll_pane_angebotübersicht.setVisible(true);
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
		    if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
			
			Statement stmt = conn.createStatement();
			
			// angebote-übersicht abrufen
			ResultSet rs = stmt.executeQuery("SELECT angebote.*, fluege.datum_von, fluege.datum_bis, kunden.*, flugzeugtypen.flugzeugtyp FROM angebote INNER JOIN fluege on angebote.angebote_id=fluege.angebote_Angebote_ID inner join kunden inner join flugzeuge inner join flugzeugtypen on angebote.kunden_kunde_id= kunden.kunde_id and angebote.flugzeuge_Flugzeug_ID=flugzeuge.Flugzeug_ID and flugzeuge.Flugzeugtypen_Flugzeugtypen_ID=flugzeugtypen.Flugzeugtypen_ID group by angebote.angebote_id");
					
			angebotedata.remove(0, angebotedata.size());
			int i = 1;
			// Testbeginn
			// rs = null;
			 // Testende
			
			while ((rs != null) && (rs.next())) {
				System.out.println(i++ + " " + rs.getInt(1) + " " + rs.getString(4) + " " + rs.getString(40) + " "
						+ rs.getString(41) + " " + rs.getString(21) + " " + rs.getString(22) + " " + rs.getString(29) + " " + rs.getString(30));
				angebotedata.add(new Angebote(rs.getInt(1), rs.getString(4), rs.getString(40), rs.getString(41), rs.getString(21),rs.getString(22), rs.getString(29), rs.getString(30)));
			}
			
			//wenn die Datenbank bei der Entwicklung leer ist
			//angebotedata.add(new Angebote(303043,"22.05.2016","Einzelflug","CORP"));
			
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
		scroll_pane_costtrackingoverview.setVisible(false);
		scroll_pane_costtrackingreminder_warnings.setVisible(false);
		scroll_pane_rechnungenübersicht.setVisible(false);	
		
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
	
	@FXML	public void actiongetaufträge() {
		// lbl_dbconnect.setText("Mouse geklickt!");

		set_allunvisible();
		scroll_pane_auftragübersicht.setVisible(true);
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
			final String hostname = "172.20.1.24"; 
	        final String port = "3306"; 
	        String dbname = "myflight";
			String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
		    if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
			
			Statement stmt = conn.createStatement();
			
			// Aufträge-übersicht abrufen
			ResultSet rs = stmt.executeQuery("SELECT auftraege.*, angebote.*, fluege.datum_von, fluege.datum_bis, kunden.*, flugzeugtypen.flugzeugtyp FROM auftraege inner join angebote INNER JOIN fluege inner join kunden inner join flugzeuge inner join flugzeugtypen on angebote.angebote_id=fluege.angebote_Angebote_ID and angebote.kunden_kunde_id= kunden.kunde_id and angebote.flugzeuge_Flugzeug_ID=flugzeuge.Flugzeug_ID and flugzeuge.Flugzeugtypen_Flugzeugtypen_ID=flugzeugtypen.Flugzeugtypen_ID and angebote.angebote_id=auftraege.Angebote_Angebote_ID group by auftraege.auftraege_id");
			
			auftraegedata.remove(0, auftraegedata.size());
			int i = 1;
			// Testbeginn
			// rs = null;
			 // Testende
			
			while ((rs != null) && (rs.next())) {
				System.out.println(i++ + " " + rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " "
						+ rs.getString(8) + " " + rs.getString(44) + " " + rs.getString(33) + " " + rs.getString(34) + " " + rs.getString(45)+ " " + rs.getString(25)+ " " + rs.getString(26));
				auftraegedata.add(new Aufträge(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(8), rs.getString(44),rs.getString(33), rs.getString(34), rs.getString(45), rs.getString(25), rs.getString(26)));
			}
			
			//wenn die Datenbank bei der Entwicklung leer ist
			//angebotedata.add(new Angebote(303043,"22.05.2016","Einzelflug","CORP"));
			
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

	@FXML	public void actiongetrechnungen() {
		// lbl_dbconnect.setText("Mouse geklickt!");

		set_allunvisible();
		scroll_pane_rechnungenübersicht.setVisible(true);
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
		    if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
			
			Statement stmt = conn.createStatement();
			
			// Rechnungen-übersicht abrufen
			ResultSet rs = stmt.executeQuery("SELECT auftraege.*, angebote.*, fluege.datum_von, fluege.datum_bis, kunden.*, flugzeugtypen.flugzeugtyp, rechnungen.* FROM auftraege inner join angebote INNER JOIN fluege on angebote.angebote_id=fluege.angebote_Angebote_ID inner join kunden inner join flugzeuge inner join flugzeugtypen on angebote.kunden_kunde_id= kunden.kunde_id inner join rechnungen on rechnungen.auftraege_auftraege_id=auftraege.auftraege_id and angebote.flugzeuge_Flugzeug_ID=flugzeuge.Flugzeug_ID and flugzeuge.Flugzeugtypen_Flugzeugtypen_ID=flugzeugtypen.Flugzeugtypen_ID and auftraege.angebote_angebote_id = angebote.angebote_id");
		
			
			
			
			billdata.remove(0, billdata.size());
			int i = 1;
			// Testbeginn
			// rs = null;
			 // Testende
			
			while ((rs != null) && (rs.next())) {
				System.out.println(i++ + " " + rs.getInt(46) + " " + rs.getString(49) + " " + rs.getString(33) + " "
						+ rs.getString(48) + " " + rs.getFloat(11)+ " " + rs.getFloat(17)+ " " + rs.getFloat(13)+ " " + rs.getString(44) );
				billdata.add(new Rechnungen(rs.getInt(46), rs.getString(49), rs.getString(33), rs.getString(48), rs.getFloat(11), rs.getFloat(17), rs.getFloat(13), rs.getString(44)));
			}
		//wenn die Datenbank bei der Entwicklung leer ist
//			billdata.remove(0, billdata.size());
//			billdata.add(new Rechnungen(30302,"erstellt","2016-05-16",2450.45F,150.00F,15.00F,"PRE"));
//			billdata.add(new Rechnungen(30514,"verschickt","2016-05-14",5300.00F,0.00F,0.00F,"CORP"));
						
			
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
	
	@FXML	public void actiongetcosttrackingoverview() {
		// lbl_dbconnect.setText("Mouse geklickt!");

		set_allunvisible();
		scroll_pane_costtrackingoverview.setVisible(true);
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
		    if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
			
			Statement stmt = conn.createStatement();
			
			// Rechnungen-übersicht abrufen, die noch nicht bezahlt sind
			
			// Rechnungen-übersicht abrufen
			ResultSet rs = stmt.executeQuery("SELECT auftraege.*, angebote.*, fluege.datum_von, fluege.datum_bis, kunden.*, flugzeugtypen.flugzeugtyp, rechnungen.* FROM auftraege inner join angebote INNER JOIN fluege on angebote.angebote_id=fluege.angebote_Angebote_ID inner join kunden inner join flugzeuge inner join flugzeugtypen on angebote.kunden_kunde_id= kunden.kunde_id inner join rechnungen on rechnungen.auftraege_auftraege_id=auftraege.auftraege_id and angebote.flugzeuge_Flugzeug_ID=flugzeuge.Flugzeug_ID and flugzeuge.Flugzeugtypen_Flugzeugtypen_ID=flugzeugtypen.Flugzeugtypen_ID and auftraege.angebote_angebote_id = angebote.angebote_id and rechnungen.rechnungsstatus_rechnungsstatus<>'bezahlt'");
		

		
			
			
			
			costbilldata.remove(0, costbilldata.size());
			int i = 1;
			// Testbeginn
			// rs = null;
			 // Testende
			
			while ((rs != null) && (rs.next())) {
				System.out.println(i++ + " " + rs.getInt(46) + " " + rs.getString(49) + " " + rs.getString(33) + " " + rs.getString(48) + " " + rs.getFloat(11)+ " " + rs.getFloat(17)+ " " + rs.getFloat(13)+ " " + rs.getString(44) );
				
				costbilldata.add(new RechnungenCost(rs.getInt(46), rs.getString(49), rs.getString(33), rs.getString(48), rs.getFloat(11), rs.getFloat(17), rs.getFloat(13), rs.getString(44)));
		
				
		}
		//wenn die Datenbank bei der Entwicklung leer ist
//			billdata.remove(0, billdata.size());
//			billdata.add(new Rechnungen(30302,"erstellt","2016-05-16",2450.45F,150.00F,15.00F,"PRE"));
//			billdata.add(new Rechnungen(30514,"verschickt","2016-05-14",5300.00F,0.00F,0.00F,"CORP"));
						
			
			if (costbilldata.size()== 0 ) lbl_dbconnect.setText("keine Rechnungen vorhanden");
			
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
	

	
	@FXML	public void actiongetcosttrackingreminder_warnings() {
		// lbl_dbconnect.setText("Mouse geklickt!");

		set_allunvisible();
		scroll_pane_costtrackingreminder_warnings.setVisible(true);
		costtrackingreminder_warnings.setVisible(true);
		apa_btn_costtrackingreminder.setVisible(true);
		maskentitel.setVisible(true);
		maskentitel.setText("Zahlungserinnerungen/-Mahnungen");
		
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
		    if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
			
			Statement stmt = conn.createStatement();
			
			// Rechnungen-übersicht abrufen, deren Zahlungstermin überschritten ist
			
			ResultSet rs = stmt.executeQuery("SELECT auftraege.*, angebote.*, fluege.datum_von, fluege.datum_bis, kunden.*, flugzeugtypen.flugzeugtyp, rechnungen.* FROM auftraege inner join angebote INNER JOIN fluege on angebote.angebote_id=fluege.angebote_Angebote_ID inner join kunden inner join flugzeuge inner join flugzeugtypen on angebote.kunden_kunde_id= kunden.kunde_id inner join rechnungen on rechnungen.auftraege_auftraege_id=auftraege.auftraege_id and angebote.flugzeuge_Flugzeug_ID=flugzeuge.Flugzeug_ID and flugzeuge.Flugzeugtypen_Flugzeugtypen_ID=flugzeugtypen.Flugzeugtypen_ID and auftraege.angebote_angebote_id=angebote.angebote_id and rechnungen.rechnungsstatus_rechnungsstatus<>'bezahlt'");
		
			
			GregorianCalendar now = new GregorianCalendar();
			
			costreminder_warnings_billdata.remove(0, costreminder_warnings_billdata.size());
			int i = 1;
			// Testbeginn
			// rs = null;
			 // Testende
			
			while ((rs != null) && (rs.next())) {
				System.out.println(i++ + " " + rs.getInt(46) + " " + rs.getString(49) + " " + rs.getString(33) + " " + rs.getString(48) + " " + rs.getFloat(11)+ " " + rs.getFloat(17)+ " " + rs.getFloat(51)+ " " + rs.getString(44) );
			
		//		if (now.after(rs.getDate(48))) {
				System.out.println(now.after(rs.getDate(48)));
				System.out.println(now.getTime());
				costreminder_warnings_billdata.add(new RechnungenCostreminder(rs.getInt(46), rs.getString(49), rs.getString(33), rs.getString(48), rs.getFloat(11), rs.getFloat(17), rs.getFloat(51), rs.getString(44)));
				}
				
		//	}
		//wenn die Datenbank bei der Entwicklung leer ist
//			billdata.remove(0, billdata.size());
//			billdata.add(new Rechnungen(30302,"erstellt","2016-05-16",2450.45F,150.00F,15.00F,"PRE"));
//			billdata.add(new Rechnungen(30514,"verschickt","2016-05-14",5300.00F,0.00F,0.00F,"CORP"));
						
			
			if (costreminder_warnings_billdata.size()== 0 ) lbl_dbconnect.setText("keine Rechnungen vorhanden");
			
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
	


		
		
	
	
	@FXML	public void angebotedit_click(ActionEvent event) throws SQLException {

		// System.out.println(Kdname.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex()));
		set_allunvisible(); 
		auftragändernform.setVisible(true);
		ancpanebtn_changeorder.setVisible(true);
		scroll_pane_changeorder.setVisible(true);
		maskentitel.setVisible(true);
		maskentitel.setText("Auftragstatus ändern");
		choiceorderstatus.getItems().clear();
		choiceorderstatus.getItems().addAll("offen","positiv","negativ");
	
		//angebote_id übernehmen
		int angebot_id = Nummerorder.getCellData(auftragtable.getSelectionModel().getSelectedIndex());
		
		// Kundenname
				String sql = "select kunden.kundename from kunden inner join angebote on kunden.kunde_id=angebote.kunden_kunde_id and angebote.angebote_id='"
				+ angebot_id + "'";

				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
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
				sql = "select Flugzeugtyp from flugzeugtypen inner join flugzeuge inner join angebote on flugzeuge.flugzeug_ID = angebote.flugzeuge_Flugzeug_ID where angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				flgztyp1.setText(rs.getString(1));
				System.out.println(rs.getString(1));
		//Flugzeugkennzeichen
				sql = "select angebote.flugzeuge_flugzeug_id from angebote where angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				flgzkz1.setText(rs.getString(1));
				System.out.println(rs.getString(1));
		//Datum von
				sql = "select angebote.termin_von from angebote where angebote.angebote_id='"+angebot_id+"'";
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
				sql = "select angebote.termin_bis from angebote where angebote.angebote_id='"+angebot_id+"'";
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

		//Abflugort
				sql = "select flughafen_von.flughafenname from flughafen_von inner join fluege on flughafen_von.FlughafenKuerzel=fluege.flughafen_von_FlughafenKuerzel inner join angebote on angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				abflugort1.setText(rs.getString(1));
				System.out.println(rs.getString(1));				
		//Ankunftort
				sql = "select flughafen_bis.flughafenname from flughafen_bis inner join fluege on flughafen_bis.FlughafenKuerzel=fluege.flughafen_bis_FlughafenKuerzel inner join angebote on angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				ankunftort1.setText(rs.getString(1));
				System.out.println(rs.getString(1));				
					
						
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
		set_allunvisible(); 
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
		set_allunvisible(); 
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
		float tmpzusatzkostenextracostedit = Preiscostreminder_warnings_bill_zusatzkosten.getCellData(costreminder_warnings_billtable.getSelectionModel().getSelectedIndex());
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
		String sql = "select angebote.angebote_id from angebote inner join rechnungen inner join auftraege on angebote.angebote_id = auftraege.angebote_angebote_id and auftraege.auftraege_id = rechnungen.auftraege_auftraege_id where rechnungen.rechnungen_ID = '"+rechnung_id+"'";

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
		}

		if (AuswahlDokutyp == "PDF") {
			erzeugePdf(angebot_id, tmprechnungstatusextracostedit);
		}

		if (AuswahlDokutyp == "Word") {
			erzeugeWord(angebot_id, tmprechnungstatusextracostedit);

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
		}

		if (AuswahlAktion == "Drucken" && AuswahlDokutyp == "PDF") {

			filename = System.getProperty("user.dir") + "/" + Integer.toString(angebot_id) + "m.pdf";
			f = new File(filename);

			PDFPrinter druck = new PDFPrinter(f);
			lbl_dbconnect.setText("PDF-Ausdruck gestartet");

		}
		if (AuswahlAktion == "Drucken" && AuswahlDokutyp == "Word") {

			strFilenamedoc = Integer.toString(angebot_id) + "m.docx";
			Druckjob druck = new Druckjob(strFilenamedoc);
			lbl_dbconnect.setText("Docx-Ausdruck gestartet");

		}

	}
	
	@FXML public void actiongetcosttrackingreminder (ActionEvent event) {
		actiongetcosttrackingreminder_warnings();
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
	public void createorder(ActionEvent event) throws SQLException {
		System.out.println(Nummer.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex()));
		set_allunvisible();
		scroll_pane_order.setVisible(true);
		scroll_pane_order.setVvalue(0);;
		ancpane_createorder.setVisible(true);
		ancpanebtn_createorder.setVisible(true);
		maskentitel.setVisible(true);
		maskentitel.setText("Auftrag erstellen");
		
		//angebote_id übernehmen
		int angebot_id = Nummer.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex());
		
		// Daten für Auftrag erstellen einlesen
				String sql = "SELECT angebote.*, fluege.datum_von, fluege.datum_bis, kunden.*, flugzeugtypen.flugzeugtyp FROM angebote INNER JOIN fluege on angebote.angebote_id=fluege.angebote_Angebote_ID inner join kunden inner join flugzeuge inner join flugzeugtypen on angebote.kunden_kunde_id= kunden.kunde_id and angebote.flugzeuge_Flugzeug_ID=flugzeuge.Flugzeug_ID and flugzeuge.Flugzeugtypen_Flugzeugtypen_ID=flugzeugtypen.Flugzeugtypen_ID where angebote.angebote_id = '"+ angebot_id + "'";

				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				rs.next();
			kdname.setText(rs.getString(29));
			System.out.println(rs.getString(29));

			kdvname.setText(rs.getString(30));
				System.out.println(rs.getString(30));

			artcharter.setText(rs.getString(4));
				System.out.println(rs.getString(4));
			flgztyp.setText(rs.getString(41));
				System.out.println(rs.getString(41));
			flgzkz.setText(rs.getString(16));
				System.out.println(rs.getString(16));
				// Create an instance of SimpleDateFormat used for formatting 
				// the string representation of date (month/day/year)
				DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
				
				// Using DateFormat format method we can create a string 
				// representation of a date with the defined format.
				String reportDate = df.format(rs.getObject(25));
				System.out.println(reportDate);
				datumvon.setPromptText(reportDate);
				// Create an instance of SimpleDateFormat used for formatting 
				// the string representation of date (month/day/year)
				//DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					      
				// Using DateFormat format method we can create a string 
				// representation of a date with the defined format.
				
				reportDate = df.format(rs.getObject(26));
				System.out.println(reportDate);
				datumbis.setPromptText(reportDate);
				//Charterdauer
			//	System.out.println(rs.getTime(14));		
		//		charterdauer.setText(Object.toString(rs.getObject(14)));
				//Flugzeit
		//		System.out.println(rs.getString(15));		
			//	charterdauer.setText(rs.getString(15));
				//Preis netto
				System.out.println(rs.getInt(8));		
				preisnetto.setText(Integer.toString(rs.getInt(8)));
				int Preisnetto = rs.getInt(8);
				// Preis Brutto
				System.out.println(rs.getInt(7));		
				preisbrutto.setText(Integer.toString(rs.getInt(7)));
				int Preisbrutto = rs.getInt(7);					
		//Preis Mwst
				int Preismwst = Preisbrutto - Preisnetto;
				System.out.println(Preismwst);		
				preismwst.setText(Integer.toString(Preismwst));
				
		//Abflugort
				sql = "select flughafen_von.flughafenname from flughafen_von inner join fluege on flughafen_von.FlughafenKuerzel=fluege.flughafen_von_FlughafenKuerzel inner join angebote on angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				abflugort.setText(rs.getString(1));
				System.out.println(rs.getString(1));				
		//Ankunftort
				sql = "select flughafen_bis.flughafenname from flughafen_bis inner join fluege on flughafen_bis.FlughafenKuerzel=fluege.flughafen_bis_FlughafenKuerzel inner join angebote on angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				ankunftort.setText(rs.getString(1));
				System.out.println(rs.getString(1));				
					

				
	}

	@FXML
	public void action_createorder(ActionEvent event) throws Exception {
		// gewähltes Angebot dessen Daten für Speicherung Auftrag übernehmen
		int angebot_id = Nummer.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex());
		// String tmpstatus =
		// Status.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex());
		String tmpAart = Aart.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex());
		String tmpkdgruppe = Kdgruppe.getCellData(angebotetabelle.getSelectionModel().getSelectedIndex());

		// Prüfung, ob Auftrag bereits angelegt ist
		String sql = "select auftraege.angebote_angebote_id from auftraege where auftraege.angebote_angebote_id='" + angebot_id
				+ "'";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if ((rs != null) && (rs.next()))
			lbl_dbconnect.setText("Auftrag bereits vorhanden");
		else {

			// ermittle nächste Auftrags-ID Speichern eines Auftrags

			sql = "select max(auftraege_id) from auftraege";
			rs = stmt.executeQuery(sql);
			rs.next();
			int newauftraege_id = (rs.getInt(1) / 10000 + 1) * 10000 + 2016;

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
			}

			if (AuswahlDokutyp == "PDF") {
				erzeugePdf(angebot_id,"Auftrag");
			}

			if (AuswahlDokutyp == "Word") {
				erzeugeWord(angebot_id, "Auftrag");

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
				sql = "select kunden.KundeAnrede from kunden inner join angebote on kunden.kunde_id=angebote.kunden_kunde_id and angebote.angebote_id='"
						+ angebot_id + "'";

				// Statement stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getString(1));
				String kundenanrede = rs.getString(1);

				// Kundenname
				sql = "select kunden.kundename from kunden inner join angebote on kunden.kunde_id=angebote.kunden_kunde_id angebote.angebote_id = '"
						+ angebot_id + "'";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getString(1));
				String Kunde = rs.getString(1);
				// Datum von
				sql = "select angebotstermin.datum_von from angebotstermin inner join angebote on angebote.angebote_id=angebotstermin.angebote_angebote_id where angebote.angebote_id='"
						+ angebot_id + "'";
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
				sql = "select kunden.kundeemail from kunden inner join angebote on kunden.kunde_id=angebote.kunden_kunde_id and angebote.angebote_id='"
						+ angebot_id + "'";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getString(1));
				String mailadresse = rs.getString(1);

				// String Kunde = "Burggraf";
				// int Nummer = 100302;
				// String Datum = "10.06.2016";
				Mainmail mail = new Mainmail(kundenanrede, Kunde, angebot_id, Datum, mailadresse);

			}
		}

	}
	
    
	
	
	@FXML
	public void change_billstatus(ActionEvent event) throws SQLException {
		set_allunvisible();
		scrollpane_changebillstatus.setVisible(true);
		scrollpane_changebillstatus.setVvalue(0);;
		apa_formchangebillstatus.setVisible(true);
		ancpanebtn_changebillstatus.setVisible(true);
		maskentitel.setVisible(true);
		maskentitel.setText("Rechnungsstatus ändern");
		choicebillstatus.getItems().clear();
		choicebillstatus.getItems().addAll("erstellt","verschickt");

		//rechnung_id übernehmen
		int rechnung_id = Nummerbill.getCellData(billtable.getSelectionModel().getSelectedIndex());
		

	
		
		
		// Kundenname
				String sql = "select kunden.kundename from kunden inner join angebote inner join rechnungen on kunden.kunde_id=angebote.kunden_kunde_id and rechnungen.rechnungen_id='"
				+ rechnung_id + "'";

				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				rs.next();
			kdname2.setText(rs.getString(1));
			System.out.println(rs.getString(1));

		// Kundenvorname
				sql = "select kunden.kundevorname from kunden inner join angebote inner join rechnungen on kunden.kunde_id=angebote.kunden_kunde_id and rechnungen.rechnungen_id='"
				+ rechnung_id + "'";
				rs = stmt.executeQuery(sql);
				rs.next();
				kdvname2.setText(rs.getString(1));
				System.out.println(rs.getString(1));

		//Art des Charters
				sql = "select angebote.chartertyp_chartertyp from angebote inner join auftraege inner join rechnungen on rechnungen.auftraege_auftraege_id=auftraege.auftraege_id and auftraege.angebote_angebote_id=angebote.angebote_id where rechnungen.rechnungen_id='"+rechnung_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				artcharter2.setText(rs.getString(1));
				System.out.println(rs.getString(1));
		//Flugzeugtyp
				sql = "select flugzeugtypen.flugzeugtyp from flugzeugtypen inner join flugzeuge on flugzeugtypen.flugzeugtypen_id=flugzeuge.flugzeugtypen_flugzeugtypen_id inner join angebote on flugzeuge.flugzeug_id=angebote.flugzeuge_flugzeug_id inner join auftraege inner join rechnungen on rechnungen.auftraege_auftraege_id=auftraege.auftraege_id and auftraege.angebote_angebote_id=angebote.angebote_id where rechnungen.rechnungen_id='"+rechnung_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				flgztyp2.setText(rs.getString(1));
				System.out.println(rs.getString(1));
		//Flugzeugkennzeichen
				sql = "select flugzeuge.flugzeug_id from flugzeuge inner join angebote on flugzeuge.flugzeug_id=angebote.flugzeuge_flugzeug_id inner join auftraege inner join rechnungen on rechnungen.auftraege_auftraege_id=auftraege.auftraege_id and auftraege.angebote_angebote_id=angebote.angebote_id where rechnungen.rechnungen_id='"+rechnung_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				flgzkz2.setText(rs.getString(1));
				System.out.println(rs.getString(1));
		//Datum von
				sql = "select angebote.termin_von from angebote inner join auftraege inner join rechnungen on rechnungen.auftraege_auftraege_id=auftraege.auftraege_id and auftraege.angebote_angebote_id=angebote.angebote_id where rechnungen.rechnungen_id='"+rechnung_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				// Create an instance of SimpleDateFormat used for formatting 
				// the string representation of date (month/day/year)
				DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

					      
				// Using DateFormat format method we can create a string 
				// representation of a date with the defined format.
				String reportDate = df.format(rs.getObject(1));
				System.out.println(reportDate);
				datumvon2.setPromptText(reportDate);
				//datumvon.setPromptText(rs.getString(1));
				//System.out.println(rs.getString(1));
		//Datum bis
				sql = "select angebote.termin_bis from angebote inner join auftraege inner join rechnungen on rechnungen.auftraege_auftraege_id=auftraege.auftraege_id and auftraege.angebote_angebote_id=angebote.angebote_id where rechnungen.rechnungen_id='"+rechnung_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				// Create an instance of SimpleDateFormat used for formatting 
				// the string representation of date (month/day/year)
				//DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					      
				// Using DateFormat format method we can create a string 
				// representation of a date with the defined format.
				reportDate = df.format(rs.getObject(1));
				System.out.println(reportDate);
				datumbis2.setPromptText(reportDate);
		//Abflugort
				sql = "select flughafen_von.flughafenname from flughafen_von inner join fluege on flughafen_von.FlughafenKuerzel=fluege.flughafen_von_FlughafenKuerzel inner join angebote inner join rechnungen inner join auftraege on fluege.angebote_angebote_id=angebote.angebote_id and auftraege.angebote_angebote_id=angebote.angebote_id and rechnungen.auftraege_auftraege_id=auftraege.auftraege_id where rechnungen.rechnungen_id='"+rechnung_id+"'";    
				rs = stmt.executeQuery(sql);
				rs.next();
				abflugort2.setText(rs.getString(1));
				System.out.println(rs.getString(1));				
		//Ankunftort
				sql = "select flughafen_bis.flughafenname from flughafen_bis inner join fluege on flughafen_bis.FlughafenKuerzel=fluege.flughafen_bis_FlughafenKuerzel inner join angebote inner join rechnungen inner join auftraege on fluege.angebote_angebote_id=angebote.angebote_id and auftraege.angebote_angebote_id=angebote.angebote_id and rechnungen.auftraege_auftraege_id=auftraege.auftraege_id where rechnungen.rechnungen_id='"+rechnung_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				ankunftort2.setText(rs.getString(1));
				System.out.println(rs.getString(1));				
		//Preis netto
				sql = "select angebote.angebotspreis_netto from angebote inner join rechnungen inner join auftraege on auftraege.angebote_angebote_id=angebote.angebote_id and rechnungen.auftraege_auftraege_id=auftraege.auftraege_id where rechnungen.rechnungen_id='"+rechnung_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getFloat(1));		
				preisnetto2.setText(Float.toString(rs.getFloat(1)));
				float Preisnetto = rs.getFloat(1);
		//Preis brutto
				sql = "select angebote.angebotspreis_brutto from angebote inner join rechnungen inner join auftraege on auftraege.angebote_angebote_id=angebote.angebote_id and rechnungen.auftraege_auftraege_id=auftraege.auftraege_id where rechnungen.rechnungen_id='"+rechnung_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				System.out.println(rs.getInt(1));		
				preisbrutto2.setText(Float.toString(rs.getFloat(1)));
				float Preisbrutto = rs.getFloat(1);					
		//Preis Mwst
				float Preismwst = Preisbrutto - Preisnetto;
				System.out.println(Preismwst);		
				preismwst2.setText(Float.toString(Preismwst));

	
	
	
	
	}
	
	public void erzeugePdf(int angebot_id, String modus) throws Exception {
	
		Document document = new Document(PageSize.A4);
		document.setMargins(50f, 40f, 50f, 40f);
		if (modus == "Auftrag") {
			filename = System.getProperty("user.dir") + "/" + Integer.toString(angebot_id) + ".pdf";
		} else {
			filename = System.getProperty("user.dir") + "/" + Integer.toString(angebot_id) + "m.pdf";
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

		Image image = Image.getInstance(PdfGenerator.class.getResource("logo2.jpg"));
		image.scaleAbsolute(507,40);
		document.add(image);
		// Dokumententitel (mit Rahmen!)
		border.setActive(true);
		Paragraph p = new Paragraph("", styleTitel);
		p.setAlignment(Element.ALIGN_LEFT);
		document.add(p);
		border.setActive(false);
		// Parameter für Dokumenterstellung
		
	
		// Daten für Auftrag erstellen einlesen
		String sql = "SELECT angebote.*, fluege.datum_von, fluege.datum_bis, kunden.*, flugzeugtypen.flugzeugtyp FROM angebote INNER JOIN fluege on angebote.angebote_id=fluege.angebote_Angebote_ID inner join kunden inner join flugzeuge inner join flugzeugtypen on angebote.kunden_kunde_id= kunden.kunde_id and angebote.flugzeuge_Flugzeug_ID=flugzeuge.Flugzeug_ID and flugzeuge.Flugzeugtypen_Flugzeugtypen_ID=flugzeugtypen.Flugzeugtypen_ID where angebote.angebote_id = '"+ angebot_id + "'";

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
	String AG = rs.getString(30);
	AG = AG+" "+rs.getString(29);
	String Typ=rs.getString(41);
	String Kennzeichen =rs.getString(16);
	
	 //artcharter.setText(rs.getString(4));
		
		// Create an instance of SimpleDateFormat used for formatting 
		// the string representation of date (month/day/year)
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		
		// Using DateFormat format method we can create a string 
		// representation of a date with the defined format.
		String Beginndatum = df.format(rs.getObject(25));
		
		// Create an instance of SimpleDateFormat used for formatting 
		// the string representation of date (month/day/year)
		//DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			      
		// Using DateFormat format method we can create a string 
		// representation of a date with the defined format.
		
		String Endedatum = df.format(rs.getObject(26));
		//Charterdauer
	//	System.out.println(rs.getTime(14));		
//		charterdauer.setText(Object.toString(rs.getObject(14)));
		//Flugzeit
//		System.out.println(rs.getString(15));		
	//	charterdauer.setText(rs.getString(15));
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
		
		//Abflugort
		sql = "select flughafen_von.flughafenname from flughafen_von inner join fluege on flughafen_von.FlughafenKuerzel=fluege.flughafen_von_FlughafenKuerzel inner join angebote on angebote.angebote_id='"+angebot_id+"'";
		rs = stmt.executeQuery(sql);
		rs.next();
		String FlugAnfang = rs.getString(1);
						
//Ankunftort
		sql = "select flughafen_bis.flughafenname from flughafen_bis inner join fluege on flughafen_bis.FlughafenKuerzel=fluege.flughafen_bis_FlughafenKuerzel inner join angebote on angebote.angebote_id='"+angebot_id+"'";
		rs = stmt.executeQuery(sql);
		rs.next();
		String FlugEnde = rs.getString(1);
						
				
		
			
		
							
						//String AG = "Erich";
						//String Typ = "Dornier";
						//String Kennzeichen = "120";
						//String Beginndatum = "20.05.2016";
						//String Endedatum = "01.06.2016";
						//String FlugAnfang = "München";
						//String FlugEnde = "New York";
						String Zwischen1 = "Paris";
						String Zwischen2 = "London";
						String Zwischen3 = "Reykjavík";
						String Charterdauer = "124:30 h";
						String Flugzeit = "24:45";
						//String Preisnetto = "1.450,00 EUR";
						//String Mwst = "275,50 EUR";
						//String Preisbrutto = "1.725,50 EUR";
						
						Chunk underline = new Chunk("                                                  ");

		
		switch (modus) {
		
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
				p = new Paragraph("Firma oder Person", styleUeberschrift1);
				p.setAlignment(Element.ALIGN_CENTER);
				// etwas abstand hinter der überschrift
				p.setSpacingAfter(20f);
				document.add(p);
				
				
			
				
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

				
		break;		
		default:

			p = new Paragraph(" ", styleText);
			p.setAlignment(Element.ALIGN_CENTER);
			// etwas abstand hinter der überschrift
			p.setSpacingAfter(6f);
			document.add(p);
			p = new Paragraph(modus, styleUeberschrift1);
			p.setAlignment(Element.ALIGN_LEFT);
			// etwas abstand hinter der überschrift
			p.setSpacingAfter(6f);
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
					break;
		
				
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
	
			
	//*********************************************************************************************************************************************
	//*********************************************************************************************************************************************		
			public void erzeugeWord(int angebot_id, String modus) throws Exception {
				
				if (modus=="Auftrag"){
					filename = System.getProperty("user.dir") + "/"+Integer.toString(angebot_id)+".docx";}
				else {
					filename = System.getProperty("user.dir") + "/"+Integer.toString(angebot_id)+"m.docx";}
				
					
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

				
				
				// Parameter für Dokumenterstellung
				
				String sql = "SELECT angebote.*, fluege.datum_von, fluege.datum_bis, kunden.*, flugzeugtypen.flugzeugtyp FROM angebote INNER JOIN fluege on angebote.angebote_id=fluege.angebote_Angebote_ID inner join kunden inner join flugzeuge inner join flugzeugtypen on angebote.kunden_kunde_id= kunden.kunde_id and angebote.flugzeuge_Flugzeug_ID=flugzeuge.Flugzeug_ID and flugzeuge.Flugzeugtypen_Flugzeugtypen_ID=flugzeugtypen.Flugzeugtypen_ID where angebote.angebote_id = '"+ angebot_id + "'";

				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				rs.next();
			String AG = rs.getString(30);
			AG = AG+" "+rs.getString(29);
			String Typ=rs.getString(41);
			String Kennzeichen =rs.getString(16);
			
			 //artcharter.setText(rs.getString(4));
				
				// Create an instance of SimpleDateFormat used for formatting 
				// the string representation of date (month/day/year)
				DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
				
				// Using DateFormat format method we can create a string 
				// representation of a date with the defined format.
				String Beginndatum = df.format(rs.getObject(25));
				
				// Create an instance of SimpleDateFormat used for formatting 
				// the string representation of date (month/day/year)
				//DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					      
				// Using DateFormat format method we can create a string 
				// representation of a date with the defined format.
				
				String Endedatum = df.format(rs.getObject(26));
				//Charterdauer
			//	System.out.println(rs.getTime(14));		
//				charterdauer.setText(Object.toString(rs.getObject(14)));
				//Flugzeit
//				System.out.println(rs.getString(15));		
			//	charterdauer.setText(rs.getString(15));
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
				
				//Abflugort
				sql = "select flughafen_von.flughafenname from flughafen_von inner join fluege on flughafen_von.FlughafenKuerzel=fluege.flughafen_von_FlughafenKuerzel inner join angebote on angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				String FlugAnfang = rs.getString(1);
								
		//Ankunftort
				sql = "select flughafen_bis.flughafenname from flughafen_bis inner join fluege on flughafen_bis.FlughafenKuerzel=fluege.flughafen_bis_FlughafenKuerzel inner join angebote on angebote.angebote_id='"+angebot_id+"'";
				rs = stmt.executeQuery(sql);
				rs.next();
				String FlugEnde = rs.getString(1);
								
						
				
					
				
									
								//String AG = "Erich";
								//String Typ = "Dornier";
								//String Kennzeichen = "120";
								//String Beginndatum = "20.05.2016";
								//String Endedatum = "01.06.2016";
								//String FlugAnfang = "München";
								//String FlugEnde = "New York";
								String Zwischen1 = "Paris";
								String Zwischen2 = "London";
								String Zwischen3 = "Reykjavík";
								String Charterdauer = "124:30 h";
								String Flugzeit = "24:45";
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
						centerParagraph(mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), "Firma oder Person"));
						doBoldFormat(getFirstRunOfParagraph(getLastParagraph(mdp)));
		
								
				mdp.addParagraphOfText(AG+" chartert das Luftfahrzeug "+Typ+" "+Kennzeichen+" für die Zeit vom "+Beginndatum+" zum "+Endedatum+" zu einer Reise von "+FlugAnfang+" nach "+FlugEnde+" über "+Zwischen1+", "+Zwischen2+", "+Zwischen3+".");
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
		
				break;
			default :
			
				//centerParagraph(mdp.addParagraphOfText(
				//		"Ganz normaler Text."));
				mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), modus);
				doBoldFormat(getFirstRunOfParagraph(getLastParagraph(mdp)));
				mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), "Sehr geehrte(r) "+AG+",");
				mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), "nachdem Sie auf unsere Erinnerungen zur Begleichung unserer Rechnung nicht reagiert haben, mahnen wir Sie nun an, die Rechnung binnen 7 Tage zu begleichen. Andernfalls sehen wir uns gezwungen, gerichtliche Schritte gegen Sie einzuleiten. ");
				// doBoldFormat(getFirstRunOfParagraph(getLastParagraph(mdp)));
				mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), "Mit freundlichen Grüßen,");
				mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), "HINOTORI Executive AG");
				// doBoldFormat(getFirstRunOfParagraph(getLastParagraph(mdp)));

				
			break;
			
			
			
				
			}
				
				// speichern
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
			
			public void action_drucken() {
				int angebot_id = Nummerorder.getCellData(auftragtable.getSelectionModel().getSelectedIndex());

				filename = System.getProperty("user.dir") + "/"+Integer.toString(angebot_id)+".pdf";
				f = new File(filename);

				
				
				PDFPrinter druck = new PDFPrinter(f);
				lbl_dbconnect.setText("Ausdruck gestartet");
				
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
			Mainmail mail = new Mainmail(kundenanrede,Kunde,angebot_id,Datum,mailadresse);
			
				
				
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
					actiongetaufträge();					
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
	        target.add(GregorianCalendar.DAY_OF_MONTH, -14);
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
		String sql = "delete from auftraege where auftraege.auftraege_id ='" + auftrag_id + "'";
		Statement statement = conn.createStatement();
		try {
			statement.executeUpdate(sql);
			lbl_dbconnect.setText("Auftrag gelöscht");
			actiongetaufträge();
		} catch (SQLException sqle) {

			lbl_dbconnect.setText("Datenbankverbindung fehlgeschlagen");
				sqle.printStackTrace();
		}
	}

	@FXML public void action_get_dashboard () {
		 	
			set_allunvisible();
			apa_login.setVisible(false);
			apa_btn_login.setVisible(true);
		    apa_welcome.setVisible(true);
		    lbl_username.setText(user);
		    
		    btn_login.setVisible(false);
		    btn_change_user.setVisible(true);
		    
	}
	@FXML public void action_change_user (ActionEvent event) {
	 	
		set_allunvisible();
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
		float tmpzusatzkostenextracostedit = Float.parseFloat(zusatzkostenextracostedit.getText());
		Statement stmt = conn.createStatement();
		try {
		stmt.executeUpdate("Update rechnungen set zusatzkosten_mahnungen='"+tmpzusatzkostenextracostedit+"' where rechnungen.rechnungen_id='"+rechnung_id+"'");
		lbl_dbconnect.setText("Änderung gespeichert");	
		actiongetcosttrackingreminder_warnings();
	} catch (SQLException sqle) {

		lbl_dbconnect.setText("Datenbankverbindung fehlgeschlagen");
		// System.out.println("geht nicht");
		sqle.printStackTrace();
	}
	}	
	
	
	
	
}
