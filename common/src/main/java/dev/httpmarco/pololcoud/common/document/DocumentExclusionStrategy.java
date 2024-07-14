package dev.httpmarco.pololcoud.common.document;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class DocumentExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(@NotNull FieldAttributes f) {
        return f.getAnnotation(DocumentIgnore.class) != null;
    }

    @Contract(pure = true)
    @Override
    public boolean shouldSkipClass(@NotNull Class<?> clazz) {
        return clazz.isAnnotationPresent(DocumentIgnore.class);
    }
}
