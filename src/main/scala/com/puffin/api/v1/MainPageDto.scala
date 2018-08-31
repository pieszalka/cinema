package com.puffin.api.v1

case class MainPageDto(
  lastReviews: List[MainPageReviewDto],
  newMovies: List[MainPageMovieDto],
  movieCategories: List[String]
)
