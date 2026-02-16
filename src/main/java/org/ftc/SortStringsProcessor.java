package org.ftc;

import org.ftc.analyzer.IAnalyzer;
import org.ftc.analyzer.StateAnalyzer;
import org.ftc.analyzer.StringType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class SortStringsProcessor {
    private final List<String> inputFilesPaths;
    private final IAnalyzer analyzer;

    public SortStringsProcessor(List<String> inputFilesPaths) {
        this.inputFilesPaths = inputFilesPaths;
        this.analyzer = new StateAnalyzer();
    }

    public void process() {
        for (String path: inputFilesPaths) {
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                String line;
                while ((line = br.readLine()) != null) {
                    process(line);
                }
            } catch (IOException e) {
                System.out.println("Warning! File " + path + " not found!");
            }
        }
    }

    private void process(String str) {
        StringType stringType = analyzer.analyze(str);
        System.out.println(str + ": " + stringType);
    }
}
