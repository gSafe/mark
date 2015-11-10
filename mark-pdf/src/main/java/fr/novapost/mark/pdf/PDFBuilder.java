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

import com.google.common.base.Strings;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.*;

public class PDFBuilder {

    private static final int[] BARCODE_DIMENSIONS = { 10, 12, 14, 16, 18, 20, 22, 24, 26, 32, 36, 40, 44, 48, 52, 64, 72, 80, 88, 96, 104, 120, 132, 144 };

    public static InputStream stampText(InputStream pdfInput, String text, String info) throws Exception {
        if (Strings.isNullOrEmpty(text) && Strings.isNullOrEmpty(info)) {
            return pdfInput;
        }

        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        PdfGState gs = new PdfGState();
        ExtendedColor textColor = new GrayColor(0.6f);
        ExtendedColor infoColor = new GrayColor(0.4f);
        gs.setFillOpacity(0.40f);

        PdfReader reader = null;
        PdfStamper stamper = null;

        File tmpFile = File.createTempFile("nova", "stamp-label");
        OutputStream output = null;
        try {
            output = new FileOutputStream(tmpFile);
            reader = new PdfReader(pdfInput);
            stamper = new PdfStamper(reader, output);

            PdfContentByte over;
            PdfContentByte infoOver;
            for (int index = 1; index < reader.getNumberOfPages() + 1; ++index) {
                Rectangle size = reader.getPageSize(index);
                
                if (text != null) {
                    over = stamper.getOverContent(index);
                    over.setGState(gs);
                    over.beginText();
                    over.setFontAndSize(bf, size.getHeight() / 9);
                    over.setColorFill(textColor);
                    over.showTextAligned(PdfContentByte.ALIGN_LEFT, text, size.getWidth() / 6, size.getHeight() / 6, 40);
                    over.endText();
                }

                if (info != null) {
                    infoOver = stamper.getOverContent(index);
                    infoOver.setGState(gs);
                    infoOver.beginText();
                    infoOver.setFontAndSize(bf, 10);
                    infoOver.setColorFill(infoColor);
                    infoOver.showTextAligned(PdfContentByte.ALIGN_LEFT, info, 20, 10, 0);
                    infoOver.endText();
                }
            }
            pdfInput.close();
            return new FileInputStream(tmpFile);
        } finally {
            if (stamper != null) {
                stamper.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (output != null) {
                output.close();
            }
        }
    }

    public static InputStream stampDatamatrix(InputStream pdfInput, String barcodeData, int x, int y) throws Exception {
        if (Strings.isNullOrEmpty(barcodeData)) {
            return pdfInput;
        }

        PdfReader reader = null;
        PdfStamper stamper = null;

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            reader = new PdfReader(pdfInput);
            stamper = new PdfStamper(reader, output);

            for (int index = 1; index < reader.getNumberOfPages() + 1; ++index) {
                Image img = generateAutoSizedDatamatrix(barcodeData);
                img.setAbsolutePosition(x, y);
                stamper.getOverContent(index).addImage(img);
            }
            stamper.close();
            return new ByteArrayInputStream(output.toByteArray());
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (pdfInput != null) {
                pdfInput.close();
            }
        }

    }

    private static Image generateAutoSizedDatamatrix(String textToEncode) throws Exception {

        BarcodeDatamatrix barcode = new BarcodeDatamatrix();
        barcode.setOptions(BarcodeDatamatrix.DM_AUTO);
        int returnResult = BarcodeDatamatrix.DM_NO_ERROR;

        // try to generate the barcode, resizing as needed.
        for (int generateCount = 0; generateCount < BARCODE_DIMENSIONS.length; generateCount++) {
            barcode.setWidth(BARCODE_DIMENSIONS[generateCount]);
            barcode.setHeight(BARCODE_DIMENSIONS[generateCount]);
            returnResult = barcode.generate(textToEncode);
            if (returnResult == BarcodeDatamatrix.DM_NO_ERROR) {
                return barcode.createImage();
            }
        }

        throw new Exception("Error generating barcode. Error " + returnResult);
    }
}
