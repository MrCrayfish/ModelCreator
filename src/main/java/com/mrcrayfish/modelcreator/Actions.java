package com.mrcrayfish.modelcreator;

import com.mrcrayfish.modelcreator.element.Element;
import com.mrcrayfish.modelcreator.element.ElementManager;
import com.mrcrayfish.modelcreator.element.Face;

/**
 * Author: MrCrayfish
 */
public class Actions
{
    public static void rotateModel(ElementManager manager, boolean clockwise)
    {
        manager.getAllElements().forEach(element -> rotateElement(element, clockwise));
        manager.updateValues();
        StateManager.pushState(manager);
    }

    private static void rotateElement(Element element, boolean clockwise)
    {
        /* Calculates and sets the new starting x and y position of the element */
        double newX;
        double newZ;
        if(clockwise)
        {
            newX = element.getStartX() - 8 > 0 ? 16 - (element.getDepth() + element.getStartZ()) : 16 - element.getDepth() - element.getStartZ();
            newZ = element.getStartX();
        }
        else
        {
            newX = element.getStartZ();
            newZ = element.getStartZ() - 8 > 0 ? 16 - (element.getWidth() + element.getStartX()) : 16 - element.getWidth() - element.getStartX();
        }
        element.setStartX(newX);
        element.setStartZ(newZ);

        /* Swaps the width and depth of the element */
        double width = element.getWidth();
        element.setWidth(element.getDepth());
        element.setDepth(width);

        /* Shifts the UVs of horizontal faces to the next target face */
        Face[] faces = element.getAllFaces();
        Face tempFace = new Face(faces[clockwise ? 3 : 0]);
        if(clockwise)
        {
            for(int i = 3; i >= 1; i--)
            {
                faces[i].copyProperties(faces[i - 1]);
            }
        }
        else
        {
            for(int i = 0; i < 3; i++)
            {
                faces[i].copyProperties(faces[i + 1]);
            }
        }
        faces[clockwise ? 0 : 3].copyProperties(tempFace);

        /* Rotates the textures on the top so they match the original when rotated */
        faces[Face.UP].setRotation(faces[Face.UP].getRotation() + (clockwise ? 1 : -1));
        faces[Face.DOWN].setRotation(faces[Face.DOWN].getRotation() + (clockwise ? 1 : -1));

        /* Rotates the rotation axis. This only applies to horizontal axis */
        if(element.getRotationAxis() == 0)
        {
            element.setRotationAxis(2);
            if(!clockwise)
            {
                element.setRotation(-element.getRotation());
            }
        }
        else if(element.getRotationAxis() == 2)
        {
            element.setRotationAxis(0);
            if(clockwise)
            {
                element.setRotation(-element.getRotation());
            }
        }

        /* Rotates the origin starting x and y */
        double newOriginX;
        double newOriginZ;
        if(clockwise)
        {
            newOriginX = element.getOriginX() - 8 > 0 ? 16 - element.getOriginZ() : 16 - element.getOriginZ();
            newOriginZ = element.getOriginX();
        }
        else
        {
            newOriginX = element.getOriginZ();
            newOriginZ = element.getOriginZ() - 8 > 0 ? 16 - element.getOriginX() : 16 - element.getOriginX();
        }
        element.setOriginX(newOriginX);
        element.setOriginZ(newOriginZ);
    }
}
