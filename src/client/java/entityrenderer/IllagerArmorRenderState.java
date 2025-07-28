package entityrenderer;

import net.minecraft.client.render.entity.state.IllagerEntityRenderState;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.item.ItemStack;

public class IllagerArmorRenderState extends IllagerEntityRenderState {
    public ItemStack headArmor = ItemStack.EMPTY;
    public ItemStack bodyArmor = ItemStack.EMPTY;
    public ItemStack legArmor = ItemStack.EMPTY;
    public ItemStack feetArmor = ItemStack.EMPTY;

    // Store the entity reference
    public IllagerEntity entity;
}