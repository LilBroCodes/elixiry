package org.lilbrocodes.elixiry.common.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lilbrocodes.elixiry.Elixiry;
import org.lilbrocodes.elixiry.common.block.entity.WitchCauldronBlockEntity;
import org.lilbrocodes.elixiry.common.registry.ModBlockEntities;
import org.lilbrocodes.elixiry.common.util.PotionModifier;

import java.util.Locale;

import static org.lilbrocodes.elixiry.common.block.entity.WitchCauldronBlockEntity.StirDirection.*;

@SuppressWarnings("deprecation")
public class WitchCauldron extends BlockWithEntity {
    public static final BooleanProperty HAS_FLUID = BooleanProperty.of("has_fluid");
    public static final EnumProperty<HeatState> HEAT_STATE = EnumProperty.of("heat_state", HeatState.class);
    public static final Identifier CONTENTS = Elixiry.identify("contents");

    public static final VoxelShape SHAPE = VoxelShapes.union(
            createCuboidShape(
                    2, 0, 2,
                    14, 1, 14
            ),
            createCuboidShape(
                    14, 1, 1,
                    15, 14, 15
            ),
            createCuboidShape(
                    1, 1, 1,
                    2, 14, 15
            ),
            createCuboidShape(
                    2, 1, 14,
                    14, 14, 15
            ),
            createCuboidShape(
                    2, 1, 1,
                    14, 14, 2
            )
    );

    public WitchCauldron() {
        super(FabricBlockSettings.create().sounds(BlockSoundGroup.LANTERN).hardness(3.5f));
        setDefaultState(getStateManager().getDefaultState().with(HAS_FLUID, false).with(HEAT_STATE, HeatState.NONE));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HAS_FLUID, HEAT_STATE);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WitchCauldronBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0f;
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(HEAT_STATE, getHeat(ctx.getWorld().getBlockState(ctx.getBlockPos().down()).getBlock()));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN) {
            if (!state.canPlaceAt(world, pos)) return Blocks.AIR.getDefaultState();
            else {
                BlockState belowState = world.getBlockState(pos.down());
                HeatState heat = getHeat(belowState.getBlock());

                world.setBlockState(pos, state.with(HEAT_STATE, heat), 3);
            }
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    private static @NotNull HeatState getHeat(Block block) {
        HeatState heat;

        if (block == Blocks.CAMPFIRE || block == Blocks.FIRE || block == Blocks.LAVA) {
            heat = HeatState.HIGH;
        } else if (block == Blocks.MAGMA_BLOCK) {
            heat = HeatState.LOW;
        } else if (block == Blocks.SOUL_CAMPFIRE || block == Blocks.SOUL_FIRE) {
            heat = HeatState.SOUL;
        } else if (block == Blocks.ICE || block == Blocks.FROSTED_ICE) {
            heat = HeatState.ICE;
        } else if (block == Blocks.BLUE_ICE || block == Blocks.PACKED_ICE) {
            heat = HeatState.HIGH_ICE;
        } else {
            heat = HeatState.NONE;
        }
        return heat;
    }

    public boolean inside(BlockPos pos, Entity entity) {
        return entity.squaredDistanceTo(Vec3d.ofCenter(pos).subtract(0, 0.1, 0)) < 0.4 * 0.4;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (inside(pos, entity)) {
            if (entity instanceof ItemEntity item && world.getBlockEntity(pos) instanceof WitchCauldronBlockEntity be) {
                if (!world.isClient) {
                    ItemStack stack = be.addItem(item.getStack().copy());
                    if (stack == ItemStack.EMPTY) item.discard();
                    be.markDirty();
                }
            } else {
                switch (state.get(HEAT_STATE)) {
                    case SOUL, HIGH -> entity.setOnFireFor(1);
                    case HIGH_ICE -> entity.setInPowderSnow(true);
                    case NONE, ICE -> entity.extinguishWithSound();
                }
            }
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!(world.getBlockEntity(pos) instanceof WitchCauldronBlockEntity be)) {
            return ActionResult.PASS;
        }

        ItemStack stack = player.getStackInHand(hand);

        if (stack.isEmpty()) {
            if (world.isClient) {
                be.animateStir(2.5f * (player.shouldCancelInteraction() ? -1 : 1), 20);
            } else {
                be.stir(player.shouldCancelInteraction() ? LEFT : RIGHT);
            }
            return ActionResult.success(world.isClient);
        }

        if (stack.getItem() == Items.POTION) {
            Potion potion = PotionUtil.getPotion(stack);
            if (!state.get(WitchCauldron.HAS_FLUID)) {
                be.setPotion(potion, new PotionModifier(0, 0));
                world.setBlockState(pos, state.with(WitchCauldron.HAS_FLUID, true), Block.NOTIFY_ALL);
                world.updateListeners(pos, state, state.with(WitchCauldron.HAS_FLUID, true), Block.NOTIFY_ALL);

                if (!player.getAbilities().creativeMode) {
                    player.setStackInHand(hand,
                            ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                }
                return ActionResult.SUCCESS;
            }
        }

        if (stack.getItem() == Items.GLASS_BOTTLE && state.get(WitchCauldron.HAS_FLUID)) {
            ItemStack potionStack = be.takePotion();
            player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, potionStack));

            if (!be.hasPotion()) {
                world.setBlockState(pos, state.with(WitchCauldron.HAS_FLUID, false), Block.NOTIFY_ALL);
                world.updateListeners(pos, state, state.with(WitchCauldron.HAS_FLUID, false), Block.NOTIFY_ALL);
            }
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.WITCH_CAULDRON, (w, p, s, be) -> be.tick());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof WitchCauldronBlockEntity be) {
                be.copyStacks().forEach(identifier -> {
                    ItemStack stack = identifier.stack();
                    if (!stack.isEmpty()) {
                        ItemEntity drop = new ItemEntity(
                                world,
                                pos.getX() + 0.5,
                                pos.getY() + 0.5,
                                pos.getZ() + 0.5,
                                stack
                        );
                        world.spawnEntity(drop);
                    }
                });

                be.inventory.clear();
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    public enum HeatState implements StringIdentifiable {
        NONE,
        LOW,
        HIGH,
        SOUL,
        ICE,
        HIGH_ICE;

        @Override
        public String asString() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
