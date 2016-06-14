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
	private SimpleIntegerProperty Preisbill = null;
	private SimpleFloatProperty Preisbill_aufschlag = null;
	private SimpleFloatProperty Preisbill_zusatzkosten = null;
	private SimpleStringProperty Kdgruppebill = null;
	private SimpleStringProperty Kdnamebill = null;
	private SimpleIntegerProperty Nummerorder_forbilltable = null;
	private SimpleStringProperty Statusorder_forbilltable = null;
	
	
	

	public Rechnungen(Integer Nummer, String Status, int Preis, String Kdgruppe, String Kdname) {
		this.Nummerbill = new SimpleIntegerProperty(Nummer);
		this.Statusbill = new SimpleStringProperty(Status) ;
//		this.Datumtopay = new SimpleStringProperty(Datum) ;
		this.Preisbill = new SimpleIntegerProperty(Preis);
//		this.Preisbill_aufschlag = new SimpleFloatProperty(Preisaufschlag);
//		this.Preisbill_zusatzkosten = new SimpleFloatProperty(Zusatzkosten);
		this.Kdgruppebill = new SimpleStringProperty(Kdgruppe);
		this.Kdnamebill = new SimpleStringProperty(Kdname);	
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
	public int getPreisbill() {
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
    public void setPreisbill(int Preis) {
        this.Preisbill.set(Preis);
    }
    public void setPreisbill_aufschlag(Float Preisaufschlag) {
        this.Preisbill_aufschlag.set(Preisaufschlag);
    }
    public void setPreisbill_zusatzkosten(Float Zusatzkosten) {
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
    public IntegerProperty PreisbillProperty() {
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
