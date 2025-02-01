package com.ecommerce.sb_ecom.Exceptions;

public class RessourceNotFoundException extends RuntimeException{
    String ressourceName;
    String field;
    String fieldName;
    Long fieldId;

    public RessourceNotFoundException(String ressourceName, String field, String fieldName) {
        super(String.format("%s not found with %s : %s", ressourceName, field, fieldName));
        this.ressourceName = ressourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public RessourceNotFoundException(String ressourceName, String field, Long fieldId) {
        super(String.format("%s not found with %s : %d", ressourceName, field, fieldId));
        this.ressourceName = ressourceName;
        this.field = field;
        this.fieldId = fieldId;
    }

    public RessourceNotFoundException() {
    }
}
