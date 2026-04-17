/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.accbdd.complicated_bees.util.forge;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * Equivalent to {@link Predicate}, except with nonnull contract.
 *
 * @see Predicate
 */
@FunctionalInterface
@Deprecated
public interface NonNullPredicate<T>
{
	boolean test(@NotNull T t);
}