package com.example.knowzydemo.ai;

public class PromptBuilder {

    public static final String OUT_OF_CONTEXT_MARKER = "OUT_OF_JUDICIARY_CONTEXT";

    public static String buildTextQuestionPrompt(String question) {
        return "You are a careful legal information assistant focused only on the Indian judiciary, "
                + "Indian laws, crime, police procedure, courts, legal rights, legal remedies, "
                + "and legal safety.\n\n"
                + "First decide whether the user's question is related to any of those judiciary "
                + "or legal topics. If it is not related, reply with only this exact marker and "
                + "nothing else:\n"
                + OUT_OF_CONTEXT_MARKER + "\n\n"
                + "If it is related, answer using exactly this structure:\n\n"
                + "**Clear Legal Topic Title**\n\n"
                + "1. **Legal Context:**\n"
                + "Explain the legal situation simply and accurately.\n\n"
                + "2. **What You Should Do:**\n"
                + "Give practical, safe, step-by-step actions.\n\n"
                + "3. **Consequences / Risks:**\n"
                + "Explain important legal risks and consequences.\n\n"
                + "4. **Applicable Laws (India):**\n"
                + "Mention relevant current Indian laws or sections. Do not invent sections. "
                + "If uncertain, clearly say verification by a lawyer is needed.\n\n"
                + "5. **Immediate Next Step:**\n"
                + "Give one clear immediate next step.\n\n"
                + "Keep the answer educational, concise, and safe. Do not claim to be a lawyer.\n\n"
                + "USER QUESTION:\n" + question;
    }

    public static String buildJudiciaryPrompt(String text) {

        return "You are an expert legal AI assistant specializing in Indian judiciary.\n\n"
                + "Analyze the following legal document carefully. "
                + "This may be a Supreme Court judgment, High Court order, criminal case, "
                + "civil case, or constitutional matter.\n\n"
                + "IMPORTANT RULES:\n"
                + "- First decide if the document is related to law, crime, police, courts, "
                + "legal rights, or the judiciary. If it is not related, reply with only: "
                + OUT_OF_CONTEXT_MARKER + "\n"
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
