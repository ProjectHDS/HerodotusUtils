package projecthds.herodotusutils.modsupport.theoneprobe;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IItemStyle;
import mcjty.theoneprobe.apiimpl.client.ElementItemStackRender;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;
import mcjty.theoneprobe.network.NetworkTools;
import net.minecraft.item.ItemStack;

/**
 * @author youyihj
 */
public class ElementItemWithName implements IElement {
    private final ItemStack itemStack;
    private final String name;
    private final IItemStyle itemStyle;

    public ElementItemWithName(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.name = itemStack.getDisplayName();
        this.itemStyle = new ItemStyle();
    }

    public ElementItemWithName(ByteBuf buf) {
        if (buf.readBoolean()) {
            this.itemStack = NetworkTools.readItemStack(buf);
        } else {
            this.itemStack = ItemStack.EMPTY;
        }
        this.name = itemStack.getDisplayName();
        this.itemStyle = new ItemStyle();
    }

    @Override
    public void render(int x, int y) {
        if (!itemStack.isEmpty()) {
            ElementItemStackRender.render(itemStack, itemStyle, x, y);
            ElementTextRender.render(name, itemStyle.getWidth() + 5 + x, y + 4);
        }
    }

    @Override
    public int getWidth() {
        return itemStyle.getWidth() + ElementTextRender.getWidth(name) + 5;
    }

    @Override
    public int getHeight() {
        return itemStyle.getHeight();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (!itemStack.isEmpty()) {
            buf.writeBoolean(true);
            NetworkTools.writeItemStack(buf, itemStack);
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public int getID() {
        return TOPHandler.ELEMENT_ITEM_WITH_NAME_ID;
    }
}
