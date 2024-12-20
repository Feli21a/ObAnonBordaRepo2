package ObligatorioDDA_IS.APIs;

public class ChatGPTRequest {
    private String model;
    private String prompt;
    private int max_tokens;
    private double temperature;

    public ChatGPTRequest(String model, String prompt, int max_tokens, double temperature) {
        this.model = model;
        this.prompt = prompt;
        this.max_tokens = max_tokens;
        this.temperature = temperature;
    }

    // Getters y Setters
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public int getMaxTokens() {
        return max_tokens;
    }

    public void setMaxTokens(int max_tokens) {
        this.max_tokens = max_tokens;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
