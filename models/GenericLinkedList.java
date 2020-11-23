package models;

import java.util.Collection;

/**
 * A generic linked list class.
 * Inspiration for this class came from lobster1234 on Github.
 * https://gist.github.com/lobster1234/5037064
 *
 * Implemented here by Kyle.
 * @param <T>
 */
public class GenericLinkedList<T> {

    private Item start = null;

    public void insert(Item<T> node){
        node.setNext(start);
        start = node;
    }

    public void remove(){
        if (start.getNext() != null) {
            start = start.getNext();
        } else {
            start = null;
        }
    }

    private class Item<T>{
        private final T VALUE;
        private Item<T> next;

        public Item(T _value){
            this.VALUE = _value;
        }

        public void setNext(Item<T> _next){
            this.next = _next;
        }

        public Item<T> getNext(){
            return this.next;
        }

        public T getValue(){
            return this.VALUE;
        }
    }

}
