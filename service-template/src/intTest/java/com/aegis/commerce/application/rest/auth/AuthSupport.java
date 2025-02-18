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
package com.aegis.commerce.application.rest.auth;

import com.aegis.commerce.application.api.command.LoginUser;
import com.aegis.commerce.application.api.command.LoginUserResult;
import com.aegis.commerce.application.api.command.RegisterUser;
import com.aegis.commerce.application.api.dto.LoginUserDto;
import com.aegis.commerce.application.api.dto.RegisterUserDto;
import com.aegis.commerce.application.api.operation.UserClient;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Singleton
public class AuthSupport {

    private static AuthSupport INSTANCE;

    @Inject
    private UserClient userClient;

    @EventListener
    void onStartup(StartupEvent event) {
        INSTANCE = this;
    }

    public static RegisteredUser register() {
        String uuid = UUID.randomUUID().toString();
        return register(uuid, email(uuid), uuid);
    }

    public static RegisteredUser register(String username, String email, String password) {
        RegisterUserDto data = RegisterUserDto.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();
        INSTANCE.userClient.register(new RegisterUser(data));
        return new RegisteredUser(email, username, password);
    }

    private static String email(String uuid) {
        return uuid + "@ex.com";
    }

    public static void login(String cred) {
        login(email(cred), cred);
    }

    public static void login(String email, String password) {
        LoginUserResult result = INSTANCE.userClient.login(new LoginUser(new LoginUserDto(email, password)));
        TokenHolder.token = result.getUser().getToken();
    }

    public static void logout() {
        TokenHolder.token = null;
    }

    public static class TokenHolder {
        public static String token;
    }

    @AllArgsConstructor
    @Getter
    public static class RegisteredUser {
        private String email;
        private String username;
        private String password;

        public RegisteredUser login() {
            AuthSupport.login(email, password);
            return this;
        }
    }

}
