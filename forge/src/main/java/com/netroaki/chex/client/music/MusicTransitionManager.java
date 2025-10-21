package com.netroaki.chex.client.music;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

/** Handles smooth transitions between music tracks with crossfading */
public class MusicTransitionManager {
  private static final float FADE_DURATION = 20.0f; // 1 second for fade in/out (in ticks)

  private final Minecraft mc;
  private SoundInstance currentTrack;
  private SoundInstance nextTrack;
  private int fadeTicks = 0;
  private boolean isTransitioning = false;

  public MusicTransitionManager() {
    this.mc = Minecraft.getInstance();
  }

  /** Start a transition to a new music track */
  public void transitionTo(SoundEvent soundEvent, float targetVolume) {
    // If already transitioning, stop the next track that was about to play
    if (isTransitioning && nextTrack != null) {
      mc.getSoundManager().stop(nextTrack);
    }

    // If we have a current track, start fading it out
    if (currentTrack != null) {
      // If the requested track is the same as current, don't do anything
      if (currentTrack.getLocation().equals(soundEvent.getLocation())) {
        return;
      }

      // Start fading out current track
      isTransitioning = true;
      fadeTicks = 0;

      // Create and start the new track at volume 0
      nextTrack = createSoundInstance(soundEvent, 0.0f);
      mc.getSoundManager().play(nextTrack);
    } else {
      // No current track, just play the new one
      currentTrack = createSoundInstance(soundEvent, targetVolume);
      mc.getSoundManager().play(currentTrack);
    }
  }

  /** Stop all music with a fade out */
  public void stop() {
    if (currentTrack != null) {
      // Start fading out current track
      isTransitioning = true;
      fadeTicks = 0;
      nextTrack = null; // No track to transition to
    }
  }

  /** Update the transition state (call every tick) */
  public void tick() {
    if (!isTransitioning) return;

    fadeTicks++;
    float progress = fadeTicks / FADE_DURATION;

    if (progress >= 1.0f) {
      // Transition complete
      if (nextTrack != null) {
        // Fade in complete
        if (currentTrack != null) {
          mc.getSoundManager().stop(currentTrack);
        }
        currentTrack = nextTrack;
        nextTrack = null;
      } else {
        // Fade out complete
        if (currentTrack != null) {
          mc.getSoundManager().stop(currentTrack);
          currentTrack = null;
        }
      }
      isTransitioning = false;
    } else {
      // Update volumes during transition
      if (nextTrack != null) {
        // Fade out current, fade in next
        float fadeOut = 1.0f - Mth.clamp(progress * 2.0f, 0.0f, 1.0f);
        float fadeIn = Mth.clamp((progress - 0.5f) * 2.0f, 0.0f, 1.0f);

        if (currentTrack instanceof FadeableSoundInstance fadeableCurrent) {
          fadeableCurrent.setFade(fadeOut);
        }
        if (nextTrack instanceof FadeableSoundInstance fadeableNext) {
          fadeableNext.setFade(fadeIn);
        }
      } else if (currentTrack != null) {
        // Just fading out current
        float fadeOut = 1.0f - progress;
        if (currentTrack instanceof FadeableSoundInstance fadeableCurrent) {
          fadeableCurrent.setFade(fadeOut);
        }
      }
    }
  }

  /** Check if a transition is in progress */
  public boolean isTransitioning() {
    return isTransitioning;
  }

  private SoundInstance createSoundInstance(SoundEvent soundEvent, float volume) {
    return new FadeableSoundInstance(
        soundEvent,
        SoundSource.MUSIC,
        volume,
        1.0f,
        SoundInstance.createUnsealed(),
        false,
        0,
        SoundInstance.Attenuation.NONE,
        0.0,
        0.0,
        0.0,
        false);
  }

  /** A sound instance that supports volume fading */
  private static class FadeableSoundInstance extends SimpleSoundInstance {
    private float fade = 1.0f;
    private final float baseVolume;

    public FadeableSoundInstance(
        SoundEvent soundEvent,
        SoundSource soundSource,
        float volume,
        float pitch,
        SoundInstance soundInstance,
        boolean loop,
        int delay,
        Attenuation attenuation,
        double x,
        double y,
        double z,
        boolean relative) {
      super(
          soundEvent,
          soundSource,
          volume,
          pitch,
          soundInstance,
          loop,
          delay,
          attenuation,
          x,
          y,
          z,
          relative);
      this.baseVolume = volume;
    }

    public void setFade(float fade) {
      this.fade = Mth.clamp(fade, 0.0f, 1.0f);
    }

    @Override
    public float getVolume() {
      return super.getVolume() * fade;
    }
  }
}
