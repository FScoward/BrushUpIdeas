package models.database

import scala.slick.driver.H2Driver.simple._
import play.api.db.DB
import play.api.Play.current

case class Idea(id: Int = -1, contents: String, twitterAccount: String)
object Ideas {
  
  val database = Database.forDataSource(DB.getDataSource())
  
  class Ideas(tag: Tag) extends Table[Idea](tag, "IDEAS") {
    def id = column[Int]("IDEA_ID", O.PrimaryKey, O.AutoInc)
    def contents = column[String]("CONTENTS")
    def twitterAccount = column[String]("TWITTER_ACCOUNT")
    def * = (id, contents, twitterAccount) <> (Idea.tupled, Idea.unapply)
  }
  
  val ideas = TableQuery[Ideas]
	
  def create(idea: Idea) = database.withTransaction { implicit session: Session =>
    ideas.insert(idea)
  }
	
}