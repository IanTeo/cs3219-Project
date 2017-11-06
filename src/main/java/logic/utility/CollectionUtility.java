package logic.utility;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import model.Paper;

public class CollectionUtility {
    /**
     * Removes from {@code collection} elements that do not match {@code predicate}.
     */
    public static <T> void removeFromCollection(Collection<T> collection, Predicate<T> predicate) {
        Collection<T> toRetain = collection.stream().filter(predicate).collect(Collectors.toList());
        collection.retainAll(toRetain);
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
