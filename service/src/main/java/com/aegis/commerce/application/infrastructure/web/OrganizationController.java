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
import com.aegis.commerce.application.api.command.AddComment;
import com.aegis.commerce.application.api.command.AddCommentResult;
import com.aegis.commerce.application.api.command.CreateOrganization;
import com.aegis.commerce.application.api.command.CreateOrganizationResult;
import com.aegis.commerce.application.api.command.DeleteOrganization;
import com.aegis.commerce.application.api.command.DeleteComment;
import com.aegis.commerce.application.api.command.FavoriteOrganization;
import com.aegis.commerce.application.api.command.FavoriteOrganizationResult;
import com.aegis.commerce.application.api.command.UnfavoriteOrganization;
import com.aegis.commerce.application.api.command.UnfavoriteOrganizationResult;
import com.aegis.commerce.application.api.command.UpdateOrganization;
import com.aegis.commerce.application.api.command.UpdateOrganizationResult;
import com.aegis.commerce.application.api.operation.OrganizationOperations;
import com.aegis.commerce.application.api.query.GetOrganization;
import com.aegis.commerce.application.api.query.GetOrganizationResult;
import com.aegis.commerce.application.api.query.GetOrganizations;
import com.aegis.commerce.application.api.query.GetOrganizationsResult;
import com.aegis.commerce.application.api.query.GetComment;
import com.aegis.commerce.application.api.query.GetCommentResult;
import com.aegis.commerce.application.api.query.GetComments;
import com.aegis.commerce.application.api.query.GetCommentsResult;
import com.aegis.commerce.application.api.query.GetFeed;
import com.aegis.commerce.application.api.query.GetFeedResult;
import com.aegis.commerce.application.application.service.AuthenticationService;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.annotation.Controller;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;

@RequiredArgsConstructor
@Tag(name = "Organizations")
@Controller("${api.version}")
public class OrganizationController implements OrganizationOperations {

    private final Bus bus;
    private final AuthenticationService auth;

    @Override
    public GetOrganizationsResult findByFilters(@Nullable String tag,
                                           @Nullable String owner,
                                           @Nullable String favorited,
                                           Integer limit,
                                           Integer offset) {
        return bus.executeQuery(new GetOrganizations(auth.currentUsername(), tag, owner, favorited, limit, offset));
    }

    @Override
    public CreateOrganizationResult create(@Valid CreateOrganization command) {
        return bus.executeCommand(command.withCurrentUsername(auth.currentUsername()));
    }

    @Override
    public GetFeedResult feed(Integer limit, Integer offset) {
        return bus.executeQuery(new GetFeed(auth.currentUsername(), limit, offset));
    }

    @Override
    public GetOrganizationResult findBySlug(String slug) {
        return bus.executeQuery(new GetOrganization(auth.currentUsername(), slug));
    }

    @Override
    public UpdateOrganizationResult updateBySlug(String slug, @Valid UpdateOrganization command) {
        return bus.executeCommand(command.withCurrentUsername(auth.currentUsername()).withSlug(slug));
    }

    @Override
    public void deleteBySlug(String slug) {
        bus.executeCommand(new DeleteOrganization(auth.currentUsername(), slug));
    }

    @Override
    public FavoriteOrganizationResult favorite(String slug) {
        return bus.executeCommand(new FavoriteOrganization(auth.currentUsername(), slug));
    }

    @Override
    public UnfavoriteOrganizationResult unfavorite(String slug) {
        return bus.executeCommand(new UnfavoriteOrganization(auth.currentUsername(), slug));
    }

    @Override
    public GetCommentsResult findAllComments(String slug) {
        return bus.executeQuery(new GetComments(auth.currentUsername(), slug));
    }

    @Override
    public GetCommentResult findComment(String slug, Long id) {
        return bus.executeQuery(new GetComment(auth.currentUsername(), slug, id));
    }

    @Override
    public AddCommentResult addComment(String slug, @Valid AddComment command) {
        return bus.executeCommand(command.withCurrentUsername(auth.currentUsername()).withSlug(slug));
    }

    @Override
    public void deleteComment(String slug, Long id) {
        bus.executeCommand(new DeleteComment(auth.currentUsername(), slug, id));
    }

}
