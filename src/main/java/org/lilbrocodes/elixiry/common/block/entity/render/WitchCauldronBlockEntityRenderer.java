package org.lilbrocodes.elixiry.common.block.entity.render;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory.Context;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.lilbrocodes.elixiry.common.block.WitchCauldron;
import org.lilbrocodes.elixiry.common.block.entity.WitchCauldronBlockEntity;
import org.lilbrocodes.elixiry.common.block.entity.WitchCauldronBlockEntity.ItemData;
import org.lilbrocodes.elixiry.common.block.entity.WitchCauldronBlockEntity.ItemIdentifier;
import org.lilbrocodes.elixiry.common.block.entity.model.StirringStickModel;
import org.lilbrocodes.elixiry.common.registry.ModBlocks;
import org.lilbrocodes.elixiry.common.registry.ModModelLayers;

import java.util.List;

public class WitchCauldronBlockEntityRenderer implements BlockEntityRenderer<WitchCauldronBlockEntity> {
    private static final Identifier TEXTURE = new Identifier("textures/block/spruce_planks.png");
    private final StirringStickModel model;
    private final ItemRenderer itemRenderer;

    public WitchCauldronBlockEntityRenderer(Context context) {
        model = new StirringStickModel(
                context.getLayerModelPart(
                        ModModelLayers.STIRRING_STICK
                )
        );
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(WitchCauldronBlockEntity be, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        for (int i = 0; i < be.inventory.stacks.size(); i++) {
            if (be.inventory.getStack(i).isEmpty()) be.itemData.set(i, new ItemData(0, 0));
        }

        if (be.prevItemCount != 0 && be.prevItemCount != be.copyStacks().size()) {
            be.boopItems();
        }

        matrices.push();
        matrices.translate(0.5, 0.35, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));

        float interpolatedRotation = be.stickRotation + be.stickRotationSpeed * tickDelta;
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(interpolatedRotation));

        VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
        model.render(matrices, vc, light, overlay, 1f, 1f, 1f, 1f);

        matrices.pop();

        be.stickRotation += be.stickRotationSpeed;
        be.stickRotation %= 3600;

        if (be.stickTicks <= 0) {
            be.stickTicks = 0;
            be.stickRotationSpeed *= 0.98f;
            if (Math.abs(be.stickRotationSpeed) < 0.01f) {
                be.stickRotationSpeed = 0;
            }
        }

        BlockState state = be.getWorld().getBlockState(be.getPos());
        boolean hasFluid = false;
        if (state.getBlock() == ModBlocks.WITCH_CAULDRON.block) {
            hasFluid = state.get(WitchCauldron.HAS_FLUID);
        }

        matrices.push();
        matrices.translate(0.5, 0.1, 0.5);

        List<ItemIdentifier> items = be.copyStacks();
        int itemCount = be.inventory.size();

        for (int i = 0; i < items.size(); i++) {
            ItemIdentifier item = items.get(i);
            ItemStack stack = item.stack();
            ItemData data = be.itemData.get(item.i());
            matrices.push();
            matrices.translate(0, data.getRenderHeight(tickDelta), 0);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));

            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(
                    (be.stickRotation * (hasFluid ? 1 : 0)) * 0.6f + (360f / itemCount) * item.i()
            ));

            float tiltAngle = 15f;
            float direction = (float) i / itemCount - 0.5f;
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-direction * tiltAngle));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-direction * tiltAngle / 2f));

            matrices.translate(0.15, 0, 0);

            itemRenderer.renderItem(
                    stack,
                    ModelTransformationMode.GROUND,
                    light,
                    overlay,
                    matrices,
                    vertexConsumers,
                    be.getWorld(),
                    0
            );

            matrices.pop();
        }
        matrices.pop();

        be.prevItemCount = items.size();
    }
}