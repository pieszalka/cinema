package com.puffin.infrastructure.jackson

import java.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMcvConfigurer extends WebMvcConfigurer {

  @Autowired
  private val objectMapper: ObjectMapper = null

  override def extendMessageConverters(converters: util.List[HttpMessageConverter[_]]): Unit = {
    super.extendMessageConverters(converters)

    import scala.collection.JavaConverters._
    converters.asScala.foreach {
      case converter: MappingJackson2HttpMessageConverter =>
        converter
          .asInstanceOf[MappingJackson2HttpMessageConverter]
          .setObjectMapper(objectMapper)
      case _ =>
    }
  }

  @Bean
  def methodValidationPostProcessor(): MethodValidationPostProcessor = {
    new MethodValidationPostProcessor()
  }
}
