package com.example.knowzydemo.ai;

public class PromptBuilder {

    public static String buildJudiciaryPrompt(String text) {

        return "You are an expert legal AI assistant specializing in Indian judiciary.\n\n"
                + "Analyze the following legal document carefully. "
                + "This may be a Supreme Court judgment, High Court order, criminal case, "
                + "civil case, or constitutional matter.\n\n"
                + "IMPORTANT RULES:\n"
                + "- Follow the exact format below\n"
                + "- Extract as much detail as possible from the text\n"
                + "- For fields not applicable to this case type, write a brief explanation why\n"
                + "- For Suggestions, always provide 2-3 practical legal suggestions based on the judgment\n"
                + "- For Risk Level: Low / Medium / High based on legal implications\n"
                + "- For Confidence Level: percentage of how confident you are in the analysis\n"
                + "- Clean OCR errors if present\n"
                + "- Do NOT leave any field completely empty\n\n"
                + "FORMAT:\n\n"
                + "Case Overview:\n"
                + "Parties Involved:\n"
                + "Petitioner/Appellant:\n"
                + "Respondent:\n"
                + "Judge(s):\n"
                + "Witness:\n\n"
                + "Incident Summary:\n"
                + "Legal Sections:\n"
                + "Evidence & Arguments:\n"
                + "Judgment & Punishment:\n"
                + "Conclusion:\n"
                + "Suggestions:\n"
                + "Case Status:\n"
                + "Risk Level:\n"
                + "Confidence Level:\n\n"
                + "DOCUMENT:\n"
                + text;
    }
}