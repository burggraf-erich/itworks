package application;

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
	private SimpleStringProperty Status = null;
	private SimpleStringProperty Aart = null;
	private SimpleStringProperty Kdgruppe = null;
	
	

	public Angebote(Integer Nummer, String Status, String Aart, String Kdgruppe) {
        this.Nummer = new SimpleIntegerProperty(Nummer);
        this.Status = new SimpleStringProperty(Status);
        this.Aart = new SimpleStringProperty(Aart);
        this.Kdgruppe = new SimpleStringProperty(Kdgruppe);
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
    public String getStatus() {
        return Status.get();
    }

    public void setStatus(String Status) {
        this.Status.set(Status);
    }

    public StringProperty StatusProperty() {
        return Status;
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

}
