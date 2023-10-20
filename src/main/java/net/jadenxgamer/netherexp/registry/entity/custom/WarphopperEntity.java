package net.jadenxgamer.netherexp.registry.entity.custom;

import net.jadenxgamer.netherexp.registry.sound.ModSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class WarphopperEntity
extends AnimalEntity
implements GeoEntity {
    @SuppressWarnings("all")
    private AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);

    public WarphopperEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 15.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2);
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new EscapeDangerGoal(this, 1.25));
        this.goalSelector.add(3, new FleeGoal<>(this, PlayerEntity.class, 16.0f, 1.8, 2.8));
        this.goalSelector.add(3, new FleeGoal<>(this, PiglinEntity.class, 10.0f, 1.8, 2.8));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 0.8, 1.0000001E-5f));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(6, new LookAroundGoal(this));
    }

    public EntityGroup getGroup() {
        return EntityGroup.ARTHROPOD;
    }

    static class FleeGoal<T extends LivingEntity>
    extends FleeEntityGoal<T> {

        public FleeGoal(WarphopperEntity warphopper, Class<T> fleeFromType, float distance, double slowSpeed, double fastSpeed) {
            super(warphopper, fleeFromType, distance, slowSpeed, fastSpeed);
        }
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @SuppressWarnings("all")
    private PlayState predicate(AnimationState animationState) {
        if(animationState.isMoving()) {
            animationState.getController().setAnimation(RawAnimation.begin().then("animation.warphopper.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        else {
            animationState.getController().setAnimation(RawAnimation.begin().then("animation.warphopper.idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller",
                0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.ENTITY_WARPHOPPER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSoundEvents.ENTITY_WARPHOPPER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.ENTITY_WARPHOPPER_DEATH;
    }

    //TODO: Make a Custom Step Sound
    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_HOGLIN_STEP, 0.15f, 1.0f);
    }
}