package projecthds.herodotusutils.modsupport.theoneprobe;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.io.IOException;

/**
 * @author youyihj
 */
public class ElementTextComponent implements IElement {
    private final TextStyleClass textStyleClass;
    private final ITextComponent textComponent;

    public ElementTextComponent(TextStyleClass textStyleClass, ITextComponent textComponent) {
        this.textStyleClass = textStyleClass;
        this.textComponent = textComponent;
    }

    public ElementTextComponent(ByteBuf buf) {
        ITextComponent textComponent1;
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        this.textStyleClass = TextStyleClass.values()[buf.readByte()];
        try {
            textComponent1 = packetBuffer.readTextComponent();
        } catch (IOException e) {
            e.printStackTrace();
            textComponent1 = new TextComponentString("ERROR");
        }
        this.textComponent = textComponent1;
    }

    @Override
    public void render(int x, int y) {
        ElementTextRender.render(textStyleClass + textComponent.getUnformattedText(), x, y);
    }

    @Override
    public int getWidth() {
        return ElementTextRender.getWidth(textComponent.getUnformattedText());
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        packetBuffer.writeByte(textStyleClass.ordinal());
        packetBuffer.writeTextComponent(textComponent);
    }

    @Override
    public int getID() {
        return TOPHandler.ELEMENT_TEXT_COMPONENT;
    }
}
