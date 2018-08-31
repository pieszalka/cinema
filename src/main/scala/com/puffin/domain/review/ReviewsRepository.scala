package com.puffin.domain.review

abstract class ReviewsRepository {
  def getByMovieId(movieId: String): List[Review]

  def getByMovieIds(movieIds: List[String]): List[Review]

  def getOrderedByCreateDate(limit: Int): List[Review]

  def create(review: Review): Boolean
}
