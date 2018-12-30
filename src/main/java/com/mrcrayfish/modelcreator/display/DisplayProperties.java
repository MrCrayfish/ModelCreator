package com.mrcrayfish.modelcreator.display;

import com.mrcrayfish.modelcreator.display.render.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class DisplayProperties
{
    public static final Map<String, CanvasRenderer> RENDER_MAP = new HashMap<>();
    public static final DisplayProperties MODEL_CREATOR_BLOCK;
    public static final DisplayProperties DEFAULT_BLOCK;
    public static final DisplayProperties DEFAULT_ITEM;

    static
    {
        MODEL_CREATOR_BLOCK = new DisplayProperties("Model Creator Block", true);
        MODEL_CREATOR_BLOCK.add("gui", 30, 45, 0, 0, 0, 0, 0.625, 0.625, 0.625);
        MODEL_CREATOR_BLOCK.add("ground", 0, 0, 0, 0, 3, 0, 0.25, 0.25, 0.25);
        MODEL_CREATOR_BLOCK.add("fixed", 0, 0, 0, 0, 0, 0, 1, 1, 1);
        MODEL_CREATOR_BLOCK.add("head", 0, 0, 0, 0, 0, 0, 1, 1, 1);
        MODEL_CREATOR_BLOCK.add("firstperson_righthand", 0, 315, 0, 0, 2.5, 0, 0.4, 0.4, 0.4);
        MODEL_CREATOR_BLOCK.add("firstperson_lefthand", 0, 0, 45, 0, 2.5, 0, 0.4, 0.4, 0.4);
        MODEL_CREATOR_BLOCK.add("thirdperson_righthand", 75, 315, 0, 0, 2.5, 0, 0.375, 0.375, 0.375);
        MODEL_CREATOR_BLOCK.add("thirdperson_lefthand", 75, 45, 0, 0, 2.5, 0, 0.375, 0.375, 0.375);

        DEFAULT_BLOCK = new DisplayProperties("Default Block", true);
        DEFAULT_BLOCK.add("gui", 30, 225, 0, 0, 0, 0, 0.625, 0.625, 0.625);
        DEFAULT_BLOCK.add("ground", 0, 0, 0, 0, 3, 0, 0.25, 0.25, 0.25);
        DEFAULT_BLOCK.add("fixed", 0, 0, 0, 0, 0, 0, 0.5, 0.5, 0.5);
        DEFAULT_BLOCK.add("head", 0, 0, 0, 0, 0, 0, 1, 1, 1);
        DEFAULT_BLOCK.add("firstperson_righthand", 0, 45, 0, 0, 2.5, 0, 0.4, 0.4, 0.4);
        DEFAULT_BLOCK.add("firstperson_lefthand", 0, 0, 225, 0, 2.5, 0, 0.4, 0.4, 0.4);
        DEFAULT_BLOCK.add("thirdperson_righthand", 75, 45, 0, 0, 2.5, 0, 0.375, 0.375, 0.375);
        DEFAULT_BLOCK.add("thirdperson_lefthand", 75, 225, 0, 0, 2.5, 0, 0.375, 0.375, 0.375);

        DEFAULT_ITEM = new DisplayProperties("Default Item", true);
        DEFAULT_ITEM.add("gui", 0, 0, 0, 0, 0, 0, 1, 1, 1);
        DEFAULT_ITEM.add("ground", 0, 0, 0, 0, 2, 0, 0.5, 0.5, 0.5);
        DEFAULT_ITEM.add("fixed", 0, 180, 0, 0, 0, 0, 1, 1, 1);
        DEFAULT_ITEM.add("head", 0, 180, 0, 0, 13, 7, 1, 1, 1);
        DEFAULT_ITEM.add("firstperson_righthand", 0, -90, 25, 1.13, 3.2, 1.13, 0.68, 0.68, 0.68);
        DEFAULT_ITEM.add("firstperson_lefthand", 0, -90, 25, 1.13, 3.2, 1.13, 0.68, 0.68, 0.68);
        DEFAULT_ITEM.add("thirdperson_righthand", 0, 0, 0, 0, 3, 1, 0.55, 0.55, 0.55);
        DEFAULT_ITEM.add("thirdperson_lefthand", 0, 0, 0, 0, 3, 1, 0.55, 0.55, 0.55);

        RENDER_MAP.put("head", new HeadPropertyRenderer());
        RENDER_MAP.put("gui", new GuiPropertyRenderer());
        RENDER_MAP.put("ground", new GroundPropertyRenderer());
        RENDER_MAP.put("fixed", new FixedPropertyRenderer());
        RENDER_MAP.put("firstperson_righthand", new FirstPersonPropertyRenderer());
        RENDER_MAP.put("thirdperson_righthand", new ThirdPersonPropertyRenderer());

    }

    private Map<String, Entry> entries = new HashMap<>();
    private String name;
    private boolean isDefault = false;

    private DisplayProperties(String name, boolean isDefault)
    {
        this.name = name;
        this.isDefault = isDefault;
    }

    public DisplayProperties(DisplayProperties properties)
    {
        this.name = properties.name;
        properties.entries.forEach((id, entry) -> entries.put(id, new Entry(entry)));
    }

    public DisplayProperties(String name, DisplayProperties properties)
    {
        this.name = name;
        properties.entries.forEach((id, entry) -> entries.put(id, new Entry(entry)));
    }

    public void add(String id, double rotationX, double rotationY, double rotationZ, double translationX, double translationY, double translationZ, double scaleX, double scaleY, double scaleZ)
    {
        entries.put(id, new Entry(id, rotationX, rotationY, rotationZ, translationX, translationY, translationZ, scaleX, scaleY, scaleZ));
    }

    public String getName()
    {
        return name;
    }

    public boolean isDefault()
    {
        return isDefault;
    }

    public Entry getEntry(String id)
    {
        return entries.get(id);
    }

    public Map<String, Entry> getEntries()
    {
        return entries;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public static class Entry
    {
        private boolean enabled = true;
        private String id;
        private double rotationX, rotationY, rotationZ;
        private double translationX, translationY, translationZ;
        private double scaleX, scaleY, scaleZ;

        public Entry(String id, double rotationX, double rotationY, double rotationZ, double translationX, double translationY, double translationZ, double scaleX, double scaleY, double scaleZ)
        {
            this.id = id;
            this.rotationX = rotationX;
            this.rotationY = rotationY;
            this.rotationZ = rotationZ;
            this.translationX = translationX;
            this.translationY = translationY;
            this.translationZ = translationZ;
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.scaleZ = scaleZ;
        }

        public Entry(Entry entry)
        {
            this.id = entry.id;
            this.rotationX = entry.rotationX;
            this.rotationY = entry.rotationY;
            this.rotationZ = entry.rotationZ;
            this.translationX = entry.translationX;
            this.translationY = entry.translationY;
            this.translationZ = entry.translationZ;
            this.scaleX = entry.scaleX;
            this.scaleY = entry.scaleY;
            this.scaleZ = entry.scaleZ;
        }

        public void setEnabled(boolean enabled)
        {
            this.enabled = enabled;
        }

        public boolean isEnabled()
        {
            return enabled;
        }

        public String getId()
        {
            return id;
        }

        public double getRotationX()
        {
            return rotationX;
        }

        public void setRotationX(double rotationX)
        {
            this.rotationX = rotationX;
        }

        public double getRotationY()
        {
            return rotationY;
        }

        public void setRotationY(double rotationY)
        {
            this.rotationY = rotationY;
        }

        public double getRotationZ()
        {
            return rotationZ;
        }

        public void setRotationZ(double rotationZ)
        {
            this.rotationZ = rotationZ;
        }

        public double getTranslationX()
        {
            return translationX;
        }

        public void setTranslationX(double translationX)
        {
            this.translationX = translationX;
        }

        public double getTranslationY()
        {
            return translationY;
        }

        public void setTranslationY(double translationY)
        {
            this.translationY = translationY;
        }

        public double getTranslationZ()
        {
            return translationZ;
        }

        public void setTranslationZ(double translationZ)
        {
            this.translationZ = translationZ;
        }

        public double getScaleX()
        {
            return scaleX;
        }

        public void setScaleX(double scaleX)
        {
            this.scaleX = scaleX;
        }

        public double getScaleY()
        {
            return scaleY;
        }

        public void setScaleY(double scaleY)
        {
            this.scaleY = scaleY;
        }

        public double getScaleZ()
        {
            return scaleZ;
        }

        public void setScaleZ(double scaleZ)
        {
            this.scaleZ = scaleZ;
        }
    }
}
