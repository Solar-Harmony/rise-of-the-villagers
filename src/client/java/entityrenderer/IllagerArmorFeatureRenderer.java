package entityrenderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.client.render.entity.state.IllagerEntityRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.util.Identifier;
import net.minecraft.client.render.entity.model.ModelPart;
import net.minecraft.client.render.EquipmentRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.model.Dilation;

public class IllagerArmorFeatureRenderer extends ArmorFeatureRenderer<IllagerEntity, IllagerEntityModel<IllagerEntityRenderState>> {

    private final IllagerEntityModel<IllagerEntityRenderState> innerModel;
    private final IllagerEntityModel<IllagerEntityRenderState> outerModel;

    public IllagerArmorFeatureRenderer(
            FeatureRendererContext<IllagerEntity, IllagerEntityModel<IllagerEntityRenderState>> context,
            IllagerEntityModel<IllagerEntityRenderState> innerModel,
            IllagerEntityModel<IllagerEntityRenderState> outerModel,
            EquipmentRenderer equipmentRenderer) {
        super(context, createBipedArmorModel(0.5F), createBipedArmorModel(1.0F), equipmentRenderer);
        this.innerModel = innerModel;
        this.outerModel = outerModel;
    }

    // Create placeholder biped models to satisfy the parent constructor
    private static BipedEntityModel createBipedArmorModel(float scale) {
        return new BipedEntityModel(Dilation.NONE);
    }

    // Override the setVisible method to use illager model parts
    @Override
    protected void setVisible(IllagerEntityModel<IllagerEntityRenderState> model, EquipmentSlot slot, boolean visible) {
        model.setVisible(false);
        switch (slot) {
            case HEAD:
                model.getHead().visible = visible;
                break;
            case CHEST:
                model.getBody().visible = visible;
                model.getRightArm().visible = visible;
                model.getLeftArm().visible = visible;
                break;
            case LEGS:
                model.getBody().visible = visible;
                model.getRightLeg().visible = visible;
                model.getLeftLeg().visible = visible;
                break;
            case FEET:
                model.getRightLeg().visible = visible;
                model.getLeftLeg().visible = visible;
                break;
        }
    }

    // Override other methods as needed to render the armor on the illager model
    @Override
    protected void setPartVisibility(IllagerEntityModel<IllagerEntityRenderState> model, EquipmentSlot slot) {
        setVisible(model, slot, true);
    }

    // Override render method to use the illager models instead of biped models
    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                       IllagerEntity entity, float limbAngle, float limbDistance,
                       float tickDelta, float animationProgress, float headYaw, float headPitch) {
        // Custom rendering logic that uses innerModel and outerModel
        // This would be similar to the parent class but adapted for illager models
    }
}