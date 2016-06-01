package application;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class ParagraphBorder extends PdfPageEventHelper {
	public boolean active = false;

	public void setActive(boolean active) {
		this.active = active;
	}

	public float offset = 5;
	public float startPosition;

	@Override
	public void onParagraph(PdfWriter writer, Document document, float paragraphPosition) {
		this.startPosition = paragraphPosition;
	}

	@Override
	public void onParagraphEnd(PdfWriter writer, Document document, float paragraphPosition) {
		if (active) {
			PdfContentByte cb = writer.getDirectContentUnder();
			cb.setColorStroke(BaseColor.BLUE);
			cb.moveTo(document.left(), paragraphPosition - offset);
			cb.lineTo(document.right(), paragraphPosition - offset);
			// für Rahmen 
			//cb.rectangle(document.left(), paragraphPosition - offset,
			 //document.right() - document.left(), startPosition -
			 //paragraphPosition);
			cb.stroke();
		}
	}
}