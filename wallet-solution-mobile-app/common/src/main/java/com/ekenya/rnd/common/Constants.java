package com.ekenya.rnd.common;

public class Constants {
    /**
     * The Base Package ID path for the application module
     *
     * The app module and all other modules should be relative to this path
     * E.G if base = 'com.ekenya.rnd', then app id would be 'com.ekenya.rnd.app', then module id, like support should be 'com.ekenya.rnd.support'
     */
    public static String BASE_PACKAGE_NAME = "com.ekenya.rnd";
    public static int MY_PERMISSIONS_REQUEST_READ_STORAGE = 101;

    //public static String BASE_URL = "https://demo-api.ekenya.co.ke/api/";
    public static String BASE_URL2 = "https://demo-api.ekenya.co.ke/botswana-wallet/api/";
    public static String BASE_URL_CBG_DEMO = "https://demo-api.ekenya.co.ke/cbg/mobile/";
    public static String BASE_URL_CBG_DEMO2 = "https://test-api.ekenya.co.ke/cbg/";

    public static String FIRST_NAME = "first_name";
    public static String TOLLO_OTP = "tollo-otp";
    public static String DEVICE_TOKEN = "device_token";
    public static String MIDDLE_NAME = "middle_name";
    public static int CAMERA = 2;
    public static String ALGORITHM="RSA";
    public static String ACCOUNT_LOOKUP_SERVICE="AccountLookup";
    public static String  RSA_PADDING="RSA/ECB/OAEPWithSHA-1AndMGF1Padding";
    public static String PUBLIC_KEY_ADD_CARD = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsnEX7NzS6Pl3VVf8soedldzSy50p4tadrnzYGi2yXh7jECfm9ZQD8yJ65LoHKoiAl32TCqO7aLfwNaCNvWnm6LALDxVOaUatF4/CS2LSCiE/fAfzSNtDzqruc3YeUqBtaXYjZHUoaHONOHltPib8FwQHw6+C37kjFhsQ42ySDLeyGkMoTrvStST9abZ1QlvS3jhq3ka/dUvRafcijfkMc7QH5y1b6snPyQSVr6bjnGp/tQ2CyeUdMHieF7eryE3DNLMuFlNoP/izmIGnL8cenOpxASD4GPIK772ekcLTSFZ7p1gWLxHtt7yOn3+HICEYC+/SMSkJ4UzCKVpVSii4xQIDAQAB";



   // public static String PUBLIC_KEY_ADD_CARD="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsnEX7NzS6Pl3VVf8soedldzSy50p4tadrnzYGi2yXh7jECfm9ZQD8yJ65LoHKoiAl32TCqO7aLfwNaCNvWnm6LALDxVOaUatF4/CS2LSCiE/fAfzSNtDzqruc3YeUqBtaXYjZHUoaHONOHltPib8FwQHw6+C37kjFhsQ42ySDLeyGkMoTrvStST9abZ1QlvS3jhq3ka/dUvRafcijfkMc7QH5y1b6snPyQSVr6bjnGp/tQ2CyeUdMHieF7eryE3DNLMuFlNoP/izmIGnL8cenOpxASD4GPIK772ekcLTSFZ7p1gWLxHtt7yOn3+HICEYC+/SMSkJ4UzCKVpVSii4xQIDAQAB;
    public static int  REQUEST_IMAGE_CAPTURE = 2;

    public static String SURNAME = "surname";
    public static String EMAIL_ADDRESS = "email_address";
    public static String ID_NUMBER = "id_number";
    public static String DOB = "dob";
    public static String PHONE_NUMBER = "phone_number";
    public static String MERCHANT_CODE = "merchant_code";
    public static String Luggage = "luggage";
    public static String CARD_NUMBER = "card_number";
    public static String IMEI = "imei";
    public static String PASSWORD = "password";
    public static String PIN = "pin";
    public static String INITIALPIN = "initial_pin";
    public static String OTP_TOKEN = "otp_token";
    public static String AMOUNT_TO_TOPUP = "amount_to_topup";
    public static String DEPOSIT_AMOUNT = "deposit_amount";
    public static String ACCESS_TOKEN = "access_token";
    public static String ACCOUNT_NUMBER = "account_number";
    public static String SAVINGSACCOUNTS_NUMBER = "savings_account_number";
    public static String PROFILE_PHOTO = "profile_picture";
    public static String ACCOUNT_BALANCE = "account_balance";
    public static String RAW_BALANCE = "raw_balance";
    public static String GRANT_TYPE = "grant_type";
    public static String GEOLOCATION = "geolocation";
    public static String USER_AGENT_VERSION = "user_agent_version";
    public static String USER_AGENT = "user_agent";
    public static String ENCRYPTED_CARD = "encrypted_card";
    public static String MyPREFERENCES = "MyPrefs";
    public static String IS_FIRST_TIME_USER = "is_first_time_user";
    public static String IS_AMHARIC_SELECTED = "is_amharic_selected";
    public static String HAS_SET_DATE = "has_set_date";
    public static String IS_FIRST_TIME_AT_LANDING = "is_first_time_user";
    public static String HAS_REACHED_HOMEPAGE = "has_reached_homepage";
    public static String IS_REGISTERED_USER = "is_first_time_user";
    public static String HaS_SET_INITIAL_PIN = "has_set_initial_pin";

    public static String HAS_FINISHED_SLIDERS = "has_finished_sliders";
    public static String DSTV_PAYMENTS_FRAGMENT = "dstvpayments";
    public static String BOTSWANA_POWER_FRAGMENT = "botswanapowerpayments";
    public static String PAY_MERCHANT_FRAGMENT = "paymercnant";
    public static String TICKETING_FRAGMENT = "ticketing";
    public static String ADD_SAVINGS_FRAGMENT = "add_savings";
    public static String EXPORT_STATEMENTS = "export_statements";
    public static String WITHDRAW_FRAGMENT = "withdraw_fragment";
    public static String TOP_WALLET_FRAGMENT = "topup_wallet_fragment";
    public static String PAY_TRAFFIC_FINE = "pay_fine_fragment";
    public static String DO_CARD_PAYMENTS = "card_payments";
    public static String SEND_TO_WALLET = "send_to_wallet";
    public static String SEND_TO_BANK = "send_to_bank";
    public static String SEND_TO_MOBILE = "send_to_mobile";
    public static String BUY_AIRTIME_FRAGMENT = "orange_airtime";
    public static String MASCOM_AIRTIME_FRAGMENT = "mascom_airtime";
    public static  String DEBIT_OR_CREDIT = "Debit Or Credit Card";
    public static  String MOBILE_MONEY = "mobile_money";
    public static  String MARI_WALLET = "Tollo Cash Wallet";


}
