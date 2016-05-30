package application;
// V 1.08
import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Font.FontStyle;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.TabSettings;

public class PdfGenerator {

	private static final String WORD_STYLE_TITLE = "Title";
	private static final String WORD_STYLE_HEADING1 = "Heading1";
	private static final String WORD_STYLE_HEADING2 = "Heading2";

	public static void main(String[] args) throws Exception {
		// step 1
		Document document = new Document(PageSize.A4);
		document.setMargins(50f, 40f, 50f, 40f);
		// step 2
		String filename = System.getProperty("user.dir") + "/test.pdf";
		PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(filename));
		// nur für die Möglichkeit, dass wir einen Rahmen zeichnen können
		ParagraphBorder border = enableBordering(pdfWriter);
		// step 3
		document.open();

		// step 4
		Font styleTitel = new Font(FontFamily.HELVETICA);
		styleTitel.setSize(18);
		styleTitel.setStyle(FontStyle.BOLD.name());

		Font styleUeberschrift1 = new Font(FontFamily.HELVETICA);
		styleUeberschrift1.setSize(12);
		styleUeberschrift1.setColor(BaseColor.BLUE);
		styleUeberschrift1.setStyle(FontStyle.BOLD.name());

		Font styleUeberschrift2 = new Font(FontFamily.HELVETICA);
		styleUeberschrift2.setSize(14);
		styleUeberschrift2.setColor(BaseColor.BLUE);
		styleUeberschrift2.setStyle(FontStyle.NORMAL.name());

		Font styleText = new Font(FontFamily.HELVETICA);
		styleText.setSize(12);
		styleText.setStyle(FontStyle.NORMAL.name());

		Font styleTextunderline = new Font(FontFamily.HELVETICA);
		styleText.setSize(12);
		styleText.setStyle(FontStyle.UNDERLINE.name());

		Font styleFettText = new Font(FontFamily.HELVETICA);
		styleFettText.setSize(12);
		styleFettText.setStyle(FontStyle.BOLD.name());

		Font styleRot = new Font(FontFamily.HELVETICA);
		styleRot.setSize(12);
		styleRot.setColor(BaseColor.RED);
		styleRot.setStyle(FontStyle.BOLD.name());

		Font styleKursiv = new Font(FontFamily.HELVETICA);
		styleKursiv.setSize(12);
		styleKursiv.setStyle(FontStyle.ITALIC.name());

		Image image = Image.getInstance(PdfGenerator.class.getResource("logo2.jpg"));
		image.scaleAbsolute(507,40);
		document.add(image);
		// Dokumententitel (mit Rahmen!)
		border.setActive(true);
		Paragraph p = new Paragraph("", styleTitel);
		p.setAlignment(Element.ALIGN_LEFT);
		document.add(p);
		border.setActive(false);

		
	

		p = new Paragraph(" ", styleText);
		p.setAlignment(Element.ALIGN_CENTER);
		// etwas abstand hinter der überschrift
		p.setSpacingAfter(6f);
		document.add(p);p = new Paragraph("Chartervertrag", styleUeberschrift1);
		p.setAlignment(Element.ALIGN_CENTER);
		// etwas abstand hinter der überschrift
		p.setSpacingAfter(6f);
		document.add(p);
		p = new Paragraph("zwischen", styleText);
		p.setAlignment(Element.ALIGN_CENTER);
		// etwas abstand hinter der überschrift
		p.setSpacingAfter(6f);
		document.add(p);
		p = new Paragraph("HINOTORI Executive AG (Auftragnehmer)", styleUeberschrift1);
		p.setAlignment(Element.ALIGN_CENTER);
		// etwas abstand hinter der überschrift
		p.setSpacingAfter(6f);
		document.add(p);
		p = new Paragraph("und", styleText);
		p.setAlignment(Element.ALIGN_CENTER);
		// etwas abstand hinter der überschrift
		p.setSpacingAfter(6f);
		document.add(p);
		p = new Paragraph("Firma oder Person", styleUeberschrift1);
		p.setAlignment(Element.ALIGN_CENTER);
		// etwas abstand hinter der überschrift
		p.setSpacingAfter(20f);
		document.add(p);
		
		String AG = "Erich";
		String Typ = "Dornier";
		String Kennzeichen = "120";
		String Beginndatum = "20.05.2016";
		String Endedatum = "01.06.2016";
		String Anfang = "München";
		String Ende = "New York";
		String Zwischen1 = "Paris";
		String Zwischen2 = "London";
		String Zwischen3 = "Reykjavík";
		String Charterdauer = "124:30 h";
		String Flugzeit = "24:45";
		String Preisnetto = "1.450,00 EUR";
		String Mwst = "275,50 EUR";
		String Preisbrutto = "1.725,50 EUR";
		
		p = new Paragraph(
				AG+" chartert das Luftfahrzeug "+Typ+" "+Kennzeichen+"für die Zeit vom "+Beginndatum+" zum "+Endedatum+" zu einer Reise von "+Anfang+" nach "+Ende+" über "+Zwischen1+", "+Zwischen2+", "+Zwischen3+".",styleText);
		p.setAlignment(Element.ALIGN_JUSTIFIED);
		// zeilenabstand kleiner wählen
		p.setLeading(15f);
		p.setSpacingAfter(20f);
		document.add(p);

		p = new Paragraph("Flugplan:", styleUeberschrift2);
		p.setAlignment(Element.ALIGN_LEFT);
		// etwas abstand hinter der überschrift
		p.setSpacingAfter(6f);
		document.add(p);

		
		/*p = new Paragraph();
		p.add(new Chunk("Dies ist ein ", styleText));
		p.add(new Chunk("Satz", styleRot));
		p.add(new Chunk(" mit unterschiedlich formatierten ", styleFettText));
		p.add(new Chunk("Wörter", styleKursiv));
		p.add(new Chunk(".", styleText));
		// zeilenabstand kleiner wählen
		p.setSpacingBefore(6f);
		p.setAlignment(Element.ALIGN_JUSTIFIED);
		// zeilenabstand kleiner wählen
		p.setLeading(15f);
		document.add(p);
*/
		addTable(document);

		p = new Paragraph(" ", styleText);
		p.setAlignment(Element.ALIGN_LEFT);
		// etwas abstand hinter dem Text
		p.setSpacingAfter(6f);
		document.add(p);
		
		p = new Paragraph();
		p.setTabSettings(new TabSettings(35f));
        p.add(new Chunk("Charterdauer insgesamt (Stunden): ", styleText));
		p.add(Chunk.TABBING);
		
		
		p.add(new Chunk(Charterdauer, styleText));
		
		// etwas abstand hinter dem Text
		p.setSpacingAfter(15f);
		document.add(p);
		
		p = new Paragraph();
		p.setTabSettings(new TabSettings(35f));
        p.add(new Chunk("Davon Flugzeit (h/min): ", styleText));
		p.add(Chunk.TABBING);
		p.add(Chunk.TABBING);
		p.add(Chunk.TABBING);
		
		p.add(new Chunk(Flugzeit, styleText));
		
		// etwas abstand hinter dem Text
		p.setSpacingAfter(15f);
		document.add(p);
		

		p = new Paragraph();
		p.setTabSettings(new TabSettings(35f));
        p.add(new Chunk("Gesamtpreis netto (EUR): ", styleText));
		p.add(Chunk.TABBING);
		p.add(Chunk.TABBING);
		
		p.add(new Chunk(Preisnetto, styleText));
		
		// etwas abstand hinter dem Text
		p.setSpacingAfter(6f);
		document.add(p);
		
		
		
		p = new Paragraph();
		p.setTabSettings(new TabSettings(35f));
        p.add(new Chunk("19 % Mwst: ", styleText));
		p.add(Chunk.TABBING);
		p.add(Chunk.TABBING);
		p.add(Chunk.TABBING);
		p.add(Chunk.TABBING);
		p.add(Chunk.TABBING);
		
		p.add(new Chunk(Mwst, styleText));
		p.setAlignment(Element.ALIGN_LEFT);
		// etwas abstand hinter dem Text
		p.setSpacingAfter(6f);
		document.add(p);
		
		p = new Paragraph();
		p.setTabSettings(new TabSettings(35f));
        p.add(new Chunk("Gesamtpreis brutto (EUR): ", styleText));
		p.add(Chunk.TABBING);
		p.add(Chunk.TABBING);
		
		
		p.add(new Chunk(Preisbrutto, styleText));
		
		// etwas abstand hinter dem Text
		p.setSpacingAfter(25f);
		document.add(p);
		
		
		p = new Paragraph();
		p.setTabSettings(new TabSettings(35f));
        p.add(new Chunk("Datum, Ort", styleText));
		p.add(Chunk.TABBING);
		p.add(Chunk.TABBING);
		p.add(Chunk.TABBING);
		p.add(Chunk.TABBING);
		p.add(Chunk.TABBING);
		p.add(Chunk.TABBING);
		p.add(Chunk.TABBING);
		
		p.add(new Chunk("Datum, Ort", styleText));
		p.setSpacingAfter(25);
		document.add(p);
		
		p = new Paragraph();
		p.setTabSettings(new TabSettings(35f));
		Chunk underline = new Chunk("                                                  ");
		underline.setUnderline(0.3f, -2f); //0.1 thick, -2 y-location
		p.add(underline);
		p.add(Chunk.TABBING);
		p.add(Chunk.TABBING);
		p.add(Chunk.TABBING);
		p.add(Chunk.TABBING);
		p.add(underline);
		p.setSpacingAfter(25);
		document.add(p);
		
		p = new Paragraph();
		p.setTabSettings(new TabSettings(35f));
        p.add(new Chunk("HINOTORI Executive AG", styleText));
		p.add(Chunk.TABBING);
		p.add(Chunk.TABBING);
		p.add(Chunk.TABBING);
		p.add(Chunk.TABBING);
		p.add(Chunk.TABBING);
		
		p.add(new Chunk("Auftraggeber",styleText));
		p.setAlignment(Element.ALIGN_LEFT);
		// etwas abstand hinter dem Text
		p.setSpacingAfter(6f);
		document.add(p);
		
		// step 5
		document.close();

		File file = new File(filename);
		System.out.println("Saved " + file.getCanonicalPath());
	}

	private static ParagraphBorder enableBordering(PdfWriter pdfWriter) {
		ParagraphBorder border = new ParagraphBorder();
		pdfWriter.setPageEvent(border);
		return border;
	}

	private static void addTable(Document document) throws DocumentException {
		document.add(getSampleTable());
	}
	String AG = "Erich";
	String Typ = "Dornier";
	String Kennzeichen = "120";
	String Beginndatum = "20.05.2016";
	String Endedatum = "01.06.2016";
	String Anfang = "München";
	String Ende = "New York";
	String Zwischen1 = "Paris";
	String Zwischen2 = "London";
	String Zwischen3 = "Reykjavík";
	
	private static String[] SPALTENKOPF = new String[] { "Datum", "Zeit \n(Abflug)", "Ort \n(von)","Flugzeit","Zeit (Ankunft)","Ort \n(nach)","Anzahl Passagiere"};

	private static String[][] DATEN = new String[][] { { "20.05.2016","08:00","München","1:30","09:30","Paris","3" },
		{ "21.05.2016","15:00","Paris","2:00","17:00","London","2" }, { "22.05.2016","09:00","London","2:00","11:00","Reykjavik","5" } };

	private static PdfPTable getSampleTable() throws DocumentException {
		int rows = DATEN.length;
		int cols = SPALTENKOPF.length;
		PdfPTable table = new PdfPTable(cols);
		table.setSpacingBefore(16f);
		table.setWidthPercentage(100);
		

		// Spaltenkopf
		for (int colIndex = 0; colIndex < cols; colIndex++) {
			Phrase phrase = new Phrase(SPALTENKOPF[colIndex]);
			PdfPCell cell = new PdfPCell(phrase);
			// 0 = schwarz, 1 = weiß
			cell.setGrayFill(0.8f);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			float[] columnWidths = new float[] {9f, 7f, 10f, 7f, 7f, 10f, 9f};
	        table.setWidths(columnWidths);
		}

		// mit Daten auffüllen
		for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
			// wegen Tabellenkopf eine Zeile tiefer anfangen
			for (int colIndex = 0; colIndex < cols; colIndex++) {
				table.addCell(DATEN[rowIndex][colIndex]);
			}
		}
		return table;
	}

	
}
