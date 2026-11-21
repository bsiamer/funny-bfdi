package xyz.imcodist.funnybfdi.accessor;

import java.util.UUID;

public interface BFDIRenderState {
    UUID bfdi$getUuid();
    void bfdi$setUuid(UUID uuid);

    float bfdi$getHealth();
    void bfdi$setHealth(float health);

    boolean bfdi$hasAbsorption();
    void bfdi$setAbsorption(boolean hasAbsorption);

    boolean bfdi$hasPoison();
    void bfdi$setPoison(boolean hasPoison);

    boolean bfdi$isSubmergedInWater();
    void bfdi$setSubmergedInWater(boolean submerged);

    int bfdi$getHurtTime();
    void bfdi$setHurtTime(int hurtTime);
}
