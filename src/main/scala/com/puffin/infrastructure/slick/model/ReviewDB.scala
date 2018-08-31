package com.puffin.infrastructure.slick.model

import java.sql.Timestamp

case class ReviewDB(
  id: String,
  movieId: String,
  rate: Int,
  description: String,
  createDate: Timestamp
)
