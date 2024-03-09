package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private Map<Long, Message> msgStorage = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        var newMsg = msg.toBuilder().build();
        msgStorage.put(newMsg.getId(), newMsg);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(msgStorage.get(id));
    }
}
