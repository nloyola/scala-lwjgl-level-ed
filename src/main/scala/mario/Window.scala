package mario

import mario.scenes._
import org.lwjgl._
import org.lwjgl.glfw._
import org.lwjgl.opengl._
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11._
import org.lwjgl.system.MemoryUtil._

object Window {
  var width  = 1920
  var height = 1080
  val title  = "LWJGL"
  var currentScene:       Option[Scene]      = None
  private var imguiLayer: Option[ImGuiLayer] = None

  def main(args: Array[String]): Unit = {
    val window = new Window
    window.run
  }

  def getScene(): Scene = {
    currentScene match {
      case Some(s) => s
      case _       => throw new Error(s"Scene not assigned")
    }
  }

  def changeScene(newScene: Int): Unit = {
    newScene match {
      case 0 => currentScene = Some(new LevelEditorScene)
      case 1 => currentScene = Some(new LevelScene)
      case _ => throw new Error(s"Unknown scene: $newScene")
    }

    currentScene.foreach { s =>
      s.init
      s.start
    }
  }

  def updateScene(dt: Float): Unit = {
    currentScene.foreach { s =>
      s.update(dt)
    }

  }

  def getWidth(): Int = width

  def setWidth(w: Int): Unit = width = w

  def getHeight(): Int = height

  def setHeight(h: Int): Unit = height = h
}

class Window { // The window handle
  import Window._

  private var window = 0L

  def run(): Unit = {
    System.out.println("Hello LWJGL " + Version.getVersion + "!")
    init()
    loop()
    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(window)
    glfwDestroyWindow(window)
    // Terminate GLFW and free the error callback
    glfwTerminate
    glfwSetErrorCallback(null).free
  }

  private def init(): Unit = {
    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (!glfwInit) {
      throw new IllegalStateException("Unable to initialize GLFW")
    }

    // Configure GLFW
    glfwDefaultWindowHints // optional, the current window hints are already the default
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable

    // Create the window
    window = glfwCreateWindow(width, height, title, NULL, NULL)
    if (window == NULL) {
      throw new IllegalStateException("Failed to create the GLFW window.")
    }

    // // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    // glfwSetKeyCallback(
    //   window,
    //   (window: Long, key: Int, scancode: Int, action: Int, mods: Int) => {
    //     if ((key == GLFW_KEY_ESCAPE) && (action == GLFW_RELEASE)) {
    //       glfwSetWindowShouldClose(window, true)
    //     }
    //   })

    glfwSetCursorPosCallback(window, MouseListener.mousePosCallback)
    glfwSetMouseButtonCallback(window, MouseListener.mouseButtonCallback)
    glfwSetScrollCallback(window, MouseListener.mouseScrollCallback)
    glfwSetKeyCallback(window, KeyListener.keyCallback)
    glfwSetWindowSizeCallback(window, (w: Long, newWidth: Int, newHeight: Int) => {
      setWidth(newWidth)
      setHeight(newHeight)
    })

    // Make the OpenGL context current
    glfwMakeContextCurrent(window)
    // Enable v-sync
    glfwSwapInterval(1)
    // Make the window visible
    glfwShowWindow(window)

    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities

    glEnable(GL_BLEND)
    glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA)
    imguiLayer = Some(new ImGuiLayer(window))
    imguiLayer.foreach(_.initImGui())

    changeScene(0)
  }

  private def loop(): Unit = {
    val r = 1.0f
    val b = 1.0f
    val g = 1.0f
    val a = 1.0f

    var beginTime = glfwGetTime.toFloat
    var endTime   = 0.0f
    var dt        = -1.0f

    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while (!glfwWindowShouldClose(window)) {
      // Poll for window events. The key callback above will only be
      // invoked during this call.
      glfwPollEvents

      glClearColor(r, g, b, a)
      //glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT) // clear the framebuffer
      glClear(GL_COLOR_BUFFER_BIT)

      if (dt >= 0) {
        updateScene(dt)
      }

      currentScene.foreach { scene =>
        imguiLayer.foreach(_.update(dt, scene))
      }
      glfwSwapBuffers(window) // swap the color buffers

      endTime   = glfwGetTime.toFloat
      dt        = endTime - beginTime
      beginTime = endTime
    }
  }

}
