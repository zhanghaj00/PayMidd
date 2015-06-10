package main.rest

import spray.json.DefaultJsonProtocol

/**
 * Created by zhanghao on 2015/6/9.
 */
object Domain {

  case class User(email:String,userName:String,passWord:String)

  object User extends DefaultJsonProtocol{
      implicit  val format = jsonFormat3(User.apply)
  }
}
