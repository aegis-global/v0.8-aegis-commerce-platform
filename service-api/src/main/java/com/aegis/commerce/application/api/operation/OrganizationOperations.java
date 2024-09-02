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

import com.aegis.commerce.application.api.command.AddComment;
import com.aegis.commerce.application.api.command.AddCommentResult;
import com.aegis.commerce.application.api.command.CreateOrganization;
import com.aegis.commerce.application.api.command.CreateOrganizationResult;
import com.aegis.commerce.application.api.command.FavoriteOrganizationResult;
import com.aegis.commerce.application.api.command.UnfavoriteOrganizationResult;
import com.aegis.commerce.application.api.command.UpdateOrganization;
import com.aegis.commerce.application.api.command.UpdateOrganizationResult;
import com.aegis.commerce.application.api.query.GetOrganizationResult;
import com.aegis.commerce.application.api.query.GetOrganizationsResult;
import com.aegis.commerce.application.api.query.GetCommentResult;
import com.aegis.commerce.application.api.query.GetCommentsResult;
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

public interface OrganizationOperations {

    @Get("/organizations{?tag,owner,favorited,limit,offset}")
    GetOrganizationsResult findByFilters(@QueryValue(value = "tag") @Nullable String tag,
                                    @QueryValue(value = "owner") @Nullable String owner,
                                    @QueryValue(value = "favourited") @Nullable String favorited,
                                    @QueryValue(value = "limit", defaultValue = "20") Integer limit,
                                    @QueryValue(value = "offset", defaultValue = "0") Integer offset);

    @Status(HttpStatus.CREATED)
    @Post("/organizations")
    CreateOrganizationResult create(@Valid @Body CreateOrganization command);

    @Get("/organizations/feed")
    GetFeedResult feed(@QueryValue(value = "limit", defaultValue = "20") Integer limit,
                       @QueryValue(value = "offset", defaultValue = "0") Integer offset);

    @Get("/organizations/{slug}")
    GetOrganizationResult findBySlug(@PathVariable("slug") String slug);

    @Put("/organizations/{slug}")
    UpdateOrganizationResult updateBySlug(@PathVariable("slug") String slug, @Valid @Body UpdateOrganization command);

    @Status(HttpStatus.NO_CONTENT)
    @Delete("/organizations/{slug}")
    void deleteBySlug(@PathVariable("slug") String slug);

    @Post("/organizations/{slug}/favorite")
    FavoriteOrganizationResult favorite(@PathVariable("slug") String slug);

    @Delete("/organizations/{slug}/favorite")
    UnfavoriteOrganizationResult unfavorite(@PathVariable("slug") String slug);

    @Get("/organizations/{slug}/comments")
    GetCommentsResult findAllComments(@PathVariable("slug") String slug);

    @Get("/organizations/{slug}/comments/{id}")
    GetCommentResult findComment(@PathVariable("slug") String slug, @PathVariable("id") Long id);

    @Post("/organizations/{slug}/comments")
    AddCommentResult addComment(@PathVariable("slug") String slug, @Valid @Body AddComment command);

    @Status(HttpStatus.NO_CONTENT)
    @Delete("/organizations/{slug}/comments/{id}")
    void deleteComment(@PathVariable("slug") String slug, @PathVariable("id") Long id);

}
