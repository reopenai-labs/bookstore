package com.reopenai.bookstore.component.lambda;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Extends the capabilities of {@link Function} by adding serialization support.
 *
 * @author Allen Huang
 * @see Function
 */
@FunctionalInterface
public interface XFunction<T, R> extends Function<T, R>, Serializable {

}