package ua.lozychenko.nonogram.service.messenger;

import org.apache.kafka.common.header.Header;
import org.springframework.kafka.requestreply.RequestReplyFuture;

import java.util.List;

public interface MessengerService<Key, Request, Reply> {
    RequestReplyFuture<Key, Request, Reply> send(Request request, List<Header> headers);
}