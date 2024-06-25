package com.tat.mobile.analyzer.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
public class MapToCsvConverter {

    public static void mapToCsv(Map<String, List<Map<String, Object>>> data, String outputPath) {
        try (FileWriter writer = new FileWriter(outputPath)) {
            // CSV file header
            writer.write("Filename,Author,Additions,Deletions,Changes,CommitSHA,CommitDate\n");

            // Go through all files
            for (Map.Entry<String, List<Map<String, Object>>> fileEntry : data.entrySet()) {
                String filename = fileEntry.getKey();

                // Go through all records for the given file
                for (Map<String, Object> contributorEntry : fileEntry.getValue()) {
                    String author = contributorEntry.keySet().iterator().next();
                    List<Map<String, Object>> statsList = (List<Map<String, Object>>) contributorEntry.get(author);

                    // Go through the statistics for this author
                    for (Map<String, Object> stats : statsList) {
                        writer.write(filename + ",");
                        writer.write(author + ",");
                        writer.write(stats.get("additions") + ",");
                        writer.write(stats.get("deletions") + ",");
                        writer.write(stats.get("changes") + ",");
                        writer.write(stats.get("commitSHA") + ",");
                        writer.write(stats.get("commitDate") + "\n");
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
