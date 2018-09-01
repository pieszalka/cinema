package com.puffin.infrastructure.slick

import java.sql.Timestamp

import com.puffin.domain.movie.CategoryType.CategoryType
import com.puffin.domain.movie.domain.MovieId
import com.puffin.domain.movie.{CategoryType, Movie, MoviesRepository}
import com.puffin.infrastructure.slick.model.MovieDB
import com.typesafe.scalalogging.LazyLogging
import org.springframework.stereotype.Component
import slick.jdbc.JdbcProfile

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

@Component
class SlickMoviesRepository(
  protected val dBProvider: DBProvider
) extends MoviesRepository with SlickMoviesRepositoryDDL with LazyLogging {

  override protected val profile: JdbcProfile = dBProvider.jdbcProfile
  implicit private val ec = ExecutionContext.global

  import profile.api._

  override def getByMovieId(movieId: MovieId): Option[Movie] = {
    waitForResult(
      dBProvider().run(
        MovieQueries.findOneQuery(movieId).result.headOption
      ).map(
        movieDBOption => movieDBOption.map(toMovie)
      )
    )
  }

  override def getByCategory(category: CategoryType): List[Movie] = {
    waitForResult(
      dBProvider().run(
        MovieQueries.findByCategoryQuery(category.toString).result
      ).map(movies => movies.map(toMovie).toList)
    )
  }

  override def getByMovieIds(movieIds: List[MovieId]): List[Movie] = {
    waitForResult(
      dBProvider().run(
        MovieQueries.movies.result
      ).map(movies => movies.filter(movie => movieIds.contains(movie.id)).map(toMovie).toList)
    )
  }

  override def getOrderedByCreateDate(limit: Int): List[Movie] = {
    waitForResult(
      dBProvider().run(
        MovieQueries.findOrdered(limit).result
      ).map(movies => movies.map(toMovie).toList)
    )
  }

  override def create(movie: Movie): Boolean = {
    waitForResult(dBProvider().run(MovieQueries.movies.insertOrUpdate(toMovieDB(movie)).map(_ == 1)))
  }

  private def toMovieDB(movie: Movie): MovieDB = {
    MovieDB(movie.id, movie.name, movie.description, movie.category.toString, Timestamp.from(movie.createDate))
  }

  private def toMovie(movie: MovieDB): Movie = {
    val category = CategoryType.values.find(category => category.toString == movie.category).getOrElse(CategoryType.OTHER)
    Movie(movie.id, movie.name, movie.description, category, movie.createDate.toInstant)
  }

  private def waitForResult[T](future: Future[T]): T = Await.result(future, 10.seconds)
}
