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
import com.aegis.commerce.application.api.command.AddMember;
import com.aegis.commerce.application.api.command.AddMemberResult;
import com.aegis.commerce.application.api.command.CreateMembership;
import com.aegis.commerce.application.api.command.CreateMembershipResult;
import com.aegis.commerce.application.api.command.DeleteMembership;
import com.aegis.commerce.application.api.command.DeleteMember;
import com.aegis.commerce.application.api.command.UpdateMembership;
import com.aegis.commerce.application.api.command.UpdateMembershipResult;
import com.aegis.commerce.application.api.operation.MembershipOperations;
import com.aegis.commerce.application.api.query.GetMembership;
import com.aegis.commerce.application.api.query.GetMembershipResult;
import com.aegis.commerce.application.api.query.GetMemberships;
import com.aegis.commerce.application.api.query.GetMembershipsResult;
import com.aegis.commerce.application.api.query.GetMember;
import com.aegis.commerce.application.api.query.GetMemberResult;
import com.aegis.commerce.application.api.query.GetMembers;
import com.aegis.commerce.application.api.query.GetMembersResult;
import com.aegis.commerce.application.api.query.GetFeed;
import com.aegis.commerce.application.api.query.GetFeedResult;
import com.aegis.commerce.application.application.service.AuthenticationService;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.annotation.Controller;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;

@RequiredArgsConstructor
@Tag(name = "Memberships")
@Controller("${api.version}")
public class MembershipController implements MembershipOperations {

    private final Bus bus;
    private final AuthenticationService auth;

    @Override
    public CreateMembershipResult create(@Valid CreateMembership command) {
        return bus.executeCommand(command.withCurrentUsername(auth.currentUsername()));
    }

    @Override
    public GetFeedResult feed(Integer limit, Integer offset) {
        return bus.executeQuery(new GetFeed(auth.currentUsername(), limit, offset));
    }

    @Override
    public GetMembershipResult findBySlug(String slug) {
        return bus.executeQuery(new GetMembership(auth.currentUsername(), slug));
    }

    @Override
    public void deleteBySlug(String slug) {
        bus.executeCommand(new DeleteMembership(auth.currentUsername(), slug));
    }

    @Override
    public GetMembersResult findAllMembers(String slug) {
        return bus.executeQuery(new GetMembers(auth.currentUsername(), slug));
    }

    @Override
    public GetMemberResult findMember(String slug, Long id) {
        return bus.executeQuery(new GetMember(auth.currentUsername(), slug, id));
    }

    @Override
    public AddMemberResult addMember(String slug, @Valid AddMember command) {
        return bus.executeCommand(command.withCurrentUsername(auth.currentUsername()).withSlug(slug));
    }

    @Override
    public void deleteMember(String slug, Long id) {
        bus.executeCommand(new DeleteMember(auth.currentUsername(), slug, id));
    }

}
