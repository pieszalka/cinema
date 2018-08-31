package com.puffin.application.v1

import com.puffin.api.CinemaApi
import com.puffin.api.v1._
import com.puffin.domain.movie.MoviesService
import com.puffin.domain.review.ReviewsService
import io.swagger.annotations.{Api, ApiOperation, ApiParam}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation._

@RestController
@RequestMapping(path = Array("/movies"), produces = Array(MediaType.APPLICATION_JSON_VALUE), consumes = Array(CinemaApi.V1))
@Api(value = "/movies")
class MoviePageEndpoint @Autowired()(
  moviesService: MoviesService,
  reviewsService: ReviewsService
) {

  @GetMapping(path = Array("/{id}"))
  @ApiOperation(value = "Get data for movie page", response = classOf[MoviePageDto])
  def getMoviePageData(
    @ApiParam(name = "id", allowMultiple = false, example = "DRAMA")
    @PathVariable(value = "id", required = true)
    id: String
  ): MoviePageDto = {
    val movie = moviesService.getMovie(id)
    if (movie.nonEmpty) {
      val reviews = reviewsService.getReviews(id)
      MoviePageDto(
        movie.get,
        reviewsService.calculateAverageRating(reviews),
        reviews
      )
    } else {
      throw new Exception(s"Movie $id not exists")
    }
  }

}
