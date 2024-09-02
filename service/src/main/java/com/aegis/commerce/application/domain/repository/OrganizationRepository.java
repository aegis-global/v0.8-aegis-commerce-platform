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
package com.aegis.commerce.application.domain.repository;

import com.aegis.commerce.application.domain.model.Organization;
import com.aegis.commerce.application.domain.model.Membership;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository {

    Optional<Organization> findBySlug(String slug);

    Optional<Organization> findByName(String name);

    List<Organization> findByFilters(String tag, String author, String favorited, Integer limit, Integer offset);

    List<Organization> findByFollowees(List<UUID> followees, Integer limit, Integer offset);

    void delete(Organization organization);

    List<Organization> getMembers(List<UUID> members, Integer limit, Integer offset);

    Organization save(Organization organization);
}
