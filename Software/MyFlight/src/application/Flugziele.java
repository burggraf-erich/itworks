package application;
//V2.00
//wird benötigt für Datenversorgung Tabellen mit Variablen
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;

public class Flugziele {

	private SimpleStringProperty fzflgh = null;
	private SimpleStringProperty fzname = null;
	private SimpleStringProperty fzstadt = null;
	private SimpleStringProperty fzland = null;
	
	private SimpleFloatProperty fzlon = null;
	private SimpleFloatProperty fzlat = null;
	
	
//Konstruktor für Instanzerzeugung 
	public Flugziele(String fzflgh , String fzname, String fzstadt, String fzland, float fzlon, float fzlat) {
   this.fzflgh = new SimpleStringProperty(fzflgh);
     this.fzname = new SimpleStringProperty(fzname);
   this.fzstadt = new SimpleStringProperty(fzstadt);
   this.fzland = new SimpleStringProperty(fzland);
   this.fzlon = new SimpleFloatProperty(fzlon);
   this.fzlat = new SimpleFloatProperty(fzlat);
  
	}
	public String getfzflgh() {
   return fzflgh.get();
}

public void setfzflgh(String fzflgh) {
   this.fzflgh.set(fzflgh);
}

public StringProperty fzflghProperty() {
   return fzflgh;
}
public String getfzland() {
   return fzland.get();
}

public void setfzland(String fzland) {
   this.fzland.set(fzland);
}

public StringProperty fzlandProperty() {
   return fzland;
}

public Float getfzlon() {
   return fzlon.get();
}

public void setfzlon(Float fzlon) {
   this.fzlon.set(fzlon);
}

public FloatProperty fzlonProperty() {
   return fzlon;
}
public Float getfzlat() {
   return fzlat.get();
}

public void setfzlat(Float fzlat) {
   this.fzlat.set(fzlat);
}

public FloatProperty fzlatProperty() {
   return fzlat;
}
public String getfzname() {
   return fzname.get();
}

public void setfzname(String fzname) {
   this.fzname.set(fzname);
}

public StringProperty fznameProperty() {
   return fzname;
}
public String getfzstadt() {
   return fzstadt.get();
}

public void setfzstadt(String fzstadt) {
   this.fzstadt.set(fzstadt);
}

public StringProperty fzstadtProperty() {
   return fzstadt;
}




}
