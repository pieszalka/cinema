package com.puffin.infrastructure.slick

import java.sql.Timestamp

import com.puffin.infrastructure.slick.model.ReviewDB
import slick.jdbc.JdbcProfile

trait SlickReviewsRepositoryDDL {

  protected val dBProvider: DBProvider

  protected val profile: JdbcProfile = dBProvider.jdbcProfile

  import profile.api._

  protected object Ddl {

    class ReviewsDB_DDL(tag: Tag) extends Table[ReviewDB](tag, "reviews") {

      def id: Rep[String] = column[String]("id", O.PrimaryKey)

      def movieId: Rep[String] = column[String]("movieId")

      def rate: Rep[Int] = column[Int]("rate")

      def description: Rep[String] = column[String]("description")

      def createDate: Rep[Timestamp] = column[Timestamp]("create_date")

      def * =
        (id, movieId, rate, description, createDate) <> (ReviewDB.tupled, ReviewDB.unapply)

    }
  }


  protected object ReviewQueries {

    val reviews: TableQuery[Ddl.ReviewsDB_DDL] = TableQuery[Ddl.ReviewsDB_DDL]

    val findOneQuery = {
      def query(id: Rep[String]) = reviews.filter(_.movieId === id)

      Compiled(query _)
    }

    def findOrdered(limit: Int) = reviews.sortBy(review => review.createDate.desc).take(limit)

  }
}
