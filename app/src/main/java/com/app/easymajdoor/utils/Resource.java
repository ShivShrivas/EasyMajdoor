package com.app.easymajdoor.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Objects;

public class Resource<T, V> {

    @NonNull
    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final V error;

    private Resource(
        @NonNull Status status,
        @Nullable T data,
        @Nullable V error
    ) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T, V> Resource<T, V> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T, V> Resource<T, V> success() {
        return new Resource<>(Status.SUCCESS, null, null);
    }

    public static <T, V> Resource<T, V> error(V error) {
        return new Resource<>(Status.ERROR, null, error);
    }

    @Nullable
    public V getError() {
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource<?, ?> resource = (Resource<?, ?>) o;
        return status == resource.status && Objects.equals(data, resource.data) && Objects.equals(error, resource.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, data, error);
    }
}