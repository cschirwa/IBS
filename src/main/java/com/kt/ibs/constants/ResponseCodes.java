package com.kt.ibs.constants;

public enum ResponseCodes {
    CODE_SUCCESS(200),
    CODE_ERROR(400),
    CODE_WARN(100),
    CODE_INFO(201),
    CODE_EXCEPTION(500);

    private final int responseLevel;

    ResponseCodes(int responseLevel) {
        this.responseLevel = responseLevel;
    }

    /**
     * Get the initialised response-code from the enum.
     *
     * @return initialised response-code.
     */
    public int getResponseLevel() {
        return this.responseLevel;
    }

    @Override
    public String toString() {
        return String.valueOf(getResponseLevel());
    }
}
