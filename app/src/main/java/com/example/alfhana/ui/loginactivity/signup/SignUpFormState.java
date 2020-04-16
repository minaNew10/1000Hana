package com.example.alfhana.ui.loginactivity.signup;

import androidx.annotation.Nullable;

public class SignUpFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;

    private Integer emailError;

    private boolean isDataValid;

    SignUpFormState(@Nullable Integer usernameError, @Nullable Integer passwordError,Integer emailError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.emailError = emailError;
        this.isDataValid = false;
    }

    SignUpFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.emailError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    public Integer getEmailError() {
        return emailError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
