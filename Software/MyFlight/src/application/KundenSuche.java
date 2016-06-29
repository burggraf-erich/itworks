package application;

import java.sql.ResultSet;
import java.sql.Statement;

public class KundenSuche {
	
	int Kid;
	String Name;
	String Vorname;
	String Firma;
	String Phone;
	String Mobile;
	String Mail;
	String Strasse;
	String Ort;
	String PLZ;
	String Land;
	String KG;
	String Anrede;
	String Zusatz;
	
	public KundenSuche(int kid, String name, String vorname, String firma, String strasse, String pLZ, String ort,
			String phone, String mail, String land, String kG, String anrede, String zusatz) {
		
		Kid = kid;
		Name = name;
		Vorname = vorname;
		Firma = firma;
		Phone = phone;
		//Mobile = mobile;
		Mail = mail;
		Strasse = strasse;
		Ort = ort;
		PLZ = pLZ;
		Land = land;
		KG = kG;
		Anrede = anrede;
		Zusatz = zusatz;
	
	}



	public int getKid() {
		return Kid;
	}
	public void setKid(int kid) {
		Kid = kid;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getVorname() {
		return Vorname;
	}
	public void setVorname(String vorname) {
		Vorname = vorname;
	}
	public String getFirma() {
		return Firma;
	}
	public void setFirma(String firma) {
		Firma = firma;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getMobile() {
		return Mobile;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
	}
	public String getMail() {
		return Mail;
	}
	public void setMail(String mail) {
		Mail = mail;
	}
	public String getStrasse() {
		return Strasse;
	}
	public void setStrasse(String strasse) {
		Strasse = strasse;
	}
	public String getOrt() {
		return Ort;
	}
	public void setOrt(String ort) {
		Ort = ort;
	}

	public String getPLZ() {
		return PLZ;
	}
	public void setPLZ(String pLZ) {
		PLZ = pLZ;
	}
	public String getLand() {
		return Land;
	}
	public void setLand(String land) {
		Land = land;
	}
	public String getKG() {
		return KG;
	}
	public void setKG(String kG) {
		KG = kG;
	}



	public String getAnrede() {
		return Anrede;
	}



	public void setAnrede(String anrede) {
		Anrede = anrede;
	}



	public String getZusatz() {
		return Zusatz;
	}



	public void setZusatz(String zusatz) {
		Zusatz = zusatz;
	}

}


