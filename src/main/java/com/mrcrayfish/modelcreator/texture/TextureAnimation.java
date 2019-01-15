package com.mrcrayfish.modelcreator.texture;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextureAnimation
{
    private int width = 16, height = 16;

    private List<Integer> frames = new ArrayList<>();
    private Map<Integer, Integer> customTimes = new HashMap<>();
    private int frametime = 1;
    private boolean interpolate = false;

    public void setSize(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setFrameTime(int frametime)
    {
        this.frametime = frametime;
    }

    public void setFrames(List<Integer> frameList)
    {
        frames = new ArrayList<>();
        frames.addAll(frameList);
    }

    public void setCustomTimes(Map<Integer, Integer> times)
    {
        customTimes = new HashMap<>();
        customTimes.putAll(times);
    }

    public void setInterpolate(boolean interpolate)
    {
        this.interpolate = interpolate;
    }

    public boolean isInterpolated()
    {
        return interpolate;
    }

    public int getFrameCount()
    {
        return frames.size();
    }

    public long getMaxTime()
    {
        long maxTime = 0;
        for(int i = 0; i < frames.size(); i++)
        {
            maxTime += getFrameTime(i);
        }
        return maxTime;
    }

    public int getCurrentAnimationFrame()
    {
        long maxTime = this.getMaxTime();
        if(maxTime > 0)
        {
            long animTime = System.currentTimeMillis() % maxTime;
            for(int i = 0; i < frames.size(); i++)
            {
                if(animTime < getFrameTime(i))
                {
                    return frames.get(i);
                }
                animTime -= getFrameTime(i);
            }
        }
        return 0;
    }

    public int getNextAnimationFrame()
    {
        if(frames.size() < 1)
        {
            return 0;
        }

        long maxTime = 0;
        for(int i = 0; i < frames.size(); i++)
        {
            maxTime += getFrameTime(i);
        }

        long animTime = System.currentTimeMillis() % maxTime;

        for(int i = 0; i < frames.size(); i++)
        {
            if(animTime <= getFrameTime(i))
            {
                if(i < frames.size() - 1)
                {
                    return frames.get(i + 1);
                }
                else
                {
                    return frames.get(0);
                }
            }
            animTime -= getFrameTime(i);
        }

        return 0;
    }

    public long getFrameTime(int frame)
    {
        if(customTimes != null && customTimes.containsKey(frame))
        {
            return customTimes.get(frame) * 50L;
        }
        return frametime * 50L;
    }

    public double getFrameInterpolation()
    {
        long maxTime = 0;
        for(int i = 0; i < frames.size(); i++)
        {
            maxTime += getFrameTime(i);
        }

        long animTime = System.currentTimeMillis() % maxTime;

        for(int i = 0; i < frames.size(); i++)
        {
            if(animTime <= getFrameTime(i))
            {
                return animTime / (double) getFrameTime(i);
            }
            animTime -= getFrameTime(i);
        }

        return 0;
    }

    public int getPasses()
    {
        if(isInterpolated() && getFrameCount() > 1)
        {
            return 2;
        }
        return 1;
    }

    public static TextureAnimation getAnimationForTexture(File file, int width, int height)
    {
        try
        {
            file = new File(file.getAbsolutePath() + ".mcmeta");
            if(file.exists())
            {
                TextureAnimation anim = null;
                JsonObject animationObj = null;
                JsonParser parser = new JsonParser();
                JsonElement read = parser.parse(new FileReader(file));

                if(read.isJsonObject())
                {
                    JsonObject mcMeta = read.getAsJsonObject();
                    if(mcMeta.has("animation") && mcMeta.get("animation").isJsonObject())
                    {
                        animationObj = mcMeta.get("animation").getAsJsonObject();
                        anim = new TextureAnimation();
                    }
                }

                if(anim != null && animationObj != null)
                {
                    int frametime = 1;
                    if(animationObj.has("frametime") && animationObj.get("frametime").isJsonPrimitive())
                    {
                        frametime = animationObj.get("frametime").getAsInt();
                    }
                    anim.setFrameTime(frametime);

                    if(animationObj.has("interpolate") && animationObj.get("interpolate").isJsonPrimitive())
                    {
                        boolean interpolate = animationObj.get("interpolate").getAsBoolean();
                        anim.setInterpolate(interpolate);
                    }

                    if(animationObj.has("frames") && animationObj.get("frames").isJsonArray())
                    {
                        JsonArray frames = animationObj.get("frames").getAsJsonArray();
                        if(frames.size() > 0)
                        {
                            List<Integer> frameList = new ArrayList<>();
                            Map<Integer, Integer> customTimes = new HashMap<>();

                            for(int i = 0; i < frames.size(); i++)
                            {
                                JsonElement frame = frames.get(i);

                                int index = 0;
                                int time = frametime;
                                if(frame.isJsonPrimitive())
                                {
                                    index = frame.getAsInt();
                                }
                                else if(frame.isJsonObject())
                                {
                                    JsonObject frameObj = frame.getAsJsonObject();

                                    if(frameObj.has("index") && frameObj.get("index").isJsonPrimitive())
                                    {
                                        index = frameObj.get("index").getAsInt();
                                    }
                                    if(frameObj.has("time") && frameObj.get("time").isJsonPrimitive())
                                    {
                                        time = frameObj.get("time").getAsInt();
                                    }
                                }

                                frameList.add(index);
                                if(time != frametime)
                                {
                                    customTimes.put(frameList.size() - 1, time);
                                }
                            }

                            anim.setFrames(frameList);
                            anim.setCustomTimes(customTimes);
                        }
                    }
                    else
                    {
                        int columns = width / 16;
                        List<Integer> frameList = new ArrayList<>();
                        for(int i = 0; i < (height / 16) * columns; i++)
                        {
                            frameList.add(i);
                        }
                        anim.setFrames(frameList);
                    }
                    return anim;
                }
            }
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
