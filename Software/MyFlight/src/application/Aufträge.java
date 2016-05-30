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
	
	
// Konstruktor für Instanzerzeugung
	public Aufträge(Integer Nummer, String Status, String Aart, String Kdgruppe) {
        this.Nummerorder = new SimpleIntegerProperty(Nummer);
        this.Statusorder = new SimpleStringProperty(Status);
        this.Aartorder = new SimpleStringProperty(Aart);
        this.Kdgruppeorder = new SimpleStringProperty(Kdgruppe);
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

}
