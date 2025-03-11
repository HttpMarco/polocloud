package dev.httpmarco.polocloud.launcher;

import dev.httpmarco.polocloud.launcher.encoding.WindowsUtf8Console;

public final class PolocloudLauncher {

    public final PolocloudProcess polocloudProcess;

    public PolocloudLauncher() {

        // set utf/8 encoding -> windows
        WindowsUtf8Console.setUtf8Encoding();

        this.polocloudProcess = new PolocloudProcess();
        this.polocloudProcess.setDaemon(false);
        this.polocloudProcess.start();
    }
}