/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.beam.sdk.io.gcp.pubsub;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.beam.sdk.coders.Coder;
import org.apache.beam.sdk.testing.CoderProperties;
import org.apache.beam.sdk.util.SerializableUtils;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link org.apache.beam.sdk.io.gcp.pubsub.PubsubMessageCoder}. */
@RunWith(JUnit4.class)
public class PubsubMessageCoderTest {
  private static final String DATA = "testData";
  private static final String MESSAGE_ID = "testMessageId";
  private static final Map<String, String> ATTRIBUTES =
      new ImmutableMap.Builder<String, String>().put("1", "hello").build();
  private static final Coder<PubsubMessage> TEST_CODER = PubsubMessageCoder.of();
  private static final PubsubMessage TEST_PAYLOAD_ONLY_VALUE =
      new PubsubMessage(DATA.getBytes(StandardCharsets.UTF_8), null);
  private static final PubsubMessage TEST_PAYLOAD_ATTRIBUTES_VALUE =
      new PubsubMessage(DATA.getBytes(StandardCharsets.UTF_8), ATTRIBUTES);
  private static final PubsubMessage TEST_PAYLOAD_MESSAGEID_VALUE =
      new PubsubMessage(DATA.getBytes(StandardCharsets.UTF_8), null, MESSAGE_ID);
  private static final PubsubMessage TEST_PAYLOAD_ATTRIBUTES_MESSAGEID_VALUE =
      new PubsubMessage(DATA.getBytes(StandardCharsets.UTF_8), ATTRIBUTES, MESSAGE_ID);

  @Test
  public void testValueEncodable() {
    SerializableUtils.ensureSerializableByCoder(TEST_CODER, TEST_PAYLOAD_ONLY_VALUE, "error");
    SerializableUtils.ensureSerializableByCoder(TEST_CODER, TEST_PAYLOAD_ATTRIBUTES_VALUE, "error");
    SerializableUtils.ensureSerializableByCoder(TEST_CODER, TEST_PAYLOAD_MESSAGEID_VALUE, "error");
    SerializableUtils.ensureSerializableByCoder(
        TEST_CODER, TEST_PAYLOAD_ATTRIBUTES_MESSAGEID_VALUE, "error");
  }

  @Test
  public void testCoderDecodeEncodeEqual() throws Exception {
    CoderProperties.structuralValueDecodeEncodeEqual(TEST_CODER, TEST_PAYLOAD_ONLY_VALUE);
    CoderProperties.structuralValueDecodeEncodeEqual(TEST_CODER, TEST_PAYLOAD_ATTRIBUTES_VALUE);
    CoderProperties.structuralValueDecodeEncodeEqual(TEST_CODER, TEST_PAYLOAD_MESSAGEID_VALUE);
    CoderProperties.structuralValueDecodeEncodeEqual(
        TEST_CODER, TEST_PAYLOAD_ATTRIBUTES_MESSAGEID_VALUE);
  }

  @Test
  public void testEncodedTypeDescriptor() {
    TypeDescriptor<PubsubMessage> typeDescriptor = new TypeDescriptor<PubsubMessage>() {};
    assertThat(TEST_CODER.getEncodedTypeDescriptor(), equalTo(typeDescriptor));
  }
}
