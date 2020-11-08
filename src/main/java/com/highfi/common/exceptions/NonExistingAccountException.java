package com.highfi.common.exceptions;



public class NonExistingAccountException extends RuntimeException {

	 public NonExistingAccountException(String message) {
		 super(message);
	 }
}
