package application;
//V2.00
//wird benötigt für Datenversorgung Tabellen mit Variablen
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;

public class Flugzeugdaten {

	private IntegerProperty fid = null;
	private SimpleStringProperty fstatus = null;
	private SimpleStringProperty fname = null;
	private SimpleStringProperty ftyp = null;
	
	
//Konstruktor für Instanzerzeugung 
	public Flugzeugdaten(Integer fid, String fstatus, String fname, String ftyp) {
   this.fid = new SimpleIntegerProperty(fid);
     this.fstatus = new SimpleStringProperty(fstatus);
   this.fname = new SimpleStringProperty(fname);
   this.ftyp = new SimpleStringProperty(ftyp);
   
  
	}
	public int getfid() {
   return fid.get();
}

public void setfid(int fid) {
   this.fid.set(fid);
}

public IntegerProperty fidProperty() {
   return fid;
}
public String getftyp() {
   return ftyp.get();
}

public void setftyp(String ftyp) {
   this.ftyp.set(ftyp);
}

public StringProperty ftypProperty() {
   return ftyp;
}

public String getfstatus() {
   return fstatus.get();
}

public void setfstatus(String fstatus) {
   this.fstatus.set(fstatus);
}

public StringProperty fstatusProperty() {
   return fstatus;
}

public String getfname() {
   return fname.get();
}

public void setfname(String fname) {
   this.fname.set(fname);
}

public StringProperty fnameProperty() {
   return fname;
}





}
