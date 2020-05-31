package mario.components

import imgui.ImGui;
import mario.GameObject
import mario.Transform
import mario.renderers.Texture
import org.joml.Vector2f
import org.joml.Vector4f
import org.slf4j.LoggerFactory

class SpriteRenderer(private var sprite: Sprite, private var color: Vector4f) extends Component {

  var gameObject:    Option[GameObject] = None
  var lastTransform: Option[Transform]  = None
  var _isDirty = true

  private val logger = LoggerFactory.getLogger(this.getClass)

  def this(color: Vector4f) = this(new Sprite(None), color)

  def this(sprite: Sprite) = this(sprite, new Vector4f(1, 1, 1, 1))

  override def start(): Unit = {
    lastTransform = gameObject.map(go => go.getTransform.copy)
    //lastTransform.foreach(lt => logger.info(s"last transform: $lt"))
  }

  override def update(dt: Float): Unit = {
    for {
      lt <- lastTransform
      go <- gameObject
    } yield {
      if (!lt.equals(go.getTransform)) {
        logger.info(s"last transform: $lt")
        go.setTransform(lt.copy)
        _isDirty = true
      }
    }
    ()
  }

  override def imgui(): Unit = {
    val imColors = Array[Float](color.x, color.y, color.z, color.w)
    if (ImGui.colorPicker4("Color: ", imColors)) {
      color.set(imColors(0), imColors(1), imColors(2), imColors(3))
      _isDirty = true
    }
  }

  def getColor(): Vector4f = color

  def getTexture(): Option[Texture] = sprite.getTexture

  def getTexCoords(): List[Vector2f] = sprite.getTexCoords

  def setSprite(s: Sprite): Unit = {
    sprite   = s
    _isDirty = true
  }

  def setColor(c: Vector4f): Unit = {
    if (!color.equals(c)) {
      color.set(c)
      _isDirty = true
    }
  }

  def isDirty(): Boolean = _isDirty

  def setClean(): Unit = _isDirty = false

  override def toString: String = s"gameObject: $gameObject, isDirty: ${_isDirty})"

}
