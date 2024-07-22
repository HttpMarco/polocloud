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

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.base.Node;
import dev.httpmarco.polocloud.base.logging.Logger;
import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.DefaultCommand;
import dev.httpmarco.polocloud.base.terminal.commands.SubCommand;
import dev.httpmarco.polocloud.base.terminal.commands.SubCommandCompleter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jline.reader.Candidate;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.List;

@Command(command = "service", aliases = {"services", "ser"}, description = "Manage all your online services")
public final class ServiceCommand {

    private final Logger logger = Node.instance().logger();

    @DefaultCommand
    public void handle() {
        logger.info("&bservice &7list &8- &7List all online services&8.");
        logger.info("&bservice &8<&7name&8> &8- &7All specific information&8.");
        logger.info("&bservice &8<&7name&8> &7log &8- &7Get the last not read log lines&8.");
        logger.info("&bservice &8<&7name&8> &7screen &8- &7Join into a service console&8.");
        logger.info("&bservice &8<&7name&8> &7shutdown &8- &7Shutdown a specific service&8.");
        logger.info("&bservice &8<&7name&8> &7execute &8<&7command...&8> &8- &7Execute a specific command on a service&8.");
        logger.info("&bservice &8<&7name&8> &7copy &8<&7template&8> &8- &7Copy the service into an template&8.");
        logger.info("&bservice &8<&7name&8> &7property set &8<&7key&8> &8<&7value&8> &8- &7Attach a new service property&8.");
        logger.info("&bservice &8<&7name&8> &7property remove &8<&7key&8> &8- &7Remove a existing property&8.");
    }

    @SubCommand(args = {"list"})
    public void handleList() {
        var services = CloudAPI.instance().serviceProvider().services();
        logger.info("Following &b" + services.size() + " &7services are online&8:");
        services.forEach(service -> logger.info("&8- &4" + service.name() + "&8: (&7" + service + "&8)"));
    }

    @SubCommand(args = {"<name>"})
    public void handleInfo(String name) {
        if (serviceNotExists(name)) {
            return;
        }

        final var service = CloudAPI.instance().serviceProvider().find(name);

        this.logger.info("Name&8: &b" + name);
        this.logger.info("Platform&8: &b" + service.group().platform().version());
        this.logger.info("Current memory&8: &b" + service.currentMemory() + "mb");
        this.logger.info("Players&8: &b" + service.onlinePlayersCount());
        this.logger.info("Maximal players&8: &b" + service.maxPlayers());
        this.logger.info("Port &8: &b" + service.port());
        this.logger.info("State&8: &b" + service.state());
        this.logger.info("Properties &8(&7" + service.properties().pool().size() + "&8): &b");

        service.properties().pool().forEach((groupProperties, o) -> this.logger.info("   &8- &7" + groupProperties + " &8= &7" + o));
    }

    @SubCommandCompleter(completionPattern = {"<name>"})
    public void completeInfoMethod(int index, List<Candidate> candidates) {
        if (index == 1) {
            candidates.addAll(CloudAPI.instance().serviceProvider().services().stream().map(it -> new Candidate(it.name())).toList());
        }
    }

    @SubCommand(args = {"<name>", "log"})
    public void handleLog(String name) {
        if (serviceNotExists(name)) {
            return;
        }

        for (var log : CloudAPI.instance().serviceProvider().find(name).log()) {
            this.logger.info("&b" + name + "&8: &7" + log);
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
        if (serviceNotExists(name)) {
            return;
        }

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
        if (serviceNotExists(name)) {
            return;
        }

        CloudAPI.instance().serviceProvider().find(name).execute(command);
        this.logger.info("&4" + Node.instance().nodeProvider().localEndpoint().data().id() + "&7 -> &4" + name + " &8 | &7" + command);
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
        if (serviceNotExists(name)) {
            return;
        }

        final var templatesService = Node.instance().templatesService();
        if (templatesService.template(template) == null) {
            templatesService.createTemplates(template, "every", "every_server");
        }

        var service = CloudAPI.instance().serviceProvider().find(name);
        var runningFolder = ((LocalCloudService) service).runningFolder();
        var templateFolder = Path.of("templates", template);

        //add a filter so the session.lock file wont be copied as well otherwise it will throw an error
        var filter = new FileFilter() {
            @Override
            public boolean accept(@NotNull File file) {
                return !file.getName().equalsIgnoreCase("session.lock");
            }
        };


        //todo communicate with other nodes
        // OsganFile.create(templateFolder);
        //FileUtils.copyDirectory(runningFolder.toFile(), templateFolder.toFile(), filter);

        this.logger.success("The Service &8'&4" + service.name() + "&8' &7has been copied to the template &8'&4" + template + "&8'");
    }

    @SubCommandCompleter(completionPattern = {"<name>", "copy", "<template>"})
    public void completeCopyMethod(int index, List<Candidate> candidates) {
        if (index == 2) {
            candidates.add(new Candidate("copy"));
        } else if (index == 3) {
            candidates.addAll(Node.instance().templatesService().templates().stream().map(it -> new Candidate(it.id())).toList());
        }
    }

    //todo property commands

    private boolean serviceNotExists(String name) {
        var serviceProvider = CloudAPI.instance().serviceProvider();
        if (serviceProvider == null) {
            return true;
        }

        var service = serviceProvider.find(name);
        if (service != null) {
            return false;
        }

        this.logger.info("This services does not exists&8!");
        return true;
    }
}
