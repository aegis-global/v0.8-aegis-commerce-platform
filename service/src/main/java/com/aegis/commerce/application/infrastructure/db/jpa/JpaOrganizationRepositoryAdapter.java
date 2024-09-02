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
package com.aegis.commerce.application.infrastructure.db.jpa;

import com.aegis.commerce.application.domain.model.Organization;
import com.aegis.commerce.application.domain.repository.OrganizationRepository;
import com.aegis.commerce.application.domain.repository.MembershipRepository;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import jakarta.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Singleton
public class JpaOrganizationRepositoryAdapter implements OrganizationRepository {

    private final DataOrganizationRepository repository;

    @Override
    public Optional<Organization> findBySlug(String slug) {
        return repository.findBySlug(slug);
    }

    @Override
    public Optional<Organization> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<Organization> findByFilters(String tag, String owner, String favorited, Integer limit, Integer offset) {
        OffsetBasedPageable pageable = new OffsetBasedPageable(limit, offset, Sort.of(Sort.Order.desc("createdAt")));
        return repository.findByFilters(tag, owner, favorited, pageable);
    }

    @Override
    public List<Organization> findByFollowees(List<UUID> followees, Integer limit, Integer offset) {
        OffsetBasedPageable pageable = new OffsetBasedPageable(limit, offset, Sort.of(Sort.Order.desc("createdAt")));
        return repository.findByFollowees(followees, pageable);
    }

    @Override
    public void delete(Organization organization) {
        repository.delete(organization);
    }

    @Override
    public List<Organization> getMembers(List<UUID> members, Integer limit, Integer offset) {
        OffsetBasedPageable pageable = new OffsetBasedPageable(limit, offset, Sort.of(Sort.Order.desc("createdAt")));
        // TODO fix
        return null;
    }

    @Override
    public Organization save(Organization organization) {
        if (repository.existsById(organization.getId())) {
            return repository.update(organization);
        }
        return repository.save(organization);
    }

    @RequiredArgsConstructor
    @Getter
    private static class OffsetBasedPageable implements Pageable {
        private final int size;
        private int number;
        private final long offset;
        private final Sort sort;
    }

}
