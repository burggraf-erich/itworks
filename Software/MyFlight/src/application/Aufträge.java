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
	private SimpleStringProperty datumauftragorder = null;
	private SimpleStringProperty Statusorder = null;
	private SimpleStringProperty Aartorder = null;
	private SimpleStringProperty Kdgruppeorder = null;
	private SimpleStringProperty Kdnameorder = null;
	private SimpleStringProperty Kdvnameorder = null;
	private SimpleStringProperty Flgztyporder = null;
	private SimpleStringProperty Beginnorder = null;
	private SimpleStringProperty Endeorder = null;
	private IntegerProperty billorder = null;
	
	
	
// Konstruktor für Instanzerzeugung
	public Aufträge(Integer Nummer, String datumauftragorder, String Status, String Aart, String Kdgruppe, String Kdnameorder, String Kdvnameorder, String Flgztyporder, String Beginnorder, String Endeorder, int billorder) {
        this.Nummerorder = new SimpleIntegerProperty(Nummer);
        this.datumauftragorder = new SimpleStringProperty(datumauftragorder);
        this.Statusorder = new SimpleStringProperty(Status);
        this.Aartorder = new SimpleStringProperty(Aart);
        this.Kdgruppeorder = new SimpleStringProperty(Kdgruppe);
        this.Flgztyporder = new SimpleStringProperty(Flgztyporder);
        this.Beginnorder = new SimpleStringProperty(Beginnorder);
        this.Endeorder = new SimpleStringProperty(Endeorder);
        this.Kdnameorder = new SimpleStringProperty(Kdnameorder);
        this.Kdvnameorder = new SimpleStringProperty(Kdvnameorder);
        this.billorder = new SimpleIntegerProperty(billorder);
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
	public int getbillorder() {
        return billorder.get();
    }

    public void setbillorder(int bill) {
        this.billorder.set(bill);
    }    
    public IntegerProperty billorderProperty() {
        return billorder;
    }
    public String getFlgztyporder() {
        return Flgztyporder.get();
    }

    public void setFlgztyporder(String Flgztyp) {
        this.Flgztyporder.set(Flgztyp);
    }
    public StringProperty FlgztyporderProperty() {
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
    public String getdatumauftragorder() {
        return datumauftragorder.get();
    }

    public void setdatumauftragorder(String datumauftragorder) {
        this.datumauftragorder.set(datumauftragorder);
    }
    public StringProperty datumauftragorderProperty() {
        return datumauftragorder;
    }


}
