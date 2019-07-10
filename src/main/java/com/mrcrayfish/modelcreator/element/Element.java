package com.mrcrayfish.modelcreator.element;

import com.mrcrayfish.modelcreator.ModelCreator;
import com.mrcrayfish.modelcreator.object.FaceDimension;
import com.mrcrayfish.modelcreator.sidebar.UVSidebar;
import com.mrcrayfish.modelcreator.texture.TextureEntry;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

import static org.lwjgl.opengl.GL11.*;

public class Element
{
    private String name = "Cube";
    private boolean visible = true;

    // Face Variables
    private int selectedFace = 0;
    private Face[] faces = new Face[6];

    // Element Variables
    private double startX = 0.0, startY = 0.0, startZ = 0.0;
    private double width = 16.0, height = 1.0, depth = 1.0;

    // Rotation Variables
    private double originX = 8, originY = 8, originZ = 8;
    private double rotation;
    private int axis = 0;
    private boolean rescale = false;
    private boolean snapOrigin = false;

    // Extra Variables
    private boolean shade = true;

    // Rotation Point Indicator
    private Sphere sphere = new Sphere();

    public Element(double width, double height, double depth)
    {
        this.width = width;
        this.height = height;
        this.depth = depth;
        initFaces();
        updateEndUVs();
    }

    public Element(Element cuboid)
    {
        this.name = cuboid.getName();
        this.width = cuboid.getWidth();
        this.height = cuboid.getHeight();
        this.depth = cuboid.getDepth();
        this.startX = cuboid.getStartX();
        this.startY = cuboid.getStartY();
        this.startZ = cuboid.getStartZ();
        this.originX = cuboid.getOriginX();
        this.originY = cuboid.getOriginY();
        this.originZ = cuboid.getOriginZ();
        this.rotation = cuboid.getRotation();
        this.axis = cuboid.getRotationAxis();
        this.rescale = cuboid.shouldRescale();
        this.snapOrigin = cuboid.shouldSnapOrigin();
        this.shade = cuboid.isShaded();
        this.selectedFace = cuboid.getSelectedFaceIndex();
        initFaces();
        for(int i = 0; i < faces.length; i++)
        {
            Face oldFace = cuboid.getAllFaces()[i];
            faces[i].fitTexture(oldFace.shouldFitTexture());
            faces[i].setTexture(oldFace.getTexture());
            faces[i].setStartU(oldFace.getStartU());
            faces[i].setStartV(oldFace.getStartV());
            faces[i].setEndU(oldFace.getEndU());
            faces[i].setEndV(oldFace.getEndV());
            faces[i].setRotation(oldFace.getRotation());
            faces[i].setCullface(oldFace.isCullfaced());
            faces[i].setEnabled(oldFace.isEnabled());
            faces[i].setAutoUVEnabled(oldFace.isAutoUVEnabled());
            faces[i].setTintIndexEnabled(oldFace.isTintIndexEnabled());
            faces[i].setTintIndex(oldFace.getTintIndex());
        }
        updateEndUVs();
    }

    private void initFaces()
    {
        for(int i = 0; i < faces.length; i++)
            faces[i] = new Face(this, i);
    }

    public void setSelectedFace(int face)
    {
        this.selectedFace = face;
    }

    public Face getSelectedFace()
    {
        return faces[selectedFace];
    }

    public int getSelectedFaceIndex()
    {
        return selectedFace;
    }

    public Face[] getAllFaces()
    {
        return faces;
    }

    public FaceDimension getFaceDimension(int side)
    {
        switch(side)
        {
            case 0:
                return new FaceDimension(getWidth(), getHeight());
            case 1:
                return new FaceDimension(getDepth(), getHeight());
            case 2:
                return new FaceDimension(getWidth(), getHeight());
            case 3:
                return new FaceDimension(getDepth(), getHeight());
            case 4:
                return new FaceDimension(getWidth(), getDepth());
            case 5:
                return new FaceDimension(getWidth(), getDepth());
        }
        return null;
    }

    public void setAllTextures(TextureEntry entry)
    {
        for(Face face : faces)
        {
            face.setTexture(entry);
        }
    }
    
    public void setAllAutoUV(boolean uv)
    {
        for(Face face : faces)
        {
            face.setAutoUVEnabled(uv);
        }
    }

    public void draw()
    {
        GL11.glPushMatrix();
        {
            GL11.glEnable(GL_BLEND);
            GL11.glEnable(GL_CULL_FACE);
            GL11.glTranslated(getOriginX(), getOriginY(), getOriginZ());
            rotateAxis();
            GL11.glTranslated(-getOriginX(), -getOriginY(), -getOriginZ());

            // North
            if(faces[0].isEnabled())
            {
                faces[0].renderNorth();
            }

            // East
            if(faces[1].isEnabled())
            {
                faces[1].renderEast();
            }

            // South
            if(faces[2].isEnabled())
            {
                faces[2].renderSouth();
            }

            // West
            if(faces[3].isEnabled())
            {
                faces[3].renderWest();
            }

            // Top
            if(faces[4].isEnabled())
            {
                faces[4].renderUp();
            }

            // Bottom
            if(faces[5].isEnabled())
            {
                faces[5].renderDown();
            }
        }
        GL11.glPopMatrix();
    }

    public void drawExtras(ElementManager manager)
    {
        if(manager.getSelectedElement() == this)
        {
            GL11.glPushMatrix();
            {
                GL11.glTranslated(getOriginX(), getOriginY(), getOriginZ());
                GL11.glColor3f(0.25F, 0.25F, 0.25F);
                sphere.draw(0.2F, 16, 16);
                rotateAxis();
                GL11.glLineWidth(2F);
                GL11.glBegin(GL_LINES);
                {
                    GL11.glColor3f(1, 0, 0);
                    GL11.glVertex3i(-4, 0, 0);
                    GL11.glVertex3i(4, 0, 0);
                    GL11.glColor3f(0, 1, 0);
                    GL11.glVertex3i(0, -4, 0);
                    GL11.glVertex3i(0, 4, 0);
                    GL11.glColor3f(0, 0, 1);
                    GL11.glVertex3i(0, 0, -4);
                    GL11.glVertex3i(0, 0, 4);
                }
                GL11.glEnd();
            }
            GL11.glPopMatrix();
        }
    }

    public void drawOutline()
    {
        GL11.glDepthMask(false);
        GL11.glPushMatrix();
        {
            GL11.glTranslated(getOriginX(), getOriginY(), getOriginZ());
            rotateAxis();
            GL11.glTranslated(-getOriginX(), -getOriginY(), -getOriginZ());
            GL11.glTranslated(getStartX(), getStartY(), getStartZ());

            float outlineScale = 1.01F;
            GL11.glScalef(outlineScale, outlineScale, outlineScale);
            GL11.glTranslated(-((getWidth() * outlineScale) - getWidth()) / 2.0, -((getHeight() * outlineScale) - getHeight()) / 2.0, -((getDepth() * outlineScale) - getDepth()) / 2.0);

            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            GL11.glLineWidth(3F);

            boolean grabbing = ((UVSidebar)ModelCreator.uvSidebar).isGrabbing();
            int hoveredFace = ((UVSidebar)ModelCreator.uvSidebar).getHoveredFace();
            if(hoveredFace == -1 && ModelCreator.isUVSidebarOpen)
            {
                hoveredFace = this.getSelectedFace().getSide();
            }

            /* Bottom */
            if(hoveredFace != Face.DOWN)
            {
                GL11.glBegin(GL_LINE_LOOP);
                {
                    GL11.glVertex3d(0, 0, 0);
                    GL11.glVertex3d(width, 0, 0);
                    GL11.glVertex3d(width, 0, depth);
                    GL11.glVertex3d(0, 0, depth);
                }
                GL11.glEnd();
            }

            /* Top */
            if(hoveredFace != Face.UP)
            {
                GL11.glBegin(GL_LINE_LOOP);
                {
                    GL11.glVertex3d(0, height, 0);
                    GL11.glVertex3d(width, height, 0);
                    GL11.glVertex3d(width, height, depth);
                    GL11.glVertex3d(0, height, depth);
                }
                GL11.glEnd();
            }

            /* North */
            if(hoveredFace != Face.NORTH)
            {
                GL11.glBegin(GL_LINE_LOOP);
                {
                    GL11.glVertex3d(0, 0, 0);
                    GL11.glVertex3d(0, height, 0);
                    GL11.glVertex3d(width, height, 0);
                    GL11.glVertex3d(width, 0, 0);
                }
                GL11.glEnd();
            }

            /* South */
            if(hoveredFace != Face.SOUTH)
            {
                GL11.glBegin(GL_LINE_LOOP);
                {
                    GL11.glVertex3d(0, 0, depth);
                    GL11.glVertex3d(0, height, depth);
                    GL11.glVertex3d(width, height, depth);
                    GL11.glVertex3d(width, 0, depth);
                }
                GL11.glEnd();
            }

            /* West */
            if(hoveredFace != Face.WEST)
            {
                GL11.glBegin(GL_LINE_LOOP);
                {
                    GL11.glVertex3d(0, 0, 0);
                    GL11.glVertex3d(0, 0, depth);
                    GL11.glVertex3d(0, height, depth);
                    GL11.glVertex3d(0, height, 0);
                }
                GL11.glEnd();
            }

            /* EAST */
            if(hoveredFace != Face.EAST)
            {
                GL11.glBegin(GL_LINE_LOOP);
                {
                    GL11.glVertex3d(width, 0, 0);
                    GL11.glVertex3d(width, 0, depth);
                    GL11.glVertex3d(width, height, depth);
                    GL11.glVertex3d(width, height, 0);
                }
                GL11.glEnd();
            }

            GL11.glColor3f(1.0F, 0.0F, 0.0F);
            GL11.glLineWidth(3F);

            /* Bottom */
            if(hoveredFace == Face.DOWN)
            {
                GL11.glBegin(GL_LINE_LOOP);
                {
                    GL11.glVertex3d(0, 0, 0);
                    GL11.glVertex3d(width, 0, 0);
                    GL11.glVertex3d(width, 0, depth);
                    GL11.glVertex3d(0, 0, depth);
                }
                GL11.glEnd();
            }

            /* Top */
            if(hoveredFace == Face.UP)
            {
                GL11.glBegin(GL_LINE_LOOP);
                {
                    GL11.glVertex3d(0, height, 0);
                    GL11.glVertex3d(width, height, 0);
                    GL11.glVertex3d(width, height, depth);
                    GL11.glVertex3d(0, height, depth);
                }
                GL11.glEnd();
            }

            /* North */
            if(hoveredFace == Face.NORTH)
            {
                GL11.glBegin(GL_LINE_LOOP);
                {
                    GL11.glVertex3d(0, 0, 0);
                    GL11.glVertex3d(0, height, 0);
                    GL11.glVertex3d(width, height, 0);
                    GL11.glVertex3d(width, 0, 0);
                }
                GL11.glEnd();
            }

            /* South */
            if(hoveredFace == Face.SOUTH)
            {
                GL11.glBegin(GL_LINE_LOOP);
                {
                    GL11.glVertex3d(0, 0, depth);
                    GL11.glVertex3d(0, height, depth);
                    GL11.glVertex3d(width, height, depth);
                    GL11.glVertex3d(width, 0, depth);
                }
                GL11.glEnd();
            }

            /* West */
            if(hoveredFace == Face.WEST)
            {
                GL11.glBegin(GL_LINE_LOOP);
                {
                    GL11.glVertex3d(0, 0, 0);
                    GL11.glVertex3d(0, 0, depth);
                    GL11.glVertex3d(0, height, depth);
                    GL11.glVertex3d(0, height, 0);
                }
                GL11.glEnd();
            }

            /* East */
            if(hoveredFace == Face.EAST)
            {
                GL11.glBegin(GL_LINE_LOOP);
                {
                    GL11.glVertex3d(width, 0, 0);
                    GL11.glVertex3d(width, 0, depth);
                    GL11.glVertex3d(width, height, depth);
                    GL11.glVertex3d(width, height, 0);
                }
                GL11.glEnd();
            }
        }
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
    }

    public void addStartX(double amt)
    {
        this.startX += amt;
    }

    public void addStartY(double amt)
    {
        this.startY += amt;
    }

    public void addStartZ(double amt)
    {
        this.startZ += amt;
    }

    public double getStartX()
    {
        return startX;
    }

    public double getStartY()
    {
        return startY;
    }

    public double getStartZ()
    {
        return startZ;
    }

    public void setStartX(double amt)
    {
        this.startX = amt;
    }

    public void setStartY(double amt)
    {
        this.startY = amt;
    }

    public void setStartZ(double amt)
    {
        this.startZ = amt;
    }

    public double getWidth()
    {
        return width;
    }

    public double getHeight()
    {
        return height;
    }

    public double getDepth()
    {
        return depth;
    }

    public void addWidth(double amt)
    {
        this.width += amt;
    }

    public void addHeight(double amt)
    {
        this.height += amt;
    }

    public void addDepth(double amt)
    {
        this.depth += amt;
    }

    public void setWidth(double width)
    {
        this.width = width;
    }

    public void setHeight(double height)
    {
        this.height = height;
    }

    public void setDepth(double depth)
    {
        this.depth = depth;
    }

    public double getOriginX()
    {
        return originX;
    }

    public double getOriginY()
    {
        return originY;
    }

    public double getOriginZ()
    {
        return originZ;
    }

    public void addOriginX(double amt)
    {
        this.originX += amt;
    }

    public void addOriginY(double amt)
    {
        this.originY += amt;
    }

    public void addOriginZ(double amt)
    {
        this.originZ += amt;
    }

    public void setOriginX(double amt)
    {
        this.originX = amt;
    }

    public void setOriginY(double amt)
    {
        this.originY = amt;
    }

    public void setOriginZ(double amt)
    {
        this.originZ = amt;
    }

    public double getRotation()
    {
        return rotation;
    }

    public void setRotation(double rotation)
    {
        this.rotation = rotation;
    }

    public int getRotationAxis()
    {
        return axis;
    }

    public void setRotationAxis(int axis)
    {
        this.axis = axis;
    }

    public void setRescale(boolean rescale)
    {
        this.rescale = rescale;
    }
    
    public void setSnapOrigin(boolean snapOrigin)
    {
        this.snapOrigin = snapOrigin;
    }

    public boolean shouldRescale()
    {
        return rescale;
    }
    
    public boolean shouldSnapOrigin()
    {
        return snapOrigin;
    }

    public boolean isShaded()
    {
        return shade;
    }

    public void setShade(boolean shade)
    {
        this.shade = shade;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return name + "(w:" + width + ",h:" + height + ",d:" + depth + ")";
    }

    public void updateStartUVs()
    {
        for(Face face : faces)
        {
            face.updateStartUV();
        }
    }

    public void updateEndUVs()
    {
        for(Face face : faces)
        {
            face.updateEndUV();
        }
    }
    
    public void updateOrigin()
    {
        if(snapOrigin)
        {
            originX = startX;
            originY = startY;
            originZ = startZ;
        }
    }
    
    public void setOrigin()
    {
        originX = startX;
        originY = startY;
        originZ = startZ;
    }

    private void rotateAxis()
    {
        switch(axis)
        {
            case 0:
                GL11.glRotated(getRotation(), 1, 0, 0);
                break;
            case 1:
                GL11.glRotated(getRotation(), 0, 1, 0);
                break;
            case 2:
                GL11.glRotated(getRotation(), 0, 0, 1);
                break;
        }
    }

    public static String parseAxis(int axis)
    {
        switch(axis)
        {
            case 0:
                return "x";
            case 1:
                return "y";
            case 2:
                return "z";
        }
        return "x";
    }

    public static int parseAxisString(String axis)
    {
        switch(axis)
        {
            case "x":
                return 0;
            case "y":
                return 1;
            case "z":
                return 2;
        }
        return 0;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }
}
