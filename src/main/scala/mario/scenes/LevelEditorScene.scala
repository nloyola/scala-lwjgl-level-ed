package mario.scenes

import imgui.ImGui
import mario.{Camera, GameObject}
import mario.components.SpriteRenderer
import mario.components.Spritesheet
import mario.Transform
import mario.util.AssetPool
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.slf4j.LoggerFactory
import mario.components.Sprite

class LevelEditorScene extends Scene {

  protected lazy val camera = new Camera(new Vector2f(-250, 0))
  private val logger        = LoggerFactory.getLogger(this.getClass)

  def init(): Unit = {
    loadResources()

    logger.debug(s"init")

    //val sheet = AssetPool.getSpritesheet("assets/images/spritesheet.png")
    //sprites = Some(sheet)

    //obj1.addComponent(new SpriteRenderer(sheet.getSprite(0)))
    val obj1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100), new Vector2f(256, 256)), 2)
    obj1.addComponent(new SpriteRenderer(new Vector4f(0, 0, 1, 1)))
    addGameObjectToScene(obj1)
    activeGameObject = Some(obj1)

    val obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 1)
    //obj2.addComponent(new SpriteRenderer(sheet.getSprite(7)))
    obj2.addComponent(
      new SpriteRenderer(new Sprite(Some(AssetPool.getTexture("assets/images/blendImage2.png"))))
    )
    addGameObjectToScene(obj2)
  }

  def update(dt: Float): Unit = {
    // val tex = AssetPool.getTexture("assets/images/blendImage2.png")
    // tex.debugTexture(0f, 0f, 1000f, 1000f)

    gameObjects.foreach(_.update(dt))
    renderer.render
  }

  override def imgui(): Unit = {
    ImGui.begin("Test window")
    ImGui.text("Some random text")
    ImGui.end
  }

  private def loadResources(): Unit = {
    AssetPool.getShader("assets/shaders/default.glsl")

    val assetName = "assets/images/spritesheet.png"
    AssetPool.addSpritesheet(assetName, new Spritesheet(AssetPool.getTexture(assetName), 16, 16, 26, 0));
    ()
  }
}
