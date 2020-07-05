package com.kt.ibs.constants;

import java.util.function.Predicate;

public class Constants {

    public static final String DELIMITER_ERL_ENTRY_ID = "{erlEntryId}";
    public static final String DEFAULT_DT_ERRORMESSAGE = "DT Error";
    public static final String RESPONSE_MESSAGE_ERROR = "Validation errors!";
    public static final String REPONSE_MESSAGE_SUCCESS = "Success!";
    public static final int EXCEPTIONERROR = 500;
    public static final String RESPONSE_MESSAGE_SUCCESS = "Success!";
    public static final String RESPONSE_MESSAGE_WARNING = "Warning!";
    public static final String RESPONSE_MESSAGE_INFO = "Information!";
    public static final String DEFAULT_REQUIRED_ERROR_MESSAGE = "Field is required";
    public static final String LEVEL_ERROR = "ERROR";
    public static final String MESSAGE_EXCEPTION = "An exceptions has occurred in ";
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_ERROR = 400;
    public static final int CODE_WARN = 100;
    public static final int CODE_INFO = 201;
    public static final String FIRSTNAME = "Firstname";
    public static final String BENEFICIARY_FULLNAME = "Beneficiary name";
    public static final String BENEFICIARY = "Beneficiary";
    public static final String ACCOUNT = "Account";
    public static final String BENEFICIARY_REFERENCE = "Beneficiary Reference";
    public static final String SENDER_REFERENCE = "Sender Reference";
    public static final String SYSTEM_ERROR = "System Error!";
    public static final String LASTNAME = "Lastname";
    public static final String COUNTRY_OF_RESIDENCE = "Country of Residence";
    public static final String COUNTRY_OF_ISSUE = "Country of Issue";
    public static final String IDENTITY_TYPE = "Identity Type";
    public static final String IDENTITY_NUMBER = "Identity Number";
    public static final String PHONE_NUMBER = "Phone number";
    public static final String USERNAME = "Username";
    public static final String PASSWORD = "Password";
    public static final String RECIPIENT_COUNTRY = "Recipient Country";

    public static final String DEFAULTREQUIREDERRORMESSAGE = "Field is required";
    public static final String PASSWORD_COMPLEXITY_MESSAGE = "Password should be a combination of letters and numbers, and between 6 and 20 characters in length";
    public static final String PHONE_NUMBER_FORMAT_MESSAGE = "Phone number should be 10 digits starting with a 0";
    public static final String LEVELERROR = "ERROR";
    public static final int CODEERROR = 400;

    public static final String ACCOUNT_NAME = "Account name";
    public static final String CUSTOMER_ID = "Customer ID";
    public static final String PREFERRED_NAME = "Preferred name";
    public static final String EMAIL_ADDRESS = "Email Address";
    public static final String POSITION_TYPE = "Position type";
    public static final String CURRENCY = "Currency";
    public static final String ACCOUNT_TITLE_1 = "Account Title";
    public static final String ACCOUNT_CATEGORY = "Account Category";

    public static final String ACCOUNT_NUMBER = "Account number";
    public static final String BANK_NAME = "Bank name";
    public static final String BANK_BRANCH = "Bank branch";
    public static final String ACCOUNT_TYPE = "Account Type";

    public static final String VALID = "Valid";
    public static final String INSUFFICIENT_FUNDS = "Selected account has insufficient funds for transfer!";
    public static final String TRANSFER_DETAILS = "Account number or transfer amount invalid";
    public static final String NATIONALITY = "Nationality";
    public static final String CURRENCY_CODE = "Currency code";
    public static final String CURRENCY_DESCRIPTION = "Currency description";
    public static final String CURRENCY_MIN_ALLOWED = "Currency minimum amount allowed";
    public static final String CURRENCY_MAX_ALLOWED = "Currency maximum amount allowed";
    public static final String CONVERSION_RATE = "Conversion rate";
    public static final String SOURCE_ACCOUNT = "Source Account";
    public static final String DESTINATION_ACCOUNT = "Destination Account";
    public static final String CUSTOMER_AUDIT = "Customer Audit details";
    public static final String AMOUNT = "Amount";
    public static final String RECIPIENT = "Recipient";
    public static final Predicate<String> checkEmpty = (value -> value == null || value.isEmpty() || "".equals(value));
}
