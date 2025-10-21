package com.netroaki.chex.client.music;

import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

/** Represents a music track with associated metadata */
public class MusicTrack {
  private final SoundEvent soundEvent;
  private final int minDelay;
  private final int maxDelay;
  private final float volume;
  private final float pitch;
  private final boolean loop;
  private final ResourceLocation id;

  private MusicTrack(Builder builder) {
    this.soundEvent = builder.soundEvent;
    this.minDelay = builder.minDelay;
    this.maxDelay = builder.maxDelay;
    this.volume = builder.volume;
    this.pitch = builder.pitch;
    this.loop = builder.loop;
    this.id = builder.id != null ? builder.id : soundEvent.getLocation();
  }

  public SoundEvent getSoundEvent() {
    return soundEvent;
  }

  public int getMinDelay() {
    return minDelay;
  }

  public int getMaxDelay() {
    return maxDelay;
  }

  public float getVolume() {
    return volume;
  }

  public float getPitch() {
    return pitch;
  }

  public boolean isLoop() {
    return loop;
  }

  public ResourceLocation getId() {
    return id;
  }

  /** Create a sound instance for this track */
  public SoundInstance createInstance() {
    return SimpleSoundInstance.forMusic(soundEvent);
  }

  /** Builder for MusicTrack */
  public static class Builder {
    private final SoundEvent soundEvent;
    private int minDelay = 6000; // 5 minutes
    private int maxDelay = 12000; // 10 minutes
    private float volume = 1.0f;
    private float pitch = 1.0f;
    private boolean loop = false;
    private ResourceLocation id;

    public Builder(SoundEvent soundEvent) {
      this.soundEvent = soundEvent;
    }

    public Builder withDelays(int minDelay, int maxDelay) {
      this.minDelay = minDelay;
      this.maxDelay = maxDelay;
      return this;
    }

    public Builder withVolume(float volume) {
      this.volume = volume;
      return this;
    }

    public Builder withPitch(float pitch) {
      this.pitch = pitch;
      return this;
    }

    public Builder withLoop(boolean loop) {
      this.loop = loop;
      return this;
    }

    public Builder withId(ResourceLocation id) {
      this.id = id;
      return this;
    }

    public MusicTrack build() {
      return new MusicTrack(this);
    }
  }
}
