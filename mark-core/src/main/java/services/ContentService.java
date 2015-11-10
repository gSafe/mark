package services;

/*
 * #%L
 * mark-core
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

import java.io.IOException;
import java.io.InputStream;

import fr.novapost.mark.pdf.PDFBuilder;

public class ContentService {

    public InputStream watermarking(InputStream fileStream, String watermark, String info) throws IOException {
        try {
            return PDFBuilder.stampText(fileStream, watermark, info);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public InputStream barcodeMarking2D(InputStream fileStream, String barcodeData, int x, int y) throws IOException {
        try {
            return PDFBuilder.stampDatamatrix(fileStream, barcodeData, x, y);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }
}
