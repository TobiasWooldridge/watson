package watson.db;

import com.mumfrey.liteloader.gl.GL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;

import watson.Configuration;
import watson.PrivateFieldsWatson;
import watson.model.ARGB;

// ----------------------------------------------------------------------------
/**
 * Represents a visible annotation attached to a {@link BlockEditSet}.
 */
public class Annotation
{
  // --------------------------------------------------------------------------
  /**
   * Constructor.
   *
   * @param x the world x coordinate.
   * @param y the world y coordinate.
   * @param z the world z coordinate.
   * @param text the annotation text to display.
   */
  public Annotation(int x, int y, int z, String text)
  {
    _text = text;
    _x = x;
    _y = y;
    _z = z;
  }

  // --------------------------------------------------------------------------
  /**
   * Return the annotation text to display.
   *
   * @return the annotation text to display.
   */
  public String getText()
  {
    return _text;
  }

  // --------------------------------------------------------------------------
  /**
   * Return the X coordinate of the block that was annotated.
   *
   * @return the X coordinate of the block that was annotated.
   */
  public int getX()
  {
    return _x;
  }

  // --------------------------------------------------------------------------
  /**
   * Return the Y coordinate of the block that was annotated.
   *
   * @return the Y coordinate of the block that was annotated.
   */
  public int getY()
  {
    return _y;
  }

  // --------------------------------------------------------------------------
  /**
   * Return the Z coordinate of the block that was annotated.
   *
   * @return the Z coordinate of the block that was annotated.
   */
  public int getZ()
  {
    return _z;
  }

  // --------------------------------------------------------------------------
  /**
   * Draw this annotation.
   */
  public void draw()
  {
    drawBillboard(getX(), getY(), getZ(),
      Configuration.instance.getBillboardBackground(),
      Configuration.instance.getBillboardForeground(), 0.02, getText());
  }

  // --------------------------------------------------------------------------
  /**
   * Draw a camera-facing text billboard in three dimensions.
   *
   * @param x the x world coordinate.
   * @param y the y world coordinate.
   * @param z the z world coordinate.
   * @param bgARGB the background colour of the billboard, with alpha in the top
   *          8 bits, then red, green, blue in less significant octets (blue in
   *          the least significant 8 bits).
   * @param fgARGB the foreground (text) colour of the billboard, with alpha in
   *          the top 8 bits, then red, green, blue in less significant octets
   *          (blue in the least significant 8 bits).
   * @param scaleFactor a scale factor to adjust the size of the billboard. Try
   *          0.02.
   * @param text the text on the billboard.
   */
  public static void drawBillboard(double x, double y, double z, int bgARGB,
                                   int fgARGB, double scaleFactor, String text)
  {
    RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
    FontRenderer fontRenderer = renderManager.getFontRenderer();
    if (fontRenderer == null)
    {
      // Not ready yet.
      return;
    }

    Minecraft mc = Minecraft.getMinecraft();
    // (512 >> mc.gameSettings.renderDistance) * 0.8;
    double far = mc.gameSettings.renderDistanceChunks * 16;
    double dx = x - PrivateFieldsWatson.renderPosX.get(renderManager) + 0.5d;
    double dy = y - PrivateFieldsWatson.renderPosY.get(renderManager) + 0.5d;
    double dz = z - PrivateFieldsWatson.renderPosZ.get(renderManager) + 0.5d;
    double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
    double dl = distance;
    if (dl > far)
    {
      double d = far / dl;
      dx *= d;
      dy *= d;
      dz *= d;
      dl = far;
    }

    GL.glPushMatrix();

    double scale = (0.05 * dl + 1.0) * scaleFactor;
    GL.glTranslated(dx, dy, dz);
    GL.glRotatef(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
    GL.glRotatef(
      mc.gameSettings.thirdPersonView != 2 ? renderManager.playerViewX
        : -renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
    GL.glScaled(-scale, -scale, scale);
    GL.glDisableLighting();
    GL.glEnableBlend();
    GL.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
    Tessellator tessellator = Tessellator.getInstance();
    VertexBuffer vr = tessellator.getBuffer();

    int textWidth = fontRenderer.getStringWidth(text) >> 1;
    if (textWidth != 0)
    {
      GL.glDisableTexture2D();
      GL.glDisableDepthTest();;
      GL.glDepthMask(false);

      // Draw background plate.
      vr.begin(GL.GL_QUADS, GL.VF_POSITION);
      ARGB bgColour = new ARGB(bgARGB);
      GL.glColor4f(bgColour.getRed() / 255f, bgColour.getGreen() / 255f, bgColour.getBlue() / 255f, bgColour.getAlpha());
      vr.pos(-textWidth - 1, -6, 0.0).endVertex();
      vr.pos(-textWidth - 1, 4, 0.0).endVertex();
      vr.pos(textWidth + 1, 4, 0.0).endVertex();
      vr.pos(textWidth + 1, -6, 0.0).endVertex();
      tessellator.draw();

      // Draw text.
      GL.glEnableTexture2D();
      GL.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
      fontRenderer.drawString(text, -textWidth, -5, fgARGB);
      GL.glEnableDepthTest();
      GL.glDepthMask(true);
    }

    GL.glDisableBlend();
    GL.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    GL.glEnableTexture2D();
    GL.glEnableLighting();
    GL.glPopMatrix();
  } // drawBillboard

  // --------------------------------------------------------------------------
  /**
   * The annotation text to display.
   */
  protected String _text;

  /**
   * The x coordinate of the annotated block.
   */
  protected int    _x;

  /**
   * The y coordinate of the annotated block.
   */
  protected int    _y;

  /**
   * The z coordinate of the annotated block.
   */
  protected int    _z;
} // class Annotation
