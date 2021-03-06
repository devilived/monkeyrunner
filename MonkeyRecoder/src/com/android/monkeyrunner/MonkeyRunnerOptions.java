/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.monkeyrunner;

import com.google.common.collect.ImmutableList;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MonkeyRunnerOptions {
    private static final Logger LOG = Logger.getLogger(MonkeyRunnerOptions.class.getName());
    private static String DEFAULT_MONKEY_SERVER_ADDRESS = "127.0.0.1";
    private static int DEFAULT_MONKEY_PORT = 12345;

    private final int port;
    private final String hostname;
    private final File scriptFile;
    private final String backend;
    private final Collection<File> plugins;
    private final Collection<String> arguments;
    private final Level logLevel;

    private MonkeyRunnerOptions(String hostname, int port, File scriptFile, String backend,
            Level logLevel, Collection<File> plugins, Collection<String> arguments) {
        this.hostname = hostname;
        this.port = port;
        this.scriptFile = scriptFile;
        this.backend = backend;
        this.logLevel = logLevel;
        this.plugins = plugins;
        this.arguments = arguments;
    }

    public int getPort() {
        return port;
    }

    public String getHostname() {
        return hostname;
    }

    public File getScriptFile() {
        return scriptFile;
    }

    public String getBackendName() {
        return backend;
    }

    public Collection<File> getPlugins() {
        return plugins;
    }

    public Collection<String> getArguments() {
        return arguments;
    }

    public Level getLogLevel() {
        return logLevel;
    }

    private static void printUsage(String message) {
        System.out.println(message);
        System.out.println("Usage: monkeyrunner [options] SCRIPT_FILE");
        System.out.println("");
        System.out.println("    -s      MonkeyServer IP Address.");
        System.out.println("    -p      MonkeyServer TCP Port.");
        System.out.println("    -v      MonkeyServer Logging level (ALL, FINEST, FINER, FINE, CONFIG, INFO, WARNING, SEVERE, OFF)");
        System.out.println("");
        System.out.println("");
    }

    /**
     * Process the command-line options
     *
     * @return the parsed options, or null if there was an error.
     */

    public static MonkeyRunnerOptions processOptions(String[] args,String path) {
        // parse command line parameters.

        String hostname = DEFAULT_MONKEY_SERVER_ADDRESS;
        File scriptFile = new File("monkey_playback.py");
        int port = DEFAULT_MONKEY_PORT;
        String backend = "adb";
        Level logLevel = Level.SEVERE;

        ImmutableList.Builder<File> pluginListBuilder = ImmutableList.builder();
        ImmutableList.Builder<String> argumentBuilder = ImmutableList.builder();
        argumentBuilder.add(path);

        return new MonkeyRunnerOptions(hostname, port, scriptFile, backend, logLevel,
                pluginListBuilder.build(), argumentBuilder.build());
    }
}
