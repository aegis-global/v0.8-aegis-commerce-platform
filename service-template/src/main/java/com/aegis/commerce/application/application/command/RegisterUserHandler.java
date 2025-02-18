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
import com.aegis.commerce.application.api.command.RegisterUser;
import com.aegis.commerce.application.api.command.RegisterUserResult;
import com.aegis.commerce.application.api.dto.RegisterUserDto;
import com.aegis.commerce.application.application.UserAssembler;
import com.aegis.commerce.application.application.service.JwtService;
import com.aegis.commerce.application.application.service.PasswordEncoder;
import com.aegis.commerce.application.domain.model.User;
import com.aegis.commerce.application.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

import static com.aegis.commerce.application.application.exception.Exceptions.badRequest;

@RequiredArgsConstructor
@Singleton
public class RegisterUserHandler implements CommandHandler<RegisterUserResult, RegisterUser> {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public RegisterUserResult handle(RegisterUser command) {
        RegisterUserDto data = command.getUser();
        Optional<User> userByEmailOptional = userRepository.findByEmail(data.getEmail());
        if (userByEmailOptional.isPresent()) {
            throw badRequest("user [email=%s] already exists", data.getEmail());
        }
        Optional<User> userByUsernameOptional = userRepository.findByUsername(data.getUsername());
        if (userByUsernameOptional.isPresent()) {
            throw badRequest("user [name=%s] already exists", data.getUsername());
        }

        User user = User.builder()
                .id(UUID.randomUUID())
                .username(data.getUsername())
                .email(data.getEmail())
                .password(passwordEncoder.encode(data.getPassword()))
                .build();
        userRepository.save(user);

        return new RegisterUserResult(UserAssembler.assemble(user, jwtService));
    }

}
