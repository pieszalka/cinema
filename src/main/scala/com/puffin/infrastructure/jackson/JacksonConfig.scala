package com.puffin.infrastructure.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.http.converter.json.{Jackson2ObjectMapperBuilder, MappingJackson2HttpMessageConverter}

@Configuration
class JacksonConfig {

  @Bean
  def mappingJackson2HttpMessageConverter(obj: ObjectMapper): MappingJackson2HttpMessageConverter = {
    new MappingJackson2HttpMessageConverter(obj)
  }

  @Bean
  def scalaModule: DefaultScalaModule = DefaultScalaModule

  @Bean
  def jacksonBuilderCustomizer(): Jackson2ObjectMapperBuilderCustomizer = {
    new Jackson2ObjectMapperBuilderCustomizer() {
      override def customize(jacksonObjectMapperBuilder: Jackson2ObjectMapperBuilder): Unit = {
        jacksonObjectMapperBuilder
          .filters(new SimpleFilterProvider().setFailOnUnknownId(false))
      }
    }
  }
}
