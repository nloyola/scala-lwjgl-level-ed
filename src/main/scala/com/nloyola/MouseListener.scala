package com.nloyola

import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE
import scala.collection.mutable.ListBuffer

object MouseListener {
  private var scrollX: Double = 0.0
  private var scrollY: Double = 0.0
  private var xPos: Double = 0.0
  private var yPos: Double = 0.0
  private var lastY: Double = 0.0
  private var lastX: Double = 0.0

  private val mouseButtonPressed = ListBuffer(false, false, false)
  private var isMouseDragging = false

  def mousePosCallback(window: Long, xpos: Double, ypos: Double): Unit = {
    lastX = xPos
    lastY = yPos
    this.xPos = xpos
    this.yPos = ypos
    isMouseDragging = mouseButtonPressed(0) || mouseButtonPressed(1) || mouseButtonPressed(2)
  }

  def mouseButtonCallback(window: Long, button: Int, action: Int, mods: Int): Unit = {
    if (action == GLFW_PRESS) {
      if (button < mouseButtonPressed.size) {
        mouseButtonPressed(button) = true
      }
    } else if (action == GLFW_RELEASE) {
      if (button < mouseButtonPressed.size) {
        mouseButtonPressed(button) = false
        isMouseDragging = false
      }
    }
  }

  def mouseScrollCallback(window: Long, xOffset: Double, yOffset: Double): Unit = {
    scrollX = xOffset
    scrollY = yOffset
  }

  def endFrame(): Unit = {
    scrollX = 0
    scrollY = 0
    lastX = xPos
    lastY = yPos
  }

  def getX(): Float = {
    xPos.toFloat
  }

  def getY(): Float = {
    yPos.toFloat
  }

  def getDx(): Float = {
     (lastX - xPos).toFloat
  }

  def getDy(): Float = {
    (lastY - yPos).toFloat
  }

  def getScrollX(): Float = {
    scrollX.toFloat
  }

  def getScrollY(): Float =  {
    scrollY.toFloat
  }

  def isDragging(): Boolean = {
    isMouseDragging
  }

  def mouseButtonDown(button: Int): Boolean = {
    if (button < mouseButtonPressed.length) mouseButtonPressed(button)
    else return false
  }
}
