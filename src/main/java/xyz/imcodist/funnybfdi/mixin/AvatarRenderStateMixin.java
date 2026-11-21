package xyz.imcodist.funnybfdi.mixin;

import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xyz.imcodist.funnybfdi.accessor.BFDIRenderState;

import java.util.UUID;

@Mixin(AvatarRenderState.class)
public class AvatarRenderStateMixin implements BFDIRenderState {
    @Unique
    private UUID bfdi$uuid;
    @Unique
    private float bfdi$health = 20.0f;
    @Unique
    private boolean bfdi$absorption;
    @Unique
    private boolean bfdi$poison;
    @Unique
    private boolean bfdi$submergedInWater;
    @Unique
    private int bfdi$hurtTime;

    @Override
    public UUID bfdi$getUuid() {
        return this.bfdi$uuid;
    }

    @Override
    public void bfdi$setUuid(UUID uuid) {
        this.bfdi$uuid = uuid;
    }

    @Override
    public float bfdi$getHealth() {
        return this.bfdi$health;
    }

    @Override
    public void bfdi$setHealth(float health) {
        this.bfdi$health = health;
    }

    @Override
    public boolean bfdi$hasAbsorption() {
        return this.bfdi$absorption;
    }

    @Override
    public void bfdi$setAbsorption(boolean absorption) {
        this.bfdi$absorption = absorption;
    }

    @Override
    public boolean bfdi$hasPoison() {
        return this.bfdi$poison;
    }

    @Override
    public void bfdi$setPoison(boolean poison) {
        this.bfdi$poison = poison;
    }

    @Override
    public boolean bfdi$isSubmergedInWater() {
        return this.bfdi$submergedInWater;
    }

    @Override
    public void bfdi$setSubmergedInWater(boolean submergedInWater) {
        this.bfdi$submergedInWater = submergedInWater;
    }

    @Override
    public int bfdi$getHurtTime() {
        return this.bfdi$hurtTime;
    }

    @Override
    public void bfdi$setHurtTime(int hurtTime) {
        this.bfdi$hurtTime = hurtTime;
    }
}
