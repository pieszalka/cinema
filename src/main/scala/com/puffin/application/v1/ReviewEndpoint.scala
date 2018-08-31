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
@RequestMapping(path = Array("/reviews"), produces = Array(MediaType.APPLICATION_JSON_VALUE), consumes = Array(CinemaApi.V1, CinemaApi.V2))
@Api(value = "/reviews")
class ReviewEndpoint @Autowired()(
  moviesService: MoviesService,
  reviewsService: ReviewsService
) {

  @PostMapping(path = Array(""))
  @ApiOperation(value = "Create review", response = classOf[Unit])
  def createReview(
    @ApiParam(name = "movie")
    @RequestBody(required = true)
    review: ReviewDto
  ): Unit = {
    if (moviesService.getMovie(review.movieId).isEmpty) {
      throw new Exception(s"Movie ${review.movieId} not exists")
    }
    reviewsService.create(review)
  }
}
