package com.puffin.application.v1

import com.puffin.api.CinemaApi
import com.puffin.api.v1._
import com.puffin.domain.movie.MoviesService
import com.puffin.domain.review.ReviewsService
import io.swagger.annotations.{Api, ApiOperation, ApiParam}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RequestParam, RestController}

@RestController
@RequestMapping(path = Array("/main"), produces = Array(MediaType.APPLICATION_JSON_VALUE), consumes = Array(CinemaApi.V1, CinemaApi.V2))
@Api(value = "/main")
class MainPageEndpoint @Autowired()(
  moviesService: MoviesService,
  reviewsService: ReviewsService
) {

  @GetMapping(path = Array(""))
  @ApiOperation(value = "Get data for main page", response = classOf[MainPageDto])
  def getMainPageData(
    @ApiParam(name = "reviews.limit", allowMultiple = false, example = "3", defaultValue = "3")
    @RequestParam(value = "reviews.limit", defaultValue = "3", required = false)
    reviewsLimit: Int,

    @ApiParam(name = "movies.limit", allowMultiple = false, example = "4", defaultValue = "4")
    @RequestParam(value = "movies.limit", defaultValue = "4", required = false)
    moviesLimit: Int

): MainPageDto = {
    val lastReviews = reviewsService.getReviewsOrdered(reviewsLimit)
    val lastReviewsMovies = moviesService.getMovies(lastReviews.map(_.movieId))
    val newMovies = moviesService.getMoviesOrdered(moviesLimit)
    val newMoviesReviews = reviewsService.getReviews(newMovies.map(_.id))

    MainPageDto(
      getLastReviewsForMainPage(lastReviews, lastReviewsMovies),
      getNewMoviesForMainPage(newMovies, newMoviesReviews),
      moviesService.getMoviesCategories
    )
  }

  private def getLastReviewsForMainPage(lastReviews: List[ReviewDto], lastReviewsMovies: List[MovieDto]): List[MainPageReviewDto] = {
    lastReviews.flatMap(review =>
      lastReviewsMovies.find(movie => movie.id == review.movieId)
        .map(movie => List(MainPageReviewDto(review.id, movie.id, movie.name, review.description)))
        .getOrElse(List())
    )
  }

  private def getNewMoviesForMainPage(newMovies: List[MovieDto], newMoviesReviews: List[ReviewDto]): List[MainPageMovieDto] = {
    newMovies.map(movie =>
      MainPageMovieDto(
        movie.id, movie.name, reviewsService.calculateAverageRating(newMoviesReviews.filter(review => review.movieId == movie.id))
      )
    )
  }
}
