package application;
//V1.08
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
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;

public class Fluege {

	private SimpleStringProperty tablecoldateabflug = null;
	private SimpleStringProperty tablecoltimeabflug = null;
	private SimpleStringProperty tablecolortabflug = null;
	private SimpleFloatProperty  tablecolflugzeit = null;
	private SimpleStringProperty tablecoltimeankunft = null;
	private SimpleStringProperty tablecolortankunft = null;
	private IntegerProperty tablecolanzahlpax = null;
	
	
//Konstruktor für Instanzerzeugung 
	public Fluege(String tablecoldateabflug, String tablecoltimeabflug, String tablecolortabflug , float tablecolflugzeit , String tablecoltimeankunft , String tablecolortankunft, Integer  tablecolanzahlpax) {
     
		this.tablecoldateabflug = new SimpleStringProperty(tablecoldateabflug);
		this.tablecoltimeabflug = new SimpleStringProperty(tablecoltimeabflug);
		this.tablecolortabflug = new SimpleStringProperty(tablecolortabflug);
		this.tablecolflugzeit = new SimpleFloatProperty(tablecolflugzeit);
		this.tablecoltimeankunft = new SimpleStringProperty(tablecoltimeankunft);
		this.tablecolortankunft = new SimpleStringProperty(tablecolortankunft);
		this.tablecolanzahlpax = new SimpleIntegerProperty(tablecolanzahlpax);
		
		
	}
public int gettablecolanzahlpax() {
     return tablecolanzahlpax.get();
 }

 public void settablecolanzahlpax(int tablecolanzahlpax) {
     this.tablecolanzahlpax.set(tablecolanzahlpax);
 }

 public IntegerProperty tablecolanzahlpaxProperty() {
     return tablecolanzahlpax;
 }
 public Float gettablecolflugzeit() {
	   return tablecolflugzeit.get();
	}

	public void settablecolflugzeit(Float tablecolflugzeit) {
	   this.tablecolflugzeit.set(tablecolflugzeit);
	}

	public FloatProperty tablecolflugzeitProperty() {
	   return tablecolflugzeit;
	   
	}
 public String gettablecoldateabflug() {
     return tablecoldateabflug.get();
 }

 public void settablecoldateabflug(String tablecoldateabflug) {
     this.tablecoldateabflug.set(tablecoldateabflug);
 }

 public StringProperty tablecoldateabflugProperty() {
     return tablecoldateabflug;
 }
 
 public String gettablecoltimeabflug() {
     return tablecoltimeabflug.get();
 }

 public void settablecoltimeabflug(String tablecoltimeabflug) {
     this.tablecoltimeabflug.set(tablecoltimeabflug);
 }

 public StringProperty tablecoltimeabflugProperty() {
     return tablecoltimeabflug;
 }
 public String gettablecolortabflug() {
     return tablecolortabflug.get();
 }

 public void settablecolortabflug(String tablecolortabflug) {
     this.tablecolortabflug.set(tablecolortabflug);
 }

 public StringProperty tablecolortabflugProperty() {
     return tablecolortabflug;
 }
 public String gettablecoltimeankunft() {
     return tablecoltimeankunft.get();
 }

 public void settablecoltimeankunft(String tablecoltimeankunft) {
     this.tablecoltimeankunft.set(tablecoltimeankunft);
 }

 public StringProperty tablecoltimeankunftProperty() {
     return tablecoltimeankunft;
 }
 public String gettablecolortankunft() {
     return tablecolortankunft.get();
 }

 public void settablecolortankunft(String tablecolortankunft) {
     this.tablecolortankunft.set(tablecolortankunft);
 }

 public StringProperty tablecolortankunftProperty() {
     return tablecolortankunft;
 }
 
}
