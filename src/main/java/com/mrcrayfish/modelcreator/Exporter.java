package com.mrcrayfish.modelcreator;

import com.mrcrayfish.modelcreator.element.ElementManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public abstract class Exporter
{
    /**
     * decimalformatter for rounding
     */
    public static final DecimalFormat FORMAT = new DecimalFormat("#.###");
    private static final DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols();

    static
    {
        SYMBOLS.setDecimalSeparator('.');
        FORMAT.setDecimalFormatSymbols(SYMBOLS);
    }

    // Model Variables
    protected ElementManager manager;

    public Exporter(ElementManager manager)
    {
        this.manager = manager;
    }

    public File export(File file)
    {
        File path = file.getParentFile();
        if(path.exists() && path.isDirectory())
        {
            this.writeFile(file);
        }
        return file;
    }

    protected abstract void write(BufferedWriter writer) throws IOException;

    public File writeFile(File file)
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file)))
        {
            if(!file.exists())
            {
                file.createNewFile();
            }
            this.write(writer);
            return file;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    protected String space(int size)
    {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < size; i++)
        {
            //TODO add setting to export with tabs instead
            builder.append("    ");
        }
        return builder.toString();
    }
}
