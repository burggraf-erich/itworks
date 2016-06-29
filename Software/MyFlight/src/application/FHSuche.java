package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;

public class FHSuche extends MyFlightController{
	
	private SimpleStringProperty IATA = null;
	private SimpleStringProperty Name = null;
	private SimpleStringProperty Stadt = null;
	private SimpleStringProperty Land = null;
	
	public FHSuche(String IATA,String Name, String Stadt, String Land) {

        this.IATA = new SimpleStringProperty(IATA);
        this.Name = new SimpleStringProperty(Name);
        this.Stadt = new SimpleStringProperty(Stadt);
        this.Land = new SimpleStringProperty(Land);
    }
	
	    public String getIATA_state() {
        return IATA.get();
    }

    public void setIATA(String IATA) {
        this.IATA.set(IATA);
    }

    public StringProperty IATAProperty() {
        return IATA;
    }
    
    public String getName() {
        return Name.get();
    }

    public void setName(String Name)  {
        this.Name.set(Name);
    }

    public StringProperty NameProperty() {
        return Name;
    }

	    public String getStadt() {
        return Stadt.get();
    }

    public void setStadt(String Stadt) {
        this.Stadt.set(Stadt);
    }

    public StringProperty StadtProperty() {
        return Stadt;
    }
    
	public String getLand() {
        return Land.get();
    }

    public void setLand(String Land) {
        this.Land.set(Land);
    }

    public StringProperty LandProperty() {
        return Land;
    }
	

}
