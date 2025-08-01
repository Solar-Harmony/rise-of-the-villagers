//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package renderer;

import model.IllagerBipedModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import util.ArmorfulUtil;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;;


public class PillagerBipedRenderer extends IllagerBipedRenderer<PillagerEntity> {
    private static final Identifier PILLAGER = ArmorfulUtil.defaultID("textures/entity/illager/pillager.png");

    public PillagerBipedRenderer(EntityRendererFactory.Context builder) {
        super(builder);
        this.addFeature(new HeldItemFeatureRenderer(this, builder.method_43338()));
    }

    @Override
    public EntityRenderState createRenderState() {
        return null;
    }

    public @NotNull Identifier getTextureLocation(PillagerEntity entity) {
        return PILLAGER;
    }

    @Override
    public Identifier getTexture(LivingEntityRenderState state) {
        return null;
    }
}
