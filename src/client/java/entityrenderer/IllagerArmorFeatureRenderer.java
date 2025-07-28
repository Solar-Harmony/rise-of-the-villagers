package entityrenderer;

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
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class IllagerArmorFeatureRenderer extends FeatureRenderer<IllagerEntityRenderState, IllagerEntityModel<IllagerEntityRenderState>> {

    private static final Identifier ARMOR_TEXTURE = Identifier.of("minecraft", "textures/models/armor/iron_layer_1.png");

    private final FeatureRendererContext<IllagerEntityRenderState, IllagerEntityModel<IllagerEntityRenderState>> rendererContext;
    private final BipedEntityModel<?> innerArmorModel;
    private final BipedEntityModel<?> outerArmorModel;

    public IllagerArmorFeatureRenderer(
            FeatureRendererContext<IllagerEntityRenderState, IllagerEntityModel<IllagerEntityRenderState>> context,
            BipedEntityModel<?> innerArmorModel,
            BipedEntityModel<?> outerArmorModel) {
        super(context);
        this.rendererContext = context;
        this.innerArmorModel = innerArmorModel;
        this.outerArmorModel = outerArmorModel;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                       IllagerEntityRenderState state, float limbAngle, float limbDistance) {
        System.out.println("DEBUG: IllagerArmorFeatureRenderer.render() called.");

        // Copy state from illager model to armor models
        copyStateToModel(state, this.innerArmorModel);
        copyStateToModel(state, this.outerArmorModel);

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;

            ItemStack itemStack = switch (slot) {
                case HEAD -> state.headArmor;
                case CHEST -> state.bodyArmor;
                case LEGS -> state.legArmor;
                case FEET -> state.feetArmor;
                default -> ItemStack.EMPTY;
            };

            System.out.println("DEBUG: Checking slot " + slot + " -> " + itemStack);

            if (itemStack.isEmpty()) continue;

            BipedEntityModel<?> armorModel = (slot == EquipmentSlot.LEGS) ? innerArmorModel : outerArmorModel;
            setArmorPartVisibility(armorModel, slot);

            renderArmorPiece(this.rendererContext, matrices, vertexConsumers, light, itemStack, armorModel, slot);
        }





    }

    private void copyStateToModel(IllagerEntityRenderState state, BipedEntityModel<?> model) {
        if (state.attacking) {
            model.rightArm.pitch = -1.5F;
            model.leftArm.pitch = -1.5F;
        } else {
            model.rightArm.pitch = 0.0F;
            model.leftArm.pitch = 0.0F;
        }
    }

    private void setArmorPartVisibility(BipedEntityModel<?> model, EquipmentSlot slot) {
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

    private Identifier getArmorTexture(ItemStack stack, EquipmentSlot slot) {
        String layer = slot == EquipmentSlot.LEGS ? "2" : "1";
        String material = "iron";

        String itemName = stack.getItem().toString();
        if (itemName.contains("diamond")) material = "diamond";
        else if (itemName.contains("netherite")) material = "netherite";
        else if (itemName.contains("gold")) material = "gold";
        else if (itemName.contains("leather")) material = "leather";
        else if (itemName.contains("chainmail")) material = "chainmail";

        Identifier texture = Identifier.of("minecraft", "textures/models/armor/" + material + "_layer_" + layer + ".png");
        System.out.println("DEBUG: getArmorTexture() -> " + texture);
        return texture;
    }

    private void renderArmorPiece(
            FeatureRendererContext<?, ? extends EntityModel<?>> context,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            ItemStack stack,
            BipedEntityModel<?> model,
            EquipmentSlot slot) {

        if (stack.isEmpty()) {
            System.out.println("DEBUG: Skipping empty armor stack for slot " + slot);
            return;
        }

        Identifier texture = getArmorTexture(stack, slot);
        System.out.println("DEBUG: renderArmorPiece() rendering slot " + slot + " with " + texture);

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getArmorCutoutNoCull(texture));
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
    }
}
