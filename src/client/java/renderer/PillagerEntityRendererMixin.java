package renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.entity.PillagerEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;

import entityrenderer.NingNongEntityRenderer;
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
        // Cast `this` to the real renderer
        PillagerEntityRenderer renderer = (PillagerEntityRenderer)(Object)this;

        // Use the static utility method from NingNongEntityRenderer
        NingNongEntityRenderer.addArmorLayerTo(renderer, ctx);
    }
}