package application;
//V2.00
//wird benötigt für Datenversorgung Tabellen mit Variablen
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;

public class Kundendaten {

	private IntegerProperty kdid = null;
	private SimpleStringProperty kdverwname = null;
	private SimpleStringProperty kdverwvname = null;
	private SimpleStringProperty kdfirma = null;
	private SimpleStringProperty kdgruppe = null;
	
//Konstruktor für Instanzerzeugung 
	public Kundendaten(Integer kdid, String kdverwname, String kdverwvname, String kdfirma, String kdgruppe) {
   this.kdid = new SimpleIntegerProperty(kdid);
     this.kdverwname = new SimpleStringProperty(kdverwname);
   this.kdverwvname = new SimpleStringProperty(kdverwvname);
   this.kdfirma = new SimpleStringProperty(kdfirma);
   this.kdgruppe = new SimpleStringProperty(kdgruppe);
   
  
	}
	public int getkdid() {
   return kdid.get();
}

public void setkdid(int kdid) {
   this.kdid.set(kdid);
}

public IntegerProperty kdidProperty() {
   return kdid;
}
public String getkdfirma() {
   return kdfirma.get();
}

public void setkdfirma(String kdfirma) {
   this.kdfirma.set(kdfirma);
}

public StringProperty kdfirmaProperty() {
   return kdfirma;
}

public String getkdgruppe() {
   return kdgruppe.get();
}

public void setkdgruppe(String kdgruppe) {
   this.kdgruppe.set(kdgruppe);
}

public StringProperty kdgruppeProperty() {
   return kdgruppe;
}
public String getkdverwname() {
   return kdverwname.get();
}

public void setkdverwname(String kdverwname) {
   this.kdverwname.set(kdverwname);
}

public StringProperty kdverwnameProperty() {
   return kdverwname;
}
public String getkdverwvname() {
   return kdverwvname.get();
}

public void setkdverwvname(String kdverwvname) {
   this.kdverwvname.set(kdverwvname);
}

public StringProperty kdverwvnameProperty() {
   return kdverwvname;
}




}
