package entityrenderer;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.client.render.entity.equipment.EquipmentRenderer;

public class IllagerArmorFeatureRenderer<T extends IllagerEntity>
        extends FeatureRenderer<T, IllagerEntityModel<T>> {

    private final IllagerEntityModel<T> innerArmorModel;
    private final IllagerEntityModel<T> outerArmorModel;
    private final EquipmentRenderer equipmentRenderer;

    public IllagerArmorFeatureRenderer(
            FeatureRendererContext<T, IllagerEntityModel<T>> context,
            IllagerEntityModel<T> innerModel,
            IllagerEntityModel<T> outerModel,
            EquipmentRenderer equipmentRenderer) {
        super(context);
        this.innerArmorModel = innerModel;
        this.outerArmorModel = outerModel;
        this.equipmentRenderer = equipmentRenderer;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                       T entity, float limbAngle, float limbDistance, float tickDelta,
                       float animationProgress, float headYaw, float headPitch) {

        // Render each armor piece
        renderArmor(matrices, vertexConsumers, entity, EquipmentSlot.HEAD, light, this.getHeadArmorModel());
        renderArmor(matrices, vertexConsumers, entity, EquipmentSlot.CHEST, light, this.getBodyArmorModel());
        renderArmor(matrices, vertexConsumers, entity, EquipmentSlot.LEGS, light, this.getLeggingsArmorModel());
        renderArmor(matrices, vertexConsumers, entity, EquipmentSlot.FEET, light, this.getFeetArmorModel());
    }

    private void renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                             T entity, EquipmentSlot armorSlot, int light,
                             IllagerEntityModel<T> armorModel) {

        ItemStack itemStack = entity.getEquippedStack(armorSlot);
        if (!(itemStack.getItem() instanceof ArmorItem armorItem) || armorItem.getSlotType() != armorSlot) {
            return;
        }

        // Copy pose from main model
        copyModelPose(armorModel);

        // Make only the relevant parts visible
        setArmorPartVisibility(armorModel, armorSlot);

        // Get appropriate texture for the armor
        boolean isLeggings = armorSlot == EquipmentSlot.LEGS;
        String materialName = armorItem.getMaterial().getName();
        String armorTexturePath = String.format("textures/models/armor/%s_layer_%d.png",
                materialName, isLeggings ? 2 : 1);
        Identifier armorTexture = new Identifier(armorTexturePath);

        // Render the armor piece
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(
                RenderLayer.getArmorCutoutNoCull(armorTexture));

        armorModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV,
                1.0F, 1.0F, 1.0F, 1.0F);

        // Render glint if needed
        if (itemStack.hasGlint()) {
            renderGlint(matrices, vertexConsumers, light, armorModel);
        }
    }

    private void copyModelPose(IllagerEntityModel<T> armorModel) {
        IllagerEntityModel<T> contextModel = this.getContextModel();

        // Copy all relevant transformations based on actual IllagerEntityModel structure
        if (armorModel.head != null && contextModel.head != null) {
            armorModel.head.copyTransform(contextModel.head);
        }

        // Copy arms
        if (armorModel.arms != null && contextModel.arms != null) {
            armorModel.arms.copyTransform(contextModel.arms);
        }

        // Copy individual limbs
        if (armorModel.leftArm != null && contextModel.leftArm != null) {
            armorModel.leftArm.copyTransform(contextModel.leftArm);
        }
        if (armorModel.rightArm != null && contextModel.rightArm != null) {
            armorModel.rightArm.copyTransform(contextModel.rightArm);
        }
        if (armorModel.leftLeg != null && contextModel.leftLeg != null) {
            armorModel.leftLeg.copyTransform(contextModel.leftLeg);
        }
        if (armorModel.rightLeg != null && contextModel.rightLeg != null) {
            armorModel.rightLeg.copyTransform(contextModel.rightLeg);
        }
    }

    private void setArmorPartVisibility(IllagerEntityModel<T> model, EquipmentSlot slot) {
        model.setVisible(false);

        switch (slot) {
            case HEAD -> model.head.visible = true;
            case CHEST -> {
                // Illagers have arms instead of body
                if (model.arms != null) model.arms.visible = true;
                if (model.leftArm != null) model.leftArm.visible = true;
                if (model.rightArm != null) model.rightArm.visible = true;
            }
            case LEGS -> {
                // No body part for illagers, just use legs
                if (model.leftLeg != null) model.leftLeg.visible = true;
                if (model.rightLeg != null) model.rightLeg.visible = true;
            }
            case FEET -> {
                if (model.leftLeg != null) model.leftLeg.visible = true;
                if (model.rightLeg != null) model.rightLeg.visible = true;
            }
        }
    }

    private IllagerEntityModel<T> getHeadArmorModel() {
        return this.innerArmorModel;
    }

    private IllagerEntityModel<T> getBodyArmorModel() {
        return this.innerArmorModel;
    }

    private IllagerEntityModel<T> getLeggingsArmorModel() {
        return this.outerArmorModel;
    }

    private IllagerEntityModel<T> getFeetArmorModel() {
        return this.innerArmorModel;
    }

    private void renderGlint(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                             int light, IllagerEntityModel<T> model) {
        model.render(
                matrices,
                ItemRenderer.getDirectItemGlintConsumer(
                        vertexConsumers,
                        RenderLayer.getArmorEntityGlint(),
                        false,
                        true),
                light,
                OverlayTexture.DEFAULT_UV,
                1.0F, 1.0F, 1.0F, 1.0F);
    }
}