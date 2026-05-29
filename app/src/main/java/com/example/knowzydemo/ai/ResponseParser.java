package com.example.knowzydemo.ai;

import java.util.LinkedHashMap;

public class ResponseParser {

    public static LinkedHashMap<String, String> parseJudiciaryResponse(String text) {

        // LinkedHashMap preserves insertion order for display
        LinkedHashMap<String, String> data = new LinkedHashMap<>();

        String[] sections = {
                "Case Overview",
                "Parties Involved",
                "Victim",
                "Accused",
                "Judge",
                "Witness",
                "Incident Summary",
                "Legal Sections",
                "Evidence & Arguments",
                "Judgment & Punishment",
                "Conclusion",
                "Suggestions",
                "Case Status",
                "Risk Level",
                "Confidence Level"
        };

        for (int i = 0; i < sections.length; i++) {

            String current = sections[i];
            // Match "SectionName:" pattern to avoid partial matches
            String searchKey = current + ":";
            int start = text.indexOf(searchKey);

            if (start != -1) {

                int valueStart = start + searchKey.length();
                int end = text.length();

                // Find where next section begins
                for (int j = i + 1; j < sections.length; j++) {
                    int nextIndex = text.indexOf(sections[j] + ":", valueStart);
                    if (nextIndex != -1 && nextIndex < end) {
                        end = nextIndex;
                    }
                }

                String value = text.substring(valueStart, end).trim();
                if (value.isEmpty()) {
                    value = "Not Available";
                }
                data.put(current, value);
            } else {
                data.put(current, "Not Available");
            }
        }

        return data;
    }
}