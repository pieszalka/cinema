package com.puffin.domain.review

import java.time.Instant
import java.util.UUID

import com.puffin.api.v1.ReviewDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ReviewsService @Autowired()(
  reviewsRepository: ReviewsRepository
) {
  def getReviews(movieId: String): List[ReviewDto] = {
    reviewsRepository.getByMovieId(movieId).map(toReviewDto)
  }

  def getReviews(movieIds: List[String]): List[ReviewDto] = {
    reviewsRepository.getByMovieIds(movieIds).map(toReviewDto)
  }

  def getReviewsOrdered(limit: Int): List[ReviewDto] = {
    reviewsRepository.getOrderedByCreateDate(limit).map(toReviewDto)
  }

  def calculateAverageRating(reviews: List[ReviewDto]): Int = {
    if (reviews.isEmpty) {
      0
    } else {
      Math.round(reviews.map(_.rate).sum.toFloat / reviews.size.toFloat)
    }
  }

  def calculatePositiveRatings(reviews: List[ReviewDto]): Int = {
    reviews.count(review => review.rate >= 3)
  }

  def calculateNegativeRatings(reviews: List[ReviewDto]): Int = {
    reviews.count(review => review.rate <= 2)
  }

  def create(review: ReviewDto): Unit = {
    reviewsRepository.create(
      Review(UUID.randomUUID().toString, review.movieId, review.rate, review.description, Instant.now())
    )
  }

  private def toReviewDto(review: Review): ReviewDto = {
    ReviewDto(review.id, review.movieId, review.rate, review.description)
  }
}
