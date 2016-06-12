package com.mrcrayfish.modelcreator;

import com.mrcrayfish.modelcreator.util.ResourceUtil;

import javax.swing.*;

public class Icons {
    public static Icon add;
    public static Icon add_rollover;
    public static Icon bin;
    public static Icon bin_open;
    public static Icon new_;
    public static Icon import_;
    public static Icon export;
    public static Icon texture;
    public static Icon clear;
    public static Icon copy;
    public static Icon clipboard;
    public static Icon transparent;
    public static Icon coin;
    public static Icon load;
    public static Icon disk;
    public static Icon exit;
    public static Icon cube;
    public static Icon light_on;
    public static Icon light_off;
    public static Icon arrow_up;
    public static Icon arrow_down;
    public static Icon facebook;
    public static Icon twitter;
    public static Icon reddit;
    public static Icon imgur;
    public static Icon planet_minecraft;
    public static Icon minecraft_forum;
    public static Icon github;
    public static Icon model_cauldron;
    public static Icon model_chair;

    public static void init() {
        cube = new ImageIcon(ResourceUtil.getResource("icons/cube.png"));
        bin = new ImageIcon(ResourceUtil.getResource("icons/bin.png"));
        bin_open = new ImageIcon(ResourceUtil.getResource("icons/bin_open.png"));
        add = new ImageIcon(ResourceUtil.getResource("icons/add.png"));
        add_rollover = new ImageIcon(ResourceUtil.getResource("icons/add_rollover.png"));
        new_ = new ImageIcon(ResourceUtil.getResource("icons/new.png"));
        import_ = new ImageIcon(ResourceUtil.getResource("icons/import.png"));
        export = new ImageIcon(ResourceUtil.getResource("icons/export.png"));
        texture = new ImageIcon(ResourceUtil.getResource("icons/texture.png"));
        clear = new ImageIcon(ResourceUtil.getResource("icons/clear.png"));
        copy = new ImageIcon(ResourceUtil.getResource("icons/copy.png"));
        clipboard = new ImageIcon(ResourceUtil.getResource("icons/clipboard.png"));
        transparent = new ImageIcon(ResourceUtil.getResource("icons/transparent.png"));
        coin = new ImageIcon(ResourceUtil.getResource("icons/coin.png"));
        load = new ImageIcon(ResourceUtil.getResource("icons/load.png"));
        disk = new ImageIcon(ResourceUtil.getResource("icons/disk.png"));
        exit = new ImageIcon(ResourceUtil.getResource("icons/exit.png"));

        light_on = new ImageIcon(ResourceUtil.getResource("icons/box_off.png"));
        light_off = new ImageIcon(ResourceUtil.getResource("icons/box_on.png"));

        arrow_up = new ImageIcon(ResourceUtil.getResource("icons/arrow_up.png"));
        arrow_down = new ImageIcon(ResourceUtil.getResource("icons/arrow_down.png"));

        facebook = new ImageIcon(ResourceUtil.getResource("icons/facebook.png"));
        twitter = new ImageIcon(ResourceUtil.getResource("icons/twitter.png"));
        reddit = new ImageIcon(ResourceUtil.getResource("icons/reddit.png"));
        imgur = new ImageIcon(ResourceUtil.getResource("icons/imgur.png"));
        planet_minecraft = new ImageIcon(ResourceUtil.getResource("icons/planet_minecraft.png"));
        minecraft_forum = new ImageIcon(ResourceUtil.getResource("icons/minecraft_forum.png"));
        github = new ImageIcon(ResourceUtil.getResource("icons/github.png"));

        model_cauldron = new ImageIcon(ResourceUtil.getResource("icons/model_cauldron.png"));
        model_chair = new ImageIcon(ResourceUtil.getResource("icons/model_chair.png"));
    }
}
