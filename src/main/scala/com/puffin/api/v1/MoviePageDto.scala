package com.puffin.api.v1

case class MoviePageDto(
  movie: MovieDto,
  movieAverageRate: Int,
  movieReviews: List[ReviewDto]
)
