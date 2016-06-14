package application;
// V1.08
// wird benötigt für Datenversorgung Tabellen mit Variablen
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;

public class Angebote {

	private IntegerProperty Nummer = null;
	private SimpleStringProperty Aart = null;
	private SimpleStringProperty Kdgruppe = null;
	private IntegerProperty Flugztyp = null;
	private SimpleStringProperty Datum_von = null;
	private SimpleStringProperty Datum_bis = null;
	private SimpleStringProperty Kdname = null;
	private SimpleStringProperty Kdvname = null;
	
// Konstruktor für Instanzerzeugung 
	public Angebote(Integer Nummer, String Aart, String Kdgruppe, Integer Flugztyp, String Datum_von, String Datum_bis, String Kdname, String Kdvname) {
        this.Nummer = new SimpleIntegerProperty(Nummer);
          this.Aart = new SimpleStringProperty(Aart);
        this.Kdgruppe = new SimpleStringProperty(Kdgruppe);
        this.Flugztyp = new SimpleIntegerProperty(Flugztyp);
        this.Datum_von = new SimpleStringProperty(Datum_von);
        this.Datum_bis = new SimpleStringProperty(Datum_bis);
        this.Kdname = new SimpleStringProperty(Kdname);
        this.Kdvname = new SimpleStringProperty(Kdvname);
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
    public int getFlugztyp() {
        return Flugztyp.get();
    }

    public void setFlugztyp(int Flugztyp) {
        this.Flugztyp.set(Flugztyp);
    }

    public IntegerProperty FlugztypProperty() {
        return Flugztyp;
    }
    
    public String getDatum_von() {
        return Datum_von.get();
    }

    public void setDatum_von(String Datum_von) {
        this.Datum_von.set(Datum_von);
    }

    public StringProperty Datum_vonProperty() {
        return Datum_von;
    }
    public String getDatum_bis() {
        return Datum_bis.get();
    }

    public void setDatum_bis(String Datum_bis) {
        this.Datum_bis.set(Datum_bis);
    }

    public StringProperty Datum_bisProperty() {
        return Datum_bis;
    }
    public String getAart() {
        return Aart.get();
    }

    public void setAart(String Aart) {
        this.Aart.set(Aart);
    }

    public StringProperty AartProperty() {
        return Aart;
    }
    public String getKdgruppe() {
        return Kdgruppe.get();
    }

    public void setKdgruppe(String Kdgruppe) {
        this.Kdgruppe.set(Kdgruppe);
    }

    public StringProperty KdgruppeProperty() {
        return Kdgruppe;
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
    public String getKdvname() {
        return Kdvname.get();
    }

    public void setKdvname(String Kdvname) {
        this.Kdname.set(Kdvname);
    }

    public StringProperty KdvnameProperty() {
        return Kdvname;
    }
}
