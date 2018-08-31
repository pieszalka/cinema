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
import scala.concurrent.{Await, ExecutionContext}

@Component
class SlickMoviesRepository(
  protected val dBProvider: DBProvider
) extends MoviesRepository with SlickMoviesRepositoryDDL with LazyLogging {

  override protected val profile: JdbcProfile = dBProvider.jdbcProfile
  implicit private val ec = ExecutionContext.global

  import profile.api._

  override def getByMovieId(movieId: MovieId): Option[Movie] = {
    Await.result(
      dBProvider().run(
        MovieQueries.findOneQuery(movieId).result.headOption
      ).map(
        movieDBOption => movieDBOption.map(toMovie)
      ),
      10.seconds
    )
  }

  override def getByCategory(category: CategoryType): List[Movie] = {
    Await.result(
      dBProvider().run(
        MovieQueries.findByCategoryQuery(category.toString).result
      ).map(movies => movies.map(toMovie).toList),
      10.seconds
    )
  }

  override def getByMovieIds(movieIds: List[MovieId]): List[Movie] = {
    Await.result(
      dBProvider().run(
        MovieQueries.movies.result
      ).map(movies => movies.filter(movie => movieIds.contains(movie.id)).map(toMovie).toList),
      10.seconds
    )
  }

  override def getOrderedByCreateDate(limit: Int): List[Movie] = {
    Await.result(
      dBProvider().run(
        MovieQueries.findOrdered(limit).result
      ).map(movies => movies.map(toMovie).toList),
      10.seconds
    )
  }

  override def create(movie: Movie): Boolean = {
    Await.result(
      dBProvider().run(MovieQueries.movies.insertOrUpdate(toMovieDB(movie)).map(_ == 1)),
      10.seconds
    )
  }

  private def toMovieDB(movie: Movie): MovieDB = {
    MovieDB(movie.id, movie.name, movie.description, movie.category.toString, Timestamp.from(movie.createDate))
  }

  private def toMovie(movie: MovieDB): Movie = {
    val category = CategoryType.values.find(category => category.toString == movie.category).getOrElse(CategoryType.OTHER)
    Movie(movie.id, movie.name, movie.description, category, movie.createDate.toInstant)
  }
}
