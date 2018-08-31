package com.puffin.infrastructure.slick

import java.sql.Timestamp

import com.puffin.infrastructure.slick.model.MovieDB
import slick.jdbc.JdbcProfile

trait SlickMoviesRepositoryDDL {

  protected val dBProvider: DBProvider

  protected val profile: JdbcProfile = dBProvider.jdbcProfile

  import profile.api._

  protected object Ddl {

    class MoviesDB_DDL(tag: Tag) extends Table[MovieDB](tag, "movies") {

      def id: Rep[String] = column[String]("id", O.PrimaryKey)

      def name: Rep[String] = column[String]("name")

      def description: Rep[String] = column[String]("description")

      def category: Rep[String] = column[String]("category")

      def createDate: Rep[Timestamp] = column[Timestamp]("create_date")

      def * =
        (id, name, description, category, createDate) <> (MovieDB.tupled, MovieDB.unapply)

    }
  }

  protected object MovieQueries {

    val movies: TableQuery[Ddl.MoviesDB_DDL] = TableQuery[Ddl.MoviesDB_DDL]

    val findOneQuery = {
      def query(id: Rep[String]) = movies.filter(_.id === id)

      Compiled(query _)
    }

    val findByCategoryQuery = {
      def query(category: Rep[String]) = movies.filter(_.category === category)

      Compiled(query _)
    }

    def findOrdered(limit: Int) = movies.sortBy(movie => movie.createDate.desc).take(limit)

  }
}
