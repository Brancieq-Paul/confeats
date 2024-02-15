package fr.paulbrancieq.confeats.client.gui.impl;

import fr.paulbrancieq.accessoptions.OptionsAccessHandler;
import fr.paulbrancieq.accessoptions.commons.options.Option;
import fr.paulbrancieq.confeats.client.gui.api.ThrowConsumer;
import fr.paulbrancieq.confeats.client.gui.api.YACLBinding;
import org.apache.commons.lang3.NotImplementedException;

import java.util.function.Supplier;

public class YACLBindingImpl<S, T> implements YACLBinding<T> {
  private final T def;
  private final Supplier<T> getter;
  private final Supplier<T> originalValueGetter;
  private final ThrowConsumer<T> setter;
  private final OptionsAccessHandler optionsAccessHandler;

  public YACLBindingImpl(T def, Option<S, T> option, OptionsAccessHandler optionsAccessHandler) {
    this.optionsAccessHandler = optionsAccessHandler;
    this.def = def;
    this.getter = option::getPendingValue;
    this.originalValueGetter = option::getOriginalValue;
    this.setter = (newValue) -> this.optionsAccessHandler.modifyOption(option, newValue);
  }

  @Override
  public void setValue(T value) {
    throw new NotImplementedException("Not implemented. Should not be called. Likely a bug in the mod Confeats.");
  }

  @Override
  public void modifyValue(T value) throws Exception {
    setter.accept(value);
  }

  @Override
  public T getValue() {
    return getter.get();
  }
  @Override
  public T getOriginalValue() {
    return originalValueGetter.get();
  }

  @Override
  public T defaultValue() {
    return def;
  }
}
