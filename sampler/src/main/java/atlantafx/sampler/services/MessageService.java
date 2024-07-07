package atlantafx.sampler.services;

import atlantafx.sampler.entities.Messages;

import java.util.List;

public interface MessageService {


    List<Messages> getAllMessages();

    List<Messages> searchMessagesByText(String text);

    Messages getMessageById(int eventId);
    void addMessage(Messages messages);
    void updateMessage(Messages messages);
    void deleteMessage(int messageId);
}
