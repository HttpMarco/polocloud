package dev.httpmarco.polocloud.launcher;

public final class PolocloudLauncher {

    public final PolocloudProcess polocloudProcess;

    public PolocloudLauncher() {

        Runtime.getRuntime().addShutdownHook(new Thread() {
            // todo
        });

        this.polocloudProcess = new PolocloudProcess();
        this.polocloudProcess.setDaemon(false);
        this.polocloudProcess.start();
    }
}