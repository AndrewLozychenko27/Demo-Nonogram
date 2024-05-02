package ua.lozychenko.nonogram.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ua.lozychenko.nonogram.service.messenger.dto.CellsDto;
import ua.lozychenko.nonogram.service.messenger.dto.PuzzleGenerateRequestDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @Value("${kafka.bootstrapServer}")
    private String BOOTSTRAP_SERVER;

    @Value("${kafka.replyTopic}")
    private String REPLY_TOPIC;

    public static final String GROUP_ID = "nonogram-group";

    @Bean
    public ConsumerFactory<String, CellsDto> consumerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.TYPE_MAPPINGS, "ua.lozychenko.generatorservice.dto.CellsDto:ua.lozychenko.nonogram.service.messenger.dto.CellsDto");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(CellsDto.class));
    }

    @Bean
    public KafkaListenerContainerFactory<?> listenerContainerFactory(ConsumerFactory<String, CellsDto> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, CellsDto> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        return factory;
    }

    @Bean
    public KafkaMessageListenerContainer<String, CellsDto> replyListenerContainer(ConsumerFactory<String, CellsDto> consumerFactory) {
        ContainerProperties containerProperties = new ContainerProperties(REPLY_TOPIC);

        return new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
    }

    @Bean
    public ProducerFactory<String, PuzzleGenerateRequestDto> producerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, PuzzleGenerateRequestDto> kafkaTemplate(ProducerFactory<String, PuzzleGenerateRequestDto> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ReplyingKafkaTemplate<String, PuzzleGenerateRequestDto, CellsDto> replyingKafkaTemplate(ProducerFactory<String, PuzzleGenerateRequestDto> producerFactory, KafkaMessageListenerContainer<String, CellsDto> listenerContainer) {
        return new ReplyingKafkaTemplate<>(producerFactory, listenerContainer);
    }
}