//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package renderer;

import kotlin.jvm.JvmField;
import org.solarharmony.RiseOfTheVillagersClient;
import model.IllagerArmorModel;
import model.IllagerBipedModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

public abstract class IllagerBipedRenderer<T extends IllagerEntity> extends MobEntityRenderer<T, IllagerBipedModel<T>> {
    public IllagerBipedRenderer(EntityRendererFactory.Context builder) {
        super(builder, new IllagerBipedModel(builder.getPart(RiseOfTheVillagersClient.getIllagerBiped())), 0.5F);
        this.addFeature(new HeadFeatureRenderer(this, builder.getEntityModels(), builder.method_43338()));
        this.addFeature(new ElytraFeatureRenderer(this, builder.getEntityModels()));
        this.addFeature(new ArmorFeatureRenderer(this, new IllagerArmorModel(builder.getPart(RiseOfTheVillagersClient.getIllagerBipedInnerArmor())), new IllagerArmorModel(builder.getPart(RiseOfTheVillagersClient.getIllagerBipedOuterArmor())), MinecraftClient.getInstance().getBakedModelManager()));
    }

    protected void scale(T entity, MatrixStack matrices, float amount) {
        matrices.scale(0.9375F, 0.9375F, 0.9375F);
    }

    public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        this.setModelVisibilities(entityIn);
        super.method_4054(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

//    private void setModelVisibilities(T entity) {
//        IllagerBipedModel<T> illagerModel = (IllagerBipedModel)this.getModel();
//        ItemStack itemstack = entity.getMainHandStack();
//        ItemStack itemstack1 = entity.getOffHandStack();
//        BipedEntityModel.ArmPose bipedModel.leftArmPose = this.getArmPose(entity, itemstack, itemstack1, Hand.MAIN_HAND);
//        BipedEntityModel.ArmPose bipedModel.rightArmPose = this.getArmPose(entity, itemstack, itemstack1, Hand.OFF_HAND);
//        if (entity.getMainArm() == Arm.RIGHT) {
//            illagerModel.leftArmPose = bipedModel.leftArmPose;
//            illagerModel.rightArmPose = bipedModel.rightArmPose;
//        } else {
//            illagerModel.leftArmPose = bipedModel.rightArmPose;
//            illagerModel.rightArmPose = bipedModel.leftArmPose;
//        }
//
//    }
private void setModelVisibilities(T entity) {
    IllagerBipedModel<T> illagerModel = (IllagerBipedModel<T>) this.getModel();
    ItemStack itemstack = entity.getMainHandStack();
    ItemStack itemstack1 = entity.getOffHandStack();



    BipedEntityModel.ArmPose leftArmPose = this.getArmPose(entity, itemstack, itemstack1, Hand.MAIN_HAND);
    BipedEntityModel.ArmPose rightArmPose = this.getArmPose(entity, itemstack, itemstack1, Hand.OFF_HAND);

    if (entity.getMainArm() == Arm.RIGHT) {
        illagerModel.leftArmPose = leftArmPose;
        illagerModel.rightArmPose = rightArmPose;
    } else {
        illagerModel.leftArmPose = rightArmPose;
        illagerModel.rightArmPose = leftArmPose;
    }
}


    private BipedEntityModel.ArmPose getArmPose(T entity, ItemStack itemStackMain, ItemStack itemStackOff, Hand handIn) {
        BipedEntityModel.ArmPose armPose = ArmPose.EMPTY;
        ItemStack mainItemStack = handIn == Hand.MAIN_HAND ? itemStackMain : itemStackOff;
        if (!mainItemStack.isEmpty()) {
            armPose = ArmPose.ITEM;
            UseAction useaction = mainItemStack.getUseAction();
            switch (useaction) {
                case BLOCK:
                    armPose = ArmPose.BLOCK;
                    break;
                case BOW:
                    armPose = ArmPose.BOW_AND_ARROW;
                    break;
                case SPEAR:
                    armPose = ArmPose.THROW_SPEAR;
                    break;
                case CROSSBOW:
                    if (handIn == entity.getActiveHand()) {
                        armPose = ArmPose.CROSSBOW_CHARGE;
                    }
                    break;
                default:
                    armPose = ArmPose.EMPTY;
            }
        } else {
            boolean hasCrossbowMain = itemStackMain.getItem() instanceof CrossbowItem;
            boolean hasCrossbowOff = itemStackOff.getItem() instanceof CrossbowItem;
            if (hasCrossbowMain) {
                armPose = ArmPose.CROSSBOW_HOLD;
            }

            if (hasCrossbowOff && itemStackMain.getItem().getUseAction(itemStackMain) == UseAction.NONE) {
                armPose = ArmPose.CROSSBOW_HOLD;
            }
        }

        return armPose;
    }
}
