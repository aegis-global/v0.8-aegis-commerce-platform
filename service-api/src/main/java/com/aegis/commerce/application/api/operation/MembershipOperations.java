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
package com.aegis.commerce.application.api.operation;

import com.aegis.commerce.application.api.command.AddMember;
import com.aegis.commerce.application.api.command.AddMemberResult;
import com.aegis.commerce.application.api.command.CreateMembership;
import com.aegis.commerce.application.api.command.CreateMembershipResult;
import com.aegis.commerce.application.api.query.GetMembershipResult;
import com.aegis.commerce.application.api.query.GetMemberResult;
import com.aegis.commerce.application.api.query.GetMembersResult;
import com.aegis.commerce.application.api.query.GetFeedResult;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.annotation.Status;

import jakarta.validation.Valid;

public interface MembershipOperations {

    @Status(HttpStatus.CREATED)
    @Post("/memberships")
    CreateMembershipResult create(@Valid @Body CreateMembership command);

    @Get("/memberships/feed")
    GetFeedResult feed(@QueryValue(value = "limit", defaultValue = "20") Integer limit,
                       @QueryValue(value = "offset", defaultValue = "0") Integer offset);

    @Get("/memberships/{slug}")
    GetMembershipResult findBySlug(@PathVariable("slug") String slug);

    @Status(HttpStatus.NO_CONTENT)
    @Delete("/memberships/{slug}")
    void deleteBySlug(@PathVariable("slug") String slug);

    @Get("/memberships/{slug}/members")
    GetMembersResult findAllMembers(@PathVariable("slug") String slug);

    @Get("/memberships/{slug}/members/{id}")
    GetMemberResult findMember(@PathVariable("slug") String slug, @PathVariable("id") Long id);

    @Post("/memberships/{slug}/members")
    AddMemberResult addMember(@PathVariable("slug") String slug, @Valid @Body AddMember command);

    @Status(HttpStatus.NO_CONTENT)
    @Delete("/memberships/{slug}/members/{id}")
    void deleteMember(@PathVariable("slug") String slug, @PathVariable("id") Long id);

}
