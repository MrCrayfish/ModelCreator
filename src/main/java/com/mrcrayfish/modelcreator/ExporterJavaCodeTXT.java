package com.mrcrayfish.modelcreator;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;

import com.mrcrayfish.modelcreator.element.Element;

public class ExporterJavaCodeTXT extends Exporter
{
    private DecimalFormat format = new DecimalFormat("#.######");
    private ModelCreator creator;
    private boolean includeAABBs, includeMethods, useBoundsHelper, generateRotatedBounds;

    public ExporterJavaCodeTXT(ModelCreator creator, boolean includeAABBs, boolean includeMethods, boolean useBoundsHelper, boolean generateRotatedBounds)
    {
        super(creator.getElementManager());
        this.creator = creator;
        this.includeAABBs = includeAABBs;
        this.includeMethods = includeMethods;
        this.useBoundsHelper = useBoundsHelper;
        this.generateRotatedBounds = generateRotatedBounds;
        format.setDecimalFormatSymbols(SYMBOLS);
    }

    public void writeComponentsToClipboard() throws IOException
    {
        BufferedWriter writer = null;
        try
        {
            StringWriter writerFile = new StringWriter();
            writer = new BufferedWriter(writerFile);
            writeComponents(writer);
            writer.flush();
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(writerFile.toString()), null);
        }
        finally
        {
            writer.close();
        }
    }

    @Override
    protected void writeComponents(BufferedWriter writer) throws IOException
    {
        if (includeAABBs)
        {
            StringBuilder boxList = new StringBuilder("private static final List<AxisAlignedBB>");
            boxList.append(generateRotatedBounds ? "[] COLLISION_BOXES = Bounds.getRotatedBoundLists(" : " COLLISION_BOXES = Lists.newArrayList(");
            ModelBounds bounds = useBoundsHelper ? null : new ModelBounds();
            String name = null;
            double x, y, z;
            for (Element element : manager.getAllElements())
            {
                if (element.getRotation() != 0)
                {
                    writer.write(String.format("// Skipped '%s', as it has roatation", element.getName()));
                }
                else
                {
                    if (name != null)
                        boxList.append(", ");

                    x = element.getStartX();
                    y = element.getStartY();
                    z = element.getStartZ();
                    name = element.getName();
                    name = name.toUpperCase().replaceAll(" ", "_");
                    boxList.append(name);
                    writeElement(writer, bounds, name, x, y, z, x + element.getWidth(), y + element.getHeight(), z + element.getDepth());
                }
                writer.newLine();
            }
            if (name == null)
            {
                JOptionPane.showMessageDialog(creator, "No non-rotated elements were found.", "None Found", JOptionPane.OK_OPTION);
                return;
            }
            if (useBoundsHelper)
                writer.newLine();
            else
            {
                writeNewLine(writer, "/**");
                writeNewLine(writer, String.format("* %s generated using MrCrayfish's Model Creator <a href=\"https://mrcrayfish.com/tools?id=mc\">https://mrcrayfish.com/tools?id=mc</a>",
                        includeMethods ? "AxisAlignedBBs and methods getBoundingBox, collisionRayTrace, and collisionRayTrace" : "AxisAlignedBBs"));
                writeNewLine(writer, "*/");
            }
            writer.write(boxList.append(");").toString());
            writer.newLine();
            if (bounds != null)
                bounds.write(writer);
            else if (generateRotatedBounds)
                writer.write("private static final AxisAlignedBB[] BOUNDING_BOX = Bounds.getBoundingBoxes(COLLISION_BOXES);");
            else
                writer.write("private static final AxisAlignedBB BOUNDING_BOX = Bounds.getBoundingBox(COLLISION_BOXES);");
        }
        if (!includeMethods)
            return;

        if (includeAABBs)
        {
            writer.newLine();
            writer.newLine();
        }
        writeNewLine(writer, "@Override");
        writeNewLine(writer, "public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)");
        writeNewLine(writer, "{");
        writeNewLine(writer, "    return BOUNDING_BOX%s", generateRotatedBounds ? "[state.getValue(FACING).getHorizontalIndex()];" : ";");
        writeNewLine(writer, "}");
        if (useBoundsHelper)
        {
            writer.newLine();
            writeNewLine(writer, "@Override");
            writeNewLine(writer, "protected List<AxisAlignedBB> getCollisionBoxes(IBlockState state, World world, BlockPos pos, @Nullable Entity entity, boolean isActualState)");
            writeNewLine(writer, "{");
            writeNewLine(writer, "    return COLLISION_BOXES%s", generateRotatedBounds ? "[state.getValue(FACING).getHorizontalIndex()];" : ";");
                    writer.write("}");
            return;
        }
        writer.newLine();
        writeNewLine(writer, "@Override");
        writeNewLine(writer, "public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entity, boolean isActualState)");
        writeNewLine(writer, "{");
        writeNewLine(writer, "    entityBox = entityBox.offset(-pos.getX(), -pos.getY(), -pos.getZ());");
        writeNewLine(writer, "    for (AxisAlignedBB box : COLLISION_BOXES)");
        writeNewLine(writer, "    {");
        writeNewLine(writer, "        if (entityBox.intersects(box))");
        writeNewLine(writer, "            collidingBoxes.add(box.offset(pos));");
        writeNewLine(writer, "    }");
        writeNewLine(writer, "}");
        writer.newLine();
        writeNewLine(writer, "@Override");
        writeNewLine(writer, "@Nullable");
        writeNewLine(writer, "public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end)");
        writeNewLine(writer, "{");
        writeNewLine(writer, "    double distanceSq;");
        writeNewLine(writer, "    double distanceSqShortest = Double.POSITIVE_INFINITY;");
        writeNewLine(writer, "    RayTraceResult resultClosest = null;");
        writeNewLine(writer, "    RayTraceResult result;");
        writeNewLine(writer, "    start = start.subtract(pos.getX(), pos.getY(), pos.getZ());");
        writeNewLine(writer, "    end = end.subtract(pos.getX(), pos.getY(), pos.getZ());");
        writeNewLine(writer, "    for (AxisAlignedBB box : COLLISION_BOXES)");
        writeNewLine(writer, "    {");
        writeNewLine(writer, "        result = box.calculateIntercept(start, end);");
        writeNewLine(writer, "        if (result == null)");
        writeNewLine(writer, "            continue;");
        writer.newLine();
        writeNewLine(writer, "        distanceSq = result.hitVec.squareDistanceTo(start);");
        writeNewLine(writer, "        if (distanceSq < distanceSqShortest)");
        writeNewLine(writer, "        {");
        writeNewLine(writer, "            distanceSqShortest = distanceSq;");
        writeNewLine(writer, "            resultClosest = result;");
        writeNewLine(writer, "        }");
        writeNewLine(writer, "    }");
        writeNewLine(writer, "    return resultClosest == null ? null : new RayTraceResult(RayTraceResult.Type.BLOCK, resultClosest.hitVec.addVector(pos.getX(), pos.getY(), pos.getZ()), resultClosest.sideHit, pos);");
                writer.write("}");
    }

    private String format(double value)
    {
        return format.format(useBoundsHelper ? value : value * 0.0625);
    }

    private void writeElement(BufferedWriter writer, ModelBounds bounds, String name, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) throws IOException
    {
        StringBuilder builder = new StringBuilder("private static final AxisAlignedBB");
        if (generateRotatedBounds)
            builder.append("[]");

        builder.append(" %s = new ").append(useBoundsHelper ? "Bounds" : "AxisAlignedBB").append("(%s, %s, %s, %s, %s, %s)");
        if (useBoundsHelper)
            builder.append(generateRotatedBounds ? ".getRotatedBounds()" : ".toAABB()");

        writer.write(String.format(builder.append(";").toString(), name, format(minX), format(minY), format(minZ), format(maxX), format(maxY), format(maxZ)));
        if (bounds != null)
            bounds.union(minX, minY, minZ, maxX, maxY, maxZ);
    }

    private void writeNewLine(BufferedWriter writer, String line, Object... args) throws IOException
    {
        writer.write(String.format(line, args));
        writer.newLine();
    }

    private class ModelBounds
    {
        private double minX, minY, minZ, maxX, maxY, maxZ;

        public ModelBounds()
        {
            minX = minY = minZ = Double.MAX_VALUE;
            maxX = maxY = maxZ = Double.MIN_VALUE;
        }

        public void write(BufferedWriter writer) throws IOException
        {
            writeElement(writer, this, "BOUNDING_BOX", minX, minY, minZ, maxX, maxY, maxZ);
        }

        public void union(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
        {
            this.minX = Math.min(this.minX, minX);
            this.minY = Math.min(this.minY, minY);
            this.minZ = Math.min(this.minZ, minZ);
            this.maxX = Math.max(this.maxX, maxX);
            this.maxY = Math.max(this.maxY, maxY);
            this.maxZ = Math.max(this.maxZ, maxZ);
        }
    }
}