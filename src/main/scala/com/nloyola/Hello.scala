package com.nloyola

import org.lwjgl._
import org.lwjgl.glfw._
import org.lwjgl.opengl._
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11._
import org.lwjgl.system.MemoryUtil._
import scala.Console

object HelloWorld {
  val width = 1920
  val height = 1080
  val title = "LWJGL"

  def main(args: Array[String]): Unit = {
    new HelloWorld().run()
  }
}

class HelloWorld { // The window handle
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
    window = glfwCreateWindow(HelloWorld.width, HelloWorld.height, HelloWorld.title, NULL, NULL)
    if (window == NULL) {
      throw new IllegalStateException("Failed to create the GLFW window.");
    }

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(
      window,
      (window: Long, key: Int, scancode: Int, action: Int, mods: Int) => {
        if ((key == GLFW_KEY_ESCAPE) && (action == GLFW_RELEASE)) {
          glfwSetWindowShouldClose(window, true)
        }
      })

    glfwSetCursorPosCallback(window, MouseListener.mousePosCallback);
    glfwSetMouseButtonCallback(window, MouseListener.mouseButtonCallback);
    glfwSetScrollCallback(window, MouseListener.mouseScrollCallback);
    glfwSetKeyCallback(window, KeyListener.keyCallback);

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
    ()
  }

  private def loop(): Unit = {
    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while (!glfwWindowShouldClose(window)) {
      // Poll for window events. The key callback above will only be
      // invoked during this call.
      glfwPollEvents

      glClearColor(1.0f, 0.0f, 0.0f, 0.0f)
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT) // clear the framebuffer

      glfwSwapBuffers(window) // swap the color buffers
    }
  }
}
