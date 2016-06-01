package application;

import java.io.File;

import application.Druckjob;
public class druckauftrag {

	public static String strFilename = "test.pdf";
	public static String filename = System.getProperty("user.dir") + "/"+strFilename;
	public static File f = new File(filename);
	
	public static int Dialog = -1;
	
	
	public static void main (String[] args) throws Exception {
	
	//	DOCPrinter.main(null);
	PDFPrinter druck = new PDFPrinter(f);
	//	Druckjob druck = new Druckjob(strFilename,Dialog);
	}
}
