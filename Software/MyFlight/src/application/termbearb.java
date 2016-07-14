package application;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class termbearb extends MyFlightController {
	
	private IntegerProperty id = null;
	private SimpleStringProperty termart = null;
	private SimpleStringProperty startd = null;
	private SimpleStringProperty zield = null;
	private SimpleStringProperty startz = null;
	private SimpleStringProperty zielz = null;
	
	public termbearb(Integer id,String termart, String startd, String startz, String zield, String zielz) {

        this.id = new SimpleIntegerProperty(id);
        this.termart = new SimpleStringProperty(termart);
        this.startd = new SimpleStringProperty(startd);
        this.startz = new SimpleStringProperty(startz);
        this.zield = new SimpleStringProperty(zield);
        this.zielz = new SimpleStringProperty(zielz);
    
	}
	
	public int getid() {
        return id.get();
    }

    public void setid(int id) {
        this.id.set(id);
    }
	
    public String getzield() {
        return zield.get();
    }

    public void setzield(String zield)  {
        this.zield.set(zield);
    }
	
    public String getzielz() {
        return zielz.get();
    }

    public void setzielz(String zielz)  {
        this.zielz.set(zielz);
    }
	
    public String getstartd() {
        return startd.get();
    }

    public void setstartd(String startd)  {
        this.startd.set(startd);
    }
	
    public String getstartz() {
        return startz.get();
    }

    public void setstartz(String startz)  {
        this.startz.set(startz);
    }
	
    public String gettermart() {
        return termart.get();
    }

    public void settermart(String termart)  {
        this.termart.set(termart);
    }
	
	public IntegerProperty idProperty() {
        return id;
    }
	
	 public StringProperty zielzProperty() {
	        return zielz;
	    }
	
	 public StringProperty zieldProperty() {
	        return zield;
	    }
	
	 public StringProperty startzProperty() {
	        return startz;
	    }
	
	 public StringProperty startdProperty() {
	        return startd;
	    }
	
	public StringProperty termartProperty() {
	        return termart;
	    }
	



	
	
	
}
