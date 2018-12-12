package id.ac.uii.a16523169students.kumis;


import java.util.Date;

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private String messageSender;
    private String messageHistory;
    private long messageTime;

    public ChatMessage(String messageText, String messageUser, String messageSender, String messageHistory) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageSender = messageSender;
        this.messageHistory = messageHistory;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageSender() { return messageSender; }

    public void setMessageSender(String messageSender) { this.messageSender = messageSender; }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageHistory() {
        return messageHistory;
    }

    public void setMessageHistory(String messageHistory) {
        this.messageHistory = messageHistory;
    }
}
