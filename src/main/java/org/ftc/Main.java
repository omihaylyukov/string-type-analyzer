package org.ftc;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Parameters;

import java.util.List;

@Command(
        name = "string-type-analyzer",
        description = "CLI application that reads strings from files and sorts them into separate files: " +
                "floats.txt, integers.txt, strings.txt.",
        version = "1.0.0",
        mixinStandardHelpOptions = true
)
public class Main implements Runnable {

    @Option(names = {"-o", "--options"}, defaultValue = "/", description = "Specifies the folder path where the output files will be written.")
    String options;

    @Option(names = {"-p", "--prefix"}, defaultValue = "", description = "Sets the prefix for output file names. " +
            "For example, -p result_ will produce files like result_integers.txt, result_floats.txt, etc.")
    String prefix;

    @Option(
            names = {"-a", "--append-mode"},
            description = "Append to existing files instead of overwriting."
    )
    boolean appendMode;

    static class StatsMode {
        @Option(names = {"-s", "--short"}, description = "Collect short statistics. Short statistics include only the count of elements written to the output files.")
        boolean shortStats;

        @Option(names = {"-f", "--full"}, description = "Collect full statistics. Full statistics for numbers include minimum, maximum, sum, and average. " +
                "Full statistics for strings include count, length of the shortest string, and length of the longest string.")
        boolean fullStats;
    }

    @ArgGroup(multiplicity = "1")
    StatsMode mode;

    @Parameters(arity = "1..*", description = "Names of files containing a mix of integers, floats, and strings.")
    List<String> files;

    @Override
    public void run() {
        System.out.println(appendMode);
    }

    static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}
