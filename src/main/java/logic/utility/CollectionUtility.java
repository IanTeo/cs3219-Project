package logic.utility;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import model.Paper;

public class CollectionUtility {
    public static <T> void removeFromCollection(Collection<T> collection, Predicate<T> predicate) {
        Collection<T> toRetain = collection.stream().filter(predicate).collect(Collectors.toList());
        collection.retainAll(toRetain);
    }

    public static <T> void removeFromCollections(Collection<Collection<T>> outerCollection, Predicate<T> predicate) {
        for (Collection<T> innerCollection : outerCollection) {
            removeFromCollection(innerCollection, predicate);
        }
    }

    public static BiFunction<Collection<Paper>, Collection<Paper>, Collection<Paper>> mergeLists() {
        return (collectionOne, collectionTwo) -> Stream.of(collectionOne, collectionTwo)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
