package com.fraud.transaction.config;

import com.fraud.common.constants.AppConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Message queue configuration placeholder.
 * - Configure Kafka/Rabbit producers and consumers; define serializers/deserializers.
 * - Externalize broker addresses and credentials.
 */
@Configuration
public class MessageQueueConfig {
    // TODO: Add KafkaTemplate / ConsumerFactory beans and topic configuration.
    /**
     *
     * @return
     */
    @Bean
    public NewTopic createTransactionTopic() {
        return TopicBuilder
                .name(AppConstants.CREATE_TRANSACTION_TOPIC).build();
    }
}
