package net.jadenxgamer.netherexp.registry.item.custom;

import net.jadenxgamer.netherexp.registry.advancements.JNECriteriaTriggers;
import net.jadenxgamer.netherexp.registry.enchantment.JNEEnchantments;
import net.jadenxgamer.netherexp.registry.entity.custom.SoulBullet;
import net.jadenxgamer.netherexp.registry.item.JNEItemRenderer;
import net.jadenxgamer.netherexp.registry.item.JNEItems;
import net.jadenxgamer.netherexp.registry.item.client.ItemAnimationState;
import net.jadenxgamer.netherexp.registry.misc_registry.JNEDamageSources;
import net.jadenxgamer.netherexp.registry.misc_registry.JNESoundEvents;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PumpChargeShotgunItem extends ProjectileWeaponItem implements Vanishable, IShotgun {
    public final ItemAnimationState fireAnimationState = new ItemAnimationState();
    public final ItemAnimationState pumpAnimationState = new ItemAnimationState();
    public final ItemAnimationState overpumpAnimationState = new ItemAnimationState();

    private int pumpTimeOut;
    private boolean pumpFlag;

    private int fireTimeOut;
    private boolean fireFlag;

    public PumpChargeShotgunItem(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new JNEItemRenderer();
            }
        });
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (this.pumpFlag) {
            playPumpAnimation(stack, entity.tickCount);
        }
        if (this.fireFlag) {
            playFireAnimation(stack, entity.tickCount);
        }
        if (getCharge(stack) >= 4) {
            overpumpAnimationState.startIfStopped(entity.tickCount, stack);
        } else {
            overpumpAnimationState.stop(stack);
        }
    }

    private void useProjectile(ItemStack stack, LivingEntity user) {
        if (user instanceof Player player) {
            boolean creative = player.getAbilities().instabuild;
            ItemStack projectileStack = player.getProjectile(stack);
            if (!projectileStack.isEmpty() || creative) {
                if (projectileStack.isEmpty()) {
                    projectileStack = new ItemStack(JNEItems.WRAITHING_FLESH.get());
                }
                boolean bl = projectileStack.getItem() == JNEItems.WRAITHING_FLESH.get();
                if (bl && !creative) {
                    projectileStack.shrink(1);
                    if (projectileStack.isEmpty()) {
                        player.getInventory().removeItem(projectileStack);
                    }
                }
                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        int cartridge = EnchantmentHelper.getItemEnchantmentLevel(JNEEnchantments.CARTRIDGE.get(), stack) * 10;
        if (player.isShiftKeyDown() && getCharge(stack) <= 3) {
            this.pumpFlag = true;
            setCharge(stack, getCharge(stack) + 1);
            player.startUsingItem(interactionHand);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), JNESoundEvents.SHOTGUN_LOAD.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        else {
            if (getCharge(stack) <= 3) {
                if (!player.getProjectile(stack).isEmpty() || player.getAbilities().instabuild) {
                    performShooting(level, player, stack);
                    stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), JNESoundEvents.SHOTGUN_USE.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                    player.getCooldowns().addCooldown(this, 15);
                    useProjectile(stack, player);
                    setCharge(stack, 0);
                    return InteractionResultHolder.pass(stack);
                }
            }
            else {
                if (!player.getProjectile(stack).isEmpty() || player.getAbilities().instabuild) {
                    this.fireFlag = true;
                    performShooting(level, player, stack);
                    level.explode(player, player.getX(), player.getY(), player.getZ(), 3, false, Level.ExplosionInteraction.NONE);
                    player.getCooldowns().addCooldown(this, 100);
                    player.hurt(level.damageSources().source(JNEDamageSources.SHOTGUN_EXPLOSION, player), 10);
                    stack.hurtAndBreak(5, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
                    useProjectile(stack, player);
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), JNESoundEvents.SHOTGUN_USE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    setCharge(stack, 0);

                    Vec3 look = player.getLookAngle();
                    Vec3 pushBack = new Vec3(-look.x, -look.y, -look.z).normalize();
                    player.push(pushBack.x * (1.75), pushBack.y * (1.75), pushBack.z * (1.75));

                    List<Entity> nearbyEntities = level.getEntities(player, new AABB(player.getOnPos()).inflate(5.0, 5.0, 5.0));
                    if (nearbyEntities.stream().filter(entity -> entity instanceof Mob).filter(entity -> ((Mob) entity).isDeadOrDying()).count() >= 10 && player instanceof ServerPlayer serverPlayer) {
                        JNECriteriaTriggers.KILLED_WITH_PUMP_CHARGE.trigger(serverPlayer);
                    }
                }
            }
        }
        return InteractionResultHolder.fail(stack);
    }

    public void performShooting(Level level, LivingEntity user, ItemStack stack) {
        this.fireFlag = true;

        int chargeCount = getCharge(stack) * 6;
        int chargeInaccuracy = getCharge(stack) * 8;
        int recoil = EnchantmentHelper.getItemEnchantmentLevel(JNEEnchantments.RECOIL.get(), stack);
        int artemis = EnchantmentHelper.getItemEnchantmentLevel(JNEEnchantments.ARTEMIS.get(), stack);
        // Bonuses
        int aBulletDistanceBonus = artemis / 5;
        double recoilPushBonus = (double) recoil / 16;
        double chargePushBonus = (double) getCharge(stack) / 10;

        Vec3 look = user.getLookAngle();
        Vec3 pushBack = new Vec3(-look.x, -look.y, -look.z).normalize();
        int maxCount = 4 + chargeCount;
        int minCount = Math.max(1, (4 + (getCharge(stack) / 2)));
        int count = Math.max(minCount, maxCount);
        if (!level.isClientSide) {
            for (int i = 0; i < count; i++) {
                SoulBullet soulBullet = new SoulBullet(level, user);
                soulBullet.shoot(look.x, look.y, look.z, (1.0F + aBulletDistanceBonus), (5 + chargeInaccuracy));
                level.addFreshEntity(soulBullet);
            }
        }
        Vec3 raycastStart = user.getEyePosition(1.0F);
        Vec3 raycastEnd = raycastStart.add(user.getViewVector(1.0F).scale(5));
        AABB aabb = new AABB(raycastStart, raycastEnd);
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(level, user, raycastStart, raycastEnd, aabb, (entity) -> entity instanceof LivingEntity && entity != user);
        if (entityHitResult != null && entityHitResult.getEntity() instanceof LivingEntity livingEntity && livingEntity.isAlive()) {
            user.push(pushBack.x * (0.75 + recoilPushBonus + chargePushBonus), pushBack.y * (0.75 + recoilPushBonus + chargePushBonus), pushBack.z * (0.75 + recoilPushBonus + chargePushBonus));
        } else {
            user.push(pushBack.x * (0.3 + recoilPushBonus + chargePushBonus), pushBack.y * (0.3 + recoilPushBonus + chargePushBonus), pushBack.z * (0.3 + recoilPushBonus + chargePushBonus));
        }
    }

    @Override
    public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
        return (stack) -> stack.is(JNEItems.WRAITHING_FLESH.get());
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    public static int getCharge(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        return nbt.getInt("Charge");
    }

    public static void setCharge(ItemStack stack, int i) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putInt("Charge", i);
        nbt.putInt("CustomModelData", i);
    }

    private void playPumpAnimation(ItemStack stack, int tickCount) {
        if (this.pumpTimeOut == 20) {
            this.pumpAnimationState.startIfStopped(tickCount, stack);
        }
        if (this.pumpTimeOut > 0) {
            --this.pumpTimeOut;
        }
        if (this.pumpTimeOut <= 0) {
            this.pumpAnimationState.stop(stack);
            this.pumpTimeOut = 20;
            this.pumpFlag = false;
        }
    }

    private void playFireAnimation(ItemStack stack, int tickCount) {
        if (this.fireTimeOut == 20) {
            this.fireAnimationState.startIfStopped(tickCount, stack);
        }
        if (this.fireTimeOut > 0) {
            --this.fireTimeOut;
        }
        if (this.fireTimeOut <= 0) {
            this.fireAnimationState.stop(stack);
            this.fireTimeOut = 20;
            this.fireFlag = false;
        }
    }
}
