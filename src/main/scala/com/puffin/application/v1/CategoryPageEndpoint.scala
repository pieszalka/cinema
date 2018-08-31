package com.puffin.application.v1

import com.puffin.api.CinemaApi
import com.puffin.api.v1._
import com.puffin.domain.movie.MoviesService
import com.puffin.domain.review.ReviewsService
import io.swagger.annotations.{Api, ApiOperation, ApiParam}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.{GetMapping, PathVariable, RequestMapping, RestController}

@RestController
@RequestMapping(path = Array("/categories"), produces = Array(MediaType.APPLICATION_JSON_VALUE), consumes = Array(CinemaApi.V1, CinemaApi.V2))
@Api(value = "/categories")
class CategoryPageEndpoint @Autowired()(
  moviesService: MoviesService,
  reviewsService: ReviewsService
) {

  @GetMapping(path = Array("/{category}"))
  @ApiOperation(value = "Get data for category page", response = classOf[CategoryPageDto])
  def getCategoryPageData(
    @ApiParam(name = "category", allowMultiple = false, example = "DRAMA")
    @PathVariable(value = "category", required = true)
    category: String
  ): CategoryPageDto = {

    if (!moviesService.getMoviesCategories.contains(category)) {
      throw new Exception(s"Category $category not exists")
    }

    val movies = moviesService.getMoviesFromCategory(category)
    val reviews = reviewsService.getReviews(movies.map(_.id))

    CategoryPageDto(
      movies.map(movie =>
        CategoryPageMovieDto(
          movie.id,
          movie.name,
          reviewsService.calculateAverageRating(reviews.filter(review => review.movieId == movie.id))
        )
      )
    )
  }

}
