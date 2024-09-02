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
package com.aegis.commerce.application.infrastructure.web;

import com.aegis.commerce.bus.Bus;
import com.aegis.commerce.application.api.command.LoginUser;
import com.aegis.commerce.application.api.command.LoginUserResult;
import com.aegis.commerce.application.api.command.RegisterUser;
import com.aegis.commerce.application.api.command.RegisterUserResult;
import com.aegis.commerce.application.api.command.UpdateUser;
import com.aegis.commerce.application.api.command.UpdateUserResult;
import com.aegis.commerce.application.api.operation.UserOperations;
import com.aegis.commerce.application.api.query.GetCurrentUser;
import com.aegis.commerce.application.api.query.GetCurrentUserResult;
import com.aegis.commerce.application.application.service.AuthenticationService;
import io.micronaut.http.annotation.Controller;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;

@RequiredArgsConstructor
@Tag(name = "Users")
@Controller("${api.version}")
public class UserController implements UserOperations {

    private final Bus bus;
    private final AuthenticationService auth;

    @Override
    public LoginUserResult login(@Valid LoginUser command) {
        return bus.executeCommand(command);
    }

    @Override
    public RegisterUserResult register(@Valid RegisterUser command) {
        return bus.executeCommand(command);
    }

    @Override
    public GetCurrentUserResult current() {
        return bus.executeQuery(new GetCurrentUser(auth.currentUsername()));
    }

    @Override
    public UpdateUserResult update(@Valid UpdateUser command) {
        return bus.executeCommand(command.withCurrentUsername(auth.currentUsername()));
    }

}
