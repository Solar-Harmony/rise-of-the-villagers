//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package renderer;

import dev.imb11.armorful.util.ArmorfulUtil;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class PillagerBipedRenderer extends IllagerBipedRenderer<PillagerEntity> {
    private static final Identifier PILLAGER = ArmorfulUtil.defaultID("textures/entity/illager/pillager.png");

    public PillagerBipedRenderer(EntityRendererFactory.Context builder) {
        super(builder);
        this.addFeature(new HeldItemFeatureRenderer(this, builder.method_43338()));
    }

    public @NotNull Identifier getTextureLocation(PillagerEntity entity) {
        return PILLAGER;
    }
}
