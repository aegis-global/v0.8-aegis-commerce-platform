/*
 * MIT License
 *
 * Copyright (c) 2020 - present Aegis GIS, LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.aegis.commerce.application.rest;

import com.aegis.commerce.application.api.command.LoginUser;
import com.aegis.commerce.application.api.command.RegisterUser;
import com.aegis.commerce.application.api.command.UpdateUser;
import com.aegis.commerce.application.api.dto.LoginUserDto;
import com.aegis.commerce.application.api.dto.RegisterUserDto;
import com.aegis.commerce.application.api.dto.UpdateUserDto;
import com.aegis.commerce.application.api.dto.UserDto;
import com.aegis.commerce.application.api.operation.UserClient;
import com.aegis.commerce.application.rest.auth.AuthSupport;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.aegis.commerce.application.rest.auth.AuthSupport.register;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

@MicronautTest
public class UserApiTest {

    public static final String ALTERED_EMAIL = "altered-email@example.com";
    public static final String ALTERED_USERNAME = "altered-username";
    public static final String ALTERED_PASSWORD = "altered-password";
    public static final String ALTERED_BIO = "altered-bio";
    public static final String ALTERED_IMAGE = "altered-image";

    @Inject
    private UserClient userClient;

    @Test
    void should_returnCorrectData_whenRegisterUser() {
        RegisterUser command = registerCommand();

        UserDto user = userClient.register(command).getUser();

        assertThat(user.getUsername()).isEqualTo(command.getUser().getUsername());
        assertThat(user.getEmail()).isEqualTo(command.getUser().getEmail());
        assertThat(user.getToken()).isNotBlank();
    }

    @Test
    void should_returnCorrectData_whenLoginUser() {
        RegisterUser command = registerCommand();

        userClient.register(command);

        LoginUserDto data = new LoginUserDto(command.getUser().getEmail(), command.getUser().getPassword());
        UserDto user = userClient.login(new LoginUser(data)).getUser();

        assertThat(user.getToken()).isNotBlank();
    }

    @Test
    void should_returnCorrectData_whenLoginUserWithWrongPassword() {
        RegisterUser command = registerCommand();

        userClient.register(command);

        LoginUserDto data = new LoginUserDto(command.getUser().getEmail(), UUID.randomUUID().toString());
        HttpClientResponseException exception = catchThrowableOfType(
                () -> userClient.login(new LoginUser(data)),
                HttpClientResponseException.class
        );

        assertThat(exception.getStatus().getCode()).isEqualTo(HttpStatus.UNAUTHORIZED.getCode());
    }

    @Test
    void should_return400_whenLoginUser() {
        String s = UUID.randomUUID().toString();
        HttpClientResponseException exception = catchThrowableOfType(
                () -> userClient.login(new LoginUser(new LoginUserDto(s + "@ex.com", s))),
                HttpClientResponseException.class
        );

        assertThat(exception.getStatus().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());
    }

    @Test
    void should_returnCorrectData_whenUpdateUser() {
        register().login();

        UpdateUserDto data = UpdateUserDto.builder()
                .email(ALTERED_EMAIL)
                .username(ALTERED_USERNAME)
                .password(ALTERED_PASSWORD)
                .bio(ALTERED_BIO)
                .image(ALTERED_IMAGE)
                .build();

        UserDto user = userClient.update(new UpdateUser(null, data)).getUser();

        assertThat(user.getEmail()).isEqualTo(ALTERED_EMAIL);
        assertThat(user.getUsername()).isEqualTo(ALTERED_USERNAME);
        assertThat(user.getBio()).isEqualTo(ALTERED_BIO);
        assertThat(user.getImage()).isEqualTo(ALTERED_IMAGE);
    }

    @Test
    void should_throw400_whenUpdateUserWithExistingEmail() {
        AuthSupport.RegisteredUser registeredUser = register();

        register().login();

        UpdateUserDto data = UpdateUserDto.builder()
                .email(registeredUser.getEmail())
                .build();

        HttpClientResponseException exception = catchThrowableOfType(
                () -> userClient.update(new UpdateUser(null, data)),
                HttpClientResponseException.class
        );

        assertThat(exception).isNotNull();
    }

    @Test
    void should_throw400_whenUpdateUserWithExistingName() {
        AuthSupport.RegisteredUser registeredUser = register();

        register().login();

        UpdateUserDto data = UpdateUserDto.builder()
                .username(registeredUser.getUsername())
                .build();

        HttpClientResponseException exception = catchThrowableOfType(
                () -> userClient.update(new UpdateUser(null, data)),
                HttpClientResponseException.class
        );

        assertThat(exception).isNotNull();
    }

    private static RegisterUser registerCommand() {
        RegisterUserDto data = RegisterUserDto.builder()
                .username(UUID.randomUUID().toString())
                .email(UUID.randomUUID().toString() + "@ex.com")
                .password(UUID.randomUUID().toString())
                .build();
        return new RegisterUser(data);
    }

}
