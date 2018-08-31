package com.puffin.domain.movie

import com.puffin.domain.movie.CategoryType.CategoryType
import com.puffin.domain.movie.domain.MovieId

abstract class MoviesRepository {
  def getByMovieId(movieId: MovieId): Option[Movie]

  def getByCategory(category: CategoryType): List[Movie]

  def getByMovieIds(movieIds: List[MovieId]): List[Movie]

  def getOrderedByCreateDate(limit: Int): List[Movie]

  def create(movie: Movie): Boolean
}
