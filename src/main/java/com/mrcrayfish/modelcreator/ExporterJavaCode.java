package com.mrcrayfish.modelcreator;

import com.mrcrayfish.modelcreator.element.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

public class ExporterJavaCode extends Exporter
{
    private ModelCreator creator;
    private Version version = Version.V_1_12;
    private boolean includeFields, includeMethods, useBoundsHelper, generateRotatedBounds;

    public ExporterJavaCode(ModelCreator creator, boolean includeFields, boolean includeMethods, boolean useBoundsHelper, boolean generateRotatedBounds)
    {
        super(creator.getElementManager());
        this.creator = creator;
        this.includeFields = includeFields;
        this.includeMethods = includeMethods;
        this.useBoundsHelper = useBoundsHelper;
        this.generateRotatedBounds = useBoundsHelper && generateRotatedBounds;
    }

    public void setVersion(Version version)
    {
        this.version = version;
    }

    public void writeCodeToClipboard() throws IOException
    {
        StringWriter writerFile = new StringWriter();
        try(BufferedWriter writer = new BufferedWriter(writerFile))
        {
            write(writer);
            writer.flush();
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(writerFile.toString()), null);
        }
    }

    @Override
    protected void write(BufferedWriter writer) throws IOException
    {
        if(version == Version.V_1_13 || version == Version.V_1_14)
        {
            if(includeFields)
            {
                /* Generates member fields */
                writeNewLine(writer, "/* Member variables */");
                if(generateRotatedBounds)
                {
                    writeNewLine(writer, "public final ImmutableMap<IBlockState, VoxelShape> SHAPES;");
                }
                else
                {
                    writeNewLine(writer, "public final VoxelShape SHAPE;");
                }
                writer.newLine();

                /* Generates logic which is to be placed into the constructor */
                writeNewLine(writer, "/* Place in Constructor */");
                if(generateRotatedBounds)
                {
                    writeNewLine(writer, "SHAPES = this.generateShapes(this.getStateContainer().getValidStates());");
                }
                else
                {
                    writeNewLine(writer, "SHAPE = this.generateShape();");
                }
                writer.newLine();
            }

            if(!includeMethods)
            {
                return;
            }

            writeNewLine(writer, "/* Methods */");

            /* Creates method for generating voxel shapes for rotatable blocks */
            if(generateRotatedBounds)
            {
                writeNewLine(writer, "private ImmutableMap<IBlockState, VoxelShape> generateShapes(ImmutableList<IBlockState> states)");
                writeNewLine(writer, "{");
                for(Element element : manager.getAllElements())
                {
                    if(element.getRotation() == 0)
                    {
                        String name = element.getName().toUpperCase().replaceAll(" ", "_");
                        double x = element.getStartX();
                        double y = element.getStartY();
                        double z = element.getStartZ();
                        writer.write("    ");
                        writeField(writer, null, name, x, y, z, x + element.getWidth(), y + element.getHeight(), z + element.getDepth());
                    }
                    else
                    {
                        writer.write(String.format("    // Skipped '%s', as it has rotation", element.getName()));
                    }
                    writer.newLine();
                }

                writer.newLine();
                writeNewLine(writer, "    ImmutableMap.Builder<IBlockState, VoxelShape> builder = new ImmutableMap.Builder<>();");
                writeNewLine(writer, "    for(IBlockState state : states)");
                writeNewLine(writer, "    {");
                writeNewLine(writer, "        EnumFacing facing = state.getValue(HORIZONTAL_FACING);");
                writeNewLine(writer, "        List<VoxelShape> shapes = new ArrayList<>();");

                for(Element element : manager.getAllElements())
                {
                    if(element.getRotation() == 0)
                    {
                        String name = element.getName().toUpperCase().replaceAll(" ", "_");
                        writeNewLine(writer, String.format("        shapes.add(%s[facing.getHorizontalIndex()]);", name));
                    }
                }

                writeNewLine(writer, "        builder.put(state, VoxelShapeHelper.combineAll(shapes));");
                writeNewLine(writer, "    }");
                writeNewLine(writer, "    return builder.build();");
                writeNewLine(writer, "}");
                writer.newLine();
            }
            else
            {
                writeNewLine(writer, "private VoxelShape generateShape()");
                writeNewLine(writer, "{");
                writeNewLine(writer, "    List<VoxelShape> shapes = new ArrayList<>();");
                for(Element element : manager.getAllElements())
                {
                    if(element.getRotation() == 0)
                    {
                        String name = element.getName().toUpperCase().replaceAll(" ", "_");
                        double x = element.getStartX();
                        double y = element.getStartY();
                        double z = element.getStartZ();
                        writer.write("    ");
                        writeField(writer, null, name, x, y, z, x + element.getWidth(), y + element.getHeight(), z + element.getDepth());
                    }
                    else
                    {
                        writer.write(String.format("    // Skipped '%s', as it has rotation", element.getName()));
                    }
                    writer.newLine();
                }

                if(useBoundsHelper)
                {
                    writeNewLine(writer, "    return VoxelShapeHelper.combineAll(shapes)");
                }
                else
                {
                    writer.newLine();
                    writeNewLine(writer, "    VoxelShape result = ShapeUtils.empty();");
                    writeNewLine(writer, "    for(VoxelShape shape : shapes)");
                    writeNewLine(writer, "    {");
                    writeNewLine(writer, "        result = ShapeUtils.combine(result, shape, IBooleanFunction.OR);");
                    writeNewLine(writer, "    }");
                    writeNewLine(writer, "    return result.simplify();");
                }
                writeNewLine(writer, "}");
                writer.newLine();
            }

            /* Produces the method for selection box */
            writeNewLine(writer, "@Override");
            writeNewLine(writer, "public VoxelShape getShape(IBlockState state, IBlockReader reader, BlockPos pos)");
            writeNewLine(writer, "{");
            if(generateRotatedBounds)
            {
                writeNewLine(writer, "    return SHAPES.get(state);");
            }
            else
            {
                writeNewLine(writer, "    return SHAPE;");
            }
            writeNewLine(writer, "}");
            writer.newLine();

            /* Produces the method for collisions */
            writeNewLine(writer, "@Override");
            writeNewLine(writer, "public VoxelShape getCollisionShape(IBlockState state, IBlockReader reader, BlockPos pos)");
            writeNewLine(writer, "{");
            if(generateRotatedBounds)
            {
                writeNewLine(writer, "    return SHAPES.get(state);");
            }
            else
            {
                writeNewLine(writer, "    return SHAPE;");
            }
            writeNewLine(writer, "}");
        }
        else if(version == Version.V_1_12)
        {
            if(includeFields)
            {
                StringBuilder boxList = new StringBuilder("private static final List<AxisAlignedBB>");
                boxList.append(generateRotatedBounds ? "[] COLLISION_BOXES = Bounds.getRotatedBoundLists(" : " COLLISION_BOXES = Lists.newArrayList(");
                ModelBounds bounds = useBoundsHelper ? null : new ModelBounds();
                String name = null;
                double x, y, z;
                for(Element element : manager.getAllElements())
                {
                    if(element.getRotation() != 0)
                    {
                        writer.write(String.format("// Skipped '%s', as it has roatation", element.getName()));
                    }
                    else
                    {
                        if(name != null)
                        {
                            boxList.append(", ");
                        }

                        x = element.getStartX();
                        y = element.getStartY();
                        z = element.getStartZ();
                        name = element.getName();
                        name = name.toUpperCase().replaceAll(" ", "_");
                        boxList.append(name);
                        writeField(writer, bounds, name, x, y, z, x + element.getWidth(), y + element.getHeight(), z + element.getDepth());
                    }
                    writer.newLine();
                }

                if(name == null)
                {
                    JOptionPane.showMessageDialog(creator, "No non-rotated elements were found.", "None Found", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if(useBoundsHelper)
                {
                    writer.newLine();
                }
                else
                {
                    writeNewLine(writer, "/**");
                    writeNewLine(writer, String.format("* %s generated using MrCrayfish's Model Creator <a href=\"https://mrcrayfish.com/tools?id=mc\">https://mrcrayfish.com/tools?id=mc</a>", includeMethods ? "AxisAlignedBBs and methods getBoundingBox, collisionRayTrace, and collisionRayTrace" : "AxisAlignedBBs"));
                    writeNewLine(writer, "*/");
                }

                writer.write(boxList.append(");").toString());
                writer.newLine();

                if(bounds != null)
                {
                    bounds.write(writer);
                }
                else if(generateRotatedBounds)
                {
                    writer.write("private static final AxisAlignedBB[] BOUNDING_BOX = Bounds.getBoundingBoxes(COLLISION_BOXES);");
                }
                else
                {
                    writer.write("private static final AxisAlignedBB BOUNDING_BOX = Bounds.getBoundingBox(COLLISION_BOXES);");
                }
            }

            if(!includeMethods)
            {
                return;
            }

            if(includeFields)
            {
                writer.newLine();
                writer.newLine();
            }

            writeNewLine(writer, "@Override");
            writeNewLine(writer, "public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)");
            writeNewLine(writer, "{");
            writeNewLine(writer, "    return BOUNDING_BOX%s", generateRotatedBounds ? "[state.getValue(FACING).getHorizontalIndex()];" : ";");
            writeNewLine(writer, "}");

            if(useBoundsHelper)
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
    }

    private String format(double value)
    {
        if(version == Version.V_1_12 || version == Version.V_1_13)
        {
            return FORMAT.format(useBoundsHelper ? value : value * 0.0625);
        }
        else
        {
            return FORMAT.format(useBoundsHelper ? value : value);
        }
    }

    private void writeField(BufferedWriter writer, ModelBounds bounds, String name, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) throws IOException
    {
        if(version == Version.V_1_13 || version == Version.V_1_14)
        {
            if(generateRotatedBounds)
            {
                writer.write(String.format("final VoxelShape[] %s = VoxelShapeHelper.getRotatedVoxelShapes(Block.makeCuboidShape(%s, %s, %s, %s, %s, %s));", name, format(minX), format(minY), format(minZ), format(maxX), format(maxY), format(maxZ)));
            }
            else
            {
                writer.write(String.format("shapes.add(Block.makeCuboidShape(%s, %s, %s, %s, %s, %s)); // %s", format(minX), format(minY), format(minZ), format(maxX), format(maxY), format(maxZ), name));
            }
        }
        else if(version == Version.V_1_12)
        {
            StringBuilder builder = new StringBuilder("private static final AxisAlignedBB");

            if(generateRotatedBounds)
            {
                builder.append("[]");
            }

            builder.append(" %s = new ").append(useBoundsHelper ? "Bounds" : "AxisAlignedBB").append("(%s, %s, %s, %s, %s, %s)");

            if(useBoundsHelper)
            {
                builder.append(generateRotatedBounds ? ".getRotatedBounds()" : ".toAABB()");
            }

            writer.write(String.format(builder.append(";").toString(), name, format(minX), format(minY), format(minZ), format(maxX), format(maxY), format(maxZ)));
        }

        if(bounds != null)
        {
            bounds.union(minX, minY, minZ, maxX, maxY, maxZ);
        }
    }

    private void writeNewLine(BufferedWriter writer, String line, Object... args) throws IOException
    {
        writer.write(String.format(line, args));
        writer.newLine();
    }

    private class ModelBounds
    {
        private double minX, minY, minZ, maxX, maxY, maxZ;

        private ModelBounds()
        {
            minX = minY = minZ = Double.MAX_VALUE;
            maxX = maxY = maxZ = Double.MIN_VALUE;
        }

        public void write(BufferedWriter writer) throws IOException
        {
            writeField(writer, this, "BOUNDING_BOX", minX, minY, minZ, maxX, maxY, maxZ);
        }

        private void union(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
        {
            this.minX = Math.min(this.minX, minX);
            this.minY = Math.min(this.minY, minY);
            this.minZ = Math.min(this.minZ, minZ);
            this.maxX = Math.max(this.maxX, maxX);
            this.maxY = Math.max(this.maxY, maxY);
            this.maxZ = Math.max(this.maxZ, maxZ);
        }
    }

    public enum Version
    {
        V_1_12("1.12"), V_1_13("1.13"), V_1_14("1.14");

        private String label;

        Version(String label)
        {
            this.label = label;
        }

        @Override
        public String toString()
        {
            return label;
        }
    }
}