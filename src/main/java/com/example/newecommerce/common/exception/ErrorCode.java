package com.example.newecommerce.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "계정 정보를 찾을 수 없습니다"),
    OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "재고가 부족합니다"),
    //서버에러
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 에러가 발생했습니다"),

    INSUFFICIENT_CASH(HttpStatus.BAD_REQUEST, "잔액이 충분하지 않습니다"),
    ORDERINFO_NOT_FIND(HttpStatus.NOT_FOUND,  "주문정보를 찾을 수 없습니다"),
    CARTINFO_NOT_FIND(HttpStatus.NOT_FOUND, "장바구니 정보를 찾을 수 없습니다"),
    PRODUCT_NOT_FIND(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다"),
    CART_ADD_FAIL(HttpStatus.NOT_FOUND, "장바구니 상품을 추가할 수 없습니다"),
    CART_DELETE_FAIL(HttpStatus.NOT_FOUND, "장바구니 상품을 삭제할 수 없습니다"),

    //요청값이 잘못됨
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "요청이 잘못되었습니다");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }


}
