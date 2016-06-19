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

public class RechnungenCost {

	private SimpleIntegerProperty Nummercostbill = null;
	private SimpleStringProperty Statuscostbill = null;
	private SimpleStringProperty Kdnamecostbill = null;
	private SimpleStringProperty Datumcosttopay = null;
	private SimpleFloatProperty Preiscostbill = null;
	private SimpleFloatProperty Preiscostbill_aufschlag = null;
	private SimpleFloatProperty Preiscostbill_zusatzkosten = null;
	private SimpleStringProperty Kdgruppecostbill = null;
	private SimpleIntegerProperty Nummerorder_forcostbilltable = null;
	private SimpleStringProperty Statusorder_forcostbilltable = null;
	
	
	
	public RechnungenCost(Integer Nummer, String Status, String Kdname, String zahltermin, float Preis, float preisaufschlag, float zusatzkosten, String Kdgruppe) {
		this.Nummercostbill = new SimpleIntegerProperty(Nummer);
		this.Statuscostbill = new SimpleStringProperty(Status) ;
		this.Kdnamecostbill = new SimpleStringProperty(Kdname);
		this.Datumcosttopay = new SimpleStringProperty(zahltermin) ;
		this.Preiscostbill = new SimpleFloatProperty(Preis);
		this.Preiscostbill_aufschlag = new SimpleFloatProperty(preisaufschlag);
		this.Preiscostbill_zusatzkosten = new SimpleFloatProperty(zusatzkosten);
		this.Kdgruppecostbill = new SimpleStringProperty(Kdgruppe);
		}
	public int getNummercostbill() {
        return Nummercostbill.get();
    }
	public String getStatuscostbill() {
        return Statuscostbill.get();
    }
	public String getKdnamecostbill() {
        return Kdnamecostbill.get();
    }
	public String getDatumcosttopay() {
        return Datumcosttopay.get();
    }
	public Float getPreiscostbill() {
        return Preiscostbill.get();
    }
	public Float getPreisbcostill_aufschlag() {
        return Preiscostbill_aufschlag.get();
    }
	public Float getPreiscostbill_zusatzkosten() {
        return Preiscostbill_zusatzkosten.get();
    }
	public String getKdgruppecostbill() {
        return Kdgruppecostbill.get();
    }
    public void setNummercostbill(int Nummer) {
        this.Nummercostbill.set(Nummer);
    }
    public void setStatuscostbill(String Status) {
        this.Statuscostbill.set(Status);
    }
    public void setKdnamecostbill(String Status) {
        this.Kdnamecostbill.set(Status);
    }
    public void setDatumcosttopay(String Datum) {
        this.Datumcosttopay.set(Datum);
    }
    public void setPreiscostbill(Float Preis) {
        this.Preiscostbill.set(Preis);
    }
    public void setPreiscostbill_aufschlag(Float Preisaufschlag) {
        this.Preiscostbill_aufschlag.set(Preisaufschlag);
    }
    public void setPreiscostbill_zusatzkosten(Float Zusatzkosten) {
        this.Preiscostbill_zusatzkosten.set(Zusatzkosten);
    }
    public void setKdgruppecostbill(String Kdgruppe) {
        this.Kdgruppecostbill.set(Kdgruppe);
    }
    
    public IntegerProperty NummercostbillProperty() {
        return Nummercostbill;
    }
    public StringProperty StatuscostbillProperty() {
        return Statuscostbill;
    }
    public StringProperty KdnamecostbillProperty() {
        return Kdnamecostbill;
    }
    public StringProperty DatumcosttopayProperty() {
        return Datumcosttopay;
    }
    public FloatProperty PreiscostbillProperty() {
        return Preiscostbill;
    }
    public FloatProperty Preiscostbill_aufschlagProperty() {
        return Preiscostbill_aufschlag;
    }
    public FloatProperty Preiscostbill_zusatzkostenProperty() {
        return Preiscostbill_zusatzkosten;
    }
    public StringProperty KdgruppecostbillProperty() {
        return Kdgruppecostbill;
    }
    
    
}
