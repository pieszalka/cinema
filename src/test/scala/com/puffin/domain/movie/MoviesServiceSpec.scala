package com.puffin.domain.movie

import java.time.Instant

import com.puffin.api.v1.MovieDto
import org.mockito.Mockito.when
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar

class MoviesServiceSpec extends FlatSpec with Matchers with MockitoSugar with ScalaFutures {

  it should "get movies categories" in {
    // given
    val moviesRepository = mock[MoviesRepository]
    val service = new MoviesService(moviesRepository)

    //when
    val categories = service.getMoviesCategories

    //then
    categories should be (List("DRAMA", "HORROR", "ROMANCE"))
  }

  it should "get movie" in {
    // given
    val movieId = "1"
    val movie = Movie(movieId, "movie-name", "movie-description", CategoryType.DRAMA, Instant.now)
    val moviesRepository = mock[MoviesRepository]
    when(moviesRepository.getByMovieId(movieId)).thenReturn(Some(movie))
    val service = new MoviesService(moviesRepository)

    //when
    val movieResult = service.getMovie(movieId)

    //then
    movieResult.get should be (MovieDto(movieId, "movie-name", "movie-description", "DRAMA"))
  }
}