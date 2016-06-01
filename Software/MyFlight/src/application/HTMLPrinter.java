package application;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
 
public class HTMLPrinter {  
    public static void main(String args[]) throws Exception {
         
    	String filename = System.getProperty("user.dir") + "/"+"test.html";
    	 
        File f = new File(filename);
 
        if (f.exists() && !f.isDirectory()){
             
            System.out.println("Valid File = "+ filename);      
             
            if (Desktop.isDesktopSupported()){
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.PRINT))
                {
                    try {
                        desktop.print(f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.exit(0);
            }
        } else {
            System.out.println("File does exist = "+ filename);
        }
    }
}