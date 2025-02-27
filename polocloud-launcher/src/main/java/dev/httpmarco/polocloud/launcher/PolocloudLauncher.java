package dev.httpmarco.polocloud.launcher;

import dev.httpmarco.polocloud.launcher.encoding.WindowsUtf8Console;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public final class PolocloudLauncher {

    public final PolocloudProcess polocloudProcess;

    public PolocloudLauncher() {

        // set utf/8 encoding -> windows
        //WindowsUtf8Console.setUtf8Encoding();

        System.setProperty("file.encoding", "UTF-8");

        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            // todo
        });

        this.polocloudProcess = new PolocloudProcess();
        this.polocloudProcess.setDaemon(false);
        this.polocloudProcess.start();
    }
}