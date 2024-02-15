package fr.paulbrancieq.confeats.client.gui.api;

import dev.isxander.yacl3.api.Binding;
import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import fr.paulbrancieq.accessoptions.commons.options.Option;
import fr.paulbrancieq.confeats.client.gui.impl.YACLOptionImpl;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface YACLOption<T> extends dev.isxander.yacl3.api.Option<T> {
  static <T> YACLOption.Builder<T> createBuilder(Option<?, T> option) {
    return new YACLOptionImpl.BuilderImpl<T>()
        .name(option.getDisplayName())
        .description(OptionDescription.of(Text.of(option.getDescription())))
        .instant(option.getReloaders().isEmpty());
  }

  interface Builder<T> {
    /**
     * Sets the name to be used by the option.
     *
     * @see dev.isxander.yacl3.api.Option#name()
     */
    YACLOption.Builder<T> name(@NotNull Text name);

    /**
     * Sets the description to be used by the option.
     * @see OptionDescription
     * @param description the static description.
     * @return this builder
     */
    YACLOption.Builder<T> description(@NotNull OptionDescription description);

    /**
     * Sets the function to get the description by the option's current value.
     *
     * @see OptionDescription
     * @param descriptionFunction the function to get the description by the option's current value.
     * @return this builder
     */
    YACLOption.Builder<T> description(@NotNull Function<T, OptionDescription> descriptionFunction);

    YACLOption.Builder<T> controller(@NotNull Function<dev.isxander.yacl3.api.Option<T>, ControllerBuilder<T>> controllerBuilder);

    /**
     * Sets the controller for the option.
     * This is how you interact and change the options.
     *
     * @see dev.isxander.yacl3.gui.controllers
     */
    YACLOption.Builder<T> customController(@NotNull Function<dev.isxander.yacl3.api.Option<T>, Controller<T>> control);

    /**
     * Sets the binding for the option.
     * Used for default, getter and setter.
     *
     * @see Binding
     */
    YACLOption.Builder<T> binding(@NotNull YACLBinding<T> binding);

    /**
     * Sets if the option can be configured
     *
     * @see dev.isxander.yacl3.api.Option#available()
     */
    YACLOption.Builder<T> available(boolean available);

    /**
     * Instantly invokes the binder's setter when modified in the GUI.
     * Prevents the user from undoing the change
     * <p>
     * Does not support {@link dev.isxander.yacl3.api.Option#flags()}!
     */
    YACLOption.Builder<T> instant(boolean instant);
    YACLOption<T> build();
  }
}
