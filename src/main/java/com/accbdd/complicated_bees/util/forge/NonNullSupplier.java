/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.accbdd.complicated_bees.util.forge;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Equivalent to {@link Supplier}, except with nonnull contract.
 *
 * @see Supplier
 */
@FunctionalInterface
@Deprecated
public interface NonNullSupplier<T>
{
	@NotNull T get();
}