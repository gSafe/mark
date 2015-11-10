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

import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

public class TestMarkClient extends JerseyTest {
    
    protected static final int PORT = 9998;
    
    protected MarkClient client;

    public TestMarkClient() {
        super(new WebAppDescriptor.Builder("fr.novapost.mark.mock").servletClass(com.sun.jersey.spi.container.servlet.ServletContainer.class)//
                .initParam("com.sun.jersey.api.json.POJOMappingFeature", "true").contextPath("/").build());
        client = new MarkClient("http://localhost:" + PORT);
    }

}

