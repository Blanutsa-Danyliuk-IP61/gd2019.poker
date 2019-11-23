package gd2019.poker.model;

import java.util.LinkedList;

/**
 * @author Mykola Danyliuk
 */
public class CircularList<E> extends LinkedList<E> {

    public CircularList() {
        super();
    }

    @Override
    public E get(int index) {
        return super.get(index % size());
    }
}
