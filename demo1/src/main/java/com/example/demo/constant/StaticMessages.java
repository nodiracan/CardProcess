package com.example.demo.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StaticMessages {


    public static final String USER_NOT_FOUND = "User Not Found: ";
    public static final String USER_ALREADY_EXIST = "User Already Exist: ";
    public static final String CARD_NOT_FOUND = "Card with such id not exists in processing.: ";
    public static final String BLOCK_CARD = "Response confirming the card has been blocked";
    public static final String UNBLOCK_CARD = "Response confirming the card has been blocked";
    public static final String KEY_NOT_FOUND = "Idempotemcy Key not found";
    public static final String INSUFFICIENT_FUNDS = "Insufficient funds";
}
