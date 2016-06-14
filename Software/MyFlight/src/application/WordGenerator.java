package application;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;

import org.docx4j.Docx4J;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.model.table.TblFactory;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.StyleDefinitionsPart;
import org.docx4j.wml.CTShd;
import org.docx4j.wml.Jc;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.Style;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.TcPr;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

public class WordGenerator {

	private static final String WORD_STYLE_TITLE = "Title";
	private static final String WORD_STYLE_HEADING1 = "Heading1";
	private static final String WORD_STYLE_HEADING2 = "Heading2";

	private static ObjectFactory factory;

	public static void main(String[] args) throws Exception {
		factory = Context.getWmlObjectFactory();

		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
		MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();

		// mann kann die "vordefinierten" Styles ausgeben, diese wären
		// Kandidaten für solche Konstanten dann, wie WORD_STYLE_TITLE

	//	 Set<String> styles = StyleDefinitionsPart.getKnownStyles().keySet();
	 //	System.out.println(Arrays.deepToString(styles.toArray()));

		Style styleTitel = StyleDefinitionsPart.getKnownStyles().get(WORD_STYLE_TITLE);
		Style styleUeberschrift1 = StyleDefinitionsPart.getKnownStyles().get(WORD_STYLE_HEADING1);
		Style styleUeberschrift2 = StyleDefinitionsPart.getKnownStyles().get(WORD_STYLE_HEADING2);
		// under construction Style orgStyle = createStyle(StyleDefinitionsPart.getKnownStyles().get(WORD_STYLE_HEADING2), " ", 14, true, JcEnumeration.CENTER);
		// Logo einfügen
		addLogo(mdp, wordMLPackage);
		
		
		// hier habe ich die Inhalte ins Dokument eingefügt
		//mdp.addStyledParagraphOfText(styleTitel.getStyleId(), "");

		
		
		//centerParagraph(mdp.addParagraphOfText(
		//		"Ganz normaler Text."));
		centerParagraph(mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), "Chartervertrag"));
		doBoldFormat(getFirstRunOfParagraph(getLastParagraph(mdp)));
		centerParagraph(mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), "zwischen"));
		centerParagraph(mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), "HINOTORI Executive AG (Auftragnehmer)"));
		doBoldFormat(getFirstRunOfParagraph(getLastParagraph(mdp)));
		centerParagraph(mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), "und"));
		centerParagraph(mdp.addStyledParagraphOfText(styleUeberschrift1.getStyleId(), "Firma oder Person"));
		doBoldFormat(getFirstRunOfParagraph(getLastParagraph(mdp)));
		
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
		
		mdp.addParagraphOfText(AG+" chartert das Luftfahrzeug "+Typ+" "+Kennzeichen+" für die Zeit vom "+Beginndatum+" zum "+Endedatum+" zu einer Reise von "+Anfang+" nach "+Ende+" über "+Zwischen1+", "+Zwischen2+", "+Zwischen3+".");
		mdp.addStyledParagraphOfText(styleUeberschrift2.getStyleId(), "Flugplan:");
				
		// das hier zeigt, wie ein ganzer Paragraph relativ einfach fett gemacht werden kann
		//mdp.addParagraphOfText("Fetter Text. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam.");
		// hier nehme ich den letzten Paragraphen des Dokumentes (der zuletzt ins Dokument eingefügte Paragraph)
		// und mach ihn bzw. seine erste Passage fett
		doBoldFormat(getFirstRunOfParagraph(getLastParagraph(mdp)));

		// hier schreiben wir einen Satz über die komplexe Art und weise, weil einige Wörter anders formattiert sein müssen
		// addMixedStyleParagraph(mdp);

		// Tabelle
		addTable(mdp, wordMLPackage);

		mdp.addParagraphOfText("");
		mdp.addParagraphOfText("Charterdauer insgesamt (Stunden): \t\t\t\t"+Charterdauer);
		
		mdp.addParagraphOfText("Davon Flugzeit (h/min): \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+Flugzeit);
		mdp.addParagraphOfText("");
		mdp.addParagraphOfText("Gesamtpreis netto (EUR): \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+Preisnetto);
		mdp.addParagraphOfText("19 % Mwst: \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+Mwst);
		mdp.addParagraphOfText("Gesamtpreis brutto (EUR): \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+Preisbrutto);
		doBoldFormat(getFirstRunOfParagraph(getLastParagraph(mdp)));
		mdp.addParagraphOfText("");
		mdp.addParagraphOfText("Datum, Ort                                                                                               Datum, Ort");
		mdp.addParagraphOfText("");
		mdp.addParagraphOfText("HINOTORI Executive AG                                                                         Auftraggeber");
		
		
		
		// speichern
		String filename = System.getProperty("user.dir") + "/test.docx";
		File file = new java.io.File(filename);
		Docx4J.save(wordMLPackage, file, Docx4J.FLAG_SAVE_ZIP_FILE);
		System.out.println("Saved " + file.getCanonicalPath());
	}

	private static R getFirstRunOfParagraph(P lastParagraph) {
		return (R) lastParagraph.getContent().get(0);
	}

	private static P getLastParagraph(MainDocumentPart mdp) {
		int lastContentItem = mdp.getContent().size() - 1;
		P lastParagraph = (P) mdp.getContent().get(lastContentItem);
		return lastParagraph;
	}

	private static void addLogo(MainDocumentPart mdp, WordprocessingMLPackage wordMLPackage) throws Exception {
		InputStream inputStream = WordGenerator.class.getResourceAsStream("logo2.jpg");
		long fileLength = 94712; // 97kB

		byte[] bytes = new byte[(int) fileLength];

		int offset = 0;
		int numRead = 0;

		while (offset < bytes.length && (numRead = inputStream.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		inputStream.close();

		String filenameHint = "FreeLogo";
		String altText = "Einfach nur ein Logo";

		int id1 = 0;
		int id2 = 1;

		P p = newImage(wordMLPackage, bytes, filenameHint, altText, id1, id2);

		mdp.addObject(p);
	}

	public static P newImage(WordprocessingMLPackage wordMLPackage, byte[] bytes, String filenameHint, String altText,
			int id1, int id2) throws Exception {
		BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);
		Inline inline = imagePart.createImageInline(filenameHint, altText, id1, id2, 9100, false);
		
		
		P p = factory.createP();

		R run = factory.createR();
		p.getContent().add(run);

		org.docx4j.wml.Drawing drawing = factory.createDrawing();
		run.getContent().add(drawing);
		drawing.getAnchorOrInline().add(inline);

		return p;
	}

	private static void addTable(MainDocumentPart mdp, WordprocessingMLPackage wordMLPackage) {
		mdp.addObject(getSampleTable(wordMLPackage));
	}

		private static String[] SPALTENKOPF = new String[] { "Datum", "Zeit \n(Abflug)", "Ort \n(von)","Flugzeit","Zeit (Ankunft)","Ort \n(nach)","Anzahl Passagiere"};


		private static String[][] DATEN = new String[][] { { "20.05.2016","08:00","München","1:30","09:30","Paris","3" },
			{ "21.05.2016","15:00","Paris","2:00","17:00","London","2" }, { "22.05.2016","09:00","London","2:00","11:00","Reykjavik","5" } };

	private static Tbl getSampleTable(WordprocessingMLPackage wPMLpackage) {
		int writableWidthTwips = wPMLpackage.getDocumentModel().getSections().get(0).getPageDimensions()
				.getWritableWidthTwips();

		int rows = DATEN.length;
		int cols = SPALTENKOPF.length;
		int cellWidthTwips = new Double(Math.floor((writableWidthTwips / cols))).intValue();

		// wegen Tabellenkopf eine Zeile mehr
		Tbl table = TblFactory.createTable(1 + rows, cols, cellWidthTwips);

		Tr headerRow = (Tr) table.getContent().get(0);

		// Spaltenkopf erstellen
		for (int colIndex = 0; colIndex < cols; colIndex++) {
			Tc column = (Tc) headerRow.getContent().get(colIndex);

			// grauer Hintergrund
			TcPr tcpr = factory.createTcPr();
			CTShd shd = factory.createCTShd();
			shd.setColor("auto");
			shd.setFill("E7E6E6");
			tcpr.setShd(shd);
			column.setTcPr(tcpr);

			P columnPara = (P) column.getContent().get(0);

			// zentriert darstellen
			centerParagraph(columnPara);

			Text text = factory.createText();
			text.setValue(SPALTENKOPF[colIndex]);
			R run = factory.createR();
			run.getContent().add(text);

			// fettschrift
			doBoldFormat(run);

			columnPara.getContent().add(run);
		}

		// mit Daten auffüllen
		for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
			// wegen Tabellenkopf eine Zeile tiefer anfangen
			Tr row = (Tr) table.getContent().get(rowIndex + 1);

			for (int colIndex = 0; colIndex < cols; colIndex++) {
				Tc column = (Tc) row.getContent().get(colIndex);

				P columnPara = (P) column.getContent().get(0);

				Text tx = factory.createText();
				R run = factory.createR();
				tx.setValue(DATEN[rowIndex][colIndex]);
				run.getContent().add(tx);
				columnPara.getContent().add(run);
			}
		}
		return table;
	}

	private static void centerParagraph(P columnPara) {
		PPr paragraphProperties = factory.createPPr();
		Jc justification = factory.createJc();
		justification.setVal(JcEnumeration.CENTER);
		paragraphProperties.setJc(justification);
						

		RPr rpr = factory.createRPr();
		columnPara.setPPr(paragraphProperties);
	}

	private static void addMixedStyleParagraph(MainDocumentPart mdp) {
		org.docx4j.wml.P p = factory.createP();

		// 1. Bestandteil des Satzes
		p.getContent().add(createRun("Dies ist ein "));

		// 2. Bestandteil des Satzes
		org.docx4j.wml.R run2 = createRun("Satz");
		p.getContent().add(run2);
		doBoldRedFormat(run2);

		// 3. Bestandteil des Satzes
		p.getContent().add(createRun(" mit unterschiedlich formattierten "));

		// 4. Bestandteil des Satzes
		org.docx4j.wml.R run4 = createRun("Wörter");
		p.getContent().add(run4);
		doItalicsFormat(run4);

		// 5. Bestandteil des Satzes
		p.getContent().add(createRun("."));

		// Add it to the doc
		mdp.addObject(p);
	}

	private static void doItalicsFormat(R run) {
		org.docx4j.wml.RPr rpr = factory.createRPr();

		// kursiv
		org.docx4j.wml.BooleanDefaultTrue b = new org.docx4j.wml.BooleanDefaultTrue();
		b.setVal(true);
		rpr.setI(b);

		run.setRPr(rpr);
	}

	private static void doBoldFormat(org.docx4j.wml.R run) {
		org.docx4j.wml.RPr rpr = factory.createRPr();

		// fettschrift
		org.docx4j.wml.BooleanDefaultTrue b = new org.docx4j.wml.BooleanDefaultTrue();
		b.setVal(true);
		rpr.setB(b);
		
		
		run.setRPr(rpr);
	}

	private static void doBoldRedFormat(org.docx4j.wml.R run) {
		org.docx4j.wml.RPr rpr = factory.createRPr();

		// fettschrift
		org.docx4j.wml.BooleanDefaultTrue b = new org.docx4j.wml.BooleanDefaultTrue();
		b.setVal(true);
		rpr.setB(b);

		// rote farbe
		org.docx4j.wml.Color c = new org.docx4j.wml.Color();
		c.setVal("#FF0000");
		rpr.setColor(c);

		run.setRPr(rpr);
	}

	private static org.docx4j.wml.R createRun(String content) {
		org.docx4j.wml.Text t = factory.createText();
		t.setSpace("preserve");
		t.setValue(content);
		org.docx4j.wml.R run = factory.createR();
		run.getContent().add(t);
		return run;
	}

}
