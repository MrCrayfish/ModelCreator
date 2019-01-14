package com.mrcrayfish.modelcreator.element;

import com.mrcrayfish.modelcreator.Settings;
import com.mrcrayfish.modelcreator.texture.TextureEntry;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Face
{
    private static int[] colors;

    static
    {
        colors = Settings.getFaceColors();
    }

    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;
    public static final int UP = 4;
    public static final int DOWN = 5;

    private TextureEntry texture = null;
    private double textureU = 0;
    private double textureV = 0;
    private double textureUEnd = 16;
    private double textureVEnd = 16;
    private boolean fitTexture = false;
    private boolean binded = false;
    private boolean cullface = false;
    private boolean enabled = true;
    private boolean autoUV = true;
    private int rotation;

    private Element cuboid;
    private int side;

    public Face(Element cuboid, int side)
    {
        this.cuboid = cuboid;
        this.side = side;
    }

    public Face(Face face)
    {
        this.copyProperties(face);
    }

    public void copyProperties(Face face)
    {
        this.fitTexture = face.fitTexture;
        this.texture = face.texture;
        this.textureU = face.textureU;
        this.textureV = face.textureV;
        this.textureUEnd = face.textureUEnd;
        this.textureVEnd = face.textureVEnd;
        this.rotation = face.rotation;
        this.cullface = face.cullface;
        this.enabled = face.enabled;
        this.autoUV = face.autoUV;
    }

    public void renderNorth()
    {
        int passes = texture != null ? texture.getPasses() : 1;
        for(int i = 0; i < passes; i++)
        {
            renderNorth(i, GL11.GL_QUADS);
        }
    }

    private void renderNorth(int pass, int mode)
    {
        GL11.glPushMatrix();
        {
            startRender(pass);
            applyShade(0.45F);

            GL11.glBegin(mode);
            {
                if(binded)
                {
                    setTexCoord(0);
                }
                GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY(), cuboid.getStartZ());

                if(binded)
                {
                    setTexCoord(1);
                }
                GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY(), cuboid.getStartZ());

                if(binded)
                {
                    setTexCoord(2);
                }
                GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ());

                if(binded)
                {
                    setTexCoord(3);
                }
                GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ());
            }
            GL11.glEnd();

            finishRender();
        }
        GL11.glPopMatrix();
    }

    public void renderEast()
    {
        int passes = texture != null ? texture.getPasses() : 1;
        for(int i = 0; i < passes; i++)
        {
            renderEast(i, GL11.GL_QUADS);
        }
    }

    private void renderEast(int pass, int mode)
    {
        GL11.glPushMatrix();
        {
            startRender(pass);
            applyShade(0.3F);

            GL11.glBegin(mode);
            {
                if(binded)
                {
                    setTexCoord(0);
                }
                GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY(), cuboid.getStartZ() + cuboid.getDepth());

                if(binded)
                {
                    setTexCoord(1);
                }
                GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY(), cuboid.getStartZ());

                if(binded)
                {
                    setTexCoord(2);
                }
                GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ());

                if(binded)
                {
                    setTexCoord(3);
                }
                GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ() + cuboid.getDepth());
            }
            GL11.glEnd();

            finishRender();
        }
        GL11.glPopMatrix();
    }

    public void renderSouth()
    {
        int passes = texture != null ? texture.getPasses() : 1;
        for(int i = 0; i < passes; i++)
        {
            renderSouth(i, GL11.GL_QUADS);
        }
    }

    private void renderSouth(int pass, int mode)
    {
        GL11.glPushMatrix();
        {
            startRender(pass);
            applyShade(0.15F);

            GL11.glBegin(mode);
            {
                if(binded)
                {
                    setTexCoord(0);
                }
                GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY(), cuboid.getStartZ() + cuboid.getDepth());

                if(binded)
                {
                    setTexCoord(1);
                }
                GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY(), cuboid.getStartZ() + cuboid.getDepth());

                if(binded)
                {
                    setTexCoord(2);
                }
                GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ() + cuboid.getDepth());

                if(binded)
                {
                    setTexCoord(3);
                }
                GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ() + cuboid.getDepth());
            }
            GL11.glEnd();

            finishRender();
        }
        GL11.glPopMatrix();
    }

    public void renderWest()
    {
        int passes = texture != null ? texture.getPasses() : 1;
        for(int i = 0; i < passes; i++)
        {
            renderWest(i, GL11.GL_QUADS);
        }
    }

    private void renderWest(int pass, int mode)
    {
        GL11.glPushMatrix();
        {
            startRender(pass);
            applyShade(0.3F);

            GL11.glBegin(mode);
            {
                if(binded)
                {
                    setTexCoord(0);
                }
                GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY(), cuboid.getStartZ());

                if(binded)
                {
                    setTexCoord(1);
                }
                GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY(), cuboid.getStartZ() + cuboid.getDepth());

                if(binded)
                {
                    setTexCoord(2);
                }
                GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ() + cuboid.getDepth());

                if(binded)
                {
                    setTexCoord(3);
                }
                GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ());
            }
            GL11.glEnd();

            finishRender();
        }
        GL11.glPopMatrix();
    }

    public void renderUp()
    {
        int passes = texture != null ? texture.getPasses() : 1;
        for(int i = 0; i < passes; i++)
        {
            renderUp(i, GL11.GL_QUADS);
        }
    }

    private void renderUp(int pass, int mode)
    {
        GL11.glPushMatrix();
        {
            startRender(pass);

            GL11.glBegin(mode);
            {
                if(binded)
                {
                    setTexCoord(0);
                }
                GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ() + cuboid.getDepth());

                if(binded)
                {
                    setTexCoord(1);
                }
                GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ() + cuboid.getDepth());

                if(binded)
                {
                    setTexCoord(2);
                }
                GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ());

                if(binded)
                {
                    setTexCoord(3);
                }
                GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY() + cuboid.getHeight(), cuboid.getStartZ());
            }
            GL11.glEnd();

            finishRender();
        }
        GL11.glPopMatrix();
    }

    public void renderDown()
    {
        int passes = texture != null ? texture.getPasses() : 1;
        for(int i = 0; i < passes; i++)
        {
            renderDown(i, GL11.GL_QUADS);
        }
    }

    private void renderDown(int pass, int mode)
    {
        GL11.glPushMatrix();
        {
            startRender(pass);
            applyShade(0.55F);

            GL11.glBegin(mode);
            {
                if(binded)
                {
                    setTexCoord(0);
                }
                GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY(), cuboid.getStartZ());

                if(binded)
                {
                    setTexCoord(1);
                }
                GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY(), cuboid.getStartZ());

                if(binded)
                {
                    setTexCoord(2);
                }
                GL11.glVertex3d(cuboid.getStartX() + cuboid.getWidth(), cuboid.getStartY(), cuboid.getStartZ() + cuboid.getDepth());

                if(binded)
                {
                    setTexCoord(3);
                }
                GL11.glVertex3d(cuboid.getStartX(), cuboid.getStartY(), cuboid.getStartZ() + cuboid.getDepth());
            }
            GL11.glEnd();

            finishRender();
        }
        GL11.glPopMatrix();
    }

    private void setTexCoord(int corner)
    {
        setTexCoord(corner, false);
    }

    private void setTexCoord(int corner, boolean forceFit)
    {
        int coord = corner + rotation;
        if(coord == 0 | coord == 4)
        {
            GL11.glTexCoord2d(fitTexture || forceFit ? 0 : (textureU / 16), fitTexture || forceFit ? 1 : (textureVEnd / 16));
        }
        if(coord == 1 | coord == 5)
        {
            GL11.glTexCoord2d(fitTexture || forceFit ? 1 : (textureUEnd / 16), fitTexture || forceFit ? 1 : (textureVEnd / 16));
        }
        if(coord == 2 | coord == 6)
        {
            GL11.glTexCoord2d(fitTexture || forceFit ? 1 : (textureUEnd / 16), fitTexture || forceFit ? 0 : (textureV / 16));
        }
        if(coord == 3)
        {
            GL11.glTexCoord2d(fitTexture || forceFit ? 0 : (textureU / 16), fitTexture || forceFit ? 0 : (textureV / 16));
        }
    }

    private void startRender(int pass)
    {
        int color = Face.colors[side];
        float b = (float) (color & 0xFF) / 0xFF;
        float g = (float) ((color >>> 8) & 0xFF) / 0xFF;
        float r = (float) ((color >>> 16) & 0xFF) / 0xFF;
        GL11.glColor3f(r, g, b);
        GL11.glEnable(GL_TEXTURE_2D);
        bindTexture(pass);
    }

    private void finishRender()
    {
        GL11.glDisable(GL_TEXTURE_2D);
    }

    private void applyShade(float reduction)
    {
        if(cuboid.isShaded())
        {
            FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
            GL11.glGetFloat(GL11.GL_CURRENT_COLOR, buffer);
            GL11.glColor3f(buffer.get() - reduction, buffer.get() - reduction, buffer.get() - reduction);
        }
    }

    public void setTexture(TextureEntry texture)
    {
        this.texture = texture;
    }

    public void bindTexture(int pass)
    {
        GL11.glDisable(GL_TEXTURE_2D);
        if(texture != null)
        {
            if(pass == 0)
            {
                GL11.glEnable(GL_TEXTURE_2D);
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
                texture.bindTexture();
            }
            else if(pass == 1)
            {
                if(texture.isAnimated())
                {
                    GL11.glEnable(GL_TEXTURE_2D);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glDepthFunc(GL11.GL_EQUAL);

                    texture.bindNextTexture();
                    GL11.glColor4d(1.0D, 1.0D, 1.0D, texture.getAnimation().getFrameInterpolation());
                }
            }

            if(texture.hasProperties() && texture.getProperties().isBlurred())
            {
                GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            }

            binded = true;
        }
    }

    public void moveTextureU(double amt)
    {
        this.textureU += amt;
        this.textureUEnd += amt;
    }

    public void moveTextureV(double amt)
    {
        this.textureV += amt;
        this.textureVEnd += amt;
    }

    private double getMinX()
    {
        if(side == EAST)
        {
            return cuboid.getStartX() + cuboid.getWidth();
        }
        return cuboid.getStartX();
    }

    private double getMinY()
    {
        if(side == UP)
        {
            return cuboid.getStartY() + cuboid.getHeight();
        }
        return cuboid.getStartY();
    }

    private double getMinZ()
    {
        if(side == SOUTH)
        {
            return cuboid.getStartZ() + cuboid.getDepth();
        }
        return cuboid.getStartZ();
    }

    private double getMaxX()
    {
        if(side == WEST)
        {
            return cuboid.getStartX();
        }
        return cuboid.getStartX() + cuboid.getWidth();
    }

    private double getMaxY()
    {
        if(side == DOWN)
        {
            return cuboid.getStartY();
        }
        return cuboid.getStartY() + cuboid.getHeight();
    }

    private double getMaxZ()
    {
        if(side == NORTH)
        {
            return cuboid.getStartZ();
        }
        return cuboid.getStartZ() + cuboid.getDepth();
    }

    public void addTextureX(double amt)
    {
        this.textureU += amt;
    }

    public void addTextureY(double amt)
    {
        this.textureV += amt;
    }

    public void addTextureXEnd(double amt)
    {
        this.textureUEnd += amt;
    }

    public void addTextureYEnd(double amt)
    {
        this.textureVEnd += amt;
    }

    public double getStartU()
    {
        return textureU;
    }

    public double getStartV()
    {
        return textureV;
    }

    public double getEndU()
    {
        return textureUEnd;
    }

    public double getEndV()
    {
        return textureVEnd;
    }

    public void setStartU(double u)
    {
        textureU = u;
    }

    public void setStartV(double v)
    {
        textureV = v;
    }

    public void setEndU(double ue)
    {
        textureUEnd = ue;
    }

    public void setEndV(double ve)
    {
        textureVEnd = ve;
    }

    public TextureEntry getTexture()
    {
        return texture;
    }

    public void fitTexture(boolean fitTexture)
    {
        this.fitTexture = fitTexture;
    }

    public boolean shouldFitTexture()
    {
        return fitTexture;
    }

    public int getSide()
    {
        return side;
    }

    public boolean isCullfaced()
    {
        return cullface;
    }

    public void setCullface(boolean cullface)
    {
        this.cullface = cullface;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public boolean isAutoUVEnabled()
    {
        return autoUV;
    }

    public void setAutoUVEnabled(boolean enabled)
    {
        this.autoUV = enabled;
    }

    public boolean isBinded()
    {
        return binded;
    }

    public void updateStartUV()
    {
        if(autoUV)
        {
            textureU = Math.max(0.0, textureUEnd - cuboid.getFaceDimension(side).getWidth());
            textureV = Math.max(0.0, textureVEnd - cuboid.getFaceDimension(side).getHeight());
        }
    }

    public void updateEndUV()
    {
        if(autoUV)
        {
            textureUEnd = Math.min(16.0, textureU + cuboid.getFaceDimension(side).getWidth());
            textureVEnd = Math.min(16.0, textureV + cuboid.getFaceDimension(side).getHeight());
        }
    }

    public static String getFaceName(int face)
    {
        switch(face)
        {
            case 0:
                return "north";
            case 1:
                return "east";
            case 2:
                return "south";
            case 3:
                return "west";
            case 4:
                return "up";
            case 5:
                return "down";
        }
        return null;
    }

    public static int getFaceSide(String name)
    {
        switch(name)
        {
            case "north":
                return 0;
            case "east":
                return 1;
            case "south":
                return 2;
            case "west":
                return 3;
            case "up":
                return 4;
            case "down":
                return 5;
        }
        return -1;
    }

    public static int getFaceColour(int side)
    {
        if(side >= 0 && side < colors.length)
        {
            return colors[side];
        }
        return 0;
    }

    public static int[] getFaceColors()
    {
        return colors;
    }

    public static void setFaceColors(int[] colors)
    {
        if(colors.length == 6)
        {
            Face.colors = colors;
        }
    }

    public static void setFaceColor(int side, int color)
    {
        Face.colors[side] = color;
    }

    public int getRotation()
    {
        return rotation;
    }

    public void setRotation(int rotation)
    {
        this.rotation = rotation;
    }

    public boolean isVisible(ElementManager manager)
    {
        if(cuboid.getRotation() != 0.0) //TODO make it an option
        {
            return true;
        }

        for(Element element : manager.getAllElements())
        {
            if(element == cuboid || element.getRotation() != 0.0)
            {
                continue;
            }

            if(this.getMinX() >= element.getStartX() && this.getMinX() <= element.getStartX() + element.getWidth())
            {
                if(this.getMinY() >= element.getStartY() && this.getMinY() <= element.getStartY() + element.getHeight())
                {
                    if(this.getMinZ() >= element.getStartZ() && this.getMinZ() <= element.getStartZ() + element.getDepth())
                    {
                        if(this.getMaxX() >= element.getStartX() && this.getMaxX() <= element.getStartX() + element.getWidth())
                        {
                            if(this.getMaxY() >= element.getStartY() && this.getMaxY() <= element.getStartY() + element.getHeight())
                            {
                                if(this.getMaxZ() >= element.getStartZ() && this.getMaxZ() <= element.getStartZ() + element.getDepth())
                                {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
