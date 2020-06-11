package mario.components

import org.joml.Vector2f
import mario._
import mario.renderers.Texture
import play.api.libs.json._

final case class Sprite(texture: Option[Texture], texCoords: List[Vector2f]) {

  override def toString: String = s"texture: $texture, texCoord: $texCoords"

}

object Sprite {

  def apply(texture: Option[Texture]): Sprite =
    Sprite(texture, List(new Vector2f(1, 1), new Vector2f(1, 0), new Vector2f(0, 0), new Vector2f(0, 1)))

  implicit val spriteFormat: Format[Sprite] = Json.format[Sprite]

}
