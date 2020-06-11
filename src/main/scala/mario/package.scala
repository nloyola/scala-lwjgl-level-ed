import play.api.libs.json._
import org.joml.{Vector2f, Vector4f}

package object mario {

  implicit val vector2fFormat: Format[Vector2f] = new Format[Vector2f] {
    override def writes(v: Vector2f): JsValue = Json.obj("x" -> v.x, "y" -> v.y)

    override def reads(json: JsValue): JsResult[Vector2f] =
      JsSuccess(new Vector2f((json \ "x").as[Float], (json \ "y").as[Float]))
  }

  implicit val vector4fFormat: Format[Vector4f] = new Format[Vector4f] {
    override def writes(v: Vector4f): JsValue = Json.obj("w" -> v.w, "x" -> v.x, "y" -> v.y, "z" -> v.z)

    override def reads(json: JsValue): JsResult[Vector4f] =
      JsSuccess(
        new Vector4f((json \ "w").as[Float],
                     (json \ "x").as[Float],
                     (json \ "y").as[Float],
                     (json \ "z").as[Float])
      )
  }
}
