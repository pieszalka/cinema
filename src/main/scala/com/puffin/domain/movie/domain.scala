package com.puffin.domain.movie

import java.time.Instant

import com.puffin.domain.movie.CategoryType.CategoryType

object CategoryType extends Enumeration {
  type CategoryType = Value
  val HORROR, DRAMA, ROMANCE, OTHER = Value
}

package object domain {
  type MovieId = String
}

case class Movie(id: String, name: String, description: String, category: CategoryType, createDate: Instant)
