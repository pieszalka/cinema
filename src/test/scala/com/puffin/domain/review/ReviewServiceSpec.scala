package com.puffin.domain.review

import java.time.Instant

import com.puffin.api.v1.ReviewDto
import org.mockito.Mockito.when
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

class ReviewServiceSpec extends FlatSpec with Matchers with MockitoSugar with ScalaFutures {

  it should "get reviews" in {
    // given
    val movieId = "1"
    val review = Review("1", movieId, 1, "review-description", Instant.now)
    val reviewsRepository = mock[ReviewsRepository]
    when(reviewsRepository.getByMovieId(movieId)).thenReturn(List(review))
    val service = new ReviewsService(reviewsRepository)

    //when
    val movieResult = service.getReviews(movieId)

    //then
    movieResult should be (List(ReviewDto("1", movieId, 1, "review-description")))
  }

  it should "calculate average rating" in {
    // given
    val review1 = ReviewDto("review-1", "movie-1", 1, "review-description")
    val review2 = ReviewDto("review-2", "movie-1", 2, "review-description")
    val review3 = ReviewDto("review-3", "movie-1", 5, "review-description")
    val review4 = ReviewDto("review-4", "movie-1", 2, "review-description")
    val reviewsRepository = mock[ReviewsRepository]
    val service = new ReviewsService(reviewsRepository)

    //when
    val rating = service.calculateAverageRating(List(review1, review2, review3, review4))

    //then
    rating should be (3)
  }

  it should "calculate average rating for no reviews" in {
    // given
    val reviewsRepository = mock[ReviewsRepository]
    val service = new ReviewsService(reviewsRepository)

    //when
    val rating = service.calculateAverageRating(List())

    //then
    rating should be (0)
  }

  it should "calculate positive ratings" in {
    // given
    val review1 = ReviewDto("review-1", "movie-1", 1, "review-description")
    val review2 = ReviewDto("review-2", "movie-1", 2, "review-description")
    val review3 = ReviewDto("review-3", "movie-1", 5, "review-description")
    val review4 = ReviewDto("review-4", "movie-1", 2, "review-description")
    val reviewsRepository = mock[ReviewsRepository]
    val service = new ReviewsService(reviewsRepository)

    //when
    val rating = service.calculatePositiveRatings(List(review1, review2, review3, review4))

    //then
    rating should be (1)
  }

  it should "calculate negative ratings" in {
    // given
    val review1 = ReviewDto("review-1", "movie-1", 1, "review-description")
    val review2 = ReviewDto("review-2", "movie-1", 2, "review-description")
    val review3 = ReviewDto("review-3", "movie-1", 5, "review-description")
    val review4 = ReviewDto("review-4", "movie-1", 2, "review-description")
    val reviewsRepository = mock[ReviewsRepository]
    val service = new ReviewsService(reviewsRepository)

    //when
    val rating = service.calculateNegativeRatings(List(review1, review2, review3, review4))

    //then
    rating should be (3)
  }
}