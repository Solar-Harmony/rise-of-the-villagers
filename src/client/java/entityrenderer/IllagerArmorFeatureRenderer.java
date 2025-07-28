package entityrenderer;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.client.render.entity.state.IllagerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class IllagerArmorFeatureRenderer extends FeatureRenderer<IllagerEntityRenderState, IllagerEntityModel<IllagerEntityRenderState>> {

    // Use Identifier.of() instead of the constructor
    private static final Identifier ARMOR_TEXTURE = Identifier.of("minecraft", "textures/models/armor/iron_layer_1.png");

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
        System.out.println("DEBUG: IllagerArmorFeatureRenderer.render() called.");

        // We need to get the entity somehow
        IllagerEntity entity = null;

        // Try to get entity from our custom state if possible
        if (state instanceof IllagerArmorRenderState armorState) {
            entity = armorState.entity;
            System.out.println("DEBUG: Got entity from IllagerArmorRenderState");
        }

        // If we have an entity, check its equipment
        if (entity != null) {
            ItemStack headStack = entity.getEquippedStack(EquipmentSlot.HEAD);
            if (!headStack.isEmpty()) {
                System.out.println("DEBUG: Entity has helmet: " + headStack.getItem());
                renderHelmet(matrices, vertexConsumers, light, headStack);
            }
        } else {
            System.out.println("DEBUG: No entity found in render state");
        }
    }

    private void renderHelmet(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ItemStack stack) {
        // Use the outer armor model for helmet
        BipedEntityModel<?> model = outerArmorModel;

        // Only show head parts
        model.setVisible(false);
        model.head.visible = true;
        model.hat.visible = true;

        // Copy head rotation from parent model
        IllagerEntityModel<?> parentModel = this.getContextModel();
        model.head.copyTransform(parentModel.getHead());
        model.hat.copyTransform(parentModel.getHead());

        // Render the helmet
        Identifier texture = getArmorTexture(stack);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getArmorCutoutNoCull(texture));

        // Fix the render method call to match what your version expects
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
    }

    private Identifier getArmorTexture(ItemStack stack) {
        String material = "iron";
        String itemName = stack.getItem().toString();

        if (itemName.contains("diamond")) material = "diamond";
        else if (itemName.contains("netherite")) material = "netherite";
        else if (itemName.contains("gold")) material = "gold";
        else if (itemName.contains("leather")) material = "leather";
        else if (itemName.contains("chainmail")) material = "chainmail";

        // Use Identifier.of() instead of constructor
        return Identifier.of("minecraft", "textures/models/armor/" + material + "_layer_1.png");
    }
}