package com.puffin.infrastructure.slick

import javax.sql.DataSource

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import slick.jdbc.JdbcProfile

@Component
class DBProvider(
  val jdbcProfile: JdbcProfile,
  dataSource: DataSource,
  @Value("${slick.async-executor.name:slickExecutor}") val executorName: String,
  @Value("${spring.datasource.hikari.maximum-pool-size}") maxConnections: Int
) {

  import jdbcProfile.api._

  private val db: jdbcProfile.backend.DatabaseDef = Database.forDataSource(
    dataSource,
    Option(maxConnections),
    executor = AsyncExecutor(
      executorName,
      minThreads = Math.min(maxConnections, 20),
      maxThreads = Math.min(maxConnections, 20),
      queueSize = 1000,
      maxConnections = maxConnections,
      registerMbeans = true
    )
  )

  def apply(): jdbcProfile.backend.DatabaseDef = db
}
