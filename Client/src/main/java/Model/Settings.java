package Model;

public class Settings {

    String customMessage;
    int iterations, messagesCount;

    public Settings(int iterations, int messagesCount, String customMessage) {
        this.customMessage = customMessage;
        this.iterations = iterations;
        this.messagesCount = messagesCount;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getMessagesCount() {
        return messagesCount;
    }

    public void setMessagesCount(int messagesCount) {
        this.messagesCount = messagesCount;
    }

    @Override
    public String toString() {
        return "--- Settings ---" + "\n" + "Iterations: " + iterations
                + "\n" + "MessageCount: " + messagesCount + "\n" +
                "Custom Message: " + customMessage + "\n----------------";
    }
}
