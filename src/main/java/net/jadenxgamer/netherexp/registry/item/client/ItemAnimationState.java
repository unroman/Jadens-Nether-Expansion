package net.jadenxgamer.netherexp.registry.item.client;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class ItemAnimationState {
    private static final long STOPPED = Long.MAX_VALUE;
    private long lastTime = Long.MAX_VALUE;
    private long accumulatedTime;
    private ItemStack stack;

    public ItemAnimationState() {
    }

    public void start(int pTickCount, ItemStack stack) {
        this.lastTime = (long)pTickCount * 1000L / 20L;
        this.accumulatedTime = 0L;
        this.stack = stack;
    }

    public void startIfStopped(int pTickCount, ItemStack stack) {
        if (!this.isStarted()) {
            this.start(pTickCount, stack);
        }

    }

    public void animateWhen(boolean p_252220_, int p_249486_, ItemStack stack) {
        if (p_252220_) {
            this.startIfStopped(p_249486_, stack);
        } else {
            this.stop(stack);
        }

    }

    public void stop(ItemStack stack) {
        this.lastTime = Long.MAX_VALUE;
    }

    public void ifStarted(Consumer<ItemAnimationState> pAction) {
        if (this.isStarted()) {
            pAction.accept(this);
        }

    }

    public void updateTime(float pAgeInTicks, float pSpeed) {
        if (this.isStarted()) {
            long $$2 = Mth.lfloor(pAgeInTicks * 1000.0F / 20.0F);
            this.accumulatedTime += (long)((float)($$2 - this.lastTime) * pSpeed);
            this.lastTime = $$2;
        }
    }

    public long getAccumulatedTime() {
        return this.accumulatedTime;
    }

    public boolean isStarted() {
        return this.lastTime != Long.MAX_VALUE;
    }

    public boolean matchesStack(ItemStack stack) {
        return this.stack == stack;
    }
}
