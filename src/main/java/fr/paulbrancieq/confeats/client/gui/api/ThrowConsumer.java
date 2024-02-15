package fr.paulbrancieq.confeats.client.gui.api;

public interface ThrowConsumer<T> {
  void accept(T t) throws Exception;
}
