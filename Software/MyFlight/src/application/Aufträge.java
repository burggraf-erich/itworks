package application;
// V1.08
// wird benötigt für Datenversorgung Tabelle mit Variablen
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;

public class Aufträge {

	private IntegerProperty Nummerorder = null;
	private SimpleStringProperty Statusorder = null;
	private SimpleStringProperty Aartorder = null;
	private SimpleStringProperty Kdgruppeorder = null;
	private SimpleStringProperty Kdnameorder = null;
	private SimpleStringProperty Kdvnameorder = null;
	private IntegerProperty Flgztyporder = null;
	private SimpleStringProperty Beginnorder = null;
	private SimpleStringProperty Endeorder = null;
	
	
// Konstruktor für Instanzerzeugung
	public Aufträge(Integer Nummer, String Status, String Aart, String Kdgruppe, String Kdnameorder, String Kdvnameorder, Integer Flgztyporder, String Beginnorder, String Endeorder) {
        this.Nummerorder = new SimpleIntegerProperty(Nummer);
        this.Statusorder = new SimpleStringProperty(Status);
        this.Aartorder = new SimpleStringProperty(Aart);
        this.Kdgruppeorder = new SimpleStringProperty(Kdgruppe);
        this.Flgztyporder = new SimpleIntegerProperty(Flgztyporder);
        this.Beginnorder = new SimpleStringProperty(Beginnorder);
        this.Endeorder = new SimpleStringProperty(Endeorder);
        this.Kdnameorder = new SimpleStringProperty(Kdnameorder);
        this.Kdvnameorder = new SimpleStringProperty(Kdvnameorder);
        
        }
	public int getNummerorder() {
        return Nummerorder.get();
    }

    public void setNummerorder(int Nummer) {
        this.Nummerorder.set(Nummer);
    }    
    public IntegerProperty NummerorderProperty() {
        return Nummerorder;
    }
	public int getFlgztyporder() {
        return Flgztyporder.get();
    }

    public void setFlgztyporder(int Flgztyp) {
        this.Flgztyporder.set(Flgztyp);
    }
    public IntegerProperty FlgztyporderProperty() {
        return Flgztyporder;
    }

    public String getStatusorder() {
        return Statusorder.get();
    }

    public void setStatusorder(String Status) {
        this.Statusorder.set(Status);
    }

    public StringProperty StatusorderProperty() {
        return Statusorder;
    }
    public String getAartorder() {
        return Aartorder.get();
    }

    public void setAartorder(String Aart) {
        this.Aartorder.set(Aart);
    }

    public StringProperty AartorderProperty() {
        return Aartorder;
    }
    
    public String getKdgruppeorder() {
        return Kdgruppeorder.get();
    }

    public void setKdgruppeorder(String Kdgruppe) {
        this.Kdgruppeorder.set(Kdgruppe);
    }

    public StringProperty KdgruppeorderProperty() {
        return Kdgruppeorder;
    }
    public String getKdnameorder() {
        return Kdnameorder.get();
    }

    public void setKdnameorder(String Kdname) {
        this.Kdnameorder.set(Kdname);
    }

    public StringProperty KdnameorderProperty() {
        return Kdnameorder;
    }
    public String getKdvnameorder() {
        return Kdvnameorder.get();
    }

    public void setKdvnameorder(String Kdvname) {
        this.Kdvnameorder.set(Kdvname);
    }

    public StringProperty KdvnameorderProperty() {
        return Kdvnameorder;
    }
    public String getBeginnorder() {
        return Beginnorder.get();
    }

    public void setBeginnorder(String Beginn) {
        this.Beginnorder.set(Beginn);
    }

    public StringProperty BeginnorderProperty() {
        return Beginnorder;
    }
    public String getEndeorder() {
        return Endeorder.get();
    }

    public void setEndeorder(String Ende) {
        this.Endeorder.set(Ende);
    }

    public StringProperty EndeorderProperty() {
        return Endeorder;
    }
}
