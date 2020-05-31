package mario.components

import org.joml.Vector2f;
import mario.renderers.Texture;

class Sprite(private val texture: Option[Texture], private val texCoords: List[Vector2f]) {

  def this(texture: Option[Texture]) = {
    this(texture, List(new Vector2f(1, 1), new Vector2f(1, 0), new Vector2f(0, 0), new Vector2f(0, 1)))
  }

  def getTexture(): Option[Texture] = texture

  def getTexCoords(): List[Vector2f] = texCoords

  override def toString: String = s"texture: $texture, texCoord: $texCoords"
}
