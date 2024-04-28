package com.example.TatMobileAnalyzer.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MapToCsvConverter {

    public static void mapToCsv(Map<String, List<Map<String, Object>>> data, String outputPath) {
        try (FileWriter writer = new FileWriter(outputPath)) {
            // Заголовок CSV файла
            writer.write("Filename,Author,Additions,Deletions,Changes,CommitSHA,CommitDate\n");

            // Проходим по всем файлам
            for (Map.Entry<String, List<Map<String, Object>>> fileEntry : data.entrySet()) {
                String filename = fileEntry.getKey();

                // Проходим по всем записям для данного файла
                for (Map<String, Object> contributorEntry : fileEntry.getValue()) {
                    String author = contributorEntry.keySet().iterator().next();
                    List<Map<String, Object>> statsList = (List<Map<String, Object>>) contributorEntry.get(author);

                    // Проходим по статистике для данного автора
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
            e.printStackTrace();
        }
    }
}
