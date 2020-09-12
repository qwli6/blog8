package me.lqw.blog8.model.dto;

import me.lqw.blog8.model.Moment;

import java.io.Serializable;

public class MomentNavDTO implements Serializable {

    private Moment prev;

    private Moment next;

    public MomentNavDTO() {
        super();
    }

    public MomentNavDTO(Moment prev, Moment next) {
        super();
        this.prev = prev;
        this.next = next;
    }

    public Moment getPrev() {
        return prev;
    }

    public void setPrev(Moment prev) {
        this.prev = prev;
    }

    public Moment getNext() {
        return next;
    }

    public void setNext(Moment next) {
        this.next = next;
    }
}
