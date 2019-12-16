package gd2019.poker.dto;

import lombok.Getter;

@Getter
public class SimpleRequest<T> {

    private T data;
}
