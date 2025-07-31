//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package model;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.mob.IllagerEntity;

public class IllagerArmorModel<T extends IllagerEntity> extends BipedEntityModel<T> {
    public IllagerArmorModel(ModelPart part) {
        super(part);
    }

    public static TexturedModelData createOuterArmorLayer() {
        ModelData modelData = BipedEntityModel.getModelData(new Dilation(1.0F), 0.0F);
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.0F)), ModelTransform.origin(0.0F, 1.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 32);
    }

    public static TexturedModelData createInnerArmorLayer() {
        ModelData meshdefinition = BipedEntityModel.getModelData(new Dilation(0.5F), 0.0F);
        return TexturedModelData.of(meshdefinition, 64, 32);
    }
}
