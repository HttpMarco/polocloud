/*
 * Copyright 2024 Mirco Lindenau | HttpMarco
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.httpmarco.polocloud.base.services;

import dev.httpmarco.osgan.files.Files;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.logging.Logger;
import dev.httpmarco.polocloud.base.CloudBase;
import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.DefaultCommand;
import dev.httpmarco.polocloud.base.terminal.commands.SubCommand;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;

@Command(command = "service", aliases = {"services", "ser"}, description = "Manage all your online services")
public final class ServiceCommand {

    private final Logger logger = CloudAPI.instance().logger();

    @DefaultCommand
    public void handle() {
        logger.info("&3service &1list &2- &1List all online services&2.");
        logger.info("&3service &2<&1name&2> &2- &1All specific information&2.");
        logger.info("&3service &2<&1name&2> &1log &2- &1Get the last not read log lines&2.");
        logger.info("&3service &2<&1name&2> &1screen &2- &1Join into a service console&2.");
        logger.info("&3service &2<&1name&2> &1shutdown &2- &1Shutdown a specific service&2.");
        logger.info("&3service &2<&1name&2> &1execute &2<&1command...&2> &2- &1Execute a specific command on a service&2.");
        logger.info("&3service &2<&1name&2> &1copy &2<&1template&2> &2- &1Copy the service into an template&2.");
        logger.info("&3service &2<&1name&2> &1property set &2<&1key&2> &2<&1value&2> &2- &1Attach a new service property&2.");
        logger.info("&3service &2<&1name&2> &1property remove &2<&1key&2> &2- &1Remove a existing property&2.");
    }

    @SubCommand(args = {"list"})
    public void handleList() {
        var services = CloudAPI.instance().serviceProvider().services();
        logger.info("Following &3" + services.size() + " &1services are online&2:");
        services.forEach(service -> logger.info("&2- &4" + service.name() + "&2: (&1" + service + "&2)"));
    }

    @SubCommand(args = {"<name>"})
    public void handleInfo(String name) {
        var service = CloudAPI.instance().serviceProvider().service(name);
        if (service == null) {
            logger.info("This services does not exists&2!");
            return;
        }

        logger.info("Name&2: &3" + name);
        logger.info("Platform&2: &3" + service.group().platform().version());
        logger.info("Current memory&2: &3-1");
        logger.info("Players&2: &3-1");
        logger.info("Maximal players&2: &3" + service.maxPlayers());
        logger.info("Port &2: &3" + service.port());
        logger.info("State&2: &3" + service.state());
        logger.info("Properties &2(&1" + service.properties().properties().size() + "&2): &3");

        service.properties().properties().forEach((groupProperties, o) -> {
            logger.info("   &2- &1" + groupProperties.id() + " &2= &1" + o.toString());
        });
    }

    @SubCommand(args = {"<name>", "log"})
    public void handleLog(String name) {
        var service = CloudAPI.instance().serviceProvider().service(name);
        if (service == null) {
            logger.info("This services does not exists&2!");
            return;
        }

        for (var log : service.log()) {
            logger.info("&3" + name + "&2: &1" + log);
        }
    }

    @SubCommand(args = {"<name>", "screen"})
    public void handleScreen(String name) {
        var service = CloudAPI.instance().serviceProvider().service(name);
        if (service == null) {
            logger.info("This services does not exists&2!");
            return;
        }
        ((LocalCloudService) service).subscribeLog();
    }

    @SubCommand(args = {"<name>", "shutdown"})
    public void handleShutdown(String name) {
        var service = CloudAPI.instance().serviceProvider().service(name);
        if (service == null) {
            logger.info("This services does not exists&2!");
            return;
        }
        service.shutdown();
    }

    @SneakyThrows
    @SubCommand(args = {"<name>", "copy", "<template>"})
    public void handleCopy(String name, String template) {
        var service = CloudAPI.instance().serviceProvider().service(name);
        if (service == null) {
            logger.info("This services does not exists&2!");
            return;
        }

        final var temp = CloudBase.instance().templatesService().template(template);
        if (temp == null) {
            CloudBase.instance().templatesService().createTemplates(template, "every", "every_server");
        }

        var runningFolder = ((LocalCloudService) service).runningFolder();
        var templateFolder = Path.of("templates", template);

        //add a filter so the session.lock file wont be copied as well otherwise it will throw an error
        var filter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return !file.getName().equalsIgnoreCase("session.lock");
            }
        };

        Files.createDirectoryIfNotExists(templateFolder);
        FileUtils.copyDirectory(runningFolder.toFile(), templateFolder.toFile(), filter);

        logger.success("The Service &2'&4" + service.name() + "&2' &1has been copied to the template &2'&4" + template + "&2'");
    }
}
