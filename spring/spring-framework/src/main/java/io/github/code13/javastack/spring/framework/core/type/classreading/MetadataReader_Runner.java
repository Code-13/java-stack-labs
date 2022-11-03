/*
 * Copyright 2022-present the original author or authors.
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

package io.github.code13.javastack.spring.framework.core.type.classreading;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * MetadataReader_Runner.
 *
 * @see org.springframework.core.type.classreading.MetadataReader
 * @see org.springframework.core.type.classreading.SimpleMetadataReader
 * @see org.springframework.core.type.classreading.MetadataReaderFactory
 * @see org.springframework.core.type.classreading.SimpleMetadataReaderFactory
 * @see org.springframework.core.type.classreading.CachingMetadataReaderFactory
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/3 21:32
 */
public class MetadataReader_Runner {

  /*
   * 上述所有实现，都是委托对应元素直接基于 反射 实现的，因此前提是对应的 Class 必须加载到 JVM 中，
   * 而实际的应用场景，并不一定保证对应的 Class 已加载，比如 Spring 的第三方类扫描
   * 因此，MetadataReader 接口抽象元数据的读取，其实现基于 ASM 直接扫描对应文件字节码实现，
   * Spring 提供了唯一实现 SimpleMetadataReader
   */

  /*
   * 对于 MetadataReader，Spring 也提供了对应的 工厂类 去获取，顶层接口 MetadataReaderFactory
   * SimpleMetadataReaderFactory 返回对应的 SimpleMetadataReader
   * CachingMetadataReaderFactory 基于 SimpleMetadataReaderFactory
   * 做了缓存，功能更强大 借助它们，我们就可以获取对应的 MetadataReader，同样也可以获取对应的元数据
   */

  @Service
  @Configuration
  public static class Config {

    @RequestMapping
    public void a() {}
  }

  @Test
  @DisplayName("metadataReader")
  void metadataReader() throws IOException {
    String component = "org.springframework.stereotype.Component";
    String configuration = "org.springframework.context.annotation.Configuration";
    String requestMapping = "org.springframework.web.bind.annotation.RequestMapping";

    // 基于反射获取
    AnnotationMetadata annotationMetadata = AnnotationMetadata.introspect(Config.class);

    // 基于 MetadataReader 获取
    CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
    MetadataReader metadataReader =
        factory.getMetadataReader(
            "io.github.code13.javastack.spring.framework.core.type.classreading"
                + ".MetadataReader_Runner.Config");
    AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();

    assertTrue(metadata.hasAnnotation(configuration));
    assertTrue(metadata.hasMetaAnnotation(component));
    assertTrue(metadata.hasAnnotatedMethods(requestMapping));
  }
}
