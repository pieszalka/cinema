package com.puffin.infrastructure.slick

import org.springframework.context.annotation.{Bean, Configuration}
import slick.jdbc.JdbcProfile

@Configuration
class SlickConfiguration {

  @Bean
  def jdbcProfile(): JdbcProfile = {
    _root_.slick.jdbc.MySQLProfile
  }

}
