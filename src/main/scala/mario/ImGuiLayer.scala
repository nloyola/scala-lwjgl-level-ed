package mario

import imgui._
import imgui.callbacks.ImStrConsumer
import imgui.callbacks.ImStrSupplier
import imgui.enums.ImGuiBackendFlags
import imgui.enums.ImGuiConfigFlags
import imgui.enums.ImGuiKey
import imgui.enums.ImGuiMouseCursor
import imgui.gl3.ImGuiImplGl3
import mario.scenes.Scene
import org.lwjgl.glfw.GLFW._

class ImGuiLayer(private val glfwWindow: Long) {

  // Mouse cursors provided by GLFW
  private val mouseCursors = new Array[Long](ImGuiMouseCursor.COUNT)

  // LWJGL3 renderer (SHOULD be initialized)
  private val imGuiGl3 = new ImGuiImplGl3()

  // Initialize Dear ImGui.
  def initImGui(): Unit = {
    // IMPORTANT!!
    // This line is critical for Dear ImGui to work.
    ImGui.createContext()

    // ------------------------------------------------------------
    // Initialize ImGuiIO config
    val io = ImGui.getIO()

    io.setIniFilename("imgui.ini") // We don't want to save .ini file
    io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard) // Navigation with keyboard
    io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors) // Mouse cursors to display while resizing windows etc.
    io.setBackendPlatformName("imgui_java_impl_glfw")

    // ------------------------------------------------------------
    // Keyboard mapping. ImGui will use those indices to peek into the io.KeysDown[] array.
    val keyMap = new Array[Int](ImGuiKey.COUNT)
    keyMap(ImGuiKey.Tab)         = GLFW_KEY_TAB
    keyMap(ImGuiKey.LeftArrow)   = GLFW_KEY_LEFT
    keyMap(ImGuiKey.RightArrow)  = GLFW_KEY_RIGHT
    keyMap(ImGuiKey.UpArrow)     = GLFW_KEY_UP
    keyMap(ImGuiKey.DownArrow)   = GLFW_KEY_DOWN
    keyMap(ImGuiKey.PageUp)      = GLFW_KEY_PAGE_UP
    keyMap(ImGuiKey.PageDown)    = GLFW_KEY_PAGE_DOWN
    keyMap(ImGuiKey.Home)        = GLFW_KEY_HOME
    keyMap(ImGuiKey.End)         = GLFW_KEY_END
    keyMap(ImGuiKey.Insert)      = GLFW_KEY_INSERT
    keyMap(ImGuiKey.Delete)      = GLFW_KEY_DELETE
    keyMap(ImGuiKey.Backspace)   = GLFW_KEY_BACKSPACE
    keyMap(ImGuiKey.Space)       = GLFW_KEY_SPACE
    keyMap(ImGuiKey.Enter)       = GLFW_KEY_ENTER
    keyMap(ImGuiKey.Escape)      = GLFW_KEY_ESCAPE
    keyMap(ImGuiKey.KeyPadEnter) = GLFW_KEY_KP_ENTER
    keyMap(ImGuiKey.A)           = GLFW_KEY_A
    keyMap(ImGuiKey.C)           = GLFW_KEY_C
    keyMap(ImGuiKey.V)           = GLFW_KEY_V
    keyMap(ImGuiKey.X)           = GLFW_KEY_X
    keyMap(ImGuiKey.Y)           = GLFW_KEY_Y
    keyMap(ImGuiKey.Z)           = GLFW_KEY_Z
    io.setKeyMap(keyMap)

    // ------------------------------------------------------------
    // Mouse cursors mapping
    mouseCursors(ImGuiMouseCursor.Arrow)      = glfwCreateStandardCursor(GLFW_ARROW_CURSOR)
    mouseCursors(ImGuiMouseCursor.TextInput)  = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR)
    mouseCursors(ImGuiMouseCursor.ResizeAll)  = glfwCreateStandardCursor(GLFW_ARROW_CURSOR)
    mouseCursors(ImGuiMouseCursor.ResizeNS)   = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR)
    mouseCursors(ImGuiMouseCursor.ResizeEW)   = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR)
    mouseCursors(ImGuiMouseCursor.ResizeNESW) = glfwCreateStandardCursor(GLFW_ARROW_CURSOR)
    mouseCursors(ImGuiMouseCursor.ResizeNWSE) = glfwCreateStandardCursor(GLFW_ARROW_CURSOR)
    mouseCursors(ImGuiMouseCursor.Hand)       = glfwCreateStandardCursor(GLFW_HAND_CURSOR)
    mouseCursors(ImGuiMouseCursor.NotAllowed) = glfwCreateStandardCursor(GLFW_ARROW_CURSOR)

    // ------------------------------------------------------------
    // GLFW callbacks to handle user input

    glfwSetKeyCallback(glfwWindow,
                       (window: Long, key: Int, scancode: Int, action: Int, mods: Int) => {
                         if (action == GLFW_PRESS) {
                           io.setKeysDown(key, true)
                         } else if (action == GLFW_RELEASE) {
                           io.setKeysDown(key, false)
                         }

                         io.setKeyCtrl(
                           io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL)
                         )
                         io.setKeyShift(
                           io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT)
                         )
                         io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT))
                         io.setKeySuper(
                           io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER)
                         )
                       })

    glfwSetCharCallback(glfwWindow, (w: Long, c: Int) => {
      if (c != GLFW_KEY_DELETE) {
        io.addInputCharacter(c)
      }
    })

    glfwSetMouseButtonCallback(glfwWindow,
                               (window: Long, button: Int, action: Int, mods: Int) => {
                                 val mouseDown =
                                   Array[Boolean](button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE,
                                                  button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE,
                                                  button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE,
                                                  button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE,
                                                  button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE)

                                 io.setMouseDown(mouseDown)

                                 if (!io.getWantCaptureMouse() && mouseDown(1)) {
                                   ImGui.setWindowFocus(null)
                                 }
                               })

    glfwSetScrollCallback(glfwWindow, (w: Long, xOffset: Double, yOffset: Double) => {
      io.setMouseWheelH(io.getMouseWheelH() + xOffset.toFloat)
      io.setMouseWheel(io.getMouseWheel() + yOffset.toFloat)
    })

    io.setSetClipboardTextFn(new ImStrConsumer() {
      override def accept(s: String): Unit = {
        glfwSetClipboardString(glfwWindow, s)
      }
    })

    io.setGetClipboardTextFn(new ImStrSupplier() {
      override def get(): String = {
        val clipboardString = glfwGetClipboardString(glfwWindow)
        if (clipboardString != null) {
          return clipboardString
        } else {
          return ""
        }
      }
    })

    // ------------------------------------------------------------
    // Fonts configuration
    // Read: https://raw.githubusercontent.com/ocornut/imgui/master/docs/FONTS.txt

    val fontAtlas  = io.getFonts
    val fontConfig = new ImFontConfig() // Natively allocated object, should be explicitly destroyed

    // Glyphs could be added per-font as well as per config used globally like here
    fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault)

    // Fonts merge example
    fontConfig.setPixelSnapH(true)

    // Fonts from file/memory example
    // We can add new fonts from the file system
    fontAtlas.addFontFromFileTTF("assets/fonts/Ubuntu-R.ttf", 24, fontConfig)
    fontConfig.destroy() // After all fonts were added we don't need this config more

    // ------------------------------------------------------------
    // Use freetype instead of stb_truetype to build a fonts texture
    ImGuiFreeType.buildFontAtlas(fontAtlas, ImGuiFreeType.RasterizerFlags.LightHinting)

    // Method initializes LWJGL3 renderer.
    // This method SHOULD be called after you've initialized your ImGui configuration (fonts and so on).
    // ImGui context should be created as well.
    imGuiGl3.init("#version 330 core")
  }

  def update(dt: Float, currentScene: Scene): Unit = {
    startFrame(dt)

    // Any Dear ImGui code SHOULD go between ImGui.newFrame()/ImGui.render() methods
    ImGui.newFrame()
    currentScene.sceneImgui();
    ImGui.showDemoWindow()
    ImGui.render()

    endFrame()
  }

  def startFrame(deltaTime: Float): Unit = {
    // Get window properties and mouse position
    val winWidth  = Array[Float](Window.getWidth.toFloat)
    val winHeight = Array[Float](Window.getHeight.toFloat)

    val mousePosX = Array[Double](0)
    val mousePosY = Array[Double](0)
    glfwGetCursorPos(glfwWindow, mousePosX, mousePosY)

    // We SHOULD call those methods to update Dear ImGui state for the current frame
    val io = ImGui.getIO()
    io.setDisplaySize(winWidth(0), winHeight(0))
    io.setDisplayFramebufferScale(1f, 1f)
    io.setMousePos(mousePosX(0).toFloat, mousePosY(0).toFloat)
    io.setDeltaTime(deltaTime)

    // Update the mouse cursor
    val imguiCursor = ImGui.getMouseCursor()
    glfwSetCursor(glfwWindow, mouseCursors(imguiCursor))
    glfwSetInputMode(glfwWindow, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
  }

  private def endFrame(): Unit = {
    // After Dear ImGui prepared a draw data, we use it in the LWJGL3 renderer.
    // At that moment ImGui will be rendered to the current OpenGL context.
    imGuiGl3.render(ImGui.getDrawData())
  }

  // If you want to clean a room after yourself - do it by yourself
  // private
  def destroyImGui(): Unit = {
    imGuiGl3.dispose()
    ImGui.destroyContext()
  }
}
