package watson.model;

import com.mumfrey.liteloader.gl.GL;
import net.minecraft.client.renderer.Tessellator;

import net.minecraft.client.renderer.VertexBuffer;
import org.lwjgl.opengl.GL11;

import watson.db.BlockType;

// --------------------------------------------------------------------------
/**
 * Render a wireframe model of a stair.
 */
public class StairBlockModel extends BlockModel
{
  // --------------------------------------------------------------------------
  /**
   * Default constructor.
   */
  public StairBlockModel()
  {
    super("stair");
  }

  // --------------------------------------------------------------------------
  /**
   * @see watson.model.BlockModel#render(watson.db.BlockType, int, int, int)
   */
  @Override
  public void render(BlockType blockType, int x, int y, int z)
  {
    Tessellator tess = Tessellator.getInstance();
      VertexBuffer vr = tess.getBuffer();

    // Opposite corners.
    double x1 = x + blockType.getX1();
    double y1 = y + blockType.getY1();
    double z1 = z + blockType.getZ1();
    double x2 = x + blockType.getX2();
    double y2 = y + blockType.getY2();
    double z2 = z + blockType.getZ2();

    // Concave corner, mid-stair.
    double yMid = y + 0.5 * (blockType.getY1() + blockType.getY2());
    double zMid = z + 0.5 * (blockType.getZ1() + blockType.getZ2());

    // x1 side.
      vr.begin(GL.GL_LINE_LOOP, GL.VF_POSITION);
      vr.color(blockType.getARGB().getRed(),
      blockType.getARGB().getGreen(), blockType.getARGB().getBlue(),
      blockType.getARGB().getAlpha());
    GL11.glLineWidth(blockType.getLineWidth());
      vr.pos(x1, y1, z1);
      vr.pos(x1, y1, z2);
      vr.pos(x1, y2, z2);
      vr.pos(x1, y2, zMid);
      vr.pos(x1, yMid, zMid);
      vr.pos(x1, yMid, z1);
    tess.draw();

    // x2 side.
    vr.begin(GL.GL_LINE_LOOP, GL.VF_POSITION);
      vr.color(blockType.getARGB().getRed(),
      blockType.getARGB().getGreen(), blockType.getARGB().getBlue(),
      blockType.getARGB().getAlpha());
    GL11.glLineWidth(blockType.getLineWidth());
      vr.pos(x2, y1, z1);
      vr.pos(x2, y1, z2);
      vr.pos(x2, y2, z2);
      vr.pos(x2, y2, zMid);
      vr.pos(x2, yMid, zMid);
      vr.pos(x2, yMid, z1);
    tess.draw();

    // Horizontal lines joining the two sides.
    vr.begin(GL.GL_LINES, GL.VF_POSITION);
      vr.color(blockType.getARGB().getRed(),
      blockType.getARGB().getGreen(), blockType.getARGB().getBlue(),
      blockType.getARGB().getAlpha());
    GL11.glLineWidth(blockType.getLineWidth());

      vr.pos(x1, y1, z1);
      vr.pos(x2, y1, z1);

      vr.pos(x1, y1, z2);
      vr.pos(x2, y1, z2);

      vr.pos(x1, y2, z2);
      vr.pos(x2, y2, z2);

      vr.pos(x1, y2, zMid);
      vr.pos(x2, y2, zMid);

      vr.pos(x1, yMid, zMid);
      vr.pos(x2, yMid, zMid);

      vr.pos(x1, yMid, z1);
      vr.pos(x2, yMid, z1);
    tess.draw();
  } // render
} // class StairBlockModel