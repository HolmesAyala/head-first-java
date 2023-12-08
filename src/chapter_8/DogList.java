package chapter_8;

import java.lang.reflect.Array;
import java.util.Arrays;

public class DogList<T> {
    private static final int DEFAULT_MAX_LENGTH = 5;
    private T[] items;

    private int nextIndexToAssign;

    public DogList(Class<T> type) {
        this.items = (T[]) Array.newInstance(type, this.DEFAULT_MAX_LENGTH);
        this.nextIndexToAssign = 0;
    }

    public void add(T itemToAdd) {
        if (this.nextIndexToAssign >= DEFAULT_MAX_LENGTH) return;

        int indexToAssign = this.nextIndexToAssign;

        this.items[indexToAssign] = itemToAdd;

        this.nextIndexToAssign++;
    }

    @Override
    public String toString() {
        return Arrays.deepToString(this.items);
    }
}
