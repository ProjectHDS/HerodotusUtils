package youyihj.herodotusutils.entity.golem;

import net.minecraft.entity.EntityLivingBase;

/**
 * @author youyihj
 */
public interface IGolem {
    Color getColor();

    Shape getShape();

    int getLevel();

    void setColor(Color color);

    void setShape(Shape shape);

    void setLevel(int level);

    EntityLivingBase getEntity();

    IGolem copy();
}
