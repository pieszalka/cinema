package com.puffin.domain.movie

import java.time.Instant
import java.util.UUID

import com.puffin.api.v1.MovieDto
import com.puffin.domain.movie.CategoryType.CategoryType
import com.puffin.domain.movie.domain.MovieId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MoviesService @Autowired()(
  moviesRepository: MoviesRepository
) {
  def getMovie(movieId: MovieId): Option[MovieDto] = {
    moviesRepository.getByMovieId(movieId).map(toMovieDto)
  }

  def getMoviesFromCategory(category: String): List[MovieDto] = {
    val movieCategory = getCategoryType(category)
    moviesRepository.getByCategory(movieCategory).map(toMovieDto)
  }

  def getMovies(movieIds: List[MovieId]): List[MovieDto] = {
    moviesRepository.getByMovieIds(movieIds).map(toMovieDto)
  }

  def getMoviesOrdered(limit: Int): List[MovieDto] = {
    moviesRepository.getOrderedByCreateDate(limit).map(toMovieDto)
  }

  def getMoviesCategories: List[String] = {
    CategoryType.values.filter(category => category != CategoryType.OTHER).map(_.toString).toList
  }

  def create(movie: MovieDto): Unit = {
    val movieCategory = getCategoryType(movie.category)
    moviesRepository.create(
      Movie(UUID.randomUUID().toString, movie.name, movie.description, movieCategory, Instant.now())
    )
  }

  private def toMovieDto(movie: Movie): MovieDto = {
    MovieDto(movie.id, movie.name, movie.description, movie.category.toString)
  }

  private def getCategoryType(movieCategory: String): CategoryType = {
    CategoryType.values.find(category => category.toString == movieCategory).getOrElse(CategoryType.OTHER)
  }
}
