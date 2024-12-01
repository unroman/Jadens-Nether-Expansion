package net.jadenxgamer.netherexp.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.jadenxgamer.netherexp.registry.misc_registry.JNETags;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Shadow protected int sprintTriggerTime;

    @Shadow public Input input;

    @WrapOperation(
            method = "aiStep",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/Input;leftImpulse:F", opcode = Opcodes.PUTFIELD)
    )
    private void netherexp$leftImpulse(Input instance, float value, Operation<Void> original) {
        LocalPlayer player = ((LocalPlayer) (Object) this);
        if (player.getMainHandItem().is(JNETags.Items.DOESNT_SLOWDOWN_WHEN_USING)) {
            input.leftImpulse *= 1.0f;
        } else {
            original.call(instance, value);
        }
    }

    @WrapOperation(
            method = "aiStep",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/Input;forwardImpulse:F", opcode = Opcodes.PUTFIELD)
    )
    private void netherexp$forwardImpulse(Input instance, float value, Operation<Void> original) {
        LocalPlayer player = ((LocalPlayer) (Object) this);
        if (player.getMainHandItem().is(JNETags.Items.DOESNT_SLOWDOWN_WHEN_USING)) {
            input.forwardImpulse *= 1.0f;
        } else {
            original.call(instance, value);
        }
    }

    @WrapOperation(
            method = "aiStep",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;sprintTriggerTime:I", opcode = Opcodes.PUTFIELD, ordinal = 1)
    )
    private void netherexp$sprintTriggerTime(LocalPlayer instance, int value, Operation<Void> original) {
        LocalPlayer player = ((LocalPlayer) (Object) this);
        if (player.getMainHandItem().is(JNETags.Items.DOESNT_SLOWDOWN_WHEN_USING)) {
            // we do absolutely nothing so that the player can still sprint
        } else {
            original.call(instance, value);
        }
    }
}
