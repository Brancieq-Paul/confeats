package fr.paulbrancieq.confeats.client.gui.impl;

import com.google.common.collect.ImmutableSet;
import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionFlag;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.impl.utils.YACLConstants;
import fr.paulbrancieq.accessoptions.commons.exeptions.AccessOptionsException;
import fr.paulbrancieq.confeats.client.gui.api.YACLOption;
import fr.paulbrancieq.confeats.Confeats;
import fr.paulbrancieq.confeats.client.gui.api.YACLBinding;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class YACLOptionImpl<T> implements YACLOption<T> {
  private final Text name;
  private OptionDescription description;
  private final Controller<T> controller;
  private final YACLBinding<T> binding;
  private boolean available;
  private T pendingValue;
  private final ImmutableSet<OptionFlag> flags;
  private final List<BiConsumer<Option<T>, T>> listeners;
  private int listenerTriggerDepth = 0;
  private Boolean instant;

  public YACLOptionImpl (
      @NotNull Text name,
      @NotNull Function<T, OptionDescription> descriptionFunction,
      @NotNull Function<Option<T>, Controller<T>> controlGetter,
      @NotNull YACLBinding<T> binding,
      boolean available,
      boolean instant
  ) {
    this.name = name;
    this.binding = binding;
    this.available = available;
    this.instant = instant;

    this.pendingValue = binding.getValue();
    this.controller = controlGetter.apply(this);
    this.flags = ImmutableSet.copyOf(new HashSet<>());
    this.listeners = new ArrayList<>();

    addListener((opt, pending) -> description = descriptionFunction.apply(pending));
    triggerListeners(true);
  }

  @Override
  public @NotNull Text name() {
    return name;
  }

  @Override
  public @NotNull OptionDescription description() {
    return this.description;
  }

  @Override
  public @NotNull Text tooltip() {
    return description.text();
  }

  @Override
  public @NotNull Controller<T> controller() {
    return controller;
  }

  @Override
  public @NotNull YACLBinding<T> binding() {
    return binding;
  }

  @Override
  public boolean available() {
    return available;
  }

  @Override
  public void setAvailable(boolean available) {
    boolean changed = this.available != available;

    this.available = available;

    if (changed) {
      if (!available) {
        this.pendingValue = binding().getValue();
      }
      this.triggerListeners(!available);
    }
  }

  @Override
  public @NotNull ImmutableSet<OptionFlag> flags() {
    return flags;
  }

  @Override
  public boolean changed() {
    return !binding().getOriginalValue().equals(pendingValue);
  }

  @Override
  public @NotNull T pendingValue() {
    return pendingValue;
  }

  @Override
  public void requestSet(@NotNull T value) {
    Validate.notNull(value, "`value` cannot be null");

    try {
      binding().modifyValue(value);
      pendingValue = value;

    } catch (AccessOptionsException.PendingOptionNotDifferent ignored) {
    } catch (Exception e) {
      setAvailable(false);
      Confeats.getLogger().error("Exception whilst modifying value for option '%s'".formatted(name.getString()), e);
    }
    this.triggerListeners(true);
  }

  @Override
  public boolean applyValue() {
    throw new NotImplementedException("Not implemented, should not be called. Likely a bug from Confeats mod.");
  }

  @Override
  public void forgetPendingValue() {
    requestSet(binding().getOriginalValue());
  }

  @Override
  public void requestSetDefault() {
    requestSet(binding().defaultValue());
  }

  @Override
  public boolean isPendingValueDefault() {
    return binding().defaultValue().equals(pendingValue());
  }

  @Override
  public void addListener(BiConsumer<Option<T>, T> changedListener) {
    this.listeners.add(changedListener);
  }

  private void triggerListeners(boolean bypass) {
    if (bypass || listenerTriggerDepth == 0) {
      if (listenerTriggerDepth > 10) {
        throw new IllegalStateException("Listener trigger depth exceeded 10! This means a listener triggered a listener etc etc 10 times deep. This is likely a bug in the mod using YACL!");
      }

      this.listenerTriggerDepth++;

      for (BiConsumer<Option<T>, T> listener : listeners) {
        try {
          listener.accept(this, pendingValue);
        } catch (Exception e) {
          YACLConstants.LOGGER.error("Exception whilst triggering listener for option '%s'".formatted(name.getString()), e);
        }
      }

      this.listenerTriggerDepth--;
    }
  }

  public static class BuilderImpl<T> implements YACLOption.Builder<T> {
    private Text name = Text.literal("Name not specified!").formatted(Formatting.RED);

    private Function<T, OptionDescription> descriptionFunction = pending -> OptionDescription.EMPTY;

    private Function<Option<T>, Controller<T>> controlGetter;

    private YACLBinding<T> binding;

    private boolean available = true;

    private boolean instant = false;

    @Override
    public YACLOption.Builder<T> name(@NotNull Text name) {
      Validate.notNull(name, "`name` cannot be null");

      this.name = name;
      return this;
    }

    @Override
    public YACLOption.Builder<T> description(@NotNull OptionDescription description) {
      return description(opt -> description);
    }

    @Override
    public YACLOption.Builder<T> description(@NotNull Function<T, OptionDescription> descriptionFunction) {
      this.descriptionFunction = descriptionFunction;
      return this;
    }

    @Override
    @SuppressWarnings("all")
    public YACLOption.Builder<T> controller(@NotNull Function<Option<T>, ControllerBuilder<T>> controllerBuilder) {
      Validate.notNull(controllerBuilder, "`controllerBuilder` cannot be null");

      return customController(opt -> controllerBuilder.apply(opt).build());
    }

    @Override
    public YACLOption.Builder<T> customController(@NotNull Function<Option<T>, Controller<T>> control) {
      Validate.notNull(control, "`control` cannot be null");

      this.controlGetter = control;
      return this;
    }

    @Override
    public YACLOption.Builder<T> binding(@NotNull YACLBinding<T> binding) {
      Validate.notNull(binding, "`binding` cannot be null");

      this.binding = binding;
      return this;
    }

    @Override
    public YACLOption.Builder<T> available(boolean available) {
      this.available = available;
      return this;
    }

    @Override
    public YACLOption.Builder<T> instant(boolean instant) {
      this.instant = instant;
      return this;
    }

    @Override
    public YACLOption<T> build() {
      Validate.notNull(controlGetter, "`control` must not be null when building `Option`");
      Validate.notNull(binding, "`binding` must not be null when building `Option`");

      return new YACLOptionImpl<T>(name, descriptionFunction, controlGetter, binding, available, instant);
    }
  }
}
