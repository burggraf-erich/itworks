package application;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.ColorSupported;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;

import com.pretty_tools.dde.DDEException;
import com.pretty_tools.dde.DDEMLException;
import com.pretty_tools.dde.client.DDEClientConversation;

import application.Druckjob;

public class Druckjob {
	
	private static final int CONNECT_WAIT_MILLIS = 500;
	private static final int CONNECT_MAX_TRIES = 6;
	// private static String filename1 = System.getProperty("user.dir") + "/"+"test.docx";
	private static String filename ;

	public Druckjob(String strFilename) {

	//	String filename = System.getProperty("user.dir") + "/"+strFilename;
	filename = strFilename;
	
	try {
		
	

	//public static void main(String[] args) throws IOException {
		// Druckjob druckjob = new Druckjob("tmp/simple.html");
		// Druckjob druckjob = new Druckjob("tmp/simple.pdf");
		// druckjob.drucken();

		// diese Variante druck ein Word-Dokument, allerdings muss
		// der Pfad zum Microsoft Word gesetzt werden und hier muss der Drucker
		// vorher als Standarddrucker ausgewählt worden sein

		// wo Microsoft Word liegt müsste in der Anwendung eingestellt werden
		// (in den Einstellungen)
	//	Druckjob druckjob = new Druckjob(filename);
	//	druckjob.druckenMitWord("C:\\Program Files\\Microsoft Office 15\\root\\office15\\winword.exe");
		//String filename = args[0];
		System.out.println(filename);
	//	Druckjob druckjob = new Druckjob(filename);
		Druckjob.this.druckenMitWord("C:\\Program Files (x86)\\Microsoft Office\\Office14\\WINWORD.EXE");
	}catch (IOException e) {
        e.printStackTrace();
    }
	}
	
	void druckenMitWord(String wordExe) throws IOException {
		if (filename == null || filename.length() == 0) {
			System.err.println("Kein Dateiname gesetzt!");
		}

		// diese Parameter stellen sicher, dass Microsoft Word in einem
		// Modus gestartet wird, in dem Word per DDE angesprochen werden kann
		String params = "/x /q";
		// den Pfad zur Datei ermitteln
	//	File file = new File(System.getProperty("user.dir"), filename);
		File file = new File(filename);
		// Word ausführen
		if (!checkFileExists(wordExe)) {
			System.out.println("Word wurde unter dem angegebenen Pfad nicht gefunden...");
			return;
		}
		Process process = Runtime.getRuntime().exec(wordExe + " " + params + " " + file); //.getAbsolutePath());
		// Word läuft in diesem Modus im Hintergrund weiter, wenn alles gut ging
		if (process.isAlive()) {
			// wir starten die Konversation mit Word per DDE
			try {
				DDEClientConversation conversation = new DDEClientConversation();

				// We can use UNICODE format if server prefers it
				// conversation.setTextFormat(ClipboardFormat.CF_UNICODETEXT);
				System.out.println("Verbindung zum Word wird gestartet...");

				if (!connectToWord(conversation, "WINWORD", file.getAbsolutePath())) {
					
					System.out.println("Word war zu lange beschäftigt oder kann keine DDE Befehle empfangen");
					// Word beenden
					if (process.isAlive()) {
						process.destroy();
					}
					return;
				} 
				System.out.println("Word ist gestartet, jetzt DDE Befehle schicken");

				try {
					// Word den Befehl schicken sich zu minimieren und zu
					// drucken
					conversation.execute("[AppMinimize][FilePrint][FileExit]");
				} finally {
					System.out.println("Verbindung zum Word beenden...");
					conversation.disconnect();

					// Word nicht beenden, weil der Drucktask kann noch dauern
				}
			} catch (DDEMLException e) {
				System.out.println("DDEMLException: 0x" + Integer.toHexString(e.getErrorCode()) + " " + e.getMessage());
			} catch (DDEException e) {
				System.out.println("DDEClientException: " + e.getMessage());
			} catch (Exception e) {
				System.out.println("Exception: " + e);
			}
		}

	}

	private boolean checkFileExists(String path) {
		File file = new File(path);

		return file.exists() || file.isFile();
	}

	/**
	 * Verbindungsversuch zum Word
	 * 
	 * @param conversation
	 *            die Konversation, die aufgebaut werden muss
	 * @param SERVICE
	 *            für die DDE-Kommunikation
	 * @param TOPIC
	 *            für die DDE-Kommunikation
	 * @return true = hat geklappt, false = hat nicht geklappt
	 * @throws InterruptedException
	 */
	private boolean connectToWord(DDEClientConversation conversation, String SERVICE, String TOPIC)
			throws InterruptedException {
		// die DDE-Kommunikation kann bei langsameren Rechnern etwas dauern,
		// darum
		// versuchen wir ein paar mal zu connecten und zwischen den Versuchen
		// warten
		// wir eine kurze Zeit
		int fehler = 0;
		Date startzeit = new Date();
		boolean zeitAbgelaufen = false;
		// max Wartezeit: Anzahl Versuche x die Wartezeit per Versuch
		while (fehler < CONNECT_MAX_TRIES
				&& !(zeitAbgelaufen = zeitAbgelaufen(startzeit, CONNECT_MAX_TRIES * CONNECT_WAIT_MILLIS))) {
			try {
				// Verbindungsversuch
				conversation.connect(SERVICE, TOPIC);
				break;
			} catch (DDEException d) {
				// Fehler, weil Word noch nicht fertig ist
				fehler++;
				if (fehler == CONNECT_MAX_TRIES) {
					break;
				} else {
					Thread.sleep(CONNECT_WAIT_MILLIS);
				}
			}
		}

		return (fehler < CONNECT_MAX_TRIES && !zeitAbgelaufen);
	}

	private boolean zeitAbgelaufen(Date startzeit, long timeoutInMilis) {
		return new Date().getTime() - startzeit.getTime() > timeoutInMilis;
	}

	/**
	 * Nutzt das Java-API zum Drucken. 'filename' muss vorher gesetzt werden.
	 */
	void druckenMitJavaApi() {
		if (filename == null || filename.length() == 0) {
			System.err.println("Kein Dateiname gesetzt!");
		}

		PrintRequestAttributeSet printReqAttrs = new HashPrintRequestAttributeSet();
		DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
		PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, printReqAttrs);
		PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
		PrintService service = ServiceUI.printDialog(
				GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration(),
				200, 200, printService, defaultService, flavor, printReqAttrs);
		if (service != null) {
			DocPrintJob job = service.createPrintJob();
			File file = new File(filename);

			try {
				FileInputStream fis = new FileInputStream(file);
				DocAttributeSet docAttrs = new HashDocAttributeSet();
				docAttrs.add(OrientationRequested.PORTRAIT);
				docAttrs.add(ColorSupported.SUPPORTED);
				Doc document = new SimpleDoc(fis, flavor, docAttrs);
				job.addPrintJobListener(new PrintJobListener() {

					@Override
					public void printJobRequiresAttention(PrintJobEvent pje) {
						System.out.println("Der Druck braucht Eingaben!");
					}

					@Override
					public void printJobNoMoreEvents(PrintJobEvent pje) {
						System.out.println(
								"Der Druck sendet keine weiteren Events mehr (übergeben an weiteres Programm?).");
					}

					@Override
					public void printJobFailed(PrintJobEvent pje) {
						System.out.println("Der Druck ist fehlgeschlagen.");
					}

					@Override
					public void printJobCompleted(PrintJobEvent pje) {
						System.out.println("Der Druck ist erfolgreich gewesen.");
					}

					@Override
					public void printJobCanceled(PrintJobEvent pje) {
						System.out.println("Der Druck wurde abgebrochen.");
					}

					@Override
					public void printDataTransferCompleted(PrintJobEvent pje) {
						System.out.println("Der Datentransfer ist fertig.");
					}
				});
				job.print(document, printReqAttrs);
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (PrintException e) {
				e.printStackTrace();
			}
		}
	}
}
