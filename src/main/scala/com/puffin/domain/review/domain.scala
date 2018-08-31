package com.puffin.domain.review

import java.time.Instant

import com.puffin.domain.review.domain.ReviewId

package object domain {
  type ReviewId = String
}
case class Review(id: ReviewId, movieId: String, rate: Int, description: String, createDate: Instant)