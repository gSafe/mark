package fr.novapost.mark.pdf;

/*
 * #%L
 * mark-pdf
 * %%
 * Copyright (C) 2013 - 2015 gSafe
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.itextpdf.text.pdf.PdfReader;

public class PDFContentExtractor {

    private PDFContentExtractor() {
        // Hide the constructor
    }

    /**
     * EXtract text content from PDF (using pdfbox)
     * 
     * @param pdf
     *            pdf input file
     * @return text content
     * @throws java.io.IOException
     */
    public static String extractText(File pdf) throws IOException {
        return extractText(new FileInputStream(pdf));
    }

    public static String extractText(InputStream src) throws IOException {
        StringBuilder text = new StringBuilder();
        COSDocument cosDoc = null;
        PDDocument pdDoc = null;
        try {
            PDFParser parser = new PDFParser(src);
            parser.parse();
            cosDoc = parser.getDocument();
            PDFTextStripper stripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            int nbPages = pdDoc.getDocumentCatalog().getPages().getCount();
            for (int i = 0; i < nbPages; i++) {
                stripper.setStartPage(i + 1);
                stripper.setEndPage(i + 1);
                text.append(stripper.getText(pdDoc));
            }
        } finally {
            try {
                if (cosDoc != null) {
                    cosDoc.close();
                }
            } catch (IOException e) {
                // Do nada
            }
            try {
                if (pdDoc != null) {
                    pdDoc.close();
                }
            } catch (IOException e) {
                // Do nada
            }
        }
        return text.toString();
    }

    public static int getPageCount(File pdf) throws IOException {
        PdfReader reader = new PdfReader(pdf.getAbsolutePath());
        try {
            return reader.getNumberOfPages();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
