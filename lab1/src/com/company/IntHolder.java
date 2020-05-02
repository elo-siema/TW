package com.company;

public class IntHolder
{
    public Integer value;

    IntHolder(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}