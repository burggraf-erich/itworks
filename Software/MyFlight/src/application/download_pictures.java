package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class download_pictures {
	static Connection conn;
	public static Statement stmt;
	final static String hostname = "172.20.1.24"; 
    final static String port = "3306"; 
    static String user = "erich.burggraf";
    static String password = "Ulrike0506";
    static String dbname = "benutzerverwaltung";
	public static void main (String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException {
		Class.forName("org.gjt.mm.mysql.Driver").newInstance();
    String url = "jdbc:mysql://"+hostname+":"+port+"/"+dbname;
	conn = DriverManager.getConnection(url, user, password);
	//if (conn.isClosed()) conn = DriverManager.getConnection(url, user, password);
	
	stmt = conn.createStatement();
	
	String sql = "select * from benutzerverwaltung.flugzeug_bilder where flugzeug_bilder.flugzeuge_Flugzeug_ID = 3";
	ResultSet rs = stmt.executeQuery(sql);
	String flgzid;
	File image;
	while (rs.next()) {
	String filenamepic = System.getProperty("user.dir") + "/picture"+rs.getString(1)+".jpg";
	image = new File (filenamepic);
	FileOutputStream fos = new FileOutputStream(image);

	      byte[] buffer = new byte[1];
	      InputStream is = rs.getBinaryStream(2);
	      while (is.read(buffer) > 0) {
          fos.write(buffer);
      }
	      fos.close();

	
	}
	}
}
