package com.example.aquariux_test.request;

import lombok.NonNull;

public record UserRequest(
       @NonNull String name,
       @NonNull String email) {

}