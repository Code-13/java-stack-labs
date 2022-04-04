/*
 *
 * Copyright $today.year-present the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.code13.javastack.spring.kafka;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasKey;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasPartition;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasValue;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;

/**
 * SpringKafkaTestApp.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/4/4 15:09
 */
@EmbeddedKafka(
    count = 4,
    ports = {9092, 9093, 9094, 9095})
class SpringKafkaTestApp {

  private static final String TEMPLATE_TOPIC = "templateTopic";

  @Test
  void testEmbeddedKafkaBroker(EmbeddedKafkaBroker broker) {
    String brokerList = broker.getBrokersAsString();
    System.out.println(brokerList);
  }

  @Test
  @DisplayName("testTemplate")
  void testTemplate(EmbeddedKafkaBroker embeddedKafka) throws InterruptedException {
    Map<String, Object> consumerProps =
        KafkaTestUtils.consumerProps("testT", "false", embeddedKafka);
    DefaultKafkaConsumerFactory<Integer, String> cf =
        new DefaultKafkaConsumerFactory<Integer, String>(consumerProps);
    ContainerProperties containerProperties = new ContainerProperties(TEMPLATE_TOPIC);
    KafkaMessageListenerContainer<Integer, String> container =
        new KafkaMessageListenerContainer<>(cf, containerProperties);

    BlockingQueue<ConsumerRecord<Integer, String>> records = new LinkedBlockingQueue<>();

    container.setupMessageListener(
        (MessageListener<Integer, String>)
            record -> {
              System.out.println(record);
              records.add(record);
            });

    container.setBeanName("templateTests");
    container.start();
    ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());

    // --------

    Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafka);
    ProducerFactory<Integer, String> pf =
        new DefaultKafkaProducerFactory<Integer, String>(producerProps);

    KafkaTemplate<Integer, String> template = new KafkaTemplate<>(pf);
    template.setDefaultTopic(TEMPLATE_TOPIC);
    template.sendDefault("foo");

    assertThat(records.poll(10, TimeUnit.SECONDS), hasValue("foo"));

    template.sendDefault(0, 2, "bar");
    ConsumerRecord<Integer, String> received = records.poll(10, TimeUnit.SECONDS);
    assertThat(received, hasKey(2));
    assertThat(received, hasPartition(0));
    assertThat(received, hasValue("bar"));

    template.send(TEMPLATE_TOPIC, 0, 2, "baz");
    received = records.poll(10, TimeUnit.SECONDS);
    assertThat(received, hasKey(2));
    assertThat(received, hasPartition(0));
    assertThat(received, hasValue("baz"));
  }
}
