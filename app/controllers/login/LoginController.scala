package controllers.login

import play.api._
import play.api.mvc._
import twitter4j.{TwitterException, TwitterFactory}
import play.api.cache._
import play.api.Play.current
import twitter4j.auth.AccessToken

/**
 * Created by FScoward on 14/03/02.
 */
object LoginController extends Controller {

  // TODO
  def login = Action {
    val twitter = TwitterFactory.getSingleton

    try{
      val requestToken = twitter.getOAuthRequestToken
      val url = requestToken.getAuthorizationURL

      Redirect(url)
    } catch {
      case e: TwitterException => {
        // TODO
        play.Logger.debug("TwitterException")
        BadRequest(e.getMessage + e.getErrorMessage)
      }
      case e: IllegalStateException => {
//        twitter.getOAuthAccessToken // TODO 要確認
//        Ok(views.html.registration.registration(InvitationController.invitationForm))

        Cache.get("accessToken") match {
          case Some(at: AccessToken) =>{
            val twitter = TwitterFactory.getSingleton
            twitter.setOAuthAccessToken(at)
            val twitterAccount = twitter.getScreenName
            Ok(views.html.ideas(Some(twitterAccount))).withSession("twitterAccount" -> twitterAccount)
          }
          case None => BadRequest(e.getMessage)
        }
      }
    }
  }

  // TODO
  /*
  * Twitter認証用URLにリダイレクトすることによって、Twitterユーザーのログイン・アプリの許可の確認画面が開く。
  * 一度認証されると、次からはそれらの画面は開かずに直接コールバックURLにリダイレクトされる。
  * （Twitterからログアウトすると、再度認証が要求される）
  * */
  def callback = Action { request =>

    val oauthVerifier = request.getQueryString("oauth_verifier").get

    val twitter = TwitterFactory.getSingleton
    val accessToken = twitter.getOAuthAccessToken(oauthVerifier)

    /**
     * TODO
     * Cache にアクセストークンを作成
     * */

    Cache.set("accessToken", accessToken)

    // twitter account の取得
    val twitterAccount = twitter.getScreenName

    Ok(views.html.ideas(Some(twitterAccount))).withSession("twitterAccount" -> twitterAccount)
  }

}
