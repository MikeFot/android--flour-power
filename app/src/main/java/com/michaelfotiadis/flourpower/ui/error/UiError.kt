package com.michaelfotiadis.flourpower.ui.error

import androidx.annotation.StringRes
import com.michaelfotiadis.flourpower.R

enum class UiError(@StringRes val messageResId: Int) {

    COMMUNICATION(R.string.ui_error_communication),
    INTERNAL_SERVER_ERROR(R.string.ui_error_internal_server_error),
    UNEXPECTED(R.string.ui_error_unexpected),
    REQUEST_FAILED(R.string.ui_error_request_failed),
    NO_NETWORK(R.string.ui_error_no_network),
    INVALID_REQUEST_PARAMETERS(R.string.ui_error_invalid_request),
    NOT_FOUND(R.string.ui_error_not_found),
    AUTHENTICATION(R.string.ui_error_authentication),
    AUTHORISATION(R.string.ui_error_authorisation),
    BAD_REQUEST(R.string.ui_error_bad_request),
    PAYLOAD_TOO_LARGE(R.string.ui_error_payload_too_large),
    NOT_PERMITTED(R.string.ui_error_no_permission);
}