package fr.paulbrancieq.confeats.client.gui.api;

import fr.paulbrancieq.accessoptions.commons.exeptions.AccessOptionsException;

public interface ThrowConsumer<T> {
  void accept(T t) throws AccessOptionsException.PendingOptionNotDifferent, Exception;
}
