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
package com.aegis.commerce.application.application.command;

import com.aegis.commerce.bus.CommandHandler;
import com.aegis.commerce.application.api.command.LoginUser;
import com.aegis.commerce.application.api.command.LoginUserResult;
import com.aegis.commerce.application.api.dto.LoginUserDto;
import com.aegis.commerce.application.application.UserAssembler;
import com.aegis.commerce.application.application.service.JwtService;
import com.aegis.commerce.application.application.service.PasswordEncoder;
import com.aegis.commerce.application.domain.model.User;
import com.aegis.commerce.application.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import static com.aegis.commerce.application.application.exception.Exceptions.badRequest;
import static com.aegis.commerce.application.application.exception.Exceptions.unauthorized;

@RequiredArgsConstructor
@Singleton
public class LoginUserHandler implements CommandHandler<LoginUserResult, LoginUser> {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public LoginUserResult handle(LoginUser command) {
        LoginUserDto data = command.getUser();
        User user = userRepository.findByEmail(data.getEmail())
                .orElseThrow(() -> badRequest("user [email=%s] does not exist", data.getEmail()));

        if (!passwordEncoder.matches(data.getPassword(), user.getPassword())) {
            throw unauthorized("user [email=%s] password is incorrect", data.getEmail());
        }

        return new LoginUserResult(UserAssembler.assemble(user, jwtService));
    }

}
