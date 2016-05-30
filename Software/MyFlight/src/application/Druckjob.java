package application;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
//import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.event.PrintJobListener;

import org.apache.fop.tools.TestConverter;

import javax.print.event.PrintJobEvent;
 
public class Druckjob {
 
 //   private MyDruckListener drucker = new MyDruckListener();
 
	String filen;
    public Druckjob(String strFilename, int iDevice) {
 
    	filen = strFilename;
    	drucken();	
    }
    
    
    public void drucken() {
    System.out.println(filen);
      /*  try {
 
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
 
            //setzt das auszudruckend Dokument fest
          //  String filename = System.getProperty("user.dir") + "/test.pdf";
           // InputStream inputStream = new FileInputStream(filename);
          //  Doc doc = new SimpleDoc(inputStream, DocFlavor.INPUT_STREAM.AUTOSENSE,null);
            
            DocFlavor flavor = DocFlavor.INPUT_STREAM.TEXT_HTML_UTF_16; //hier nachsehen, welches man benötigt!
 
            PrintService printService[] = PrintServiceLookup.lookupPrintServices(
                    flavor, pras);
 
            PrintService defaultService = PrintServiceLookup.
                    lookupDefaultPrintService();
 
            PrintService service = null;
 
            //wenn als Device -1 übergeben wird, wird ein Dialog für das drucken auswählen ausgegeben!
            if (iDevice == -1) {
 
               service = ServiceUI.printDialog(GraphicsEnvironment.
                        getLocalGraphicsEnvironment().
                        getDefaultScreenDevice().
                        getDefaultConfiguration(), 600, 600,
                        printService, defaultService, flavor, pras);
 
            } 
            //ansonsten wird der 1te (Standard/Default) Drucker genommen
            else {
 
                //wenn es keine Drucker gibt und das Device niedriger ist als die Länge
                if (printService != null && printService.length != 0 && printService.length > iDevice) {
                    service = printService[iDevice];
                } 
 
                //ansonsten standarddevice
                else if (printService != null && printService.length != 0) {
                    service = printService[0];
                }
            }
 
            //wenn der Dateiename null ist, wird die Druckfunktion beendet!
            if (strFilename == null) {
                return;
            }
 
            //wenn der Service nicht null ist, wird ausgedruckt
            if (service != null) {
                DocPrintJob job = service.createPrintJob();
 
                //fügt listener hinzu
                job.addPrintJobListener(drucker);
                String filename = System.getProperty("user.dir") + "/"+strFilename;
                FileInputStream fis = new FileInputStream(filename);
                DocAttributeSet das = new HashDocAttributeSet();
                SimpleDoc doc = new SimpleDoc(fis, flavor, das);
                job.print(doc, pras);
            }
        } //wenn kein Druckerdevice gefunden wurde!!
        catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
            System.out.println("Keine Drucker gefunden!!");
        } 
        //bei sonstigen Exceptions!
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
 
    class MyDruckListener implements PrintJobListener {
 
        public void printDataTransferCompleted(PrintJobEvent printJobEvent) {
            System.out.println("Daten wurden zum Drucker geschickt!");
        }
 
        public void printJobCompleted(PrintJobEvent printJobEvent) {
            System.out.println("Drucker hat fertig gedruckt!");
        }
 
        public void printJobFailed(PrintJobEvent printJobEvent) {
            System.out.println("Fehler beim Drucken!");
        }
 
        public void printJobCanceled(PrintJobEvent printJobEvent) {
            System.out.println("Abbruch des druckes!");
        }
 
        public void printJobNoMoreEvents(PrintJobEvent printJobEvent) {
            System.out.println("JobNoMoreEvents!");
        }
 
        public void printJobRequiresAttention(PrintJobEvent printJobEvent) {
            System.out.println("JobRequieresAttention!");
        } 
   
       
    */
    
    	PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
    	DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
    	PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
    	PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
    	PrintService service = ServiceUI.printDialog(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration(), 200, 200,
    	                      printService, defaultService, flavor, pras);
    	if (service != null) {
    	    DocPrintJob job = service.createPrintJob();
    	    String filen1 = System.getProperty("user.dir") + "/"+filen;
    	    
    	    FileInputStream fis = new FileInputStream(filen1);
    	    DocAttributeSet das = new HashDocAttributeSet();
    	    Doc document = new SimpleDoc(fis, flavor, das);
    	    job.print(document, pras);
    	}
    }
    } 
    	

