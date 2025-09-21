package org.lilbrocodes.elixiry.common.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.joml.Vector3f;
import org.lilbrocodes.composer_reloaded.api.particle.ParticleManager;
import org.lilbrocodes.elixiry.common.block.WitchCauldron;
import org.lilbrocodes.elixiry.common.recipe.processing.ActiveBrewingSession;
import org.lilbrocodes.elixiry.common.registry.ModBlockEntities;
import org.lilbrocodes.elixiry.common.registry.ModBlockTags;
import org.lilbrocodes.elixiry.common.registry.ModBlocks;
import org.lilbrocodes.elixiry.common.util.PotionModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class WitchCauldronBlockEntity extends BlockEntity {
    public enum StirDirection {
        LEFT,
        RIGHT
    }

    public static final int MAX_ITEMS = 4;
    public static final int MAX_POWER = 32;
    public static final List<BlockPos> POSSIBLE_ARCANE_PROVIDERS = BlockPos.stream(-2, -2, -2, 2, 2, 2)
            .map(BlockPos::toImmutable)
            .toList();


    public final SimpleInventory inventory = new SimpleInventory(MAX_ITEMS) {
        @Override
        public ItemStack addStack(ItemStack stack) {
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                ItemStack itemStack = stack.copy();
                if (itemStack.isEmpty()) {
                    return ItemStack.EMPTY;
                } else {
                    this.addToNewSlot(itemStack);
                    return itemStack.isEmpty() ? ItemStack.EMPTY : itemStack;
                }
            }
        }

        private void addToNewSlot(ItemStack stack) {
            for (int i = 0; i < MAX_ITEMS; i++) {
                ItemStack itemStack = this.getStack(i);
                if (itemStack.isEmpty()) {
                    this.setStack(i, stack.copyAndEmpty());
                    return;
                }
            }
        }
    };

    public final List<ItemData> itemData = DefaultedList.ofSize(MAX_ITEMS, new ItemData(0, 0));
    public Potion potion = Potions.EMPTY;
    public PotionModifier modifier = new PotionModifier(0, 0);
    public ActiveBrewingSession session;

    public float stickTicks = 0;
    public float stickRotation = 0;
    public float stickRotationSpeed = 0;
    public int prevItemCount = 0;
    public final Random random;

    public static class ItemData {
        public float height;
        public float prevHeight;
        public float speed;

        private static final float SURFACE = 2.1f;
        private static final float BOTTOM = 0.0f;

        private static final float GRAVITY = -0.04f;
        private static final float BUOYANCY_COEFF = 0.06f;

        private static final float DRAG_FLUID = 0.92f;
        private static final float DRAG_AIR = 0.92f;
        private static final float RESTITUTION_BOTTOM = 0.28f;
        private static final float RESTITUTION_SURFACE = 0.32f;

        public ItemData(float height, float speed) {
            this.height = height;
            this.prevHeight = height;
            this.speed = speed;
        }

        public void tick(boolean hasFluid) {
            prevHeight = height;

            float accel = GRAVITY;

            if (hasFluid && height < SURFACE) {
                float submerged = (SURFACE - height) / SURFACE;
                if (submerged < 0f) submerged = 0f;
                if (submerged > 1f) submerged = 1f;

                accel += BUOYANCY_COEFF * submerged;
            }

            speed += accel;
            speed *= (hasFluid && height < SURFACE) ? DRAG_FLUID : DRAG_AIR;
            height += speed;

            if (height < BOTTOM) {
                height = BOTTOM;
                if (Math.abs(speed) > 0.01f) {
                    speed = -speed * RESTITUTION_BOTTOM;
                } else {
                    speed = 0f;
                }
            }

            if (hasFluid && height > SURFACE) {
                height = SURFACE;
                if (speed > 0) {
                    speed = -speed * RESTITUTION_SURFACE;
                }
            }
        }

        public float getRenderHeight(float tickDelta) {
            return prevHeight + (height - prevHeight) * tickDelta;
        }

        public void boop() {
            speed -= 0.02f;
        }
    }

    public record ItemIdentifier(ItemStack stack, int i) {}
    public List<ItemIdentifier> copyStacks() {
        List<ItemIdentifier> stacks = new ArrayList<>();
        AtomicInteger i = new AtomicInteger(0);
        inventory.stacks.forEach(stack -> {
            if (!stack.isEmpty()) stacks.add(new ItemIdentifier(stack.copy(), i.get()));
            i.getAndIncrement();
        });
        return stacks;
    }

    public WitchCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WITCH_CAULDRON, pos, state);
        this.random = new Random();
    }

    public static int getBlockWaterColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex) {
        if (world.getBlockEntity(pos) instanceof WitchCauldronBlockEntity be && !be.isWater()) return be.calculatePotionColor();
        return BiomeColors.getWaterColor(world, pos);
    }

    public int calculatePotionColor() {
        if (potion.getEffects().isEmpty()) {
            return 0x385dc6;
        }

        return PotionUtil.getColor(potion);
    }

    public boolean isWater() {
        return potion == Potions.WATER || potion == Potions.EMPTY;
    }

    public WitchCauldron.HeatState getHeat() {
        if (world == null) return WitchCauldron.HeatState.NONE;
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() != ModBlocks.WITCH_CAULDRON.block) return WitchCauldron.HeatState.NONE;
        else return state.get(WitchCauldron.HEAT_STATE);
    }

    public int calculateArcanePower() {
        int power = 0;
        if (world == null) return power;

        for (BlockPos offset : POSSIBLE_ARCANE_PROVIDERS) {
            BlockPos checkPos = pos.add(offset);
            BlockState state = world.getBlockState(checkPos);

            if (state.isIn(ModBlockTags.WEAK_ARCANE_BLOCKS)) {
                power += 1;
            } else if (state.isIn(ModBlockTags.STRONG_ARCANE_BLOCKS)) {
                power += 5;
            }
        }

        return Math.min(power, MAX_POWER);
    }

    public void tick() {
        if (stickTicks > 0) {
            stickTicks--;
        }

        assert getWorld() != null;
        WitchCauldron.HeatState heat = getHeat();
        ParticleEffect ice = new DustParticleEffect(new Vector3f(0.75f, 1.0f, 1.0f), 0.5f);

        switch (heat) {
            case ICE -> spawnParticleInside(ice, 10);
            case HIGH_ICE -> spawnParticleInside(ice, 6);
            case LOW -> spawnParticleInside(ParticleTypes.BUBBLE_POP, 10);
            case HIGH -> spawnParticleInside(ParticleTypes.BUBBLE_POP, 6);
            case SOUL -> spawnParticleInside(ParticleTypes.SOUL_FIRE_FLAME, 8);
        }

        if (world != null) {
            if (world.isClient) {
                BlockState state = world.getBlockState(pos);
                boolean hasFluid = false;
                if (state.getBlock() == ModBlocks.WITCH_CAULDRON.block) {
                    hasFluid = state.get(WitchCauldron.HAS_FLUID);
                }

                for (ItemData data : itemData) {
                    data.tick(hasFluid);
                }
            } else {
                if (session == null) {
                    this.session = ActiveBrewingSession.start(this);
                } else {
                    if (session.tick(this)) {
                        explode();
                        session = null;
                    } else if (session.done()) {
                        var winner = session.getWinningRecipe();
                        if (winner != null) {
                            if (calculateArcanePower() < winner.getRecipe().minimumArcaneEnergy) {
                                explode();
                            } else {
                                this.potion = winner.getResult();
                                this.modifier = winner.getModifier(world, pos);
                            }
                        }
                        this.session = null;
                        this.inventory.clear();
                    }
                    markDirty();
                }
            }
        }
    }

    public void spawnParticleInside(ParticleEffect effect, int onOffset) {
        if (world == null) return;
        if (world.getBlockState(pos).get(WitchCauldron.HAS_FLUID) && world.getTime() % onOffset == 0) {
            Vec3d center = Vec3d.ofCenter(pos).add(
                    random.nextFloat(0.5f) - 0.25,
                    0.4,
                    random.nextFloat(0.5f) - 0.25
            );
            ParticleManager.spawnParticle(world, center, Vec3d.ZERO, effect);
        }
    }

    public void explode() {
        if (world != null && !world.isClient) {
            world.breakBlock(pos, false);
            world.createExplosion(
                    null,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    0.6f,
                    World.ExplosionSourceType.BLOCK
            );
        }
    }

    public ItemStack addItem(ItemStack stack) {
        ItemStack intermediary = this.inventory.addStack(stack.copy());
        if (session != null && session.addItem(stack)) explode();
        markDirty();
        return intermediary;
    }

    public boolean canTakePotion() {
        return session == null;
    }

    public void setPotion(Potion potion, PotionModifier modifier) {
        this.potion = potion;
        this.modifier = modifier != null ? modifier : new PotionModifier(0, 0);
        markDirty();
    }

    public void clearPotion() {
        this.potion = Potions.EMPTY;
        this.modifier = new PotionModifier(0, 0);
        markDirty();
    }

    public void softReset() {
        clearPotion();
        this.inventory.clear();
        this.session = null;

        markDirty();
    }

    public boolean hasPotion() {
        return potion != null && potion != Potions.EMPTY;
    }

    public ItemStack takePotion() {
        ItemStack stack = new ItemStack(Items.POTION);
        PotionUtil.setCustomPotionEffects(stack, getEffects());
        PotionUtil.setPotion(stack, potion);
        stack.setSubNbt("ElixiryPotion", NbtByte.of(true));

        softReset();
        if (world != null && world.getBlockState(pos).getBlock() == ModBlocks.WITCH_CAULDRON.block) world.setBlockState(pos, world.getBlockState(pos).with(WitchCauldron.HAS_FLUID, false), 3);

        markDirty();
        return stack;
    }

    public List<StatusEffectInstance> getEffects() {
        List<StatusEffectInstance> effects = new ArrayList<>();
        potion.getEffects().forEach(effect -> {
            if (modifier != null) effects.add(new StatusEffectInstance(effect.getEffectType(), effect.getDuration() + modifier.duration, effect.getAmplifier() + modifier.level, effect.isAmbient(), effect.shouldShowParticles(), effect.shouldShowIcon()));
        });
        return effects;
    }

    public void boopItems() {
        itemData.forEach(ItemData::boop);
    }

    public void stir(StirDirection direction) {
        if (session != null && session.stir(direction)) explode();
        markDirty();
    }

    public void animateStir(float speed, int ticks) {
        this.stickRotationSpeed = speed;
        this.stickTicks = ticks;
    }

    @Override
    protected void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.put("Items", inventory.toNbtList());
        tag.put("Modifier", modifier.writeNbt(new NbtCompound()));
        tag.putBoolean("HasSession", session != null);
        if (session != null) {
            tag.put("RecipeSession", session.writeNbt(new NbtCompound()));
        }
        tag.putString("Potion", Registries.POTION.getId(potion).toString());
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        inventory.readNbtList(tag.getList("Items", NbtElement.COMPOUND_TYPE));
        modifier = PotionModifier.readNbt(tag.getCompound("Modifier"));
        potion = Potion.byId(tag.getString("Potion"));

        if (tag.getBoolean("HasSession")) {
            session = ActiveBrewingSession.readNbt(tag.getCompound("RecipeSession"));
        } else {
            session = null;
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (world != null && !world.isClient) {
            world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), Block.NOTIFY_ALL);
            world.scheduleBlockTick(pos, ModBlocks.WITCH_CAULDRON.block, 0, TickPriority.EXTREMELY_HIGH);
        }
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
