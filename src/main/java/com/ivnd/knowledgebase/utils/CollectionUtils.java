package com.ivnd.knowledgebase.utils;

import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: tuyen.nguyenvan
 * @email: tuyen.nguyen3@ivnd.com.vn
 * @Date: 02/05/2024
 */
public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static <T, R> List<R> toListWithoutPredicate(Collection<T> collection, Function<T, R> func) {
        return toListWithPredicate(collection, f -> true, func);
    }

    public static <T, R> List<R> toListWithPredicate(Collection<T> collection, Predicate<T> predicate, Function<T, R> func) {
        return toList(func).apply(collection, predicate);
    }

    private static <T, R> BiFunction<Collection<T>, Predicate<T>, List<R>> toList(Function<T, R> func) {
        return (collection, predicate) -> collection.stream()
                .filter(predicate)
                .map(func)
                .collect(Collectors.toList());
    }

    public static <T, R> Stream<R> streamOf(Collection<T> collection, Function<T, R> func) {
        return collection.stream().map(func);
    }

    public static boolean isEmpty(@Nullable Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNonEmpty(@Nullable Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static <T, R> boolean contains(Collection<T> collection, List<R> values, Function<T, ? extends R> valueExtractor) {
        return collection.stream()
                .anyMatch(t -> values.contains(valueExtractor.apply(t)));
    }

    public static <T, R> boolean contains(Collection<T> collection, R value, Function<T, ? extends R> valueExtractor) {
        return contains(collection, Collections.singletonList(value), valueExtractor);
    }

    public static <T> List<T> emptyList() {
        return Collections.emptyList();
    }
}
