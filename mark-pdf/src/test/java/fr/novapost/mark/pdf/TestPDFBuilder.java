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

import com.google.common.io.ByteStreams;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class TestPDFBuilder {
    
    @Test
    public void shouldStampText() throws Exception {
        File pdf = loadFile("test.pdf");
        File stamped = File.createTempFile("stamp", ".pdf");
        try {
            InputStream is = PDFBuilder.stampText(new FileInputStream(pdf), "DUPLICATA", "Document téléchargé le mardi 24 avril 2014\n par Kevin Henri de la Villiardière.");
            ByteStreams.copy(is, new FileOutputStream(stamped));
            assertEquals("this is a template\nDU\nPL\nICA\nTA\nDocument téléchargé le mardi 24 avril 2014\n par Kevin Henri de la Villiardière.\n", PDFContentExtractor.extractText(stamped));
        } finally {
            stamped.delete();
        }
    }

    private File loadFile(String fileNameForClassLoader) {
        return new File(this.getClass().getClassLoader().getResource(fileNameForClassLoader).getFile());
    }
}
