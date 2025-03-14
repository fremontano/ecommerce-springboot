package com.ecommerce.sb_ecom.exceptions;

public class ResourceNotFoundException extends RuntimeException {

     String resourceName;
     String field;
     String fielName;
     Long fieldId;


     public ResourceNotFoundException(){
     }

     public ResourceNotFoundException(String resourceName, String field, String fielName) {
         super(String.format("Resource %s with %s  %d not found", resourceName, field, fielName));
         this.resourceName = resourceName;
         this.field = field;
         this.fieldId = fieldId;
     }

    public ResourceNotFoundException(String resourceName, String field, Long fieldId) {
         super(String.format("Resource %s with %s = %d not found", resourceName, field, fieldId));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }

}
