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

import java.io.InputStream;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;
import services.ContentService;

@Path("/documents")
public class DocumentResource {

    private final ContentService contentService = new ContentService();

    private static final Logger LOGGER = LoggerFactory.getLogger("DocumentResource");

    @GET
    @Path("info")
    public String info() {
        return "Mark resource";
    }

    @POST
    @Path("watermark")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/octet-stream")
    public Response duplicate(@FormDataParam("file") InputStream stream, @FormDataParam("file") FormDataContentDisposition fileDetail, @FormDataParam("file") FormDataBodyPart body,
            @FormDataParam("watermark") String watermark, @FormDataParam("info") String info) {
        try {
            if (Strings.isNullOrEmpty(watermark) && Strings.isNullOrEmpty(info)) {
                LOGGER.warn("Neither the watermark, nor the information data has been filled.");
                return Response.ok(stream)//
                        .header("Content-Type", body.getMediaType().toString())//
                        .header("Content-Disposition", String.format("attachment; filename=\"%s\"", fileDetail.getFileName()))//
                        .build();
            }

            if (stream == null) {
                return Response.status(400).entity("A file stream is mandatory and cannot be empty!").build();
            }

            InputStream stampedDoc = contentService.watermarking(stream, watermark, info);
            return Response.ok(stampedDoc)//
                    .header("Content-Type", body.getMediaType().toString())//
                    .header("Content-Disposition", String.format("attachment; filename=\"%s\"", fileDetail.getFileName()))//
                    .build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("datamatrix")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/octet-stream")
    public Response stampDatamatrix(@FormDataParam("file") InputStream stream, @FormDataParam("file") FormDataContentDisposition fileDetail, @FormDataParam("file") FormDataBodyPart body,
            @FormDataParam("barcodeData") String barcodeData, @FormDataParam("x") int x, @FormDataParam("y") int y) {
        try {
            if (Strings.isNullOrEmpty(barcodeData)) {
                LOGGER.warn("The datamatrix has not been filled.");
                return Response.ok(stream)//
                        .header("Content-Type", body.getMediaType().toString())//
                        .header("Content-Disposition", String.format("attachment; filename=\"%s\"", fileDetail.getFileName()))//
                        .build();
            }

            if (stream == null) {
                return Response.status(400).entity("A file stream is mandatory and cannot be empty!").build();
            }

            InputStream stampedDoc = contentService.barcodeMarking2D(stream, barcodeData, x, y);
            return Response.ok(stampedDoc)//
                    .header("Content-Type", body.getMediaType().toString())//
                    .header("Content-Disposition", String.format("attachment; filename=\"%s\"", fileDetail.getFileName()))//
                    .build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

}
