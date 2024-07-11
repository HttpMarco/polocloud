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

import dev.httpmarco.osgan.files.OsganFile;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.logging.Logger;
import dev.httpmarco.polocloud.base.CloudBase;
import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.DefaultCommand;
import dev.httpmarco.polocloud.base.terminal.commands.SubCommand;
import dev.httpmarco.polocloud.base.terminal.commands.SubCommandCompleter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.jline.reader.Candidate;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.List;

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
        if (serviceNotExists(name)) return;
        final var service = CloudAPI.instance().serviceProvider().find(name);

        this.logger.info("Name&2: &3" + name);
        this.logger.info("Platform&2: &3" + service.group().platform().version());
        this.logger.info("Current memory&2: &3" + service.currentMemory() + "mb");
        this.logger.info("Players&2: &3" + service.onlinePlayersCount());
        this.logger.info("Maximal players&2: &3" + service.maxPlayers());
        this.logger.info("Port &2: &3" + service.port());
        this.logger.info("State&2: &3" + service.state());
        this.logger.info("Properties &2(&1" + service.properties().pool().size() + "&2): &3");

        service.properties().pool().forEach((groupProperties, o) -> this.logger.info("   &2- &1" + groupProperties + " &2= &1" + o));
    }

    @SubCommandCompleter(completionPattern = {"<name>"})
    public void completeInfoMethod(int index, List<Candidate> candidates) {
        if (index == 1) {
            candidates.addAll(CloudAPI.instance().serviceProvider().services().stream().map(it -> new Candidate(it.name())).toList());
        }
    }

    @SubCommand(args = {"<name>", "log"})
    public void handleLog(String name) {
        if (serviceNotExists(name)) return;

        for (var log : CloudAPI.instance().serviceProvider().find(name).log()) {
            this.logger.info("&3" + name + "&2: &1" + log);
        }
    }

    @SubCommandCompleter(completionPattern = {"<name>", "log"})
    public void completeLogMethod(int index, List<Candidate> candidates) {
        if (index == 2) {
            candidates.add(new Candidate("log"));
        }
    }

    //TODO screen command

    @SubCommand(args = {"<name>", "shutdown"})
    public void handleShutdown(String name) {
        if (serviceNotExists(name)) return;

        CloudAPI.instance().serviceProvider().find(name).shutdown();
    }

    @SubCommandCompleter(completionPattern = {"<name>", "shutdown"})
    public void completeShutdownMethod(int index, List<Candidate> candidates) {
        if (index == 2) {
            candidates.add(new Candidate("shutdown"));
        }
    }

    @SubCommand(args = {"<name>", "execute", "<command...>"})
    public void handelExecuteCommand(String name, String command) {
        if (serviceNotExists(name)) return;

        CloudAPI.instance().serviceProvider().find(name).execute(command);
        this.logger.info("&4" + CloudAPI.instance().nodeService().localNode().name() + "&1 -> &4" + name + " &2 | &1" + command);
    }

    @SubCommandCompleter(completionPattern = {"<name>", "execute", "<command...>"})
    public void completeExecuteCommandMethod(int index, List<Candidate> candidates) {
        if (index == 2) {
            candidates.add(new Candidate("execute"));
        }
    }

    @SneakyThrows
    @SubCommand(args = {"<name>", "copy", "<template>"})
    public void handleCopy(String name, String template) {
        if (serviceNotExists(name)) return;

        final var templatesService = CloudBase.instance().templatesService();
        if (templatesService.template(template) == null) {
            templatesService.createTemplates(template, "every", "every_server");
        }

        var service = CloudAPI.instance().serviceProvider().find(name);
        var runningFolder = ((LocalCloudService) service).runningFolder();
        var templateFolder = Path.of("templates", template);

        //add a filter so the session.lock file wont be copied as well otherwise it will throw an error
        var filter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return !file.getName().equalsIgnoreCase("session.lock");
            }
        };

        OsganFile.create(templateFolder);
        FileUtils.copyDirectory(runningFolder.toFile(), templateFolder.toFile(), filter);

        this.logger.success("The Service &2'&4" + service.name() + "&2' &1has been copied to the template &2'&4" + template + "&2'");
    }

    @SubCommandCompleter(completionPattern = {"<name>", "copy", "<template>"})
    public void completeCopyMethod(int index, List<Candidate> candidates) {
        if (index == 2) {
            candidates.add(new Candidate("copy"));
        } else if (index == 3) {
            candidates.addAll(CloudBase.instance().templatesService().templates().stream().map(it -> new Candidate(it.id())).toList());
        }
    }

    //todo property commands

    private boolean serviceNotExists(String name) {
        var serviceProvider = CloudAPI.instance().serviceProvider();
        if (serviceProvider == null) return true;

        var service = serviceProvider.find(name);
        if (service != null) return false;

        this.logger.info("This services does not exists&2!");
        return true;
    }
}
