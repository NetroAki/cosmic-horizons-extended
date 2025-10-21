package com.netroaki.chex.entity.kepler;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FlutterwingEntity extends Animal implements FlyingAnimal {
  public FlutterwingEntity(EntityType<? extends Animal> type, Level level) {
    super(type, level);
    this.noCulling = true;
  }

  @Override
  public boolean isFood(net.minecraft.world.item.ItemStack stack) {
    return false;
  }

  @Override
  public void travel(Vec3 travelVector) {
    if (this.isInWater()) {
      this.moveRelative(0.02F, travelVector);
      this.move(this.getDeltaMovement());
      this.setDeltaMovement(this.getDeltaMovement().scale(0.8));
    } else if (this.isInLava()) {
      this.moveRelative(0.02F, travelVector);
      this.move(this.getDeltaMovement());
      this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
    } else {
      float friction = 0.91F;
      if (this.onGround) {
        friction =
            this.level()
                    .getBlockState(this.getBlockPosBelowThatAffectsMyMovement())
                    .getFriction(this.level(), this.getBlockPosBelowThatAffectsMyMovement(), this)
                * 0.91F;
      }

      float f = 0.16277137F / (friction * friction * friction);
      this.moveRelative(this.onGround ? 0.1F * f : 0.02F, travelVector);

      friction = 0.91F;
      if (this.onGround) {
        friction =
            this.level()
                    .getBlockState(this.getBlockPosBelowThatAffectsMyMovement())
                    .getFriction(this.level(), this.getBlockPosBelowThatAffectsMyMovement(), this)
                * 0.91F;
      }

      this.move(this.getDeltaMovement());
      Vec3 vec3 = this.getDeltaMovement();
      if ((this.horizontalCollision || this.jumping) && this.isInWall()) {
        vec3 = new Vec3(vec3.x, 0.2D, vec3.z);
      }

      this.setDeltaMovement(vec3.multiply(friction, 0.98F, friction));
    }
    this.animationSpeedOld = this.animationSpeed;
    double d1 = this.getX() - this.xo;
    double d0 = this.getZ() - this.zo;
    float f1 = (float) (d1 * d1 + d0 * d0) * 4.0F;
    if (f1 > 1.0F) {
      f1 = 1.0F;
    }

    this.animationSpeed += (f1 - this.animationSpeed) * 0.4F;
    this.animationPosition += this.animationSpeed;
  }

  @Override
  public boolean isFlying() {
    return !this.onGround();
  }
}
