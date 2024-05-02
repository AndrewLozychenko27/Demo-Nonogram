package ua.lozychenko.generatorservice.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ua.lozychenko.generatorservice.dto.CellsDto;
import ua.lozychenko.generatorservice.dto.PuzzleGenerateRequestDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    private static final String BOOSTRAP_SERVER = "localhost:9092";

    private static final String GROUP_ID = "puzzle-generator-group";

    @Bean
    public ConsumerFactory<String, PuzzleGenerateRequestDto> consumerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOSTRAP_SERVER);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.TYPE_MAPPINGS, "ua.lozychenko.nonogram.service.messenger.dto.PuzzleGenerateRequestDto:ua.lozychenko.generatorservice.dto.PuzzleGenerateRequestDto");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(PuzzleGenerateRequestDto.class));
    }

    @Bean
    public KafkaListenerContainerFactory<?> listenerContainerFactory(ConsumerFactory<String, PuzzleGenerateRequestDto> consumerFactory, KafkaTemplate<String, CellsDto> kafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<String, PuzzleGenerateRequestDto> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        factory.setReplyTemplate(kafkaTemplate);

        return factory;
    }

    @Bean
    public ProducerFactory<String, CellsDto> producerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOSTRAP_SERVER);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, CellsDto> kafkaTemplate(ProducerFactory<String, CellsDto> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}