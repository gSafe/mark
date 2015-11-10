package fr.novapost.mark.mock;

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

import com.google.common.base.Strings;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Path("/documents")
public class MockDocumentResource {

    @POST
    @Path("watermark")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/octet-stream")
    public Response duplicate(@FormDataParam("file") InputStream stream, @FormDataParam("file") FormDataContentDisposition fileDetail, @FormDataParam("file") FormDataBodyPart body,
                              @FormDataParam("watermark") String watermark, @FormDataParam("info") String info) {
        if (stream == null) {
            return Response.status(400).entity("A file stream is mandatory and cannot be empty!").build();
        }
        
        return Response.ok().build();
    }

    @POST
    @Path("datamatrix")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/octet-stream")
    public Response stampDatamatrix(@FormDataParam("file") InputStream stream, @FormDataParam("file") FormDataContentDisposition fileDetail, @FormDataParam("file") FormDataBodyPart body,
                                    @FormDataParam("barcodeData") String barcodeData, @FormDataParam("x") int x, @FormDataParam("y") int y) {
        if (stream == null) {
            return Response.status(400).entity("A file stream is mandatory and cannot be empty!").build();
        }
        
        return Response.ok().build();
    }
}
