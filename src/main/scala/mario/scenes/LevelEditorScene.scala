package mario.scenes

import imgui.{ImGui, ImVec2}
import mario.{Camera, GameObject}
import mario.components._
import mario.Transform
import mario.util.AssetPool
import org.joml.{Vector2f, Vector3f, Vector4f}
import org.slf4j.LoggerFactory
import mario.components.Sprite

class LevelEditorScene extends Scene {

  protected lazy val camera = new Camera(new Vector2f(-250, 0))
  private val logger        = LoggerFactory.getLogger(this.getClass)
  private var sprites: Option[Spritesheet] = None

  def init(): Unit = {
    logger.debug("init")
    loadResources()

    sprites = Some(AssetPool.getSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png"))

    if (levelLoaded) {
      activeGameObject = Some(gameObjects(0))
    } else {

      //obj1.addComponent(new SpriteRenderer(sheet.getSprite(0)))
      val obj1 = GameObject("Object 1", new Transform(new Vector2f(200, 100), new Vector2f(256, 256)), 2)
      obj1.addComponent(SpriteRenderer(new Vector4f(0, 0, 1, 1)))
      obj1.addComponent(RigidBody(1, new Vector3f(0, 0.5f, 0), 0.8f, new Vector4f(0, 0, 0, 0)))
      addGameObjectToScene(obj1)
      activeGameObject = Some(obj1)

      val obj2 = GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 1)
      //obj2.addComponent(new SpriteRenderer(sheet.getSprite(7)))
      obj2.addComponent(SpriteRenderer(Sprite(Some(AssetPool.getTexture("assets/images/blendImage2.png")))))
      addGameObjectToScene(obj2)
    }
  }

  def update(dt: Float): Unit = {
    // val tex = AssetPool.getTexture("assets/images/blendImage2.png")
    // tex.debugTexture(0f, 0f, 1000f, 1000f)

    gameObjects.foreach(_.update(dt))
    renderer.render()
  }

  override def imgui(): Unit = {
    ImGui.begin("Test window")

    val windowPos = new ImVec2()
    ImGui.getWindowPos(windowPos)
    val windowSize = new ImVec2()
    ImGui.getWindowSize(windowSize)
    val itemSpacing = new ImVec2()
    ImGui.getStyle().getItemSpacing(itemSpacing)

    val windowX2 = windowPos.x + windowSize.x

    sprites.foreach { sheet =>
      sheet.sprites.zipWithIndex.foreach {
        case (sprite, index) =>
          sprite.texId().map { id =>
            val spriteWidth  = sprite.width * 4
            val spriteHeight = sprite.height * 4
            val texCoords    = sprite.texCoords

            ImGui.pushID(index);
            if (ImGui.imageButton(id,
                                  spriteWidth.toFloat,
                                  spriteHeight.toFloat,
                                  texCoords(0).x,
                                  texCoords(0).y,
                                  texCoords(2).x,
                                  texCoords(2).y)) {
              logger.info(s"Button $index clicked")
            }
            ImGui.popID();

            val lastButtonPos = new ImVec2()
            ImGui.getItemRectMax(lastButtonPos);
            val lastButtonX2 = lastButtonPos.x;
            val nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth
            if (index + 1 < sheet.size() && nextButtonX2 < windowX2) {
              ImGui.sameLine();
            }
          }
      }
    }
    ImGui.end
  }

  private def loadResources(): Unit = {
    AssetPool.getShader("assets/shaders/default.glsl")

    val assetName = "assets/images/spritesheets/decorationsAndBlocks.png"
    AssetPool.addSpritesheet(assetName, new Spritesheet(AssetPool.getTexture(assetName), 16, 16, 81, 0));

    AssetPool.getTexture("assets/images/blendImage2.png")
    AssetPool.getTexture("assets/images/blendImage1.png")
    ()
  }
}
