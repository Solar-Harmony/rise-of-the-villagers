package entityrenderer;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PillagerEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.state.IllagerEntityRenderState;

import java.lang.reflect.Method;

public class NingNongEntityRenderer extends PillagerEntityRenderer {

    public NingNongEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    /**
     * Static utility method that can be called from the renderer.PillagerEntityRendererMixin
     * to add armor layers to any PillagerEntityRenderer instance
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void addArmorLayerTo(PillagerEntityRenderer renderer, EntityRendererFactory.Context ctx) {
        // Get the baked ModelPart for armor
        var innerModel = new IllagerEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER_INNER_ARMOR));
        var outerModel = new IllagerEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER_OUTER_ARMOR));

        // Create the armor feature renderer
        // Using unchecked casts to match the expected types
        FeatureRenderer armorFeature = new IllagerArmorFeatureRenderer(
                (FeatureRendererContext<IllagerEntityRenderState, IllagerEntityModel<IllagerEntityRenderState>>) renderer,
                new BipedEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER_INNER_ARMOR)),
                new BipedEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER_OUTER_ARMOR))
        );

;

        // Use reflection to access the protected method
        try {
            // Find the addFeature method in the class hierarchy
            Class<?> currentClass = renderer.getClass();
            Method addFeatureMethod = null;

            while (addFeatureMethod == null && currentClass != Object.class) {
                try {
                    addFeatureMethod = currentClass.getDeclaredMethod("addFeature", FeatureRenderer.class);
                } catch (NoSuchMethodException e) {
                    currentClass = currentClass.getSuperclass();
                }
            }

            if (addFeatureMethod != null) {
                addFeatureMethod.setAccessible(true);
                addFeatureMethod.invoke(renderer, armorFeature);
            } else {
                System.err.println("Could not find addFeature method in class hierarchy");
            }
        } catch (Exception e) {
            System.err.println("Failed to add armor layer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}