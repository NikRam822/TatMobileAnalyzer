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

    public static PatchInfo readPatch(String patchOrDiff) {
        PatchInfo patchInfo = new PatchInfo();
        if (patchOrDiff == null || patchOrDiff.isEmpty()) {
            return patchInfo;
        }

        String[] lines = patchOrDiff.split("\n");
        int currentAddIndex = 0;
        int currentDelIndex = 0;

        for (String line : lines) {
            if (line.startsWith("@@")) {
                String[] parts = line.split(" ");
                String[] addNumbers = parts[2].split(",");
                String[] delNumbers = parts[1].split(",");
                currentAddIndex = Integer.parseInt(addNumbers[0].substring(1));
                currentDelIndex = Math.abs(Integer.parseInt(delNumbers[0].substring(1)));
            } else if (line.startsWith("+")) {
                patchInfo.getAdd().add(currentAddIndex + ". " + line.substring(1));
                currentAddIndex++;
            } else if (line.startsWith("-")) {
                patchInfo.getDel().add(currentDelIndex + ". " + line.substring(1));
                currentDelIndex++;
            } else if (!line.startsWith("\\")) {
                currentAddIndex++;
                currentDelIndex++;
            }
        }

        return patchInfo;
    }
}
