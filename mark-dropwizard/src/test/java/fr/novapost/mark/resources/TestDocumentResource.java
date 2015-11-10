package fr.novapost.mark.resources;

/*
 * #%L
 * mark-dropwizard
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.junit.ClassRule;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import io.dropwizard.testing.junit.ResourceTestRule;

public class TestDocumentResource {

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder().addResource(new DocumentResource()).build();

    @Test
    public void testGETInfo() {
        String info = resources.client().resource("/documents/info").get(String.class);
        assertEquals("Mark resource", info);
    }

    @Test
    public void duplicate_shouldReturn200_ifEmptyWatermark() throws FileNotFoundException {
        FormDataMultiPart fdmp = createFormDataMultiPart(loadFile("pdf-sample.pdf"), null, "created by Novapost");
        ClientResponse response = resources.client().resource("/documents/watermark").type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, fdmp);
        assertEquals(200, response.getStatus());
        assertEquals("attachment; filename=\"pdf-sample.pdf\"", response.getHeaders().get("Content-Disposition").get(0));
    }

    @Test
    public void duplicate_shouldReturn200_ifEmptyInfo() throws IOException {
        FormDataMultiPart fdmp = createFormDataMultiPart(loadFile("pdf-sample.pdf"), "watermark", null);
        ClientResponse response = resources.client().resource("/documents/watermark").type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, fdmp);
        assertEquals(200, response.getStatus());
        assertEquals("attachment; filename=\"pdf-sample.pdf\"", response.getHeaders().get("Content-Disposition").get(0));
    }

    @Test
    public void duplicate_shouldReturn200() throws IOException {
        FormDataMultiPart fdmp = createFormDataMultiPart(loadFile("pdf-sample.pdf"), "watermark", "created by Novapost");
        ClientResponse response = resources.client().resource("/documents/watermark").type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, fdmp);
        assertEquals(200, response.getStatus());
        assertEquals("attachment; filename=\"pdf-sample.pdf\"", response.getHeaders().get("Content-Disposition").get(0));
    }

    @Test
    public void duplicate_shouldReturn200ForDatamatrix() throws IOException {
        FormDataMultiPart fdmp = createFormDataMultiPart(loadFile("pdf-sample.pdf"), "create datamatrix", 100, 200);
        ClientResponse response = resources.client().resource("/documents/datamatrix").type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, fdmp);
        assertEquals(200, response.getStatus());
        assertEquals("attachment; filename=\"pdf-sample.pdf\"", response.getHeaders().get("Content-Disposition").get(0));
    }

    @Test
    public void duplicate_shouldReturn200_ifEmptyWatermarkAndInfo() throws FileNotFoundException {
        FormDataMultiPart fdmp = createFormDataMultiPart(loadFile("pdf-sample.pdf"), null, null);
        ClientResponse response = resources.client().resource("/documents/watermark").type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, fdmp);
        assertEquals(200, response.getStatus());
        assertEquals("attachment; filename=\"pdf-sample.pdf\"", response.getHeaders().get("Content-Disposition").get(0));
    }
    
    @Test
    public void duplicate_shouldReturn200_ifEmptyXAndY() throws FileNotFoundException {
        FormDataMultiPart fdmp = createFormDataMultiPart(loadFile("pdf-sample.pdf"), "create datamatrix");
        ClientResponse response = resources.client().resource("/documents/datamatrix").type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, fdmp);
        assertEquals(200, response.getStatus());
        assertEquals("attachment; filename=\"pdf-sample.pdf\"", response.getHeaders().get("Content-Disposition").get(0));
    }

    @Test
    public void duplicate_shouldReturn400_ifEmptyDocument() throws FileNotFoundException {
        FormDataMultiPart fdmp = createFormDataMultiPart((File) null, "watermark", "created by Novapost");
        ClientResponse response = resources.client().resource("/documents/watermark").type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, fdmp);
        assertEquals(400, response.getStatus());
        assertEquals("A file stream is mandatory and cannot be empty!", response.getEntity(String.class));
    }

    @Test
    public void duplicate_shouldReturn400_ifEmptyDocumentForDatamatrix() throws FileNotFoundException {
        FormDataMultiPart fdmp = createFormDataMultiPart((File) null, "create datamatrix", 100, 200);
        ClientResponse response = resources.client().resource("/documents/datamatrix").type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, fdmp);
        assertEquals(400, response.getStatus());
        assertEquals("A file stream is mandatory and cannot be empty!", response.getEntity(String.class));
    }

    private FormDataMultiPart createFormDataMultiPart(File file, String watermark, String info) {
        FormDataMultiPart fdmp = new FormDataMultiPart();

        if (watermark != null) {
            fdmp.field("watermark", watermark);
        }

        if (info != null) {
            fdmp.field("info", info);
        }

        if (file != null) {
            fdmp.bodyPart(new FileDataBodyPart("file", file, MediaType.TEXT_PLAIN_TYPE));
        }
        return fdmp;
    }

    private FormDataMultiPart createFormDataMultiPart(File file, String barcodeData, int x, int y) {
        FormDataMultiPart fdmp = new FormDataMultiPart();

        if (barcodeData != null) {
            fdmp.field("barcodeData", barcodeData);
        }

        fdmp.field("x", String.valueOf(x));
        fdmp.field("y", String.valueOf(y));

        if (file != null) {
            fdmp.bodyPart(new FileDataBodyPart("file", file, MediaType.TEXT_PLAIN_TYPE));
        }
        return fdmp;
    }

    private FormDataMultiPart createFormDataMultiPart(File file, String barcodeData) {
        FormDataMultiPart fdmp = new FormDataMultiPart();

        if (barcodeData != null) {
            fdmp.field("barcodeData", barcodeData);
        }

        if (file != null) {
            fdmp.bodyPart(new FileDataBodyPart("file", file, MediaType.TEXT_PLAIN_TYPE));
        }
        return fdmp;
    }

    private File loadFile(String fileNameForClassLoader) {
        return new File(this.getClass().getClassLoader().getResource(fileNameForClassLoader).getFile());
    }
}
