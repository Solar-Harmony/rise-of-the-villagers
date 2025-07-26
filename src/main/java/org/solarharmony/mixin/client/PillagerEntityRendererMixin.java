package org.solarharmony.mixin.client;

import com.mojang.blaze3d.vertex.VertexConsumerProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.PillagerEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PillagerEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.PillagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Injects the vanilla armor‐rendering feature so equipped helmets/armor
 * will actually be drawn on pillager models.
 */
@Environment(EnvType.CLIENT)
@Mixin(PillagerEntityRenderer.class)
public abstract class PillagerEntityRendererMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void addArmorFeature(
            // parameters must match PillagerEntityRenderer’s constructor:
            // (EntityRenderDispatcher dispatcher, EntityModelLoader loader, float shadowSize, CallbackInfo ci)
            Object dispatcher, EntityModelLoader loader, float shadowSize, CallbackInfo ci
    ) {
        // `this` is the PillagerEntityRenderer instance
        PillagerEntityRenderer self = (PillagerEntityRenderer)(Object)this;

        // create the two layers of armor (inner & outer)
        BipedEntityModel<PillagerEntity> inner = new BipedEntityModel<>(
                loader.getModelPart(EntityModelLayers.ARMOR_INNER));
        BipedEntityModel<PillagerEntity> outer = new BipedEntityModel<>(
                loader.getModelPart(EntityModelLayers.ARMOR_OUTER));

        // attach the feature so the client will render helmets, chestplates, etc.
        self.addFeature(new ArmorFeatureRenderer<>(self, inner, outer));
    }
}
