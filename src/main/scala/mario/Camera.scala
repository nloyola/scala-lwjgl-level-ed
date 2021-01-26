package mario

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

class Camera(private var position: Vector2f) {

  private val projectionMatrix = new Matrix4f
  private val viewMatrix       = new Matrix4f

  def adjustProjection(): Unit = {
    projectionMatrix.identity
    projectionMatrix.ortho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f, 0.0f, 100.0f)
    ()
  }

  def getViewMatrix(): Matrix4f = {
    val cameraFront = new Vector3f(0.0f, 0.0f, -1.0f)
    val cameraUp    = new Vector3f(0.0f, 1.0f, 0.0f)
    viewMatrix.identity
    viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
                      cameraFront.add(position.x, position.y, 0.0f),
                      cameraUp)

    viewMatrix
  }

  def getProjectionMatrix(): Matrix4f = projectionMatrix

  def move(x: Float, y: Float): Unit = {
    position.x = position.x + x
    position.y = position.y + y
  }

  adjustProjection()

}
