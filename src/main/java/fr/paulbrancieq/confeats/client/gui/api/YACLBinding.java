package fr.paulbrancieq.confeats.client.gui.api;

public interface YACLBinding<T> extends dev.isxander.yacl3.api.Binding<T> {
  void modifyValue(T value) throws Exception;
  T getOriginalValue();
}
