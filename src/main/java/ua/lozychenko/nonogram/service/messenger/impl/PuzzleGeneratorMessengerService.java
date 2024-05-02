package ua.lozychenko.nonogram.service.messenger.impl;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.service.messenger.MessengerService;
import ua.lozychenko.nonogram.service.messenger.dto.CellsDto;
import ua.lozychenko.nonogram.service.messenger.dto.PuzzleGenerateRequestDto;

import java.util.List;

@Service
public class PuzzleGeneratorMessengerService implements MessengerService<String, PuzzleGenerateRequestDto, CellsDto> {
    @Value("${kafka.requestTopic}")
    private String requestTopic;
    private final ReplyingKafkaTemplate<String, PuzzleGenerateRequestDto, CellsDto> kafkaTemplate;

    public PuzzleGeneratorMessengerService(ReplyingKafkaTemplate<String, PuzzleGenerateRequestDto, CellsDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public RequestReplyFuture<String, PuzzleGenerateRequestDto, CellsDto> send(PuzzleGenerateRequestDto puzzleGenerateRequestDto, List<Header> headers) {
        ProducerRecord<String, PuzzleGenerateRequestDto> record = new ProducerRecord<>(requestTopic, null, (String) null, puzzleGenerateRequestDto, headers);

        return kafkaTemplate.sendAndReceive(record);
    }
}