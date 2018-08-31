package com.puffin.application.v2

import com.puffin.api.CinemaApi
import com.puffin.api.v2.MoviePageDto
import com.puffin.domain.movie.MoviesService
import com.puffin.domain.review.ReviewsService
import io.swagger.annotations.{Api, ApiOperation, ApiParam}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation._

@RestController
@RequestMapping(path = Array("/movies"), produces = Array(MediaType.APPLICATION_JSON_VALUE), consumes = Array(CinemaApi.V2))
@Api(value = "/movies")
class MoviePageEndpointV2 @Autowired()(
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
        reviewsService.calculatePositiveRatings(reviews),
        reviewsService.calculateNegativeRatings(reviews),
        reviews
      )
    } else {
      throw new Exception(s"Movie $id not exists")
    }
  }

}
