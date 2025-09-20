package org.lilbrocodes.elixiry.block.entity.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class StirringStickModel extends EntityModel<Entity> {
	private final ModelPart stick;
	public StirringStickModel(ModelPart root) {
		this.stick = root.getChild("stick");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData stick = modelPartData.addChild("stick", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 2.0F, 0.0F));

        stick.addChild("cube_r1", ModelPartBuilder.create().uv(4, 0).cuboid(-0.5F, -16.0F, -0.5F, 1.0F, 16.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.2967F, 0.0F, 0.2967F));
        return TexturedModelData.of(modelData, 16, 16);
	}

	@Override
	public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		stick.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}