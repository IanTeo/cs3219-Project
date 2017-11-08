package logic.utility;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import model.Paper;

public class CollectionUtility {
    /**
     * Returns the {@code collection} of elements that matches {@code predicate}.
     */
    public static <T> Collection<T> removeFromCollection(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * Returns a function that accepts 2 collections and merges the contents of both collections.
     */
    public static BiFunction<Collection<Paper>, Collection<Paper>, Collection<Paper>> mergeLists() {
        return (collectionOne, collectionTwo) -> Stream.of(collectionOne, collectionTwo)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
