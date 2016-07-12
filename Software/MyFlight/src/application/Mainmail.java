package application;

	import java.awt.Desktop;
	import java.io.IOException;
	import java.net.URI;
	import java.net.URISyntaxException;
	import java.net.URLEncoder;

	public class Mainmail {
		public static String kundenanrede;
		public static String kunde ;
		public static int Nummer ;
		public static String Datum ;
		public static String mail;
		public static String modus;
		
		public Mainmail (String kundenanrede, String kunde, int Nummer, String Datum, String mail, String modus) throws IOException, URISyntaxException {
			this.kundenanrede =kundenanrede;
			this.kunde = kunde;
			this.Nummer= Nummer;
			this.Datum = Datum; 
			this.mail = mail;
			this.modus = modus;
			
			switch (modus) {
			
			case "Auftrag" :
		executeMail(kundenanrede, kunde,Nummer,Datum,mail); 
		break;
			case "Angebot" :
				executeMail_Angebot(kundenanrede, kunde,Nummer,Datum,mail); 
				}
		}
			public void executeMail(String anrede, String kunde, int Nummer, String Datum, String mail)throws IOException, URISyntaxException {
			boolean ok = Desktop.isDesktopSupported();
			if (!ok) {
				System.out.println("Desktop not supported");
			} else {
				String mailto = "mailto:";
				mailto += "?to="+mail;
				mailto += "&subject=";
				mailto += URLEncoder.encode("Auftrag Nr. "+Nummer+" zu unserem Angebot vom "+Datum);
				mailto += "&body=";
				mailto += URLEncoder.encode("Sehr geehrte(r) "+anrede+" "+kunde+",\nherzlichen Dank f�r Ihre Anfrage, anbei �bersenden wir Ihnen wie gew�nscht einen \nverbindlichen Auftragsentwurf mit der Bitte um Ihre Pr�fung.\n\n�ber eine Unterschrift und R�ckantwort innerhalb der n�chsten 5 Tage freuen wir uns sehr.\n\nGerne stehen wir f�r R�ckfragen zu Ihrer Verf�gung und verbleiben\n\nmit freundlichen Gr��en,\n\nHINOTORI Executive GmbH", "UTF-8");
				//mailto += URLEncoder.encode("vielen Dank f�r Ihre Nachfrage nach unserem Angebot!", "UTF-8");
				

				// + Zeichen sind eigentlich richtig, doch sie kommen im
				// Mailprogramm falsch an
				mailto = mailto.replace("+", "%20");
				URI uri = new URI(mailto);
				System.out.println("URI: " + uri);
				Desktop.getDesktop().mail(uri);
			}
		}
			public void executeMail_Angebot(String anrede, String kunde, int Nummer, String Datum, String mail)throws IOException, URISyntaxException {
				boolean ok = Desktop.isDesktopSupported();
				if (!ok) {
					System.out.println("Desktop not supported");
				} else {
					String mailto = "mailto:";
					mailto += "?to="+mail;
					mailto += "&subject=";
					mailto += URLEncoder.encode("Angebot Nr. "+Nummer+" vom "+Datum);
					mailto += "&body=";
					mailto += URLEncoder.encode("Sehr geehrte(r) "+anrede+" "+kunde+",\nherzlichen Dank f�r Ihre Anfrage, anbei �bersenden wir Ihnen wie gew�nscht ein \nunverbindliches Angebot mit der Bitte um Ihre Pr�fung.\n\n�ber eine R�ckantwort innerhalb der n�chsten 5 Tage freuen wir uns sehr.\n\nGerne stehen wir f�r R�ckfragen zu Ihrer Verf�gung und verbleiben\n\nmit freundlichen Gr��en,\n\nHINOTORI Executive GmbH", "UTF-8");
					//mailto += URLEncoder.encode("vielen Dank f�r Ihre Nachfrage nach unserem Angebot!", "UTF-8");
					

					// + Zeichen sind eigentlich richtig, doch sie kommen im
					// Mailprogramm falsch an
					mailto = mailto.replace("+", "%20");
					URI uri = new URI(mailto);
					System.out.println("URI: " + uri);
					Desktop.getDesktop().mail(uri);
				}
			}
	}
