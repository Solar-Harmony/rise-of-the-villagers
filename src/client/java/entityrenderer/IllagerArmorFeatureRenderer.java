package entityrenderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.client.render.entity.state.IllagerEntityRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class IllagerArmorFeatureRenderer extends FeatureRenderer<IllagerEntityRenderState, IllagerEntityModel<IllagerEntityRenderState>> {

    private static final Identifier ARMOR_TEXTURE = new Identifier("textures/models/armor/iron_layer_1.png");

    private final BipedEntityModel<IllagerEntityRenderState> innerArmorModel;
    private final BipedEntityModel<IllagerEntityRenderState> outerArmorModel;

    public IllagerArmorFeatureRenderer(
            FeatureRendererContext<IllagerEntityRenderState, IllagerEntityModel<IllagerEntityRenderState>> context,
            BipedEntityModel<IllagerEntityRenderState> innerArmorModel,
            BipedEntityModel<IllagerEntityRenderState> outerArmorModel) {
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

        // Render each equipment slot
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.ARMOR) continue;

            ItemStack itemStack = illager.getEquippedStack(slot);
            if (!(itemStack.getItem() instanceof ArmorItem)) continue;

            ArmorItem armorItem = (ArmorItem)itemStack.getItem();
            if (armorItem.getSlotType() != slot) continue;

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

    private void copyStateToModel(IllagerEntityRenderState state, BipedEntityModel<IllagerEntityRenderState> model) {
        // Update armor model with illager animation state
        model.handSwingProgress = state.handSwingProgress;
        model.riding = state.hasVehicle;
        model.child = false;

        // Set arm pose based on state
        if (state.attacking) {
            model.rightArmPose = BipedEntityModel.ArmPose.ATTACK;
            model.leftArmPose = BipedEntityModel.ArmPose.ATTACK;
        } else {
            model.rightArmPose = BipedEntityModel.ArmPose.EMPTY;
            model.leftArmPose = BipedEntityModel.ArmPose.EMPTY;
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
        return ARMOR_TEXTURE;
    }

    // Helper method to render armor piece
    private void renderArmorPiece(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                  int light, IllagerEntity illager, float limbAngle,
                                  float limbDistance, BipedEntityModel<IllagerEntityRenderState> model,
                                  Identifier texture) {
        // This is simplified - normally you would handle coloring, glint effects, etc.
        model.render(matrices, vertexConsumers.getBuffer(model.getLayer(texture)),
                light, ItemRenderer.getItemGlintConsumer(vertexConsumers, model.getLayer(texture),
                        false, false), 1.0F, 1.0F, 1.0F, 1.0F);
    }
}