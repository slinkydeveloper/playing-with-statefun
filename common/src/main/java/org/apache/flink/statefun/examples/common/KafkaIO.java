package org.apache.flink.statefun.examples.common;

import org.apache.flink.statefun.sdk.io.EgressIdentifier;
import org.apache.flink.statefun.sdk.io.EgressSpec;
import org.apache.flink.statefun.sdk.io.IngressIdentifier;
import org.apache.flink.statefun.sdk.io.IngressSpec;
import org.apache.flink.statefun.sdk.kafka.KafkaEgressBuilder;
import org.apache.flink.statefun.sdk.kafka.KafkaEgressSerializer;
import org.apache.flink.statefun.sdk.kafka.KafkaIngressBuilder;
import org.apache.flink.statefun.sdk.kafka.KafkaIngressDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class KafkaIO {

    private final String kafkaAddress;
    private final String inputTopic;
    private final String outputTopic;

    public KafkaIO(String kafkaAddress, String inputTopic, String outputTopic) {
        this.kafkaAddress = Objects.requireNonNull(kafkaAddress);
        this.inputTopic = inputTopic;
        this.outputTopic = outputTopic;
    }

    public static IngressSpec<String> getIngressSpec(IngressIdentifier<String> identifier, String kafkaAddress, String inputTopic) {
        return KafkaIngressBuilder
            .forIdentifier(identifier)
            .withKafkaAddress(kafkaAddress)
            .withTopic(inputTopic)
            .withDeserializer(InboundDeserializer.class)
            .withProperty(ConsumerConfig.GROUP_ID_CONFIG, "inbound")
            .build();
    }

    public static EgressSpec<String> getEgressSpec(EgressIdentifier<String> identifier, String kafkaAddress) {
        return KafkaEgressBuilder.forIdentifier(identifier)
            .withKafkaAddress(kafkaAddress)
            .withSerializer(OutboundSerializer.class)
            .build();
    }

    private static final class InboundDeserializer implements KafkaIngressDeserializer<String> {

        private static final long serialVersionUID = 1L;

        @Override
        public String deserialize(ConsumerRecord<byte[], byte[]> input) {
            return new String(input.value(), StandardCharsets.UTF_8);
        }
    }

    private static final class OutboundSerializer implements KafkaEgressSerializer<String> {

        private static final long serialVersionUID = 1L;

        @Override
        public ProducerRecord<byte[], byte[]> serialize(String response) {
            return new ProducerRecord<>("outbound", response.getBytes());
        }
    }

}
