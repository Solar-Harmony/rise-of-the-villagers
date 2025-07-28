package renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.PillagerEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.client.render.entity.state.IllagerEntityRenderState;
import entityrenderer.IllagerArmorFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PillagerEntityRenderer.class)
public abstract class PillagerEntityRendererMixin {

    @Inject(
            method = "<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;)V",
            at = @At("RETURN")
    )
    private void addArmorLayer(EntityRendererFactory.Context ctx, CallbackInfo ci) {
        System.out.println("DEBUG: PillagerEntityRendererMixin.addArmorLayer() called.");

        // Cast `this` to the feature renderer context
        FeatureRendererContext<IllagerEntityRenderState, IllagerEntityModel<IllagerEntityRenderState>> context =
                (FeatureRendererContext<IllagerEntityRenderState, IllagerEntityModel<IllagerEntityRenderState>>)(Object)this;

        // Create and add our custom armor renderer
        IllagerArmorFeatureRenderer armorRenderer = new IllagerArmorFeatureRenderer(
                context,
                new BipedEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER_INNER_ARMOR)),
                new BipedEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER_OUTER_ARMOR))
        );

        // Use reflection to add the feature renderer
        try {
            java.lang.reflect.Method addFeatureMethod = PillagerEntityRenderer.class
                    .getDeclaredMethod("addFeature", net.minecraft.client.render.entity.feature.FeatureRenderer.class);
            addFeatureMethod.setAccessible(true);
            addFeatureMethod.invoke(this, armorRenderer);
            System.out.println("DEBUG: Successfully added armor feature renderer");
        } catch (Exception e) {
            System.err.println("Failed to add armor renderer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}