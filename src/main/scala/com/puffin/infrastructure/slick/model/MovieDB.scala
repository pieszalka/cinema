package com.puffin.infrastructure.slick.model

import java.sql.Timestamp

case class MovieDB(
  id: String,
  name: String,
  description: String,
  category: String,
  createDate: Timestamp
)
