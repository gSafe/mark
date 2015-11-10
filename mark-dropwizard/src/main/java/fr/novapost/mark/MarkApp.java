package fr.novapost.mark;

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

import fr.novapost.mark.configuration.MarkConfiguration;
import fr.novapost.mark.resources.DocumentResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MarkApp extends Application<MarkConfiguration> {

    public static void main(String[] args) throws Exception {
        new MarkApp().run(args);
    }

    @Override
    public void initialize(Bootstrap<MarkConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/docs/", "/docs", "index.html"));
    }

    @Override
    public void run(MarkConfiguration configuration, Environment environment) {
        environment.jersey().register(new DocumentResource());
    }
}