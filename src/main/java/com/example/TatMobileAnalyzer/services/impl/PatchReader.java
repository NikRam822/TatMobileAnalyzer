package com.example.TatMobileAnalyzer.services.impl;

import java.util.ArrayList;
import java.util.List;

public class PatchReader {

    public static class PatchInfo {
        private List<String> add;
        private List<String> del;

        public PatchInfo() {
            this.add = new ArrayList<>();
            this.del = new ArrayList<>();
        }

        public List<String> getAdd() {
            return add;
        }

        public List<String> getDel() {
            return del;
        }

    }

    public static PatchInfo readPatch(String patch) {
        PatchInfo patchInfo = new PatchInfo();

        String[] lines = patch.split("\n");
        int currentIndex = 0;
        int addIndexOffset = 0;
        int delIndexOffset = 0;

        for (String line : lines) {
            boolean inAddition = false;
            if (line.startsWith("@@")) {
                inAddition = false;
                String[] parts = line.split(" ");
                String[] numbers = parts[1].split(",");
                currentIndex = Integer.parseInt(numbers[0].substring(1));
                addIndexOffset = 0;
                delIndexOffset = 0;
            } else if (line.startsWith("+")) {
                inAddition = true;
                patchInfo.getAdd().add((currentIndex + addIndexOffset) + ". " + line.substring(1));
                addIndexOffset++;
            } else if (line.startsWith("-")) {
                if (inAddition) {
                    continue;
                }
                patchInfo.getDel().add((currentIndex + delIndexOffset) + ". " + line.substring(1));
                delIndexOffset++;
            } else {
                currentIndex++;
            }
        }

        return patchInfo;
    }

}