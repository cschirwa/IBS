package com.kt.ibs.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.kt.ibs.controllers.vo.AccountDetails;
import com.kt.ibs.controllers.vo.Transaction;
import com.kt.ibs.entity.AccountTransaction;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public final class StatementService {

    public static ByteArrayOutputStream generateTransactionConfirmation(final AccountTransaction transaction) {
        try {
            Path path = Paths.get(StatementService.class.getClassLoader()
                    .getResource("logo.png").toURI());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            Image img = Image.getInstance(path.toAbsolutePath().toString());
            document.add(img);

            PdfPTable table = new PdfPTable(3);
            addTableHeader(table);
            addRows(table);
            addCustomRows(table);
            document.add(table);

            document.close();
            log.info("rturning {}", byteArrayOutputStream.toByteArray());
            return byteArrayOutputStream;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ByteArrayOutputStream generateAccountConfirmation(final AccountDetails details) throws DocumentException, URISyntaxException, MalformedURLException, IOException {
        Path path = Paths.get(StatementService.class.getClassLoader()
                .getResource("logo.png").toURI());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();
        Image img = Image.getInstance(path.toAbsolutePath().toString());
        document.add(img);

        PdfPTable table = new PdfPTable(3);
        addTableHeader(table);
        addRows(table);
        addCustomRows(table);
        document.add(table);

        document.close();
        log.info("rturning {}", byteArrayOutputStream.toByteArray());
        return byteArrayOutputStream;
    }

    private static void addTableHeader(final PdfPTable table) {
        Stream.of("Date", "Description", "Reference", "Amount")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private static void addRows(final PdfPTable table) {

        table.addCell("row 1, col 1");
        table.addCell("row 1, col 2");
        table.addCell("row 1, col 3");
    }
    
    private static void addRow(final PdfPTable table, final String data) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setBorderWidth(2);
        cell.setPhrase(new Phrase(data));
        table.addCell(cell);
    }

    private static void addCustomRows(final PdfPTable table)
            throws URISyntaxException, BadElementException, IOException {
//        Path path = Paths.get(ClassLoader.getSystemResource("metbank-logo.png").toURI());
        //Image img = Image.getInstance(path.toAbsolutePath().toString());
     //   img.scalePercent(10);

       // PdfPCell imageCell = new PdfPCell(img);
        //table.addCell(imageCell);

        PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
        horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(horizontalAlignCell);

        PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
        verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(verticalAlignCell);
    }
    
    
    public static ByteArrayOutputStream createPDF (final AccountDetails details) throws DocumentException{
         Font bfBold12 = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0)); 
         Font bf12 = new Font(FontFamily.TIMES_ROMAN, 12); 
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         Document document = new Document();
         PdfWriter.getInstance(document, byteArrayOutputStream);
         document.open();
         
         //document header attributes
         document.addAuthor("Metbank");
         document.addCreationDate();
        document.addProducer();
         document.addCreator("www.metbank.co.zw");
         document.addTitle("Transactions for acount "+ details.getAccountNumber());
         document.setPageSize(PageSize.LETTER);
        

         //create a paragraph
         Paragraph paragraph = new Paragraph(String.format( "Transactions for account %s the period", details.getAccountNumber()));
         
         
         //specify column widths
         //float[] columnWidths = {1.5f, 2f, 5f, 2f, 3f};
         //create PDF table with the given widths
         PdfPTable table = new PdfPTable(5);
         /*
         Stream.of("Date", "Description", "Reference", "Amount", "Closing Balance")
         .forEach(columnTitle -> {
             PdfPCell header = new PdfPCell();
             header.setBackgroundColor(BaseColor.LIGHT_GRAY);
             header.setBorderWidth(2);
             header.setPhrase(new Phrase(columnTitle));
             table.addCell(header);
         });
         */
         // set table width a percentage of the page width
         //table.setWidthPercentage(90f);

         //insert column headings
         
         insertCell(table, "Date", Element.ALIGN_RIGHT, 1, bfBold12);
         insertCell(table, "Description", Element.ALIGN_LEFT, 1, bfBold12);
         insertCell(table, "Reference", Element.ALIGN_LEFT, 1, bfBold12);
         insertCell(table, "Amount", Element.ALIGN_LEFT, 1, bfBold12);
         insertCell(table, "Closing Balance", Element.ALIGN_RIGHT, 1, bfBold12);
         table.setHeaderRows(1);
         

         //insert an empty row
        // insertCell(table, "", Element.ALIGN_LEFT, 5, bfBold12);
         //create section heading by cell merging
         //insertCell(table, "Tr ...", Element.ALIGN_LEFT, 5, bfBold12);
         
         //just some random data to fill 
         for( Transaction transaction:details.getTransactions()){
          insertCell(table, transaction.getValueDate().toString(), Element.ALIGN_LEFT, 1, bf12);
          insertCell(table, transaction.getReference(), Element.ALIGN_LEFT, 1, bf12);
          insertCell(table, transaction.getDescription(), Element.ALIGN_LEFT, 1, bf12);
          insertCell(table, transaction.getTxnAmount().toPlainString(), Element.ALIGN_RIGHT, 1, bf12);
          insertCell(table, transaction.getClosingBalance().toPlainString(), Element.ALIGN_RIGHT, 1, bf12);
        
         }
                
         //add the PDF table to the paragraph 
         paragraph.add(table);
         // add the paragraph to the document
         document.add(paragraph);
         document.close();
         return byteArrayOutputStream;
   
 
       }
       
       private static void insertCell(final PdfPTable table, final String text, final int align, final int colspan, final Font font){
           //addRow(table, text);
           
        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
         cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);
        
        
       }
}
