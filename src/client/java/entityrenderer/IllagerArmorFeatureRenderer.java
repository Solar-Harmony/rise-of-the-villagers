package entityrenderer;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.client.render.entity.state.IllagerEntityRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class IllagerArmorFeatureRenderer extends FeatureRenderer<IllagerEntityRenderState, IllagerEntityModel<IllagerEntityRenderState>> {

    private static final Identifier ARMOR_TEXTURE = new Identifier("textures/models/armor/iron_layer_1.png");

    private final BipedEntityModel<?> innerArmorModel;
    private final BipedEntityModel<?> outerArmorModel;

    public IllagerArmorFeatureRenderer(
            FeatureRendererContext<IllagerEntityRenderState, IllagerEntityModel<IllagerEntityRenderState>> context,
            BipedEntityModel<?> innerArmorModel,
            BipedEntityModel<?> outerArmorModel) {
        super(context);
        this.innerArmorModel = innerArmorModel;
        this.outerArmorModel = outerArmorModel;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                       IllagerEntityRenderState state, float limbAngle, float limbDistance) {
        // Copy state from illager model to armor models
        copyStateToModel(state, this.innerArmorModel);
        copyStateToModel(state, this.outerArmorModel);

        // Get the corresponding IllagerEntity from the state
        // This would need to be adapted based on how your state system works
        IllagerEntity illager = getIllagerEntityFromState(state);
        if (illager == null) return;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;

            ItemStack itemStack = illager.getEquippedStack(slot);
            if (itemStack.isEmpty()) continue;

            BipedEntityModel<IllagerEntityRenderState> armorModel =
                    (slot == EquipmentSlot.LEGS) ? innerArmorModel : outerArmorModel;

            setArmorPartVisibility(armorModel, slot);
            renderArmorPiece(context, matrices, vertexConsumers, light, itemStack, armorModel, slot);
        }







            BipedEntityModel<IllagerEntityRenderState> model = slot == EquipmentSlot.LEGS ?
                    this.innerArmorModel : this.outerArmorModel;

            // Set visibility based on slot
            setArmorPartVisibility(model, slot);

            // Get proper texture for the armor
            Identifier texture = getArmorTexture(armorItem, slot);

            // Render the armor piece
            renderArmorPiece(matrices, vertexConsumers, light, illager, limbAngle,
                    limbDistance, model, texture);
        }
    }

    private void copyStateToModel(IllagerEntityRenderState state, BipedEntityModel<?> model) {
        // Instead of setting handSwingProgress or riding on the model,
        // we directly configure the ModelPart rotations here if needed.
        if (state.attacking) {
            // Example: attack pose
            model.rightArm.pitch = -1.5F;
            model.leftArm.pitch = -1.5F;
        } else {
            model.rightArm.pitch = 0.0F;
            model.leftArm.pitch = 0.0F;
        }
    }



    private void setArmorPartVisibility(BipedEntityModel<IllagerEntityRenderState> model, EquipmentSlot slot) {
        model.setVisible(false);
        switch (slot) {
            case HEAD:
                model.head.visible = true;
                model.hat.visible = true;
                break;
            case CHEST:
                model.body.visible = true;
                model.rightArm.visible = true;
                model.leftArm.visible = true;
                break;
            case LEGS:
                model.body.visible = true;
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
                break;
            case FEET:
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
                break;
            default:
                break;
        }
    }

    // Helper method to get illager entity from state
    // This would need to be implemented based on your system
    private IllagerEntity getIllagerEntityFromState(IllagerEntityRenderState state) {
        // This is a placeholder - you would need to implement this
        // based on how your rendering system works
        return null;
    }

    // Helper method to get armor texture
    private Identifier getArmorTexture(ArmorItem armorItem, EquipmentSlot slot) {
        // This is simplified - normally you would determine the texture based on material and slot
        return IllagerArmorFeatureRenderer.ARMOR_TEXTURE;
    }

    // Helper method to render armor piece
    private void renderArmorPiece(
            FeatureRendererContext<IllagerEntityRenderState, ? extends EntityModel<IllagerEntityRenderState>> context,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            ItemStack stack,
            BipedEntityModel<IllagerEntityRenderState> model,
            EquipmentSlot slot) {

        if (stack.isEmpty()) return;

        // Bind the texture of the armor
        Identifier texture = getArmorTexture(stack, slot);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getArmorCutoutNoCull(texture));

        // Render the armor model
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
    }



