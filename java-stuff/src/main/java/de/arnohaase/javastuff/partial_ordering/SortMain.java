package de.arnohaase.javastuff.partial_ordering;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author arno
 */
public class SortMain {
    public static void main(String[] args) {
        final List<String> jobs = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e"));
        final Map<String, Collection<String>> required = new HashMap<>();
        required.put("a", Arrays.asList("b", "c"));
        required.put("b", Arrays.asList("d"));
        required.put("c", Collections.<String>emptyList());
        required.put("d", Collections.<String>emptyList());
        required.put("e", Arrays.asList("d"));

        Collections.sort(jobs, new Comparator<String>() {
            @Override public int compare(String o1, String o2) {
                if(required.get(o1).contains(o2)) {
                    return 1;
                }
                if(required.get(o2).contains(o1)) {
                    return -1;
                }
                return 0;
            }
        });

        System.out.println(jobs);

        final List<String> sorted = sort(jobs, new PartialOrder<String>() {
            @Override public Collection<String> getPrerequisites(String o) {
                return required.get(o);
            }
        });

        System.out.println(sorted);
    }

    static <T> List<T> sort(List<T> l, PartialOrder<T> order) {
        final List<T> result = new ArrayList<>(l.size());

        final Set<T> sorted = new HashSet<>();
        final List<T> unsorted = new CopyOnWriteArrayList<>(l);
        while(! unsorted.isEmpty()) {
            boolean modified = false;
            for(T o: unsorted) {
                if(sorted.containsAll(order.getPrerequisites(o))) {
                    unsorted.remove(o);
                    sorted.add(o);
                    result.add(o);
                    modified = true;
                }
            }
            if(!modified) {
                throw new IllegalArgumentException("cyclic dependency");
            }
        }

        return result;
    }
}

interface PartialOrder <T> {
    Collection<T> getPrerequisites(T o);
}