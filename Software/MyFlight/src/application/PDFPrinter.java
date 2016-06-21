package application;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.swing.JOptionPane;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFRenderer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PDFPrinter {

	@FXML Label lbl_dbconnect;
	
    public PDFPrinter(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();
            ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            PDFFile pdfFile = new PDFFile(bb); // Create PDF Print Page
            PDFPrintPage pages = new PDFPrintPage(pdfFile);

            // Create Print Job
            PrinterJob pjob = PrinterJob.getPrinterJob();
            PageFormat pf = PrinterJob.getPrinterJob().defaultPage();
            Paper a4paper = new Paper();
            double paperWidth = 8.26;
            double paperHeight = 11.69;
            a4paper.setSize(paperWidth * 72.0, paperHeight * 72.0);

            /*
             * set the margins respectively the imageable area
             */
            double leftMargin = 0.3;
            double rightMargin = 0.3;
            double topMargin = 0.5;
            double bottomMargin = 0.5;

            a4paper.setImageableArea(leftMargin * 72.0, topMargin * 72.0,
                    (paperWidth - leftMargin - rightMargin) * 72.0,
                    (paperHeight - topMargin - bottomMargin) * 72.0);
            pf.setPaper(a4paper);

            pjob.setJobName(file.getName());
            Book book = new Book();
            book.append(pages, pf, pdfFile.getNumPages());
            pjob.setPageable(book);

            // Send print job to default printer
          if (pjob.printDialog()) {
  
            pjob.print();
            }
        } catch (IOException e) {
        	lbl_dbconnect.setText("Druckdatei nicht vorhanden");
            e.printStackTrace();
         
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(null, "Printing Error: "
                    + e.getMessage(), "Print Aborted",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    class PDFPrintPage implements Printable {
        private PDFFile file;

        PDFPrintPage(PDFFile file) {
            this.file = file;
        }

        public int print(Graphics g, PageFormat format, int index)
                throws PrinterException {
            int pagenum = index + 1;

            // don't bother if the page number is out of range.
            if ((pagenum >= 1) && (pagenum <= file.getNumPages())) {
                // fit the PDFPage into the printing area
                Graphics2D g2 = (Graphics2D) g;
                PDFPage page = file.getPage(pagenum);
                double pwidth = format.getImageableWidth();
                double pheight = format.getImageableHeight();

                double aspect = page.getAspectRatio();
                double paperaspect = pwidth / pheight;

                Rectangle imgbounds;

                if (aspect > paperaspect) {
                    // paper is too tall / pdfpage is too wide
                    int height = (int) (pwidth / aspect);
                    imgbounds = new Rectangle(
                            (int) format.getImageableX(),
                            (int) (format.getImageableY() + ((pheight - height) / 2)),
                            (int) pwidth, height);
                } else {
                    // paper is too wide / pdfpage is too tall
                    int width = (int) (pheight * aspect);
                    imgbounds = new Rectangle(
                            (int) (format.getImageableX() + ((pwidth - width) / 2)),
                            (int) format.getImageableY(), width, (int) pheight);
                }

                // render the page
                PDFRenderer pgs = new PDFRenderer(page, g2, imgbounds, null,
                        null);
                try {
                    page.waitForFinish();
                    pgs.run();
                } catch (InterruptedException ie) {
                }

                return PAGE_EXISTS;
            } else {
                return NO_SUCH_PAGE;
            }
        }
    }
}