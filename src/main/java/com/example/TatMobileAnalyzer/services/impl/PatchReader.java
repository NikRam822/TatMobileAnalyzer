package com.example.TatMobileAnalyzer.services.impl;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class PatchReader {

    @Getter
    public static class PatchInfo {
        private final List<String> add;
        private final List<String> del;

        public PatchInfo() {
            this.add = new ArrayList<>();
            this.del = new ArrayList<>();
        }

    }

    public static PatchInfo readPatch(String patch) {
        PatchInfo patchInfo = new PatchInfo();
        if (patch == null) {
            return patchInfo;
        }
        String[] lines = patch.split("\n");
        int currentIndex = 0;
        int addIndexOffset = 0;
        int delIndexOffset = 0;

        for (String line : lines) {
            if (line.startsWith("@@")) {
                String[] parts = line.split(" ");
                String[] numbers = parts[2].split(",");
                currentIndex = Integer.parseInt(numbers[0].substring(1));
                addIndexOffset = 0;
                delIndexOffset = 0;
            } else if (line.startsWith("+")) {

                patchInfo.getAdd().add((currentIndex + addIndexOffset) + ". " + line.substring(1));
                addIndexOffset++;
            } else if (line.startsWith("-")) {
                patchInfo.getDel().add((currentIndex + delIndexOffset) + ". " + line.substring(1));
                delIndexOffset++;
            } else {
                currentIndex++;
            }
        }

        return patchInfo;
    }

}