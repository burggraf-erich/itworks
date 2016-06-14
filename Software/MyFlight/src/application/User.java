package application;

public class User {

	private String vorname, name, rolle;
	private int berechtigung;

	public User(String vorname, String name, String rolle, int berechtigung) {
		this.vorname = vorname;
		this.name = name;
		this.rolle = rolle;
		this.berechtigung = berechtigung;
	}

	public int getberechtigung() {
		return this.berechtigung;
	}
public String getrolle() {
	return this.rolle;}

	
}
