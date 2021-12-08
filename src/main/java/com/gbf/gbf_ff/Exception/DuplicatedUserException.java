package com.gbf.gbf_ff.Exception;

public class DuplicatedUserException extends RuntimeException {
    public DuplicatedUserException(){}
    public DuplicatedUserException(String msg){
        super(msg);
    }

}