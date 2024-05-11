package net.jadenxgamer.netherexp.registry.entity.custom;

import net.jadenxgamer.netherexp.registry.advancements.JNECriteriaTriggers;
import net.jadenxgamer.netherexp.registry.entity.JNEEntityType;
import net.jadenxgamer.netherexp.registry.misc_registry.JNESoundEvents;
import net.jadenxgamer.netherexp.registry.particle.JNEParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;

public class Vessel extends Monster implements RangedAttackMob {

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState aimAnimationState = new AnimationState();
    public final AnimationState aimIdleAnimationState = new AnimationState();
    public final AnimationState shootAnimationState = new AnimationState();

    private static final EntityDataAccessor<Boolean> IS_AIMING = SynchedEntityData.defineId(Vessel.class, EntityDataSerializers.BOOLEAN);

    public Vessel(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, 1.0F);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            if (this.isMoving()) {
                idleAnimationState.stop();
                walkAnimationState.startIfStopped(this.tickCount);
            }
            else {
                walkAnimationState.stop();
                idleAnimationState.startIfStopped(this.tickCount);
            }
            if (getIsAiming()) {
                aimIdleAnimationState.startIfStopped(this.tickCount);
            }
            else {
                aimIdleAnimationState.stop();
            }
        }
    }

    private boolean isMoving() {
        return this.getDeltaMovement().horizontalDistance() > 0.01F;
    }

    @Override
    public void aiStep() {
        if (this.isInWaterOrRain()) {
            this.doExorcism();
        }
        super.aiStep();
    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.UNDEAD;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FOLLOW_RANGE, 12.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(3, new VesselAttackGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float f) {
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), JNESoundEvents.SHOTGUN_USE.get(), this.getSoundSource(), 1.0F, 1.0F);
        int count = Mth.nextInt(this.level().random, 16, 20);
        Vec3 look = this.getLookAngle();
        double d0 = livingEntity.getX() - this.getX();
        double d2 = livingEntity.getZ() - this.getZ();
        for (int i = 0; i < count; i++) {
            SoulBullet soulBullet = new SoulBullet(this.getX(), this.getY() + 1.5, this.getZ(), this.level(), this);
            soulBullet.shoot(d0, look.y, d2, 1.0F, 16);
            this.level().addFreshEntity(soulBullet);
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if (damageSource.getDirectEntity() instanceof ThrownPotion thrownPotion && hurtWithCleanWater(thrownPotion)) {
            doExorcism();
            if (thrownPotion.getOwner() instanceof Player player) {
                JNECriteriaTriggers.EXORCISM.trigger((ServerPlayer) player);
            }
        }
        return super.hurt(damageSource, f);
    }

    private void doExorcism() {
        Skeleton skeleton = this.convertTo(EntityType.SKELETON, false);
        if (skeleton != null) {
            skeleton.finalizeSpawn((ServerLevel)this.level(), this.level().getCurrentDifficultyAt(skeleton.blockPosition()), MobSpawnType.CONVERSION, new Zombie.ZombieGroupData(false, false), null);
            skeleton.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
            skeleton.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 2));
            skeleton.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
            if (this.hasCustomName()) {
                skeleton.setCustomName(skeleton.getCustomName());
            }
        }
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.NEUTRAL, 1.0f, 1.0f);
        for(int i = 0; i < 10; ++i) {
            this.level().addParticle(JNEParticleTypes.WISP.get(), this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0.0, 0.0, 0.0);
        }
    }

    private boolean hurtWithCleanWater(ThrownPotion thrownPotion) {
        ItemStack itemStack = thrownPotion.getItem();
        Potion potion = PotionUtils.getPotion(itemStack);
        List<MobEffectInstance> list = PotionUtils.getMobEffects(itemStack);
        return potion == Potions.WATER && list.isEmpty();
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
        if (this.level().getDifficulty() == Difficulty.EASY) {
            for (int i = 0; i < this.level().getRandom().nextInt(2) + 1; i++) {
                Wisp wisp = JNEEntityType.WISP.get().create(this.level());
                if (wisp != null) {
                    wisp.setBoredDelay(0);
                    wisp.setPos(this.getX(), this.getY(), this.getZ());
                    this.level().addFreshEntity(wisp);
                }
            }
        }
        else {
            Apparition apparition = JNEEntityType.APPARITION.get().create(this.level());
            if (apparition != null) {
                apparition.setPos(this.getX(), this.getY(), this.getZ());
                if (this.getTarget() != null) {
                    apparition.setTarget(this.getTarget());
                }
            }
            this.level().addFreshEntity(apparition);
        }
    }

    /////////
    // NBT //
    /////////

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(IS_AIMING, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("IsAiming")) {
            this.setIsAiming(nbt.getBoolean("IsAiming"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("IsAiming", this.getIsAiming());
    }

    private boolean getIsAiming() {
        return this.getEntityData().get(IS_AIMING);
    }

    private void setIsAiming(boolean bl) {
        this.getEntityData().set(IS_AIMING, bl);
    }

    ////////////
    // SOUNDS //
    ////////////

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
        this.playSound(SoundEvents.SKELETON_STEP, 0.15F, 1.0F);
    }

    ////////
    // AI //
    ////////

    private static class VesselAttackGoal extends Goal {
        private final Vessel vessel;
        private int attackTime;
        private boolean shot;
        private int seeTime;
        private boolean strafingClockwise;
        private boolean strafingBackwards;
        private int strafingTime;

        public VesselAttackGoal(Vessel vessel) {
            this.vessel = vessel;
            this.attackTime = 60;
            this.strafingTime = -1;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity livingEntity = this.vessel.getTarget();
            return livingEntity != null && livingEntity.isAlive() && this.vessel.canAttack(livingEntity);
        }

        @Override
        public boolean canContinueToUse() {
            return !shot && super.canContinueToUse();
        }

        @Override
        public void start() {
            this.attackTime = 60;
            super.start();
        }

        @Override
        public void stop() {
            this.shot = false;
            vessel.setIsAiming(false);
            this.seeTime = 0;
            this.attackTime = 60;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            --this.attackTime;
            LivingEntity target = this.vessel.getTarget();
            if (target != null) {
                boolean hasSight = this.vessel.getSensing().hasLineOfSight(target);
                double distance = this.vessel.distanceToSqr(target);
                double attackRadius = this.getFollowDistance() * this.getFollowDistance();
                boolean hadSightLastTick = this.seeTime > 0;
                if (hasSight != hadSightLastTick) {
                    this.seeTime = 0;
                }

                if (hasSight) {
                    ++this.seeTime;
                } else {
                    --this.seeTime;
                }

                if (!(distance > attackRadius) && this.seeTime >= 20) {
                    this.vessel.getNavigation().stop();
                    ++this.strafingTime;
                } else {
                    this.vessel.getNavigation().moveTo(target, 1.0);
                    this.strafingTime = -1;
                }

                if (this.strafingTime >= 20) {
                    if ((double)this.vessel.getRandom().nextFloat() < 0.3) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }

                    if ((double)this.vessel.getRandom().nextFloat() < 0.3) {
                        this.strafingBackwards = !this.strafingBackwards;
                    }

                    this.strafingTime = 0;
                }

                if (this.strafingTime > -1) {
                    if (distance > attackRadius * 0.75F) {
                        this.strafingBackwards = false;
                    } else if (distance < attackRadius * 0.25F) {
                        this.strafingBackwards = true;
                    }

                    this.vessel.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    Entity entity = this.vessel.getControlledVehicle();
                    if (entity instanceof Mob mob) {
                        mob.lookAt(target, 30.0F, 30.0F);
                    }

                    this.vessel.lookAt(target, 30.0F, 30.0F);
                } else {
                    this.vessel.getLookControl().setLookAt(target, 30.0F, 30.0F);
                }

                if (distance < attackRadius && hasSight) {
                    if (this.attackTime == 40) {
                        vessel.aimAnimationState.startIfStopped(vessel.tickCount);
                        vessel.setIsAiming(true);
                    } else if (this.attackTime <= 0) {
                        this.shot = true;
                        vessel.setIsAiming(false);
                        vessel.shootAnimationState.startIfStopped(vessel.tickCount);
                        vessel.performRangedAttack(target, 1.0f);
                    }
                }
            }

            super.tick();
        }

        private double getFollowDistance() {
            return this.vessel.getAttributeValue(Attributes.FOLLOW_RANGE);
        }
    }
}