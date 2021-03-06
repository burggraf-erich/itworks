package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.FloatProperty;
import java.time.LocalDate;

public class Rechnungen {

	private SimpleIntegerProperty Nummerbill = null;
	private SimpleStringProperty Statusbill = null;
	private SimpleStringProperty Datumtopay = null;
	private SimpleFloatProperty Preisbill = null;
	private SimpleFloatProperty Preisbill_aufschlag = null;
	private SimpleFloatProperty Preisbill_zusatzkosten = null;
	private SimpleStringProperty Kdgruppebill = null;
	private SimpleStringProperty Kdnamebill = null;
	private SimpleIntegerProperty Nummerorder_forbilltable = null;
	private SimpleStringProperty Statusorder_forbilltable = null;
	
	
	

	public Rechnungen(Integer Nummer, String Status, String Kdname, String zahltermin, float Preis, float preisaufschlag, float zusatzkosten, String Kdgruppe) {
		this.Nummerbill = new SimpleIntegerProperty(Nummer);
		this.Statusbill = new SimpleStringProperty(Status) ;
		this.Kdnamebill = new SimpleStringProperty(Kdname);	
		this.Datumtopay = new SimpleStringProperty(zahltermin) ;
		this.Preisbill = new SimpleFloatProperty(Preis);
		this.Preisbill_aufschlag = new SimpleFloatProperty(preisaufschlag);
		this.Preisbill_zusatzkosten = new SimpleFloatProperty(zusatzkosten);
		this.Kdgruppebill = new SimpleStringProperty(Kdgruppe);
	
	}
	public int getNummerbill() {
        return Nummerbill.get();
    }
	public String getStatusbill() {
        return Statusbill.get();
    }
	public String getDatumtopay() {
        return Datumtopay.get();
    }
	public float getPreisbill() {
        return Preisbill.get();
    }
	public float getPreisbill_aufschlag() {
        return Preisbill_aufschlag.get();
    }
	public Float getPreisbill_zusatzkosten() {
        return Preisbill_zusatzkosten.get();
    }
	public String getKdgruppebill() {
        return Kdgruppebill.get();
    }
	public String getKdnamebill() {
        return Kdnamebill.get();
    }
	public void setNummerbill(int Nummer) {
        this.Nummerbill.set(Nummer);
    }
    public void setStatusbill(String Status) {
        this.Statusbill.set(Status);
    }
    public void setDatumtopay(String Datum) {
        this.Datumtopay.set(Datum);
    }
    public void setPreisbill(float Preis) {
        this.Preisbill.set(Preis);
    }
    public void setPreisbill_aufschlag(float Preisaufschlag) {
        this.Preisbill_aufschlag.set(Preisaufschlag);
    }
    public void setPreisbill_zusatzkosten(float Zusatzkosten) {
        this.Preisbill_zusatzkosten.set(Zusatzkosten);
    }
    public void setKdgruppebill(String Kdgruppe) {
        this.Kdgruppebill.set(Kdgruppe);
    }
    public void setKdnamebill(String Kdname) {
        this.Kdnamebill.set(Kdname);
    }
    public IntegerProperty NummerbillProperty() {
        return Nummerbill;
    }
    public StringProperty StatusbillProperty() {
        return Statusbill;
    }
    public StringProperty DatumtopayProperty() {
        return Datumtopay;
    }
    public FloatProperty PreisbillProperty() {
        return Preisbill;
    }
    public FloatProperty Preisbill_aufschlagProperty() {
        return Preisbill_aufschlag;
    }
    public FloatProperty Preisbill_zusatzkostenProperty() {
        return Preisbill_zusatzkosten;
    }
    public StringProperty KdgruppebillProperty() {
        return Kdgruppebill;
    }
    public StringProperty KdnamebillProperty() {
        return Kdnamebill;
    }
    
}
