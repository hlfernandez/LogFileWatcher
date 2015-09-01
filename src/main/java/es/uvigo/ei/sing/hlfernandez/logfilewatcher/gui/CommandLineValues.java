package es.uvigo.ei.sing.hlfernandez.logfilewatcher.gui;

import java.io.File;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class CommandLineValues {
    @Option(name = "-l", aliases = { "--log-file" }, required = true,
            usage = "log file to watch")
    private File logFile;

    private boolean errorFree = false;

    public CommandLineValues(String... args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);

            errorFree = true;
        } catch (CmdLineException e) {
            parser.printUsage(System.err);
        }
    }
    
    public boolean isErrorFree() {
        return errorFree;
    }

    public File getLogFile() {
        return logFile;
    }
}
