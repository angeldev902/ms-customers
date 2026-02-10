package com.ordersystem.customers.infrastructure.kafka;

import com.ordersystem.customers.domain.event.CustomerEvent;
import com.ordersystem.customers.domain.event.CustomerEventType;
import com.ordersystem.customers.domain.publisher.CustomerEventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaCustomerEventPublisher implements CustomerEventPublisher {

    private static final String TOPIC = "customers.events";

    private final KafkaTemplate<String, CustomerEvent> kafkaTemplate;

    @Override
    public void publishCustomerCreated(Long customerId) {
        publish(
                new CustomerEvent(
                        CustomerEventType.CUSTOMER_CREATED,
                        customerId
                )
        );
    }

    @Override
    public void publishCustomerUpdated(Long customerId) {
        publish(
                new CustomerEvent(
                        CustomerEventType.CUSTOMER_UPDATED,
                        customerId
                )
        );
    }

    @Override
    public void publishCustomerDeleted(Long customerId) {
        publish(
                new CustomerEvent(
                        CustomerEventType.CUSTOMER_DELETED,
                        customerId
                )
        );
    }

    private void publish(CustomerEvent event) {
        log.info(
                "Publishing event type={} customerId={}",
                event.getEventType(),
                event.getCustomerId()
        );

        kafkaTemplate.send(
                TOPIC,
                event.getCustomerId().toString(), // key
                event
        );
    }
}
