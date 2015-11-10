package fr.novapost.mark;

/*
 * #%L
 * mark-client
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class TestMarkClient_stampDatamatrix extends TestMarkClient {

    @Test
    public void shouldFailIfMissingFile() {
        try {
            client.stampDatamatrix(null, "text for datamatrix", 0, 0);
            fail("Should throw an exception when missing file");
        } catch (Exception e) {
            assertEquals("Mark call error : 400 A file stream is mandatory and cannot be empty!.",e.getMessage());
        }
    }

    @Test
    public void shouldPassIfMissingDatamatrix() throws IOException {
        File file = new File("src/test/resources/bulletin.pdf");
        InputStream inputStream = client.stampDatamatrix(file, null, 0, 0);
        assertNotNull(inputStream);
    }


    @Test
    public void shouldStampDatamatrix() throws IOException {
        File file = new File("src/test/resources/bulletin.pdf");
        InputStream inputStream = client.stampDatamatrix(file, "créé par Novapost", 0, 0);
        assertNotNull(inputStream);
    }
}
