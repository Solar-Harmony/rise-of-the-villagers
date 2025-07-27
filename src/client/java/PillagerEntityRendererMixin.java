package org.solarharmony.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.PillagerEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.client.render.entity.state.IllagerEntityRenderState;
import net.minecraft.client.render.entity.EntityRendererFactory;

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
        // cast `this` to the real renderer
        PillagerEntityRenderer renderer = (PillagerEntityRenderer)(Object)this;

        // grab the baked ModelPart for armor
        ModelPart innerPart = ctx.getPart(EntityModelLayers.PLAYER_INNER_ARMOR);
        ModelPart outerPart = ctx.getPart(EntityModelLayers.PLAYER_OUTER_ARMOR);

        // create IllagerEntityModels from those parts
        IllagerEntityModel<IllagerEntityRenderState> innerModel =
                new IllagerEntityModel<>(innerPart);
        IllagerEntityModel<IllagerEntityRenderState> outerModel =
                new IllagerEntityModel<>(outerPart);

        // attach vanilla's ArmorFeatureRenderer (handles helmets, chestplates, etc.)
        renderer.addFeature(new ArmorFeatureRenderer<>(
                renderer,
                innerModel,
                outerModel,
                ctx.getEquipmentRenderer()
        ));
    }
}
