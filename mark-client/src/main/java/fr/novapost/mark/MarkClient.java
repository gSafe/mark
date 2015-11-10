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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;

import com.google.common.base.Strings;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import com.sun.jersey.multipart.impl.MultiPartWriter;

public class MarkClient {

    private Client client;

    private String serviceUrl;

    public MarkClient(String url) {
        if (Strings.isNullOrEmpty(url)) {
            throw new IllegalArgumentException("Mark service url can't be empty.");
        }
        ClientConfig config = new DefaultClientConfig();
        config.getClasses().add(MultiPartWriter.class);
        client = Client.create(config);
        client.setConnectTimeout(30000);
        client.setReadTimeout(30000);
        serviceUrl = url;
    }

    public InputStream duplicate(File file, String watermark, String info) throws IOException {
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

        ClientResponse response = client.resource(serviceUrl + "/documents/watermark").type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, fdmp);
        checkStatus(response);

        return response.getEntityInputStream();
    }

    public InputStream stampDatamatrix(File file, String barcodeData, int x, int y) throws IOException {
        FormDataMultiPart fdmp = new FormDataMultiPart();

        if (barcodeData != null) {
            fdmp.field("barcodeData", barcodeData);
        }

        fdmp.field("x", String.valueOf(x));
        fdmp.field("y", String.valueOf(y));

        if (file != null) {
            fdmp.bodyPart(new FileDataBodyPart("file", file, MediaType.TEXT_PLAIN_TYPE));
        }

        ClientResponse response = client.resource(serviceUrl + "/documents/datamatrix").type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, fdmp);
        checkStatus(response);

        return response.getEntityInputStream();
    }

    private void checkStatus(ClientResponse response) throws IOException {
        if (ClientResponse.Status.OK.getStatusCode() != response.getStatus()) {
            throw new IOException(String.format("Mark call error : %s %s.", response.getStatus(), response.getEntity(String.class)));
        }
    }
}
