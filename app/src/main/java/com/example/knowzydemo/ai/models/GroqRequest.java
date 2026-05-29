package com.example.knowzydemo.ai.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GroqRequest {

    @SerializedName("model")
    private String model;

    @SerializedName("messages")
    private List<Message> messages;

    @SerializedName("temperature")
    private double temperature = 0.7;

    @SerializedName("max_tokens")
    private int max_tokens = 1024;

    public GroqRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    public String getModel() { return model; }
    public List<Message> getMessages() { return messages; }
    public double getTemperature() { return temperature; }
    public int getMax_tokens() { return max_tokens; }

    public static class Message {

        @SerializedName("role")
        private String role;

        @SerializedName("content")
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() { return role; }
        public String getContent() { return content; }
    }
}