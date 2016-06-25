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

public class Personaldaten {

	private IntegerProperty pid = null;
	private SimpleStringProperty pname = null;
	private SimpleStringProperty pvname = null;
	private SimpleStringProperty ppos = null;
	private SimpleStringProperty pstatus = null;
	private IntegerProperty pgehalt = null;
	
//Konstruktor für Instanzerzeugung 
	public Personaldaten(Integer pid, String pname, String pvname, String ppos, String pstatus, int pgehalt) {
     this.pid = new SimpleIntegerProperty(pid);
       this.pname = new SimpleStringProperty(pname);
     this.pvname = new SimpleStringProperty(pvname);
     this.ppos = new SimpleStringProperty(ppos);
     this.pstatus = new SimpleStringProperty(pstatus);
     this.pgehalt = new SimpleIntegerProperty(pgehalt);
    
	}
	public int getpid() {
     return pid.get();
 }

 public void setpid(int pid) {
     this.pid.set(pid);
 }

 public IntegerProperty pidProperty() {
     return pid;
 }
 public String getppos() {
     return ppos.get();
 }

 public void setppos(String ppos) {
     this.ppos.set(ppos);
 }

 public StringProperty pposProperty() {
     return ppos;
 }
 
 public String getpstatus() {
     return pstatus.get();
 }

 public void setpstatus(String pstatus) {
     this.pstatus.set(pstatus);
 }

 public StringProperty pstatusProperty() {
     return pstatus;
 }
 public int getpgehalt() {
     return pgehalt.get();
 }

 public void setpgehalt(int pgehalt) {
     this.pgehalt.set(pgehalt);
 }
 
 public IntegerProperty pgehaltProperty() {
     return pgehalt;
 }
 public String getpname() {
     return pname.get();
 }

 public void setpname(String pname) {
     this.pname.set(pname);
 }

 public StringProperty pnameProperty() {
     return pname;
 }
 public String getpvname() {
     return pvname.get();
 }

 public void setpvname(String pvname) {
     this.pvname.set(pvname);
 }

 public StringProperty pvnameProperty() {
     return pvname;
 }




}
