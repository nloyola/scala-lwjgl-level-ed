package mario.scenes

import imgui.ImGui;
import mario.{Camera, GameObject}
import mario.renderers.Renderer
import scala.collection.mutable.ListBuffer

trait Scene {

  protected val renderer = new Renderer
  protected val camera: Camera
  protected var isRunning   = false
  protected val gameObjects = ListBuffer.empty[GameObject]
  protected var activeGameObj: Option[GameObject] = None

  def init(): Unit

  def update(deltaTime: Float): Unit

  def start(): Unit = {
    gameObjects.foreach { obj =>
      obj.start
      renderer.add(obj)
    }
    isRunning = true
  }

  def addGameObjectToScene(obj: GameObject): Unit = {
    gameObjects += obj
    if (isRunning) {
      obj.start
      renderer.add(obj)
    }
  }

  def getCamera(): Camera = camera

  def sceneImgui(): Unit = {
    activeGameObj.foreach { go =>
      ImGui.begin("Instpector")
      go.imgui();
      ImGui.end
    }
  }

  def imgui(): Unit = {}

}
