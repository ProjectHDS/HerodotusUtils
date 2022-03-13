package youyihj.herodotusutils.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import youyihj.herodotusutils.entity.golem.Color;
import youyihj.herodotusutils.entity.golem.Shape;

/**
 * @author youyihj
 */
public class TileGolemCore extends TileEntity {
    private Color color;
    private Shape shape;

    @SuppressWarnings("unused")
    public TileGolemCore() {
    }

    public TileGolemCore(Color color, Shape shape) {
        this.color = color;
        this.shape = shape;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        color = Color.valueOf(compound.getString("color"));
        shape = Shape.valueOf(compound.getString("shape"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setString("color", color.name());
        compound.setString("shape", shape.name());
        return super.writeToNBT(compound);
    }

    public Color getColor() {
        return color;
    }

    public Shape getShape() {
        return shape;
    }
}
