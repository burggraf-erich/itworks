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

public class RechnungenCostreminder {

	private SimpleIntegerProperty Nummercostreminder_warnings_bill = null;
	private SimpleStringProperty Statuscostreminder_warnings_bill = null;
	private SimpleStringProperty Datumcostreminder_warnings_topay = null;
	private SimpleFloatProperty Preiscostreminder_warnings_bill = null;
	private SimpleFloatProperty Preiscostreminder_warnings_bill_aufschlag = null;
	private SimpleFloatProperty Preiscostreminder_warnings_bill_zusatzkosten = null;
	private SimpleStringProperty Kdgruppecostreminder_warnings_bill = null;
	private SimpleIntegerProperty Nummerorder_forcostreminder_warnings_billtable = null;
	private SimpleStringProperty Statusorder_forcostreminder_warnings_billtable = null;
	
	
	

	public RechnungenCostreminder(Integer Nummer, String Status, String Datum, Float Preis, Float Preisaufschlag, Float Zusatzkosten, String Kdgruppe) {
		this.Nummercostreminder_warnings_bill = new SimpleIntegerProperty(Nummer);
		this.Statuscostreminder_warnings_bill = new SimpleStringProperty(Status) ;
		this.Datumcostreminder_warnings_topay = new SimpleStringProperty(Datum) ;
		this.Preiscostreminder_warnings_bill = new SimpleFloatProperty(Preis);
		this.Preiscostreminder_warnings_bill_aufschlag = new SimpleFloatProperty(Preisaufschlag);
		this.Preiscostreminder_warnings_bill_zusatzkosten = new SimpleFloatProperty(Zusatzkosten);
		this.Kdgruppecostreminder_warnings_bill = new SimpleStringProperty(Kdgruppe);
		}
	public int getNummercostreminder_warnings_bill() {
        return Nummercostreminder_warnings_bill.get();
    }
	public String getStatuscostreminder_warnings_bill() {
        return Statuscostreminder_warnings_bill.get();
    }
	public String getDatumcostreminder_warnings_topay() {
        return Datumcostreminder_warnings_topay.get();
    }
	public Float getPreiscostreminder_warnings_bill() {
        return Preiscostreminder_warnings_bill.get();
    }
	public Float getPreiscostreminder_warnings_bill_aufschlag() {
        return Preiscostreminder_warnings_bill_aufschlag.get();
    }
	public Float getPreiscostreminder_warnings_bill_zusatzkosten() {
        return Preiscostreminder_warnings_bill_zusatzkosten.get();
    }
	public String getKdgruppecostreminder_warnings_bill() {
        return Kdgruppecostreminder_warnings_bill.get();
    }
    public void setNummercostreminder_warnings_bill(int Nummer) {
        this.Nummercostreminder_warnings_bill.set(Nummer);
    }
    public void setStatuscostreminder_warnings_bill(String Status) {
        this.Statuscostreminder_warnings_bill.set(Status);
    }
    public void setDatumcostreminder_warnings_topay(String Datum) {
        this.Datumcostreminder_warnings_topay.set(Datum);
    }
    public void setPreiscostreminder_warnings_bill(Float Preis) {
        this.Preiscostreminder_warnings_bill.set(Preis);
    }
    public void setPreiscostreminder_warnings_bill_aufschlag(Float Preisaufschlag) {
        this.Preiscostreminder_warnings_bill_aufschlag.set(Preisaufschlag);
    }
    public void setPreiscostreminder_warnings_bill_zusatzkosten(Float Zusatzkosten) {
        this.Preiscostreminder_warnings_bill_zusatzkosten.set(Zusatzkosten);
    }
    public void setKdgruppecostreminder_warnings_bill(String Kdgruppe) {
        this.Kdgruppecostreminder_warnings_bill.set(Kdgruppe);
    }
    
    public IntegerProperty Nummercostreminder_warnings_billProperty() {
        return Nummercostreminder_warnings_bill;
    }
    public StringProperty Statuscostreminder_warnings_billProperty() {
        return Statuscostreminder_warnings_bill;
    }
    public StringProperty Datumcostreminder_warnings_topayProperty() {
        return Datumcostreminder_warnings_topay;
    }
    public FloatProperty Preiscostreminder_warnings_billProperty() {
        return Preiscostreminder_warnings_bill;
    }
    public FloatProperty Preiscostreminder_warnings_bill_aufschlagProperty() {
        return Preiscostreminder_warnings_bill_aufschlag;
    }
    public FloatProperty Preiscostreminder_warnings_bill_zusatzkostenProperty() {
        return Preiscostreminder_warnings_bill_zusatzkosten;
    }
    public StringProperty Kdgruppecostreminder_warnings_billProperty() {
        return Kdgruppecostreminder_warnings_bill;
    }
    
    
}
