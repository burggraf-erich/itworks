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
	private SimpleStringProperty Kdname = null;
	private SimpleStringProperty Datum = null;
	

	public Angebote(Integer Nummer, String Datum, String Kdname) {
        this.Nummer = new SimpleIntegerProperty(Nummer);
        this.Kdname = new SimpleStringProperty(Kdname);
        this.Datum = new SimpleStringProperty(Datum);
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
    public String getKdname() {
        return Kdname.get();
    }

    public void setKdname(String Kdname) {
        this.Kdname.set(Kdname);
    }

    public StringProperty KdnameProperty() {
        return Kdname;
    }
    public String getDatum() {
        return Datum.get();
    }

    public void setDatum(String Datum) {
        this.Datum.set(Datum);
    }

    public StringProperty DatumProperty() {
        return Datum;
    }
}
