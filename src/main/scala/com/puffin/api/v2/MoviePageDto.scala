package com.puffin.api.v2

import com.puffin.api.v1.{MovieDto, ReviewDto}

case class MoviePageDto(
  movie: MovieDto,
  movieLikes: Int,
  movieDislikes: Int,
  movieReviews: List[ReviewDto]
)
