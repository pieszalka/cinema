package com.puffin.infrastructure.slick

import java.sql.Timestamp

import com.puffin.domain.review.{Review, ReviewsRepository}
import com.puffin.infrastructure.slick.model.ReviewDB
import com.typesafe.scalalogging.LazyLogging
import org.springframework.stereotype.Component
import slick.jdbc.JdbcProfile

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

@Component
class SlickReviewsRepository(
  protected val dBProvider: DBProvider
) extends ReviewsRepository with SlickReviewsRepositoryDDL with LazyLogging {

  override protected val profile: JdbcProfile = dBProvider.jdbcProfile
  implicit private val ec = ExecutionContext.global

  import profile.api._

  override def getByMovieId(movieId: String): List[Review] = {
    waitForResult(
      dBProvider().run(
        ReviewQueries.findOneQuery(movieId).result
      ).map(reviews => reviews.map(toReview).toList)
    )
  }

  override def getByMovieIds(movieIds: List[String]): List[Review] = {
    waitForResult(
      dBProvider().run(
        ReviewQueries.reviews.result
      ).map(reviews => reviews.filter(review => movieIds.contains(review.movieId)).map(toReview).toList)
    )
  }

  override def getOrderedByCreateDate(limit: Int): List[Review] = {
    waitForResult(
      dBProvider().run(
        ReviewQueries.findOrdered(limit).result
      ).map(reviews => reviews.map(toReview).toList)
    )
  }

  override def create(review: Review): Boolean = {
    waitForResult(dBProvider().run(ReviewQueries.reviews.insertOrUpdate(toReviewDB(review)).map(_ == 1)))
  }

  private def toReviewDB(review: Review): ReviewDB = {
    ReviewDB(review.id, review.movieId, review.rate, review.description, Timestamp.from(review.createDate))
  }

  private def toReview(review: ReviewDB): Review = {
    Review(review.id, review.movieId, review.rate, review.description, review.createDate.toInstant)
  }

  private def waitForResult[T](future: Future[T]): T = Await.result(future, 10.seconds)
}
