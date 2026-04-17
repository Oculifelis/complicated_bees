/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.accbdd.complicated_bees.util.forge;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Equivalent to {@link Consumer}, except with nonnull contract.
 *
 * @see Consumer
 */
@FunctionalInterface
@Deprecated
public interface NonNullConsumer<T>
{
	void accept(@NotNull T t);
}